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

import static zuo.biao.apijson.JSONObject.KEY_COLUMN;
import static zuo.biao.apijson.JSONObject.KEY_GROUP;
import static zuo.biao.apijson.JSONObject.KEY_HAVING;
import static zuo.biao.apijson.JSONObject.KEY_ORDER;
import static zuo.biao.apijson.JSONObject.KEY_ROLE;
import static zuo.biao.apijson.JSONObject.KEY_SCHEMA;
import static zuo.biao.apijson.JSONRequest.KEY_COUNT;
import static zuo.biao.apijson.JSONRequest.KEY_PAGE;
import static zuo.biao.apijson.JSONRequest.KEY_QUERY;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;
import static zuo.biao.apijson.SQL.AND;
import static zuo.biao.apijson.SQL.NOT;
import static zuo.biao.apijson.SQL.OR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import apijson.demo.server.model.User;
import apijson.demo.server.model.UserPrivacy;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.SQL;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.Logic;
import zuo.biao.apijson.server.Parser;
import zuo.biao.apijson.server.exception.NotExistException;

/**config sql for JSON Request
 * @author Lemon
 */
public class SQLConfig {
	private static final String TAG = "SQLConfig";

	public static final String MYSQL_URI = "jdbc:mysql://localhost:3306";//TODO 改成你自己的
	public static final String MYSQL_SCHEMA = "sys";//TODO 改成你自己的
	public static final String MYSQL_ACCOUNT = "root";//TODO 改成你自己的
	public static final String MYSQL_PASSWORD = "root";//TODO 改成你自己的


	public static final String ID = JSONResponse.KEY_ID;

	public static final List<String> ARRAY_KEY_LIST;
	static {
		ARRAY_KEY_LIST = new ArrayList<String>();
		ARRAY_KEY_LIST.add(KEY_QUERY);
		ARRAY_KEY_LIST.add(KEY_COUNT);
		ARRAY_KEY_LIST.add(KEY_PAGE);
	}
	public static final List<String> TABLE_KEY_LIST;
	static {
		TABLE_KEY_LIST = new ArrayList<String>();
		TABLE_KEY_LIST.add(KEY_ROLE);
		TABLE_KEY_LIST.add(KEY_SCHEMA);
		TABLE_KEY_LIST.add(KEY_COLUMN);
		TABLE_KEY_LIST.add(KEY_GROUP);
		TABLE_KEY_LIST.add(KEY_HAVING);
		TABLE_KEY_LIST.add(KEY_ORDER);
	}



	//表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	public static final Map<String, String> TABLE_KEY_MAP;
	static {
		TABLE_KEY_MAP = new HashMap<String, String>();
		TABLE_KEY_MAP.put(User.class.getSimpleName(), "apijson_user");
		TABLE_KEY_MAP.put(UserPrivacy.class.getSimpleName(), "apijson_user_privacy");
	}



	private long id; //Table的id
	private RequestMethod method; //操作方法
	private RequestRole role; //发送请求的用户的角色
	private String schema; //Table所在的数据库
	private String table; //Table名
	private String group; //分组方式的字符串数组，','分隔
	private String having; //聚合函数的字符串数组，','分隔
	private String order; //排序方式的字符串数组，','分隔
	private String column; //Table内字段名(或函数名，仅查询操作可用)的字符串数组，','分隔
	private String values; //对应Table内字段的值的字符串数组，','分隔
	private Map<String, Object> content; //Request内容，key:value形式，column = content.keySet()，values = content.values()
	private Map<String, Object> where; //筛选条件，key:value形式


	//array item <<<<<<<<<<
	private int count; //Table数量
	private int page; //Table所在页码
	private int position; //Table在[]中的位置
	private int query; //JSONRequest.query
	private int type; //ObjectParser.type
	//array item >>>>>>>>>>
	private boolean cacheStatic; //静态缓存

