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
import android.view.KeyEvent;

import apijson.demo.application.DemoApplication;

/**Activity 基类
 * @author Lemon
 */
public abstract class UIAutoBaseActivity extends Activity {
	private static final String TAG = "UIAutoBaseActivity";

	//只在 TouchLayout 中记录
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		DemoApplication.getInstance().onTouchEvent(event, this);
//		return super.onTouchEvent(event);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		DemoApplication.getInstance().onKeyDown(keyCode, event, this);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		DemoApplication.getInstance().onKeyUp(keyCode, event, this);
		return super.onKeyUp(keyCode, event);
	}

}
