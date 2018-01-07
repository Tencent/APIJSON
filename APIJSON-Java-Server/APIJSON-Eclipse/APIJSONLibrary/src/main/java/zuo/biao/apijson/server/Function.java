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

import zuo.biao.apijson.Log;
import zuo.biao.apijson.StringUtil;

/**可远程调用的函数类
 * @author Lemon
 */
public class Function {
	private static final String TAG = "Function";

	/**反射调用
	 * @param jsonMap
	 * @param function 例如get(Map:map,key)，参数只允许引用，不能直接传值
	 * @return
	 */
	public static Object invoke(Function fun, Map<String, Object> jsonMap, String function) throws Exception {

		int start = function.indexOf("(");
		int end = function.lastIndexOf(")");
		Entry<String, String> method = Pair.parseVariableEntry(function.substring(0, start));
		if (method == null || StringUtil.isNotEmpty(method.getValue(), true) == false) {
			Log.i(TAG, "invoke  method == null || StringUtil.isNotEmpty(method.getValue(), true) == false"
					+ " >> return null;");
			return null;
		}

		String[] typeValues = StringUtil.split(function.substring(start + 1, end));

		Class<?>[] types = null;
		Object[] values = null;
		if (typeValues != null && typeValues.length > 0) {
			types = new Class<?>[typeValues.length];
			values = new Object[typeValues.length];

			Entry<Class<?>, Object> entry;
			for (int i = 0; i < typeValues.length; i++) {
				entry = Pair.parseVariableEntry(typeValues[i], jsonMap);
				if (entry != null && entry.isEmpty() == false) {
					types[i] = entry.getKey();
					values[i] = entry.getValue();
				}
			}
		}
		return invoke(fun, method.getValue(), types, values); 
	}
	/**反射调用
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static Object invoke(Function fun, String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
		Class<?> clazz = fun.getClass();
		return clazz.getDeclaredMethod(methodName, parameterTypes).invoke(fun, args);
	}

}