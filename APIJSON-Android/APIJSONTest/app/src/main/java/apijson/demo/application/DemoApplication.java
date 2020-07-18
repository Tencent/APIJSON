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

package apijson.demo.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import apijson.demo.R;

/**Application
 * @author Lemon
 */
public class DemoApplication extends Application {
	private static final String TAG = "DemoApplication";


	private static DemoApplication instance;
	public static DemoApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Log.d(TAG, "项目启动 >>>>>>>>>>>>>>>>>>>> \n\n");

		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {


			@Override
			public void onActivityStarted(Activity activity) {
				Log.v(TAG, "onActivityStarted  activity = " + activity.getClass().getName());
			}

			@Override
			public void onActivityStopped(Activity activity) {
				Log.v(TAG, "onActivityStopped  activity = " + activity.getClass().getName());
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				Log.v(TAG, "onActivitySaveInstanceState  activity = " + activity.getClass().getName());
			}

			@Override
			public void onActivityResumed(Activity activity) {
				Log.v(TAG, "onActivityResumed  activity = " + activity.getClass().getName());
				setCurrentActivity(activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {
				Log.v(TAG, "onActivityPaused  activity = " + activity.getClass().getName());
				setCurrentActivity(activityList.isEmpty() ? null : activityList.get(activityList.size() - 1));
			}

			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				Log.v(TAG, "onActivityCreated  activity = " + activity.getClass().getName());
				activityList.add(activity);
				//TODO 按键、键盘监听拦截和转发
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.v(TAG, "onActivityDestroyed  activity = " + activity.getClass().getName());
				activityList.remove(activity);
			}

		});
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

	private List<Activity> activityList = new LinkedList<>();

	private WeakReference<Activity> sCurrentActivityWeakRef;
	public Activity getCurrentActivity() {
		Activity currentActivity = null;
		if (sCurrentActivityWeakRef != null) {
			currentActivity = sCurrentActivityWeakRef.get();
		}
		return currentActivity;
	}

	public void setCurrentActivity(Activity activity) {
		if (sCurrentActivityWeakRef == null || !activity.equals(sCurrentActivityWeakRef.get())) {
			sCurrentActivityWeakRef = new WeakReference<>(activity);
		}
	}
}
