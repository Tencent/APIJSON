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
import static zuo.biao.apijson.RequestRole.CONTACT;
import static zuo.biao.apijson.RequestRole.LOGIN;
import static zuo.biao.apijson.RequestRole.OWNER;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

import zuo.biao.apijson.MethodAccess;

/**登录类，已用session替代
 * @author Lemon
 */
@Deprecated
@SuppressWarnings("serial")
@MethodAccess(
		GETS = {UNKNOWN, LOGIN, CONTACT, OWNER, ADMIN},
		HEADS = {UNKNOWN, LOGIN, CONTACT, OWNER, ADMIN},
		POST = {UNKNOWN, LOGIN, CONTACT, OWNER, ADMIN},
		PUT = {UNKNOWN, LOGIN, CONTACT, OWNER, ADMIN},
		DELETE = {UNKNOWN, LOGIN, CONTACT, OWNER, ADMIN}
		)
public class Login extends BaseModel {

	public static final int TYPE_PASSWORD = 0;//密码登录
	public static final int TYPE_VERIFY = 1;//验证码登录
	
	private Long userId;
	private Integer type;

	public Login() {
		super();
	}
	public Login(long userId) {
		this();
		setUserId(userId);
	}

	public Long getUserId() {
		return userId;
	}
	public Login setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	public Integer getType() {
		return type;
	}
	public Login setType(Integer type) {
		this.type = type;
		return this;
	}

}
