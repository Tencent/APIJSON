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

/**阿里fastjson封装类 防止解析时异常
 * @modifier Lemon
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
	public static String getCorrectJson(String s) {
		return isJsonCorrect(s) ? s : "";
	}
	
	/**
	 * @param s
	 * @return
	 */
	public static JSONObject parseObject(String s) {
		try {
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(s));
		} catch (Exception e) {
			Log.e(TAG, "parseObject  catch \n" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String s, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseObject(getCorrectJson(s), clazz);
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
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj);
		} catch (Exception e) {
			Log.e(TAG, "toJSONString  catch \n" + e.getMessage());
		}
		return null;
	}

	/**
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static JSONArray parseArray(String s) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(s));
		} catch (Exception e) {
			Log.e(TAG, "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}
	/**
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String s, Class<T> clazz) {
		try {
			return com.alibaba.fastjson.JSON.parseArray(getCorrectJson(s), clazz);
		} catch (Exception e) {
			Log.e(TAG, "parseArray  catch \n" + e.getMessage());
		}
		return null;
	}

}
