/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import static apijson.RequestMethod.DELETE;
import static apijson.RequestMethod.GET;
import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.HEAD;
import static apijson.RequestMethod.HEADS;
import static apijson.RequestMethod.POST;
import static apijson.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.MethodAccess;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;
import apijson.StringUtil;
import apijson.orm.exception.ConflictException;
import apijson.orm.exception.NotLoggedInException;
import apijson.orm.model.Access;
import apijson.orm.model.Column;
import apijson.orm.model.Document;
import apijson.orm.model.ExtendedProperty;
import apijson.orm.model.Function;
import apijson.orm.model.PgAttribute;
import apijson.orm.model.PgClass;
import apijson.orm.model.Request;
import apijson.orm.model.Response;
import apijson.orm.model.SysColumn;
import apijson.orm.model.SysTable;
import apijson.orm.model.Table;
import apijson.orm.model.Test;
import apijson.orm.model.TestRecord;

/**权限验证
 * @author Lemon
 * @param <T> id 与 userId 的类型，一般为 Long
 */
public abstract class AbstractVerifier<T> implements Verifier<T> {
	private static final String TAG = "AbstractVerifier";


	// <TableName, <METHOD, allowRoles>>
	// <User, <GET, [OWNER, ADMIN]>>
	public static final Map<String, Map<RequestMethod, RequestRole[]>> SYSTEM_ACCESS_MAP;
	public static final Map<String, Map<RequestMethod, RequestRole[]>> ACCESS_MAP;
	;
	static {
		SYSTEM_ACCESS_MAP = new HashMap<String, Map<RequestMethod, RequestRole[]>>();

		SYSTEM_ACCESS_MAP.put(Function.class.getSimpleName(), getAccessMap(Function.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Request.class.getSimpleName(), getAccessMap(Request.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Response.class.getSimpleName(), getAccessMap(Response.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Test.class.getSimpleName(), getAccessMap(Test.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Access.class.getSimpleName(), getAccessMap(Access.class.getAnnotation(MethodAccess.class)));

		if (Log.DEBUG) {
			SYSTEM_ACCESS_MAP.put(Table.class.getSimpleName(), getAccessMap(Table.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(Column.class.getSimpleName(), getAccessMap(Column.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(PgAttribute.class.getSimpleName(), getAccessMap(PgAttribute.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(PgClass.class.getSimpleName(), getAccessMap(PgClass.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(SysTable.class.getSimpleName(), getAccessMap(SysTable.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(SysColumn.class.getSimpleName(), getAccessMap(SysColumn.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(ExtendedProperty.class.getSimpleName(), getAccessMap(ExtendedProperty.class.getAnnotation(MethodAccess.class)));

			SYSTEM_ACCESS_MAP.put(Document.class.getSimpleName(), getAccessMap(Document.class.getAnnotation(MethodAccess.class)));
			SYSTEM_ACCESS_MAP.put(TestRecord.class.getSimpleName(), getAccessMap(TestRecord.class.getAnnotation(MethodAccess.class)));
		}

		ACCESS_MAP = new HashMap<>(SYSTEM_ACCESS_MAP);
	}

	/**获取权限Map，每种操作都只允许对应的角色
	 * @param access
	 * @return
	 */
	public static HashMap<RequestMethod, RequestRole[]> getAccessMap(MethodAccess access) {
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


	@Override
	public String getVisitorIdKey(SQLConfig config) {
		return config.getUserIdKey();
	}



	@NotNull
	protected Visitor<T> visitor;
	protected Object visitorId;
	@NotNull
	@Override
	public Visitor<T> getVisitor() {
		return visitor;
	}
	@Override
	public AbstractVerifier<T> setVisitor(Visitor<T> visitor) {
		this.visitor = visitor;
		this.visitorId = visitor == null ? null : visitor.getId();

		//导致内部调用且放行校验(noVerifyLogin, noVerifyRole)也抛异常
		//		if (visitorId == null) {
		//			throw new NullPointerException(TAG + ".setVisitor visitorId == null !!! 可能导致权限校验失效，引发安全问题！");
		//		}

		return this;
	}


	/**验证权限是否通过
	 * @param config
	 * @param visitor
	 * @return
	 * @throws Exception
	 */
	public boolean verify(SQLConfig config) throws Exception {
		String table = config == null ? null : config.getTable();
		if (table == null) {
			return true;
		}
		RequestRole role = config.getRole();
		if (role == null) {
			role = RequestRole.UNKNOWN;
		}

		if (role != RequestRole.UNKNOWN) {//未登录的角色
			verifyLogin();
		}

		RequestMethod method = config.getMethod();

		verifyRole(table, method, role);//验证允许的角色


		//验证角色，假定真实强制匹配<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		String visitorIdkey = getVisitorIdKey(config);

		Object requestId;
		switch (role) {
		case LOGIN://verifyRole通过就行
			break;
		case CONTACT:
		case CIRCLE:
			//TODO 做一个缓存contactMap<visitorId, contactArray>，提高[]:{}查询性能， removeAccessInfo时map.remove(visitorId)
			//不能在Visitor内null -> [] ! 否则会导致某些查询加上不需要的条件！
			List<Object> list = visitor.getContactIdList() == null
			? new ArrayList<Object>() : new ArrayList<Object>(visitor.getContactIdList());
			if (role == RequestRole.CIRCLE) {
				list.add(visitorId);
			}

			//key!{}:[] 或 其它没有明确id的条件 等 可以和key{}:list组合。类型错误就报错
			requestId = (Number) config.getWhere(visitorIdkey, true);//JSON里数值不能保证是Long，可能是Integer
			JSONArray requestIdArray = (JSONArray) config.getWhere(visitorIdkey + "{}", true);//不能是 &{}， |{} 不要传，直接{}
			if (requestId != null) {
				if (requestIdArray == null) {
					requestIdArray = new JSONArray();
				}
				requestIdArray.add(requestId);
			}

			if (requestIdArray == null) {//可能是@得到 || requestIdArray.isEmpty()) {//请求未声明key:id或key{}:[...]条件，自动补全
				config.putWhere(visitorIdkey+"{}", JSON.parseArray(list), true); //key{}:[]有效，SQLConfig里throw NotExistException
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
						throw new IllegalAccessException(visitorIdkey + " = " + id + " 的 " + table
								+ " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
					}
				}
			}
			break;
		case OWNER:
			if (config.getMethod() == RequestMethod.POST) {
				List<String> c = config.getColumn();
				List<List<Object>> ovs = config.getValues();
				if ( (c == null || c.isEmpty()) || (ovs == null || ovs.isEmpty()) ) {
					throw new IllegalArgumentException("POST 请求必须在Table内设置要保存的 key:value ！");
				}

				int index = c.indexOf(visitorIdkey);
				if (index >= 0) {
					Object oid;
					for (List<Object> ovl : ovs) {
						oid = ovl == null || index >= ovl.size() ? null : ovl.get(index);
						if (oid == null || StringUtil.getString(oid).equals("" + visitorId) == false) {
							throw new IllegalAccessException(visitorIdkey + " = " + oid + " 的 " + table
									+ " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
						}
					}
				}
				else {
					List<String> nc = new ArrayList<>(c);
					nc.add(visitorIdkey);
					config.setColumn(nc);

					List<List<Object>> nvs = new ArrayList<>();
					List<Object> nvl;
					for (List<Object> ovl : ovs) {
						nvl = ovl == null || ovl.isEmpty() ? new ArrayList<>() : new ArrayList<>(ovl);
						nvl.add(visitorId);
						nvs.add(nvl);
					}

					config.setValues(nvs);
				}
			}
			else {
				requestId = config.getWhere(visitorIdkey, true);//JSON里数值不能保证是Long，可能是Integer
				if (requestId != null && StringUtil.getString(requestId).equals(StringUtil.getString(visitorId)) == false) {
					throw new IllegalAccessException(visitorIdkey + " = " + requestId + " 的 " + table
							+ " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
				}

				config.putWhere(visitorIdkey, visitorId, true);
			}
			break;
		case ADMIN://这里不好做，在特定接口内部判。 可以是  /get/admin + 固定秘钥  Parser#noVerify，之后全局跳过验证
			verifyAdmin();
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
	 * @see {@link apijson.JSONObject#KEY_ROLE} 
	 */
	public void verifyRole(String table, RequestMethod method, RequestRole role) throws Exception {
		Log.d(TAG, "verifyRole  table = " + table + "; method = " + method + "; role = " + role);
		if (table != null) {
			if (method == null) {
				method = GET;
			}
			if (role == null) {
				role = RequestRole.UNKNOWN;
			}
			Map<RequestMethod, RequestRole[]> map = ACCESS_MAP.get(table);

			if (map == null || Arrays.asList(map.get(method)).contains(role) == false) {
				throw new IllegalAccessException(table + " 不允许 " + role.name() + " 用户的 " + method.name() + " 请求！");
			}
		}
	}


	/**登录校验
	 * @author Lemon
	 * @param visitorId
	 * @throws Exception
	 */
	@Override
	public void verifyLogin() throws Exception {
		//未登录没有权限操作
		if (visitorId == null) {
			throw new NotLoggedInException("未登录，请登录后再操作！");
		}

		if (visitorId instanceof Number) {
			if (((Number) visitorId).longValue() <= 0) {
				throw new NotLoggedInException("未登录，请登录后再操作！");
			}
		} 
		else if (visitorId instanceof String) {
			if (StringUtil.isEmpty(visitorId, true)) {
				throw new NotLoggedInException("未登录，请登录后再操作！");
			}
		}
		else {
			throw new UnsupportedDataTypeException("visitorId 只能是 Long 或 String 类型！");
		}

	}

	@Override
	public void verifyAdmin() throws Exception {
		throw new UnsupportedOperationException("不支持 ADMIN 角色！如果要支持就在子类重写这个方法来校验 ADMIN 角色，不通过则 throw IllegalAccessException!");
	}


	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	@Override
	public void verifyRepeat(String table, String key, Object value) throws Exception {
		verifyRepeat(table, key, value, 0);
	}
	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	@Override
	public void verifyRepeat(String table, String key, Object value, long exceptId) throws Exception {
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
		JSONObject repeat = createParser().setMethod(HEAD).setNeedVerify(true).parseResponse(
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

}
