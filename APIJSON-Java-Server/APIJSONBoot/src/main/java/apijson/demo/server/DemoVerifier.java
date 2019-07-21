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

import static apijson.demo.server.Controller.ACCESS_;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.AbstractVerifier;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.Visitor;


/**权限验证器
 * @author Lemon
 */
public class DemoVerifier extends AbstractVerifier<Long> {
	private static final String TAG = "DemoVerifier";


	//	由 init 方法读取数据库 Access 表来替代手动输入配置
	//	// <TableName, <METHOD, allowRoles>>
	//	// <User, <GET, [OWNER, ADMIN]>>
	//	static { //注册权限
	//		ACCESS_MAP.put(User.class.getSimpleName(), getAccessMap(User.class.getAnnotation(MethodAccess.class)));
	//		ACCESS_MAP.put(Privacy.class.getSimpleName(), getAccessMap(Privacy.class.getAnnotation(MethodAccess.class)));
	//		ACCESS_MAP.put(Moment.class.getSimpleName(), getAccessMap(Moment.class.getAnnotation(MethodAccess.class)));
	//		ACCESS_MAP.put(Comment.class.getSimpleName(), getAccessMap(Comment.class.getAnnotation(MethodAccess.class)));
	//		ACCESS_MAP.put(Verify.class.getSimpleName(), getAccessMap(Verify.class.getAnnotation(MethodAccess.class)));
	//		ACCESS_MAP.put(Login.class.getSimpleName(), getAccessMap(Login.class.getAnnotation(MethodAccess.class)));
	//	}

	/**初始化，加载所有权限配置
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init() throws ServerException {
		return init(false);
	}
	/**初始化，加载所有权限配置
	 * @param shutdownWhenServerError 
	 * @return 
	 * @throws ServerException
	 */
	public static JSONObject init(boolean shutdownWhenServerError) throws ServerException {
		JSONRequest request = new JSONRequest();

		{   //Access[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			JSONRequest accessItem = new JSONRequest();

			{   //Access<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				JSONRequest access = new JSONRequest();
				accessItem.put(ACCESS_, access);
			}   //Access>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			request.putAll(accessItem.toArray(0, 0, ACCESS_));
		}   //Access[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		
		JSONObject response = new DemoParser(RequestMethod.GET, true).parseResponse(request);
		if (JSONResponse.isSuccess(response) == false) {
			Log.e(TAG, "\n\n\n\n\n !!!! 查询权限配置异常 !!!\n" + response.getString(JSONResponse.KEY_MSG) + "\n\n\n\n\n");
			onServerError("查询权限配置异常 !", shutdownWhenServerError);
		}

		JSONArray list = response.getJSONArray(ACCESS_ + "[]");
		if (list == null || list.isEmpty()) {
			Log.w(TAG, "init list == null || list.isEmpty()，没有可用的权限配置");
			throw new NullPointerException("没有可用的权限配置");
		}

		Log.d(TAG, "init < for ACCESS_MAP.size() = " + ACCESS_MAP.size() + " <<<<<<<<<<<<<<<<<<<<<<<<");
		
		ACCESS_MAP.clear();

		JSONObject item;
		for (int i = 0; i < list.size(); i++) {
			item = list.getJSONObject(i);
			if (item == null) {
				continue;
			}

			Map<RequestMethod, RequestRole[]> map = new HashMap<>();
			map.put(RequestMethod.GET, JSON.parseObject(item.getString("get"), RequestRole[].class));
			map.put(RequestMethod.HEAD, JSON.parseObject(item.getString("head"), RequestRole[].class));
			map.put(RequestMethod.GETS, JSON.parseObject(item.getString("gets"), RequestRole[].class));
			map.put(RequestMethod.HEADS, JSON.parseObject(item.getString("heads"), RequestRole[].class));
			map.put(RequestMethod.POST, JSON.parseObject(item.getString("post"), RequestRole[].class));
			map.put(RequestMethod.PUT, JSON.parseObject(item.getString("put"), RequestRole[].class));
			map.put(RequestMethod.DELETE, JSON.parseObject(item.getString("delete"), RequestRole[].class));

			String name = item.getString("name");
			String alias = item.getString("alias");
			
			/**TODO 
			 * 以下判断写得比较复杂，因为表设计不够好，但为了兼容旧版 APIJSON 服务 和 APIAuto 工具而保留了下来。
			 * 如果是 name 为接口传参的 表对象 的 key，对应一个可缺省的 tableName，判断就会简单不少。
			 */
			
			if (StringUtil.isEmpty(name, true)) {
				onServerError("字段 name 的值不能为空！", shutdownWhenServerError);
			}
			
			if (StringUtil.isEmpty(alias, true)) {
				if (JSONRequest.isTableKey(name) == false) {
					onServerError("name: " + name + "不合法！字段 alias 的值为空时，name 必须为合法表名！", shutdownWhenServerError);
				}
				
				ACCESS_MAP.put(name, map);
			}
			else {
				if (JSONRequest.isTableKey(alias) == false) {
					onServerError("alias: " + alias + "不合法！字段 alias 的值只能为 空 或者 合法表名！", shutdownWhenServerError);
				}
				
				ACCESS_MAP.put(alias, map);
			}
			
		}

		Log.d(TAG, "init  for /> ACCESS_MAP.size() = " + ACCESS_MAP.size() + " >>>>>>>>>>>>>>>>>>>>>>>");

		return response;
	}


	private static void onServerError(String msg, boolean shutdown) throws ServerException {
		Log.e(TAG, "\n权限配置文档测试未通过！\n请修改 Access 表里的记录！\n保证前端看到的权限配置文档是正确的！！！\n\n原因：\n" + msg);

		if (shutdown) {
			System.exit(1);	
		} else {
			throw new ServerException(msg);
		}
	}



	@NotNull
	@Override
	public DemoParser createParser() {
		DemoParser parser = new DemoParser();
		parser.setVisitor(visitor);
		return parser;
	}

	/**登录校验
	 * @author 
	 * @modifier Lemon
	 * @param session
	 * @throws Exception
	 */
	public static void verifyLogin(HttpSession session) throws Exception {
		Log.d(TAG, "verifyLogin  session.getId() = " + (session == null ? null : session.getId()));
		new DemoVerifier().setVisitor(getVisitor(session)).verifyLogin();
	}


	/**获取来访用户的id
	 * @author Lemon
	 * @param session
	 * @return 
	 */
	public static long getVisitorId(HttpSession session) {
		if (session == null) {
			return 0;
		}
		Long id = (Long) session.getAttribute(Controller.USER_ID);
		if (id == null) {
			Visitor<Long> v = getVisitor(session);
			id = v == null ? 0 : value(v.getId());
			session.setAttribute(Controller.USER_ID, id);
		}
		return value(id);
	}
	/**获取来访用户
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Visitor<Long> getVisitor(HttpSession session) {
		return session == null ? null : (Visitor<Long>) session.getAttribute(Controller.USER_);
	}

	public static long value(Long v) {
		return v == null ? 0 : v;
	}




}
