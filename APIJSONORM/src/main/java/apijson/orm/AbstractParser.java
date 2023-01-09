/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;

import apijson.JSON;
import apijson.JSONRequest;
import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.exception.CommonException;
import apijson.orm.exception.UnsupportedDataTypeException;

import static apijson.JSONObject.KEY_COMBINE;
import static apijson.JSONObject.KEY_EXPLAIN;
import static apijson.RequestMethod.CRUD;
import static apijson.RequestMethod.GET;

/**parser for parsing request to JSONObject
 * @author Lemon
 */
public abstract class AbstractParser<T extends Object> implements Parser<T>, ParserCreator<T>, VerifierCreator<T>, SQLCreator {
	protected static final String TAG = "AbstractParser";
	
	/**
	 * json对象、数组对应的数据源、版本、角色、method等
	 */
	protected Map<Object, Map<String, Object>> keyObjectAttributesMap = new HashMap<>();
	/**
	 * 可以通过切换该变量来控制是否打印关键的接口请求内容。保守起见，该值默认为false。
	 * 与 {@link Log#DEBUG} 任何一个为 true 都会打印关键的接口请求内容。
	 */
	public static boolean IS_PRINT_REQUEST_STRING_LOG = false;

	/**
	 * 打印大数据量日志的标识。线上环境比较敏感，可以通过切换该变量来控制异常栈抛出、错误日志打印。保守起见，该值默认为false。
	 * 与 {@link Log#DEBUG} 任何一个为 true 都会打印关键的接口请求及响应信息。
	 */
	public static boolean IS_PRINT_BIG_LOG = false;

	/**
	 * 可以通过切换该变量来控制是否打印关键的接口请求结束时间。保守起见，该值默认为false。
	 * 与 {@link Log#DEBUG} 任何一个为 true 都会打印关键的接口请求结束时间。
	 */
	public static boolean IS_PRINT_REQUEST_ENDTIME_LOG = false;


	public static int DEFAULT_QUERY_COUNT = 10;
	public static int MAX_QUERY_PAGE = 100;
	public static int MAX_QUERY_COUNT = 100;
	public static int MAX_UPDATE_COUNT = 10;
	public static int MAX_SQL_COUNT = 200;
	public static int MAX_OBJECT_COUNT = 5;
	public static int MAX_ARRAY_COUNT = 5;
	public static int MAX_QUERY_DEPTH = 5;

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


	/**
	 * method = null
	 */
	public AbstractParser() {
		this(null);
	}
	/**needVerify = true
	 * @param method null ? requestMethod = GET
	 */
	public AbstractParser(RequestMethod method) {
        super();
        setMethod(method);
        setNeedVerifyRole(AbstractVerifier.ENABLE_VERIFY_ROLE);
        setNeedVerifyContent(AbstractVerifier.ENABLE_VERIFY_CONTENT);
	}
	/**
	 * @param method null ? requestMethod = GET
	 * @param needVerify 仅限于为服务端提供方法免验证特权，普通请求不要设置为 false ！ 如果对应Table有权限也建议用默认值 true，保持和客户端权限一致
	 */
	public AbstractParser(RequestMethod method, boolean needVerify) {
		super();
		setMethod(method);
		setNeedVerify(needVerify);
	}

