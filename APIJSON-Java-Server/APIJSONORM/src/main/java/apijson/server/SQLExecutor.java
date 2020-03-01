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

package apijson.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public interface SQLExecutor {

	/**保存缓存
	 * @param sql
	 * @param map
	 * @param type
	 */
	void putCache(String sql, List<JSONObject> list, int type);;
	
	List<JSONObject> getCache(String sql, int type);

	/**移除缓存
	 * @param sql
	 * @param type
	 */
	void removeCache(String sql, int type);
	/**获取缓存
	 * @param sql
	 * @param position
	 * @param type
	 * @return
	 */
	JSONObject getCacheItem(String sql, int position, int type);


	/**执行SQL
	 * @param config
	 * @return
	 * @throws Exception
	 */
	JSONObject execute(@NotNull SQLConfig config, boolean unknowType) throws Exception;
	
	//executeQuery和executeUpdate这两个函数因为返回类型不同，所以不好合并
	/**执行查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	ResultSet executeQuery(@NotNull SQLConfig config) throws Exception;
	
	/**执行增、删、改
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	int executeUpdate(@NotNull SQLConfig config) throws Exception;
	

	/**判断是否为JSON类型
	 * @param rsmd
	 * @param position
	 * @return
	 */
	boolean isJSONType(@NotNull SQLConfig config, ResultSetMetaData rsmd, int position, String lable);

	
	Connection getConnection(@NotNull SQLConfig config) throws Exception;
	Statement getStatement(@NotNull SQLConfig config) throws Exception;
	
	int getTransactionIsolation();
	void setTransactionIsolation(int transactionIsolation);
	/**开始事务
	 * @throws SQLException
	 */
	void begin(int transactionIsolation) throws SQLException;
	/**回滚事务
	 * @throws SQLException
	 */
	void rollback() throws SQLException;
	/**提交事务
	 * @throws SQLException
	 */
	void rollback(Savepoint savepoint) throws SQLException;
	/**提交事务
	 * @throws SQLException
	 */
	void commit() throws SQLException;
	/**关闭连接，释放资源
	 */
	void close();
	
	ResultSet executeQuery(@NotNull Statement statement, String sql) throws Exception;
	
	int executeUpdate(@NotNull Statement statement, String sql) throws Exception;
	
	ResultSet execute(@NotNull Statement statement, String sql) throws Exception;

	int getGeneratedSQLCount();

	int getCachedSQLCount();

	int getExecutedSQLCount();

	
}
