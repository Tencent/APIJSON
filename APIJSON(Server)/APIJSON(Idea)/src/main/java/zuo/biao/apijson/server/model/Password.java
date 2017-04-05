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

package zuo.biao.apijson.server.model;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.BaseModel;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;

/**登录类
 * @author Lemon
 */
@SuppressWarnings("serial")
@APIJSONRequest(
		method = {RequestMethod.POST_GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		DELETE = "{necessaryColumns:id}"
		)
public class Password extends BaseModel {

	private String model;//table是MySQL关键字不能用！
	private Integer type;
	private String password;

	public Password() {
		super();
	}
	public Password(String model, String phone) {
		this();
		setModel(model);
		setPhone(phone);
	}
	public Password(String model, String phone, String password) {
		this(model, phone);
		setPassword(password);
	}

	public Password setPhone(String phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}
	
	public String getModel() {
		return StringUtil.isNotEmpty(model, true) ? model : "User";
	}
	public Password setModel(String model) {
		this.model = model;
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
