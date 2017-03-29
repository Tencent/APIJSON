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

package apijson.demo.client.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.base.BaseView;
import zuo.biao.library.base.BaseViewAdapter;
import zuo.biao.library.util.ImageLoaderUtil;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import apijson.demo.client.R;
import apijson.demo.client.activity_fragment.UserActivity;
import apijson.demo.client.adapter.CommentAdapter.ItemView;
import apijson.demo.client.adapter.CommentAdapter.ItemView.OnCommentClickListener;
import apijson.demo.client.adapter.CommentAdapter.ItemView.OnShowAllListener;
import apijson.demo.client.model.CommentItem;

/**评论列表
 * @author Lemon
 */
public class CommentAdapter extends BaseViewAdapter<CommentItem, ItemView> {


	private OnCommentClickListener onCommentClickListener;
	public CommentAdapter(Activity context, OnCommentClickListener onCommentClickListener) {     
		super(context);
		this.onCommentClickListener = onCommentClickListener;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	private boolean showAll = false;
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	@Override
	public synchronized void refresh(List<CommentItem> list) {
		showAllMap.clear();
		super.refresh(list);
	}

	@Override
	public ItemView createView(int position, ViewGroup parent) {
		return new ItemView(context, resources)
		.setOnCommentClickListener(onCommentClickListener)
		.setOnShowAllListener(new OnShowAllListener() {
			@Override
			public void onShowAll(int position, ItemView bv, boolean show) {
				showAllMap.put(position, show);
				bindView(position, bv);
			}
		});
	}

	private Map<Integer, Boolean> showAllMap = new HashMap<>();
	@Override
	public void bindView(int position, ItemView bv) {
		//true : showAllMap.get(position)怎么搞都崩溃
		bv.setShowAll(showAll ? Boolean.valueOf(true) : showAllMap.get(position));
		super.bindView(position, bv);
	}


	public static class ItemView extends BaseView<CommentItem> implements OnClickListener {  

		/**点击评论监听回调
		 */
		public interface OnCommentClickListener {
			void onCommentClick(CommentItem item, int position, int index, boolean isLong);
		}

		/**显示更多监听回调
		 * @author Lemon
		 */
		public interface OnShowAllListener {
			public void onShowAll(int position, ItemView bv, boolean show);
		}
		
		private OnCommentClickListener onCommentClickListener;
		public ItemView setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
			this.onCommentClickListener = onCommentClickListener;
			return this;
		}
		
		private OnShowAllListener onShowAllListener;
		public ItemView setOnShowAllListener(OnShowAllListener onShowAllListener) {
			this.onShowAllListener = onShowAllListener;
			return this;
		}
		


		public ItemView(Activity context, Resources resources) {
			super(context, resources);
		}

		private LayoutInflater inflater;

		public ImageView ivCommentHead;
		public TextView tvCommentName;
		public TextView tvCommentContent;
		public TextView tvCommentTime;

		public View vCommentItemDivider;
		public LinearLayout llCommentContainer;
		public TextView tvCommentMore;

		@SuppressLint("InflateParams")
		@Override
		public View createView(LayoutInflater inflater) {
			this.inflater = inflater;
			convertView = inflater.inflate(R.layout.comment_main_item, null);

			ivCommentHead = findViewById(R.id.ivCommentHead, this);

			tvCommentName = (TextView) findViewById(R.id.tvCommentName, this);
			tvCommentContent = (TextView) findViewById(R.id.tvCommentContent);
			tvCommentTime = (TextView) findViewById(R.id.tvCommentTime);

			vCommentItemDivider = findViewById(R.id.vCommentItemDivider);
			llCommentContainer = findViewById(R.id.llCommentContainer);
			tvCommentMore = findViewById(R.id.tvCommentMore, this);

			return convertView;
		}


		private boolean showAll = false;
		public void setShowAll(Boolean showAll) {
			this.showAll = showAll == null ? false : showAll;
		}

		@Override
		public void bindView(CommentItem data){
			this.data = data;

			String name = StringUtil.getTrimedString(data.getUser().getName());
			String content = StringUtil.getTrimedString(data.getComment().getContent());

			tvCommentName.setText("" + name);
			tvCommentContent.setText("" + content);
			tvCommentTime.setText("" + TimeUtil.getSmartDate(data.getDate()));
			ImageLoaderUtil.loadImage(ivCommentHead, data.getUser().getHead(), ImageLoaderUtil.TYPE_OVAL);
			
			setChildComment();
		}


		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ivCommentHead:
			case R.id.tvCommentName:
				toActivity(UserActivity.createIntent(context, data.getUser().getId()));
				break;
			case R.id.tvCommentMore:
				if (onShowAllListener != null) {
					onShowAllListener.onShowAll(position, this, true);
				}
				break;
			default:
				break;
			}
		}

		/**显示子评论
		 * @param data
		 */
		@SuppressLint("InflateParams")
		public void setChildComment() {
			List<CommentItem> downList = data.getChildList();
			boolean isEmpty = downList == null || downList.isEmpty();

			llCommentContainer.removeAllViews();
			vCommentItemDivider.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
			tvCommentMore.setVisibility(View.GONE);

			if (isEmpty) {
				return;
			}

			if (showAll == false && downList.size() > 3) {
				tvCommentMore.setVisibility(View.VISIBLE);
				downList = downList.subList(0, 3);
			}

			for (int i = 0; i < downList.size(); i++) {
				final int index = i;

				TextView childComment = (TextView) inflater.inflate(R.layout.comment_down_item, null);

				final CommentItem data = downList.get(i);
				String name = StringUtil.getTrimedString(data.getUser().getName());
				String content = StringUtil.getTrimedString(data.getComment().getContent());
				childComment.setText(Html.fromHtml("<font color=\"#25a281\">" + StringUtil.getString(name) + "</font>"
						+ " 回复 " + "<font color=\"#25a281\">" + StringUtil.getString(data.getToUser().getName())
						+ "</font>" + " : " + content));

				childComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onCommentClick(data, position, index, false);
					}
				});
				childComment.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						onCommentClick(data, position, index, true);
						return true;
					}
				});

				llCommentContainer.addView(childComment);
			}

		}

		protected void onCommentClick(CommentItem item, int position, int index, boolean isLong) {
			if (onCommentClickListener != null) {
				onCommentClickListener.onCommentClick(item, position, index, isLong);
			}
		}
	}

}
