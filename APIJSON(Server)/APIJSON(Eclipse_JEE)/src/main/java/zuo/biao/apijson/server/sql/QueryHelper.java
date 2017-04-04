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

/**helper for query MySQL database
 * @author Lemon
 */
public class QueryHelper {
	private static final String TAG = "QueryHelper";

	private static final String YOUR_MYSQL_URL = "jdbc:mysql://localhost:3306/";//TODO edit to an available one
	private static final String YOUR_MYSQL_SCHEMA = "sys";//TODO edit to an available one
	private static final String YOUR_MYSQL_ACCOUNT = "root";//TODO edit to an available one
	private static final String YOUR_MYSQL_PASSWORD = "apijson";//TODO edit to an available one

	private Map<String, Map<Integer, JSONObject>> cacheMap = new HashMap<String, Map<Integer, JSONObject>>();
	static {
		try {//调用Class.forName()方法加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private synchronized Connection getConnection() throws Exception {
		Log.i(TAG, "成功加载MySQL驱动！");
		return DriverManager.getConnection(YOUR_MYSQL_URL + YOUR_MYSQL_SCHEMA, YOUR_MYSQL_ACCOUNT, YOUR_MYSQL_PASSWORD);
	}

	//TODO key应该改成SQL
	private synchronized void saveCache(String sql, Map<Integer, JSONObject> map) {
		if (sql == null || map == null || map.isEmpty()) {
			Log.i(TAG, "saveList  sql == null || map == null || map.isEmpty() >> return;");
			return;
		}
		cacheMap.put(sql, map);
	}
	public synchronized void removeCache(String sql) {
		if (sql == null) {
			Log.i(TAG, "removeList  sql == null >> return;");
			return;
		}
		cacheMap.remove(sql);
	}

	private JSONObject getFromCache(String sql, int position) {
		Map<Integer, JSONObject> map = cacheMap.get(sql);
		return map == null ? null : map.get(position);
	}

	private Connection connection;
	private Statement statement;
	private DatabaseMetaData metaData;
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

	public JSONObject select(QueryConfig config) throws Exception {
		final String sql = QueryConfig.getSQL(config);
		if (StringUtil.isNotEmpty(sql, true) == false) {
			Log.e(TAG, "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		JSONObject result = null;

		Log.d(TAG, "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"
				+ "select  sql = " + sql
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
			result.put(JSONResponse.KEY_ID, config.getId());
			return result;

		case GET:
		case POST_GET:
			break;

		default://OPTIONS, TRACE等
			Log.e(TAG, "select  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
			return null;
		}


		final int position = config.getPosition();
		result = getFromCache(sql, position);
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

		saveCache(sql, resultMap);
		Log.i(TAG, ">>> select  saveCache('" + sql + "', resultMap);  resultMap.size() = " + resultMap.size());

		Log.d(TAG, "\n\n select  return resultMap.get(" + position + ");"  + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		return resultMap.get(position);
	}


	/**获取全部字段名列表
	 * @param config
	 * @return
	 * @throws SQLException 
	 */
	private String[] getColumnArray(QueryConfig config) throws SQLException {
		if (config == null) {
			return null;
		}
		String column = config.getColumn();
		if (StringUtil.isNotEmpty(column, true)) {
			return StringUtil.split(column);//column.contains(",") ? column.split(",") : new String[]{column};
		}
		List<String> list = new ArrayList<String>();
		String table = config.getTable();
		ResultSet rs = metaData.getColumns(YOUR_MYSQL_SCHEMA, null, table, "%");
		while (rs.next()) {
			Log.i(TAG, rs.getString(4));
			list.add(rs.getString(4));
		}
		rs.close();
		return list.toArray(new String[]{});
	}


	//	/**
	//	 * @param table
	//	 * @param schema
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	public int getCount(String table) throws Exception {
	//		if (connection == null || connection.isClosed()) {
	//			Log.i(TAG, "getCount  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
	//			connection = getConnection();
	//			statement = connection.createStatement(); //创建Statement对象
	//			metaData = connection.getMetaData();
	//		}
	//
	//		ResultSet rs = statement.executeQuery("SELECT Count(*) FROM " + table);//创建数据对象
	//
	//		int count = 0;
	//		if (rs.next()) {
	//			count = rs.getInt(1);
	//		}
	//		Log.i(TAG, "getCount  count = " + count) ;
	//
	//		rs.close();
	//
	//		return count;
	//	}

}
