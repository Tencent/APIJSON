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

package zuo.biao.apijson;

import zuo.biao.apijson.HttpManager.OnHttpResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**activity for query from a SQL database
 * @author Lemon
 */
public class QueryActivity extends Activity implements OnHttpResponseListener {
	private static final String TAG = "QueryActivity";


	public static final String INTENT_WAY = "INTENT_WAY";
	public static final String INTENT_REQUEST = "INTENT_REQUEST";

	public static Intent createIntent(Context context, int way, String request) {
		return new Intent(context, QueryActivity.class)
		.putExtra(QueryActivity.INTENT_WAY, way)
		.putExtra(QueryActivity.INTENT_REQUEST, request);
	}


	public static final int WAY_LOCAL = 0;
	public static final int WAY_SERVER = 1;

	private int way = WAY_LOCAL;
	private String request;

	private Activity context;
	private boolean isAlive;

	private TextView tvQueryResult;
	private ProgressBar pbQuery;
	private EditText etQueryUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_activity);
		context = this;
		isAlive = true;

		way = getIntent().getIntExtra(INTENT_WAY, way);
		request = getIntent().getStringExtra(INTENT_REQUEST);

		tvQueryResult = (TextView) findViewById(R.id.tvQueryResult);
		pbQuery = (ProgressBar) findViewById(R.id.pbQuery);
		etQueryUri = (EditText) findViewById(R.id.etQueryUri);

		etQueryUri.setText(StringUtil.getNoBlankString(way == 0 ? "" : "http://192.168.1.104:8080/get/"));

		query();
	}



	//click event,called form layout android:onClick <<<<<<<<<<<<<<<<
	public void query(View v) {
		query();
	}
	//click event,called form layout android:onClick >>>>>>>>>>>>>>>>

	String uri;
	private void query() {
		uri = StringUtil.getNoBlankString(etQueryUri);
		request = StringUtil.getTrimedString(request);

		tvQueryResult.setText("requesting...\n\n uri = " + uri + "\n\n request = " + request);
		pbQuery.setVisibility(View.VISIBLE);

		if (StringUtil.isUrl(uri)) {
			HttpManager.getInstance().get(uri, request, this);
		} else {
			//TODO
		}
	}



	@Override
	public void onHttpResponse(int requestCode, final String resultJson, final Exception e) {
		Log.d(TAG, "onHttpResponse  resultJson = " + resultJson);
		if (e != null) {
			Log.e(TAG, "onHttpResponse e = " + e.getMessage());
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isAlive) {
					pbQuery.setVisibility(View.GONE);
					Toast.makeText(context, "received result!", Toast.LENGTH_SHORT).show();
					
					tvQueryResult.setText(e == null || JSON.isJsonCorrect(resultJson)
							? StringUtil.getTrimedString(resultJson) : e.getMessage());
				}
			}
		});
	}



	@Override
	protected void onStop() {
		super.onStop();
		isAlive = false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAlive = false;
	}

}
