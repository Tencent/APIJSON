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

import static zuo.biao.apijson.JSONResponse.CODE_TIME_OUT;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.TextClearSuit;
import zuo.biao.library.util.EditTextUtil;
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
import apijson.demo.server.model.Privacy;

/**注册、验证码登录、重置密码等密码相关界面
 * @author Lemon
 */
public class PasswordActivity extends BaseActivity implements OnClickListener, OnHttpResponseListener, OnBottomDragListener {
	public static final String TAG = "PasswordActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	
	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context, int type) {
		return createIntent(context, type, null, null);
	}
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


	/**
	 * @param isGetting
	 */
	private void showVerifyGet(final boolean isGetting) {
		pbPasswordGettingVerify.setVisibility(isGetting ? View.VISIBLE : View.GONE);
		btnPasswordGetVerify.setText(isGetting ? "获取中..." : "重新获取");
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
	 */
	private void getVerify() {
		if (EditTextUtil.isInputedCorrect(context, etPasswordPhone, EditTextUtil.TYPE_PHONE) == false) {
			return;
		}

		if (type == TYPE_VERIFY) {
			etPasswordVerify.requestFocus();
		} else {
			if (StringUtil.isNotEmpty(etPasswordPassword0, true)) {
				etPasswordPassword1.requestFocus();
			} else {
				etPasswordPassword0.requestFocus();
			}
		}

		showVerifyGet(true);
		time = new TimeCount(60000, 1000);

		//判断手机号是否已经注册
		HttpRequest.checkRegister(StringUtil.getTrimedString(etPasswordPhone), HTTP_CHECK_REGISTER, this);
	}

	/**从服务器获取验证码
	 */
	private void getVerifyFromServer() {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				showVerifyGet(true);
			}
		});

		HttpRequest.getVerify(StringUtil.getTrimedString(etPasswordPhone), HTTP_GET_VERIFY, this);
	}

	/**下一步
	 */
	private void toNextStep() {
		if (type != TYPE_VERIFY) {
			if (EditTextUtil.isInputedCorrect(context, etPasswordPassword0, EditTextUtil.TYPE_PASSWORD) == false
					|| EditTextUtil.isInputedCorrect(context, etPasswordPassword1, EditTextUtil.TYPE_PASSWORD) == false) {
				return;
			}

			final String password0 = StringUtil.getTrimedString(etPasswordPassword0);
			String password1 = StringUtil.getTrimedString(etPasswordPassword1);
			if (! password0.equals(password1)) {
				showShortToast("密码输入不一致，请重新输入");
				return;
			}
		}

		switch (type) {
		case TYPE_REGISTER:
			register();
			break;
		default:
			checkVerify(true);
			break;
		}
	}

	/**验证验证码
	 * @param fromServer
	 */
	private boolean checkVerify(boolean fromServer) {
		if (EditTextUtil.isInputedCorrect(context, etPasswordPhone, EditTextUtil.TYPE_PHONE) == false
				|| EditTextUtil.isInputedCorrect(context, etPasswordVerify, EditTextUtil.TYPE_VERIFY) == false) {
			return false;
		}

		if (fromServer) {
			showProgressDialog();
			HttpRequest.checkVerify(StringUtil.getTrimedString(etPasswordPhone),
					StringUtil.getTrimedString(etPasswordVerify), HTTP_CHECK_VERIFY, this);
		}

		return true;
	}


	private void register() {
		if (checkVerify(false) == false) {
			return;
		}
		showProgressDialog();
		HttpRequest.register(StringUtil.getTrimedString(etPasswordVerify)
				, StringUtil.getTrimedString(etPasswordPhone)
				, StringUtil.getString(etPasswordPassword0),
				"APIJSONUser", 0, HTTP_REGISTER, this); // 注册接口
	}

	private void setPassword() {
		showProgressDialog();
		HttpRequest.setPassword(StringUtil.getTrimedString(etPasswordVerify)
				, StringUtil.getTrimedString(etPasswordPhone)
				, StringUtil.getString(etPasswordPassword0),
				HTTP_RESET_PASSWORD, this); // 注册接口
	}


	private Intent newResult() {
		return new Intent()
		.putExtra(RESULT_PHONE, StringUtil.getTrimedString(etPasswordPhone))
		.putExtra(RESULT_VERIFY, StringUtil.getTrimedString(etPasswordVerify))
		.putExtra(RESULT_PASSWORD, StringUtil.getTrimedString(etPasswordPassword0));
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
		final JSONResponse response2;

		dismissProgressDialog();
		switch (requestCode) {
		case HTTP_CHECK_REGISTER:
			response2 = response.getJSONResponse(HttpRequest.PRIVACY_);
			Log.i(TAG, "checkPassword result = " + resultJson);
			runUiThread(new Runnable() {
				@Override
				public void run() {
					showVerifyGet(false);
					if (JSONResponse.isSucceed(response2) == false) {
						showShortToast(R.string.get_failed);
					} else if (JSONResponse.isExist(response2)) {
						if (type == TYPE_REGISTER) {
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
			response2 = response.getJSONResponse(Verify.class.getSimpleName());
			runUiThread(new Runnable() {
				@Override
				public void run() {
					if (JSONResponse.isExist(response2)) {	//验证验证码成功
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
					} else {//验证码错误
						EditTextUtil.showInputedError(context, etPasswordVerify
								, response.getCode() == CODE_TIME_OUT ? "验证码已过期" : "验证码错误");
					}
				}
			});
			break;
		case HTTP_GET_VERIFY:
			final Verify verify = response.getObject(Verify.class);
			runUiThread(new Runnable() {
				@Override
				public void run() {
					showVerifyGet(false);
					if (verify == null || StringUtil.isNotEmpty(verify.getVerify(), true) == false) {
						showShortToast(R.string.get_failed);
					} else {
						Log.i(TAG, "发送成功");
						time.start();
						Toast.makeText(context, "验证码是\n" + verify.getVerify(), Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case HTTP_REGISTER:
			User user = response.getObject(User.class);
			dismissProgressDialog();
			if (user == null || user.getId() <= 0 || JSONResponse.isSucceed(
					response.getJSONResponse(User.class.getSimpleName())) == false) {
				if (response.getCode() == CODE_TIME_OUT || response.getCode() == 412) {
					EditTextUtil.showInputedError(context, etPasswordVerify
							, response.getCode() == CODE_TIME_OUT ? "验证码已过期" : "验证码错误");
				} else {
					showShortToast("注册失败，请检查网络后重试");
				}
			} else {
				showShortToast("注册成功");
				APIJSONApplication.getInstance().saveCurrentUser(user);

				saveAndExit(newResult().putExtra(INTENT_ID, user.getId()));
			}
			break;
		case HTTP_RESET_PASSWORD:
			response2 = response.getJSONResponse(Privacy.class.getSimpleName());
			dismissProgressDialog();
			if (JSONResponse.isSucceed(response2) == false) {
				EditTextUtil.showInputedError(context, etPasswordVerify
						, response.getCode() == CODE_TIME_OUT ? "验证码已过期" : "验证码错误");
			} else {
				showShortToast("修改密码成功，请重新登录");

				saveAndExit(newResult().putExtra(INTENT_PHONE, phone));
			}
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