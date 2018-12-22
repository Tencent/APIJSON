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

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.AbstractObjectParser;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.SQLConfig;


/**简化Parser，getObject和getArray(getArrayConfig)都能用
 * @author Lemon
 */
public abstract class DemoObjectParser extends AbstractObjectParser {

	static {
		COMPILE_MAP.put("phone", StringUtil.PATTERN_PHONE);
		COMPILE_MAP.put("email", StringUtil.PATTERN_EMAIL);
		COMPILE_MAP.put("idCard", StringUtil.PATTERN_ID_CARD);
	}


	
	/**for single object
	 * @param parentPath
	 * @param request
	 * @param name
	 * @throws Exception 
	 */
	public DemoObjectParser(HttpSession session, @NotNull JSONObject request, String parentPath, String name, SQLConfig arrayConfig) throws Exception {
		super(request, parentPath, name, arrayConfig);
	}
	
	@Override
	public DemoObjectParser setMethod(RequestMethod method) {
		super.setMethod(method);
		return this;
	}

	@Override
	public DemoObjectParser setParser(Parser<?> parser) {
		super.setParser(parser);
		return this;
	}

	@Override
	public SQLConfig newSQLConfig() throws Exception {
		return DemoSQLConfig.newSQLConfig(method, table, sqlRequest, joinList);
	}


}
