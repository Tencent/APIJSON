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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.Table;

/**config model for query
 * @author Lemon
 */
public class QueryConfig {
	private static final String TAG = "QueryConfig";

	public static final List<String> keyList;
	static {
		keyList = new ArrayList<String>();
		keyList.add(JSONRequest.KEY_COLUMNS);
	}



	private long id;
	private RequestMethod method;
	private String table;
	private String columns;
	private String values;
	private Map<String, Object> where;
	private int count;
	private int page;
	private String sort;
	private int position;

	public QueryConfig(RequestMethod method) {
		setMethod(method);
	}
	public QueryConfig(RequestMethod method, String table) {
		this(method);
		setTable(table);
	}
	public QueryConfig(RequestMethod method, String table, Map<String, Object> where) {
		this(method, table);
		setWhere(where);
	}
	public QueryConfig(RequestMethod method, String table, String columns, String values) {
		this(method, table);
		setColumns(columns);
		setValues(values);
	}
	public QueryConfig(RequestMethod method, String table, String[] columns, String[][] values) {
		this(method, table);
		setColumns(columns);
		setValues(values);
	}
	public QueryConfig(RequestMethod method, int count, int page) {
		this(method);
		setCount(count);
		setPage(page);
	}

	public RequestMethod getMethod() {
		if (method == null) {
			method = RequestMethod.GET;
		}
		return method;
	}
	public QueryConfig setMethod(RequestMethod method) {
		this.method = method;
		return this;
	}
	public String getTable() {
		return table;
	}
	public QueryConfig setTable(String table) {
		this.table = table;
		return this;
	}
	public String getColumns() {
		return columns;
	}
	public QueryConfig setColumns(String[] columns) {
		return setColumns(StringUtil.getString(columns));
	}
	public QueryConfig setColumns(String columns) {
		this.columns = columns;
		return this;
	}
	private String getColumnsString() {
		switch (getMethod()) {
		case POST:
			return StringUtil.isNotEmpty(columns, true) ? "(" + columns + ")" : "";
		case HEAD:
		case POST_HEAD:
			return " COUNT(0) COUNT ";
		default:
			return StringUtil.isNotEmpty(columns, true) ? columns : "*";
		}
	}

	public long getId() {
		return id;
	}
	public QueryConfig setId(long id) {
		this.id = id;
		return this;
	}

