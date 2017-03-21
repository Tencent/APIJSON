package apijson.demo.client.base;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.base.BaseBroadcastReceiver;
import zuo.biao.library.interfaces.OnResultListener;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import apijson.demo.client.R;
import apijson.demo.client.activity_fragment.LoginActivity;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.model.User;
import apijson.demo.client.util.ActionUtil;
import apijson.demo.client.util.HttpRequest;

public abstract class BaseFragment extends zuo.biao.library.base.BaseFragment implements Runnable {
	private static final String TAG = "BaseFragment";

	
	protected User currentUser;
	protected long currentUserId;
	protected boolean isLoggedIn;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		setCurrentUser();
		
		BaseBroadcastReceiver.register(context, receiver, ActionUtil.ACTION_USER_CHANGED);
		
		return view;
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
		loadAfterCorrect();
	}
	
	/*只有当isCurrentUserCorrect()时才会被调用，如果不符合则会获取currentUser并再次判断来决定是否调用
	 */
	@Override
	public abstract void run();
	
	/**
	 */
	protected void invalidate(User user) {
		currentUser = user;
		loadAfterCorrect();
	}

	/**
	 * @param runnable
	 */
	protected void loadAfterCorrect() {
		if (isCurrentUserCorrect() == false) {
			loadCurrentUser(new OnResultListener<User>() {

				@Override
				public void onResult(User result) {
					if (isCurrentUserCorrect()) {
						run();
					}
				}
			});
			return;
		}

		run();
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

	public static final int HTTP_GET_USER = -1;

	/**
	 * @param listener
	 */
	public void loadCurrentUser(final OnResultListener<User> listener) {
		loadUser(APIJSONApplication.getInstance().getCurrentUserId(), listener);
	}
	/**
	 * @param id
	 * @param listener
	 */
	public void loadUser(long id, final OnResultListener<User> listener) {
		HttpRequest.getUser(id, HTTP_GET_USER, new OnHttpResponseListener() {

			@Override
			public void onHttpResponse(final int requestCode, final String resultJson, final Exception e) {
				final User user = new JSONResponse(resultJson).getObject(User.class);
				if (isUserCorrect(user) == false) {
					showShortToast(R.string.get_failed);
					//					listener.onHttpResponse(requestCode, null, new NullPointerException("user == null"
					//					+ (e == null ? "" : " because " + e.getMessage())));
					listener.onResult(null);
					return;
				}
				if (APIJSONApplication.getInstance().isCurrentUser(user.getId())) {
					APIJSONApplication.getInstance().saveCurrentUser(user);
					currentUser = APIJSONApplication.getInstance().getCurrentUser();
				}

				runUiThread(new Runnable() {

					@Override
					public void run() {
						listener.onResult(user);
					}
				});
			}
		});
	}


	@Override
	public void onDestroy() {
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
					invalidate(JSON.parseObject(intent.getStringExtra(ActionUtil.INTENT_USER), User.class));
				}
			}
		}
	};

}
