/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package zuo.biao.library.ui.xlistview;

import zuo.biao.library.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @modifier Lemon
 */
public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_COMPLETE = 3;//Lemon add: all data were loaded and no more data available

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mProgressBar.setVisibility(View.GONE);
		mHintView.setVisibility(View.VISIBLE);

		switch (state) {
		case STATE_READY:
			mHintView.setText(R.string.xlistview_footer_hint_ready);
			break;
		case STATE_LOADING:
			mProgressBar.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_loading);
			break;
		case STATE_COMPLETE:
			mHintView.setText(R.string.xlistview_footer_hint_complete);
			break;
		default:
			mHintView.setText(R.string.xlistview_footer_hint_normal);
			break;
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private boolean isShowing = false;
	public boolean isShowing() {
		return isShowing;
	}
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		if (needFooter) {
			isShowing = false;
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
			lp.height = 1;
			mContentView.setLayoutParams(lp);
		}
	}

	/**
	 * show footer
	 */
	public void show() {
		if (needFooter) {
			isShowing = true;
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
			lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
			mContentView.setLayoutParams(lp);
		}
	}

	@SuppressLint("InflateParams")
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
	}

	private boolean needFooter = true;
	public void setGone() { // 取消上拉模式初始化时调用
		if (needFooter) {
			hide();
			needFooter = false;
		}
	}
}
