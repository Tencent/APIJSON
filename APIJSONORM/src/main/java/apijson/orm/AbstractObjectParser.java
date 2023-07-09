/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.AbstractFunctionParser.FunctionBean;
import apijson.orm.exception.ConflictException;
import apijson.orm.exception.CommonException;
import apijson.orm.exception.NotExistException;
import apijson.orm.exception.UnsupportedDataTypeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.rmi.ServerException;
import java.util.*;
import java.util.Map.Entry;

import static apijson.JSONObject.KEY_COMBINE;
import static apijson.JSONObject.KEY_DROP;
import static apijson.JSONObject.KEY_TRY;
import static apijson.RequestMethod.POST;
import static apijson.RequestMethod.PUT;
import static apijson.orm.SQLConfig.TYPE_ITEM;
import static apijson.RequestMethod.GET;

/**简化Parser，getObject和getArray(getArrayConfig)都能用
 * @author Lemon
 */
public abstract class AbstractObjectParser implements ObjectParser {
	private static final String TAG = "AbstractObjectParser";

	@NotNull
	protected AbstractParser<?> parser;
	public AbstractObjectParser setParser(AbstractParser<?> parser) {
		this.parser = parser;
		return this;
	}


	protected JSONObject request;//不用final是为了recycle
	protected String parentPath;//不用final是为了recycle
	protected SQLConfig arrayConfig;//不用final是为了recycle
	protected boolean isSubquery;

	protected final int type;
	protected final String arrayTable;
	protected final List<Join> joinList;
	protected final boolean isTable;
	protected final boolean isArrayMainTable;

	protected final boolean tri;
	/**
	 * TODO Parser内要不因为 非 TYPE_ITEM_CHILD_0 的Table 为空导致后续中断。
	 */
	protected final boolean drop;

	/**for single object
	 */
	public AbstractObjectParser(@NotNull JSONObject request, String parentPath, SQLConfig arrayConfig
			, boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
		if (request == null) {
			throw new IllegalArgumentException(TAG + ".ObjectParser  request == null!!!");
		}
		this.request = request;
		this.parentPath = parentPath;

		this.arrayConfig = arrayConfig;
		this.isSubquery = isSubquery;

		this.type = arrayConfig == null ? 0 : arrayConfig.getType();
		this.arrayTable = arrayConfig == null ? null : arrayConfig.getTable();
		this.joinList = arrayConfig == null ? null : arrayConfig.getJoinList();

		this.isTable = isTable; // apijson.JSONObject.isTableKey(table);
		this.isArrayMainTable = isArrayMainTable; // isSubquery == false && this.isTable && this.type == SQLConfig.TYPE_ITEM_CHILD_0 && RequestMethod.isGetMethod(method, true);
		//		this.isReuse = isReuse; // isArrayMainTable && arrayConfig != null && arrayConfig.getPosition() > 0;

		this.objectCount = 0;
		this.arrayCount = 0;

		boolean isEmpty = request.isEmpty();//empty有效 User:{}
		if (isEmpty) {
			this.tri = false;
			this.drop = false;
		}
		else {
			this.tri = request.getBooleanValue(KEY_TRY);
			this.drop = request.getBooleanValue(KEY_DROP);

			request.remove(KEY_TRY);
			request.remove(KEY_DROP);
		}

        if (isTable) {
            String raw = request.getString(JSONRequest.KEY_RAW);
            String[] rks = StringUtil.split(raw);
            rawKeyList = rks == null || rks.length <= 0 ? null : Arrays.asList(rks);
        }
    }

	@Override
	public String getParentPath() {
		return parentPath;
	}

