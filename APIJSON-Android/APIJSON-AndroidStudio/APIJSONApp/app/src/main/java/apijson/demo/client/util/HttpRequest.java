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
import zuo.biao.apijson.RequestRole;
import zuo.biao.library.manager.HttpManager.OnHttpResponseListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import android.os.Handler;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.manager.HttpManager;
import apijson.demo.client.model.CommentItem;
import apijson.demo.server.model.Comment;
import apijson.demo.server.model.Moment;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;

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
	public static final String URL_HEADS = URL_BASE + "heads/";
	public static final String URL_GETS = URL_BASE + "gets/";
	public static final String URL_PUT = URL_BASE + "put/";
	public static final String URL_DELETE = URL_BASE + "delete/";

	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 */
	public static void head(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_HEAD, request, requestCode, listener);
	}
	/**
	 * @param request
	 * @param requestCode
	 * @param listener
	 */
	public static void get(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_GET, request, requestCode, listener);
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
		HttpManager.getInstance().post(URL_HEADS, request, requestCode, listener);
	}
	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @param requestCode
	 * @param listener
	 * @must request最外层有tag，部分请求还要currentUserId和对应的password
	 */
	public static void postGet(JSONObject request, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_GETS, request, requestCode, listener);
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










	//加 _ 表示class名，避免VERIFY不知道是 Verify 还是 verify 这种冲突
	public static final String USER_;
	public static final String PRIVACY_;
	public static final String MOMENT_;
	public static final String COMMENT_;
	public static final String VERIFY_;
	static {
		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		MOMENT_ = Moment.class.getSimpleName();
		COMMENT_ = Comment.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}


	//user<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String TOTAL = JSONResponse.KEY_TOTAL;

	public static final String RANGE = "range";

	public static final String ID = "id";
	public static final String USER_ID = "userId";

	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String LOGIN_PASSWORD = "loginPassword";
	public static final String PAY_PASSWORD = "payPassword";
	public static final String OLD_PASSWORD = "oldPassword";
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
		JSONObject request = new JSONRequest(new Privacy(phone, password));
		request.puts(new User().setName(name).setSex(sex));
		request.puts(VERIFY, verify);
		HttpManager.getInstance().post(URL_BASE + "register", request, requestCode, listener);
	}

	/**
	 * @param phone
	 * @param requestCode
	 * @param listener
	 */
	public static void checkRegister(String phone, int requestCode, OnHttpResponseListener listener) {
		head(new JSONRequest(new Privacy().setPhone(phone)), requestCode, listener);
	}

	/**登录
	 * @param phone
	 * @param password
	 * @param requestCode
	 * @param listener
	 */
	public static void login(String phone, String password, int type, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();
		request.put(TYPE, type);
		request.put(PHONE, phone);
		request.put(PASSWORD, password);
		HttpManager.getInstance().post(URL_BASE + "login/", request, requestCode, listener);
	}
	/**退出登录
	 * @param requestCode
	 * @param listener
	 */
	public static void logout(int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_BASE + "logout/", new JSONRequest(), requestCode, listener);
		//不能在传到服务器之前销毁session
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				HttpManager.getInstance().saveCookie(null);				
			}
		}, 500);
	}

	/**重置登录密码
	 * @param verify
	 * @param phone
	 * @param password
	 * @param requestCode
	 * @param listener
	 */
	public static void setPassword(String verify, String phone, String password
			, int requestCode, OnHttpResponseListener listener) {
		setPassword(verify, phone, password, Privacy.PASSWORD_TYPE_LOGIN, requestCode, listener);
	}
	/**重置密码
	 * @param verify
	 * @param phone
	 * @param password
	 * @param type
	 * @param requestCode
	 * @param listener
	 */
	public static void setPassword(String verify, String phone, String password, int type
			, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();
		request.put(VERIFY, verify);
		Privacy privacy = new Privacy().setPhone(phone);
		if (type == Privacy.PASSWORD_TYPE_LOGIN) {
			privacy.setPassword(password);
		} else {
			privacy.setPayPassword(password);
		}
		request.put(privacy);

		HttpManager.getInstance().post(URL_BASE + "put/password", request, requestCode, listener);
	}



	/**获取验证码
	 * @param type
	 * @param phone
	 * @param requestCode
	 * @param listener
	 */
	public static void getVerify(int type, String phone, int requestCode, OnHttpResponseListener listener) {
		HttpManager.getInstance().post(URL_BASE + "post/verify/"
				, new JSONRequest(PHONE, phone).puts(TYPE, type).setTag(VERIFY_)
				, requestCode, listener);
	}
	/**校验验证码
	 * @param type
	 * @param phone
	 * @param verify
	 * @param requestCode
	 * @param listener
	 */
	public static void checkVerify(int type, String phone, String verify, int requestCode, OnHttpResponseListener listener) {
		JSONRequest request = new JSONRequest();
		request.put(TYPE, type);
		request.put(PHONE, phone);
		request.put(VERIFY, verify);

		HttpManager.getInstance().post(URL_BASE + "heads/verify"
				, request.setTag(VERIFY_)
				, requestCode, listener);
	}


	/**设置密码
	 * @param type
	 * @param password 只需要phone和verify，不需要old password
	 * @param phone
	 * @param verify
	 * @param requestCode
	 * @param listener
	 */
	public static void setPassword(int type, String password, String phone, String verify
			, int requestCode, OnHttpResponseListener listener) {
		Privacy privacy = new Privacy(phone, password);
		JSONRequest request = new JSONRequest(privacy);
		request.put(VERIFY, verify);

		put(request.setTag(PRIVACY_), requestCode, listener);
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
			request.putsAll(new JSONRequest(MOMENT_
					, new JSONRequest(USER_ID, id).setColumn("pictureList").setOrder(DATE_DOWN))
			.toArray(3, 0, MOMENT_));
		}
		get(request, requestCode, listener);
	}
	/**添加联系人
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void setIsFriend(long id, boolean isFriend, int requestCode, OnHttpResponseListener listener) {
		User user = application.getCurrentUser();
		if (user == null) {
			user = new User();
		}
		List<Long> list = new ArrayList<Long>();
		list.add(id);
		JSONObject userObject = new JSONObject(new User(user.getId()));
		userObject.put("contactIdList" + (isFriend ? "+" : "-"), list);
		put(new JSONRequest(USER_, userObject).setTag(USER_), requestCode, listener);
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
			apijson.demo.client.model.User currentUser = application.getCurrentUser();
			if (currentUser == null) {
				currentUser = new apijson.demo.client.model.User();
			}
			switch (range) {
			case RANGE_ALL://1.首推注册时间长的（也可以是级别高的）；2.给男性用户首推女性用户
				userItem.put("id!", currentUser.getId()); //排除当前用户
				userItem.setOrder(DATE_UP, (currentUser.getSex() == 0 ? "sex-" : ""));
				break;
			case RANGE_SINGLE:
			case RANGE_USER:
				userItem.put(ID, id);
				break;
			case RANGE_USER_FRIEND:
			case RANGE_USER_CIRCLE:
				if (application.isCurrentUser(id) == false) {
					Log.e(TAG, "只允许查看当前用户的!");
					return;
				}
				List<Long> list = currentUser.getContactIdList();
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
				request.put(MOMENT_, moment);
				userItem.put(ID_IN+"@", "Moment/praiseUserIdList");
				break;
			case RANGE_COMMENT:
				JSONObject comment = new JSONObject(new Comment(id));
				comment.setColumn(USER_ID);
				request.put(COMMENT_, comment);
				userItem.put(ID_AT, "Comment/userId");
				break;
			default:
				break;
			}
			userItem.putsAll(search);
		}

		JSONRequest listRequest = new JSONRequest(USER_, userItem);
		listRequest = listRequest.toArray(count, page, USER_);
		request.putsAll(listRequest);
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
		request.put(USER_, new JSONRequest(ID_AT, "/Moment/userId").setColumn(COLUMNS_USER));
		//praise <<<<<<<<<<<<<<<<<<
		JSONRequest userItem = new JSONRequest();
		userItem.put(USER_, new JSONRequest(ID_IN+"@", "Moment/praiseUserIdList")
		.setColumn(COLUMNS_USER_SIMPLE));

		userItem.setQuery(JSONRequest.QUERY_ALL);//同时获取Table和total
		request.putsAll(userItem.toArray(10, 0, USER_));
		request.put("praiseCount@", "/User[]/total");//获取Table的总数total
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
			if (application.isCurrentUser(id) == false) {
				Log.e(TAG, "只允许查看当前用户的!");
				return;
			}
			apijson.demo.client.model.User currentUser = application.getCurrentUser();
			if (currentUser == null) {
				currentUser = new apijson.demo.client.model.User();
			}
			List<Long> list = currentUser.getContactIdList();
			if (list == null) {
				list = new ArrayList<Long>();
			}
			if (range == RANGE_USER_CIRCLE) {
				list.add(currentUser.getId());
			}
			moment.put(USER_ID_IN, list);
			//			moment.setRole(range == RANGE_USER_FRIEND ? RequestRole.CONTACT.name() : RequestRole.CIRCLE.name());
			break;
		default:
			break;
		}
		moment.setOrder(DATE_DOWN);
		moment.putsAll(search);

		request.put(MOMENT_, moment);
		request.put(USER_, new JSONRequest(ID_AT, "/Moment/userId").setColumn(COLUMNS_USER));

		//praise <<<<<<<<<<<<<<<<<<
		JSONRequest userItem = new JSONRequest();
		userItem.put(USER_, new JSONRequest(ID_IN+"@", "[]/Moment/praiseUserIdList")
		.setColumn(COLUMNS_USER_SIMPLE));

		//		userItem.setQuery(JSONRequest.QUERY_ALL);
		request.putsAll(userItem.toArray(10, 0, USER_));
		//		request.put("praiseCount@", "/User[]/total");
		//praise >>>>>>>>>>>>>>>>>>

		//comment <<<<<<<<<<<<<<<<<<
		JSONRequest commentItem = new JSONRequest();
		commentItem.put(COMMENT_, new JSONRequest(MOMENT_ID_AT, "[]/Moment/id").setOrder(DATE_UP));
		commentItem.put(USER_, new JSONRequest(ID_AT, "/Comment/userId")
		.setColumn(COLUMNS_USER_SIMPLE));

		//		commentItem.setQuery(JSONRequest.QUERY_ALL);
		request.putsAll(commentItem.toArray(6, 0, CommentItem.class.getSimpleName()));
		//		request.put("commentCount@", "/CommentItem[]/total");
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
		list.add(application.getCurrentUserId());
		data.puts("praiseUserIdList" + (toPraise ? "+" : "-"), list);

		put(new JSONRequest(MOMENT_, data).setTag(MOMENT_), requestCode, listener);
	}

	/**新增动态
	 * @param content
	 * @param requestCode
	 * @param listener
	 */
	public static void addMoment(String content, int requestCode, OnHttpResponseListener listener) {
		List<String> list = new ArrayList<String>();
		list.add("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000");
		list.add("http://common.cnblogs.com/images/icon_weibo_24.png");
		
		post(new JSONRequest(
				new Moment()
				.setUserId(application.getCurrentUserId())
				.setContent(content)
				.setPictureList(list)
				).setTag(MOMENT_)
				, requestCode, listener);		
	}
	
	/**删除动态
	 * @param id
	 * @param requestCode
	 * @param listener
	 */
	public static void deleteMoment(Long id, int requestCode, OnHttpResponseListener listener) {
		delete(new JSONRequest(new Moment(id)).setTag(MOMENT_), requestCode, listener);
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
		request.put(COMMENT_, comment.setOrder("toId+", DATE_UP));
		request.put(USER_, new JSONRequest(ID_AT, "/Comment/userId").setColumn(COLUMNS_USER));

		//		if (page == 0) {
		//			request.setQuery(JSONRequest.QUERY_ALL);
		//		}
		request = request.toArray(count, page);
		//		if (page == 0) {
		//			request.putPath(TOTAL, "[]", TOTAL);
		//		}

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

		post(new JSONRequest(comment).setTag(COMMENT_), requestCode, listener);
	}
	/**
	 * @param id
	 * @param userId
	 * @param requestCode
	 * @param listener
	 */
	public static void deleteComment(long id, long userId, int requestCode, OnHttpResponseListener listener) {
		delete(new JSONRequest(
				COMMENT_, new JSONObject(
						new Comment(id)
						).setRole(application.isCurrentUser(userId) ? RequestRole.OWNER.name() : RequestRole.ADMIN.name())
				).setTag(COMMENT_)
				, requestCode, listener);
	}

	//Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//Money<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**获取隐私信息
	 * @param requestCode
	 * @param listener
	 */
	public static void getPrivacy(int requestCode, OnHttpResponseListener listener) {
		postGet(new JSONRequest(
				new Privacy(application.getCurrentUserId())
				).setTag(PRIVACY_), requestCode, listener);
	}

	/**修改余额
	 * @param change
	 * @param payPassword
	 * @param requestCode
	 * @param listener
	 */
	public static void changeBalance(double change, String payPassword, int requestCode, OnHttpResponseListener listener) {
		JSONObject privacy = new JSONObject(
				new Privacy(application.getCurrentUserId()).setPayPassword(payPassword)
				);
		privacy.puts("balance+", change);
		JSONRequest request = new JSONRequest(PRIVACY_, privacy);

		HttpManager.getInstance().post(URL_BASE + "put/balance", request.setTag(PRIVACY_), requestCode, listener);
	}


	//Money>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}