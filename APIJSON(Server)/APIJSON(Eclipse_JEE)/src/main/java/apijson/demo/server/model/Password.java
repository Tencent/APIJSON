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

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;

/**密码类
 * @author Lemon
 * @see
 * <br >POST_HEAD:<pre>
{
 "Password":{
     "disallow":"!",
     "necessary":"id,type"
 }
}
 * </pre>
 * <br >PUT:put/loginPassword, put/payPassword<pre>
{
    "Password":{
        "disallow":"!",
        "necessary":"id,type,password"
    },
    "necessary":"oldPassword"
}
 * </pre>
 */
@SuppressWarnings("serial")
@APIJSONRequest(
		method = {RequestMethod.POST_HEAD, RequestMethod.PUT},
		POST_HEAD = "{\"disallow\": \"!\", \"necessary\": \"id,type\"}",
		PUT = "{\"Password\": {\"disallow\": \"!\", \"necessary\": \"id,type,password\"}, \"necessary\": \"oldPassword\"}"
		)
public class Password extends BaseModel {

	public static final int TYPE_LOGIN = 0;
	public static final int TYPE_PAY = 1;
	
	private Integer type;
	private String password;

	public Password() {
		super();
	}
	public Password(String phone) {
		this();
		setPhone(phone);
	}
	public Password(String phone, String password) {
		this(phone);
		setPassword(password);
	}

	public Password setPhone(String phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}
	
	public Integer getType() {
		return type;
	}
	public Password setType(Integer type) {
		this.type = type;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public Password setPassword(String password) {
		this.password = password;
		return this;
	}

}
