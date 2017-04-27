package apijson.demo.ui;

import java.util.Set;

import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import apijson.demo.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
			String pairKey;
			for (String key : set) {
				value = request.get(key);
				if (value == null) {
					continue;
				}

				pairKey = new String(key instanceof String ? "\"" + key + "\"" : key);
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
								+ parentKey + ".add(" +  getItemKey(key) + ".toArray("
								+ count  + ", " + page + (prefix.isEmpty() ? "" : ", \"" + prefix + "\"") + "));";

						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					} else {//常规JSONObject，往下一级提取
						response += NEWLINE + NEWLINE + "//" + key + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";

						response += parse(key, (JSONObject) value);

						response += NEWLINE + NEWLINE + parentKey + ".put(" + pairKey + ", " + getTableKey(key) + ");";
						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					}
				} else {//其它Object，直接填充
					if (value instanceof String) {
						value = "\"" + value + "\"";
					} else if (value instanceof JSONArray) {
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

					response += NEWLINE + parentKey + ".put(" + pairKey + ", " + value + ");";
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
