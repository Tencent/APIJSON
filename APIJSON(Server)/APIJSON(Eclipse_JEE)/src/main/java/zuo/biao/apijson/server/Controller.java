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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;

/**request receiver and controller
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {

	@RequestMapping("get/{request}")
	public String get(@PathVariable String request) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.GET).parse(request);
	}

	/**用POST方法GET数据，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post_get", method = RequestMethod.POST)
	public String post_get(@RequestBody String request) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.POST_GET).parse(request);
	}

	@RequestMapping(value="post", method = RequestMethod.POST)
	public String post(@RequestBody String request) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.POST).parse(request);
	}

	/**以下接口继续用POST接口是为了客户端方便，只需要做get，post请求。也可以改用实际对应的方法。
	 * post，put方法名可以改为add，update等更客户端容易懂的名称
	 */
	@RequestMapping(value="put", method = RequestMethod.POST)
	public String put(@RequestBody String request) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.PUT).parse(request);
	}

	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(@RequestBody String request) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.DELETE).parse(request);
	}


	@RequestMapping("post/authCode/{phone}")
	public String postAuthCode(@PathVariable String phone) {
		new RequestParser(zuo.biao.apijson.RequestMethod.DELETE).parse(newVerifyRequest(newVerify(phone, 0)));
		
		JSONObject response = new RequestParser(zuo.biao.apijson.RequestMethod.POST).parseResponse(
				newVerifyRequest(newVerify(phone, new Random().nextInt(9999) + 1000)));
		
		JSONObject verify = null;
		try {
			 verify = response.getJSONObject("Verify");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (verify == null || verify.getIntValue("status") != 200) {
			return JSON.toJSONString(response);
		}
		
		return getAuthCode(phone);
	}
	
	@RequestMapping(value="post_get/authCode/{phone}", method = RequestMethod.POST)
	public String getAuthCode(@PathVariable String phone) {
		return new RequestParser(zuo.biao.apijson.RequestMethod.POST_GET).parse(newVerifyRequest(newVerify(phone, 0)));
	}
	
	@RequestMapping("check/authCode/{phone}/{code}")
	public String checkAuthCode(@PathVariable String phone, @PathVariable String code) {
		if (StringUtil.isNumer(code) == false) {
			code = "-1";
		}
		return new RequestParser(zuo.biao.apijson.RequestMethod.POST_GET).parse(
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
	private JSONObject newRequest(JSONObject object, String name, boolean needTag) {
		JSONObject request = new JSONObject(true);
		request.put(name, object);
		if (needTag) {
			request.put("tag", name);
		}
		return request;
	}

}
