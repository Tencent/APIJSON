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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.NotNull;
import zuo.biao.apijson.RequestMethod;

/**解析器
 * @author Lemon
 */
public interface Parser {

	int MAX_QUERY_COUNT = 100;
	int MAX_UPDATE_COUNT = 10;
	
	
	@NotNull
	Visitor getVisitor();
	Parser setVisitor(@NotNull Visitor visitor);

	@NotNull
	RequestMethod getMethod();
	Parser setMethod(@NotNull RequestMethod method);

	JSONObject getRequest();
	Parser setRequest(JSONObject request);

	boolean isNoVerify();
	Parser setNoVerify(boolean noVerify);

	boolean isNoVerifyLogin();
	Parser setNoVerifyLogin(boolean noVerifyLogin);

	boolean isNoVerifyRole();
	Parser setNoVerifyRole(boolean noVerifyRole);

	boolean isNoVerifyContent();
	Parser setNoVerifyContent(boolean noVerifyContent);

	
	@NotNull
	Verifier createVerifier();

	@NotNull
	SQLConfig createSQLConfig();

	@NotNull
	SQLExecutor createSQLExecutor();


	String parse(String request);
	String parse(JSONObject request);

	JSONObject parseResponse(String request);
	JSONObject parseResponse(JSONObject request);




	JSONObject parseCorrectRequest() throws Exception;
	
	JSONObject parseCorrectRequest(JSONObject target) throws Exception;

	JSONObject parseCorrectResponse(String table, JSONObject response) throws Exception;

	JSONObject getStructure(String table, String key, String value, int version) throws Exception;



	JSONObject onObjectParse(JSONObject request, String parentPath, String name, SQLConfig arrayConfig) throws Exception;

	JSONArray onArrayParse(JSONObject request, String parentPath, String name) throws Exception;

	ObjectParser createObjectParser(JSONObject request, String parentPath, String name, SQLConfig arrayConfig) throws Exception;

	int getMaxQueryCount();
	int getMaxUpdateCount();
	
	void putQueryResult(String path, Object result);


	Object getValueByPath(String valuePath);


	JSONObject executeSQL(SQLConfig config) throws Exception;

}
