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

import java.util.Set;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import apijson.demo.R;
import apijson.demo.StringUtil;

import com.alibaba.fastjson.JSONObject;

/**自动生成代码
 * @author Lemon
 */
public class AutoActivity extends Activity {
	private static final String TAG = "AutoActivity";


	/**
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, AutoActivity.class);
	}



	EditText tvAutoResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_activity);


		tvAutoResult = (EditText) findViewById(R.id.tvAutoResult);



		String request = "{\"User\":{\"id\":1,\"@column\":\"id,name\"},\"[]\":{\"count\":3,\"page\":1,\"Moment\":{\"userId@\":\"User/id\"}}}";

		tvAutoResult.setText(StringUtil.getString(JSON.format(request)));

	}


	public void create(View v) {
		JSONRequest request = new JSONRequest();

		//User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest userRequest = new JSONRequest();
		userRequest.put("id", 1);
		userRequest.put("@column", "id,name");

		request.put("User", userRequest);
		//User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		//[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest item = new JSONRequest();

		//Moment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONRequest momentRequest = new JSONRequest();
		momentRequest.put("userId@", "User/id");

		item.put("Moment", momentRequest);
		//Moment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		request.add(item.toArray(0, 0));
		//[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		
		String json = StringUtil.getString(JSON.format(request));
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n request = \n" + json + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		
		tvAutoResult.setText(json);
	}



	public void auto(View v) {
		auto(StringUtil.getString(tvAutoResult));		
	}
	public void auto(String request) {
		String response = parse("", JSON.parseObject(request)); //newObjectRequest(request);

		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n request = \n" + request + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n response = \n" + response + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		tvAutoResult.setText(StringUtil.getString(response));
	}


	public static final String NEWLINE = "\n";

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
				pairKey = new String(key instanceof String ? "\"" + key + "\"" : key);
				if (value instanceof JSONObject) {//APIJSON Array转为常规JSONArray
					if (isArrayKey(key)) {//APIJSON Array转为常规JSONArray
						response += NEWLINE + NEWLINE + "//" + key + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
						
						int count = ((JSONObject) value).getIntValue(JSONRequest.KEY_COUNT);
						int page = ((JSONObject) value).getIntValue(JSONRequest.KEY_PAGE);
						((JSONObject) value).remove(JSONRequest.KEY_COUNT);
						((JSONObject) value).remove(JSONRequest.KEY_PAGE);

						response += parse(key, (JSONObject) value);


						String prefix = key.substring(0, key.length() - 2);
						response += NEWLINE + NEWLINE
								+ parentKey + ".add(" +  getItemKey(key) + ".toArray("
								+ count  + ", " + page + (prefix.isEmpty() ? "" : ", " + prefix) + "));";
						
						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					} else {//常规JSONObject，往下一级提取
						response += NEWLINE + NEWLINE + "//" + key + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
						
						response += parse(key, (JSONObject) value);

						response += NEWLINE + NEWLINE + parentKey + ".put(" + pairKey + ", " + getTableKey(key) + ");";
						response += NEWLINE + "//" + key + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NEWLINE;
					}
				} else {//其它Object，直接填充
					value = value instanceof String ? new String("\"" + value + "\"") : value;

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
		return addSuffix(key, "Request");
	}
	/**获取Table变量名
	 * @param key
	 * @return empty ? "request" : key + "Request" 且首字母小写
	 */
	private static String getItemKey(String key) {
		return addSuffix(key.substring(0, key.length() - 2), "Item");
	}
	/**
	 * @param key
	 * @param suffix
	 * @return key + suffix，第一个字母小写
	 */
	private static String addSuffix(String key, String suffix) {
		key = StringUtil.getNoBlankString(key);
		if (key.isEmpty()) {
			return firstCase(suffix);
		}

		return firstCase(key) + firstCase(suffix, true);
	}
	/**
	 * @param key
	 */
	private static String firstCase(String key) {
		return firstCase(key, false);
	}
	/**
	 * @param key
	 * @param upper
	 * @return
	 */
	private static String firstCase(String key, boolean upper) {
		key = StringUtil.getString(key);
		if (key.isEmpty()) {
			return "";
		}

		String first = key.substring(0, 1);
		key = (upper ? first.toUpperCase() : first.toLowerCase()) + key.substring(1, key.length());

		return key;
	}



	private static String getSimpleName(String arrayKey) {
		return JSONResponse.getSimpleName(arrayKey);
	}

	private static boolean isArrayKey(String key) {
		return JSONResponse.isArrayKey(key);
	}


}
