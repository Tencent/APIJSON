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

import static zuo.biao.apijson.RequestMethod.PUT;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.sql.QueryConfig;

/**简化Parser，getObject和getArray(getArrayConfig)都能用
 * @author Lemon
 */
public class ObjectParser implements ParserAdapter {
	private static final String TAG = "ObjectParser";



	private ParserAdapter adapter;
	public ObjectParser init(ParserAdapter adapter) {
		this.adapter = adapter;
		return this;
	}
	public ParserAdapter getAdapter() {
		return adapter == null ? this : adapter;
	}



	public static final int TYPE_CHILD = 0;
	public static final int TYPE_ITEM = 1;
	public static final int TYPE_ITEM_CHILD_0 = 2;


	/**for array item
	 * name = null
	 * @param parentPath
	 * @param request
	 */
	public ObjectParser(@NotNull JSONObject request, String parentPath, int type) {
		this(request, parentPath, type, null);
	}

	protected JSONObject request;
	protected String parentPath;
	
	protected final int type;
	protected String path;
	protected String table;
	protected final boolean isTableKey;
	/**for single object
	 * @param parentPath
	 * @param request
	 * @param name
	 */
	public ObjectParser(@NotNull JSONObject request, String parentPath, int type, String name) {
		if (request == null) {
			throw new IllegalArgumentException(TAG + ".ObjectParser  request == null!!!");
		}
		this.request = request;
		this.parentPath = parentPath;
		this.type = type;
		
		this.path = Parser.getAbsPath(parentPath, name);
		this.table = Pair.parseEntry(name, true).getKey();
		this.isTableKey = Parser.isTableKey(table);
		Log.d(TAG, "ObjectParser  table = " + table + "; isTableKey = " + isTableKey);
	}




	private boolean invalidate = false;
	public void invalidate() {
		invalidate = true;
	}
	public boolean isInvalidate() {
		return invalidate;
	}

	private boolean breakParse = false;
	public void breakParse() {
		breakParse = true;
	}
	public boolean isBreakParse() {
		return breakParse || isInvalidate();
	}


	protected JSONObject response;
	protected JSONObject sqlRequest;
	protected JSONObject sqlReponse;
	protected Map<String, Object> customMap;
	protected Map<String, String> functionMap;
	protected Map<String, JSON> childMap;

	/**解析成员
	 * response重新赋值
	 * @param config 传递给第0个Table
	 * @return null or this
	 * @throws Exception
	 */
	public ObjectParser parse() throws Exception {
		breakParse = false;

		response = new JSONObject(true);//must init

		sqlRequest = new JSONObject(true);//must init
		sqlReponse = null;//must init
		customMap = null;//must init
		functionMap = null;//must init
		childMap = null;//must init

		Set<Entry<String, Object>> set = new LinkedHashSet<Entry<String, Object>>(request.entrySet());
		if (set != null && set.isEmpty() == false) {//判断换取少几个变量的初始化是否值得？
			if (isTableKey) {//非Table下不必分离出去再添加进来
				customMap = new LinkedHashMap<String, Object>();
				childMap = new LinkedHashMap<String, JSON>();
			}
			functionMap = new LinkedHashMap<String, String>();//必须执行

			String key;
			Object value;
			for (Entry<String, Object> entry : set) {
				value = entry.getValue();
				if (value == null) {
					continue;
				}
				key = entry.getKey();

				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					putChild(key, (JSON) value);
				} else if (method == PUT && value instanceof JSONArray) {//PUT JSONArray
					onPUTArrayParse(key, (JSONArray) value);
				} else {//JSONArray或其它Object，直接填充
					if (onParse(key, value) == false) {
						invalidate();
					}
				}

				if (isInvalidate()) {
					recycle();
					return null;
				}
				if (isBreakParse()) {
					break;
				}
			}
		}

