package zuo.biao.apijson.server.sql;

import java.rmi.AccessException;

import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.StringUtil;

/**权限验证类
 * @author Lemon
 */
public class AccessVerifier {
	private static final String TAG = "AccessVerifier: ";

	private static final int ACCESS_LOGIN = 1;
	private static final int ACCESS_PAY = 2;

	public static final String KEY_CURRENT_USER_ID = "currentUserId";
	public static final String KEY_LOGIN_PASSWORD = "loginPassword";
	public static final String KEY_PAY_PASSWORD = "payPassword";

	//	public static final String[] LOGIN_ACCESS_TABLE_NAMES = {"Work", "Comment"};
	public static final String[] PAY_ACCESS_TABLE_NAMES = {"Wallet"};

	/**验证权限是否通过
	 * @param method 
	 * @param request
	 * @param tableName
	 * @return
	 */
	public static boolean verify(RequestMethod method, JSONObject request, String tableName) throws AccessException {
		return verify(method, request, getAccessId(tableName));
	}


	/**验证权限是否通过
	 * @param method 
	 * @param request
	 * @param accessId 可以直接在代码里写ACCESS_LOGIN等，或者建一个Access表，包括id和需要改权限的table的tableName列表
	 * @return
	 * @throws AccessException 
	 */
	public static boolean verify(RequestMethod method, JSONObject request, int accessId) throws AccessException {
		if (accessId < 0 || request == null) {
			return true;
		}
		long currentUserId = request.getLongValue(KEY_CURRENT_USER_ID);
		if (currentUserId <= 0) {
			System.out.println(TAG + "verify accessId = " + accessId
					+ " >>  currentUserId <= 0, currentUserId = " + currentUserId);
			throw new AccessException("请设置"+ KEY_CURRENT_USER_ID + "和对应的password！");
		}
		String password;

		switch (accessId) {
		case ACCESS_LOGIN:
			password = StringUtil.getString(request.getString(KEY_LOGIN_PASSWORD));
			if (password.equals(StringUtil.getString(getLoginPassword(currentUserId))) == false) {
				System.out.println(TAG + "verify accessId = " + accessId
						+ " >> currentUserId or loginPassword error"
						+ "  currentUserId = " + currentUserId + ", loginPassword = " + password);
				throw new AccessException(KEY_CURRENT_USER_ID + "或" + KEY_LOGIN_PASSWORD + "错误！");
			}
		case ACCESS_PAY:
			password = StringUtil.getString(request.getString(KEY_PAY_PASSWORD));
			if (password.equals(StringUtil.getString(getPayPassword(currentUserId))) == false) {
				System.out.println(TAG + "verify accessId = " + accessId
						+ " >> currentUserId or payPassword error"
						+ "  currentUserId = " + currentUserId + ", payPassword = " + password);
				throw new AccessException(KEY_CURRENT_USER_ID + "或" + KEY_PAY_PASSWORD + "错误！");
			}
		default:
			return true;
		}
	}



	/**获取权限id
	 * @param tableName
	 * @return
	 */
	public static int getAccessId(String tableName) {
		if (StringUtil.isNotEmpty(tableName, true) == false) {
			return -1;
		}
		//		for (int i = 0; i < LOGIN_ACCESS_TABLE_NAMES.length; i++) {
		//			if (tableName.equals(LOGIN_ACCESS_TABLE_NAMES[i])) {
		//				return ACCESS_LOGIN;
		//			}
		//		}
		for (int i = 0; i < PAY_ACCESS_TABLE_NAMES.length; i++) {
			if (tableName.equals(PAY_ACCESS_TABLE_NAMES[i])) {
				return ACCESS_PAY;
			}
		}
		return -1;
	}

	/**获取登录密码
	 * @param userId
	 * @return
	 */
	public static String getLoginPassword(long userId) {
		// TODO 查询并返回对应userId的登录密码
		return "123456";//仅测试用
	}

	/**获取支付密码
	 * @param userId
	 * @return
	 */
	public static String getPayPassword(long userId) {
		// TODO 查询并返回对应userId的支付密码
		return "123456";//仅测试用
	}

	/**删除请求里的权限信息
	 * @param requestObject
	 * @return
	 */
	public static JSONObject removeAccessInfo(JSONObject requestObject) {
		if (requestObject != null) {
			requestObject.remove(KEY_CURRENT_USER_ID);
			requestObject.remove(KEY_LOGIN_PASSWORD);
			requestObject.remove(KEY_PAY_PASSWORD);
		}
		return requestObject;
	}

}
