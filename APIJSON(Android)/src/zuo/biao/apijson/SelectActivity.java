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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**activity for select a request json
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
	public void selectSingle(View v) {
		setResult(v);
	}
	
	public void selectRely(View v) {
		setResult(v);
	}
	
	public void selectArray(View v) {
		setResult(v);
	}
	
	public void selectComplex(View v) {
		setResult(v);
	}
	//click event,called form layout android:onClick >>>>>>>>>>>>>>>>
	
	
	private void setResult(View v) {
		setResult(RESULT_OK, new Intent().putExtra(RESULT_JSON, StringUtil.getTrimedString((TextView) v)));
		finish();
	}
	
}
