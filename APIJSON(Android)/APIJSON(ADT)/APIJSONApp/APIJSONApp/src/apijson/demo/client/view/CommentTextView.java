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

import zuo.biao.library.util.StringUtil;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import apijson.demo.client.model.Comment;
import apijson.demo.client.model.CommentItem;
import apijson.demo.client.model.User;

/**评论TextView
 */
public class CommentTextView extends TextView {


	public CommentTextView(Context context) {
		super(context);
	}

	public CommentTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommentTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private OnClickListener listener;
	public void setOnNameClickListener(OnClickListener l) {
		this.listener = l;
	}

	private Comment comment;
	private User user;
	private User toUser;
	/**设置View
	 * @param comment
	 */
	public void setView(CommentItem item, boolean showCommenter) {
		if (item == null) {
			item = new CommentItem();
		}
		this.comment = item.getComment();
		this.user = item.getUser();
		this.toUser = item.getToUser();
		String content = StringUtil.getTrimedString(comment.getContent());

		String userName = StringUtil.getTrimedString(user.getName());
		int userNameLength = showCommenter ? userName.length() : 0;

		String targetUserName = StringUtil.getTrimedString(toUser.getName());

		SpannableString msp = null;
		if (toUser.getId() <= 0) {
			msp = new SpannableString(( showCommenter ? userName + ": " : "" ) + content);
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setColor(Color.parseColor("#009ed3")); // 设置超链接颜色
					ds.setUnderlineText(false); // 超链接去掉下划线
				}
				@Override
				public void onClick(View widget) {
					if (listener != null) {
						listener.onClick(widget);
					}
				}
			}, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			msp = new SpannableString((showCommenter ? userName : "") + "回复 " + targetUserName
					+ ": " + content);
			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setColor(Color.parseColor("#009ed3")); // 设置超链接颜色
					ds.setUnderlineText(false); // 超链接去掉下划线
				}

				@Override
				public void onClick(View widget) {
					if (listener != null) {
						listener.onClick(widget);
					}
				}
			}, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			msp.setSpan(new ClickableSpan() {
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setColor(Color.parseColor("#009ed3")); // 设置超链接颜色
					ds.setUnderlineText(false); // 超链接去掉下划线
				}

				@Override
				public void onClick(View widget) {
					if (listener != null) {
						listener.onClick(widget);
					}
				}
			}, userNameLength + 3, userNameLength + 3 + targetUserName.length(),
			Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		setText(msp);
		// setMovementMethod(LinkMovementMethod.getInstance());
	}
}
