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

import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseView.OnDataChangedListener;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.CacheManager;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.ui.BottomMenuView;
import zuo.biao.library.ui.BottomMenuView.OnBottomMenuItemClickListener;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.CutPictureActivity;
import zuo.biao.library.ui.EditTextInfoActivity;
import zuo.biao.library.ui.EditTextInfoWindow;
import zuo.biao.library.ui.SelectPictureActivity;
import zuo.biao.library.ui.TextClearSuit;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import apijson.demo.client.R;
import apijson.demo.client.base.BaseActivity;
import apijson.demo.client.model.User;
import apijson.demo.client.util.ActionUtil;
import apijson.demo.client.util.HttpRequest;
import apijson.demo.client.util.MenuUtil;
import apijson.demo.client.view.UserView;

/**联系人资料界面
 * @author Lemon
 */
public class UserActivity extends BaseActivity implements OnClickListener, OnBottomDragListener
, OnBottomMenuItemClickListener, OnHttpResponseListener, Runnable, OnDialogButtonClickListener {
	public static final String TAG = "UserActivity";

	public static final String INTENT_IS_ON_EDIT_MODE = "INTENT_IS_ON_EDIT_MODE";

	/**获取启动UserActivity的intent
	 * @param context
	 * @param id
	 * @return
	 */
	public static Intent createIntent(Context context, long id) {
		return new Intent(context, UserActivity.class).putExtra(INTENT_ID, id);
	}


	@Override
	public Activity getActivity() {
		return this;
	}

	private long id = 0;
	private boolean isOnEditMode = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_activity, this);

		intent = getIntent();
		id = intent.getLongExtra(INTENT_ID, id);
		isOnEditMode = intent.getBooleanExtra(INTENT_IS_ON_EDIT_MODE, isOnEditMode);

		id = getIntent().getLongExtra(INTENT_ID, id);
		if (id <= 0) {
			finishWithError("用户不存在！");
			return;
		}

		if (isOnEditMode && isCurrentUser(id) == false) {
			Log.e(TAG, "只能修改自己的个人资料");
			isOnEditMode = false;
		}

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//	private BaseViewLayout<User> bvlUser;//方式一
	//	private UserViewLayout uvlUser;//方式二

	private ViewGroup llUserBusinessCardContainer;//方式三
	private UserView userView;

	private EditText etUserRemark;
	private TextView tvUserTag;

	private ViewGroup llUserBottomMenuContainer;
	private BottomMenuView bottomMenuView;
	@Override
	public void initView() {//必须调用

		//添加用户名片，这些方式都可<<<<<<<<<<<<<<<<<<<<<<
		//		//方式一
		//		bvlUser = (BaseViewLayout<User>) findViewById(R.id.bvlUser);
		//		bvlUser.createView(new UserView(context, getResources()));
		//		
		//		//方式二
		//		uvlUser = (UserViewLayout) findViewById(R.id.uvlUser);

		//方式三
		llUserBusinessCardContainer = (ViewGroup) findViewById(R.id.llUserBusinessCardContainer);
		llUserBusinessCardContainer.removeAllViews();

		userView = new UserView(context, getResources());
		llUserBusinessCardContainer.addView(userView.createView(getLayoutInflater()));
		//添加用户名片，这些方式都可>>>>>>>>>>>>>>>>>>>>>>>


		etUserRemark = (EditText) findViewById(R.id.etUserRemark);
		tvUserTag = (TextView) findViewById(R.id.tvUserTag);


		if (isOnEditMode == false) {
			//添加底部菜单<<<<<<<<<<<<<<<<<<<<<<
			llUserBottomMenuContainer = (ViewGroup) findViewById(R.id.llUserBottomMenuContainer);
			llUserBottomMenuContainer.removeAllViews();

			bottomMenuView = new BottomMenuView(context, getResources(), REQUEST_TO_BOTTOM_MENU);
			llUserBottomMenuContainer.addView(bottomMenuView.createView(getLayoutInflater()));
			//添加底部菜单>>>>>>>>>>>>>>>>>>>>>>>
		}

	}

	private User user;
	/**显示用户
	 * @param user_
	 */
	private void setUser(User user_) {
		this.user = user_;
		if (user == null) {
			Log.w(TAG, "setUser  user == null >> user = new User();");
			user = new User();
		}

		runUiThread(new Runnable() {

			@Override
			public void run() {
				//				bvlUser.bindView(user);//方式一
				//				uvlUser.bindView(user);//方式二
				userView.bindView(user);//方式三

				etUserRemark.setText(StringUtil.getTrimedString(user.getHead()));
				tvUserTag.setText(StringUtil.getTrimedString(user.getTag()));
			}
		});
	}

	/**裁剪图片
	 * @param path
	 */
	private void cutPicture(String path) {
		if (StringUtil.isFilePath(path) == false) {
			Log.e(TAG, "cutPicture  StringUtil.isFilePath(path) == false >> showShortToast(找不到图片);return;");
			showShortToast("找不到图片");
			return;
		}

		toActivity(CutPictureActivity.createIntent(context, path
				, DataKeeper.imagePath, "photo" + System.currentTimeMillis(), 200)
				, REQUEST_TO_CUT_PICTURE);
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initData() {//必须调用
		super.initData();

		if (isCurrentUserCorrect() == false) {//解决不能在未登录情况下查看
			run();
		}
	}

	@Override
	public void run() {
		tvBaseTitle.setText(isOnEditMode ? "编辑资料" : (isCurrentUser(id) ? "我的资料" : "详细资料"));

		if (bottomMenuView != null) {
			bottomMenuView.bindView(MenuUtil.getMenuList(MenuUtil.USER, id, ! User.isFirend(currentUser, id)));
		}

		setUser(CacheManager.getInstance().get(User.class, "" + id));//先加载缓存数据，比网络请求快很多
		HttpRequest.getUser(id, HTTP_GET, UserActivity.this);
	}


	private User getUser() {
		if (user == null) {
			user = new User(id);
		}
		return user;
	}

	private void putUser() {
		showProgress("正在上传...");
		user = getUser();
		apijson.demo.client.server.model.User userRequest = new apijson.demo.client.server.model.User(user.getId());
		userRequest.setName(user.getName());
		userRequest.setSex(user.getSex());
		userRequest.setTag(user.getTag());
		userRequest.setHead(user.getHead());
		//		userRequest.setStarred(user.getStarred());

		HttpRequest.put(new JSONRequest(userRequest).setTag(User.class.getSimpleName())
				, HTTP_PUT, this);
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		findViewById(R.id.llUserTag).setOnClickListener(this);

		new TextClearSuit().addClearListener(etUserRemark, findViewById(R.id.ivUserRemarkClear));//清空备注按钮点击监听

		userView.setOnDataChangedListener(new OnDataChangedListener() {

			@Override
			public void onDataChanged() {
				user = userView.getData();
			}
		});

		if (isOnEditMode) {
			userView.setOnDataChangedListener(new OnDataChangedListener() {

				@Override
				public void onDataChanged() {
					isDataChanged = true;
					user = userView.getData();
				}
			});
			
			userView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (user == null) {
						return;
					}
					switch (v.getId()) {
					case R.id.ivUserViewHead:
						toActivity(SelectPictureActivity.createIntent(context), REQUEST_TO_SELECT_PICTURE, false);
						break;
					case R.id.tvUserViewName:
						toActivity(EditTextInfoWindow.createIntent(context, "名字", user.getName())
								, REQUEST_TO_EDIT_TEXT_INFO_NAME, false);
						break;
					default:
						switch (v.getId()) {
						case R.id.tvUserViewSex:
							user.setSex(user.getSex() == User.SEX_FEMALE ? User.SEX_MAIL : User.SEX_FEMALE);
							break;
						case R.id.tvUserViewPhone:
							toActivity(EditTextInfoWindow.createIntent(context, EditTextInfoWindow.TYPE_PHONE
									, "手机", user.getPhone()), REQUEST_TO_EDIT_TEXT_INFO_PHONE, false);
							break;
						default:
							return;
						}
						userView.bindView(user);
						isDataChanged = true;
						break;
					}				
				}
			});
		}

		if (bottomMenuView != null) {
			bottomMenuView.setOnMenuItemClickListener(this);//底部菜单点击监听
		}
	}

	@Override
	public void onBottomMenuItemClick(int intentCode) {
		if (user == null) {
			Log.e(TAG, "onBottomMenuItemClick  user == null >> return;");
			return;
		}
		if (verifyLogin() == false) {
			return;
		}
		switch (intentCode) {
		case MenuUtil.INTENT_CODE_EDIT:
			toActivity(UserActivity.createIntent(context, id).putExtra(UserActivity.INTENT_IS_ON_EDIT_MODE, true));
			break;
		case MenuUtil.INTENT_CODE_ADD:
			HttpRequest.setIsFriend(id, true, HTTP_ADD, this);
			break;
		case MenuUtil.INTENT_CODE_DELETE:
			HttpRequest.setIsFriend(id, false, HTTP_DELETE, this);
			break;
		case MenuUtil.INTENT_CODE_QRCODE:
			toActivity(QRCodeActivity.createIntent(context, id));
			break;
		case MenuUtil.INTENT_CODE_SEND:
			CommonUtil.shareInfo(context, JSON.toJSONString(user));
			break;
		default:
			break;
		}
	}

	//对应HttpRequest.getUser(id, 0, UserActivity.this); <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	protected static final int HTTP_GET = 1;
	protected static final int HTTP_ADD = 2;
	protected static final int HTTP_DELETE = 3;
	protected static final int HTTP_PUT = 4;

	@Override
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		JSONResponse response = new JSONResponse(resultJson).getJSONResponse(User.class.getSimpleName());
		boolean isSucceed = JSONResponse.isSucceed(response);

		dismissProgress();
		switch (requestCode) {
		case HTTP_GET:
			setUser(new JSONResponse(resultJson).getObject(User.class));
			break;
		case HTTP_ADD:
		case HTTP_DELETE:
			if (isSucceed) {
				showShortToast(requestCode == HTTP_ADD ? R.string.add_succeed : R.string.delete_succeed);
				sendBroadcast(new Intent(ActionUtil.ACTION_RELOAD_CURRENT_USER));
			} else {
				showShortToast(requestCode == HTTP_ADD ? R.string.add_failed : R.string.delete_failed);
			}
			break;
		case HTTP_PUT:
			if (isSucceed) {
				isDataChanged = false;
				sendBroadcast(new Intent(ActionUtil.ACTION_USER_CHANGED)
				.putExtra(INTENT_ID, id));
			}
			showShortToast(isSucceed ? "提交成功" : "提交失败，请检查网络后重试");
			break;
		default:
			break;
		}
	}
	//对应HttpRequest.getUser(id, 0, UserActivity.this); >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llUserTag:
			toActivity(EditTextInfoActivity.createIntent(context, "标签"
					, StringUtil.getTrimedString(tvUserTag)), REQUEST_TO_EDIT_TEXT_INFO);
			break;
		default:
			break;
		}
	}

	public static final int DIALOG_PUT_USER = 1;
	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (isPositive == false) {
			if (requestCode == DIALOG_PUT_USER) {
				isDataChanged = false;
				finish();
			}
			return;
		}

		switch (requestCode) {
		case DIALOG_PUT_USER:
			putUser();
			break;
		default:
			break;
		}
	}


	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private static final int REQUEST_TO_BOTTOM_MENU = 1;
	private static final int REQUEST_TO_SELECT_PICTURE = 2;
	private static final int REQUEST_TO_CUT_PICTURE = 3;
	private static final int REQUEST_TO_EDIT_TEXT_INFO = 4;
	private static final int REQUEST_TO_EDIT_TEXT_INFO_NAME = 5;
	private static final int REQUEST_TO_EDIT_TEXT_INFO_PHONE = 6;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_BOTTOM_MENU:
			if (data != null) {
				onBottomMenuItemClick(data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1));
			}
			break;
		case REQUEST_TO_EDIT_TEXT_INFO:
		case REQUEST_TO_EDIT_TEXT_INFO_NAME:
		case REQUEST_TO_EDIT_TEXT_INFO_PHONE:
			if (data == null) {
				break;
			}
			isDataChanged = true;
			user = getUser();
			String value = data.getStringExtra(EditTextInfoActivity.RESULT_VALUE);
			switch (requestCode) {
			case REQUEST_TO_EDIT_TEXT_INFO_NAME:
				user.setName(value);
				break;
			case REQUEST_TO_EDIT_TEXT_INFO_PHONE:
				user.setPhone(value);
				break;
			default:
				user.setTag(value);
				break;
			}
			setUser(user);
			break;
		case REQUEST_TO_SELECT_PICTURE:
			if (data != null) {
				cutPicture(data.getStringExtra(SelectPictureActivity.RESULT_PICTURE_PATH));
			}
			break;
		case REQUEST_TO_CUT_PICTURE:
			if (data != null) {
				isDataChanged = true;

				user = getUser();
				user.setHead(data.getStringExtra(CutPictureActivity.RESULT_PICTURE_PATH));
				setUser(user);
			}
			break;
		}
	}


	private boolean isDataChanged = false;
	@Override
	public void finish() {
		user = getUser();
		user.setHead(StringUtil.getTrimedString(etUserRemark));
		if (isOnEditMode && isDataChanged) {
			new AlertDialog(context, "", "资料已改变，需要保存吗？", true, DIALOG_PUT_USER, this).show();
			return;
		}
		if (user != null) {
			CacheManager.getInstance().save(User.class, user, "" + user.getId(), "range=" + 0);//更新缓存
		}

		super.finish();
	}


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}