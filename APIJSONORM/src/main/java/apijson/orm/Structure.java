/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import static apijson.JSONObject.KEY_ID;
import static apijson.JSONObject.KEY_USER_ID;
import static apijson.orm.Operation.DISALLOW;
import static apijson.orm.Operation.EXIST;
import static apijson.orm.Operation.INSERT;
import static apijson.orm.Operation.NECESSARY;
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
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.exception.ConflictException;
import apijson.orm.model.Test;

/**结构类
 * 增删改查: OPERATION(ADD,REPLACE,PUT,REMOVE)   OPERATION:{key0:value0, key1:value1 ...}
 * 对值校验: VERIFY:{key0:value0, key1:value1 ...}  (key{}:range,key$:"%m%"等)
 * 对值重复性校验: UNIQUE:"key0:, key1 ..."  (UNIQUE:"phone,email" 等)
 * @author Lemon
 */
public class Structure {
	public static final String TAG = "Structure";

	public static final Map<String, Pattern> COMPILE_MAP;
	static {
		COMPILE_MAP = new HashMap<String, Pattern>();
	}

	
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
		Log.i(TAG, "parseRequest  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request));
		if (target == null || request == null) {// || request.isEmpty()) {
			Log.i(TAG, "parseRequest  target == null || request == null >> return null;");
			return null;
		}

		//已在 Verifier 中处理
		//		if (RequestRole.get(request.getString(JSONRequest.KEY_ROLE)) == RequestRole.ADMIN) {
		//			throw new IllegalArgumentException("角色设置错误！不允许在写操作Request中传 " + name + 
		//					":{ " + JSONRequest.KEY_ROLE + ":admin } ！");
		//		}

		//解析
		return parse(method, name, target, request, creator, new OnParseCallback() {

			@Override
			public JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
				//				Log.i(TAG, "parseRequest.parse.onParseJSONObject  key = " + key + "; robj = " + robj);
				if (robj == null) {
					if (tobj != null) {//不允许不传Target中指定的Table
						throw new IllegalArgumentException(method + "请求，请在 " + name + " 内传 " + key + ":{} ！");
					}
				} else if (apijson.JSONObject.isTableKey(key)) {
					if (method == RequestMethod.POST) {
						if (robj.containsKey(KEY_ID)) {
							throw new IllegalArgumentException(method + "请求，" + name + "/" + key + " 不能传 " + KEY_ID + " ！");
						}
					} else {
						if (RequestMethod.isQueryMethod(method) == false) {
							verifyId(method.name(), name, key, robj, KEY_ID, maxUpdateCount, true);
							verifyId(method.name(), name, key, robj, KEY_USER_ID, maxUpdateCount, false);
						}
					}
				} 

				return parseRequest(method, key, tobj, robj, maxUpdateCount, creator);
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
				if (o != null && o instanceof Number == false && o instanceof String == false) {
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
	 * @param callback 
	 * @param creator 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parseResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, SQLCreator creator, OnParseCallback callback) throws Exception {
		Log.i(TAG, "parseResponse  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n response = \n" + JSON.toJSONString(response));
		if (target == null || response == null) {// || target.isEmpty() {
			Log.i(TAG, "parseRequest  target == null || response == null >> return response;");
			return response;
		}

		//解析
		return parse(method, name, target, response, creator, callback != null ? callback : new OnParseCallback() {});
	}


	/**对request和response不同的解析用callback返回
	 * @param target
	 * @param request
	 * @param callback
	 * @param creator 
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject parse(@NotNull final RequestMethod method, String name, JSONObject target, JSONObject real
			, SQLCreator creator, @NotNull OnParseCallback callback) throws Exception {
		if (target == null) {
			return null;
		}


		//获取配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject type = target.getJSONObject(TYPE.name());
		JSONObject verify = target.getJSONObject(VERIFY.name());
		JSONObject insert = target.getJSONObject(INSERT.name());
		JSONObject update = target.getJSONObject(UPDATE.name());
		JSONObject replace = target.getJSONObject(REPLACE.name());

		String exist = StringUtil.getNoBlankString(target.getString(EXIST.name()));
		String unique = StringUtil.getNoBlankString(target.getString(UNIQUE.name()));
		String remove = StringUtil.getNoBlankString(target.getString(REMOVE.name()));
		String necessary = StringUtil.getNoBlankString(target.getString(NECESSARY.name()));
		String disallow = StringUtil.getNoBlankString(target.getString(DISALLOW.name()));

		target.remove(TYPE.name());
		target.remove(VERIFY.name());
		target.remove(INSERT.name());
		target.remove(UPDATE.name());
		target.remove(REPLACE.name());

		target.remove(EXIST.name());
		target.remove(UNIQUE.name());
		target.remove(REMOVE.name());
		target.remove(NECESSARY.name());
		target.remove(DISALLOW.name());



		//移除字段<<<<<<<<<<<<<<<<<<<
		String[] removes = StringUtil.split(remove);
		if (removes != null && removes.length > 0) {
			for (String r : removes) {
				real.remove(r);
			}
		}
		//移除字段>>>>>>>>>>>>>>>>>>>

		//判断必要字段是否都有<<<<<<<<<<<<<<<<<<<
		String[] necessarys = StringUtil.split(necessary);
		List<String> necessaryList = necessarys == null ? new ArrayList<String>() : Arrays.asList(necessarys);
		for (String s : necessaryList) {
			if (real.get(s) == null) {//可能传null进来，这里还会通过 real.containsKey(s) == false) {
				throw new IllegalArgumentException(method + "请求，" + name
						+ " 里面不能缺少 " + s + " 等[" + necessary + "]内的任何字段！");
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
				if (key == null) {
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
		List<String> disallowList = new ArrayList<String>();
		if ("!".equals(disallow)) {//所有非necessary，改成 !necessary 更好
			for (String key : rkset) {//对@key放行，@role,@column,自定义@position等
				if (key != null && key.startsWith("@") == false
						&& necessaryList.contains(key) == false && objKeySet.contains(key) == false) {
					disallowList.add(key);
				}
			}
		} else {
			String[] disallows = StringUtil.split(disallow);
			if (disallows != null && disallows.length > 0) {
				disallowList.addAll(Arrays.asList(disallows));
			}
		}
		//解析不允许的字段>>>>>>>>>>>>>>>>>>>


		//判断不允许传的key<<<<<<<<<<<<<<<<<<<<<<<<<
		for (String rk : rkset) {
			if (disallowList.contains(rk)) { //不允许的字段
				throw new IllegalArgumentException(method + "请求，" + name
						+ " 里面不允许传 " + rk + " 等" + StringUtil.getString(disallowList) + "内的任何字段！");
			}

			if (rk == null) { //无效的key
				real.remove(rk);
				continue;
			}

			Object rv = real.get(rk);

			//不允许传远程函数，只能后端配置
			if (rk.endsWith("()") && rv instanceof String) {
				throw new UnsupportedOperationException(method + " 请求，" +rk + " 不合法！非开放请求不允许传远程函数 key():\"fun()\" ！");
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

		//TODO放在operate前？考虑性能、operate修改后再验证的值是否和原来一样
		//校验存在<<<<<<<<<<<<<<<<<<< TODO 格式改为 id;version,tag 兼容多个字段联合主键
		String[] exists = StringUtil.split(exist);
		if (exists != null && exists.length > 0) {
			long exceptId = real.getLongValue(KEY_ID);
			for (String e : exists) {
				verifyExist(name, e, real.get(e), exceptId, creator);
			}
		}
		//校验存在>>>>>>>>>>>>>>>>>>>

		//TODO放在operate前？考虑性能、operate修改后再验证的值是否和原来一样
		//校验重复<<<<<<<<<<<<<<<<<<< TODO 格式改为 id;version,tag 兼容多个字段联合主键
		String[] uniques = StringUtil.split(unique);
		if (uniques != null && uniques.length > 0) {
			long exceptId = real.getLongValue(KEY_ID);
			for (String u : uniques) {
				verifyRepeat(name, u, real.get(u), exceptId, creator);
			}
		}
		//校验重复>>>>>>>>>>>>>>>>>>>


		//还原 <<<<<<<<<<
		target.put(TYPE.name(), type);
		target.put(VERIFY.name(), verify);
		target.put(INSERT.name(), insert);
		target.put(UPDATE.name(), update);
		target.put(REPLACE.name(), replace);

		target.put(EXIST.name(), exist);
		target.put(UNIQUE.name(), unique);
		target.put(REMOVE.name(), remove);
		target.put(NECESSARY.name(), necessary);
		target.put(DISALLOW.name(), disallow);
		//还原 >>>>>>>>>>

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
			if (tk == null) {
				continue;
			}
			tv = e.getValue();

			if (opt == TYPE) {
				type(tk, tv, real);
			}
			else if (opt == VERIFY) {
				verify(tk, tv, real, creator);
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
		if (rv == null) {
			return;
		}

		if (tv.endsWith("[]")) {

			type(tk, "ARRAY", rv);

			for (Object o : (Collection<?>) rv) {
				type(tk, tv.substring(0, tv.length() - 2), o, true);
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
	private static void verify(@NotNull String tk, @NotNull Object tv, @NotNull JSONObject real, SQLCreator creator) throws Exception {
		if (tv == null) {
			throw new IllegalArgumentException("operate  operate == VERIFY " + tk + ":" + tv + " ,  >> tv == null!!!");
		}

		String rk;
		Object rv;
		Logic logic;
		if (tk.endsWith("$")) { //搜索
			sqlVerify("$", real, tk, tv, creator);
		}
		else if (tk.endsWith("~") || tk.endsWith("?")) { //TODO 正则表达式, 以后可能取消支持 ? 作为 正则匹配 的功能符
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
				sqlVerify("{}", real, tk, tv, creator);
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
	private static void sqlVerify(@NotNull String funChar, @NotNull JSONObject real, @NotNull String tk, @NotNull Object tv
			, @NotNull SQLCreator creator) throws Exception {
		//不能用Parser, 0 这种不符合 StringUtil.isName !
		Logic logic = new Logic(tk.substring(0, tk.length() - funChar.length()));
		String rk = logic.getKey();
		Object rv = real.get(rk);
		if (rv == null) {
			return;
		}

		SQLConfig config = creator.createSQLConfig().setMethod(RequestMethod.HEAD).setCount(1).setPage(0);
		config.setTable(Test.class.getSimpleName());
		config.setTest(true);
		config.putWhere("'" + rv + "'" + logic.getChar() + funChar, tv, false);

		SQLExecutor executor = creator.createSQLExecutor();
		JSONObject result = null;
		try {
			result = executor.execute(config, false);
		} finally {
			executor.close();
		}
		if (result != null && JSONResponse.isExist(result.getIntValue(JSONResponse.KEY_COUNT)) == false) {
			throw new IllegalArgumentException(rk + ":" + rv + "中value不合法！必须匹配 " + logic.getChar() + tv + " ！");
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
		if (key == null || value == null) {
			Log.e(TAG, "verifyRepeat  key == null || value == null >> return;");
			return;
		}
		if (value instanceof JSON) {
			throw new UnsupportedDataTypeException(key + ":value 中value的类型不能为JSON！");
		}
		
		
		SQLConfig config = creator.createSQLConfig().setMethod(RequestMethod.HEAD).setCount(1).setPage(0);
		config.setTable(table);
		if (exceptId > 0) {//允许修改自己的属性为该属性原来的值
			config.putWhere(JSONRequest.KEY_ID + "!", exceptId, false);
		}
		config.putWhere(key, value, false);
		
		SQLExecutor executor = creator.createSQLExecutor();
		try {
			JSONObject result = executor.execute(config, false);
			if (result == null) {
				throw new Exception("服务器内部错误  verifyRepeat  result == null");
			}
			if (result.getIntValue(JSONResponse.KEY_COUNT) > 0) {
				throw new ConflictException(key + ": " + value + " 已经存在，不能重复！");
			}
		} finally {
			executor.close();
		}
	}


}
