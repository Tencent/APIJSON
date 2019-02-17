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

package apijson.demo.server;

import java.io.BufferedReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.postgresql.util.PGobject;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.server.AbstractSQLExecutor;
import zuo.biao.apijson.server.SQLConfig;


/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public class DemoSQLExecutor extends AbstractSQLExecutor {
	private static final String TAG = "DemoSQLExecutor";


	static {
		try { //加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			Log.d(TAG, "成功加载 MySQL 驱动！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { //加载驱动程序
			Class.forName("org.postgresql.Driver");
			Log.d(TAG, "成功加载 PostgresSQL 驱动！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}




	@Override
	public ResultSet executeQuery(@NotNull SQLConfig config) throws Exception {
		return getStatement(config).executeQuery();
	}

	@Override
	public int executeUpdate(@NotNull SQLConfig config) throws Exception {
		return getStatement(config).executeUpdate();
	}


	//TODO String 改为 enum Database 解决大小写不一致(MySQL, mysql等)导致创建多余的 Connection
	private Map<String, Connection> connectionMap = new HashMap<>();
	/**
	 * @param config 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	private PreparedStatement getStatement(@NotNull SQLConfig config) throws Exception {
		Connection connection = connectionMap.get(config.getDatabase());
		if (connection == null || connection.isClosed()) {
			Log.i(TAG, "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;

			if (DemoSQLConfig.DATABASE_POSTGRESQL.equals(config.getDatabase())) { //PostgreSQL 不允许 cross-database
				connection = DriverManager.getConnection(config.getDBUri(), config.getDBAccount(), config.getDBPassword());
			}
			else {
				connection = DriverManager.getConnection(config.getDBUri() + "?useUnicode=true&characterEncoding=UTF-8&user="
						+ config.getDBAccount() + "&password=" + config.getDBPassword());
			}
			connectionMap.put(config.getDatabase(), connection);
		}

		PreparedStatement statement = connection.prepareStatement(config.getSQL(config.isPrepared())); //创建Statement对象
		List<Object> valueList = config.isPrepared() ? config.getPreparedValueList() : null;

		if (valueList != null && valueList.isEmpty() == false) {

			Object v;
			for (int i = 0; i < valueList.size(); i++) {
				v = valueList.get(i); //JSON.isBooleanOrNumberOrString(v) 解决 PostgreSQL: Can't infer the SQL type to use for an instance of com.alibaba.fastjson.JSONArray
				
				if (DemoSQLConfig.DATABASE_POSTGRESQL.equals(config.getDatabase())) {
					if (JSON.isBooleanOrNumberOrString(v)) {
						statement.setObject(i + 1, v); //PostgreSQL JDBC 不支持隐式类型转换 tinyint = varchar 报错
					}
					else {
						PGobject o = new PGobject();
						o.setType("jsonb");
						o.setValue(v == null ? null : v.toString());
						statement.setObject(i + 1, o); //PostgreSQL 除了基本类型，其它的必须通过 PGobject 设置进去，否则 jsonb = varchar 等报错
					}
				}
				else {
					statement.setString(i + 1, v == null ? null : v.toString()); //MySQL setObject 不支持 JSON 类型
				}
			}
		}
		// statement.close();

		return statement;
	}

	@Override
	protected Object getValue(SQLConfig config, ResultSet rs, ResultSetMetaData rsmd, int tablePosition,
			JSONObject table, int columnIndex, Map<String, JSONObject> childMap) throws Exception {
		Object value = super.getValue(config, rs, rsmd, tablePosition, table, columnIndex, childMap);
		
		if (value instanceof Blob) { //FIXME 存的是 abcde，取出来直接就是 [97, 98, 99, 100, 101] 这种 byte[] 类型，没有经过以下处理，但最终序列化后又变成了字符串 YWJjZGU=
			value = new String(((Blob) value).getBytes(1, (int) ((Blob) value).length()), "UTF-8");
		}
		else if (value instanceof Clob) {
			
			StringBuffer sb = new StringBuffer(); 
			BufferedReader br = new BufferedReader(((Clob) value).getCharacterStream()); 
			String s = br.readLine();
			while (s != null) {
				sb.append(s); 
				s = br.readLine(); 
			}
			value = sb.toString();
		}
		else if (value instanceof PGobject) {
			value = JSON.parse(((PGobject) value).getValue());
		}

		return value;
	}

	/**关闭连接，释放资源
	 */
	@Override
	public void close() {
		super.close();

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
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		connectionMap.clear();
		connectionMap = null;
	}

}
