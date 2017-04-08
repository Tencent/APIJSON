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

package zuo.biao.apijson.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import zuo.biao.apijson.StringUtil;

/**key:value
 * @author Lemon
 */
public class Pair extends Entry<String, String> {

	private static Map<String, Class<?>> classMap;
	static {
		classMap = new HashMap<String, Class<?>>();
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


	public Pair() {
		super();
	}

	public boolean isEmpty(boolean trim) {
		return StringUtil.isNotEmpty(key, trim) == false && StringUtil.isNotEmpty(value, trim) == false;
	}

	/**
	 * @param <K>
	 * @param pair
	 * @return
	 */
	public static <K, V> boolean isCorrect(Entry<K, V> pair) {
		return pair != null && StringUtil.isNotEmpty(pair.getValue(), true);
	}

	/**
	 * @param pair
	 * @return
	 */
	public String toPairString() {
		return toPairString(getKey(), getValue());
	}
	/**
	 * @param pair
	 * @return
	 */
	public static String toPairString(String typeKey, String valueKey) {
		return (typeKey == null ? "" : typeKey + ":") + valueKey;
	}
	/**
	 * @param type
	 * @param value
	 * @return
	 */
	public static String toPairString(Class<?> type, Object value) {
		return toPairString(type == null ? null : type.getSimpleName(), StringUtil.getString(value));
	}

	/**
	 * isRightValueDefault = false;
	 * "key":null不应该出现？因为FastJSON内默认不存null
	 * @param pair leftKey:rightValue
	 * @return {@link #parseEntry(String, boolean)}
	 */
	public static Entry<String, String> parseEntry(String pair) {
		return parseEntry(pair, false);
	}
	/**
	 * isRightValueDefault = false;
	 * "key":null不应该出现？因为FastJSON内默认不存null
	 * @param pair leftKey:rightValue
	 * @param isRightValueDefault 右边值缺省，当pair不包含 : 时默认整个pair为leftKey；false-相反
	 * @return {@link #parseEntry(String, boolean, String)}
	 */
	public static Entry<String, String> parseEntry(String pair, boolean isRightValueDefault) {
		return parseEntry(pair, isRightValueDefault, null);
	}
	/**
	 * "key":null不应该出现？因为FastJSON内默认不存null
	 * @param pair leftKey:rightValue
	 * @param isRightValueDefault 右边值缺省，当pair不包含 : 时默认整个pair为leftKey；false-相反
	 * @param defaultKey 缺省值
	 * @return @NonNull
	 */
	public static Entry<String, String> parseEntry(String pair, boolean isRightValueDefault, String defaultValue) {
		pair = StringUtil.getString(pair);//让客户端去掉所有空格 getNoBlankString(pair);
		Entry<String, String> entry = new Entry<String, String>();
		if (pair.isEmpty() == false) {
			int index = pair.indexOf(":");
			if (index < 0) {
				entry.setKey(isRightValueDefault ? pair : defaultValue);
				entry.setValue(isRightValueDefault ? defaultValue : pair);
			} else {
				entry.setKey(pair.substring(0, index));
				entry.setValue(pair.substring(index + 1, pair.length()));
			}

		}
		return entry;
	}
	/**
	 * @param pair
	 * @return
	 */
	public static Entry<String, String> parseVariableEntry(String pair) {
		return parseEntry(pair, false, Object.class.getSimpleName());
	}
	/**
	 * @param pair
	 * @param valueMap
	 * @return
	 */
	public static Entry<Class<?>, Object> parseVariableEntry(String pair, Map<String, Object> valueMap) {
		pair = StringUtil.getString(pair);//让客户端去掉所有空格 getNoBlankString(pair);
		Entry<Class<?>, Object> entry = new Entry<Class<?>, Object>();
		if (pair.isEmpty() == false) {
			int index = pair.contains(":") ? pair.indexOf(":") : -1;

			entry.setKey(classMap.get(index < 0 ? Object.class.getSimpleName() : pair.substring(0, index)));
			entry.setValue(valueMap == null ? null : valueMap.get(pair.substring(index + 1, pair.length())));
		}
		return entry;
	}
}
