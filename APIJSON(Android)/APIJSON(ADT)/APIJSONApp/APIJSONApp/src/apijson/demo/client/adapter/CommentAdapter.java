package apijson.demo.client.adapter;

import java.util.List;

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

	@Override
	public ItemView createView(int position, ViewGroup parent) {
		return new ItemView(context, resources).setOnCommentClickListener(onCommentClickListener);
	}


	public static class ItemView extends BaseView<CommentItem> implements OnClickListener {  

		/**
		 */
		public interface OnCommentClickListener {
			void onCommentClick(CommentItem item, int position, int index, boolean isLong);
		}
		
		private OnCommentClickListener onCommentClickListener;
		public ItemView setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
			this.onCommentClickListener = onCommentClickListener;
			return this;
		}
		
		public ItemView(Activity context, Resources resources) {
			super(context, resources);
		}

		private LayoutInflater inflater;
		
		public View rlCommentMainItem;
		public TextView tvName;
		public TextView tvContent;
		public TextView tvTime;

		public ImageView ivHead;
		public TextView tvFloor;
		public LinearLayout llDownCommentContainer;
		public TextView tvWatchMore;
		
		@SuppressLint("InflateParams")
		@Override
		public View createView(LayoutInflater inflater) {
			this.inflater = inflater;
			convertView = inflater.inflate(R.layout.comment_main_item, null);

			ivHead = findViewById(R.id.ivCommentHead, this);
			llDownCommentContainer = findViewById(R.id.llCommentDownCommentContainer);
			tvWatchMore = findViewById(R.id.tvCommentWatchMore);
			rlCommentMainItem = findViewById(R.id.rlCommentMainItem);

			tvName = (TextView) findViewById(R.id.tvCommentName, this);
			tvContent = (TextView) findViewById(R.id.tvCommentContent);
			tvTime = (TextView) findViewById(R.id.tvCommentTime);

			return convertView;
		}

		@Override
		public void bindView(CommentItem data){
			this.data = data;

			String name = StringUtil.getTrimedString(data.getUser().getName());
			String content = StringUtil.getTrimedString(data.getComment().getContent());

			tvName.setText("" + name);
			tvContent.setText("" + content);
			tvTime.setText("" + TimeUtil.getSmartDate(data.getDate()));
			ImageLoaderUtil.loadImage(ivHead, data.getUser().getHead(), ImageLoaderUtil.TYPE_OVAL);
			setCommentDownComment(data, false);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tvCommentContent:
				if (onCommentClickListener != null) {
					onCommentClickListener.onCommentClick(data, position, -1, false);
				}
				break;
			case R.id.ivCommentHead:
				showShortToast("ivHead.ivHead  userId = " + data.getUser().getId());
				toActivity(UserActivity.createIntent(context, data.getUser().getId()));
				break;
			default:
				break;
			}
		}
		
		/**显示子评论
		 * @param data
		 */
		@SuppressLint("InflateParams")
		public void setCommentDownComment(final CommentItem parentItem, boolean showAll) {

			List<CommentItem> downList = parentItem.getChildList();
			if (downList == null || downList.isEmpty()) {
				llDownCommentContainer.removeAllViews();
				findViewById(R.id.vCommentItemDivider).setVisibility(View.GONE);
			} else {
				findViewById(R.id.vCommentItemDivider).setVisibility(View.VISIBLE);

				tvWatchMore.setVisibility(View.GONE);
				if (showAll == false && downList.size() > 4) {

					tvWatchMore.setText("查看更多");
					tvWatchMore.setVisibility(View.VISIBLE);
					tvWatchMore.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							setCommentDownComment(parentItem, true);
						}
					});

					downList = downList.subList(0, 4);
				}

				llDownCommentContainer.removeAllViews();
				for (int i = 0; i < downList.size(); i++) {
					final int index = i;
					
					View commentItem = inflater.inflate(R.layout.comment_down_item, null);
					TextView tvContent = (TextView) commentItem.findViewById(R.id.tvCommentContent);

					final CommentItem data = downList.get(i);
					String name = StringUtil.getTrimedString(data.getUser().getName());
					String content = StringUtil.getTrimedString(data.getComment().getContent());
					tvContent.setText(Html.fromHtml("<font color=\"#25a281\">" + StringUtil.getString(name) + "</font>"
							+ " 回复 " + "<font color=\"#25a281\">" + StringUtil.getString(data.getToUser().getName())
							+ "</font>" + " : " + content));

					commentItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							onCommentClick(data, position, index, false);
							
						}
					});
					commentItem.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							onCommentClick(data, position, index, true);
							return true;
						}
					});

					llDownCommentContainer.addView(commentItem);
				}
			}
		}

		protected void onCommentClick(CommentItem item, int position, int index, boolean isLong) {
			if (onCommentClickListener != null) {
				onCommentClickListener.onCommentClick(item, position, index, isLong);
			}
		}
	}

}
