/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson.server.sql;

import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;
import zuo.biao.apijson.server.model.Comment;
import zuo.biao.apijson.server.model.Login;
import zuo.biao.apijson.server.model.Moment;
import zuo.biao.apijson.server.model.Password;
import zuo.biao.apijson.server.model.Request;
import zuo.biao.apijson.server.model.User;
import zuo.biao.apijson.server.model.Verify;
import zuo.biao.apijson.server.model.Wallet;
import zuo.biao.apijson.server.model.Work;

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
	static {
		accessMap = new HashMap<String, RequestMethod[]>();
		
		//与客户端更好地统一
		accessMap.put(User.class.getSimpleName(), User.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Work.class.getSimpleName(), Work.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Moment.class.getSimpleName(), Moment.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Comment.class.getSimpleName(), Comment.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Wallet.class.getSimpleName(), Wallet.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Password.class.getSimpleName(), Password.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Verify.class.getSimpleName(), Verify.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Login.class.getSimpleName(), Login.class.getAnnotation(APIJSONRequest.class).method());
		accessMap.put(Request.class.getSimpleName(), Request.class.getAnnotation(APIJSONRequest.class).method());

		//原来的做法
		//		accessMap.put("User", RequestMethod.values());
		//		accessMap.put("Work", RequestMethod.values());
		//		accessMap.put("Moment", RequestMethod.values());
		//		accessMap.put("Comment", RequestMethod.values());
		//		accessMap.put("Wallet", new RequestMethod[]{POST_GET, POST, PUT, DELETE});
		//		accessMap.put("Password", new RequestMethod[]{POST_GET, POST, PUT, DELETE});
		//		accessMap.put("Login", new RequestMethod[]{POST_GET, POST, DELETE});
		//		accessMap.put("Request", new RequestMethod[]{GET, POST_GET});
		//		accessMap.put("Verify", new RequestMethod[]{POST_HEAD, POST_GET, POST, DELETE});
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
		return "apijson";//仅测试用
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
		if (methods == null || methods.length <= 0) {
			throw new AccessException(table + "不允许访问！");
		}
		if (method == null) {
			method = RequestMethod.GET;
		}
		for (int i = 0; i < methods.length; i++) {
			if (method == methods[i]) {
				return true;
			}
		}

		throw new AccessException(table + "不支持" + method + "方法！");
	}


}
