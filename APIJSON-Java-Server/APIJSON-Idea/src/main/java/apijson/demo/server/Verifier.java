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

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.http.HttpSession;

import apijson.demo.server.model.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.MethodAccess;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.model.Column;
import zuo.biao.apijson.model.Request;
import zuo.biao.apijson.model.Response;
import zuo.biao.apijson.model.Table;
import zuo.biao.apijson.model.Test;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotLoggedInException;
import zuo.biao.apijson.server.sql.SQLConfig;

/**权限验证类
 * @author Lemon
 */
public class Verifier {
	private static final String TAG = "Verifier";


	public static final String KEY_PASSWORD = "password";
	public static final String KEY_LOGIN_PASSWORD = "loginPassword";
	public static final String KEY_PAY_PASSWORD = "payPassword";
	public static final String KEY_OLD_PASSWORD = "oldPassword";


	// <TableName, <METHOD, allowRoles>>
	// <User, <GET, [OWNER, ADMIN]>>
	public static final Map<String, Map<RequestMethod, RequestRole[]>> ACCESS_MAP;
	static {
		ACCESS_MAP = new HashMap<String, Map<RequestMethod, RequestRole[]>>();

		ACCESS_MAP.put(Table.class.getSimpleName(), getAccessMap(Table.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Column.class.getSimpleName(), getAccessMap(Column.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Test.class.getSimpleName(), getAccessMap(Test.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Request.class.getSimpleName(), getAccessMap(Request.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Response.class.getSimpleName(), getAccessMap(Response.class.getAnnotation(MethodAccess.class)));

		ACCESS_MAP.put(User.class.getSimpleName(), getAccessMap(User.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Privacy.class.getSimpleName(), getAccessMap(Privacy.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Moment.class.getSimpleName(), getAccessMap(Moment.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Comment.class.getSimpleName(), getAccessMap(Comment.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Verify.class.getSimpleName(), getAccessMap(Verify.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Login.class.getSimpleName(), getAccessMap(Login.class.getAnnotation(MethodAccess.class)));
		
		ACCESS_MAP.put(NetsbdNews.class.getSimpleName(), getAccessMap(NetsbdNews.class.getAnnotation(MethodAccess.class)));
        ACCESS_MAP.put(NetsbdNewsCategory.class.getSimpleName(), getAccessMap(NetsbdNewsCategory.class.getAnnotation(MethodAccess.class)));

	}

	/**获取权限Map，每种操作都只允许对应的角色
	 * @param access
	 * @return
	 */
	private static HashMap<RequestMethod, RequestRole[]> getAccessMap(MethodAccess access) {
		if (access == null) {
			return null;
		}

		HashMap<RequestMethod, RequestRole[]> map = new HashMap<>();
		map.put(GET, access.GET());
		map.put(HEAD, access.HEAD());
		map.put(GETS, access.GETS());
		map.put(HEADS, access.HEADS());
		map.put(POST, access.POST());
		map.put(PUT, access.PUT());
		map.put(DELETE, access.DELETE());

		return map;
	}


	/**验证权限是否通过
	 * @param config
	 * @param visitor
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(SQLConfig config, User visitor) throws Exception {
		String table = config == null ? null : config.getTable();
		if (table == null) {
			return true;
		}
		RequestRole role = config.getRole();

		long userId = visitor == null ? 0 : visitor.getId();
		//TODO 暂时去掉，方便测试
		if (role != RequestRole.UNKNOWN) {//未登录的角色
			verifyLogin(userId);
		}

		RequestMethod method = config.getMethod();
		//验证允许的角色
		verifyRole(table, method, role);


		//验证角色，假定真实强制匹配<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		String userIdkey = Controller.USER_.equals(config.getTable()) || Controller.PRIVACY_.equals(config.getTable())
				? Controller.ID : Controller.USER_ID;

		if (role == null) {
			role = RequestRole.UNKNOWN;
		}
		switch (role) {
		case LOGIN://verifyRole通过就行
			break;
		case CONTACT:
		case CIRCLE:
			//TODO 做一个缓存contactMap<userId, contactArray>，提高[]:{}查询性能， removeAccessInfo时map.remove(userId)
			//不能在User内null -> [] ! 否则会导致某些查询加上不需要的条件！
			List<Object> list = visitor == null || visitor.getContactIdList() == null
			? new ArrayList<Object>() : new ArrayList<Object>(visitor.getContactIdList());
			if (role == RequestRole.CIRCLE) {
				list.add(userId);
			}

			//key!{}:[] 或 其它没有明确id的条件 等 可以和key{}:list组合。类型错误就报错
			Number requestId = (Number) config.getWhere(userIdkey, true);//JSON里数值不能保证是Long，可能是Integer
			JSONArray requestIdArray = (JSONArray) config.getWhere(userIdkey + "{}", true);//不能是 &{}， |{} 不要传，直接{}
			if (requestId != null) {
				if (requestIdArray == null) {
					requestIdArray = new JSONArray();
				}
				requestIdArray.add(requestId);
			}

			if (requestIdArray == null) {//可能是@得到 || requestIdArray.isEmpty()) {//请求未声明key:id或key{}:[...]条件，自动补全
				config.addWhere(userIdkey+"{}", JSON.parseArray(list)); //key{}:[]有效，SQLConfig里throw NotExistException
			} 
			else {//请求已声明key:id或key{}:[]条件，直接验证
				for (Object id : requestIdArray) {
					if (id == null) {
						continue;
					}
					if (id instanceof Number == false) {//不能准确地判断Long，可能是Integer
						throw new UnsupportedDataTypeException(table + ".id类型错误，id类型必须是Long！");
					}
					if (list.contains(new Long("" + id)) == false) {//Integer等转为Long才能正确判断。强转崩溃
						if (method == null) {
							method = GET;
						}
						throw new IllegalAccessException(userIdkey + " = " + id + " 的 " + table
								+ " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
					}
				}
			}
			break;
		case OWNER:
			config.addWhere(userIdkey, userId);
			break;
		case ADMIN://这里不好做，在特定接口内部判断？ TODO  /get/admin + 固定秘钥  Parser#noVerify，之后全局跳过验证
			break;
		default://unknown，verifyRole通过就行
			break;
		}

		//验证角色，假定真实强制匹配>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		return true;
	}


	/**允许请求，角色不好判断，让访问者发过来角色名，OWNER,CONTACT,ADMIN等
	 * @param table
	 * @param method
	 * @param role
	 * @return
	 * @throws Exception 
	 * @see {@link zuo.biao.apijson.JSONObject#KEY_ROLE} 
	 */
	public static void verifyRole(String table, RequestMethod method, RequestRole role) throws Exception {
		Log.d(TAG, "verifyRole  table = " + table + "; method = " + method + "; role = " + role);
		if (table != null) {
			if (method == null) {
				method = GET;
			}
			if (role == null) {
				role = RequestRole.UNKNOWN;
			}
			Map<RequestMethod, RequestRole[]> map = ACCESS_MAP.get(table);

			if (map == null || BaseModel.isContain(map.get(method), role) == false) {
				throw new IllegalAccessException(table + " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
			}
		}
	}


	/**登录校验
	 * @author 
	 * @modifier Lemon
	 * @param session
	 * @throws Exception
	 */
	public static void verifyLogin(HttpSession session) throws Exception {
		Log.d(TAG, "verifyLogin  session.getId() = " + (session == null ? null : session.getId()));
		verifyLogin(getUserId(session));
	}
	/**登录校验
	 * @author Lemon
	 * @param userId
	 * @throws Exception
	 */
	public static void verifyLogin(Long userId) throws Exception {
		//未登录没有权限操作
		if (BaseModel.value(userId) <= 0) {
			throw new NotLoggedInException("未登录，请登录后再操作！");
		}
	}
	
	
	
	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void verifyRepeat(String table, String key, Object value) throws Exception {
		verifyRepeat(table, key, value, 0);
	}
	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	public static void verifyRepeat(String table, String key, Object value, long exceptId) throws Exception {
		if (key == null || value == null) {
			Log.e(TAG, "verifyRepeat  key == null || value == null >> return;");
			return;
		}
		if (value instanceof JSON) {
			throw new UnsupportedDataTypeException(key + ":value 中value的类型不能为JSON！");
		}

		JSONRequest request = new JSONRequest(key, value);
		if (exceptId > 0) {//允许修改自己的属性为该属性原来的值
			request.put(JSONRequest.KEY_ID + "!", exceptId);
		}
		JSONObject repeat = new Parser(HEAD, true).parseResponse(
				new JSONRequest(table, request)
				);
		repeat = repeat == null ? null : repeat.getJSONObject(table);
		if (repeat == null) {
			throw new Exception("服务器内部错误  verifyRepeat  repeat == null");
		}
		if (repeat.getIntValue(JSONResponse.KEY_COUNT) > 0) {
			throw new ConflictException(key + ": " + value + " 已经存在，不能重复！");
		}
	}
	

	/**获取来访用户的id
	 * @author Lemon
	 * @param session
	 * @return 
	 */
	public static long getUserId(HttpSession session) {
		if (session == null) {
			return 0;
		}
		Long userId = (Long) session.getAttribute(Controller.USER_ID);
		if (userId == null) {
			User user = getUser(session);
			userId = user == null ? 0 : BaseModel.value(user.getId());
			session.setAttribute(Controller.USER_ID, userId);
		}
		return BaseModel.value(userId);
	}
	/**获取来访用户
	 * @param session
	 * @return
	 */
	public static User getUser(HttpSession session) {
		return session == null ? null : (User) session.getAttribute(Controller.USER_);
	}


	/**删除请求里的权限信息
	 * @param requestObject
	 * @return
	 */
	public static JSONObject removeAccessInfo(JSONObject requestObject) {
		if (requestObject != null) {
			requestObject.remove(KEY_PASSWORD);
			requestObject.remove(KEY_LOGIN_PASSWORD);
			requestObject.remove(KEY_PAY_PASSWORD);
			requestObject.remove(KEY_OLD_PASSWORD);
		}
		return requestObject;
	}

}
