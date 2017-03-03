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

import static zuo.biao.apijson.StringUtil.bigAlphaPattern;
import static zuo.biao.apijson.StringUtil.namePattern;

import java.util.Set;


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
		this(toJSONString(object));
	}
	/**parse JSONObject with JSON String
	 * @param json
	 */
	public JSONObject(String json) {
		this(parseObject(json));
	}
	/**transfer com.alibaba.fastjson.JSONObject to JSONObject
	 * @param object
	 */
	public JSONObject(com.alibaba.fastjson.JSONObject object) {
		this();
		add(object);
	}




	/**put key-value in object into this
	 * @param object
	 * @return this
	 */
	public JSONObject add(com.alibaba.fastjson.JSONObject object) {
		Set<String> set = object == null ? null : object.keySet();
		if (set != null) {
			for (String key : set) {
				put(key, object.get(key));
			}
		}
		return this;
	}





	//judge <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public static final String KEY_ARRAY = "[]";
	
	/**判断是否为Array的key
	 * @param key
	 * @return
	 */
	public static boolean isArrayKey(String key) {
		return key != null && key.endsWith("[]");
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

}
