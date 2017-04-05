package apijson.demo.client.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import apijson.demo.client.R;
import apijson.demo.client.activity_fragment.UserActivity;
import apijson.demo.client.model.CommentItem;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.ImageLoaderUtil;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;

/**
 * Created by Tommy on 17/4/5.
 */
public class CommentItemView extends BaseView<CommentItem> implements View.OnClickListener {

    /**点击评论监听回调
     */
    public interface OnCommentClickListener {
        void onCommentClick(CommentItem item, int position, int index, boolean isLong);
    }

    /**显示更多监听回调
     * @author Lemon
     */
    public interface OnShowAllListener {
        public void onShowAll(int position, CommentItemView bv, boolean show);
    }

    private OnCommentClickListener onCommentClickListener;
    public CommentItemView setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
        return this;
    }

    private OnShowAllListener onShowAllListener;
    public CommentItemView setOnShowAllListener(OnShowAllListener onShowAllListener) {
        this.onShowAllListener = onShowAllListener;
        return this;
    }



    public CommentItemView(Activity context, Resources resources) {
        super(context, resources);
    }

    private LayoutInflater inflater;

    public ImageView ivCommentHead;
    public TextView tvCommentName;
    public TextView tvCommentContent;
    public TextView tvCommentTime;


    public LinearLayout llCommentContainer;

    @SuppressLint("InflateParams")
    @Override
    public View createView(LayoutInflater inflater) {
        this.inflater = inflater;
        convertView = inflater.inflate(R.layout.comment_main_item, null);

        ivCommentHead = findViewById(R.id.ivCommentHead, this);

        tvCommentName = findViewById(R.id.tvCommentName, this);
        tvCommentContent = findViewById(R.id.tvCommentContent);
        tvCommentTime = findViewById(R.id.tvCommentTime);

        llCommentContainer = findViewById(R.id.llCommentContainer);

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
            case R.id.tvCommentContainerViewMore:
                if (onShowAllListener != null) {
                    onShowAllListener.onShowAll(position, this, true);
                }
                break;
            default:
                break;
        }
    }

    public CommentContainerView commentContainerView;
    /**显示子评论
     */
    public void setChildComment() {
        if (commentContainerView == null) {
            commentContainerView = new CommentContainerView(context, resources);
            llCommentContainer.removeAllViews();
            llCommentContainer.addView(commentContainerView.createView(inflater));

            commentContainerView.setOnCommentClickListener(onCommentClickListener);
            commentContainerView.tvCommentContainerViewMore.setOnClickListener(this);
        }

        commentContainerView.setMaxShowCount(showAll ? 0 : 3);
        commentContainerView.bindView(data.getChildList());
    }

}