	public String getValues() {
		return values;
	}
	public String getValuesString() {
		return values;
	}
	public QueryConfig setValues(String[][] values) {
		String s = "";
		if (values != null && values.length > 0) {
			String[] items = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				items[i] = "(" + StringUtil.getString(values[i]) + ")";
			}
			s = StringUtil.getString(items);
		}
		return setValues(s);
	}
	public QueryConfig setValues(String values) {
		this.values = values;
		return this;
	}
	public Map<String, Object> getWhere() {
		return where;
	}
	public QueryConfig setWhere(Map<String, Object> where) {
		this.where = where;
		return this;
	}

	public int getCount() {
		return count;
	}
	public QueryConfig setCount(int count) {
		this.count = count;
		return this;
	}
	public int getPage() {
		return page;
	}
	public QueryConfig setPage(int page) {
		this.page = page;
		return this;
	}
	public String getSort() {
		return sort;
	}
	public QueryConfig setSort(String sort) {
		this.sort = sort;
		return this;
	}
	public int getPosition() {
		return position;
	}
	public QueryConfig setPosition(int position) {
		this.position = position;
		return this;
	}

	/**获取限制数量
	 * @return
	 */
	public String getLimitString() {
		return getLimitString(getPage(), getCount());// + 1);
	}
	/**获取限制数量
	 * @param limit
	 * @return
	 */
	public static String getLimitString(int page, int count) {
		return count <= 0 ? "" : " LIMIT " + page*count + ", " + count;
	}

	//WHERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取WHERE
	 * @return
	 * @throws Exception 
	 */
	public String getWhereString() throws Exception {
		return getWhereString(getMethod(), getWhere());
	}
	/**获取WHERE
	 * @param method
	 * @param where
	 * @return
	 * @throws Exception 
	 */
	public static String getWhereString(RequestMethod method, Map<String, Object> where) throws Exception {
		Set<String> set = where == null ? null : where.keySet();
		if (set != null && set.size() > 0) {
			if (RequestParser.isGetMethod(method) == false && method != RequestMethod.POST_GET
					&& where.containsKey(Table.ID) == false) {
				throw new IllegalArgumentException("请设置" + Table.ID + "！");
			}

			String whereString = " WHERE ";
			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - $, 2 - {} 
			Object value;
			for (String key : set) {
				Log.d(TAG, "getWhereString  key = " + key);
				//避免筛选到全部	value = key == null ? null : where.get(key);
				if (key == null || key.startsWith("@") || key.endsWith("()")) {//关键字||方法, +或-直接报错
					Log.d(TAG, "getWhereString  key == null || key.startsWith(@) || key.endsWith(()) >> continue;");
					continue;
				}
				if (key.endsWith("@")) {//引用
					key = key.substring(0, key.lastIndexOf("@"));
					//					throw new IllegalArgumentException(TAG + ".getWhereString: 字符 " + key + " 不合法！");
				}
				if (key.endsWith("$")) {
					keyType = 1;
				} else if (key.endsWith("{}")) {
					keyType = 2;
				}
				value = where.get(key);
				key = RequestParser.getRealKey(method, key, false);

				whereString += (isFirst ? "" : " AND ") + (key + (keyType == 1 ? " LIKE '" + value + "'" : (keyType == 2
						? getRangeString(key, value) : "='" + value + "'") ));

				isFirst = false;
			}

			if (whereString.trim().endsWith("WHERE") == false) {
				return whereString;
			}
		}
		return "";
	}
	
	/**WHERE key > 'key0' AND key <= 'key1' AND ...
	 * @param key
	 * @param range "condition0,condition1..."
	 * @return key condition0 AND key condition1 AND ...
	 */
	public static String getRangeString(String key, Object range) {
		if (range instanceof JSONArray) {
			return getInString(((JSONArray) range).toArray());
		}
		if (range instanceof String) {
			return ((String) range).replaceAll(",", " AND " + key);//非Number类型需要客户端拼接成 < 'value0', >= 'value1'这种
		}

		throw new IllegalArgumentException("\"key{}\":range 中range只能是 用','分隔条件的字符串 或者 可取选项JSONArray！");
	}
	/**WHERE key IN ('key0', 'key1', ... )
	 * @param in
	 * @return IN ('key0', 'key1', ... )
	 */
	public static String getInString(Object[] in) {
		String inString = "";
		if (in != null) {//返回 "" 会导致 id:[] 空值时效果和没有筛选id一样！
			for (int i = 0; i < in.length; i++) {
				inString += ((i > 0 ? "," : "") + "'" + in[i] + "'");
			}
		}
		return " IN (" + inString + ") ";
	}
	//WHERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//SET <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取SET
	 * @return
	 * @throws Exception 
	 */
	public String getSetString() throws Exception {
		return getSetString(getMethod(), getWhere());
	}
	/**获取SET
	 * @param method
	 * @param where
	 * @return
	 * @throws Exception 
	 */
	public static String getSetString(RequestMethod method, Map<String, Object> where) throws Exception {
		Set<String> set = where == null ? null : where.keySet();
		if (set != null && set.size() > 0) {
			if (where.containsKey(Table.ID) == false) {
				throw new IllegalArgumentException("请设置" + Table.ID + "！");
			}
			String setString = " SET ";
			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - +, 2 - -
			Object value;
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : where.get(key);
				if (key == null || Table.ID.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				}
				value = where.get(key);
				key = RequestParser.getRealKey(method, key, false);

				setString += (isFirst ? "" : ", ") + (key + "=" + (keyType == 1 ? getAddString(key, value) : (keyType == 2
						? getRemoveString(key, value) : "'" + value + "'") ) );

				isFirst = false;
			}

			if (setString.trim().endsWith("SET") == false) {
				return setString + " WHERE " + Table.ID + "='" + where.get(Table.ID) + "' ";
			}
		}
		return "";
	}

	/**SET key = CONCAT (key, 'value')
	 * @param key
	 * @param value
	 * @return CONCAT (key, 'value')
	 * @throws IllegalArgumentException
	 */
	public static String getAddString(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			return key + " + " + value;
		}
		if (value instanceof String) {
			return " CONCAT (" + key + ", '" + value + "') ";
		}
		throw new IllegalArgumentException(key + "+ 对应的值 " + value + " 不是Number,String,Array中的任何一种！");
	}
	/**SET key = REPLACE (key, 'value', '')
	 * @param key
	 * @param value
	 * @return REPLACE (key, 'value', '')
	 * @throws IllegalArgumentException
	 */
	public static String getRemoveString(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			return key + " - " + value;
		}
		if (value instanceof String) {
			return " REPLACE (" + key + ", '" + value + "', '') ";
		}
		throw new IllegalArgumentException(key + "- 对应的值 " + value + " 不是Number,String,Array中的任何一种！");
	}
	//SET >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public static synchronized QueryConfig newQueryConfig(RequestMethod method, String table, JSONObject request) {
		QueryConfig config = new QueryConfig(method, table);

		Set<String> set = request == null ? null : request.keySet();
		if (set != null) {
			String columns = request.getString(JSONRequest.KEY_COLUMNS);
			if (method == RequestMethod.POST) {
				config.setColumns(StringUtil.getString(set.toArray(new String[]{})));
				String valuesString = "";
				Collection<Object> valueCollection = request.values();
				Object[] values = valueCollection == null || valueCollection.isEmpty() ? null : valueCollection.toArray();
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						valuesString += ((i > 0 ? "," : "") + "'" + values[i] + "'");
					}
				}
				config.setValues("(" + valuesString + ")");
			} else {
				request.remove(JSONRequest.KEY_COLUMNS);

				Map<String, Object> transferredRequest = new HashMap<String, Object>();
				Object value;
				for (String key : set) {
					value = request.get(key);
					if (value instanceof JSONObject == false) {//只允许常规Object
						transferredRequest.put(key, value);//一样 instanceof JSONArray ? JSON.toJSONString(value) : value);
					}
				}
				config.setWhere(transferredRequest);
			}


			if (StringUtil.isNotEmpty(columns, true)) {
				config.setColumns(columns);
			}
		}

		try {
			config.setId(request.getLongValue(Table.ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return config;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public String getSQL() throws Exception {
		return getSQL(this);
	}
	/**
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	public static String getSQL(QueryConfig config) throws Exception {
		if (config == null) {
			System.out.println("QueryConfig: getSQL  config == null >> return null;");
			return null;
		}
		if (config.getMethod() == null) {
			config.setMethod(RequestMethod.GET);
		}
		switch (config.getMethod()) {
		case POST:
			return "INSERT INTO " + config.getTable() + config.getColumnsString() + " VALUES" + config.getValuesString();
		case PUT:
			return "UPDATE " + config.getTable() + config.getSetString();
		case DELETE:
			return "DELETE FROM " + config.getTable() + config.getWhereString();
		default:
			return "SELECT "+ config.getColumnsString() + " FROM " + config.getTable()
			+ config.getWhereString() + config.getLimitString();
		}
	}


}
