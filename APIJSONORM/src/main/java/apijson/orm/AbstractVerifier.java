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
import static apijson.orm.Operation.EXIST;
import static apijson.orm.Operation.INSERT;
import static apijson.orm.Operation.MUST;
import static apijson.orm.Operation.REFUSE;
import static apijson.orm.Operation.REMOVE;
import static apijson.orm.Operation.REPLACE;
import static apijson.orm.Operation.TYPE;
import static apijson.orm.Operation.UNIQUE;
import static apijson.orm.Operation.UPDATE;
import static apijson.orm.Operation.VERIFY;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Pattern;

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
import apijson.orm.AbstractSQLConfig.IdCallback;
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
import apijson.orm.model.TestRecord;

/**校验器(权限、请求参数、返回结果等)
 * TODO 合并 Structure 的代码
 * @author Lemon
 * @param <T> id 与 userId 的类型，一般为 Long
 */
public abstract class AbstractVerifier<T> implements Verifier<T>, IdCallback {
	private static final String TAG = "AbstractVerifier";


	// 共享 STRUCTURE_MAP 则不能 remove 等做任何变更，否则在并发情况下可能会出错，加锁效率又低，所以这里改为忽略对应的 key
	public static final List<String> OPERATION_KEY_LIST;

	// <TableName, <METHOD, allowRoles>>
	// <User, <GET, [OWNER, ADMIN]>>
	@NotNull
	public static final Map<String, Map<RequestMethod, RequestRole[]>> SYSTEM_ACCESS_MAP;
	@NotNull
	public static final Map<String, Map<RequestMethod, RequestRole[]>> ACCESS_MAP;

	// <method tag, <version, Request>>
	// <PUT Comment, <1, { "method":"PUT", "tag":"Comment", "structure":{ "MUST":"id"... }... }>>
	@NotNull
	public static final Map<String, SortedMap<Integer, JSONObject>> REQUEST_MAP;

