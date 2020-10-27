/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;

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
	 * @see {@link apijson.JSONObject#KEY_ROLE} 
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

	
	String getVisitorIdKey(SQLConfig config);

}
