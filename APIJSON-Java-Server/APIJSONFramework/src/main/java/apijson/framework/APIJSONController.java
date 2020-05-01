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

import static apijson.RequestMethod.DELETE;
import static apijson.RequestMethod.GET;
import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.HEAD;
import static apijson.RequestMethod.HEADS;
import static apijson.RequestMethod.POST;
import static apijson.RequestMethod.PUT;
import static apijson.framework.APIJSONConstant.ACCESS_;
import static apijson.framework.APIJSONConstant.DEFAULTS;
import static apijson.framework.APIJSONConstant.FORMAT;
import static apijson.framework.APIJSONConstant.FUNCTION_;
import static apijson.framework.APIJSONConstant.REQUEST_;
import static apijson.framework.APIJSONConstant.VERSION;
import static apijson.framework.APIJSONConstant.VISITOR_;
import static apijson.framework.APIJSONConstant.VISITOR_ID;

import java.rmi.ServerException;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.Parser;
import apijson.orm.Visitor;


/**request controller
 * <br > 全通过 HTTP POST 来请求:
 * <br > 1.减少代码 - 客户端无需写 HTTP GET, HTTP PUT 等各种方式的请求代码
 * <br > 2.提高性能 - 无需 URL encode 和 decode
 * <br > 3.调试方便 - 建议使用 APIAuto-机器学习自动化接口管理工具(https://github.com/TommyLemon/APIAuto)
 * @author Lemon
 */
public class APIJSONController {
	public static final String TAG = "APIJSONController";

	public Parser<Long> newParser(HttpSession session, RequestMethod method) {
		Parser<Long> parser = APIJSONApplication.DEFAULT_APIJSON_CREATOR.createParser();
		parser.setMethod(method);
		if (parser instanceof APIJSONParser) {
			((APIJSONParser) parser).setSession(session);
		}
		return parser;
	}

	public String parse(String request, HttpSession session, RequestMethod method) {
		return newParser(session, method).parse(request);
	}
	
	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	public String get(String request, HttpSession session) {
		return parse(request, session, GET);
	}

	/**计数
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	public String head(String request, HttpSession session) {
		return parse(request, session, HEAD);
	}

	/**限制性GET，request和response都非明文，浏览器看不到，用于对安全性要求高的GET请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	public String gets(String request, HttpSession session) {
		return parse(request, session, GETS);
	}

	/**限制性HEAD，request和response都非明文，浏览器看不到，用于对安全性要求高的HEAD请求
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	public String heads(String request, HttpSession session) {
		return parse(request, session, HEADS);
	}

	/**新增
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	public String post(String request, HttpSession session) {
		return parse(request, session, POST);
	}

	/**修改
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	public String put(String request, HttpSession session) {
		return parse(request, session, PUT);
	}

	/**删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	public String delete(String request, HttpSession session) {
		return parse(request, session, DELETE);
	}


	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	/**重新加载配置
	 * @param request
	 * @return
	 * @see
	 * <pre>
		{
			"type": "ALL",  //重载对象，ALL, FUNCTION, REQUEST, ACCESS，非必须
			"phone": "13000082001",
			"verify": "1234567" //验证码，对应类型为 Verify.TYPE_RELOAD
		}
	 * </pre>
	 */
	public JSONObject reload(String type) {
		JSONObject result = APIJSONParser.newSuccessResult();

		boolean reloadAll = StringUtil.isEmpty(type, true) || "ALL".equals(type);

		if (reloadAll || "FUNCTION".equals(type)) {
			try {
				result.put(FUNCTION_, APIJSONFunctionParser.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(FUNCTION_, APIJSONParser.newErrorResult(e));
			}
		}

		if (reloadAll || "REQUEST".equals(type)) {
			try {
				result.put(REQUEST_, StructureUtil.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(REQUEST_, APIJSONParser.newErrorResult(e));
			}
		}

		if (reloadAll || "ACCESS".equals(type)) {
			try {
				result.put(ACCESS_, APIJSONVerifier.init());
			} catch (ServerException e) {
				e.printStackTrace();
				result.put(ACCESS_, APIJSONParser.newErrorResult(e));
			}
		}

		return result;
	}


	/**用户登录
	 * @param session 
	 * @param visitor 
	 * @param version 
	 * @param format 
	 * @param defaults 
	 * @return 返回类型设置为 Object 是为了子类重写时可以有返回值，避免因为冲突而另写一个有返回值的登录方法
	 */
	public Object login(@NotNull HttpSession session, Visitor<Long> visitor, Integer version, Boolean format, JSONObject defaults) {
		//登录状态保存至session
		session.setAttribute(VISITOR_ID, visitor.getId()); //用户id
		session.setAttribute(VISITOR_, visitor); //用户
		session.setAttribute(VERSION, version); //全局默认版本号
		session.setAttribute(FORMAT, format); //全局默认格式化配置
		session.setAttribute(DEFAULTS, defaults); //给每个请求JSON最外层加的字段
		return null;
	}

	/**退出登录，清空session
	 * @param session
	 * @return 返回类型设置为 Object 是为了子类重写时可以有返回值，避免因为冲突而另写一个有返回值的登录方法
	 */
	public Object logout(@NotNull HttpSession session) {
		Object userId = APIJSONVerifier.getVisitorId(session);//必须在session.invalidate();前！
		Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
		session.invalidate();
		return null;
	}


	public JSONObject invokeMethod(String request) {
		return MethodUtil.invokeMethod(request);
	}

	public JSONObject listMethod(String request) {
		return MethodUtil.listMethod(request);
	}

}
