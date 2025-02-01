/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;

/**简化Parser，getObject和getArray(getArrayConfig)都能用
 * @author Lemon
 */
public interface ObjectParser<T extends Object> {

	Parser<T> getParser();
	ObjectParser<T> setParser(Parser<T> parser);

	String getParentPath();
	ObjectParser<T> setParentPath(String parentPath);

	ObjectParser<T> setCache(JSONObject cache);
	JSONObject getCache();


	/**解析成员
	 * response重新赋值
	 * @param name
	 * @param isReuse 
	 * @return null or this
	 * @throws Exception
	 */
	ObjectParser<T> parse(String name, boolean isReuse) throws Exception;

	/**调用 parser 的 sqlExecutor 来解析结果
	 * @param method
	 * @param table
	 * @param alias
	 * @param request
	 * @param joinList
	 * @param isProcedure
	 * @return
	 * @throws Exception
	 */
	JSONObject parseResponse(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception;
	/**调用 parser 的 sqlExecutor 来解析结果
	 * @param config
	 * @param isProcedure
	 * @return
	 * @throws Exception
	 */
	JSONObject parseResponse(SQLConfig<T> config, boolean isProcedure) throws Exception;



	/**解析普通成员
	 * @param key
	 * @param value
	 * @return whether parse succeed
	 */
	boolean onParse(@NotNull String key, @NotNull Object value) throws Exception;

	/**解析子对象
	 * @param index
	 * @param key
	 * @param value
	 * @param cache SQL 结果缓存
	 * @return
	 * @throws Exception
	 */
	JSON onChildParse(int index, String key, JSONObject value, JSON cache) throws Exception;
	
	/**解析赋值引用
	 * @param path
	 * @return
	 */
	Object onReferenceParse(@NotNull String path);

	//TODO 改用 MySQL json_add,json_remove,json_contains 等函数！ 
	/**修改数组 PUT key:[]
	 * @param key
	 * @param array
	 * @throws Exception
	 */
	void onPUTArrayParse(@NotNull String key, @NotNull JSONArray array) throws Exception;

	/**批量新增或修改 POST or PUT  Table[]:[{}]
	 * @param key
	 * @param array
	 * @throws Exception
	 */
	void onTableArrayParse(@NotNull String key, @NotNull JSONArray array) throws Exception;

	/**SQL 配置，for single object
	 * @return {@link #setSQLConfig(int, int, int)}
	 * @throws Exception
	 */
	ObjectParser<T> setSQLConfig() throws Exception;

	/**SQL 配置
	 * @return 
	 * @throws Exception
	 */
	ObjectParser<T> setSQLConfig(int count, int page, int position) throws Exception;
	
	
	/**执行 SQL
	 * @return 
	 * @throws Exception
	 */
	ObjectParser<T> executeSQL() throws Exception;

	
	/**
	 * @return
	 * @throws Exception
	 */
	JSONObject onSQLExecute() throws Exception;
	
	
	/**
	 * @return response
	 * @throws Exception
	 */
	JSONObject response() throws Exception;

	void onFunctionResponse(String type) throws Exception;

	void onChildResponse() throws Exception;
	

	SQLConfig<T> newSQLConfig(boolean isProcedure) throws Exception;
	SQLConfig<T> newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception;
	
	/**
	 * response has the final value after parse (and query if isTableKey)
	 */
	void onComplete();


	/**回收内存
	 */
	void recycle();


	ObjectParser<T> setMethod(RequestMethod method);
	RequestMethod getMethod();


	boolean isTable();
	String getPath();
	String getTable();
	String getAlias();
	SQLConfig<T> getArrayConfig();

	SQLConfig<T> getSQLConfig();
	JSONObject getResponse();
	JSONObject getSqlRequest();
	JSONObject getSqlResponse();

	Map<String, Object> getCustomMap();
	Map<String, Map<String, String>> getFunctionMap();
	Map<String, JSONObject> getChildMap();

}
