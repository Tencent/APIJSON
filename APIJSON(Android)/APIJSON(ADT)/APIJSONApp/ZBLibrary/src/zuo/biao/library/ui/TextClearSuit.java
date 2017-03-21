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

import zuo.biao.library.util.StringUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**带清除按钮EditText或TextView套件，如果输入为空则隐藏清除按钮
 * @author Lemon
 * @use new TextClearSuit().addClearListener(...);
 */
public class TextClearSuit {
	private static final String TAG = "TextClearSuit";

	private TextView tv;
	private View clearView;
	
	private String inputedString;
	private int cursorPosition = 0;

	public TextView getTextView() {
		return tv;
	}
	public View getClearView() {
		return clearView;
	}
	public String getInputedString() {
		return inputedString;
	}
	public int getCursorPosition() {
		return cursorPosition;
	}


	public static final int BLANK_TYPE_DEFAULT = 0;
	public static final int BLANK_TYPE_TRIM = 1;
	public static final int BLANK_TYPE_NO_BLANK = 2;
	/**默认trim，隐藏方式为gone
	 * @param tv
	 * @param clearView
	 */
	public void addClearListener(final TextView tv, final View clearView) {
		addClearListener(tv, BLANK_TYPE_TRIM, clearView, false);
	}
	/**默认隐藏方式为gone
	 * @param tv
	 * @param trim
	 * @param clearView
	 */
	public void addClearListener(final TextView tv, final int blankType, final View clearView) {
		addClearListener(tv, blankType, clearView, false);
	}
	/**
	 * @param tv 输入框
	 * @param trim et内容前后是否不能含有空格
	 * @param clearView 清除输入框内容按钮
	 * @param isClearViewInvisible  如果et输入为空，隐藏clearView的方式为gone(false)还是invisible(true)
	 */
	public void addClearListener(final TextView tv, final int blankType, final View clearView, final boolean isClearViewInvisible) {
		if (tv == null || clearView == null) {
			Log.e(TAG, "addClearListener  (tv == null || clearView == null)  >> return;");
			return;
		}

		this.tv = tv;
		this.clearView = clearView;
		if (tv.getText() != null) {
			inputedString = tv.getText().toString();
		}

		clearView.setVisibility(StringUtil.isNotEmpty(tv, false) ? View.VISIBLE : View.GONE);
		clearView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.setText("");
				tv.requestFocus();
			}
		});
		tv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s == null || StringUtil.isNotEmpty(s.toString(), false) == false) {
					inputedString = "";
					if (isClearViewInvisible == false) {
						clearView.setVisibility(View.GONE);
					} else {
						clearView.setVisibility(View.INVISIBLE);
					}
				} else {
					inputedString = "" + s.toString();
//					if (inputedString.contains(" ")) {//stackoverflow
//						if (blankType == BLANK_TYPE_TRIM) {
//							inputedString = inputedString.trim();
//						} else if (blankType == BLANK_TYPE_NO_BLANK) {
//							inputedString = inputedString.replaceAll(" ", "");
//						}
//						tv.setText(inputedString);
//						tv.setSelection(inputedString.length());
//					}
					clearView.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	public interface onTextChangedListener {
		void onTextChanged(CharSequence s, int start, int before, int count);
	}

}
