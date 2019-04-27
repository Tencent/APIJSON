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

package zuo.biao.apijson.server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.NotNull;

/**executor for query(read) or update(write) MySQL database
 * @author Lemon
 */
public interface SQLExecutor {

	/**保存缓存
	 * @param sql
	 * @param map
	 * @param isStatic
	 */
	void putCache(String sql, List<JSONObject> list, boolean isStatic);
	
	List<JSONObject> getCache(String sql, boolean cacheStatic);

	/**移除缓存
	 * @param sql
	 * @param isStatic
	 */
	void removeCache(String sql, boolean isStatic);
	/**获取缓存
	 * @param sql
	 * @param position
	 * @param isStatic
	 * @return
	 */
	JSONObject getCacheItem(String sql, int position, boolean isStatic);


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
	boolean isJSONType(ResultSetMetaData rsmd, int position);

	/**关闭连接，释放资源
	 */
	void close();
	
	Statement getStatement(@NotNull SQLConfig config) throws Exception;
	
	ResultSet executeQuery(@NotNull Statement statement, String sql) throws Exception;
	
	int executeUpdate(@NotNull Statement statement, String sql) throws Exception;
	
	ResultSet execute(@NotNull Statement statement, String sql) throws Exception;
	
}
