/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/

package apijson;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import apijson.orm.exception.UnsupportedDataTypeException;
import apijson.Log;
import apijson.StringUtil;

/**JSON工具类 防止解析时异常
 * @author Lemon
 */
public interface JSON {
	static final String TAG = "JSON";

	JSONParser<? extends Map<String, Object>, ? extends List<Object>> DEFAULT_JSON_PARSER = new JSONParser<>() {

		@Override
		public Map<String, Object> parseJSON(Object json) {
			return Map.of();
		}

		@Override
		public Map<String, Object> parseObject(Object json) {
			return Map.of();
		}

		@Override
		public <T> T parseObject(Object json, Class<T> clazz) {
			return null;
		}

		@Override
		public List<Object> parseArray(Object json) {
			return List.of();
		}

		@Override
		public <T> List<T> parseArray(Object json, Class<T> clazz) {
			return List.of();
		}

		//
		@Override
		public String toJSONString(Object obj) {
			return JSON.toJSONString(obj);
		}

		@Override
		public Map<String, Object> createJSONObject() {
			return new LinkedHashMap<>();
		}

		@Override
		public List<Object> createJSONArray() {
			return new ArrayList<>();
		}
	};

	public static JSONCreator<? extends Map<String, Object>, ? extends List<Object>> DEFAULT_JSON_CREATOR = DEFAULT_JSON_PARSER;


	public static Object parseJSON(Object json) throws Exception {
		if (json instanceof Boolean || json instanceof Number || json instanceof Enum<?>) {
			return json;
		}

		String s = StringUtil.trim(toJSONString(json));
		if (s.startsWith("{")) {
			return parseObject(json, DEFAULT_JSON_PARSER);
		}

		if (s.startsWith("[")) {
			return parseArray(json, DEFAULT_JSON_PARSER);
		}

		throw new UnsupportedDataTypeException("JSON 格式错误！" + s);
	}

