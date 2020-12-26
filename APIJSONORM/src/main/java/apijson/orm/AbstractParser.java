/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import static apijson.JSONObject.KEY_EXPLAIN;
import static apijson.RequestMethod.GET;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.RequestRole;
import apijson.StringUtil;
import apijson.orm.exception.ConditionErrorException;
import apijson.orm.exception.ConflictException;
import apijson.orm.exception.NotExistException;
import apijson.orm.exception.NotLoggedInException;
import apijson.orm.exception.OutOfRangeException;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public abstract class AbstractParser<T> implements Parser<T>, ParserCreator<T>, VerifierCreator<T>, SQLCreator {
	protected static final String TAG = "AbstractParser";


	/**
	 * method = null
	 */
	public AbstractParser() {
		this(null);
	}
	/**needVerify = true
	 * @param requestMethod null ? requestMethod = GET
	 */
	public AbstractParser(RequestMethod method) {
		this(method, true);
	}
	/**
	 * @param requestMethod null ? requestMethod = GET
	 * @param needVerify 仅限于为服务端提供方法免验证特权，普通请求不要设置为 false ！ 如果对应Table有权限也建议用默认值 true，保持和客户端权限一致
	 */
	public AbstractParser(RequestMethod method, boolean needVerify) {
		super();
		setMethod(method);
		setNeedVerify(needVerify);
	}

	@NotNull
	protected Visitor<T> visitor;
	@NotNull
	@Override
	public Visitor<T> getVisitor() {
		if (visitor == null) {
			visitor = new Visitor<T>() {

				@Override
				public T getId() {
					return null;
				}

				@Override
				public List<T> getContactIdList() {
					return null;
				}
			};
		}
		return visitor;
	}
	@Override
	public AbstractParser<T> setVisitor(@NotNull Visitor<T> visitor) {
		this.visitor = visitor;
		return this;
	}

	protected RequestMethod requestMethod;
	@NotNull
	@Override
	public RequestMethod getMethod() {
		return requestMethod;
	}
	@NotNull
	@Override
	public AbstractParser<T> setMethod(RequestMethod method) {
		this.requestMethod = method == null ? GET : method;
		this.transactionIsolation = RequestMethod.isQueryMethod(method) ? Connection.TRANSACTION_NONE : Connection.TRANSACTION_REPEATABLE_READ;
		return this;
	}

	protected int version;
	@Override
	public int getVersion() {
		return version;
	}
	@Override
	public AbstractParser<T> setVersion(int version) {
		this.version = version;
		return this;
	}

	protected String tag;
	@Override
	public String getTag() {
		return tag;
	}
	@Override
	public AbstractParser<T> setTag(String tag) {
		this.tag = tag;
		return this;
	}

	protected JSONObject requestObject;
	@Override
	public JSONObject getRequest() {
		return requestObject;
	}
	@Override
	public AbstractParser<T> setRequest(JSONObject request) {
		this.requestObject = request;
		return this;
	}

	protected Boolean globleFormat;
	public AbstractParser<T> setGlobleFormat(Boolean globleFormat) {
		this.globleFormat = globleFormat;
		return this;
	}
	@Override
	public Boolean getGlobleFormat() {
		return globleFormat;
	}
	protected RequestRole globleRole;
	public AbstractParser<T> setGlobleRole(RequestRole globleRole) {
		this.globleRole = globleRole;
		return this;
	}
	@Override
	public RequestRole getGlobleRole() {
		return globleRole;
	}
	protected String globleDatabase;
	public AbstractParser<T> setGlobleDatabase(String globleDatabase) {
		this.globleDatabase = globleDatabase;
		return this;
	}
	@Override
	public String getGlobleDatabase() {
		return globleDatabase;
	}
	protected String globleSchema;
	public AbstractParser<T> setGlobleSchema(String globleSchema) {
		this.globleSchema = globleSchema;
		return this;
	}
	@Override
	public String getGlobleSchema() {
		return globleSchema;
	}
	protected Boolean globleExplain;
	public AbstractParser<T> setGlobleExplain(Boolean globleExplain) {
		this.globleExplain = globleExplain;
		return this;
	}
	@Override
	public Boolean getGlobleExplain() {
		return globleExplain;
	}
	protected String globleCache;
	public AbstractParser<T> setGlobleCache(String globleCache) {
		this.globleCache = globleCache;
		return this;
	}
	@Override
	public String getGlobleCache() {
		return globleCache;
	}

	@Override
	public AbstractParser<T> setNeedVerify(boolean needVerify) {
		setNeedVerifyLogin(needVerify);
		setNeedVerifyRole(needVerify);
		setNeedVerifyContent(needVerify);
		return this;
	}

	protected boolean needVerifyLogin;
	@Override
	public boolean isNeedVerifyLogin() {
		return needVerifyLogin;
	}
	@Override
	public AbstractParser<T> setNeedVerifyLogin(boolean needVerifyLogin) {
		this.needVerifyLogin = needVerifyLogin;
		return this;
	}
	protected boolean needVerifyRole;
	@Override
	public boolean isNeedVerifyRole() {
		return needVerifyRole;
	}
	@Override
	public AbstractParser<T> setNeedVerifyRole(boolean needVerifyRole) {
		this.needVerifyRole = needVerifyRole;
		return this;
	}
	protected boolean needVerifyContent;
	@Override
	public boolean isNeedVerifyContent() {
		return needVerifyContent;
	}
	@Override
	public AbstractParser<T> setNeedVerifyContent(boolean needVerifyContent) {
		this.needVerifyContent = needVerifyContent;
		return this;
	}





	protected SQLExecutor sqlExecutor;
	protected Verifier<T> verifier;
	protected Map<String, Object> queryResultMap;//path-result

	@Override
	public SQLExecutor getSQLExecutor() {
		if (sqlExecutor == null) {
			sqlExecutor = createSQLExecutor();
		}
		return sqlExecutor;
	}
	@Override
	public Verifier<T> getVerifier() {
		if (verifier == null) {
			verifier = createVerifier().setVisitor(getVisitor());
		}
		return verifier;
	}

	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	@Override
	public String parse(String request) {
		return JSON.toJSONString(parseResponse(request));
	}
	/**解析请求json并获取对应结果
	 * @param request
	 * @return
	 */
	@NotNull
	@Override
	public String parse(JSONObject request) {
		return JSON.toJSONString(parseResponse(request));
	}

	/**解析请求json并获取对应结果
	 * @param request 先parseRequest中URLDecoder.decode(request, UTF_8);再parseResponse(getCorrectRequest(...))
	 * @return parseResponse(requestObject);
	 */
	@NotNull
	@Override
	public JSONObject parseResponse(String request) {
		Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"
				+ requestMethod + "/parseResponse  request = \n" + request + "\n\n");

		try {
			requestObject = parseRequest(request);
		} catch (Exception e) {
			return newErrorResult(e);
		}

		return parseResponse(requestObject);
	}

	private int queryDepth;

	/**解析请求json并获取对应结果
	 * @param request
	 * @return requestObject
	 */
	@NotNull
	@Override
	public JSONObject parseResponse(JSONObject request) {
		long startTime = System.currentTimeMillis();
		Log.d(TAG, "parseResponse  startTime = " + startTime
				+ "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n\n\n ");

		requestObject = request;

		verifier = createVerifier().setVisitor(getVisitor());

		if (RequestMethod.isPublicMethod(requestMethod) == false) {
			try {
				if (isNeedVerifyLogin()) {
					onVerifyLogin();
				}
				if (isNeedVerifyContent()) {
					onVerifyContent();
				}
			} catch (Exception e) {
				return extendErrorResult(requestObject, e);
			}
		}

		//必须在parseCorrectRequest后面，因为parseCorrectRequest可能会添加 @role
		if (isNeedVerifyRole() && globleRole == null) {
			try {
				setGlobleRole(RequestRole.get(requestObject.getString(JSONRequest.KEY_ROLE)));
				requestObject.remove(JSONRequest.KEY_ROLE);
			} catch (Exception e) {
				return extendErrorResult(requestObject, e);
			}
		}

		try {
			setGlobleFormat(requestObject.getBoolean(JSONRequest.KEY_FORMAT));
			setGlobleDatabase(requestObject.getString(JSONRequest.KEY_DATABASE));
			setGlobleSchema(requestObject.getString(JSONRequest.KEY_SCHEMA));
			setGlobleExplain(requestObject.getBoolean(JSONRequest.KEY_EXPLAIN));
			setGlobleCache(requestObject.getString(JSONRequest.KEY_CACHE));

			requestObject.remove(JSONRequest.KEY_FORMAT);
			requestObject.remove(JSONRequest.KEY_DATABASE);
			requestObject.remove(JSONRequest.KEY_SCHEMA);
			requestObject.remove(JSONRequest.KEY_EXPLAIN);
			requestObject.remove(JSONRequest.KEY_CACHE);
		} catch (Exception e) {
			return extendErrorResult(requestObject, e);
		}

		final String requestString = JSON.toJSONString(request);//request传进去解析后已经变了


		queryResultMap = new HashMap<String, Object>();

		Exception error = null;
		sqlExecutor = createSQLExecutor();
		onBegin();
		try {
			queryDepth = 0;
			requestObject = onObjectParse(request, null, null, null, false);

			onCommit();
		}
		catch (Exception e) {
			e.printStackTrace();
			error = e;

			onRollback();
		}

		requestObject = error == null ? extendSuccessResult(requestObject) : extendErrorResult(requestObject, error);

		JSONObject res = (globleFormat != null && globleFormat) && JSONResponse.isSuccess(requestObject) ? new JSONResponse(requestObject) : requestObject;

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		if (Log.DEBUG) { //用 | 替代 /，避免 APIJSON ORM，APIAuto 等解析路径错误
			requestObject.put("sql:generate|cache|execute|maxExecute", getSQLExecutor().getGeneratedSQLCount() + "|" + getSQLExecutor().getCachedSQLCount() + "|" + getSQLExecutor().getExecutedSQLCount() + "|" + getMaxSQLCount());
			requestObject.put("depth:count|max", queryDepth + "|" + getMaxQueryDepth());
			requestObject.put("time:start|duration|end", startTime + "|" + duration + "|" + endTime);
			if (error != null) {
				requestObject.put("throw", error.getClass().getName());
				requestObject.put("trace", error.getStackTrace());
			}
		}

		onClose();

		//会不会导致原来的session = null？		session = null;

		if (Log.DEBUG) {
			Log.d(TAG, "\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n "
					+ requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");

			Log.d(TAG, "parseResponse  return response = \n" + JSON.toJSONString(requestObject)
			+ "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n\n\n");
		}
		Log.d(TAG, "parseResponse  endTime = " + endTime + ";  duration = " + duration
				+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");

		return res;
	}


	@Override
	public void onVerifyLogin() throws Exception {
		getVerifier().verifyLogin();
	}
	@Override
	public void onVerifyContent() throws Exception {
		requestObject = parseCorrectRequest();
	}
	/**校验角色及对应操作的权限
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public void onVerifyRole(@NotNull SQLConfig config) throws Exception {
		if (Log.DEBUG) {
			Log.i(TAG, "onVerifyRole  config = " + JSON.toJSONString(config));
		}

		if (isNeedVerifyRole()) {
			if (config.getRole() == null) {
				if (globleRole != null) {
					config.setRole(globleRole);
				} else {
					config.setRole(getVisitor().getId() == null ? RequestRole.UNKNOWN : RequestRole.LOGIN);
				}
			}
			getVerifier().verifyAccess(config);
		}

	}


	/**解析请求JSONObject
	 * @param request => URLDecoder.decode(request, UTF_8);
	 * @return
	 * @throws Exception 
	 */
	@NotNull
	public static JSONObject parseRequest(String request) throws Exception {
		JSONObject obj = JSON.parseObject(request);
		if (obj == null) {
			throw new UnsupportedEncodingException("JSON格式不合法！");
		}
		return obj;
	}

	@Override
	public JSONObject parseCorrectRequest(RequestMethod method, String tag, int version, String name, @NotNull JSONObject request
			, int maxUpdateCount, SQLCreator creator) throws Exception {
		
		if (RequestMethod.isPublicMethod(method)) {
			return request;//需要指定JSON结构的get请求可以改为post请求。一般只有对安全性要求高的才会指定，而这种情况用明文的GET方式几乎肯定不安全
		}

		if (StringUtil.isEmpty(tag, true)) {
			throw new IllegalArgumentException("请在最外层传 tag ！一般是 Table 名，例如 \"tag\": \"User\" ");
		}

		//获取指定的JSON结构 <<<<<<<<<<<<
		JSONObject object = null;
		String error = "";
		try {
			object = getStructure("Request", method.name(), tag, version);
		} catch (Exception e) {
			error = e.getMessage();
		}
		if (object == null) { //empty表示随意操作  || object.isEmpty()) {
			throw new UnsupportedOperationException("找不到 version: " + version + ", method: " + method.name() + ", tag: " + tag + " 对应的 structure ！"
					+ "非开放请求必须是后端 Request 表中校验规则允许的操作！\n " + error + "\n如果需要则在 Request 表中新增配置！");
		}

		JSONObject target = object;
		if (object.containsKey(tag) == false) { //tag 是 Table 名或 Table[]
			
			boolean isArrayKey = tag.endsWith(":[]");  //  JSONRequest.isArrayKey(tag);
			String key = isArrayKey ? tag.substring(0, tag.length() - 3) : tag;
			
			if (apijson.JSONObject.isTableKey(key)) {
				if (isArrayKey) { //自动为 tag = Comment:[] 的 { ... } 新增键值对 "Comment[]":[] 为 { "Comment[]":[], ... }
					target.put(key + "[]", new JSONArray()); 
				}
				else { //自动为 tag = Comment 的 { ... } 包一层为 { "Comment": { ... } }
					target = new JSONObject(true);
					target.put(tag, object);
				}
			}
		}
		
		//获取指定的JSON结构 >>>>>>>>>>>>>>

		
		//JSONObject clone 浅拷贝没用，Structure.parse 会导致 structure 里面被清空，第二次从缓存里取到的就是 {}
		return getVerifier().verifyRequest(method, name, target, request, maxUpdateCount, getGlobleDatabase(), getGlobleSchema(), creator);
	}
	
	
	/**新建带状态内容的JSONObject
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject newResult(int code, String msg) {
		return extendResult(null, code, msg);
	}
	/**添加JSONObject的状态内容，一般用于错误提示结果
	 * @param object
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject extendResult(JSONObject object, int code, String msg) {
		if (object == null) {
			object = new JSONObject(true);
		}
		if (object.containsKey(JSONResponse.KEY_OK) == false) {
			object.put(JSONResponse.KEY_OK, JSONResponse.isSuccess(code));
		}
		if (object.containsKey(JSONResponse.KEY_CODE) == false) {
			object.put(JSONResponse.KEY_CODE, code);
		}
		
		String m = StringUtil.getString(object.getString(JSONResponse.KEY_MSG));
		if (m.isEmpty() == false) {
			msg = m + " ;\n " + StringUtil.getString(msg);
		}
		object.put(JSONResponse.KEY_MSG, msg);
		return object;
	}


	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object) {
		return extendResult(object, JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED);
	}
	/**获取请求成功的状态内容
	 * @return
	 */
	public static JSONObject newSuccessResult() {
		return newResult(JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED);
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Exception e) {
		JSONObject error = newErrorResult(e);
		return extendResult(object, error.getIntValue(JSONResponse.KEY_CODE), error.getString(JSONResponse.KEY_MSG));
	}
	/**新建错误状态内容
	 * @param e
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e) {
		if (e != null) {
			e.printStackTrace();

			int code;
			if (e instanceof UnsupportedEncodingException) {
				code = JSONResponse.CODE_UNSUPPORTED_ENCODING;
			} 
			else if (e instanceof IllegalAccessException) {
				code = JSONResponse.CODE_ILLEGAL_ACCESS;
			}
			else if (e instanceof UnsupportedOperationException) {
				code = JSONResponse.CODE_UNSUPPORTED_OPERATION;
			}
			else if (e instanceof NotExistException) {
				code = JSONResponse.CODE_NOT_FOUND;
			}
			else if (e instanceof IllegalArgumentException) {
				code = JSONResponse.CODE_ILLEGAL_ARGUMENT;
			}
			else if (e instanceof NotLoggedInException) {
				code = JSONResponse.CODE_NOT_LOGGED_IN;
			}
			else if (e instanceof TimeoutException) {
				code = JSONResponse.CODE_TIME_OUT;
			} 
			else if (e instanceof ConflictException) {
				code = JSONResponse.CODE_CONFLICT;
			}
			else if (e instanceof ConditionErrorException) {
				code = JSONResponse.CODE_CONDITION_ERROR;
			}
			else if (e instanceof UnsupportedDataTypeException) {
				code = JSONResponse.CODE_UNSUPPORTED_TYPE;
			}
			else if (e instanceof OutOfRangeException) {
				code = JSONResponse.CODE_OUT_OF_RANGE;
			}
			else if (e instanceof NullPointerException) {
				code = JSONResponse.CODE_NULL_POINTER;
			}
			else {
				code = JSONResponse.CODE_SERVER_ERROR;
			}

			return newResult(code, e.getMessage());
		}

		return newResult(JSONResponse.CODE_SERVER_ERROR, JSONResponse.MSG_SERVER_ERROR);
	}




	//TODO 启动时一次性加载Request所有内容，作为初始化。
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @param method
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject parseCorrectRequest() throws Exception {
		setTag(requestObject.getString(JSONRequest.KEY_TAG));
		setVersion(requestObject.getIntValue(JSONRequest.KEY_VERSION));
		requestObject.remove(JSONRequest.KEY_TAG);
		requestObject.remove(JSONRequest.KEY_VERSION);
		return parseCorrectRequest(requestMethod, tag, version, "", requestObject, getMaxUpdateCount(), this);
	}


	//TODO 优化性能！
	/**获取正确的返回结果
	 * @param method
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject parseCorrectResponse(String table, JSONObject response) throws Exception {
		//		Log.d(TAG, "getCorrectResponse  method = " + method + "; table = " + table);
		//		if (response == null || response.isEmpty()) {//避免无效空result:{}添加内容后变有效
		//			Log.e(TAG, "getCorrectResponse  response == null || response.isEmpty() >> return response;");
		return response;
		//		}
		//
		//		JSONObject target = apijson.JSONObject.isTableKey(table) == false
		//				? new JSONObject() : getStructure(method, "Response", "model", table);
		//
		//				return MethodStructure.parseResponse(method, table, target, response, new OnParseCallback() {
		//
		//					@Override
		//					protected JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
		//						return getCorrectResponse(method, key, robj);
		//					}
		//				});
	}

	/**获取Request或Response内指定JSON结构
	 * @param table
	 * @param method
	 * @param tag
	 * @param version
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getStructure(@NotNull String table, String method, String tag, int version) throws Exception  {
		// TODO 目前只使用 Request 而不使用 Response，所以这里写死用 REQUEST_MAP，以后可能 Response 表也会与 Request 表合并，用字段来区分
		String cacheKey = AbstractVerifier.getCacheKeyForRequest(method, tag);
		SortedMap<Integer, JSONObject> versionedMap = AbstractVerifier.REQUEST_MAP.get(cacheKey);
		
		JSONObject result = versionedMap == null ? null : versionedMap.get(Integer.valueOf(version));
		if (result == null) {  // version <= 0 时使用最新，version > 0 时使用 > version 的最接近版本（最小版本）
			Set<Entry<Integer, JSONObject>> set = versionedMap == null ? null : versionedMap.entrySet();
			
			if (set != null && set.isEmpty() == false) {
				Entry<Integer, JSONObject> maxEntry = null;
				
				for (Entry<Integer, JSONObject> entry : set) {
					if (entry == null || entry.getKey() == null || entry.getValue() == null) {
						continue;
					}

					if (version <= 0 || version == entry.getKey()) {  // 这里应该不会出现相等，因为上面 versionedMap.get(Integer.valueOf(version))
						maxEntry = entry;
						break;
					}

					if (entry.getKey() < version) {
						break;
					}
					
					maxEntry = entry;
				}
				
				result = maxEntry == null ? null : maxEntry.getValue();
			}
			
			if (result != null) {  // 加快下次查询，查到值的话组合情况其实是有限的，不属于恶意请求
				if (versionedMap == null) {
					versionedMap = new TreeMap<>(new Comparator<Integer>() {

						@Override
						public int compare(Integer o1, Integer o2) {
							return o2 == null ? -1 : o2.compareTo(o1);  // 降序
						}
					});
				}
				
				versionedMap.put(Integer.valueOf(version), result);
				AbstractVerifier.REQUEST_MAP.put(cacheKey, versionedMap);
			}
		}
		
		if (result == null) {
			if (AbstractVerifier.REQUEST_MAP.isEmpty() == false) {
				return null;  // 已使用 REQUEST_MAP 缓存全部，但没查到
			}
			
			//获取指定的JSON结构 <<<<<<<<<<<<<<
			SQLConfig config = createSQLConfig().setMethod(GET).setTable(table);
			config.setPrepared(false);
			config.setColumn(Arrays.asList("structure"));

			Map<String, Object> where = new HashMap<String, Object>();
			where.put("method", method);
			where.put(JSONRequest.KEY_TAG, tag);
			
			if (version > 0) {
				where.put(JSONRequest.KEY_VERSION + "{}", ">=" + version);
			}
			config.setWhere(where);
			config.setOrder(JSONRequest.KEY_VERSION + (version > 0 ? "+" : "-"));
			config.setCount(1);

			//too many connections error: 不try-catch，可以让客户端看到是服务器内部异常
			result = getSQLExecutor().execute(config, false);
			
			// version, method, tag 组合情况太多了，JDK 里又没有 LRUCache，所以要么启动时一次性缓存全部后面只用缓存，要么每次都查数据库
			//			versionedMap.put(Integer.valueOf(version), result);
			//			AbstractVerifier.REQUEST_MAP.put(cacheKey, versionedMap);
		}
		
		return getJSONObject(result, "structure"); //解决返回值套了一层 "structure":{}
	}



	//	protected SQLConfig itemConfig;
	/**获取单个对象，该对象处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @param config for array item
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JSONObject onObjectParse(final JSONObject request
			, String parentPath, String name, final SQLConfig arrayConfig, boolean isSubquery) throws Exception {

		if (Log.DEBUG) {
			Log.i(TAG, "\ngetObject:  parentPath = " + parentPath
					+ ";\n name = " + name + "; request = " + JSON.toJSONString(request));
		}
		if (request == null) {// Moment:{}   || request.isEmpty()) {//key-value条件
			return null;
		}

		int type = arrayConfig == null ? 0 : arrayConfig.getType();

		String[] arr = StringUtil.split(parentPath, "/");
		if (arrayConfig == null || arrayConfig.getPosition() == 0) {
			int d = arr == null ? 1 : arr.length + 1;
			if (queryDepth < d) {
				queryDepth = d;
				int maxQueryDepth = getMaxQueryDepth();
				if (queryDepth > maxQueryDepth) {
					throw new IllegalArgumentException(parentPath + "/" + name + ":{} 的深度(或者说层级) 为 " + queryDepth + " 已超限，必须在 1-" + maxQueryDepth + " 内 !");
				}
			}
		}

		ObjectParser op = createObjectParser(request, parentPath, name, arrayConfig, isSubquery).parse();


		JSONObject response = null;
		if (op != null) {//TODO SQL查询结果为空时，functionMap和customMap还有没有意义？
			if (arrayConfig == null) {//Common
				response = op.setSQLConfig().executeSQL().response();
			}
			else {//Array Item Child
				int query = arrayConfig.getQuery();

				//total 这里不能用arrayConfig.getType()，因为在createObjectParser.onChildParse传到onObjectParse时已被改掉
				if (type == SQLConfig.TYPE_ITEM_CHILD_0 && query != JSONRequest.QUERY_TABLE
						&& arrayConfig.getPosition() == 0) {
					JSONObject rp = op.setMethod(RequestMethod.HEAD).setSQLConfig().executeSQL().getSqlReponse();
					if (rp != null) {
						int index = parentPath.lastIndexOf("]/");
						if (index >= 0) {
							int total = rp.getIntValue(JSONResponse.KEY_COUNT);
							
							String pathPrefix = parentPath.substring(0, index) + "]/";
							putQueryResult(pathPrefix + JSONResponse.KEY_TOTAL, total);
							
							//详细的分页信息，主要为 PC 端提供
							int count = arrayConfig.getCount();
							int page = arrayConfig.getPage();
							int max = (int) ((total - 1)/count);
							if (max < 0) {
								max = 0;
							}
							
							JSONObject pagination = new JSONObject(true);
							pagination.put(JSONResponse.KEY_TOTAL, total);
							pagination.put(JSONRequest.KEY_COUNT, count);
							pagination.put(JSONRequest.KEY_PAGE, page);
							pagination.put(JSONResponse.KEY_MAX, max);
							pagination.put(JSONResponse.KEY_MORE, page < max);
							pagination.put(JSONResponse.KEY_FIRST, page == 0);
							pagination.put(JSONResponse.KEY_LAST, page == max);
							putQueryResult(pathPrefix + JSONResponse.KEY_INFO, pagination);

							if (total <= count*page) {
								query = JSONRequest.QUERY_TOTAL;//数量不够了，不再往后查询
							}
						}
					}

					op.setMethod(requestMethod);
				}

				//Table
				if (query == JSONRequest.QUERY_TOTAL) {
					response = null;//不再往后查询
				} else {
					response = op
							.setSQLConfig(arrayConfig.getCount(), arrayConfig.getPage(), arrayConfig.getPosition())
							.executeSQL()
							.response();
					//					itemConfig = op.getConfig();
				}
			}

			op.recycle();
			op = null;
		}

		return response;
	}

	/**获取对象数组，该对象数组处于parentObject内
	 * @param parentPath parentObject的路径
	 * @param name parentObject的key
	 * @param request parentObject的value
	 * @return 
	 * @throws Exception
	 */
	@Override
	public JSONArray onArrayParse(JSONObject request, String parentPath, String name, boolean isSubquery) throws Exception {
		if (Log.DEBUG) {
			Log.i(TAG, "\n\n\n onArrayParse parentPath = " + parentPath
					+ "; name = " + name + "; request = " + JSON.toJSONString(request));
		}

		//不能允许GETS，否则会被通过"[]":{"@role":"ADMIN"},"Table":{},"tag":"Table"绕过权限并能批量查询
		if (isSubquery == false && RequestMethod.isGetMethod(requestMethod, false) == false) {
			throw new UnsupportedOperationException("key[]:{}只支持GET方法！不允许传 " + name + ":{} ！");
		}
		if (request == null || request.isEmpty()) {//jsonKey-jsonValue条件
			return null;
		}
		String path = getAbsPath(parentPath, name);


		//不能改变，因为后面可能继续用到，导致1以上都改变 []:{0:{Comment[]:{0:{Comment:{}},1:{...},...}},1:{...},...}
		final String query = request.getString(JSONRequest.KEY_QUERY);
		final Integer count = request.getInteger(JSONRequest.KEY_COUNT); //TODO 如果不想用默认数量可以改成 getIntValue(JSONRequest.KEY_COUNT);
		final int page = request.getIntValue(JSONRequest.KEY_PAGE);
		final Object join = request.get(JSONRequest.KEY_JOIN);

		int query2;
		if (query == null) {
			query2 = JSONRequest.QUERY_TABLE;
		}
		else {
			switch (query) {
			case "0":
			case JSONRequest.QUERY_TABLE_STRING:
				query2 = JSONRequest.QUERY_TABLE;
				break;
			case "1":
			case JSONRequest.QUERY_TOTAL_STRING:
				query2 = JSONRequest.QUERY_TOTAL;
				break;
			case "2":
			case JSONRequest.QUERY_ALL_STRING:
				query2 = JSONRequest.QUERY_ALL;
				break;
			default:
				throw new IllegalArgumentException(path + "/" + JSONRequest.KEY_QUERY + ":value 中 value 的值不合法！必须在 [0,1,2] 或 [TABLE, TOTAL, ALL] 内 !");
			}
		}

		int maxPage = getMaxQueryPage();
		if (page < 0 || page > maxPage) {
			throw new IllegalArgumentException(path + "/" + JSONRequest.KEY_PAGE + ":value 中 value 的值不合法！必须在 0-" + maxPage + " 内 !");
		}

		//不用total限制数量了，只用中断机制，total只在query = 1,2的时候才获取
		int count2 = isSubquery || count != null ? (count == null ? 0 : count) : getDefaultQueryCount();
		int max = isSubquery ? count2 : getMaxQueryCount();

		if (count2 < 0 || count2 > max) {
			throw new IllegalArgumentException(path + "/" + JSONRequest.KEY_COUNT + ":value 中 value 的值不合法！必须在 0-" + max + " 内 !");
		}

		request.remove(JSONRequest.KEY_QUERY);
		request.remove(JSONRequest.KEY_COUNT);
		request.remove(JSONRequest.KEY_PAGE);
		request.remove(JSONRequest.KEY_JOIN);
		Log.d(TAG, "onArrayParse  query = " + query + "; count = " + count + "; page = " + page + "; join = " + join);

		if (request.isEmpty()) {//如果条件成立，说明所有的 parentPath/name:request 中request都无效！！！
			Log.e(TAG, "onArrayParse  request.isEmpty() >> return null;");
			return null;
		}


		int size = count2 == 0 ? max : count2;//count为每页数量，size为第page页实际数量，max(size) = count
		Log.d(TAG, "onArrayParse  size = " + size + "; page = " + page);


		//key[]:{Table:{}}中key equals Table时 提取Table
		int index = isSubquery || name == null ? -1 : name.lastIndexOf("[]");
		String childPath = index <= 0 ? null : Pair.parseEntry(name.substring(0, index), true).getKey(); // Table-key1-key2...

		//判断第一个key，即Table是否存在，如果存在就提取
		String[] childKeys = StringUtil.split(childPath, "-", false);
		if (childKeys == null || childKeys.length <= 0 || request.containsKey(childKeys[0]) == false) {
			childKeys = null;
		}


		//Table<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONArray response = new JSONArray();
		SQLConfig config = createSQLConfig()
				.setMethod(requestMethod)
				.setCount(size)
				.setPage(page)
				.setQuery(query2)
				.setJoinList(onJoinParse(join, request));

		JSONObject parent;
		//生成size个
		for (int i = 0; i < (isSubquery ? 1 : size); i++) {
			parent = onObjectParse(request, isSubquery ? parentPath : path, isSubquery ? name : "" + i, config.setType(SQLConfig.TYPE_ITEM).setPosition(i), isSubquery);
			if (parent == null || parent.isEmpty()) {
				break;
			}
			//key[]:{Table:{}}中key equals Table时 提取Table
			response.add(getValue(parent, childKeys)); //null有意义
		}
		//Table>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		/*
		 * 支持引用取值后的数组
			{
			    "User-id[]": {
			        "User": {
			            "contactIdList<>": 82002
			        }
			    },
			    "Moment-userId[]": {
			        "Moment": {
			            "userId{}@": "User-id[]"
			        }
			    }
			}
		 */
		Object fo = childKeys == null || response.isEmpty() ? null : response.get(0);
		if (fo instanceof Boolean || fo instanceof Number || fo instanceof String) { //[{}] 和 [[]] 都没意义
			putQueryResult(path, response);
		}


		//后面还可能用到，要还原
		request.put(JSONRequest.KEY_QUERY, query);
		request.put(JSONRequest.KEY_COUNT, count);
		request.put(JSONRequest.KEY_PAGE, page);
		request.put(JSONRequest.KEY_JOIN, join);

		if (Log.DEBUG) {
			Log.i(TAG, "onArrayParse  return response = \n" + JSON.toJSONString(response) + "\n>>>>>>>>>>>>>>>\n\n\n");
		}
		return response;
	}

	/**多表同时筛选
	 * @param join "&/User/id@,</User[]/User/id{}@,</[]/Comment/momentId@"
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	private List<Join> onJoinParse(Object join, JSONObject request) throws Exception {
		JSONObject joinMap = null;

		if (join instanceof JSONObject) {
			joinMap = (JSONObject) join;
		}
		else if (join instanceof String) {
			String[] sArr = request == null || request.isEmpty() ? null : StringUtil.split((String) join);
			if (sArr != null && sArr.length > 0) {
				joinMap = new JSONObject(true); //注意：这里必须要保证join连接顺序，保证后边遍历是按照join参数的顺序生成的SQL
				for (int i = 0; i < sArr.length; i++) {
					joinMap.put(sArr[i], new JSONObject());
				}
			}
		}
		else if (join != null){
			throw new UnsupportedDataTypeException(TAG + ".onJoinParse  join 只能是 String 或 JSONObject 类型！");
		}

		Set<Entry<String, Object>> set = joinMap == null ? null : joinMap.entrySet();
		if (set == null || set.isEmpty()) {
			Log.e(TAG, "doJoin  set == null || set.isEmpty() >> return null;");
			return null;
		}



		List<Join> joinList = new ArrayList<>();


		JSONObject tableObj;
		String targetPath;

		JSONObject targetObj;
		String targetTable;
		String targetKey;

		String path;

		//		List<String> onList = new ArrayList<>();
		for (Entry<String, Object> e : set) {//User/id@
			if (e.getValue() instanceof JSONObject == false) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中value不合法！"
						+ "必须为 &/Table0/key0,</Table1/key1,... 或 { '&/Table0/key0':{}, '</Table1/key1':{},... } 这种形式！");
			}

			//分割 /Table/key
			path = "" + e.getKey();

			int index = path.indexOf("/");
			if (index < 0) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中value不合法！"
						+ "必须为 &/Table0/key0,</Table1/key1,... 或 { '&/Table0/key0':{}, '</Table1/key1':{},... } 这种形式！");
			}
			String joinType = path.substring(0, index); //& | ! < > ( ) <> () *
			//			if (StringUtil.isEmpty(joinType, true)) {
			//				joinType = "|"; // FULL JOIN
			//			}
			path = path.substring(index + 1);

			index = path.indexOf("/");
			String tableKey = index < 0 ? null : path.substring(0, index); //User:owner
			apijson.orm.Entry<String, String> entry = Pair.parseEntry(tableKey, true);
			String table = entry.getKey(); //User
			String alias = entry.getValue(); //owner
			String key = StringUtil.isEmpty(table, true) ? null : path.substring(index + 1);//id@
			if (StringUtil.isEmpty(key, true)) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中value不合法！"
						+ "必须为 &/Table0/key0,</Table1/key1,... 这种形式！");
			}

			//取出Table对应的JSONObject，及内部引用赋值 key:value
			tableObj = request.getJSONObject(tableKey);
			targetPath = tableObj == null ? null : tableObj.getString(key);
			if (StringUtil.isEmpty(targetPath, true)) {
				throw new IllegalArgumentException("/" + path + ":value 中value必须为引用赋值的路径 '/targetTable/targetKey' ！");
			}

			//取出引用赋值路径targetPath对应的Table和key
			index = targetPath.lastIndexOf("/");
			targetKey = index < 0 ? null : targetPath.substring(index + 1);
			if (StringUtil.isEmpty(targetKey, true)) {
				throw new IllegalArgumentException("/" + path + ":'/targetTable/targetKey' 中targetKey不能为空！");
			}

			targetPath = targetPath.substring(0, index);
			index = targetPath.lastIndexOf("/");
			targetTable = index < 0 ? targetPath : targetPath.substring(index + 1);


			//对引用的JSONObject添加条件
			targetObj = request.getJSONObject(targetTable);
			if (targetObj == null) {
				throw new IllegalArgumentException(targetTable + "." + targetKey
						+ ":'/targetTable/targetKey' 中路径对应的对象不存在！");
			}

			tableObj.put(key, tableObj.remove(key)); //保证和SQLExcecutor缓存的Config里where顺序一致，生成的SQL也就一致

			Join j = new Join();
			j.setPath(path);
			j.setOriginKey(key);
			j.setOriginValue(targetPath);
			j.setJoinType(joinType);
			j.setTable(table);
			j.setAlias(alias);
			j.setTargetName(targetTable);
			j.setTargetKey(targetKey);
			j.setKeyAndType(key);
			j.setRequest(getJoinObject(table, tableObj, key));
			j.setOuter((JSONObject) e.getValue());

			joinList.add(j);

			//			onList.add(table + "." + key + " = " + targetTable + "." + targetKey); // ON User.id = Moment.userId

		}


		//拼接多个 SQLConfig 的SQL语句，然后执行，再把结果分别缓存(Moment, User等)到 SQLExecutor 的 cacheMap
		//		AbstractSQLConfig config0 = null;
		//		String sql = "SELECT " + config0.getColumnString() + " FROM " + config0.getTable() + " INNER JOIN " + targetTable + " ON "
		//				+ onList.get(0) + config0.getGroupString() + config0.getHavingString() + config0.getOrderString();


		return joinList;
	}



	private static final List<String> JOIN_COPY_KEY_LIST;
	static {//TODO 不全
		JOIN_COPY_KEY_LIST = new ArrayList<String>();
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_DATABASE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_SCHEMA);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_COLUMN);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_COMBINE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_GROUP);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_HAVING);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_ORDER);
	}

	/**
	 * 取指定json对象的id集合
	 * @param table
	 * @param key
	 * @param obj
	 * @param targetKey 
	 * @return null ? 全部 : 有限的数组
	 */
	private JSONObject getJoinObject(String table, JSONObject obj, String key) {
		if (obj == null || obj.isEmpty()) {
			Log.e(TAG, "getIdList  obj == null || obj.isEmpty() >> return null;");
			return null;
		}
		if (StringUtil.isEmpty(key, true)) {
			Log.e(TAG, "getIdList  StringUtil.isEmpty(key, true) >> return null;");
			return null;
		}

		//取出所有join条件
		JSONObject requestObj = new JSONObject(true);//(JSONObject) obj.clone();//
		Set<String> set = new LinkedHashSet<>(obj.keySet());
		for (String k : set) {
			if (StringUtil.isEmpty(k, true)) {
				continue;
			}

			if (k.startsWith("@")) {
				if (JOIN_COPY_KEY_LIST.contains(k)) {
					requestObj.put(k, obj.get(k)); //保留
				}
			}
			else {
				if (k.endsWith("@")) {
					if (k.equals(key)) {
						continue;
					}
					throw new UnsupportedOperationException(table + "." + k + " 不合法！" + JSONRequest.KEY_JOIN
							+ " 关联的Table中只能有1个 key@:value ！");
				}

				if (k.contains("()") == false) { //不需要远程函数
					//					requestObj.put(k, obj.remove(k)); //remove是为了避免重复查询副表
					requestObj.put(k, obj.get(k)); //remove是为了避免重复查询副表
				}
			}
		}


		return requestObj;
	}

	@Override
	public int getDefaultQueryCount() {
		return DEFAULT_QUERY_COUNT;
	}
	@Override
	public int getMaxQueryPage() {
		return MAX_QUERY_PAGE;
	}
	@Override
	public int getMaxQueryCount() {
		return MAX_QUERY_COUNT;
	}
	@Override
	public int getMaxUpdateCount() {
		return MAX_UPDATE_COUNT;
	}
	@Override
	public int getMaxSQLCount() {
		return MAX_SQL_COUNT;
	}
	@Override
	public int getMaxObjectCount() {
		return MAX_OBJECT_COUNT;
	}
	@Override
	public int getMaxArrayCount() {
		return MAX_ARRAY_COUNT;
	}
	@Override
	public int getMaxQueryDepth() {
		return MAX_QUERY_DEPTH;
	}


	/**根据路径取值
	 * @param parent
	 * @param pathKeys
	 * @return
	 */
	protected static Object getValue(JSONObject parent, String[] pathKeys) {
		if (parent == null || pathKeys == null || pathKeys.length <= 0) {
			Log.w(TAG, "getChild  parent == null || pathKeys == null || pathKeys.length <= 0 >> return parent;");
			return parent;
		}

		//逐层到达child的直接容器JSONObject parent
		final int last = pathKeys.length - 1;
		for (int i = 0; i < last; i++) {//一步一步到达指定位置
			if (parent == null) {//不存在或路径错误(中间的key对应value不是JSONObject)
				break;
			}
			parent = getJSONObject(parent, pathKeys[i]);
		}

		return parent == null ? null : parent.get(pathKeys[last]);
	}


	/**获取被依赖引用的key的路径, 实时替换[] -> []/i
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String getValuePath(String parentPath, String valuePath) {
		if (valuePath.startsWith("/")) {
			valuePath = getAbsPath(parentPath, valuePath);
		} else {//处理[] -> []/i
			valuePath = replaceArrayChildPath(parentPath, valuePath);
		}
		return valuePath;
	}

	/**获取绝对路径
	 * @param path
	 * @param name
	 * @return
	 */
	public static String getAbsPath(String path, String name) {
		Log.i(TAG, "getPath  path = " + path + "; name = " + name + " <<<<<<<<<<<<<");
		path = StringUtil.getString(path);
		name = StringUtil.getString(name);
		if (StringUtil.isNotEmpty(path, false)) {
			if (StringUtil.isNotEmpty(name, false)) {
				path += ((name.startsWith("/") ? "" : "/") + name);
			}
		} else {
			path = name;
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		Log.i(TAG, "getPath  return " + path + " >>>>>>>>>>>>>>>>");
		return path;
	}

	/**替换[] -> []/i
	 * 不能写在getAbsPath里，因为name不一定是依赖路径
	 * @param parentPath
	 * @param valuePath
	 * @return
	 */
	public static String replaceArrayChildPath(String parentPath, String valuePath) {
		String[] ps = StringUtil.split(parentPath, "]/");//"[]/");
		if (ps != null && ps.length > 1) {
			String[] vs = StringUtil.split(valuePath, "]/");

			if (vs != null && vs.length > 0) {
				String pos;
				for (int i = 0; i < ps.length - 1; i++) {
					if (ps[i] == null || ps[i].equals(vs[i]) == false) {//允许""？
						break;
					}

					pos = ps[i+1].contains("/") == false ? ps[i+1]
							: ps[i+1].substring(0, ps[i+1].indexOf("/"));
					if (
							//StringUtil.isNumer(pos) && 
							vs[i+1].startsWith(pos + "/") == false) {
						vs[i+1] = pos + "/" + vs[i+1];
					}
				}
				return StringUtil.getString(vs, "]/");
			}
		}
		return valuePath;
	}

	//依赖引用关系 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**将已获取完成的object的内容替换requestObject里对应的值
	 * @param path object的路径
	 * @param result 需要被关联的object
	 */
	@Override
	public synchronized void putQueryResult(String path, Object result) {
		Log.i(TAG, "\n putQueryResult  valuePath = " + path + "; result = " + result + "\n <<<<<<<<<<<<<<<<<<<<<<<");
		//		if (queryResultMap.containsKey(valuePath)) {//只保存被关联的value
		Log.d(TAG, "putQueryResult  queryResultMap.containsKey(valuePath) >> queryResultMap.put(path, result);");
		queryResultMap.put(path, result);
		//		}
	}
	/**根据路径获取值
	 * @param valuePath
	 * @return parent == null ? valuePath : parent.get(keys[keys.length - 1])
	 */
	@Override
	public Object getValueByPath(String valuePath) {
		Log.i(TAG, "<<<<<<<<<<<<<<< \n getValueByPath  valuePath = " + valuePath + "\n <<<<<<<<<<<<<<<<<<");
		if (StringUtil.isEmpty(valuePath, true)) {
			Log.e(TAG, "getValueByPath  StringUtil.isNotEmpty(valuePath, true) == false >> return null;");
			return null;
		}
		Object target = queryResultMap.get(valuePath);
		if (target != null) {
			return target;
		}

		//取出key被valuePath包含的result，再从里面获取key对应的value
		Set<String> set = queryResultMap.keySet();
		JSONObject parent = null;
		String[] keys = null;
		for (String path : set) {
			if (valuePath.startsWith(path + "/")) {
				try {
					parent = (JSONObject) queryResultMap.get(path);
				} catch (Exception e) {
					Log.e(TAG, "getValueByPath  try { parent = (JSONObject) queryResultMap.get(path); } catch { "
							+ "\n parent not instanceof JSONObject!");
					parent = null;
				}
				if (parent != null) {
					keys = StringUtil.splitPath(valuePath.substring(path.length()));
				}
				break;
			}
		}

		//逐层到达targetKey的直接容器JSONObject parent
		if (keys != null && keys.length > 1) {
			for (int i = 0; i < keys.length - 1; i++) {//一步一步到达指定位置parentPath
				if (parent == null) {//不存在或路径错误(中间的key对应value不是JSONObject)
					break;
				}
				parent = getJSONObject(parent, keys[i]);
			}
		}

		if (parent != null) {
			Log.i(TAG, "getValueByPath >> get from queryResultMap >> return  parent.get(keys[keys.length - 1]);");
			target = parent.get(keys[keys.length - 1]); //值为null应该报错NotExistExeption，一般都是id关联，不可为null，否则可能绕过安全机制
			if (target != null) {
				Log.i(TAG, "getValueByPath >> getValue >> return target = " + target);
				return target;
			}
		}


		//从requestObject中取值
		target = getValue(requestObject, StringUtil.splitPath(valuePath));
		if (target != null) {
			Log.i(TAG, "getValueByPath >> getValue >> return target = " + target);
			return target;
		}

		Log.i(TAG, "getValueByPath  return valuePath;");
		return valuePath;
	}

	//依赖引用关系 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	public static JSONObject getJSONObject(JSONObject object, String key) {
		try {
			return object.getJSONObject(key);
		} catch (Exception e) {
			Log.i(TAG, "getJSONObject  try { return object.getJSONObject(key);"
					+ " } catch (Exception e) { \n"  + e.getMessage());
		}
		return null;
	}


	public static final String KEY_CONFIG = "config";

	/**执行 SQL 并返回 JSONObject
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject executeSQL(SQLConfig config, boolean isSubquery) throws Exception {
		if (config == null) {
			Log.d(TAG, "executeSQL  config == null >> return null;");
			return null;
		}

		if (isSubquery) {
			JSONObject sqlObj = new JSONObject(true);
			sqlObj.put(KEY_CONFIG, config);
			return sqlObj;//容易丢失信息 JSON.parseObject(config);
		}

		try {
			boolean explain = config.isExplain();
			JSONObject result;
			if (explain) { //如果先执行 explain，则 execute 会死循环，所以只能先执行非 explain
				config.setExplain(false); //对下面 config.getSQL(false); 生效
				JSONObject res = getSQLExecutor().execute(config, false);

				config.setExplain(explain);
				JSONObject explainResult = config.isMain() && config.getPosition() != 0 ? null : getSQLExecutor().execute(config, false);

				if (explainResult == null) {
					result = res;
				}
				else {
					result = new JSONObject(true);
					result.put(KEY_EXPLAIN, explainResult);
					result.putAll(res);
				}
			}
			else {
				result = getSQLExecutor().execute(config, false);
			}

			return parseCorrectResponse(config.getTable(), result);
		}
		catch (Exception e) {
			if (Log.DEBUG == false && e instanceof SQLException) {
				throw new SQLException("数据库驱动执行异常SQLException，非 Log.DEBUG 模式下不显示详情，避免泄漏真实模式名、表名等隐私信息", e);
			}
			throw e;
		}
		finally {
			if (config.getPosition() == 0 && config.limitSQLCount()) {
				int maxSQLCount = getMaxSQLCount();
				int sqlCount = getSQLExecutor().getExecutedSQLCount();
				Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n\n\n 已执行 " + sqlCount + "/" + maxSQLCount + " 条 SQL \n\n\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				if (sqlCount > maxSQLCount) {
					throw new IllegalArgumentException("截至 " + config.getTable() + " 已执行 " + sqlCount + " 条 SQL，数量已超限，必须在 0-" + maxSQLCount + " 内 !");
				}
			}
		}
	}


	//事务处理 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private int transactionIsolation = Connection.TRANSACTION_NONE;
	@Override
	public int getTransactionIsolation() {
		return transactionIsolation;
	}
	@Override
	public void setTransactionIsolation(int transactionIsolation) {
		this.transactionIsolation = transactionIsolation;
	}

	@Override
	public void begin(int transactionIsolation) {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<<<<<<<<<<< begin transactionIsolation = " + transactionIsolation + " >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		getSQLExecutor().setTransactionIsolation(transactionIsolation); //不知道 connection 什么时候创建，不能在这里准确控制，getSqlExecutor().begin(transactionIsolation);
	}
	@Override
	public void rollback() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<<<<<<<<<<< rollback >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		getSQLExecutor().rollback();
	}
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<<<<<<<<<<< rollback savepoint " + (savepoint == null ? "" : "!") + "= null >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		getSQLExecutor().rollback(savepoint);
	}
	@Override
	public void commit() throws SQLException {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<<<<<<<<<<< commit >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		getSQLExecutor().commit();
	}
	@Override
	public void close() {
		Log.d("\n\n" + TAG, "<<<<<<<<<<<<<<<<<<<<<<< close >>>>>>>>>>>>>>>>>>>>>>> \n\n");
		getSQLExecutor().close();
	}

	/**开始事务
	 */
	protected void onBegin() {
		//		Log.d(TAG, "onBegin >>");
		if (RequestMethod.isQueryMethod(requestMethod)) {
			return;
		}

		begin(getTransactionIsolation());
	}
	/**提交事务
	 */
	protected void onCommit() {
		//		Log.d(TAG, "onCommit >>");
		if (RequestMethod.isQueryMethod(requestMethod)) {
			return;
		}

		try {
			commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**回滚事务
	 */
	protected void onRollback() {
		//		Log.d(TAG, "onRollback >>");
		if (RequestMethod.isQueryMethod(requestMethod)) {
			return;
		}

		try {
			rollback();
		}
		catch (SQLException e1) {
			e1.printStackTrace();
			try {
				rollback(null);
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

	//事务处理 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	protected void onClose() {
		//		Log.d(TAG, "onClose >>");

		close();
		verifier = null;
		sqlExecutor = null;
		queryResultMap.clear();
		queryResultMap = null;
	}

}
