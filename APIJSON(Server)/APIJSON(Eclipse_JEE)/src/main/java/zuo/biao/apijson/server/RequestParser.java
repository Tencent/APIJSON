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
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
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
		this.requestMethod = requestMethod;
	}


	private JSONObject requestObject;

	private boolean parseRelation;
	private Map<String, String> relationMap;

	/**
	 * @param json
	 */
	public JSONObject parse(String json) {


		try {
			json = URLDecoder.decode(json, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(TAG + "parse  json = " + json);

		relationMap = new HashMap<String, String>();
		parseRelation = false;
		try {
			requestObject = getCorrectRequest(requestMethod, JSON.parseObject(json));
		} catch (Exception e) {
			e.printStackTrace();
			return QueryHelper.newJSONObject(403, e.getMessage());
		}

		requestObject = getObject(null, null, null, requestObject);
		parseRelation = true;
		requestObject = getObject(null, null, null, requestObject);

		requestObject = AccessVerifier.removeAccessInfo(requestObject);

		QueryHelper.getInstance().close();
		//		QueryHelper2.getInstance().close();

		System.out.println(TAG + "\n\n最终返回至客户端的json:\n" + JSON.toJSONString(requestObject));
		return requestObject;
	}


	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 */
	public JSONObject getCorrectRequest(RequestMethod method, JSONObject request) throws Exception {
		if (method == null || method == RequestMethod.GET) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		String tag = request.getString("tag");
		if (StringUtil.isNotEmpty(tag, true) == false) {
			throw new NullPointerException("请指定tag！一般是table名称");
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
			throw new IllegalAccessException("非GET请求必须是服务端允许的操作！ \n " + error);
		}
		object = object.getJSONObject("structure");//解决返回值套了一层 "structure":{}

		JSONObject target = null;
		if (isObjectKey(tag) && object.containsKey(tag) == false) {//tag是table名
			target = new JSONObject(true);
			target.put(tag, object);
		} else {
			target = object;
		}
		//获取指定的JSON结构 >>>>>>>>>>>>>>
		
		request.remove("tag");
		return fillTarget(target, request, "");
	}


	public static final String NECESSARY_COLUMNS = "necessaryColumns";
	public static final String DISALLOW_COLUMNS = "disallowColumns";

	/**从request提取target指定的内容
	 * @param target
	 * @param request
	 * @return
	 */
	private JSONObject fillTarget(JSONObject target, final JSONObject request, String requestName) throws Exception {
		System.out.println(TAG + "filterTarget  requestName = " + requestName + " target = \n" + JSON.toJSONString(target)
		+ "\n request = \n" + JSON.toJSONString(request) + "\n >> return null;");
		if (target == null || request == null) {// || request.isEmpty()) {
			System.out.println(TAG + "filterTarget  target == null || request == null >> return null;");
			return null;
		}

		//	//TODO 方法一	 ：逻辑错了，不是填充target里的key，而是把request对应的所有key-value填充至target
		//		Set<String> set = target.keySet();
		//		if (set != null) {
		//			String value;
		//			JSONObject child;
		//			for (String key : set) {
		//				value = target.getString(key);
		//				if (DISALLOW_COLUMNS.equals(key)) {
		//					if (isContainKeyInArray(request, StringUtil.split(value))) {
		//						throw new IllegalArgumentException("不允许传[" + value + "]内的任何字段！");
		//					}
		//				} else if (NECESSARY_COLUMNS.equals(key)) {
		//					if (isContainAllKeyInArray(request, StringUtil.split(value)) == false) {
		//						throw new IllegalArgumentException(requestName + "缺少[" + value + "]内的某些字段！");
		//					}
		//				}
		//
		//				child = JSON.parseObject(value);
		//				if (child == null) {//key - value
		//					target.put(key, request.getString(key));//提取key-value
		//				} else { //object
		//					target.put(key, filterTarget(child, request.getJSONObject(key), key));//往下一级提取
		//				}
		//			}
		//		}




		//		// 方法二：行不通，target外层可能加上tag，也可能不加
		//		String necessarys = StringUtil.getNoBlankString(target.getString(NECESSARY_COLUMNS));
		//		String disallows = StringUtil.getNoBlankString(target.getString(DISALLOW_COLUMNS));
		//
		//
		//		//提取必须的字段 <<<<<<<<<<<<<<<<<<<
		//		String[] necessaryColumns = StringUtil.split(necessarys);
		//		String value;
		//		if (necessaryColumns != null) {
		//			for (String column : necessaryColumns) {
		//				if (StringUtil.isNotEmpty(column, true) == false) {
		//					continue;
		//				}
		//				value = request.getString(column);
		//				if (StringUtil.isNotEmpty(value, true) == false) {
		//					throw new IllegalArgumentException(requestName + "缺少[" + necessarys + "]内的字段 " + column + " ！");
		//				}
		//				target.put(column, value);//提取key-value
		//				request.remove(column);
		//			}
		//			target.put(NECESSARY_COLUMNS, necessarys);
		//			target.put(DISALLOW_COLUMNS, disallows);
		//		}
		//		//提取提取必须的字段 >>>>>>>>>>>>>>>>>>>
		//
		//		//排除不允许的字段，提取可选字段 <<<<<<<<<<<<<<<<<<<
		//		String[] disallowColumns = null;
		//		Set<String> set = request.keySet();
		//		if ("!".equals(disallows)) {//所有非necessaryColumns
		//			if (set != null && set.isEmpty() == false) {
		//				throw new IllegalArgumentException("不允许传[" + disallows + "]内的任何字段！");
		//			}
		//		} else {
		//			disallowColumns = StringUtil.split(disallows);
		//		}
		//		if (set != null) {
		//			for (String column : set) {
		//				if (StringUtil.isNotEmpty(column, true) == false) {
		//					continue;
		//				}
		//				if (isContainInArray(column, disallowColumns)) {
		//					throw new IllegalArgumentException("不允许传[" + disallows + "]内的任何字段！");
		//				}
		//				target.put(column, request.getString(column));//提取可选字段
		////				request.remove(column);
		//			}
		//			
		//			target.put(NECESSARY_COLUMNS, necessarys);
		//			target.put(DISALLOW_COLUMNS, disallows);
		//		}
		//		//排除不允许的字段，提取可选字段 >>>>>>>>>>>>>>>>>>>
		//
		//
		//		set = target.keySet();
		//		if (set != null) {
		//			JSONObject child;
		//			for (String key : set) {
		//				child = JSON.parseObject(target.getString(key));
		//				if (child != null) { //object
		//					target.put(key, fillTarget(child, request.getJSONObject(key), key));//往下一级提取
		//				}
		//			}
		//		}
		//
		//		target.remove(DISALLOW_COLUMNS);
		//		target.remove(NECESSARY_COLUMNS);





		// 方法三：遍历request，transferredRequest只添加target所包含的object，且移除target中DISALLOW_COLUMNS，期间判断NECESSARY_COLUMNS是否都有
		String necessarys = StringUtil.getNoBlankString(target.getString(NECESSARY_COLUMNS));
		String[] necessaryColumns = StringUtil.split(necessarys);

		//判断必要字段是否都有
		if (necessaryColumns != null) {
			for (String s : necessaryColumns) {
				if (request.containsKey(s) == false) {
					throw new IllegalArgumentException(requestName
							+ "不能缺少 " + s + ":" + "\"" + "\"" + " 等[" + necessarys + "]内的任何字段！");
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
					if (isContainInArray(key, necessaryColumns) == false) {
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
			String value;
			JSONObject child;
			JSONObject result;
			for (String key : set) {
				value = request.getString(key);
				child = JSON.parseObject(value);
				if (child == null) {//key - value
					if (isContainInArray(key, disallowColumns)) {
						throw new IllegalArgumentException("不允许传 " + key + " 等[" + disallows + "]内的任何字段！");
					}
					transferredRequest.put(key, value);
				} else {//object
					if (target.containsKey(key)) {//只填充target有的object
						result = fillTarget(target.getJSONObject(key), child, key);//往下一级提取
						System.out.println(TAG + "fillTarget  key = " + key + "; result = " + result);
						if (result == null || result.isEmpty()) {//只添加!=null的值，可能数据库返回数据不够count
							throw new IllegalArgumentException(requestName
									+ "不能缺少 " + key + ":{} 等[" + necessarys + "]内的任何JSONObject！");
						}
						transferredRequest.put(key, result);
					}
				}
			}
		}

		System.out.println(TAG + "filterTarget  return target = " + JSON.toJSONString(target));
		return transferredRequest;
	}

	/**array至少有一个值在request的key中
	 * @param request
	 * @param array
	 * @return
	 */
	private boolean isContainInArray(String key, String[] array) {
		if (array == null || array.length <= 0) {
			System.out.println(TAG + "isContainKeyInArray  array == null || array.length <= 0 >> return false;");
			return false;
		}
		if (key == null) {
			System.out.println(TAG + "isContainKeyInArray  key == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (key.equals(s) == true) {
				return true;
			}
		}

		return false;
	}

	/**array至少有一个值在request的key中
	 * @param request
	 * @param array
	 * @return
	 */
	private boolean isContainKeyInArray(JSONObject request, String[] array) {
		if (array == null || array.length <= 0) {
			System.out.println(TAG + "isContainKeyInArray  array == null || array.length <= 0 >> return false;");
			return false;
		}
		if (request == null) {
			System.out.println(TAG + "isContainKeyInArray  request == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (request.containsKey(s) == true) {
				return true;
			}
		}

		return false;
	}
	/**array的所有值都在request的key中
	 * @param request
	 * @param array
	 * @return
	 */
	private boolean isContainAllKeyInArray(JSONObject request, String[] array) {
		if (array == null || array.length <= 0) {
			System.out.println(TAG + "isContainAllKeyInArray  array == null || array.length <= 0 >> return true;");
			return true;
		}
		if (request == null) {
			System.out.println(TAG + "isContainAllKeyInArray  request == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (request.containsKey(s) == false) {
				return false;
			}
		}

		return true;
	}
	//	/**array至少有一个值在request的key中
	//	 * @param request
	//	 * @param array
	//	 * @return
	//	 */
	//	private boolean isContainKeyInArray(JSONObject request, String[] array) {
	//		return verifyContainKeyInArray(request, array, true);
	//	}
	//	/**array的所有值都在request的key中
	//	 * @param request
	//	 * @param array
	//	 * @return
	//	 */
	//	private boolean isContainAllKeyInArray(JSONObject request, String[] array) {
	//		return verifyContainKeyInArray(request, array, false);
	//	}
	/**判断array的值和request的key的相同性
	 * @param request
	 * @param array
	 * @param contain
	 * @return
	 */
	private boolean verifyContainKeyInArray(JSONObject request, String[] array, boolean contain) {
		if (array == null || array.length <= 0) {
			System.out.println(TAG + "verifyContainKeyInArray  array == null || array.length <= 0 >> return ! contain;");
			return ! contain;
		}
		if (request == null) {
			System.out.println(TAG + "verifyContainKeyInArray  request == null >> return false;");
			return false;
		}

		for (String s : array) {
			if (request.containsKey(s) == contain) {
				return contain;
			}
		}

		return ! contain;
	}

	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param parentConfig
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 */
	private JSONObject getObject(String parentPath, final QueryConfig parentConfig, String name, final JSONObject request) {
		System.out.println(TAG + "\ngetObject:  parentPath = " + parentPath
				+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		if (request == null) {//key-value条件
			return null;
		}
		String path = getPath(parentPath, name);

		QueryConfig config = StringUtil.isNumer(name) ? parentConfig : null;
		if (config == null) {
			config = new QueryConfig(requestMethod, name);
		}
		boolean nameIsNumber = StringUtil.isNumer(name);
		final int position = nameIsNumber ? Integer.valueOf(0 + StringUtil.getNumber(name)) : 0;

		boolean containRelation = false;

		Set<String> set = request.keySet();
		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			String value;
			JSONObject child;
			JSONObject result;
			boolean isFirst = true;
			for (String key : set) {
				value = transferredRequest.containsKey(key) ? transferredRequest.getString(key) : request.getString(key);
				child = JSON.parseObject(value);
				if (child == null) {//key - value
					transferredRequest.put(key, value);
					if (StringUtil.isPath(value)) {
						System.out.println("getObject  StringUtil.isPath(value) >> parseRelation = " + parseRelation);
						if (parseRelation) {
							transferredRequest.put(key, getValueByPath(relationMap.get(getPath(path, key))));
							//							relationMap.remove(path + SEPARATOR + key);
						} else {
							containRelation = true;
							relationMap.put(getPath(path, key)//value.contains(parentPath)会因为结构变化而改变
									, getPath((value.startsWith(SEPARATOR) ? parentPath : ""), value));
						}
					}
				} else {
					config.setPosition(isFirst && nameIsNumber ? position : 0);
					if (isArrayKey(key)) {//json array
						result = getArray(path, config, key, child);
					} else {//json object
						isFirst = false;
						result = getObject(path, config, key, child);
					}
					System.out.println(TAG + "getObject  key = " + key + "; result = " + result);
					if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
						transferredRequest.put(key, result);
					}
				}
			}
		}

		if (containRelation == false && isObjectKey(name)) {
			if (parseRelation == false || isInRelationMap(path)) {//避免覆盖原来已经获取的
				//			relationMap.remove(path);
				QueryConfig config2 = getQueryConfig(name, transferredRequest);

				if (parentConfig != null) {
					config2.setLimit(parentConfig.getLimit()).setPage(parentConfig.getPage())
					.setPosition(parentConfig.getPosition());//避免position > 0的object获取不到
				}
				JSONObject result = null;
				try {
					result = getSQLObject(config2);
				} catch (Exception e) {
					//					e.printStackTrace();
					result = QueryHelper.newJSONObject(403, e.getMessage());
				}
				//				if (result != null) {
				transferredRequest = result;
				if (parseRelation) {
					putValueByPath(path, transferredRequest);//解决获取关联数据时requestObject里不存在需要的关联数据
				}
				//				}
			}
		}

		return transferredRequest;
	}



	//TODO 如果获取key顺序不能保证就用一个"readOrder":{"user", "work", ...}确定顺序
	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param parentConfig parentObject对子object的SQL查询配置
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return
	 */
	private JSONObject getArray(String parentPath, QueryConfig parentConfig, String name, final JSONObject request) {
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
			//			System.out.println(TAG + "getArray   try { page = arrayObject.getIntValue(page); ..." +
			//					" >> } catch (Exception e) {\n" + e.getMessage());
		}
		if (count <= 0) {//解决count<=0导致没有查询结果
			count = 100;
		}

		//		if (parseRelation) {
		//			request.remove("page");
		//			request.remove("count");
		//		}
		System.out.println(TAG + "getArray page = " + page + "; count = " + count);

		QueryConfig config = new QueryConfig(requestMethod, count, page);

		Set<String> set = request.keySet();
		JSONObject transferredRequest = new JSONObject(true);
		if (set != null) {
			JSONObject parent = null;
			String value;
			JSONObject child;
			JSONObject result;
			if (parseRelation == false) {
				//生成count个
				for (int i = 0; i < count; i++) {
					parent = new JSONObject(true);
					for (String key : set) {
						value = request.getString(key);
						child = JSON.parseObject(value);
						if (child == null) {//key - value
							//array里不允许关联，只能在object中关联
							transferredRequest.put(key, value);
						} else {
							config.setPosition(i);
							if (isArrayKey(key)) {//json array
								result = getArray(getPath(path, "" + i), config, key, child);
							} else {//json object
								result = getObject(getPath(path, "" + i), config, key, child);
							}
							System.out.println(TAG + "getArray  parseRelation == false >>  i = " + i + "result = " + result);
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
						}
					}
					if (parent.isEmpty() == false) {//可能数据库返回数据不够count
						transferredRequest.put("" + i, parent);
					}
				}
			} else {
				for (String key : set) {
					child = JSON.parseObject(request.getString(key));
					if (child == null) {//key - value
						//array里不允许关联，只能在object中关联
					} else {
						config.setPosition(Integer.valueOf(0 + StringUtil.getNumber(key, true)));
						if (isArrayKey(key)) {//json array
							result = getArray(path, config, key, child);
						} else {//json object
							result = getObject(path, config, key, child);
						}
						if (result != null && result.isEmpty() == false) {//只添加!=null的值，可能数据库返回数据不够count
							transferredRequest.put(key, result);
						}
					}
				}
			}
		}

		System.out.println(TAG + "getArray  return " + JSON.toJSONString(transferredRequest) + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");

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
		//		return relationMap == null ? false : relationMap.containsKey(path);
		Set<String> set = relationMap == null ? null : relationMap.keySet();
		if (set != null) {
			for (String key : set) {
				if (key != null && key.contains(path)) {
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
	public synchronized void putValueByPath(String path, Object value) {
		System.out.println("\n putValueByPath  path = " + path + "; value = " + value + "\n <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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
		System.out.println(TAG + "putValueByPath  requestObject" + JSON.toJSONString(requestObject) + "\n >>>>>>>>>>>>>>>>>>");
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
					return "";
				}
				parent = child;
			}
			try {
				System.out.println("getValueByPath  return parent.get(keys[keys.length - 1]); >> ");
				return parent.get(keys[keys.length - 1]);
			} catch (Exception e) {
				System.out.println("getValueByPath  try { return parent.get(keys[keys.length - 1]); " +
						"} catch (Exception e) {\n" + e.getMessage());
			}
		}
		System.out.println(TAG + "getValueByPath  return requestObject" + JSON.toJSONString(requestObject) + "\n >>>>>>>>>>>>>>>>>>");
		return requestObject;
	}


	private JSONObject getJSONObject(JSONObject object, String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			System.out.println("getJSONObject  try { return object.getJSONObject(key); } catch (Exception e) { \n"  + e.getMessage());
		}
		return null;
	}


	/**获取数据库返回的String
	 * @param config
	 * @return
	 * @throws AccessException 
	 */
	private synchronized JSONObject getSQLObject(QueryConfig config) throws Exception {
		System.out.println("getSQLObject  config = " + JSON.toJSONString(config));
		AccessVerifier.verify(requestMethod, requestObject, config == null ? null : config.getTable());
		return QueryHelper.getInstance().select(config);//QueryHelper2.getInstance().select(config);//
	}

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public QueryConfig getQueryConfig(String table, JSONObject request) {
		return QueryConfig.getQueryConfig(requestMethod, table, request);
	}
	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public QueryConfig newQueryConfig(String table, JSONObject request) {
		return QueryConfig.getQueryConfig(requestMethod, table, request);
	}
	/**把parentConfig的array属性继承下来
	 * @param config
	 * @param parentConfig
	 * @return
	 */
	private QueryConfig extendQueryConfig(QueryConfig config, QueryConfig parentConfig) {
		if (parentConfig != null) {
			if (config == null) {
				return parentConfig;
			}
			config.setLimit(parentConfig.getLimit()).setPage(parentConfig.getPage()).setPosition(parentConfig.getPosition());
		}
		return config;
	}


	public boolean isObject(String json) {
		return JSON.parseObject(json) != null;//json.startsWith("{") && json.endsWith("}");
	}

	private boolean isObjectKey(String key) {
		key = StringUtil.getString(key);
		return StringUtil.isNotEmpty(key, false) && isArrayKey(key) == false && StringUtil.isAlpha(key.substring(0, 1));
	}
	public boolean isArrayKey(String key) {
		return key != null && key.endsWith("[]");//[key]改为了key[]，更符合常规逻辑。 key.startsWith("[") && key.endsWith("]");
	}

}
