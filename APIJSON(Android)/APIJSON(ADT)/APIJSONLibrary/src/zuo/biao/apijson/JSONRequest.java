/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

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

/**encapsulator for request JSONObject, encode in default cases
 * @author Lemon
 * @see #toArray
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.put(...);//not a must
 * <br> request.toArray(...);//not a must
 */
public class JSONRequest extends JSONObject {

	private static final long serialVersionUID = -2223023180338466812L;

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






	public static final String KEY_TAG = "tag";//只在最外层，最外层用JSONRequest

	public JSONObject setTag(String tag) {
		put(KEY_TAG, tag);
		return this;
	}
	public String getTag() {
		return getString(KEY_TAG);
	}


	//array object <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";

	public JSONRequest setCount(int count) {
		put(KEY_COUNT, count);
		return this;
	}
	public int getCount() {
		return getIntValue(KEY_COUNT);
	}

	public JSONRequest setPage(int page) {
		put(KEY_PAGE, page);
		return this;
	}
	public int getPage() {
		return getIntValue(KEY_PAGE);
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
