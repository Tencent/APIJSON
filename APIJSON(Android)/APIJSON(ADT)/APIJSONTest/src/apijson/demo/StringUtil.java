package apijson.demo;

import android.widget.TextView;

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
	
}
