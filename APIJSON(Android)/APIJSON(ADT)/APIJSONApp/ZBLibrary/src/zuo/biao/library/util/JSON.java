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

package zuo.biao.library.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
//		Log.i(TAG, "isJsonCorrect  <<<<     " + s + "     >>>>>>>");
		if (s == null || s.equals("[]") 
				|| s.equals("{}") || s.equals("") || s.equals("[null]") || s.equals("{null}") || s.equals("null")) {
			return false;
		}
		return true;
	}

	/**获取有效的json
	 * @param s
	 * @return
	 */
	public static String getCorrectJson(String json) {
		return isJsonCorrect(json) ? json : "";
	}
	
	/**
	 * @param s
	 * @return
	 */
	public static JSONObject parseObject(Object obj) {
		return parseObject(toJSONString(obj));
	}
	/**json转JSONObject
	 * @param json
	 * @return
	 */
	public static JSONObject parseObject(String json) {
		try {
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(json));
		} catch (Exception e) {
			Log.e(TAG, "parseObject  catch \n" + e.getMessage());
		}
		return null;
	}

	/**JSONObject转实体类
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(JSONObject object, Class<T> clazz) {
		return parseObject(toJSONString(object), clazz);
	}
	/**json转实体类
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(json), clazz);
		} catch (Exception e) {
			Log.e(TAG, "parseObject  catch \n" + e.getMessage());
		}
		return null;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
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

	/**
	 * @param json
	 * @return
	 */
	public static JSONArray parseArray(String json) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(json));
		} catch (Exception e) {
			Log.e(TAG, "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}
	/**
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(json), clazz);
		} catch (Exception e) {
			Log.e(TAG, "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}

	/**格式化，显示更好看
	 * @param object
	 * @return
	 */
	public static String format(Object object) {
		try {
			return com.alibaba.fastjson.JSON.toJSONString(object, true);
		} catch (Exception e) {
			Log.e(TAG, "format  catch \n" + e.getMessage());
		}
		return null;
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

}
