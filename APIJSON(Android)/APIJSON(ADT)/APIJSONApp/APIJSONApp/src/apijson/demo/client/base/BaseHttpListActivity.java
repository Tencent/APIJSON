package apijson.demo.client.base;

import zuo.biao.library.base.BaseBroadcastReceiver;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import apijson.demo.client.R;
import apijson.demo.client.activity_fragment.LoginActivity;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.model.User;
import apijson.demo.client.util.ActionUtil;

public abstract class BaseHttpListActivity<T, BA extends BaseAdapter>
extends zuo.biao.library.base.BaseHttpListActivity<T, BA> implements Runnable {
	private static final String TAG = "BaseHttpListFragment";


	protected User currentUser;
	protected long currentUserId;
	protected boolean isLoggedIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCurrentUser();

		BaseBroadcastReceiver.register(context, receiver, ActionUtil.ACTION_USER_CHANGED);
	}

	private void setCurrentUser() {
		currentUser = APIJSONApplication.getInstance().getCurrentUser();
		currentUserId = currentUser == null ? 0 : currentUser.getId();
		isLoggedIn = isCurrentUserCorrect();		
	}

	protected static boolean isCurrentUser(long userId) {
		return APIJSONApplication.getInstance().isCurrentUser(userId);
	}
	/**未登录会toLoginActivity();
	 * @return isLoggedIn
	 */
	protected boolean verifyLogin() {
		if (isLoggedIn == false) {
			showShortToast("请先登录");
			toLoginActivity();
		}
		return isLoggedIn;
	}
	protected void toLoginActivity() {
		startActivity(LoginActivity.createIntent(context));
		context.overridePendingTransition(R.anim.bottom_push_in, R.anim.hold);
	}

	@Override
	public void initData() {
		super.initData();
		loadAfterCorrect();
	}
	
	/*只有当isCurrentUserCorrect()时才会被调用，如果不符合则会获取currentUser并再次判断来决定是否调用
	 */
	@Override
	public abstract void run();
	
	/**
	 */
	protected void invalidate() {
		setCurrentUser();
		loadAfterCorrect();
	}

	/**
	 * @param runnable
	 */
	protected void loadAfterCorrect() {
		if (isCurrentUserCorrect()) {//请求currentUser都统一交给MainTabActivity，避免同时多次相同请求
			run();
		}
	}


	/**
	 * @return
	 */
	public boolean isCurrentUserCorrect() {
		return isUserCorrect(currentUser);
	}
	/**
	 * @param user
	 * @return
	 */
	public boolean isUserCorrect(User user) {
		return user != null && user.getId() > 0;
	}


	@Override
	protected void onDestroy() {
		BaseBroadcastReceiver.unregister(context, receiver);
		super.onDestroy();
	}

	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent == null ? null : intent.getAction();
			if (isAlive() == false || StringUtil.isNotEmpty(action, true) == false) {
				Log.e(TAG, "receiver.onReceive  isAlive() == false" +
						" || StringUtil.isNotEmpty(action, true) == false >> return;");
				return;
			}

			if (ActionUtil.ACTION_USER_CHANGED.equals(action)) {
				if (isCurrentUser(intent.getLongExtra(INTENT_ID, 0))) {
					invalidate();
				}
			}
		}
	};

}