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

package zuo.biao.apijson.server;

import zuo.biao.apijson.NotNull;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;

/**权限验证器
 * @author Lemon
 */
public interface Verifier<T> {

	/**验证权限是否通过
	 * @param config
	 * @param visitor
	 * @return
	 * @throws Exception
	 */
	boolean verify(SQLConfig config) throws Exception;

	/**允许请求，角色不好判断，让访问者发过来角色名，OWNER,CONTACT,ADMIN等
	 * @param table
	 * @param method
	 * @param role
	 * @return
	 * @throws Exception 
	 * @see {@link zuo.biao.apijson.JSONObject#KEY_ROLE} 
	 */
	void verifyRole(String table, RequestMethod method, RequestRole role) throws Exception;

	/**登录校验
	 * @param config
	 * @throws Exception
	 */
	void verifyLogin() throws Exception;
	/**管理员角色校验
	 * @param config
	 * @throws Exception
	 */
	void verifyAdmin() throws Exception;



	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	void verifyRepeat(String table, String key, Object value) throws Exception;
	
	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	void verifyRepeat(String table, String key, Object value, long exceptId) throws Exception;
	

	@NotNull
	Parser<T> createParser();
	

	@NotNull
	Visitor<T> getVisitor();
	Verifier<T> setVisitor(@NotNull Visitor<T> visitor);

	
	String getVisitorKey();
	
	String getVisitorIdKey();
	
	String getVisitorIdKey(String table);





}
