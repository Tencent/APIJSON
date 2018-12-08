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

package zuo.biao.apijson;

/**来访的用户角色
 * @author Lemon
 */
public enum RequestRole {

	/**未登录，不明身份的用户
	 */
	UNKNOWN,

	/**已登录的用户
	 */
	LOGIN,

	/**联系人，必须已登录
	 */
	CONTACT,

	/**圈子成员(CONTACT + OWNER)，必须已登录
	 */
	CIRCLE,

	/**拥有者，必须已登录
	 */
	OWNER,

	/**管理员，必须已登录
	 */
	ADMIN;

	//似乎不管怎么做，外部引用后都是空值。并且如果在注解内的位置不是最前的，还会导致被注解的类在其它类中import报错。
	//虽然直接打印显示正常，但被@MethodAccess内RequestRole[] GET()等方法引用后获取的是空值
	//	public static final RequestRole[] ALL = {RequestRole.UNKNOWN};//values();//所有
	//	public static final RequestRole[] HIGHS;//高级
	//	static {
	//		HIGHS = new RequestRole[] {OWNER, ADMIN};
	//	}

	public static final String[] NAMES = {
			UNKNOWN.name(), LOGIN.name(), CONTACT.name(), CIRCLE.name(), OWNER.name(), ADMIN.name()
	};

	public static RequestRole get(String name) throws Exception {
		if (name == null) {
			return null;
		}
		try { //Enum.valueOf只要找不到对应的值就会抛异常
			return RequestRole.valueOf(StringUtil.toUpperCase(name));
		} catch (Exception e) {
			throw new IllegalArgumentException("角色 " + name + " 不存在！只能是[" + StringUtil.getString(NAMES) + "]中的一种！", e);
		}
	}

}
