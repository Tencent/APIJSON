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

package apijson.demo.client.model;

import zuo.biao.library.util.StringUtil;


/**钱包类
 * @author Lemon
 */
public class Verify extends apijson.demo.client.server.model.Verify {
	private static final long serialVersionUID = 4298571449155754300L;

	public Verify() {
		super();
	}
	public Verify(long phone) {
		super(phone);
	}
	public Verify(String code) {
		this();
		setCode(code);
	}

	@Override
	public Long getId() {
		return value(super.getId());
	}
	
	/**服务器用id作为phone
	 * @return
	 */
	public String getPhone() {
		return "" + getId();
	}
	public Verify setPhone(String phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}
	public Verify setPhone(Long phone) {
		setId(Long.valueOf(0 + StringUtil.getNumber(phone)));
		return this;
	}

}
