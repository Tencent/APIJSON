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

package zuo.biao.apijson.server;

import java.util.Random;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.model.BaseModel;
import zuo.biao.apijson.server.model.Login;
import zuo.biao.apijson.server.model.Password;
import zuo.biao.apijson.server.model.User;
import zuo.biao.apijson.server.model.Verify;

/**request receiver and controller
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {

	@RequestMapping("head/{request}")
	public String head(@PathVariable String request) {
		return new RequestParser(RequestMethod.HEAD).parse(request);
	}

	@RequestMapping("get/{request}")
	public String get(@PathVariable String request) {
		return new RequestParser(RequestMethod.GET).parse(request);
	}


	@RequestMapping(value="post", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post(@RequestBody String request) {
		return new RequestParser(RequestMethod.POST).parse(request);
	}

	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_head", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post_head(@RequestBody String request) {
		return new RequestParser(RequestMethod.POST_HEAD).parse(request);
	}
	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_get", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String post_get(@RequestBody String request) {
		return new RequestParser(RequestMethod.POST_GET).parse(request);
	}

	/**以下接口继续用POST接口是为了客户端方便，只需要做get，post请求。也可以改用实际对应的方法。
	 * post，put方法名可以改为add，update等更客户端容易懂的名称
	 */
	@RequestMapping(value="put", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String put(@RequestBody String request) {
		return new RequestParser(RequestMethod.PUT).parse(request);
	}

	@RequestMapping(value="delete", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String delete(@RequestBody String request) {
		return new RequestParser(RequestMethod.DELETE).parse(request);
	}














	@RequestMapping("post/authCode/{phone}")
	public String postAuthCode(@PathVariable String phone) {
		new RequestParser(RequestMethod.DELETE).parse(newVerifyRequest(newVerify(phone, 0)));

		JSONObject response = new RequestParser(RequestMethod.POST).parseResponse(
				newVerifyRequest(newVerify(phone, new Random().nextInt(9999) + 1000)));

		JSONObject verify = null;
		try {
			verify = response.getJSONObject("Verify");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (verify == null || verify.getIntValue("status") != 200) {
			new RequestParser(RequestMethod.DELETE).parseResponse(new JSONRequest(new Verify(phone)));
			return JSON.toJSONString(RequestParser.extendErrorResult(response, null));
		}

		return getAuthCode(phone);
	}

	@RequestMapping(value="post_get/authCode/{phone}", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String getAuthCode(@PathVariable String phone) {
		return new RequestParser(RequestMethod.POST_GET).parse(newVerifyRequest(newVerify(phone, 0)));
	}

	@RequestMapping("check/authCode/{phone}/{code}")
	public String checkAuthCode(@PathVariable String phone, @PathVariable String code) {
		if (StringUtil.isNumer(code) == false) {
			code = "-1";
		}
		return new RequestParser(RequestMethod.POST_GET).parse(
				newVerifyRequest(newVerify(phone, Integer.parseInt(0 + StringUtil.getNumber(code)))));
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
			return JSON.toJSONString(RequestParser.newErrorResult(new IllegalArgumentException("手机号不合法！")));
		}
		if (StringUtil.isNotEmpty(password, true) == false) {
			return JSON.toJSONString(RequestParser.newErrorResult(new IllegalArgumentException("密码/验证码不合法！")));
		}

		//手机号是否已注册
		JSONObject requestObject = new RequestParser(RequestMethod.HEAD).parseResponse(
				new JSONRequest(new User().setPhone(phone)));
		JSONResponse response = new JSONResponse(requestObject).getJSONResponse(User.class.getSimpleName());
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(response);
		}
		if(JSONResponse.isExist(response) == false) {
			return JSON.toJSONString(RequestParser.newErrorResult(new NullPointerException("手机号未注册")));
		}

		//校验凭证
		int type = Integer.valueOf(0 + StringUtil.getNumber(typeString));
		if (type == Login.TYPE_PASSWORD) {//password
			response = new JSONResponse(new RequestParser(RequestMethod.HEAD).parseResponse(
					new JSONRequest(new Password(User.class.getSimpleName(), phone, password))));
		} else {//verify
			response = new JSONResponse(new RequestParser(RequestMethod.HEAD).parseResponse(
					new JSONRequest(new Verify(phone, password))));
		}
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(response);
		}
		response = response.getJSONResponse(type == Login.TYPE_PASSWORD ? Password.class.getSimpleName() : Verify.class.getSimpleName());
		if (JSONResponse.isExist(response) == false) {
			return JSON.toJSONString(RequestParser.newErrorResult(new ConditionNotMatchException("账号或密码错误")));
		}


		//根据phone获取User
		JSONObject result = new RequestParser().parseResponse(new JSONRequest(new User().setPhone(phone)));
		response = new JSONResponse(result);

		User user = response == null ? null : response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) <= 0) {
			return JSON.toJSONString(RequestParser.extendErrorResult(result, null));
		}
		//删除Login
		new RequestParser(RequestMethod.DELETE).parseResponse(new JSONRequest(new Login().setUserId(user.getId())));
		//写入Login
		new RequestParser(RequestMethod.POST).parseResponse(new JSONRequest(
				new Login().setType(type).setUserId(user.getId())));

		return JSON.toJSONString(result);
	}



	@RequestMapping(value="post/register/user", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public String register(@RequestBody String request) {
		JSONObject requestObject = null;
		try {
			requestObject = RequestParser.getCorrectRequest(RequestMethod.POST
					, RequestParser.parseRequest(request, RequestMethod.POST));
		} catch (Exception e) {
			return JSON.toJSONString(RequestParser.newErrorResult(e));
		}

		JSONObject user = requestObject == null ? null : requestObject.getJSONObject("User");
		String phone = user == null ? null : user.getString("phone");
		if (StringUtil.isPhone(phone) == false) {
			return JSON.toJSONString(RequestParser.extendErrorResult(requestObject
					, new IllegalArgumentException("User.phone: " + phone + " 不合法！")));
		}
		String password = StringUtil.getString(requestObject.getString("password"));
		if (password.length() < 6) {
			return JSON.toJSONString(RequestParser.extendErrorResult(requestObject
					, new IllegalArgumentException("User.password: " + password + " 不合法！不能小于6个字符！")));
		}
		//		String verify = StringUtil.getString(user.getString("verify"));
		//		if (verify.length() < 4) {
		//			return JSON.toJSONString(RequestParser.extendErrorResult(requestObject
		//					, new IllegalArgumentException("User.verify: " + verify + " 不合法！不能小于6个字符！")));
		//		}

		JSONResponse response = new JSONResponse(new RequestParser(RequestMethod.POST_GET).parseResponse(new JSONRequest(
				new Verify(phone))));
		Verify verify = response.getObject(Verify.class);
		//验证码过期
		if (verify != null && System.currentTimeMillis() - verify.getDate() > 60000) {
			new JSONResponse(new RequestParser(RequestMethod.DELETE).parseResponse(new JSONRequest(
					new Verify(phone))));
			verify = null;
		}
		//手机号或验证码错误
		if (verify == null || ("" + verify.getId()).equals(phone) == false) {
			return JSON.toJSONString(RequestParser.extendErrorResult(requestObject
					, new ConditionNotMatchException("手机号或验证码错误！")));
		}


		JSONObject check = new RequestParser(RequestMethod.HEAD)
				.parseResponse(newUserRequest(newUser(phone)));
		JSONObject checkUser = check == null ? null : check.getJSONObject("User");
		if (checkUser == null || checkUser.getIntValue("count") > 0) {
			return JSON.toJSONString(RequestParser.newErrorResult(new ConflictException("手机号" + phone + "已经注册")));
		}

		//生成User
		JSONObject result = new RequestParser(RequestMethod.POST).parseResponse(requestObject);
		response = new JSONResponse(result);
		if (JSONResponse.isSucceed(response) == false) {
			return JSON.toJSONString(RequestParser.extendErrorResult(result, null));
		}
		
		//生成Password
		response = new JSONResponse(new RequestParser(RequestMethod.POST).parseResponse(
				new JSONRequest(new Password(User.class.getSimpleName(), phone, password))));
		if (JSONResponse.isSucceed(response.getJSONResponse(Password.class.getSimpleName())) == false) {
			new RequestParser(RequestMethod.DELETE).parseResponse(new JSONRequest(new User().setPhone(phone)));
			new RequestParser(RequestMethod.DELETE).parseResponse(new JSONRequest(new Password().setPhone(phone)));
			return JSON.toJSONString(RequestParser.extendErrorResult(result, null));
		}

		return JSON.toJSONString(result);
	}





	private JSONObject newUser(String phone) {
		JSONObject verify = new JSONObject(true);
		verify.put("phone", phone);
		return verify;
	}
	private JSONObject newUserRequest(JSONObject user) {
		return newRequest(user, "User", true);
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
