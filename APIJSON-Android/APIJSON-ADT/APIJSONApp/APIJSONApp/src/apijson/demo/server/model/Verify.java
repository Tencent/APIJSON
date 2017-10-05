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
import zuo.biao.apijson.StringUtil;

/**验证码
 * @author Lemon
 * @see
 * <br >GETS:gets/authCode<pre>
{
    "Verify":{
        "disallow":"id"
    }
}
 * </pre>
 * <br >POST:post/authCode<pre>
{
    "Verify":{
        "disallow":"!",
        "necessary":"id"
    }
}
 * </pre>
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
	
	private String verify;

	public Verify() {
		super();
	}
	public Verify(String phone) {
		this();
		setPhone(phone);
	}
	public Verify(Long phone) {
		this();
		setId(phone);
	}
	public Verify(Long phone, Integer verify) {
		this(phone);
		setVerify(verify == null ? null : ("" + verify));
	}
	public Verify(String phone, String verify) {
		this(phone);
		setVerify(verify);
	}


	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}

	//phone is not column
	//	public String getPhone() {
	//		return StringUtil.getString(getId());
	//	}
	public Verify setPhone(String phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}
}
