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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.Log;
import zuo.biao.apijson.server.AbstractSQLExecutor;
import zuo.biao.apijson.server.SQLConfig;


/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public class DemoSQLExecutor extends AbstractSQLExecutor {
	private static final String TAG = "DemoSQLExecutor";


	static {
		try {//调用Class.forName()方法加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			Log.d(TAG, "成功加载MySQL驱动！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}



	private SQLConfig config = null;
	@Override
	public JSONObject execute(SQLConfig config) throws Exception {
		this.config = config;
		return super.execute(config);
	}


	@Override
	public ResultSet executeQuery(@NotNull String sql) throws Exception {
		return getStatement().executeQuery(sql);
	}

	@Override
	public int executeUpdate(@NotNull String sql) throws Exception {
		return getStatement().executeUpdate(sql);
	}


	private Connection connection = null;
	private Statement statement = null;
	/**
	 * @return
	 * @throws Exception
	 */
	private Statement getStatement() throws Exception {
		if (connection == null || connection.isClosed()) {
			Log.i(TAG, "select  connection " + (connection == null ? " = null" : ("isClosed = " + connection.isClosed()))) ;

			connection = DriverManager.getConnection(config.getDBUri() + "?useUnicode=true&characterEncoding=UTF-8&user="
					+ config.getDBAccount() + "&password=" + config.getDBPassword());

			statement = connection.createStatement(); //创建Statement对象
		}
		return statement;
	}


	/**关闭连接，释放资源
	 */
	@Override
	public void close() {
		super.close();
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
		config = null;
		statement = null;
		connection = null;
	}



}
