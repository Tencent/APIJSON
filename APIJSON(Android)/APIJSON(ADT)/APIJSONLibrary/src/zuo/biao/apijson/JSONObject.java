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
import static zuo.biao.apijson.StringUtil.bigAlphaPattern;
import static zuo.biao.apijson.StringUtil.namePattern;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;


/**use this class instead of com.alibaba.fastjson.JSONObject, not encode in default cases
 * @author Lemon
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject {
	private static final long serialVersionUID = 8907029699680768212L;

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
	 * @param value
	 * @return {@link #put(String, boolean)}
	 */
	public Object put(Object value) {
		return put(value, false);
	}
	/**
	 * key = value.getClass().getSimpleName()
	 * @param value
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
			key = value == null ? null : value.getClass().getSimpleName();
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
		return isWord(key) && bigAlphaPattern.matcher(key.substring(0, 1)).matches();
	}
	/**判断是否为词，只能包含字母，数字或下划线
	 * @param key
	 * @return
	 */
	public static boolean isWord(String key) {
		return StringUtil.isNotEmpty(key, false) && namePattern.matcher(key).matches();
	}
	//judge >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	public static final String KEY_COLUMNS = "@columns";//@key关键字都放这个类

	/**set columns need to be returned
	 * @param columns  "column0,column1,column2..."
	 * @return
	 */
	public JSONObject setColumns(String columns) {
		put(KEY_COLUMNS, columns);
		return this;
	}
	public String getColumns() {
		return getString(KEY_COLUMNS);
	}


}
