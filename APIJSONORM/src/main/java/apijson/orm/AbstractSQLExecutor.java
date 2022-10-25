/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.io.BufferedReader;
import java.rmi.ServerError;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.Join.On;
import apijson.orm.exception.NotExistException;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public abstract class AbstractSQLExecutor implements SQLExecutor {
	private static final String TAG = "AbstractSQLExecutor";

	public static String KEY_RAW_LIST = "@RAW@LIST";  // 避免和字段命名冲突，不用 $RAW@LIST$ 是因为 $ 会在 fastjson 内部转义，浪费性能

	private int generatedSQLCount = 0;
	private int cachedSQLCount = 0;
	private int executedSQLCount = 0;

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

	private long executedSQLDuration = 0;
	private long sqlResultDuration = 0;
	@Override
	public long getExecutedSQLDuration() {
		return executedSQLDuration;
	}

	@Override
	public long getSqlResultDuration() {
		return sqlResultDuration;
	}


	/**
	 * 缓存 Map
	 */
	protected Map<String, List<JSONObject>> cacheMap = new HashMap<>();

	/**保存缓存
	 * @param sql  key
	 * @param list  value
	 * @param config  一般主表 SQLConfig 不为 null，JOIN 副表的为 null
	 */
	@Override
	public void putCache(String sql, List<JSONObject> list, SQLConfig config) {
		if (sql == null || list == null) { // 空 list 有效，说明查询过 sql 了  || list.isEmpty()) {
			Log.i(TAG, "saveList  sql == null || list == null >> return;");
			return;
		}

		cacheMap.put(sql, list);
	}

	/**获取缓存
	 * @param sql  key
	 * @param config  一般主表 SQLConfig 不为 null，JOIN 副表的为 null
	 */
	@Override
	public List<JSONObject> getCache(String sql, SQLConfig config) {
		return cacheMap.get(sql);
	}

	/**获取缓存
	 * @param sql  key
	 * @param position
	 * @param config  一般主表 SQLConfig 不为 null，JOIN 副表的为 null
	 * @return
	 */
	@Override
	public JSONObject getCacheItem(String sql, int position, SQLConfig config) {
		List<JSONObject> list = getCache(sql, config);
		// 只要 list 不为 null，则如果 list.get(position) == null，则返回 {} ，避免再次 SQL 查询
		if (list == null) {
			return null;
		}

		JSONObject result = position >= list.size() ? null : list.get(position);
		return result != null ? result : new JSONObject();
	}

	/**移除缓存
	 * @param sql  key
	 * @param config
	 */
	@Override
	public void removeCache(String sql, SQLConfig config) {
		if (sql == null) {
			Log.i(TAG, "removeList  sql == null >> return;");
			return;
		}
		cacheMap.remove(sql);
	}


	@Override
	public ResultSet executeQuery(@NotNull Statement statement, String sql) throws Exception {
		ResultSet rs = statement.executeQuery(sql);
		return rs;
	}
	@Override
	public int executeUpdate(@NotNull Statement statement, String sql) throws Exception {
		int c = statement.executeUpdate(sql);
		return c;
	}
	@Override
	public ResultSet execute(@NotNull Statement statement, String sql) throws Exception {
		statement.execute(sql);
		ResultSet rs = statement.getResultSet();
		return rs;
	}

	/**执行SQL
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject execute(@NotNull SQLConfig config, boolean unknownType) throws Exception {
		long executedSQLStartTime = System.currentTimeMillis();

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
			if (unknownType) {
				if (isExplain == false) { //只有 SELECT 才能 EXPLAIN
					executedSQLCount ++;
					executedSQLStartTime = System.currentTimeMillis();
				}
				Statement statement = getStatement(config);
				rs = execute(statement, sql);
				int updateCount = statement.getUpdateCount();
				if (isExplain == false) {
					executedSQLDuration += System.currentTimeMillis() - executedSQLStartTime;
				}

				result = new JSONObject(true);
				result.put(JSONResponse.KEY_COUNT, updateCount);
				result.put("update", updateCount >= 0);
				//导致后面 rs.getMetaData() 报错 Operation not allowed after ResultSet closed		result.put("moreResults", statement.getMoreResults());
			}
			else {
				switch (config.getMethod()) {
				case POST:
				case PUT:
				case DELETE:
					if (isExplain == false) { //只有 SELECT 才能 EXPLAIN
						executedSQLCount ++;
						executedSQLStartTime = System.currentTimeMillis();
					}
					int updateCount = executeUpdate(config);
					if (isExplain == false) {
						executedSQLDuration += System.currentTimeMillis() - executedSQLStartTime;
					}

					if (updateCount <= 0) {
						throw new IllegalAccessException("没权限访问或对象不存在！");  // NotExistException 会被 catch 转为成功状态
					}

					// updateCount>0时收集结果。例如更新操作成功时，返回count(affected rows)、id字段
					result = AbstractParser.newSuccessResult();  // TODO 对 APIAuto 及其它现有的前端/客户端影响比较大，暂时还是返回 code 和 msg，5.0 再移除  new JSONObject(true);

					//id,id{}至少一个会有，一定会返回，不用抛异常来阻止关联写操作时前面错误导致后面无条件执行！
					result.put(JSONResponse.KEY_COUNT, updateCount);//返回修改的记录数

					String idKey = config.getIdKey();
					if (config.getId() != null) {
						result.put(idKey, config.getId());
					}
					if (config.getIdIn() != null) {
						result.put(idKey + "[]", config.getIdIn());
					}

					return result;

				case GET:
				case GETS:
				case HEAD:
				case HEADS:
					result = isHead || isExplain ? null : getCacheItem(sql, position, config);
					Log.i(TAG, ">>> execute  result = getCache('" + sql + "', " + position + ") = " + result);
					if (result != null) {
						cachedSQLCount ++;
						List<JSONObject> cache = getCache(sql, config);
						if (cache != null && cache.size() > 1) {
							result.put(KEY_RAW_LIST, cache);
						}
						Log.d(TAG, "\n\n execute  result != null >> return result;"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
						return result;
					}

					if (isExplain == false) { //只有 SELECT 才能 EXPLAIN
						executedSQLCount ++;
						executedSQLStartTime = System.currentTimeMillis();
					}
					rs = executeQuery(config);  //FIXME SQL Server 是一次返回两个结果集，包括查询结果和执行计划，需要 moreResults
					if (isExplain == false) {
						executedSQLDuration += System.currentTimeMillis() - executedSQLStartTime;
					}
					break;

				default: //OPTIONS, TRACE等
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
				//		Log.d(TAG, "select  cache = " + cache + "; resultList" + (resultList == null ? "=" : "!=") + "null");
				try {  // 设置初始容量为查到的数据量，解决频繁扩容导致的延迟，貌似只有 rs.last 取 rs.getRow() ? 然后又得 rs.beforeFirst 重置位置以便下方取值
					rs.last();  //移到最后一行
					resultList = new ArrayList<>(rs.getRow());
					rs.beforeFirst();
				}
				catch (Throwable e) {
					Log.e(TAG, "try { rs.last(); resultList = new ArrayList<>(rs.getRow()); rs.beforeFirst(); >> } catch (Throwable e) = " + e.getMessage());
					int capacity;
					if (config.getId() != null) {  // id:Object 一定是 AND 条件，最终返回数据最多就这么多
						capacity = 1;
					}
					else {
						Object idIn = config.getIdIn();
						if (idIn instanceof Collection<?>) {  // id{}:[] 一定是 AND 条件，最终返回数据最多就这么多
							capacity = ((Collection<?>) idIn).size();
						}
						else {  // 预估容量
							capacity = config.getCount() <= 0 ? AbstractParser.MAX_QUERY_COUNT : config.getCount();
							if (capacity > 100) {
								// 有 WHERE 条件，条件越多过滤数据越多，暂时不考虑 @combine:"a | (b & !c)" 里面 | OR 和 ! NOT 条件，太复杂也不是很必要
								Map<String, List<String>> combine = config.getCombineMap();

								List<String> andList = combine == null ? null : combine.get("&");
								int andCondCount = andList == null ? (config.getWhere() == null ? 0 : config.getWhere().size()) : andList.size();

								List<String> orList = combine == null ? null : combine.get("|");
								int orCondCount = orList == null ? 0 : orList.size();

								List<String> notList = combine == null ? null : combine.get("!");
								int notCondCount = notList == null ? 0 : notList.size();

								// 有 GROUP BY 分组，字段越少过滤数据越多
								String[] group = StringUtil.split(config.getGroup());
								int groupCount = group == null ? 0 : group.length;
								if (groupCount > 0 && Arrays.asList(group).contains(config.getIdKey())) {
									groupCount = 0;
								}

								// 有 HAVING 聚合函数，字段越多过滤数据越多，暂时不考虑 @combine:"a | (b & !c)" 里面 | OR 和 ! NOT 条件，太复杂也不是很必要
								Map<String, Object> having = config.getHaving();
								int havingCount = having == null ? 0 : having.size();

								capacity /= Math.pow(1.5, Math.log10(capacity)
										+ andCondCount
										+ ((orCondCount <= 0 ? 0 : 2.0d/orCondCount)  // 1: 2.3, 2: 1.5, 3: 1.3, 4: 1.23, 5: 1.18
										+ (notCondCount/5.0d)  // 1: 1.08, 2: 1.18, 3: 1.28, 4: 1.38, 1.50
										+ (groupCount <= 0 ? 0 : 10.0d/groupCount))  // 1: 57.7, 7.6, 3: 3.9, 4: 2.8, 5: 2.3
										+ havingCount
										);
								capacity += 1;  // 避免正好比需要容量少一点点导致多一次扩容，大量数据 System.arrayCopy
							}
						}
					}

					resultList = new ArrayList<>(capacity);
				}

				int index = -1;

				long startTime2 = System.currentTimeMillis();
				ResultSetMetaData rsmd = rs.getMetaData();
				final int length = rsmd.getColumnCount();
				sqlResultDuration += System.currentTimeMillis() - startTime2;

				//<SELECT * FROM Comment WHERE momentId = '470', { id: 1, content: "csdgs" }>
				childMap = new HashMap<>(); //要存到cacheMap
//				Map<Integer, Join> columnIndexAndJoinMap = new HashMap<>(length);
				String lastTableName = null;  // 默认就是主表 config.getTable();
				String lastAliasName = null;  // 默认就是主表 config.getAlias();
				int lastViceTableStart = 0;
				int lastViceColumnStart = 0;
				Join lastJoin = null;
				// TODO				String[] columnIndexAndTableMap = new String[length];
				// WHERE id = ? AND ... 或 WHERE ... AND id = ? 强制排序 remove 再 put，还是重新 getSQL吧


				List<Join> joinList = config.getJoinList();
				boolean hasJoin = config.hasJoin() && joinList != null && ! joinList.isEmpty();

				// 直接用数组存取更快  Map<Integer, Join> columnIndexAndJoinMap = isExplain || ! hasJoin ? null : new HashMap<>(length);
				Join[] columnIndexAndJoinMap = isExplain || ! hasJoin ? null : new Join[length];

//				int viceColumnStart = length + 1; //第一个副表字段的index

//				FIXME 统计游标查找的时长？可能 ResultSet.next() 及 getTableName, getColumnName, getObject 比较耗时，因为不是一次加载到内存，而是边读边发

				long lastCursorTime = System.currentTimeMillis();
				while (rs.next()) {
					sqlResultDuration += System.currentTimeMillis() - lastCursorTime;
					lastCursorTime = System.currentTimeMillis();

					index ++;
					Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n execute while (rs.next()){  index = " + index + "\n\n");

					JSONObject item = new JSONObject(true);
					JSONObject curItem = item;
					boolean isMain = true;

					for (int i = 1; i <= length; i++) {

						// if (hasJoin && viceColumnStart > length && config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
						// 	viceColumnStart = i;
						// }

						// bugfix-修复非常规数据库字段，获取表名失败导致输出异常
						Join curJoin = columnIndexAndJoinMap == null ? null : columnIndexAndJoinMap[i - 1];  // columnIndexAndJoinMap.get(i);

						// 为什么 isExplain == false 不用判断？因为所有字段都在一张 Query Plan 表
						if (index <= 0 && columnIndexAndJoinMap != null) { // && viceColumnStart > length) {

							SQLConfig curConfig = curJoin == null || ! curJoin.isSQLJoin() ? null : curJoin.getCacheConfig();
							List<String> curColumn = curConfig == null ? null : curConfig.getColumn();
							String sqlTable = curConfig == null ? null : curConfig.getSQLTable();
							String sqlAlias = curConfig == null ? null : curConfig.getAlias();

							List<String> column = config.getColumn();
							int mainColumnSize = column == null ? 0 : column.size();
							// FIXME 主副表同名导致主表数据当成副表数据  { "[]": { "join": "</Comment:to/id@", "Comment": { "toId>": 0 }, "Comment:to": { "@column": "id,content", "id@": "/Comment/toId" } }, "@explain": true }
							boolean toFindJoin = mainColumnSize <= 0 || i > mainColumnSize;  // 主表就不用找 JOIN 配置

							if (StringUtil.isEmpty(sqlTable, true)) {
								if (toFindJoin) {  // 在主表字段数量内的都归属主表
									long startTime3 = System.currentTimeMillis();
									sqlTable = rsmd.getTableName(i);  // SQL 函数甚至部分字段都不返回表名，当然如果没传 @column 生成的 Table.* 则返回的所有字段都会带表名
									sqlResultDuration += System.currentTimeMillis() - startTime3;

                                    if (StringUtil.isEmpty(sqlTable, true)) {
                                        boolean isEmpty = curItem == null || curItem.isEmpty();
                                        String label = isEmpty ? null : getKey(config, rs, rsmd, index, curItem, i, childMap);
                                        if (isEmpty || curItem.containsKey(label) == false) {  // 重复字段几乎肯定不是一张表的，尤其是主副表同名主键 id
                                            sqlTable = i <= 1 ? config.getSQLTable() : lastTableName; // Presto 等引擎 JDBC 返回 rsmd.getTableName(i) 为空，主表如果一个字段都没有会导致 APISJON 主副表所有字段都不返回
                                        }
                                    }

									if (StringUtil.isEmpty(sqlTable, true)) {  // hasJoin 已包含这个判断 && joinList != null) {

										int nextViceColumnStart = lastViceColumnStart;  // 主表没有 @column 时会偏小 lastViceColumnStart
										for (int j = lastViceTableStart; j < joinList.size(); j++) {  // 查找副表 @column，定位字段所在表
											Join join = joinList.get(j);
											SQLConfig cfg = join == null || ! join.isSQLJoin() ? null : join.getJoinConfig();
											List<String> c = cfg == null ? null : cfg.getColumn();

											nextViceColumnStart += (c != null && ! c.isEmpty() ?
													c.size() : (
															StringUtil.equalsIgnoreCase(sqlTable, lastTableName)
															&& StringUtil.equals(sqlAlias, lastAliasName) ? 1 : 0
															)
													);
											if (i < nextViceColumnStart) {
												sqlTable = cfg.getSQLTable();
												sqlAlias = cfg.getAlias();
												lastViceTableStart = j;  // 避免后面的空 @column 表内字段被放到之前的空 @column 表

												curJoin = join;
												curConfig = cfg;
												curColumn = c;

												toFindJoin = false;
												isMain = false;
												break;
											}
										}
									}

									// 没有 @column，仍然定位不了，用前一个 table 名。FIXME 如果刚好某个表内第一个字段是就是 SQL 函数？
									if (StringUtil.isEmpty(sqlTable, true)) {
										sqlTable = lastTableName;
										sqlAlias = lastAliasName;
										toFindJoin = false;
									}
								}
							}
							else if (config.isClickHouse() && (sqlTable.startsWith("`") || sqlTable.startsWith("\""))){
								sqlTable = sqlTable.substring(1, sqlTable.length() - 1);
							}

							if (StringUtil.equalsIgnoreCase(sqlTable, lastTableName) == false || StringUtil.equals(sqlAlias, lastAliasName) == false) {
								lastTableName = sqlTable;
								lastAliasName = sqlAlias;
								lastViceColumnStart = i;

								if (toFindJoin) {  // 找到对应的副表 JOIN 配置
									for (int j = lastViceTableStart; j < joinList.size(); j++) {  // 查找副表 @column，定位字段所在表
										Join join = joinList.get(j);
										SQLConfig cfg = join == null || ! join.isSQLJoin() ? null : join.getJoinConfig();

										if (cfg != null && StringUtil.equalsIgnoreCase(sqlTable, cfg.getSQLTable())
												) {  // FIXME 导致副表字段错放到主表 && StringUtil.equals(sqlAlias, cfg.getAlias())) {
											lastViceTableStart = j;  // 避免后面的空 @column 表内字段被放到之前的空 @column 表

											curJoin = join;
											curConfig = cfg;
											curColumn = curConfig == null ? null : curConfig.getColumn();

											isMain = false;
											break;
										}
									}
								}
							}

							if (isMain) {
								lastViceColumnStart ++;
							}
							else {
								if (curJoin == null) {
									curJoin = lastJoin;
								}
								else {
									lastJoin = curJoin;
								}

								if (curColumn == null) {
									curConfig = curJoin == null || ! curJoin.isSQLJoin() ? null : curJoin.getJoinConfig();
									curColumn = curConfig == null ? null : curConfig.getColumn();
								}

								// 解决后面的表内 SQL 函数被放到之前的空 @column 表
								if (curColumn == null || curColumn.isEmpty()) {
									lastViceColumnStart ++;
								}
							}

							columnIndexAndJoinMap[i - 1] = curJoin;
						}

						// 如果是主表则直接用主表对应的 item，否则缓存副表数据到 childMap
						Join prevJoin = columnIndexAndJoinMap == null || i < 2 ? null : columnIndexAndJoinMap[i - 2];
						if (curJoin != prevJoin) {  // 前后字段不在同一个表对象，即便后面出现 null，也不该是主表数据，而是逻辑 bug 导致
							SQLConfig viceConfig = curJoin != null && curJoin.isSQLJoin() ? curJoin.getCacheConfig() : null;
							if (viceConfig != null) {  //FIXME 只有和主表关联才能用 item，否则应该从 childMap 查其它副表数据
								List<On> onList = curJoin.getOnList();
								int size = onList == null ? 0 : onList.size();
								if (size > 0) {
									for (int j = size - 1; j >= 0; j--) {
										On on = onList.get(j);
										String ok = on == null ? null : on.getOriginKey();
										if (ok == null) {
											throw new NullPointerException("服务器内部错误，List<Join> 中 Join.onList[" + j + (on == null ? "] = null！" : ".getOriginKey() = null！"));
										}

										viceConfig.putWhere(ok.substring(0, ok.length() - 1), item.get(on.getTargetKey()), true);
									}
								}
							}
							String viceSql = viceConfig == null ? null : viceConfig.getSQL(false);  //TODO 在 SQLConfig 缓存 SQL，减少大量的重复生成

							if (StringUtil.isEmpty(viceSql, true)) {
								Log.i(TAG, "execute StringUtil.isEmpty(viceSql, true) >> item = null; >> ");
								curItem = null;
							}
							else if (curJoin.isOuterJoin() || curJoin.isAntiJoin()) {
								Log.i(TAG, "execute curJoin.isOuterJoin() || curJoin.isAntiJoin() >> item = null; >> ");
								curItem = null;  // 肯定没有数据，缓存也无意义
								// 副表是按常规条件查询，缓存会导致其它同表同条件对象查询结果集为空		childMap.put(viceSql, new JSONObject());  // 缓存固定空数据，避免后续多余查询
							}
							else {
								curItem = childMap.get(viceSql);
								if (curItem == null) {
									curItem = new JSONObject(true);
									childMap.put(viceSql, curItem);
								}
							}
						}

						curItem = onPutColumn(config, rs, rsmd, index, curItem, i, curJoin, childMap);  // isExplain == false && hasJoin && i >= viceColumnStart ? childMap : null);
					}

					resultList = onPutTable(config, rs, rsmd, resultList, index, item);

					Log.d(TAG, "execute  while (rs.next()) { resultList.put( " + index + ", result); " + "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");
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

		if (unknownType || isExplain) {
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
			Map<String, List<JSONObject>> appJoinChildMap = new HashMap<>();
			childMap.forEach((viceSql, item) -> appJoinChildMap.put(viceSql, Arrays.asList(item)));
			executeAppJoin(config, resultList, appJoinChildMap);

			// @ APP JOIN 查询副表并缓存到 childMap >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			//子查询 SELECT Moment.*, Comment.id 中的 Comment 内字段
			Set<Entry<String, List<JSONObject>>> set = appJoinChildMap.entrySet();

			//<sql, Table>
			for (Entry<String, List<JSONObject>> entry : set) {
				putCache(entry.getKey(), entry.getValue(), null);
			}

			putCache(sql, resultList, config);
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
	protected void executeAppJoin(SQLConfig config, List<JSONObject> resultList, Map<String, List<JSONObject>> childMap) throws Exception {
		List<Join> joinList = config.getJoinList();
		if (joinList != null) {

			for (Join join : joinList) {
				if (join.isAppJoin() == false) {
					Log.i(TAG, "executeAppJoin  for (Join j : joinList) >> j.isAppJoin() == false >>  continue;");
					continue;
				}

				SQLConfig cc = join.getCacheConfig(); //这里用config改了getSQL后再还原很麻烦，所以提前给一个config2更好
				if (cc == null) {
					if (Log.DEBUG) {
						throw new NullPointerException("服务器内部错误, executeAppJoin cc == null ! 导致不能缓存 @ APP JOIN 的副表数据！");
					}
					continue;
				}

				SQLConfig jc = join.getJoinConfig();

				List<On> onList = join.getOnList();
				On on = onList == null || onList.isEmpty() ? null : onList.get(0);  // APP JOIN 应该有且只有一个 ON 条件
				String originKey = on == null ? null : on.getOriginKey();
				if (originKey == null) {
				throw new NullPointerException("服务器内部错误，List<Join> 中 Join.onList[0" + (on == null ? "] = null！" : ".getOriginKey() = null！"));
				}
				String key = on.getKey();
				if (key == null) {
				throw new NullPointerException("服务器内部错误，List<Join> 中 Join.onList[0" + (on == null ? "] = null！" : ".getKey() = null！"));
				}

				// 取出 "id@": "@/User/userId" 中所有 userId 的值
				List<Object> targetValueList = new ArrayList<>();

				for (int i = 0; i < resultList.size(); i++) {
				  JSONObject mainTable = resultList.get(i);
				  Object targetValue = mainTable == null ? null : mainTable.get(on.getTargetKey());

				  if (targetValue != null && targetValueList.contains(targetValue) == false) {
				    targetValueList.add(targetValue);
				  }
				}

				if (targetValueList.isEmpty() && config.isExplain() == false) {
				  throw new NotExistException("targetValueList.isEmpty() && config.isExplain() == false");
				}

				// 替换为 "id{}": [userId1, userId2, userId3...]
				jc.putWhere(originKey, null, false);  // remove originKey
				jc.putWhere(key + "{}", targetValueList, true);  // add originKey{}          }

							jc.setMain(true).setPreparedValueList(new ArrayList<>());

				// 放一块逻辑更清晰，也避免解析 * 等不支持或性能开销
				//        String q = jc.getQuote();
				//        if (allChildCount > 0 && jc.getCount() <= 0) {
				//          List<String> column = jc.getColumn();
				//          if (column == null || column.isEmpty()) {
				//            column = Arrays.asList("*;row_number()OVER(PARTITION BY " + q + key + q + " ORDER BY " + q + key + q + " ASC):_row_num_");
				//          }
				//          else {
				//            column.add("row_number()OVER(PARTITION BY " + q + key + q + " ORDER BY " + q + key + q + " ASC):_row_num_");
				//          }
				//          jc.setColumn(column);
				//        }

				int childCount = cc.getCount();
				int allChildCount = childCount*config.getCount();  // 所有分组子项数量总和
				boolean isOne2Many = childCount != 1 || join.isOne2Many();
				// 一对多会漏副表数据  TODO 似乎一对一走以下优化 row_number() <= childCount 逻辑也没问题
				//        if (isOne2Many == false && allChildCount > 0 && jc.getCount() < allChildCount) {
				//          jc.setCount(allChildCount);
				//        }

				boolean prepared = jc.isPrepared();
				String sql = jc.getSQL(false);
				jc.setPrepared(prepared);

				if (StringUtil.isEmpty(sql, true)) {
					throw new NullPointerException(TAG + ".executeAppJoin  StringUtil.isEmpty(sql, true) >> return null;");
				}

				String sql2 = null;
				if (childCount > 0 && isOne2Many && (jc.isMySQL() == false || jc.getDBVersionNums()[0] >= 8)) {
				//          加 row_number 字段并不会导致 count 等聚合函数统计出错，结果偏大，SQL JOIN 才会，之前没发现是因为缓存失效 bug
				//          boolean noAggrFun = true;
				//          List<String> column = jc.getColumn();
				//          if (column != null) {
				//            for (String c : column) {
				//              int start = c == null ? -1 : c.indexOf("(");
				//              int end = start <= 0 ? -1 : c.lastIndexOf(")");
				//              if (start > 0 && end > start) {
				//                String fun = c.substring(0, start);
				//                if (AbstractSQLConfig.SQL_AGGREGATE_FUNCTION_MAP.containsKey(fun)) {
				//                  noAggrFun = false;
				//                  break;
				//                }
				//              }
				//            }
				//          }
				//
				//          if (noAggrFun) { // 加 row_number 字段会导致 count 等聚合函数统计出错，结果偏大？
				  String q = jc.getQuote();
				  sql2 = prepared ? jc.getSQL(true) : sql;

				  String prefix = "SELECT * FROM(";
				  String rnStr = ", row_number() OVER (PARTITION BY " + q + key + q + ((AbstractSQLConfig) jc).getOrderString(true) + ") _row_num_ FROM ";
				  String suffix = ") _t WHERE ( (_row_num_ <= " + childCount + ") )" + (allChildCount > 0 ? " LIMIT " + allChildCount : "");

				  sql2 = prefix
				    // 放一块逻辑更清晰，也避免解析 * 等不支持或性能开销  + sql
				    + sql2.replaceFirst(" FROM ", rnStr)  // * 居然只能放在 row_number() 前面，放后面就报错 "SELECT ", rnStr)
				    + suffix;

				  sql = prepared ? (prefix + sql.replaceFirst(" FROM ", rnStr) + suffix) : sql2;
				//          }
				}

				boolean isExplain = jc.isExplain();
				if (isExplain == false) {
					generatedSQLCount ++;
				}

				long startTime = System.currentTimeMillis();
				Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
						+ "\n executeAppJoin  startTime = " + startTime
						+ "\n sql = \n " + sql
						+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

				//执行副表的批量查询 并 缓存到 childMap
				ResultSet rs = null;
				try {
					long executedSQLStartTime = 0;
					if (isExplain == false) { //只有 SELECT 才能 EXPLAIN
					    executedSQLCount ++;
					    executedSQLStartTime = System.currentTimeMillis();
					}
									rs = executeQuery(jc, sql2);
					if (isExplain == false) {
					    executedSQLDuration += System.currentTimeMillis() - executedSQLStartTime;
					}

					int count = 0;

					int index = -1;

					long startTime2 = System.currentTimeMillis();
					ResultSetMetaData rsmd = rs.getMetaData();
					final int length = rsmd.getColumnCount();
					sqlResultDuration += System.currentTimeMillis() - startTime2;

					Map<String, Boolean> skipMap = new HashMap<>();

					long lastCursorTime = System.currentTimeMillis();
					while ((allChildCount <= 0 || count < allChildCount) && rs.next()) { //FIXME 同时有 @ APP JOIN 和 < 等 SQL JOIN 时，next = false 总是无法进入循环，导致缓存失效，可能是连接池或线程问题
						sqlResultDuration += System.currentTimeMillis() - lastCursorTime;
						lastCursorTime = System.currentTimeMillis();

						index ++;
						Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n executeAppJoin while (rs.next()){  index = " + index + "\n\n");

						JSONObject result = new JSONObject(true);

						for (int i = 1; i <= length; i++) {
							result = onPutColumn(jc, rs, rsmd, index, result, i, null, null);
						}

						//每个 result 都要用新的 SQL 来存 childResultMap = onPutTable(config, rs, rsmd, childResultMap, index, result);

						Log.d(TAG, "\n executeAppJoin  while (rs.next()) { resultList.put(" + index + ", result); "
								+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");

						//TODO 兼容复杂关联
						cc.putWhere(key, result.get(key), true);  // APP JOIN 应该有且只有一个 ON 条件
						String cacheSql = cc.getSQL(false);
						List<JSONObject> results = childMap.get(cacheSql);

						if (results == null || skipMap.get(cacheSql) == null) {  // 避免添加重复数据
						      results = new ArrayList<>(childCount);
						      childMap.put(cacheSql, results);
						      skipMap.put(cacheSql, Boolean.TRUE);
						}

						if (childCount <= 0 || results.size() < childCount) {  // 避免超过子数组每页数量
						      //              if (count == 1 && results.isEmpty() == false) {  // 避免添加重复数据
						      //                results.clear();
						      //              }
						      results.add(result);  //缓存到 childMap
						      count ++;
						      Log.d(TAG, ">>> executeAppJoin childMap.put('" + cacheSql + "', result);  childMap.size() = " + childMap.size());
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
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, Join join, Map<String, JSONObject> childMap) throws Exception {
		if (table == null) {  // 对应副表 viceSql 不能生成正常 SQL， 或者是 ! - Outer, ( - ANTI JOIN 的副表这种不需要缓存及返回的数据
			Log.i(TAG, "onPutColumn table == null >> return table;");
			return table;
		}

		if (isHideColumn(config, rs, rsmd, tablePosition, table, columnIndex, childMap)) {
			Log.i(TAG, "onPutColumn isHideColumn(config, rs, rsmd, tablePosition, table, columnIndex, childMap) >> return table;");
			return table;
		}

		String label = getKey(config, rs, rsmd, tablePosition, table, columnIndex, childMap);
		Object value = getValue(config, rs, rsmd, tablePosition, table, columnIndex, label, childMap);

		// 主表必须 put 至少一个 null 进去，否则全部字段为 null 都不 put 会导致中断后续正常返回值
		if (value != null || (join == null && table.isEmpty())) {
			table.put(label, value);
		}

		return table;
	}

	/**如果不需要这个功能，在子类重写并直接 return false; 来提高性能
	 * @param config
	 * @param rs
	 * @param rsmd
	 * @param tablePosition
	 * @param table
	 * @param columnIndex
	 * @param childMap
	 * @return
	 * @throws SQLException
	 */
	protected boolean isHideColumn(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, Map<String, JSONObject> childMap) throws SQLException {
		return rsmd.getColumnName(columnIndex).startsWith("_");
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
		long startTime = System.currentTimeMillis();
		String key = rsmd.getColumnLabel(columnIndex);  // dotIndex < 0 ? lable : lable.substring(dotIndex + 1);
		sqlResultDuration += System.currentTimeMillis() - startTime;

		if (config.isHive()) {
			String tableName = config.getTable();
			String realTableName = AbstractSQLConfig.TABLE_KEY_MAP.get(tableName);

			String pattern = "^" + (StringUtil.isEmpty(realTableName, true) ? tableName : realTableName) + "\\." + "[a-zA-Z]+$";
			boolean isMatch = Pattern.matches(pattern, key);
			if (isMatch) {
				key = key.split("\\.")[1];
			}
		}

		return key;
	}

	protected Object getValue(@NotNull SQLConfig config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int tablePosition, @NotNull JSONObject table, final int columnIndex, String lable, Map<String, JSONObject> childMap) throws Exception {

		long startTime = System.currentTimeMillis();
		Object value = rs.getObject(columnIndex);
		sqlResultDuration += System.currentTimeMillis() - startTime;

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
		else if (value instanceof Date) {  // java.sql.Date 和 java.sql.Time 都继承 java.util.Date
			value = ((Date) value).toString();
		}
		else if (value instanceof LocalDateTime) {
			value = ((LocalDateTime) value).toString();
		}
		else if (value instanceof Year) {
			value = ((Year) value).getValue();
		}
		else if (value instanceof Month) {
			value = ((Month) value).getValue();
		}
		else if (value instanceof DayOfWeek) {
			value = ((DayOfWeek) value).getValue();
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
			long startTime = System.currentTimeMillis();
			String column = rsmd.getColumnTypeName(position);
			sqlResultDuration += System.currentTimeMillis() - startTime;

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



	@Override  // 重写是为了返回类型从 Statement 改为 PreparedStatement，避免其它方法出错
	public PreparedStatement getStatement(@NotNull SQLConfig config) throws Exception {
		return getStatement(config, null);
	}
	@Override
	public PreparedStatement getStatement(@NotNull SQLConfig config, String sql) throws Exception {
		if (StringUtil.isEmpty(sql)) {
			sql = config.getSQL(config.isPrepared());
		}

		PreparedStatement statement; //创建Statement对象
		if (config.getMethod() == RequestMethod.POST && config.getId() == null) { //自增id
			if (config.isOracle()) {
				// 解决 oracle 使用自增主键 插入获取不到id问题
				String[] generatedColumns = {config.getIdKey()};
				statement = getConnection(config).prepareStatement(sql, generatedColumns);
			} else {
				statement = getConnection(config).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}
		}
		else if (RequestMethod.isGetMethod(config.getMethod(), true)) {
			statement = getConnection(config).prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		else {
			statement = getConnection(config).prepareStatement(sql);
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

		// TDengine 驱动内部事务处理方法都是空实现，手动 commit 无效
		int ti = config.isTDengine() ? Connection.TRANSACTION_NONE : getTransactionIsolation();
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
			connection.setTransactionIsolation(transactionIsolation);  // 这句导致 TDengine 驱动报错
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
	public ResultSet executeQuery(@NotNull SQLConfig config, String sql) throws Exception {
		if (config.isPresto() || config.isTrino() || config.isTDengine()) {
			Connection conn = getConnection(config);
            Statement stt = config.isTDengine()
                    ? conn.createStatement() // fix Presto: ResultSet: Exception: set type is TYPE_FORWARD_ONLY, Result set concurrency must be CONCUR_READ_ONLY
                    : conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

            return executeQuery(stt, StringUtil.isEmpty(sql) ? config.getSQL(false) : sql);
		}

		PreparedStatement stt = getStatement(config, sql);
		ResultSet rs = stt.executeQuery();  //PreparedStatement 不用传 SQL
		//		if (config.isExplain() && (config.isSQLServer() || config.isOracle())) {
		// FIXME 返回的是 boolean 值			rs = stt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		//		}

		return rs;
	}


	@Override
	public int executeUpdate(@NotNull SQLConfig config, String sql) throws Exception {
		Statement stt;
		int count;
		if (config.isTDengine()) {
			Connection conn = getConnection(config);
			stt = conn.createStatement();
			count = stt.executeUpdate(StringUtil.isEmpty(sql) ? config.getSQL(false) : sql);
		}
		else {
			stt = getStatement(config);
			count = ((PreparedStatement) stt).executeUpdate();  // PreparedStatement 不用传 SQL
		}

		if (count <= 0 && config.isHive()) {
			count = 1;
		}

		if (config.getId() == null && config.getMethod() == RequestMethod.POST) {  // 自增id
			ResultSet rs = stt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				config.setId(rs.getLong(1));
			}
		}

		return count;
	}


}
