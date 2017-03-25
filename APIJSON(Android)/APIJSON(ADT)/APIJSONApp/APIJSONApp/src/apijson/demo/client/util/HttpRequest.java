/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.client.util;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.apijson.JSONObject;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.manager.HttpManager;
import apijson.demo.client.model.CommentItem;
import apijson.demo.client.server.model.Comment;
import apijson.demo.client.server.model.Login;
import apijson.demo.client.server.model.Moment;
import apijson.demo.client.server.model.User;
import apijson.demo.client.server.model.Verify;
import apijson.demo.client.server.model.Wallet;

/**HTTP请求工具类
 * @author Lemon
 * @use 添加请求方法xxxMethod >> HttpxxxMethod(...)
 * @must 所有请求的url、请求方法(GET, POST等)、请求参数(key-value方式，必要key一定要加，没提供的key不要加，value要符合指定范围)
 *       都要符合后端给的接口文档
 */
public class HttpRequest {
	private static final String TAG = "HttpRequest";

	/**基础URL，这里服务器设置可切换*/
	public static String URL_BASE;
	static {
		URL_BASE = SettingUtil.getCurrentServerAddress();
	}

	public static final String URL_HEAD = URL_BASE + "head/";
	public static final String URL_GET = URL_BASE + "get/";
	public static final String URL_POST = URL_BASE + "post/";
	public static final String URL_POST_HEAD = URL_BASE + "post_head/";
	public static final String URL_POST_GET = URL_BASE + "post_get/";
	public static final String URL_PUT = URL_BASE + "put/";
	public static final String URL_DELETE = URL_BASE + "delete/";

	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 */
	public static void head(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().get(URL_HEAD, request, requestCode, listener);
	}
	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 */
	public static void get(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().get(URL_GET, request, requestCode, listener);
	}
	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void post(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_POST, request, requestCode, listener);
	}
	/**用POST方法HEAD数据，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void postHead(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_POST_HEAD, request, requestCode, listener);
	}
	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void postGet(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_POST_GET, request, requestCode, listener);
	}
	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void put(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_PUT, request, requestCode, listener);
	}
	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void delete(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_DELETE, request, requestCode, listener);
	}



	//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	//user<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String KEY_RANGE = "range";

	public static final String KEY_ID = "id";
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_CURRENT_USER_ID = "currentUserId";

	public static final String KEY_NAME = "name";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_LOGIN_PASSWORD = "loginPassword";
	public static final String KEY_PAY_PASSWORD = "payPassword";
	public static final String KEY_VERIFY = "verify";

	public static final String KEY_SEX = "sex";
	public static final String KEY_TYPE = "type";


	public static final String ID_AT = KEY_ID + "@";
	public static final String USER_ID_AT = KEY_USER_ID + "@";
	public static final String MOMENT_ID_AT = "momentId@";
	public static final String COMMENT_ID_AT = "commentId@";

	public static final String ID_IN = KEY_ID + "{}";
	public static final String USER_ID_IN = KEY_USER_ID + "{}";
	public static final String MOMENT_ID_IN = "momentId{}";
	public static final String COMMENT_ID_IN = "commentId{}";

	public static final String NAME_SEARCH = KEY_NAME + "$";
	public static final String PHONE_SEARCH = KEY_PHONE + "$";
	public static final String CONTENT_SEARCH = "content$";


	//account<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**注册
	 * @param phone
	 * @param password
	 * @param name
	 * @param sex
	 * @param requestCode
	 * @param listener
	 */
	public static void register(String verify, String phone, String password, String name, int sex
			, int requestCode, OnHttpResponseListener listener) {
		JSONObject request = new JSONRequest(new User().setPhone(phone).setName(name).setSex(sex))
		.setTag(User.class.getSimpleName());
		request.put(KEY_VERIFY, verify);
		request.put(KEY_PASSWORD, password);
		HttpManager.getInstance().post(URL_POST + "register/user/", request, requestCode, listener);
	}
	/**重置密码
	 * @param verify
	 * @param phone
	 * @param password
	 * @param requestCode
	 * @param listener
	 */
	public static void setPassword(String verify, String phone, String password
			, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_PUT + "user/password/" + verify + "/" + phone + "/" + password
				, null, requestCode, listener);
	}
	/**
	 * @param phone
	 * @param requestCode
	 * @param listener
	 */
	public static void checkRegister(String phone, int requestCode, OnHttpResponseListener listener) {
		head(new JSONRequest(new User().setPhone(phone)), requestCode, listener);		
	}

	/**登陆
	 * @param phone
	 * @param password
	 * @param requestCode
	 * @param listener
	 */
	public static void login(String phone, String password, int type, int requestCode, OnHttpResponseListener listener) {
		//		JSONRequest request = new JSONRequest(new User().setPhone(phone).setPassword(password));
		//		get(request, requestCode, listener);

		//		JSONRequest request = new JSONRequest(new Password(User.class.getSimpleName(), phone, password));
		//		
		//		JSONRequest user = new JSONRequest();
		//		user.put(ID_AT, "Password/tableId");
		//		request.put(User.class.getSimpleName(), user);

		//post
		////		Login login = new Login().setType(true);
		//		JSONRequest loginObject = new JSONRequest();
		//		loginObject.put(USER_ID_AT, "/Password/tableId");
		//		loginObject.put(KEY_TYPE, true);
		//		request.put(Login.class.getSimpleName(), loginObject);

		//		postGet(request.setTag(Login.class.getSimpleName()), requestCode, listener);

		HttpManager.getInstance().get(URL_GET + "login/" + type + "/" + phone + "/" + password
				, null, requestCode, listener);
	}
	/**退出登录
	 * @param requestCode
	 * @param listener
	 */
	public static void logout(int requestCode, OnHttpResponseListener listener) {
		post(new JSONRequest(new Login(APIJSONApplication.getInstance().getCurrentUserId()).setType(0)
				).setTag(Login.class.getSimpleName()), requestCode, listener);
	}

	/**
	 * @param phone
	 * @param requestCode
	 * @param listener
	 */
	public static void getAuthCode(String phone, int requestCode, OnHttpResponseListener listener) {
		//		get(new JSONRequest(new Verify(phone)), requestCode, listener);
		HttpManager.getInstance().get(URL_POST + "authCode/" + phone, null, requestCode, listener);
	}
	public static void checkAuthCode(String phone, String code, int requestCode, OnHttpResponseListener listener) {
		//		postHead(new JSONRequest(new Verify(phone, code)).setTag(Verify.class.getSimpleName()), requestCode, listener);
		HttpManager.getInstance().get(URL_BASE + "check/authCode/" + phone + "/" + code, null, requestCode, listener);
	}

	//account>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public static final String COLUMNS_USER_SIMPLE = "id,sex,name";
	public static final String COLUMNS_USER = "id,sex,name,head";

	/**获取用户
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void getUser(long id, int requestCode, OnHttpResponseListener listener) {
		get(new JSONRequest(new User(id)), requestCode, listener);
	}
	/**添加联系人
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void setIsFriend(long id, boolean isFriend, int requestCode, OnHttpResponseListener listener) {
		User user = APIJSONApplication.getInstance().getCurrentUser();
		List<Long> list = new ArrayList<Long>();
		list.add(id);
		JSONObject userObject = new JSONObject(new User(user.getId()));
		userObject.put("friendIdList" + (isFriend ? "+" : "-"), list, true);
		put(new JSONRequest(User.class.getSimpleName(), userObject).setTag(User.class.getSimpleName())
				, requestCode, listener);
	}

	public static final int RANGE_ALL = 0;
	public static final int RANGE_SINGLE = 1;
	public static final int RANGE_USER = 2;
	public static final int RANGE_USER_FRIEND = 3;
	public static final int RANGE_USER_CIRCLE = 4;//RANGE_USER + RANGE_USER_FRIEND
	/**获取用户列表
	 * @param range
	 * @param id 
	 * @param search 
	 * @param count
	 * @param page
	 * @param requestCode
	 * @param listener
	 */
	public static void getUserList(int range, long id, com.alibaba.fastjson.JSONObject search
			, int count, int page, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();

		JSONRequest userItem = new JSONRequest();
		switch (range) {
		case RANGE_ALL:
			//do nothing
			break;
		case RANGE_SINGLE:
		case RANGE_USER:
			userItem.put(KEY_ID, id);
			break;
		case RANGE_USER_FRIEND:
		case RANGE_USER_CIRCLE:
			if (APIJSONApplication.getInstance().isCurrentUser(id) == false) {
				Log.e(TAG, "只允许查看当前用户的!");
				return;
			}
			apijson.demo.client.model.User currentUser = APIJSONApplication.getInstance().getCurrentUser();
			List<Long> list = currentUser == null ? null : currentUser.getFriendIdList();
			if (list == null) {//不能放在range == RANGE_USER_CIRCLE里面，为null不会当成查询条件！
				list = new ArrayList<Long>();
			}
			if (range == RANGE_USER_CIRCLE) {
				list.add(currentUser.getId());
			}
			userItem.put(ID_IN, list);
			break;
		default:
			break;
		}
		userItem.add(search);

		JSONRequest listRequest = new JSONRequest(User.class.getSimpleName(), userItem);
		listRequest = listRequest.toArray(count, page, User.class.getSimpleName());
		request.add(listRequest);
		get(request, requestCode, listener);
	}

	/**获取作品
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void getMoment(long id, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();

		//测试远程函数调用成功
		JSONRequest moment = new JSONRequest(KEY_ID, id);
		//		moment.put("praiseCount()", "count(Collection:praiseUserIdList)");//测试成功
		//		moment.put("praised()", "isContain(Collection:praiseUserIdList, userId)");//测试成功
		//		moment.put("plus()", "plus(long:id,long:userId)");//测试成功
		//		moment.put("@commentCount@", "Comment[]/total");
		request.put(Moment.class.getSimpleName(), moment);

		//		request.put(new Moment(id));
		request.put(User.class.getSimpleName(), new JSONRequest(ID_AT, "/Moment/userId"));

		JSONRequest commentItem = new JSONRequest();
		commentItem.put(Comment.class.getSimpleName(), new JSONRequest(MOMENT_ID_AT, "Moment/id"));
		//		commentItem.put("release", "total:Moment/commentCount");
		request.add(commentItem.toArray(3, 0, Comment.class.getSimpleName()));
		//		request.put("commentCount@", "Comment[]/total");
		get(request, requestCode, listener);
	}

	/**获取动态列表
	 * @param range
	 * @param id
	 * @param search 
	 * @param count
	 * @param page
	 * @param requestCode
	 * @param listener
	 */
	public static void getMomentList(int range, long id, com.alibaba.fastjson.JSONObject search
			, int count, int page, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();
		//		request.put("commentCount@", "[]/CommentItem[]/total");

		JSONRequest moment = new JSONRequest();
		switch (range) {
		case RANGE_ALL:
			//do noting
			break;
		case RANGE_SINGLE:
			moment.put(KEY_ID, id);
			break;
		case RANGE_USER:
			moment.put(KEY_USER_ID, id);
			break;
		case RANGE_USER_FRIEND:
		case RANGE_USER_CIRCLE:
			if (APIJSONApplication.getInstance().isCurrentUser(id) == false) {
				Log.e(TAG, "只允许查看当前用户的!");
				return;
			}
			apijson.demo.client.model.User currentUser = APIJSONApplication.getInstance().getCurrentUser();
			List<Long> list = currentUser == null ? null : currentUser.getFriendIdList();
			if (list == null) {
				list = new ArrayList<Long>();
			}
			if (range == RANGE_USER_CIRCLE) {
				list.add(currentUser.getId());
			}
			moment.put(USER_ID_IN, list);
			break;
		default:
			break;
		}
		//		moment.put("@commentCount@", "[]/CommentItem[]/total");
		moment.add(search);
		//测试远程函数调用成功
		//		moment.put("praiseCount()", "count(Collection:praiseUserIdList)");
		//		moment.put("praised()", "isContain(Collection:praiseUserIdList,userId)");

		request.put(Moment.class.getSimpleName(), moment);
		request.put(User.class.getSimpleName(), new JSONRequest(ID_AT, "/Moment/userId").setColumns(COLUMNS_USER));

		//comment <<<<<<<<<<<<<<<<<<
		JSONRequest commentItem = new JSONRequest();
		commentItem.put(Comment.class.getSimpleName(), new JSONRequest(MOMENT_ID_AT, "[]/Moment/id"));
		commentItem.put(User.class.getSimpleName(), new JSONRequest(ID_AT, "/Comment/userId").setColumns(COLUMNS_USER));

		JSONObject toUserObject = new JSONObject();
		toUserObject.put(User.class.getSimpleName()
				, new JSONRequest(ID_AT, "[]/CommentItem[]/Comment/toUserId").setColumns(COLUMNS_USER_SIMPLE));
		commentItem.put("toUserObject", toUserObject);

		//		commentItem.put("release", "total:[]/Moment/commentCount");
		request.add(commentItem.toArray(3, 0, CommentItem.class.getSimpleName()));
		//comment >>>>>>>>>>>>>>>>>>

		get(request.toArray(count, page), requestCode, listener);
	}

	/**获取评论列表
	 * @param momentId
	 * @param count
	 * @param page
	 * @param requestCode
	 * @param listener
	 */
	public static void getCommentList(long momentId, int count, int page
			, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();
		request.put(new Comment().setMomentId(momentId));
		request.put(User.class.getSimpleName(), new JSONRequest(ID_AT, "/Comment/userId").setColumns(COLUMNS_USER));

		JSONObject toUserObject = new JSONObject();
		toUserObject.put(User.class.getSimpleName()
				, new JSONRequest(ID_AT, "[]/Comment/toUserId").setColumns(COLUMNS_USER_SIMPLE));
		request.put("toUserObject", toUserObject);

		get(request.toArray(count, page), requestCode, listener);
	}

	/**赞动态
	 * @param id
	 * @param toPraise
	 * @param requestCode
	 * @param listener
	 */
	public static void praiseMoment(long id, boolean toPraise, int requestCode, OnHttpResponseListener listener) {
		JSONObject data = new JSONObject(new Moment(id));
		List<Long> list = new ArrayList<Long>();
		list.add(APIJSONApplication.getInstance().getCurrentUserId());
		data.put("praiseUserIdList" + (toPraise ? "+" : "-"), list, true);
		put(new JSONRequest(Moment.class.getSimpleName(), data).setTag(Moment.class.getSimpleName())
				, requestCode, listener);
	}

	/**删除动态
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void deleteMoment(Long id, int requestCode, OnHttpResponseListener listener) {
		delete(new JSONRequest(new Moment(id)).setTag(Moment.class.getSimpleName()), requestCode, listener);
	}

	/**获取钱包
	 * @param requestCode
	 * @param listener
	 */
	public static void getWallet(int requestCode, OnHttpResponseListener listener) {
		long userId = APIJSONApplication.getInstance().getCurrentUserId();
		if (userId <= 0) {
			userId = 38710;
		}

		JSONRequest request = new JSONRequest();
		request.put(new Wallet().setUserId(userId));
		request.put(KEY_CURRENT_USER_ID, userId);
		request.put(KEY_LOGIN_PASSWORD, "apijson");
		request.put(KEY_PAY_PASSWORD, "123456");
		postGet(request.setTag(Wallet.class.getSimpleName()), requestCode, listener);
	}







	//user>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}