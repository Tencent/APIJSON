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

package apijson.demo.client.activity_fragment;

import zuo.biao.apijson.JSON;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import apijson.demo.client.R;
import apijson.demo.client.application.APIJSONApplication;

import com.alibaba.fastjson.JSONObject;

/**使用方法：复制>粘贴>改名>改代码  */
/**fragmentActivity示例
 * @author Lemon
 * @use toActivity(DemoFragmentActivity.createIntent(...));
 */
public class MomentListActivity extends BaseActivity implements OnBottomDragListener {
	//	private static final String TAG = "DemoFragmentActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_RANGE = "INTENT_RANGE";
	public static final String INTENT_SEARCH = "INTENT_SEARCH";
	public static final String INTENT_SHOW_SEARCH = "INTENT_SHOW_SEARCH";

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return createIntent(context, MomentListFragment.RANGE_USER_CIRCLE
				, APIJSONApplication.getInstance().getCurrentUserId());
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return createIntent(context, MomentListFragment.RANGE_USER, userId);
	}
	/**启动这个Activity的Intent
	 * showSearch = true;
	 * @param context
	 * @param range
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, int range, long id) {
		return createIntent(context, range, id, null, true);
	}
	/**启动这个Activity的Intent
	 * showSearch = false;
	 * @param context
	 * @param search
	 * @return
	 */
	public static Intent createIntent(Context context, JSONObject search) {
		return createIntent(context, search, false);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param search
	 * @param showSearch
	 * @return
	 */
	public static Intent createIntent(Context context, JSONObject search, boolean showSearch) {
		return createIntent(context, MomentListFragment.RANGE_ALL, 0, search, showSearch);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param range
	 * @param userId
	 * @param search
	 * @param showSearch
	 * @return
	 */
	public static Intent createIntent(Context context, int range, long id, JSONObject search, boolean showSearch) {
		return new Intent(context, MomentListActivity.class)
		.putExtra(INTENT_RANGE, range)
		.putExtra(INTENT_ID, id)
		.putExtra(INTENT_SEARCH, JSON.toJSONString(search))
		.putExtra(INTENT_SHOW_SEARCH, showSearch);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	public Activity getActivity() {
		return this;
	}

	private int range = MomentListFragment.RANGE_ALL;
	private long id;
	private JSONObject search;
	private boolean showSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moment_list_activity, this);

		range = getIntent().getIntExtra(INTENT_RANGE, range);
		id = getIntent().getLongExtra(INTENT_ID, id);
		search = JSON.parseObject(getIntent().getStringExtra(INTENT_SEARCH));
		showSearch = getIntent().getBooleanExtra(INTENT_SHOW_SEARCH, showSearch);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private MomentListFragment fragment;
	@Override
	public void initView() {//必须在onCreate方法内调用
		String title;
		switch (range) {
		case MomentListFragment.RANGE_ALL:
			title = "全部";
			break;
			//		case MomentListFragment.RANGE_SINGLE:
			//			title = "动态";
			//			break;
		case MomentListFragment.RANGE_USER:
			title = APIJSONApplication.getInstance().isCurrentUser(id) ? "我的动态" : "TA的动态";
			break;
		case MomentListFragment.RANGE_USER_CIRCLE:
			title = "朋友圈";
			break;
		default:
			title = "动态";
			break;
		}
		tvBaseTitle.setText(title);
		autoSetTitle();

		findViewById(R.id.ivMomentListForward).setVisibility(showSearch ? View.VISIBLE : View.GONE);


		fragment = MomentListFragment.createInstance(range, id, search);

		fragmentManager
		.beginTransaction()
		.add(R.id.flMomentListContainer, fragment)
		.show(fragment)
		.commit();

	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用

	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreate方法内调用

	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			fragment.onDragBottom(rightToLeft);
			return;
		}	

		finish();
	}

	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<





	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}