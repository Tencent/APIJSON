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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.R;
import apijson.demo.RequestUtil;
import apijson.demo.StringUtil;
import zuo.biao.apijson.JSON;

/**选择Activity
 * 选择向服务器发起的请求
 * @author Lemon
 */
public class SelectActivity extends Activity implements OnClickListener {

	public static final String CONFIG_PATH = "CONFIG_PATH";

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_URL = "KEY_URL";
	public static final String KEY_METHOD = "KEY_METHOD";
	public static final String KEY_NAME = "KEY_NAME";


	private Activity context;

	private long id;
	private String url;
	private String method;
	private String name;

	private TextView tvSelectLogin;
	private Button[] buttons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_activity);
		context = this;


		//读取保存的配置
		SharedPreferences sp = getSharedPreferences(CONFIG_PATH, Context.MODE_PRIVATE);
		id = sp.getLong(KEY_ID, id);
		url = sp.getString(KEY_URL, null);
		method = sp.getString(KEY_METHOD, null);
		name = sp.getString(KEY_NAME, null);

		tvSelectLogin = findViewById(R.id.tvSelectLogin);

		buttons = new Button[10];
		buttons[0] = findViewById(R.id.btnSelectPost);
		buttons[1] = findViewById(R.id.btnSelectPut);
		buttons[2] = findViewById(R.id.btnSelectDelete);
		buttons[3] = findViewById(R.id.btnSelectSingle);
		buttons[4] = findViewById(R.id.btnSelectColumns);
		buttons[5] = findViewById(R.id.btnSelectRely);
		buttons[6] = findViewById(R.id.btnSelectArray);
		buttons[7] = findViewById(R.id.btnSelectComplex);
		buttons[8] = findViewById(R.id.btnSelectAccessError);
		buttons[9] = findViewById(R.id.btnSelectAccessPermitted);


		setRequest();



		tvSelectLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtil.isEmpty(name, true)) {
					startActivityForResult(RequestActivity.createIntent(context, id, url, "login", RequestUtil.newLoginRequest()), REQUEST_TO_REQUEST);
				} else {
					startActivityForResult(RequestActivity.createIntent(context, id, url, "logout", RequestUtil.newLogoutRequest()), REQUEST_TO_REQUEST);
				}
			}
		});


		for (int i = 0; i < buttons.length; i++) {
			final int which = i;
			buttons[which].setOnClickListener(this);
			buttons[which].setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					StringUtil.copyText(context, StringUtil.getString(buttons[which]));
					return true;
				}
			});
		}

	}



	public void toAuto(View v) {
		startActivityForResult(AutoActivity.createIntent(context), REQUEST_TO_AUTO);
	}

	public void toUnit(View v) {
		startActivity(UnitAutoActivity.createIntent(context));
	}

	public void toUI(View v) {
		startActivity(UIAutoActivity.createIntent(context));
	}

	public void toUpdateLog(View v) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
				StringUtil.getCorrectUrl("github.com/TommyLemon/APIJSON/commits/master"))));
	}


	/**
	 */
	public void setRequest() {
		tvSelectLogin.setText(StringUtil.isEmpty(name, true)
				? getString(R.string.login) : name + getString(R.string.logout));

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(JSON.format(getRequest(buttons[i])));
		}
	}



	/**
	 * @param v
	 * @return
	 */
	public JSONObject getRequest(View v) {
		switch (v.getId()) {
			case R.id.btnSelectPost:
				return RequestUtil.newPostRequest();
			case R.id.btnSelectPut:
				return RequestUtil.newPutRequest(id);
			case R.id.btnSelectDelete:
				return RequestUtil.newDeleteRequest(id);

			case R.id.btnSelectSingle:
				return RequestUtil.newSingleRequest(id);
			case R.id.btnSelectColumns:
				return RequestUtil.newColumnsRequest(id);
			case R.id.btnSelectRely:
				return RequestUtil.newRelyRequest(id);
			case R.id.btnSelectArray:
				return RequestUtil.newArrayRequest();

			case R.id.btnSelectAccessError:
				return RequestUtil.newAccessErrorRequest();
			case R.id.btnSelectAccessPermitted:
				return RequestUtil.newAccessPermittedRequest();
			default:
				return RequestUtil.newComplexRequest();
		}
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSelectPost:
				select(getRequest(v), "post");
				break;
			case R.id.btnSelectPut:
				select(getRequest(v), "put");
				break;
			case R.id.btnSelectDelete:
				select(getRequest(v), "delete");
				break;

			case R.id.btnSelectAccessError:
				select(getRequest(v), "gets");
				break;
			case R.id.btnSelectAccessPermitted:
				select(getRequest(v), "gets");
				break;

			default:
				select(getRequest(v), "get");
				break;
		}
	}


	private void select(JSONObject request, String method) {
		startActivityForResult(RequestActivity.createIntent(context, id, url, method, request), REQUEST_TO_REQUEST);
	}



	private static final int REQUEST_TO_REQUEST = 1;
	private static final int REQUEST_TO_AUTO = 2;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case REQUEST_TO_REQUEST:
			case REQUEST_TO_AUTO:
				if (data == null) {
					Toast.makeText(context, "onActivityResult  data == null !!!", Toast.LENGTH_SHORT).show();
				} else {
					url = data.getStringExtra(RequestActivity.RESULT_URL);
					method = data.getStringExtra(RequestActivity.RESULT_METHOD);

					//保存配置
					SharedPreferences.Editor e = getSharedPreferences(CONFIG_PATH, Context.MODE_PRIVATE).edit();

					e.remove(KEY_URL)
							.putString(KEY_URL, url)
							.remove(KEY_METHOD)
							.putString(KEY_METHOD, method);

					if (RequestUtil.isLogMethod(method)) {
						name = data.getStringExtra(RequestActivity.RESULT_NAME);
						e.remove(KEY_NAME).putString(KEY_NAME, name);
					} else {
						id = data.getLongExtra(RequestActivity.RESULT_ID, id);
						e.remove(KEY_ID).putLong(KEY_ID, id);
					}

					setRequest();

					e.commit();
				}
				break;
			default:
				break;
		}
	}


}
