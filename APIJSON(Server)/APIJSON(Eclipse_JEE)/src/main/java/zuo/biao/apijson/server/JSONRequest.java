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

package zuo.biao.apijson.server;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.SQL;
import zuo.biao.apijson.StringUtil;

/**JSONRequest for Server to replace zuo.biao.apijson.JSONRequest,
 * put JSON.parseObject(value) and not encode in default cases
 * @author Lemon
 * @see #put(String, Object, boolean)
 */
public class JSONRequest extends zuo.biao.apijson.JSONRequest {

	private static final long serialVersionUID = -2223023180338466812L;

	public JSONRequest() {
		super();
	}
	/**
	 * encode = true
	 * {@link #JSONRequest(String, Object)}
	 * @param object
	 */
	public JSONRequest(Object object) {
		this(null, object);
	}
	/**
	 * encode = false
	 * {@link #JSONRequest(String, Object, boolean)}
	 * @param name
	 * @param object
	 */
	public JSONRequest(String name, Object object) {
		this(name, object, false);
	}
	/**
	 * {@link #JSONRequest(String, Object, boolean)}
	 * @param object
	 * @param encode
	 */
	public JSONRequest(Object object, boolean encode) {
		super(object, encode);
	}
	/**
	 * {@link #put(String, Object, boolean)}
	 * @param name
	 * @param object
	 * @param encode
	 */
	public JSONRequest(String name, Object object, boolean encode) {
		super(name, object, encode);
	}



	/**
	 * decode = true
	 * @param key
	 * return {@link #get(Object, boolean)}
	 */
	@Override
	public Object get(Object key) {
		return get(key, false);
	}

	/**
	 * encode = false
	 * @param value
	 * @return {@link #put(String, boolean)}
	 */
	@Override
	public Object put(Object value) {
		return put(value, false);
	}
	/**
	 * @param value
	 * @param encode
	 * @return {@link #put(String, Object, boolean)}
	 */
	@Override
	public Object put(Object value, boolean encode) {
		return put(null, value, encode);
	}
	/**
	 * encode = false
	 * @param key
	 * @param value
	 * return {@link #put(String, Object, boolean)}
	 */
	@Override
	public Object put(String key, Object value) {
		return put(key, value, false);
	}
	/**自定义类型必须转为JSONObject或JSONArray，否则RequestParser解析不了
	 */
	@Override
	public Object put(String key, Object value, boolean encode) {
		return super.put(StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName() //must handle key here
				, JSON.parseObject(value), encode);
	}

	/**设置搜索
	 * @param key
	 * @param value
	 * @return 
	 * @see {@link #putSearch(String, String, int)}
	 */
	@Override
	public JSONRequest putSearch(String key, String value) {
		return putSearch(key, value, SQL.SEARCH_TYPE_CONTAIN_FULL);
	}
	/**设置搜索
	 * @param key
	 * @param value
	 * @param type
	 * @see {@link #putSearch(String, String, int, boolean)}
	 */
	@Override
	public JSONRequest putSearch(String key, String value, int type) {
		putSearch(key, value, type, false);
		return this;
	}

}
