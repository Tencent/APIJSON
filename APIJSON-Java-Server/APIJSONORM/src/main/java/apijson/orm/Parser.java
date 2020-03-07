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

package apijson.orm;

import java.sql.SQLException;
import java.sql.Savepoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;

/**解析器
 * @author Lemon
 */
public interface Parser<T> {

	int DEFAULT_QUERY_COUNT = 10;
	int MAX_QUERY_PAGE = 100;
	int MAX_QUERY_COUNT = 100;
	int MAX_UPDATE_COUNT = 10;
	int MAX_SQL_COUNT = 200;
	int MAX_OBJECT_COUNT = 5;
	int MAX_ARRAY_COUNT = 5;
	int MAX_QUERY_DEPTH = 5;
	
	
	@NotNull
	Visitor<T> getVisitor();
	Parser<T> setVisitor(@NotNull Visitor<T> visitor);

	@NotNull
	RequestMethod getMethod();
	Parser<T> setMethod(@NotNull RequestMethod method);

	int getVersion();
	Parser<T> setVersion(int version);
	
	String getTag();
	Parser<T> setTag(String tag);

	JSONObject getRequest();
	Parser<T> setRequest(JSONObject request);

	boolean isNoVerify();
	Parser<T> setNoVerify(boolean noVerify);

	boolean isNoVerifyLogin();
	Parser<T> setNoVerifyLogin(boolean noVerifyLogin);

	boolean isNoVerifyRole();
	Parser<T> setNoVerifyRole(boolean noVerifyRole);

	boolean isNoVerifyContent();
	Parser<T> setNoVerifyContent(boolean noVerifyContent);

	
	String parse(String request);
	String parse(JSONObject request);

	JSONObject parseResponse(String request);
	JSONObject parseResponse(JSONObject request);



	JSONObject parseCorrectRequest() throws Exception;
	
	JSONObject parseCorrectRequest(RequestMethod method, String tag, int version, String name, JSONObject request,
			int maxUpdateCount, SQLCreator creator) throws Exception;
	
	JSONObject parseCorrectResponse(String table, JSONObject response) throws Exception;

	JSONObject getStructure(String table, String key, String value, int version) throws Exception;



	JSONObject onObjectParse(JSONObject request, String parentPath, String name, SQLConfig arrayConfig, boolean isSubquery) throws Exception;

	JSONArray onArrayParse(JSONObject request, String parentPath, String name, boolean isSubquery) throws Exception;

	/**解析远程函数
	 * @param object
	 * @param function
	 * @return
	 * @throws Exception
	 */
	Object onFunctionParse(JSONObject object, String function) throws Exception;
	
	ObjectParser createObjectParser(JSONObject request, String parentPath, String name, SQLConfig arrayConfig, boolean isSubquery) throws Exception;

	int getDefaultQueryCount();
	int getMaxQueryPage();
	int getMaxQueryCount();
	int getMaxUpdateCount();
	int getMaxSQLCount();
	int getMaxObjectCount();
	int getMaxArrayCount();
	int getMaxQueryDepth();
	
	void putQueryResult(String path, Object result);


	Object getValueByPath(String valuePath);


	void onVerifyLogin() throws Exception;
	void onVerifyContent() throws Exception;
	void onVerifyRole(SQLConfig config) throws Exception;
	
	JSONObject executeSQL(SQLConfig config, boolean isSubquery) throws Exception;
	
	SQLExecutor getSQLExecutor();
	Verifier<T> getVerifier();
	
	
	Boolean getGlobleFormat();
	RequestRole getGlobleRole();
	String getGlobleDatabase();
	String getGlobleSchema();
	Boolean getGlobleExplain();
	String getGlobleCache();

	
	int getTransactionIsolation();
	void setTransactionIsolation(int transactionIsolation);
	
	void begin(int transactionIsolation);
	void rollback() throws SQLException;
	void rollback(Savepoint savepoint) throws SQLException;
	void commit() throws SQLException;
	void close();

}
