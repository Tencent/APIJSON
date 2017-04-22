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

package apijson.demo.client.view;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import apijson.demo.client.R;
import apijson.demo.client.activity_fragment.UserActivity;
import apijson.demo.client.model.Comment;
import apijson.demo.client.model.CommentItem;
import apijson.demo.client.model.User;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;

/**评论TextView
 */
public class CommentTextView extends TextView {

	public interface OnNameClickListener {
		void onNameClick(int index, View widget, User user);
	}
	private OnNameClickListener listener;
	public void setOnNameClickListener(OnNameClickListener l) {
		this.listener = l;
	}


	public CommentTextView(Context context) {
		super(context);
	}

	public CommentTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommentTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}



	private Comment comment;
	private User user;
	private User toUser;
	/**设置View
	 * @param item
	 */
	public void setView(CommentItem item) {
		if (item == null) {
			item = new CommentItem();
		}
		this.comment = item.getComment();
		this.user = item.getUser();
		this.toUser = item.getToUser();
		String content = StringUtil.getTrimedString(comment.getContent());

		String userName = StringUtil.getTrimedString(user.getName());
		int userNameLength = userName.length();

		String toUserName = StringUtil.getTrimedString(toUser.getName());


		SpannableString msp = null;
		if (toUser.getId() <= 0) {
			msp = new SpannableString(userName + " : " + content);
		} else {
			msp = new SpannableString(userName + " 回复 " + toUserName + " : " + content);
			setSpan(msp, 1, userNameLength + 4, userNameLength + 4 + toUserName.length(), toUser);
		}
		setSpan(msp, 0, 0, userNameLength, user);

		setText(msp);
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void setSpan(SpannableString msp, final int index, int start, int end, final User user) {
		msp.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				onNameClick(index, widget, user);
			}
		}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}


	public void onNameClick(int index, View widget, User user) {
		if (listener != null) {
			listener.onNameClick(index, widget, user);
		} else {
			CommonUtil.toActivity((Activity) getContext(), UserActivity.createIntent(getContext(), user.getId()));
		}
	}

	public abstract class ClickableSpan extends android.text.style.ClickableSpan {
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.blue)); // 设置超链接颜色
			ds.setUnderlineText(false); // 超链接下划线
		}
	}
}