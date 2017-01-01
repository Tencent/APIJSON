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

package zuo.biao.apijson.client;

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;

/**use this class instead of com.alibaba.fastjson.JSONObject
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
	 * @param object
	 */
	public JSONObject(Object object) {
		this(JSON.toJSONString(object));
	}
	/**parse JSONObject with JSON String
	 * @param json
	 */
	public JSONObject(String json) {
		this(JSON.parseObject(json));
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * @param object
	 */
	public JSONObject(com.alibaba.fastjson.JSONObject object) {
		this();
		add(object);
	}




	/**
	 * @param key
	 * @return if value is String, return URLDecoder.decode((String) value, UTF_8);
	 */
	public Object getWithDecode(String key) {
		Object value = get(key);
		if (value instanceof String) {
			try {
				return URLDecoder.decode((String) value, UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}


	/**put(value.getClass().getSimpleName(), value);
	 * @param value
	 * @return
	 */
	public Object putWithEncode(Object value) {
		return putWithEncode(null, value);
	}
	/**
	 * @param key
	 * @param value if is String, value = URLEncoder.encode((String) value, UTF_8);
	 * @return
	 */
	public Object putWithEncode(String key, Object value) {
		if (value instanceof String) {
			try {
				value = URLEncoder.encode((String) value, UTF_8);
				//just encode /, not need to encode [] 	? URLEncoder.encode(key, UTF_8) 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return super.put(StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName(), value);
	}

	/**put key-value in object into this
	 * @param object
	 */
	public void add(com.alibaba.fastjson.JSONObject object) {
		Set<String> set = object == null ? null : object.keySet();
		if (set != null) {
			for (String key : set) {
				put(key, object.get(key));
			}
		}
	}
	
	
	public static final String KEY_TAG = "tag";
	public static final String KEY_COLUMNS = "columns";
	/**set tag
	 * @param tag
	 * @return
	 */
	public JSONObject setTag(String tag) {
		put(KEY_TAG, tag);
		return this;
	}
	public String getTag() {
		return getString(KEY_TAG);
	}
	
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
	


	//array object <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";

	public JSONObject setCount(int count) {
		put(KEY_COUNT, count);
		return this;
	}
	public int getCount() {
		return getIntValue(KEY_COUNT);
	}

	public JSONObject setPage(int page) {
		put(KEY_PAGE, page);
		return this;
	}
	public int getPage() {
		return getIntValue(KEY_PAGE);
	}
	//array object >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//judge <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * @param json
	 * @return
	 */
	public static boolean isJSONObject(String json) {
		return JSON.parseObject(json) != null;
	}
	/**
	 * @param key
	 * @return
	 */
	public static boolean isObjectKey(String key) {
		key = StringUtil.getString(key);
		return StringUtil.isNotEmpty(key, false) && isArrayKey(key) == false && StringUtil.isAlpha(key.substring(0, 1));
	}
	/**
	 * @param key
	 * @return
	 */
	public static boolean isArrayKey(String key) {
		return key.endsWith("[]");
	}
	//judge >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
