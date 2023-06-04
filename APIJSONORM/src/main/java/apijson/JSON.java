/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/

package apijson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.JSONReader;

import java.util.List;

/**阿里FastJSON封装类 防止解析时异常
 * @author Lemon
 */
public class JSON {
	private static final String TAG = "JSON";

	/**判断json格式是否正确
	 * @param s
	 * @return
	 */
	public static boolean isJsonCorrect(String s) {
		//太长		Log.i(TAG, "isJsonCorrect  <<<<     " + s + "     >>>>>>>");
		if (s == null
				//				|| s.equals("[]")
				//				|| s.equals("{}")
				|| s.equals("")
				|| s.equals("[null]")
				|| s.equals("{null}")
				|| s.equals("null")) {
			return false;
		}
		return true;
	}

	/**获取有效的json
	 * @param s
	 * @return
	 */
	public static String getCorrectJson(String s) {
		return getCorrectJson(s, false);
	}
	/**获取有效的json
	 * @param s
	 * @param isArray
	 * @return
	 */
	public static String getCorrectJson(String s, boolean isArray) {
		s = StringUtil.getTrimedString(s);
		//		if (isArray) {
		//			while (s.startsWith("\"")) {
		//				s = s.substring(1);
		//			}
		//			while (s.endsWith("\"")) {
		//				s = s.substring(0, s.length() - 1);
		//			}
		//		}
		return s;//isJsonCorrect(s) ? s : null;
	}

	/**
	 * @param json
	 * @return
	 */
	private static final Feature[] DEFAULT_FASTJSON_FEATURES = {Feature.OrderedField, Feature.UseBigDecimal};
	public static Object parse(Object obj) {
		try {
			return com.alibaba.fastjson.JSON.parse(obj instanceof String ? (String) obj : toJSONString(obj), DEFAULT_FASTJSON_FEATURES);
		} catch (Exception e) {
			Log.i(TAG, "parse  catch \n" + e.getMessage());
		}
		return null;
	}

	/**obj转JSONObject
	 * @param obj
	 * @return
	 */
	public static JSONObject parseObject(Object obj) {
		if (obj instanceof JSONObject) {
			return (JSONObject) obj;
		}
		return parseObject(toJSONString(obj));
	}
	/**json转JSONObject
	 * @param json
	 * @return
	 */
	public static JSONObject parseObject(String json) {
		return parseObject(json, JSONObject.class);
	}
	/**json转实体类
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		if (clazz == null || StringUtil.isEmpty(json, true)) {
			Log.e(TAG, "parseObject  clazz == null || StringUtil.isEmpty(json, true) >> return null;");
		} else {
			try {
				return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(json), clazz, DEFAULT_FASTJSON_FEATURES);
			} catch (Exception e) {
				Log.i(TAG, "parseObject  catch \n" + e.getMessage());
			}
		}
		return null;
	}

	/**list转JSONArray
	 * @param list
	 * @return
	 */
	public static JSONArray parseArray(List<Object> list) {
		return new JSONArray(list);
	}
	/**obj转JSONArray
	 * @param obj
	 * @return
	 */
	public static JSONArray parseArray(Object obj) {
		if (obj instanceof JSONArray) {
			return (JSONArray) obj;
		}
		return parseArray(toJSONString(obj));
	}
	/**json转JSONArray
	 * @param json
	 * @return
	 */
	public static JSONArray parseArray(String json) {
		if (StringUtil.isEmpty(json, true)) {
			Log.e(TAG, "parseArray  StringUtil.isEmpty(json, true) >> return null;");
		} else {
			try {
				return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(json, true));
			} catch (Exception e) {
				Log.i(TAG, "parseArray  catch \n" + e.getMessage());
			}
		}
		return null;
	}
	/**JSONArray转实体类列表
	 * @param array
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(JSONArray array, Class<T> clazz) {
		return parseArray(toJSONString(array), clazz);
	}
	/**json转实体类列表
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		if (clazz == null || StringUtil.isEmpty(json, true)) {
			Log.e(TAG, "parseArray  clazz == null || StringUtil.isEmpty(json, true) >> return null;");
		} else {
			try {
				return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(json, true), clazz);
			} catch (Exception e) {
				Log.i(TAG, "parseArray  catch \n" + e.getMessage());
			}
		}
		return null;
	}

	/**实体类转json
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj);
		} catch (Exception e) {
			Log.e(TAG, "toJSONString  catch \n" + e.getMessage());
		}
		return null;
	}

	/**实体类转json
	 * @param obj
	 * @param features
	 * @return
	 */
	public static String toJSONString(Object obj, SerializerFeature... features) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj, features);
		} catch (Exception e) {
			Log.e(TAG, "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}

	/**格式化，显示更好看
	 * @param json
	 * @return
	 */
	public static String format(String json) {
		return format(parse(json));
	}
	/**格式化，显示更好看
	 * @param object
	 * @return
	 */
	public static String format(Object object) {
		return toJSONString(object, SerializerFeature.PrettyFormat);
	}

	/**判断是否为JSONObject
	 * @param obj instanceof String ? parseObject
	 * @return
	 */
	public static boolean isJSONObject(Object obj) {
		if (obj instanceof JSONObject) {
			return true;
		}
		if (obj instanceof String) {
			try {
				JSONObject json = parseObject((String) obj);
				return json != null && json.isEmpty() == false;
			} catch (Exception e) {
				Log.e(TAG, "isJSONObject  catch \n" + e.getMessage());
			}
		}

		return false;
	}
	/**判断是否为JSONArray
	 * @param obj instanceof String ? parseArray
	 * @return
	 */
	public static boolean isJSONArray(Object obj) {
		if (obj instanceof JSONArray) {
			return true;
		}
		if (obj instanceof String) {
			try {
				JSONArray json = parseArray((String) obj);
				return json != null && json.isEmpty() == false;
			} catch (Exception e) {
				Log.e(TAG, "isJSONArray  catch \n" + e.getMessage());
			}
		}

		return false;
	}

	/**判断是否为 Boolean,Number,String 中的一种
	 * @param obj
	 * @return
	 */
	public static boolean isBooleanOrNumberOrString(Object obj) {
		return obj instanceof Boolean || obj instanceof Number || obj instanceof String;
	}

}
