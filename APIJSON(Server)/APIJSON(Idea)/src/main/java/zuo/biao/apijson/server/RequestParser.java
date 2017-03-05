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
import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.Table;
import zuo.biao.apijson.TypeValueKeyEntry;
import zuo.biao.apijson.server.sql.AccessVerifier;
import zuo.biao.apijson.server.sql.QueryHelper;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public class RequestParser {
	private static final String TAG = "RequestParser: ";

	public static final String SEPARATOR = StringUtil.SEPARATOR;

	private RequestMethod requestMethod;
	public RequestParser() {
		this(null);
	}
	public RequestParser(RequestMethod requestMethod) {
		super();
		if (requestMethod == null) {
			requestMethod = RequestMethod.GET;
		}
		this.requestMethod = requestMethod;
	}


	private JSONObject requestObject;

	private boolean parseRelation;
	private Map<String, String> relationMap;


	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	public String parse(String request) {
		String response = JSON.toJSONString(parseResponse(request));

		System.out.println(TAG + "parse  return response = \n" + response
				+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

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
		System.out.println("\n\n\n\n" + TAG + requestMethod.name() + "/parseResponse  request = \n" + request);
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
		relationMap = new HashMap<String, String>();
		parseRelation = false;

		Exception error = null;
		try {
			requestObject = getObject(null, null, null, request);

			if (relationMap.isEmpty() == false) {//优化性能，没有依赖引用的就不用再遍历了
				parseRelation = true;
				requestObject = getObject(null, null, null, requestObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}

		QueryHelper.getInstance().close();
		//		QueryHelper2.getInstance().close();


		requestObject = AccessVerifier.removeAccessInfo(requestObject);
		//		if (isGetMethod(requestMethod) || requestMethod == RequestMethod.POST_GET) {//分情况把我都搞晕了@_@
		requestObject = error == null ? extendSuccessResult(requestObject)
				: extendResult(requestObject, 206, "未完成全部请求：\n" + error.getMessage());
		//		}

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
			method = RequestMethod.GET;
		}
		System.out.println("\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n " + TAG + method.name()
		+ "/parseResponse  request = \n" + request);
		return JSON.parseObject(request);
	}


	/**是否为GET请求方法
	 * @param method
	 * @return
	 */
	public static boolean isGetMethod(RequestMethod method) {
		return method == null || method == RequestMethod.GET || method == RequestMethod.HEAD;
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
		object.put("status", status);
		object.put("message", message);
		return object;
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object) {
		return extendResult(object, 200, "success");
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Exception e) {
		JSONObject error = newErrorResult(e);
		return extendResult(object, error.getIntValue("status"), error.getString("message"));
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
	 */
	public static JSONObject getCorrectRequest(RequestMethod method, JSONObject request) throws Exception {
		if (isGetMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString(JSONRequest.KEY_TAG);
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<<<
		QueryConfig config = new QueryConfig(RequestMethod.GET, "Request");
		config.setColumns("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", method.name());
		where.put(JSONRequest.KEY_TAG, tag);
		config.setWhere(where);

		JSONObject object = null;
		String error = "";
		try {
			object = QueryHelper.getInstance().select(config);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		}
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
		System.out.println(TAG + "filterTarget  requestName = " + requestName
				+ " target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request) + "\n >> return null;");
		if (target == null || request == null) {// || request.isEmpty()) {
			System.out.println(TAG + "filterTarget  target == null || request == null >> return null;");
			return null;
		}
		//		if (method == null) {
		//			method = RequestMethod.GET;
		//		}

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
				List<String> disallowList = new ArrayList<>();
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
						System.out.println(TAG + "fillTarget  key = " + key + "; result = " + result);
						if (result == null || result.isEmpty()) {//只添加!=null的值，可能数据库返回数据不够count
							throw new IllegalArgumentException(requestName
									+ "不能缺少 " + key + " 等[" + necessarys + "]内的任何JSONObject！");
						}
						if (method == RequestMethod.POST && result.containsKey(Table.ID) == false) {//为注册用户返回id
							result.put(Table.ID, System.currentTimeMillis());
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

		System.out.println(TAG + "filterTarget  return transferredRequest = " + JSON.toJSONString(transferredRequest));
		return transferredRequest;
	}

	/**array至少有一个值在request的key中
	 * @param key
	 * @param array
	 * @return
	 */
	public static boolean isContainKeyInArray(String key, String[] array) {
		if (array == null || array.length <= 0 || key == null) {
			System.out.println(TAG + "isContainKeyInArray"
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
	 * @param parentConfig
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getObject(String parentPath, final QueryConfig parentConfig, String name
			, final JSONObject request) throws Exception {
		System.out.println(TAG + "\ngetObject:  parentPath = " + parentPath
				+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {//key-value条件
			return null;
		}
		final String path = getPath(parentPath, name);
		if (parseRelation //优化性能：第二遍遍历时如果内部没有依赖引用其它就跳过
				&& StringUtil.isNotEmpty(path, true)//避免最外层返回true而不能parseRelation
				&& isInRelationMap(path) == false) {
			return request;
		}

		boolean nameIsNumber = StringUtil.isNumer(name);
		QueryConfig config = nameIsNumber ? parentConfig : null;
		if (config == null) {
			config = new QueryConfig(requestMethod, name).setCount(1);
		}
		//避免"[]":{"0":{"1":{}}}这种导致第3层当成[]的直接子Object
		if (nameIsNumber && ("" + config.getPosition()).equals(name) == false) {
			config.setPosition(0).setCount(1).setPage(0);
		}

		boolean containRelation = false;

		Set<String> set = request.keySet();
		JSONObject transferredRequest = new JSONObject(true);
		Map<String, String> functionMap = new LinkedHashMap<>();
		if (set != null) {
			Object value;
			JSONObject result;
			boolean isFirst = true;
			for (String key : set) {
				value = transferredRequest.containsKey(key) ? transferredRequest.get(key) : request.get(key);
				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					if (isArrayKey(key)) {//json array
						result = getArray(path, config, key, (JSONObject) value);
					} else {//json object
						result = getObject(path, isFirst == false || nameIsNumber == false //[]里第一个不能为[]
								? null : config, key, (JSONObject) value);
						isFirst = false;
					}
					System.out.println(TAG + "getObject  key = " + key + "; result = " + result);
					if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
						transferredRequest.put(key, result);
					}
				} else {//JSONArray或其它Object，直接填充
					transferredRequest.put(key, value);
					if (key.endsWith("()")) {
						if (value instanceof String == false) {
							throw new IllegalArgumentException("\"key()\": 后面必须为函数String！");
						}
						functionMap.put(key, (String) value);
					} else if (key.endsWith("@")) {//StringUtil.isPath((String) value)) {
						if (value instanceof String == false) {
							throw new IllegalArgumentException("\"key@\": 后面必须为依赖路径String！");
						}
						System.out.println("getObject  StringUtil.isPath(value) >> parseRelation = " + parseRelation);
						String replaceKey = getRealKey(key, false);
						if (parseRelation) {
							transferredRequest.put(replaceKey, getValueByPath(relationMap.get(getPath(path, replaceKey))));
							//							relationMap.remove(path + SEPARATOR + key);
							updateRelation(path, getPath(path, replaceKey));//request结构已改变，需要更新依赖关系
						} else {
							containRelation = true;
							relationMap.put(getPath(path, replaceKey)//value.contains(parentPath)会因为结构变化而改变
									, getPath((((String) value).startsWith(SEPARATOR) ? parentPath : ""), (String) value));
						}
					}
				}
			}
		}

		if (containRelation == false && isTableKey(name)) {//提高性能 isObjectKey(name)) {
			if (parseRelation == false || isInRelationMap(path)) {//避免覆盖原来已经获取的
				//			relationMap.remove(path);
				QueryConfig config2 = newQueryConfig(name, transferredRequest);

				if (parentConfig != null) {
					config2.setCount(parentConfig.getCount()).setPage(parentConfig.getPage())
					.setPosition(parentConfig.getPosition());//避免position > 0的object获取不到
				}

				transferredRequest = getSQLObject(config2);//不管用：暂时用这个解决返回多余空数据
				//				
				//				JSONObject result = getSQLObject(config2);
				//				if (result != null && result.isEmpty() == false) {//解决获取失败导致不能获取里面JSONObject
				//					transferredRequest = result;


				if (transferredRequest != null && transferredRequest.isEmpty() == false) {//避免返回空的
					//解析函数function
					Set<String> functionSet = functionMap.keySet();
					if (functionSet != null && functionSet.isEmpty() == false) {
						for (String key : functionSet) {
							try {
								transferredRequest.put(getRealKey(key, false)
										, Function.invoke(transferredRequest, functionMap.get(key)));
							} catch (Exception e) {
								Log.e(TAG, "getObject  containRelation == false && isTableKey(name)"
										+ " >> transferredRequest.put(getRealKey(key, false),"
										+ " Function.invoke(transferredRequest, functionMap.get(key)));"
										+ " >> } catch (Exception e) {");
								e.printStackTrace();
							}
						}
					}
				}

				if (parseRelation) {
					putValueByPath(path, transferredRequest);//解决获取关联数据时requestObject里不存在需要的关联数据
				}
				//				}
			}
		}

		return transferredRequest;
	}



	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param parentConfig parentObject对子object的SQL查询配置
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 * @throws Exception 
	 */
	private JSONObject getArray(String parentPath, QueryConfig parentConfig, String name
			, final JSONObject request) throws Exception {
		System.out.println(TAG + "\n\n\n getArray parentPath = " + parentPath
				+ "; name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {//jsonKey-jsonValue条件
			return null;
		}
		String path = getPath(parentPath, name);

		int page = 0, count = 0;
		try {
			page = request.getIntValue(JSONRequest.KEY_PAGE);
			count = request.getIntValue(JSONRequest.KEY_COUNT);
		} catch (Exception e) {
			System.out.println(TAG + "getArray   try { page = arrayObject.getIntValue(page); ..." +
					" >> } catch (Exception e) {\n" + e.getMessage());
		}

		//最好先获取第一个table的所有项（where条件），填充一个列表？
		Set<String> set = request.keySet();
		if (count <= 0 || count > 10) {//10以下不优化长度
			if(parseRelation == false && set != null) {
				JSONObject object;
				int totalCount = 0;
				for (String key : set) {
					if (isTableKey(key)) {
						object = getJSONObject(request, key);
						if (object != null) {// && object.isEmpty() == false) {
							totalCount = QueryHelper.getInstance().getCount(key);
							break;
						}
					}
				}
				if (totalCount <= 0) {//request内部没有JSONObject或者不存在适合条件的table内容
					return null;
				}
				if (count > totalCount) {
					count = totalCount;
				}
			}
		}
		if (count <= 0 || count > 100) {//count最大为100
			count = 100;
		}

		//		if (parseRelation) {
		//			request.remove("page");
		//			request.remove("count");
		//		}
		System.out.println(TAG + "getArray page = " + page + "; count = " + count);

		QueryConfig config = new QueryConfig(requestMethod, count, page);

		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			JSONObject parent = null;
			Object value;
			JSONObject result;
			if (parseRelation == false) {
				//生成count个
				for (int i = 0; i < count; i++) {
					parent = new JSONObject(true);
					for (String key : set) {
						value = request.get(key);
						if (value instanceof JSONObject) {//JSONObject
							config.setPosition(i);
							if (isArrayKey(key)) {//json array
								result = getArray(getPath(path, "" + i), config, key, (JSONObject) value);
							} else {//json object
								result = getObject(getPath(path, "" + i), config, key, (JSONObject) value);
							}
							System.out.println(TAG + "getArray  parseRelation == false"
									+ " >> i = " + i + "result = " + result);
							if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
								parent.put(key, result);

								updateRelation(path, getPath(path, "" + i));//request结构已改变，需要更新依赖关系
							}
						} else {//JSONArray或其它Object，直接填充
							transferredRequest.put(key, value);//array里不允许关联，只能在object中关联
						}
					}
					if (parent.isEmpty() == false) {//可能数据库返回数据不够count
						transferredRequest.put("" + i, parent);
					}
				}
			} else {
				for (String key : set) {
					value = request.get(key);
					if (value instanceof JSONObject) {//JSONObject，往下一级提取
						config.setPosition(Integer.valueOf(0 + StringUtil.getNumber(key, true)));
						if (isArrayKey(key)) {//json array
							result = getArray(path, config, key, (JSONObject) value);
						} else {//json object
							result = getObject(path, config, key, (JSONObject) value);
						}
						if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
							transferredRequest.put(key, result);
						}
					} else {//JSONArray或其它Object
						//array里不允许关联，只能在object中关联
					}
				}
			}
		}

		System.out.println(TAG + "getArray  return " + JSON.toJSONString(transferredRequest) + "\n>>>>>>>>>>>>>>>\n\n\n");

		return transferredRequest;
	}

	/**
	 * @param path
	 * @param replacePath
	 */
	private void updateRelation(String path, String replacePath) {
		//更新关系path中对应改变字段
		Set<String> relationSet = replacePath == null || relationMap == null ? null : relationMap.keySet();
		if (relationSet != null) {
			String relationValue;
			for (String relationKey : relationSet) {
				if (relationKey == null || relationKey.startsWith(replacePath) == false) {
					continue;
				}
				relationValue = relationMap.get(relationKey);
				if (relationValue != null && relationValue.startsWith(path)
						&& relationValue.startsWith(replacePath) == false) {
					relationMap.put(relationKey, relationValue.replace(path, replacePath));
				}
			}
		}		
	}

	/**获取拼接路径
	 * @param path
	 * @param name
	 * @return
	 */
	private String getPath(String path, String name) {
		System.out.println("getPath  path = " + path + "; name = " + name + " <<<<<<<<<<<<<");
		path = StringUtil.getString(path);
		name = StringUtil.getString(name);
		if (StringUtil.isNotEmpty(path, false)) {
			if (StringUtil.isNotEmpty(name, false)) {
				path += ((name.startsWith(SEPARATOR) ? "" : SEPARATOR) + name);
			}
		} else {
			path = name;
		}
		System.out.println("getPath  return " + path + " >>>>>>>>>>>>>>>>");
		return path;
	}

	/**有关联代码的object的关联key在relationMap里
	 * @param path
	 * @return
	 */
	private boolean isInRelationMap(String path) {
		if (path == null) {
			return false;
		}
		//		return relationMap == null ? false : relationMap.containsKey(path);
		Set<String> set = relationMap == null ? null : relationMap.keySet();
		if (set != null) {
			for (String key : set) {
				if (path.equals(key) || key.startsWith(path + "/")) {//解决相同字符导致的错误) {//key.contains(path)) {//
					return true;
				}
			}
		}
		return false;
	}


	/**将已获取完成的object的内容替换requestObject里对应的值
	 * @param path object的路径
	 * @param value 需要被关联的object
	 */
	private synchronized void putValueByPath(String path, Object value) {
		System.out.println("\n putValueByPath  path = " + path + "; value = " + value + "\n <<<<<<<<<<<<<<<<<<<<<<<");
		String[] keys = StringUtil.splitPath(path);
		if (keys == null) {
			return;
		}
		if (requestObject == null) {
			requestObject = new JSONObject(true);
		}
		JSONObject parent = requestObject;
		JSONObject child = null;
		String key;
		for (int i = 0; i < keys.length - 1; i++) {//一步一步到达指定位置parentPath
			key = keys[i];
			child = getJSONObject(parent, key);
			if (child == null) {//不存在该路径就创建
				child = new JSONObject(true);
				parent.put(key, child);
			}
			parent = child;
		}

		try {
			System.out.println("putValueByPath  parent.put(keys[keys.length - 1], value); >> ");
			parent.put(keys[keys.length - 1], value);
		} catch (Exception e) {
			System.out.println("putValueByPath  try { parent.put(keys[keys.length - 1], value); " +
					"} catch (Exception e) {\n" + e.getMessage());
		}
		System.out.println(TAG + "putValueByPath  requestObject" + JSON.toJSONString(requestObject) + "\n >>>>>>>>>>>>>>");
	}
	/**根据路径获取值
	 * @param path
	 * @return
	 */
	private Object getValueByPath(String path) {
		System.out.println(TAG + "<<<<<<<<<<<<<<< \n getValueByPath  path = " + path + "\n <<<<<<<<<<<<<<<<<<");
		String[] keys = StringUtil.splitPath(path);

		if (keys != null) {
			JSONObject parent = requestObject;
			JSONObject child = null;
			String key;
			for (int i = 0; i < keys.length - 1; i++) {//一步一步到达指定位置parentPath
				key = keys[i];
				child = getJSONObject(parent, key);
				if (child == null) {//不存在
					return path;
				}
				parent = child;
			}
			try {
				System.out.println("getValueByPath  return parent.get(keys[keys.length - 1]); >> ");
				return parent.get(keys[keys.length - 1]);
			} catch (Exception e) {
				System.out.println("getValueByPath  try { return parent.get(keys[keys.length - 1]); " +
						"} catch (Exception e) {\n" + e.getMessage() + "\n >> return '';");
				return path;
			}
		}

		System.out.println(TAG + "getValueByPath  return requestObject"
				+ JSON.toJSONString(requestObject) + "\n >>>>>>>>>>>>>>>>>>");
		return requestObject;
	}


	public static JSONObject getJSONObject(JSONObject object, String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			System.out.println("getJSONObject  try { return object.getJSONObject(key);"
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
		System.out.println("getSQLObject  config = " + JSON.toJSONString(config));
		AccessVerifier.verify(requestObject, config);
		return QueryHelper.getInstance().select(config);//QueryHelper2.getInstance().select(config);//
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
	 * 缺点：完成查询后key没有替换为客户端所需的，要么不解决，要么最后增加一次遍历来替换key；需要在getValueByPath和putValueByPath中遍历keySet找到映射key
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
	 * #作为方法引用符号，()作为包含关系？% & | {} [] <> < 这些呢？
	 * <br> "userId@":"/User/id"           //@根据路径依赖，@始终在最后。value是'/'分隔的字符串。
	 * <br> "isPraised()":"isContain(Collection:idList,long:id)"  //()使用方法，value是方法表达式。不能与@并用。
	 * <br> "content$":"%searchKey%"       //$搜索，右边紧跟key。value是搜索表达式。
	 * <br> "@columns":"id,sex,name"       //关键字，左边紧跟key。暂时不用，因为目前关键字很少，几乎不会发生冲突。value是','分隔的字符串。
	 * 
	 * @param key
	 * @return
	 */
	public static String getRealKey(String originKey, boolean isTableKey) throws Exception {
		Log.i(TAG, "getRealKey  originKey = " + originKey);
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
		}

		//"User:toUser":User转换"toUser":User, User为查询同名Table得到的JSONObject。交给客户端处理更好？不，查询就得截取
		if (isTableKey) {//不允许在column key中使用Type:key形式
			key = TypeValueKeyEntry.parseKeyEntry(key).getKey();
		}

		if (isWord(key.startsWith("@") ? key.substring(1) : key) == false) {
			throw new IllegalArgumentException(TAG + " getRealKey: 字符 " + originKey + " 不合法！");
		}

		return key;
	}

}