	@Override
	public AbstractObjectParser setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}

	protected int position;
	public int getPosition() {
		return position;
	}
	public AbstractObjectParser setPosition(int position) {
		this.position = position;
		return this;
	}


	private boolean invalidate = false;
	public void invalidate() {
		invalidate = true;
	}
	public boolean isInvalidate() {
		return invalidate;
	}

	private boolean breakParse = false;
	public void breakParse() {
		breakParse = true;
	}
	public boolean isBreakParse() {
		return breakParse || isInvalidate();
	}

	protected String name;
	protected String table;
	protected String alias;
	protected boolean isReuse;
	protected String path;

	protected JSONObject response;
	protected JSONObject sqlRequest;
	protected JSONObject sqlResponse;
	/**
	 * 自定义关键词
	 */
	protected Map<String, Object> customMap;
	/**
	 * 远程函数
	 * {"-":{ "key-()":value }, "0":{ "key()":value }, "+":{ "key+()":value } }
	 * - : 在executeSQL前解析
	 * 0 : 在executeSQL后、onChildParse前解析
	 * + : 在onChildParse后解析
	 */
	protected Map<String, Map<String, String>> functionMap;
	/**
	 * 子对象
	 */
	protected Map<String, JSONObject> childMap;

	private int objectCount;
	private int arrayCount;

    private List<String> rawKeyList;
	/**解析成员
	 * response重新赋值
	 * @return null or this
	 * @throws Exception
	 */
	@Override
	public AbstractObjectParser parse(String name, boolean isReuse) throws Exception {
		if (isInvalidate() == false) {
			this.isReuse = isReuse;
			this.name = name;
			this.path = AbstractParser.getAbsPath(parentPath, name);

			apijson.orm.Entry<String, String> tentry = Pair.parseEntry(name, true);
			this.table = tentry.getKey();
			this.alias = tentry.getValue();

			Log.d(TAG, "AbstractObjectParser  parentPath = " + parentPath + "; name = " + name + "; table = " + table + "; alias = " + alias);
			Log.d(TAG, "AbstractObjectParser  type = " + type + "; isTable = " + isTable + "; isArrayMainTable = " + isArrayMainTable);
			Log.d(TAG, "AbstractObjectParser  isEmpty = " + request.isEmpty() + "; tri = " + tri + "; drop = " + drop);

			breakParse = false;

			response = new JSONObject(true); // must init
			sqlResponse = null; // must init

			if (isReuse == false) {
				sqlRequest = new JSONObject(true); // must init

				customMap = null; // must init
				functionMap = null; // must init
				childMap = null; // must init

				Set<Entry<String, Object>> set = request.isEmpty() ? null : new LinkedHashSet<>(request.entrySet());
				if (set != null && set.isEmpty() == false) { // 判断换取少几个变量的初始化是否值得？
					if (isTable) { // 非Table下必须保证原有顺序！否则 count,page 会丢, total@:"/[]/total" 会在[]:{}前执行！
						customMap = new LinkedHashMap<String, Object>();
						childMap = new LinkedHashMap<String, JSONObject>();
					}
					functionMap = new LinkedHashMap<String, Map<String, String>>();//必须执行

					// 条件 <<<<<<<<<<<<<<<<<<<
					List<String> whereList = null;
					if (method == PUT) { // 这里只有PUTArray需要处理  || method == DELETE) {
						String[] combine = StringUtil.split(request.getString(KEY_COMBINE));
						if (combine != null) {
							String w;
							for (int i = 0; i < combine.length; i++) { // 去除 &,|,! 前缀
								w = combine[i];
								if (w != null && (w.startsWith("&") || w.startsWith("|") || w.startsWith("!"))) {
									combine[i] = w.substring(1);
								}
							}
						}
						// Arrays.asList() 返回值不支持 add 方法！
						whereList = new ArrayList<String>(Arrays.asList(combine != null ? combine : new String[]{}));
						whereList.add(apijson.JSONRequest.KEY_ID);
						whereList.add(apijson.JSONRequest.KEY_ID_IN);
						//						whereList.add(apijson.JSONRequest.KEY_USER_ID);
						//						whereList.add(apijson.JSONRequest.KEY_USER_ID_IN);
					}
					// 条件>>>>>>>>>>>>>>>>>>>

					int index = 0;
                    // hasOtherKeyNotFun = false;

					for (Entry<String, Object> entry : set) {
						if (isBreakParse()) {
							break;
						}

						String key = entry == null ? null : entry.getKey();
						Object value = key == null ? null : entry.getValue();
						if (value == null) {
							continue;
						}

                        // 处理url crud, 将crud 转换为真实method
                        RequestMethod _method = this.parser.getRealMethod(method, key, value);
						// 没有执行校验流程的情况,比如url head, sql@子查询, sql@ method=GET

						Object obj = key.endsWith("@") ? request.get(key) : null;
						if (obj instanceof JSONObject) {
							((JSONObject) obj).put(apijson.JSONObject.KEY_METHOD, GET);
						}

						try {
                            boolean startsWithAt = key.startsWith("@");
                            // if (startsWithAt || (key.endsWith("()") == false)) {
                            //     hasOtherKeyNotFun = true;
                            // }

							if (startsWithAt || key.endsWith("@") || (key.endsWith("<>") && value instanceof JSONObject)) {
								if (onParse(key, value) == false) {
									invalidate();
								}
							}
							else if (value instanceof JSONObject) {  // JSONObject，往下一级提取
								if (childMap != null) {  // 添加到childMap，最后再解析
									childMap.put(key, (JSONObject)value);
								}
								else {  // 直接解析并替换原来的，[]:{} 内必须直接解析，否则会因为丢掉count等属性，并且total@:"/[]/total"必须在[]:{} 后！
									response.put(key, onChildParse(index, key, (JSONObject)value));
									index ++;
								}
							}
							else if ((_method == POST || _method == PUT) && value instanceof JSONArray
									&& JSONRequest.isTableArray(key)) {  // JSONArray，批量新增或修改，往下一级提取
								onTableArrayParse(key, (JSONArray) value);
							}
							else if (_method == PUT && value instanceof JSONArray && (whereList == null || whereList.contains(key) == false)
									&& StringUtil.isName(key.replaceFirst("[+-]$", ""))) {  // PUT JSONArray
								onPUTArrayParse(key, (JSONArray) value);
							}
							else {  // JSONArray 或其它 Object，直接填充
								if (onParse(key, value) == false) {
									invalidate();
								}
							}
						} catch (Exception e) {
							if (tri == false) {
								throw CommonException.wrap(e, sqlConfig);  // 不忽略错误，抛异常
							}
							invalidate();  // 忽略错误，还原request
						}
					}

				}

				if (isTable) {
					if (parser.getGlobalDatabase() != null && sqlRequest.get(JSONRequest.KEY_DATABASE) == null) {
						sqlRequest.put(JSONRequest.KEY_DATABASE, parser.getGlobalDatabase());
					}
					if (parser.getGlobalSchema() != null && sqlRequest.get(JSONRequest.KEY_SCHEMA) == null) {
						sqlRequest.put(JSONRequest.KEY_SCHEMA, parser.getGlobalSchema());
					}
					if (parser.getGlobalDatasource() != null && sqlRequest.get(JSONRequest.KEY_DATASOURCE) == null) {
						sqlRequest.put(JSONRequest.KEY_DATASOURCE, parser.getGlobalDatasource());
					}

					if (isSubquery == false) { // 解决 SQL 语法报错，子查询不能 EXPLAIN
						if (parser.getGlobalExplain() != null && sqlRequest.get(JSONRequest.KEY_EXPLAIN) == null) {
							sqlRequest.put(JSONRequest.KEY_EXPLAIN, parser.getGlobalExplain());
						}
						if (parser.getGlobalCache() != null && sqlRequest.get(JSONRequest.KEY_CACHE) == null) {
							sqlRequest.put(JSONRequest.KEY_CACHE, parser.getGlobalCache());
						}
					}
				}
			}

			if (isTable) {  // 非Table内的函数会被滞后在onChildParse后调用
				onFunctionResponse("-");
			}

		}

		if (isInvalidate()) {
			recycle();
			return null;
		}

		return this;
	}




    //private boolean hasOtherKeyNotFun = false;

    /**解析普通成员
	 * @param key
	 * @param value
	 * @return whether parse succeed
	 */
	@Override
	public boolean onParse(@NotNull String key, @NotNull Object value) throws Exception {
		if (key.endsWith("@")) {  // StringUtil.isPath((String) value)) {
			// [] 内主表 position > 0 时，用来生成 SQLConfig 的键值对全都忽略，不解析
			if (value instanceof JSONObject) {  // key{}@ getRealKey, SQL 子查询对象，JSONObject -> SQLConfig.getSQL
				String replaceKey = key.substring(0, key.length() - 1);

				JSONObject subquery = (JSONObject) value;
				String range = subquery.getString(JSONRequest.KEY_SUBQUERY_RANGE);
				if (range != null && JSONRequest.SUBQUERY_RANGE_ALL.equals(range) == false
                        && JSONRequest.SUBQUERY_RANGE_ANY.equals(range) == false) {
					throw new IllegalArgumentException("子查询 " + path + "/" + key + ":{ range:value } 中 value 只能为 ["
                            + JSONRequest.SUBQUERY_RANGE_ALL + ", " + JSONRequest.SUBQUERY_RANGE_ANY + "] 中的一个！");
				}

				JSONArray arr = parser.onArrayParse(subquery, path, key, true);

				JSONObject obj = arr == null || arr.isEmpty() ? null : arr.getJSONObject(0);
				if (obj == null) {
					throw new Exception("服务器内部错误，解析子查询 " + path + "/" + key + ":{ } 为 Subquery 对象失败！");
				}

				String from = subquery.getString(JSONRequest.KEY_SUBQUERY_FROM);
				boolean isEmpty = StringUtil.isEmpty(from);
				JSONObject arrObj = isEmpty ? null : obj.getJSONObject(from);
				if (isEmpty) {
					Set<Entry<String, Object>> set = obj.entrySet();
					for (Entry<String, Object> e : set) {
						String k = e == null ? null : e.getKey();
						Object v = k == null ? null : e.getValue();
						if (v instanceof JSONObject && JSONRequest.isTableKey(k)) {
							from = k;
							arrObj = (JSONObject) v;
							break;
						}
					}
				}

				if (arrObj == null) {
					throw new IllegalArgumentException("子查询 " + path + "/"
                            + key + ":{ from:value } 中 value 对应的主表对象 " + from + ":{} 不存在！");
				}

				SQLConfig cfg = (SQLConfig) arrObj.get(AbstractParser.KEY_CONFIG);
				if (cfg == null) {
					throw new NotExistException(TAG + ".onParse  cfg == null");
				}

				Subquery s = new Subquery();
				s.setPath(path);
				s.setOriginKey(key);
				s.setOriginValue(subquery);

				s.setFrom(from);
				s.setRange(range);
				s.setKey(replaceKey);
				s.setConfig(cfg);

				key = replaceKey;
				value = s; //(range == null || range.isEmpty() ? "" : "range") + "(" + cfg.getSQL(false) + ") ";

				parser.putQueryResult(AbstractParser.getAbsPath(path, key), s); //字符串引用保证不了安全性 parser.getSQL(cfg));
			}
			else if (value instanceof String) { // //key{}@ getRealKey, 引用赋值路径
				String replaceKey = key.substring(0, key.length() - 1);

				// System.out.println("getObject  key.endsWith(@) >> parseRelation = " + parseRelation);
				String targetPath = AbstractParser.getValuePath(type == TYPE_ITEM ? path : parentPath, (String) value);

				// 先尝试获取，尽量保留缺省依赖路径，这样就不需要担心路径改变
				Object target = onReferenceParse(targetPath);
				Log.i(TAG, "onParse targetPath = " + targetPath + "; target = " + target);

				if (target == null) { // String#equals(null)会出错
					Log.d(TAG, "onParse  target == null  >>  return true;");
					return true;
				}

//				if (target instanceof Map) { // target 可能是从 requestObject 里取出的 {}
//					if (isTable || targetPath.endsWith("[]/" + JSONResponse.KEY_INFO) == false) {
//						Log.d(TAG, "onParse  target instanceof Map  >>  return false;");
//						return false; // FIXME 这个判断现在来看是否还有必要？为啥不允许为 JSONObject ？以前可能因为防止二次遍历再解析，现在只有一次遍历
//					}
//				}
//
//				// FIXME 这个判断现在来看是否还有必要？为啥不允许为 JSONObject ？以前可能因为防止二次遍历再解析，现在只有一次遍历
//				if (targetPath.equals(target)) { // 必须 valuePath 和保证 getValueByPath 传进去的一致！
//					Log.d(TAG, "onParse  targetPath.equals(target)  >>");
//
//					//非查询关键词 @key 不影响查询，直接跳过
//					if (isTable && (key.startsWith("@") == false || JSONRequest.TABLE_KEY_LIST.contains(key))) {
//						Log.e(TAG, "onParse  isTable && (key.startsWith(@) == false"
//								+ " || JSONRequest.TABLE_KEY_LIST.contains(key)) >>  return null;");
//						return false;//获取不到就不用再做无效的query了。不考虑 Table:{Table:{}}嵌套
//					} else {
//						Log.d(TAG, "onParse  isTable(table) == false >> return true;");
//						return true;//舍去，对Table无影响
//					}
//				}

				// 直接替换原来的 key@: path 为 key: target
				Log.i(TAG, "onParse    >>  key = replaceKey; value = target;");
				key = replaceKey;
				value = target;
				Log.d(TAG, "onParse key = " + key + "; value = " + value);
			}
			else {
				throw new IllegalArgumentException(path + "/" + key + ":value 中 value 必须为 依赖路径String 或 SQL子查询JSONObject ！");
			}
		}

		if (key.endsWith("()")) {
			if (value instanceof String == false) {
				throw new IllegalArgumentException(path + "/" + key + ":value 中 value 必须为函数String！");
			}

			String k = key.substring(0, key.length() - 2);

			String type; //远程函数比较少用，一般一个Table:{}内用到也就一两个，所以这里用 "-","0","+" 更直观，转用 -1,0,1 对性能提升不大。
			boolean isMinus = k.endsWith("-");
            boolean isPlus = isMinus == false && k.endsWith("+");
			if (isMinus) { //不能封装到functionMap后批量执行，否则会导致非Table内的 key-():function() 在onChildParse后执行！
				type = "-";
				k = k.substring(0, k.length() - 1);
			}
			else if (isPlus) {
				type = "+";
				k = k.substring(0, k.length() - 1);
			}
			else {
				type = "0";
			}

            if (isPlus == false && isTable == false) {
                parseFunction(key, k, (String) value, parentPath, name, request, isMinus);
            }
            else {
				//远程函数比较少用，一般一个Table:{}内用到也就一两个，所以这里循环里new出来对性能影响不大。
				Map<String, String> map = functionMap.get(type);
				if (map == null) {
					map = new LinkedHashMap<>();
				}
				map.put(k, (String) value);

				functionMap.put(type, map);
			}
		}
		else if (isTable && key.startsWith("@") && JSONRequest.TABLE_KEY_LIST.contains(key) == false) {
            customMap.put(key, value);
		}
		else {
			sqlRequest.put(key, value);
		}

		return true;
	}



	/**
	 * @param index
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSON onChildParse(int index, String key, JSONObject value) throws Exception {
		boolean isFirst = index <= 0;
		boolean isMain = isFirst && type == TYPE_ITEM;

		JSON child;
		boolean isEmpty;

		if (apijson.JSONObject.isArrayKey(key)) {//APIJSON Array
			if (isMain) {
				throw new IllegalArgumentException(parentPath + "/" + key + ":{} 不合法！"
						+ "数组 []:{} 中第一个 key:{} 必须是主表 TableKey:{} ！不能为 arrayKey[]:{} ！");
			}

			if (arrayConfig == null || arrayConfig.getPosition() == 0) {
				arrayCount ++;
				int maxArrayCount = parser.getMaxArrayCount();
				if (arrayCount > maxArrayCount) {
					throw new IllegalArgumentException(path + " 内截至 " + key + ":{} 时数组对象 key[]:{} "
                            + "的数量达到 " + arrayCount + " 已超限，必须在 0-" + maxArrayCount + " 内 !");
				}
			}

			child = parser.onArrayParse(value, path, key, isSubquery);
			isEmpty = child == null || ((JSONArray) child).isEmpty();
		}
		else { //APIJSON Object
			boolean isTableKey = JSONRequest.isTableKey(Pair.parseEntry(key, true).getKey());
			if (type == TYPE_ITEM && isTableKey == false) {
				throw new IllegalArgumentException(parentPath + "/" + key + ":{} 不合法！"
						+ "数组 []:{} 中每个 key:{} 都必须是表 TableKey:{} 或 数组 arrayKey[]:{} ！");
			}

			if ( //避免使用 "test":{"Test":{}} 绕过限制，实现查询爆炸   isTableKey &&
					(arrayConfig == null || arrayConfig.getPosition() == 0)) {
				objectCount ++;
				int maxObjectCount = parser.getMaxObjectCount();
				if (objectCount > maxObjectCount) {  //TODO 这里判断是批量新增/修改，然后上限为 maxUpdateCount
					throw new IllegalArgumentException(path + " 内截至 " + key + ":{} 时对象"
							+ " key:{} 的数量达到 " + objectCount + " 已超限，必须在 0-" + maxObjectCount + " 内 !");
				}
			}

			child = parser.onObjectParse(value, path, key, isMain ? arrayConfig.setType(SQLConfig.TYPE_ITEM_CHILD_0) : null, isSubquery);

			isEmpty = child == null || ((JSONObject) child).isEmpty();
			if (isFirst && isEmpty) {
				invalidate();
			}
		}
//		Log.i(TAG, "onChildParse  ObjectParser.onParse  key = " + key + "; child = " + child);

		return isEmpty ? null : child;//只添加! isChildEmpty的值，可能数据库返回数据不够count
	}


	//TODO 改用 MySQL json_add,json_remove,json_contains 等函数！不过就没有具体报错了，或许可以新增功能符，或者直接调 SQL 函数

	/**PUT key:[]
	 * @param key
	 * @param array
	 * @throws Exception
	 */
	@Override
	public void onPUTArrayParse(@NotNull String key, @NotNull JSONArray array) throws Exception {
		if (isTable == false || array.isEmpty()) {
			sqlRequest.put(key, array);
			Log.e(TAG, "onPUTArrayParse  isTable == false || array == null || array.isEmpty() >> return;");
			return;
		}

		int putType = 0;
		if (key.endsWith("+")) {//add
			putType = 1;
		} else if (key.endsWith("-")) {//remove
			putType = 2;
		} else {//replace
			sqlRequest.put(key, array);
			return;
		}
		String realKey = AbstractSQLConfig.getRealKey(method, key, false, false);

		//GET > add all 或 remove all > PUT > remove key

		//GET <<<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject rq = new JSONObject(true);
		rq.put(JSONRequest.KEY_ID, request.get(JSONRequest.KEY_ID));
		rq.put(JSONRequest.KEY_COLUMN, realKey);
		JSONObject rp = parseResponse(RequestMethod.GET, table, null, rq, null, false);
		//GET >>>>>>>>>>>>>>>>>>>>>>>>>


		//add all 或 remove all <<<<<<<<<<<<<<<<<<<<<<<<<
		Object target = rp == null ? null : rp.get(realKey);
		if (target instanceof String) {
			try {
				target = JSON.parse((String) target);
			} catch (Throwable e) {
				if (Log.DEBUG) {
					Log.e(TAG, "try {\n" +
							"\t\t\t\ttarget = JSON.parse((String) target);\n" +
							"\t\t\t}\n" +
							"\t\t\tcatch (Throwable e) = " + e.getMessage());
				}
			}
		}

		if (apijson.JSON.isBooleanOrNumberOrString(target)) {
			throw new NullPointerException("PUT " + path + ", " + realKey + " 类型为 " + target.getClass().getSimpleName() + "，"
					+ "不支持 Boolean, String, Number 等类型字段使用 'key+': [] 或 'key-': [] ！"
					+ "对应字段在数据库的值必须为 JSONArray, JSONObject 中的一种！"
					+ "值为 JSONObject 类型时传参必须是 'key+': [{'key': value, 'key2': value2}] 或 'key-': ['key', 'key2'] ！"
			);
		}

		boolean isAdd = putType == 1;

		Collection<Object> targetArray = target instanceof Collection ? (Collection<Object>) target : null;
		Map<String, ?> targetObj = target instanceof Map ? (Map<String, Object>) target : null;

		if (targetArray == null && targetObj == null) {
			if (isAdd == false) {
				throw new NullPointerException("PUT " + path + ", " + realKey + (target == null ? " 值为 null，不支持移除！"
						: " 类型为 " + target.getClass().getSimpleName() + "，不支持这样移除！")
						+ "对应字段在数据库的值必须为 JSONArray, JSONObject 中的一种，且 key- 移除时，本身的值不能为 null！"
						+ "值为 JSONObject 类型时传参必须是 'key+': [{'key': value, 'key2': value2}] 或 'key-': ['key', 'key2'] ！"
				);
			}

			targetArray = new JSONArray();
		}

		for (int i = 0; i < array.size(); i++) {
			Object obj = array.get(i);
			if (obj == null) {
				continue;
			}

			if (isAdd) {
				if (targetArray != null) {
					if (targetArray.contains(obj)) {
						throw new ConflictException("PUT " + path + ", " + key + "/" + i + " 已存在！");
					}
					targetArray.add(obj);
				} else {
					if (obj != null && obj instanceof Map == false) {
						throw new ConflictException("PUT " + path + ", " + key + "/" + i + " 必须为 JSONObject {} ！");
					}
					targetObj.putAll((Map) obj);
				}
			} else {
				if (targetArray != null) {
					if (targetArray.contains(obj) == false) {
						throw new NullPointerException("PUT " + path + ", " + key + "/" + i + " 不存在！");
					}
					targetArray.remove(obj);
				} else {
					if (obj instanceof String == false) {
						throw new ConflictException("PUT " + path + ", " + key + "/" + i + " 必须为 String 类型 ！");
					}
					if (targetObj.containsKey(obj) == false) {
						throw new NullPointerException("PUT " + path + ", " + key + "/" + i + " 不存在！");
					}
					targetObj.remove(obj);
				}
			}
		}

		//add all 或 remove all >>>>>>>>>>>>>>>>>>>>>>>>>

		//PUT <<<<<<<<<<<<<<<<<<<<<<<<<
		sqlRequest.put(realKey, targetArray != null ? targetArray : JSON.toJSONString(targetObj, SerializerFeature.WriteMapNullValue));
		//PUT >>>>>>>>>>>>>>>>>>>>>>>>>

	}


	@Override
	public void onTableArrayParse(String key, JSONArray valueArray) throws Exception {
		String childKey = key.substring(0, key.length() - JSONRequest.KEY_ARRAY.length());

		int allCount = 0;
		JSONArray ids = new JSONArray();

		int version = parser.getVersion();
		int maxUpdateCount = parser.getMaxUpdateCount();

        SQLConfig cfg = null; // 不能污染当前的配置 getSQLConfig();
        if (cfg == null) { // TODO 每次都创建成本比较高，是否新增 defaultInstance 或者 configInstance 用来专门 getIdKey 等？
            cfg = parser.createSQLConfig();
        }

		String idKey = cfg.getIdKey(); //Table[]: [{}] arrayConfig 为 null
		boolean isNeedVerifyContent = parser.isNeedVerifyContent();

        cfg.setTable(childKey); // Request 表 structure 中配置 "ALLOW_PARTIAL_UPDATE_FAILED": "Table[],key[],key:alias[]" 自动配置
        boolean allowPartialFailed = cfg.allowPartialUpdateFailed();
        JSONArray failedIds = allowPartialFailed ? new JSONArray() : null;

        int firstFailIndex = -1;
        JSONObject firstFailReq = null;
        Throwable firstFailThrow = null;
		for (int i = 0; i < valueArray.size(); i++) { //只要有一条失败，则抛出异常，全部失败
			//TODO 改成一条多 VALUES 的 SQL 性能更高，报错也更会更好处理，更人性化
			JSONObject item;
			try {
				item = valueArray.getJSONObject(i);
                if (item == null) {
                    throw new NullPointerException();
                }
			}
			catch (Exception e) {
				throw new UnsupportedDataTypeException(
                        "批量新增/修改失败！" + key + "/" + i + ":value 中value不合法！类型必须是 OBJECT ，结构为 {} !"
                );
			}

            Object id = item.get(idKey);
			JSONObject req = new JSONRequest(childKey, item);
            JSONObject result = null;
            try {
                if (isNeedVerifyContent) {
                    req = parser.parseCorrectRequest(method, childKey, version, "", req, maxUpdateCount, parser);
                }
                //parser.getMaxSQLCount() ? 可能恶意调用接口，把数据库拖死
                result = (JSONObject) onChildParse(0, "" + i, req);
            }
            catch (Exception e) {
                if (allowPartialFailed == false) {
                    throw e;
                }

                if (firstFailThrow == null) {
                    firstFailThrow = e;
                    firstFailReq = valueArray.getJSONObject(i); // item
                }
            }

            result = result == null ? null : result.getJSONObject(childKey);

            boolean success = JSONResponse.isSuccess(result);
            int count = result == null ? 0 : result.getIntValue(JSONResponse.KEY_COUNT);
            if (id == null && result != null) {
                id = result.get(idKey);
            }

            if (success == false || count != 1) { //如果 code = 200 但 count != 1，不能算成功，掩盖了错误不好排查问题
                if (allowPartialFailed) {
                    failedIds.add(id);
                    if (firstFailIndex < 0) {
                        firstFailIndex = i;
                    }
                }
                else {
                    throw new ServerException(
                            "批量新增/修改失败！" + key + "/" + i + "：" + (success ? "成功但 count != 1 ！"
                                    : (result == null ? "null" : result.getString(JSONResponse.KEY_MSG))
                    ));
                }
            }

			allCount += 1; // 加了 allowPartialFailed 后 count 可能为 0  allCount += count;
			ids.add(id);
		}

        int failedCount = failedIds == null ? 0 : failedIds.size();
        if (failedCount > 0 && failedCount >= allCount) {
            throw new ServerException("批量新增/修改 " + key + ":[] 中 " + allCount + " 个子项全部失败！"
                    + "第 " + firstFailIndex + " 项失败原因：" + (firstFailThrow == null ? "" : firstFailThrow.getMessage()));
        }

        JSONObject allResult = AbstractParser.newSuccessResult();
        if (failedCount > 0) {
            allResult.put("failedCount", failedCount);
            allResult.put("failedIdList", failedIds);

            JSONObject failObj = new JSONObject(true);
            failObj.put("index", firstFailIndex);
            failObj.put(childKey, firstFailReq);

            if (firstFailThrow instanceof CommonException && firstFailThrow.getCause() != null) {
                firstFailThrow = firstFailThrow.getCause();
            }
            JSONObject obj = firstFailThrow == null ? failObj : AbstractParser.extendErrorResult(failObj, firstFailThrow, parser.isRoot());
            if (Log.DEBUG && firstFailThrow != null) {
                obj.put("trace:throw", firstFailThrow.getClass().getName());
                obj.put("trace:stack", firstFailThrow.getStackTrace());
            }

            allResult.put("firstFailed", obj);
        }
        allResult.put(JSONResponse.KEY_COUNT, allCount);
        allResult.put(idKey + "[]", ids);

		response.put(childKey, allResult); //不按原样返回，避免数据量过大
	}


	@Override
	public JSONObject parseResponse(RequestMethod method, String table, String alias
            , JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
		SQLConfig config = newSQLConfig(method, table, alias, request, joinList, isProcedure)
				.setParser(parser)
				.setObjectParser(this);
		return parseResponse(config, isProcedure);
	}
	@Override
	public JSONObject parseResponse(SQLConfig config, boolean isProcedure) throws Exception {
		if (parser.getSQLExecutor() == null) {
			parser.createSQLExecutor();
		}
		return parser.getSQLExecutor().execute(config, isProcedure);
	}


	@Override
	public SQLConfig newSQLConfig(boolean isProcedure) throws Exception {
		return newSQLConfig(method, table, alias, sqlRequest, joinList, isProcedure)
				.setParser(parser)
				.setObjectParser(this);
	}

	/**SQL 配置，for single object
	 * @return {@link #setSQLConfig(int, int, int)}
	 * @throws Exception
	 */
	@Override
	public AbstractObjectParser setSQLConfig() throws Exception {
		return setSQLConfig(RequestMethod.isQueryMethod(method) ? 1 : 0, 0, 0);
	}

	@Override
	public AbstractObjectParser setSQLConfig(int count, int page, int position) throws Exception {
		if (isTable == false || isReuse) {
			return setPosition(position);
		}

		if (sqlConfig == null) {
			try {
				sqlConfig = newSQLConfig(false);
			}
			catch (Exception e) {
				if (e instanceof NotExistException || (e instanceof CommonException && e.getCause() instanceof NotExistException)) {
					return this;
				}
				throw e;
			}
		}
		sqlConfig.setCount(sqlConfig.getCount() <= 0 ? count : sqlConfig.getCount()).setPage(page).setPosition(position);

		parser.onVerifyRole(sqlConfig);

		return this;
	}




	protected SQLConfig sqlConfig = null;//array item复用
	/**SQL查询，for array item
	 * @return this
	 * @throws Exception
	 */
	@Override
	public AbstractObjectParser executeSQL() throws Exception {
		//执行SQL操作数据库
		if (isTable == false) {//提高性能
			sqlResponse = new JSONObject(sqlRequest);
		}
		else {
            try {
                sqlResponse = onSQLExecute();
            }
            catch (Exception e) {
                if (e instanceof NotExistException || (e instanceof CommonException && e.getCause() instanceof NotExistException)) {
                    //				Log.e(TAG, "getObject  try { response = getSQLObject(config2); } catch (Exception e) {");
                    //				if (e instanceof NotExistException) {//非严重异常，有时候只是数据不存在
                    //					//						e.printStackTrace();
                    sqlResponse = null;//内部吃掉异常，put到最外层
                    //						requestObject.put(JSONResponse.KEY_MSG
                    //								, StringUtil.getString(requestObject.get(JSONResponse.KEY_MSG)
                    //										+ "; query " + path + " cath NotExistException:"
                    //										+ newErrorResult(e).getString(JSONResponse.KEY_MSG)));
                    //				} else {
                    //					throw e;
                    //				}
                }
                else {
                    throw e;
                }
            }
        }

        if (drop) {//丢弃Table，只为了向下提供条件
            sqlResponse = null;
        }

        return this;
	}

	/**
	 * @return response
	 * @throws Exception
	 */
	@Override
	public JSONObject response() throws Exception {
		if (sqlResponse == null || sqlResponse.isEmpty()) {
			if (isTable) {//Table自身都获取不到值，则里面的Child都无意义，不需要再解析
				return null;  // response;
			}
		} else {
			response.putAll(sqlResponse);
		}


		//把isTable时取出去的custom重新添加回来
		if (customMap != null) {
			response.putAll(customMap);
		}


		onFunctionResponse("0");

		onChildResponse();

		onFunctionResponse("+");

		onComplete();

		return response;
	}


	@Override
	public void onFunctionResponse(String type) throws Exception {
		Map<String, String> map = functionMap == null ? null : functionMap.get(type);

		//解析函数function
		Set<Entry<String, String>> functionSet = map == null ? null : map.entrySet();
		if (functionSet != null && functionSet.isEmpty() == false) {
            boolean isMinus = "-".equals(type);
			JSONObject json = isMinus ? sqlRequest : response; // key-():function 是实时执行，而不是在这里批量执行

			for (Entry<String, String> entry : functionSet) {
                parseFunction(entry.getKey(), entry.getKey(), entry.getValue(), parentPath, name, json, isMinus);
			}
		}
	}


	//public void parseFunction(String key, String value, String parentPath, String currentName, JSONObject currentObject) throws Exception {
    //    parseFunction(key, value, parentPath, currentName, currentObject, false);
    //}
	public void parseFunction(String rawKey, String key, String value, String parentPath
            , String currentName, JSONObject currentObject, boolean isMinus) throws Exception {
		Object result;
        boolean containRaw = rawKeyList != null && rawKeyList.contains(rawKey);

        boolean isProcedure = key.startsWith("@");
		if (isProcedure) {
			FunctionBean fb = AbstractFunctionParser.parseFunction(value, currentObject, true, containRaw);

			SQLConfig config = newSQLConfig(true);
            String sch = fb.getSchema();
            if (StringUtil.isNotEmpty(sch, true)) {
                config.setSchema(sch);
            }
			config.setProcedure(fb.toFunctionCallString(true));
			result = parseResponse(config, true);

			key = key.substring(1);
		}
		else {
			result = parser.onFunctionParse(key, value, parentPath, currentName, currentObject, containRaw);
		}

		String k = AbstractSQLConfig.getRealKey(method, key, false, false);

        if (isProcedure == false && isMinus) {
            if (result != null) {
                sqlRequest.put(k, result);
            } else {
                sqlRequest.remove(k);
            }
        }

        if (result != null) {
            response.put(k, result);
        } else {
            response.remove(k);
        }

        parser.putQueryResult(AbstractParser.getAbsPath(path, k), result);
	}

	@Override
	public void onChildResponse() throws Exception {
		//把isTable时取出去child解析后重新添加回来
		Set<Entry<String, JSONObject>> set = childMap == null ? null : childMap.entrySet();
		if (set != null) {
			int index = 0;
			for (Entry<String, JSONObject> entry : set) {
				Object child = entry == null ? null : onChildParse(index, entry.getKey(), entry.getValue());
				if (child == null
						|| (child instanceof JSONObject && ((JSONObject) child).isEmpty())
						|| (child instanceof JSONArray && ((JSONArray) child).isEmpty())
						) {
					continue;
				}

				response.put(entry.getKey(), child);
				index ++;
			}
		}
	}



	@Override
	public Object onReferenceParse(@NotNull String path) {
		return parser.getValueByPath(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject onSQLExecute() throws Exception {
		int position = getPosition();

		JSONObject result;
		if (isArrayMainTable && position > 0) {  // 数组主表使用专门的缓存数据
			result = parser.getArrayMainCacheItem(parentPath.substring(0, parentPath.lastIndexOf("[]") + 2), position);
		}
		else {
			result = parser.executeSQL(sqlConfig, isSubquery);

			boolean isSimpleArray = false;
            // 提取并缓存数组主表的列表数据
            List<JSONObject> rawList = result == null ? null : (List<JSONObject>) result.remove(AbstractSQLExecutor.KEY_RAW_LIST);

            if (isArrayMainTable && position == 0 && rawList != null) {

                isSimpleArray = (functionMap == null || functionMap.isEmpty())
                        && (customMap == null || customMap.isEmpty())
                        && (childMap == null || childMap.isEmpty())
                        && (table.equals(arrayTable));

                // APP JOIN 副表时副表返回了这个字段   rawList = (List<JSONObject>) result.remove(AbstractSQLExecutor.KEY_RAW_LIST);
                String arrayPath = parentPath.substring(0, parentPath.lastIndexOf("[]") + 2);

                if (isSimpleArray == false) {
                    long startTime = System.currentTimeMillis();

                    for (int i = 1; i < rawList.size(); i++) {  // 从 1 开始，0 已经处理过
                        JSONObject obj = rawList.get(i);

                        if (obj != null) {
                            parser.putQueryResult(arrayPath + "/" + i + "/" + name, obj);  // 解决获取关联数据时requestObject里不存在需要的关联数据
                        }
                    }

                    long endTime = System.currentTimeMillis();  // 3ms - 8ms
                    Log.e(TAG, "\n onSQLExecute <<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                            + "\n for (int i = 1; i < list.size(); i++)  startTime = " + startTime
                            + "; endTime = " + endTime + "; duration = " + (endTime - startTime)
                            + "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n ");
                }

                parser.putArrayMainCache(arrayPath, rawList);
            }

            if (isSubquery == false && result != null) {
                parser.putQueryResult(path, result);  // 解决获取关联数据时requestObject里不存在需要的关联数据

                if (isSimpleArray && rawList != null) {
                    result.put(AbstractSQLExecutor.KEY_RAW_LIST, rawList);
                }
            }
        }

		return result;
	}


	/**
	 * response has the final value after parse (and query if isTable)
	 */
	@Override
	public void onComplete() {
	}


	/**回收内存
	 */
	@Override
	public void recycle() {
		//后面还可能用到，要还原
		if (tri) {//避免返回未传的字段
			request.put(KEY_TRY, tri);
		}
		if (drop) {
			request.put(KEY_DROP, drop);
		}


		method = null;
		parentPath = null;
		arrayConfig = null;

		//		if (response != null) {
		//			response.clear();//有效果?
		//			response = null;
		//		}

		request = null;
		response = null;
		sqlRequest = null;
		sqlResponse = null;

		functionMap = null;
		customMap = null;
		childMap = null;
	}






	protected RequestMethod method;
	@Override
	public AbstractObjectParser setMethod(RequestMethod method) {
		if (this.method != method) {
			this.method = method;
			sqlConfig = null;
			//TODO ?			sqlResponse = null;
		}
		return this;
	}
	@Override
	public RequestMethod getMethod() {
		return method;
	}




	@Override
	public boolean isTable() {
		return isTable;
	}
	@Override
	public String getPath() {
		return path;
	}
	@Override
	public String getTable() {
		return table;
	}
	@Override
	public String getAlias() {
		return alias;
	}
	@Override
	public SQLConfig getArrayConfig() {
		return arrayConfig;
	}


	@Override
	public SQLConfig getSQLConfig() {
		return sqlConfig;
	}

	@Override
	public JSONObject getResponse() {
		return response;
	}
	@Override
	public JSONObject getSqlRequest() {
		return sqlRequest;
	}
	@Override
	public JSONObject getSqlResponse() {
		return sqlResponse;
	}

	@Override
	public Map<String, Object> getCustomMap() {
		return customMap;
	}
	@Override
	public Map<String, Map<String, String>> getFunctionMap() {
		return functionMap;
	}
	@Override
	public Map<String, JSONObject> getChildMap() {
		return childMap;
	}


}
