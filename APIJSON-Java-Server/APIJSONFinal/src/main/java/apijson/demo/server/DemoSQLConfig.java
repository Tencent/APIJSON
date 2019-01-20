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

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.AbstractSQLConfig;
import zuo.biao.apijson.server.Join;
import zuo.biao.apijson.server.SQLConfig;


/**SQL配置
 * TiDB 用法和 MySQL 一致
 * @author Lemon
 */
public class DemoSQLConfig extends AbstractSQLConfig {

	
	//表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	static {
		TABLE_KEY_MAP.put(User.class.getSimpleName(), "apijson_user");
		TABLE_KEY_MAP.put(Privacy.class.getSimpleName(), "apijson_privacy");
	}

	@Override
	public String getDBUri() {
		//TODO 改成你自己的，TiDB 默认端口为 4000
		return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? "jdbc:postgresql://localhost:5432/postgres" : "jdbc:mysql://localhost:3306";
	}
	@Override
	public String getDBAccount() {
		return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? "postgres" : "root"; //TODO 改成你自己的
	}
	@Override
	public String getDBPassword() {
		return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? null : "apijson"; //TODO 改成你自己的，TiDB 默认密码为空字符串 ""
	}
	@Override
	public String getSchema() {
		String s = super.getSchema();
		return StringUtil.isEmpty(s, true) ? "sys" : s; //TODO 改成你自己的
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
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static SQLConfig newSQLConfig(RequestMethod method, String table, JSONObject request, List<Join> joinList) throws Exception {
		return newSQLConfig(method, table, request, joinList, new Callback() {

			@Override
			public DemoSQLConfig getSQLConfig(RequestMethod method, String table) {
				return new DemoSQLConfig(method, table);
			}
			
			@Override
			public Object newId(RequestMethod method, String table) {
				return System.currentTimeMillis(); //为 post 请求提供 id， 只能是 Long 或 String
			}
		});
	}


}
