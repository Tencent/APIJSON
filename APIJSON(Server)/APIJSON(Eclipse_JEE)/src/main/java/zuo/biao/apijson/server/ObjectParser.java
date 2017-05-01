package zuo.biao.apijson.server;

import static zuo.biao.apijson.RequestMethod.PUT;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.constraints.NotNull;

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
public abstract class ObjectParser {
	private static final String TAG = "ObjectParser";


	/**
	 * @param path path of target
	 * @return
	 */
	protected abstract Object getTarget(String path);
	




	protected RequestMethod requestMethod;
	protected String parentPath;
	protected String path;
	protected String table;
	protected final boolean isTableKey;
	protected JSONObject request;

	public ObjectParser(RequestMethod requestMethod, String parentPath, String name, @NotNull JSONObject request) {
		if (request == null) {
			throw new IllegalArgumentException(TAG + ".ObjectParser  request == null!!!");
		}
		this.requestMethod = requestMethod;
		this.parentPath = parentPath;
		this.path = Parser.getAbsPath(parentPath, name);
		this.table = Pair.parseEntry(name, true).getKey();
		this.isTableKey = Parser.isTableKey(table);
		this.request = request;
		Log.d(TAG, "ObjectParser  table = " + table + "; isTableKey = " + isTableKey);
	}



	protected JSONObject response;
	protected JSONObject childObject;
	protected Map<String, Object> customMap;
	protected Map<String, String> functionMap;

	/**解析成员
	 * @param request
	 * @return null or this
	 * @throws Exception
	 */
	public ObjectParser parse() throws Exception {
		response = new JSONObject(true);//must init

		Set<Entry<String, Object>> set = new LinkedHashSet<>(request.entrySet());
		if (set != null && set.isEmpty() == false) {//判断换取少几个变量的初始化是否值得？
			if (isTableKey) {
				customMap = new LinkedHashMap<String, Object>();
				functionMap = new LinkedHashMap<String, String>();
			}

			String key;
			Object value;
			for (Entry<String, Object> entry : set) {
				value = entry.getValue();
				if (value == null) {
					continue;
				}
				key = entry.getKey();

				if (value instanceof JSONObject) {//JSONObject，往下一级提取
					onChildParse(key, (JSONObject) value);
				} else if (requestMethod == PUT && value instanceof JSONArray) {//PUT JSONArray
					onPUTArrayParse(key, (JSONArray) value);
				} else {//JSONArray或其它Object，直接填充
					if (onParse(key, value) == false) {
						return null;
					}
				}
			}
		}

		return this;
	}

	/**
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	protected void onChildParse(String key, JSONObject value) throws Exception {
		childObject.put(key, value);
	}
	/**
	 * @return
	 * @throws Exception
	 */
	protected JSONObject onSQLExecute() throws Exception {
		return response;
	}

	/**解析普通成员
	 * @param key
	 * @param value
	 * @return whether parse succeed
	 */
	protected boolean onParse(String key, Object value) throws Exception {
		if (key.endsWith("@")) {//StringUtil.isPath((String) value)) {
			if (value instanceof String == false) {
				throw new IllegalArgumentException("\"key@\": 后面必须为依赖路径String！");
			}
			//						System.out.println("getObject  key.endsWith(@) >> parseRelation = " + parseRelation);
			String replaceKey = key.substring(0, key.length() - 1);//key{}@ getRealKey
			String targetPath = Parser.getValuePath(parentPath, new String((String) value));

			//先尝试获取，尽量保留缺省依赖路径，这样就不需要担心路径改变
			Object target = getTarget(targetPath);
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
					recycle();
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

		if (isTableKey == false) {//不查询，所以不必分开
			response.put(key, value);
		} else {
			if (key.endsWith("()")) {
				if (value instanceof String == false) {
					throw new IllegalArgumentException(path + "/" + key + "():function() 后面必须为函数String！");
				}
				functionMap.put(key, (String) value);
			} else if (key.startsWith("@") && QueryConfig.TABLE_KEY_LIST.contains(key) == false) {
				customMap.put(key, value);
			} else {
				response.put(key, value);
			}
		}

		return true;
	}


	/**PUT key:[]
	 * @param key
	 * @param array
	 * @throws Exception
	 */
	protected void onPUTArrayParse(String key, JSONArray array) throws Exception {
		if (isTableKey == false || array == null || array.isEmpty()) {
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
		String realKey = Parser.getRealKey(requestMethod, key, false, false);

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
		response.put(realKey, targetArray);
		//PUT >>>>>>>>>>>>>>>>>>>>>>>>>

	}


	/**SQL查询，for single object
	 * @return
	 * @throws Exception
	 */
	public JSONObject executeSQL() throws Exception {
		return executeSQL(1, 0, 0);
	}

	protected QueryConfig config = null;//array item复用
	/**SQL查询，for array item
	 * @param count
	 * @param page
	 * @param position
	 * @return
	 * @throws Exception
	 */
	public JSONObject executeSQL(int count, int page, int position) throws Exception {
		boolean query = false;
		//执行SQL操作数据库
		if (isTableKey()) {//提高性能
			query = true;

			if (config == null) {
				config = QueryConfig.newQueryConfig(requestMethod, table, response);
			}
			config.setCount(count).setPage(page).setPosition(position);

			try {
				response = onSQLExecute();
			} catch (Exception e) {
				Log.e(TAG, "getObject  try { response = getSQLObject(config2); } catch (Exception e) {");
				if (e instanceof NotExistException) {//非严重异常，有时候只是数据不存在
					//						e.printStackTrace();
					response = null;//内部吃掉异常，put到最外层
					//						requestObject.put(JSONResponse.KEY_MESSAGE
					//								, StringUtil.getString(requestObject.get(JSONResponse.KEY_MESSAGE)
					//										+ "; query " + path + " cath NotExistException:"
					//										+ newErrorResult(e).getString(JSONResponse.KEY_MESSAGE)));
				} else {
					throw e;
				}
			}
		}

		if (response == null) {
			response = new JSONObject(true);
		}

		if (customMap != null) {
			response.putAll(customMap);
		}
		if (functionMap != null) {
			if (query) {
				//解析函数function
				Set<String> functionSet = functionMap == null ? null : functionMap.keySet();
				if (functionSet != null && functionSet.isEmpty() == false) {
					for (String key : functionSet) {
						response.put(Parser.getRealKey(requestMethod, key, false, false)
								, Function.invoke(response, functionMap.get(key)));
					}
				}
			} else {
				response.putAll(functionMap);
			}
		}
		
		onComplete();

		return response;
	}

	/**
	 * response has the final value after parse (and query if isTableKey)
	 */
	protected void onComplete() {
	}


	/**回收内存
	 */
	public void recycle() {
		requestMethod = null;
		parentPath = null;
		path = null;
		table = null;

		//		if (response != null) {
		//			response.clear();//有效果?
		//			response = null;
		//		}

		response = null;
		childObject = null;
		functionMap = null;
		customMap = null;
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
	public JSONObject getResponse() {
		return response;
	}
	public JSONObject getChildObject() {
		return childObject;
	}
	public Map<String, String> getFunctionMap() {
		return functionMap;
	}
	public Map<String, Object> getCustomMap() {
		return customMap;
	}

}