	protected boolean isRoot = true;
	public boolean isRoot() {
		return isRoot;
	}
	public AbstractParser<T> setRoot(boolean isRoot) {
		this.isRoot = isRoot;
		return this;
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

	protected String requestURL;
	public String getRequestURL() {
		return requestURL;
	}
	public AbstractParser<T> setRequestURL(String requestURL) {
		this.requestURL = requestURL;
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

	protected Boolean globalFormat;
	public AbstractParser<T> setGlobalFormat(Boolean globalFormat) {
		this.globalFormat = globalFormat;
		return this;
	}
	@Override
	public Boolean getGlobalFormat() {
		return globalFormat;
	}
	protected String globalRole;
	public AbstractParser<T> setGlobalRole(String globalRole) {
		this.globalRole = globalRole;
		return this;
	}
	@Override
	public String getGlobalRole() {
		return globalRole;
	}
	protected String globalDatabase;
	public AbstractParser<T> setGlobalDatabase(String globalDatabase) {
		this.globalDatabase = globalDatabase;
		return this;
	}
	@Override
	public String getGlobalDatabase() {
		return globalDatabase;
	}
	protected String globalSchema;
	public AbstractParser<T> setGlobalSchema(String globalSchema) {
		this.globalSchema = globalSchema;
		return this;
	}
	@Override
	public String getGlobalSchema() {
		return globalSchema;
	}
	protected String globalDatasource;
	@Override
	public String getGlobalDatasource() {
		return globalDatasource;
	}
	public AbstractParser<T> setGlobalDatasource(String globalDatasource) {
		this.globalDatasource = globalDatasource;
		return this;
	}

	protected Boolean globalExplain;
	public AbstractParser<T> setGlobalExplain(Boolean globalExplain) {
		this.globalExplain = globalExplain;
		return this;
	}
	@Override
	public Boolean getGlobalExplain() {
		return globalExplain;
	}
	protected String globalCache;
	public AbstractParser<T> setGlobalCache(String globalCache) {
		this.globalCache = globalCache;
		return this;
	}
	@Override
	public String getGlobalCache() {
		return globalCache;
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
			return newErrorResult(e, isRoot);
		}

		return parseResponse(requestObject);
	}

	private int queryDepth;
	private long executedSQLDuration;

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
				return extendErrorResult(requestObject, e, requestMethod, getRequestURL(), isRoot);
			}
		}

		//必须在parseCorrectRequest后面，因为parseCorrectRequest可能会添加 @role
		if (isNeedVerifyRole() && globalRole == null) {
			try {
				setGlobalRole(requestObject.getString(JSONRequest.KEY_ROLE));
				requestObject.remove(JSONRequest.KEY_ROLE);
			} catch (Exception e) {
				return extendErrorResult(requestObject, e, requestMethod, getRequestURL(), isRoot);
			}
		}

		try {
			setGlobalFormat(requestObject.getBoolean(JSONRequest.KEY_FORMAT));
			setGlobalDatabase(requestObject.getString(JSONRequest.KEY_DATABASE));
			setGlobalSchema(requestObject.getString(JSONRequest.KEY_SCHEMA));
			setGlobalDatasource(requestObject.getString(JSONRequest.KEY_DATASOURCE));
			setGlobalExplain(requestObject.getBoolean(JSONRequest.KEY_EXPLAIN));
			setGlobalCache(requestObject.getString(JSONRequest.KEY_CACHE));

			requestObject.remove(JSONRequest.KEY_FORMAT);
			requestObject.remove(JSONRequest.KEY_DATABASE);
			requestObject.remove(JSONRequest.KEY_SCHEMA);
			requestObject.remove(JSONRequest.KEY_DATASOURCE);
			requestObject.remove(JSONRequest.KEY_EXPLAIN);
			requestObject.remove(JSONRequest.KEY_CACHE);
		} catch (Exception e) {
			return extendErrorResult(requestObject, e, requestMethod, getRequestURL(), isRoot);
		}

		final String requestString = JSON.toJSONString(request);//request传进去解析后已经变了


		queryResultMap = new HashMap<String, Object>();

		Exception error = null;
		sqlExecutor = createSQLExecutor();
		onBegin();
		try {
			queryDepth = 0;
			executedSQLDuration = 0;

			requestObject = onObjectParse(request, null, null, null, false);

			onCommit();
		}
		catch (Exception e) {
			e.printStackTrace();
			error = e;

			onRollback();
		}

		requestObject = error == null ? extendSuccessResult(requestObject, isRoot) : extendErrorResult(requestObject, error, requestMethod, getRequestURL(), isRoot);

		JSONObject res = (globalFormat != null && globalFormat) && JSONResponse.isSuccess(requestObject) ? new JSONResponse(requestObject) : requestObject;

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		if (Log.DEBUG) {
			res.put("sql:generate|cache|execute|maxExecute", getSQLExecutor().getGeneratedSQLCount() + "|" + getSQLExecutor().getCachedSQLCount() + "|" + getSQLExecutor().getExecutedSQLCount() + "|" + getMaxSQLCount());
			res.put("depth:count|max", queryDepth + "|" + getMaxQueryDepth());

			executedSQLDuration += sqlExecutor.getExecutedSQLDuration() + sqlExecutor.getSqlResultDuration();
			long parseDuration = duration - executedSQLDuration;
			res.put("time:start|duration|end|parse|sql", startTime + "|" + duration + "|" + endTime + "|" + parseDuration + "|" + executedSQLDuration);

			if (error != null) {
                //        String msg = error.getMessage();
                //        if (msg != null && msg.contains(Log.KEY_SYSTEM_INFO_DIVIDER)) {
                //        }
                Throwable t = error instanceof CommonException && error.getCause() != null ? error.getCause() : error;
				res.put("trace:throw", t.getClass().getName());
				res.put("trace:stack", t.getStackTrace());
			}
		}

		onClose();

		// CS304 Issue link: https://github.com/Tencent/APIJSON/issues/232
		if (IS_PRINT_REQUEST_STRING_LOG || Log.DEBUG || error != null) {
			Log.sl("\n\n\n", '<', "");
			Log.fd(TAG, requestMethod + "/parseResponse  request = \n" + requestString + "\n\n");
		}
		if (IS_PRINT_BIG_LOG || Log.DEBUG || error != null) {  // 日志仅存服务器，所以不太敏感，而且这些日志虽然量大但非常重要，对排查 bug 很关键
			Log.fd(TAG, requestMethod + "/parseResponse return response = \n" + JSON.toJSONString(requestObject) + "\n\n");
		}
		if (IS_PRINT_REQUEST_ENDTIME_LOG || Log.DEBUG || error != null) {
			Log.fd(TAG, requestMethod + "/parseResponse  endTime = " + endTime + ";  duration = " + duration);
			Log.sl("", '>', "\n\n\n");
		}
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
				if (globalRole != null) {
					config.setRole(globalRole);
				} else {
					config.setRole(getVisitor().getId() == null ? AbstractVerifier.UNKNOWN : AbstractVerifier.LOGIN);
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

		return batchVerify(method, tag, version, name, request, maxUpdateCount, creator);
	}
	
	/**自动根据 tag 是否为 TableKey 及是否被包含在 object 内来决定是否包装一层，改为 { tag: object, "tag": tag }
	 * @param object
	 * @param tag
	 * @return
	 */
	public static JSONObject wrapRequest(RequestMethod method, String tag, JSONObject object, boolean isStructure) {
		boolean putTag = ! isStructure;

		if (object == null || object.containsKey(tag)) { //tag 是 Table 名或 Table[]
			if (putTag) {
				if (object == null) {
					object = new JSONObject(true);
				}
				object.put(JSONRequest.KEY_TAG, tag);
			}
			return object;
		}

		boolean isDiffArrayKey = tag.endsWith(":[]");
		boolean isArrayKey = isDiffArrayKey || JSONRequest.isArrayKey(tag);
		String key = isArrayKey ? tag.substring(0, tag.length() - (isDiffArrayKey ? 3 : 2)) : tag;

		JSONObject target = object;
		if (apijson.JSONObject.isTableKey(key)) {
			if (isDiffArrayKey) { //自动为 tag = Comment:[] 的 { ... } 新增键值对为 { "Comment[]":[], "TYPE": { "Comment[]": "OBJECT[]" } ... }
				if (isStructure && (method == RequestMethod.POST || method == RequestMethod.PUT)) {
					String arrKey = key + "[]";

					if (target.containsKey(arrKey) == false) {
						target.put(arrKey, new JSONArray());
					}

					try {
						JSONObject type = target.getJSONObject(Operation.TYPE.name());
						if (type == null || (type.containsKey(arrKey) == false)) {
							if (type == null) {
								type = new JSONObject(true);
							}

							type.put(arrKey, "OBJECT[]");
							target.put(Operation.TYPE.name(), type);
						}
					}
					catch (Throwable e) {
						Log.w(TAG, "wrapRequest try { JSONObject type = target.getJSONObject(Operation.TYPE.name()); } catch (Exception e) = " + e.getMessage());
					}
				}
			}
			else { //自动为 tag = Comment 的 { ... } 包一层为 { "Comment": { ... } }
				if (isArrayKey == false || RequestMethod.isGetMethod(method, true)) {
					target = new JSONObject(true);
					target.put(tag, object);
				}
				else if (target.containsKey(key) == false) {
					target = new JSONObject(true);
					target.put(key, object);
				}
			}
		}

		if (putTag) {
			target.put(JSONRequest.KEY_TAG, tag);
		}

		return target;
	}


	/**新建带状态内容的JSONObject
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject newResult(int code, String msg) {
		return newResult(code, msg, false);
	}
	/**新建带状态内容的JSONObject
	 * @param code
	 * @param msg
	 * @param isRoot
	 * @return
	 */
	public static JSONObject newResult(int code, String msg, boolean isRoot) {
		return extendResult(null, code, msg, isRoot);
	}

	/**添加JSONObject的状态内容，一般用于错误提示结果
	 * @param object
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject extendResult(JSONObject object, int code, String msg, boolean isRoot) {
		int index = Log.DEBUG == false || isRoot == false || msg == null ? -1 : msg.lastIndexOf(Log.KEY_SYSTEM_INFO_DIVIDER);
		String debug = Log.DEBUG == false || isRoot == false ? null : (index >= 0 ? msg.substring(index + Log.KEY_SYSTEM_INFO_DIVIDER.length()).trim()
				: " \n提 bug 请发请求和响应的【完整截屏】，没图的自行解决！"
						+ " \n开发者有限的时间和精力主要放在【维护项目源码和文档】上！"
						+ " \n【描述不详细】 或 【文档/常见问题 已有答案】 的问题可能会被忽略！！"
						+ " \n【态度 不文明/不友善】的可能会被踢出群，问题也可能不予解答！！！"
				+ " \n\n **环境信息** "
				+ " \n系统: " + Log.OS_NAME + " " + Log.OS_VERSION
				+ " \n数据库: DEFAULT_DATABASE = " + AbstractSQLConfig.DEFAULT_DATABASE
				+ " \nJDK: " + Log.JAVA_VERSION + " " + Log.OS_ARCH
				+ " \nAPIJSON: " + Log.VERSION
				+ " \n   \n【常见问题】：https://github.com/Tencent/APIJSON/issues/36"
				+ " \n【通用文档】：https://github.com/Tencent/APIJSON/blob/master/Document.md"
				+ " \n【视频教程】：https://search.bilibili.com/all?keyword=APIJSON");

		msg = index >= 0 ? msg.substring(0, index) : msg;

		if (object == null) {
			object = new JSONObject(true);
		}

		if (object.get(JSONResponse.KEY_OK) == null) {
			object.put(JSONResponse.KEY_OK, JSONResponse.isSuccess(code));
		}
		if (object.get(JSONResponse.KEY_CODE) == null) {
			object.put(JSONResponse.KEY_CODE, code);
		}

		String m = StringUtil.getString(object.getString(JSONResponse.KEY_MSG));
		if (m.isEmpty() == false) {
			msg = m + " ;\n " + StringUtil.getString(msg);
		}

		object.put(JSONResponse.KEY_MSG, msg);
		if (debug != null) {
			object.put("debug:info|help", debug);
		}

		return object;
	}


	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object) {
		return extendSuccessResult(object, false);
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @param isRoot
	 * @return
	 */
	public static JSONObject extendSuccessResult(JSONObject object, boolean isRoot) {
		return extendResult(object, JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED, isRoot);
	}
	/**获取请求成功的状态内容
	 * @return
	 */
	public static JSONObject newSuccessResult() {
		return newSuccessResult(false);
	}
	/**获取请求成功的状态内容
	 * @param isRoot
	 * @return
	 */
	public static JSONObject newSuccessResult(boolean isRoot) {
		return newResult(JSONResponse.CODE_SUCCESS, JSONResponse.MSG_SUCCEED, isRoot);
	}

	/**添加请求成功的状态内容
	 * @param object
	 * @param e
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Throwable e) {
		return extendErrorResult(object, e, false);
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @param e
	 * @param isRoot
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Throwable e, boolean isRoot) {
		return extendErrorResult(object, e, null, null, isRoot);
	}
	/**添加请求成功的状态内容
	 * @param object
	 * @return
	 */
	public static JSONObject extendErrorResult(JSONObject object, Throwable e, RequestMethod requestMethod, String url, boolean isRoot) {
        String msg = CommonException.getMsg(e);

        if (Log.DEBUG && isRoot) {
            try {
                boolean isCommon = e instanceof CommonException;
                String env = isCommon ? ((CommonException) e).getEnvironment() : null;
                if (StringUtil.isEmpty(env)) {
                    //int index = msg.lastIndexOf(Log.KEY_SYSTEM_INFO_DIVIDER);
                    //env = index >= 0 ? msg.substring(index + Log.KEY_SYSTEM_INFO_DIVIDER.length()).trim()
                    env = " \n **环境信息** "
                            + " \n 系统: " + Log.OS_NAME + " " + Log.OS_VERSION
                            + " \n 数据库: <!-- 请填写，例如 MySQL 5.7。默认数据库为 " + AbstractSQLConfig.DEFAULT_DATABASE + " -->"
                            + " \n JDK: " + Log.JAVA_VERSION + " " + Log.OS_ARCH
                            + " \n APIJSON: " + Log.VERSION;

                    //msg = index < 0 ? msg : msg.substring(0, index).trim();
                }

                String encodedMsg = URLEncoder.encode(msg, "UTF-8");

                if (StringUtil.isEmpty(url, true)) {
                    String host = "localhost";
                    try {
                        host = InetAddress.getLocalHost().getHostAddress();
                    } catch (Throwable e2) {}

                    String port = "8080";
                    try {
                        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();

                        Set<ObjectName> objectNames = beanServer.queryNames(
                                new ObjectName("*:type=Connector,*"),
                                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"))
                        );
                        String p = objectNames.iterator().next().getKeyProperty("port");
                        port = StringUtil.isEmpty(p, true) ? port : p;
                    } catch (Throwable e2) {}

                    url = "http://" + host + ":" + port + "/" + (requestMethod == null ? RequestMethod.GET : requestMethod).name().toLowerCase();
                }

                String req = JSON.toJSONString(object);
                try {
                    req = URLEncoder.encode(req, "UTF-8");
                } catch (Throwable e2) {}

                Throwable t = isCommon ? e.getCause() : e;
                boolean isSQLException = t instanceof SQLException;  // SQL 报错一般都是通用问题，优先搜索引擎
                String apiatuoAndGitHubLink = "\n\n【APIAuto】： \n http://apijson.cn/api?type=JSON&url=" + URLEncoder.encode(url, "UTF-8") + "&json=" + req
                        + "        \n\n【GitHub】： \n https://www.google.com/search?q=site%3Agithub.com%2FTencent%2FAPIJSON+++" + encodedMsg;

                msg += Log.KEY_SYSTEM_INFO_DIVIDER + "    浏览器打开以下链接查看解答"
                        + (isSQLException ? "" : apiatuoAndGitHubLink)
                        //	GitHub Issue 搜索貌似是精准包含，不易找到答案 	+ "        \n\nGitHub： \n https://github.com/Tencent/APIJSON/issues?q=is%3Aissue+" + encodedMsg
                        + "        \n\n【Google】：\n https://www.google.com/search?q=" + encodedMsg
                        + "        \n\n【百度】：\n https://www.baidu.com/s?ie=UTF-8&wd=" + encodedMsg
                        + (isSQLException ? apiatuoAndGitHubLink : "")
                        + "        \n\n都没找到答案？打开这个链接 \n https://github.com/Tencent/APIJSON/issues/new?assignees=&labels=&template=--bug.md  "
                        + " \n然后提交问题，推荐用以下模板修改，注意要换行保持清晰可读。"
                        + " \n【标题】：" + msg
                        + " \n【内容】：" + env + "\n\n**问题描述**\n" + msg
                        + " \n\n<!-- 尽量完整截屏(至少包含请求和回包结果，还可以加上控制台报错日志)，然后复制粘贴到这里 -->"
                        + " \n\nPOST " + url
                        + " \n发送请求 Request JSON：\n ```js"
                        + " \n 请填写，例如 { \"Users\":{} }"
                        + " \n```"
                        + " \n\n返回结果 Response JSON：\n ```js"
                        + " \n 请填写，例如 { \"Users\": {}, \"code\": 401, \"msg\": \"Users 不允许 UNKNOWN 用户的 GET 请求！\" }"
                        + " \n```";
            } catch (Throwable e2) {}
        }

        int code = CommonException.getCode(e);
        return extendResult(object, code, msg, isRoot);
    }

	/**新建错误状态内容
	 * @param e
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e) {
		return newErrorResult(e, false);
	}
	/**新建错误状态内容
	 * @param e
	 * @param isRoot
	 * @return
	 */
	public static JSONObject newErrorResult(Exception e, boolean isRoot) {
		if (e != null) {
      //      if (Log.DEBUG) {
      e.printStackTrace();
      //      }

      String msg = CommonException.getMsg(e);
			Integer code = CommonException.getCode(e);

			return newResult(code, msg, isRoot);
		}

		return newResult(JSONResponse.CODE_SERVER_ERROR, JSONResponse.MSG_SERVER_ERROR, isRoot);
	}




	//TODO 启动时一次性加载Request所有内容，作为初始化。
	/**获取正确的请求，非GET请求必须是服务器指定的
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject parseCorrectRequest() throws Exception {
		if(this.getMethod() != RequestMethod.CRUD) {
			setTag(requestObject.getString(JSONRequest.KEY_TAG));
		}
		setVersion(requestObject.getIntValue(JSONRequest.KEY_VERSION));
		requestObject.remove(JSONRequest.KEY_TAG);
		requestObject.remove(JSONRequest.KEY_VERSION);
		return parseCorrectRequest(requestMethod, tag, version, "", requestObject, getMaxUpdateCount(), this);
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
					versionedMap = new TreeMap<>((o1, o2) -> {
						return o2 == null ? -1 : o2.compareTo(o1);  // 降序
					});
				}

				versionedMap.put(Integer.valueOf(version), result);
				AbstractVerifier.REQUEST_MAP.put(cacheKey, versionedMap);
			}
		}

		if (result == null) {
			if (Log.DEBUG == false && AbstractVerifier.REQUEST_MAP.isEmpty() == false) {
				return null;  // 已使用 REQUEST_MAP 缓存全部，但没查到
			}

			// 获取指定的JSON结构 <<<<<<<<<<<<<<
			SQLConfig config = createSQLConfig().setMethod(GET).setTable(table);
			config.setPrepared(false);
			config.setColumn(Arrays.asList("structure"));

			Map<String, Object> where = new HashMap<String, Object>();
			where.put("method", method);
			where.put(JSONRequest.KEY_TAG, tag);

			if (version > 0) {
				where.put(JSONRequest.KEY_VERSION + ">=", version);
			}
			config.setWhere(where);
			config.setOrder(JSONRequest.KEY_VERSION + (version > 0 ? "+" : "-"));
			config.setCount(1);

			// too many connections error: 不try-catch，可以让客户端看到是服务器内部异常
			result = getSQLExecutor().execute(config, false);

			// version, method, tag 组合情况太多了，JDK 里又没有 LRUCache，所以要么启动时一次性缓存全部后面只用缓存，要么每次都查数据库
			//			versionedMap.put(Integer.valueOf(version), result);
			//			AbstractVerifier.REQUEST_MAP.put(cacheKey, versionedMap);
		}

		return getJSONObject(result, "structure"); //解决返回值套了一层 "structure":{}
	}



	protected Map<String, ObjectParser> arrayObjectParserCacheMap = new HashMap<>();

	//	protected SQLConfig itemConfig;
	/**获取单个对象，该对象处于parentObject内
   * @param request parentObject 的 value
   * @param parentPath parentObject 的路径
   * @param name parentObject 的 key
   * @param arrayConfig config for array item
   * @param isSubquery 是否为子查询
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
		int position = arrayConfig == null ? 0 : arrayConfig.getPosition();

		String[] arr = StringUtil.split(parentPath, "/");
		if (position == 0) {
			int d = arr == null ? 1 : arr.length + 1;
			if (queryDepth < d) {
				queryDepth = d;
				int maxQueryDepth = getMaxQueryDepth();
				if (queryDepth > maxQueryDepth) {
					throw new IllegalArgumentException(parentPath + "/" + name + ":{} 的深度(或者说层级) 为 " + queryDepth + " 已超限，必须在 1-" + maxQueryDepth + " 内 !");
				}
			}
		}

		apijson.orm.Entry<String, String> entry = Pair.parseEntry(name, true);
		String table = entry.getKey(); //Comment
		// String alias = entry.getValue(); //to

		boolean isTable = apijson.JSONObject.isTableKey(table);
		boolean isArrayMainTable = isSubquery == false && isTable && type == SQLConfig.TYPE_ITEM_CHILD_0 && arrayConfig != null && RequestMethod.isGetMethod(arrayConfig.getMethod(), true);
		boolean isReuse = isArrayMainTable && position > 0;

		ObjectParser op = null;
		if (isReuse) {  // 数组主表使用专门的缓存数据
			op = arrayObjectParserCacheMap.get(parentPath.substring(0, parentPath.lastIndexOf("[]") + 2));
			op.setParentPath(parentPath);
		}

		if (op == null) {
			op = createObjectParser(request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
		}
		// 对象 - 设置 method
		setOpMethod(request, op, name);
		op = op.parse(name, isReuse);

		JSONObject response = null;
		if (op != null) {//SQL查询结果为空时，functionMap和customMap没有意义

			if (arrayConfig == null) { //Common
				response = op.setSQLConfig().executeSQL().response();
			}
			else {//Array Item Child
				int query = arrayConfig.getQuery();

				//total 这里不能用arrayConfig.getType()，因为在createObjectParser.onChildParse传到onObjectParse时已被改掉
				if (type == SQLConfig.TYPE_ITEM_CHILD_0 && query != JSONRequest.QUERY_TABLE && position == 0) {

					//TODO 应在这里判断 @column 中是否有聚合函数，而不是 AbstractSQLConfig.getColumnString

					JSONObject rp;
					Boolean compat = arrayConfig.getCompat();
					if (compat != null && compat) {
						// 解决对聚合函数字段通过 query:2 分页查总数返回值错误
						// 这里可能改变了内部的一些数据，下方通过 arrayConfig 还原
						SQLConfig cfg = op.setSQLConfig(0, 0, 0).getSQLConfig();
						boolean isExplain = cfg.isExplain();
						cfg.setExplain(false);

						Subquery subqy = new Subquery();
						subqy.setFrom(cfg.getTable());
						subqy.setConfig(cfg);

						SQLConfig countSQLCfg = createSQLConfig();
						countSQLCfg.setColumn(Arrays.asList("count(*):count"));
						countSQLCfg.setFrom(subqy);

						rp = executeSQL(countSQLCfg, false);

						cfg.setExplain(isExplain);
					}
					else {
						// 对聚合函数字段通过 query:2 分页查总数返回值错误
						RequestMethod method = op.getMethod();
						rp = op.setMethod(RequestMethod.HEAD).setSQLConfig().executeSQL().getSqlResponse();
						op.setMethod(method);
					}

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
							Object explain = rp.get(JSONResponse.KEY_EXPLAIN);
							if (explain instanceof JSONObject) {
								pagination.put(JSONResponse.KEY_EXPLAIN, explain);
							}
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
							.setSQLConfig(arrayConfig.getCount(), arrayConfig.getPage(), position)
							.executeSQL()
							.response();
					//					itemConfig = op.getConfig();
				}
			}

			if (isArrayMainTable) {
				if (position == 0) {  // 提取并缓存数组主表的列表数据
					arrayObjectParserCacheMap.put(parentPath.substring(0, parentPath.lastIndexOf("[]") + 2), op);
				}
			}
			//			else {
			//				op.recycle();
			//			}
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
		RequestMethod _method = request.get(apijson.JSONObject.KEY_METHOD) == null ? requestMethod : RequestMethod.valueOf(request.getString(apijson.JSONObject.KEY_METHOD));
		if (isSubquery == false && RequestMethod.isGetMethod(_method, true) == false) {
			throw new UnsupportedOperationException("key[]:{} 只支持 GET, GETS 方法！其它方法不允许传 " + name + ":{} 等这种 key[]:{} 格式！");
		}
		if (request == null || request.isEmpty()) { // jsonKey-jsonValue 条件
			return null;
		}
		String path = getAbsPath(parentPath, name);


		//不能改变，因为后面可能继续用到，导致1以上都改变 []:{0:{Comment[]:{0:{Comment:{}},1:{...},...}},1:{...},...}
		final String query = request.getString(JSONRequest.KEY_QUERY);
		final Boolean compat = request.getBoolean(JSONRequest.KEY_COMPAT);
		final Integer count = request.getInteger(JSONRequest.KEY_COUNT); //TODO 如果不想用默认数量可以改成 getIntValue(JSONRequest.KEY_COUNT);
		final Integer page = request.getInteger(JSONRequest.KEY_PAGE);
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

		int page2 = page == null ? 0 : page;
		int maxPage = getMaxQueryPage();
		if (page2 < 0 || page2 > maxPage) {
			throw new IllegalArgumentException(path + "/" + JSONRequest.KEY_PAGE + ":value 中 value 的值不合法！必须在 0-" + maxPage + " 内 !");
		}

		//不用total限制数量了，只用中断机制，total只在query = 1,2的时候才获取
		int count2 = isSubquery || count != null ? (count == null ? 0 : count) : getDefaultQueryCount();
		int max = isSubquery ? count2 : getMaxQueryCount();

		if (count2 < 0 || count2 > max) {
			throw new IllegalArgumentException(path + "/" + JSONRequest.KEY_COUNT + ":value 中 value 的值不合法！必须在 0-" + max + " 内 !");
		}

		request.remove(JSONRequest.KEY_QUERY);
		request.remove(JSONRequest.KEY_COMPAT);
		request.remove(JSONRequest.KEY_COUNT);
		request.remove(JSONRequest.KEY_PAGE);
		request.remove(JSONRequest.KEY_JOIN);
		Log.d(TAG, "onArrayParse  query = " + query + "; count = " + count + "; page = " + page + "; join = " + join);

		if (request.isEmpty()) { // 如果条件成立，说明所有的 parentPath/name:request 中request都无效！！！ 后续都不执行，没必要还原数组关键词浪费性能
			Log.e(TAG, "onArrayParse  request.isEmpty() >> return null;");
			return null;
		}

		JSONArray response = null;
		try {
			int size = count2 == 0 ? max : count2; //count为每页数量，size为第page页实际数量，max(size) = count
			Log.d(TAG, "onArrayParse  size = " + size + "; page = " + page2);


			//key[]:{Table:{}}中key equals Table时 提取Table
			int index = isSubquery || name == null ? -1 : name.lastIndexOf("[]");
			String childPath = index <= 0 ? null : Pair.parseEntry(name.substring(0, index), true).getKey(); // Table-key1-key2...

			String arrTableKey = null;
			//判断第一个key，即Table是否存在，如果存在就提取
			String[] childKeys = StringUtil.split(childPath, "-", false);
			if (childKeys == null || childKeys.length <= 0 || request.containsKey(childKeys[0]) == false) {
				childKeys = null;
			}
			else if (childKeys.length == 1 && JSONRequest.isTableKey(childKeys[0])) {  // 可能无需提取，直接返回 rawList 即可
				arrTableKey = childKeys[0];
			}


			//Table<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			response = new JSONArray();
			SQLConfig config = createSQLConfig()
					.setMethod(requestMethod)
					.setCount(size)
					.setPage(page2)
					.setQuery(query2)
					.setCompat(compat)
					.setTable(arrTableKey)
					.setJoinList(onJoinParse(join, request));

			JSONObject parent;

			boolean isExtract = true;

			//生成size个
			for (int i = 0; i < (isSubquery ? 1 : size); i++) {
				parent = onObjectParse(request, isSubquery ? parentPath : path, isSubquery ? name : "" + i, config.setType(SQLConfig.TYPE_ITEM).setPosition(i), isSubquery);
				if (parent == null || parent.isEmpty()) {
					break;
				}

				long startTime = System.currentTimeMillis();

				/* 这里优化了 Table[]: { Table:{} } 这种情况下的性能
				 * 如果把 List<JSONObject> 改成 JSONArray 来减少以下 addAll 一次复制，则会导致 AbstractSQLExecutor 等其它很多地方 get 要改为 getJSONObject，
				 * 修改类型会导致不兼容旧版依赖 ORM 的项目，而且整体上性能只有特殊情况下性能提升，其它非特殊情况下因为多出很多 instanceof JSONObject 的判断而降低了性能。
				 */
				JSONObject fo = i != 0 || arrTableKey == null ? null : parent.getJSONObject(arrTableKey);
				@SuppressWarnings("unchecked")
				List<JSONObject> list = fo == null ? null : (List<JSONObject>) fo.remove(AbstractSQLExecutor.KEY_RAW_LIST);

				if (list != null && list.isEmpty() == false) {
					isExtract = false;

					list.set(0, fo);  // 不知道为啥第 0 项也加了 @RAW@LIST
					response.addAll(list);  // List<JSONObject> cannot match List<Object>   response = new JSONArray(list);

					long endTime = System.currentTimeMillis();  // 0ms
					Log.d(TAG, "\n onArrayParse <<<<<<<<<<<<<<<<<<<<<<<<<<<<\n for (int i = 0; i < (isSubquery ? 1 : size); i++) "
							+ " startTime = " + startTime + "; endTime = " + endTime + "; duration = " + (endTime - startTime) + "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
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
			if (isExtract) {
				long startTime = System.currentTimeMillis();

				Object fo = childKeys == null || response.isEmpty() ? null : response.get(0);
				if (fo instanceof Boolean || fo instanceof Number || fo instanceof String) { //[{}] 和 [[]] 都没意义
					putQueryResult(path, response);
				}

				long endTime = System.currentTimeMillis();
				Log.d(TAG, "\n onArrayParse <<<<<<<<<<<<<<<<<<<<<<<<<<<<\n isExtract >> putQueryResult "
						+ " startTime = " + startTime + "; endTime = " + endTime + "; duration = " + (endTime - startTime) + "\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
			}

		} finally {
			//后面还可能用到，要还原
			request.put(JSONRequest.KEY_QUERY, query);
			request.put(JSONRequest.KEY_COMPAT, compat);
			request.put(JSONRequest.KEY_COUNT, count);
			request.put(JSONRequest.KEY_PAGE, page);
			request.put(JSONRequest.KEY_JOIN, join);
		}

		if (Log.DEBUG) {
			Log.i(TAG, "onArrayParse  return response = \n" + JSON.toJSONString(response) + "\n>>>>>>>>>>>>>>>\n\n\n");
		}
		return response;
	}



	private static final List<String> JOIN_COPY_KEY_LIST;
	static {  // TODO 不全
		JOIN_COPY_KEY_LIST = new ArrayList<String>();
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_ROLE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_DATABASE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_SCHEMA);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_DATASOURCE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_COLUMN);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_NULL);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_CAST);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_COMBINE);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_GROUP);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_HAVING);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_HAVING_AND);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_ORDER);
		JOIN_COPY_KEY_LIST.add(JSONRequest.KEY_RAW);
	}

	/**JOIN 多表同时筛选
	 * @param join "&/User,</Moment/id@",@/Comment/toId@" 或 "&/User":{}, "</Moment/id@":{"@column":"id"}, "@/Comment/toId@": {"@group":"toId", "@having":"toId>0"}
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
			Log.e(TAG, "onJoinParse  set == null || set.isEmpty() >> return null;");
			return null;
		}

		List<Join> joinList = new ArrayList<>();

		for (Entry<String, Object> e : set) {  // { &/User:{}, </Moment/id@":{}, @/Comment/toId@:{} }
			// 分割 /Table/key
			String path = e == null ? null : e.getKey();
			Object outer = path == null ? null : e.getValue();

			if (outer instanceof JSONObject == false) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中value不合法！"
						+ "必须为 &/Table0/key0,</Table1/key1,... 或 { '&/Table0/key0':{}, '</Table1/key1':{},... } 这种形式！");
			}

			int index = path == null ? -1 : path.indexOf("/");
			if (index < 0) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中 value 值 " + path + " 不合法！"
						+ "必须为 &/Table0,</Table1/key1,@/Table1:alias2/key2,... 或 { '&/Table0':{}, '</Table1/key1':{},... } 这种形式！");
			}
			String joinType = path.substring(0, index); //& | ! < > ( ) <> () *
			//			if (StringUtil.isEmpty(joinType, true)) {
			//				joinType = "|"; // FULL JOIN
			//			}
			path = path.substring(index + 1);

			index = path.lastIndexOf("/");
			String tableKey = index < 0 ? path : path.substring(0, index); // User:owner
      int index2 = tableKey.lastIndexOf("/");
      String arrKey = index2 < 0 ? null : tableKey.substring(0, index2);
      if (arrKey != null && JSONRequest.isArrayKey(arrKey) == false) {
        throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 对应的 " + arrKey + " 不是合法的数组 key[] ！" +
          "@ APP JOIN 最多允许跨 1 层，只能是子数组，且数组对象中不能有 join: value 键值对！");
      }

      tableKey = index2 < 0 ? tableKey : tableKey.substring(index2+1);

      apijson.orm.Entry<String, String> entry = Pair.parseEntry(tableKey, true);
      String table = entry.getKey(); // User
			if (StringUtil.isName(table) == false) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中 value 的 Table 值 " + table + " 不合法！"
						+ "必须为 &/Table0,</Table1/key1,@/Table1:alias2/key2,... 或 { '&/Table0':{}, '</Table1/key1':{},... } 这种格式！"
						+ "且 Table 必须满足大写字母开头的表对象英文单词 key 格式！");
			}

			String alias = entry.getValue(); // owner
			if (StringUtil.isNotEmpty(alias, true) && StringUtil.isName(alias) == false) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中 value 的 alias 值 " + alias + " 不合法！"
						+ "必须为 &/Table0,</Table1/key1,@/Table1:alias2/key2,... 或 { '&/Table0':{}, '</Table1/key1':{},... } 这种格式！"
						+ "且 Table:alias 的 alias 必须满足英文单词变量名格式！");
			}

			// 取出Table对应的JSONObject，及内部引用赋值 key:value
			JSONObject tableObj;
			JSONObject parentPathObj;	// 保留
			try {
        parentPathObj = arrKey == null ? request : request.getJSONObject(arrKey);	// 保留
        tableObj = parentPathObj == null ? null : parentPathObj.getJSONObject(tableKey);
        if (tableObj == null) {
					throw new NullPointerException("tableObj == null");
				}
			}
			catch (Exception e2) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 对应的 " + tableKey + ":value 中 value 类型不合法！" +
          "必须是 {} 这种 JSONObject 格式！" + e2.getMessage());
			}

      if (arrKey != null) {
        if (parentPathObj.get(JSONRequest.KEY_JOIN) != null) {
          throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 对应的 " + arrKey + ":{ join: value } 中 value 不合法！" +
            "@ APP JOIN 最多允许跨 1 层，只能是子数组，且数组对象中不能有 join: value 键值对！");
        }

        Integer subPage = parentPathObj.getInteger(JSONRequest.KEY_PAGE);
        if (subPage != null && subPage != 0) {
          throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 对应的 " + arrKey + ":{ page: value } 中 value 不合法！" +
            "@ APP JOIN 最多允许跨 1 层，只能是子数组，且数组对象中 page 值只能为 null 或 0 ！");
        }
      }

      boolean isAppJoin = "@".equals(joinType);

			JSONObject refObj = new JSONObject(tableObj.size(), true);

			String key = index < 0 ? null : path.substring(index + 1); // id@
			if (key != null) {  // 指定某个 key 为 JOIN ON 条件
				if (key.indexOf("@") != key.length() - 1) {
					throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":" + e.getKey() + " 中 " + key + " 不合法！"
							+ "必须为 &/Table0,</Table1/key1,@/Table1:alias2/key2,... 或 { '&/Table0':{}, '</Table1/key1':{},... } 这种格式！"
							+ "且 Table:alias 的 alias 必须满足英文单词变量名格式！");
				}

				if (tableObj.get(key) instanceof String == false) {
					throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":" + e.getKey() + "' 对应的 "
            + tableKey + ":{ " + key + ": value } 中 value 类型不合法！必须为同层级引用赋值路径 String！");
				}

        if (isAppJoin && StringUtil.isName(key.substring(0, key.length() - 1)) == false) {
          throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 中 " + key + " 不合法 ！" +
            "@ APP JOIN 只允许 key@:/Table/refKey 这种 = 等价连接！");
        }

				refObj.put(key, tableObj.getString(key));
			}


			Set<Entry<String, Object>> tableSet = tableObj.entrySet();
			// 取出所有 join 条件
			JSONObject requestObj = new JSONObject(true); // (JSONObject) obj.clone();

			boolean matchSingle = false;
			for (Entry<String, Object> tableEntry : tableSet) {
				String k = tableEntry.getKey();
				Object v = k == null ? null : tableEntry.getValue();
				if (v == null) {
					continue;
				}

				matchSingle = matchSingle == false && k.equals(key);
				if (matchSingle) {
					continue;
				}

				if (k.length() > 1 && k.indexOf("@") == k.length() - 1 && v instanceof String) {
					String sv = (String) v;
					int ind = sv.endsWith("@") ? -1 : sv.indexOf("/");
					if (ind == 0 && key == null) {  // 指定了某个就只允许一个 ON 条件
						String p = sv.substring(1);
						int ind2 = p.indexOf("/");
						String tk = ind2 < 0 ? null : p.substring(0, ind2);

						apijson.orm.Entry<String, String> te = tk == null || p.substring(ind2 + 1).indexOf("/") >= 0 ? null : Pair.parseEntry(tk, true);

						if (te != null && JSONRequest.isTableKey(te.getKey()) && request.get(tk) instanceof JSONObject) {
              if (isAppJoin) {
                if (refObj.size() >= 1) {
                  throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":" + e.getKey() + " 中 " + k + " 不合法！"
                    + "@ APP JOIN 必须有且只有一个引用赋值键值对！");
                }

                if (StringUtil.isName(k.substring(0, k.length() - 1)) == false) {
                  throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":'" + e.getKey() + "' 中 " + k + " 不合法 ！" +
                    "@ APP JOIN 只允许 key@:/Table/refKey 这种 = 等价连接！");
                }
              }

              refObj.put(k, v);
							continue;
						}
					}

					Object rv = getValueByPath(sv);
					if (rv != null && rv.equals(sv) == false) {
						requestObj.put(k.substring(0, k.length() - 1), rv);
						continue;
					}

					throw new UnsupportedOperationException(table + "/" + k + " 不合法！" + JSONRequest.KEY_JOIN + " 关联的 Table 中，"
							+ "join: ?/Table/key 时只能有 1 个 key@:value；join: ?/Table 时所有 key@:value 要么是符合 join 格式，要么能直接解析成具体值！");  // TODO 支持 join on
				}

				if (k.startsWith("@")) {
					if (JOIN_COPY_KEY_LIST.contains(k)) {
						requestObj.put(k, v); // 保留
					}
				}
				else {
					if (k.endsWith("@")) {
						throw new UnsupportedOperationException(table + "/" + k + " 不合法！" + JSONRequest.KEY_JOIN + " 关联的 Table 中，"
								+ "join: ?/Table/key 时只能有 1 个 key@:value；join: ?/Table 时所有 key@:value 要么是符合 join 格式，要么能直接解析成具体值！");  // TODO 支持 join on
					}

					if (k.contains("()") == false) { // 不需要远程函数
						requestObj.put(k, v); // 保留
					}
				}
			}

			Set<Entry<String, Object>> refSet = refObj.entrySet();
			if (refSet.isEmpty() && "*".equals(joinType) == false) {
				throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中 value 的 alias 值 " + alias + " 不合法！"
						+ "必须为 &/Table0,</Table1/key1,@/Table1:alias2/key2,... 或 { '&/Table0':{}, '</Table1/key1':{},... } 这种格式！"
						+ "且 Table:alias 的 alias 必须满足英文单词变量名格式！");
			}


			Join j = new Join();
			j.setPath(e.getKey());
			j.setJoinType(joinType);
			j.setTable(table);
			j.setAlias(alias);
			j.setOuter((JSONObject) outer);
			j.setRequest(requestObj);
			if (arrKey != null) {
        Integer count = parentPathObj.getInteger(JSONRequest.KEY_COUNT);
        j.setCount(count == null ? getDefaultQueryCount() : count);
			}

			List<Join.On> onList = new ArrayList<>();
			for (Entry<String, Object> refEntry : refSet) {
				String originKey = refEntry.getKey();

				String targetPath = (String) refEntry.getValue();
				if (StringUtil.isEmpty(targetPath, true)) {
					throw new IllegalArgumentException(e.getKey() + ":value 中 value 值 " + targetPath + " 不合法！必须为引用赋值的路径 '/targetTable/targetKey' ！");
				}

				// 取出引用赋值路径 targetPath 对应的 Table 和 key
				index = targetPath.lastIndexOf("/");
				String targetKey = index < 0 ? null : targetPath.substring(index + 1);
				if (StringUtil.isName(targetKey) == false) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable/targetKey' 中 targetKey 值 " + targetKey + " 不合法！必须满足英文单词变量名格式！");
				}

				targetPath = targetPath.substring(0, index);
				index = targetPath.lastIndexOf("/");
				String targetTableKey = index < 0 ? targetPath : targetPath.substring(index + 1);

				// 主表允许别名
				apijson.orm.Entry<String, String> targetEntry = Pair.parseEntry(targetTableKey, true);
				String targetTable = targetEntry.getKey(); //User
				if (StringUtil.isName(targetTable) == false) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable/targetKey' 中 targetTable 值 " + targetTable + " 不合法！必须满足大写字母开头的表对象英文单词 key 格式！");
				}

				String targetAlias = targetEntry.getValue(); //owner
				if (StringUtil.isNotEmpty(targetAlias, true) && StringUtil.isName(targetAlias) == false) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable:targetAlias/targetKey' 中 targetAlias 值 " + targetAlias + " 不合法！必须满足英文单词变量名格式！");
				}

				targetTable = targetTableKey;  // 主表允许别名
				if (StringUtil.isName(targetTable) == false) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable/targetKey' 中 targetTable 值 " + targetTable + " 不合法！必须满足大写字母开头的表对象英文单词 key 格式！");
				}

				//对引用的JSONObject添加条件
				JSONObject targetObj;
				try {
					targetObj = request.getJSONObject(targetTableKey);
				}
				catch (Exception e2) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable/targetKey' 中路径对应的 '" + targetTableKey + "':value 中 value 类型不合法！必须是 {} 这种 JSONObject 格式！" + e2.getMessage());
				}

				if (targetObj == null) {
					throw new IllegalArgumentException(e.getKey() + ":'/targetTable/targetKey' 中路径对应的对象 '" + targetTableKey + "':{} 不存在或值为 null ！必须是 {} 这种 JSONObject 格式！");
				}

				Join.On on = new Join.On();
				on.setKeyAndType(j.getJoinType(), j.getTable(), originKey);
				if (StringUtil.isName(on.getKey()) == false) {
					throw new IllegalArgumentException(JSONRequest.KEY_JOIN + ":value 中 value 的 key@ 中 key 值 " + on.getKey() + " 不合法！必须满足英文单词变量名格式！");
				}

				on.setOriginKey(originKey);
				on.setOriginValue((String) refEntry.getValue());
				on.setTargetTable(targetTable);
				on.setTargetAlias(targetAlias);
				on.setTargetKey(targetKey);

				onList.add(on);
			}

			j.setOnList(onList);

			joinList.add(j);
			//			onList.add(table + "." + key + " = " + targetTable + "." + targetKey); // ON User.id = Moment.userId

			// 保证和 SQLExcecutor 缓存的 Config 里 where 顺序一致，生成的 SQL 也就一致 <<<<<<<<<
			// AbstractSQLConfig.newSQLConfig 中强制把 id, id{}, userId, userId{} 放到了最前面		tableObj.put(key, tableObj.remove(key));

			if (refObj.size() != tableObj.size()) {  // 把 key 强制放最前，AbstractSQLExcecutor 中 config.putWhere 也是放尽可能最前
				refObj.putAll(tableObj);
				parentPathObj.put(tableKey, refObj);

//				tableObj.clear();
//				tableObj.putAll(refObj);
			}
			// 保证和 SQLExcecutor 缓存的 Config 里 where 顺序一致，生成的 SQL 也就一致 >>>>>>>>>
		}

		//拼接多个 SQLConfig 的SQL语句，然后执行，再把结果分别缓存(Moment, User等)到 SQLExecutor 的 cacheMap
		//		AbstractSQLConfig config0 = null;
		//		String sql = "SELECT " + config0.getColumnString() + " FROM " + config0.getTable() + " INNER JOIN " + targetTable + " ON "
		//				+ onList.get(0) + config0.getGroupString() + config0.getHavingString() + config0.getOrderString();

		return joinList;
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
	public void putQueryResult(String path, Object result) {
		Log.i(TAG, "\n putQueryResult  valuePath = " + path + "; result = " + result + "\n <<<<<<<<<<<<<<<<<<<<<<<");
		//		if (queryResultMap.containsKey(valuePath)) {//只保存被关联的value
		Log.d(TAG, "putQueryResult  queryResultMap.containsKey(valuePath) >> queryResultMap.put(path, result);");
		queryResultMap.put(path, result);
		//		}
	}
	//CS304 Issue link: https://github.com/Tencent/APIJSON/issues/48
	/**根据路径获取值
	 * @param valuePath -the path need to get value
	 * @return parent == null ? valuePath : parent.get(keys[keys.length - 1])
	 * <p>use entrySet+getValue() to replace keySet+get() to enhance efficiency</p>
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
		JSONObject parent = null;
		String[] keys = null;
		for (Entry<String,Object> entry : queryResultMap.entrySet()){
			String path = entry.getKey();
			if (valuePath.startsWith(path + "/")) {
				try {
					parent = (JSONObject) entry.getValue();
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
			target = keys == null || keys.length <= 0 ? parent : parent.get(keys[keys.length - 1]); //值为null应该报错NotExistExeption，一般都是id关联，不可为null，否则可能绕过安全机制
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

	public static final String KEY_SQL = "sql";

	protected Map<String, List<JSONObject>> arrayMainCacheMap = new HashMap<>();
	public void putArrayMainCache(String arrayPath, List<JSONObject> mainTableDataList) {
		arrayMainCacheMap.put(arrayPath, mainTableDataList);
	}
	public List<JSONObject> getArrayMainCache(String arrayPath) {
		return arrayMainCacheMap.get(arrayPath);
	}
	public JSONObject getArrayMainCacheItem(String arrayPath, int position) {
		List<JSONObject> list = getArrayMainCache(arrayPath);
		return list == null || position >= list.size() ? null : list.get(position);
	}



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
			JSONObject result;

			boolean explain = config.isExplain();
			if (explain) {
				//如果先执行 explain，则 execute 会死循环，所以只能先执行非 explain
				config.setExplain(false); //对下面 config.getSQL(false); 生效
				JSONObject res = getSQLExecutor().execute(config, false);

				//如果是查询方法，才能执行explain
				if (RequestMethod.isQueryMethod(config.getMethod()) && config.isElasticsearch() == false){
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
				else {//如果是更新请求，不执行explain，但可以返回sql
					result = new JSONObject(true);
					result.put(KEY_SQL, config.getSQL(false));
					result.putAll(res);
				}
			}
			else {
				sqlExecutor = getSQLExecutor();
				result = sqlExecutor.execute(config, false);
				// FIXME 改为直接在 sqlExecutor 内加好，最后 Parser 取结果，可以解决并发执行导致内部计算出错
//				executedSQLDuration += sqlExecutor.getExecutedSQLDuration() + sqlExecutor.getSqlResultDuration();
			}

			return result;
		}
		catch (Exception e) {
      throw CommonException.wrap(e, config);
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
		// this.sqlExecutor.getTransactionIsolation() 只有json第一次执行才会设置, get请求=0
		if (RequestMethod.isQueryMethod(requestMethod) && this.sqlExecutor.getTransactionIsolation() == Connection.TRANSACTION_NONE ) {
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

	private void setOpMethod(JSONObject request,ObjectParser op, String key) {
		if(key != null && request.getString(apijson.JSONObject.KEY_METHOD) != null) {
			String _method = request.getString(apijson.JSONObject.KEY_METHOD);
			if( _method != null) {
				RequestMethod method = RequestMethod.valueOf(_method.toUpperCase());
				this.setMethod(method);
				op.setMethod(method);
			}
		}
	}

	protected JSONObject getRequestStructure(RequestMethod method, String tag, int version) throws Exception {
		// 获取指定的JSON结构 <<<<<<<<<<<<
		JSONObject object = null;
		String error = "";
		try {
			object = getStructure("Request", method.name(), tag, version);
		} catch (Exception e) {
			error = e.getMessage();
		}
		if (object == null) { // empty表示随意操作 || object.isEmpty()) {
			throw new UnsupportedOperationException("找不到 version: " + version + ", method: " + method.name() + ", tag: " + tag + " 对应的 structure ！" + "非开放请求必须是后端 Request 表中校验规则允许的操作！\n " + error + "\n如果需要则在 Request 表中新增配置！");
		}
		return object;
	}

	private JSONObject batchVerify(RequestMethod method, String tag, int version, String name, @NotNull JSONObject request, int maxUpdateCount, SQLCreator creator) throws Exception {
		JSONObject jsonObject = new JSONObject(true);
		List<String> removeTmpKeys = new ArrayList<>(); // 请求json里面的临时变量,不需要带入后面的业务中,比如 @post、@get等
		if (request.keySet() == null || request.keySet().size() == 0) {
			throw new IllegalArgumentException("json对象格式不正确 ！,例如 \"User\": {}");
		}

		for (String key : request.keySet()) {
			// key重复直接抛错(xxx:alias, xxx:alias[])
			if (jsonObject.containsKey(key) || jsonObject.containsKey(key + apijson.JSONObject.KEY_ARRAY)) {
				throw new IllegalArgumentException("对象名重复,请添加别名区分 ! ,重复对象名为: " + key);
			}

			// @post、@get等RequestMethod
			try {
				if (key.startsWith("@")) {
					try {
						// 如果不匹配,异常不处理即可
						RequestMethod _method = RequestMethod.valueOf(key.substring(1).toUpperCase());
						removeTmpKeys.add(key);
						for (String objKey : request.getJSONObject(key).keySet()) {
							Map<String, Object> object_attributes_map = new HashMap<>();
							object_attributes_map.put(apijson.JSONObject.KEY_METHOD, _method);
							keyObjectAttributesMap.put(objKey, object_attributes_map);
							JSONObject objAttrJson = request.getJSONObject(key).getJSONObject(objKey);
							for (String objAttr : objAttrJson.keySet()) {
								switch (objAttr) {
								case apijson.JSONObject.KEY_DATASOURCE:
									object_attributes_map.put(apijson.JSONObject.KEY_DATASOURCE, objAttrJson.getString(objAttr));
									break;
								case apijson.JSONObject.KEY_SCHEMA:
									object_attributes_map.put(apijson.JSONObject.KEY_SCHEMA, objAttrJson.getString(objAttr));
									break;
								case apijson.JSONObject.KEY_DATABASE:
									object_attributes_map.put(apijson.JSONObject.KEY_DATABASE, objAttrJson.getString(objAttr));
									break;
								case apijson.JSONObject.VERSION:
									object_attributes_map.put(apijson.JSONObject.VERSION, objAttrJson.getString(objAttr));
									break;
								case apijson.JSONObject.KEY_ROLE:
									object_attributes_map.put(apijson.JSONObject.KEY_ROLE, objAttrJson.getString(objAttr));
									break;
								case JSONRequest.KEY_TAG:
									object_attributes_map.put(JSONRequest.KEY_TAG, objAttrJson.getString(objAttr));
									break;
								default:
									break;
								}
							}
						}
						continue;
					} catch (Exception e) {
					}
				}
				
				// 1、非crud,对于没有显式声明操作方法的，直接用 URL(/get, /post 等) 对应的默认操作方法
				// 2、crud, 没有声明就用 GET
				// 3、兼容 sql@ JSONObject,设置 GET方法
				// 将method 设置到每个object, op执行会解析
				if (request.get(key) instanceof JSONObject) {
					if (keyObjectAttributesMap.get(key) == null) {
						// 数组会解析为对象进行校验,做一下兼容
						if (keyObjectAttributesMap.get(key + apijson.JSONObject.KEY_ARRAY) == null) {
							if (method == RequestMethod.CRUD || key.endsWith("@")) {
								request.getJSONObject(key).put(apijson.JSONObject.KEY_METHOD, GET);
								if(keyObjectAttributesMap.get(key) == null) {
									Map<String, Object> object_attributes_map = new HashMap<>();
									object_attributes_map.put(apijson.JSONObject.KEY_METHOD, GET);
									keyObjectAttributesMap.put(key, object_attributes_map);
								}else {
									keyObjectAttributesMap.get(key).put(apijson.JSONObject.KEY_METHOD, GET);
								}
							} else {
								request.getJSONObject(key).put(apijson.JSONObject.KEY_METHOD, method);
								if(keyObjectAttributesMap.get(key) == null) {
									Map<String, Object> object_attributes_map = new HashMap<>();
									object_attributes_map.put(apijson.JSONObject.KEY_METHOD, method);
									keyObjectAttributesMap.put(key, object_attributes_map);
								}else {
									keyObjectAttributesMap.get(key).put(apijson.JSONObject.KEY_METHOD, method);
								}
							}
						} else {
							setRequestAttribute(key, true, apijson.JSONObject.KEY_METHOD, request);
							setRequestAttribute(key, true, apijson.JSONObject.KEY_DATASOURCE, request);
							setRequestAttribute(key, true, apijson.JSONObject.KEY_SCHEMA, request);
							setRequestAttribute(key, true, apijson.JSONObject.KEY_DATABASE, request);
							setRequestAttribute(key, true, apijson.JSONObject.VERSION, request);
							setRequestAttribute(key, true, apijson.JSONObject.KEY_ROLE, request);
						}
					} else {
						setRequestAttribute(key, false, apijson.JSONObject.KEY_METHOD, request);
						setRequestAttribute(key, false, apijson.JSONObject.KEY_DATASOURCE, request);
						setRequestAttribute(key, false, apijson.JSONObject.KEY_SCHEMA, request);
						setRequestAttribute(key, false, apijson.JSONObject.KEY_DATABASE, request);
						setRequestAttribute(key, false, apijson.JSONObject.VERSION, request);
						setRequestAttribute(key, false, apijson.JSONObject.KEY_ROLE, request);
					}
				}
				
				if (key.startsWith("@") || key.endsWith("@")) {
					jsonObject.put(key, request.get(key));
					continue;
				}

				if (request.get(key) instanceof JSONObject || request.get(key) instanceof JSONArray) {
					RequestMethod  _method = null;
					if (request.get(key) instanceof JSONObject) {
						_method = RequestMethod.valueOf(request.getJSONObject(key).getString(apijson.JSONObject.KEY_METHOD).toUpperCase());
						String combine = request.getJSONObject(key).getString(KEY_COMBINE);
						if (combine != null && RequestMethod.isPublicMethod(_method) == false) {
							throw new IllegalArgumentException(key + ":{} 里的 @combine:value 不合法！开放请求 GET、HEAD 才允许传 @combine:value !");
						}
					} else {
						if (keyObjectAttributesMap.get(key) == null) {
							if (method == RequestMethod.CRUD) {
								_method = GET;
								if(keyObjectAttributesMap.get(key) == null) {
									Map<String, Object> object_attributes_map = new HashMap<>();
									object_attributes_map.put(apijson.JSONObject.KEY_METHOD, GET);
									keyObjectAttributesMap.put(key, object_attributes_map);
								}else {
									keyObjectAttributesMap.get(key).put(apijson.JSONObject.KEY_METHOD, GET);
								}
							} else {
								_method = method;
								if(keyObjectAttributesMap.get(key) == null) {
									Map<String, Object> object_attributes_map = new HashMap<>();
									object_attributes_map.put(apijson.JSONObject.KEY_METHOD, method);
									keyObjectAttributesMap.put(key, object_attributes_map);
								}else {
									keyObjectAttributesMap.get(key).put(apijson.JSONObject.KEY_METHOD, method);
								}
							}
						} else {
							_method = (RequestMethod) keyObjectAttributesMap.get(key).get(apijson.JSONObject.KEY_METHOD);
						}
					}

					// 非 CRUD 方法，都只能和 URL method 完全一致，避免意料之外的安全风险。
					if (method != RequestMethod.CRUD && _method != method) {
						throw new IllegalArgumentException("不支持在 " + method + " 中 " + _method + " ！");
					}
					
					// get请求不校验
					if (RequestMethod.isPublicMethod(_method)) {
						jsonObject.put(key, request.get(key));
						continue;
					}
					
					String _tag = buildTag(request, key, method, tag);
					JSONObject requestItem = new JSONObject();
					// key 处理别名
					String _key = keyDelAliase(key);
					requestItem.put(_key, request.get(key));
					JSONObject object = getRequestStructure(_method, _tag, version);
					JSONObject ret = objectVerify(_method, _tag, version, name, requestItem, maxUpdateCount, creator, object);
					jsonObject.put(key, ret.get(_key));
				} else {
					jsonObject.put(key, request.get(key));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
		// 这里是requestObject ref request 的引用, 删除不需要的临时变量
		for(String removeKey : removeTmpKeys) {
			request.remove(removeKey);
		}
		return jsonObject;
	}
	
	private void setRequestAttribute(String key, boolean isArray, String attrKey, @NotNull JSONObject request) {
		Object attrVal = null;
		if(isArray) {
			attrVal = keyObjectAttributesMap.get(key + apijson.JSONObject.KEY_ARRAY).get(attrKey);
		}else {
			attrVal = keyObjectAttributesMap.get(key).get(attrKey);
		}
		
		if(attrVal != null && request.getJSONObject(key).get(attrKey) == null) {
			// 如果对象内部已经包含该属性,不覆盖
			request.getJSONObject(key).put(attrKey, attrVal);
		}
	}
	
	private String keyDelAliase(String key) {
		int keyIndex = key.indexOf(":");
		if (keyIndex != -1) {
			String _key = key.substring(0, keyIndex);
			if (apijson.JSONObject.isTableArray(key)) {
				_key += apijson.JSONObject.KEY_ARRAY;
			}
			return _key;
		}
		return key;
	}
	
	private String buildTag(JSONObject request, String key, RequestMethod method, String tag) {
		if (method == RequestMethod.CRUD) {
			if (keyObjectAttributesMap.get(key) != null && keyObjectAttributesMap.get(key).get(JSONRequest.KEY_TAG) != null) {
				if (request.get(key) instanceof JSONArray) {
					return keyObjectAttributesMap.get(key).get(JSONRequest.KEY_TAG).toString();
				} else {
					tag = keyObjectAttributesMap.get(key).get(JSONRequest.KEY_TAG).toString();
				}
			} else {
				// key 作为默认的 tag
				if (StringUtil.isEmpty(tag)) {
					if (request.get(key) instanceof JSONArray) {
						return keyDelAliase(key);
					} else {
						tag = key;
					}
				} else {
					if (request.get(key) instanceof JSONArray) {
						return tag;
					}
				}
			}
		} else {
			if (StringUtil.isEmpty(tag, true)) {
				throw new IllegalArgumentException("请在最外层传 tag ！一般是 Table 名，例如 \"tag\": \"User\" ");
			}
			if (request.get(key) instanceof JSONArray) {
				return tag;
			}
		}
		
		// 通用判断
		// 对象, 需处理别名
		if (request.get(key) instanceof JSONObject && StringUtil.isNotEmpty(tag)) {
			int keyIndex = tag.indexOf(":");
			if (keyIndex != -1) {
				return tag.substring(0, keyIndex);
			}
			return tag;
		}
		return tag;
	}
	

	protected JSONObject objectVerify(RequestMethod method, String tag, int version, String name, @NotNull JSONObject request, int maxUpdateCount, SQLCreator creator, JSONObject object) throws Exception {
		// 获取指定的JSON结构 >>>>>>>>>>>>>>
		JSONObject target = wrapRequest(method, tag, object, true);
		// JSONObject clone 浅拷贝没用，Structure.parse 会导致 structure 里面被清空，第二次从缓存里取到的就是 {}
		return getVerifier().verifyRequest(method, name, target, request, maxUpdateCount, getGlobalDatabase(), getGlobalSchema(), creator);
	}
	
	/***
	 * 兼容url crud, 获取真实method
	 * @param method = crud
	 * @param key
	 * @return
	 */
	public RequestMethod getRealMethod(RequestMethod method, String key, Object value) {
		if(method == CRUD && (value instanceof JSONObject || value instanceof JSONArray)) {
			if (this.keyObjectAttributesMap.get(key) == null || this.keyObjectAttributesMap.get(key).get(apijson.JSONObject.KEY_METHOD) == null) {
				return method;
			}
			return (RequestMethod)this.keyObjectAttributesMap.get(key).get(apijson.JSONObject.KEY_METHOD);
		}
		return method;
	}
}
