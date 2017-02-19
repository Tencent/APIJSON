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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;

/**helper for query MySQL database with cache
 * @author Lemon
 */
public class QueryHelper2 {
	private static final String TAG = "QueryHelper2: ";

	private static final String YOUR_MYSQL_URL = "jdbc:mysql://localhost:3306/";//TODO edit to an available one
	private static final String YOUR_MYSQL_SCHEMA = "sys";//TODO edit to an available one
	private static final String YOUR_MYSQL_ACCOUNT = "root";//TODO edit to an available one
	private static final String YOUR_MYSQL_PASSWORD = "apijson";//TODO edit to an available one

	private Map<String, List<JSONObject>> cacheMap;
	private QueryHelper2() {

	}

	private static QueryHelper2 instance;
	public static synchronized QueryHelper2 getInstance() {
		if (instance == null) {
			instance = new QueryHelper2();
		}
		return instance;
	}

	public Connection getConnection() throws Exception {
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(TAG + "成功加载MySQL驱动！");
		return DriverManager.getConnection(YOUR_MYSQL_URL + YOUR_MYSQL_SCHEMA, YOUR_MYSQL_ACCOUNT, YOUR_MYSQL_PASSWORD);
	}

	private void saveCache(String key, List<JSONObject> list) {
		if (key == null) {
			System.out.println("saveList  key == null >> return;");
			return;
		}
		cacheMap.put(key, list);
	}
	private void removeCache(String key) {
		if (key == null) {
			System.out.println("removeList  key == null >> return;");
			return;
		}
		cacheMap.remove(key);
	}

	private JSONObject getFromCache(String key, int position) {
		List<JSONObject> list = cacheMap.get(key);
		return list == null || position < 0 || position >= list.size() ? null : list.get(position);
	}

	private static Connection connection;
	private static Statement statement;
	private static DatabaseMetaData metaData;
	public void close() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		metaData = null;
		statement = null;
		cacheMap = null;
	}

	public JSONObject select(QueryConfig config) {
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			System.out.println(TAG + "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		final String sql = config.getSQL();
		final int position = config.getPosition();

		if (cacheMap == null) {
			cacheMap = new HashMap<String, List<JSONObject>>();
		}
		JSONObject object = getFromCache(sql, position);
		if (object != null) {
			return object;
		}

		try{
			if (connection == null || connection.isClosed()) {
				System.out.println(TAG + "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
				connection = getConnection();
				statement = connection.createStatement(); //创建Statement对象
				metaData = connection.getMetaData();
			}
			List<String> list = getColumnList(config.getTable(), metaData);
			if (list == null || list.isEmpty()) {
				return null;
			}


			System.out.println(TAG + "select  sql = " + sql);

			ResultSet rs = statement.executeQuery(sql);//创建数据对象

			List<JSONObject> resultList = new ArrayList<JSONObject>();
			while (rs.next()){
				object = new JSONObject(true);
				try {
					for (int i = 0; i < list.size(); i++) {
						object.put(list.get(i), rs.getObject(rs.findColumn(list.get(i))));
					}
				} catch (Exception e) {
					System.out.println(TAG + "select while (rs.next()){ ... >>  try { object.put(list.get(i), ..." +
							" >> } catch (Exception e) {\n" + e.getMessage());
					e.printStackTrace();
					object = null;
				}
				resultList.add(object);
			}
			rs.close();

			//从缓存存取，避免 too many connections崩溃
			if (position < config.getCount() - 1) {
				System.out.println("select  position < config.getLimit() - 1 >> saveCache(sql, resultList);");
				saveCache(sql, resultList);
			} else {
				System.out.println("select  position >= config.getLimit() - 1 >> removeCache(sql); return object;");
				removeCache(sql);
				return object;
			}

			System.out.println("select  return position < 0 || position >= resultList.size() ? null : resultList.get(position); ");
			return position < 0 || position >= resultList.size() ? null : resultList.get(position);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}



	/**获取全部字段名列表
	 * @param table
	 * @param meta
	 * @return
	 */
	public List<String> getColumnList(String table, DatabaseMetaData meta) {
		List<String> list = new ArrayList<String>();
		ResultSet rs;
		try {
			rs = meta.getColumns(YOUR_MYSQL_SCHEMA, null, table, "%");
			while (rs.next()) {
				System.out.println(TAG + rs.getString(4));
				list.add(rs.getString(4));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(TAG + "getColumnList   try { DatabaseMetaData meta = conn.getMetaData(); ... >>  " +
					"} catch (Exception e) {\n" + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
}