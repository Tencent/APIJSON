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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import apijson.demo.R;

/**activity for selecting a request
 * @author Lemon
 */
public class SelectActivity extends Activity {

	public static final String RESULT_JSON = "RESULT_JSON";

	public static Intent createIntent(Context context) {
		return new Intent(context, SelectActivity.class);
	}


	private Activity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_activity);

		context = this;
	}

	//click event,called form layout android:onClick <<<<<<<<<<<<<<<<
	public void selectPost(View v) {
		select(QueryActivity.TYPE_POST);
	}
	
	public void selectDelete(View v) {
		select(QueryActivity.TYPE_DELETE);
	}

	public void selectPut(View v) {
		select(QueryActivity.TYPE_PUT);
	}
	
	//get <<<<<<<<<<<<<<<<<<<<<<<<<<<
	public void selectSingle(View v) {
		select(QueryActivity.TYPE_SINGLE);
	}
	
	public void selectColumns(View v) {
		select(QueryActivity.TYPE_COLUMNS);
	}

	public void selectRely(View v) {
		select(QueryActivity.TYPE_RELY);
	}

	public void selectArray(View v) {
		select(QueryActivity.TYPE_ARRAY);
	}
	
	public void selectComplex(View v) {
		select(QueryActivity.TYPE_COMPLEX);
	}

	public void selectAccessError(View v) {
		select(QueryActivity.TYPE_ACCESS_ERROR);
	}

	public void selectAccessPermitted(View v) {
		select(QueryActivity.TYPE_ACCESS_PERMITTED);
	}
	//get >>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	//click event,called form layout android:onClick >>>>>>>>>>>>>>>>

	private String url;
	private void select(int type) {
		startActivityForResult(QueryActivity.createIntent(context, type, url), REQUEST_TO_QUERY);
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
				url = data.getStringExtra(QueryActivity.RESULT_URL);
			}
			break;
		default:
			break;
		}
	}


}
