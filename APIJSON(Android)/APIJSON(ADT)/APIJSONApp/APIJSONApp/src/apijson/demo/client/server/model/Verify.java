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

package apijson.demo.client.server.model;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;

/**登录类
 * @author Lemon
 */
@SuppressWarnings("serial")
@APIJSONRequest(
		method = {RequestMethod.POST_HEAD, RequestMethod.POST_GET, RequestMethod.POST, RequestMethod.DELETE},
		DELETE = "{necessaryColumns:id}"
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

	public Verify setPhone(String phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}
}
