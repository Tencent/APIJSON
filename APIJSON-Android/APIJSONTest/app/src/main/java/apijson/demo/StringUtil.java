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


package apijson.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**String工具类
 * @author Lemon
 */
public class StringUtil extends zuo.biao.apijson.StringUtil {

	public static String getString(TextView tv) {
		if (tv == null || tv.getText() == null) {
			return "";
		}
		return getString(tv.getText().toString());
	}
	public static String getTrimedString(TextView tv) {
		return getTrimedString(getString(tv));
	}
	public static String getNoBlankString(TextView tv) {
		return getNoBlankString(getString(tv));
	}

	public static boolean isNotEmpty(TextView tv, boolean trim) {
		return isNotEmpty(getString(tv), trim);
	}

	/**
	 * @param value
	 */
	public static void copyText(Context context, String value) {
		if (context == null || StringUtil.isNotEmpty(value, true) == false) {
			Log.e("StringUtil", "copyText  context == null || StringUtil.isNotEmpty(value, true) == false >> return;");
			return;
		}
		ClipData cD = ClipData.newPlainText("simple text", value);
		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setPrimaryClip(cD);
		Toast.makeText(context, "已复制\n" + value, Toast.LENGTH_SHORT).show();
	}

}
