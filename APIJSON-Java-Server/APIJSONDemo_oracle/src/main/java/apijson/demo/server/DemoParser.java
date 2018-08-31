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

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.server.AbstractParser;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.SQLConfig;
import zuo.biao.apijson.server.SQLCreator;
import zuo.biao.apijson.server.SQLExecutor;
import zuo.biao.apijson.server.Structure;
import zuo.biao.apijson.server.Verifier;

import java.io.FileNotFoundException;


/**请求解析器
 * @author Lemon
 */
public class DemoParser extends AbstractParser implements SQLCreator {


	public DemoParser() {
		super();
	}
	public DemoParser(RequestMethod method) {
		super(method);
	}
	public DemoParser(RequestMethod method, boolean noVerify) {
		super(method, noVerify);
	}

	protected HttpSession session;
	public HttpSession getSession() {
		return session;
	}
	public AbstractParser setSession(HttpSession session) {
		this.session = session;
		setVisitor(DemoVerifier.getVisitor(session));
		return this;
	}


	@Override
	public Verifier createVerifier() {
		return new DemoVerifier();
	}
	@Override
	public SQLConfig createSQLConfig() {
		try {
			return new DemoSQLConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public SQLExecutor createSQLExecutor() {
		return new DemoSQLExecutor();
	}
	
	
	
	@Override
	public DemoObjectParser createObjectParser(JSONObject request, String parentPath, String name, SQLConfig arrayConfig) throws Exception {

		return new DemoObjectParser(session, request, parentPath, name, arrayConfig) {

			//TODO 删除，onPUTArrayParse改用MySQL函数JSON_ADD, JSON_REMOVE等
			@Override
			public JSONObject parseResponse(JSONRequest request) throws Exception {
				DemoParser demoParser = new DemoParser(RequestMethod.GET);
				demoParser.setSession(session);
				//						parser.setNoVerifyRequest(noVerifyRequest)
				demoParser.setNoVerifyLogin(noVerifyLogin);
				demoParser.setNoVerifyRole(noVerifyRole);
				return demoParser.parseResponse(request);
			}


			//			@Override
			//			protected DemoSQLConfig newQueryConfig() {
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

		}.setMethod(requestMethod).setParser(this);
	}

	
	
	@Override
	protected void onVerifyContent() throws Exception {
		//补充全局缺省版本号
		if (session != null && requestObject.getIntValue(JSONRequest.KEY_VERSION) <= 0) {
			requestObject.put(JSONRequest.KEY_VERSION, session.getAttribute(JSONRequest.KEY_VERSION));
		}
		super.onVerifyContent();
	}


	@Override
	public JSONObject parseCorrectRequest(JSONObject target) throws Exception {
		return Structure.parseRequest(requestMethod, "", target, requestObject, this);
	}

}
