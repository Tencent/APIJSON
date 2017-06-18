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

package zuo.biao.library.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**屏幕相关类
 * @author Lemon
 * @use ScreenUtil.xxxMethod(...);
 */
public class ScreenUtil {
	//	private static final String TAG = "SceenUtil";

	private ScreenUtil() {/* 不能实例化**/}


	public static int[] screenSize;
	public static int[] getScreenSize(Context context){
		if (screenSize == null || screenSize[0] <= 480 || screenSize[1] <= 800) {//小于该分辨率会显示不全
			screenSize = new int[2];

			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			// float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
			// int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
			// float xdpi = dm.xdpi;
			// float ydpi = dm.ydpi;
			// Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
			// Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI="
			// + densityDPI);

			screenSize[0] = dm.widthPixels;// 屏幕宽（像素，如：480px）
			screenSize[1] = dm.heightPixels;// 屏幕高（像素，如：800px）
		}

		return screenSize;
	}
	
	public static int getScreenWidth(Context context){
		return getScreenSize(context)[0];
	}
	public static int getScreenHeight(Context context){
		return getScreenSize(context)[1];
	}


}
