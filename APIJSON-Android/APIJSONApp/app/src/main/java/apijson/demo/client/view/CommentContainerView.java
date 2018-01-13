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

import java.util.List;

import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import apijson.demo.client.R;
import apijson.demo.client.model.CommentItem;
import apijson.demo.client.view.CommentView.OnCommentClickListener;

/**评论容器
 * @author Lemon
 * @use
 * <br> CommentContainerView commentContainerView = new CommentContainerView(context, resources);
 * <br> adapter中使用:[具体参考.DemoAdapter2(getView使用自定义View的写法)]
 * <br> convertView = commentContainerView.createView(inflater, position, viewType);
 * <br> commentContainerView.bindView(data, position, viewType);
 * <br> 或  其它类中使用:
 * <br> containerView.addView(commentContainerView.createView(inflater));
 * <br> commentContainerView.bindView(data);
 * <br> 然后
 * <br> commentContainerView.setOnCommentClickListener(onCommentClickListener);//非必需
 * <br> commentContainerView.setOnDataChangedListener(onDataChangedListener);data = commentContainerView.getData();//非必需
 * <br> commentContainerView.setOnClickListener(onClickListener);//非必需
 * <br> ...
 */
public class CommentContainerView extends BaseView<List<CommentItem>> {
	private static final String TAG = "CommentContainerView";

	private OnCommentClickListener onCommentClickListener;
	/**设置点击评论监听
	 * @param onCommentClickListener
	 */
	public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
		this.onCommentClickListener = onCommentClickListener;
	}


	public CommentContainerView(Activity context, Resources resources) {
		super(context, resources);
	}



	private LayoutInflater inflater;

	public ViewGroup llCommentContainerViewContainer;
	public View tvCommentContainerViewMore;

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		this.inflater = inflater;
		convertView = inflater.inflate(R.layout.comment_container_view, null);

		llCommentContainerViewContainer = findViewById(R.id.llCommentContainerViewContainer);

		tvCommentContainerViewMore = findViewById(R.id.tvCommentContainerViewMore);

		return convertView;
	}


	@Override
	public void bindView(List<CommentItem> list){
		this.data = list;

		int count = list == null ? 0 : list.size();
		boolean showMore = maxShowCount > 0 && count > maxShowCount;

		tvCommentContainerViewMore.setVisibility(showMore ? View.VISIBLE : View.GONE);
		llCommentContainerViewContainer.setVisibility(count <= 0 ? View.GONE : View.VISIBLE);
		llCommentContainerViewContainer.removeAllViews();//TODO 貌似比 只在count > 0时执行 性能更好，有待具体测试

		if (count > 0) {
			if (showMore) {
				list = list.subList(0, maxShowCount);
			}
			for (int i = 0; i < list.size(); i++) {
				addCommentView(i, list.get(i));
			}
		}
		
	}


	private int maxShowCount = 3;
	/**设置最多显示数量，超过则折叠
	 * @param maxShowCount <= 0 ? 显示全部 : 超过则折叠
	 * @must 在bindView前调用
	 */
	public void setMaxShowCount(int maxShowCount) {
		this.maxShowCount = maxShowCount;
	}



	/**添加评论
	 * @param index
	 * @param comment
	 */
	@SuppressLint("InflateParams")
	private void addCommentView(final int index, final CommentItem comment) {
		if (comment == null) {
			Log.e(TAG, "addCommentView comment == null >> return; ");
			return;
		}
		String content = StringUtil.getTrimedString(comment.getComment().getContent());
		if (StringUtil.isNotEmpty(content, true) == false) {
			Log.e(TAG, "addCommentView StringUtil.isNotEmpty(content, true) == false >> return; ");
			return;
		}

		CommentTextView commentView = (CommentTextView) inflater.inflate(R.layout.comment_item, null);
		commentView.setView(comment);

		if (onCommentClickListener != null) {
			commentView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onCommentClickListener.onCommentClick(comment, position, index, false);
				}
			});
			commentView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					onCommentClickListener.onCommentClick(comment, position, index, true);
					return true;
				}
			});
		}

		llCommentContainerViewContainer.addView(commentView);
	}

}