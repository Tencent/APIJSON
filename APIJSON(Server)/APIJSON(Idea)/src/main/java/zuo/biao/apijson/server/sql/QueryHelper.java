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
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.Table;
import zuo.biao.apijson.server.QueryConfig;
import zuo.biao.apijson.server.RequestParser;

/**helper for query MySQL database
 * @author Lemon
 */
public class QueryHelper {
	private static final String TAG = "QueryHelper: ";

	private static final String YOUR_MYSQL_URL = "jdbc:mysql://localhost:3306/";//TODO edit to an available one
	private static final String YOUR_MYSQL_SCHEMA = "sys";//TODO edit to an available one
	private static final String YOUR_MYSQL_ACCOUNT = "root";//TODO edit to an available one
	private static final String YOUR_MYSQL_PASSWORD = "apijson";//TODO edit to an available one

	private QueryHelper() {

	}

	private static QueryHelper instance;
	public static synchronized QueryHelper getInstance() {
		if (instance == null) {
			instance = new QueryHelper();
		}
		return instance;
	}

	public Connection getConnection() throws Exception {
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(TAG + "成功加载MySQL驱动！");
		return DriverManager.getConnection(YOUR_MYSQL_URL + YOUR_MYSQL_SCHEMA, YOUR_MYSQL_ACCOUNT, YOUR_MYSQL_PASSWORD);
	}


	private static Connection connection;
	private static Statement statement;
	private static DatabaseMetaData metaData;
	public void close() {
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
	}

	public JSONObject select(QueryConfig config) throws Exception {
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			System.out.println(TAG + "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		final String sql = config.getSQL();

		System.out.println(TAG + "成功连接到数据库！");

		if (connection == null || connection.isClosed()) {
			System.out.println(TAG + "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
			connection = getConnection();
			statement = connection.createStatement(); //创建Statement对象
			metaData = connection.getMetaData();
		}

		String[] columnArray = getColumnArray(config);
		if (columnArray == null || columnArray.length <= 0) {
			return null;
		}

		System.out.println(TAG + "select  sql = " + sql);

		JSONObject object  = null;//new JSONObject(true);
		ResultSet rs = null;
		switch (config.getMethod()) {
		case POST:
		case PUT:
		case DELETE:
			int updateCount = statement.executeUpdate(sql);//创建数据对象

			JSONObject result = RequestParser.newResult(updateCount > 0 ? 200 : 404
					, updateCount > 0 ? "success" : "可能对象不存在！");
			result.put(Table.ID, config.getId());
			return result;
		default:
			rs = statement.executeQuery(sql);//创建数据对象
			break;
		}


		int position = -1;
		while (rs.next()){
			position ++;
			if (position != config.getPosition()) {
				continue;
			}
			object = new JSONObject(true);
			try {
				for (int i = 0; i < columnArray.length; i++) {
					object.put(columnArray[i], rs.getObject(rs.findColumn(columnArray[i])));
				}
			} catch (Exception e) {
				System.out.println(TAG + "select while (rs.next()){ ... >>  try { object.put(list.get(i), ..." +
						" >> } catch (Exception e) {\n" + e.getMessage());
				e.printStackTrace();
			}
			break;
		}

		rs.close();

		return object;
	}


	/**获取全部字段名列表
	 * @param table
	 * @return
	 * @throws SQLException 
	 */
	public String[] getColumnArray(QueryConfig config) throws SQLException {
		if (config == null) {
			return null;
		}
		String columns = config.getColumns();
		if (StringUtil.isNotEmpty(columns, true)) {
			return StringUtil.split(columns);//columns.contains(",") ? columns.split(",") : new String[]{columns};
		}
		List<String> list = new ArrayList<String>();
		String table = config.getTable();
		ResultSet rs;
		rs = metaData.getColumns(YOUR_MYSQL_SCHEMA, null, table, "%");
		while (rs.next()) {
			System.out.println(TAG + rs.getString(4));
			list.add(rs.getString(4));
		}
		rs.close();
		return list.toArray(new String[]{});
	}

}