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
