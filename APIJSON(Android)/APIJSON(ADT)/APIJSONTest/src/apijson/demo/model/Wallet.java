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

package apijson.demo.model;

import java.math.BigDecimal;

import zuo.biao.apijson.APIJSONRequest;
import zuo.biao.apijson.RequestMethod;

/**钱包类
 * @author Lemon
 */
@APIJSONRequest(
		method = {RequestMethod.POST_GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
		POST_GET = "{Wallet:{disallowColumns:!, necessaryColumns:userId}, necessaryColumns:currentUserId,loginPassword}",
		DELETE = "{necessaryColumns:id}"
		)
public class Wallet extends BaseModel {
	private static final long serialVersionUID = 4298571449155754300L;
	
	public BigDecimal balance;
	
	private Long userId;
	
	/**默认构造方法，JSON等解析时必须要有
	 */
	public Wallet() {
		super();
	}
	public Wallet(long id) {
		this();
		setId(id);
	}
	
	public Wallet setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public Wallet setBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}

}
