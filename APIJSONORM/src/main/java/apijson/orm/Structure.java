/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.util.Map;
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONObject;

import apijson.JSON;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.orm.AbstractSQLConfig.Callback;
import apijson.orm.AbstractSQLConfig.IdCallback;

/**结构类。已整合进 AbstractVerifier，最快 4.5.0 移除
 * 增删改查: OPERATION(ADD,REPLACE,PUT,REMOVE)   OPERATION:{key0:value0, key1:value1 ...}
 * 对值校验: VERIFY:{key0:value0, key1:value1 ...}  (key{}:range,key$:"%m%"等)
 * 对值重复性校验: UNIQUE:"key0:, key1 ..."  (UNIQUE:"phone,email" 等)
 * @author Lemon
 */
@Deprecated
public class Structure {
	public static final String TAG = "Structure";

	public static final Map<String, Pattern> COMPILE_MAP = AbstractVerifier.COMPILE_MAP;

	private Structure() {}


	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @param creator
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parseRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final SQLCreator creator) throws Exception {
		return parseRequest(method, name, target, request, Parser.MAX_UPDATE_COUNT, creator);
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
	public static JSONObject parseRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount, final SQLCreator creator) throws Exception {
		return parseRequest(method, name, target, request, maxUpdateCount, null, null, null, creator);
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
	public static JSONObject parseRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request, final int maxUpdateCount
			, final String database, final String schema, final IdCallback idCallback, final SQLCreator creator) throws Exception {

		return AbstractVerifier.verifyRequest(method, name, target, request, maxUpdateCount, database, schema, idCallback, creator);
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
	public static JSONObject parseResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, SQLCreator creator, OnParseCallback callback) throws Exception {
		return parseResponse(method, name, target, response, null, null, null, creator, callback);
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
	public static JSONObject parseResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, final String database, final String schema
			, final Callback idKeyCallback, SQLCreator creator, OnParseCallback callback) throws Exception {

		Log.i(TAG, "parseResponse  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n response = \n" + JSON.toJSONString(response));

		if (target == null || response == null) {// || target.isEmpty() {
			Log.i(TAG, "parseRequest  target == null || response == null >> return response;");
			return response;
		}

		//解析
		return parse(method, name, target, response, database, schema, idKeyCallback, creator, callback != null ? callback : new OnParseCallback() {});
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
	 * @param idKey
	 * @param userIdKey
	 * @param creator
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parse(@NotNull final RequestMethod method, String name, JSONObject target, JSONObject real
			, final String database, final String schema, final IdCallback idCallback, SQLCreator creator, @NotNull OnParseCallback callback) throws Exception {
		return AbstractVerifier.parse(method, name, target, real, database, schema, idCallback, creator, callback);
	}



	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param real
	 * @throws Exception 
	 */
	public static void type(@NotNull String tk, Object tv, @NotNull JSONObject real) throws UnsupportedDataTypeException {
		if (tv instanceof String == false) {
			throw new UnsupportedDataTypeException("服务器内部错误，" + tk + ":value 的value不合法！"
					+ "Request表校验规则中 TYPE:{ key:value } 中的value只能是String类型！");
		}

		type(tk, (String) tv, real.get(tk));
	}
	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param rv
	 * @throws Exception 
	 */
	public static void type(@NotNull String tk, @NotNull String tv, Object rv) throws UnsupportedDataTypeException {
		type(tk, tv, rv, false);
	}
	/**验证值类型
	 * @param tk
	 * @param tv {@link Operation}
	 * @param rv
	 * @param isInArray
	 * @throws Exception 
	 */
	public static void type(@NotNull String tk, @NotNull String tv, Object rv, boolean isInArray) throws UnsupportedDataTypeException {
		AbstractVerifier.verifyType(tk, tv, rv, isInArray);
	}


	/**验证是否存在
	 * @param table
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void verifyExist(String table, String key, Object value, long exceptId, @NotNull SQLCreator creator) throws Exception {
		AbstractVerifier.verifyExist(table, key, value, exceptId, creator);
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
		AbstractVerifier.verifyRepeat(table, key, value, exceptId, idKey, creator);
	}


}
