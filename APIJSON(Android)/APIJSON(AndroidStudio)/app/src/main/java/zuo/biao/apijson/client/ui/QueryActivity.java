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

package zuo.biao.apijson.client.ui;

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.client.HttpManager;
import zuo.biao.apijson.client.HttpManager.OnHttpResponseListener;
import zuo.biao.apijson.client.JSONResponse;
import zuo.biao.apijson.client.R;
import zuo.biao.apijson.client.RequestUtil;
import zuo.biao.apijson.client.model.Comment;
import zuo.biao.apijson.client.model.User;
import zuo.biao.apijson.client.model.Work;
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

import com.alibaba.fastjson.JSONArray;

/**activity for requesting a query in Server
 * @author Lemon
 */
public class QueryActivity extends Activity implements OnHttpResponseListener {
	private static final String TAG = "QueryActivity";


	public static final String INTENT_TYPE = "INTENT_TYPE";
	public static final String INTENT_URL = "INTENT_URL";

	public static final String RESULT_URL = "RESULT_URL";

	/**
	 * @param context
	 * @param type
	 * @param url
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String url) {
		return new Intent(context, QueryActivity.class)
		.putExtra(QueryActivity.INTENT_TYPE, type)
		.putExtra(QueryActivity.INTENT_URL, url);
	}


	public static final int TYPE_SINGLE = 0;
	public static final int TYPE_RELY = 1;
	public static final int TYPE_ARRAY = 2;
	public static final int TYPE_COMPLEX = 3;


	private int type = TYPE_SINGLE;
	private String url;

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
		url = intent.getStringExtra(INTENT_URL);


		tvQueryResult = (TextView) findViewById(R.id.tvQueryResult);
		pbQuery = (ProgressBar) findViewById(R.id.pbQuery);
		etQueryUri = (EditText) findViewById(R.id.etQueryUri);

		etQueryUri.setText(StringUtil.getString(StringUtil.isNotEmpty(url, true)
				? url : "http://192.168.1.104:8080/get/"));//TODO my computer ipv4 address,edit it to an available one


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
		url = StringUtil.getNoBlankString(etQueryUri);
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
		Log.d(TAG, "setRequest  url = " + url + ";\n request = " + request);
	}

	/**request a query from server,and the result will be received by onHttpResponse method
	 */
	private void query() {
		setRequest();

		tvQueryResult.setText("requesting...\n\n url = " + url + "\n\n request = \n" + request
				+ "\n\n\n" + getResources().getString(R.string.query_error));
		pbQuery.setVisibility(View.VISIBLE);

		HttpManager.getInstance().get(url, request, this);
	}


	
	@Override
	public void onHttpResponse(int requestCode, final String resultJson, final Exception e) {
		Log.d(TAG, "onHttpResponse  resultJson = " + resultJson);
		if (e != null) {
			Log.e(TAG, "onHttpResponse e = " + e.getMessage());
		}
		JSONResponse response = new JSONResponse(resultJson);
		if (type == TYPE_ARRAY) {
			logList(JSONResponse.getList(response.getJSONObject("User[]"), User.class));
		} else if (type == TYPE_COMPLEX) {
			JSONArray array = JSONResponse.getJSONArray(response.getJSONObject("[]"));//, "Comment[]");//
			if (array == null || array.isEmpty()) {
				Log.e(TAG, "onHttpResponse  type == TYPE_COMPLEX >> array == null || array.isEmpty() >> return;");
				return;
			}
			response = new JSONResponse(array.getJSONObject(0));
			
			User user = JSONResponse.getObject(response, User.class);
			Log.d(TAG, "onHttpResponse  type == TYPE_COMPLEX >>  user = " + JSON.toJSONString(user));
			Work work = JSONResponse.getObject(response, Work.class);
			Log.d(TAG, "onHttpResponse  type == TYPE_COMPLEX >>  work = " + JSON.toJSONString(work));
			logList(JSONResponse.getList(response == null ? null : response.getJSONObject("Comment[]"), Comment.class));
		}
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isAlive) {
					pbQuery.setVisibility(View.GONE);
					Toast.makeText(context, "received result!", Toast.LENGTH_SHORT).show();

					tvQueryResult.setText(e == null || JSON.isJsonCorrect(resultJson)
							? StringUtil.getTrimedString(resultJson)
									: e.getMessage() + "\n\n\n" + getResources().getString(R.string.query_error));
					
				}
			}
		});
	}


	private <T> void logList(List<T> list) {
		if (list == null || list.isEmpty()) {
			Log.e(TAG, "logList  list == null || list.isEmpty() >> return;");
			return;
		}
		for (T data : list) {
			Log.d(TAG, "\n logList  " + (data == null ? "data" : data.getClass().getSimpleName())
					+ " = \n" + JSON.toJSONString(data));
		}		
	}



	/**open request URL String with a browser
	 */
	public void openWebSite() {
		setRequest();
		String webSite = null;
		try {
			webSite = StringUtil.getNoBlankString(url)
					+ URLEncoder.encode(StringUtil.getNoBlankString(request), UTF_8);
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
		setResult(RESULT_OK, new Intent().putExtra(RESULT_URL, url));
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAlive = false;
	}

}
