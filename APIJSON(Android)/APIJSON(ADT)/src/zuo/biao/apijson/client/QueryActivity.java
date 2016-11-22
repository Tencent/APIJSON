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

package zuo.biao.apijson.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import zuo.biao.apijson.client.HttpManager.OnHttpResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**activity for query from a SQL database
 * @author Lemon
 */
public class QueryActivity extends Activity implements OnHttpResponseListener {
	private static final String TAG = "QueryActivity";


	public static final String INTENT_TYPE = "INTENT_TYPE";
	public static final String INTENT_URI = "INTENT_URI";

	public static final String RESULT_URI = "RESULT_URI";

	/**
	 * @param context
	 * @param type
	 * @param uri
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String uri) {
		return new Intent(context, QueryActivity.class)
		.putExtra(QueryActivity.INTENT_TYPE, type)
		.putExtra(QueryActivity.INTENT_URI, uri);
	}


	public static final int TYPE_SINGLE = 0;
	public static final int TYPE_RELY = 1;
	public static final int TYPE_ARRAY = 2;
	public static final int TYPE_COMPLEX = 3;


	private int type = TYPE_SINGLE;
	private String uri;

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

		Intent intent = getIntent();
		type = intent.getIntExtra(INTENT_TYPE, type);
		uri = intent.getStringExtra(INTENT_URI);


		tvQueryResult = (TextView) findViewById(R.id.tvQueryResult);
		pbQuery = (ProgressBar) findViewById(R.id.pbQuery);
		etQueryUri = (EditText) findViewById(R.id.etQueryUri);

		etQueryUri.setText(StringUtil.getString(StringUtil.isNotEmpty(uri, true)
				? uri : "http://192.168.1.104:8080/get/"));


		query();


		findViewById(R.id.btnQueryQuery).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				query();
			}
		});
		findViewById(R.id.btnQueryQuery).setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				openWebSite();
				return true;
			}
		});
	}



	//click event,called form layout android:onClick <<<<<<<<<<<<<<<<
	public void query(View v) {
		query();
	}
	//click event,called form layout android:onClick >>>>>>>>>>>>>>>>

	private String request;
	public void setRequest() {
		uri = StringUtil.getNoBlankString(etQueryUri);
		switch (type) {
		case TYPE_SINGLE:
			request = JSON.toJSONString(RequestUtil.newSingleRequest());
			break;
		case TYPE_RELY:
			request = JSON.toJSONString(RequestUtil.newRelyRequest());
			break;
		case TYPE_ARRAY:
			request = JSON.toJSONString(RequestUtil.newArrayRequest());
			break;
		default:
			request = JSON.toJSONString(RequestUtil.newComplexRequest());
			break;
		}
		System.out.println("request = " + request);
	}

	private void query() {
		setRequest();

		tvQueryResult.setText("requesting...\n\n uri = " + uri + "\n\n request = \n" + request);
		pbQuery.setVisibility(View.VISIBLE);

		HttpManager.getInstance().get(uri, request, this);
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


	/**打开网站
	 */
	public void openWebSite() {
		setRequest();
		String webSite = null;
		try {
			webSite = StringUtil.getNoBlankString(uri)
					+ URLEncoder.encode(StringUtil.getNoBlankString(request), HttpManager.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (StringUtil.isNotEmpty(webSite, true) == false) {
			Log.e(TAG, "openWebSite  StringUtil.isNotEmpty(webSite, true) == false >> return;");
			return;
		}

		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webSite)));
	}



	@Override
	public void finish() {
		setResult(RESULT_OK, new Intent().putExtra(RESULT_URI, uri));
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAlive = false;
	}

}
