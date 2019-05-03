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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.NotNull;
import zuo.biao.apijson.StringUtil;

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

	/**关闭连接，释放资源
	 */
	@Override
	public void close() {
		cacheMap.clear();
		cacheMap = null;

		generatedSQLCount = 0;
		cachedSQLCount = 0;
		executedSQLCount = 0;
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
		boolean prepared = config.isPrepared();

		final String sql = config.getSQL(false);

		config.setPrepared(prepared);

		if (StringUtil.isEmpty(sql, true)) {
			Log.e(TAG, "select  StringUtil.isEmpty(sql, true) >> return null;");
			return null;
		}

		final int position = config.getPosition();
		JSONObject result;

		generatedSQLCount ++;

		long startTime = System.currentTimeMillis();
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
				+ "\n已生成 " + generatedSQLCount + " 条 SQL"
				+ "\nselect  startTime = " + startTime
				+ "\nsql = \n" + sql
				+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		ResultSet rs = null;

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
			case HEAD:
			case HEADS:
				executedSQLCount ++;

				rs = executeQuery(config);

				result = rs.next() ? AbstractParser.newSuccessResult()
						: AbstractParser.newErrorResult(new SQLException("数据库错误, rs.next() 失败！"));
				result.put(JSONResponse.KEY_COUNT, rs.getLong(1));

				rs.close();
				return result;

			case POST:
			case PUT:
			case DELETE:
				executedSQLCount ++;

				int updateCount = executeUpdate(config);

				result = AbstractParser.newResult(updateCount > 0 ? JSONResponse.CODE_SUCCESS : JSONResponse.CODE_NOT_FOUND
						, updateCount > 0 ? JSONResponse.MSG_SUCCEED : "没权限访问或对象不存在！");

				//id,id{}至少一个会有，一定会返回，不用抛异常来阻止关联写操作时前面错误导致后面无条件执行！
				if (config.getId() != null) {
					result.put(config.getIdKey(), config.getId());
				} else {
					result.put(config.getIdKey() + "[]", config.getWhere(config.getIdKey() + "{}", true));
				}
				result.put(JSONResponse.KEY_COUNT, updateCount);//返回修改的记录数
				return result;

			case GET:
			case GETS:
				result = getCacheItem(sql, position, config.getCache());
				Log.i(TAG, ">>> select  result = getCache('" + sql + "', " + position + ") = " + result);
				if (result != null) {
					cachedSQLCount ++;

					Log.d(TAG, "\n\n select  result != null >> return result;"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
					return result;
				}

				executedSQLCount ++;

				rs = executeQuery(config);
				break;

			default://OPTIONS, TRACE等
				Log.e(TAG, "select  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
				return null;
			}
		}



		//		final boolean cache = config.getCount() != 1;
		List<JSONObject> resultList = new ArrayList<>();
		//		Log.d(TAG, "select  cache = " + cache + "; resultList" + (resultList == null ? "=" : "!=") + "null");

		int index = -1;

		ResultSetMetaData rsmd = rs.getMetaData();
		final int length = rsmd.getColumnCount();

		//<SELECT * FROM Comment WHERE momentId = '470', { id: 1, content: "csdgs" }>
		Map<String, JSONObject> childMap = new HashMap<>(); //要存到cacheMap
		// WHERE id = ? AND ... 或 WHERE ... AND id = ? 强制排序 remove 再 put，还是重新 getSQL吧


		boolean hasJoin = config.hasJoin();
		int viceColumnStart = length + 1; //第一个副表字段的index
		while (rs.next()) {
			index ++;
			Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n select while (rs.next()){  index = " + index + "\n\n");

			JSONObject item = new JSONObject(true);

			for (int i = 1; i <= length; i++) {

				// if (hasJoin && viceColumnStart > length && config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
				// 	viceColumnStart = i;
				// }

				// bugfix-修复非常规数据库字段，获取表名失败导致输出异常
				if (config.isExplain() == false && hasJoin && viceColumnStart > length) {
					List<String> column = config.getColumn();

					if (column != null && column.isEmpty() == false) {
						viceColumnStart = column.size() + 1;
					}
					else if (config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
						viceColumnStart = i;
					}
				}

				item = onPutColumn(config, rs, rsmd, index, item, i, config.isExplain() == false && hasJoin && i >= viceColumnStart ? childMap : null);
			}

			resultList = onPutTable(config, rs, rsmd, resultList, index, item);

			Log.d(TAG, "\n select  while (rs.next()) { resultList.put( " + index + ", result); "
					+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");
		}

		rs.close();

		if (unknowType || config.isExplain()) {
			if (config.isExplain()) {
				if (result == null) {
					result = new JSONObject(true);
				}
				result.put("sql", sql);
			}
			result.put("list", resultList);
			return result;
		}

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
		Log.i(TAG, ">>> select  putCache('" + sql + "', resultList);  resultList.size() = " + resultList.size());

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "\n\n select  endTime = " + endTime + "; duration = " + (endTime - startTime)
				+ "\n return resultList.get(" + position + ");"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		return position >= resultList.size() ? new JSONObject() : resultList.get(position);
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

				jc = j.getJoinConfig();
				cc = j.getCacheConfig(); //这里用config改了getSQL后再还原很麻烦，所以提前给一个config2更好

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
				jc.putWhere(j.getOriginKey(), null, false);
				jc.putWhere(j.getKey() + "{}", targetValueList, false);

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
				ResultSet rs = executeQuery(jc);

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
					cc.putWhere(j.getKey(), result.get(j.getKey()), false);
					cacheSql = cc.getSQL(false);
					childMap.put(cacheSql, result);

					Log.d(TAG, ">>> executeAppJoin childMap.put('" + cacheSql + "', result);  childMap.size() = " + childMap.size());
				}

				rs.close();


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
		String lable = rsmd.getColumnLabel(columnIndex);//dotIndex < 0 ? lable : lable.substring(dotIndex + 1);

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

						childConfig.putWhere(j.getKey(), table.get(j.getTargetKey()), false);
						childSql = childConfig.getSQL(false);

						if (StringUtil.isEmpty(childSql, true)) {
							return table;
						}

						finalTable = (JSONObject) childMap.get(childSql);
						break;
					}
				}
			}

			if (finalTable == null) {
				finalTable = new JSONObject(true);
				childMap.put(childSql, finalTable);
			}
		}

		finalTable.put(lable, getValue(config, rs, rsmd, tablePosition, table, columnIndex, childMap));

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


	protected Object getValue(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, Map<String, JSONObject> childMap) throws Exception {

		Object value = rs.getObject(columnIndex);
		//					Log.d(TAG, "name:" + rsmd.getColumnName(i));
		//					Log.d(TAG, "lable:" + rsmd.getColumnLabel(i));
		//					Log.d(TAG, "type:" + rsmd.getColumnType(i));
		//					Log.d(TAG, "typeName:" + rsmd.getColumnTypeName(i));

		//				Log.i(TAG, "select  while (rs.next()) { >> for (int i = 0; i < length; i++) {"
		//						+ "\n  >>> value = " + value);

		//数据库查出来的null和empty值都有意义，去掉会导致 Moment:{ @column:"content" } 部分无结果及中断数组查询！
		if (value instanceof Timestamp) {
			value = ((Timestamp) value).toString();
		}
		else if (value instanceof String && isJSONType(rsmd, columnIndex)) { //json String
			value = JSON.parse((String) value);
		}

		return value;
	}


	/**判断是否为JSON类型
	 * @param rsmd
	 * @param position
	 * @return
	 */
	@Override
	public boolean isJSONType(ResultSetMetaData rsmd, int position) {
		try {
			//TODO CHAR和JSON类型的字段，getColumnType返回值都是1	，如果不用CHAR，改用VARCHAR，则可以用上面这行来提高性能。
			//return rsmd.getColumnType(position) == 1;
			return rsmd.getColumnTypeName(position).toLowerCase().contains("json");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


}
