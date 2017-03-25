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

package apijson.demo.client.activity_fragment;

import java.util.List;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.CacheManager;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.CommonUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import apijson.demo.client.R;
import apijson.demo.client.adapter.CommentAdapter;
import apijson.demo.client.adapter.CommentAdapter.ItemView.OnCommentClickListener;
import apijson.demo.client.model.CommentItem;
import apijson.demo.client.model.MomentItem;
import apijson.demo.client.util.HttpRequest;
import apijson.demo.client.view.MomentView;

/**用户列表界面fragment
 * @author Lemon
 * @use new CommentListFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 * @must 查看 .HttpManager 中的@must和@warn
 *       查看 .SettingUtil 中的@must和@warn
 */
public class MomentActivity extends BaseHttpListActivity<CommentItem, CommentAdapter>
implements CacheCallBack<CommentItem>, OnHttpResponseListener, OnCommentClickListener, OnBottomDragListener {
	private static final String TAG = "MomentActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_MOMENT_ID = "INTENT_MOMENT_ID";
	public static final String INTENT_COMMENT_ID = "INTENT_COMMENT_ID";
	public static final String INTENT_IS_TO_COMMENT = "INTENT_IS_TO_COMMENT";


	/**启动这个Activity的Intent
	 * @param context
	 * @param momentId
	 * @return
	 */
	public static Intent createIntent(Context context, long momentId) {
		return createIntent(context, momentId, false);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param momentId
	 * @param isToComment
	 * @return
	 */
	public static Intent createIntent(Context context, long momentId, boolean isToComment) {
		return createIntent(context, momentId, isToComment, 0);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param momentId
	 * @param isToComment
	 * @param commentId
	 * @return
	 */
	public static Intent createIntent(Context context, long momentId, boolean isToComment, long commentId) {
		return new Intent(context, MomentActivity.class).
				putExtra(INTENT_MOMENT_ID, momentId).
				putExtra(INTENT_IS_TO_COMMENT, isToComment).
				putExtra(INTENT_COMMENT_ID, commentId);
	}


	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	public Activity getActivity() {
		return this;
	}

	private long momentId;
	private boolean isToComment;
	private long commentId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moment_activity, this);

		momentId = getIntent().getLongExtra(INTENT_MOMENT_ID, momentId);
		isToComment = getIntent().getBooleanExtra(INTENT_IS_TO_COMMENT, isToComment);
		commentId = getIntent().getLongExtra(INTENT_COMMENT_ID, commentId);
		if (momentId <= 0) {
			finishWithError("动态不存在！");
			return;
		}

		initCache(this);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		lvBaseList.onRefresh();
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private MomentView momentView;
	@Override
	public void initView() {//必须调用
		super.initView();

		momentView = new MomentView(context, getResources());
		lvBaseList.addHeaderView(momentView.createView(getLayoutInflater()));
		momentView.setShowComment(false);
	}

	private MomentItem momentItem;
	/**
	 * @param momentItem
	 */
	private void setHead(final MomentItem momentItem) {
		this.momentItem = momentItem;
		runUiThread(new Runnable() {

			@Override
			public void run() {
				momentView.bindView(momentItem);
			}
		});
	}

	@Override
	public void setList(final List<CommentItem> list) {
		setList(new AdapterCallBack<CommentAdapter>() {

			@Override
			public CommentAdapter createAdapter() {
				return new CommentAdapter(context, MomentActivity.this);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	private void showInput() {

	}

	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		super.initData();

		runThread(TAG + "initData", new Runnable() {

			@Override
			public void run() {

				momentItem = CacheManager.getInstance().get(MomentItem.class, "" + momentId);
				final List<CommentItem> list = CacheManager.getInstance().getList(
						getCacheClass(), getCacheGroup(), 0, getCacheCount());
				runUiThread(new Runnable() {

					@Override
					public void run() {
						setHead(momentItem);
						setList(list);
					}
				});
			}
		});

	}

	@Override
	public void getListAsync(final int page) {
		if (page <= 0) {
			HttpRequest.getMoment(momentId, HTTP_GET_MOMENT, MomentActivity.this);
		}
		HttpRequest.getCommentList(momentId, 4, page, -page, this);
	}

	@Override
	public List<CommentItem> parseArray(String json) {
		return JSON.parseArray(new JSONResponse(json).getArray(CommentItem.class.getSimpleName()), CommentItem.class);
	}

	@Override
	public Class<CommentItem> getCacheClass() {
		return CommentItem.class;
	}
	@Override
	public String getCacheGroup() {
		return "momentId=" + momentId;
	}
	@Override
	public String getCacheId(CommentItem data) {
		return data == null ? null : "" + data.getId();
	}
	@Override
	public int getCacheCount() {
		return 10;
	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initEvent() {//必须调用
		super.initEvent();

		momentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tvMomentViewContent:
					if (momentItem != null) {
						CommonUtil.copyText(context, momentItem.getMoment().getContent());
					}
					break;
				case R.id.llMomentViewComment:
					showInput();
					break;
				default:
					momentView.onClick(v);
					break;
				}
			}
		});

		lvBaseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toActivity(CommentActivity.createIntent(context, id));
			}
		});
	}

	@Override
	public void onCommentClick(CommentItem cb) {

	}

	public static final int HTTP_GET_MOMENT = 1;
	@Override
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		switch (requestCode) {
		case HTTP_GET_MOMENT:
			MomentItem data = JSONResponse.toObject(new JSONResponse(resultJson), MomentItem.class);
			setHead(data);

			//			// 测试成功
			//			List<Comment> commentList = data.getCommentList();
			//			if (commentList != null) {
			//				List<CommentItem> itemList = new ArrayList<>();
			//				for (Comment comment : commentList) {
			//					itemList.add(new CommentItem(comment));
			//				}
			//				setList(itemList);
			//			}
			break;
		default:
			super.onHttpResponse(requestCode, resultJson, e);
			break;
		}
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	//系统自带监听方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


}