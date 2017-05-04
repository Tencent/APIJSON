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

package apijson.demo.client.util;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.apijson.JSONObject;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.manager.HttpManager;
import apijson.demo.client.model.CommentItem;
import apijson.demo.server.model.Comment;
import apijson.demo.server.model.Login;
import apijson.demo.server.model.Moment;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Wallet;

/**HTTP请求工具类
 * @author Lemon
 * @use 添加请求方法xxxMethod >> HttpRequest.xxxMethod(...)
 * @must 所有请求的url、请求方法(GET, POST等)、请求参数(key-value方式，必要key一定要加，没提供的key不要加，value要符合指定范围)
 *       都要符合后端给的接口文档
 */
public class HttpRequest {
	private static final String TAG = "HttpRequest";

	private static APIJSONApplication application;
	/**基础URL，这里服务器设置可切换*/
	public static String URL_BASE;
	static {
		application = APIJSONApplication.getInstance();
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







	
	

	public static final String USER;
	public static final String MOMENT;
	public static final String COMMENT;
	static {
		USER = User.class.getSimpleName();
		MOMENT = Moment.class.getSimpleName();
		COMMENT = Comment.class.getSimpleName();
	}


	//user<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String TOTAL = JSONResponse.KEY_TOTAL;

	public static final String RANGE = "range";

	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";

	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String LOGIN_PASSWORD = "loginPassword";
	public static final String PAY_PASSWORD = "payPassword";
	public static final String VERIFY = "verify";

	public static final String SEX = "sex";
	public static final String TYPE = "type";
	public static final String CONTENT = "content";




	public static final String DATE_UP = "date+";//同 "date ASC"
	public static final String DATE_DOWN = "date-";//同 "date DESC"

	public static final String ID_AT = ID + "@";
	public static final String USER_ID_AT = USER_ID + "@";
	public static final String MOMENT_ID_AT = "momentId@";
	public static final String COMMENT_ID_AT = "commentId@";

	public static final String ID_IN = ID + "{}";
	public static final String USER_ID_IN = USER_ID + "{}";
	public static final String MOMENT_ID_IN = "momentId{}";
	public static final String COMMENT_ID_IN = "commentId{}";

	public static final String NAME_SEARCH = NAME + "$";
	public static final String PHONE_SEARCH = PHONE + "$";
	public static final String CONTENT_SEARCH = CONTENT + "$";



	public static final String COLUMNS_USER_SIMPLE = "id,name";
	public static final String COLUMNS_USER = "id,sex,name,head";


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
		.setTag(USER);
		request.put(VERIFY, verify);
		request.put(PASSWORD, password);
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



	//User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**获取用户
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void getUser(long id, int requestCode, OnHttpResponseListener listener) {
		getUser(id, false, requestCode, listener);
	}
	/**获取用户
	 * @param id
	 * @param withMomentList
	 * @param requestCode
	 * @param listener
	 */
	public static void getUser(long id, boolean withMomentList, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest(new User(id));
		if (withMomentList) {
			request.add(new JSONRequest(MOMENT
					, new JSONRequest(USER_ID, id).setColumn("pictureList").setOrder(DATE_DOWN))
			.toArray(3, 0, MOMENT));
		}
		get(request, requestCode, listener);
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
		put(new JSONRequest(USER, userObject).setTag(USER), requestCode, listener);
	}

	public static final int RANGE_ALL = 0;
	public static final int RANGE_SINGLE = 1;
	public static final int RANGE_USER = 2;
	public static final int RANGE_USER_FRIEND = 3;
	public static final int RANGE_USER_CIRCLE = 4;//RANGE_USER + RANGE_USER_FRIEND
	public static final int RANGE_MOMENT = 5;
	public static final int RANGE_COMMENT = 6;
	/**获取用户列表
	 * @param range
	 * @param id
	 * @param search
	 * @param idList
	 * @param count
	 * @param page
	 * @param requestCode
	 * @param listener
	 */
	public static void getUserList(int range, long id, com.alibaba.fastjson.JSONObject search, List<Long> idList
			, int count, int page, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();

		JSONRequest userItem = new JSONRequest();
		if (idList != null) {
			userItem.put(ID_IN, idList);
		} else {
			apijson.demo.client.model.User currentUser = APIJSONApplication.getInstance().getCurrentUser();
			if (currentUser == null) {
				currentUser = new apijson.demo.client.model.User();
			}
			switch (range) {
			case RANGE_ALL://1.首推注册时间长的（也可以是级别高的）；2.给男性用户首推女性用户
				userItem.setOrder(DATE_UP, (currentUser.getSex() == 0 ? "sex-" : ""));
				break;
			case RANGE_SINGLE:
			case RANGE_USER:
				userItem.put(ID, id);
				break;
			case RANGE_USER_FRIEND:
			case RANGE_USER_CIRCLE:
				if (APIJSONApplication.getInstance().isCurrentUser(id) == false) {
					Log.e(TAG, "只允许查看当前用户的!");
					return;
				}
				List<Long> list = currentUser.getFriendIdList();
				if (list == null) {//不能放在range == RANGE_USER_CIRCLE里面，为null不会当成查询条件！
					list = new ArrayList<Long>();
				}
				if (range == RANGE_USER_CIRCLE) {
					list.add(currentUser.getId());
				} else {//问题可能在于登录状态错误
					list.remove(currentUser.getId());//避免误添加
				}
				userItem.put(ID_IN, list);
				userItem.setOrder("name+");
				break;
			case RANGE_MOMENT:
				JSONObject moment = new JSONObject(new Moment(id));
				moment.setColumn("praiseUserIdList");
				request.put(MOMENT, moment);
				userItem.put(ID_IN+"@", "Moment/praiseUserIdList");
				break;
			case RANGE_COMMENT:
				JSONObject comment = new JSONObject(new Comment(id));
				comment.setColumn(USER_ID);
				request.put(COMMENT, comment);
				userItem.put(ID_AT, "Comment/userId");
				break;
			default:
				break;
			}
			userItem.add(search);
		}

		JSONRequest listRequest = new JSONRequest(USER, userItem);
		listRequest = listRequest.toArray(count, page, USER);
		request.add(listRequest);
		get(request, requestCode, listener);
	}

	//User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	//Moment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**获取作品
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void getMoment(long id, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest(new Moment(id));
		request.put(USER, new JSONRequest(ID_AT, "/Moment/userId").setColumn(COLUMNS_USER));
		//praise <<<<<<<<<<<<<<<<<<
		JSONRequest userItem = new JSONRequest();
		userItem.put(USER, new JSONRequest(ID_IN+"@", "Moment/praiseUserIdList")
		.setColumn(COLUMNS_USER_SIMPLE));

		userItem.setQuery(JSONRequest.QUERY_ALL);
		request.add(userItem.toArray(10, 0, USER));
		request.put("praiseCount@", "/User[]/total");
		//praise >>>>>>>>>>>>>>>>>>

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

		JSONRequest moment = new JSONRequest();
		switch (range) {
		case RANGE_ALL:
			//do noting
			break;
		case RANGE_SINGLE:
			moment.put(ID, id);
			break;
		case RANGE_USER:
			moment.put(USER_ID, id);
			break;
		case RANGE_USER_FRIEND:
		case RANGE_USER_CIRCLE:
			if (APIJSONApplication.getInstance().isCurrentUser(id) == false) {
				Log.e(TAG, "只允许查看当前用户的!");
				return;
			}
			apijson.demo.client.model.User currentUser = APIJSONApplication.getInstance().getCurrentUser();
			if (currentUser == null) {
				currentUser = new apijson.demo.client.model.User();
			}
			List<Long> list = currentUser.getFriendIdList();
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
		moment.setOrder(DATE_DOWN);
		moment.add(search);

		request.put(MOMENT, moment);
		request.put(USER, new JSONRequest(ID_AT, "/Moment/userId").setColumn(COLUMNS_USER));

		//praise <<<<<<<<<<<<<<<<<<
		JSONRequest userItem = new JSONRequest();
		userItem.put(USER, new JSONRequest(ID_IN+"@", "[]/Moment/praiseUserIdList")
		.setColumn(COLUMNS_USER_SIMPLE));

		userItem.setQuery(JSONRequest.QUERY_ALL);
		request.add(userItem.toArray(10, 0, USER));
		request.put("praiseCount@", "/User[]/total");
		//praise >>>>>>>>>>>>>>>>>>

		//comment <<<<<<<<<<<<<<<<<<
		JSONRequest commentItem = new JSONRequest();
		commentItem.put(COMMENT, new JSONRequest(MOMENT_ID_AT, "[]/Moment/id").setOrder(DATE_UP));
		commentItem.put(USER, new JSONRequest(ID_AT, "/Comment/userId")
		.setColumn(COLUMNS_USER_SIMPLE));

		commentItem.setQuery(JSONRequest.QUERY_ALL);
		request.add(commentItem.toArray(6, 0, CommentItem.class.getSimpleName()));
		request.put("commentCount@", "/CommentItem[]/total");
		//comment >>>>>>>>>>>>>>>>>>

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
		put(new JSONRequest(MOMENT, data).setTag(MOMENT), requestCode, listener);
	}

	/**删除动态
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void deleteMoment(Long id, int requestCode, OnHttpResponseListener listener) {
		delete(new JSONRequest(new Moment(id)).setTag(MOMENT), requestCode, listener);
	}

	//Moment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	//Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

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
		JSONObject comment = new JSONObject(new Comment().setMomentId(momentId));
		request.put(COMMENT, comment.setOrder(DATE_UP));
		request.put(USER, new JSONRequest(ID_AT, "/Comment/userId").setColumn(COLUMNS_USER));

		if (page == 0) {
			request.setQuery(JSONRequest.QUERY_ALL);
		}
		request = request.toArray(count, page);
		if (page == 0) {
			request.putPath(TOTAL, "[]", TOTAL);
		}

		get(request, requestCode, listener);
	}

	/**
	 * @param momentId
	 * @param toCommentId
	 * @param toUserId 不能省，可能同一toCommentId下回复不同的人
	 * @param content
	 * @param requestCode
	 * @param listener
	 */
	public static void addComment(long momentId, long toCommentId, long toUserId, String content
			, int requestCode, OnHttpResponseListener listener) {
		Comment comment = new Comment()
		.setToId(toCommentId)
		.setUserId(application.getCurrentUserId())
		.setMomentId(momentId)
		.setContent(content);
		post(new JSONRequest(comment).setTag(COMMENT), requestCode, listener);
	}
	/**
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void deleteComment(long id, int requestCode, OnHttpResponseListener listener) {
		delete(new JSONRequest(new Comment(id)).setTag(COMMENT), requestCode, listener);
	}

	//Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>





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
		request.put(CURRENT_USER_ID, userId);
		request.put(LOGIN_PASSWORD, "apijson");
		request.put(PAY_PASSWORD, "123456");
		postGet(request.setTag(Wallet.class.getSimpleName()), requestCode, listener);
	}






	//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}