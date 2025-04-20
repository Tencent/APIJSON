/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import apijson.*;
import apijson.orm.Join.On;
import apijson.orm.exception.NotExistException;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public abstract class AbstractSQLExecutor<T, M extends Map<String, Object>, L extends List<Object>>
		implements SQLExecutor<T, M, L> {
	private static final String TAG = "AbstractSQLExecutor";
	//是否返回 值为null的字段
	public static boolean ENABLE_OUTPUT_NULL_COLUMN = false;
	public static String KEY_RAW_LIST = "@RAW@LIST";  // 避免和字段命名冲突，不用 $RAW@LIST$ 是因为 $ 会在 fastjson 内部转义，浪费性能
	public static String KEY_VICE_ITEM = "@VICE@ITEM";  // 避免和字段命名冲突，不用 $VICE@LIST$ 是因为 $ 会在 fastjson 内部转义，浪费性能

	private Parser<T, M, L> parser;
	@Override
	public Parser<T, M, L> getParser() {
		return parser;
	}
	@Override
	public AbstractSQLExecutor<T, M, L> setParser(Parser<T, M, L> parser) {
		this.parser = parser;
		return this;
	}

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
	protected Map<String, List<M>> cacheMap = new HashMap<>();

	/**保存缓存
	 * @param sql  key
	 * @param list  value
	 * @param config  一般主表 SQLConfig<T, M, L> 不为 null，JOIN 副表的为 null
	 */
	@Override
	public void putCache(String sql, List<M> list, SQLConfig<T, M, L> config) {
		if (sql == null || list == null) { // 空 list 有效，说明查询过 sql 了  || list.isEmpty()) {
			Log.i(TAG, "saveList  sql == null || list == null >> return;");
			return;
		}

		cacheMap.put(sql, list);
	}

	/**获取缓存
	 * @param sql  key
	 * @param config  一般主表 SQLConfig<T, M, L> 不为 null，JOIN 副表的为 null
	 */
	@Override
	public List<M> getCache(String sql, SQLConfig<T, M, L> config) {
		return cacheMap.get(sql);
	}

	/**获取缓存
	 * @param sql  key
	 * @param position
	 * @param config  一般主表 SQLConfig<T, M, L> 不为 null，JOIN 副表的为 null
	 * @return
	 */
	@Override
	public M getCacheItem(String sql, int position, SQLConfig<T, M, L> config) {
		List<M> list = getCache(sql, config);
		return getCacheItem(list, position, config);
	}

	public M getCacheItem(List<M> list, int position, SQLConfig<T, M, L> config) {
		// 只要 list 不为 null，则如果 list.get(position) == null，则返回 {} ，避免再次 SQL 查询
		if (list == null) {
			return null;
		}

		M result = position >= list.size() ? null : list.get(position);
		return result != null ? result : JSON.createJSONObject();
	}



	/**移除缓存
	 * @param sql  key
	 * @param config
	 */
	@Override
	public void removeCache(String sql, SQLConfig<T, M, L> config) {
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
	public M execute(@NotNull SQLConfig<T, M, L> config, boolean unknownType) throws Exception {
		long executedSQLStartTime = System.currentTimeMillis();
		final String sql = config.gainSQL(false);

		if (StringUtil.isEmpty(sql, true)) {
			Log.e(TAG, "execute  StringUtil.isEmpty(sql, true) >> return null;");
			return null;
		}

		Parser<T, M, L> parser2 = config.gainParser();
		parser = parser2 != null ? parser2 : getParser();;

		boolean isExplain = config.isExplain();
		boolean isHead = RequestMethod.isHeadMethod(config.getMethod(), true);

		final int position = config.getPosition();
		M result;

		if (isExplain == false) {
			generatedSQLCount ++;
		}

		long startTime = System.currentTimeMillis();
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
				+ "\n已生成 " + generatedSQLCount + " 条 SQL"
				+ "\nexecute  startTime = " + startTime
				+ "\ndatabase = " + StringUtil.get(config.getDatabase())
				+ "; schema = " + StringUtil.get(config.getSchema())
				+ "; sql = \n" + sql
				+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		ResultSet rs = null;
		List<M> resultList = null;
		Map<String, M> childMap = null;
		Map<String, String> keyMap = null;

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

				result = JSON.createJSONObject();
				result.put(JSONResponse.KEY_COUNT, updateCount);
				result.put("update", updateCount >= 0);
				//导致后面 rs.getMetaData() 报错 Operation not allowed after ResultSet closed		result.put("moreResults", statement.getMoreResults());
			}
			else {
				RequestMethod method = config.getMethod();
				switch (method) {
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
					result = parser.newSuccessResult();  // TODO 对 APIAuto 及其它现有的前端/客户端影响比较大，暂时还是返回 code 和 msg，5.0 再移除  JSON.createJSONObject();

					//id,id{}至少一个会有，一定会返回，不用抛异常来阻止关联写操作时前面错误导致后面无条件执行！
					result.put(JSONResponse.KEY_COUNT, updateCount);//返回修改的记录数

					String idKey = config.getIdKey();
					if (config.getId() != null) {
						result.put(idKey, config.getId());
					}
					if (config.getIdIn() != null) {
						result.put(idKey + "[]", config.getIdIn());
					}

					if (method == RequestMethod.PUT || method == RequestMethod.DELETE) {
						config.setMethod(RequestMethod.GET);
						removeCache(config.gainSQL(false), config);
						config.setMethod(method);
					}

					return result;

				case GET:
				case GETS:
				case HEAD:
				case HEADS:
					List<M> cache = getCache(sql, config);
					result = getCacheItem(cache, position, config);
					Log.i(TAG, ">>> execute  result = getCache('" + sql + "', " + position + ") = " + result);
					if (result != null) {
						if (isExplain == false) {
							cachedSQLCount ++;
						}

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
					return parser.newErrorResult(new SQLException("数据库错误, rs.next() 失败！"));
				}

				result = parser.newSuccessResult();
				// 兼容nosql,比如 elasticSearch-sql
				if(config.isElasticsearch()) {
					result.put(JSONResponse.KEY_COUNT, rs.getObject(1));
				}else {
					result.put(JSONResponse.KEY_COUNT, rs.getLong(1));
				}
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


				List<Join<T, M, L>> joinList = config.getJoinList();
				boolean hasJoin = config.hasJoin() && joinList != null && ! joinList.isEmpty();

				// 直接用数组存取更快  Map<Integer, Join> columnIndexAndJoinMap = isExplain || ! hasJoin ? null : new HashMap<>(length);
				Join[] columnIndexAndJoinMap = isExplain || ! hasJoin ? null : new Join[length];
				Map<String, Integer> repeatMap = columnIndexAndJoinMap == null || ! config.isQuestDB() ? null : new HashMap<>();
				keyMap = repeatMap == null ? null : new HashMap<>();

//				int viceColumnStart = length + 1; //第一个副表字段的index

//				FIXME 统计游标查找的时长？可能 ResultSet.next() 及 getTableName, getColumnName, getObject 比较耗时，因为不是一次加载到内存，而是边读边发

				long lastCursorTime = System.currentTimeMillis();
				while (rs.next()) {
					sqlResultDuration += System.currentTimeMillis() - lastCursorTime;
					lastCursorTime = System.currentTimeMillis();

					index ++;
					Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n execute while (rs.next()){  index = " + index + "\n\n");

					M item = JSON.createJSONObject();
					M viceItem = null;
					M curItem = item;
					boolean isMain = true;
					boolean reseted = false;

					for (int i = 1; i <= length; i++) {

						// if (hasJoin && viceColumnStart > length && config.getSQLTable().equalsIgnoreCase(rsmd.getTableName(i)) == false) {
						// 	viceColumnStart = i;
						// }

						// bugfix-修复非常规数据库字段，获取表名失败导致输出异常
						Join curJoin = columnIndexAndJoinMap == null ? null : columnIndexAndJoinMap[i - 1];  // columnIndexAndJoinMap.get(i);

						// 为什么 isExplain == false 不用判断？因为所有字段都在一张 Query Plan 表
						if (index <= 0 && columnIndexAndJoinMap != null) { // && viceColumnStart > length) {

							SQLConfig<T, M, L> curConfig = curJoin == null || ! curJoin.isSQLJoin() ? null : curJoin.getCacheConfig();
							List<String> curColumn = curConfig == null ? null : curConfig.getColumn();
							String sqlTable = curConfig == null ? null : curConfig.gainSQLTable();
							String sqlAlias = curConfig == null ? null : curConfig.getAlias();

							List<String> column = config.getColumn();
							int mainColumnSize = column == null ? 0 : column.size();
							boolean toFindJoin = mainColumnSize <= 0 || i > mainColumnSize;  // 主表就不用找 JOIN 配置

							if (StringUtil.isEmpty(sqlTable, true)) {
								//sqlTable = null;

								if (toFindJoin) {  // 在主表字段数量内的都归属主表
									long startTime3 = System.currentTimeMillis();
									sqlTable = rsmd.getTableName(i);  // SQL 函数甚至部分字段都不返回表名，当然如果没传 @column 生成的 Table.* 则返回的所有字段都会带表名

                                    //if (StringUtil.isEmpty(sqlTable, true)) {
									//   boolean isEmpty = curItem == null || curItem.isEmpty();
                                        String key = getKey(config, rs, rsmd, index, curItem, i, childMap, keyMap);
										char last = repeatMap == null ? 0 : key.charAt(key.length() - 1);
										String repeatKey = last < '0' || last > '9' ? null : key.substring(0, key.length() - 1);
										Integer repeatCount = repeatKey == null ? null : repeatMap.get(repeatKey);
										int nc = repeatCount == null ? 1 : repeatCount + 1;
										if (last == nc + '0') {
											keyMap.put(key, repeatKey);
											repeatMap.put(repeatKey, nc);
											key = repeatKey; // QuestDB 会自动把副表与主表同名的字段重命名，例如 id 改为 id1, date 改为 date1
										}

                                        if (i > 1 && ( (curItem != null && curItem.containsKey(key))
												|| (StringUtil.isNotEmpty(key) && StringUtil.equals(key, curConfig == null ? null : curConfig.getIdKey())))
										) { // Presto 等引擎 JDBC 返回 rsmd.getTableName(i) 为空，主表如果一个字段都没有会导致 APISJON 主副表所有字段都不返回
											sqlTable = null;
											if (reseted) {
												SQLConfig<T, M, L> lastCfg = lastJoin == null ? null : lastJoin.getCacheConfig();
												List<String> lastColumn = lastCfg == null ? null : lastCfg.getColumn();

												lastViceTableStart ++;
												lastViceColumnStart += lastColumn == null ? 1 : lastColumn.size();
											}
											else if (isMain) {
												for (int j = 0; j < joinList.size(); j++) {
													Join<T, M, L> join = joinList.get(j);
													SQLConfig<T, M, L> cfg = join == null || ! join.isSQLJoin() ? null : join.getJoinConfig();
													List<String> c = cfg == null ? null : cfg.getColumn();

													if (cfg != null) {
														sqlTable = cfg.gainSQLTable();
														sqlAlias = cfg.getAlias();
														lastViceTableStart = j;  // 避免后面的空 @column 表内字段被放到之前的空 @column 表
														lastViceColumnStart = i + 1;

														curJoin = join;
														curConfig = cfg;
														curColumn = c;

														toFindJoin = false;
														isMain = false;
														break;
													}
												}
											}

											reseted = true;
										}
                                    //}
									sqlResultDuration += System.currentTimeMillis() - startTime3;

									if (toFindJoin && StringUtil.isEmpty(sqlTable, true)) {  // hasJoin 已包含这个判断 && joinList != null) {
										//sqlTable = null; // QuestDB 等 rsmd.getTableName(i) 返回 "" 导致以下 StringUtil.equalsIgnoreCase 对比失败

										int nextViceColumnStart = lastViceColumnStart;  // 主表没有 @column 时会偏小 lastViceColumnStart
										int joinCount = joinList.size();
										for (int j = lastViceTableStart; j < joinCount; j++) {  // 查找副表 @column，定位字段所在表
											Join<T, M, L> join = joinList.get(j);
											SQLConfig<T, M, L> cfg = join == null || ! join.isSQLJoin() ? null : join.getJoinConfig();
											List<String> c = cfg == null ? null : cfg.getColumn();

											nextViceColumnStart += (c != null && ! c.isEmpty() ?
													c.size() : (
													StringUtil.equalsIgnoreCase(sqlTable, lastTableName)
															&& StringUtil.equals(sqlAlias, lastAliasName) ? 1 : 0
													)
											);
											if (i < nextViceColumnStart) { // 导致只 JOIN 一张副表时主表数据放到副表 || j >= joinCount - 1) {
												sqlTable = cfg.gainSQLTable();
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
							} else if (config.isClickHouse() && (sqlTable.startsWith("`") || sqlTable.startsWith("\""))){
								sqlTable = sqlTable.substring(1, sqlTable.length() - 1);
							}

							if (StringUtil.equalsIgnoreCase(sqlTable, lastTableName) == false || StringUtil.equals(sqlAlias, lastAliasName) == false) {
								lastTableName = sqlTable;
								lastAliasName = sqlAlias;
								lastViceColumnStart = i;

								if (toFindJoin) {  // 找到对应的副表 JOIN 配置
									for (int j = lastViceTableStart; j < joinList.size(); j++) {  // 查找副表 @column，定位字段所在表
										Join join = joinList.get(j);
										SQLConfig<T, M, L> cfg = join == null || ! join.isSQLJoin() ? null : join.getJoinConfig();

										if (cfg != null && StringUtil.equalsIgnoreCase(sqlTable, cfg.gainSQLTable())
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
							SQLConfig<T, M, L> viceConfig = curJoin != null && curJoin.isSQLJoin() ? curJoin.getCacheConfig() : null;
							boolean hasPK = false;
							if (viceConfig != null) {  //FIXME 只有和主表关联才能用 item，否则应该从 childMap 查其它副表数据
								List<On> onList = curJoin.getOnList();
								int size = onList == null ? 0 : onList.size();
								if (size > 0) {
									String idKey = viceConfig.getIdKey();
									String tblKey = config.gainTableKey();
									for (int j = size - 1; j >= 0; j--) {
										On on = onList.get(j);
										String ok = on == null ? null : on.getOriginKey();
										if (ok == null) {
											throw new NullPointerException("服务器内部错误，List<Join> 中 Join.onList[" + j + (on == null ? "] = null！" : ".getOriginKey() = null！"));
										}

										String k = ok.substring(0, ok.length() - 1);
										String ttk = on.getTargetTableKey();

										M target = StringUtil.equals(ttk, tblKey) ? item : (viceItem == null ? null : JSON.get(viceItem, ttk));
										Object v = target == null ? null : target.get(on.getTargetKey());
										hasPK = hasPK || (k.equals(idKey) && v != null);

										viceConfig.putWhere(k, v, true);
									}
								}
							}

							if (viceConfig == null) { // StringUtil.isEmpty(viceSql, true)) {
								Log.i(TAG, "execute viceConfig == null >> item = null; >> ");
								curItem = null;
							}
							else if (curJoin.isOuterJoin() || curJoin.isAntiJoin()) {
								Log.i(TAG, "execute curJoin.isOuterJoin() || curJoin.isAntiJoin() >> item = null; >> ");
								curItem = null;  // 肯定没有数据，缓存也无意义
								// 副表是按常规条件查询，缓存会导致其它同表同条件对象查询结果集为空	childMap.put(viceSql, JSON.createJSONObject());  // 缓存固定空数据，避免后续多余查询
							}
							else {
								String viceName = viceConfig.gainTableKey();
								if (viceItem == null) {
									viceItem = JSON.createJSONObject();
								}
								curItem = JSON.get(viceItem, viceName);

								String viceSql = hasPK ? viceConfig.gainSQL(false) : null; // TODO 在 SQLConfig<T, M, L> 缓存 SQL，减少大量的重复生成
								M curCache = hasPK ? childMap.get(viceSql) : null;

								if (curItem == null || curItem.isEmpty()) {
									// 导致前面判断重复 key 出错 curItem = curCache != null ? curCache : JSON.createJSONObject();
									curItem = JSON.createJSONObject();
									viceItem.put(viceName, curItem);
									if (hasPK && curCache == null) {
										childMap.put(viceSql, curItem);
									}
								}
								else if (hasPK) {
									if (curCache == null || curCache.isEmpty()) {
										childMap.put(viceSql, curItem);
									}
									else {
										curCache.putAll(curItem);
										// 导致前面判断重复 key 出错 curItem = curCache;
										// viceItem.put(viceName, curItem);
									}
								}
							}
						}

						curItem = (M) onPutColumn(config, rs, rsmd, index, curItem, i, curJoin, childMap, keyMap);  // isExplain == false && hasJoin && i >= viceColumnStart ? childMap : null);
					}

					if (viceItem != null) {
						item.put(KEY_VICE_ITEM, viceItem);
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
					result = JSON.createJSONObject();
				}
				config.setExplain(false);
				result.put("sql", config.gainSQL(false));
				config.setExplain(isExplain);
			}
			result.put("list", resultList);

			if (unknownType == false) {
				putCache(sql, Arrays.asList(result), config);
			}

			return result;
		}

		if (isHead == false) {
			// @ APP JOIN 查询副表并缓存到 childMap <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			Map<String, List<M>> appJoinChildMap = new HashMap<>();
			childMap.forEach((viceSql, item) -> appJoinChildMap.put(viceSql, Arrays.asList(item)));
			executeAppJoin(config, resultList, appJoinChildMap, keyMap);

			// @ APP JOIN 查询副表并缓存到 childMap >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			//子查询 SELECT Moment.*, Comment.id 中的 Comment 内字段
			Set<Entry<String, List<M>>> set = appJoinChildMap.entrySet();

			//<sql, Table>
			for (Entry<String, List<M>> entry : set) {
				putCache(entry.getKey(), entry.getValue(), null);
			}

			Log.i(TAG, ">>> execute  putCache('" + sql + "', resultList);  resultList.size() = " + resultList.size());

			// 数组主表对象额外一次返回全部，方便 Parser<T, M, L> 缓存来提高性能

			result = position >= resultList.size() ? JSON.createJSONObject() : resultList.get(position);
			if (position == 0 && resultList.size() > 1 && result != null && result.isEmpty() == false) {
				// 不是 main 不会直接执行，count=1 返回的不会超过 1   && config.isMain() && config.getCount() != 1
				Log.i(TAG, ">>> execute  position == 0 && resultList.size() > 1 && result != null && result.isEmpty() == false"
						+ " >> result = JSON.createJSONObject(result); result.put(KEY_RAW_LIST, resultList);");

				result = (M) JSON.createJSONObject(result);
				result.put(KEY_RAW_LIST, resultList);
			}
		}

		putCache(sql, resultList, config);

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
	protected void executeAppJoin(SQLConfig<T, M, L> config, List<M> resultList, Map<String, List<M>> childMap, Map<String, String> keyMap) throws Exception {
		List<Join<T, M, L>> joinList = config.getJoinList();
		if (joinList != null) {

			for (Join<T, M, L> join : joinList) {
				if (join.isAppJoin() == false) {
					Log.i(TAG, "executeAppJoin  for (Join j : joinList) >> j.isAppJoin() == false >>  continue;");
					continue;
				}

				SQLConfig<T, M, L> cc = join.getCacheConfig(); //这里用config改了getSQL后再还原很麻烦，所以提前给一个config2更好
				if (cc == null) {
					if (Log.DEBUG) {
						throw new NullPointerException("服务器内部错误, executeAppJoin cc == null ! 导致不能缓存 @ APP JOIN 的副表数据！");
					}
					continue;
				}

				SQLConfig<T, M, L> jc = join.getJoinConfig();

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
					M mainTable = resultList.get(i);
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
				String sql = jc.gainSQL(false);

				if (StringUtil.isEmpty(sql, true)) {
					throw new NullPointerException(TAG + ".executeAppJoin  StringUtil.isEmpty(sql, true) >> return null;");
				}

				String sql2 = null;
				if (childCount > 0 && isOne2Many && (jc.isMySQL() == false || jc.gainDBVersionNums()[0] >= 8)) {
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
					sql2 = prepared && jc.isTDengine() == false ? jc.gainSQL(true) : sql;

					String prefix = "SELECT * FROM(";
					String rnStr = ", row_number() OVER (PARTITION BY " + q + key + q + ((AbstractSQLConfig) jc).gainOrderString(true) + ") _row_num_ FROM ";
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

						M result = JSON.createJSONObject();

						for (int i = 1; i <= length; i++) {
							result = onPutColumn(jc, rs, rsmd, index, result, i, null, null, keyMap);
						}

						//每个 result 都要用新的 SQL 来存 childResultMap = onPutTable(config, rs, rsmd, childResultMap, index, result);

						Log.d(TAG, "\n executeAppJoin  while (rs.next()) { resultList.put(" + index + ", result); "
								+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");

						//TODO 兼容复杂关联
						cc.putWhere(key, result.get(key), true);  // APP JOIN 应该有且只有一个 ON 条件
						String cacheSql = cc.gainSQL(false);
						List<M> results = childMap.get(cacheSql);

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
	 * @param row 从0开始
	 * @param table
	 * @param columnIndex 从1开始
	 * @param childMap
	 * @return result
	 * @throws Exception
	 */
	protected M onPutColumn(@NotNull SQLConfig<T, M, L> config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int row, @NotNull M table, final int columnIndex, Join<T, M, L> join, Map<String, M> childMap
			, Map<String, String> keyMap) throws Exception {
		if (table == null) {  // 对应副表 viceSql 不能生成正常 SQL， 或者是 ! - Outer, ( - ANTI JOIN 的副表这种不需要缓存及返回的数据
			Log.i(TAG, "onPutColumn table == null >> return table;");
			return table;
		}

		if (isHideColumn(config, rs, rsmd, row, table, columnIndex, childMap, keyMap)) {
			Log.i(TAG, "onPutColumn isHideColumn(config, rs, rsmd, row, table, columnIndex, childMap) >> return table;");
			return table;
		}

		String label = getKey(config, rs, rsmd, row, table, columnIndex, childMap, keyMap);
		Object value = getValue(config, rs, rsmd, row, table, columnIndex, label, childMap, keyMap);

		// 主表必须 put 至少一个 null 进去，否则全部字段为 null 都不 put 会导致中断后续正常返回值
		if (value != null || ENABLE_OUTPUT_NULL_COLUMN || (join == null && table.isEmpty())) {
			table.put(label, value);
		}

		return table;
	}

	/**如果不需要这个功能，在子类重写并直接 return false; 来提高性能
	 * @param config
	 * @param rs
	 * @param rsmd
	 * @param row
	 * @param table
	 * @param columnIndex
	 * @param childMap
	 * @return
	 * @throws SQLException
	 */
	protected boolean isHideColumn(@NotNull SQLConfig<T, M, L> config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int row, @NotNull M table, final int columnIndex, Map<String, M> childMap
			, Map<String, String> keyMap) throws SQLException {
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
	protected List<M> onPutTable(@NotNull SQLConfig<T, M, L> config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, @NotNull List<M> resultList, int position, @NotNull M table) {

		resultList.add(table);
		return resultList;
	}

	protected String getKey(@NotNull SQLConfig<T, M, L> config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int row, @NotNull M table, final int columnIndex, Map<String, M> childMap
			, Map<String, String> keyMap) throws Exception {
		long startTime = System.currentTimeMillis();
		String key = rsmd.getColumnLabel(columnIndex);  // dotIndex < 0 ? label : label.substring(dotIndex + 1);
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

		if (keyMap != null && ! keyMap.isEmpty()) {
			String nk = keyMap.get(key);
			if (StringUtil.isNotEmpty(nk, true)) {
				key = nk; // QuestDB 会自动把副表与主表同名的字段重命名，例如 id 改为 id1, date 改为 date1
			}
		}

		return key;
	}

	protected Object getValue(@NotNull SQLConfig<T, M, L> config, @NotNull ResultSet rs, @NotNull ResultSetMetaData rsmd
			, final int row, @NotNull M table, final int columnIndex, String label
			, Map<String, M> childMap, Map<String, String> keyMap) throws Exception {

		long startTime = System.currentTimeMillis();
		Object value = rs.getObject(columnIndex);
		sqlResultDuration += System.currentTimeMillis() - startTime;

		//					Log.d(TAG, "name:" + rsmd.getColumnName(i));
		//					Log.d(TAG, "label:" + rsmd.getColumnLabel(i));
		//					Log.d(TAG, "type:" + rsmd.getColumnType(i));
		//					Log.d(TAG, "typeName:" + rsmd.getColumnTypeName(i));

		//				Log.i(TAG, "select  while (rs.next()) { >> for (int i = 0; i < length; i++) {"
		//						+ "\n  >>> value = " + value);

		boolean castToJson = false;

		//数据库查出来的null和empty值都有意义，去掉会导致 Moment:{ @column:"content" } 部分无结果及中断数组查询！
		if (value instanceof Boolean) {
			//加快判断速度
		}
		else if (value instanceof Number) {
			value = getNumVal((Number) value);
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
		else if (value instanceof String && isJSONType(config, rsmd, columnIndex, label)) { //json String
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
			castToJson = json != null && json.contains(label);
		}
		if (castToJson) {
			try {
				value = JSON.parse(value);
			} catch (Exception e) {
				Log.e(TAG, "getValue  try { value = parseJSON((String) value); } catch (Exception e) { \n" + e.getMessage());
			}
		}

		return value;
	}

	public Object getNumVal(Number value) {
		if (value == null) {
			return null;
		}

		if (value instanceof BigInteger) {
			return ((BigInteger) value).toString();
		}

		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).toString();
		}

		double v = value.doubleValue();
		// if (v > Integer.MAX_VALUE || v < Integer.MIN_VALUE) { // 避免前端/客户端拿到精度丢失甚至严重失真的值
		// 	return value.toString();
		// }
		// JavaScript: Number.MAX_SAFE_INTEGER ~ Number.MIN_SAFE_INTEGER
		if (v > 9007199254740991L || v < -9007199254740991L) { // 避免前端/客户端拿到精度丢失甚至严重失真的值
			return value.toString();
		}
		
		return value;
	}


	/**判断是否为JSON类型
	 * @param config
	 * @param label
	 * @param rsmd
	 * @param position
	 * @return
	 */
	@Override
	public boolean isJSONType(@NotNull SQLConfig<T, M, L> config, ResultSetMetaData rsmd, int position, String label) {
		try {
			long startTime = System.currentTimeMillis();
			String column = rsmd.getColumnTypeName(position);
			sqlResultDuration += System.currentTimeMillis() - startTime;
			// nosql elasticSearch jdbc获取不到 字段类型
			if(StringUtil.isEmpty(column)) {
				return false;
			}
			//TODO CHAR和JSON类型的字段，getColumnType返回值都是1	，如果不用CHAR，改用VARCHAR，则可以用上面这行来提高性能。
			//return rsmd.getColumnType(position) == 1;

			if (column.toLowerCase().contains("json")) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//		List<String> json = config.getJson();
		//		return json != null && json.contains(label);
		return false;
	}


	@Override  // 重写是为了返回类型从 Statement 改为 PreparedStatement，避免其它方法出错
	public PreparedStatement getStatement(@NotNull SQLConfig<T, M, L> config) throws Exception {
		return getStatement(config, null);
	}

	@Override
	public PreparedStatement getStatement(@NotNull SQLConfig<T, M, L> config, String sql) throws Exception {
		if (StringUtil.isEmpty(sql)) {
			sql = config.gainSQL(config.isPrepared());
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
            // if (config.isPresto() || config.isTrino()) {
            //    statement = getConnection(config).prepareStatement(sql); // , ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            // } else {
            //    statement = getConnection(config).prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            // }

			// TODO 补充各种支持 TYPE_SCROLL_SENSITIVE 和 CONCUR_UPDATABLE 的数据库
            if (config.isMySQL() || config.isTiDB() || config.isMariaDB() || config.isOracle() || config.isSQLServer() || config.isDb2()
					|| config.isPostgreSQL() || config.isCockroachDB() || config.isOpenGauss() || config.isTimescaleDB() || config.isQuestDB()
			) {
                statement = getConnection(config).prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } else {
                statement = getConnection(config).prepareStatement(sql);
            }
		}
		else {
			statement = getConnection(config).prepareStatement(sql);
		}

		List<Object> valueList = config.isPrepared() ? config.getPreparedValueList() : null;
//		List<Object> withAsExprePreparedValueList = config.isPrepared() ? config.getWithAsExprePreparedValueList() : null;
//
//		// 不同数据库, 预编译mysql使用with-as
//		if (valueList != null && withAsExprePreparedValueList != null && withAsExprePreparedValueList.size() > 0) {
//			withAsExprePreparedValueList.addAll(valueList);
//			valueList = withAsExprePreparedValueList;
//			// 多条POST/PUT/DELETE语句的情况,需要重新初始化
//			config.setWithAsExprePreparedValueList(new ArrayList<>());
//		}

		if (valueList != null && valueList.isEmpty() == false) {
			for (int i = 0; i < valueList.size(); i++) {
				statement = setArgument(config, statement, i, valueList.get(i));
			}
		}
		// statement.close();

		return statement;
	}

	public PreparedStatement setArgument(@NotNull SQLConfig<T, M, L> config, @NotNull PreparedStatement statement, int index, Object value) throws SQLException {
		//JSON.isBooleanOrNumberOrString(v) 解决 PostgreSQL: Can't infer the SQL type to use for an instance of com.alibaba.fastjson.JSONList
		if (apijson.JSON.isBoolOrNumOrStr(value)) {
			statement.setObject(index + 1, value); //PostgreSQL JDBC 不支持隐式类型转换 tinyint = varchar 报错
		}
		else {
			statement.setString(index + 1, value == null ? null : value.toString()); //MySQL setObject 不支持 JSON 类型
		}
		return statement;
	}

	protected Map<String, Connection> connectionMap = new HashMap<>();
	public Map<String, Connection> getConnectionMap() {
		if (connectionMap == null) {
			connectionMap = new HashMap<>();
		}
		return connectionMap;
	}

	protected Connection connection;
	@NotNull
	@Override
	public Connection getConnection(@NotNull SQLConfig<T, M, L> config) throws Exception {
		String connectionKey = getConnectionKey(config);
		connection = getConnectionMap().get(connectionKey);
		if (connection == null || connection.isClosed()) {
			Log.i(TAG, "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
			// PostgreSQL 不允许 cross-database
			connection = DriverManager.getConnection(config.gainDBUri(), config.gainDBAccount(), config.gainDBPassword());
			getConnectionMap().put(connectionKey, connection);
		}

		// TDengine 驱动内部事务处理方法都是空实现，手动 commit 无效
		int ti = config.isTDengine() ? Connection.TRANSACTION_NONE : getTransactionIsolation();
		if (ti != Connection.TRANSACTION_NONE) { //java.sql.SQLException: Transaction isolation level NONE not supported by MySQL
			begin(ti);
		}

		return connection;
	}

	public String getConnectionKey(@NotNull SQLConfig<T, M, L> config) {
		return getConnectionKey(config.getNamespace(), config.getCatalog(), config.getDatasource(), config.getDatabase());
	}
	public String getConnectionKey(String database, String datasource, String namespace, String catalog) {
		return database + "-" + datasource + "-" + namespace + "-" + catalog;
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

	protected Map<Connection, Integer> isolationMap = new LinkedHashMap<>();
	@Override
	public void begin(int transactionIsolation) throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION begin transactionIsolation = " + transactionIsolation + " >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		// 不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
		//		if (connection == null || connection.isClosed()) {
		//			return;
		//		}

		// 将所有连接设置隔离级别，且禁止自动提交，需要以下代码来 commit/rollback
		Collection<Connection> connections = connectionMap == null ? null : connectionMap.values();
		if (connections != null) {
			for (Connection connection : connections) {
				try {
					Integer isolation = isolationMap.get(connection);
					if (isolation == null || isolation != transactionIsolation) { // 只设置一次 Isolation 等级 PG 及 MySQL 某些版本重复设置事务等级会报错
						isolationMap.put(connection, transactionIsolation);

						connection.setTransactionIsolation(transactionIsolation); // 这句导致 TDengine 驱动报错
						if (isolation == null) {
							connection.setAutoCommit(false); // java.sql.SQLException: Can''t call commit when autocommit=true
						}
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void rollback() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION rollback >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
//		if (connection == null) { // || connection.isClosed()) {
//			return;
//		}

		// 将所有连接进行回滚
		Collection<Connection> connections = connectionMap == null ? null : connectionMap.values();
		if (connections != null) {
			for (Connection connection : connections) {
				try {
					if (connection != null && connection.isClosed() == false) {
						connection.rollback();
						connection.setAutoCommit(true);

						isolationMap.remove(connection);
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION rollback savepoint " + (savepoint == null ? "" : "!") + "= null >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		if (savepoint == null) {
			rollback();
			return;
		}

		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
//		if (connection == null) { // || connection.isClosed()) {
//			return;
//		}

		// 将所有连接进行回滚
		Collection<Connection> connections = connectionMap == null ? null : connectionMap.values();
		if (connections != null) {
			for (Connection connection : connections) {
				try {
					if (connection != null && connection.isClosed() == false) {
						connection.rollback(savepoint);
						connection.setAutoCommit(true);

						isolationMap.remove(connection);
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void commit() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<< TRANSACTION commit >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		//权限校验不通过，connection 也不会生成，还是得判断  //不做判断，如果掩盖了问题，调用层都不知道为啥事务没有提交成功
//		if (connection == null) { // || connection.isClosed()) {
//			return;
//		}
		
		// 将所有连接进行提交
		Collection<Connection> connections = connectionMap == null ? null : connectionMap.values();
		if (connections != null) {
			for (Connection connection : connections) {
				try {
					if (connection != null && connection.isClosed() == false) {
						connection.commit();

						isolationMap.remove(connection);
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
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

		if (connectionMap == null || connectionMap.isEmpty()) {
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
	public ResultSet executeQuery(@NotNull SQLConfig<T, M, L> config, String sql) throws Exception {
		if (config.isPrepared() == false || config.isTDengine() // TDengine JDBC 不支持 PreparedStatement
            || (config.isExplain() && (config.isPresto() || config.isTrino()))) { // Presto JDBC 0.277 在 EXPLAIN 模式下预编译值不会替代 ? 占位导致报错

            Connection conn = getConnection(config);
            Statement stt = conn.createStatement();
            // Statement stt = config.isTDengine()
            //        ? conn.createStatement() // fix Presto: ResultSet: Exception: set type is TYPE_FORWARD_ONLY, Result set concurrency must be CONCUR_READ_ONLY
            //        : conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

            return executeQuery(stt, StringUtil.isEmpty(sql) ? config.gainSQL(false) : sql);
		}

        // Presto JDBC 0.277 在 EXPLAIN 模式下预编译值不会替代 ? 占位导致报错
		PreparedStatement stt = getStatement(config, sql);
		ResultSet rs = stt.executeQuery();  //PreparedStatement 不用传 SQL
		//		if (config.isExplain() && (config.isSQLServer() || config.isOracle())) {
		// FIXME 返回的是 boolean 值			rs = stt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
		//		}

		return rs;
	}


	@Override
	public int executeUpdate(@NotNull SQLConfig<T, M, L> config, String sql) throws Exception {
		Statement stt;
		int count;
		if (config.isTDengine()) {
			Connection conn = getConnection(config);
            stt = conn.createStatement();
			//stt = config.isTDengine()
            //        ? conn.createStatement() // fix Presto: ResultSet: Exception: set type is TYPE_FORWARD_ONLY, Result set concurrency must be CONCUR_READ_ONLY
            //        : conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

            count = stt.executeUpdate(StringUtil.isEmpty(sql) ? config.gainSQL(false) : sql);
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
