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
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;

/**验证码类
 * @author Lemon
 * @see
 * <br >POST_GET:post_get/authCode<pre>
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
@SuppressWarnings("serial")
@APIJSONRequest(
		method = {RequestMethod.POST_HEAD, RequestMethod.POST_GET, RequestMethod.POST},
		POST_GET = "{\"necessary\": \"id\"}",
		POST = "{\"disallow\": \"!\", \"necessary\": \"id\"}"
		)
public class Verify extends BaseModel {

	private String code;

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
	public Verify(String phone, String code) {
		this(phone);
		setCode(code);
	}


	public String getCode() {
		return code;
	}
	public Verify setCode(String code) {
		this.code = code;
		return this;
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
