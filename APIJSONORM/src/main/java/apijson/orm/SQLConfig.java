/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.util.List;
import java.util.Map;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;

/**SQL配置
 * @author Lemon
 */
public interface SQLConfig<T extends Object> {

	String DATABASE_MYSQL = "MYSQL"; // https://www.mysql.com
	String DATABASE_POSTGRESQL = "POSTGRESQL"; // https://www.postgresql.org
	String DATABASE_SQLSERVER = "SQLSERVER"; // https://www.microsoft.com/en-us/sql-server
	String DATABASE_ORACLE = "ORACLE"; // https://www.oracle.com/database
	String DATABASE_DB2 = "DB2"; // https://www.ibm.com/products/db2
    String DATABASE_MARIADB = "MARIADB"; // https://mariadb.org
    String DATABASE_TIDB = "TIDB"; // https://www.pingcap.com/tidb
    String DATABASE_DAMENG = "DAMENG"; // https://www.dameng.com
    String DATABASE_KINGBASE = "KINGBASE"; // https://www.kingbase.com.cn
    String DATABASE_ELASTICSEARCH = "ELASTICSEARCH"; // https://www.elastic.co/guide/en/elasticsearch/reference/7.4/xpack-sql.html
    String DATABASE_CLICKHOUSE = "CLICKHOUSE"; // https://clickhouse.com
	String DATABASE_HIVE = "HIVE"; // https://hive.apache.org
	String DATABASE_PRESTO = "PRESTO"; // Facebook PrestoDB  https://prestodb.io
	String DATABASE_TRINO = "TRINO"; // PrestoSQL  https://trino.io
	String DATABASE_SNOWFLAKE = "SNOWFLAKE"; // https://www.snowflake.com
	String DATABASE_DATABRICKS = "DATABRICKS"; // https://www.databricks.com
	String DATABASE_CASSANDRA = "CASSANDRA"; // https://cassandra.apache.org
	String DATABASE_INFLUXDB = "INFLUXDB"; // https://www.influxdata.com/products/influxdb-overview
	String DATABASE_TDENGINE = "TDENGINE"; // https://tdengine.com
	String DATABASE_REDIS = "REDIS";
	String DATABASE_MQ = "MQ";

	String SCHEMA_INFORMATION = "information_schema";  //MySQL, PostgreSQL, SQL Server 都有的系统模式
	String SCHEMA_SYS = "sys";  //SQL Server 系统模式
	String TABLE_SCHEMA = "table_schema";
	String TABLE_NAME = "table_name";

	int TYPE_CHILD = 0;
	int TYPE_ITEM = 1;
	int TYPE_ITEM_CHILD_0 = 2;

	Parser<T> getParser();

	AbstractSQLConfig setParser(Parser<T> parser);

	ObjectParser getObjectParser();

	AbstractSQLConfig setObjectParser(ObjectParser objectParser);

	int getVersion();

	AbstractSQLConfig setVersion(int version);

	String getTag();

	AbstractSQLConfig setTag(String tag);

	boolean isMySQL();
	boolean isPostgreSQL();
	boolean isSQLServer();
	boolean isOracle();
	boolean isDb2();
	boolean isMariaDB();
	boolean isTiDB();
	boolean isDameng();
	boolean isKingBase();
	boolean isElasticsearch();
	boolean isClickHouse();
	boolean isHive();
	boolean isPresto();
	boolean isSnowflake();
	boolean isDatabricks();
	boolean isCassandra();
	boolean isTrino();
	boolean isInfluxDB();
	boolean isTDengine();
	boolean isRedis();
	boolean isMQ();


	//暂时只兼容以上几种
	//	boolean isSQL();
	//	boolean isTSQL();
	//	boolean isPLSQL();
	//	boolean isAnsiSQL();

    /**用来给 Table, Column 等系统属性表来绕过 MAX_SQL_COUNT 等限制
     * @return
     */
	boolean limitSQLCount();

	/**是否开启 WITH AS 表达式来简化 SQL 和提升性能
	 * @return
	 */
	boolean isWithAsEnable();
	/**允许增删改部分失败
     * @return
     */
    boolean allowPartialUpdateFailed();