	/**
	 * @param json
	 * @return
	 */
	public static Map<String, Object> parseObject(Object json) {
		return parseObject(json, DEFAULT_JSON_PARSER);
	}
	public static <M extends Map<String, Object>, L extends List<Object>> M parseObject(Object json, JSONParser<M, L> parser) {
		String s = toJSONString(json);
		if (StringUtil.isEmpty(s, true)) {
			return null;
		}

		try {
			M obj = parser.parseObject(s);
			return obj;
		} catch (Exception e) {
			Log.i(TAG, "parseObject catch \n" + e.getMessage());
		}
		return null;
	}
	public static <T, M extends Map<String, Object>, L extends List<Object>> T parseObject(Object json, Class<T> clazz, JSONParser<M, L> parser) {
		String s = toJSONString(json);
		if (StringUtil.isEmpty(s, true)) {
			return null;
		}

		try {
			T obj = parser.parseObject(s, clazz);
			return obj;
		} catch (Exception e) {
			Log.i(TAG, "parseObject catch \n" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param json
	 * @return
	 */
	public static List<Object> parseArray(Object json) {
		return parseArray(json, DEFAULT_JSON_PARSER);
	}

	public static <M extends Map<String, Object>, L extends List<Object>> L parseArray(Object json, JSONParser<M, L> parser) {
		String s = toJSONString(json);
		if (StringUtil.isEmpty(s, true)) {
			return null;
		}

		try {
			L arr = parser.parseArray(s);
			return arr;
		} catch (Exception e) {
			Log.i(TAG, "parseArray catch \n" + e.getMessage());
		}
		return null;
	}

//	public static <T, M extends Map<String, Object>> List<T> parseArray(Object json, Class<T> clazz) {
//		return parseArray(json, clazz, DEFAULT_JSON_PARSER);
//	}
	public static <T, M extends Map<String, Object>> List<T> parseArray(Object json, Class<T> clazz, JSONParser<M, List<Object>> parser) {
		String s = toJSONString(json);
		if (StringUtil.isEmpty(s, true)) {
			return null;
		}

		try {
			List<T> arr = parser.parseArray(s, clazz);
			return arr;
		} catch (Exception e) {
			Log.i(TAG, "parseArray catch \n" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		if (obj == null) {
			return null;
		}
		
		// In a real implementation, you would use a JSON parser library
		// Here we're just providing a basic implementation to replace fastjson
		try {
			// For now, this is a placeholder. In a real implementation,
			// you would convert the object to a JSON string
			if (obj instanceof String) {
				return (String) obj;
			}

			if (obj instanceof Map) {
				// Simple JSON object format
				StringBuilder sb = new StringBuilder("{");
				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) obj;
				boolean first = true;
				for (Map.Entry<Object, Object> entry : map.entrySet()) {
					if (! first) {
						sb.append(",");
					}
					first = false;
					sb.append("\"").append(entry.getKey()).append("\":");
					Object value = entry.getValue();
					if (value instanceof String) {
						sb.append("\"").append(value).append("\"");
					} else {
						sb.append(toJSONString(value));
					}
				}
				sb.append("}");
				return sb.toString();
			}

			if (obj instanceof List) {
				// Simple JSON array format
				StringBuilder sb = new StringBuilder("[");
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) obj;
				boolean first = true;
				for (Object item : list) {
					if (! first) {
						sb.append(",");
					}
					first = false;
					if (item instanceof String) {
						sb.append("\"").append(item).append("\"");
					} else {
						sb.append(toJSONString(item));
					}
				}
				sb.append("]");
				return sb.toString();
			}

			return obj.toString();
		}
		catch (Exception e) {
			Log.i(TAG, "toJSONString catch \n" + e.getMessage());
		}
		return null;
	}


	/**判断是否为JSONObject或JSONArray的isXxx方法名
	 * @param key
	 * @return
	 */
	public static boolean isJSONType(String key) {
		return key != null && key.startsWith("is") && key.length() > 2 && key.contains("JSON");
	}

	public static boolean isBooleanOrNumberOrString(Object obj) {
		return obj instanceof Boolean || obj instanceof Number || obj instanceof String;
	}

	/**
	 * Get a value from a Map and convert to the specified type
	 * @param map Source map
	 * @param key The key
	 * @param <T> Target type
	 * @return The converted value
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Map<String, Object> map, String key) {
		return map == null || key == null ? null : (T) map.get(key);
	}

	/**
	 * Get a value from a Map and convert to the specified type
	 * @param list Source map
	 * @param index The key
	 * @param <T> Target type
	 * @return The converted value
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(List<Object> list, int index) {
		return list == null || index < 0 || index >= list.size() ? null : (T) list.get(index);
	}
//	/**
//	 * Get a value from a Map and convert to the specified type
//	 * @param map Source map
//	 * @param key The key
//	 * @param <T> Target type
//	 * @return The converted value
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T get(List<T> list, int index) {
//		return list == null || index < 0 || index >= list.size() ? null : list.get(index);
//	}

	/**
	 * Get a Map value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The Map value
	 * @throws UnsupportedDataTypeException If value is not a Map and cannot be converted
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}
		
		if (value instanceof Map) {
			return (Map<String, Object>) value;
		}
		
		throw new UnsupportedDataTypeException("Value for key '" + key + "' is not a Map: " + value.getClass().getName());
	}

	/**
	 * Get a List value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The List value
	 * @throws UnsupportedDataTypeException If value is not a List and cannot be converted
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getList(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}
		
		if (value instanceof List) {
			return (List<Object>) value;
		}
		
		throw new UnsupportedDataTypeException("Value for key '" + key + "' is not a List: " + value.getClass().getName());
	}

	/**
	 * Get an int value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The int value
	 * @throws UnsupportedDataTypeException If value cannot be converted to int
	 */
	public static Integer getInteger(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to int: " + e.getMessage());
			}
		}

		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to int");
	}

	/**
	 * Get an int value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The int value
	 * @throws UnsupportedDataTypeException If value cannot be converted to int
	 */
	public static int getIntValue(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return 0;
		}
		
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		
		if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to int: " + e.getMessage());
			}
		}
		
		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to int");
	}

	/**
	 * Get an int value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The int value
	 * @throws UnsupportedDataTypeException If value cannot be converted to int
	 */
	public static Long getLong(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		if (value instanceof String) {
			try {
				return Long.parseLong((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to int: " + e.getMessage());
			}
		}

		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to int");
	}

	/**
	 * Get a long value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The long value
	 * @throws UnsupportedDataTypeException If value cannot be converted to long
	 */
	public static long getLongValue(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return 0;
		}
		
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		
		if (value instanceof String) {
			try {
				return Long.parseLong((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to long: " + e.getMessage());
			}
		}
		
		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to long");
	}

	/**
	 * Get a double value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The double value
	 * @throws UnsupportedDataTypeException If value cannot be converted to double
	 */
	public static Double getDouble(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}
		
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		
		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to double: " + e.getMessage());
			}
		}
		
		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to double");
	}

	/**
	 * Get a double value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The double value
	 * @throws UnsupportedDataTypeException If value cannot be converted to double
	 */
	public static double getDoubleValue(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return 0;
		}

		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to double: " + e.getMessage());
			}
		}

		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to double");
	}


	/**
	 * Get a boolean value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The boolean value
	 * @throws UnsupportedDataTypeException If value cannot be converted to boolean
	 */
	public static Boolean getBoolean(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}

		if (value instanceof Boolean) {
			return (Boolean) value;
		}

		if (value instanceof String) {
			String str = ((String) value).toLowerCase();
			if (str.equals("true") || str.equals("false")) {
				return Boolean.parseBoolean(str);
			}
			throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to boolean");
		}

		if (value instanceof Number) {
			int intValue = ((Number) value).intValue();
			if (intValue == 0 || intValue == 1) {
				return intValue != 0;
			}
			throw new UnsupportedDataTypeException("Cannot convert Number value '" + value + "' to boolean. Only 0 and 1 are supported.");
		}

		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to boolean");
	}

	/**
	 * Get a boolean value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The boolean value
	 * @throws UnsupportedDataTypeException If value cannot be converted to boolean
	 */
	public static boolean getBooleanValue(Map<String, Object> map, String key) throws UnsupportedDataTypeException {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return false;
		}
		
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		
		if (value instanceof String) {
			String str = ((String) value).toLowerCase();
			if (str.equals("true") || str.equals("false")) {
				return Boolean.parseBoolean(str);
			}
			throw new UnsupportedDataTypeException("Cannot convert String value '" + value + "' to boolean");
		}
		
		if (value instanceof Number) {
			int intValue = ((Number) value).intValue();
			if (intValue == 0 || intValue == 1) {
				return intValue != 0;
			}
			throw new UnsupportedDataTypeException("Cannot convert Number value '" + value + "' to boolean. Only 0 and 1 are supported.");
		}
		
		throw new UnsupportedDataTypeException("Cannot convert value of type " + value.getClass().getName() + " to boolean");
	}

	/**
	 * Get a string value from a Map
	 * @param map Source map
	 * @param key The key
	 * @return The string value
	 */
	public static String getString(Map<String, Object> map, String key) {
		Object value = map == null || key == null ? null : map.get(key);
		if (value == null) {
			return null;
		}
		
		return value.toString();
	}
}
