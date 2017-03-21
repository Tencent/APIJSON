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


/**钱包类
 * @author Lemon
 */
public class Wallet extends apijson.demo.client.server.model.Wallet {
	private static final long serialVersionUID = 4298571449155754300L;
	
	
	public Wallet() {
		super();
	}
	public Wallet(long id) {
		super(id);
	}
	
	@Override
	public Long getId() {
		Long value = super.getId();
		return value == null ? 0 : value;
	}
	@Override
	public Long getUserId() {
		Long value = super.getUserId();
		return value == null ? 0 : value;
	}

}
