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

package zuo.biao.apijson.server.sql;

import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.POST_GET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSONRequest;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.SQL;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.Logic;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.exception.NotExistException;

/**config model for query
 * @author Lemon
 */
public class QueryConfig {
	private static final String TAG = "QueryConfig";

	public static final String ID = JSONResponse.KEY_ID;

	public static final List<String> ARRAY_KEY_LIST;
	static {
		ARRAY_KEY_LIST = new ArrayList<String>();
		ARRAY_KEY_LIST.add(JSONRequest.KEY_QUERY);
		ARRAY_KEY_LIST.add(JSONRequest.KEY_COUNT);
		ARRAY_KEY_LIST.add(JSONRequest.KEY_PAGE);
	}
	public static final List<String> TABLE_KEY_LIST;
	static {
		TABLE_KEY_LIST = new ArrayList<String>();
		TABLE_KEY_LIST.add(JSONRequest.KEY_COLUMN);
		TABLE_KEY_LIST.add(JSONRequest.KEY_GROUP);
		TABLE_KEY_LIST.add(JSONRequest.KEY_HAVING);
		TABLE_KEY_LIST.add(JSONRequest.KEY_ORDER);
	}

	public static final String OR = SQL.OR;
	public static final String AND = SQL.AND;
	public static final String NOT = SQL.NOT;



	private long id;
	private RequestMethod method;
	private String table;
	private String values;
	private Map<String, Object> where;
	private String column;
	private String group;
	private String having;
	private String order;


	private int count;
	private int page;
	private int position;
	private int total;

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
	public QueryConfig(RequestMethod method, String table, String column, String values) {
		this(method, table);
		setColumn(column);
		setValues(values);
	}
	public QueryConfig(RequestMethod method, String table, String[] column, String[][] values) {
		this(method, table);
		setColumn(column);
		setValues(values);
	}
	public QueryConfig(RequestMethod method, int count, int page) {
		this(method);
		setCount(count);
		setPage(page);
	}

	public RequestMethod getMethod() {
		if (method == null) {
			method = GET;
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

	public String getColumn() {
		return column;
	}
	public QueryConfig setColumn(String... keys) {
		return setColumn(StringUtil.getString(keys));
	}
	public QueryConfig setColumn(String column) {
		this.column = column;
		return this;
	}
	public String getColumnString() throws NotExistException {
		switch (getMethod()) {
		case HEAD:
		case POST_HEAD:
			return "count(0) AS count";
		case POST:
			if (StringUtil.isEmpty(column, true)) {
				throw new NotExistException(TAG + "getColumnString  getMethod() = POST"
						+ " >> StringUtil.isEmpty(column, true)");
			}
			return "(" + column + ")";
		default:
			column = StringUtil.getString(column);
			if (column.isEmpty()) {
				return "*";
			}
			return column.contains(":") == false ? column : column.replaceAll(":", " AS ");//不能在这里改，后续还要用到:
		}
	}

	public String getGroup() {
		return group;
	}
	public QueryConfig setGroup(String... keys) {
		return setGroup(StringUtil.getString(keys));
	}
	public QueryConfig setGroup(String group) {
		this.group = group;
		return this;
	}
	public String getGroupString() {
		group = StringUtil.getTrimedString(group);
		return group.isEmpty() ? "" : " GROUP BY " + group;
	}

	public String getHaving() {
		return having;
	}
	public QueryConfig setHaving(String... conditions) {
		return setHaving(StringUtil.getString(conditions));
	}
	public QueryConfig setHaving(String having) {
		this.having = having;
		return this;
	}
	public String getHavingString() {
		having = StringUtil.getTrimedString(having);
		return having.isEmpty() ? "" : " HAVING " + having;
	}

	public String getOrder() {
		return order;
	}
	public QueryConfig setOrder(String... conditions) {
		return setOrder(StringUtil.getString(conditions));
	}
	public QueryConfig setOrder(String order) {
		this.order = order;
		return this;
	}
	public String getOrderString() {
		order = StringUtil.getTrimedString(order);
		if (order.isEmpty()) {
			return "";
		}
		if (order.contains("+")) {//replace没有包含的replacement会崩溃
			order = order.replaceAll("\\+", " ASC ");
		}
		if (order.contains("-")) {
			order = order.replaceAll("-", " DESC ");
		}
		return " ORDER BY " + order;
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
	public int getPosition() {
		return position;
	}
	public QueryConfig setPosition(int position) {
		this.position = position;
		return this;
	}
	public int getTotal() {
		return total;
	}
	public QueryConfig setTotal(int total) {
		this.total = total;
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
			if (Parser.isGetMethod(method, true) == false && Parser.isHeadMethod(method, true) == false
					&& where.containsKey(ID) == false) {//POST必须有id，否则不能INSERT后直接返回id 
				throw new IllegalArgumentException("请设置" + ID + "！");
			}

			String whereString = "";
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
				} else if (key.endsWith("<>")) {
					keyType = 3;
				}
				value = where.get(key);
				key = Parser.getRealKey(method, key, false, true);

				String condition = "";
				switch (keyType) {
				case 1:
					condition = getSearchString(key, value);
					break;
				case 2:
					condition = getRangeString(key, value);
					break;
				case 3:
					condition = getContainString(key, value);
					break;
				default:
					condition = (key + "='" + value + "'");
					break;
				}
				if (StringUtil.isEmpty(condition, true)) {//避免SQL条件连接错误
					continue;
				}
				
				whereString += (isFirst ? "" : AND) + condition;

				isFirst = false;
			}

			if (whereString.isEmpty() == false) {
				return " WHERE " + whereString;
			}
		}
		return "";
	}