		return this;
	}



	/**
	 * @param key
	 * @param value 类型不用JSON是因为JSON不能强转为JSONObject
	 * @throws Exception
	 */
	protected void putChild(String key, JSON child) throws Exception {
		if (child != null) {
			if (childMap != null) {//添加到childMap，最后再解析
				childMap.put(key, child);
			} else {//直接解析并替换原来的
				response.put(key, onChildParse(key, child));
			}
		}
	}

	/**
	 * @param key
	 * @param value 类型不用JSON是因为JSON不能强转为JSONObject
	 * @throws Exception
	 */
	@Override
	public JSON parseChild(String path, String key, JSON value) throws Exception {
		return value;
	}
	@Override
	public JSONObject executeSQL(String path, QueryConfig config) throws Exception {
		return sqlRequest;
	}
	@Override
	public Object getTarget(String path) {
		return path;
	}


	/**
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	protected JSON onChildParse(String key, JSON value) throws Exception {
		return getAdapter().parseChild(path, key, value);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	protected JSONObject onSQLExecute() throws Exception {
		return getAdapter().executeSQL(path, config);
	}

	/**解析普通成员
	 * @param key
	 * @param value
	 * @return whether parse succeed
	 */
	protected boolean onParse(@NotNull String key, @NotNull Object value) throws Exception {
		if (key.endsWith("@")) {//StringUtil.isPath((String) value)) {
			if (value instanceof String == false) {
				throw new IllegalArgumentException("\"key@\": 后面必须为依赖路径String！");
			}
			//						System.out.println("getObject  key.endsWith(@) >> parseRelation = " + parseRelation);
			String replaceKey = key.substring(0, key.length() - 1);//key{}@ getRealKey
			String targetPath = Parser.getValuePath(getType() == TYPE_ITEM
					? path : parentPath, new String((String) value));

			//先尝试获取，尽量保留缺省依赖路径，这样就不需要担心路径改变
			Object target = getAdapter().getTarget(targetPath);
			Log.i(TAG, "getObject targetPath = " + targetPath + "; target = " + target);

			if (target == null) {//String#equals(null)会出错
				Log.d(TAG, "getObject  target == null  >>  continue;");
				return true;
			}
			if (targetPath.equals(target)) {//必须valuePath和保证getValueByPath传进去的一致！
				Log.d(TAG, "getObject  targetPath.equals(target)  >>");

				//非查询关键词 @key 不影响查询，直接跳过
				if (isTableKey && (key.startsWith("@") == false || QueryConfig.TABLE_KEY_LIST.contains(key))) {
					Log.e(TAG, "getObject  isTableKey && (key.startsWith(@) == false"
							+ " || QueryConfig.TABLE_KEY_LIST.contains(key)) >>  return null;");
					return false;//获取不到就不用再做无效的query了。不考虑 Table:{Table:{}}嵌套
				} else {
					Log.d(TAG, "getObject  isTableKey(table) == false >> continue;");
					return true;//舍去，对Table无影响
				}
			} 


			//直接替换原来的key@:path为key:target
			Log.i(TAG, "getObject    >>  key = replaceKey; value = target;");
			key = replaceKey;
			value = target;
			Log.d(TAG, "getObject key = " + key + "; value = " + value);
		}

		if (key.endsWith("()")) {
			if (value instanceof String == false) {
				throw new IllegalArgumentException(path + "/" + key + "():function() 后面必须为函数String！");
			}
			functionMap.put(key, (String) value);
		} else if (isTableKey && key.startsWith("@") && QueryConfig.TABLE_KEY_LIST.contains(key) == false) {
			customMap.put(key, value);
		} else {
			sqlRequest.put(key, value);
		}

		return true;
	}


	/**PUT key:[]
	 * @param key
	 * @param array
	 * @throws Exception
	 */
	protected void onPUTArrayParse(@NotNull String key, @NotNull JSONArray array) throws Exception {
		if (isTableKey == false || array.isEmpty()) {
			Log.e(TAG, "onPUTArray  isTableKey == false || array == null || array.isEmpty() >> return;");
			return;
		}

		int putType = 0;
		if (key.endsWith("+")) {//add
			putType = 1;
		} else if (key.endsWith("-")) {//remove
			putType = 2;
		} else {//replace
			throw new IllegalAccessException("PUT " + path + ", PUT Array不允许 " + key + 
					" 这种没有 + 或 - 结尾的key！不允许整个替换掉原来的Array！");
		}
		String realKey = Parser.getRealKey(method, key, false, false);

		//GET > add all 或 remove all > PUT > remove key

		//GET <<<<<<<<<<<<<<<<<<<<<<<<<
		JSONObject rq = new JSONObject();
		rq.put(QueryConfig.ID, request.get(QueryConfig.ID));
		rq.put(JSONRequest.KEY_COLUMN, realKey);
		JSONObject rp = new Parser().parseResponse(new JSONRequest(table, rq));
		//GET >>>>>>>>>>>>>>>>>>>>>>>>>


		//add all 或 remove all <<<<<<<<<<<<<<<<<<<<<<<<<
		if (rp != null) {
			rp = rp.getJSONObject(table);
		}
		JSONArray targetArray = rp == null ? null : rp.getJSONArray(realKey);
		if (targetArray == null) {
			targetArray = new JSONArray();
		}
		for (Object obj : array) {
			if (obj == null) {
				continue;
			}
			if (putType == 1) {
				if (targetArray.contains(obj)) {
					throw new ConflictException("PUT " + path + ", " + realKey + ":" + obj + " 已存在！");
				}
				targetArray.add(obj);
			} else if (putType == 2) {
				if (targetArray.contains(obj) == false) {
					throw new NullPointerException("PUT " + path + ", " + realKey + ":" + obj + " 不存在！");
				}
				targetArray.remove(obj);
			}
		}

		//add all 或 remove all >>>>>>>>>>>>>>>>>>>>>>>>>

		//PUT <<<<<<<<<<<<<<<<<<<<<<<<<
		sqlRequest.put(realKey, targetArray);
		//PUT >>>>>>>>>>>>>>>>>>>>>>>>>

	}


	/**SQL查询，for single object
	 * @return {@link #executeSQL(int, int, int)}
	 * @throws Exception
	 */
	public ObjectParser executeSQL() throws Exception {
		return executeSQL(1, 0, 0);
	}

	protected QueryConfig config = null;//array item复用
	/**SQL查询，for array item
	 * @param count
	 * @param page
	 * @param position
	 * @return this
	 * @throws Exception
	 */
	public ObjectParser executeSQL(int count, int page, int position) throws Exception {
		//执行SQL操作数据库
		if (isTableKey == false) {//提高性能
			sqlReponse = new JSONObject(sqlRequest);
		} else {
			if (config == null) {
				config = newQueryConfig();
			}
			config.setCount(count).setPage(page).setPosition(position);

			try {
				sqlReponse = onSQLExecute();
			} catch (Exception e) {
				Log.e(TAG, "getObject  try { response = getSQLObject(config2); } catch (Exception e) {");
				if (e instanceof NotExistException) {//非严重异常，有时候只是数据不存在
					//						e.printStackTrace();
					sqlReponse = null;//内部吃掉异常，put到最外层
					//						requestObject.put(JSONResponse.KEY_MESSAGE
					//								, StringUtil.getString(requestObject.get(JSONResponse.KEY_MESSAGE)
					//										+ "; query " + path + " cath NotExistException:"
					//										+ newErrorResult(e).getString(JSONResponse.KEY_MESSAGE)));
				} else {
					throw e;
				}
			}
		}

		return this;
	}

	/**
	 * @return response
	 * @throws Exception
	 */
	public JSONObject response() throws Exception {
		if (sqlReponse == null || sqlReponse.isEmpty()) {
			if (isTableKey) {//Table自身都获取不到值，则里面的Child都无意义，不需要再解析
				return response;
			}
		} else {
			response.putAll(sqlReponse);
		}

		//把isTableKey时取出去的custom重新添加回来
		if (customMap != null) {
			response.putAll(customMap);
		}
		
		//解析函数function
		if (functionMap != null) {
			Set<Entry<String, String>> functionSet = functionMap == null ? null : functionMap.entrySet();
			if (functionSet != null && functionSet.isEmpty() == false) {
				for (Entry<String, String> entry : functionSet) {
					response.put(Parser.getRealKey(method, entry.getKey(), false, false)
							, Function.invoke(response, entry.getValue()));
				}
			}
		}

		//把isTableKey时取出去child解析后重新添加回来
		Set<Entry<String, JSON>> set = childMap == null ? null : childMap.entrySet();
		if (set != null) {
			for (Entry<String, JSON> entry : set) {
				if (entry != null) {
					response.put(entry.getKey(), onChildParse(entry.getKey(), entry.getValue()));
				}
			}
		}
		
		onComplete();

		return response;
	}

	protected QueryConfig newQueryConfig() {
		return QueryConfig.newQueryConfig(method, table, sqlRequest);
	}
	/**
	 * response has the final value after parse (and query if isTableKey)
	 */
	protected void onComplete() {
	}


	/**回收内存
	 */
	public void recycle() {
		method = null;
		parentPath = null;
		path = null;
		table = null;

		//		if (response != null) {
		//			response.clear();//有效果?
		//			response = null;
		//		}

		request = null;
		response = null;
		sqlRequest = null;
		sqlReponse = null;

		functionMap = null;
		customMap = null;
		childMap = null;
	}






	protected RequestMethod method;
	public ObjectParser setMethod(RequestMethod method) {
		if (this.method != method) {
			this.method = method;
			config = null;
			//TODO ?			sqlReponse = null;
		}
		return this;
	}
	public RequestMethod getMethod() {
		return method;
	}



	public int getType() {
		return type;
	}
	public String getPath() {
		return path;
	}
	public String getTable() {
		return table;
	}
	public boolean isTableKey() {
		return isTableKey;
	}
	
	

	public QueryConfig getConfig() {
		return config;
	}
	
	public JSONObject getResponse() {
		return response;
	}
	public JSONObject getSqlRequest() {
		return sqlRequest;
	}
	public JSONObject getSqlReponse() {
		return sqlReponse;
	}


	public Map<String, Object> getCustomMap() {
		return customMap;
	}
	public Map<String, String> getFunctionMap() {
		return functionMap;
	}
	public Map<String, JSON> getChildMap() {
		return childMap;
	}


}
