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

import com.alibaba.fastjson.JSONObject;

import apijson.NotNull;
import apijson.RequestMethod;


/**远程函数解析器
 * @author Lemon
 */
public interface FunctionParser {

	Object invoke(@NotNull String function, @NotNull JSONObject currentObject) throws Exception;
	
	RequestMethod getMethod();
	FunctionParser setMethod(RequestMethod method);

	String getTag();
	FunctionParser setTag(String tag);

	int getVersion();
	AbstractFunctionParser setVersion(int version);

	@NotNull 
	JSONObject getRequest();
	FunctionParser setRequest(@NotNull JSONObject request);


	String getKey();
	FunctionParser setKey(String key);
	
	String getParentPath();
	FunctionParser setParentPath(String parentPath);

	String getCurrentName();
	FunctionParser setCurrentName(String currentName);

	@NotNull
	JSONObject getCurrentObject();
	FunctionParser setCurrentObject(@NotNull JSONObject currentObject);



}
