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

package apijson.demo.server;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.net.URLDecoder;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.model.BaseModel;
import apijson.demo.server.model.Comment;
import apijson.demo.server.model.Moment;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.exception.ConditionErrorException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.exception.OutOfRangeException;

/**request controller
 * <br > 建议全通过HTTP POST来请求:
 * <br > 1.减少代码 - 客户端无需写HTTP GET,PUT等各种方式的请求代码
 * <br > 2.提高性能 - 无需URL encode和decode
 * <br > 3.调试方便 - 建议使用 APIJSON在线测试工具 或 Postman
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {
	private static final String TAG = "Controller";

	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@RequestMapping(value = "get", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String get(@RequestBody String request, HttpSession session) {
		return new Parser(GET).setSession(session).parse(request);
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@RequestMapping(value = "head", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String head(@RequestBody String request, HttpSession session) {
		return new Parser(HEAD).setSession(session).parse(request);
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	@RequestMapping(value = "gets", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String gets(@RequestBody String request, HttpSession session) {
		return new Parser(GETS).setSession(session).parse(request);
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	@RequestMapping(value = "heads", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String heads(@RequestBody String request, HttpSession session) {
		return new Parser(HEADS).setSession(session).parse(request);
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	@RequestMapping(value = "post", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post(@RequestBody String request, HttpSession session) {
		return new Parser(POST).setSession(session).parse(request);
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	@RequestMapping(value = "put", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String put(@RequestBody String request, HttpSession session) {
		return new Parser(PUT).setSession(session).parse(request);
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	@RequestMapping(value = "delete", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String delete(@RequestBody String request, HttpSession session) {
		return new Parser(DELETE).setSession(session).parse(request);
	}

	
	
	
	
	/**获取
	 * 只为兼容HTTP GET请求，推荐用HTTP POST，可删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@RequestMapping("get/{request}")
	public String open_get(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return get(request, session);
	}

	/**计数
	 * 只为兼容HTTP GET请求，推荐用HTTP POST，可删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@RequestMapping("head/{request}")
	public String open_head(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return head(request, session);
	}
	
	
	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>













	public static final String USER_;
	public static final String PRIVACY_;
	public static final String MOMENT_;
	public static final String COMMENT_;
	public static final String VERIFY_; //加下划线后缀是为了避免 Verify 和 verify 都叫VERIFY，分不清
	static {
		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		MOMENT_ = Moment.class.getSimpleName();
		COMMENT_ = Comment.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}

	public static final String COUNT = JSONResponse.KEY_COUNT;
	public static final String TOTAL = JSONResponse.KEY_TOTAL;

	public static final String RANGE = "range";

	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";

	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String _PASSWORD = "_password";
	public static final String _PAY_PASSWORD = "_payPassword";
	public static final String OLD_PASSWORD = "oldPassword";
	public static final String VERIFY = "verify";

	public static final String SEX = "sex";
	public static final String TYPE = "type";
	public static final String WAY = "way";
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




	/**生成验证码,修改为post请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "post/verify", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject postVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		String phone;
		try {
			requestObject = Parser.parseRequest(request, POST);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}

		new Parser(DELETE, true).parse(newVerifyRequest(phone, null));

		JSONObject response = new Parser(POST, true).parseResponse(
				newVerifyRequest(phone, "" + (new Random().nextInt(9999) + 1000)));

		JSONObject verify = null;
		try {
			verify = response.getJSONObject(VERIFY_);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (verify == null || JSONResponse.isSuccess(verify.getIntValue(JSONResponse.KEY_CODE)) == false) {
			new Parser(DELETE, true).parseResponse(new JSONRequest(new Verify(phone)));
			return response;
		}

		//TODO 这里直接返回验证码，方便测试。实际上应该只返回成功信息，验证码通过短信发送
		JSONObject object = new JSONObject();
		object.put(PHONE, phone);
		return getVerify(JSON.toJSONString(object));
	}

	/**获取验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "gets/verify", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject getVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		String phone;
		try {
			requestObject = Parser.parseRequest(request, GETS);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}
		return new Parser(GETS, true).parseResponse(newVerifyRequest(phone, null));
	}

	/**校验验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "heads/verify", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject headVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		String phone;
		String verify;
		try {
			requestObject = Parser.parseRequest(request, HEADS);
			phone = requestObject.getString(PHONE);
			verify = requestObject.getString(VERIFY);
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}
		return headVerify(phone, verify);
	}

	/**校验验证码
	 * @param phone
	 * @param vfy
	 * @return
	 */
	public JSONObject headVerify(String phone, String vfy) {
		JSONResponse response = new JSONResponse(
				new Parser(GETS, true).parseResponse(
						new JSONRequest(new Verify(phone)).setTag(VERIFY_)
						)
				);
		Verify verify = response.getObject(Verify.class);
		if (verify == null) {
			return Parser.newErrorResult(new NullPointerException("验证码为空！"));
		}

		//验证码过期
		if (System.currentTimeMillis() > (60000 + BaseModel.getTimeMillis(verify.getDate()))) {
			new Parser(DELETE, true).parseResponse(
					new JSONRequest(new Verify(phone)).setTag(VERIFY_)
					);
			return Parser.newErrorResult(new TimeoutException("验证码已过期！"));
		}

		return new JSONResponse(
				new Parser(HEADS, true).parseResponse(
						new JSONRequest(new Verify(phone, vfy))
						)
				);
	}


	/**新建一个验证码请求
	 * @param phone
	 * @param verify
	 * @return
	 */
	private JSONObject newVerifyRequest(String phone, String verify) {
		return new JSONRequest(new Verify(phone, verify)).setTag(VERIFY_);
	}


	public static final int LOGIN_TYPE_PASSWORD = 0;//密码登录
	public static final int LOGIN_TYPE_VERIFY = 1;//验证码登录
	/**用户登录
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
		{
			"type": 0,
			"phone": "13000082001",
			"password": "1234567"
		}
	 * </pre>
	 */
	@RequestMapping(value = "login", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject login(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		boolean isPassword;
		String phone;
		String password;
		try {
			requestObject = Parser.parseRequest(request, POST);
			isPassword = requestObject.getIntValue(TYPE) == LOGIN_TYPE_PASSWORD;//登录方式
			phone = requestObject.getString(PHONE);//手机
			password = requestObject.getString(PASSWORD);//密码

			if (StringUtil.isPhone(phone) == false) {
				throw new IllegalArgumentException("手机号不合法！");
			}

			if (isPassword) {
				if (StringUtil.isPassword(password) == false) {
					throw new IllegalArgumentException("密码不合法！");
				}
			} else {
				if (StringUtil.isVerify(password) == false) {
					throw new IllegalArgumentException("验证码不合法！");
				}
			}
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}



		//手机号是否已注册
		JSONObject phoneResponse = new Parser(HEADS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		JSONResponse response = new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		if(JSONResponse.isExist(response) == false) {
			return Parser.newErrorResult(new NotExistException("手机号未注册"));
		}

		//根据phone获取User
		JSONObject privacyResponse = new Parser(GETS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		response = new JSONResponse(privacyResponse);

		Privacy privacy = response == null ? null : response.getObject(Privacy.class);
		long userId = privacy == null ? 0 : BaseModel.value(privacy.getId());
		if (userId <= 0) {
			return privacyResponse;
		}

		//校验凭证 
		if (isPassword) {//password密码登录
			response = new JSONResponse(
					new Parser(HEADS, true).parseResponse(
							new JSONRequest(new Privacy(userId).setPassword(password))
							)
					);
		} else {//verify手机验证码登录
			response = new JSONResponse(headVerify(phone, password));
		}
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		response = response.getJSONResponse(isPassword ? PRIVACY_ : VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			return Parser.newErrorResult(new ConditionErrorException("账号或密码错误"));
		}

		response = new JSONResponse(
				new Parser(GETS, true).parseResponse(
						new JSONRequest(new User(userId))
						)
				);
		User user = response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) != userId) {
			return Parser.newErrorResult(new NullPointerException("服务器内部错误"));
		}

		//登录状态保存至session
		session.setAttribute(USER_ID, userId);//用户id
		session.setAttribute(TYPE, isPassword ? LOGIN_TYPE_PASSWORD : LOGIN_TYPE_VERIFY);//登录方式
		session.setAttribute(USER_, user);//用户
		session.setAttribute(PRIVACY_, privacy);//用户隐私信息
		//		session.setMaxInactiveInterval(1*60);//设置session过期时间

		return response;
	}

	/**退出登录，清空session
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "logout", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject logout(HttpSession session) {
		long userId;
		try {
			userId = Verifier.getUserId(session);//必须在session.invalidate();前！
			Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
			session.invalidate();
		} catch (Exception e) {
			return Parser.newErrorResult(e);
		}

		JSONObject result = Parser.newSuccessResult();
		JSONObject user = Parser.newSuccessResult();
		user.put(ID, userId);
		user.put(COUNT, 1);
		result.put(USER_, user);

		return result;
	}


	/**注册
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
		{
			"Privacy": {
				"phone": "13000082222",
				"_password": "12345678"
			},
			"User": {
				"name": "APIJSONUser",
				"sex": 0
			},
			"verify": "2139"
		}
	 * </pre>
	 */
	@RequestMapping(value = "register", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject register(@RequestBody String request) {
		JSONObject requestObject = null;
		String verify;

		JSONObject privacyObj;
		String phone;
		String password;

		JSONObject userObj;
		String name;
		try {
			requestObject = Parser.parseRequest(request, POST);

			verify = requestObject.getString(VERIFY);
			requestObject.remove(VERIFY);

			privacyObj = requestObject.getJSONObject(PRIVACY_);
			if (privacyObj == null) {
				throw new NullPointerException("请设置 "  + PRIVACY_);
			}
			requestObject.remove(PRIVACY_);

			phone = privacyObj.getString(PHONE);
			password = privacyObj.getString(_PASSWORD);

			userObj = requestObject.getJSONObject(USER_);
			if (userObj == null) {
				throw new NullPointerException("请设置 "  + USER_);
			}
			name = userObj.getString(NAME);


			if (StringUtil.isVerify(verify) == false) {
				throw new IllegalArgumentException(VERIFY + ":value 中value不合法！");
			}
			if (StringUtil.isPhone(phone) == false) {
				throw new IllegalArgumentException(PHONE + ":value 中value不合法！");
			}
			if (StringUtil.isPassword(password) == false) {
				throw new IllegalArgumentException(_PASSWORD + ":value 中value不合法！");
			}
			if (StringUtil.isEmpty(name, true)) {
				throw new IllegalArgumentException(NAME + ":value 中value不合法！");
			}
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}

		//验证码是否正确
		JSONResponse response = new JSONResponse(headVerify(phone, verify));
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}

		//手机号或验证码错误
		if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
			return Parser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！"));
		}

		//验证手机号是否已经注册
		JSONObject check = new Parser(HEADS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		JSONObject checkUser = check == null ? null : check.getJSONObject(PRIVACY_);
		if (checkUser == null || checkUser.getIntValue(JSONResponse.KEY_COUNT) > 0) {
			return Parser.newErrorResult(new ConflictException("手机号" + phone + "已经注册"));
		}

		//生成User
		response = new JSONResponse(new Parser(POST, true).parseResponse(requestObject));

		JSONResponse userRes = response.getJSONResponse(USER_);
		long userId = userRes == null ? 0 : userRes.getId();
		if (userId <= 0) {
			return response;
		}

		//生成Privacy
		JSONResponse response2 = new JSONResponse(
				new Parser(POST, true).parseResponse(
						new JSONRequest(
								new Privacy(userId).setPhone(phone).setPassword(password)
								)
						)
				);
		if (JSONResponse.isSuccess(response2.getJSONResponse(PRIVACY_)) == false) {//创建失败，删除新增的无效User和Privacy

			new Parser(DELETE, true).parseResponse(
					new JSONRequest(
							new User(userId)
							)
					);

			new Parser(DELETE, true).parseResponse(
					new JSONRequest(
							new Privacy(userId)
							)
					);

			return Parser.extendErrorResult(requestObject, new Exception("服务器内部错误"));
		}

		response.putAll(response2);
		return response;
	}


	/**设置密码
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
		{
			"type": 0,
			"password": "1234567",
			"phone": "13000082001",
			"verify": "1234"
		}
	 * </pre>
	 */
	@RequestMapping(value = "put/password", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject putPassword(@RequestBody String request){
		JSONObject requestObject = null;
		boolean isLogin;
		String verify;
		String phone;
		String password;
		try {
			requestObject = Parser.parseRequest(request, PUT);

			isLogin = requestObject.getIntValue(TYPE) == Privacy.PASSWORD_TYPE_LOGIN;
			verify = requestObject.getString(VERIFY);
			phone = requestObject.getString(PHONE);
			password = requestObject.getString(PASSWORD);

			if (StringUtil.isVerify(verify) == false) {
				throw new IllegalArgumentException(VERIFY + ":value 中value不合法！");
			}
			if (StringUtil.isPhone(phone) == false) {
				throw new IllegalArgumentException(PHONE + ":value 中value不合法！");
			}
			if (isLogin) {
				if (StringUtil.isPassword(password) == false) {
					throw new IllegalArgumentException(PASSWORD + ":value 中value不合法！");
				}
			} else {
				if (StringUtil.isNumberPassword(password) == false) {
					throw new IllegalArgumentException(PASSWORD + ":value 中value不合法！");
				}
			}
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}


		//校验验证码
		JSONResponse response = new JSONResponse(headVerify(phone, verify));
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		//手机号或验证码错误
		if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
			return Parser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！"));
		}

		response = new JSONResponse(
				new Parser(GET, true).parseResponse(
						new JSONRequest(
								new Privacy().setPhone(phone)
								)
						)
				);
		Privacy privacy = response.getObject(Privacy.class);
		long userId = privacy == null ? 0 : privacy.getId();
		if (userId <= 0) {
			return Parser.extendErrorResult(requestObject, new NotExistException("手机号未注册！"));
		}

		//修改密码
		return new Parser(PUT, true).parseResponse(
				new JSONRequest(
						PRIVACY_, new zuo.biao.apijson.JSONObject(
								new Privacy(userId)
								).puts(isLogin ? _PASSWORD : _PAY_PASSWORD, password)
						)
				);
	}



	/**充值/提现
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see
	 * <pre>
		{
			"Privacy": {
				"id": 82001,
				"balance+": 100,
				"_payPassword": "123456"
			}
		}
	 * </pre>
	 */
	@RequestMapping(value = "put/balance", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public JSONObject putBalance(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		JSONObject privacyObj;
		long userId;
		String payPassword;
		double change;
		try {
			Verifier.verifyLogin(session);
			requestObject = Parser.getCorrectRequest(PUT, Parser.parseRequest(request, PUT));

			privacyObj = requestObject.getJSONObject(PRIVACY_);
			if (privacyObj == null) {
				throw new NullPointerException("请设置 " + PRIVACY_ + "！");
			}
			userId = privacyObj.getLongValue(ID);
			payPassword = privacyObj.getString(_PAY_PASSWORD);
			change = privacyObj.getDoubleValue("balance+");

			if (userId <= 0) {
				throw new IllegalArgumentException(PRIVACY_ + "." + ID + ":value 中value不合法！");
			}
			if (StringUtil.isPassword(payPassword) == false) {
				throw new IllegalArgumentException(PRIVACY_ + "." + _PAY_PASSWORD + ":value 中value不合法！");
			}
		} catch (Exception e) {
			return Parser.extendErrorResult(requestObject, e);
		}

		//验证密码<<<<<<<<<<<<<<<<<<<<<<<

		privacyObj.remove("balance+");
		JSONResponse response = new JSONResponse(
				new Parser(HEADS, true).setSession(session).parseResponse(
						new JSONRequest(PRIVACY_, privacyObj)
						)
				);
		response = response.getJSONResponse(PRIVACY_);
		if (JSONResponse.isExist(response) == false) {
			return Parser.extendErrorResult(requestObject, new ConditionErrorException("支付密码错误！"));
		}

		//验证密码>>>>>>>>>>>>>>>>>>>>>>>>


		//验证金额范围<<<<<<<<<<<<<<<<<<<<<<<

		if (change == 0) {
			return Parser.extendErrorResult(requestObject, new OutOfRangeException("balance+的值不能为0！"));
		}
		if (Math.abs(change) > 10000) {
			return Parser.extendErrorResult(requestObject, new OutOfRangeException("单次 充值/提现 的金额不能超过10000元！"));
		}

		//验证金额范围>>>>>>>>>>>>>>>>>>>>>>>>

		if (change < 0) {//提现
			response = new JSONResponse(
					new Parser(GETS, true).parseResponse(
							new JSONRequest(
									new Privacy(userId)
									)
							)
					);
			Privacy privacy = response == null ? null : response.getObject(Privacy.class);
			long id = privacy == null ? 0 : BaseModel.value(privacy.getId());
			if (id != userId) {
				return Parser.extendErrorResult(requestObject, new Exception("服务器内部错误！"));
			}

			if (BaseModel.value(privacy.getBalance()) < -change) {
				return Parser.extendErrorResult(requestObject, new OutOfRangeException("余额不足！"));
			}
		}


		privacyObj.remove(_PAY_PASSWORD);
		privacyObj.put("balance+", change);
		requestObject.put(PRIVACY_, privacyObj);
		requestObject.put(JSONRequest.KEY_TAG, PRIVACY_);
		//不免验证，里面会验证身份
		return new Parser(PUT).setSession(session).parseResponse(requestObject);
	}


}
