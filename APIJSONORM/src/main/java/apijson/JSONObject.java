/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

import java.util.*;


/**use this class instead of com.alibaba.fastjson.JSONObject
 * @author Lemon
 * @see #put
 * @see #puts
 * @see #putsAll
 */
public class JSONObject extends JSON implements Map<String, Object> {
	private static final String TAG = "JSONObject";

	private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

	/**ordered
	 */
	public JSONObject() {
		super();
	}
	/**transfer Object to JSONObject
	 * @param object
	 * @see {@link #JSONObject(Object)}
	 */
	public JSONObject(Object object) {
		this();
		if (object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object;
			putAll(map);
		} else if (object != null) {
			String json = JSON.toJSONString(object);
			if (json != null) {
				Map<String, Object> map = JSON.parseObject(json);
				if (map != null) {
					putAll(map);
				}
			}
		}
	}
	/**parse JSONObject with JSON String
	 * @param json
	 * @see {@link #JSONObject(String)}
	 */
	public JSONObject(String json) {
		this();
		Map<String, Object> map = JSON.parseObject(json);
		if (map != null) {
			putAll(map);
		}
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * @param object
	 * @see {@link #putsAll(Map<? extends String, ? extends Object>)}
	 */
	public JSONObject(Map<String, Object> object) {
		this();
		putsAll(object);
	}


	//judge <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public static final String KEY_ARRAY = "[]";

	/**判断是否为Array的key
	 * @param key
	 * @return
	 */
	public static boolean isArrayKey(String key) {
		return key != null && key.endsWith(KEY_ARRAY);
	}
	/**判断是否为对应Table的key
	 * @param key
	 * @return
	 */
	public static boolean isTableKey(String key) {
		return StringUtil.isBigName(key);
	}
	/**判断是否为对应Table数组的 key
	 * @param key
	 * @return
	 */
	public static boolean isTableArray(String key) {
		return isArrayKey(key) && isTableKey(key.substring(0, key.length() - KEY_ARRAY.length()));
	}
	//judge >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//JSONObject内关键词 key <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	public static String KEY_ID = "id";
	public static String KEY_ID_IN = KEY_ID + "{}";
	public static String KEY_USER_ID = "userId";
	public static String KEY_USER_ID_IN = KEY_USER_ID + "{}";

	/**set "id":id in Table layer
	 * @param id
	 * @return
	 */
	public JSONObject setId(Long id) {
		return puts(KEY_ID, id);
	}
	/**set "id{}":[] in Table layer
	 * @param list
	 * @return
	 */
	public JSONObject setIdIn(List<Object> list) {
		return puts(KEY_ID_IN, list);
	}

	/**set "userId":userId in Table layer
	 * @param id
	 * @return
	 */
	public JSONObject setUserId(Long id) {
		return puts(KEY_USER_ID, id);
	}
	/**set "userId{}":[] in Table layer
	 * @param list
	 * @return
	 */
	public JSONObject setUserIdIn(List<Object> list) {
		return puts(KEY_USER_ID_IN, list);
	}


	public static final int CACHE_ALL = 0;
	public static final int CACHE_ROM = 1;
	public static final int CACHE_RAM = 2;

	public static final String CACHE_ALL_STRING = "ALL";
	public static final String CACHE_ROM_STRING = "ROM";
	public static final String CACHE_RAM_STRING = "RAM";


	//@key关键字都放这个类 <<<<<<<<<<<<<<<<<<<<<<
	public static final String KEY_TRY = "@try"; //尝试，忽略异常
	public static final String KEY_CATCH = "@catch"; //TODO 捕捉到异常后，处理方式  null-不处理；DEFAULT-返回默认值；ORIGIN-返回请求里的原始值
	public static final String KEY_DROP = "@drop"; //丢弃，不返回，TODO 应该通过 fastjson 的 ignore 之类的机制来处理，避免导致下面的对象也不返回
	//	public static final String KEY_KEEP = "@keep"; //一定会返回，为 null 或 空对象时，会使用默认值(非空)，解决其它对象因为不关联的第一个对为空导致也不返回
	public static final String KEY_DEFULT = "@default"; //TODO 自定义默认值 { "@default":true }，@default 可完全替代 @keep
	public static final String KEY_NULL = "@null"; //TODO 值为 null 的键值对 "@null":"tag,pictureList"，允许 is NULL 条件判断， SET tag = NULL 修改值为 NULL 等
	public static final String KEY_CAST = "@cast"; //TODO 类型转换 cast(date AS DATE)

	public static final String KEY_ROLE = "@role"; //角色，拥有对某些数据的某些操作的权限
	public static final String KEY_DATABASE = "@database"; //数据库类型，默认为MySQL
	public static final String KEY_DATASOURCE = "@datasource"; //数据源
	public static final String KEY_NAMESPACE = "@namespace"; //命名空间，Table 在非默认 namespace 内时需要声明
	public static final String KEY_CATALOG = "@catalog"; //目录，Table 在非默认 catalog 内时需要声明
	public static final String KEY_SCHEMA = "@schema"; //数据库，Table 在非默认 schema 内时需要声明
	public static final String KEY_EXPLAIN = "@explain"; //分析 true/false
	public static final String KEY_CACHE = "@cache"; //缓存 RAM/ROM/ALL
	public static final String KEY_COLUMN = "@column"; //查询的Table字段或SQL函数
	public static final String KEY_FROM = "@from"; //FROM语句
	public static final String KEY_COMBINE = "@combine"; //条件组合，每个条件key前面可以放&,|,!逻辑关系  "id!{},&sex,!name&$"
	public static final String KEY_GROUP = "@group"; //分组方式
	public static final String KEY_HAVING = "@having"; //聚合函数条件，一般和@group一起用
	public static final String KEY_HAVING_AND = "@having&"; //聚合函数条件，一般和@group一起用
	public static final String KEY_SAMPLE = "@sample"; //取样方式
	public static final String KEY_LATEST = "@latest"; //最近方式
	public static final String KEY_PARTITION = "@partition"; //分区方式
	public static final String KEY_FILL = "@fill"; //填充方式
	public static final String KEY_ORDER = "@order"; //排序方式
	public static final String KEY_KEY = "@key"; // key 映射，year:left(date,4);name_tag:(name,tag)
	public static final String KEY_RAW = "@raw"; // 自定义原始 SQL 片段
	public static final String KEY_JSON = "@json"; //SQL Server 把字段转为 JSON 输出
	public static final String KEY_METHOD = "@method"; // json 对象配置操作方法
	public static final String KEY_GET = "@get"; // json 对象配置操作方法
	public static final String KEY_GETS = "@gets"; // json 对象配置操作方法
	public static final String KEY_HEAD = "@head"; // json 对象配置操作方法
	public static final String KEY_HEADS = "@heads"; // json 对象配置操作方法
	public static final String KEY_POST = "@post"; // json 对象配置操作方法
	public static final String KEY_PUT = "@put"; // json 对象配置操作方法
	public static final String KEY_DELETE = "@delete"; // json 对象配置操作方法

	public static final Map<String, RequestMethod> KEY_METHOD_ENUM_MAP;

	public static final List<String> TABLE_KEY_LIST;
	static {
		TABLE_KEY_LIST = new ArrayList<String>();
		TABLE_KEY_LIST.add(KEY_ROLE);
		TABLE_KEY_LIST.add(KEY_DATABASE);
		TABLE_KEY_LIST.add(KEY_DATASOURCE);
		TABLE_KEY_LIST.add(KEY_NAMESPACE);
		TABLE_KEY_LIST.add(KEY_CATALOG);
		TABLE_KEY_LIST.add(KEY_SCHEMA);
		TABLE_KEY_LIST.add(KEY_EXPLAIN);
		TABLE_KEY_LIST.add(KEY_CACHE);
		TABLE_KEY_LIST.add(KEY_COLUMN);
		TABLE_KEY_LIST.add(KEY_FROM);
		TABLE_KEY_LIST.add(KEY_NULL);
		TABLE_KEY_LIST.add(KEY_CAST);
		TABLE_KEY_LIST.add(KEY_COMBINE);
		TABLE_KEY_LIST.add(KEY_GROUP);
		TABLE_KEY_LIST.add(KEY_HAVING);
		TABLE_KEY_LIST.add(KEY_HAVING_AND);
		TABLE_KEY_LIST.add(KEY_SAMPLE);
		TABLE_KEY_LIST.add(KEY_LATEST);
		TABLE_KEY_LIST.add(KEY_PARTITION);
		TABLE_KEY_LIST.add(KEY_FILL);
		TABLE_KEY_LIST.add(KEY_ORDER);
		TABLE_KEY_LIST.add(KEY_KEY);
		TABLE_KEY_LIST.add(KEY_RAW);
		TABLE_KEY_LIST.add(KEY_JSON);
		TABLE_KEY_LIST.add(KEY_METHOD);
		TABLE_KEY_LIST.add(KEY_GET);
		TABLE_KEY_LIST.add(KEY_GETS);
		TABLE_KEY_LIST.add(KEY_HEAD);
		TABLE_KEY_LIST.add(KEY_HEADS);
		TABLE_KEY_LIST.add(KEY_POST);
		TABLE_KEY_LIST.add(KEY_PUT);
		TABLE_KEY_LIST.add(KEY_DELETE);

		KEY_METHOD_ENUM_MAP = new LinkedHashMap<>();
		KEY_METHOD_ENUM_MAP.put(KEY_GET, RequestMethod.GET);
		KEY_METHOD_ENUM_MAP.put(KEY_GETS, RequestMethod.GETS);
		KEY_METHOD_ENUM_MAP.put(KEY_HEAD, RequestMethod.HEAD);
		KEY_METHOD_ENUM_MAP.put(KEY_HEADS, RequestMethod.HEADS);
		KEY_METHOD_ENUM_MAP.put(KEY_POST, RequestMethod.POST);
		KEY_METHOD_ENUM_MAP.put(KEY_PUT, RequestMethod.PUT);
		KEY_METHOD_ENUM_MAP.put(KEY_DELETE, RequestMethod.DELETE);
	}

	//@key关键字都放这个类 >>>>>>>>>>>>>>>>>>>>>>


	/**set try, ignore exceptions
	 * @param tri
	 * @return this
	 */
	public JSONObject setTry(Boolean tri) {
		return puts(KEY_TRY, tri);
	}

	/**set catch
	 * @param isCatch
	 * @return this
	 */
	public JSONObject setCatch(String isCatch) {
		return puts(KEY_CATCH, isCatch);
	}
	/**set drop, data dropped will not return
	 * @param drop
	 * @return this
	 */
	public JSONObject setDrop(Boolean drop) {
		return puts(KEY_DROP, drop);
	}

	/**set if has default
	 * @param hasDefault
	 * @return this
	 */
	public JSONObject setDefault(Boolean hasDefault) {
		return puts(KEY_DEFULT, hasDefault);
	}


	/**set role of request sender
	 * @param role
	 * @return this
	 */
	public JSONObject setRole(String role) {
		return puts(KEY_ROLE, role);
	}
	/**set database where table was puts
	 * @param database
	 * @return this
	 */
	public JSONObject setDatabase(String database) {
		return puts(KEY_DATABASE, database);
	}
	/**set datasource where table was puts
	 * @param datasource
	 * @return this
	 */
	public JSONObject setDatasource(String datasource) {
		return puts(KEY_DATASOURCE, datasource);
	}
	/**set namespace where table was puts
	 * @param namespace
	 * @return this
	 */
	public JSONObject setNamespace(String namespace) {
		return puts(KEY_NAMESPACE, namespace);
	}
	/**set catalog where table was puts
	 * @param catalog
	 * @return this
	 */
	public JSONObject setCatalog(String catalog) {
		return puts(KEY_CATALOG, catalog);
	}
	/**set schema where table was puts
	 * @param schema
	 * @return this
	 */
	public JSONObject setSchema(String schema) {
		return puts(KEY_SCHEMA, schema);
	}
	/**set if return explain informations
	 * @param explain
	 * @return
	 */
	public JSONObject setExplain(Boolean explain) {
		return puts(KEY_EXPLAIN, explain);
	}
	/**set cache type
	 * @param cache
	 * @return
	 * @see {@link #CACHE_ALL}
	 * @see {@link #CACHE_RAM}
	 * @see {@link #CACHE_ROM}
	 */
	public JSONObject setCache(Integer cache) {
		return puts(KEY_CACHE, cache);
	}
	/**set cache type
	 * @param cache
	 * @return
	 * @see {@link #CACHE_ALL_STRING}
	 * @see {@link #CACHE_RAM_STRING}
	 * @see {@link #CACHE_ROM_STRING}
	 */
	public JSONObject setCache(String cache) {
		return puts(KEY_CACHE, cache);
	}

	/**set keys need to be returned
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setColumn(String)}
	 */
	public JSONObject setColumn(String... keys) {
		return setColumn(StringUtil.get(keys, true));
	}
	/**set keys need to be returned
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setColumn(String keys) {
		return puts(KEY_COLUMN, keys);
	}

	/**set keys whose value is null
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setNull(String)}
	 */
	public JSONObject setNull(String... keys) {
		return setNull(StringUtil.get(keys, true));
	}
	/**set keys whose value is null
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setNull(String keys) {
		return puts(KEY_NULL, keys);
	}
	
	/**set keys and types whose value should be cast to type, cast(value AS DATE)
	 * @param keyTypes  key0:type0, key1:type1, key2:type2 ...
	 * @return {@link #setCast(String)}
	 */
	public JSONObject setCast(String... keyTypes) {
		return setCast(StringUtil.get(keyTypes, true));
	}
	/**set keys and types whose value should be cast to type, cast(value AS DATE)
	 * @param keyTypes  "key0:type0,key1:type1,key2:type2..."
	 * @return
	 */
	public JSONObject setCast(String keyTypes) {
		return puts(KEY_CAST, keyTypes);
	}

	/**set combination of keys for conditions
	 * @param keys  key0,&key1,|key2,!key3 ...  TODO or key0> | (key1{} & !key2)...
	 * @return {@link #setColumn(String)}
	 */
	public JSONObject setCombine(String... keys) {
		return setCombine(StringUtil.get(keys, true));
	}
	/**set combination of keys for conditions
	 * @param keys  key0,&key1,|key2,!key3 ...  TODO or key0> | (key1{} & !key2)...
	 * @return
	 */
	public JSONObject setCombine(String keys) {
		return puts(KEY_COMBINE, keys);
	}

	/**set keys for group by
	 * @param keys key0, key1, key2 ...
	 * @return {@link #setGroup(String)}
	 */
	public JSONObject setGroup(String... keys) {
		return setGroup(StringUtil.get(keys, true));
	}
	/**set keys for group by
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setGroup(String keys) {
		return puts(KEY_GROUP, keys);
	}

	/**set keys for having
	 * @param keys count(key0) > 1, sum(key1) <= 5, function2(key2) ? value2 ...
	 * @return {@link #setHaving(String)}
	 */
	public JSONObject setHaving(String... keys) {
		return setHaving(StringUtil.get(keys, true));
	}
	/**set keys for having
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setHaving(String keys) {
		return setHaving(keys, false);
	}
	/**set keys for having
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setHaving(String keys, boolean isAnd) {
		return puts(isAnd ? KEY_HAVING_AND : KEY_HAVING, keys);
	}

	/**set keys for sample by
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setSample(String)}
	 */
	public JSONObject setSample(String... keys) {
		return setSample(StringUtil.get(keys, true));
	}
	/**set keys for sample by
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setSample(String keys) {
		return puts(KEY_SAMPLE, keys);
	}

	/**set keys for latest on
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setLatest(String)}
	 */
	public JSONObject setLatest(String... keys) {
		return setLatest(StringUtil.get(keys, true));
	}
	/**set keys for latest on
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setLatest(String keys) {
		return puts(KEY_LATEST, keys);
	}

	/**set keys for partition by
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setPartition(String)}
	 */
	public JSONObject setPartition(String... keys) {
		return setPartition(StringUtil.get(keys, true));
	}
	/**set keys for partition by
	 * @param keys  key0, key1, key2 ...
	 * @return
	 */
	public JSONObject setPartition(String keys) {
		return puts(KEY_PARTITION, keys);
	}

	/**set keys for fill(key): fill(null), fill(linear), fill(prev)
	 * @param keys  key0, key1, key2 ...
	 * @return {@link #setFill(String)}
	 */
	public JSONObject setFill(String... keys) {
		return setFill(StringUtil.get(keys, true));
	}
	/**set keys for fill(key): fill(null), fill(linear), fill(prev)
	 * @param keys  key0, key1, key2 ...
	 * @return
	 */
	public JSONObject setFill(String keys) {
		return puts(KEY_FILL, keys);
	}

	/**set keys for order by
	 * @param keys  key0, key1+, key2- ...
	 * @return {@link #setOrder(String)}
	 */
	public JSONObject setOrder(String... keys) {
		return setOrder(StringUtil.get(keys, true));
	}
	/**set keys for order by
	 * @param keys  "key0,key1+,key2-..."
	 * @return
	 */
	public JSONObject setOrder(String keys) {
		return puts(KEY_ORDER, keys);
	}

	/**set key map
	 * @param keyMap  "name_tag:(name,tag);year:left(date,1,5)..."
	 * @return
	 */
	public JSONObject setKey(String keyMap) {
		return puts(KEY_KEY, keyMap);
	}

	/**set keys to raw
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setRaw(String keys) {
		return puts(KEY_RAW, keys);
	}

	/**set keys to cast to json
	 * @param keys  "key0,key1,key2..."
	 * @return
	 */
	public JSONObject setJson(String keys) {
		return puts(KEY_JSON, keys);
	}

	//JSONObject内关键词 key >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//Request <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**
	 * @param key
	 * @param keys  path = keys[0] + "/" + keys[1] + "/" + keys[2] + ...
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsPath(String key, String... keys) {
		return puts(key+"@", StringUtil.get(keys, "/"));
	}

	/**
	 * @param key
	 * @param isNull
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsNull(String key, boolean isNull) {
		return puts(key+"{}", SQL.isNull(isNull));
	}
	/**
	 * trim = false
	 * @param key
	 * @param isEmpty
	 * @return {@link #putsEmpty(String, boolean, boolean)}
	 */
	public JSONObject putsEmpty(String key, boolean isEmpty) {
		return putsEmpty(key, isEmpty, false);
	}
	/**
	 * @param key
	 * @param isEmpty
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsEmpty(String key, boolean isEmpty, boolean trim) {
		return puts(key+"{}", SQL.isEmpty(key, isEmpty, trim));
	}
	/**
	 * @param key
	 * @param compare <=0, >5 ...
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsLength(String key, String compare) {
		return puts(key+"{}", SQL.length(key) + compare);
	}
	/**
	 * @param key
	 * @param compare <=, > ...
	 * @param value 1, 5, 3.14, -99 ...
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsLength(String key, String compare, Object value) {
		return puts(key+"["+(StringUtil.isEmpty(compare) || "=".equals(compare) ? "" : ("!=".equals(compare) ? "!" : compare)), value);
	}
	/**
	 * @param key
	 * @param compare <=0, >5 ...
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsJSONLength(String key, String compare) {
		return puts(key+"{}", SQL.json_length(key) + compare);
	}
	/**
	 * @param key
	 * @param compare <=0, >5 ...
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsJSONLength(String key, String compare, Object value) {
		return puts(key + "{" + (StringUtil.isEmpty(compare) || "=".equals(compare) ? "" : ("!=".equals(compare) ? "!" : compare)), value);
	}

	/**设置搜索
	 * type = SEARCH_TYPE_CONTAIN_FULL
	 * @param key
	 * @param value
	 * @return {@link #putsSearch(String, String, int)}
	 */
	public JSONObject putsSearch(String key, String value) {
		return putsSearch(key, value, SQL.SEARCH_TYPE_CONTAIN_FULL);
	}
	/**设置搜索
	 * @param key
	 * @param value
	 * @param type
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject putsSearch(String key, String value, int type) {
		return puts(key+"$", SQL.search(value, type));
	}

	//Request >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**put and return this
	 * @param value  must be annotated by {@link MethodAccess}
	 * @return {@link #puts(String, Object)}
	 */
	public JSONObject puts(Object value) {
		return puts(null, value);
	}
	/**put and return this
	 * @param key
	 * @param value 
	 * @return this
	 * @see {@link #put(String, Object)}
	 */
	public JSONObject puts(String key, Object value) {
		put(key, value);
		return this;
	}

	/**put and return value
	 * @param value  must be annotated by {@link MethodAccess}
	 * @return {@link #put(String, Object)}
	 */
	public Object put(Object value) {
		return put(null, value);
	}
	/**put and return value
	 * @param key  StringUtil.isEmpty(key, true) ? key = value.getClass().getSimpleName();
	 * @param value
	 * @return value
	 */
	@Override
	public Object put(String key, Object value) {
		if (value == null) {
			Log.e(TAG, "put  value == null >> return null;");
			return null;
		}
		if (StringUtil.isEmpty(key, true)) {
			Class<?> clazz = value.getClass(); //should not return null
			if (clazz.getAnnotation(MethodAccess.class) == null) {
				throw new IllegalArgumentException("puts  StringUtil.isEmpty(key, true)" +
						" clazz.getAnnotation(MethodAccess.class) == null" +
						" \n key为空时仅支持 类型被@MethodAccess注解 的value !!!" +
						" \n 如果一定要这么用，请对 " + clazz.getName() + " 注解！" +
						" \n 如果是类似 key[]:{} 结构的请求，建议用 putsAll(...) ！");
			}
			key = value.getClass().getSimpleName();
		}

		return map.put(key, value);
	}

	/**puts key-value in object into this
	 * @param map
	 * @return this
	 */
	public JSONObject putsAll(Map<? extends String, ? extends Object> map) {
		putAll(map);
		return this;
	}


	/**
	 * Get a boolean value from the JSONObject
	 * @param key the key
	 * @return the boolean value or false if not found
	 */
	public boolean getBooleanValue(String key) {
		try {
			return JSON.getBooleanValue(this, key);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * Get an integer value from the JSONObject
	 * @param key the key
	 * @return the integer value or 0 if not found
	 */
	public int getIntValue(String key) {
		try {
			return JSON.getIntValue(this, key);
		} catch (IllegalArgumentException e) {
			return 0;
		}
	}
	
	/**
	 * Get a long value from the JSONObject
	 * @param key the key
	 * @return the long value or 0 if not found
	 */
	public long getLongValue(String key) {
		try {
			return JSON.getLongValue(this, key);
		} catch (IllegalArgumentException e) {
			return 0L;
		}
	}
	
	/**
	 * Get a double value from the JSONObject
	 * @param key the key
	 * @return the double value or 0 if not found
	 */
	public double getDoubleValue(String key) {
		try {
			return JSON.getDoubleValue(this, key);
		} catch (IllegalArgumentException e) {
			return 0.0;
		}
	}
	
	/**
	 * Get a string value from the JSONObject
	 * @param key the key
	 * @return the string value or null if not found
	 */
	public String getString(String key) {
		Object value = get(key);
		return value != null ? value.toString() : null;
	}
	
	/**
	 * Get a JSONObject value from the JSONObject
	 * @param key the key
	 * @return the JSONObject value or null if not found
	 */
	public JSONObject getJSONObject(String key) {
		try {
			Map<String, Object> map = JSON.getMap(this, key);
			return map != null ? new JSONObject(map) : null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	/**
	 * Get a JSONArray value from the JSONObject
	 * @param key the key
	 * @return the JSONArray value or null if not found
	 */
	public JSONArray getJSONArray(String key) {
		try {
			List<Object> list = JSON.getList(this, key);
			return list != null ? new JSONArray(list) : null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public int size() {
		return map.size();
	}

	/**
	 * Check if the JSONObject is empty or has no values other than null
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
		Set<? extends Entry<? extends String, ?>> set = map == null ? null : map.entrySet();
		if (set != null || set.isEmpty()) {
			return;
		}

		for (Entry<? extends String, ?> entry : set) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public String toString() {
		return JSON.toJSONString(map);
	}

}
