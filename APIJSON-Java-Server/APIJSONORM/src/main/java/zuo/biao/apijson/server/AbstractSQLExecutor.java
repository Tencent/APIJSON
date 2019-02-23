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


	//访问一次后丢失，可能因为static导致内存共享，别的地方改变了内部对象的值
	//	private static final Map<String, Map<Integer, JSONObject>> staticCacheMap;
	//	static {
	//		staticCacheMap = new HashMap<String, Map<Integer, JSONObject>>();
	//	}

	/**
	 * 缓存map
	 */
	protected Map<String, Map<Integer, JSONObject>> cacheMap = new HashMap<String, Map<Integer, JSONObject>>();


	/**保存缓存
	 * @param sql
	 * @param map
	 * @param isStatic
	 */
	@Override
	public synchronized void putCache(String sql, Map<Integer, JSONObject> map, boolean isStatic) {
		if (sql == null || map == null) { //空map有效，说明查询过sql了  || map.isEmpty()) {
			Log.i(TAG, "saveList  sql == null || map == null >> return;");
			return;
		}
		//		if (isStatic) {
		//			staticCacheMap.put(sql, map);
		//		} else {
		cacheMap.put(sql, map);
		//		}
	}
	/**移除缓存
	 * @param sql
	 * @param isStatic
	 */
	@Override
	public synchronized void removeCache(String sql, boolean isStatic) {
		if (sql == null) {
			Log.i(TAG, "removeList  sql == null >> return;");
			return;
		}
		//		if (isStatic) {
		//			staticCacheMap.remove(sql);
		//		} else {
		cacheMap.remove(sql);
		//		}
	}

	/**获取缓存
	 * @param sql
	 * @param position
	 * @param isStatic
	 * @return
	 */
	@Override
	public JSONObject getCache(String sql, int position, boolean isStatic) {
		Map<Integer, JSONObject> map = /** isStatic ? staticCacheMap.get(sql) : */ cacheMap.get(sql);
		//只要map不为null，则如果 map.get(position) == null，则返回 {} ，避免再次SQL查询
		if (map == null) {
			return null;
		} 
		JSONObject result = map.get(position);
		return result != null ? result : new JSONObject();
	}

	/**关闭连接，释放资源
	 */
	@Override
	public void close() {
		cacheMap.clear();
		cacheMap = null;
	}

	/**执行SQL
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject execute(SQLConfig config) throws Exception {
		if (config == null) {
			Log.e(TAG, "select  config==null >> return null;");
			return null;
		}
		boolean prepared = config.isPrepared();

		final String sql = config.getSQL(false);

		config.setPrepared(prepared);

		if (StringUtil.isEmpty(sql, true)) {
			Log.e(TAG, "select  StringUtil.isEmpty(sql, true) >> return null;");
			return null;
		}
		JSONObject result = null;

		long startTime = System.currentTimeMillis();
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
				+ "\n select  startTime = " + startTime
				+ "\n sql = \n " + sql
				+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		ResultSet rs = null;
		switch (config.getMethod()) {
		case HEAD:
		case HEADS:
			rs = executeQuery(config);

			result = rs.next() ? AbstractParser.newSuccessResult()
					: AbstractParser.newErrorResult(new SQLException("数据库错误, rs.next() 失败！"));
			result.put(JSONResponse.KEY_COUNT, rs.getLong(1));

			rs.close();
			return result;

		case POST:
		case PUT:
		case DELETE:
			long updateCount = executeUpdate(config);

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
			break;

		default://OPTIONS, TRACE等
			Log.e(TAG, "select  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
			return null;
		}


		final int position = config.getPosition();
		result = getCache(sql, position, config.isCacheStatic());
		Log.i(TAG, ">>> select  result = getCache('" + sql + "', " + position + ") = " + result);
		if (result != null) {
			Log.d(TAG, "\n\n select  result != null >> return result;"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
			return result;
		}

		rs = executeQuery(config);

		//		final boolean cache = config.getCount() != 1;
		Map<Integer, JSONObject> resultMap = new HashMap<Integer, JSONObject>();
		//		Log.d(TAG, "select  cache = " + cache + "; resultMap" + (resultMap == null ? "=" : "!=") + "null");

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

			result = new JSONObject(true);

			for (int i = 1; i <= length; i++) {

				if (hasJoin && viceColumnStart > length && config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
					viceColumnStart = i;
				}

				result = onPutColumn(config, rs, rsmd, index, result, i, hasJoin && i >= viceColumnStart ? childMap : null);
			}

			resultMap = onPutTable(config, rs, rsmd, resultMap, index, result);

			Log.d(TAG, "\n select  while (rs.next()) { resultMap.put( " + index + ", result); "
					+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");
		}

		rs.close();


		//TODO @ APP JOIN 查询副表并缓存到 childMap <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		executeAppJoin(config, resultMap, childMap);

		//TODO @ APP JOIN 查询副表并缓存到 childMap >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



		//子查询 SELECT Moment.*, Comment.id 中的 Comment 内字段
		Set<Entry<String, JSONObject>> set = childMap.entrySet();

		//<sql, Table>
		for (Entry<String, JSONObject> entry : set) {
			Map<Integer, JSONObject> m = new HashMap<Integer, JSONObject>();
			m.put(0, entry.getValue());
			putCache(entry.getKey(), m, false);
		}


		putCache(sql, resultMap, config.isCacheStatic());
		Log.i(TAG, ">>> select  putCache('" + sql + "', resultMap);  resultMap.size() = " + resultMap.size());

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "\n\n select  endTime = " + endTime + "; duration = " + (endTime - startTime)
				+ "\n return resultMap.get(" + position + ");"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		return resultMap.get(position);
	}


	/**@ APP JOIN 查询副表并缓存到 childMap
	 * @param config
	 * @param resultMap
	 * @param childMap
	 * @throws Exception 
	 */
	protected void executeAppJoin(SQLConfig config, Map<Integer, JSONObject> resultMap, Map<String, JSONObject> childMap) throws Exception {
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

				for (int i = 0; i < resultMap.size(); i++) {
					mainTable = resultMap.get(i);
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

					Log.d(TAG, "\n executeAppJoin  while (rs.next()) { resultMap.put( " + index + ", result); "
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

	/**resultMap.put(position, table);
	 * @param config 
	 * @param rs
	 * @param rsmd
	 * @param resultMap
	 * @param position
	 * @param table
	 * @return resultMap
	 */
	protected Map<Integer, JSONObject> onPutTable(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, @NotNull Map<Integer, JSONObject> resultMap, int position, @NotNull JSONObject table) {

		resultMap.put(position, table);
		return resultMap;
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