	//$ search <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**search key match value
	 * @param in
	 * @return {@link #getSearchString(String, Object[], int)}
	 * @throws IllegalArgumentException 
	 */
	public static String getSearchString(String key, Object value) throws IllegalArgumentException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRangeString key = " + key);

		if (value instanceof JSONArray == false) {//TODO 直接掉Like性能最好！
			JSONArray array = new JSONArray();
			array.add(value);
			value = array;
		}
		if (((JSONArray) value).isEmpty()) {
			return "";
		}
		return getSearchString(key, ((JSONArray) value).toArray(), logic.getType());
	}
	/**search key match values
	 * @param in
	 * @return LOGIC [  key LIKE 'values[i]' ]
	 * @throws IllegalArgumentException 
	 */
	public static String getSearchString(String key, Object[] values, int type) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "$\":value 中value只能为String或JSONArray<String>！");
			}
			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getLikeString(key, values[i]);
		}

		return (Logic.isNot(type) ? NOT : "") + "(" + condition + ")";
	}

	/**WHERE key LIKE 'value'
	 * @param key endsWith("!") ? key = key.substring(0, key.length() - 1) + NOT;
	 * @param value
	 * @return key LIKE 'value'
	 */
	public static String getLikeString(String key, Object value) {
		return key + " LIKE '" + value + "'";
	}
	//$ search >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//{} range <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE key > 'key0' AND key <= 'key1' AND ...
	 * @param key
	 * @param range "condition0,condition1..."
	 * @return key condition0 AND key condition1 AND ...
	 * @throws Exception 
	 */
	public static String getRangeString(String key, Object range) throws Exception {
		Log.i(TAG, "getRangeString key = " + key);
		if (range == null) {//依赖的对象都没有给出有效值，这个存在无意义。如果是客户端传的，那就能在客户端确定了。
			throw new NotExistException(TAG + "getRangeString(" + key + ", " + range
					+ ") range == null");
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRangeString key = " + key);

		if (range instanceof JSONArray) {
			if (logic.isOr() || logic.isNot()) {
				return key + getInString(key, ((JSONArray) range).toArray(), logic.isNot());
			}
			throw new IllegalArgumentException(key + "{}\":[] 中key末尾的逻辑运算符只能用'|','!'中的一种 ！");
		}
		if (range instanceof String) {//非Number类型需要客户端拼接成 < 'value0', >= 'value1'这种
			String[] conditions = StringUtil.split((String) range);
			String condition = "";
			if (conditions != null) {
				int index;
				for (int i = 0; i < conditions.length; i++) {//对函数条件length(key)<=5这种不再在开头加key
					index = conditions[i] == null ? -1 : conditions[i].indexOf("(");
					condition += ((i <= 0 ? "" : (logic.isAnd() ? AND : OR))//连接方式
							+ (index >= 0 && conditions[i].substring(index).contains(")") ? "" : key + " ")//函数和非函数条件
							+ conditions[i]);//单个条件
				}
			}
			if (condition.isEmpty()) {
				return "";
			}
			condition = "(" + condition + ")";
			return logic.isNot() ? NOT + condition : condition;
		}

		throw new IllegalArgumentException(key + "{}\":range 中range只能是 用','分隔条件的字符串 或者 可取选项JSONArray！");
	}
	/**WHERE key IN ('key0', 'key1', ... )
	 * @param in
	 * @return IN ('key0', 'key1', ... )
	 * @throws NotExistException 
	 */
	public static String getInString(String key, Object[] in, boolean not) throws NotExistException {
		String condition = "";
		if (in != null) {//返回 "" 会导致 id:[] 空值时效果和没有筛选id一样！
			for (int i = 0; i < in.length; i++) {
				condition += ((i > 0 ? "," : "") + "'" + in[i] + "'");
			}
		}
		if (condition.isEmpty()) {//条件如果存在必须执行，不能忽略。条件为空会导致出错，又很难保证条件不为空(@:条件)，所以还是这样好
			throw new NotExistException(TAG + ".getInString(" + key + ", [], " + not
					+ ") >> condition.isEmpty() >> IN()");
		}
		return (not ? NOT : "") + " IN " + "(" + condition + ")";
	}
	//{} range >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//<> contain <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE key contains value
	 * @param key
	 * @param value
	 * @return 	{@link #getContainString(String, Object[], int)}
	 * @throws NotExistException
	 */
	public static String getContainString(String key, Object value) throws NotExistException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRangeString key = " + key);

		if (value instanceof JSONArray == false) {//TODO 直接掉Like性能最好！
			JSONArray array = new JSONArray();
			array.add(value);
			value = array;
		}
		return getContainString(key, ((JSONArray) value).toArray(), logic.getType());
	}
	/**WHERE key contains childs
	 * @param key
	 * @param childs null ? "" : (empty ? no child : contains childs)
	 * @param type |, &, !
	 * @return LOGIC [  ( key LIKE '[" + childs[i] + "]'  OR  key LIKE '[" + childs[i] + ", %'
	 *   OR  key LIKE '%, " + childs[i] + ", %'  OR  key LIKE '%, " + childs[i] + "]' )  ]
	 * @throws IllegalArgumentException 
	 */
	public static String getContainString(String key, Object[] childs, int type) throws IllegalArgumentException {
		String condition = "";
		if (childs != null) {
			for (int i = 0; i < childs.length; i++) {
				if (childs[i] != null) {
					if (childs[i] instanceof JSON) {
						throw new IllegalArgumentException(key + "<>\":value 中value类型不能为JSON！");
					}
					if (childs[i] instanceof String) {
						childs[i] = "\"" + childs[i] + "\"";
					}
					condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR))
							+ getSearchString(
									key
									, new String[]{
											"[" + childs[i] + "]", //全等
											"[" + childs[i] + ", %", //开始
											"%, " + childs[i] + ", %", //中间
											"%, " + childs[i] + "]" //末尾
									}
									, Logic.TYPE_OR
									);
				}
			}
			if (condition.isEmpty()) {
				condition = (SQL.isEmpty(key, true) + OR + getLikeString(key, "[]"));
			}
		}
		if (condition.isEmpty()) {
			return "";
		}
		return (Logic.isNot(type) ? NOT : "") + "(" + condition + ")";
	}
	//<> contain >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//WHERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	//SET <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
			if (where.containsKey(ID) == false) {
				throw new IllegalArgumentException("请设置" + ID + "！");
			}
			String setString = "";
			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - +, 2 - -
			Object value;
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : where.get(key);
				if (key == null || ID.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				}
				value = where.get(key);
				key = Parser.getRealKey(method, key, false, true);

				setString += (isFirst ? "" : ", ") + (key + "=" + (keyType == 1 ? getAddString(key, value) : (keyType == 2
						? getRemoveString(key, value) : "'" + value + "'") ) );

				isFirst = false;
			}
			if (setString.isEmpty()) {
				throw new NotExistException(TAG + "getSetString  >> setString.isEmpty()");
			}
			return " SET " + setString + " WHERE " + ID + "='" + where.get(ID) + "' ";
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
			String column = request.getString(JSONRequest.KEY_COLUMN);
			String group = request.getString(JSONRequest.KEY_GROUP);
			String having = request.getString(JSONRequest.KEY_HAVING);
			String order = request.getString(JSONRequest.KEY_ORDER);
			request.remove(JSONRequest.KEY_COLUMN);
			request.remove(JSONRequest.KEY_GROUP);
			request.remove(JSONRequest.KEY_HAVING);
			request.remove(JSONRequest.KEY_ORDER);

			if (method == POST) {
				config.setColumn(StringUtil.getString(set.toArray(new String[]{})));

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
				config.setColumn(column);

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

			config.setGroup(group);
			config.setHaving(having);
			config.setOrder(order);
		}

		try {
			config.setId(request.getLongValue(ID));
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
		if (config == null || StringUtil.isNotEmpty(config.getTable(), true) == false) {
			Log.i(TAG, "getSQL  config == null || StringUtil.isNotEmpty(config.getTable(), true) == false >> return null;");
			return null;
		}
		RequestMethod method = config.getMethod();
		switch (method) {
		case POST:
			return "INSERT INTO " + config.getTable() + config.getColumnString() + " VALUES" + config.getValuesString();
		case PUT:
			return "UPDATE " + config.getTable() + config.getSetString();
		case DELETE:
			return "DELETE FROM " + config.getTable() + config.getWhereString();
		default:
			return "SELECT "+ config.getColumnString() + " FROM " + config.getTable() + config.getWhereString()
			+ (method != GET && method != POST_GET ?
					"" : config.getGroupString() + config.getHavingString() + config.getOrderString() )
			+ config.getLimitString();
		}
	}


}
