/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

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

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import apijson.demo.HttpManager;
import apijson.demo.HttpManager.OnHttpResponseListener;
import apijson.demo.R;
import apijson.demo.RequestUtil;
import apijson.demo.StringUtil;
import apijson.demo.model.Comment;
import apijson.demo.model.User;
import apijson.demo.model.Wallet;
import apijson.demo.model.Work;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**activity for requesting a query in Server
 * @author Lemon
 */
public class QueryActivity extends Activity implements OnHttpResponseListener {
	private static final String TAG = "QueryActivity";


	public static final String INTENT_TYPE = "INTENT_TYPE";
	public static final String INTENT_URL = "INTENT_URL";
	public static final String INTENT_ID = "INTENT_ID";

	public static final String RESULT_URL = "RESULT_URL";
	public static final String RESULT_ID = "RESULT_ID";


	/**
	 * @param context
	 * @param type
	 * @param url
	 * @param id
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String url, long id) {
		return new Intent(context, QueryActivity.class)
		.putExtra(QueryActivity.INTENT_TYPE, type)
		.putExtra(QueryActivity.INTENT_URL, url)
		.putExtra(QueryActivity.INTENT_ID, id);
	}


	public static final int TYPE_POST = 0;
	public static final int TYPE_PUT = 1;
	public static final int TYPE_DELETE = 2;

	public static final int TYPE_SINGLE = 10;
	public static final int TYPE_COLUMNS = 11;
	public static final int TYPE_RELY = 12;
	public static final int TYPE_ARRAY = 13;
	public static final int TYPE_COMPLEX = 14;
	public static final int TYPE_ACCESS_ERROR = 5;
	public static final int TYPE_ACCESS_PERMITTED = 6;



	private Activity context;
	private boolean isAlive;

	private int type = TYPE_COMPLEX;
	private String url;
	private long id;

	private TextView tvQueryResult;
	private ProgressBar pbQuery;
	private EditText etQueryUrl;
	private Button btnQueryQuery;

	private String error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_activity);
		context = this;
		isAlive = true;

		Intent intent = getIntent();
		type = intent.getIntExtra(INTENT_TYPE, type);
		url = intent.getStringExtra(INTENT_URL);
		id = intent.getLongExtra(INTENT_ID, id);

		
		
		tvQueryResult = (TextView) findViewById(R.id.tvQueryResult);
		pbQuery = (ProgressBar) findViewById(R.id.pbQuery);
		etQueryUrl = (EditText) findViewById(R.id.etQueryUrl);
		btnQueryQuery = (Button) findViewById(R.id.btnQueryQuery);


		
		etQueryUrl.setText(StringUtil.getString(StringUtil.isNotEmpty(url, true)
				? url : "http://139.196.140.118:8080/"));//TODO my server ipv4 address, edit it to your server url
		btnQueryQuery.setText(getMethod(type));

		error = String.format(getResources().getString(R.string.query_error), StringUtil.getTrimedString(btnQueryQuery));

		query();

		

		btnQueryQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				query();
			}
		});
		btnQueryQuery.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				openWebSite();
				return true;
			}
		});

		
		
		if (id > 0) {
			Toast.makeText(context, String.format(getResources().getString(R.string.user_id_changed), "" + id)
					, Toast.LENGTH_LONG).show();
		}
	}


	/**request a query from server,and the result will be received by onHttpResponse method
	 */
	private void query() {
		setRequest();

		final String fullUrl = getUrl(type);

		tvQueryResult.setText("requesting...\n\n url = " + fullUrl + "\n\n request = \n" + JSON.format(request) + "\n\n\n" + error);
		pbQuery.setVisibility(View.VISIBLE);

		if (type < 10) {
			HttpManager.getInstance().post(fullUrl, request, this);
		} else {
			HttpManager.getInstance().get(fullUrl, request, this);
		}
	}

