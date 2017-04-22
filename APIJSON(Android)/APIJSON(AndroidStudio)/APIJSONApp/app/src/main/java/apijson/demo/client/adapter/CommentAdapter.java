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

import android.app.Activity;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apijson.demo.client.model.CommentItem;
import apijson.demo.client.view.CommentView;
import apijson.demo.client.view.CommentView.OnCommentClickListener;
import apijson.demo.client.view.CommentView.OnShowAllListener;
import zuo.biao.library.base.BaseViewAdapter;

/**评论列表
 * @author Lemon
 */
public class CommentAdapter extends BaseViewAdapter<CommentItem, CommentView> {


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
	public CommentView createView(int position, ViewGroup parent) {
		return new CommentView(context, resources)
				.setOnCommentClickListener(onCommentClickListener)
				.setOnShowAllListener(new OnShowAllListener() {
					@Override
					public void onShowAll(int position, CommentView bv, boolean show) {
						showAllMap.put(position, show);
						bindView(position, bv);
					}
				});
	}

	private Map<Integer, Boolean> showAllMap = new HashMap<>();

	@Override
	public void bindView(int position, CommentView bv) {
		//true : showAllMap.get(position)怎么搞都崩溃
		bv.setShowAll(showAll ? Boolean.valueOf(true) : showAllMap.get(position));
		super.bindView(position, bv);
	}

}