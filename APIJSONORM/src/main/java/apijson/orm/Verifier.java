/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;

/**校验器(权限、请求参数、返回结果等)
 * @author Lemon
 */
public interface Verifier<T> {


	/**验证权限是否通过
	 * @param config
	 * @param visitor
	 * @return
	 * @throws Exception
	 */
	boolean verifyAccess(SQLConfig config) throws Exception;


	/**校验请求使用的角色，角色不好判断，让访问者发过来角色名，OWNER,CONTACT,ADMIN等
	 * @param config
	 * @param table
	 * @param method
	 * @param role
	 * @return
	 * @throws Exception 
	 * @see {@link apijson.JSONObject#KEY_ROLE} 
	 */
	void verifyRole(SQLConfig config, String table, RequestMethod method, String role) throws Exception;

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
	
	/**验证请求参数的数据和结构
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	JSONObject verifyRequest(RequestMethod method, String name, JSONObject target, JSONObject request,
			int maxUpdateCount, String globalDatabase, String globalSchema, SQLCreator creator) throws Exception;

	/**验证返回结果的数据和结构
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	JSONObject verifyResponse(RequestMethod method, String name, JSONObject target, JSONObject response,
			String database, String schema, SQLCreator creator, OnParseCallback callback) throws Exception;


	@NotNull
	Parser<T> createParser();

	@NotNull
	Visitor<T> getVisitor();
	Verifier<T> setVisitor(@NotNull Visitor<T> visitor);
	
	String getVisitorIdKey(SQLConfig config);


}
