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

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import apijson.demo.R;
import apijson.demo.RequestUtil;

import com.alibaba.fastjson.JSONObject;

/**activity for selecting a request
 * @author Lemon
 */
public class SelectActivity extends Activity implements OnClickListener {


	private Activity context;

	private Button[] buttons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_activity);
		context = this;
		

		buttons = new Button[10];
		buttons[0] = (Button) findViewById(R.id.btnSelectPost);
		buttons[1] = (Button) findViewById(R.id.btnSelectPut);
		buttons[2] = (Button) findViewById(R.id.btnSelectDelete);
		buttons[3] = (Button) findViewById(R.id.btnSelectSingle);
		buttons[4] = (Button) findViewById(R.id.btnSelectColumns);
		buttons[5] = (Button) findViewById(R.id.btnSelectRely);
		buttons[6] = (Button) findViewById(R.id.btnSelectArray);
		buttons[7] = (Button) findViewById(R.id.btnSelectComplex);
		buttons[8] = (Button) findViewById(R.id.btnSelectAccessError);
		buttons[9] = (Button) findViewById(R.id.btnSelectAccessPermitted);


		
		setRequest();


		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnClickListener(this);
		}

		findViewById(R.id.btnSelectUpdateLog).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
						StringUtil.getCorrectUrl("github.com/TommyLemon/APIJSON/commits/master"))));
			}
		});

	}


	
	/**
	 */
	public void setRequest() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(JSON.format(getRequest(buttons[i], false)));
		}
	}

	/**
	 * @param v
	 * @return
	 */
	public JSONObject getRequest(View v, boolean encode) {
		switch (v.getId()) {
		case R.id.btnSelectPost:
			return RequestUtil.newPostRequest(encode);
		case R.id.btnSelectPut:
			return RequestUtil.newPutRequest(id, encode);
		case R.id.btnSelectDelete:
			return RequestUtil.newDeleteRequest(id, encode);

		case R.id.btnSelectSingle:
			return RequestUtil.newSingleRequest(id, encode);
		case R.id.btnSelectColumns:
			return RequestUtil.newColumnsRequest(id, encode);
		case R.id.btnSelectRely:
			return RequestUtil.newRelyRequest(id, encode);
		case R.id.btnSelectArray:
			return RequestUtil.newArrayRequest(encode);

		case R.id.btnSelectAccessError:
			return RequestUtil.newAccessErrorRequest(encode);
		case R.id.btnSelectAccessPermitted:
			return RequestUtil.newAccessPermittedRequest(encode);
		default:
			return RequestUtil.newComplexRequest(encode);
		}
	}
	
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSelectPost:
			select(getRequest(v, true), "post");
			break;
		case R.id.btnSelectPut:
			select(getRequest(v, true), "put");
			break;
		case R.id.btnSelectDelete:
			select(getRequest(v, true), "delete");
			break;

		case R.id.btnSelectAccessError:
			select(getRequest(v, true), "post_get");
			break;
		case R.id.btnSelectAccessPermitted:
			select(getRequest(v, true), "post_get");
			break;

		default:
			select(getRequest(v, true), "get");
			break;
		}
	}


	private long id;
	private String url;
	private void select(JSONObject request, String method) {
		startActivityForResult(QueryActivity.createIntent(context, id, url, method, request), REQUEST_TO_QUERY);
	}



	private static final int REQUEST_TO_QUERY = 1;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_QUERY:
			if (data == null) {
				Toast.makeText(context, "onActivityResult  data == null !!!", Toast.LENGTH_SHORT).show();
			} else {
				id = data.getLongExtra(QueryActivity.RESULT_ID, id);
				url = data.getStringExtra(QueryActivity.RESULT_URL);
				setRequest();
			}
			break;
		default:
			break;
		}
	}



}
