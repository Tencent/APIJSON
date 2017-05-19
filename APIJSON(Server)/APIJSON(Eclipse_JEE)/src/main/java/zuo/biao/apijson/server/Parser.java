/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson.server;

import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.POST_GET;
import static zuo.biao.apijson.RequestMethod.POST_HEAD;
import static zuo.biao.apijson.RequestMethod.PUT;
import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.AccessVerifier;
import apijson.demo.server.model.Request;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.ConditionNotMatchException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.OutOfRangeException;
import zuo.biao.apijson.server.sql.QueryConfig;
import zuo.biao.apijson.server.sql.QueryHelper;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public class Parser {
	private static final String TAG = "Parser";


	/**
	 * GET
	 */
	public Parser() {
		this(null);
	}
	/**
	 * @param requestMethod null ? requestMethod = GET
	 */
	public Parser(RequestMethod method) {
		this(method, false);
	}

	private final RequestMethod requestMethod;
	private final boolean noVerify;//方法免验证。但内容还是需要验证，避免服务端写错代码导致产生脏数据甚至清空Table
	/**
	 * @param requestMethod null ? requestMethod = GET
	 * @param noVerify 仅限于为服务端提供方法免验证特权，普通请求不要设置为true！ 如果对应Table有权限也建议用默认值false，保持和客户端权限一致
	 */
	public Parser(RequestMethod method, boolean noVerify) {
		super();
		this.requestMethod = method == null ? GET : method;
		this.noVerify = noVerify; 
	}


	private JSONObject requestObject;
	private QueryHelper queryHelper;
	private Map<String, Object> queryResultMap;//path-result


	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	public String parse(String request) {
		String response = JSON.toJSONString(parseResponse(request));

		Log.d(TAG, "parse  return response = \n" + response
				+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n\n");

		return response;
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	public String parse(JSONObject request) {
		return JSON.toJSONString(parseResponse(request));
	}

	/**解析请求json并获取对应结果
	 * @param request 先parseRequest中URLDecoder.decode(request, UTF_8);再parseResponse(getCorrectRequest(...))
	 * @return parseResponse(requestObject);
	 */
	public JSONObject parseResponse(String request) {
		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"
				+ requestMethod + "/parseResponse  request = \n" + request + "\n\n");

		try {
			requestObject = getCorrectRequest(requestMethod, parseRequest(request, requestMethod));
		} catch (Exception e) {
			return newErrorResult(e);
		}

		return parseResponse(requestObject);
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return requestObject
	 */
	public JSONObject parseResponse(JSONObject request) {
		final String requestString = JSON.toJSONString(request);//request传进去解析后已经变了

		queryResultMap = new HashMap<String, Object>();

		Exception error = null;
		queryHelper = new QueryHelper();
		try {
			requestObject = getObject(null, null, request);
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}
		queryHelper.close();
		queryHelper = null;


		if (noVerify == false) {
			requestObject = AccessVerifier.removeAccessInfo(requestObject);
		}
		requestObject = error == null ? extendSuccessResult(requestObject)
				: extendResult(requestObject, 206, "未完成全部请求：\n" + error.getMessage());


		queryResultMap.clear();

		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n "
				+ requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");

		return requestObject;
	}

	/**解析请求JSONObject
	 * @param request => URLDecoder.decode(request, UTF_8);
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject parseRequest(String request, RequestMethod method) throws Exception {
		request = URLDecoder.decode(request, UTF_8);
		if (method == null) {
			method = GET;
		}
		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n " + method
				+ "/parseResponse  request = \n" + request + "\n\n");
		return JSON.parseObject(request);
	}


	/**是否为GET请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法POST_GET
	 * @return
	 */
	public static boolean isGetMethod(RequestMethod method, boolean containPrivate) {
		boolean is = method == null || method == GET;
		return containPrivate == false ? is : is || method == POST_GET;
	}
	/**是否为HEAD请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法POST_HEAD
	 * @return
	 */
	public static boolean isHeadMethod(RequestMethod method, boolean containPrivate) {
		boolean is = method == HEAD;
		return containPrivate == false ? is : is || method == POST_HEAD;
	}
	/**是否为公开(明文，浏览器能直接访问)的请求方法
	 * @param method
	 * @return
	 */
	public static boolean isPublicMethod(RequestMethod method) {
		return method == null || method == GET || method == HEAD;
	}

	/**新建带状态内容的JSONObject
	 * @param status
	 * @param message
	 * @return
	 */
	public static JSONObject newResult(int status, String message) {
		return extendResult(null, status, message);
	}
	/**添加JSONObject的状态内容，一般用于错误提示结果
	 * @param object
	 * @param status
	 * @param message
	 * @return
	 */
	public static JSONObject extendResult(JSONObject object, int status, String message) {
		if (object == null) {
			object = new JSONObject(true);
		}
		object.put(JSONResponse.KEY_STATUS, status);
		object.put(JSONResponse.KEY_MESSAGE, message);
		return object;
	}


	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object) {
		return extendResult(object, 200, "success");
	}
	/**获取请求成功的状态内容
	 * @return
	 */
	public static JSONObject newSuccessResult() {
		return newResult(200, "success");
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Exception e) {
		JSONObject error = newErrorResult(e);
		return extendResult(object, error.getIntValue(JSONResponse.KEY_STATUS), error.getString(JSONResponse.KEY_MESSAGE));
	}
	/**新建错误状态内容
	 * @param e
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e) {
		if (e != null) {
			e.printStackTrace();

			int status = 404;
			if (e instanceof UnsupportedEncodingException) {
				status = 400;
			} else if (e instanceof IllegalAccessException) {
				status = 401;
			} else if (e instanceof UnsupportedOperationException) {
				status = 403;
			} else if (e instanceof IllegalArgumentException) {
				status = 406;
			} else if (e instanceof TimeoutException) {
				status = 408;
			} else if (e instanceof ConflictException) {
				status = 409;
			} else if (e instanceof ConditionNotMatchException) {
				status = 412;
			} else if (e instanceof OutOfRangeException) {
				status = 416;
			}

			return newResult(status, e.getMessage());
		}
		return newResult(500, "服务器内部错误");
	}





	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request) throws Exception {
		return getCorrectRequest(method, request, null);
	}
	//TODO 启动时一次性加载Request所有内容，作为初始化。
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @param queryHelper
	 * @return
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request, QueryHelper queryHelper)
			throws Exception {
		if (Parser.isPublicMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<<<
		QueryConfig config = new QueryConfig(GET, Request.class.getSimpleName());
		config.setColumn("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", method.name());
		where.put(JSONRequest.KEY_TAG, tag);
		config.setWhere(where);

		JSONObject object = null;
		String error = "";
		if (queryHelper == null) {
			queryHelper = new QueryHelper();
		}
		try {
			object = queryHelper.execute(config);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		}
		queryHelper.close();

		if (object == null) {//empty表示随意操作  || object.isEmpty()) {
			throw new UnsupportedOperationException("非GET请求必须是服务端允许的操作！ \n " + error);
		}
		object = Parser.getJSONObject(object, "structure");//解决返回值套了一层 "structure":{}

		JSONObject target = null;
		if (zuo.biao.apijson.JSONObject.isTableKey(tag) && object.containsKey(tag) == false) {//tag是table名
			target = new JSONObject(true);
			target.put(tag, object);
		} else {
			target = object;
		}
		//获取指定的JSON结构 >>>>>>>>>>>>>>

		request.remove(JSONRequest.KEY_TAG);
		return Structure.parseRequest(method, "", (JSONObject) target.clone(), request);
	}





	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getObject(String parentPath, String name, JSONObject request) throws Exception {
		return getObject(parentPath, name, request, null);
	}

	//	private QueryConfig itemConfig;
	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @param config for array item
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getObject(String parentPath, String name, final JSONObject request
			, final QueryConfig arrayConfig) throws Exception {
		Log.i(TAG, "\ngetObject:  parentPath = " + parentPath
				+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {// Moment:{}   || request.isEmpty()) {//key-value条件
			return null;
		}

		final int type = arrayConfig == null ? 0 : arrayConfig.getType();
		final boolean isArrayItem = type == ObjectParser.TYPE_ITEM;

		ObjectParser op = new ObjectParser(request, parentPath, type, name) {

			@Override
			public Object getTarget(@NotNull String path) {
				return getValueByPath(path);
			}

			@Override
			public JSONObject executeSQL(@NotNull String path, @NotNull QueryConfig config) throws Exception {
				JSONObject result = getSQLObject(config);
				if (result != null) {
					putQueryResult(path, result);//解决获取关联数据时requestObject里不存在需要的关联数据
				}
				return result;
			}

			com.alibaba.fastjson.JSON child;
			boolean isFirst = true;
			boolean isEmpty;
			@Override
			public com.alibaba.fastjson.JSON parseChild(@NotNull String path, @NotNull String key
					, @NotNull com.alibaba.fastjson.JSON value) throws Exception {
				if (zuo.biao.apijson.JSONObject.isArrayKey(key)) {//APIJSON Array
					child = getArray(path, key, (JSONObject) value);
					isEmpty = child == null || ((JSONArray) child).isEmpty();
				} else {//APIJSON Object
					child = getObject(path, key, (JSONObject) value
							, isFirst && isArrayItem ? arrayConfig.setType(TYPE_ITEM_CHILD_0) : null);
					isEmpty = child == null || ((JSONObject) child).isEmpty();
					if (isFirst && isEmpty) {
						invalidate();
					}

					isFirst = false;
				}
				Log.i(TAG, "getObject  ObjectParser.onParse  key = " + key + "; child = " + child);

				return isEmpty ? null : child;//只添加! isEmpty的值，可能数据库返回数据不够count
			}

			//			@Override
			//			protected QueryConfig newQueryConfig() {
			//				if (itemConfig != null) {
			//					return itemConfig;
			//				}
			//				return super.newQueryConfig();
			//			}

			//导致最多评论的(Strong 30个)的那个动态详情界面Android(82001)无姓名和头像，即User=null
			//			@Override
			//			protected void onComplete() {
			//				if (response != null) {
			//					putQueryResult(path, response);//解决获取关联数据时requestObject里不存在需要的关联数据
			//				}
			//			}

		}.setMethod(requestMethod).parse();



		JSONObject response = null;
		if (op != null) {//TODO SQL查询结果为空时，functionMap和customMap还有没有意义？
			if (arrayConfig == null) {//Common
				response = op.executeSQL().response();
			} else {//Array Item Child
				int query = arrayConfig.getQuery();

				//total
				if (type == ObjectParser.TYPE_ITEM_CHILD_0 && query != JSONRequest.QUERY_TABLE
						&& arrayConfig.getPosition() == 0) {
					JSONObject rp = op.setMethod(RequestMethod.HEAD).executeSQL().getSqlReponse();
					if (rp != null) {
						int index = parentPath.lastIndexOf("]/");
						if (index >= 0) {
							int total = rp.getIntValue(JSONResponse.KEY_COUNT);
							putQueryResult(parentPath.substring(0, index) + "]/" + JSONResponse.KEY_TOTAL, total);

							if (total <= arrayConfig.getCount()*arrayConfig.getPage()) {
								query = JSONRequest.QUERY_TOTAL;//数量不够了，不再往后查询
							}
						}
					}

					op.setMethod(requestMethod);
				}

				//Table
				if (query == JSONRequest.QUERY_TOTAL) {
					response = null;//不再往后查询
				} else {
					response = op.executeSQL(arrayConfig.getCount(), arrayConfig.getPage(), arrayConfig.getPosition())
							.response();
					//					itemConfig = op.getConfig();
				}
			}

			op.recycle();
			op = null;
		}

		return response;
	}

	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return 
	 * @throws Exception
	 */
	private JSONArray getArray(String parentPath, String name, final JSONObject request) throws Exception {
		Log.i(TAG, "\n\n\n getArray parentPath = " + parentPath
				+ "; name = " + name + "; request = " + JSON.toJSONString(request));
		if (isGetMethod(requestMethod, true) == false) {
			throw new UnsupportedOperationException("key[]:{}只支持GET类方法！不允许传 " + name + ":{} ！");
		}
		if (request == null || request.isEmpty()) {//jsonKey-jsonValue条件
			return null;
		}
		String path = getAbsPath(parentPath, name);

		//不能改变，因为后面可能继续用到，导致1以上都改变 []:{0:{Comment[]:{0:{Comment:{}},1:{...},...}},1:{...},...}
		final int query = request.getIntValue(JSONRequest.KEY_QUERY);
		final int count = request.getIntValue(JSONRequest.KEY_COUNT);
		final int page = request.getIntValue(JSONRequest.KEY_PAGE);
		request.remove(JSONRequest.KEY_QUERY);
		request.remove(JSONRequest.KEY_COUNT);
		request.remove(JSONRequest.KEY_PAGE);
		Log.d(TAG, "getArray  query = " + query + "; count = " + count + "; page = " + page);

		if (request.isEmpty()) {//如果条件成立，说明所有的 parentPath/name:request 中request都无效！！！
			Log.e(TAG, "getArray  request.isEmpty() >> return null;");
			return null;
		}


		//不用total限制数量了，只用中断机制，total只在query = 1,2的时候才获取
		int size = count <= 0 || count > 100 ? 100 : count;//count为每页数量，size为第page页实际数量，max(size) = count
		Log.d(TAG, "getArray  size = " + size + "; page = " + page);


		//Table<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONArray response = new JSONArray();
		JSONObject parent;
		QueryConfig config = new QueryConfig(requestMethod, size, page).setQuery(query);
		//生成size个
		for (int i = 0; i < size; i++) {
			parent = getObject(path, "" + i, request, config.setType(ObjectParser.TYPE_ITEM).setPosition(i));
			if (parent == null || parent.isEmpty()) {
				break;
			}
			response.add(parent);
		}
		//Table>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		//后面还可能用到，要还原
		request.put(JSONRequest.KEY_QUERY, query);
		request.put(JSONRequest.KEY_COUNT, count);
		request.put(JSONRequest.KEY_PAGE, page);

		Log.i(TAG, "getArray  return response = \n" + JSON.toJSONString(response) + "\n>>>>>>>>>>>>>>>\n\n\n");
		return response;
	}



	/**获取被依赖引用的key的路径, 实时替换[] -> []/i
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String getValuePath(String parentPath, String valuePath) {
		if (valuePath.startsWith("/")) {
			valuePath = getAbsPath(parentPath, valuePath);
		} else {//处理[] -> []/i
			valuePath = replaceArrayChildPath(parentPath, valuePath);
		}
		return valuePath;
	}

	/**获取绝对路径
	 * @param path
	 * @param name
	 * @return
	 */
	public static String getAbsPath(String path, String name) {
		Log.i(TAG, "getPath  path = " + path + "; name = " + name + " <<<<<<<<<<<<<");
		path = StringUtil.getString(path);
		name = StringUtil.getString(name);
		if (StringUtil.isNotEmpty(path, false)) {
			if (StringUtil.isNotEmpty(name, false)) {
				path += ((name.startsWith("/") ? "" : "/") + name);
			}
		} else {
			path = name;
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		Log.i(TAG, "getPath  return " + path + " >>>>>>>>>>>>>>>>");
		return path;
	}

	/**替换[] -> []/i
	 * 不能写在getAbsPath里，因为name不一定是依赖路径
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String replaceArrayChildPath(String parentPath, String valuePath) {
		String[] ps = StringUtil.split(parentPath, "]/");//"[]/");
		if (ps != null && ps.length > 1) {
			String[] vs = StringUtil.split(valuePath, "]/");

			if (vs != null && vs.length > 0) {
				String pos;
				for (int i = 0; i < ps.length - 1; i++) {
					if (ps[i] == null || ps[i].equals(vs[i]) == false) {//允许""？
						break;
					}

					pos = ps[i+1].contains("/") == false ? ps[i+1]
							: ps[i+1].substring(0, ps[i+1].indexOf("/"));
					if (
							//StringUtil.isNumer(pos) && 
							vs[i+1].startsWith(pos + "/") == false) {
						vs[i+1] = pos + "/" + vs[i+1];
					}
				}
				return StringUtil.getString(vs, "]/");
			}
		}
		return valuePath;
	}

	//依赖引用关系 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**将已获取完成的object的内容替换requestObject里对应的值
	 * @param path object的路径
	 * @param result 需要被关联的object
	 */
	private synchronized void putQueryResult(String path, Object result) {
		Log.i(TAG, "\n putQueryResult  valuePath = " + path + "; result = " + result + "\n <<<<<<<<<<<<<<<<<<<<<<<");
		//		if (queryResultMap.containsKey(valuePath)) {//只保存被关联的value
		Log.d(TAG, "putQueryResult  queryResultMap.containsKey(valuePath) >> queryResultMap.put(path, result);");
		queryResultMap.put(path, result);
		//		}
	}
	/**根据路径获取值
	 * @param valuePath
	 * @return parent == null ? valuePath : parent.get(keys[keys.length - 1])
	 */
	private Object getValueByPath(String valuePath) {
		Log.i(TAG, "<<<<<<<<<<<<<<< \n getValueByPath  valuePath = " + valuePath + "\n <<<<<<<<<<<<<<<<<<");
		if (StringUtil.isEmpty(valuePath, true)) {
			Log.e(TAG, "getValueByPath  StringUtil.isNotEmpty(valuePath, true) == false >> return null;");
			return null;
		}
		Object target = queryResultMap.get(valuePath);
		if (target != null) {
			return target;
		}

		//取出key被valuePath包含的result，再从里面获取key对应的value
		Set<String> set = queryResultMap.keySet();
		JSONObject parent = null;
		String[] keys = null;
		for (String path : set) {
			if (valuePath.startsWith(path)) {
				try {
					parent = (JSONObject) queryResultMap.get(path);
				} catch (Exception e) {
					Log.e(TAG, "getValueByPath  try { parent = (JSONObject) queryResultMap.get(path); } catch { "
							+ "\n parent not instanceof JSONObject!");
					parent = null;
				}
				if (parent != null) {
					keys = StringUtil.splitPath(valuePath.substring(path.length()));
				}
				break;
			}
		}

		//逐层到达targetKey的直接容器JSONObject parent
		if (keys != null && keys.length > 1) {
			for (int i = 0; i < keys.length - 1; i++) {//一步一步到达指定位置parentPath
				if (parent == null) {//不存在或路径错误(中间的key对应value不是JSONObject)
					break;
				}
				parent = getJSONObject(parent, keys[i]);
			}
		}

		System.out.println("getValueByPath  return parent == null ? valuePath : parent.get(keys[keys.length - 1]); >> ");
		return parent == null ? valuePath : parent.get(keys[keys.length - 1]);
	}

	//依赖引用关系 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	public static JSONObject getJSONObject(JSONObject object, String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			Log.i(TAG, "getJSONObject  try { return object.getJSONObject(key);"
					+ " } catch (Exception e) { \n"  + e.getMessage());
		}
		return null;
	}


	/**获取数据库返回的String
	 * @param config
	 * @return
	 * @throws Exception
	 */
	private synchronized JSONObject getSQLObject(QueryConfig config) throws Exception {
		Log.i(TAG, "getSQLObject  config = " + JSON.toJSONString(config));
		if (noVerify == false) {
			AccessVerifier.verify(requestObject, config);
		}
		return queryHelper.execute(config);
	}




	/**获取客户端实际需要的key
	 * <br> "userId@":"/User/id"      //@根据路径依赖，@始终在最后。value是'/'分隔的字符串。
	 * <br> "isPraised()":"isContain(Collection:idList,long:id)"  //()使用方法，value是方法表达式。不能与@并用。
	 * <br> "content$":"%searchKey%"  //$搜索，右边紧跟key。value是搜索表达式。
	 * <br> "@columns":"id,sex,name"  //关键字，左边紧跟key。暂时不用，因为目前关键字很少，几乎不会发生冲突。value是','分隔的字符串。
	 * 
	 * @param method
	 * @param originKey
	 * @param isTableKey
	 * @param saveLogic 保留逻辑运算符 & | !
	 * @return
	 */
	public static String getRealKey(RequestMethod method, String originKey, boolean isTableKey, boolean saveLogic)
			throws Exception {
		Log.i(TAG, "getRealKey  saveLogic = " + saveLogic + "; originKey = " + originKey);
		if (originKey == null || zuo.biao.apijson.JSONObject.isArrayKey(originKey)) {
			Log.w(TAG, "getRealKey  originKey == null || isArrayKey(originKey) >>  return originKey;");
			return originKey;
		}

		String key = new String(originKey);
		if (key.endsWith("$")) {//搜索，查询时处理
			key = key.substring(0, key.length() - 1);
		} else if (key.endsWith("{}")) {//被包含，或者说key对应值处于value的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} else if (key.endsWith("<>")) {//包含，或者说value处于key对应值的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} else if (key.endsWith("()")) {//方法，查询完后处理，先用一个Map<key,function>保存？
			key = key.substring(0, key.length() - 2);
		} else if (key.endsWith("@")) {//引用，引用对象查询完后处理。fillTarget中暂时不用处理，因为非GET请求都是由给定的id确定，不需要引用
			key = key.substring(0, key.length() - 1);
		} else if (key.endsWith("+")) {//延长，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.length() - 1);
			}
		} else if (key.endsWith("-")) {//缩减，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.length() - 1);
			}
		}

		String last = null;
		if (isGetMethod(method, true) || isHeadMethod(method, true)) {//逻辑运算符仅供GET,HEAD方法使用
			last = key.isEmpty() ? "" : key.substring(key.length() - 1);
			if ("&".equals(last) || "|".equals(last) || "!".equals(last)) {
				key = key.substring(0, key.length() - 1);
			} else {
				last = null;//避免key + StringUtil.getString(last)错误延长
			}
		}

		//"User:toUser":User转换"toUser":User, User为查询同名Table得到的JSONObject。交给客户端处理更好
		if (isTableKey) {//不允许在column key中使用Type:key形式
			key = Pair.parseEntry(key, true).getKey();//table以左边为准
		} else {
			key = Pair.parseEntry(key).getValue();//column以右边为准
		}

		if (zuo.biao.apijson.JSONObject.isWord(key.startsWith("@") ? key.substring(1) : key) == false) {
			throw new IllegalArgumentException(TAG + "/" + method + "  getRealKey: 字符 " + originKey + " 不合法！");
		}

		if (saveLogic) {
			key = key + StringUtil.getString(last);
		}
		Log.i(TAG, "getRealKey  return key = " + key);
		return key;
	}

}
