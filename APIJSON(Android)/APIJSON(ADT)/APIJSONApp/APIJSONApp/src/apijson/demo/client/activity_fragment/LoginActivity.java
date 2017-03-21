package apijson.demo.client.activity_fragment;

import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.EditTextManager;
import zuo.biao.library.ui.ServerSettingActivity;
import zuo.biao.library.ui.TextClearSuit;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import apijson.demo.client.R;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.manager.DataManager;
import apijson.demo.client.model.User;
import apijson.demo.client.server.model.Login;
import apijson.demo.client.util.HttpRequest;

/**登录界面
 * @author Lemon
 */
public class LoginActivity extends BaseActivity implements OnClickListener, OnTouchListener, OnBottomDragListener{
	private static final String TAG = "LoginActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, LoginActivity.class);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	@Override
	public Activity getActivity() {
		return this;
	}

	public static final int RESULT_LOGIN = 41;
	public static final String RESULT_LOGINED = "RESULT_LOGINED";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity, this);

		//必须调用<<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private EditText etLoginPhone;
	private EditText etLoginPassword;
	@Override
	public void initView() {//必须调用
		exitAnim = R.anim.bottom_push_out;//退出动画

		etLoginPhone = (EditText) findViewById(R.id.etLoginPhone);
		etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

	}

	private void onLoginSucceed() {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				intent = MainTabActivity.createIntent(context);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				toActivity(intent);
				enterAnim = exitAnim = R.anim.null_anim;
				finish();	
			}
		});
	}

	private void toPassword(int type, int requestCode) {
		toActivity(PasswordActivity.createIntent(context, type, StringUtil.getTrimedString(etLoginPhone)
				, StringUtil.getString(etLoginPassword)), requestCode);
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private String phone;
	private String password;

	@Override
	public void initData() {//必须调用

		phone = DataManager.getInstance().getLastUserPhone();
		if(StringUtil.isPhone(phone)) {
			etLoginPhone.setText("" + phone);
			etLoginPassword.requestFocus();
		}

	}


	private void login(int type) {
		if (EditTextManager.isInputedCorrect(context, etLoginPhone, EditTextManager.TYPE_PHONE) == false
				|| EditTextManager.isInputedCorrect(context, etLoginPassword, type == Login.TYPE_VERIFY 
				? EditTextManager.TYPE_VERIFY : EditTextManager.TYPE_PASSWORD) == false) {
			return;
		}
		EditTextManager.hideKeyboard(context, etLoginPassword);

		showProgress("正在登录，请稍后...");


		phone = StringUtil.getTrimedString(etLoginPhone);
		password = StringUtil.getString(etLoginPassword);

		//登录请求
		HttpRequest.login(phone, password, type, 0, new OnHttpResponseListener() {
			@Override
			public void onHttpResponse(int requestCode, String resultJson, Exception e) {
				dismissProgress();
				JSONResponse response = new JSONResponse(resultJson);
				User user = response.getObject(User.class);

				if (response.isSucceed() == false) {
					switch (response.getStatus()) {
					case 404:
						showShortToast("账号不存在，请先注册");
						onDragBottom(true);
						break;
					case 406:
						showShortToast("账号或密码不合法！");
						break;
					case 412:
						showShortToast("账号或密码错误！");
						break;
					default:
						showShortToast(R.string.login_faild);
						break;
					}
				} else {
					APIJSONApplication.getInstance().saveCurrentUser(user);
					if (APIJSONApplication.getInstance().isLoggedIn() == false) {
						showShortToast("账号或密码错误");
						return;
					}

					onLoginSucceed();
				}
			}
		});

	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>









	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		tvBaseTitle.setOnTouchListener(this);

		findViewById(R.id.tvLoginForget).setOnClickListener(this);
		findViewById(R.id.tvLoginLogin).setOnClickListener(this);

		new TextClearSuit().addClearListener(etLoginPhone, findViewById(R.id.ivLoginPhoneClear));
		new TextClearSuit().addClearListener(etLoginPassword, findViewById(R.id.ivLoginPasswordClear));
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			toPassword(PasswordActivity.TYPE_REGISTER, REQUEST_TO_REGISTER);
			return;
		}

		finish();
	}

	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLoginForget:
			toActivity(BottomMenuWindow.createIntent(context, new String[]{"重置密码", "验证码登录"})
					, REQUEST_TO_BOTTOM_MUNU, false);
			break;
		case R.id.tvLoginLogin:
			login(Login.TYPE_PASSWORD);
			break;
		default:
			break;
		}
	}

	private long touchDownTime = 0;
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (v.getId() == R.id.tvBaseTitle) {
				touchDownTime = System.currentTimeMillis();
				Log.i(TAG, "onTouch MotionEvent.ACTION: touchDownTime=" + touchDownTime);
				return true;
			}
		case MotionEvent.ACTION_UP:
			if (v.getId() == R.id.tvBaseTitle) {
				long time = System.currentTimeMillis() - touchDownTime;
				if (time > 5000 && time < 8000) {
					toActivity(ServerSettingActivity.createIntent(context
							, SettingUtil.getServerAddress(false), SettingUtil.getServerAddress(true)
							, SettingUtil.APP_SETTING, Context.MODE_PRIVATE
							, SettingUtil.KEY_SERVER_ADDRESS_NORMAL, SettingUtil.KEY_SERVER_ADDRESS_TEST)
							, REQUEST_TO_SERVER_SETTING);
					return true;
				}
			}
			break;
		default:
			break;
		}

		return false;
	}



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void finish() {
		if (APIJSONApplication.getInstance().isLoggedIn() == false) {
			showShortToast("未登录，有些内容会加载不出来~");
		}

		setResult(RESULT_OK, new Intent().putExtra(RESULT_LOGINED, APIJSONApplication.getInstance().isLoggedIn()));
		super.finish();
	}


	public static final int REQUEST_TO_BOTTOM_MUNU = 1;
	public static final int REQUEST_TO_SERVER_SETTING = 2;
	public static final int REQUEST_TO_REGISTER = 3;
	public static final int REQUEST_TO_VERIFY = 4;
	public static final int REQUEST_TO_RESET = 5;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TO_SERVER_SETTING:
			sendBroadcast(new Intent(ACTION_EXIT_APP));
			break;
		case REQUEST_TO_BOTTOM_MUNU:
			if (data != null) {
				switch (data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1)) {
				case 0:
					toPassword(PasswordActivity.TYPE_RESET, REQUEST_TO_RESET);
					break;
				case 1:
					toPassword(PasswordActivity.TYPE_VERIFY, REQUEST_TO_VERIFY);
					break;
				default:
					break;
				}
			}
			break;
		case REQUEST_TO_RESET:
		case REQUEST_TO_VERIFY:
		case REQUEST_TO_REGISTER:
			if (data != null) {
				String phone = data.getStringExtra(INTENT_PHONE);
				String password = data.getStringExtra(requestCode == REQUEST_TO_VERIFY ? INTENT_VERIFY : INTENT_PASSWORD);
				if (StringUtil.isPhone(phone)) {
					etLoginPhone.setText(phone);
				}
				if (StringUtil.isNotEmpty(password, true)) {
					etLoginPassword.setText(password);
				}

				login(requestCode == REQUEST_TO_VERIFY ? Login.TYPE_VERIFY : Login.TYPE_PASSWORD);
			}
			break;
		default:
			break;
		}
	}

	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


}