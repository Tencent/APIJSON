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
	//暂时只兼容以上 4 种
	//	boolean isSQL();
	//	boolean isPLSQL();
	//	boolean isTransactSQL();

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
	SQLConfig setRole(RequestRole role);

	public boolean isDistinct();
	public SQLConfig setDistinct(boolean distinct);

	String getDatabase();
	SQLConfig setDatabase(String database);

	String getQuote();

	String getSchema();
	SQLConfig setSchema(String schema);

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

	boolean isKeyPrefix();

	SQLConfig setKeyPrefix(boolean keyPrefix);


	List<Join> getJoinList();

	SQLConfig setJoinList(List<Join> joinList);

	boolean hasJoin();


	String getSubqueryString(Subquery subquery) throws Exception;

	SQLConfig setProcedure(String procedure);

}
