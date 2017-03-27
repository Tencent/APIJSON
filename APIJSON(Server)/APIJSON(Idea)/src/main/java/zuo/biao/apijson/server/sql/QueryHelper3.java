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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.Column;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.Table;
import zuo.biao.apijson.server.QueryConfig;
import zuo.biao.apijson.server.RequestParser;

/**helper for query MySQL database
 * @author Lemon
 */
public class QueryHelper3 {
	private static final String TAG = "QueryHelper: ";

	private static final String YOUR_MYSQL_URL = "jdbc:mysql://localhost:3306/";//TODO edit to an available one
	private static final String YOUR_MYSQL_SCHEMA = "sys";//TODO edit to an available one
	private static final String YOUR_MYSQL_ACCOUNT = "root";//TODO edit to an available one
	private static final String YOUR_MYSQL_PASSWORD = "apijson";//TODO edit to an available one

	private QueryHelper3() {

	}

	private static QueryHelper3 instance;
	public static synchronized QueryHelper3 getInstance() {
		if (instance == null) {
			instance = new QueryHelper3();
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

		List<Column> columnList = getColumnList(config);
		if (columnList == null || columnList.isEmpty()) {
			return null;
		}

		System.out.println(TAG + "select  sql = " + sql);

		JSONObject object  = null;//new JSONObject(true);
		ResultSet rs = null;
		switch (config.getMethod()) {
		case GET:
		case POST_GET:
			rs = statement.executeQuery(sql);
			break;
		case POST:
		case PUT:
		case DELETE:
			int updateCount = statement.executeUpdate(sql);

			JSONObject result = RequestParser.newResult(updateCount > 0 ? 200 : 404
					, updateCount > 0 ? "success" : "可能对象不存在！");
			result.put(Table.ID, config.getId());
			return result;
		default://HEAD, OPTIONS, TRACE等
			System.out.println(TAG + "select  sql = " + sql + " ; method = " + config.getMethod() + " >> return null;");
			return null;
		}


		int position = -1;
		while (rs.next()) {
			position ++;
			if (position != config.getPosition()) {
				continue;
			}
			object = new JSONObject(true);
			try {
				Object value;
				JSONArray array;
				for (Column column : columnList) {
					if (column == null || StringUtil.isNotEmpty(column.getLabel(), true) == false) {
						continue;
					}
					value = rs.getObject(rs.findColumn(column.getLabel()));
					if (column.getType() == Types.ARRAY) {//value instanceof String) {
						array = JSON.parseArray((String)value);
						if (array != null && array.isEmpty() == false) {
							value = array;
						}
					}
					object.put(column.getLabel(), value);
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
	 * @param config
	 * @return
	 * @throws SQLException 
	 */
	public List<Column> getColumnList(QueryConfig config) throws SQLException {
		if (config == null) {
			return null;
		}
		List<Column> list = new ArrayList<Column>();

		String columnsString = config.getColumns();
		if (StringUtil.isNotEmpty(columnsString, true)) {
			String[] columns = StringUtil.split(columnsString);//columns.contains(",") ? columns.split(",") : new String[]{columns};
			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					list.add(new Column(columns[i]));
				}
			}
			return list;//只要request中设置了columnsString就不查询
		}

		String table = config.getTable();
		ResultSet rs = metaData.getColumns(YOUR_MYSQL_SCHEMA, null, table, "%");
		ResultSetMetaData data = rs.getMetaData();
		
		Column column;
		while (rs.next()) {
			for (int i = 1; i<= data.getColumnCount(); i++) {
				column = new Column();
				column.setCount(data.getColumnCount());
				column.setName(data.getColumnName(i));
				column.setValue(rs.getString(i));
				column.setType(data.getColumnType(i));
				column.setTypeName(data.getColumnTypeName(i));
				column.setCatalogName(data.getCatalogName(i));
				column.setClassName(data.getColumnClassName(i));
				column.setDisplaySize(data.getColumnDisplaySize(i));
				column.setLabel(data.getColumnLabel(i));
				column.setSchemaName(data.getSchemaName(i));
				column.setPrecision(data.getPrecision(i));
				column.setScale(data.getScale(i));
				column.setTable(data.getTableName(i));
				column.setAutoInctement(data.isAutoIncrement(i));
				column.setIsCurrency(data.isCurrency(i));
				column.setNullable(data.isNullable(i));
				column.setReadOnly(data.isReadOnly(i));
				column.setSearchable(data.isSearchable(i));
				
				System.out.println(TAG + "getColumnList column = \n" + JSON.toJSONString(column));
				list.add(column);
			}
		}
		rs.close();
		return list;
	}

	/**
	 * @param table
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table) throws Exception {
		if (connection == null || connection.isClosed()) {
			System.out.println(TAG + "getCount  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
			connection = getConnection();
			statement = connection.createStatement(); //创建Statement对象
			metaData = connection.getMetaData();
		}

		ResultSet rs = statement.executeQuery("SELECT Count(*) FROM " + table);//创建数据对象

		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		System.out.println(TAG + "getCount  count = " + count) ;

		rs.close();

		return count;
	}

}