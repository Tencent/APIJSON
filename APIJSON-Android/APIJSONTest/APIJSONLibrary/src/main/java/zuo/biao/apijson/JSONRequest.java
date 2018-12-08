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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**wrapper for request
 * @author Lemon
 * @see #puts
 * @see #toArray
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.puts(...);//not a must
 * <br> request.toArray(...);//not a must
 */
public class JSONRequest extends JSONObject {
	private static final long  serialVersionUID = 1L;

	public JSONRequest() {
		super();
	}
	/**
	 * @param object must be annotated by {@link MethodAccess}
	 * @see	{@link #JSONRequest(String, Object)}
	 */
	public JSONRequest(Object object) {
		this(null, object);
	}
	/**
	 * @param name
	 * @param object
	 * @see {@link #puts(String, Object)}
	 */
	public JSONRequest(String name, Object object) {
		this();
		puts(name, object);
	}




	public static final String KEY_TAG = "tag";//只在最外层，最外层用JSONRequest
	public static final String KEY_VERSION = "version";//只在最外层，最外层用JSONRequest
	public static final String KEY_FORMAT = "format";//只在最外层，最外层用JSONRequest

	/**set "tag":tag in outermost layer
	 * for write operations
	 * @param tag
	 * @return
	 */
	public JSONRequest setTag(String tag) {
		return puts(KEY_TAG, tag);
	}
	/**set "version":version in outermost layer
	 * for target version of request
	 * @param version
	 * @return
	 */
	public JSONRequest setVersion(String version) {
		return puts(KEY_VERSION, version);
	}
	/**set "format":format in outermost layer
	 * for format APIJSON special keys to normal keys of response
	 * @param format
	 * @return
	 */
	public JSONRequest setFormat(Boolean format) {
		return puts(KEY_FORMAT, format);
	}


	//array object <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int QUERY_TABLE = 0;
	public static final int QUERY_TOTAL = 1;
	public static final int QUERY_ALL = 2;

	public static final String KEY_QUERY = "query";
	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";
	public static final String KEY_JOIN = "join";

	public static final List<String> ARRAY_KEY_LIST;
	static {
		ARRAY_KEY_LIST = new ArrayList<String>();
		ARRAY_KEY_LIST.add(KEY_QUERY);
		ARRAY_KEY_LIST.add(KEY_COUNT);
		ARRAY_KEY_LIST.add(KEY_PAGE);
	}

	/**set what to query in Array layer
	 * @param query what need to query, Table,total,ALL?
	 * @return
	 * @see {@link #QUERY_TABLE}
	 * @see {@link #QUERY_TOTAL}
	 * @see {@link #QUERY_ALL}
	 */
	public JSONRequest setQuery(int query) {
		return puts(KEY_QUERY, query);
	}
	/**set maximum count of Tables to query in Array layer
	 * @param count <= 0 || >= max ? max : count
	 * @return
	 */
	public JSONRequest setCount(int count) {
		return puts(KEY_COUNT, count);
	}
	/**set page of Tables to query in Array layer
	 * @param page <= 0 ? 0 : page
	 * @return
	 */
	public JSONRequest setPage(int page) {
		return puts(KEY_PAGE, page);
	}
	//array object >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	/**create a parent JSONObject named KEY_ARRAY
	 * @param count
	 * @param page
	 * @return {@link #toArray(int, int)}
	 */
	public JSONRequest toArray(int count, int page) {
		return toArray(count, page, null);
	}
	/**create a parent JSONObject named name+KEY_ARRAY.
	 * @param count
	 * @param page
	 * @param name
	 * @return {name+KEY_ARRAY : this}. if needs to be put, use {@link #putsAll(Map<? extends String, ? extends Object>)} instead
	 */
	public JSONRequest toArray(int count, int page, String name) {
		return new JSONRequest(StringUtil.getString(name) + KEY_ARRAY, this.setCount(count).setPage(page));
	}


	@Override
	public JSONObject putsAll(Map<? extends String, ? extends Object> map) {
		super.putsAll(map);
		return this;
	}

	@Override
	public JSONRequest puts(Object value) {
		return puts(null, value);
	}
	@Override
	public JSONRequest puts(String key, Object value) {
		super.puts(key, value);
		return this;
	}

}
