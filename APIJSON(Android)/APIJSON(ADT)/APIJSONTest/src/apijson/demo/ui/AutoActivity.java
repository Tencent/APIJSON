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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
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


	private Activity context;

	private TextView tvAutoRequest;
	private TextView tvAutoResponse;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.auto_activity);
		context = this;

		tvAutoRequest = (TextView) findViewById(R.id.tvAutoRequest);
		tvAutoResponse = (TextView) findViewById(R.id.tvAutoResponse);



		String request = "{\"User\":{\"id\":38710},\"[]\":{\"count\":3,\"page\":1,\"Moment\":{\"userId@\":\"User/id\",\"@column\":\"id,userId,content\"}}}";

		tvAutoRequest.setText(StringUtil.getString(JSON.format(request)));

	}


	public void copy(View v) {
		StringUtil.copyText(context, StringUtil.getString(tvAutoResponse));	
	}

	public void auto(View v) {
		auto(StringUtil.getString(tvAutoRequest));		
	}
	
	public void get(View v) {
		request((TextView) v);
	}
	public void head(View v) {
		request((TextView) v);
	}
	public void post(View v) {
		request((TextView) v);
	}
	public void put(View v) {
		request((TextView) v);
	}
	public void delete(View v) {
		request((TextView) v);
	}












	public void auto(String request) {
		String response = parse("", JSON.parseObject(request)); //newObjectRequest(request);

		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n request = \n" + request + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n response = \n" + response + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		tvAutoResponse.setText(StringUtil.getString(response));
	}


	@SuppressLint("DefaultLocale")
	public void request(TextView tv) {
		request(StringUtil.getNoBlankString(tv).toLowerCase());
	}
	public void request(String method) {
		startActivityForResult(RequestActivity.createIntent(context, 0, null, method
				, JSON.parseObject(StringUtil.getString(tvAutoRequest)), false), REQUEST_TO_REQUEST);
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
	@SuppressLint("DefaultLocale")
	private static String firstCase(String key, boolean upper) {
		key = StringUtil.getString(key);
		if (key.isEmpty()) {
			return "";
		}

		String first = key.substring(0, 1);
		key = (upper ? first.toUpperCase() : first.toLowerCase()) + key.substring(1, key.length());

		return key;
	}


	private static boolean isArrayKey(String key) {
		return JSONResponse.isArrayKey(key);
	}






	private static final int REQUEST_TO_REQUEST = 1;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_REQUEST:
			if (data == null) {
				Toast.makeText(context, "onActivityResult  data == null !!!", Toast.LENGTH_SHORT).show();
			} else {
				tvAutoResponse.setText(StringUtil.getString(JSON.format(
						data.getStringExtra(RequestActivity.RESULT_RESPONSE))));
			}
			break;
		default:
			break;
		}
	}


}
