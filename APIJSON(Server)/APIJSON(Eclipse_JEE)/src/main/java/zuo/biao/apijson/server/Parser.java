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
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.POST_GET;
import static zuo.biao.apijson.RequestMethod.POST_HEAD;
import static zuo.biao.apijson.RequestMethod.PUT;
import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.ConditionNotMatchException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.sql.AccessVerifier;
import zuo.biao.apijson.server.sql.QueryConfig;
import zuo.biao.apijson.server.sql.QueryHelper;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public class Parser {
	private static final String TAG = "Parser";

	public static final String SEPARATOR = StringUtil.SEPARATOR;

	private RequestMethod requestMethod;
	/**
	 * GET
	 */
	public Parser() {
		this(null);
	}
	/**
	 * @param requestMethod null ? requestMethod = GET
	 */
	public Parser(RequestMethod requestMethod) {
		super();
		if (requestMethod == null) {
			requestMethod = GET;
		}
		this.requestMethod = requestMethod;
	}


	private JSONObject requestObject;
	private QueryHelper queryHelper;
	private Map<String, Object> queryResultMap;//path-result

	private boolean parseRelation;
	//不用keyPath-valuePath-value是因为很可能很多valuePath对应同一个value
	private Map<String, String> keyValuePathMap;//keyPath-valuePath


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

		keyValuePathMap = new HashMap<String, String>();
		queryResultMap = new HashMap<String, Object>();
		parseRelation = false;

		Exception error = null;
		queryHelper = new QueryHelper();
		try {
			requestObject = getObject(null, null, null, request);

			if (keyValuePathMap.isEmpty() == false) {//优化性能，没有依赖引用的就不用再遍历了
				Log.d(TAG, "\n\n\n parseResponse  keyValuePathMap.isEmpty() == false"
						+ "\n parseRelation = true; <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n\n");

				parseRelation = true;
				requestObject = getObject(null, null, null, requestObject);

				Log.d(TAG, "\n\n parseRelation = true; >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}
		queryHelper.close();
		queryHelper = null;
		//		QueryHelper2.getInstance().close();


		requestObject = AccessVerifier.removeAccessInfo(requestObject);
		//		if (isGetMethod(requestMethod) || requestMethod == POST_GET) {//分情况把我都搞晕了@_@
		requestObject = error == null ? extendSuccessResult(requestObject)
				: extendResult(requestObject, 206, "未完成全部请求：\n" + error.getMessage());
		//		}

		keyValuePathMap.clear();;
		queryResultMap.clear();

		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n "
				+ requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");

		return requestObject;
	}

	/**解析请求JSONObject
	 * @param request => URLDecoder.decode(request, UTF_8);
	 * @return
	 */
	public static JSONObject parseRequest(String request, RequestMethod method) {
		try {
			request = URLDecoder.decode(request, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return newErrorResult(e);
		}
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
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @param queryHelper
	 * @return
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request, QueryHelper queryHelper)
			throws Exception {
		if (isPublicMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<<<
		QueryConfig config = new QueryConfig(GET, "Request");
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
			object = queryHelper.select(config);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		}
		queryHelper.close();

		if (object == null || object.isEmpty()) {
			throw new UnsupportedOperationException("非GET请求必须是服务端允许的操作！ \n " + error);
		}
		object = getJSONObject(object, "structure");//解决返回值套了一层 "structure":{}

		JSONObject target = null;
		if (isTableKey(tag) && object.containsKey(tag) == false) {//tag是table名
			target = new JSONObject(true);
			target.put(tag, object);
		} else {
			target = object;
		}
		//获取指定的JSON结构 >>>>>>>>>>>>>>

		request.remove(JSONRequest.KEY_TAG);
		return fillTarget(method, target, request, "");
	}


	public static final String NECESSARY_COLUMNS = "necessaryColumns";
	public static final String DISALLOW_COLUMNS = "disallowColumns";

	/**从request提取target指定的内容
	 * @param target
	 * @param request
	 * @return
	 */
	public static JSONObject fillTarget(RequestMethod method
			, JSONObject target, final JSONObject request, String requestName) throws Exception {
		Log.i(TAG, "filterTarget  requestName = " + requestName
				+ " target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request) + "\n >> return null;");
		if (target == null || request == null) {// || request.isEmpty()) {
			Log.i(TAG, "filterTarget  target == null || request == null >> return null;");
			return null;
		}

		/**方法三：遍历request，transferredRequest只添加target所包含的object
		 *  ，且移除target中DISALLOW_COLUMNS，期间判断NECESSARY_COLUMNS是否都有
		 */
		String necessarys = StringUtil.getNoBlankString(target.getString(NECESSARY_COLUMNS));
		String[] necessaryColumns = StringUtil.split(necessarys);

		//判断必要字段是否都有
		if (necessaryColumns != null) {
			for (String s : necessaryColumns) {
				if (request.containsKey(s) == false) {
					throw new IllegalArgumentException(requestName
							+ "不能缺少 " + s + " 等[" + necessarys + "]内的任何字段！");
				}
			}
		}

		String disallows = StringUtil.getNoBlankString(target.getString(DISALLOW_COLUMNS));
		String[] disallowColumns = null;

		Set<String> set = request.keySet();
		if ("!".equals(disallows)) {//所有非necessaryColumns，改成 !necessary 更好
			if (set != null) {
				List<String> disallowList = new ArrayList<String>();
				for (String key : set) {
					if (isContainKeyInArray(key, necessaryColumns) == false) {
						disallowList.add(key);
					}
				}
				disallowColumns = disallowList.toArray(new String[]{});
			}
		} else {
			disallowColumns = StringUtil.split(disallows);
		}


		//填充target
		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			Object value;
			JSONObject result;
			for (String key : set) {
				value = request.get(key);
				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					if (target.containsKey(key)) {//只填充target有的object
						result = fillTarget(method, getJSONObject(target, key), (JSONObject) value, key);//往下一级提取
						Log.i(TAG, "fillTarget  key = " + key + "; result = " + result);
						if (result == null || result.isEmpty()) {//只添加!=null的值，可能数据库返回数据不够count
							throw new IllegalArgumentException(requestName
									+ "不能缺少 " + key + " 等[" + necessarys + "]内的任何JSONObject！");
						}
						if (method == POST && result.containsKey(QueryConfig.ID) == false) {//为注册用户返回id
							result.put(QueryConfig.ID, System.currentTimeMillis());
						}
						transferredRequest.put(key, result);
					}
				} else {//JSONArray或其它Object
					if (isContainKeyInArray(key, disallowColumns)) {
						throw new IllegalArgumentException(requestName
								+ "不允许传 " + key + " 等[" + disallows + "]内的任何字段！");
					}
					transferredRequest.put(key, value);
				}
			}
		}

		Log.i(TAG, "filterTarget  return transferredRequest = " + JSON.toJSONString(transferredRequest));
		return transferredRequest;
	}

	/**array至少有一个值在request的key中
	 * @param key
	 * @param array
	 * @return
	 */
	public static boolean isContainKeyInArray(String key, String[] array) {
		if (array == null || array.length <= 0 || key == null) {
			Log.i(TAG, "isContainKeyInArray"
					+ "  array == null || array.length <= 0 || key == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (key.equals(s)) {
				return true;
			}
		}

		return false;
	}





	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param parentConfig 对子object的SQL查询配置，需要传两个层级
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getObject(String parentPath, final QueryConfig parentConfig, String name
			, final JSONObject request) throws Exception {
		Log.i(TAG, "\ngetObject:  parentPath = " + parentPath
				+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {// Moment:{} || request.isEmpty()) {//key-value条件
			return null;
		}
		final String path = getAbsPath(parentPath, name);
		if (parseRelation //优化性能：第二遍遍历时如果内部没有依赖引用其它就跳过
				&& StringUtil.isNotEmpty(path, true)//避免最外层返回true而不能parseRelation
				&& isInRelationMap(path) == false) {
			return request;
		}
		//为第二遍parseRelation = true服务，优化查询性能并避免"[]":{"0":{"1":{}}}这种导致第3层当成[]的直接子Object
		final boolean isArrayChild = parentConfig != null && StringUtil.isNumer(name) && ("" + parentConfig.getPosition()).equals(name);
		final String table = isArrayChild ? null : Pair.parseEntry(name, true).getKey();
		final boolean isTableKey = isArrayChild == false && isTableKey(table);
		Log.d(TAG, "getObject  table = " + table + "; isTableKey = " + isTableKey);


		boolean containRelation = false;

		JSONObject transferredRequest = new JSONObject(true);//must init
		Map<String, String> functionMap = null;
		Map<String, Object> selfDefineKeyMap = null;

		Set<String> set = new LinkedHashSet<>(request.keySet());
		if (set != null && set.size() > 0) {//判断换取少几个变量的初始化是否值得？
			functionMap = new LinkedHashMap<String, String>();
			selfDefineKeyMap = new LinkedHashMap<String, Object>();

			Object value;
			JSONObject result;
			boolean isFirst = true;
			for (String key : set) {
				value = request.get(key);
				if (value == null) {
					continue;
				}

				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					if (isArrayKey(key)) {//APIJSON Array
						result = getArray(path, parentConfig, key, (JSONObject) value);
					} else {//APIJSON Object
						result = getObject(path, isFirst && isArrayChild //以第0个JSONObject为准
								? parentConfig : null, key, (JSONObject) value);
						
						//如果第0个都为空，那后面的也都无意义了。
						if (isFirst && isArrayChild && (result == null || result.isEmpty())) {
							Log.d(TAG, "getObject  isFirst && isArrayChild"
									+ " && (result == null || result.isEmpty()) >> return null;");
							return null;
						}
						
						isFirst = false;//[]里第一个不能为[]
					}
					Log.i(TAG, "getObject  key = " + key + "; result = " + result);
					if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
						transferredRequest.put(key, result);
					}
				} else if (requestMethod == PUT && JSON.isJSONArray(value)) {//PUT JSONArray
					JSONArray array = ((JSONArray) value);
					if (array != null && array.isEmpty() == false && isTableKey(table)) {
						int putType = 0;
						if (key.endsWith("+")) {//add
							putType = 1;
						} else if (key.endsWith("-")) {//remove
							putType = 2;
						} else {//replace
							throw new IllegalAccessException("PUT " + path + ", PUT Array不允许 " + key + 
									" 这种没有 + 或 - 结尾的key！不允许整个替换掉原来的Array！");
						}
						String realKey = getRealKey(requestMethod, key, false, false);

						//GET > add all 或 remove all > PUT > remove key

						//GET <<<<<<<<<<<<<<<<<<<<<<<<<
						JSONObject arrayRequest = new JSONObject();
						arrayRequest.put(QueryConfig.ID, request.get(QueryConfig.ID));
						arrayRequest.put(JSONRequest.KEY_COLUMN, realKey);
						JSONRequest getRequest = new JSONRequest(table, arrayRequest);
						JSONObject response = new Parser().parseResponse(getRequest);
						//GET >>>>>>>>>>>>>>>>>>>>>>>>>


						//add all 或 remove all <<<<<<<<<<<<<<<<<<<<<<<<<
						response = response == null ? null : response.getJSONObject(table);
						JSONArray targetArray = response == null ? null : response.getJSONArray(realKey);
						if (targetArray == null) {
							targetArray = new JSONArray();
						}
						for (Object obj : array) {
							if (obj == null) {
								continue;
							}
							if (putType == 1) {
								if (targetArray.contains(obj)) {
									throw new ConflictException("PUT " + path + ", " + realKey + ":" + obj + " 已存在！");
								}
								targetArray.add(obj);
							} else if (putType == 2) {
								if (targetArray.contains(obj) == false) {
									throw new NullPointerException("PUT " + path + ", " + realKey + ":" + obj + " 不存在！");
								}
								targetArray.remove(obj);
							}
						}

						//add all 或 remove all >>>>>>>>>>>>>>>>>>>>>>>>>

						//PUT <<<<<<<<<<<<<<<<<<<<<<<<<
						transferredRequest.put(realKey, targetArray);
						//PUT >>>>>>>>>>>>>>>>>>>>>>>>>
					}
				} else {//JSONArray或其它Object，直接填充
					if (key.endsWith("@")) {//StringUtil.isPath((String) value)) {
						if (value instanceof String == false) {
							throw new IllegalArgumentException("\"key@\": 后面必须为依赖路径String！");
						}
						System.out.println("getObject  key.endsWith(@) >> parseRelation = " + parseRelation);
						String replaceKey = key.substring(0, key.length() - 1);//key{}@ getRealKey
						String keyPath = getAbsPath(path, replaceKey);
						String valuePath = parseRelation ? getRelationValuePath(keyPath) : new String((String) value);

						if (valuePath.startsWith(SEPARATOR)) {
							valuePath = getAbsPath(parentPath, valuePath);
						}
						//先尝试获取，尽量保留缺省依赖路径，这样就不需要担心路径改变
						Object target = getValueByPath(valuePath, true);
						Log.i(TAG, "getObject valuePath = " + valuePath + "; target = " + target);

						if (valuePath.equals(target)) {//必须valuePath和保证getValueByPath传进去的一致！
							Log.i(TAG, "getObject  target != null && target instanceof String"
									+ " && ((String) target).startsWith(valuePath)  >>  ");
							if (parseRelation) {
								//非查询关键词 @key 不影响查询，直接跳过
								if (isTableKey && (key.startsWith("@") == false || QueryConfig.TABLE_KEY_LIST.contains(key))) {
									Log.e(TAG, "getObject parseRelation >> isTableKey && (key.startsWith(@) == false"
											+ " || QueryConfig.TABLE_KEY_LIST.contains(key)) >>  return null;");
									return null;//parseRelation时还获取不到就不用再做无效的query了。不考虑 Table:{Table:{}}嵌套
								} else {
									Log.d(TAG, "getObject parseRelation >> isTableKey(table) == false >> continue;");
									continue;//舍去，对Table无影响
								}
							} else {//标记并存放依赖关系
								Log.i(TAG, "getObject parseRelation == false"
										+ " >>  containRelation = true; putRelation(keyPath, valuePath);");
								containRelation = true;
								putRelation(keyPath, valuePath);
							}
						} else {//直接替换原来的key@:path为key:target
							Log.i(TAG, "getObject    >>  key = replaceKey; value = target;");
							key = replaceKey;
							value = target;
						}
						Log.d(TAG, "getObject key = " + key + "; value = " + value);
					}

					if (key.endsWith("()")) {
						if (value instanceof String == false) {
							throw new IllegalArgumentException(path + "/" + key + "():function() 后面必须为函数String！");
						}
						functionMap.put(key, (String) value);
					} else if (key.startsWith("@") && QueryConfig.TABLE_KEY_LIST.contains(key) == false) {
						selfDefineKeyMap.put(key, value);
					} else {
						transferredRequest.put(key, value);
					}
				}
			}
		}


		boolean query = false;
		//执行SQL操作数据库
		if (containRelation == false && isTableKey) {//提高性能
			if (parseRelation == false || isInRelationMap(path)) {//避免覆盖原来已经获取的
				query = true;
				//			keyValuePathMap.remove(path);
				//移除所有startswith path的keyPath？
				QueryConfig config = newQueryConfig(table, transferredRequest);

				if (parentConfig == null) {//导致全部都是第0个 || isArrayChild == false) {
					config.setCount(1);
				} else {
					config.setCount(parentConfig.getCount()).setPage(parentConfig.getPage())
					.setPosition(parentConfig.getPosition());//避免position > 0的object获取不到
				}

				try {
					transferredRequest = getSQLObject(config);
				} catch (Exception e) {
					Log.e(TAG, "getObject  try { transferredRequest = getSQLObject(config2); } catch (Exception e) {");
					if (e instanceof NotExistException) {//非严重异常，有时候只是数据不存在
						e.printStackTrace();
						transferredRequest = null;//内部吃掉异常，put到最外层
						//						requestObject.put(JSONResponse.KEY_MESSAGE
						//								, StringUtil.getString(requestObject.get(JSONResponse.KEY_MESSAGE)
						//										+ "; query " + path + " cath NotExistException:"
						//										+ newErrorResult(e).getString(JSONResponse.KEY_MESSAGE)));
					} else {
						throw e;
					}
				}

				if (transferredRequest == null) {
					transferredRequest = new JSONObject(true);
				}
			}
		}

		if (selfDefineKeyMap != null) {
			transferredRequest.putAll(selfDefineKeyMap);
		}
		if (functionMap != null) {
			if (query) {
				//解析函数function
				Set<String> functionSet = functionMap == null ? null : functionMap.keySet();
				if (functionSet != null && functionSet.isEmpty() == false) {
					for (String key : functionSet) {
						transferredRequest.put(getRealKey(requestMethod, key, false, false)
								, Function.invoke(transferredRequest, functionMap.get(key)));
					}
				}
			} else {
				transferredRequest.putAll(functionMap);
			}
		}

		if (query) {
			putQueryResult(path, transferredRequest);//解决获取关联数据时requestObject里不存在需要的关联数据
		}

		return transferredRequest;
	}



	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param parentConfig 对子object的SQL查询配置，需要传两个层级
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return 转为JSONArray不可行，因为会和被当成条件的key:JSONArray冲突。好像一般也就key{}:JSONArray用到??
	 * @throws Exception 
	 */
	private JSONObject getArray(String parentPath, QueryConfig parentConfig, String name
			, final JSONObject request) throws Exception {
		Log.i(TAG, "\n\n\n getArray parentPath = " + parentPath
				+ "; name = " + name + "; request = " + JSON.toJSONString(request));
		if (isHeadMethod(requestMethod, true)) {
			throw new UnsupportedOperationException("HEAD、POST_HEAD方法不允许重复查询！不应该传 " + name + " 等key[]:{}！");
		}
		if (request == null || request.isEmpty()) {//jsonKey-jsonValue条件
			return null;
		}
		String path = getAbsPath(parentPath, name);

		int count = 0, page = 0, total = 0;

		count = request.getIntValue(JSONRequest.KEY_COUNT);
		page = request.getIntValue(JSONRequest.KEY_PAGE);
		request.remove(JSONRequest.KEY_COUNT);
		request.remove(JSONRequest.KEY_PAGE);

		if (count <= 0 || count > 100) {//count最大为100
			count = 100;
		}

		//最好先获取第一个table的所有项（where条件），填充一个列表？
		Set<String> set = new LinkedHashSet<>(request.keySet());
		if (count <= 0 || count > 5) {//5以下不优化长度
			if(parseRelation == false && set != null) {
				String table;
				Object value;
				for (String key : set) {
					table = Pair.parseEntry(key, true).getKey();
					value = isTableKey(table) ? request.get(key) : null;
					if (value != null && value instanceof JSONObject) {// && value.isEmpty() == false) {
						total = estimateMaxCount(path, table, (JSONObject) value);
						break;
					}
				}
				Log.d(TAG, "total = " + total);
				if (total <= 0) {//request内部没有JSONObject或者不存在适合条件的table内容
					return null;
				}
				putQueryResult(path + "/" + JSONResponse.KEY_TOTAL, total);
				if (count > total) {
					count = total;
				}
			}
		}
		Log.i(TAG, "getArray page = " + page + "; count = " + count);

		QueryConfig config = new QueryConfig(requestMethod, count, page);

		
		JSONObject transferredRequest = new JSONObject(true);

		JSONObject parent = null;
		Object value;
		JSONObject result;
		Log.d(TAG, "getArray  parseRelation = " + parseRelation);
		if (parseRelation == false) {
			//生成count个
			for (int i = 0; i < count; i++) {
				parent = getObject(path, config.setPosition(i), "" + i, request);
				if (parent == null || parent.isEmpty()) {
					break;//数据库返回数量不够count，后面没有了。有依赖不为空，无依赖直接查询数据库。
				}
				transferredRequest.put("" + i, parent);
				updateRelation(path, getAbsPath(path, "" + i));//request结构已改变，需要更新依赖关系
			}

			if (isInRelationMap(path)) {
				transferredRequest.put(JSONRequest.KEY_COUNT, count);
				transferredRequest.put(JSONRequest.KEY_PAGE, page);
			}
		} else {
			boolean isArrayKey;
			for (String key : set) {//0:{},1:{}...
				value = request.get(key);
				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					config.setPosition(Integer.valueOf(0 + StringUtil.getNumber(key, true)));
					isArrayKey = isArrayKey(key);
					if (isArrayKey) {//json array
						result = getArray(path, config, key, (JSONObject) value);
					} else {//json object
						result = getObject(path, config, key, (JSONObject) value);
					}
					if (result == null || result.isEmpty()) {
						if (isArrayKey) {
							continue;
						} else {
							break;//数据库返回数量不够count，后面没有了。有依赖不为空，无依赖直接查询数据库。
						}
					}
					transferredRequest.put(key, result);
				} else {//JSONArray或其它Object
					//array里不允许关联，只能在object中关联
				}
			}
		}

		Log.i(TAG, "getArray  return " + JSON.toJSONString(transferredRequest) + "\n>>>>>>>>>>>>>>>\n\n\n");

		return transferredRequest;
	}

	/**估计最大总数，去掉value中所有依赖引用.
	 * TODO 返回一个{"total":10, name:value}更好，省去了之后的parseRelation
	 * @param path
	 * @param name
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public int estimateMaxCount(String path, String table, JSONObject value) throws Exception {
		if (StringUtil.isNotEmpty(table, true) == false) {
			Log.e(TAG, "estimateMaxCount  StringUtil.isNotEmpty(table, true) == false >> return 0;");
			return 0;
		}

		JSONObject request = new JSONObject(true);
		Set<Entry<String, Object>> entrySet = value == null ? null : new LinkedHashSet<>(value.entrySet());
		if (entrySet != null && entrySet.isEmpty() == false) {
			String k;
			Object v;
			Object target;
			String valid;
			for (Entry<String, Object> entry : entrySet) {
				k = entry == null ? "" : StringUtil.getString(entry.getKey());
				if (k.isEmpty() == false) {
					v = entry.getValue();
					if (k.endsWith("@")) {
						if (v == null) {
							continue;
						}
						target = getValueByPath(getAbsPath(
								((String) v).startsWith(SEPARATOR) ? path : "", (String) v), true);
						if (target != null && ((String) v).equals(target) == false) {
							k = k.substring(0, k.length() - 1);
							v = target;
						}
					}

					valid = new String(k);
					if (valid.endsWith("{}")) {
						valid = valid.substring(0, valid.length() - 2);
					} else if (valid.endsWith("$")) {
						valid = valid.substring(0, valid.length() - 1);
					}
					if (valid.endsWith("&") || valid.endsWith("|") || valid.endsWith("!")) {
						valid = valid.substring(0, valid.length() - 1);
					}
					if (isWord(valid)) {
						request.put(k, v);
					}
				}
			}
		}

		JSONObject response = new Parser(RequestMethod.HEAD).parseResponse(new JSONRequest(table, request));
		if (response != null) {
			response = response.getJSONObject(table);
		}
		return response == null ? 0 : response.getIntValue(JSONResponse.KEY_COUNT);
	}


	/**获取绝对路径
	 * @param path
	 * @param name
	 * @return
	 */
	private String getAbsPath(String path, String name) {
		Log.i(TAG, "getPath  path = " + path + "; name = " + name + " <<<<<<<<<<<<<");
		path = StringUtil.getString(path);
		name = StringUtil.getString(name);
		if (StringUtil.isNotEmpty(path, false)) {
			if (StringUtil.isNotEmpty(name, false)) {
				path += ((name.startsWith(SEPARATOR) ? "" : SEPARATOR) + name);
			}
		} else {
			path = name;
		}
		if (path.startsWith(SEPARATOR)) {
			path = path.substring(1);
		}
		Log.i(TAG, "getPath  return " + path + " >>>>>>>>>>>>>>>>");
		return path;
	}

	//依赖引用关系 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**有关联代码的object的关联key在relationMap里
	 * @param keyPath
	 * @return
	 */
	private boolean isInRelationMap(String keyPath) {
		if (keyPath == null) {
			return false;
		}
		//		return keyValuePathMap == null ? false : keyValuePathMap.containsKey(path);
		Set<String> set = keyValuePathMap == null ? null : keyValuePathMap.keySet();
		if (set != null) {
			for (String key : set) {
				if (keyPath.equals(key) || key.startsWith(keyPath + "/")) {//解决相同字符导致的错误) {//key.contains(path)) {//
					return true;
				}
			}
		}
		return false;
	}


	/**新增keyPath-valuePath关联
	 * @param keyPath
	 * @param valuePath
	 */
	private void putRelation(String keyPath, String valuePath) {
		keyValuePathMap.put(keyPath, valuePath);
	}
	/**根据keyPath获取valuePath
	 * @param keyPath
	 * @return
	 */
	private String getRelationValuePath(String keyPath) {
		return keyValuePathMap.get(keyPath);
	}
	/**移除keyPath-valuePath关联
	 * @param keyPath
	 */
	private void removeRelation(String keyPath) {
		keyValuePathMap.remove(keyPath);
	}


	/**
	 * @param path
	 * @param replacePath
	 */
	private void updateRelation(String path, String replacePath) {
		//更新关系path中对应改变字段
		Set<String> relationSet = replacePath == null || keyValuePathMap == null ? null : keyValuePathMap.keySet();
		if (relationSet != null) {
			path = StringUtil.getString(path);

			String relationValue;
			for (String relationKey : relationSet) {
				if (relationKey == null || relationKey.startsWith(replacePath) == false) {
					continue;
				}
				relationValue = keyValuePathMap.get(relationKey);
				if (relationValue != null && relationValue.startsWith(path)
						&& relationValue.startsWith(replacePath) == false) {
					//用replace会将所有符合字符替换！ []/Comment[] -> []/0/Comment[]/0, replaceFirst因[]特殊字符崩溃
					relationValue = replacePath + relationValue.substring(path.length());
					keyValuePathMap.put(relationKey, relationValue);
				}
			}
		}
	}

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
	 * @return
	 */
	private Object getValueByPath(String valuePath) {
		return getValueByPath(valuePath, false);
	}
	/**根据路径获取值
	 * @param valuePath
	 * @param containKey
	 * @return containKey && parent.containsKey(targetKey) == false ? path : parent.get(targetKey);
	 */
	private Object getValueByPath(String valuePath, boolean containKey) {
		Log.i(TAG, "<<<<<<<<<<<<<<< \n getValueByPath  valuePath = " + valuePath
				+ ";  containKey = " + containKey + "\n <<<<<<<<<<<<<<<<<<");
		if (StringUtil.isNotEmpty(valuePath, true) == false) {
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
		if (parent == null) {
			return containKey ? valuePath : null;
		}

		System.out.println("getValueByPath  return parent.get(keys[keys.length - 1]); >> ");
		String targetKey = keys[keys.length - 1];
		return containKey && parent.containsKey(targetKey) == false ? valuePath : parent.get(targetKey);
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
		AccessVerifier.verify(requestObject, config);
		return queryHelper.select(config);//QueryHelper2.getInstance().select(config);//
	}

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	private QueryConfig newQueryConfig(String table, JSONObject request) {
		return QueryConfig.newQueryConfig(requestMethod, table, request);
	}


	private static final Pattern bigAlphaPattern = Pattern.compile("[A-Z]");
	private static final Pattern namePattern = Pattern.compile("^[0-9a-zA-Z_]+$");//已用55个中英字符测试通过

	/**判断是否为Array的key
	 * @param key
	 * @return
	 */
	public static boolean isArrayKey(String key) {
		return key != null && key.endsWith("[]");
	}
	/**判断是否为对应Table的key
	 * @param key
	 * @return
	 */
	public static boolean isTableKey(String key) {
		return isWord(key) && bigAlphaPattern.matcher(key.substring(0, 1)).matches();
	}
	/**判断是否为词，只能包含字母，数字或下划线
	 * @param key
	 * @return
	 */
	public static boolean isWord(String key) {
		return StringUtil.isNotEmpty(key, false) && namePattern.matcher(key).matches();
	}

	/**这些符号会对@依赖引用造成影响。[]/Moment/User:toUser/id@ ? 解决方法：
	 * 方法1(最佳)：在所有修改带操作符的地方更新依赖关系 #updateRelation
	 * 优点：替换key后结构成为客户端所需的，不带转义；可能增加updateRelation次数后性能比方法2遍历keySet找到映射key后好点
	 * 缺点：修改代码分散
	 * 
	 * 方法2：在所有用到key的地方用getRealKey(key)代替key
	 * 优点：修改代码集中
	 * 缺点：完成查询后key没有替换为客户端所需的，要么不解决，要么最后增加一次遍历来替换key；
	 *      需要在getValueByPath和putValueByPath中遍历keySet找到映射key
	 * 
	 * 方法3：方法1，2结合。增加一个keyMap<origin, real>，
	 * getValueByPath和putValueByPath中path中的realKey如果有映射就替换为originKey，
	 * 每次替换key为realkey后keyMap.remove(realkey)
	 * 
	 * 优点：替换key后结构成为客户端所需的，不带转义
	 * 缺点：逻辑复杂，而且不能单独origin-real映射，origin必须要完整路径，否则当不同Object种含有相同origin时就会出错！！！
	 * 
	 * 综上，方法1最好。
	 */

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
		if (originKey == null || isArrayKey(originKey)) {
			Log.w(TAG, "getRealKey  originKey == null || isArrayKey(originKey) >>  return originKey;");
			return originKey;
		}

		String key = new String(originKey);
		if (key.endsWith("$")) {//搜索，查询时处理
			key = key.substring(0, key.lastIndexOf("$"));
		} else if (key.endsWith("{}")) {//被包含，或者说处于value的范围内。查询时处理 "key[]":{} 和 "key{}":[]正好反过来
			key = key.substring(0, key.lastIndexOf("{}"));
		} else if (key.endsWith("()")) {//方法，查询完后处理，先用一个Map<key,function>保存？
			key = key.substring(0, key.lastIndexOf("()"));
		} else if (key.endsWith("@")) {//引用，引用对象查询完后处理。fillTarget中暂时不用处理，因为非GET请求都是由给定的id确定，不需要引用
			key = key.substring(0, key.lastIndexOf("@"));
		} else if (key.endsWith("+")) {//延长，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.lastIndexOf("+"));
			}
		} else if (key.endsWith("-")) {//缩减，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.lastIndexOf("-"));
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

		//"User:toUser":User转换"toUser":User, User为查询同名Table得到的JSONObject。交给客户端处理更好？不，查询就得截取
		if (isTableKey) {//不允许在column key中使用Type:key形式
			key = Pair.parseEntry(key, true).getKey();//table以左边为准
		} else {
			key = Pair.parseEntry(key).getValue();//column以右边为准
		}

		if (isWord(key.startsWith("@") ? key.substring(1) : key) == false) {
			throw new IllegalArgumentException(TAG + "/" + method + "  getRealKey: 字符 " + originKey + " 不合法！");
		}

		if (saveLogic) {
			key = key + StringUtil.getString(last);
		}
		Log.i(TAG, "getRealKey  return key = " + key);
		return key;
	}

}
