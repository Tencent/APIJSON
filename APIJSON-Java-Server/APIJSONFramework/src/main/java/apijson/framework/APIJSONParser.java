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

package apijson.framework;

import static apijson.framework.APIJSONConstant.DEFAULTS;
import static apijson.framework.APIJSONConstant.FORMAT;
import static apijson.framework.APIJSONConstant.VERSION;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import apijson.RequestMethod;
import apijson.orm.AbstractParser;
import apijson.orm.FunctionParser;
import apijson.orm.Parser;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import apijson.orm.Verifier;


/**请求解析器
 * @author Lemon
 */
public class APIJSONParser extends AbstractParser<Long> {
	public static final String TAG = "APIJSONParser";

	public APIJSONParser() {
		super();
	}
	public APIJSONParser(RequestMethod method) {
		super(method);
	}
	public APIJSONParser(RequestMethod method, boolean needVerify) {
		super(method, needVerify);
	}

	private HttpSession session;
	public HttpSession getSession() {
		return session;
	}
	public APIJSONParser setSession(HttpSession session) {
		this.session = session;
		setVisitor(APIJSONVerifier.getVisitor(session));
		return this;
	}

	@Override
	public Parser<Long> createParser() {
		return APIJSONApplication.DEFAULT_APIJSON_CREATOR.createParser();
	}
	@Override
	public FunctionParser createFunctionParser() {
		return APIJSONApplication.DEFAULT_APIJSON_CREATOR.createFunctionParser();
	}
	
	@Override
	public Verifier<Long> createVerifier() {
		return APIJSONApplication.DEFAULT_APIJSON_CREATOR.createVerifier();
	}
	
	@Override
	public SQLConfig createSQLConfig() {
		return APIJSONApplication.DEFAULT_APIJSON_CREATOR.createSQLConfig();
	}
	@Override
	public SQLExecutor createSQLExecutor() {
		return APIJSONApplication.DEFAULT_APIJSON_CREATOR.createSQLExecutor();
	}


	@Override
	public JSONObject parseResponse(JSONObject request) {
		//补充format
		if (session != null && request != null) {
			if (request.get(FORMAT) == null) {
				request.put(FORMAT, session.getAttribute(FORMAT));
			}
			if (request.get(DEFAULTS) == null) {
				JSONObject defaults = (JSONObject) session.getAttribute(DEFAULTS);
				Set<Map.Entry<String, Object>> set = defaults == null ? null : defaults.entrySet();

				if (set != null) {
					for (Map.Entry<String, Object> e : set) {
						if (e != null && request.get(e.getKey()) == null) {
							request.put(e.getKey(), e.getValue());
						}
					}
				}
			}
		}
		return super.parseResponse(request);
	}

	private FunctionParser functionParser;
	public FunctionParser getFunctionParser() {
		return functionParser;
	}
	@Override
	public Object onFunctionParse(JSONObject json, String fun) throws Exception {
		if (functionParser == null) {
			functionParser = createFunctionParser();
			functionParser.setMethod(getMethod());
			functionParser.setTag(getTag());
			functionParser.setVersion(getVersion());
			if (functionParser instanceof APIJSONFunctionParser) {
				((APIJSONFunctionParser) functionParser).setSession(getSession());
			}
		}
		return functionParser.invoke(fun, json);
	}


	@Override
	public APIJSONObjectParser createObjectParser(JSONObject request, String parentPath, String name, SQLConfig arrayConfig, boolean isSubquery) throws Exception {

		return new APIJSONObjectParser(getSession(), request, parentPath, name, arrayConfig, isSubquery) {

			//			@Override
			//			protected APIJSONSQLConfig newQueryConfig() {
			//				if (itemConfig != null) {
			//					return itemConfig;
			//				}
			//				return super.newQueryConfig();
			//			}

			//导致最多评论的(Strong 30个)的那个动态详情界面Android(82001)无姓名和头像，即User=null
			//			@Override
			//			protected void onComplete() {
			//				if (response != null) {
			//					putQueryResult(path, response);//解决获取关联数据时requestObject里不存在需要的关联数据
			//				}
			//			}

		}.setMethod(getMethod()).setParser(this);
	}



	@Override
	public void onVerifyContent() throws Exception {
		//补充全局缺省版本号  //可能在默认为1的前提下这个请求version就需要为0  requestObject.getIntValue(VERSION) <= 0) {
		HttpSession session = getSession();
		if (session != null && requestObject.get(VERSION) == null) {
			requestObject.put(VERSION, session.getAttribute(VERSION));
		}
		super.onVerifyContent();
	}


	//	//可重写来设置最大查询数量
	//	@Override
	//	public int getMaxQueryCount() {
	//		return 50;
	//	}

}
