/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.ui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;

import apijson.demo.StringUtil;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;

/**生成代码的工具类
 * @author Lemon
 */
public class CodeUtil {


	public static final String NEWLINE = "\n";

	/**
	 * name = ""
	 * @param request
	 * @return {@link #parse(String, JSONObject)}
	 */
	public static String parse(JSONObject request) {
		return parse("", request);
	}

	/**
	 * @param name
	 * @param request
	 * @return
	 */
	public static String parse(final String name, final JSONObject request) {
		//		Log.i(TAG, "parse  request = \n" + JSON.toJSONString(request));
		if (request == null || request.isEmpty()) {
			//			Log.i(TAG, "parse  request == null || request.isEmpty() >> return request;");
			return null;
		}
		String parentKey = isArrayKey(name) ? getItemKey(name) : getTableKey(name);


		String response = NEWLINE + "JSONRequest " + parentKey + " = new JSONRequest();";

		Set<String> set = request.keySet();
		if (set != null) {

			Object value;
			for (String key : set) {
				value = request.get(key);
				if (value == null) {
					continue;
				}

				if (value instanceof JSONObject) {//APIJSON Array转为常规JSONArray
					if (isArrayKey(key)) {//APIJSON Array转为常规JSONArray
						response += NEWLINE + NEWLINE + "//" + key + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";

						int count = ((JSONObject) value).getIntValue(JSONRequest.KEY_COUNT);
						int page = ((JSONObject) value).getIntValue(JSONRequest.KEY_PAGE);
						((JSONObject) value).remove(JSONRequest.KEY_COUNT);
						((JSONObject) value).remove(JSONRequest.KEY_PAGE);

						response += parse(key, (JSONObject) value);


						String prefix = key.substring(0, key.length() - 2);
						response += NEWLINE + NEWLINE
								+ parentKey + ".putAll(" +  getItemKey(key) + ".toArray("
								+ count  + ", " + page + (prefix.isEmpty() ? "" : ", \"" + prefix + "\"") + "));";

						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					}
					else {//常规JSONObject，往下一级提取
						response += NEWLINE + NEWLINE + "//" + key + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";

						response += parse(key, (JSONObject) value);

						response += NEWLINE + NEWLINE + parentKey + ".put(\"" + key + "\", " + getTableKey(key) + ");";
						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					}
				}
				else {//其它Object，直接填充
					if (value instanceof String) {
						value = "\"" + value + "\"";
					}
					else if (value instanceof JSONArray) {
						String s = StringUtil.getString(value);
						if (s.startsWith("[")) {
							s = s.substring(1);
						}
						if (s.endsWith("]")) {
							s = s.substring(0, s.length() - 1);
						}
						//	String type = ((JSONArray) value).getClass().getSimpleName();
						//	value = "new " + type.substring(0, type.length() - 2) + "[]{" + s + "}";
						value = "new Object[]{" + s + "}";//反射获取泛型太麻烦，反正开发中还要改的
					}

					response += NEWLINE + parentKey + ".put(\"" + key + "\", " + value + ");";
				}
			}
		}

		//		Log.i(TAG, "parse  return response = \n" + response);
		return response;
	}





	/**获取Table变量名
	 * @param key
	 * @return empty ? "request" : key + "Request" 且首字母小写
	 */
	private static String getTableKey(String key) {
		return StringUtil.addSuffix(key, "Request");
	}
	/**获取Table变量名
	 * @param key
	 * @return empty ? "request" : key + "Request" 且首字母小写
	 */
	private static String getItemKey(String key) {
		return StringUtil.addSuffix(key.substring(0, key.length() - 2), "Item");
	}


	private static boolean isArrayKey(String key) {
		return JSONResponse.isArrayKey(key);
	}

}
