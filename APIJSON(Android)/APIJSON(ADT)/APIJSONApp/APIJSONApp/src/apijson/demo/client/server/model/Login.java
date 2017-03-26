/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

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

/**登录类
 * @author Lemon
 */
@SuppressWarnings("serial")
@APIJSONRequest(
		method = {RequestMethod.POST_GET, RequestMethod.POST, RequestMethod.DELETE},
		DELETE = "{necessaryColumns:id}"
		)
public class Login extends BaseModel {

	public static final int TYPE_PASSWORD = 0;
	public static final int TYPE_VERIFY = 1;
	
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
