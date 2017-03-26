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
			void onCommentClick(CommentItem data);
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

			ivHead = findViewById(R.id.ivCommentHead);
			llDownCommentContainer = findViewById(R.id.llCommentDownCommentContainer);
			tvWatchMore = findViewById(R.id.tvCommentWatchMore);
			rlCommentMainItem = findViewById(R.id.rlCommentMainItem);

			tvName = (TextView) findViewById(R.id.tvCommentName);
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
					onCommentClickListener.onCommentClick(data);
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

					tvWatchMore.setText("查看更多" + (downList.size() - 4) + "条回复");// String.valueOf(downList.size() - 4) + "条回复");
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
					View commentItem = inflater.inflate(R.layout.comment_down_item, null);
					TextView tvContent = (TextView) commentItem.findViewById(R.id.tvCommentContent);

					final CommentItem data = downList.get(i);
					String name = StringUtil.getTrimedString(data.getUser().getName());
					String content = StringUtil.getTrimedString(data.getComment().getContent());
					tvContent.setText(Html.fromHtml("<font color=\"#25a281\">" + name + " :</font> "
							+ " 回复 "
							+ "<font color=\"#25a281\">" + data.getToUser().getName() + " :</font> "+ content 
							+ " <font color=\"#b5b5b5\">" + TimeUtil.getSmartDate(data.getDate()) + "</font>"));

					commentItem.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (onCommentClickListener != null) {
								onCommentClickListener.onCommentClick(data);
							}
						}
					});

					llDownCommentContainer.addView(commentItem);
				}
			}
		}
		
	}


}
