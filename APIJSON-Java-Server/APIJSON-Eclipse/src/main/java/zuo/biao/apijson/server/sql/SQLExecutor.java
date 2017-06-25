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

package zuo.biao.apijson.server.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.Pair;
import zuo.biao.apijson.server.Parser;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public class SQLExecutor {
	private static final String TAG = "SQLExecutor";


	//访问一次后丢失，可能因为static导致内存共享，别的地方改变了内部对象的值
	//	private static final Map<String, Map<Integer, JSONObject>> staticCacheMap;
	//	static {
	//		staticCacheMap = new HashMap<String, Map<Integer, JSONObject>>();
	//	}

	/**
	 * 缓存map
	 */
	private Map<String, Map<Integer, JSONObject>> cacheMap = new HashMap<String, Map<Integer, JSONObject>>();
	static {
		try {//调用Class.forName()方法加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**获取连接
	 * @return
	 * @throws Exception
	 */
	private synchronized Connection getConnection() throws Exception {
		Log.i(TAG, "成功加载MySQL驱动！");
		return DriverManager.getConnection(SQLConfig.MYSQL_URI + "?useUnicode=true&characterEncoding=UTF-8&user="
		+ SQLConfig.MYSQL_ACCOUNT + "&password=" + SQLConfig.MYSQL_PASSWORD);
	}

	/**保存缓存
	 * @param sql
	 * @param map
	 * @param isStatic
	 */
	private synchronized void saveCache(String sql, Map<Integer, JSONObject> map, boolean isStatic) {
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
	public JSONObject getFromCache(String sql, int position, boolean isStatic) {
		Map<Integer, JSONObject> map = /** isStatic ? staticCacheMap.get(sql) : */ cacheMap.get(sql);
		//只要map不为null，则如果 map.get(position) == null，则返回 {} ，避免再次SQL查询
		if (map == null) {
			return null;
		} 
		JSONObject result = map.get(position);
		return result != null ? result : new JSONObject();
	}

	private Connection connection;
	private Statement statement;
	private DatabaseMetaData metaData;
	/**关闭连接，释放资源
	 */
	public void close() {
		cacheMap.clear();
		try {
			if (statement != null && statement.isClosed() == false) {
				statement.close();
			}
			if (connection != null && connection.isClosed() == false) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		metaData = null;
		statement = null;
		cacheMap = null;
	}

	/**执行SQL
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public JSONObject execute(SQLConfig config) throws Exception {

		final String sql = SQLConfig.getSQL(config);
		if (StringUtil.isNotEmpty(sql, true) == false) {
			Log.e(TAG, "select  config==null||StringUtil.isNotEmpty(config.getSQLTable(), true)==false>>return null;");
			return null;
		}
		JSONObject result = null;

		long startTime = System.currentTimeMillis();
		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
				+ "\n select  startTime = " + startTime
				+ "\n sql = " + sql
				+ "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

		if (connection == null || connection.isClosed()) {
			Log.i(TAG, "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
			connection = getConnection();
			statement = connection.createStatement(); //创建Statement对象
			metaData = connection.getMetaData();
		}
		Log.i(TAG, "成功连接到数据库！");
		ResultSet rs = null;
		switch (config.getMethod()) {
		case HEAD:
		case POST_HEAD:
			rs = statement.executeQuery(sql);

			result = rs.next() ? Parser.newSuccessResult()
					: Parser.newErrorResult(new SQLException("数据库错误, rs.next() 失败！"));
			result.put(JSONResponse.KEY_COUNT, rs.getLong(1));

			rs.close();
			return result;

		case POST:
		case PUT:
		case DELETE:
			long updateCount = statement.executeUpdate(sql);

			result = Parser.newResult(updateCount > 0 ? 200 : 404
					, updateCount > 0 ? "success" : "可能对象不存在！");
			result.put(JSONResponse.KEY_ID, config.getId());//id一定有，一定会返回，不用抛异常来阻止关联写时前面错误导致后面无条件执行！
			return result;

		case GET:
		case POST_GET:
			break;

		default://OPTIONS, TRACE等
			Log.e(TAG, "select  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
			return null;
		}


		final int position = config.getPosition();
		result = getFromCache(sql, position, config.isCacheStatic());
		Log.i(TAG, ">>> select  result = getFromCache('" + sql + "', " + position + ") = " + result);
		if (result != null) {
			Log.d(TAG, "\n\n select  result != null >> return result;"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
			return result;
		}

		String[] columnArray = getColumnArray(config);
		if (columnArray == null || columnArray.length <= 0) {
			return null;
		}

		rs = statement.executeQuery(sql);

		//		final boolean cache = config.getCount() != 1;
		Map<Integer, JSONObject> resultMap = new HashMap<Integer, JSONObject>();
		//		Log.d(TAG, "select  cache = " + cache + "; resultMap" + (resultMap == null ? "=" : "!=") + "null");

		int index = -1;
		while (rs.next()){
			index ++;
			Log.d(TAG, "\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n select while (rs.next()){  index = " + index + "\n\n");

			result = new JSONObject(true);
			Object value;
			Object json;
			for (int i = 0; i < columnArray.length; i++) {
				if (columnArray[i] == null || columnArray[i].isEmpty() || columnArray[i].startsWith("_")) {
					Log.i(TAG, "select while (rs.next()){ ..."
							+ " >>  columnArray[i] == " + columnArray[i]
									+ " >> continue;");
					continue;
				}//允许 key:_alias, 但不允许_key, _key:alias
				columnArray[i] = Pair.parseEntry(columnArray[i]).getValue();
				try {
					value = rs.getObject(rs.findColumn(columnArray[i]));
				} catch (Exception e) {
					value = null;
					Log.i(TAG, "select while (rs.next()){ ..."
							+ " >>  try { value = rs.getObject(rs.findColumn(columnArray[i])); ..."
							+ " >> } catch (Exception e) {");
					e.printStackTrace();
				}
				//				Log.i(TAG, "select  while (rs.next()) { >> for (int i = 0; i < columnArray.length; i++) {"
				//						+ "\n  >>> columnArray[i]) = " + columnArray[i] + "; value = " + value);
				if (value != null && value instanceof String) {
					try {
						json = JSON.parse((String) value);
						if (json != null && StringUtil.isNotEmpty(json, true)) {
							value = json;
						}
					} catch (Exception e) {
						//太长 Log.i(TAG, "select  while (rs.next()){  >> i = "
						//  + i + "  try { json = JSON.parse((String) value);"
						//	+ ">> } catch (Exception e) {\n" + e.getMessage());
					}
				}
				result.put(columnArray[i], value);
			}

			resultMap.put(index, result);
			Log.d(TAG, "\n select  while (rs.next()) { resultMap.put( " + index + ", result); "
					+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n");
		}

		rs.close();

		saveCache(sql, resultMap, config.isCacheStatic());
		Log.i(TAG, ">>> select  saveCache('" + sql + "', resultMap);  resultMap.size() = " + resultMap.size());

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "\n\n select  endTime = " + endTime + "; duration = " + (endTime - startTime)
				+ "\n return resultMap.get(" + position + ");"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		return resultMap.get(position);
	}


	/**获取要查询的字段名数组
	 * @param config
	 * @return
	 * @throws SQLException 
	 */
	private String[] getColumnArray(SQLConfig config) throws SQLException {
		if (config == null) {
			return null;
		}
		String column = config.getColumn();
		if (StringUtil.isNotEmpty(column, true)) {
			return StringUtil.split(column);//column.contains(",") ? column.split(",") : new String[]{column};
		}
		
		List<String> list = new ArrayList<String>();
		String table = config.getSQLTable();
		ResultSet rs = metaData.getColumns(config.getSchema(), null, table, "%");
		while (rs.next()) {
			Log.i(TAG, rs.getString(4));
			list.add(rs.getString(4));
		}
		rs.close();
		
		return list.toArray(new String[]{});
	}

}
