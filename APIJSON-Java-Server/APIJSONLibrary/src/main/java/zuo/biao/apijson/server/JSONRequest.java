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

import java.util.Map;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;

/**JSONRequest for Server to replace zuo.biao.apijson.JSONRequest,
 * put JSON.parseObject(value) and not encode in default cases
 * @author Lemon
 * @see #put(String, Object, boolean)
 */
public class JSONRequest extends zuo.biao.apijson.JSONRequest {
	private static final long serialVersionUID = 1L;
	
	public JSONRequest() {
		super();
	}
	/**
	 * encode = true
	 * {@link #JSONRequest(String, Object)}
	 * @param object
	 */
	public JSONRequest(Object object) {
		super(object);
	}
	/**
	 * @param name
	 * @param object
	 */
	public JSONRequest(String name, Object object) {
		super(name, object);
	}

	
	
	@Override
	public JSONRequest putsAll(Map<? extends String, ? extends Object> map) {
		super.putsAll(map);
		return this;
	}

	/**
	 * @param value
	 * @return {@link #puts(String, Object)}
	 */
	@Override
	public JSONRequest puts(Object value) {
		return puts(null, value);
	}
	/**
	 * @param key
	 * @param value
	 * @return this
	 * @see {@link #put(String, Object)}
	 */
	@Override
	public JSONRequest puts(String key, Object value) {
		put(key, value);
		return this;
	}

	/**
	 * @param value
	 * @return {@link #put(String, Object)}
	 */
	@Override
	public Object put(Object value) {
		return put(null, value);
	}
	/**自定义类型必须转为JSONObject或JSONArray，否则RequestParser解析不了
	 */
	@Override
	public Object put(String key, Object value) {
		if (value == null) {//  || key == null
			return null;
		}

		Object target = JSON.parse(value);
		//		if (target == null) { // "tag":"User" 报错
		//			return null;
		//		}
		return super.put(StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName() //must handle key here
				, target == null ? value : target);
	}

}