	// 正则匹配的别名快捷方式，例如用 "PHONE" 代替 "^((13[0-9])|(15[^4,\\D])|(18[0-2,5-9])|(17[0-9]))\\d{8}$"
	@NotNull
	public static final Map<String, Pattern> COMPILE_MAP;
	static {
		OPERATION_KEY_LIST = new ArrayList<>();
		OPERATION_KEY_LIST.add(TYPE.name());
		OPERATION_KEY_LIST.add(VERIFY.name());
		OPERATION_KEY_LIST.add(INSERT.name());
		OPERATION_KEY_LIST.add(UPDATE.name());
		OPERATION_KEY_LIST.add(REPLACE.name());
		OPERATION_KEY_LIST.add(EXIST.name());
		OPERATION_KEY_LIST.add(UNIQUE.name());
		OPERATION_KEY_LIST.add(REMOVE.name());
		OPERATION_KEY_LIST.add(MUST.name());
		OPERATION_KEY_LIST.add(REFUSE.name());


		SYSTEM_ACCESS_MAP = new HashMap<String, Map<RequestMethod, RequestRole[]>>();

		SYSTEM_ACCESS_MAP.put(Access.class.getSimpleName(), getAccessMap(Access.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Function.class.getSimpleName(), getAccessMap(Function.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Request.class.getSimpleName(), getAccessMap(Request.class.getAnnotation(MethodAccess.class)));
		SYSTEM_ACCESS_MAP.put(Response.class.getSimpleName(), getAccessMap(Response.class.getAnnotation(MethodAccess.class)));

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

		REQUEST_MAP = new HashMap<>(ACCESS_MAP.size()*6);  // 单个与批量增删改

		COMPILE_MAP = new HashMap<String, Pattern>();
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

	@Override
	public String getIdKey(String database, String schema, String table) {
		return apijson.JSONObject.KEY_ID;
	}
	@Override
	public String getIdKey(String database, String schema, String datasource, String table) {
		return getIdKey(database, schema, table);
	}
	@Override
	public String getUserIdKey(String database, String schema, String table) {
		return apijson.JSONObject.KEY_USER_ID;
	}
	@Override
	public String getUserIdKey(String database, String schema, String datasource, String table) {
		return getUserIdKey(database, schema, table);
	}
	@Override
	public Object newId(RequestMethod method, String database, String schema, String table) {
		return System.currentTimeMillis();
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
	@Deprecated
	public boolean verify(SQLConfig config) throws Exception {
		return verifyAccess(config);
	}
	/**验证权限是否通过
	 * @param config
	 * @param visitor
	 * @return
	 * @throws Exception
	 */
	public boolean verifyAccess(SQLConfig config) throws Exception {
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
			@SuppressWarnings("unchecked") 
			Collection<Object> requestIdArray = (Collection<Object>) config.getWhere(visitorIdkey + "{}", true);//不能是 &{}， |{} 不要传，直接{}
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
					if (list.contains(Long.valueOf("" + id)) == false) {//Integer等转为Long才能正确判断。强转崩溃
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
	 * FIXME 这个方法实际上没有被使用
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
	 * FIXME 这个方法实际上没有被使用，而且与 Structure.verifyRepeat 代码重复度比较高，需要简化
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
			request.put(JSONRequest.KEY_ID + "!", exceptId);  // FIXME 这里 id 写死了，不支持自定义
		}
		JSONObject repeat = createParser().setMethod(GET).setNeedVerify(true).parseResponse(
				new JSONRequest(table, request)
				);
		repeat = repeat == null ? null : repeat.getJSONObject(table);
		if (repeat == null) {
			throw new Exception("服务器内部错误  verifyRepeat  repeat == null");
		}
		if (repeat.getIntValue(JSONResponse.KEY_CODE) > 0) {
			throw new ConflictException(key + ": " + value + " 已经存在，不能重复！");
		}
	}



	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param maxUpdateCount
	 * @param idKey
	 * @param userIdKey
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject verifyRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount
			, final String database, final String schema, final SQLCreator creator) throws Exception {
		return verifyRequest(method, name, target, request, maxUpdateCount, database, schema, this, creator);
	}

	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final SQLCreator creator) throws Exception {
		return verifyRequest(method, name, target, request, Parser.MAX_UPDATE_COUNT, creator);
	}
	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param maxUpdateCount
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount, final SQLCreator creator) throws Exception {
		return verifyRequest(method, name, target, request, maxUpdateCount, null, null, null, creator);
	}

	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param maxUpdateCount
	 * @param idKey
	 * @param userIdKey
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount
			, final String database, final String schema, final IdCallback idCallback, final SQLCreator creator) throws Exception {
		return verifyRequest(method, name, target, request, maxUpdateCount, database, schema, null, idCallback, creator);
	}
	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param maxUpdateCount
	 * @param idKey
	 * @param userIdKey
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount
			, final String database, final String schema, final String datasource, final IdCallback idCallback, final SQLCreator creator) throws Exception {

		Log.i(TAG, "verifyRequest  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request));

		if (target == null || request == null) {// || request.isEmpty()) {
			Log.i(TAG, "verifyRequest  target == null || request == null >> return null;");
			return null;
		}

		//已在 Verifier 中处理
		//		if (RequestRole.get(request.getString(JSONRequest.KEY_ROLE)) == RequestRole.ADMIN) {
		//			throw new IllegalArgumentException("角色设置错误！不允许在写操作Request中传 " + name +
		//					":{ " + JSONRequest.KEY_ROLE + ":admin } ！");
		//		}


		//解析
		return parse(method, name, target, request, database, schema, idCallback, creator, new OnParseCallback() {

			@Override
			public JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
				//				Log.i(TAG, "verifyRequest.parse.onParseJSONObject  key = " + key + "; robj = " + robj);

				if (robj == null) {
					if (tobj != null) {//不允许不传Target中指定的Table
						throw new IllegalArgumentException(method + "请求，请在 " + name + " 内传 " + key + ":{} ！");
					}
				} else if (apijson.JSONObject.isTableKey(key)) {
					String db = request.getString(apijson.JSONObject.KEY_DATABASE);
					String sh = request.getString(apijson.JSONObject.KEY_SCHEMA);
					String ds = request.getString(apijson.JSONObject.KEY_DATASOURCE);
					if (StringUtil.isEmpty(db, false)) {
						db = database;
					}
					if (StringUtil.isEmpty(sh, false)) {
						sh = schema;
					}
					if (StringUtil.isEmpty(ds, false)) {
						ds = datasource;
					}

					String idKey = idCallback == null ? null : idCallback.getIdKey(db, sh, ds, key);
					String finalIdKey = StringUtil.isEmpty(idKey, false) ? apijson.JSONObject.KEY_ID : idKey;

					if (method == RequestMethod.POST) {
						if (robj.containsKey(finalIdKey)) {
							throw new IllegalArgumentException(method + "请求，" + name + "/" + key + " 不能传 " + finalIdKey + " ！");
						}
					} else {
						if (RequestMethod.isQueryMethod(method) == false) {
							verifyId(method.name(), name, key, robj, finalIdKey, maxUpdateCount, true);

							String userIdKey = idCallback == null ? null : idCallback.getUserIdKey(db, sh, ds, key);
							String finalUserIdKey = StringUtil.isEmpty(userIdKey, false) ? apijson.JSONObject.KEY_USER_ID : userIdKey;
							verifyId(method.name(), name, key, robj, finalUserIdKey, maxUpdateCount, false);
						}
					}
				}

				return verifyRequest(method, key, tobj, robj, maxUpdateCount, database, schema, idCallback, creator);
			}

			@Override
			protected JSONArray onParseJSONArray(String key, JSONArray tarray, JSONArray rarray) throws Exception {
				if ((method == RequestMethod.POST || method == RequestMethod.PUT) && JSONRequest.isArrayKey(key)) {
					if (rarray == null || rarray.isEmpty()) {
						throw new IllegalArgumentException(method + "请求，请在 " + name + " 内传 " + key + ":[{ ... }] "
								+ "，批量新增 Table[]:value 中 value 必须是包含表对象的非空数组！其中每个子项 { ... } 都是"
								+ " tag:" + key.substring(0, key.length() - 2) + " 对应单个新增的 structure ！");
					}
					if (rarray.size() > maxUpdateCount) {
						throw new IllegalArgumentException(method + "请求，" + name + "/" + key
								+ " 里面的 " + key + ":[{ ... }] 中 [] 的长度不能超过 " + maxUpdateCount + " ！");
					}
				}
				return super.onParseJSONArray(key, tarray, rarray);
			}
		});

	}

	/**
	 * @param method
	 * @param name
	 * @param key
	 * @param robj
	 * @param idKey
	 * @param atLeastOne 至少有一个不为null
	 */
	private static void verifyId(@NotNull String method, @NotNull String name, @NotNull String key
			, @NotNull JSONObject robj, @NotNull String idKey, final int maxUpdateCount, boolean atLeastOne) {
		//单个修改或删除
		Object id = robj.get(idKey); //如果必须传 id ，可在Request表中配置NECESSARY
		if (id != null && id instanceof Number == false && id instanceof String == false) {
			throw new IllegalArgumentException(method + "请求，" + name + "/" + key
					+ " 里面的 " + idKey + ":value 中value的类型只能是 Long 或 String ！");
		}


		//批量修改或删除
		String idInKey = idKey + "{}";

		JSONArray idIn = null;
		try {
			idIn = robj.getJSONArray(idInKey); //如果必须传 id{} ，可在Request表中配置NECESSARY
		} catch (Exception e) {
			throw new IllegalArgumentException(method + "请求，" + name + "/" + key
					+ " 里面的 " + idInKey + ":value 中value的类型只能是 [Long] ！");
		}
		if (idIn == null) {
			if (atLeastOne && id == null) {
				throw new IllegalArgumentException(method + "请求，" + name + "/" + key
						+ " 里面 " + idKey + " 和 " + idInKey + " 至少传其中一个！");
			}
		} else {
			if (idIn.size() > maxUpdateCount) { //不允许一次操作 maxUpdateCount 条以上记录
				throw new IllegalArgumentException(method + "请求，" + name + "/" + key
						+ " 里面的 " + idInKey + ":[] 中[]的长度不能超过 " + maxUpdateCount + " ！");
			}
			//解决 id{}: ["1' OR 1='1'))--"] 绕过id{}限制
			//new ArrayList<Long>(idIn) 不能检查类型，Java泛型擦除问题，居然能把 ["a"] 赋值进去还不报错
			for (int i = 0; i < idIn.size(); i++) {
				Object o = idIn.get(i);
				if (o == null) {
					throw new IllegalArgumentException(method + "请求，" + name + "/" + key
							+ " 里面的 " + idInKey + ":[] 中所有项都不能为 [ null, <= 0 的数字, 空字符串 \"\" ] 中任何一个 ！");
				}
				if (o instanceof Number) {
					//解决 Windows mysql-5.6.26-winx64 等低于 5.7 的 MySQL 可能 id{}: [0] 生成 id IN(0) 触发 MySQL bug 导致忽略 IN 条件
					//例如 UPDATE `apijson`.`TestRecord` SET `testAccountId` = -1 WHERE ( (`id` IN (0)) AND (`userId`= 82001) )
					if (((Number) o).longValue() <= 0) {
						throw new IllegalArgumentException(method + "请求，" + name + "/" + key
								+ " 里面的 " + idInKey + ":[] 中所有项都不能为 [ null, <= 0 的数字, 空字符串 \"\" ] 中任何一个 ！");
					}
				}
				else if (o instanceof String) {
					if (StringUtil.isEmpty(o, true)) {
						throw new IllegalArgumentException(method + "请求，" + name + "/" + key
								+ " 里面的 " + idInKey + ":[] 中所有项都不能为 [ null, <= 0 的数字, 空字符串 \"\" ] 中任何一个 ！");
					}
				}
				else {
					throw new IllegalArgumentException(method + "请求，" + name + "/" + key
							+ " 里面的 " + idInKey + ":[] 中所有项的类型都只能是 Long 或 String ！");
				}
			}
		}
	}


	/**校验并将response转换为指定的内容和结构
	 * @param method
	 * @param name
	 * @param target
	 * @param response
	 * @param idKey
	 * @param callback
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject verifyResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, final String database, final String schema
			, SQLCreator creator, OnParseCallback callback) throws Exception {
		return verifyResponse(method, name, target, response, database, schema, this, creator, callback);
	}

	/**校验并将response转换为指定的内容和结构
	 * @param method
	 * @param name
	 * @param target
	 * @param response
	 * @param callback
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, SQLCreator creator, OnParseCallback callback) throws Exception {
		return verifyResponse(method, name, target, response, null, null, null, creator, callback);
	}
	/**校验并将response转换为指定的内容和结构
	 * @param method
	 * @param name
	 * @param target
	 * @param response
	 * @param idKey
	 * @param callback
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, final String database, final String schema
			, final IdCallback idKeyCallback, SQLCreator creator, OnParseCallback callback) throws Exception {

		Log.i(TAG, "verifyResponse  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n response = \n" + JSON.toJSONString(response));

		if (target == null || response == null) {// || target.isEmpty() {
			Log.i(TAG, "verifyResponse  target == null || response == null >> return response;");
			return response;
		}

		//解析
		return parse(method, name, target, response, database, schema, idKeyCallback, creator, callback != null ? callback : new OnParseCallback() {
			@Override
			protected JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
				return verifyResponse(method, key, tobj, robj, database, schema, idKeyCallback, creator, callback);
			}
		});
	}


	/**对request和response不同的解析用callback返回
	 * @param method
	 * @param name
	 * @param target
	 * @param real
	 * @param creator
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parse(@NotNull final RequestMethod method, String name, JSONObject target, JSONObject real
			, SQLCreator creator, @NotNull OnParseCallback callback) throws Exception {
		return parse(method, name, target, real, null, null, null, creator, callback);
	}
	/**对request和response不同的解析用callback返回
	 * @param method
	 * @param name
	 * @param target
	 * @param real
	 * @param database
	 * @param schema
	 * @param idCallback
	 * @param creator
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parse(@NotNull final RequestMethod method, String name, JSONObject target, JSONObject real
			, final String database, final String schema, final IdCallback idCallback, SQLCreator creator, @NotNull OnParseCallback callback) throws Exception {
		return parse(method, name, target, real, database, schema, null, idCallback, creator, callback);
	}
	/**对request和response不同的解析用callback返回
	 * @param method
	 * @param name
	 * @param target
	 * @param real
	 * @param database
	 * @param schema
	 * @param datasource
	 * @param idCallback
	 * @param creator
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parse(@NotNull final RequestMethod method, String name, JSONObject target, JSONObject real
			, final String database, final String schema, final String datasource, final IdCallback idCallback, SQLCreator creator, @NotNull OnParseCallback callback) throws Exception {
		if (target == null) {
			return null;
		}

		// 获取配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject type = target.getJSONObject(TYPE.name());
		JSONObject verify = target.getJSONObject(VERIFY.name());
		JSONObject insert = target.getJSONObject(INSERT.name());
		JSONObject update = target.getJSONObject(UPDATE.name());
		JSONObject replace = target.getJSONObject(REPLACE.name());

		String exist = StringUtil.getNoBlankString(target.getString(EXIST.name()));
		String unique = StringUtil.getNoBlankString(target.getString(UNIQUE.name()));
		String remove = StringUtil.getNoBlankString(target.getString(REMOVE.name()));
		String must = StringUtil.getNoBlankString(target.getString(MUST.name()));
		String refuse = StringUtil.getNoBlankString(target.getString(REFUSE.name()));


		// 移除字段<<<<<<<<<<<<<<<<<<<
		String[] removes = StringUtil.split(remove);
		if (removes != null && removes.length > 0) {
			for (String r : removes) {
				real.remove(r);
			}
		}
		// 移除字段>>>>>>>>>>>>>>>>>>>

		// 判断必要字段是否都有<<<<<<<<<<<<<<<<<<<
		String[] musts = StringUtil.split(must);
		List<String> mustList = musts == null ? new ArrayList<String>() : Arrays.asList(musts);
		for (String s : mustList) {
			if (real.get(s) == null) {  // 可能传null进来，这里还会通过 real.containsKey(s) == false) {
				throw new IllegalArgumentException(method + "请求，" + name
						+ " 里面不能缺少 " + s + " 等[" + must + "]内的任何字段！");
			}
		}
		//判断必要字段是否都有>>>>>>>>>>>>>>>>>>>


		Set<String> objKeySet = new HashSet<String>(); //不能用tableKeySet，仅判断 Table:{} 会导致 key:{ Table:{} } 绕过判断

		//解析内容<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		Set<Entry<String, Object>> set = new LinkedHashSet<>(target.entrySet());
		if (set.isEmpty() == false) {

			String key;
			Object tvalue;
			Object rvalue;
			for (Entry<String, Object> entry : set) {
				key = entry == null ? null : entry.getKey();
				if (key == null || OPERATION_KEY_LIST.contains(key)) {
					continue;
				}
				tvalue = entry.getValue();
				rvalue = real.get(key);
				if (callback.onParse(key, tvalue, rvalue) == false) {
					continue;
				}

				if (tvalue instanceof JSONObject) { //JSONObject，往下一级提取
					if (rvalue != null && rvalue instanceof JSONObject == false) {
						throw new UnsupportedDataTypeException(key + ":value 的value不合法！类型必须是 OBJECT ，结构为 {} !");
					}
					tvalue = callback.onParseJSONObject(key, (JSONObject) tvalue, (JSONObject) rvalue);

					objKeySet.add(key);
				} else if (tvalue instanceof JSONArray) { //JSONArray
					if (rvalue != null && rvalue instanceof JSONArray == false) {
						throw new UnsupportedDataTypeException(key + ":value 的value不合法！类型必须是 ARRAY ，结构为 [] !");
					}
					tvalue = callback.onParseJSONArray(key, (JSONArray) tvalue, (JSONArray) rvalue);

					if ((method == RequestMethod.POST || method == RequestMethod.PUT) && JSONRequest.isArrayKey(key)) {
						objKeySet.add(key);
					}
				} else {//其它Object
					tvalue = callback.onParseObject(key, tvalue, rvalue);
				}

				if (tvalue != null) {//可以在target中加上一些不需要客户端传的键值对
					real.put(key, tvalue);
				}
			}

		}

		//解析内容>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



		Set<String> rkset = real.keySet(); //解析内容并没有改变rkset

		//解析不允许的字段<<<<<<<<<<<<<<<<<<<
		List<String> refuseList = new ArrayList<String>();
		if ("!".equals(refuse)) {//所有非 must，改成 !must 更好
			for (String key : rkset) {//对@key放行，@role,@column,自定义@position等
				if (key != null && key.startsWith("@") == false
						&& mustList.contains(key) == false && objKeySet.contains(key) == false) {
					refuseList.add(key);
				}
			}
		} else {
			String[] refuses = StringUtil.split(refuse);
			if (refuses != null && refuses.length > 0) {
				refuseList.addAll(Arrays.asList(refuses));
			}
		}
		//解析不允许的字段>>>>>>>>>>>>>>>>>>>


		//判断不允许传的key<<<<<<<<<<<<<<<<<<<<<<<<<
		for (String rk : rkset) {
			if (refuseList.contains(rk)) { //不允许的字段
				throw new IllegalArgumentException(method + "请求，" + name
						+ " 里面不允许传 " + rk + " 等" + StringUtil.getString(refuseList) + "内的任何字段！");
			}

			if (rk == null) { //无效的key
				real.remove(rk);
				continue;
			}

			Object rv = real.get(rk);

			//不允许传远程函数，只能后端配置
			if (rk.endsWith("()") && rv instanceof String) {
				throw new UnsupportedOperationException(method + " 请求，" + rk + " 不合法！非开放请求不允许传远程函数 key():\"fun()\" ！");
			}

			//不在target内的 key:{}
			if (rk.startsWith("@") == false && objKeySet.contains(rk) == false) {
				if (rv instanceof JSONObject) {
					throw new UnsupportedOperationException(method + " 请求，" +name + " 里面不允许传 " + rk + ":{} ！");
				}
				if ((method == RequestMethod.POST || method == RequestMethod.PUT) && rv instanceof JSONArray && JSONRequest.isArrayKey(rk)) {
					throw new UnsupportedOperationException(method + " 请求，" + name + " 里面不允许 " + rk + ":[] 等未定义的 Table[]:[{}] 批量操作键值对！");
				}
			}
		}
		//判断不允许传的key>>>>>>>>>>>>>>>>>>>>>>>>>



		//校验与修改Request<<<<<<<<<<<<<<<<<
		//在tableKeySet校验后操作，避免 导致put/add进去的Table 被当成原Request的内容
		real = operate(TYPE, type, real, creator);
		real = operate(VERIFY, verify, real, creator);
		real = operate(INSERT, insert, real, creator);
		real = operate(UPDATE, update, real, creator);
		real = operate(REPLACE, replace, real, creator);
		//校验与修改Request>>>>>>>>>>>>>>>>>


		String db = real.getString(apijson.JSONObject.KEY_DATABASE);
		String sh = real.getString(apijson.JSONObject.KEY_SCHEMA);
		String ds = real.getString(apijson.JSONObject.KEY_DATASOURCE);
		if (StringUtil.isEmpty(db, false)) {
			db = database;
		}
		if (StringUtil.isEmpty(sh, false)) {
			sh = schema;
		}
		if (StringUtil.isEmpty(ds, false)) {
			ds = datasource;
		}
		String idKey = idCallback == null ? null : idCallback.getIdKey(db, sh, name);
		String finalIdKey = StringUtil.isEmpty(idKey, false) ? apijson.JSONObject.KEY_ID : idKey;

		//TODO放在operate前？考虑性能、operate修改后再验证的值是否和原来一样
		//校验存在<<<<<<<<<<<<<<<<<<< TODO 格式改为 id;version,tag 兼容多个字段联合主键
		String[] exists = StringUtil.split(exist);
		if (exists != null && exists.length > 0) {
			long exceptId = real.getLongValue(finalIdKey);
			for (String e : exists) {
				verifyExist(name, e, real.get(e), exceptId, creator);
			}
		}
		//校验存在>>>>>>>>>>>>>>>>>>>

		//TODO放在operate前？考虑性能、operate修改后再验证的值是否和原来一样
		//校验重复<<<<<<<<<<<<<<<<<<< TODO 格式改为 id;version,tag 兼容多个字段联合主键
		String[] uniques = StringUtil.split(unique);
		if (uniques != null && uniques.length > 0) {
			long exceptId = real.getLongValue(finalIdKey);
			for (String u : uniques) {
				verifyRepeat(name, u, real.get(u), exceptId, finalIdKey, creator);
			}
		}
		//校验重复>>>>>>>>>>>>>>>>>>>


		Log.i(TAG, "parse  return real = " + JSON.toJSONString(real));
		return real;
	}



	/**执行操作
	 * @param opt
	 * @param targetChild
	 * @param real
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	private static JSONObject operate(Operation opt, JSONObject targetChild, JSONObject real, SQLCreator creator) throws Exception {
		if (targetChild == null) {
			return real;
		}
		if (real == null) {
			throw new IllegalArgumentException("operate  real == null!!!");
		}


		Set<Entry<String, Object>> set = new LinkedHashSet<>(targetChild.entrySet());
		String tk;
		Object tv;

		for (Entry<String, Object> e : set) {
			tk = e == null ? null : e.getKey();
			if (tk == null || OPERATION_KEY_LIST.contains(tk)) {
				continue;
			}

			tv = e.getValue();

			if (opt == TYPE) {
				verifyType(tk, tv, real);
			}
			else if (opt == VERIFY) {
				verifyValue(tk, tv, real, creator);
			}
			else if (opt == UPDATE) {
				real.put(tk, tv);
			}
			else {
				if (real.containsKey(tk)) {
					if (opt == REPLACE) {
						real.put(tk, tv);
					}
				}
				else {
					if (opt == INSERT) {
						real.put(tk, tv);
					}
				}
			}
		}

		return real;
	}


	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param real
	 * @throws Exception
	 */
	public static void verifyType(@NotNull String tk, Object tv, @NotNull JSONObject real) throws UnsupportedDataTypeException {
		if (tv instanceof String == false) {
			throw new UnsupportedDataTypeException("服务器内部错误，" + tk + ":value 的value不合法！"
					+ "Request表校验规则中 TYPE:{ key:value } 中的value只能是String类型！");
		}

		verifyType(tk, (String) tv, real.get(tk));
	}
	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param rv
	 * @throws Exception
	 */
	public static void verifyType(@NotNull String tk, @NotNull String tv, Object rv) throws UnsupportedDataTypeException {
		verifyType(tk, tv, rv, false);
	}
	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param rv
	 * @param isInArray
	 * @throws Exception
	 */
	public static void verifyType(@NotNull String tk, @NotNull String tv, Object rv, boolean isInArray) throws UnsupportedDataTypeException {
		if (rv == null) {
			return;
		}

		if (tv.endsWith("[]")) {

			verifyType(tk, "ARRAY", rv);

			for (Object o : (Collection<?>) rv) {
				verifyType(tk, tv.substring(0, tv.length() - 2), o, true);
			}

			return;
		}

		//这里不抽取 enum，因为 enum 不能满足扩展需求，子类需要可以自定义，而且 URL[] 这种也不符合命名要求，得用 constructor + getter + setter
		switch (tv) {
		case "BOOLEAN": //Boolean.parseBoolean(real.getString(tk)); 只会判断null和true
			if (rv instanceof Boolean == false) { //JSONObject.getBoolean 可转换Number类型
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 BOOLEAN" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "NUMBER": //整数
			try {
				Long.parseLong(rv.toString()); //1.23会转换为1  real.getLong(tk);
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 NUMBER" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "DECIMAL": //小数
			try {
				Double.parseDouble(rv.toString());
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 DECIMAL" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "STRING":
			if (rv instanceof String == false) { //JSONObject.getString 可转换任何类型
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 STRING" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "URL": //网址，格式为 http://www.apijson.org, https://www.google.com 等
			try {
				new URL((String) rv);
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 URL" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "DATE": //日期，格式为 YYYY-MM-DD（例如 2020-02-20）的 STRING
			try {
				LocalDate.parse((String) rv);
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是格式为 YYYY-MM-DD（例如 2020-02-20）的 DATE" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "TIME": //时间，格式为 HH:mm:ss（例如 12:01:30）的 STRING
			try {
				LocalTime.parse((String) rv);
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是格式为 HH:mm:ss（例如 12:01:30）的 TIME" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "DATETIME": //日期+时间，格式为 YYYY-MM-DDTHH:mm:ss（例如 2020-02-20T12:01:30）的 STRING
			try {
				LocalDateTime.parse((String) rv);
			} catch (Exception e) {
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是格式为 YYYY-MM-DDTHH:mm:ss（例如 2020-02-20T12:01:30）的 DATETIME" + (isInArray ? "[] !" : " !"));
			}
			break;
		case "OBJECT":
			if (rv instanceof Map == false) { //JSONObject.getJSONObject 可转换String类型
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 OBJECT" + (isInArray ? "[] !" : " !") + " OBJECT 结构为 {} !");
			}
			break;
		case "ARRAY":
			if (rv instanceof Collection == false) { //JSONObject.getJSONArray 可转换String类型
				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 ARRAY" + (isInArray ? "[] !" : " !") + " ARRAY 结构为 [] !");
			}
			break;
			//目前在业务表中还用不上，单一的类型校验已经够用
			//		case "JSON":
			//			try {
			//				com.alibaba.fastjson.JSON.parse(rv.toString());
			//			} catch (Exception e) {
			//				throw new UnsupportedDataTypeException(tk + ":value 的value不合法！类型必须是 JSON ！"
			//						+ "也就是 {Object}, [Array] 或 它们对应的字符串 '{Object}', '[Array]' 4种中的一个 !");
			//			}
			//			break;
		default:
			throw new UnsupportedDataTypeException(
					"服务器内部错误，类型 " + tv + " 不合法！Request表校验规则中 TYPE:{ key:value } 中的 value 必须是"
							+ " [ BOOLEAN, NUMBER, DECIMAL, STRING, URL, DATE, TIME, DATETIME, OBJECT, ARRAY ] 或它们的数组"
							+ " [ BOOLEAN[], NUMBER[], DECIMAL[], STRING[], URL[], DATE[], TIME[], DATETIME[], OBJECT[], ARRAY[] ] 中的一个!");
		}

	}


	/**验证值
	 * @param tk
	 * @param tv
	 * @param real
	 * @param creator
	 * @throws Exception
	 */
	private static void verifyValue(@NotNull String tk, @NotNull Object tv, @NotNull JSONObject real, SQLCreator creator) throws Exception {
		if (tv == null) {
			throw new IllegalArgumentException("operate  operate == VERIFY " + tk + ":" + tv + " ,  >> tv == null!!!");
		}

		String rk;
		Object rv;
		Logic logic;
		if (tk.endsWith("$")) {  // 模糊搜索
			verifyCondition("$", real, tk, tv, creator);
		}
		else if (tk.endsWith("~")) {  // 正则匹配
			logic = new Logic(tk.substring(0, tk.length() - 1));
			rk = logic.getKey();
			rv = real.get(rk);
			if (rv == null) {
				return;
			}

			JSONArray array = AbstractSQLConfig.newJSONArray(tv);

			boolean m;
			boolean isOr = false;
			Pattern reg;
			for (Object r : array) {
				if (r instanceof String == false) {
					throw new UnsupportedDataTypeException(rk + ":" + rv + " 中value只支持 String 或 [String] 类型！");
				}
				reg = COMPILE_MAP.get(r);
				if (reg == null) {
					reg = Pattern.compile((String) r);
				}
				m = reg.matcher("" + rv).matches();
				if (m) {
					if (logic.isNot()) {
						throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
					}
					if (logic.isOr()) {
						isOr = true;
						break;
					}
				} else {
					if (logic.isAnd()) {
						throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
					}
				}
			}

			if (isOr == false && logic.isOr()) {
				throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
			}
		}
		else if (tk.endsWith("{}")) { //rv符合tv条件或在tv内
			if (tv instanceof String) {//TODO  >= 0, < 10
				verifyCondition("{}", real, tk, tv, creator);
			}
			else if (tv instanceof JSONArray) {
				logic = new Logic(tk.substring(0, tk.length() - 2));
				rk = logic.getKey();
				rv = real.get(rk);
				if (rv == null) {
					return;
				}

				if (((JSONArray) tv).contains(rv) == logic.isNot()) {
					throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
				}
			}
			else {
				throw new UnsupportedDataTypeException("服务器Request表verify配置错误！");
			}
		}
		else if (tk.endsWith("<>")) { //rv包含tv内的值
			logic = new Logic(tk.substring(0, tk.length() - 2));
			rk = logic.getKey();
			rv = real.get(rk);
			if (rv == null) {
				return;
			}

			if (rv instanceof JSONArray == false) {
				throw new UnsupportedDataTypeException("服务器Request表verify配置错误！");
			}

			JSONArray array = AbstractSQLConfig.newJSONArray(tv);

			boolean isOr = false;
			for (Object o : array) {
				if (((JSONArray) rv).contains(o)) {
					if (logic.isNot()) {
						throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
					}
					if (logic.isOr()) {
						isOr = true;
						break;
					}
				} else {
					if (logic.isAnd()) {
						throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
					}
				}
			}

			if (isOr == false && logic.isOr()) {
				throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 " + tk + ":" + tv + " !");
			}
		}
		else {
			throw new IllegalArgumentException("服务器Request表verify配置错误！");
		}
	}

	/**通过数据库执行SQL语句来验证条件
	 * @param funChar
	 * @param real
	 * @param tk
	 * @param tv
	 * @param creator
	 * @throws Exception
	 */
	private static void verifyCondition(@NotNull String funChar, @NotNull JSONObject real, @NotNull String tk, @NotNull Object tv
			, @NotNull SQLCreator creator) throws Exception {
		//不能用Parser, 0 这种不符合 StringUtil.isName !
		Logic logic = new Logic(tk.substring(0, tk.length() - funChar.length()));
		String rk = logic.getKey();
		Object rv = real.get(rk);
		if (rv == null) {
			return;
		}

		if (rv instanceof String && ((String) rv).contains("'")) {  // || key.contains("#") || key.contains("--")) {
			throw new IllegalArgumentException(rk + ":value 中value不合法！value 中不允许有单引号 ' ！");
		}

		SQLConfig config = creator.createSQLConfig().setMethod(RequestMethod.GET).setCount(1).setPage(0);
		config.setTest(true);
		//		config.setTable(Test.class.getSimpleName());
		//		config.setColumn(rv + logic.getChar() + funChar)
		config.putWhere(rv + logic.getChar() + funChar, tv, false);  // 字符串可能 SQL 注入，目前的解决方式是加 TYPE 校验类型或者干脆不用 sqlVerify，而是通过远程函数来校验
		config.setCount(1);

		SQLExecutor executor = creator.createSQLExecutor();
		JSONObject result = null;
		try {
			result = executor.execute(config, false);
		} finally {
			executor.close();
		}
		if (result != null && JSONResponse.isExist(result.getIntValue(JSONResponse.KEY_CODE)) == false) {
			throw new IllegalArgumentException(rk + ":value 中value不合法！必须匹配 '" + tk + "': '" + tv + "' ！");
		}
	}


	/**验证是否存在
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void verifyExist(String table, String key, Object value, long exceptId, @NotNull SQLCreator creator) throws Exception {
		if (key == null || value == null) {
			Log.e(TAG, "verifyExist  key == null || value == null >> return;");
			return;
		}
		if (value instanceof JSON) {
			throw new UnsupportedDataTypeException(key + ":value 中value的类型不能为JSON！");
		}


		SQLConfig config = creator.createSQLConfig().setMethod(RequestMethod.HEAD).setCount(1).setPage(0);
		config.setTable(table);
		config.putWhere(key, value, false);

		SQLExecutor executor = creator.createSQLExecutor();
		try {
			JSONObject result = executor.execute(config, false);
			if (result == null) {
				throw new Exception("服务器内部错误  verifyExist  result == null");
			}
			if (result.getIntValue(JSONResponse.KEY_COUNT) <= 0) {
				throw new ConflictException(key + ": " + value + " 不存在！如果必要请先创建！");
			}
		} finally {
			executor.close();
		}
	}

	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void verifyRepeat(String table, String key, Object value, @NotNull SQLCreator creator) throws Exception {
		verifyRepeat(table, key, value, 0, creator);
	}

	/**验证是否重复
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @throws Exception
	 */
	public static void verifyRepeat(String table, String key, Object value, long exceptId, @NotNull SQLCreator creator) throws Exception {
		verifyRepeat(table, key, value, exceptId, null, creator);
	}

	/**验证是否重复
	 * TODO 与 AbstractVerifier.verifyRepeat 代码重复，需要简化
	 * @param table
	 * @param key
	 * @param value
	 * @param exceptId 不包含id
	 * @param idKey
	 * @param creator
	 * @throws Exception
	 */
	public static void verifyRepeat(String table, String key, Object value, long exceptId, String idKey, @NotNull SQLCreator creator) throws Exception {
		if (key == null || value == null) {
			Log.e(TAG, "verifyRepeat  key == null || value == null >> return;");
			return;
		}
		if (value instanceof JSON) {
			throw new UnsupportedDataTypeException(key + ":value 中value的类型不能为JSON！");
		}

		String finalIdKey = StringUtil.isEmpty(idKey, false) ? apijson.JSONObject.KEY_ID : idKey;

		SQLConfig config = creator.createSQLConfig().setMethod(RequestMethod.GET).setCount(1).setPage(0);
		config.setTable(table);
		if (exceptId > 0) { //允许修改自己的属性为该属性原来的值
			config.putWhere(finalIdKey + "!", exceptId, false);
		}
		config.putWhere(key, value, false);

		SQLExecutor executor = creator.createSQLExecutor();
		try {
			JSONObject result = executor.execute(config, false);
			if (result == null) {
				throw new Exception("服务器内部错误  verifyRepeat  result == null");
			}
			if (result.getIntValue(JSONResponse.KEY_CODE) > 0) {
				throw new ConflictException(key + ": " + value + " 已经存在，不能重复！");
			}
		} finally {
			executor.close();
		}
	}

	public static String getCacheKeyForRequest(String method, String tag) {
		return method + " " + tag;
	}


}
