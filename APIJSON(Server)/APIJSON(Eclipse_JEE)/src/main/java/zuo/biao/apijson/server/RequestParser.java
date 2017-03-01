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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.Table;
import zuo.biao.apijson.server.sql.AccessVerifier;
import zuo.biao.apijson.server.sql.QueryHelper;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public class RequestParser {
	private static final String TAG = "RequestParser: ";

	public static final String SEPARATOR = StringUtil.SEPARATOR;

	private RequestMethod requestMethod;
	public RequestParser(RequestMethod requestMethod) {
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
	 * @param request
	 * @return requestObject
	 */
	public JSONObject parseResponse(JSONObject request) {
		return parseResponse(JSON.toJSONString(request));
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return requestObject
	 */
	public JSONObject parseResponse(String request) {

		try {
			request = URLDecoder.decode(request, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return newErrorResult(e);
		}
		System.out.println("\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n " + TAG + requestMethod.name()
		+ "/parseResponse  request = " + request);

		relationMap = new HashMap<String, String>();
		parseRelation = false;
		try {
			requestObject = getCorrectRequest(requestMethod, JSON.parseObject(request));
		} catch (Exception e) {
			return newErrorResult(e);
		}

		Exception error = null;
		try {
			requestObject = getObject(null, null, null, requestObject);

			parseRelation = true;
			requestObject = getObject(null, null, null, requestObject);
		} catch (Exception e) {
			e.printStackTrace();
			error = e;
		}

		QueryHelper.getInstance().close();
		//		QueryHelper2.getInstance().close();


		requestObject = AccessVerifier.removeAccessInfo(requestObject);
		if (isGetMethod(requestMethod) || requestMethod == RequestMethod.POST_GET) {
			requestObject = error == null ? extendSuccessResult(requestObject)
					: extendResult(requestObject, 206, "未完成全部请求：\n" + error.getMessage());
		}

		System.out.println("\n\n\n\n" + TAG + requestMethod.name() + "/parseResponse  request = \n" + request);
		return requestObject;
	}


	/**是否为GET请求方法
	 * @param method
	 * @return
	 */
	public static boolean isGetMethod(RequestMethod method) {
		return method == null || method == RequestMethod.GET;
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

		String tag = request.getString("tag");
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new IllegalArgumentException("请指定tag！一般是table名称");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<<<
		QueryConfig config = new QueryConfig(RequestMethod.GET, "Request");
		config.setColumns("structure");

		Map<String, Object> where = new HashMap<String, Object>();
		where.put("method", method.name());
		where.put("tag", tag);
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

		request.remove("tag");
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
		String path = getPath(parentPath, name);

		boolean nameIsNumber = StringUtil.isNumer(name);
		QueryConfig config = nameIsNumber ? parentConfig : null;
		if (config == null) {
			config = new QueryConfig(requestMethod, name);
		}
		final int position = nameIsNumber ? Integer.valueOf(0 + StringUtil.getNumber(name)) : 0;

		boolean containRelation = false;

		Set<String> set = request.keySet();
		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			Object value;
			JSONObject result;
			boolean isFirst = true;
			for (String key : set) {
				value = transferredRequest.containsKey(key) ? transferredRequest.get(key) : request.get(key);
				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					config.setPosition(isFirst && nameIsNumber ? position : 0);
					if (isArrayKey(key)) {//json array
						result = getArray(path, config, key, (JSONObject) value);
					} else {//json object
						isFirst = false;
						result = getObject(path, config, key, (JSONObject) value);
					}
					System.out.println(TAG + "getObject  key = " + key + "; result = " + result);
					if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
						transferredRequest.put(key, result);
					}
				} else {//JSONArray或其它Object，直接填充
					transferredRequest.put(key, value);

					//替换path
					if (value instanceof String && StringUtil.isPath((String) value)) {
						System.out.println("getObject  StringUtil.isPath(value) >> parseRelation = " + parseRelation);
						if (parseRelation) {
							transferredRequest.put(key, getValueByPath(relationMap.get(getPath(path, key))));
							//							relationMap.remove(path + SEPARATOR + key);
						} else {
							containRelation = true;
							relationMap.put(getPath(path, key)//value.contains(parentPath)会因为结构变化而改变
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
			page = request.getIntValue("page");
			count = request.getIntValue("count");
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

								//更新关系path中对应改变字段
								Set<String> relationSet = relationMap == null ? null : relationMap.keySet();
								if (relationSet != null) {
									String relationValue;
									String replacePath = getPath(path, "" + i);
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
		return QueryConfig.getQueryConfig(requestMethod, table, request);
	}


	private static final Pattern bigAlphaPattern = Pattern.compile("[A-Z]");
	private static final Pattern namePattern = Pattern.compile("^[0-9a-zA-Z_]+$");//已用55个中英字符测试通过

	public static boolean isTableKey(String key) {
		return StringUtil.isNotEmpty(key, false)
				&& bigAlphaPattern.matcher(key.substring(0, 1)).matches()
				&& namePattern.matcher(key.substring(1)).matches();
	}
	public static boolean isArrayKey(String key) {
		return key != null && key.endsWith("[]");
	}

}
