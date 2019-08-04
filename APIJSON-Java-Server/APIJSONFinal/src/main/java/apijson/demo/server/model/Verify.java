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

package apijson.demo.server.model;

import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.CIRCLE;
import static zuo.biao.apijson.RequestRole.CONTACT;
import static zuo.biao.apijson.RequestRole.LOGIN;
import static zuo.biao.apijson.RequestRole.OWNER;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

import zuo.biao.apijson.MethodAccess;

/**验证码
 * @author Lemon
 */
@MethodAccess(
		GET = {},
		HEAD = {},
		GETS = {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN},
		HEADS = {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN},
		POST = {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN},
		PUT = {ADMIN},
		DELETE = {ADMIN}
		)
public class Verify extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_LOGIN = 0; //登录
	public static final int TYPE_REGISTER = 1; //注册
	public static final int TYPE_PASSWORD = 2; //登录密码
	public static final int TYPE_PAY_PASSWORD = 3; //支付密码
	public static final int TYPE_RELOAD = 4; //重载配置
	
	private String phone;	//手机
	private String verify;	//验证码
	private Integer type;	//验证类型

	public Verify() {
		super();
	}
	/**type和phone为联合主键，必传
	 * @param type
	 * @param phone
	 */
	public Verify(int type, String phone) {
		this();
		setType(type);
		setPhone(phone);
	}


	public String getVerify() {
		return verify;
	}
	public Verify setVerify(String verify) {
		this.verify = verify;
		return this;
	}

	public String getPhone() {
		return phone;
	}
	public Verify setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public Integer getType() {
		return type;
	}
	public Verify setType(Integer type) {
		this.type = type;
		return this;
	}

}
