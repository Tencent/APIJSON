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

import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.EditTextInfoActivity;
import zuo.biao.library.ui.EditTextInfoWindow;
import zuo.biao.library.util.StringUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import apijson.demo.client.R;
import apijson.demo.client.model.Wallet;
import apijson.demo.client.util.HttpRequest;
import apijson.demo.server.model.Privacy;

/**钱包界面
 * @author Lemon
 * @use toActivity(WalletActivity.createIntent(...));
 */
public class WalletActivity extends BaseActivity implements OnClickListener, OnBottomDragListener
, OnHttpResponseListener {
	private static final String TAG = "WalletActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, WalletActivity.class);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallet_activity, this);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private TextView tvWalletCount;
	@Override
	public void initView() {//必须调用
		autoSetTitle();

		tvWalletCount = (TextView) findViewById(R.id.tvWalletCount);
	}


	private Privacy privacy;
	public void setWallet(Privacy privacy_) {
		this.privacy = privacy_;
		runUiThread(new Runnable() {

			@Override
			public void run() {
				dismissProgressDialog();
				tvBaseTitle.setText(getTitleName());
				if (privacy == null) {
					privacy = new Privacy();
				}
				tvWalletCount.setText(StringUtil.getPrice(privacy.getBalance(), StringUtil.PRICE_FORMAT_PREFIX));
			}
		});
	}

	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用

		showProgressDialog(getTitleName());
		HttpRequest.getPrivacy(HTTP_GET, this);
	}

	public String getTitleName() {
		return isSucceed || isShowingProgress() ? "钱包" : "有点问题，点击重试";
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		findViewById(R.id.tvWalletRecharge).setOnClickListener(this);
		findViewById(R.id.tvWalletWithdraw).setOnClickListener(this);
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}	

		finish();
	}


	private static final int HTTP_GET = 1;
	private static final int HTTP_RECHARGE = 2;
	private static final int HTTP_WITHDRAW = 3;

	private boolean isSucceed = true;
	@Override
	public void onHttpResponse(final int requestCode, final String resultJson, Exception e) {
		runThread(TAG + "onHttpResponse", new Runnable() {

			@Override
			public void run() {

				JSONResponse response = new JSONResponse(resultJson);
				isSucceed = JSONResponse.isSuccess(response);
				
				dismissProgressDialog();
				switch (requestCode) {
				case HTTP_RECHARGE:
				case HTTP_WITHDRAW:
					if (response.getCode() == JSONResponse.CODE_CONDITION_ERROR) {
						showShortToast("密码错误！");
					} else if (requestCode == HTTP_WITHDRAW && response.getCode() == JSONResponse.CODE_OUT_OF_RANGE) {
						showShortToast("余额不足！");
					} else {
						showShortToast((requestCode == HTTP_RECHARGE ? "充值" : "提现")
								+ getString(isSucceed ? R.string.succeed : R.string.failed));
						if (isSucceed) {
							initData();
						}
					}
					break;
				case HTTP_GET:
					if (isSucceed) {
						setWallet(response.getObject(Privacy.class));
					} else {
						showShortToast(R.string.get_failed);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	//系统自带监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvWalletRecharge:
			toActivity(EditTextInfoWindow.createIntent(context, EditTextInfoWindow.TYPE_DECIMAL, "充值", null)
					, REQUEST_RECHARGE, false);
			break;
		case R.id.tvWalletWithdraw:
			toActivity(EditTextInfoWindow.createIntent(context, EditTextInfoWindow.TYPE_DECIMAL, "提现", null)
					, REQUEST_WITHDRAW, false);
			break;
		default:
			break;
		}
	}




	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	private static final int REQUEST_RECHARGE = 1;
	private static final int REQUEST_WITHDRAW = 2;
	private static final int REQUEST_PASSWORD = 3;

	private boolean isRecharge;
	private double change;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_RECHARGE:
		case REQUEST_WITHDRAW:
			if (data == null) {
				break;
			}
			String value = data.getStringExtra(EditTextInfoActivity.RESULT_VALUE);
			try {
				this.change = Double.valueOf(value);
			} catch (Exception e) {
				showShortToast("输入值不是数字！");
			}
			if (change <= 0 || change > 10000) {
				showShortToast("输入值必须为0-10000间的数字！");
			} else {
				startActivityForResult(NumberPasswordActivity.createIntent(context), REQUEST_PASSWORD);
				overridePendingTransition(R.anim.bottom_push_in, R.anim.fade);
				this.isRecharge = requestCode == REQUEST_RECHARGE;
			}
			break;
		case REQUEST_PASSWORD:
			String password = data == null ? null : data.getStringExtra(EditTextInfoActivity.RESULT_PASSWORD);
			password = StringUtil.getString(password);
			if (password.isEmpty() == false) {
				showProgressDialog();
				HttpRequest.changeBalance(isRecharge ? change : -change, password
						, isRecharge ? HTTP_RECHARGE : HTTP_WITHDRAW, this);
			}
			break;
		}

	}


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}