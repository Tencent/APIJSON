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

package zuo.biao.apijson.server;

import static zuo.biao.apijson.JSONRequest.KEY_ID;
import static zuo.biao.apijson.JSONRequest.KEY_ID_IN;
import static zuo.biao.apijson.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.model.Test;
import zuo.biao.apijson.server.sql.SQLConfig;
import zuo.biao.apijson.server.sql.SQLExecutor;

//TODO 放到 zuo.biao.apijson 包内，供Android客户端校验请求结构
/**结构类
 * 增删改查: operation(add,replace,put,remove)   operation:{key0:value0, key1:value1 ...}
 * 对值校验: verify:{key0:value0, key1:value1 ...}  (key{}:range,key$:"%m%"等)
 * @author Lemon
 */
public class Structure {
	private static final String TAG = "Structure";



	private Structure() {}



	static final String requestString = "{\"Comment\":{\"disallow\": \"id\", \"necessary\": \"userId,momentId,content\"}, \"add\":{\"Comment:to\":{}}}";
	static final String responseString = "{\"User\":{\"remove\": \"phone\", \"replace\":{\"sex\":2}, \"add\":{\"name\":\"api\"}}, \"put\":{\"Comment:to\":{}}}";

	public static void test() throws Exception {
		JSONObject request;
		try {
			request = JSON.parseObject("{\"Comment\":{\"userId\":0}}");
			Log.d(TAG, "test  parseRequest = " + parseRequest(POST, "", JSON.parseObject(requestString), request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			request = JSON.parseObject("{\"Comment\":{\"userId\":0, \"momentId\":0, \"content\":\"apijson\"}}");
			Log.d(TAG, "test  parseRequest = " + parseRequest(POST, "", JSON.parseObject(requestString), request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			request = JSON.parseObject("{\"Comment\":{\"id\":0, \"userId\":0, \"momentId\":0, \"content\":\"apijson\"}}");
			Log.d(TAG, "test  parseRequest = " + parseRequest(POST, "", JSON.parseObject(requestString), request));
		} catch (Exception e) {
			e.printStackTrace();
		}


		JSONObject response;
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0}}");
			Log.d(TAG, "test  parseResponse = " + parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0, \"phone\":\"12345678\"}}");
			Log.d(TAG, "test  parseResponse = " + parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"userId\":0, \"phone\":\"12345678\", \"sex\":1}}");
			Log.d(TAG, "test  parseResponse = " + parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response = JSON.parseObject("{\"User\":{\"id\":0, \"name\":\"tommy\", \"phone\":\"12345678\", \"sex\":1}}");
			Log.d(TAG, "test  parseResponse = " + parseResponse(RequestMethod.GET, "", JSON.parseObject(responseString), response, null));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**从request提取target指定的内容
	 * @param method
	 * @param name
	 * @param target
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parseRequest(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject request) throws Exception {
		Log.i(TAG, "parseRequest  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n request = \n" + JSON.toJSONString(request));
		if (target == null || request == null) {// || request.isEmpty()) {
			Log.i(TAG, "parseRequest  target == null || request == null >> return null;");
			return null;
		}

		//TODO globleRole要不要改成@role?  只允许服务端Request表中加上可控的ADMIN角色
		if (RequestRole.get(request.getString(JSONRequest.KEY_ROLE)) == RequestRole.ADMIN) {
			throw new IllegalArgumentException("角色设置错误！不允许在写操作Request中传 " + name + 
					":{ " + JSONRequest.KEY_ROLE + ":admin } ！");
		}

		//解析
		return parse(name, target, request, new OnParseCallback() {

			@Override
			public JSONObject onParseJSONObject(String key, JSONObject tobj, JSONObject robj) throws Exception {
				//				Log.i(TAG, "parseRequest.parse.onParseJSONObject  key = " + key + "; robj = " + robj);
				if (robj == null) {
					if (tobj != null) {//不允许不传Target中指定的Table
						throw new IllegalArgumentException(method.name() + "请求，请设置 " + key + " ！");
					}
				} else if (zuo.biao.apijson.JSONObject.isTableKey(key)) {
					if (method == POST) {
						if (robj.containsKey(KEY_ID)) {
							throw new IllegalArgumentException("POST请求， " + key + " 不能设置 " + KEY_ID + " ！");
						}
					} else {
						if (RequestMethod.isQueryMethod(method) == false) {
							//单个修改或删除
							Object id = robj.get(KEY_ID); //如果必须传 id ，可在Request表中配置necessary
							if (id != null && id instanceof Number == false) {
								throw new IllegalArgumentException(method.name() + "请求， " + key
										+ " 中 " + KEY_ID + " 对应值的类型只能是Long！");
							}
							
							//批量修改或删除
							Object arr = robj.get(KEY_ID_IN); //如果必须传 id{} ，可在Request表中配置necessary
							if (arr != null && arr instanceof JSONArray == false) {
								throw new IllegalArgumentException(method.name() + "请求， " + key
										+ " 中 " + KEY_ID_IN + " 对应值的类型只能是JSONArray！");
							}
						}
					}
				} 

				return parseRequest(method, key, tobj, robj);
			}
		});

	}


	/**校验并将response转换为指定的内容和结构
	 * @param method
	 * @param name
	 * @param target
	 * @param response
	 * @param callback 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject parseResponse(@NotNull final RequestMethod method, final String name
			, final JSONObject target, final JSONObject response, OnParseCallback callback) throws Exception {
		Log.i(TAG, "parseResponse  method = " + method  + "; name = " + name
				+ "; target = \n" + JSON.toJSONString(target)
				+ "\n response = \n" + JSON.toJSONString(response));
		if (target == null || response == null) {// || target.isEmpty() {
			Log.i(TAG, "parseRequest  target == null || response == null >> return response;");
			return response;
		}

		//解析
		return parse(name, target, response, callback != null ? callback : new OnParseCallback() {});
	}


	/**对request和response不同的解析用callback返回
	 * @param target
	 * @param request
	 * @param callback
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject parse(String name, JSONObject target, JSONObject real
			, @NotNull OnParseCallback callback) throws Exception {
		if (target == null) {
			return null;
		}


		//获取配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject verify = target.getJSONObject(NAME_VERIFY);
		JSONObject add = target.getJSONObject(NAME_ADD);
		JSONObject put = target.getJSONObject(NAME_PUT);
		JSONObject replace = target.getJSONObject(NAME_REPLACE);

		String remove = StringUtil.getNoBlankString(target.getString(NAME_REMOVE));
		String necessary = StringUtil.getNoBlankString(target.getString(NAME_NECESSARY));
		String disallow = StringUtil.getNoBlankString(target.getString(NAME_DISALLOW));

		//不还原，传进来的target不应该是原来的
		target.remove(NAME_VERIFY);
		target.remove(NAME_ADD);
		target.remove(NAME_PUT);
		target.remove(NAME_REPLACE);

		target.remove(NAME_REMOVE);
		target.remove(NAME_NECESSARY);
		target.remove(NAME_DISALLOW);
		//获取配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


		Set<String> tableKeySet = new HashSet<String>();


		//移除字段<<<<<<<<<<<<<<<<<<<
		String[] removes = StringUtil.split(StringUtil.getNoBlankString(remove));
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
				throw new IllegalArgumentException(name
						+ "不能缺少 " + s + " 等[" + necessary + "]内的任何字段！");
			}
		}
		//判断必要字段是否都有>>>>>>>>>>>>>>>>>>>


		Set<String> rkset = real.keySet();

		//判断是否都有不允许的字段<<<<<<<<<<<<<<<<<<<
		List<String> disallowList = new ArrayList<String>();
		if ("!".equals(disallow)) {//所有非necessary，改成 !necessary 更好
			if (rkset != null) {
				for (String key : rkset) {//对@key放行，@role,@column,自定义@position等
					if (key != null && key.startsWith("@") == false && necessaryList.contains(key) == false) {
						disallowList.add(key);
					}
				}
			}
		} else {
			String[] disallows = StringUtil.split(disallow);
			if (disallows != null && disallows.length > 0) {
				disallowList.addAll(Arrays.asList(disallows));
			}
		}
		for (String s : disallowList) {
			if (real.containsKey(s)) {
				throw new IllegalArgumentException(name
						+ "不允许传 " + s + " 等" + StringUtil.getString(disallowList) + "内的任何字段！");
			}
		}
		//判断是否都有不允许的字段>>>>>>>>>>>>>>>>>>>



		Set<Entry<String, Object>> set = new LinkedHashSet<>(target.entrySet());
		zuo.biao.apijson.server.Entry<String, String> pair;
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

				if (tvalue instanceof JSONObject) {//JSONObject，往下一级提取
					tvalue = callback.onParseJSONObject(key, (JSONObject) tvalue, (JSONObject) rvalue);

					pair = Pair.parseEntry(key, true);
					if (pair != null && zuo.biao.apijson.JSONObject.isTableKey(pair.getKey())) {
						tableKeySet.add(key);
					}
				} else if (tvalue instanceof JSONArray) {//JSONArray
					tvalue = callback.onParseJSONArray(key, (JSONArray) tvalue, (JSONArray) rvalue);
				} else {//其它Object
					tvalue = callback.onParseObject(key, tvalue, rvalue);
				}

				if (tvalue != null) {//可以在target中加上一些不需要客户端传的键值对
					real.put(key, tvalue);
				}
			}

		}



		//不允许操作未指定Table<<<<<<<<<<<<<<<<<<<<<<<<<
		for (String rk : rkset) {
			pair = Pair.parseEntry(rk, true);//非GET类操作不允许Table:alias别名
			if (pair != null && zuo.biao.apijson.JSONObject.isTableKey(pair.getKey())
					&& tableKeySet.contains(rk) == false) {
				throw new UnsupportedOperationException("不允许操作 " + rk + " ！");
			}
		}
		//不允许操作未指定Table>>>>>>>>>>>>>>>>>>>>>>>>>


		//校验与修改Request<<<<<<<<<<<<<<<<<
		//在tableKeySet校验后操作，避免 导致put/add进去的Table 被当成原Request的内容
		real = operate(TYPE_VERIFY, verify, real);
		real = operate(TYPE_ADD, add, real);
		real = operate(TYPE_PUT, put, real);
		real = operate(TYPE_REPLACE, replace, real);
		//校验与修改Request>>>>>>>>>>>>>>>>>

		Log.i(TAG, "parse  return real = " + JSON.toJSONString(real));
		return real;
	}



	/**执行操作
	 * @param type
	 * @param targetChild
	 * @param real
	 * @return
	 * @throws Exception
	 */
	private static JSONObject operate(int type, JSONObject targetChild, JSONObject real) throws Exception {
		if (targetChild == null) {
			return real;
		}
		if (real == null) {
			throw new IllegalArgumentException("operate  real == null!!!");
		}

		if (type <= TYPE_DEFAULT || type > TYPE_REMOVE) {
			return real;
		}


		Set<Entry<String, Object>> set = new LinkedHashSet<>(targetChild.entrySet());
		String tk;
		Object tv;
		String rk;
		Object rv;
		Logic logic;
		for (Entry<String, Object> e : set) {
			tk = e == null ? null : e.getKey();
			if (tk == null) {
				continue;
			}
			tv = e.getValue();


			if (type == TYPE_VERIFY) {//TODO {}, $, <>
				if (tv == null) {
					throw new IllegalArgumentException("operate  operate == TYPE_VERIFY >> tv == null!!!");
				}

				if (tk.endsWith("{}")) {//rv符合tv条件或在tv内
					if (tv instanceof String) {//TODO  >= 0, < 10
						sqlVerify("{}", real, tk, tv);
					} 
					else if (tv instanceof JSONArray) {
						logic = new Logic(tk.substring(0, tk.length() - 2));
						rk = logic.getKey();
						rv = real.get(rk);

						if (((JSONArray) tv).contains(rv) == logic.isNot()) {
							throw new IllegalArgumentException("operate  operate == TYPE_VERIFY"
									+ " >> ((JSONArray) tv).contains(rv) == logic.isNot()");
						}
					} else {
						throw new UnsupportedDataTypeException("");
					}
				} else if (tk.endsWith("<>")) {//rv包含tv内的值
					logic = new Logic(tk.substring(0, tk.length() - 2));
					rk = logic.getKey();
					rv = real.get(rk);

					if (rv instanceof JSONArray == false) {
						throw new UnsupportedDataTypeException("服务器Request表verify配置错误！");
					}

					JSONArray array;
					if (tv instanceof JSONArray) {
						array = (JSONArray) tv;
					} else {
						array = new JSONArray();
						array.add(tv);
					}

					boolean isOr = false;
					for (Object o : array) {
						if (((JSONArray) rv).contains(o)) {
							if (logic.isNot()) {
								throw new IllegalArgumentException("operate  operate == TYPE_VERIFY"
										+ " >> ((JSONArray) rv).contains(o) >> logic.isNot()");
							}
							if (logic.isOr()) {
								isOr = true;
								break;
							}
						} else {
							if (logic.isAnd()) {
								throw new IllegalArgumentException("operate  operate == TYPE_VERIFY"
										+ " >> ((JSONArray) rv).contains(o) == false >> logic.isAnd()");
							}
						}
					}

					if (isOr == false && logic.isOr()) {
						throw new IllegalArgumentException("operate  operate == TYPE_VERIFY"
								+ " >> isOr == false && logic.isOr()");
					}
				} else if (tk.endsWith("?")) {//正则表达式
					logic = new Logic(tk.substring(0, tk.length() - 1));
					rk = logic.getKey();
					rv = real.get(rk);

					JSONArray array;
					if (tv instanceof JSONArray) {
						array = (JSONArray) tv;
					} else {
						array = new JSONArray();
						array.add(tv);
					}

					boolean m;
					boolean isOr = false;
					for (Object r : array) {
						if (r instanceof String == false) {
							throw new UnsupportedDataTypeException(rk + ":" + rv + "中value只支持 String 或 [String] 类型！");
						}
						m = Pattern.compile((String) r).matcher("" + rv).matches();
						if (m) {
							if (logic.isNot()) {
								throw new IllegalArgumentException(rk + ":" + rv + "中value不合法！必须匹配 !" + array + " ！");
							}
							if (logic.isOr()) {
								isOr = true;
								break;
							}
						} else {
							if (logic.isAnd()) {
								throw new IllegalArgumentException(rk + ":" + rv + "中value不合法！必须匹配 &" + array + " ！");
							}
						}
					}

					if (isOr == false && logic.isOr()) {
						throw new IllegalArgumentException(rk + ":" + rv + "中value不合法！必须匹配 |" + array + " ！");
					}

				} else if (tk.endsWith("$")) {//搜索
					sqlVerify("$", real, tk, tv);
				} else {
					throw new IllegalArgumentException("服务器Request表verify配置错误！");
				}
			} else if (type == TYPE_PUT) {
				real.put(tk, tv);
			} else {
				if (real.containsKey(tk)) {
					if (type == TYPE_REPLACE) {
						real.put(tk, tv);
					}
				} else {
					if (type == TYPE_ADD) {
						real.put(tk, tv);
					}
				}
			}
		}

		return real;
	}


	/**通过数据库执行SQL语句来验证条件
	 * @param funChar
	 * @param real
	 * @param tk
	 * @param tv
	 * @throws Exception
	 */
	private static void sqlVerify(@NotNull String funChar, JSONObject real, String tk, Object tv) throws Exception {
		//不能用Parser, 0 这种不符合 StringUtil.isName !
		Logic logic = new Logic(tk.substring(0, tk.length() - funChar.length()));
		String rk = logic.getKey();
		Object rv = real.get(rk);

		JSONArray array;
		if (tv instanceof JSONArray) {
			array = (JSONArray) tv;
		} else {
			array = new JSONArray();
			array.add(tv);
		}

		SQLConfig config = new SQLConfig(RequestMethod.HEAD, 1, 0);
		config.setTable(Test.class.getSimpleName());
		config.setTest(true);
		config.addWhere("'" + rv + "'" + logic.getChar() + funChar, tv);

		SQLExecutor executor = new SQLExecutor();
		JSONObject result = null;
		try {
			result = executor.execute(config);
		} catch (Exception e) {
			throw e;
		} finally {
			executor.close();
		}
		if (result != null && JSONResponse.isExist(result.getIntValue(JSONResponse.KEY_COUNT)) == false) {
			throw new IllegalArgumentException(rk + ":" + rv + "中value不合法！必须匹配 " + logic.getChar() + array + " ！");
		}		
	}



	//	/**
	//	 * @param real
	//	 * @param tk
	//	 * @param tv
	//	 * @param tableKeySet
	//	 */
	//	private static void putTargetChild(JSONObject real, String tk, Object tv, Set<String> tableKeySet) {
	//		real.put(tk, tv);
	//		zuo.biao.apijson.server.Entry<String, String> pair = Pair.parseEntry(tk, true);
	//		if (pair != null && zuo.biao.apijson.JSONObject.isTableKey(pair.getKey())) {
	//			tableKeySet.add(tk);
	//		}
	//	}


	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_VERIFY = 1;
	public static final int TYPE_ADD = 2;
	public static final int TYPE_PUT = 3;
	public static final int TYPE_REPLACE = 4;
	public static final int TYPE_REMOVE = 5;

	public static final String NAME_VERIFY = "verify";

	public static final String NAME_ADD = "add";
	public static final String NAME_PUT = "put";
	public static final String NAME_REPLACE = "replace";
	public static final String NAME_REMOVE = "remove";

	public static final String NAME_DISALLOW = "disallow";
	public static final String NAME_NECESSARY = "necessary";

	/**
	 * @param key
	 * @return
	 */
	public static int getOperate(String key) {
		if (key != null) {
			if (NAME_VERIFY.equals(key)) {
				return TYPE_VERIFY;
			}
			if (NAME_ADD.equals(key)) {
				return TYPE_ADD;
			}
			if (NAME_PUT.equals(key)) {
				return TYPE_PUT;
			}
			if (NAME_REPLACE.equals(key)) {
				return TYPE_REPLACE;
			} 
			if (NAME_REMOVE.equals(key)) {
				return TYPE_REMOVE;
			}
		}

		return TYPE_DEFAULT;
	}


}
