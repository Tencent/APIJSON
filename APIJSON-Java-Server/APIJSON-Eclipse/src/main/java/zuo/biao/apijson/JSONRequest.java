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

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**encapsulator for request JSONObject, encode in default cases
 * @author Lemon
 * @see #toArray
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.put(...);//not a must
 * <br> request.toArray(...);//not a must
 */
public class JSONRequest extends JSONObject {
	private static final long  serialVersionUID = 1L;

	public JSONRequest() {
		super();
	}
	/**
	 * encode = true
	 * @param object must be annotated by {@link APIJSONRequest}
	 * @see	{@link #JSONRequest(String, Object)}
	 */
	public JSONRequest(Object object) {
		this(null, object);
	}
	/**
	 * encode = true
	 * @param name
	 * @param object
	 * @see {@link #JSONRequest(String, Object, boolean)}
	 */
	public JSONRequest(String name, Object object) {
		this(name, object, true);
	}
	/**
	 * @param object must be annotated by {@link APIJSONRequest}
	 * @param encode
	 * @see {@link #JSONRequest(String, Object, boolean)}
	 */
	public JSONRequest(Object object, boolean encode) {
		this(null, object, encode);
	}
	/**
	 * @param name
	 * @param object
	 * @param encode
	 * @see {@link #put(String, Object, boolean)}
	 */
	public JSONRequest(String name, Object object, boolean encode) {
		this();
		put(name, object, encode);
	}






	public static final String KEY_ID = "id";
	public static final String KEY_ID_IN = KEY_ID + "{}";
	public static final String KEY_TAG = "tag";//只在最外层，最外层用JSONRequest

	/**set "id":id in Table layer
	 * @param id
	 * @return
	 */
	public JSONRequest setId(Long id) {
		put(KEY_ID, id);
		return this;
	}
	/**set id{}:[] in Table layer
	 * @param list
	 * @return
	 */
	public JSONRequest setIdIn(List<Object> list) {
		put(KEY_ID_IN, list);
		return this;
	}
	/**set "tag":tag in outermost layer
	 * for write operations
	 * @param tag
	 * @return
	 */
	public JSONRequest setTag(String tag) {
		put(KEY_TAG, tag);
		return this;
	}


	//array object <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int QUERY_TABLE = 0;
	public static final int QUERY_TOTAL = 1;
	public static final int QUERY_ALL = 2;

	public static final String KEY_QUERY = "query";
	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";

	/**set what to query in Array layer
	 * @param query what need to query, Table,total,ALL?
	 * @return
	 * @see {@link #QUERY_TABLE}
	 * @see {@link #QUERY_TOTAL}
	 * @see {@link #QUERY_ALL}
	 */
	public JSONRequest setQuery(int query) {
		put(KEY_QUERY, query);
		return this;
	}
	/**set maximum count of Tables to query in Array layer
	 * @param count <= 0 || >= max ? max : count
	 * @return
	 */
	public JSONRequest setCount(int count) {
		put(KEY_COUNT, count);
		return this;
	}
	/**set page of Tables to query in Array layer
	 * @param page <= 0 ? 0 : page
	 * @return
	 */
	public JSONRequest setPage(int page) {
		put(KEY_PAGE, page);
		return this;
	}
	//array object >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	// 导致JSONObject add >> get = null
	//	/**
	//	 * decode = true
	//	 * @param key
	//	 * return {@link #get(Object, boolean)}
	//	 */
	//	@Override
	//	public Object get(Object key) {
	//		return get(key, true);
	//	}

	/**
	 * encode = true
	 * @param value must be annotated by {@link APIJSONRequest}
	 * @return {@link #put(String, boolean)}
	 */
	@Override
	public Object put(Object value) {
		return put(value, true);
	}
	/**
	 * encode = true
	 * @param key
	 * @param value
	 * return {@link #put(String, Object, boolean)}
	 */
	@Override
	public Object put(String key, Object value) {
		return put(key, value, true);
	}


	/**create a parent JSONObject named KEY_ARRAY
	 * encode = true;
	 * @param count
	 * @param page
	 * @return {@link #toArray(int, int, boolean)}
	 */
	public JSONRequest toArray(int count, int page) {
		return toArray(count, page, true);
	}
	/**create a parent JSONObject named KEY_ARRAY
	 * encode = true;
	 * @param count
	 * @param page
	 * @return {@link #toArray(int, int, String, boolean)}
	 */
	public JSONRequest toArray(int count, int page, boolean encode) {
		return toArray(count, page, null, encode);
	}
	/**create a parent JSONObject named name+KEY_ARRAY
	 * encode = true;
	 * @param count
	 * @param page
	 * @param name
	 * @return {@link #toArray(int, int, String, boolean)}
	 */
	public JSONRequest toArray(int count, int page, String name) {
		return toArray(count, page, name, true);
	}
	/**create a parent JSONObject named name+KEY_ARRAY. 
	 * @param count
	 * @param page
	 * @param name
	 * @param encode
	 * @return {name+KEY_ARRAY : this}. if needs to be put, use {@link #add(com.alibaba.fastjson.JSONObject)} instead
	 */
	public JSONRequest toArray(int count, int page, String name, boolean encode) {
		return new JSONRequest(StringUtil.getString(name) + KEY_ARRAY, this.setCount(count).setPage(page), encode);
	}

}
