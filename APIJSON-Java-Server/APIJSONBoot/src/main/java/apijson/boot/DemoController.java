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

package apijson.boot;

import static apijson.RequestMethod.DELETE;
import static apijson.RequestMethod.GET;
import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.HEAD;
import static apijson.RequestMethod.HEADS;
import static apijson.RequestMethod.POST;
import static apijson.RequestMethod.PUT;
import static apijson.framework.APIJSONConstant.ACCESS_;
import static apijson.framework.APIJSONConstant.COUNT;
import static apijson.framework.APIJSONConstant.FORMAT;
import static apijson.framework.APIJSONConstant.FUNCTION_;
import static apijson.framework.APIJSONConstant.ID;
import static apijson.framework.APIJSONConstant.REQUEST_;
import static apijson.framework.APIJSONConstant.USER_ID;
import static apijson.framework.APIJSONConstant.VERSION;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import java.net.URLDecoder;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
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

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.boot.model.Privacy;
import apijson.boot.model.User;
import apijson.boot.model.Verify;
import apijson.framework.APIJSONController;
import apijson.framework.BaseModel;
import apijson.framework.MethodUtil;
import apijson.framework.StructureUtil;
import apijson.orm.JSONRequest;
import apijson.orm.exception.ConditionErrorException;
import apijson.orm.exception.ConflictException;
import apijson.orm.exception.NotExistException;
import apijson.orm.exception.OutOfRangeException;


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
public class DemoController extends APIJSONController {
	private static final String TAG = "DemoController";

	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@PostMapping(value = "get")
	@Override
	public String get(@RequestBody String request, HttpSession session) {
		return super.get(request, session);
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@PostMapping("head")
	@Override
	public String head(@RequestBody String request, HttpSession session) {
		return super.head(request, session);
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	@PostMapping("gets")
	@Override
	public String gets(@RequestBody String request, HttpSession session) {
		return super.gets(request, session);
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	@PostMapping("heads")
	@Override
	public String heads(@RequestBody String request, HttpSession session) {
		return super.heads(request, session);
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	@PostMapping("post")
	@Override
	public String post(@RequestBody String request, HttpSession session) {
		return super.post(request, session);
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	@PostMapping("put")
	@Override
	public String put(@RequestBody String request, HttpSession session) {
		return super.put(request, session);
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	@PostMapping("delete")
	@Override
	public String delete(@RequestBody String request, HttpSession session) {
		return super.delete(request, session);
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










	public static final String USER_;
	public static final String PRIVACY_;
	public static final String VERIFY_; //加下划线后缀是为了避免 Verify 和 verify 都叫VERIFY，分不清
	static {
		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}



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
	@Override
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
				result.put(FUNCTION_, DemoFunctionParser.init());
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

		new DemoParser(DELETE, false).parse(newVerifyRequest(type, phone, null));

		JSONObject response = new DemoParser(POST, false).parseResponse(
				newVerifyRequest(type, phone, "" + (new Random().nextInt(9999) + 1000))
				);

		JSONObject verify = null;
		try {
			verify = response.getJSONObject(StringUtil.firstCase(VERIFY_));
		} catch (Exception e) {}

		if (verify == null || JSONResponse.isSuccess(verify.getIntValue(JSONResponse.KEY_CODE)) == false) {
			new DemoParser(DELETE, false).parseResponse(new JSONRequest(new Verify(type, phone)));
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
		return new DemoParser(GETS, false).parseResponse(newVerifyRequest(type, phone, null));
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
				new DemoParser(GETS, false).parseResponse(
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
			new DemoParser(DELETE, false).parseResponse(
					new JSONRequest(new Verify(type, phone)).setTag(VERIFY_)
					);
			return DemoParser.newErrorResult(new TimeoutException("验证码已过期！"));
		}

		return new JSONResponse(
				new DemoParser(HEADS, false).parseResponse(
						new JSONRequest(new Verify(type, phone).setVerify(code)).setFormat(true)
						)
				);
	}



	/**新建一个验证码请求
	 * @param phone
	 * @param verify
	 * @return
	 */
	private apijson.JSONRequest newVerifyRequest(int type, String phone, String verify) {
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
		JSONObject phoneResponse = new DemoParser(HEADS, false).parseResponse(
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
		JSONObject privacyResponse = new DemoParser(GETS, false).parseResponse(
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
					new DemoParser(HEADS, false).parseResponse(
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
				new DemoParser(GETS, false).parseResponse(
						new JSONRequest(new User(userId)).setFormat(true)
						)
				);
		User user = response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) != userId) {
			return DemoParser.newErrorResult(new NullPointerException("服务器内部错误"));
		}

		//登录状态保存至session
		super.login(session, user, version, format, defaults);
		session.setAttribute(USER_ID, userId); //用户id
		session.setAttribute(TYPE, isPassword ? LOGIN_TYPE_PASSWORD : LOGIN_TYPE_VERIFY); //登录方式
		session.setAttribute(USER_, user); //用户
		session.setAttribute(PRIVACY_, privacy); //用户隐私信息
		session.setAttribute(REMEMBER, remember); //是否记住登录
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
	@Override
	public JSONObject logout(HttpSession session) {
		long userId;
		try {
			userId = DemoVerifier.getVisitorId(session);//必须在session.invalidate();前！
			Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
			super.logout(session);
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
				new DemoParser(POST).setNeedVerifyLogin(false).parseResponse(requestObject)
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
			new DemoParser(DELETE, false).parseResponse(
					new JSONRequest(new User(userId))
					);
			new DemoParser(DELETE, false).parseResponse(
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
					new DemoParser(HEAD, false).parseResponse(
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
					new DemoParser(GET, false).parseResponse(
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
		return new DemoParser(PUT, false).parseResponse(requestObject);
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
				new DemoParser(HEADS, false).setSession(session).parseResponse(
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
					new DemoParser(GETS, false).parseResponse(
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


	public static final String ADD_COOKIE = "Add-Cookie";
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

	/**代理接口，解决前端（APIAuto等）跨域问题
	 * @param exceptHeaders 排除请求头，必须放在最前面，放后面可能被当成 $_delegate_url 的一部分
	 * @param url 被代理的 url
	 * @param body POST Body
	 * @param method HTTP Method
	 * @param session HTTP session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delegate")
	public String delegate(
			@RequestParam(value = "$_type", required = false) String type,
			@RequestParam(value = "$_except_headers", required = false) String exceptHeaders,
			@RequestParam("$_delegate_url") String url, 
			@RequestBody(required = false) String body, 
			HttpMethod method, HttpSession session
			) {

		if ("GRPC".equals(type)) {
			int index = url.indexOf("://");
			String endpoint = index < 0 ? url : url.substring(index + 3);
			
			index = endpoint.indexOf("/");
			String remoteMethod = index < 0 ? "" : endpoint.substring(index);
			
			url = "http://localhost:50050" + remoteMethod;
			
			
			JSONObject obj = JSON.parseObject(body);
			if (obj == null) {
				obj = new JSONObject(true);
			}
			if (obj.get("endpoint") == null) {
				endpoint = index < 0 ? endpoint : endpoint.substring(0, index);
				obj.put("endpoint", endpoint);
			}
			
			body = obj.toJSONString();
		}
		
		Enumeration<String> names = request.getHeaderNames();
		HttpHeaders headers = null;
		String name;
		if (names != null) {
			headers = new HttpHeaders();
			//Arrays.asList(null) 抛异常，可以排除不存在的头来替代  exceptHeaders == null //空字符串表示不排除任何头
			List<String> exceptHeaderList = StringUtil.isEmpty(exceptHeaders, true) 
					? EXCEPT_HEADER_LIST : Arrays.asList(StringUtil.split(exceptHeaders));


			List<String> setCookie = null;
			List<String> addCookie = null;

			while (names.hasMoreElements()) {
				name = names.nextElement();
				if (name != null && exceptHeaderList.contains(name.toLowerCase()) == false) {
					//APIAuto 是一定精准发送 Set-Cookie 名称过来的，预留其它命名可实现覆盖原 Cookie Header 等更多可能 
					if (SET_COOKIE.toLowerCase().equals(name.toLowerCase())) {  //接收到时就已经被强制小写
						setCookie = Arrays.asList(request.getHeader(name));  // JSON.parseArray(request.getHeader(name), String.class);
					}
					else if (ADD_COOKIE.toLowerCase().equals(name.toLowerCase())) {
						addCookie = Arrays.asList(request.getHeader(name));
					}
					else {
						headers.add(name, request.getHeader(name));
					}
				}
			}

			if (setCookie == null && session != null) {
				setCookie = (List<String>) session.getAttribute(COOKIE);
			}
			if (addCookie != null && addCookie.isEmpty() == false) {
				if (setCookie == null) {
					setCookie = addCookie;
				}
				else {
					setCookie = new ArrayList<>(setCookie);
					setCookie.addAll(addCookie);
				}
			}
			
			if (setCookie != null) { //允许传空的 Cookie && setCookie.isEmpty() == false) {
				headers.put(COOKIE, setCookie);
			}
		}

		//可能是 HTTP POST FORM，即便是 HTTP POST JSON，URL 的参数也要拼接，尽可能保持原样  if (method == HttpMethod.GET) {
		Map<String, String[]> map = request.getParameterMap();

		if (map != null) {
			map = new HashMap<>(map);  //解决 throw exception: Unmodified Map
			map.remove("$_type");
			map.remove("$_except_headers");
			map.remove("$_delegate_url");

			Set<Entry<String, String[]>> set = map == null ? null : map.entrySet();

			if (set != null && set.isEmpty() == false) {

				if (url.contains("?") == false) {
					url += "?";
				}
				boolean first = url.endsWith("?");

				for (Entry<String, String[]> e : set) {
					if (e != null) {
						url += ((first ? "" : "&") + e.getKey() + "=" + ( e.getValue() == null || e.getValue().length <= 0 ? "" : StringUtil.getString(e.getValue()[0]) ));
						first = false;
					}
				}
			}
		}
		// }

		RestTemplate client = new RestTemplate();
		// 请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
		HttpEntity<String> requestEntity = new HttpEntity<>(method == HttpMethod.GET ? null : body, headers);
		// 执行HTTP请求，这里可能抛异常，不要包装，直接让它抛，能够在浏览器 Console/XHR/{i}/Preview
		// 看到 error: "Internal Server Error" message: "405 null" 之类的包括信息，
		// 包装后反而容易混淆，并且会因为 JSON 结构不一致导致解析问题
		ResponseEntity<String> entity = client.exchange(url, method, requestEntity, String.class);

		HttpHeaders hs = entity.getHeaders();
		if (session != null && hs != null) {
			List<String> cookie = hs.get(SET_COOKIE);
			if (cookie != null && cookie.isEmpty() == false) {
				session.setAttribute(COOKIE, cookie);
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
				"                        \"name\": \"pageNum\",\n"+
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
				"                        \"name\": \"content\",\n"+
				"                        \"description\": \"内容\",\n"+
				"                        \"default\": \"测试评论\"\n"+
				"                    }\n"+
				"                ]\n"+
				"            }\n"+
				"        }\n"+
				"    }\n"+
				"}";
	}

	/**Rap 文档 Demo，供 APIAuto 测试导入 Rap 文档到数据库用
	 * @return
	 */
	@GetMapping("repository/joined")
	public String rapJoinedRepository() {
		return 	"{\n" +
        "    \"data\": [\n" +
        "        {\n" +
        "            \"id\": 1243,\n" +
        "            \"name\": \"Test\",\n" +
        "            \"description\": \"4 test\",\n" +
        "            \"logo\": null,\n" +
        "            \"token\": \"JrA78ktHhsGJtlhtUwt4Bpk3i96-QQLE\",\n" +
        "            \"visibility\": true,\n" +
        "            \"ownerId\": 1803,\n" +
        "            \"organizationId\": null,\n" +
        "            \"creatorId\": 1803,\n" +
        "            \"lockerId\": null,\n" +
        "            \"createdAt\": \"2017-12-05T08:48:44.000Z\",\n" +
        "            \"updatedAt\": \"2019-12-30T02:36:48.000Z\",\n" +
        "            \"deletedAt\": null,\n" +
        "            \"creator\": {\n" +
        "                \"id\": 1803,\n" +
        "                \"fullname\": \"TommyLemon\",\n" +
        "                \"email\": \"111@qq.com\"\n" +
        "            },\n" +
        "            \"owner\": {\n" +
        "                \"id\": 1803,\n" +
        "                \"fullname\": \"TommyLemon\",\n" +
        "                \"email\": \"111@qq.com\"\n" +
        "            },\n" +
        "            \"locker\": null,\n" +
        "            \"members\": [\n" +
        "                {\n" +
        "                    \"id\": 1803,\n" +
        "                    \"fullname\": \"TommyLemon\",\n" +
        "                    \"email\": \"111@qq.com\"\n" +
        "                }\n" +
        "            ],\n" +
        "            \"organization\": null,\n" +
        "            \"collaborators\": [],\n" +
        "            \"RepositoriesMembers\": {\n" +
        "                \"userId\": 1803,\n" +
        "                \"repositoryId\": 1243\n" +
        "            },\n" +
        "            \"canUserEdit\": true\n" +
        "        }\n" +
        "    ]\n" +
        "}";
	}
	
	
	/**Rap 文档 Demo，供 APIAuto 测试导入 Rap 文档到数据库用
	 * @param id
	 * @return
	 */
	@GetMapping("repository/get")
	public String rapRepositoryDetail(@RequestParam("id") String id) {
		return "{\n" +
        "    \"data\": {\n" +
        "        \"id\": " + id + ",\n" +
        "        \"name\": \"Test\",\n" +
        "        \"description\": \"4 test\",\n" +
        "        \"logo\": null,\n" +
        "        \"token\": \"JrA78ktHhsGJtlhtUwt4Bpk3i96-QQLE\",\n" +
        "        \"visibility\": true,\n" +
        "        \"ownerId\": 1803,\n" +
        "        \"organizationId\": null,\n" +
        "        \"creatorId\": 1803,\n" +
        "        \"lockerId\": null,\n" +
        "        \"createdAt\": \"2017-12-05T08:48:44.000Z\",\n" +
        "        \"updatedAt\": \"2019-12-30T02:36:48.000Z\",\n" +
        "        \"deletedAt\": null,\n" +
        "        \"creator\": {\n" +
        "            \"id\": 1803,\n" +
        "            \"fullname\": \"TommyLemon\",\n" +
        "            \"email\": \"111@qq.com\"\n" +
        "        },\n" +
        "        \"owner\": {\n" +
        "            \"id\": 1803,\n" +
        "            \"fullname\": \"TommyLemon\",\n" +
        "            \"email\": \"111@qq.com\"\n" +
        "        },\n" +
        "        \"locker\": null,\n" +
        "        \"members\": [\n" +
        "            {\n" +
        "                \"id\": 1803,\n" +
        "                \"fullname\": \"TommyLemon\",\n" +
        "                \"email\": \"111@qq.com\"\n" +
        "            }\n" +
        "        ],\n" +
        "        \"organization\": null,\n" +
        "        \"collaborators\": [],\n" +
        "        \"modules\": [\n" +
        "            {\n" +
        "                \"id\": 1973,\n" +
        "                \"name\": \"示例模块\",\n" +
        "                \"description\": \"示例模块\",\n" +
        "                \"priority\": 1,\n" +
        "                \"creatorId\": 1803,\n" +
        "                \"repositoryId\": 1243,\n" +
        "                \"createdAt\": \"2017-12-05T08:48:44.000Z\",\n" +
        "                \"updatedAt\": \"2017-12-05T08:48:44.000Z\",\n" +
        "                \"deletedAt\": null,\n" +
        "                \"interfaces\": [\n" +
        "                    {\n" +
        "                        \"id\": 4042,\n" +
        "                        \"name\": \"getUser\",\n" +
        "                        \"url\": \"/get\",\n" +
        "                        \"method\": \"POST\",\n" +
        "                        \"description\": \"get an User\",\n" +
        "                        \"priority\": 1,\n" +
        "                        \"status\": 200,\n" +
        "                        \"creatorId\": 1803,\n" +
        "                        \"lockerId\": null,\n" +
        "                        \"moduleId\": 1973,\n" +
        "                        \"repositoryId\": 1243,\n" +
        "                        \"createdAt\": \"2017-12-05T08:51:02.000Z\",\n" +
        "                        \"updatedAt\": \"2020-05-30T16:25:28.000Z\",\n" +
        "                        \"deletedAt\": null,\n" +
        "                        \"locker\": null,\n" +
        "                        \"properties\": [\n" +
        "                            {\n" +
        "                                \"id\": 81553,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"Object\",\n" +
        "                                \"pos\": 2,\n" +
        "                                \"name\": \"User\",\n" +
        "                                \"rule\": \"\",\n" +
        "                                \"value\": \"{}\",\n" +
        "                                \"description\": \"\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 43204,\n" +
        "                                \"interfaceId\": 4042,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2017-12-05T08:52:03.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:28.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 81562,\n" +
        "                                \"scope\": \"response\",\n" +
        "                                \"type\": \"Object\",\n" +
        "                                \"pos\": 2,\n" +
        "                                \"name\": \"User\",\n" +
        "                                \"rule\": \"\",\n" +
        "                                \"value\": \"{\\n\\\"id\\\": 38710 ,\\n\\\"sex\\\": 0 ,\\n\\\"name\\\": \\\"TommyLemon\\\" ,\\n\\\"contactIdList\\\":  [\\n82003 ,\\n82005 ,\\n90814 \\n],\\n\\\"pictureList\\\":  [\\n\\\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\\\" \\n]\\n}\",\n" +
        "                                \"description\": \"\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1,\n" +
        "                                \"interfaceId\": 4042,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2017-12-05T08:54:16.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:28.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 17621689,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 1,\n" +
        "                                \"name\": \"site\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"apijson\",\n" +
        "                                \"description\": \"来源网站\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590855928395,\n" +
        "                                \"interfaceId\": 4042,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T16:25:28.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:28.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            }\n" +
        "                        ]\n" +
        "                    },\n" +
        "                    {\n" +
        "                        \"id\": 1446108,\n" +
        "                        \"name\": \"post\",\n" +
        "                        \"url\": \"/post\",\n" +
        "                        \"method\": \"POST\",\n" +
        "                        \"description\": \"post user\",\n" +
        "                        \"priority\": 2,\n" +
        "                        \"status\": 200,\n" +
        "                        \"creatorId\": 1803,\n" +
        "                        \"lockerId\": null,\n" +
        "                        \"moduleId\": 1973,\n" +
        "                        \"repositoryId\": 1243,\n" +
        "                        \"createdAt\": \"2020-01-13T10:32:51.000Z\",\n" +
        "                        \"updatedAt\": \"2020-05-30T16:29:13.000Z\",\n" +
        "                        \"deletedAt\": null,\n" +
        "                        \"locker\": null,\n" +
        "                        \"properties\": [\n" +
        "                            {\n" +
        "                                \"id\": 17394319,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 1,\n" +
        "                                \"name\": \"he\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"test\",\n" +
        "                                \"description\": \"\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590334790882,\n" +
        "                                \"interfaceId\": 1446108,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-24T15:39:50.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:29:13.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            }\n" +
        "                        ]\n" +
        "                    },\n" +
        "                    {\n" +
        "                        \"id\": 1596193,\n" +
        "                        \"name\": \"login\",\n" +
        "                        \"url\": \"/login\",\n" +
        "                        \"method\": \"POST\",\n" +
        "                        \"description\": \"\",\n" +
        "                        \"priority\": 1590853798312,\n" +
        "                        \"status\": 200,\n" +
        "                        \"creatorId\": 1803,\n" +
        "                        \"lockerId\": null,\n" +
        "                        \"moduleId\": 1973,\n" +
        "                        \"repositoryId\": 1243,\n" +
        "                        \"createdAt\": \"2020-05-30T15:49:58.000Z\",\n" +
        "                        \"updatedAt\": \"2020-05-30T16:25:08.000Z\",\n" +
        "                        \"deletedAt\": null,\n" +
        "                        \"locker\": null,\n" +
        "                        \"properties\": [\n" +
        "                            {\n" +
        "                                \"id\": 17621552,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 3,\n" +
        "                                \"name\": \"phone\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"13000082001\",\n" +
        "                                \"description\": \"手机号\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590853936991,\n" +
        "                                \"interfaceId\": 1596193,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T15:52:16.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 17621553,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 3,\n" +
        "                                \"name\": \"password\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"123456\",\n" +
        "                                \"description\": \"密码\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590853936994,\n" +
        "                                \"interfaceId\": 1596193,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T15:52:16.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 17621554,\n" +
        "                                \"scope\": \"response\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 3,\n" +
        "                                \"name\": \"msg\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"\",\n" +
        "                                \"description\": \"success\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590853936997,\n" +
        "                                \"interfaceId\": 1596193,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T15:52:16.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 17621565,\n" +
        "                                \"scope\": \"response\",\n" +
        "                                \"type\": \"Number\",\n" +
        "                                \"pos\": 3,\n" +
        "                                \"name\": \"code\",\n" +
        "                                \"rule\": \"\",\n" +
        "                                \"value\": \"200\",\n" +
        "                                \"description\": null,\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590853937020,\n" +
        "                                \"interfaceId\": 1596193,\n" +
        "                                \"creatorId\": null,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T15:52:17.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            },\n" +
        "                            {\n" +
        "                                \"id\": 17621688,\n" +
        "                                \"scope\": \"request\",\n" +
        "                                \"type\": \"String\",\n" +
        "                                \"pos\": 1,\n" +
        "                                \"name\": \"head\",\n" +
        "                                \"rule\": null,\n" +
        "                                \"value\": \"apijson\",\n" +
        "                                \"description\": \"请求头\",\n" +
        "                                \"parentId\": -1,\n" +
        "                                \"priority\": 1590855907992,\n" +
        "                                \"interfaceId\": 1596193,\n" +
        "                                \"creatorId\": 1803,\n" +
        "                                \"moduleId\": 1973,\n" +
        "                                \"repositoryId\": 1243,\n" +
        "                                \"required\": false,\n" +
        "                                \"createdAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"updatedAt\": \"2020-05-30T16:25:07.000Z\",\n" +
        "                                \"deletedAt\": null\n" +
        "                            }\n" +
        "                        ]\n" +
        "                    }\n" +
        "                ]\n" +
        "            }\n" +
        "        ],\n" +
        "        \"canUserEdit\": true\n" +
        "    }\n" +
        "}";
	}
	
	



	@PostMapping("method/invoke")
	public JSONObject invokeMethod(@RequestBody String request) {
		try {
			JSONObject req = JSON.parseObject(request);
			if (req != null) {
				String pkgName = req.getString("package");
				String clsName = req.getString("class");
				return MethodUtil.invokeMethod(
						req,
						DemoApplication.getApplicationContext().getBean(
								Class.forName(pkgName.replaceAll("/", ".") + "." + clsName)
								)
						);
			}
		} catch (Exception e) {
			Log.e(TAG, "listMethod  try { JSONObject req = JSON.parseObject(request); ... } catch (Exception e) { \n" + e.getMessage());
		}

		return super.invokeMethod(request);
	}

	@PostMapping("method/list")
	public JSONObject listMethod(@RequestBody String request) {
		return super.listMethod(request);
	}

}
