package zuo.biao.apijson.server.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.QueryConfig;

public class SelectTable3 {
	private static final String TAG = "SelectTable3: ";

	private Map<String, List<JSONObject>> cacheMap;
	private SelectTable3() {
		cacheMap = new HashMap<String, List<JSONObject>>();
	}

	private static SelectTable3 instance;
	public static synchronized SelectTable3 getInstance() {
		if (instance == null) {
			instance = new SelectTable3();
		}
		return instance;
	}

	public Connection getConnection() throws Exception {
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println(TAG + "成功加载MySQL驱动！");
		String url="jdbc:mysql://localhost:3306/sys";    //JDBC的URL    
		return DriverManager.getConnection(url,    "root", "199531tommy");
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

	
	public JSONObject select(QueryConfig config) {
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			System.out.println(TAG + "select  config==null||StringUtil.isNotEmpty(config.getTable(), true)==false>>return null;");
			return null;
		}
		final String sql = config.getSQL();
		final int position = config.getPosition();

		JSONObject object = getFromCache(sql, position);
		if (object != null) {
			return object;
		}

		try{
			Connection conn = getConnection();
			Statement stmt = conn.createStatement(); //创建Statement对象
			System.out.println(TAG + "成功连接到数据库！");

			List<String> list = getColumnList(config.getTable(), conn.getMetaData());
			if (list == null || list.isEmpty()) {
				return null;
			}


			System.out.println(TAG + "select  sql = " + sql);

			ResultSet rs = stmt.executeQuery(sql);//创建数据对象

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
			stmt.close();
			conn.close();

			//从缓存存取，避免 too many connections崩溃
			if (position < config.getLimit() - 1) {
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