	public SQLConfig(RequestMethod method) {
		setMethod(method);
	}
	public SQLConfig(RequestMethod method, String table) {
		this(method);
		setTable(table);
	}
	public SQLConfig(RequestMethod method, int count, int page) {
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
	public SQLConfig setMethod(RequestMethod method) {
		this.method = method;
		return this;
	}



	public long getId() {
		return id;
	}
	public SQLConfig setId(long id) {
		this.id = id;
		return this;
	}

	public RequestRole getRole() {
		return role;
	}
	public SQLConfig setRole(String roleName) {
		return setRole(RequestRole.get(roleName));
	}
	public SQLConfig setRole(RequestRole role) {
		this.role = role;
		return this;
	}

	public String getSchema() {
		if (StringUtil.isEmpty(schema, true)) {
			schema = MYSQL_SCHEMA; //非默认Schema必须要有
		}
		return schema;
	}
	public SQLConfig setSchema(String schema) {
		this.schema = schema;
		return this;
	}
	/**请求传进来的Table名
	 * @return
	 * @see {@link #getSQLTable()}
	 */
	public String getTable() {
		return table;
	}
	/**数据库里的真实Table名
	 * 通过 {@link #TABLE_KEY_MAP} 映射
	 * @return
	 */
	@JSONField(serialize = false)
	public String getSQLTable() {
		return TABLE_KEY_MAP.containsKey(table) ? TABLE_KEY_MAP.get(table) : table;
	}
	@JSONField(serialize = false)
	public String getTablePath() {
		return getSchema() + "." + getSQLTable();
	}
	public SQLConfig setTable(String table) {
		this.table = table;
		return this;
	}

	public String getGroup() {
		return group;
	}
	public SQLConfig setGroup(String... keys) {
		return setGroup(StringUtil.getString(keys));
	}
	public SQLConfig setGroup(String group) {
		this.group = group;
		return this;
	}
	@JSONField(serialize = false)
	public String getGroupString() {
		group = StringUtil.getTrimedString(group);
		return group.isEmpty() ? "" : " GROUP BY " + group;
	}

	public String getHaving() {
		return having;
	}
	public SQLConfig setHaving(String... conditions) {
		return setHaving(StringUtil.getString(conditions));
	}
	public SQLConfig setHaving(String having) {
		this.having = having;
		return this;
	}
	@JSONField(serialize = false)
	public String getHavingString() {
		having = StringUtil.getTrimedString(having);
		return having.isEmpty() ? "" : " HAVING " + having;
	}

	public String getOrder() {
		return order;
	}
	public SQLConfig setOrder(String... conditions) {
		return setOrder(StringUtil.getString(conditions));
	}
	public SQLConfig setOrder(String order) {
		this.order = order;
		return this;
	}
	@JSONField(serialize = false)
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



	public String getColumn() {
		return column;
	}
	public SQLConfig setColumn(String... keys) {
		return setColumn(StringUtil.getString(keys));
	}
	public SQLConfig setColumn(String column) {
		this.column = column;
		return this;
	}
	@JSONField(serialize = false)
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


	public String getValues() {
		return values;
	}
	@JSONField(serialize = false)
	public String getValuesString() {
		return values;
	}
	public SQLConfig setValues(String[][] values) {
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
	public SQLConfig setValues(String values) {
		this.values = values;
		return this;
	}

	public Map<String, Object> getContent() {
		return content;
	}
	public SQLConfig setContent(Map<String, Object> content) {
		this.content = content;
		return this;
	}

	public int getCount() {
		return count;
	}
	public SQLConfig setCount(int count) {
		this.count = count;
		return this;
	}
	public int getPage() {
		return page;
	}
	public SQLConfig setPage(int page) {
		this.page = page;
		return this;
	}
	public int getPosition() {
		return position;
	}
	public SQLConfig setPosition(int position) {
		this.position = position;
		return this;
	}

	public int getQuery() {
		return query;
	}
	public SQLConfig setQuery(int query) {
		this.query = query;
		return this;
	}
	public int getType() {
		return type;
	}
	public SQLConfig setType(int type) {
		this.type = type;
		return this;
	}

	public boolean isCacheStatic() {
		return cacheStatic;
	}
	public SQLConfig setCacheStatic(boolean cacheStatic) {
		this.cacheStatic = cacheStatic;
		return this;
	}


	/**获取初始位置offset
	 * @return
	 */
	@JSONField(serialize = false)
	public int getOffset() {
		return getOffset(getPage(), getCount());
	}
	/**获取初始位置offset
	 * @param page
	 * @param count
	 * @return
	 */
	public static int getOffset(int page, int count) {
		return page*count;
	}
	/**获取限制数量
	 * @return
	 */
	@JSONField(serialize = false)
	public String getLimitString() {
		return getLimitString(getPage(), getCount());// + 1);
	}
	/**获取限制数量
	 * @param limit
	 * @return
	 */
	public static String getLimitString(int page, int count) {
		return count <= 0 ? "" : " LIMIT " + getOffset(page, count) + ", " + count;
	}

	//WHERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public Map<String, Object> getWhere() {
		return where;
	}
	public SQLConfig setWhere(Map<String, Object> where) {
		this.where = where;
		return this;
	}
	/**
	 * noFunctionChar = false
	 * @param key
	 * @return
	 */
	@JSONField(serialize = false)
	public Object getWhere(String key) {
		return getWhere(key, false);
	}
	/**
	 * @param key
	 * @param exactMatch
	 * @return
	 */
	@JSONField(serialize = false)
	public Object getWhere(String key, boolean exactMatch) {
		if (exactMatch) {
			return where == null ? null : where.get(key);
		}

		Set<String> set = key == null || where == null ? null : where.keySet();
		if (set != null) {
			synchronized (where) {
				int index;
				for (String k : set) {
					index = k.indexOf(key);
					if (index > 0 && StringUtil.isWord(k.substring(index)) == false) {
						return where.get(k);
					}
				}
			}
		}
		return null;
	}
	public SQLConfig addWhere(String key, Object value) {
		if (key != null) {
			if (where == null) {
				where = new HashMap<String, Object>();	
			}
			where.put(key, value);
		}
		return this;
	}

	/**获取WHERE
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
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
							+ (index >= 0 && index < conditions[i].indexOf(")") ? "" : key + " ")//函数和非函数条件
							+ conditions[i]);//单个条件
				}
			}
			if (condition.isEmpty()) {
				return "";
			}
			condition = "(" + condition + ")";
			return logic.isNot() ? NOT + condition : condition;
		}

		throw new IllegalArgumentException(key + "{}:range 类型为" + range.getClass().getSimpleName()
				+ "！range只能是 用','分隔条件的字符串 或者 可取选项JSONArray！");
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

		if (value instanceof JSONArray == false) {//TODO 直接调Like性能最好！
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
	@JSONField(serialize = false)
	public String getSetString() throws Exception {
		return getSetString(getMethod(), getContent());
	}
	/**获取SET
	 * @param method
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	public static String getSetString(RequestMethod method, Map<String, Object> content) throws Exception {
		Set<String> set = content == null ? null : content.keySet();
		if (set != null && set.size() > 0) {
			String setString = "";
			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - +, 2 - -
			Object value;
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : content.get(key);
				if (key == null || ID.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				}
				value = content.get(key);
				key = Parser.getRealKey(method, key, false, true);

				setString += (isFirst ? "" : ", ") + (key + "=" + (keyType == 1 ? getAddString(key, value) : (keyType == 2
						? getRemoveString(key, value) : "'" + value + "'") ) );

				isFirst = false;
			}
			if (setString.isEmpty()) {
				throw new NotExistException(TAG + "getSetString  >> setString.isEmpty()");
			}
			return " SET " + setString;
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


	/**
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	public String getSQL() throws Exception {
		return getSQL(this);
	}
	/**
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	public static String getSQL(SQLConfig config) throws Exception {
		String tablePath = config == null ? null : config.getTablePath();
		if (StringUtil.isNotEmpty(tablePath, true) == false) {
			Log.i(TAG, "getSQL  StringUtil.isNotEmpty(tablePath, true) == false >> return null;");
			return null;
		}
		switch (config.getMethod()) {
		case POST:
			return "INSERT INTO " + tablePath + config.getColumnString() + " VALUES" + config.getValuesString();
		case PUT:
			return "UPDATE " + tablePath + config.getSetString() + config.getWhereString();
		case DELETE:
			return "DELETE FROM " + tablePath + config.getWhereString();
		default:
			String column = config.getColumnString();
			return "SELECT " + column + " FROM " + getConditionString(column, tablePath, config);
		}
	}

	/**获取条件SQL字符串
	 * @param page 
	 * @param column
	 * @param table
	 * @param where
	 * @return
	 * @throws Exception 
	 */
	private static String getConditionString(String column, String table, SQLConfig config) throws Exception {
		String where = config.getWhereString();

		String condition = table + where + (
				RequestMethod.isGetMethod(config.getMethod(), true) == false ?
						"" : config.getGroupString() + config.getHavingString() + config.getOrderString()
				)
				; //+ config.getLimitString();

		//no need to optimize
		//		if (config.getPage() <= 0 || ID.equals(column.trim())) {
		return condition + config.getLimitString();
		//		}
		//
		//
		//		//order: id+ -> id >= idOfStartIndex; id- -> id <= idOfStartIndex <<<<<<<<<<<<<<<<<<<
		//		String order = StringUtil.getNoBlankString(config.getOrder());
		//		List<String> orderList = order.isEmpty() ? null : Arrays.asList(StringUtil.split(order));
		//
		//		int type = 0;
		//		if (BaseModel.isEmpty(orderList) || BaseModel.isContain(orderList, ID+"+")) {
		//			type = 1;
		//		}
		//		else if (BaseModel.isContain(orderList, ID+"-")) {
		//			type = 2;
		//		}
		//
		//		if (type > 0) {
		//			return condition.replace("WHERE",
		//					"WHERE id " + (type == 1 ? ">=" : "<=") + " (SELECT id FROM " + table
		//					+ where + " ORDER BY id " + (type == 1 ? "ASC" : "DESC") + " LIMIT " + config.getOffset() + ", 1) AND"
		//					)
		//					+ " LIMIT " + config.getCount(); //子查询起始id不一定准确，只能作为最小可能！ ;//
		//		}
		//		//order: id+ -> id >= idOfStartIndex; id- -> id <= idOfStartIndex >>>>>>>>>>>>>>>>>>
		//
		//
		//		//结果错误！SELECT * FROM sys.User AS t0 INNER JOIN (SELECT id FROM sys.User ORDER BY date ASC LIMIT 20, 10) AS t1 ON t0.id = t1.id
		//		//common case, inner join
		//		condition += config.getLimitString();
		//		return table + " AS t0 INNER JOIN (SELECT id FROM " + condition + ") AS t1 ON t0.id = t1.id";
	}

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 */
	public static synchronized SQLConfig newQueryConfig(RequestMethod method, String table, JSONObject request) {
		SQLConfig config = new SQLConfig(method, table);

		if (method == POST && request != null && request.get(ID) == null) {
			request.put(ID, System.currentTimeMillis());
		}
		Set<String> set = request == null ? null : request.keySet();
		if (set != null) {
			String role = request.getString(KEY_ROLE);
			String schema = request.getString(KEY_SCHEMA);
			String column = request.getString(KEY_COLUMN);
			String group = request.getString(KEY_GROUP);
			String having = request.getString(KEY_HAVING);
			String order = request.getString(KEY_ORDER);
			request.remove(KEY_ROLE);
			request.remove(KEY_SCHEMA);
			request.remove(KEY_COLUMN);
			request.remove(KEY_GROUP);
			request.remove(KEY_HAVING);
			request.remove(KEY_ORDER);

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

				final boolean isWhere = method != PUT;//除了POST,PUT，其它全是条件！！！

				Map<String, Object> tableContent = new HashMap<String, Object>();
				Map<String, Object> tableWhere = new HashMap<String, Object>();
				Object value;
				for (String key : set) {
					value = request.get(key);
					if (value instanceof JSONObject == false) {//只允许常规Object
						//解决AccessVerifier新增userId没有作为条件，而是作为内容，导致PUT，DELETE出错
						if (isWhere || ID.equals(key)) {
							tableWhere.put(key, value);
						} else {
							tableContent.put(key, value);//一样 instanceof JSONArray ? JSON.toJSONString(value) : value);
						}
					}
				}

				config.setContent(tableContent);
				config.setWhere(tableWhere);					
			}

			config.setRole(role);
			config.setSchema(schema);
			config.setGroup(group);
			config.setHaving(having);
			config.setOrder(order);

			//后面还可能用到，要还原
			request.put(KEY_ROLE, role);
			request.put(KEY_SCHEMA, schema);
			request.put(KEY_COLUMN, column);
			request.put(KEY_GROUP, group);
			request.put(KEY_HAVING, having);
			request.put(KEY_ORDER, order);
		}

		try {
			config.setId(request.getLongValue(ID));
		} catch (Exception e) {
			// empty
		}
		return config;
	}


	// 导致getSetString，未设置id错误
	//	@Override
	//	public String toString() {
	//		return JSON.toJSONString(this);
	//	}

}
