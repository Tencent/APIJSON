/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.util.List;
import java.util.Map;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;

/**SQL配置
 * @author Lemon
 */
public interface SQLConfig {

	String DATABASE_MYSQL = "MYSQL";
	String DATABASE_POSTGRESQL = "POSTGRESQL";
	String DATABASE_SQLSERVER = "SQLSERVER";
	String DATABASE_ORACLE = "ORACLE";
	String DATABASE_DB2 = "DB2";

	String SCHEMA_INFORMATION = "information_schema";  //MySQL, PostgreSQL, SQL Server 都有的系统模式
	String SCHEMA_SYS = "sys";  //SQL Server 系统模式
	String TABLE_SCHEMA = "table_schema";
	String TABLE_NAME = "table_name";

	int TYPE_CHILD = 0;
	int TYPE_ITEM = 1;
	int TYPE_ITEM_CHILD_0 = 2;

	boolean isMySQL();
	boolean isPostgreSQL();
	boolean isSQLServer();
	boolean isOracle();
	boolean isDb2();
	//暂时只兼容以上 5 种
	//	boolean isSQL();
	//	boolean isTSQL();
	//	boolean isPLSQL();
	//	boolean isAnsiSQL();

	boolean limitSQLCount(); //用来给 Table, Column 等系统属性表来绕过 MAX_SQL_COUNT 等限制 
	
	@NotNull
	String getIdKey();
	@NotNull
	String getUserIdKey();


	/**获取数据库版本号，可通过判断版本号解决一些 JDBC 驱动连接数据库的兼容问题
	 * MYSQL: 8.0, 5.7, 5.6 等； PostgreSQL: 11, 10, 9.6 等
	 * @return
	 */
	String getDBVersion();

	/**获取数据库地址
	 * @return
	 */
	String getDBUri();

	/**获取数据库账号
	 * @return
	 */
	String getDBAccount();

	/**获取数据库密码
	 * @return
	 */
	String getDBPassword();

	/**获取SQL语句
	 * @return
	 * @throws Exception
	 */
	String getSQL(boolean prepared) throws Exception;



	boolean isTest();
	SQLConfig setTest(boolean test);

	int getType();
	SQLConfig setType(int type);

	int getCount();
	SQLConfig setCount(int count);

	int getPage();
	SQLConfig setPage(int page);

	int getQuery();
	SQLConfig setQuery(int query);

	int getPosition();
	SQLConfig setPosition(int position);

	int getCache();
	SQLConfig setCache(int cache);

	boolean isExplain();
	SQLConfig setExplain(boolean explain);


	RequestMethod getMethod();
	SQLConfig setMethod(RequestMethod method);

	Object getId();
	SQLConfig setId(Object id);

	RequestRole getRole();
	SQLConfig setRole(RequestRole role);  // TODO 提供 String 类型的，方便扩展

	public boolean isDistinct();
	public SQLConfig setDistinct(boolean distinct);

	String getDatabase();
	SQLConfig setDatabase(String database);

	String getSchema();
	SQLConfig setSchema(String schema);
	
	String getDatasource();
	SQLConfig setDatasource(String datasource);

	String getQuote();

	/**请求传进来的Table名
	 * @return
	 * @see {@link #getSQLTable()}
	 */
	String getTable();
	/**数据库里的真实Table名
	 * 通过 {@link #TABLE_KEY_MAP} 映射
	 * @return
	 */
	String getSQLTable();

	String getTablePath();

	List<String> getRaw();
	SQLConfig setRaw(List<String> raw);

	SQLConfig setTable(String table);

	String getGroup();
	SQLConfig setGroup(String group);

	String getHaving();
	SQLConfig setHaving(String having);

	String getOrder();
	SQLConfig setOrder(String order);

	List<String> getJson();
	SQLConfig setJson(List<String> json);
	
	Subquery getFrom();
	SQLConfig setFrom(Subquery from);

	List<String> getColumn();
	SQLConfig setColumn(List<String> column);

	List<List<Object>> getValues();
	SQLConfig setValues(List<List<Object>> values);

	Map<String, Object> getContent();
	SQLConfig setContent(Map<String, Object> content);

	Map<String, Object> getWhere();
	SQLConfig setWhere(Map<String, Object> where);

	Map<String, List<String>> getCombine();
	SQLConfig setCombine(Map<String, List<String>> combine);



	/**
	 * exactMatch = false
	 * @param key
	 * @return
	 */
	Object getWhere(String key);
	/**
	 * @param key
	 * @param exactMatch
	 * @return
	 */
	Object getWhere(String key, boolean exactMatch);
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	SQLConfig putWhere(String key, Object value, boolean prior);


	boolean isPrepared();

	SQLConfig setPrepared(boolean prepared);

	boolean isMain();

	SQLConfig setMain(boolean main);


	List<Object> getPreparedValueList();
	SQLConfig setPreparedValueList(List<Object> preparedValueList);


	String getAlias();

	SQLConfig setAlias(String alias);

	String getWhereString(boolean hasPrefix) throws Exception;

	String getRawSQL(String key, Object value) throws Exception;

	boolean isKeyPrefix();

	SQLConfig setKeyPrefix(boolean keyPrefix);


	List<Join> getJoinList();

	SQLConfig setJoinList(List<Join> joinList);

	boolean hasJoin();


	String getSubqueryString(Subquery subquery) throws Exception;

	SQLConfig setProcedure(String procedure);


}
