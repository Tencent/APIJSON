package apijson.demo.client.activity_fragment;

import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.EditTextManager;
import zuo.biao.library.ui.TextClearSuit;
import zuo.biao.library.util.StringUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import apijson.demo.client.R;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.model.User;
import apijson.demo.client.model.Verify;
import apijson.demo.client.util.HttpRequest;

/**注册、验证码登录、重置密码等密码相关界面
 * @author Lemon
 */
public class PasswordActivity extends BaseActivity implements OnClickListener, OnHttpResponseListener, OnBottomDragListener {
	public static final String TAG = "PasswordActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动这个Activity的Intent
	 * @param context
	 * @param phone
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String phone) {
		return createIntent(context, type, phone, null);
	}
	/**启动这个Activity的Intent
	 * @param context
	 * @param phone
	 * @param password
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String phone, String password) {
		return new Intent(context, PasswordActivity.class)
		.putExtra(INTENT_TYPE, type)
		.putExtra(INTENT_PHONE, phone)
		.putExtra(INTENT_PASSWORD, password);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	public Activity getActivity() {
		return this;
	}

	public static final int TYPE_VERIFY = 0;
	public static final int TYPE_REGISTER = 1;
	public static final int TYPE_RESET = 3;

	private int type = TYPE_VERIFY;
	private String phone;
	private String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_activity, this);

		type = getIntent().getIntExtra(INTENT_TYPE, type);
		phone = getIntent().getStringExtra(INTENT_PHONE);
		password = getIntent().getStringExtra(INTENT_PASSWORD);

		//必须调用<<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码) <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private EditText etPasswordPhone;
	private View pbPasswordGettingVerify;
	private TextView btnPasswordGetVerify;

	private View llPasswordPassword;
	private EditText etPasswordVerify;
	private EditText etPasswordPassword0;
	private EditText etPasswordPassword1;

	@Override
	public void initView() {//必须调用

		etPasswordPhone = (EditText) findViewById(R.id.etPasswordPhone);
		pbPasswordGettingVerify = findViewById(R.id.pbPasswordGettingVerify);
		btnPasswordGetVerify = (TextView) findViewById(R.id.btnPasswordGetVerify);

		llPasswordPassword = findViewById(R.id.llPasswordPassword);
		etPasswordVerify = (EditText) findViewById(R.id.etPasswordVerify);
		etPasswordPassword0 = (EditText) findViewById(R.id.etPasswordPassword0);
		etPasswordPassword1 = (EditText) findViewById(R.id.etPasswordPassword1);

	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		switch (type) {
		case TYPE_REGISTER:
			tvBaseTitle.setText("注册");
			break;
		case TYPE_RESET:
			tvBaseTitle.setText("重置密码");
			break;
		default:
			tvBaseTitle.setText("验证手机");
			break;
		}
		autoSetTitle();

		llPasswordPassword.setVisibility(type == TYPE_VERIFY ? View.GONE : View.VISIBLE);


		if (StringUtil.isNotEmpty(password, true)) {
			etPasswordPassword0.setText(StringUtil.getString(password));
		}
		if (StringUtil.isPhone(phone)) {
			etPasswordPhone.setText("" + phone);
			getVerify();
		}

	}

	private TimeCount time;
	/**获取验证码
	 * @param et
	 */
	private void getVerify() {
		if (EditTextManager.isInputedCorrect(context, etPasswordPhone, EditTextManager.TYPE_PHONE) == false) {
			return;
		}

		if (StringUtil.isNotEmpty(etPasswordPassword0, true)) {
			etPasswordPassword1.requestFocus();
		} else {
			etPasswordPassword0.requestFocus();
		}

		pbPasswordGettingVerify.setVisibility(View.VISIBLE);
		btnPasswordGetVerify.setText("获取中...");
		time = new TimeCount(60000, 1000);

		//判断手机号是否已经注册
		HttpRequest.checkRegister(StringUtil.getTrimedString(etPasswordPhone), HTTP_CHECK_REGISTER, this);
	}

	private void getVerifyFromServer() {
		showProgress();
		HttpRequest.getAuthCode(StringUtil.getTrimedString(etPasswordPhone), HTTP_GET_VERIFY, this);
	}

	private void toNextStep() {
		if (EditTextManager.isInputedCorrect(context, etPasswordPhone, EditTextManager.TYPE_PHONE) == false 
				|| EditTextManager.isInputedCorrect(context, etPasswordVerify, EditTextManager.TYPE_VERIFY) == false) {
			return;
		}
		if (type != TYPE_VERIFY) {
			if (EditTextManager.isInputedCorrect(context, etPasswordPassword0, EditTextManager.TYPE_PASSWORD) == false 
					|| EditTextManager.isInputedCorrect(context, etPasswordPassword1, EditTextManager.TYPE_PASSWORD) == false) {
				return;
			}
			
			final String password0 = StringUtil.getTrimedString(etPasswordPassword0);
			String password1 = StringUtil.getTrimedString(etPasswordPassword1);
			if (! password0.equals(password1)) {
				showShortToast("密码输入不一致，请重新输入");
				return;
			}
		}

		verify();
	}

	//验证验证码
	private void verify() {
		if (EditTextManager.isInputedCorrect(context, etPasswordPhone, EditTextManager.TYPE_PHONE) == false 
				|| EditTextManager.isInputedCorrect(context, etPasswordVerify, EditTextManager.TYPE_VERIFY) == false) {
			return;
		}

		showProgress();
		HttpRequest.checkAuthCode(StringUtil.getTrimedString(etPasswordPhone),
				StringUtil.getTrimedString(etPasswordVerify), HTTP_CHECK_VERIFY, this);
	}


	private void register() {
		showProgress();
		HttpRequest.register(StringUtil.getTrimedString(etPasswordVerify)
				, StringUtil.getTrimedString(etPasswordPhone)
				, StringUtil.getString(etPasswordPassword0), 
				"APIJSONUser", 0, HTTP_REGISTER, this); // 注册接口
	}

	private void setPassword() {
		showProgress();
		HttpRequest.setPassword(StringUtil.getTrimedString(etPasswordVerify)
				, StringUtil.getTrimedString(etPasswordPhone)
				, StringUtil.getString(etPasswordPassword0), 
				HTTP_RESET_PASSWORD, this); // 注册接口
	}


	/**
	 * @param response
	 * @param user
	 */
	private void onPasswordResponse(JSONResponse response, User user) {
		dismissProgress();

		if (user == null || user.getId() <= 0 || JSONResponse.isSucceed(response) == false) {
			showShortToast("注册失败，请检查网络后重试");
			return;
			//			} else if (result.getResultCode() == States.REGISTER_ACCOUNT_EXISTS) {
			//				showShortToast("该账号已经注册过");
			//				return;
		} else {
			showShortToast("注册成功");
			APIJSONApplication.getInstance().saveCurrentUser(user);

			saveAndExit(newResult().putExtra(INTENT_ID, user.getId()));
		}
	}

	private Intent newResult() {
		return new Intent()
		.putExtra(INTENT_PHONE, StringUtil.getTrimedString(etPasswordPhone))
		.putExtra(INTENT_VERIFY, StringUtil.getTrimedString(etPasswordVerify))
		.putExtra(INTENT_PASSWORD, StringUtil.getTrimedString(etPasswordPassword0));
	}

	private void saveAndExit(Intent intent) {
		setResult(RESULT_OK, intent);
		finish();
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		findViewById(R.id.btnPasswordGetVerify).setOnClickListener(this);

		findViewById(R.id.btnPasswordNext).setOnClickListener(this);


		new TextClearSuit().addClearListener(etPasswordPhone, findViewById(R.id.ivPasswordPhoneClear));
		new TextClearSuit().addClearListener(etPasswordVerify, findViewById(R.id.ivPasswordVerifyClear));
		new TextClearSuit().addClearListener(etPasswordPassword0, findViewById(R.id.ivPasswordPassword0Clear));
		new TextClearSuit().addClearListener(etPasswordPassword1, findViewById(R.id.ivPasswordPassword1Clear));

	}


	public static final int HTTP_CHECK_REGISTER = 1;
	public static final int HTTP_CHECK_VERIFY = 2;
	public static final int HTTP_GET_VERIFY = 3;
	public static final int HTTP_REGISTER = 4;
	public static final int HTTP_RESET_PASSWORD = 5;

	@Override
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		final JSONResponse response = new JSONResponse(resultJson);
		dismissProgress();
		switch (requestCode) {
		case HTTP_CHECK_REGISTER:
			final JSONResponse response2 = response.getJSONResponse(User.class.getSimpleName());
			Log.i(TAG, "checkPassword result = " + resultJson);
			runUiThread(new Runnable() {
				@Override
				public void run() {
					if (JSONResponse.isSucceed(response2) == false) {
						showShortToast(R.string.get_failed);
						pbPasswordGettingVerify.setVisibility(View.GONE);
						btnPasswordGetVerify.setText("重新获取");
					} else if (JSONResponse.isExist(response2)) {
						if (type == TYPE_REGISTER) {
							pbPasswordGettingVerify.setVisibility(View.GONE);
							btnPasswordGetVerify.setText("获取验证码");
							showShortToast("手机号已经注册");
						} else {
							getVerifyFromServer();
						}
					} else {//手机号未被注册过
						if (type == TYPE_REGISTER) {
							getVerifyFromServer();
						} else {
							showShortToast("手机号未注册");
						}
					}
				}
			});
			break;
		case HTTP_CHECK_VERIFY:
			runUiThread(new Runnable() {
				@Override
				public void run() {
					if (JSONResponse.isExist(response.getJSONResponse(Verify.class.getSimpleName()))) {	//验证验证码成功
						switch (type) {
						case TYPE_REGISTER:
							register();
							break;
						case TYPE_RESET:
							setPassword();
							break;
						default:
							saveAndExit(newResult());
							break;
						}

						//							etPasswordPhone.setEnabled(false);
						//						} else if(result.getResultCode()==States.AUTHCODE_CHECK_EXPIRED) {	//主线程提示验证码过期
						//							EditTextManager.showInputedError(context, etPasswordVerify, "验证码已过期");
					} else {//输入验证码错误提醒
						EditTextManager.showInputedError(context, etPasswordVerify, "验证码错误");
					}
				}
			});
			break;
		case HTTP_GET_VERIFY:
			final Verify verify = response.getObject(Verify.class);
			runUiThread(new Runnable() {
				@Override
				public void run() {
					pbPasswordGettingVerify.setVisibility(View.GONE);
					if (verify == null || StringUtil.isNotEmpty(verify.getCode(), true) == false) {
						showShortToast(R.string.get_failed);
						btnPasswordGetVerify.setText("重新获取");
					} else {
						Log.i(TAG, "发送成功");
						time.start();
						Toast.makeText(context, "验证码是\n" + verify.getCode(), Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case HTTP_REGISTER:
			onPasswordResponse(response.getJSONResponse(User.class.getSimpleName()), response.getObject(User.class));
			break;
		case HTTP_RESET_PASSWORD:
			//TODO
			break;
		default:
			break;
		}		
	}

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
		case R.id.btnPasswordGetVerify:
			getVerify();
			break;
		case R.id.btnPasswordNext:
			toNextStep();
			break;
		default:
			break;
		}
	}


	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**倒计时类
	 */
	private class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			btnPasswordGetVerify.setText("重新获取");
			btnPasswordGetVerify.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			btnPasswordGetVerify.setClickable(false);
			btnPasswordGetVerify.setText((millisUntilFinished / 1000) +"秒");
		}
	}


	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}