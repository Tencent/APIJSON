package zuo.biao.apijson.server.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;

public class SelectTable2 {
	private static final String TAG = "SelectTable2: ";

	private static final String YOUR_MYSQL_URL = "jdbc:mysql://localhost:3306/";//TODO edit to an available one
	private static final String YOUR_MYSQL_CATALOG = "sys";//TODO edit to an available one
	private static final String YOUR_MYSQL_ACCOUNT = "root";//TODO edit to an available one
	private static final String YOUR_MYSQL_PASSWORD = "apijson";//TODO edit to an available one

	private SelectTable2() {

	}

	private static SelectTable2 instance;
	public static synchronized SelectTable2 getInstance() {
		if (instance == null) {
			instance = new SelectTable2();
		}
		return instance;
	}

	public Connection getConnection() throws Exception {
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(TAG + "成功加载MySQL驱动！");
		return DriverManager.getConnection(YOUR_MYSQL_URL + YOUR_MYSQL_CATALOG, YOUR_MYSQL_ACCOUNT, YOUR_MYSQL_PASSWORD);
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
	}

	public JSONObject select(QueryConfig config) {
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			System.out.println(TAG + "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		final String sql = config.getSQL();

		try{


			System.out.println(TAG + "成功连接到数据库！");

			if (connection == null || connection.isClosed()) {
				System.out.println(TAG + "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;
				connection = getConnection();
				statement = connection.createStatement(); //创建Statement对象
				metaData = connection.getMetaData();
			}

			List<String> list = getColumnList(config.getTable());
			if (list == null || list.isEmpty()) {
				return null;
			}

			System.out.println(TAG + "select  sql = " + sql);

			ResultSet rs = statement.executeQuery(sql);//创建数据对象

			JSONObject object  = null;//new JSONObject();//null;
			int position = -1;
			while (rs.next()){
				position ++;
				if (position != config.getPosition()) {
					continue;
				}
				object = new JSONObject(true);
				try {
					for (int i = 0; i < list.size(); i++) {
						object.put(list.get(i), rs.getObject(rs.findColumn(list.get(i))));
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
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	/**获取全部字段名列表
	 * @param table
	 * @return
	 */
	public List<String> getColumnList(String table) {
		List<String> list = new ArrayList<String>();
		ResultSet rs;
		try {
			rs = metaData.getColumns("sys", null, table, "%");
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