	/**open request URL String with a browser
	 */
	public void openWebSite() {
		if (type < 10) {
			Toast.makeText(context, R.string.browser_can_only_receive_get_response, Toast.LENGTH_LONG).show();
		}
		setRequest();
		String webSite = null;
		try {
			webSite = StringUtil.getNoBlankString(getUrl(type))
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




	private String getUrl(int type) {
		return url + StringUtil.getTrimedString(btnQueryQuery) + "/";
	}

	private String getMethod(int type) {
		switch (type) {
		case TYPE_POST:
			return "post";
		case TYPE_PUT:
			return "put";
		case TYPE_DELETE:
			return "delete";
		case TYPE_ACCESS_ERROR:
		case TYPE_ACCESS_PERMITTED:
			return "post_get";
		default:
			return "get";
		}
	}

	private String request;
	public void setRequest() {
		url = StringUtil.getNoBlankString(etQueryUrl);
		switch (type) {
		case TYPE_POST:
			request = JSON.toJSONString(RequestUtil.newPostRequest());
			break;
		case TYPE_PUT:
			request = JSON.toJSONString(RequestUtil.newPutRequest(id));
			break;
		case TYPE_DELETE:
			request = JSON.toJSONString(RequestUtil.newDeleteRequest(id));
			break;

		case TYPE_SINGLE:
			request = JSON.toJSONString(RequestUtil.newSingleRequest(id));
			break;
		case TYPE_COLUMNS:
			request = JSON.toJSONString(RequestUtil.newColumnsRequest(id));
			break;
		case TYPE_RELY:
			request = JSON.toJSONString(RequestUtil.newRelyRequest(id));
			break;
		case TYPE_ARRAY:
			request = JSON.toJSONString(RequestUtil.newArrayRequest());
			break;
		case TYPE_ACCESS_ERROR:
			request = JSON.toJSONString(RequestUtil.newAccessErrorRequest(id));
			break;
		case TYPE_ACCESS_PERMITTED:
			request = JSON.toJSONString(RequestUtil.newAccessPermittedRequest(id));
			break;
		default:
			request = JSON.toJSONString(RequestUtil.newComplexRequest());
			break;
		}
		Log.d(TAG, "setRequest  url = " + url + ";\n request = " + request);
	}


	@Override
	public void onHttpResponse(int requestCode, final String resultJson, final Exception e) {
		Log.d(TAG, "onHttpResponse  resultJson = " + resultJson);
		if (e != null) {
			Log.e(TAG, "onHttpResponse e = " + e.getMessage());
		}
		JSONResponse response = new JSONResponse(resultJson);
		switch (type) {
		case TYPE_POST:
			User postedUser = response.getObject(User.class);
			id = postedUser == null ? 0 : postedUser.getId();
			Log.d(TAG, "onHttpResponse  id = " + id);
			break;
		case TYPE_DELETE:
			JSONObject result = response.getJSONObject(User.class.getSimpleName());
			if (result != null && result.getIntValue("status") == 200) {//delete succeed
				id = 0;//reuse default value
			}
			break;
		case TYPE_ARRAY:
			logList(response.getList(User.class.getSimpleName(), User.class));
			break; 
		case TYPE_COMPLEX:
			JSONArray array = response.getArray(null);//, "Comment[]");//
			if (array == null || array.isEmpty()) {
				Log.e(TAG, "onHttpResponse  type == TYPE_COMPLEX >> array == null || array.isEmpty() >> return;");
			} else {
				response = new JSONResponse(array.getJSONObject(0));

				User user = response.getObject(User.class);
				Log.d(TAG, "onHttpResponse  type == TYPE_COMPLEX >>  user = " + JSON.toJSONString(user));
				Work work = response.getObject(Work.class);
				Log.d(TAG, "onHttpResponse  type == TYPE_COMPLEX >>  work = " + JSON.toJSONString(work));
				logList(response.getList("Comment[]", Comment.class));
			}
			break; 
		case TYPE_ACCESS_PERMITTED:
			response = new JSONResponse(resultJson);
			Wallet wallet = response.getObject(Wallet.class);
			Log.d(TAG, "onHttpResponse  type == TYPE_ACCESS_PERMITTED >>  wallet = " + JSON.toJSONString(wallet));
		default:
			break;
		}

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isAlive) {
					pbQuery.setVisibility(View.GONE);
					Toast.makeText(context, R.string.received_result, Toast.LENGTH_SHORT).show();

					tvQueryResult.setText(e == null || JSON.isJsonCorrect(resultJson)
							? JSON.format(resultJson) : e.getMessage() + "\n\n\n" + error);
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



	@Override
	public void finish() {
		setResult(RESULT_OK, new Intent().putExtra(RESULT_URL, url).putExtra(RESULT_ID, id));
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAlive = false;
	}

}
