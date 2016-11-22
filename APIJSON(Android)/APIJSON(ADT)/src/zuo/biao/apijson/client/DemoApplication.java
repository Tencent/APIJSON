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

import android.app.Application;
import android.util.Log;

/**基础Application
 * @author Lemon
 * @see #init
 */
public class DemoApplication extends Application {
	private static final String TAG = "DemoApplication";

	
	private static Application instance;
	public static Application getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "项目启动 >>>>>>>>>>>>>>>>>>>> \n\n");
		
		init(this);
	}

	/**初始化方法
	 * @param application
	 * @must 调用init方法且只能调用一次，如果extends BaseApplication会自动调用
	 */
	public static void init(Application application) {
		instance = application;
		if (instance == null) {
			Log.e(TAG, "\n\n\n\n\n !!!!!! 调用BaseApplication中的init方法，instance不能为null !!!" +
					"\n <<<<<< init  instance == null ！！！ >>>>>>>> \n\n\n\n");
		}
		
	}

	/**获取应用名
	 * @return
	 */
	public String getAppName() {
		return getResources().getString(R.string.app_name);
	}
	/**获取应用版本名(显示给用户看的)
	 * @return
	 */
	public String getAppVersion() {
		return getResources().getString(R.string.app_version);
	}


}
