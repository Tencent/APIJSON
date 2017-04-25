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

import static zuo.biao.apijson.StringUtil.bigAlphaPattern;

import java.util.Set;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import apijson.demo.R;
import apijson.demo.StringUtil;
import apijson.demo.model.User;

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



		String request = "{\"User\":{\"id\":1,\"@column\":\"id,name\"},\"Moment\":{\"userId@\":\"/User/id\"}}";

		tvAutoResult.setText(StringUtil.getString(JSON.format(request)));



	}



	public void auto(View v) {
		auto(StringUtil.getString(tvAutoResult));		
	}
	public void auto(String request) {
		String response = parse("", JSON.parseObject(request)); //newObjectRequest(request);

		Log.d(TAG, "\n request = \n" + request);
		Log.d(TAG, "\n response = \n" + response);

		tvAutoResult.setText(StringUtil.getString(response));
	}


	public static final String NEWLINE = "\n";

	private String newObjectRequest(String json) {
		JSONRequest request = new JSONRequest();


		//单个JSONObject<<<<<<<<<<<<<<<<<
		JSONRequest userRequest = new JSONRequest();

		User user = new User();
		user.setId((long) 1);

		userRequest.add(JSON.parseObject(user));

		userRequest.setColumn("id,name");
		//单个JSONObject>>>>>>>>>>>>>>>>>>


		request.put("User", userRequest);


		return JSON.format(request);
	}


	public static String parse(final String name, final JSONObject request) {
		Log.i(TAG, "parse  request = \n" + JSON.toJSONString(request));
		if (request == null || request.isEmpty()) {
			Log.i(TAG, "parse  request == null || request.isEmpty() >> return request;");
			return null;
		}
		String parentKey = getTableKey(name);


		String response = NEWLINE + "JSONRequest " + parentKey + " = new JSONRequest();";

		Set<String> set = request.keySet();
		if (set != null) {

			Object value;
			String pairKey;
			String arrayKey;
			for (String key : set) {
				value = request.get(key);
				pairKey = new String(key instanceof String ? "\"" + key + "\"" : key);
				if (value instanceof JSONObject) {//APIJSON Array转为常规JSONArray
					if (isArrayKey(key)) {//APIJSON Array转为常规JSONArray
						//						arrayKey = key.substring(0, key.indexOf(JSONResponse.KEY_ARRAY));
					} else {//常规JSONObject，往下一级提取
						
						response += NEWLINE + parse(key, (JSONObject) value);
						
						response += NEWLINE + NEWLINE + parentKey + ".put(" + pairKey + ", " + getTableKey(key) + ");" + NEWLINE;
					}
				} else {//其它Object，直接填充
					value = value instanceof String ? new String("\"" + value + "\"") : value;
					
					response += NEWLINE + parentKey + ".put(" + pairKey + ", " + value + ");";
				}
			}
		}

		Log.i(TAG, "parse  return response = \n" + response);
		return response;
	}





	/**获取Table变量名
	 * @param key => StringUtil.getNoBlankString(key)
	 * @return empty ? "list" : key + "List" 且首字母小写
	 */
	private static String getTableKey(String key) {
		key = StringUtil.getNoBlankString(key);
		if (key.isEmpty()) {
			return "request";
		}

		String first = key.substring(0, 1);
		if (bigAlphaPattern.matcher(first).matches()) {
			key = first.toLowerCase() + key.substring(1, key.length());
		}
		return key + "Request";
	}


	private static String getSimpleName(String arrayKey) {
		return JSONResponse.getSimpleName(arrayKey);
	}

	private static boolean isArrayKey(String key) {
		return JSONResponse.isArrayKey(key);
	}


}
