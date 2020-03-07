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

package apijson.boot.test;

import apijson.framework.APIJSONSQLConfig;


/**SQL配置
 * TiDB 用法和 MySQL 一致
 * @author Lemon
 */
public class DemoSQLConfig extends APIJSONSQLConfig {

	static {
		DEFAULT_DATABASE = DATABASE_MYSQL;  //TODO 默认数据库类型，改成你自己的
		DEFAULT_SCHEMA = "sys";  //TODO 默认模式名，改成你自己的，默认情况是 MySQL: sys, PostgreSQL: public, SQL Server: dbo, Oracle: 
	}

	@Override
	public String getDBVersion() {
		return "5.7.22"; //"8.0.11"; //TODO 改成你自己的 MySQL 或 PostgreSQL 数据库版本号 //MYSQL 8 和 7 使用的 JDBC 配置不一样
	}
	@Override
	public String getDBUri() {
		return "jdbc:mysql://localhost:3306"; //TODO 改成你自己的，TiDB 可以当成 MySQL 使用，默认端口为 4000
	}
	@Override
	public String getDBAccount() {
		return "root";  //TODO 改成你自己的
	}
	@Override
	public String getDBPassword() {
		return "apijson";  //TODO 改成你自己的，TiDB 可以当成 MySQL 使用， 默认密码为空字符串 ""
	}

}
