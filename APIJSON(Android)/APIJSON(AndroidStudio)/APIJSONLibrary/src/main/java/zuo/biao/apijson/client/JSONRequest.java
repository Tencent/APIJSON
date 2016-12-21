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

import zuo.biao.apijson.StringUtil;

/**encapsulator for request JSONObject
 * @author Lemon
 * @see #toArray
 * @see RequestUtil
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.put(...);//not a must
 * <br> request.toArray(...);//not a must
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

	/**put(value.getClass().getSimpleName(), value);
	 * @param value
	 * @return
	 */
	public Object put(Object value) {
		return super.putWithEncode(value);
	}	
	@Override
	public Object put(String key, Object value) {
		return super.putWithEncode(key, value);
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
