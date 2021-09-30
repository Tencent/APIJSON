/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.io.BufferedReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public abstract class AbstractSQLExecutor implements SQLExecutor {
	private static final String TAG = "AbstractSQLExecutor";


	private int generatedSQLCount;
	private int cachedSQLCount;
	private int executedSQLCount;
	public AbstractSQLExecutor() {
		generatedSQLCount = 0;
		cachedSQLCount = 0;
		executedSQLCount = 0;
	}

	@Override
	public int getGeneratedSQLCount() {
		return generatedSQLCount;
	}
	@Override
	public int getCachedSQLCount() {
		return cachedSQLCount;
	}
	@Override
	public int getExecutedSQLCount() {
		return executedSQLCount;
	}

	/**
	 * 缓存map
	 */
	protected Map<String, List<JSONObject>> cacheMap = new HashMap<>();


	/**保存缓存
	 * @param sql
	 * @param list
	 * @param isStatic
	 */
	@Override
	public synchronized void putCache(String sql, List<JSONObject> list, int type) {
		if (sql == null || list == null) { //空map有效，说明查询过sql了  || list.isEmpty()) {
			Log.i(TAG, "saveList  sql == null || list == null >> return;");
			return;
		}
		cacheMap.put(sql, list);
	}
	/**移除缓存
	 * @param sql
	 * @param isStatic
	 */
	@Override
	public synchronized void removeCache(String sql, int type) {
		if (sql == null) {
			Log.i(TAG, "removeList  sql == null >> return;");
			return;
		}
		cacheMap.remove(sql);
	}

	@Override
	public List<JSONObject> getCache(String sql, int type) {
		return cacheMap.get(sql);
	}

	/**获取缓存
	 * @param sql
	 * @param position
	 * @param type
	 * @return
	 */
	@Override
	public JSONObject getCacheItem(String sql, int position, int type) {
		List<JSONObject> list = getCache(sql, type);
		//只要map不为null，则如果 list.get(position) == null，则返回 {} ，避免再次SQL查询
		if (list == null) {
			return null;
		} 
		JSONObject result = position >= list.size() ? null : list.get(position);
		return result != null ? result : new JSONObject();
	}



	@Override
	public ResultSet executeQuery(@NotNull Statement statement, String sql) throws Exception {
		executedSQLCount ++;

		return statement.executeQuery(sql);
	}
	@Override
	public int executeUpdate(@NotNull Statement statement, String sql) throws Exception {
		executedSQLCount ++;

		return statement.executeUpdate(sql);
	}
	@Override
	public ResultSet execute(@NotNull Statement statement, String sql) throws Exception {
		executedSQLCount ++;

		statement.execute(sql);
		return statement.getResultSet();
	}

	/**执行SQL
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject execute(@NotNull SQLConfig config, boolean unknowType) throws Exception {
		boolean isPrepared = config.isPrepared();

		final String sql = config.getSQL(false);

		config.setPrepared(isPrepared);

		if (StringUtil.isEmpty(sql, true)) {
			Log.e(TAG, "execute  StringUtil.isEmpty(sql, true) >> return null;");
			return null;
		}

		boolean isExplain = config.isExplain();
		boolean isHead = RequestMethod.isHeadMethod(config.getMethod(), true);

		final int position = config.getPosition();
		JSONObject result;

		if (isExplain == false) {
			generatedSQLCount ++;
		}

		long startTime = System.currentTimeMillis();
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
				+ "\n已生成 " + generatedSQLCount + " 条 SQL"
				+ "\nexecute  startTime = " + startTime
				+ "\ndatabase = " + StringUtil.getString(config.getDatabase())
				+ "; schema = " + StringUtil.getString(config.getSchema())
				+ "; sql = \n" + sql
				+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		ResultSet rs = null;
		List<JSONObject> resultList = null;
		Map<String, JSONObject> childMap = null;

		try {
			if (unknowType) {
				Statement statement = getStatement(config);
				rs = execute(statement, sql);

				result = new JSONObject(true);
				int updateCount = statement.getUpdateCount();
				result.put(JSONResponse.KEY_COUNT, updateCount);
				result.put("update", updateCount >= 0);
				//导致后面 rs.getMetaData() 报错 Operation not allowed after ResultSet closed		result.put("moreResults", statement.getMoreResults());
			}
			else {
				switch (config.getMethod()) {
				case POST:
				case PUT:
				case DELETE:
					executedSQLCount ++;

					int updateCount = executeUpdate(config);
					if (updateCount <= 0) {
						throw new IllegalAccessException("没权限访问或对象不存在！");  // NotExistException 会被 catch 转为成功状态
					}

					// updateCount>0时收集结果。例如更新操作成功时，返回count(affected rows)、id字段
					result = AbstractParser.newSuccessResult();  // TODO 对 APIAuto 及其它现有的前端/客户端影响比较大，暂时还是返回 code 和 msg，5.0 再移除  new JSONObject(true);

					//id,id{}至少一个会有，一定会返回，不用抛异常来阻止关联写操作时前面错误导致后面无条件执行！
					result.put(JSONResponse.KEY_COUNT, updateCount);//返回修改的记录数
					if (config.getId() != null) {
						result.put(config.getIdKey(), config.getId());
					} else {
						result.put(config.getIdKey() + "[]", config.getWhere(config.getIdKey() + "{}", true));
					}
					return result;

				case GET:
				case GETS:
				case HEAD:
				case HEADS:
					result = isHead ? null : getCacheItem(sql, position, config.getCache());
					Log.i(TAG, ">>> execute  result = getCache('" + sql + "', " + position + ") = " + result);
					if (result != null) {
						cachedSQLCount ++;

						Log.d(TAG, "\n\n execute  result != null >> return result;"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
						return result;
					}

					rs = executeQuery(config);  //FIXME SQL Server 是一次返回两个结果集，包括查询结果和执行计划，需要 moreResults 

					if (isExplain == false) { //只有 SELECT 才能 EXPLAIN
						executedSQLCount ++;
					}
					break;

				default://OPTIONS, TRACE等
					Log.e(TAG, "execute  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
					return null;
				}
			}


			if (isExplain == false && isHead) {
				if (rs.next() == false) {
					return AbstractParser.newErrorResult(new SQLException("数据库错误, rs.next() 失败！"));
				}

				result = AbstractParser.newSuccessResult();
				result.put(JSONResponse.KEY_COUNT, rs.getLong(1));
				resultList = new ArrayList<>(1);
				resultList.add(result);
			}
			else {
				//		final boolean cache = config.getCount() != 1;
				// TODO 设置初始容量为查到的数据量，解决频繁扩容导致的延迟，貌似只有 rs.last 取 rs.getRow() ? 然后又得 rs.beforeFirst 重置位置以便下方取值
				resultList = new ArrayList<>(config.getCount() <= 0 ? Parser.MAX_QUERY_COUNT : config.getCount());
				//		Log.d(TAG, "select  cache = " + cache + "; resultList" + (resultList == null ? "=" : "!=") + "null");

				int index = -1;

				ResultSetMetaData rsmd = rs.getMetaData();
				final int length = rsmd.getColumnCount();

				//<SELECT * FROM Comment WHERE momentId = '470', { id: 1, content: "csdgs" }>
				childMap = new HashMap<>(); //要存到cacheMap
				// WHERE id = ? AND ... 或 WHERE ... AND id = ? 强制排序 remove 再 put，还是重新 getSQL吧


				boolean hasJoin = config.hasJoin();
				int viceColumnStart = length + 1; //第一个副表字段的index
				while (rs.next()) {
					index ++;
					Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n execute while (rs.next()){  index = " + index + "\n\n");

					JSONObject item = new JSONObject(true);

					for (int i = 1; i <= length; i++) {

						// if (hasJoin && viceColumnStart > length && config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
						// 	viceColumnStart = i;
						// }

						// bugfix-修复非常规数据库字段，获取表名失败导致输出异常
						if (isExplain == false && hasJoin && viceColumnStart > length) {
							List<String> column = config.getColumn();
							String sqlTable = rsmd.getTableName(i);
							if (config.isClickHouse()&&(sqlTable.startsWith("`")||sqlTable.startsWith("\""))){
								sqlTable = sqlTable.substring(1,sqlTable.length()-1);
							}
							if (column != null && column.isEmpty() == false) {
								viceColumnStart = column.size() + 1;
							}
							else if (config.getSQLTable().equalsIgnoreCase(sqlTable) == false) {
								viceColumnStart = i;
							}
						}

						item = onPutColumn(config, rs, rsmd, index, item, i, isExplain == false && hasJoin && i >= viceColumnStart ? childMap : null);
					}

					resultList = onPutTable(config, rs, rsmd, resultList, index, item);

					Log.d(TAG, "\n execute  while (rs.next()) { resultList.put( " + index + ", result); "
							+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");
				}
			}
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (resultList == null) {
			return null;
		}

		if (unknowType || isExplain) {
			if (isExplain) {
				if (result == null) {
					result = new JSONObject(true);
				}
				config.setExplain(false);
				result.put("sql", config.getSQL(false));
				config.setExplain(isExplain);
				config.setPrepared(isPrepared);
			}
			result.put("list", resultList);
			return result;
		}

		if (isHead == false) {
			// @ APP JOIN 查询副表并缓存到 childMap <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

			executeAppJoin(config, resultList, childMap);

			// @ APP JOIN 查询副表并缓存到 childMap >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			//子查询 SELECT Moment.*, Comment.id 中的 Comment 内字段
			Set<Entry<String, JSONObject>> set = childMap.entrySet();

			//<sql, Table>
			for (Entry<String, JSONObject> entry : set) {
				List<JSONObject> l = new ArrayList<>();
				l.add(entry.getValue());
				putCache(entry.getKey(), l, JSONRequest.CACHE_ROM);
			}

			putCache(sql, resultList, config.getCache());
			Log.i(TAG, ">>> execute  putCache('" + sql + "', resultList);  resultList.size() = " + resultList.size());

			// 数组主表对象额外一次返回全部，方便 Parser 缓存来提高性能

			result = position >= resultList.size() ? new JSONObject() : resultList.get(position);
			if (position == 0 && resultList.size() > 1 && result != null && result.isEmpty() == false) { 
				// 不是 main 不会直接执行，count=1 返回的不会超过 1   && config.isMain() && config.getCount() != 1
				Log.i(TAG, ">>> execute  position == 0 && resultList.size() > 1 && result != null && result.isEmpty() == false"
						+ " >> result = new JSONObject(result); result.put(KEY_RAW_LIST, resultList);");

				result = new JSONObject(result);
				result.put(KEY_RAW_LIST, resultList);
			}
		}

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "\n\n execute  endTime = " + endTime + "; duration = " + (endTime - startTime)
				+ "\n return resultList.get(" + position + ");"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		return result;
	}


	/**@ APP JOIN 查询副表并缓存到 childMap
	 * @param config
	 * @param resultList
	 * @param childMap
	 * @throws Exception 
	 */
	protected void executeAppJoin(SQLConfig config, List<JSONObject> resultList, Map<String, JSONObject> childMap) throws Exception {
		List<Join> joinList = config.getJoinList();
		if (joinList != null) {

			SQLConfig jc;
			SQLConfig cc;

			for (Join j : joinList) {
				if (j.isAppJoin() == false) {
					Log.i(TAG, "executeAppJoin  for (Join j : joinList) >> j.isAppJoin() == false >>  continue;");
					continue;
				}

				cc = j.getCacheConfig(); //这里用config改了getSQL后再还原很麻烦，所以提前给一个config2更好
				if (cc == null) {
					if (Log.DEBUG) {
						throw new NullPointerException("服务器内部错误, executeAppJoin cc == null ! 导致不能缓存 @ APP JOIN 的副表数据！");
					}
					continue;
				}

				jc = j.getJoinConfig();

				//取出 "id@": "@/User/userId" 中所有 userId 的值
				List<Object> targetValueList = new ArrayList<>();
				JSONObject mainTable;
				Object targetValue;

				for (int i = 0; i < resultList.size(); i++) {
					mainTable = resultList.get(i);
					targetValue = mainTable == null ? null : mainTable.get(j.getTargetKey());

					if (targetValue != null && targetValueList.contains(targetValue) == false) {
						targetValueList.add(targetValue);
					}
				}


				//替换为 "id{}": [userId1, userId2, userId3...]
				jc.putWhere(j.getOriginKey(), null, false);  // remove orginKey
				jc.putWhere(j.getKey() + "{}", targetValueList, true);  // add orginKey{}

				jc.setMain(true).setPreparedValueList(new ArrayList<>());

				boolean prepared = jc.isPrepared();
				final String sql = jc.getSQL(false);
				jc.setPrepared(prepared);

				if (StringUtil.isEmpty(sql, true)) {
					throw new NullPointerException(TAG + ".executeAppJoin  StringUtil.isEmpty(sql, true) >> return null;");
				}

				long startTime = System.currentTimeMillis();
				Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
						+ "\n executeAppJoin  startTime = " + startTime
						+ "\n sql = \n " + sql
						+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

				//执行副表的批量查询 并 缓存到 childMap
				ResultSet rs = null;
				try {
					rs = executeQuery(jc);

					int index = -1;

					ResultSetMetaData rsmd = rs.getMetaData();
					final int length = rsmd.getColumnCount();

					JSONObject result;
					String cacheSql;
					while (rs.next()) { //FIXME 同时有 @ APP JOIN 和 < 等 SQL JOIN 时，next = false 总是无法进入循环，导致缓存失效，可能是连接池或线程问题
						index ++;
						Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n executeAppJoin while (rs.next()){  index = " + index + "\n\n");

						result = new JSONObject(true);

						for (int i = 1; i <= length; i++) {

							result = onPutColumn(jc, rs, rsmd, index, result, i, null);
						}

						//每个 result 都要用新的 SQL 来存 childResultMap = onPutTable(config, rs, rsmd, childResultMap, index, result);

						Log.d(TAG, "\n executeAppJoin  while (rs.next()) { resultList.put( " + index + ", result); "
								+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");

						//缓存到 childMap
						cc.putWhere(j.getKey(), result.get(j.getKey()), true);
						cacheSql = cc.getSQL(false);
						childMap.put(cacheSql, result);

						Log.d(TAG, ">>> executeAppJoin childMap.put('" + cacheSql + "', result);  childMap.size() = " + childMap.size());
					}
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				long endTime = System.currentTimeMillis();
				Log.d(TAG, "\n\n executeAppJoin  endTime = " + endTime + "; duration = " + (endTime - startTime)
						+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");

			}
		}

	}



	/**table.put(rsmd.getColumnLabel(i), rs.getObject(i));
	 * @param config 
	 * @param rs
	 * @param rsmd
	 * @param tablePosition 从0开始
	 * @param table
	 * @param columnIndex 从1开始
	 * @param childMap 
	 * @return result
	 * @throws Exception
	 */
	protected JSONObject onPutColumn(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, Map<String, JSONObject> childMap) throws Exception {

		if (rsmd.getColumnName(columnIndex).startsWith("_")) {
			Log.i(TAG, "select while (rs.next()){ ..."
					+ " >>  rsmd.getColumnName(i).startsWith(_) >> continue;");
			return table;
		}

		//已改为  rsmd.getTableName(columnIndex) 支持副表不传 @column ， 但如何判断是副表？childMap != null
		//		String lable = rsmd.getColumnLabel(columnIndex);
		//		int dotIndex = lable.indexOf(".");
		String lable = getKey(config, rs, rsmd, tablePosition, table, columnIndex, childMap);

		String childTable = childMap == null ? null : rsmd.getTableName(columnIndex); //dotIndex < 0 ? null : lable.substring(0, dotIndex);

		JSONObject finalTable = null;
		String childSql = null;
		SQLConfig childConfig = null;

		if (childTable == null) {
			finalTable = table;
		}
		else {
			//			lable = column;

			//<sql, Table>

			List<Join> joinList = config.getJoinList();
			if (joinList != null) {
				for (Join j : joinList) {
					childConfig = j.isAppJoin() ? null : j.getCacheConfig(); //这里用config改了getSQL后再还原很麻烦，所以提前给一个config2更好

					if (childConfig != null && childTable.equalsIgnoreCase(childConfig.getSQLTable())) {

						childConfig.putWhere(j.getKey(), table.get(j.getTargetKey()), true);
						childSql = childConfig.getSQL(false);

						if (StringUtil.isEmpty(childSql, true)) {
							return table;
						}

						finalTable = (JSONObject) childMap.get(childSql);
						break;
					}
				}
			}

		}

		Object value = getValue(config, rs, rsmd, tablePosition, table, columnIndex, lable, childMap);
		if (value != null) {
			if (finalTable == null) {
				finalTable = new JSONObject(true);
				childMap.put(childSql, finalTable);
			}
			finalTable.put(lable, value);
		}

		return table;
	}

	/**resultList.put(position, table);
	 * @param config 
	 * @param rs
	 * @param rsmd
	 * @param resultList
	 * @param position
	 * @param table
	 * @return resultList
	 */
	protected List<JSONObject> onPutTable(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, @NotNull List<JSONObject> resultList, int position, @NotNull JSONObject table) {

		resultList.add(table);
		return resultList;
	}



	protected String getKey(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, Map<String, JSONObject> childMap) throws Exception {
		return rsmd.getColumnLabel(columnIndex);  // dotIndex < 0 ? lable : lable.substring(dotIndex + 1);
	}

	protected Object getValue(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, String lable, Map<String, JSONObject> childMap) throws Exception {

		Object value = rs.getObject(columnIndex);
		//					Log.d(TAG, "name:" + rsmd.getColumnName(i));
		//					Log.d(TAG, "lable:" + rsmd.getColumnLabel(i));
		//					Log.d(TAG, "type:" + rsmd.getColumnType(i));
		//					Log.d(TAG, "typeName:" + rsmd.getColumnTypeName(i));

		//				Log.i(TAG, "select  while (rs.next()) { >> for (int i = 0; i < length; i++) {"
		//						+ "\n  >>> value = " + value);

		boolean castToJson = false;

		//数据库查出来的null和empty值都有意义，去掉会导致 Moment:{ @column:"content" } 部分无结果及中断数组查询！
		if (value instanceof Boolean || value instanceof Number) {
			//加快判断速度
		}
		else if (value instanceof Timestamp) {
			value = ((Timestamp) value).toString();
		}
		else if (value instanceof Date) {
			value = ((Date) value).toString();
		}
		else if (value instanceof Time) {
			value = ((Time) value).toString();
		}
		else if (value instanceof String && isJSONType(config, rsmd, columnIndex, lable)) { //json String
			castToJson = true;
		}
		else if (value instanceof Blob) { //FIXME 存的是 abcde，取出来直接就是 [97, 98, 99, 100, 101] 这种 byte[] 类型，没有经过以下处理，但最终序列化后又变成了字符串 YWJjZGU=
			castToJson = true;
			value = new String(((Blob) value).getBytes(1, (int) ((Blob) value).length()), "UTF-8");
		}
		else if (value instanceof Clob) { //SQL Server TEXT 类型 居然走这个
			castToJson = true;

			StringBuffer sb = new StringBuffer(); 
			BufferedReader br = new BufferedReader(((Clob) value).getCharacterStream()); 
			String s = br.readLine();
			while (s != null) {
				sb.append(s); 
				s = br.readLine(); 
			}
			value = sb.toString();

			try {
				br.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (castToJson == false) {
			List<String> json = config.getJson();
			castToJson = json != null && json.contains(lable);
		}
		if (castToJson) {
			try {
				value = JSON.parse((String) value);
			} catch (Exception e) {
				Log.e(TAG, "getValue  try { value = JSON.parse((String) value); } catch (Exception e) { \n" + e.getMessage());
			}
		}

		return value;
	}


	/**判断是否为JSON类型
	 * @param config 
	 * @param lable 
	 * @param rsmd
	 * @param position
	 * @return
	 */
	@Override
	public boolean isJSONType(@NotNull SQLConfig config, ResultSetMetaData rsmd, int position, String lable) {
		try {
			String column = rsmd.getColumnTypeName(position);
			//TODO CHAR和JSON类型的字段，getColumnType返回值都是1	，如果不用CHAR，改用VARCHAR，则可以用上面这行来提高性能。
			//return rsmd.getColumnType(position) == 1;

			if (column.toLowerCase().contains("json")) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//		List<String> json = config.getJson();
		//		return json != null && json.contains(lable);
		return false;
	}



	/**
	 * @param config 
	 * @return
	 * @throws Exception
	 */
	@Override
	public PreparedStatement getStatement(@NotNull SQLConfig config) throws Exception {
		PreparedStatement statement; //创建Statement对象
		if (config.getMethod() == RequestMethod.POST && config.getId() == null) { //自增id
			statement = getConnection(config).prepareStatement(config.getSQL(config.isPrepared()), Statement.RETURN_GENERATED_KEYS);
		}
		else {
			statement = getConnection(config).prepareStatement(config.getSQL(config.isPrepared()));
		}
		List<Object> valueList = config.isPrepared() ? config.getPreparedValueList() : null;

		if (valueList != null && valueList.isEmpty() == false) {
			for (int i = 0; i < valueList.size(); i++) {
				statement = setArgument(config, statement, i, valueList.get(i));
			}
		}
		// statement.close();

		return statement;
	}

	public PreparedStatement setArgument(@NotNull SQLConfig config, @NotNull PreparedStatement statement, int index, Object value) throws SQLException {
		//JSON.isBooleanOrNumberOrString(v) 解决 PostgreSQL: Can't infer the SQL type to use for an instance of com.alibaba.fastjson.JSONArray
		if (apijson.JSON.isBooleanOrNumberOrString(value)) {
			statement.setObject(index + 1, value); //PostgreSQL JDBC 不支持隐式类型转换 tinyint = varchar 报错
		}
		else {
			statement.setString(index + 1, value == null ? null : value.toString()); //MySQL setObject 不支持 JSON 类型
		}
		return statement;
	}

	protected Map<String, Connection> connectionMap = new HashMap<>();
	protected Connection connection;
	@NotNull
	@Override
	public Connection getConnection(@NotNull SQLConfig config) throws Exception {
		String connectionKey = config.getDatasource() + "-" + config.getDatabase();
		connection = connectionMap.get(connectionKey);
		if (connection == null || connection.isClosed()) {
			Log.i(TAG, "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
			// PostgreSQL 不允许 cross-database
			connection = DriverManager.getConnection(config.getDBUri(), config.getDBAccount(), config.getDBPassword());
			connectionMap.put(connectionKey, connection);
		}

		int ti = getTransactionIsolation();
		if (ti != Connection.TRANSACTION_NONE) { //java.sql.SQLException: Transaction isolation level NONE not supported by MySQL
			begin(ti);
		}

		return connection;
	}

	//事务处理 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private int transactionIsolation;
	@Override
	public int getTransactionIsolation() {
		return transactionIsolation;
	}
	@Override
	public void setTransactionIsolation(int transactionIsolation) {
		this.transactionIsolation = transactionIsolation;
	}

	private boolean isIsolationStatusSet = false; //已设置事务等级
	@Override
	public void begin(int transactionIsolation) throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION begin transactionIsolation = " + transactionIsolation + " >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
		//		if (connection == null || connection.isClosed()) {
		//			return;
		//		}
		if (! isIsolationStatusSet) { //只设置一次Isolation等级 PG重复设置事务等级会报错
			isIsolationStatusSet = true;
			connection.setTransactionIsolation(transactionIsolation);
		}
		connection.setAutoCommit(false); //java.sql.SQLException: Can''t call commit when autocommit=true
	}
	@Override
	public void rollback() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION rollback >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
		if (connection == null) { // || connection.isClosed()) {
			return;
		}
		connection.rollback();
	}
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION rollback savepoint " + (savepoint == null ? "" : "!") + "= null >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
		if (connection == null) { // || connection.isClosed()) {
			return;
		}
		connection.rollback(savepoint);
	}
	@Override
	public void commit() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION commit >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
		if (connection == null) { // || connection.isClosed()) {
			return;
		}
		connection.commit();
	}
	//事务处理 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**关闭连接，释放资源
	 */
	@Override
	public void close() {
		cacheMap.clear();
		cacheMap = null;

		generatedSQLCount = 0;
		cachedSQLCount = 0;
		executedSQLCount = 0;

		if (connectionMap == null) {
			return;
		}

		Collection<Connection> connections = connectionMap.values();

		if (connections != null) {
			for (Connection connection : connections) {
				try {
					if (connection != null && connection.isClosed() == false) {
						connection.close();
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		connectionMap.clear();
		connectionMap = null;
	}

	@Override
	public ResultSet executeQuery(@NotNull SQLConfig config) throws Exception {
		return getStatement(config).executeQuery(); //PreparedStatement 不用传 SQL
	}

	@Override
	public int executeUpdate(@NotNull SQLConfig config) throws Exception {
		PreparedStatement s = getStatement(config);
		int count = s.executeUpdate(); //PreparedStatement 不用传 SQL

		if (config.getMethod() == RequestMethod.POST && config.getId() == null) { //自增id
			ResultSet rs = s.getGeneratedKeys();
			if (rs != null && rs.next()) {
				config.setId(rs.getLong(1));//返回插入的主键id
			}
		}

		return count;
	}


}
