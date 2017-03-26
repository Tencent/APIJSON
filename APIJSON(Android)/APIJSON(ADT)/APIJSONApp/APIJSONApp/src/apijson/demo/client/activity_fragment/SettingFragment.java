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

import java.io.File;

import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.StringUtil;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.DownloadUtil;
import zuo.biao.library.util.ImageLoaderUtil;
import zuo.biao.library.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import apijson.demo.client.R;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.base.BaseFragment;
import apijson.demo.client.model.Login;
import apijson.demo.client.model.User;
import apijson.demo.client.util.HttpRequest;

/**设置fragment
 * @author Lemon
 * @use new SettingFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class SettingFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener
, OnHttpResponseListener {
	private static final String TAG = "SettingFragment";

	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**创建一个Fragment实例
	 * @return
	 */
	public static SettingFragment createInstance() {
		return new SettingFragment();
	}

	//与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//类相关初始化，必须使用<<<<<<<<<<<<<<<<
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.setting_fragment);
		//类相关初始化，必须使用>>>>>>>>>>>>>>>>

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private ImageView ivSettingHead;
	private TextView tvSettingName;
	@Override
	public void initView() {//必须调用

		ivSettingHead = findViewById(R.id.ivSettingHead);
		tvSettingName = findViewById(R.id.tvSettingName);
	}

	private User user;
	private void setUser(User user_) {
		this.user = user_;
		if (user == null) {
			user = new User();
		}
		runUiThread(new Runnable() {

			@Override
			public void run() {
				if (isLoggedIn) {
					ImageLoaderUtil.loadImage(ivSettingHead, user.getHead(), ImageLoaderUtil.TYPE_ROUND_CORNER);
					tvSettingName.setText(StringUtil.getTrimedString(user.getName()));		
				} else {
					ivSettingHead.setImageResource(R.drawable.ic_launcher);
					tvSettingName.setText("未登录");		
				}
			}
		});
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		super.initData();
		
	}

	@Override
	public void run() {
		//do nothing
		setUser(APIJSONApplication.getInstance().getCurrentUser());
	}

	
	/**下载应用
	 */
	private void downloadApp() {
		showProgress("正在下载...");
		runThread(TAG + "downloadApp", new Runnable() {
			@Override
			public void run() {
				final File file = DownloadUtil.downLoadFile(context, "APIJSON", ".apk"
						, "http://files.cnblogs.com/files/tommylemon/APIJSON%28ADT%29.apk");

				runUiThread(new Runnable() {

					@Override
					public void run() {
						dismissProgress();
						DownloadUtil.openFile(context, file);
					}
				});
			}
		});
	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		ivSettingHead.setOnClickListener(this);

		findViewById(R.id.llSettingInfo).setOnClickListener(this);
		findViewById(R.id.llSettingMoment).setOnClickListener(this);
		findViewById(R.id.llSettingWallet).setOnClickListener(this);

		findViewById(R.id.llSettingSetting).setOnClickListener(this);
		findViewById(R.id.llSettingAbout).setOnClickListener(this);
		findViewById(R.id.llSettingTest).setOnClickListener(this);
		findViewById(R.id.llSettingLogout).setOnClickListener(this);
	}


	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (! isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			HttpRequest.logout(HTTP_LOUOUT, this);
			APIJSONApplication.getInstance().logout();

			toActivity(MainTabActivity.createIntent(context).addFlags(
					Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

			context.finish();
			break;
		default:
			break;
		}
	}


	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
		case R.id.llSettingSetting:
			toActivity(SettingActivity.createIntent(context));
			break;
		case R.id.llSettingAbout:
			toActivity(AboutActivity.createIntent(context));
			break;
		case R.id.llSettingTest:
			downloadApp();
			break;
		default:
			if (verifyLogin() == false) {
				return;
			}
			switch (v.getId()) {
			case R.id.ivSettingHead:
			case R.id.llSettingInfo:
				toActivity(UserActivity.createIntent(context, APIJSONApplication.getInstance().getCurrentUserId()));
				break;
			case R.id.llSettingMoment:
				toActivity(MomentListActivity.createIntent(context, APIJSONApplication.getInstance().getCurrentUserId()));
				break;
			case R.id.llSettingWallet:
				toActivity(WalletActivity.createIntent(context));
				break;
			case R.id.llSettingLogout:
				new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
				break;
			default:
				break;
			}
			break;
		}
	}


	public static final int HTTP_LOUOUT = 2;
	@Override
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		Log.d(TAG, "onHttpResponse  requestCode = " + requestCode + "; resultJson = \n" + resultJson);
		if (e != null) {
			e.printStackTrace();
		}
		switch (requestCode) {
		case HTTP_LOUOUT:
			JSONResponse response = new JSONResponse(resultJson).getJSONResponse(Login.class.getSimpleName());
			boolean succeed = JSONResponse.isSucceed(response);
			Log.d(TAG, succeed ? "服务端退出成功" : "服务端退出失败");
			showShortToast(succeed ? "服务端退出成功" : "服务端退出失败");
			break;
		default:
			break;
		}
	}



	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>









	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}