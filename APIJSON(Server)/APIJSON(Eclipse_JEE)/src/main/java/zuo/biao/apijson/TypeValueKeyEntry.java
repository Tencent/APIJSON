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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**type:value
 * @author Lemon
 */
public class TypeValueKeyEntry extends Entry<String, String> {

	private static Map<String, Class<?>> classMap;
	static {
		classMap = new HashMap<>();
		classMap.put(boolean.class.getSimpleName(), boolean.class);
		classMap.put(int.class.getSimpleName(), int.class);
		classMap.put(long.class.getSimpleName(), long.class);
		classMap.put(float.class.getSimpleName(), float.class);
		classMap.put(double.class.getSimpleName(), double.class);
		classMap.put(Boolean.class.getSimpleName(), Boolean.class);
		classMap.put(Integer.class.getSimpleName(), Integer.class);
		classMap.put(Long.class.getSimpleName(), Long.class);
		classMap.put(Float.class.getSimpleName(), Float.class);
		classMap.put(Double.class.getSimpleName(), Double.class);

		classMap.put(Object.class.getSimpleName(), Object.class);
		classMap.put(String.class.getSimpleName(), String.class);
		//		classMap.put(JSONArray.class.getSimpleName(), JSONArray.class);//JSONArray implements Map
		classMap.put(Collection.class.getSimpleName(), Collection.class);//不允许指定<T>
		classMap.put(Map.class.getSimpleName(), Map.class);//不允许指定<T>
	}
	
	
	public TypeValueKeyEntry() {
		super();
	}
	
	
	/**
	 * @param <K>
	 * @param typeValue
	 * @return
	 */
	public static <K, V> boolean isCorrect(Entry<K, V> typeValue) {
		return typeValue != null && StringUtil.isNotEmpty(typeValue.getValue(), true);
	}

	/**
	 * @param typeValue
	 * @return
	 */
	public String toEntryString() {
		return toEntryString(getKey(), getValue());
	}
	/**
	 * @param typeValue
	 * @return
	 */
	public static String toEntryString(String typeKey, String valueKey) {
		return (typeKey == null ? "" : typeKey + ":") + valueKey;
	}
	/**
	 * @param type
	 * @param value
	 * @return
	 */
	public static String toEntryString(Class<?> type, Object value) {
		return toEntryString(type == null ? null : type.getSimpleName(), StringUtil.getString(value));
	}
	/**
	 * @param typeValue
	 * @return
	 */
	public static Entry<String, String> parseKeyEntry(String typeValue) {
		typeValue = StringUtil.getString(typeValue);//让客户端去掉所有空格 getNoBlankString(typeValue);
		if (typeValue.isEmpty()) {
			return null;
		}
		int index = typeValue.contains(":") ? typeValue.indexOf(":") : -1;

		Entry<String, String> entry = new Entry<String, String>();
		entry.setKey(index < 0 ? Object.class.getSimpleName() : typeValue.substring(0, index));
		entry.setValue(typeValue.substring(index + 1, typeValue.length()));

		return entry;
	}
	/**
	 * @param valueMap
	 * @param typeValue
	 * @return
	 */
	public static Entry<Class<?>, Object> parseEntry(Map<String, Object> valueMap, String typeValue) {
		typeValue = StringUtil.getString(typeValue);//让客户端去掉所有空格 getNoBlankString(typeValue);
		if (typeValue.isEmpty()) {
			return null;
		}
		int index = typeValue.contains(":") ? typeValue.indexOf(":") : -1;
		
		Entry<Class<?>, Object> entry = new Entry<>();
		entry.setKey(classMap.get(index < 0 ? Object.class.getSimpleName() : typeValue.substring(0, index)));
		entry.setValue(valueMap == null ? null : valueMap.get(typeValue.substring(index + 1, typeValue.length())));
		
		return entry;
	}
}
