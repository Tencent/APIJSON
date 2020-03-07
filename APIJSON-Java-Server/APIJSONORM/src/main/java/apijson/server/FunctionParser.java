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

import com.alibaba.fastjson.JSONObject;

import apijson.RequestMethod;

/**解析器
 * @author Lemon
 */
public interface FunctionParser {

	RequestMethod getMethod();
	String getTag();
	int getVersion();

	FunctionParser setMethod(RequestMethod method);
	FunctionParser setTag(String tag);
	AbstractFunctionParser setVersion(int version);

	Object invoke(String function, JSONObject currentObject) throws Exception;

}
