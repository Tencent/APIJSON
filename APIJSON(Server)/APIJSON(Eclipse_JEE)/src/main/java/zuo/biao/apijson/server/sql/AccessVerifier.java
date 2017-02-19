package zuo.biao.apijson.server.sql;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.POST_GET;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;

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

	public static final String[] LOGIN_ACCESS_TABLE_NAMES = {"Wallet"};
	public static final String[] PAY_ACCESS_TABLE_NAMES = {};


	private static Map<String, RequestMethod[]> accessMap;

	public static void init() {
		accessMap = new HashMap<String, RequestMethod[]>();
		accessMap.put("User", RequestMethod.values());
		accessMap.put("Moment", RequestMethod.values());
		accessMap.put("Comment", RequestMethod.values());
		accessMap.put("Wallet", new RequestMethod[]{POST_GET, POST, PUT, DELETE});
		accessMap.put("Password", new RequestMethod[]{POST_GET, POST, PUT, DELETE});
		accessMap.put("Login", new RequestMethod[]{POST_GET, POST, DELETE});
		accessMap.put("Request", new RequestMethod[]{GET, POST_GET});
	}

	/**验证权限是否通过
	 * @param request
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(JSONObject request, QueryConfig config) throws Exception {
		String table = config == null ? null : config.getTable();
		if (table == null) {
			return true;
		}
		if (request == null) {
			return false;
		}
		RequestMethod method = config.getMethod();

		verifyMethod(table, method);


		long currentUserId = request.getLongValue(KEY_CURRENT_USER_ID);

		switch (config.getMethod()) {
		case GET:
		case POST_GET:
			if ("Wallet".equals(table) || "Password".equals(table)) {
				verifyUserId(currentUserId, config);
			}
			break;
		case POST:
		case PUT:
		case DELETE:
			verifyUserId(currentUserId, config);
			break;
		default:
			break;
		}

		return verifyAccess(request, table, method, currentUserId);
	}

	/**
	 * @param request
	 * @param table 
	 * @param method 
	 * @param currentUserId
	 * @return 
	 * @throws AccessException 
	 */
	private static boolean verifyAccess(JSONObject request, String table, RequestMethod method, long currentUserId) throws AccessException {
		int accessId = getAccessId(method, table);
		if (accessId < 0) {
			return true;
		}
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
			break;
		case ACCESS_PAY:
			password = StringUtil.getString(request.getString(KEY_PAY_PASSWORD));
			if (password.equals(StringUtil.getString(getPayPassword(currentUserId))) == false) {
				System.out.println(TAG + "verify accessId = " + accessId
						+ " >> currentUserId or payPassword error"
						+ "  currentUserId = " + currentUserId + ", payPassword = " + password);
				throw new AccessException(KEY_CURRENT_USER_ID + "或" + KEY_PAY_PASSWORD + "错误！");
			}
			break;
		}
		return true;
	}
	/**
	 * @param currentUserId
	 * @param config
	 * @return 
	 * @throws Exception 
	 */
	private static boolean verifyUserId(long currentUserId, QueryConfig config) throws Exception {
		//		if (currentUserId <= 0 || config == null) {
		//			return true;
		//		}
		//		Map<String, Object> where = config.getWhere();
		//		long userId = 0;
		//		String table = StringUtil.getString(config.getTable());
		//		if (where != null) {
		//			try {
		//				String key = "User".equals(table) ? Table.ID : Table.USER_ID;
		//				userId = (long) where.get(key);
		//			} catch (Exception e) {
		//				// TODO: handle exception
		//			}
		//		}
		//		if (userId != currentUserId) {
		//			throw new IllegalArgumentException(table + "的userId和currentUserId不一致！");
		//		}

		return true;
	}



	/**获取权限id
	 * @param tableName
	 * @return
	 */
	public static int getAccessId(RequestMethod method, String table) {
		if (StringUtil.isNotEmpty(table, true) == false) {
			return -1;
		}
		for (int i = 0; i < LOGIN_ACCESS_TABLE_NAMES.length; i++) {
			if (table.equals(LOGIN_ACCESS_TABLE_NAMES[i])) {
				return ACCESS_LOGIN;
			}
		}
		for (int i = 0; i < PAY_ACCESS_TABLE_NAMES.length; i++) {
			if (table.equals(PAY_ACCESS_TABLE_NAMES[i])) {
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
		return "apijson123";//仅测试用
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


	/**
	 * @param table
	 * @param method
	 * @return
	 * @throws AccessException 
	 */
	public static boolean verifyMethod(String table, RequestMethod method) throws AccessException {
		RequestMethod[] methods = table == null ? null : accessMap.get(table);
		if (methods == null) {
			throw new AccessException(table + "不允许访问！");
		}
		if (method == null) {
			method = GET;
		}
		for (int i = 0; i < methods.length; i++) {
			if (method == methods[i]) {
				return true;
			}
		}

		throw new AccessException(table + "不支持" + method + "方法！");
	}

}
