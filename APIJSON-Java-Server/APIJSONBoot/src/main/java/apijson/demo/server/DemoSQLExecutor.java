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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
			Log.d(TAG, "尝试加载 MySQL 8 驱动 <<<<<<<<<<<<<<<<<<<<< ");
			Class.forName("com.mysql.cj.jdbc.Driver");
			Log.d(TAG, "成功加载 MySQL 8 驱动！>>>>>>>>>>>>>>>>>>>>>");
		}
		catch (ClassNotFoundException e) {
			Log.e(TAG, "加载 MySQL 8 驱动失败，请检查 pom.xml 中 mysql-connector-java 版本是否存在以及可用 ！！！");
			e.printStackTrace();

			try { //加载驱动程序
				Log.d(TAG, "尝试加载 MySQL 7 及以下版本的 驱动 <<<<<<<<<<<<<<<<<<<<< ");
				Class.forName("com.mysql.jdbc.Driver");
				Log.d(TAG, "成功加载 MySQL 7 及以下版本的 驱动！>>>>>>>>>>>>>>>>>>>>> ");
			}
			catch (ClassNotFoundException e2) {
				Log.e(TAG, "加载 MySQL 7 及以下版本的 驱动失败，请检查 pom.xml 中 mysql-connector-java 版本是否存在以及可用 ！！！");
				e2.printStackTrace();
			}
		}

		try { //加载驱动程序
			Log.d(TAG, "尝试加载 PostgresSQL 驱动 <<<<<<<<<<<<<<<<<<<<< ");
			Class.forName("org.postgresql.Driver");
			Log.d(TAG, "成功加载 PostgresSQL 驱动！>>>>>>>>>>>>>>>>>>>>> ");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, "加载 PostgresSQL 驱动失败，请检查 libs 目录中 postgresql.jar 版本是否存在以及可用 ！！！");
		}
	}


	@Override
	public PreparedStatement setArgument(@NotNull SQLConfig config, @NotNull PreparedStatement statement, int index, Object value) throws SQLException {
		if (SQLConfig.DATABASE_POSTGRESQL.equals(config.getDatabase()) && JSON.isBooleanOrNumberOrString(value) == false) {
			PGobject o = new PGobject();
			o.setType("jsonb");
			o.setValue(value == null ? null : value.toString());
			statement.setObject(index + 1, o); //PostgreSQL 除了基本类型，其它的必须通过 PGobject 设置进去，否则 jsonb = varchar 等报错
			return statement;
		}
		
		return super.setArgument(config, statement, index, value);
	}


	@Override
	protected Object getValue(SQLConfig config, ResultSet rs, ResultSetMetaData rsmd, int tablePosition,
			JSONObject table, int columnIndex, Map<String, JSONObject> childMap) throws Exception {
		
		Object value = super.getValue(config, rs, rsmd, tablePosition, table, columnIndex, childMap);

		return value instanceof PGobject ? JSON.parse(((PGobject) value).getValue()) : value;
	}


}
