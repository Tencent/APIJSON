/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

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

import static zuo.biao.apijson.client.HttpManager.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import zuo.biao.apijson.JSONObject;

/**auto formatted request JSONObject
 * @author Lemon
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.put(...);//not a must
 * <br> request.toArray(...);//not a must,you can use  new JSONRequest(...).put("[]", request);  instead
 * @see #toArray
 */
public class JSONRequest extends JSONObject {

	private static final long serialVersionUID = -2223023180338466812L;

	public JSONRequest() {
		super();
	}
	public JSONRequest(Object object) {
		this(null, object);
	}
	public JSONRequest(String name, Object object) {
		this();
		put(name, object);
	}


	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) super.get(key);
	}

	/**put(value.getClass().getSimpleName(), value);
	 * @param value
	 * @return
	 */
	public Object put(Object value) {
		return put(null, value);
	}	
	@Override
	public Object put(String key, Object value) {
		try {
			if (value instanceof String) {
				value = URLEncoder.encode((String) value, UTF_8);
			}
			return super.put(StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName(), value);
					//just encode /, not need to encode [] 	? URLEncoder.encode(key, UTF_8) 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**put key-value in object into this
	 * @param object
	 */
	public void add(JSONObject object) {
		Set<String> set = object == null ? null : object.keySet();
		if (set != null) {
			for (String key : set) {
				put(key, object.get(key));
			}
		}
	}

	
	
	
	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";

	public JSONRequest setCount(Integer count) {
		put(KEY_COUNT, count);
		return this;
	}
	public JSONRequest setPage(Integer page) {
		put(KEY_PAGE, page);
		return this;
	}

	/**create a parent JSONObject named []
	 * @param count
	 * @param page
	 * @return {"[]":this}
	 */
	public JSONRequest toArray(int count, int page) {
		return toArray(count, page, null);
	}
	/**create a parent JSONObject named name+[]
	 * @param count
	 * @param page
	 * @param name
	 * @return {name+"[]" : this}
	 */
	public JSONRequest toArray(int count, int page, String name) {
		return new JSONRequest(StringUtil.getString(name) + "[]", this.setCount(count).setPage(page));
	}
	
}
