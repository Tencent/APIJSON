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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

import apijson.demo.client.R;
import apijson.demo.client.adapter.UserAdapter;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.base.BaseHttpListFragment;
import apijson.demo.client.interfaces.TopBarMenuCallback;
import apijson.demo.client.model.User;
import apijson.demo.client.util.HttpRequest;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.StringUtil;
import zuo.biao.library.base.BaseView.OnDataChangedListener;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.EditTextInfoWindow;

/**用户列表界面fragment
 * @author Lemon
 * @use new UserListFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 * @must 查看 .HttpManager 中的@must和@warn
 *       查看 .SettingUtil 中的@must和@warn
 */
public class UserListFragment extends BaseHttpListFragment<User, UserAdapter>
implements CacheCallBack<User>, OnHttpResponseListener, OnBottomDragListener
, TopBarMenuCallback, OnDataChangedListener {
	//	private static final String TAG = "UserListFragment";

	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String ARGUMENT_RANGE = "ARGUMENT_RANGE";
	public static final String ARGUMENT_SEARCH = "ARGUMENT_SEARCH";
	public static final String ARGUMENT_ID_LIST = "ARGUMENT_ID_LIST";

	/**
	 * <br> range = RANGE_USER_FRIEND
	 * <br> id = APIJSONApplication.getInstance().getCurrentUserId()
	 * @return
	 */
	public static UserListFragment createInstance() {
		return createInstance(RANGE_USER_FRIEND, APIJSONApplication.getInstance().getCurrentUserId());
	}
	/**
	 * range = RANGE_USER
	 * @param userId
	 * @return
	 */
	public static UserListFragment createInstance(long userId) {
		return createInstance(RANGE_USER, userId);
	}
	/**
	 * @param range
	 * @param id
	 * @return
	 */
	public static UserListFragment createInstance(int range, long id) {
		return createInstance(range, id, null);
	}
	/**
	 * range = RANGE_ALL
	 * @param search
	 * @return
	 */
	public static UserListFragment createInstance(JSONObject search) {
		return createInstance(RANGE_ALL, 0, search);
	}
	/**
	 * @param range
	 * @param id
	 * @param search
	 * @return
	 */
	public static UserListFragment createInstance(int range, long id, JSONObject search) {
		UserListFragment fragment = new UserListFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(ARGUMENT_RANGE, range);
		bundle.putLong(ARGUMENT_ID, id);
		bundle.putString(ARGUMENT_SEARCH, JSON.toJSONString(search));

		fragment.setArguments(bundle);
		return fragment;
	}
	/**
	 * @param idList
	 * @return
	 */
	public static UserListFragment createInstance(List<Long> idList) {
		UserListFragment fragment = new UserListFragment();

		Bundle bundle = new Bundle();
		bundle.putSerializable(ARGUMENT_ID_LIST, (Serializable) idList);

		fragment.setArguments(bundle);
		return fragment;
	}

	//与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	public static final int RANGE_ALL = HttpRequest.RANGE_ALL;
	public static final int RANGE_USER = HttpRequest.RANGE_USER;
	public static final int RANGE_USER_FRIEND = HttpRequest.RANGE_USER_FRIEND;
	public static final int RANGE_USER_CIRCLE = HttpRequest.RANGE_USER_CIRCLE;
	public static final int RANGE_MOMENT = HttpRequest.RANGE_MOMENT;
	public static final int RANGE_COMMENT = HttpRequest.RANGE_COMMENT;


	private int range = RANGE_ALL;
	private long id = 0;
	private JSONObject search;
	private List<Long> idList;
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		registerObserver(this);

		argument = getArguments();
		if (argument != null) {
			idList = (List<Long>) argument.getSerializable(ARGUMENT_ID_LIST);
			if (idList == null) {
				range = argument.getInt(ARGUMENT_RANGE, range);
				id = argument.getLong(ARGUMENT_ID, id);
				search = JSON.parseObject(argument.getString(ARGUMENT_SEARCH));//JSONObject can't be serializabled
			}
		}

		initCache(this);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		lvBaseList.onRefresh();

		return view;
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {//必须调用
		super.initView();

	}

	@Override
	public void setList(final List<User> list) {
		setList(new AdapterCallBack<UserAdapter>() {

			@Override
			public UserAdapter createAdapter() {
				return new UserAdapter(context);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	
	private ImageView leftMenu;
	@SuppressLint("InflateParams")
	@Override
	public View getLeftMenu(Activity activity) {
		if (leftMenu == null) {
			leftMenu = (ImageView) LayoutInflater.from(activity).inflate(R.layout.top_right_iv, null);
			leftMenu.setImageResource(R.drawable.add);
			leftMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onDragBottom(false);
				}
			});
		}
		return leftMenu;
	}

	private ImageView rightMenu;
	@SuppressLint("InflateParams")
	@Override
	public View getRightMenu(Activity activity) {
		if (rightMenu == null) {
			rightMenu = (ImageView) LayoutInflater.from(activity).inflate(R.layout.top_right_iv, null);
			rightMenu.setImageResource(R.drawable.search);
			rightMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onDragBottom(true);
				}
			});
		}
		return rightMenu;
	}

	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		super.initData();

	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getUserList(range, id, search, idList, getCacheCount(), page, -page, this);
	}

	@Override
	public List<User> parseArray(String json) {
		return new JSONResponse(json).getList(getCacheClass().getSimpleName() + "[]", getCacheClass());
	}

	@Override
	public Class<User> getCacheClass() {
		return User.class;
	}
	@Override
	public String getCacheGroup() {
		if (search != null || idList != null) {
			return null;
		}
		if (range == RANGE_ALL) {
			return "range=" + range;
		}
		return range == RANGE_USER || range == RANGE_MOMENT || range == RANGE_COMMENT
				? null : "range=" + range + ";userId=" + id;
	}
	@Override
	public String getCacheId(User data) {
		return data == null ? null : "" + data.getId();
	}
	@Override
	public int getCacheCount() {
		return 10;
	}

	public int searchType = 0;
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initEvent() {//必须调用
		super.initEvent();

		lvBaseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toActivity(UserActivity.createIntent(context, id));
			}
		});
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (isAlive() == false) {
			return;
		}

		if (rightToLeft == false) {
			startActivity(UserListActivity.createIntent(context, RANGE_ALL, 0).putExtra(INTENT_TITLE, "添加"));
			context.overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
		} else {
			if (range != RANGE_ALL && verifyLogin() == false) {
				return;
			}

			showShortToast("输入为空则查看全部");
			if (searchType <= 0) {
				searchType = EditTextInfoWindow.TYPE_PHONE;
			}
			if (searchType == EditTextInfoWindow.TYPE_NAME) {
				toActivity(EditTextInfoWindow.createIntent(context
						, EditTextInfoWindow.TYPE_NAME, "姓名", null),
						REQUEST_TO_EDIT_TEXT_INFO_SEARCH, false);
			} else {
				toActivity(EditTextInfoWindow.createIntent(context
						, EditTextInfoWindow.TYPE_PHONE, "手机号", null),
						REQUEST_TO_EDIT_TEXT_INFO_ADD, false);
			}
		}
		
	}


	@Override
	public void onDataChanged() {
		if (range == RANGE_USER_FRIEND) {
			super.onRefresh();
		}
	}

	//系统自带监听方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int REQUEST_TO_EDIT_TEXT_INFO_SEARCH = 1;
	public static final int REQUEST_TO_EDIT_TEXT_INFO_ADD = 2;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_EDIT_TEXT_INFO_SEARCH:
		case REQUEST_TO_EDIT_TEXT_INFO_ADD:
			if (data != null) {
				boolean isSearch = requestCode == REQUEST_TO_EDIT_TEXT_INFO_SEARCH;
				String value = StringUtil.getString(data.getStringExtra(EditTextInfoWindow.RESULT_VALUE));
				String split = "";
				JSONRequest search = new JSONRequest();
				if (StringUtil.isNotEmpty(value, true)) {
					split = ":";
					if (isSearch) {
						search.putSearch(HttpRequest.NAME, value);
					} else {
						search.put(HttpRequest.PHONE, value);
					}
				}
				intent = isSearch
						? UserListActivity.createIntent(context, range, id, search, false)
								: UserListActivity.createIntent(context, search, false);
						toActivity(intent.putExtra(INTENT_TITLE, "搜索" + split + value));
			}
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public void onDestroy() {
		if (leftMenu != null) {
			leftMenu.destroyDrawingCache();
			leftMenu = null;
		}
		if (rightMenu != null) {
			rightMenu.destroyDrawingCache();
			rightMenu = null;
		}

		super.onDestroy();
	}

	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


}