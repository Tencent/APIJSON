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

package apijson.demo.client.model;

/**登录类
 * @author Lemon
 */
@SuppressWarnings("serial")
public class Login extends apijson.demo.client.server.model.Login {

	public Login() {
		super();
	}
	public Login(long userId) {
		this();
		setUserId(userId);
	}

	@Override
	public Long getUserId() {
		Long userId = super.getUserId();
		return userId == null ? 0 : userId;
	}
//	@Override
//	public Boolean getType() {
//		Boolean type = super.getType();
//		return type == null ? false : type;
//	}
	@Override
	public Integer getType() {
		Integer type = super.getType();
		return type == null ? 0 : type;
	}

}
