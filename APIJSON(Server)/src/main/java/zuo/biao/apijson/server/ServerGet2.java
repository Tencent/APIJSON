package zuo.biao.apijson.server;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.sql.SelectTable2;
import zuo.biao.apijson.server.sql.SelectTable3;


public class ServerGet2 {
	private static final String TAG = "ServerGet2: ";

	public static final String SEPARATOR = StringUtil.SEPARATOR;


	private JSONObject requestObject;

	private boolean parseRelation;
	private Map<String, String> relationMap;


	/**
	 * @param json
	 */
	public JSONObject get(String json) {
		//		System.out.println(TAG + TAG + "json = " + json);

		relationMap = new HashMap<String, String>();
		parseRelation = false;
		requestObject = getObject(null, null, null, JSON.parseObject(json));
		parseRelation = true;
		requestObject = getObject(null, null, null, requestObject);
		System.out.println(TAG + "\n\n最终返回至客户端的json:\n" + JSON.toJSONString(requestObject));

		/**
		 * TODO 格式化json，去除标记array内object位置的数字，转为[]形式，比如
		 * "Comment[]":{"0":{"Comment":{...}}, ...}
		 * 转为
		 * "Comment[]":[{...}, ...]
		 */

		SelectTable2.getInstance().close();
//		SelectTable3.getInstance().close();

		return requestObject;
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
			config = new QueryConfig(name);
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
				JSONObject result = getSQLObject(config2);
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
		if (parseRelation) {
			request.remove("page");
			request.remove("count");
		}
		System.out.println(TAG + "getArray page = " + page + "; count = " + count);

		QueryConfig config = new QueryConfig(count, page);

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
		//		synchronized (requestObject) {
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
		//		}
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
	 */
	private synchronized JSONObject getSQLObject(QueryConfig config) {
		System.out.println("getSQLObject  config = " + JSON.toJSONString(config));
		return SelectTable2.getInstance().select(config);//SelectTable3.getInstance().select(config);//
	}

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public QueryConfig getQueryConfig(String table, JSONObject request) {
		return QueryConfig.getQueryConfig(table, request);
	}
	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public QueryConfig newQueryConfig(String table, JSONObject request) {
		return QueryConfig.getQueryConfig(table, request);
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
		return key.endsWith("[]");//[key]改为了key[]，更符合常规逻辑。 key.startsWith("[") && key.endsWith("]");
	}

}
