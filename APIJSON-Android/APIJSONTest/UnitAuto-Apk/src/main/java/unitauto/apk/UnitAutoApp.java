/*Copyright ©2020 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package unitauto.apk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**Application
 * @author Lemon
 */
public class UnitAutoApp extends Application {
	private static final String TAG = "UnitAutoApp";

	private static UnitAutoApp INSTANCE;
	public static UnitAutoApp getInstance() {
		return INSTANCE;
	}

	private static Application APP;
	public static Application getApp() {
		return APP;
	}
	public static void init(Application app) {
		APP = app;

		app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

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
				setCurrentActivity(ACTIVITY_LIST.isEmpty() ? null : ACTIVITY_LIST.get(ACTIVITY_LIST.size() - 1));
			}

			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				Log.v(TAG, "onActivityCreated  activity = " + activity.getClass().getName());
				ACTIVITY_LIST.add(activity);
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.v(TAG, "onActivityDestroyed  activity = " + activity.getClass().getName());
				ACTIVITY_LIST.remove(activity);
			}

		});

	}


	private static List<Activity> ACTIVITY_LIST = new LinkedList<>();

	private static WeakReference<Activity> CURRENT_ACTIVITY_REF;
	public static Activity getCurrentActivity() {
		return CURRENT_ACTIVITY_REF == null ? null : CURRENT_ACTIVITY_REF.get();
	}
	public static void setCurrentActivity(Activity activity) {
		if (CURRENT_ACTIVITY_REF == null || ! activity.equals(CURRENT_ACTIVITY_REF.get())) {
			CURRENT_ACTIVITY_REF = new WeakReference<>(activity);
		}
	}



	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;
		init(this);
	}



}
