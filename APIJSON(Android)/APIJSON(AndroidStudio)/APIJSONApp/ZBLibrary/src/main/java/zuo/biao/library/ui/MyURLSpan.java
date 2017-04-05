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

package zuo.biao.library.ui;

import zuo.biao.library.util.CommonUtil;
import android.app.Activity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

/**自定义带链接文本
 * @author Lemon
 * @use MyURLSpan.getText(...);
 */
public class MyURLSpan extends ClickableSpan {
	private static final String TAG = "MyURLSpan";

	private Activity context;
	private String url;
	private String title;
	public MyURLSpan(Activity context, String url, String title) {
		this.context = context;
		this.url = url;
		this.title = title;
	}

	/**
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static SpannableStringBuilder getText(Activity context, Spannable spannable) {
		return getText(context, spannable, null);
	}
	/**
	 * @param context
	 * @param html
	 * @param title
	 * @return
	 */
	public static SpannableStringBuilder getText(Activity context, Html html, String title) {
		return getText(context, (Spannable) html, title);		
	}
	/**
	 * @param context
	 * @param spannable
	 * @param title
	 * @return
	 */
	public static SpannableStringBuilder getText(Activity context, Spannable spannable, String title) {
		if (spannable == null || spannable.length() <= 0) {
			Log.e(TAG, "getText  spannable == null || spannable.length() <= 0 >> return new SpannableStringBuilder();");
			return new SpannableStringBuilder();
		}

		SpannableStringBuilder builder = new SpannableStringBuilder(spannable);
		builder.clearSpans();// should clear old spans
		URLSpan[] urls = spannable.getSpans(0, spannable.length(), URLSpan.class);
		if (urls != null) {
			for (URLSpan urlSpan : urls) {
				if (urlSpan != null) {
					MyURLSpan myURLSpan = new MyURLSpan(context, urlSpan.getURL(), title);
					builder.setSpan(myURLSpan, spannable.getSpanStart(urlSpan), spannable
							.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}
			}
		}

		return builder;
	}


	@Override
	public void onClick(View widget) {
		CommonUtil.toActivity(context, WebViewActivity.createIntent(context, title, url));
	}

}
