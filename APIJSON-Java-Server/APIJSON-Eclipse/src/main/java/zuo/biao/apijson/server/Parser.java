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
import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.AccessVerifier;
import apijson.demo.server.model.User;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.ConditionErrorException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotLoggedInException;
import zuo.biao.apijson.server.exception.OutOfRangeException;
import zuo.biao.apijson.server.sql.SQLConfig;
import zuo.biao.apijson.server.sql.SQLExecutor;

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
	/**
	 * @param requestMethod null ? requestMethod = GET
	 * @param noVerify 仅限于为服务端提供方法免验证特权，普通请求不要设置为true！ 如果对应Table有权限也建议用默认值false，保持和客户端权限一致
	 */
	public Parser(RequestMethod method, boolean noVerify) {
		super();
		this.requestMethod = method == null ? GET : method;
		setNoVerifyRequest(noVerify);
		setNoVerifyLogin(noVerify);
		setNoVerifyRole(noVerify);
	}

	private HttpSession session;//可能比较大，占内存。而且不是所有地方都用
	private User visitor;//来访用户
	private long visitorId;//来访用户id
	public Parser setSession(@NotNull HttpSession session) {
		this.session = session;
		this.visitor = AccessVerifier.getUser(session);
		this.visitorId = AccessVerifier.getUserId(session);
		return this;
	}
	private RequestRole globleRole;//全局角色，对未指明角色的Table自动加上这个角色
	public Parser setGlobleRole(RequestRole globleRole) {
		this.globleRole = globleRole;
		return this;
	}

	//一定要验证结构！对管理员也要验证！
	private boolean noVerifyRequest = false;
	private boolean noVerifyLogin = false;
	private boolean noVerifyRole = false;
	public Parser setNoVerifyRequest(boolean noVerifyRequest) {
		this.noVerifyRequest = noVerifyRequest;
		return this;
	}
	public Parser setNoVerifyLogin(boolean noVerifyLogin) {
		this.noVerifyLogin = noVerifyLogin;
		return this;
	}
	public Parser setNoVerifyRole(boolean noVerifyRole) {
		this.noVerifyRole = noVerifyRole;
		return this;
	}






	private JSONObject requestObject;
	private SQLExecutor sQLExecutor;
	private Map<String, Object> queryResultMap;//path-result


	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	public String parse(String request) {
		return JSON.toJSONString(parseResponse(request));
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	@NotNull
	public String parse(JSONObject request) {
		return JSON.toJSONString(parseResponse(request));
	}

	/**解析请求json并获取对应结果
	 * @param request 先parseRequest中URLDecoder.decode(request, UTF_8);再parseResponse(getCorrectRequest(...))
	 * @return parseResponse(requestObject);
	 */
	@NotNull
	public JSONObject parseResponse(String request) {
		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"
				+ requestMethod + "/parseResponse  request = \n" + request + "\n\n");

		try {
			requestObject = parseRequest(request, requestMethod);
		} catch (Exception e) {
			return newErrorResult(e);
		}

		return parseResponse(requestObject);
	}

	/**解析请求json并获取对应结果
	 * @param request
	 * @return requestObject
	 */
	@NotNull
	public JSONObject parseResponse(JSONObject request) {
		long startTime = System.currentTimeMillis();
		Log.d(TAG, "parseResponse  startTime = " + startTime
				+ "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n\n\n ");

		requestObject = request;
		if (RequestMethod.isPublicMethod(requestMethod) == false) {
			try {
				//TODO
				//				if (noVerifyLogin == false) {
				//					Verifier.verifyLogin(session);
				//				}
				if (noVerifyRequest == false) {
					requestObject = getCorrectRequest(requestMethod, requestObject);
				}
			} catch (Exception e) {
				return Parser.extendErrorResult(requestObject, e);
			}
		}

		if (noVerifyRole == false && globleRole == null) {
			setGlobleRole(RequestRole.get(requestObject.getString(JSONRequest.KEY_ROLE)));
		}

		final String requestString = JSON.toJSONString(request);//request传进去解析后已经变了


		queryResultMap = new HashMap<String, Object>();

		Exception error = null;
		sQLExecutor = new SQLExecutor();
		try {
			requestObject = getObject(null, null, request);
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}
		sQLExecutor.close();
		sQLExecutor = null;


		requestObject = AccessVerifier.removeAccessInfo(requestObject);
		requestObject = error == null ? extendSuccessResult(requestObject) : extendErrorResult(requestObject, error);


		queryResultMap.clear();

		//会不会导致原来的session = null？		session = null;


		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n "
				+ requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");

		Log.d(TAG, "parse  return response = \n" + JSON.toJSONString(requestObject)
		+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n\n");

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "parseResponse  endTime = " + endTime + ";  duration = " + (endTime - startTime)
				+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");

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





	/**新建带状态内容的JSONObject
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject newResult(int code, String msg) {
		return extendResult(null, code, msg);
	}
	/**添加JSONObject的状态内容，一般用于错误提示结果
	 * @param object
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject extendResult(JSONObject object, int code, String msg) {
		if (object == null) {
			object = new JSONObject(true);
		}
		if (object.containsKey(JSONResponse.KEY_CODE) == false) {
			object.put(JSONResponse.KEY_CODE, code);
		}
		String m = StringUtil.getString(object.getString(JSONResponse.KEY_MSG));
		if (m.isEmpty() == false) {
			msg = m + " \n " + StringUtil.getString(msg);
		}
		object.put(JSONResponse.KEY_MSG, msg);
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
		return extendResult(object, error.getIntValue(JSONResponse.KEY_CODE), error.getString(JSONResponse.KEY_MSG));
	}
	/**新建错误状态内容
	 * @param e
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e) {
		if (e != null) {
			e.printStackTrace();

			int code = JSONResponse.CODE_NOT_FOUND;
			if (e instanceof UnsupportedEncodingException) {
				code = JSONResponse.CODE_UNSUPPORTED_ENCODING;
			} 
			else if (e instanceof IllegalAccessException) {
				code = JSONResponse.CODE_ILLEGAL_ACCESS;
			}
			else if (e instanceof UnsupportedOperationException) {
				code = JSONResponse.CODE_UNSUPPORTED_OPERATION;
			}
			else if (e instanceof IllegalArgumentException) {
				code = JSONResponse.CODE_ILLEGAL_ARGUMENT;
			}
			else if (e instanceof NotLoggedInException) {
				code = JSONResponse.CODE_NOT_LOGGED_IN;
			}
			else if (e instanceof TimeoutException) {
				code = JSONResponse.CODE_TIME_OUT;
			} 
			else if (e instanceof ConflictException) {
				code = JSONResponse.CODE_CONFLICT;
			}
			else if (e instanceof ConditionErrorException) {
				code = JSONResponse.CODE_CONDITION_ERROR;
			}
			else if (e instanceof UnsupportedDataTypeException) {
				code = JSONResponse.CODE_UNSUPPORTED_TYPE;
			}
			else if (e instanceof OutOfRangeException) {
				code = JSONResponse.CODE_OUT_OF_RANGE;
			}
			else if (e instanceof NullPointerException) {
				code = JSONResponse.CODE_NULL_POINTER;
			}

			return newResult(code, e.getMessage());
		}

		return newResult(500, "服务器内部错误");
	}




	//TODO 启动时一次性加载Request所有内容，作为初始化。
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getCorrectRequest(@NotNull RequestMethod method, JSONObject request) throws Exception {
		if (RequestMethod.isPublicMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		JSONObject object = null;
		String error = "";
		try {
			object = getStructure(method, "Request", JSONRequest.KEY_TAG, tag);
		} catch (Exception e) {
			error = e.getMessage();
		}
		if (object == null) {//empty表示随意操作  || object.isEmpty()) {
			throw new UnsupportedOperationException("非开放请求必须是服务端允许的操作！ \n " + error);
		}

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

	//TODO 优化性能！
	/**获取正确的返回结果
	 * @param method
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getCorrectResponse(@NotNull final RequestMethod method
			, String table, JSONObject response) throws Exception {
		//		Log.d(TAG, "getCorrectResponse  method = " + method + "; table = " + table);
		//		if (response == null || response.isEmpty()) {//避免无效空result:{}添加内容后变有效
		//			Log.e(TAG, "getCorrectResponse  response == null || response.isEmpty() >> return response;");
		return response;
		//		}
		//
		//		JSONObject target = zuo.biao.apijson.JSONObject.isTableKey(table) == false
		//				? new JSONObject() : getStructure(method, "Response", "model", table);
		//
		//				return MethodStructure.parseResponse(method, table, target, response, new OnParseCallback() {
		//
		//					@Override
		//					protected JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
		//						return getCorrectResponse(method, key, robj);
		//					}
		//				});
	}

	/**获取Request或Response内指定JSON结构
	 * @param method
	 * @param table
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getStructure(@NotNull final RequestMethod method, @NotNull String table,
			String key, String value) throws Exception  {
		//获取指定的JSON结构 <<<<<<<<<<<<<<
		SQLConfig config = new SQLConfig(GET, table);
		config.setColumn("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", method.name());
		if (key != null) {
			where.put(key, value);
		}
		config.setWhere(where);

		SQLExecutor qh = new SQLExecutor();

		//too many connections error: 不try-catch，可以让客户端看到是服务器内部异常
		try {
			JSONObject result = qh.execute(config.setCacheStatic(true));
			return getJSONObject(result, "structure");//解决返回值套了一层 "structure":{}
		} finally {
			qh.close();
		}
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

	//	private SQLConfig itemConfig;
	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @param config for array item
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getObject(String parentPath, String name, final JSONObject request
			, final SQLConfig arrayConfig) throws Exception {
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
			public JSONObject executeSQL(@NotNull String path, @NotNull SQLConfig config) throws Exception {
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

			@Override
			public JSONObject parseResponse(JSONRequest request) throws Exception {
				return new Parser(GET)
						.setSession(session)
						//						.setNoVerifyRequest(noVerifyRequest)
						.setNoVerifyLogin(noVerifyLogin)
						.setNoVerifyRole(noVerifyRole)
						.parseResponse(request);
			}

			//			@Override
			//			protected SQLConfig newQueryConfig() {
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
					response = op.executeSQL(
							arrayConfig.getCount(), arrayConfig.getPage(), arrayConfig.getPosition()
							).response();
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
		if (RequestMethod.isGetMethod(requestMethod, true) == false) {
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


		//key[]:{Table:{}}中key equals Table时 提取Table
		boolean isContainer = true;
		int index = name == null ? -1 : name.lastIndexOf("[]");
		String table = index <= 0 ? null : Pair.parseEntry(name.substring(0, index), true).getKey();
		if (JSONRequest.isTableKey(table) && request.containsKey(table)) {
			isContainer = false;
		}


		//Table<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONArray response = new JSONArray();
		JSONObject parent;
		SQLConfig config = new SQLConfig(requestMethod, size, page).setQuery(query);
		//生成size个
		for (int i = 0; i < size; i++) {
			parent = getObject(path, "" + i, request, config.setType(ObjectParser.TYPE_ITEM).setPosition(i));
			if (parent == null || parent.isEmpty()) {
				break;
			}
			//key[]:{Table:{}}中key equals Table时 提取Table
			response.add(isContainer ? parent : parent.getJSONObject(table) );
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

		Log.i(TAG, "getValueByPath  return parent == null ? valuePath : parent.get(keys[keys.length - 1]); >> ");
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
	private synchronized JSONObject getSQLObject(SQLConfig config) throws Exception {
		Log.i(TAG, "getSQLObject  config = " + JSON.toJSONString(config));
		if (noVerifyRole == false) {
			if (config.getRole() == null) {
				if (globleRole != null) {
					config.setRole(globleRole);
				} else {
					config.setRole(visitorId <= 0 ? RequestRole.UNKNOWN : RequestRole.LOGIN);
				}
			}
			AccessVerifier.verify(config, visitor);
		}
		return getCorrectResponse(requestMethod, config.getTable(), sQLExecutor.execute(config));
	}


}
