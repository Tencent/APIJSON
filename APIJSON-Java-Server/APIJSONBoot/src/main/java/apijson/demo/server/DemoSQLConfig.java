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

import static zuo.biao.apijson.JSONObject.KEY_ID;
import static zuo.biao.apijson.JSONObject.KEY_USER_ID;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.server.AbstractSQLConfig;
import zuo.biao.apijson.server.Join;
import zuo.biao.apijson.server.SQLConfig;


/**SQL配置
 * TiDB 用法和 MySQL 一致
 * @author Lemon
 */
public class DemoSQLConfig extends AbstractSQLConfig {


	public static final Callback SIMPLE_CALLBACK;


	static {
		DEFAULT_DATABASE = DATABASE_MYSQL;  //TODO 默认数据库类型，改成你自己的
		DEFAULT_SCHEMA = "sys";  //TODO 默认模式名，改成你自己的

		//  由 DemoVerifier.init 方法读取数据库 Access 表来替代手动输入配置
		//		//表名映射，隐藏真实表名，对安全要求很高的表可以这么做
		//		TABLE_KEY_MAP.put(User.class.getSimpleName(), "apijson_user");
		//		TABLE_KEY_MAP.put(Privacy.class.getSimpleName(), "apijson_privacy");

		//主键名映射
		SIMPLE_CALLBACK = new SimpleCallback() {

			@Override
			public AbstractSQLConfig getSQLConfig(RequestMethod method, String database, String schema, String table) {
				return new DemoSQLConfig(method, table);
			}

			//取消注释来实现自定义各个表的主键名
			//			@Override
			//			public String getIdKey(String database, String schema, String table) {
			//				return StringUtil.firstCase(table + "Id");  // userId, comemntId ...
			//				//		return StringUtil.toLowerCase(t) + "_id";  // user_id, comemnt_id ...
			//				//		return StringUtil.toUpperCase(t) + "_ID";  // USER_ID, COMMENT_ID ...
			//			}

			@Override
			public String getUserIdKey(String database, String schema, String table) {
				return Controller.USER_.equals(table) || Controller.PRIVACY_.equals(table) ? KEY_ID : KEY_USER_ID; // id / userId
			}

		};
	}

	@Override
	public String getDBVersion() {
		if (isMySQL()) {
			return "5.7.22"; //"8.0.11"; //TODO 改成你自己的 MySQL 或 PostgreSQL 数据库版本号 //MYSQL 8 和 7 使用的 JDBC 配置不一样
		}
		if (isPostgreSQL()) {
			return "9.6.15"; //TODO 改成你自己的
		}
		if (isSQLServer()) {
			return "2016"; //TODO 改成你自己的
		}
		if (isOracle()) {
			return "18c"; //TODO 改成你自己的
		}
		return null;
	}
	@Override
	public String getDBUri() {
		if (isMySQL()) {
			return "jdbc:mysql://localhost:3306"; //TODO 改成你自己的，TiDB 可以当成 MySQL 使用，默认端口为 4000
		}
		if (isPostgreSQL()) {
			return "jdbc:postgresql://localhost:5432/postgres"; //TODO 改成你自己的
		}
		if (isSQLServer()) {
			return "jdbc:jtds:sqlserver://localhost:1433/pubs;instance=SQLEXPRESS"; //TODO 改成你自己的
		}
		if (isOracle()) {
			return "jdbc:oracle:thin:@localhost:1521:orcl"; //TODO 改成你自己的
		}
		return null;
	}
	@Override
	public String getDBAccount() {
		if (isMySQL()) {
			return "root";  //TODO 改成你自己的
		}
		if (isPostgreSQL()) {
			return "postgres";  //TODO 改成你自己的
		}
		if (isSQLServer()) {
			return "sa";  //TODO 改成你自己的
		}
		if (isOracle()) {
			return "scott";  //TODO 改成你自己的
		}
		return null;
	}
	@Override
	public String getDBPassword() {
		if (isMySQL()) {
			return "apijson";  //TODO 改成你自己的，TiDB 可以当成 MySQL 使用， 默认密码为空字符串 ""
		}
		if (isPostgreSQL()) {
			return null;  //TODO 改成你自己的
		}
		if (isSQLServer()) {
			return "apijson@123";  //TODO 改成你自己的
		}
		if (isOracle()) {
			return "tiger";  //TODO 改成你自己的
		}
		return null;
	}

	//取消注释后，默认的数据库类型会由 MySQL 改为 PostgreSQL
	//	@Override
	//	public String getDatabase() {
	//		String db = super.getDatabase();
	//		return db == null ? DATABASE_POSTGRESQL : db;
	//	}

	//如果确定只用一种数据库，可以重写方法，这种数据库直接 return true，其它数据库直接 return false，来减少判断，提高性能
	//	@Override
	//	public boolean isMySQL() {
	//		return true;
	//	}
	//	@Override
	//	public boolean isPostgreSQL() {
	//		return false;
	//	}
	//	@Override
	//	public boolean isSQLServer() {
	//		return false;
	//	}
	//	@Override
	//	public boolean isOracle() {
	//		return false;
	//	}



	@Override
	public String getIdKey() {
		return SIMPLE_CALLBACK.getIdKey(getDatabase(), getSchema(), getTable());
	}

	@Override
	public String getUserIdKey() {
		return SIMPLE_CALLBACK.getUserIdKey(getDatabase(), getSchema(), getTable());
	}


	public DemoSQLConfig() {
		this(RequestMethod.GET);
	}
	public DemoSQLConfig(RequestMethod method) {
		super(method);
	}
	public DemoSQLConfig(RequestMethod method, String table) {
		super(method, table);
	}
	public DemoSQLConfig(RequestMethod method, int count, int page) {
		super(method, count, page);
	}



	/**获取SQL配置
	 * @param table
	 * @param alias 
	 * @param request
	 * @param isProcedure 
	 * @return
	 * @throws Exception 
	 */
	public static SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
		return newSQLConfig(method, table, alias, request, joinList, isProcedure, SIMPLE_CALLBACK);
	}


}
