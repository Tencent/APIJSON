/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**use this class instead of com.alibaba.fastjson.JSONObject
 * @author Lemon
 * @see #put
 * @see #puts
 * @see #putsAll
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject {
	private static final long serialVersionUID = 1L;

	private static final String TAG = "JSONObject";


	/**ordered
	 */
	public JSONObject() {
		super(true);
	}
	/**transfer Object to JSONObject
	 * @param object
	 * @see {@link #JSONObject(Object)}
	 */
	public JSONObject(Object object) {
		this(toJSONString(object));
	}
	/**parse JSONObject with JSON String
	 * @param json
	 * @see {@link #JSONObject(String)}
	 */
	public JSONObject(String json) {
		this(parseObject(json));
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * @param object
	 * @see {@link #putsAll(Map<? extends String, ? extends Object>)}
	 */
	public JSONObject(com.alibaba.fastjson.JSONObject object) {
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
	public static final String KEY_SCHEMA = "@schema"; //数据库，Table在非默认schema内时需要声明
	public static final String KEY_DATASOURCE = "@datasource"; //数据源
	public static final String KEY_EXPLAIN = "@explain"; //分析 true/false
	public static final String KEY_CACHE = "@cache"; //缓存 RAM/ROM/ALL
	public static final String KEY_COLUMN = "@column"; //查询的Table字段或SQL函数
	public static final String KEY_FROM = "@from"; //FROM语句
	public static final String KEY_COMBINE = "@combine"; //条件组合，每个条件key前面可以放&,|,!逻辑关系  "id!{},&sex,!name&$"
	public static final String KEY_GROUP = "@group"; //分组方式
	public static final String KEY_HAVING = "@having"; //聚合函数条件，一般和@group一起用
	public static final String KEY_HAVING_AND = "@having&"; //聚合函数条件，一般和@group一起用
	public static final String KEY_ORDER = "@order"; //排序方式
	public static final String KEY_RAW = "@raw"; // 自定义原始 SQL 片段
	public static final String KEY_JSON = "@json"; //SQL Server 把字段转为 JSON 输出
	public static final String KEY_METHOD = "@method"; //json对象配置操作方法

	public static final List<String> TABLE_KEY_LIST;
	static {
		TABLE_KEY_LIST = new ArrayList<String>();
		TABLE_KEY_LIST.add(KEY_ROLE);
		TABLE_KEY_LIST.add(KEY_DATABASE);
		TABLE_KEY_LIST.add(KEY_SCHEMA);
		TABLE_KEY_LIST.add(KEY_DATASOURCE);
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
		TABLE_KEY_LIST.add(KEY_ORDER);
		TABLE_KEY_LIST.add(KEY_RAW);
		TABLE_KEY_LIST.add(KEY_JSON);
		TABLE_KEY_LIST.add(KEY_METHOD);
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
	/**set schema where table was puts
	 * @param schema
	 * @return this
	 */
	public JSONObject setSchema(String schema) {
		return puts(KEY_SCHEMA, schema);
	}
	/**set datasource where table was puts
	 * @param datasource
	 * @return this
	 */
	public JSONObject setDatasource(String datasource) {
		return puts(KEY_DATASOURCE, datasource);
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
		return setColumn(StringUtil.getString(keys, true));
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
		return setNull(StringUtil.getString(keys, true));
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
		return setCast(StringUtil.getString(keyTypes, true));
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
		return setCombine(StringUtil.getString(keys, true));
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
		return setGroup(StringUtil.getString(keys, true));
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
		return setHaving(StringUtil.getString(keys, true));
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

	/**set keys for order by
	 * @param keys  key0, key1+, key2- ...
	 * @return {@link #setOrder(String)}
	 */
	public JSONObject setOrder(String... keys) {
		return setOrder(StringUtil.getString(keys, true));
	}
	/**set keys for order by
	 * @param keys  "key0,key1+,key2-..."
	 * @return
	 */
	public JSONObject setOrder(String keys) {
		return puts(KEY_ORDER, keys);
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

	/**用 setJson 替代。
	 * set keys to cast to json
	 * @param keys  "key0,key1,key2..."
	 * @return
	 * @see #{@link #setJson(String)}
	 */
	@Deprecated
	public JSONObject setJSON(String keys) {
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
		return puts(key+"@", StringUtil.getString(keys, "/"));
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



	/**puts key-value in object into this
	 * @param map
	 * @return this
	 */
	public JSONObject putsAll(Map<? extends String, ? extends Object> map) {
		putAll(map);
		return this;
	}
	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
		if (map != null && map.isEmpty() == false) {
			super.putAll(map);
		}
	}



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
		return super.put(key, value);
	}



}
