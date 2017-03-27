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






	public static final String KEY_TAG = "tag";

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
	public static final String KEY_TOTAL = "total";

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
	//	private int total;
	public JSONRequest setTotal(int total) {
		//		this.total = total;
		put(KEY_TOTAL, total);
		return this;
	}
	public int getTotal() {
		return getIntValue(KEY_TOTAL);//total;//
	}
	//array object >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	/**
	 * @param value
	 * @param parts path = parts[0] + "/" + parts[1] + "/" + parts[2] + ...
	 * @return
	 */
	public Object putPath(String key, String... parts) {
		String path = "";
		if (parts != null) {
			for (int i = 0; i < parts.length; i++) {
				path += (i > 0 ? "/" : "") + parts[i];
			}
		}
		return put(key, path);
	}

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
	/**create a parent JSONObject named name+KEY_ARRAY
	 * @param count
	 * @param page
	 * @param name
	 * @param encode
	 * @return {name+KEY_ARRAY : this}
	 */
	public JSONRequest toArray(int count, int page, String name, boolean encode) {
		return new JSONRequest(StringUtil.getString(name) + KEY_ARRAY, this.setCount(count).setPage(page), encode);
	}


	/**设置搜索
	 * @param key
	 * @param value
	 * @see {@link #putSearch(String, String, int)}
	 */
	public void putSearch(String key, String value) {
		putSearch(key, value, SEARCH_TYPE_CONTAIN_FULL);
	}
	/**设置搜索
	 * encode = true
	 * @param key
	 * @param value
	 * @param type
	 * @see {@link #putSearch(String, String, int, boolean)}
	 */
	public void putSearch(String key, String value, int type) {
		putSearch(key, value, type, true);
	}
	/**设置搜索
	 * @param key
	 * @param value
	 * @param type
	 * @param encode
	 */
	public void putSearch(String key, String value, int type, boolean encode) {
		if (key == null) {
			key = "";
		}
		if (key.endsWith("$") == false) {
			key += "$";
		}
		put(key, getSearch(value, type), encode);
	}

	public static final int SEARCH_TYPE_CONTAIN_FULL = 0;
	public static final int SEARCH_TYPE_CONTAIN_ORDER = 1;
	public static final int SEARCH_TYPE_CONTAIN_SINGLE = 2;
	public static final int SEARCH_TYPE_CONTAIN_ANY = 3;
	public static final int SEARCH_TYPE_START = 4;
	public static final int SEARCH_TYPE_END = 5;
	public static final int SEARCH_TYPE_START_SINGLE = 6;
	public static final int SEARCH_TYPE_END_SINGLE = 7;
	public static final int SEARCH_TYPE_PART_MATCH = 8;
	/**
	 * SQL中NOT LIKE就行？？
	 */
	public static final int SEARCH_TYPE_NO_CONTAIN = 9;
	/**
	 * SQL中NOT LIKE就行？？
	 */
	public static final int SEARCH_TYPE_NO_PART_MATCH = 10;
	/**获取搜索值
	 * @param key
	 * @return
	 */
	public static String getSearch(String key) {
		return getSearch(key, SEARCH_TYPE_CONTAIN_FULL);
	}
	/**获取搜索值
	 * @param key
	 * @param type
	 * @return
	 */
	public static String getSearch(String key, int type) {
		return getSearch(key, type, true);
	}
	/**获取搜索值
	 * @param key
	 * @param type
	 * @param ignoreCase
	 * @return
	 */
	public static String getSearch(String key, int type, boolean ignoreCase) {
		if (key == null) {
			return null;
		}
		switch (type) {
		case SEARCH_TYPE_CONTAIN_SINGLE:
			return "_" + key + "_";
		case SEARCH_TYPE_CONTAIN_ORDER:
			char[] cs = key.toCharArray();
			if (cs == null) {
				return null;
			}
			String s = "%";
			for (int i = 0; i < cs.length; i++) {
				s += cs[i] + "%";
			}
			return s;
		case SEARCH_TYPE_START:
			return key + "%";
		case SEARCH_TYPE_END:
			return "%" + key;
		case SEARCH_TYPE_START_SINGLE:
			return key + "_";
		case SEARCH_TYPE_END_SINGLE:
			return "_" + key;
		case SEARCH_TYPE_NO_CONTAIN:
			return "[^" + key + "]";
		case SEARCH_TYPE_NO_PART_MATCH:
			cs = key.toCharArray();
			if (cs == null) {
				return null;
			}
			s = "";
			for (int i = 0; i < cs.length; i++) {
				s += getSearch("" + cs[i], SEARCH_TYPE_NO_CONTAIN, ignoreCase);
			}
			return s;
		default:
			return "%" + key + "%";
		}
	}


}
