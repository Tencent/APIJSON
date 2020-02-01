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
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.net.URLDecoder;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.model.BaseModel;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.exception.ConditionErrorException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.exception.OutOfRangeException;
import zuo.biao.apijson.server.model.Access;
import zuo.biao.apijson.server.model.Function;
import zuo.biao.apijson.server.model.Request;


/**request controller
 * <br > 建议全通过HTTP POST来请求:
 * <br > 1.减少代码 - 客户端无需写HTTP GET,PUT等各种方式的请求代码
 * <br > 2.提高性能 - 无需URL encode和decode
 * <br > 3.调试方便 - 建议使用 APIJSON在线测试工具 或 Postman
 * @author Lemon
 */
@Service
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
	@PostMapping(value = "get")
	public String get(@RequestBody String request, HttpSession session) {
		return new DemoParser(GET).setSession(session).parse(request);
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@PostMapping("head")
	public String head(@RequestBody String request, HttpSession session) {
		return new DemoParser(HEAD).setSession(session).parse(request);
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	@PostMapping("gets")
	public String gets(@RequestBody String request, HttpSession session) {
		return new DemoParser(GETS).setSession(session).parse(request);
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	@PostMapping("heads")
	public String heads(@RequestBody String request, HttpSession session) {
		return new DemoParser(HEADS).setSession(session).parse(request);
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	@PostMapping("post")
	public String post(@RequestBody String request, HttpSession session) {
		return new DemoParser(POST).setSession(session).parse(request);
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	@PostMapping("put")
	public String put(@RequestBody String request, HttpSession session) {
		return new DemoParser(PUT).setSession(session).parse(request);
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	@PostMapping("delete")
	public String delete(@RequestBody String request, HttpSession session) {
		return new DemoParser(DELETE).setSession(session).parse(request);
	}





	/**获取
	 * 只为兼容HTTP GET请求，推荐用HTTP POST，可删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@RequestMapping("get/{request}")
	public String openGet(@PathVariable String request, HttpSession session) {
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
	public String openHead(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return head(request, session);
	}


	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>









	public static final String FUNCTION_;
	public static final String REQUEST_;
	public static final String ACCESS_;

	public static final String USER_;
	public static final String PRIVACY_;
	public static final String VERIFY_; //加下划线后缀是为了避免 Verify 和 verify 都叫VERIFY，分不清
	static {
		FUNCTION_ = Function.class.getSimpleName();
		REQUEST_ = Request.class.getSimpleName();
		ACCESS_ = Access.class.getSimpleName();

		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}

	public static final String VERSION = JSONRequest.KEY_VERSION;
	public static final String FORMAT = JSONRequest.KEY_FORMAT;
	public static final String COUNT = JSONResponse.KEY_COUNT;
	public static final String TOTAL = JSONResponse.KEY_TOTAL;

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

	public static final String TYPE = "type";



	/**重新加载配置
	 * @param request
	 * @return
	 * @see
	 * <pre>
		{
			"type": "ALL",  //重载对象，ALL, FUNCTION, REQUEST, ACCESS，非必须
			"phone": "13000082001",
			"verify": "1234567" //验证码，对应类型为 Verify.TYPE_RELOAD
		}
	 * </pre>
	 */
	@PostMapping("reload")
	public JSONObject reload(@RequestBody String request) {
		JSONObject requestObject = null;
		String type;
		String phone;
		String verify;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getString(TYPE);
			phone = requestObject.getString(PHONE);
			verify = requestObject.getString(VERIFY);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}

		JSONResponse response = new JSONResponse(headVerify(Verify.TYPE_RELOAD, phone, verify));
		response = response.getJSONResponse(VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			return DemoParser.extendErrorResult(requestObject, new ConditionErrorException("手机号或验证码错误"));
		}

		JSONObject result = DemoParser.newSuccessResult();

		boolean reloadAll = StringUtil.isEmpty(type, true) || "ALL".equals(type);

		if (reloadAll || "FUNCTION".equals(type)) {
			try {
				result.put(FUNCTION_, DemoFunction.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(FUNCTION_, DemoParser.newErrorResult(e));
			}
		}

		if (reloadAll || "REQUEST".equals(type)) {
			try {
				result.put(REQUEST_, StructureUtil.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(REQUEST_, DemoParser.newErrorResult(e));
			}
		}

		if (reloadAll || "ACCESS".equals(type)) {
			try {
				result.put(ACCESS_, DemoVerifier.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(ACCESS_, DemoParser.newErrorResult(e));
			}
		}

		return result;
	}

	/**生成验证码,修改为post请求
	 * @param request
	 * @see
	 * <pre>
		{
			"type": 0,  //类型，0,1,2,3,4，非必须
			"phone": "13000082001"
		}
	 * </pre>
	 */
	@PostMapping("post/verify")
	public JSONObject postVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}

		new DemoParser(DELETE, true).parse(newVerifyRequest(type, phone, null));

		JSONObject response = new DemoParser(POST, true).parseResponse(
				newVerifyRequest(type, phone, "" + (new Random().nextInt(9999) + 1000))
				);

		JSONObject verify = null;
		try {
			verify = response.getJSONObject(StringUtil.firstCase(VERIFY_));
		} catch (Exception e) {}

		if (verify == null || JSONResponse.isSuccess(verify.getIntValue(JSONResponse.KEY_CODE)) == false) {
			new DemoParser(DELETE, true).parseResponse(new JSONRequest(new Verify(type, phone)));
			return response;
		}

		//TODO 这里直接返回验证码，方便测试。实际上应该只返回成功信息，验证码通过短信发送
		JSONObject object = new JSONObject();
		object.put(TYPE, type);
		object.put(PHONE, phone);
		return getVerify(JSON.toJSONString(object));
	}

	/**获取验证码
	 * @param request
	 * @see
	 * <pre>
		{
			"type": 0,  //类型，0,1,2,3,4，非必须
			"phone": "13000082001"
		}
	 * </pre>
	 */
	@PostMapping("gets/verify")
	public JSONObject getVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}
		return new DemoParser(GETS, true).parseResponse(newVerifyRequest(type, phone, null));
	}

	/**校验验证码
	 * @param request
	 * @see
	 * <pre>
		{
			"type": 0,  //类型，0,1,2,3,4，非必须
			"phone": "13000082001",
			"verify": "123456"
		}
	 * </pre>
	 */
	@PostMapping("heads/verify")
	public JSONObject headVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		String verify;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
			verify = requestObject.getString(VERIFY);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}
		return headVerify(type, phone, verify);
	}

	/**校验验证码
	 * @author Lemon
	 * @param type
	 * @param phone
	 * @param code
	 * @return
	 */
	public JSONObject headVerify(int type, String phone, String code) {
		JSONResponse response = new JSONResponse(
				new DemoParser(GETS, true).parseResponse(
						new JSONRequest(
								new Verify(type, phone)
								.setVerify(code)
								).setTag(VERIFY_)
						)
				);
		Verify verify = response.getObject(Verify.class);
		if (verify == null) {
			return DemoParser.newErrorResult(StringUtil.isEmpty(code, true)
					? new NotExistException("验证码不存在！") : new ConditionErrorException("手机号或验证码错误！"));
		}

		//验证码过期
		long time = BaseModel.getTimeMillis(verify.getDate());
		long now = System.currentTimeMillis();
		if (now > 60*1000 + time) {
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new Verify(type, phone)).setTag(VERIFY_)
					);
			return DemoParser.newErrorResult(new TimeoutException("验证码已过期！"));
		}

		return new JSONResponse(
				new DemoParser(HEADS, true).parseResponse(
						new JSONRequest(new Verify(type, phone).setVerify(code)).setFormat(true)
						)
				);
	}



	/**新建一个验证码请求
	 * @param phone
	 * @param verify
	 * @return
	 */
	private zuo.biao.apijson.JSONRequest newVerifyRequest(int type, String phone, String verify) {
		return new JSONRequest(new Verify(type, phone).setVerify(verify)).setTag(VERIFY_).setFormat(true);
	}


	public static final String LOGIN = "login";
	public static final String REMEMBER = "remember";
	public static final String DEFAULTS = "defaults";

	public static final int LOGIN_TYPE_PASSWORD = 0;//密码登录
	public static final int LOGIN_TYPE_VERIFY = 1;//验证码登录
	/**用户登录
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
		{
			"type": 0,  //登录方式，非必须  0-密码 1-验证码
			"phone": "13000082001",
			"password": "1234567",
			"version": 1 //全局版本号，非必须
		}
	 * </pre>
	 */
	@PostMapping(LOGIN)
	public JSONObject login(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		boolean isPassword;
		String phone;
		String password;
		int version;
		Boolean format;
		boolean remember;
		JSONObject defaults;
		try {
			requestObject = DemoParser.parseRequest(request);

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

			version = requestObject.getIntValue(VERSION);
			format = requestObject.getBoolean(FORMAT);
			remember = requestObject.getBooleanValue(REMEMBER);
			defaults = requestObject.getJSONObject(DEFAULTS); //默认加到每个请求最外层的字段
			requestObject.remove(VERSION);
			requestObject.remove(FORMAT);
			requestObject.remove(REMEMBER);
			requestObject.remove(DEFAULTS);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}



		//手机号是否已注册
		JSONObject phoneResponse = new DemoParser(HEADS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		if (JSONResponse.isSuccess(phoneResponse) == false) {
			return DemoParser.newResult(phoneResponse.getIntValue(JSONResponse.KEY_CODE), phoneResponse.getString(JSONResponse.KEY_MSG));
		}
		JSONResponse response = new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
		if(JSONResponse.isExist(response) == false) {
			return DemoParser.newErrorResult(new NotExistException("手机号未注册"));
		}

		//根据phone获取User
		JSONObject privacyResponse = new DemoParser(GETS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						).setFormat(true)
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
					new DemoParser(HEADS, true).parseResponse(
							new JSONRequest(new Privacy(userId).setPassword(password))
							)
					);
		} else {//verify手机验证码登录
			response = new JSONResponse(headVerify(Verify.TYPE_LOGIN, phone, password));
		}
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		response = response.getJSONResponse(isPassword ? PRIVACY_ : VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			return DemoParser.newErrorResult(new ConditionErrorException("账号或密码错误"));
		}

		response = new JSONResponse(
				new DemoParser(GETS, true).parseResponse(
						new JSONRequest(new User(userId)).setFormat(true)
						)
				);
		User user = response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) != userId) {
			return DemoParser.newErrorResult(new NullPointerException("服务器内部错误"));
		}

		//登录状态保存至session
		session.setAttribute(USER_ID, userId); //用户id
		session.setAttribute(TYPE, isPassword ? LOGIN_TYPE_PASSWORD : LOGIN_TYPE_VERIFY); //登录方式
		session.setAttribute(USER_, user); //用户
		session.setAttribute(PRIVACY_, privacy); //用户隐私信息
		session.setAttribute(VERSION, version); //全局默认版本号
		session.setAttribute(FORMAT, format); //全局默认格式化配置
		session.setAttribute(REMEMBER, remember); //是否记住登录
		session.setAttribute(DEFAULTS, defaults); //给每个请求JSON最外层加的字段
		session.setMaxInactiveInterval(60*60*24*(remember ? 7 : 1)); //设置session过期时间

		response.put(REMEMBER, remember);
		response.put(DEFAULTS, defaults);
		return response;
	}

	/**退出登录，清空session
	 * @param session
	 * @return
	 */
	@PostMapping("logout")
	public JSONObject logout(HttpSession session) {
		long userId;
		try {
			userId = DemoVerifier.getVisitorId(session);//必须在session.invalidate();前！
			Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
			session.invalidate();
		} catch (Exception e) {
			return DemoParser.newErrorResult(e);
		}

		JSONObject result = DemoParser.newSuccessResult();
		JSONObject user = DemoParser.newSuccessResult();
		user.put(ID, userId);
		user.put(COUNT, 1);
		result.put(StringUtil.firstCase(USER_), user);

		return result;
	}


	private static final String REGISTER = "register";
	/**注册
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
		{
			"Privacy": {
				"phone": "13000082222",
				"_password": "123456"
			},
			"User": {
				"name": "APIJSONUser"
			},
			"verify": "1234"
		}
	 * </pre>
	 */
	@PostMapping(REGISTER)
	public JSONObject register(@RequestBody String request) {
		JSONObject requestObject = null;

		JSONObject privacyObj;

		String phone;
		String verify;
		String password;
		try {
			requestObject = DemoParser.parseRequest(request);
			privacyObj = requestObject.getJSONObject(PRIVACY_);

			phone = StringUtil.getString(privacyObj.getString(PHONE));
			verify = requestObject.getString(VERIFY);
			password = privacyObj.getString(_PASSWORD);

			if (StringUtil.isPhone(phone) == false) {
				return newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + PHONE);
			}
			if (StringUtil.isPassword(password) == false) {
				return newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + _PASSWORD);
			}
			if (StringUtil.isVerify(verify) == false) {
				return newIllegalArgumentResult(requestObject, VERIFY);
			}
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}


		JSONResponse response = new JSONResponse(headVerify(Verify.TYPE_REGISTER, phone, verify));
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		//手机号或验证码错误
		if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
			return DemoParser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！"));
		}



		//生成User和Privacy
		if (StringUtil.isEmpty(requestObject.getString(JSONRequest.KEY_TAG), true)) {
			requestObject.put(JSONRequest.KEY_TAG, REGISTER);
		}
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		response = new JSONResponse( 
				new DemoParser(POST).setNoVerifyLogin(true).parseResponse(requestObject)
				);

		//验证User和Privacy
		User user = response.getObject(User.class);
		long userId = user == null ? 0 : BaseModel.value(user.getId());
		Privacy privacy = response.getObject(Privacy.class);
		long userId2 = privacy == null ? 0 : BaseModel.value(privacy.getId());
		Exception e = null;
		if (userId <= 0 || userId != userId2) { //id不同
			e = new Exception("服务器内部错误！写入User或Privacy失败！");
		}

		if (e != null) { //出现错误，回退
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new User(userId))
					);
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new Privacy(userId2))
					);
		}

		return response;
	}


	/**
	 * @param requestObject
	 * @param key
	 * @return
	 */
	public static JSONObject newIllegalArgumentResult(JSONObject requestObject, String key) {
		return newIllegalArgumentResult(requestObject, key, null);
	}
	/**
	 * @param requestObject
	 * @param key
	 * @param msg 详细说明
	 * @return
	 */
	public static JSONObject newIllegalArgumentResult(JSONObject requestObject, String key, String msg) {
		return DemoParser.extendErrorResult(requestObject
				, new IllegalArgumentException(key + ":value 中value不合法！" + StringUtil.getString(msg)));
	}


	/**设置密码
	 * @param request 只用String，避免encode后未decode
	 * @return
	 * @see
	 * <pre>
	    使用旧密码修改
		{
			"oldPassword": 123456,
			"Privacy":{
			  "id": 13000082001,
			  "_password": "1234567"
			}
		}
		或使用手机号+验证码修改
		{
			"verify": "1234",
			"Privacy":{
			  "phone": "13000082001",
			  "_password": "1234567"
			}
		}
	 * </pre>
	 */
	@PostMapping("put/password")
	public JSONObject putPassword(@RequestBody String request){
		JSONObject requestObject = null;
		String oldPassword;
		String verify;

		int type = Verify.TYPE_PASSWORD;

		JSONObject privacyObj;
		long userId;
		String phone;
		String password;
		try {
			requestObject = DemoParser.parseRequest(request);
			oldPassword = StringUtil.getString(requestObject.getString(OLD_PASSWORD));
			verify = StringUtil.getString(requestObject.getString(VERIFY));

			requestObject.remove(OLD_PASSWORD);
			requestObject.remove(VERIFY);

			privacyObj = requestObject.getJSONObject(PRIVACY_);
			if (privacyObj == null) {
				throw new IllegalArgumentException(PRIVACY_ + " 不能为空！");
			}
			userId = privacyObj.getLongValue(ID);
			phone = privacyObj.getString(PHONE);
			password = privacyObj.getString(_PASSWORD);

			if (StringUtil.isEmpty(password, true)) { //支付密码
				type = Verify.TYPE_PAY_PASSWORD;
				password = privacyObj.getString(_PAY_PASSWORD);
				if (StringUtil.isNumberPassword(password) == false) {
					throw new IllegalArgumentException(PRIVACY_ + "/" + _PAY_PASSWORD + ":value 中value不合法！");
				}
			} else { //登录密码
				if (StringUtil.isPassword(password) == false) {
					throw new IllegalArgumentException(PRIVACY_ + "/" + _PASSWORD + ":value 中value不合法！");
				}
			}
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}


		if (StringUtil.isPassword(oldPassword)) {
			if (userId <= 0) { //手机号+验证码不需要userId
				return DemoParser.extendErrorResult(requestObject, new IllegalArgumentException(ID + ":value 中value不合法！"));
			}
			if (oldPassword.equals(password)) {
				return DemoParser.extendErrorResult(requestObject, new ConflictException("新旧密码不能一样！"));
			}

			//验证旧密码
			Privacy privacy = new Privacy(userId);
			if (type == Verify.TYPE_PASSWORD) {
				privacy.setPassword(oldPassword);
			} else {
				privacy.setPayPassword(oldPassword);
			}
			JSONResponse response = new JSONResponse( 
					new DemoParser(HEAD, true).parseResponse(
							new JSONRequest(privacy).setFormat(true)
							)
					);
			if (JSONResponse.isExist(response.getJSONResponse(PRIVACY_)) == false) {
				return DemoParser.extendErrorResult(requestObject, new ConditionErrorException("账号或原密码错误，请重新输入！"));
			}
		}
		else if (StringUtil.isPhone(phone) && StringUtil.isVerify(verify)) {
			JSONResponse response = new JSONResponse(headVerify(type, phone, verify));
			if (JSONResponse.isSuccess(response) == false) {
				return response;
			}
			if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
				return DemoParser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！"));
			}
			response = new JSONResponse(
					new DemoParser(GET, true).parseResponse(
							new JSONRequest(
									new Privacy().setPhone(phone)
									)
							)
					);
			Privacy privacy = response.getObject(Privacy.class);
			long id = privacy == null ? 0 : BaseModel.value(privacy.getId());
			privacyObj.remove(PHONE);
			privacyObj.put(ID, id);

			requestObject.put(PRIVACY_, privacyObj);
		} else {
			return DemoParser.extendErrorResult(requestObject, new IllegalArgumentException("请输入合法的 旧密码 或 手机号+验证码 ！"));
		}
		//TODO 上线版加上   password = MD5Util.MD5(password);


		//		requestObject.put(JSONRequest.KEY_TAG, "Password");
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		//修改密码
		return new DemoParser(PUT, true).parseResponse(requestObject);
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
	@PostMapping("put/balance")
	public JSONObject putBalance(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		JSONObject privacyObj;
		long userId;
		String payPassword;
		double change;
		try {
			DemoVerifier.verifyLogin(session);
			requestObject = new DemoParser(PUT).setRequest(DemoParser.parseRequest(request)).parseCorrectRequest();

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
			return DemoParser.extendErrorResult(requestObject, e);
		}

		//验证密码<<<<<<<<<<<<<<<<<<<<<<<

		privacyObj.remove("balance+");
		JSONResponse response = new JSONResponse(
				new DemoParser(HEADS, true).setSession(session).parseResponse(
						new JSONRequest(PRIVACY_, privacyObj)
						)
				);
		response = response.getJSONResponse(PRIVACY_);
		if (JSONResponse.isExist(response) == false) {
			return DemoParser.extendErrorResult(requestObject, new ConditionErrorException("支付密码错误！"));
		}

		//验证密码>>>>>>>>>>>>>>>>>>>>>>>>


		//验证金额范围<<<<<<<<<<<<<<<<<<<<<<<

		if (change == 0) {
			return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("balance+的值不能为0！"));
		}
		if (Math.abs(change) > 10000) {
			return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("单次 充值/提现 的金额不能超过10000元！"));
		}

		//验证金额范围>>>>>>>>>>>>>>>>>>>>>>>>

		if (change < 0) {//提现
			response = new JSONResponse(
					new DemoParser(GETS, true).parseResponse(
							new JSONRequest(
									new Privacy(userId)
									)
							)
					);
			Privacy privacy = response == null ? null : response.getObject(Privacy.class);
			long id = privacy == null ? 0 : BaseModel.value(privacy.getId());
			if (id != userId) {
				return DemoParser.extendErrorResult(requestObject, new Exception("服务器内部错误！"));
			}

			if (BaseModel.value(privacy.getBalance()) < -change) {
				return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("余额不足！"));
			}
		}


		privacyObj.remove(_PAY_PASSWORD);
		privacyObj.put("balance+", change);
		requestObject.put(PRIVACY_, privacyObj);
		requestObject.put(JSONRequest.KEY_TAG, PRIVACY_);
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		//不免验证，里面会验证身份
		return new DemoParser(PUT).setSession(session).parseResponse(requestObject);
	}


	public static final List<String> EXCEPT_HEADER_LIST;
	static {
		EXCEPT_HEADER_LIST = Arrays.asList(  //accept-encoding 在某些情况下导致乱码，origin 和 sec-fetch-mode 等 CORS 信息导致服务器代理失败
				"accept-encoding", "accept-language", // "accept", "connection"
				"host", "origin", "referer", "user-agent", "sec-fetch-mode", "sec-fetch-site", "sec-fetch-dest", "sec-fetch-user"
				);
	}

	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;

	@RequestMapping(value = "/delegate")
	public String delegate(@RequestParam("$_delegate_url") String url, @RequestBody String body, HttpMethod method, HttpSession session){
		Enumeration<String> names = request.getHeaderNames();
		HttpHeaders headers = null;
		String name;
		if (names != null) {
			headers = new HttpHeaders();
			while (names.hasMoreElements()) {
				name = names.nextElement();
				if (name != null && EXCEPT_HEADER_LIST.contains(name.toLowerCase()) == false) {
					headers.add(name, request.getHeader(name));
				}
			}

			@SuppressWarnings("unchecked")
			List<String> cookie = session == null ? null : (List<String>) session.getAttribute("Cookie");
			if (cookie != null && cookie.isEmpty() == false) {
				List<String> c = headers.get("Cookie");
				if (c == null) {
					c = new ArrayList<>();
				}
				c.addAll(cookie);
				headers.put("Cookie", c);
			}
		}
		try {
			request.getParameterMap().remove("$_delegate_url");
		} catch (Exception e) {
			// TODO: handle exception
		}

		RestTemplate client = new RestTemplate();
		//  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
		HttpEntity<String> requestEntity = new HttpEntity<>(method == HttpMethod.GET ? JSON.toJSONString(request.getParameterMap()) : body, headers);
		//  执行HTTP请求
		ResponseEntity<String> entity = client.exchange(url, method, requestEntity, String.class);

		HttpHeaders hs = entity.getHeaders();
		if (session != null && hs != null) {
			List<String> cookie = hs.get("Set-Cookie");
			if (cookie != null && cookie.isEmpty() == false) {
				session.setAttribute("Cookie", cookie);
			}
		}
		return entity.getBody();
	}


	/**Swagger 文档 Demo，供 APIAuto 测试导入 Swagger 文档到数据库用
	 * @return
	 */
	@GetMapping("v2/api-docs")
	public String swaggerAPIDocs() {
		return 	"{\n"+
				"    \"paths\": {\n"+
				"        \"/user/list\": {\n"+
				"            \"get\": {\n"+
				"                \"summary\": \"用户列表\",\n"+
				"                \"parameters\": [\n"+
				"                    {\n"+
				"                        \"name\": \"pageSize\",\n"+
				"                        \"description\": \"每页数量\",\n"+
				"                        \"default\": 10\n"+
				"                    },\n"+
				"                    {\n"+
				"                        \"name\": \"page\",\n"+
				"                        \"default\": 1\n"+
				"                    },\n"+
				"                    {\n"+
				"                        \"name\": \"searchKey\",\n"+
				"                        \"description\": \"搜索关键词\",\n"+
				"                        \"default\": \"a\"\n"+
				"                    }\n"+
				"                ]\n"+
				"            }\n"+
				"        },\n"+
				"        \"/user\": {\n"+
				"            \"get\": {\n"+
				"                \"summary\": \"用户详情\",\n"+
				"                \"parameters\": [\n"+
				"                    {\n"+
				"                        \"name\": \"id\",\n"+
				"                        \"description\": \"主键\",\n"+
				"                        \"default\": 82001\n"+
				"                    }\n"+
				"                ]\n"+
				"            }\n"+
				"        },\n"+
				"        \"/comment/post\": {\n"+
				"            \"post\": {\n"+
				"                \"summary\": \"新增评论\",\n"+
				"                \"parameters\": [\n"+
				"                    {\n"+
				"                        \"name\": \"userId\",\n"+
				"                        \"description\": \"用户id\",\n"+
				"                        \"default\": 82001\n"+
				"                    },\n"+
				"                    {\n"+
				"                        \"name\": \"momentId\",\n"+
				"                        \"description\": \"动态id\",\n"+
				"                        \"default\": 15\n"+
				"                    },\n"+
				"                    {\n"+
				"                        \"name\": \"conent\",\n"+
				"                        \"description\": \"内容\",\n"+
				"                        \"default\": \"测试评论\"\n"+
				"                    }\n"+
				"                ]\n"+
				"            }\n"+
				"        }\n"+
				"    }\n"+
				"}";
	}

}
