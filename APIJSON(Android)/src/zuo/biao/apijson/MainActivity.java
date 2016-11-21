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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import zuo.biao.apijson.HttpManager.OnHttpResponseListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

/**the launcher activity, for input request json
 * @author Lemon
 */
public class MainActivity extends Activity {

	private Activity context;
	private EditText etMainRequest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		context = this;

		etMainRequest = (EditText) findViewById(R.id.etMainRequest);
		
		
//		JSONObject userObject = new JSONObject(true);
//		userObject.put("sex", 0);
//
//		JSONObject workObject = new JSONObject(true);
//		//		workObject.put("userId", "/User/id");
//		try {
//			workObject.put("userId", URLEncoder.encode("/User/id", HttpManager.UTF_8));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		JSONObject commentObject = new JSONObject(true);
//		//		commentObject.put("workId", "[]/Work/id");
//		try {
//			commentObject.put("workId", URLEncoder.encode("[]/Work/id", HttpManager.UTF_8));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		JSONObject commentArrayObject = new JSONObject(true);
//		commentArrayObject.put("page", 0);
//		commentArrayObject.put("count", 3);
//		commentArrayObject.put("Comment", commentObject);
//
//		JSONObject arrayObject = new JSONObject(true);
//		arrayObject.put("page", 1);
//		arrayObject.put("count", 10);
//		arrayObject.put("User", userObject);
//		arrayObject.put("Work", workObject);
//		try {
//			arrayObject.put(URLEncoder.encode("Comment[]", HttpManager.UTF_8), commentArrayObject);
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//
//		JSONObject object = new JSONObject(true);
//		try {
//			object.put(URLEncoder.encode("[]", HttpManager.UTF_8), arrayObject);
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		
//		System.out.println("object = " + JSON.toJSONString(object));
//		
//		String request = null;
//		try {
//			request = URLEncoder.encode(JSON.toJSONString(object), HttpManager.UTF_8);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		System.out.println("request = " + request);

	}



	//click event,called form layout android:onClick <<<<<<<<<<<<<<<<
	public void select(View v) {
		startActivityForResult(SelectActivity.createIntent(context), REQUEST_TO_SELECT);
	}

	public void queryFromLocal(View v) {
		toQuery(QueryActivity.WAY_LOCAL);
	}

	public void queryFromServer(View v) {
		toQuery(QueryActivity.WAY_SERVER);
	}
	//click event,called form layout android:onClick >>>>>>>>>>>>>>>>

	
	private void toQuery(int way) {
		startActivity(QueryActivity.createIntent(context, way, StringUtil.getTrimedString(etMainRequest)));
	}



	private static final int REQUEST_TO_SELECT = 1;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_SELECT:
			if (data == null) {
				Toast.makeText(context, "onActivityResult  data == null !!!", Toast.LENGTH_SHORT).show();
				return;
			}
			etMainRequest.setText(StringUtil.getTrimedString(data.getStringExtra(SelectActivity.RESULT_JSON)));
			break;
		default:
			break;
		}
	}


}
