package apijson.demo.server;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.rmi.ServerException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.NotAction;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.HttpKit;

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

public class Controller extends com.jfinal.core.Controller {
	private static final String TAG = "Controller";

	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@Before(POST.class)
	public void get() {
		renderJson(new DemoParser(GET).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@Before(POST.class)
	public void head() {
		renderJson(new DemoParser(HEAD).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	@Before(POST.class)
	public void gets() {
		renderJson(new DemoParser(GETS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	@Before(POST.class)
	public void heads() {
		renderJson(new DemoParser(HEADS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	@Before(POST.class)
	public void post() {
		renderJson(new DemoParser(POST).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	@Before(POST.class)
	public void put() {
		renderJson(new DemoParser(PUT).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	@Before(POST.class)
	public void delete() {
		renderJson(new DemoParser(DELETE).setSession(getSession()).parse(HttpKit.readData(getRequest())));
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
	@Before(POST.class)
	public void reload() {
		String request = HttpKit.readData(getRequest());
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
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
		}

		JSONResponse response = new JSONResponse(headVerify(Verify.TYPE_RELOAD, phone, verify));
		response = response.getJSONResponse(VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			renderJson(DemoParser.extendErrorResult(requestObject, new ConditionErrorException("手机号或验证码错误")));
			return;
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

		renderJson(result);
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
	@Before(POST.class)
	@ActionKey("post/verify")
	public void postVerify() {
		String request = HttpKit.readData(getRequest());
		JSONObject requestObject = null;
		int type;
		String phone;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
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
			renderJson(response);
			return;
		}

		//TODO 这里直接返回验证码，方便测试。实际上应该只返回成功信息，验证码通过短信发送
		JSONObject object = new JSONObject();
		object.put(TYPE, type);
		object.put(PHONE, phone);
		renderJson(getVerify(JSON.toJSONString(object)));
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
	@Before(POST.class)
	@ActionKey("gets/verify")
	public void getVerify() {
		renderJson(getVerify(HttpKit.readData(getRequest())));
	}

	@NotAction
	public JSONObject getVerify(String request) {
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
	@Before(POST.class)
	@ActionKey("heads/verify")
	public void headVerify() {
		String request = HttpKit.readData(getRequest());
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
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
		}
		renderJson(headVerify(type, phone, verify));
	}

	/**校验验证码
	 * @author Lemon
	 * @param type
	 * @param phone
	 * @param code
	 * @return
	 */
	@NotAction
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
	@NotAction
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
	@Before(POST.class)
	public void login() {
		String request = HttpKit.readData(getRequest());
		HttpSession session = getSession();

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
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
		}



		//手机号是否已注册
		JSONObject phoneResponse = new DemoParser(HEADS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		if (JSONResponse.isSuccess(phoneResponse) == false) {
			renderJson(DemoParser.newResult(phoneResponse.getIntValue(JSONResponse.KEY_CODE), phoneResponse.getString(JSONResponse.KEY_MSG)));
			return;
		}
		JSONResponse response = new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
		if(JSONResponse.isExist(response) == false) {
			renderJson(DemoParser.newErrorResult(new NotExistException("手机号未注册")));
			return;
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
			renderJson(privacyResponse);
			return;
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
			renderJson(response);
			return;
		}
		response = response.getJSONResponse(isPassword ? PRIVACY_ : VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			renderJson(DemoParser.newErrorResult(new ConditionErrorException("账号或密码错误")));
			return;
		}

		response = new JSONResponse(
				new DemoParser(GETS, true).parseResponse(
						new JSONRequest(new User(userId)).setFormat(true)
						)
				);
		User user = response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) != userId) {
			renderJson(DemoParser.newErrorResult(new NullPointerException("服务器内部错误")));
			return;
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
		
		renderJson(response);
	}

	/**退出登录，清空session
	 * @param session
	 * @return
	 */
	@Before(POST.class)
	public void logout() {
		HttpSession session = getSession();
		long userId;
		try {
			userId = DemoVerifier.getVisitorId(session);//必须在session.invalidate();前！
			Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
			session.invalidate();
		} catch (Exception e) {
			renderJson(DemoParser.newErrorResult(e));
			return;
		}

		JSONObject result = DemoParser.newSuccessResult();
		JSONObject user = DemoParser.newSuccessResult();
		user.put(ID, userId);
		user.put(COUNT, 1);
		result.put(StringUtil.firstCase(USER_), user);

		renderJson(result);
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
	@Before(POST.class)
	public void register() {
		String request = HttpKit.readData(getRequest());
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
				renderJson(newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + PHONE));
				return;
			}
			if (StringUtil.isPassword(password) == false) {
				renderJson(newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + _PASSWORD));
				return;
			}
			if (StringUtil.isVerify(verify) == false) {
				renderJson(newIllegalArgumentResult(requestObject, VERIFY));
				return;
			}
		} catch (Exception e) {
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
		}


		JSONResponse response = new JSONResponse(headVerify(Verify.TYPE_REGISTER, phone, verify));
		if (JSONResponse.isSuccess(response) == false) {
			renderJson(response);
			return;
		}
		//手机号或验证码错误
		if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
			renderJson(DemoParser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！")));
			return;
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

		renderJson(response);
	}


	/**
	 * @param requestObject
	 * @param key
	 * @return
	 */
	@NotAction
	public static JSONObject newIllegalArgumentResult(JSONObject requestObject, String key) {
		return newIllegalArgumentResult(requestObject, key, null);
	}
	/**
	 * @param requestObject
	 * @param key
	 * @param msg 详细说明
	 * @return
	 */
	@NotAction
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
	@Before(POST.class)
	@ActionKey("put/password")
	public void putPassword() {
		String request = HttpKit.readData(getRequest());

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
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
		}


		if (StringUtil.isPassword(oldPassword)) {
			if (userId <= 0) { //手机号+验证码不需要userId
				renderJson(DemoParser.extendErrorResult(requestObject, new IllegalArgumentException(ID + ":value 中value不合法！")));
				return;
			}
			if (oldPassword.equals(password)) {
				renderJson(DemoParser.extendErrorResult(requestObject, new ConflictException("新旧密码不能一样！")));
				return;
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
				renderJson(DemoParser.extendErrorResult(requestObject, new ConditionErrorException("账号或原密码错误，请重新输入！")));
				return;
			}
		}
		else if (StringUtil.isPhone(phone) && StringUtil.isVerify(verify)) {
			JSONResponse response = new JSONResponse(headVerify(type, phone, verify));
			if (JSONResponse.isSuccess(response) == false) {
				renderJson(response);
				return;
			}
			if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
				renderJson(DemoParser.extendErrorResult(response, new ConditionErrorException("手机号或验证码错误！")));
				return;
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
			renderJson(DemoParser.extendErrorResult(requestObject, new IllegalArgumentException("请输入合法的 旧密码 或 手机号+验证码 ！")));
			return;
		}
		//TODO 上线版加上   password = MD5Util.MD5(password);


		//		requestObject.put(JSONRequest.KEY_TAG, "Password");
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		
		//修改密码
		renderJson(new DemoParser(PUT, true).parseResponse(requestObject));
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
	@Before(POST.class)
	@ActionKey("put/balance")
	public void putBalance() {
		String request = HttpKit.readData(getRequest());
		HttpSession session = getSession();
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
			renderJson(DemoParser.extendErrorResult(requestObject, e));
			return;
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
			renderJson(DemoParser.extendErrorResult(requestObject, new ConditionErrorException("支付密码错误！")));
			return;
		}

		//验证密码>>>>>>>>>>>>>>>>>>>>>>>>


		//验证金额范围<<<<<<<<<<<<<<<<<<<<<<<

		if (change == 0) {
			renderJson(DemoParser.extendErrorResult(requestObject, new OutOfRangeException("balance+的值不能为0！")));
			return;
		}
		if (Math.abs(change) > 10000) {
			renderJson(DemoParser.extendErrorResult(requestObject, new OutOfRangeException("单次 充值/提现 的金额不能超过10000元！")));
			return;
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
				renderJson(DemoParser.extendErrorResult(requestObject, new Exception("服务器内部错误！")));
				return;
			}

			if (BaseModel.value(privacy.getBalance()) < -change) {
				renderJson(DemoParser.extendErrorResult(requestObject, new OutOfRangeException("余额不足！")));
				return;
			}
		}


		privacyObj.remove(_PAY_PASSWORD);
		privacyObj.put("balance+", change);
		requestObject.put(PRIVACY_, privacyObj);
		requestObject.put(JSONRequest.KEY_TAG, PRIVACY_);
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		//不免验证，里面会验证身份
		renderJson(new DemoParser(PUT).setSession(session).parseResponse(requestObject));
	}


}

