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

import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.POST_GET;
import static zuo.biao.apijson.RequestMethod.POST_HEAD;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;
import static zuo.biao.apijson.RequestMethod.DELETE;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.model.BaseModel;
import apijson.demo.server.model.Comment;
import apijson.demo.server.model.Login;
import apijson.demo.server.model.Moment;
import apijson.demo.server.model.Password;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import apijson.demo.server.model.Wallet;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.exception.ConditionNotMatchException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.OutOfRangeException;

/**request receiver and controller
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {

	/**如果用在金融等对安全要求很高的领域，get和head可以测试期间使用明文的HTTP GET，上线版改用非明文的HTTP POST，兼顾系统安全与开发效率
	 * @param request
	 * @return
	 */
	@RequestMapping("head/{request}")
	public String head(@PathVariable String request) {
		return new Parser(HEAD).parse(request);
	}

	@RequestMapping("get/{request}")
	public String get(@PathVariable String request) {
		return new Parser(GET).parse(request);
	}


	@RequestMapping(value="post", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post(@RequestBody String request) {
		return new Parser(POST).parse(request);
	}

	/**用POST方法HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_head", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post_head(@RequestBody String request) {
		return new Parser(POST_HEAD).parse(request);
	}
	/**用POST方法GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_get", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post_get(@RequestBody String request) {
		return new Parser(POST_GET).parse(request);
	}

	/**以下接口继续用POST接口是为了客户端方便，只需要做get，post请求。也可以改用实际对应的方法。
	 * post，put方法名可以改为add，update等更客户端容易懂的名称
	 */
	@RequestMapping(value="put", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String put(@RequestBody String request) {
		return new Parser(PUT).parse(request);
	}

	@RequestMapping(value="delete", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String delete(@RequestBody String request) {
		return new Parser(DELETE).parse(request);
	}








	public static final String USER_;
	public static final String MOMENT_;
	public static final String COMMENT_;
	public static final String WALLET_;
	public static final String PASSWORD_;
	static {
		USER_ = User.class.getSimpleName();
		MOMENT_ = Moment.class.getSimpleName();
		COMMENT_ = Comment.class.getSimpleName();
		WALLET_ = Wallet.class.getSimpleName();
		PASSWORD_ = Password.class.getSimpleName();
	}

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




	@RequestMapping("post/authCode/{phone}")
	public String postAuthCode(@PathVariable String phone) {
		new Parser(DELETE, true).parse(newVerifyRequest(newVerify(phone, 0)));

		JSONObject response = new Parser(POST, true).parseResponse(
				newVerifyRequest(newVerify(phone, new Random().nextInt(9999) + 1000)));

		JSONObject verify = null;
		try {
			verify = response.getJSONObject("Verify");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (verify == null || verify.getIntValue("status") != 200) {
			new Parser(DELETE, true).parseResponse(new JSONRequest(new Verify(phone)));
			return JSON.toJSONString(Parser.extendErrorResult(response, null));
		}

		return getAuthCode(phone);
	}

	@RequestMapping(value="post_get/authCode/{phone}", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String getAuthCode(@PathVariable String phone) {
		return new Parser(POST_GET).parse(newVerifyRequest(newVerify(phone, 0)));
	}

	@RequestMapping("check/authCode/{phone}/{code}")
	public String checkAuthCode(@PathVariable String phone, @PathVariable String code) {
		return JSON.toJSONString(checkVerify(phone, code));
	}

	/**校验验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	public JSONObject checkVerify(String phone, String code) {
		JSONResponse response = new JSONResponse(new Parser(POST_GET, true)
				.parseResponse(new JSONRequest(new Verify(phone)).setTag(Verify.class.getSimpleName())));
		Verify verify = response.getObject(Verify.class);
		//验证码过期
		if (verify != null && System.currentTimeMillis() - verify.getDate() > 60000) {
			new Parser(DELETE, true).parseResponse(new JSONRequest(new Verify(phone))
					.setTag(Verify.class.getSimpleName()));
			return Parser.newErrorResult(new TimeoutException("验证码已过期！"));
		}

		return new JSONResponse(new Parser(POST_HEAD).parseResponse(
				new JSONRequest(new Verify(phone, code))));
	}


	private JSONObject newVerify(String phone, int code) {
		JSONObject verify = new JSONObject(true);
		verify.put("id", phone);
		if (code > 0) {
			verify.put("code", code);
		}
		return verify;
	}
	private JSONObject newVerifyRequest(JSONObject verify) {
		return newRequest(verify, "Verify", true);
	}



	@RequestMapping("get/login/{typeString}/{phone}/{password}")
	public String login(@PathVariable String typeString, @PathVariable String phone, @PathVariable String password) {
		if (StringUtil.isPhone(phone) == false) {
			return JSON.toJSONString(Parser.newErrorResult(new IllegalArgumentException("手机号不合法！")));
		}
		if (StringUtil.isNotEmpty(password, true) == false) {
			return JSON.toJSONString(Parser.newErrorResult(new IllegalArgumentException("密码/验证码不合法！")));
		}

		//手机号是否已注册
		JSONObject requestObject = new Parser(HEAD).parseResponse(
				new JSONRequest(new User().setPhone(phone)));
		JSONResponse response = new JSONResponse(requestObject).getJSONResponse(User.class.getSimpleName());
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(response);
		}
		if(JSONResponse.isExist(response) == false) {
			return JSON.toJSONString(Parser.newErrorResult(new NullPointerException("手机号未注册")));
		}

		//校验凭证
		int type = Integer.valueOf(0 + StringUtil.getNumber(typeString));
		if (type == Login.TYPE_PASSWORD) {//password
			response = new JSONResponse(new Parser(POST_HEAD).parseResponse(
					new JSONRequest(new Password(phone, password))));
		} else {//verify
			response = new JSONResponse(checkVerify(phone, password));
		}
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(response);
		}
		response = response.getJSONResponse(type == Login.TYPE_PASSWORD ? Password.class.getSimpleName() : Verify.class.getSimpleName());
		if (JSONResponse.isExist(response) == false) {
			return JSON.toJSONString(Parser.newErrorResult(new ConditionNotMatchException("账号或密码错误")));
		}


		//根据phone获取User
		JSONObject result = new Parser().parseResponse(new JSONRequest(new User().setPhone(phone)));
		response = new JSONResponse(result);

		User user = response == null ? null : response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) <= 0) {
			return JSON.toJSONString(Parser.extendErrorResult(result, null));
		}
		//删除Login
		new Parser(DELETE, true).parseResponse(new JSONRequest(new Login().setUserId(user.getId())));
		//写入Login
		new Parser(POST, true).parseResponse(new JSONRequest(
				new Login().setType(type).setUserId(user.getId())));

		return JSON.toJSONString(result);
	}



	@RequestMapping(value="post/register/user", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String register(@RequestBody String request) {
		JSONObject requestObject = null;
		try {
			requestObject = Parser.getCorrectRequest(POST
					, Parser.parseRequest(request, POST));
		} catch (Exception e) {
			return JSON.toJSONString(Parser.newErrorResult(e));
		}

		JSONObject user = requestObject == null ? null : requestObject.getJSONObject("User");
		String phone = user == null ? null : user.getString("phone");
		if (StringUtil.isPhone(phone) == false) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new IllegalArgumentException("User.phone: " + phone + " 不合法！")));
		}
		String password = StringUtil.getString(requestObject.getString("password"));
		if (password.length() < 6) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new IllegalArgumentException("User.password: " + password + " 不合法！不能小于6个字符！")));
		}
		//		String verify = StringUtil.getString(user.getString("verify"));
		//		if (verify.length() < 4) {
		//			return JSON.toJSONString(Parser.extendErrorResult(requestObject
		//					, new IllegalArgumentException("User.verify: " + verify + " 不合法！不能小于6个字符！")));
		//		}

		JSONResponse response = new JSONResponse(checkVerify(phone, requestObject.getString("verify")));
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(response);
		}
		//手机号或验证码错误
		if (JSONResponse.isExist(response.getJSONResponse(Verify.class.getSimpleName())) == false) {
			return JSON.toJSONString(Parser.extendErrorResult(response
					, new ConditionNotMatchException("手机号或验证码错误！")));
		}


		JSONObject check = new Parser(HEAD)
				.parseResponse(new JSONRequest(new User().setPhone(phone)));
		JSONObject checkUser = check == null ? null : check.getJSONObject("User");
		if (checkUser == null || checkUser.getIntValue("count") > 0) {
			return JSON.toJSONString(Parser.newErrorResult(new ConflictException("手机号" + phone + "已经注册")));
		}

		//生成User
		JSONObject result = new Parser(POST, true).parseResponse(requestObject);
		response = new JSONResponse(result);
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(Parser.extendErrorResult(result, null));
		}

		//生成Password
		response = new JSONResponse(new Parser(POST, true).parseResponse(
				new JSONRequest(new Password(phone, password))));
		if (JSONResponse.isSucceed(response.getJSONResponse(Password.class.getSimpleName())) == false) {
			new Parser(DELETE, true).parseResponse(new JSONRequest(new User().setPhone(phone)));
			new Parser(DELETE, true).parseResponse(new JSONRequest(new Password().setPhone(phone)));
			return JSON.toJSONString(Parser.extendErrorResult(result, null));
		}

		return JSON.toJSONString(result);
	}


	@RequestMapping(value="put/wallet", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String changeBalance(@RequestBody String request) {
		JSONObject requestObject = null;
		try {
			requestObject = Parser.getCorrectRequest(PUT, Parser.parseRequest(request, PUT));
		} catch (Exception e) {
			return JSON.toJSONString(Parser.newErrorResult(e));
		}

		//验证密码<<<<<<<<<<<<<<<<<<<<<<<
		
		JSONObject pwd = requestObject.getJSONObject(PASSWORD_);
		requestObject.remove(PASSWORD_);
		if (pwd.getIntValue(TYPE) != Password.TYPE_PAY) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new ConditionNotMatchException("Password type必须是支付类型！")));
		}

		JSONResponse response = new JSONResponse(new Parser(POST_HEAD).parseResponse(new JSONRequest(PASSWORD_, pwd)));
		response = response.getJSONResponse(PASSWORD_);
		if (response == null || response.isExist() == false) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new ConditionNotMatchException("支付密码错误！")));
		}
		//验证密码>>>>>>>>>>>>>>>>>>>>>>>>


		//验证金额范围<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject wallet = requestObject.getJSONObject(WALLET_);
		long id = wallet == null ? null : wallet.getLong(ID);
		if (id <= 0) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new ConditionNotMatchException("请设置Wallet及内部的id！")));
		}

		double change = wallet.getDoubleValue("balance+");
		if (change == 0) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new OutOfRangeException("balance+的值不能为0！")));
		}
		if (Math.abs(change) > 10000) {
			return JSON.toJSONString(Parser.extendErrorResult(requestObject
					, new OutOfRangeException("单次 充值/提现 的金额不能超过10000元！")));
		}

		if (change < 0) {//提现
			response = new JSONResponse(new Parser(POST_GET).parseResponse(new JSONRequest(new Wallet(id))));
			Wallet w = response == null ? null : response.getObject(Wallet.class);
			if (w == null) {
				return JSON.toJSONString(Parser.extendErrorResult(requestObject
						, new Exception("服务器内部错误！")));
			}

			if (w.getBalance() == null || w.getBalance().doubleValue() < -change) {
				return JSON.toJSONString(Parser.extendErrorResult(requestObject
						, new OutOfRangeException("余额不足！")));
			}
		}
		//验证金额范围>>>>>>>>>>>>>>>>>>>>>>>>

		return new Parser(PUT).parse(requestObject);
	}



	private JSONObject newRequest(JSONObject object, String name, boolean needTag) {
		JSONObject request = new JSONObject(true);
		request.put(name, object);
		if (needTag) {
			request.put(JSONRequest.KEY_TAG, name);
		}
		return request;
	}




}
