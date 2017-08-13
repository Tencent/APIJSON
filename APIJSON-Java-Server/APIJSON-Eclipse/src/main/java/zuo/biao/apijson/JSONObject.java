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

package zuo.biao.apijson;

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**use this class instead of com.alibaba.fastjson.JSONObject, not encode in default cases
 * @author Lemon
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject {
	private static final long  serialVersionUID = 1L;

	/**ordered
	 */
	public JSONObject() {
		super(true);
	}
	/**transfer Object to JSONObject
	 * encode = false;
	 * @param object
	 * @see {@link #JSONObject(Object, boolean)}
	 */
	public JSONObject(Object object) {
		this(object, false);
	}
	/**transfer Object to JSONObject
	 * @param object
	 * @param encode
	 * @see {@link #JSONObject(String, boolean)}
	 */
	public JSONObject(Object object, boolean encode) {
		this(toJSONString(object), encode);
	}
	/**parse JSONObject with JSON String
	 * encode = false;
	 * @param json
	 * @see {@link #JSONObject(String, boolean)}
	 */
	public JSONObject(String json) {
		this(json, false);
	}
	/**parse JSONObject with JSON String
	 * @param json
	 * @param encode
	 * @see {@link #JSONObject(com.alibaba.fastjson.JSONObject, boolean)}
	 */
	public JSONObject(String json, boolean encode) {
		this(parseObject(json), encode);
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * encode = false;
	 * @param object
	 * @see {@link #JSONObject(com.alibaba.fastjson.JSONObject, boolean)}
	 */
	public JSONObject(com.alibaba.fastjson.JSONObject object) {
		this(object, false);
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * @param object
	 * @param encode
	 * @see {@link #add(com.alibaba.fastjson.JSONObject, boolean)}
	 */
	public JSONObject(com.alibaba.fastjson.JSONObject object, boolean encode) {
		this();
		add(object, encode);
	}




	/**put key-value in object into this
	 * encode = false;
	 * @param object
	 * @return {@link #add(com.alibaba.fastjson.JSONObject, boolean)}
	 */
	public JSONObject add(com.alibaba.fastjson.JSONObject object) {
		return add(object, false);
	}
	/**put key-value in object into this
	 * @param object
	 * @param encode
	 * @return this
	 */
	public JSONObject add(com.alibaba.fastjson.JSONObject object, boolean encode) {
		//TODO  putAll(object);

		Set<String> set = object == null ? null : object.keySet();
		if (set != null) {
			for (String key : set) {
				put(key, object.get(key), encode);
			}
		}
		return this;
	}



	/**
	 * @param key if decode && key instanceof String, key = URLDecoder.decode((String) key, UTF_8)
	 * @param decode if decode && value instanceof String, value = URLDecoder.decode((String) value, UTF_8)
	 * @return 
	 */
	public Object get(Object key, boolean decode) {
		if (decode) {
			if (key instanceof String) {
				if (((String) key).endsWith("+") || ((String) key).endsWith("-")) {
					try {//多层encode导致内部Comment[]传到服务端decode后最终变为Comment%5B%5D
						key = URLDecoder.decode((String) key, UTF_8);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
			Object value = super.get(key);
			if (value instanceof String) {
				try {
					value = URLDecoder.decode((String) value, UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return value;
		}
		return super.get(key);
	}

	/**
	 * encode = false
	 * @param value must be annotated by {@link APIJSONRequest}
	 * @return {@link #put(String, boolean)}
	 */
	public Object put(Object value) {
		return put(value, false);
	}
	/**
	 * key = value.getClass().getSimpleName()
	 * @param value must be annotated by {@link APIJSONRequest}
	 * @param encode
	 * @return {@link #put(String, Object, boolean)}
	 */
	public Object put(Object value, boolean encode) {
		return put(null, value, encode);
	}	
	/**
	 * @param key  if StringUtil.isNotEmpty(key, true) == false,
	 *             <br> key = value == null ? null : value.getClass().getSimpleName();
	 *             <br> >> if decode && key instanceof String, key = URLDecoder.decode((String) key, UTF_8)
	 * @param value URLEncoder.encode((String) value, UTF_8);
	 * @param encode if value instanceof String, value = URLEncoder.encode((String) value, UTF_8);
	 * @return
	 */
	public Object put(String key, Object value, boolean encode) {
		if (StringUtil.isNotEmpty(key, true) == false) {
			Class<?> clazz = value == null ? null : value.getClass();
			if (clazz == null || clazz.getAnnotation(MethodAccess.class) == null) {
				throw new IllegalArgumentException("put  StringUtil.isNotEmpty(key, true) == false" +
						" && clazz == null || clazz.getAnnotation(MethodAccess.class) == null" +
						" \n key为空时仅支持 类型被@MethodAccess注解 的value !!!" +
						" \n 如果一定要这么用，请对 " + clazz.getName() + " 注解！" +
						" \n 如果是类似 key[]:{} 结构的请求，建议add(...)方法！");
			}
			key = value.getClass().getSimpleName();
		}
		if (encode) {
			if (key.endsWith("+") || key.endsWith("-")) {
				try {//多层encode导致内部Comment[]传到服务端decode后最终变为Comment%5B%5D
					key = URLEncoder.encode(key, UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (value instanceof String) {//只在value instanceof String时encode key？{@link #get(Object, boolean)}内做不到
				try {
					value = URLEncoder.encode((String) value, UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return super.put(key, value);
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
	//judge >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//JSONObject内关键词 key <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//@key关键字都放这个类 <<<<<<<<<<<<<<<<<<<<<<
	public static final String KEY_ROLE = "@role"; //角色，拥有对某些数据的某些操作的权限
	public static final String KEY_CONDITION = "@condition"; //条件 TODO 用 @where& @where| @where! 替代？
	public static final String KEY_TRY = "@try"; //尝试，忽略异常
	public static final String KEY_DROP = "@drop"; //丢弃，不返回
	public static final String KEY_CORRECT = "@correct"; //字段校正
	
	public static final String KEY_SCHEMA = "@schema"; //数据库，Table在非默认schema内时需要声明
	public static final String KEY_ABOUT = "@about"; //关于，返回数据库表的信息，包括表说明和字段说明
	public static final String KEY_COLUMN = "@column"; //查询的Table字段或SQL函数
	public static final String KEY_GROUP = "@group"; //分组方式
	public static final String KEY_HAVING = "@having"; //聚合函数条件，一般和@group一起用
	public static final String KEY_ORDER = "@order"; //排序方式
	//@key关键字都放这个类 >>>>>>>>>>>>>>>>>>>>>>


	/**set role of request sender
	 * @param role
	 * @return this
	 */
	public JSONObject setRole(String role) {
		put(KEY_ROLE, role);
		return this;
	}
	
	/**set try, ignore exceptions
	 * @param tri
	 * @return this
	 */
	public JSONObject setTry(boolean tri) {
		put(KEY_TRY, tri);
		return this;
	}
	
	/**set drop, data dropped will not return
	 * @param drop
	 * @return this
	 */
	public JSONObject setDrop(boolean drop) {
		put(KEY_DROP, drop);
		return this;
	}
	
	/**set correct, correct keys to target ones
	 * @param correct  Map{originKey, [posibleKeys]}， posibleKey之间用 , 隔开
	 * @return this
	 */
	public JSONObject setCorrect(Map<String, String> correct) {
		put(KEY_CORRECT, correct);
		return this;
	}
	
	

	/**set schema where table was put
	 * @param schema
	 * @return this
	 */
	public JSONObject setSchema(String schema) {
		put(KEY_SCHEMA, schema);
		return this;
	}

	/**set about
	 * @param about
	 * @return this
	 */
	public JSONObject setAbout(boolean about) {
		put(KEY_ABOUT, about);
		return this;
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
		put(KEY_COLUMN, keys);
		return this;
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
		put(KEY_GROUP, keys);
		return this;
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
		put(KEY_HAVING, keys);
		return this;
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
		put(KEY_ORDER, keys);
		return this;
	}


	//JSONObject内关键词 key >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//Request，默认encode <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**
	 * encode = true
	 * @param value
	 * @param parts path = keys[0] + "/" + keys[1] + "/" + keys[2] + ...
	 * @return #put(key+"@", StringUtil.getString(keys, "/"), true)
	 */
	public Object putPath(String key, String... keys) {
		return put(key+"@", StringUtil.getString(keys, "/"), true);
	}

	/**
	 * encode = true
	 * @param key
	 * @param isNull
	 * @return {@link #putNull(String, boolean, boolean)}
	 */
	public JSONObject putNull(String key, boolean isNull) {
		return putNull(key, isNull, true);
	}
	/**
	 * @param key
	 * @param isNull
	 * @param encode
	 * @return put(key+"{}", SQL.isNull(isNull), encode);
	 */
	public JSONObject putNull(String key, boolean isNull, boolean encode) {
		put(key+"{}", SQL.isNull(isNull), encode);
		return this;
	}
	/**
	 * trim = false
	 * @param key
	 * @param isEmpty
	 * @return {@link #putEmpty(String, boolean, boolean)}
	 */
	public JSONObject putEmpty(String key, boolean isEmpty) {
		return putEmpty(key, isEmpty, false);
	}
	/**
	 * encode = true
	 * @param key
	 * @param isEmpty
	 * @return {@link #putEmpty(String, boolean, boolean, boolean)}
	 */
	public JSONObject putEmpty(String key, boolean isEmpty, boolean trim) {
		return putEmpty(key, isEmpty, trim, true);
	}
	/**
	 * @param key
	 * @param isEmpty
	 * @param encode
	 * @return put(key+"{}", SQL.isEmpty(key, isEmpty, trim), encode);
	 */
	public JSONObject putEmpty(String key, boolean isEmpty, boolean trim, boolean encode) {
		put(key+"{}", SQL.isEmpty(key, isEmpty, trim), encode);
		return this;
	}
	/**
	 * encode = true
	 * @param key
	 * @param compare <=0, >5 ...
	 * @return {@link #putLength(String, String, boolean)}
	 */
	public JSONObject putLength(String key, String compare) {
		return putLength(key, compare, true);
	}
	/**
	 * @param key
	 * @param compare <=0, >5 ...
	 * @param encode
	 * @return put(key+"{}", SQL.length(key) + compare, encode);
	 */
	public JSONObject putLength(String key, String compare, boolean encode) {
		put(key+"{}", SQL.length(key) + compare, encode);
		return this;
	}

	/**设置搜索
	 * type = SEARCH_TYPE_CONTAIN_FULL
	 * @param key
	 * @param value
	 * @return {@link #putSearch(String, String, int)}
	 */
	public JSONObject putSearch(String key, String value) {
		return putSearch(key, value, SQL.SEARCH_TYPE_CONTAIN_FULL);
	}
	/**设置搜索
	 * encode = true
	 * @param key
	 * @param value
	 * @param type
	 * @return {@link #putSearch(String, String, int, boolean)}
	 */
	public JSONObject putSearch(String key, String value, int type) {
		return putSearch(key, value, type, true);
	}
	/**设置搜索
	 * @param key
	 * @param value
	 * @param type
	 * @param encode
	 * @return put(key+"$", SQL.search(value, type), encode);
	 */
	public JSONObject putSearch(String key, String value, int type, boolean encode) {
		put(key+"$", SQL.search(value, type), encode);
		return this;
	}

	//Request，默认encode >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
