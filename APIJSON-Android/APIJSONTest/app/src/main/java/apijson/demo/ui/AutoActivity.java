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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import apijson.demo.R;
import apijson.demo.RequestUtil;
import apijson.demo.StringUtil;
import zuo.biao.apijson.JSON;

/**自动生成代码
 * @author Lemon
 */
public class AutoActivity extends Activity {
	private static final String TAG = "AutoActivity";

	public static final String KEY_REQUEST = "KEY_REQUEST";

	/**
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, AutoActivity.class);
	}


	private Activity context;

	private long id;
	private String url;
	private String request;

	private TextView tvAutoRequest;
	private TextView tvAutoResponse;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.auto_activity);
		context = this;


		//读取保存的配置
		SharedPreferences sp = getSharedPreferences(SelectActivity.CONFIG_PATH, Context.MODE_PRIVATE);
		id = sp.getLong(SelectActivity.KEY_ID, RequestUtil.DEFAULT_MOMENT_ID);
		url = sp.getString(SelectActivity.KEY_URL, null);
		request = sp.getString(KEY_REQUEST, null);

		if (StringUtil.isEmpty(request, true)) {
			request = "{\"Moment\":{\"id\":" + id + "},\"[]\":{\"count\":3,\"page\":0,\"Comment\":{\"momentId@\":\"Moment/id\",\"@column\":\"id,userId,content\"}}}";
		}



		tvAutoRequest = (TextView) findViewById(R.id.tvAutoRequest);
		tvAutoResponse = (TextView) findViewById(R.id.tvAutoResponse);



		tvAutoRequest.setText(StringUtil.getString(JSON.format(request)));

	}


	public void copy(View v) {
		StringUtil.copyText(context, StringUtil.getString((TextView) v));
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
		String response = CodeUtil.parse(JSON.parseObject(request)); //newObjectRequest(request);

		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n request = \n" + request + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n response = \n" + response + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		tvAutoResponse.setText(StringUtil.getString(response));
	}


	@SuppressLint("DefaultLocale")
	public void request(TextView tv) {
		request(StringUtil.getNoBlankString(tv).toLowerCase());
	}
	public void request(String method) {
		startActivityForResult(RequestActivity.createIntent(context, id, url, method
				, JSON.parseObject(StringUtil.getString(tvAutoRequest))), REQUEST_TO_REQUEST);
	}






	private static final int REQUEST_TO_REQUEST = 1;

	private Intent result;
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
				result = data;

				id = data.getLongExtra(RequestActivity.RESULT_ID, RequestUtil.DEFAULT_MOMENT_ID);
				url = data.getStringExtra(RequestActivity.RESULT_URL);

				tvAutoResponse.setText(StringUtil.getString(JSON.format(
						data.getStringExtra(RequestActivity.RESULT_RESPONSE))));
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void finish() {
		//保存配置
		getSharedPreferences(SelectActivity.CONFIG_PATH, Context.MODE_PRIVATE)
				.edit()
				.remove(KEY_REQUEST)
				.putString(KEY_REQUEST, StringUtil.getTrimedString(tvAutoRequest))
				.commit();

		//需要在SelectActivity实时更新
		setResult(RESULT_OK, new Intent().
				putExtra(RequestActivity.RESULT_ID, id).
				putExtra(RequestActivity.RESULT_URL, url));

		super.finish();
	}

}
