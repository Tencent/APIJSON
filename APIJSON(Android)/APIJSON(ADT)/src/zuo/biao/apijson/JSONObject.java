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

package zuo.biao.apijson;

/**use this class instead of com.alibaba.fastjson.JSONObject
 * @author Lemon
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject {
	private static final long serialVersionUID = 8907029699680768212L;

	public JSONObject() {
		super(true);
	}
	
//	public Object put(Field key, Object value) {
//		return put(key.getName(), value);
//	}
	
}
