package zuo.biao.apijson.server.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;

public class SelectTable2 {
	private static final String TAG = "SelectTable2: ";


	public static void main(String[] args){
//		System.out.println(TAG + JSON.toJSONString(select("stu")));
	}

	public static Connection getConnection() throws Exception {
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(TAG + "成功加载MySQL驱动！");
		String url="jdbc:mysql://localhost:3306/sys";    //JDBC的URL    
		return DriverManager.getConnection(url,    "root", "199531tommy");
	}

	
	public static JSONObject select(String table) {
		return select(new QueryConfig(table));
	}
	public static JSONObject select(QueryConfig config) {
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			System.out.println(TAG + "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		try{
			Connection conn = getConnection();
			Statement stmt = conn.createStatement(); //创建Statement对象
			System.out.println(TAG + "成功连接到数据库！");

			List<String> list = getColumnList(config.getTable(), conn.getMetaData());
			if (list == null || list.isEmpty()) {
				return null;
			}

			String sql = "select * from " + config.getTable() + config.getWhereString() + config.getLimitString();    //要执行的SQL
			System.out.println(TAG + "select  sql = " + sql);
			
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象

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
			stmt.close();
			conn.close();

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
	public static List<String> getColumnList(String table, DatabaseMetaData meta) {
		List<String> list = new ArrayList<String>();
		ResultSet rs;
		try {
			rs = meta.getColumns("sys", null, table, "%");
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