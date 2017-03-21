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

import zuo.biao.apijson.StringUtil;

/**登录类
 * @author Lemon
 */
@SuppressWarnings("serial")
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
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
	}
	public Password(String model, String phone, String value) {
		this(model, phone);
		setPassword(value);
	}

	public String getModel() {
		return model;
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