    @NotNull
	String getIdKey();
	@NotNull
	String getUserIdKey();


	/**获取数据库版本号，可通过判断版本号解决一些 JDBC 驱动连接数据库的兼容问题
	 * MYSQL: 8.0, 5.7, 5.6 等； PostgreSQL: 11, 10, 9.6 等
	 * @return
	 */
	String getDBVersion();

	@NotNull
	default int[] getDBVersionNums() {
		String dbVersion = StringUtil.getNoBlankString(getDBVersion());
		if (dbVersion.isEmpty()) {
			return new int[]{0};
		}

		int index = dbVersion.indexOf("-");
		if (index > 0) {
			dbVersion = dbVersion.substring(0, index);
		}

		String[] ss = dbVersion.split("[.]");
		int[] nums = new int[Math.max(1, ss.length)];
		for (int i = 0; i < ss.length; i++) {
			nums[i] = Integer.valueOf(ss[i]);
		}

		return nums;
	}

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

	Boolean getCompat();
	SQLConfig setCompat(Boolean compat);

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

	Object getIdIn();
	SQLConfig setIdIn(Object idIn);

	Object getUserId();
	SQLConfig setUserId(Object userId);

	Object getUserIdIn();
	SQLConfig setUserIdIn(Object userIdIn);

	String getRole();
	SQLConfig setRole(String role);

	public boolean isDistinct();
	public SQLConfig setDistinct(boolean distinct);

	String getDatabase();
	SQLConfig setDatabase(String database);

	String getSchema();
	SQLConfig setSchema(String schema);

	String getDatasource();
	SQLConfig setDatasource(String datasource);

	String getQuote();

	List<String> getJson();
	SQLConfig setJson(List<String> json);

	/**请求传进来的Table名
	 * @return
	 * @see {@link #getSQLTable()}
	 */
	String getTable();

	SQLConfig setTable(String table);

	/**数据库里的真实Table名
	 * 通过 {@link AbstractSQLConfig.TABLE_KEY_MAP} 映射
	 * @return
	 */
	String getSQLTable();

	String getTablePath();

	List<String> getRaw();
	SQLConfig setRaw(List<String> raw);

	Subquery getFrom();
	SQLConfig setFrom(Subquery from);

	List<String> getColumn();
	SQLConfig setColumn(List<String> column);

	List<List<Object>> getValues();
	SQLConfig setValues(List<List<Object>> values);

	Map<String, Object> getContent();
	SQLConfig setContent(Map<String, Object> content);

	Map<String, List<String>> getCombineMap();
	SQLConfig setCombineMap(Map<String, List<String>> combineMap);

	String getCombine();
	SQLConfig setCombine(String combine);

	Map<String, String> getCast();
	SQLConfig setCast(Map<String, String> cast);

	List<String> getNull();
	SQLConfig setNull(List<String> nulls);

	Map<String, Object> getWhere();
	SQLConfig setWhere(Map<String, Object> where);

	String getGroup();
	SQLConfig setGroup(String group);

	Map<String, Object> getHaving();
	SQLConfig setHaving(Map<String, Object> having);

	String getHavingCombine();
	SQLConfig setHavingCombine(String havingCombine);

	String getOrder();
	SQLConfig setOrder(String order);

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
	String getRawSQL(String key, Object value, boolean throwWhenMissing) throws Exception;

	boolean isKeyPrefix();

	SQLConfig setKeyPrefix(boolean keyPrefix);


	List<Join> getJoinList();

	SQLConfig setJoinList(List<Join> joinList);

	boolean hasJoin();


	String getSubqueryString(Subquery subquery) throws Exception;

	SQLConfig setProcedure(String procedure);



	List<Object> getWithAsExprPreparedValueList();
	SQLConfig setWithAsExprPreparedValueList(List<Object> withAsExprePreparedValueList);

	boolean isFakeDelete();

	Map<String, Object> onFakeDelete(Map<String, Object> map);
}
