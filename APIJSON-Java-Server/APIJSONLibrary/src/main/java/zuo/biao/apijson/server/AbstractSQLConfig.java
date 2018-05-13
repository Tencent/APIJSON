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

import static zuo.biao.apijson.JSONObject.KEY_ABOUT;
import static zuo.biao.apijson.JSONObject.KEY_COLUMN;
import static zuo.biao.apijson.JSONObject.KEY_CONDITION;
import static zuo.biao.apijson.JSONObject.KEY_GROUP;
import static zuo.biao.apijson.JSONObject.KEY_HAVING;
import static zuo.biao.apijson.JSONObject.KEY_ID;
import static zuo.biao.apijson.JSONObject.KEY_ID_IN;
import static zuo.biao.apijson.JSONObject.KEY_USER_ID;
import static zuo.biao.apijson.JSONObject.KEY_USER_ID_IN;
import static zuo.biao.apijson.JSONObject.KEY_ORDER;
import static zuo.biao.apijson.JSONObject.KEY_ROLE;
import static zuo.biao.apijson.JSONObject.KEY_SCHEMA;
import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;
import static zuo.biao.apijson.SQL.AND;
import static zuo.biao.apijson.SQL.NOT;
import static zuo.biao.apijson.SQL.OR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.SQL;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.model.Column;
import zuo.biao.apijson.server.model.Table;

/**config sql for JSON Request
 * @author Lemon
 */
public abstract class AbstractSQLConfig implements SQLConfig {
	private static final String TAG = "SQLConfig";


	/**
	 * 表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	 */
	public static final Map<String, String> TABLE_KEY_MAP;
	static {
		TABLE_KEY_MAP = new HashMap<String, String>();
		TABLE_KEY_MAP.put(Table.class.getSimpleName(), Table.TAG);
		TABLE_KEY_MAP.put(Column.class.getSimpleName(), Column.TAG);
	}



	private long id; //Table的id
	private RequestMethod method; //操作方法
	private boolean prepared = true; //预编译
	/**
	 * TODO 被关联的表通过就忽略关联的表？(这个不行 User:{"sex@":"/Comment/toId"})
	 */
	private RequestRole role; //发送请求的用户的角色
	private String schema; //Table所在的数据库
	private String table; //Table名
	private boolean about; //关于，返回数据库表的信息，包括表说明和字段说明
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
	private boolean test; //测试
	private boolean cacheStatic; //静态缓存

	public AbstractSQLConfig(RequestMethod method) {
		setMethod(method);
	}
	public AbstractSQLConfig(RequestMethod method, String table) {
		this(method);
		setTable(table);
	}
	public AbstractSQLConfig(RequestMethod method, int count, int page) {
		this(method);
		setCount(count);
		setPage(page);
	}

	@NotNull
	@Override
	public RequestMethod getMethod() {
		if (method == null) {
			method = GET;
		}
		return method;
	}
	@Override
	public AbstractSQLConfig setMethod(RequestMethod method) {
		this.method = method;
		return this;
	}
	@Override
	public boolean isPrepared() {
		return prepared;
	}
	@Override
	public AbstractSQLConfig setPrepared(boolean prepared) {
		this.prepared = prepared;
		return this;
	}


	@Override
	public long getId() {
		return id;
	}
	@Override
	public AbstractSQLConfig setId(long id) {
		this.id = id;
		return this;
	}

	@Override
	public RequestRole getRole() {
		//不能 @NotNull , AbstractParser#getSQLObject 内当getRole() == null时填充默认值
		return role;
	}
	public AbstractSQLConfig setRole(String roleName) throws Exception {
		return setRole(RequestRole.get(roleName));
	}
	@Override
	public AbstractSQLConfig setRole(RequestRole role) {
		this.role = role;
		return this;
	}

	@Override
	public String getSchema() {
		String sqlTable = getSQLTable();
		if (sqlTable != null && sqlTable.startsWith("`")) {
			return SCHEMA_INFORMATION;
		}
		return schema;
	}
	@Override
	public AbstractSQLConfig setSchema(String schema) {
		this.schema = schema;
		return this;
	}
	/**请求传进来的Table名
	 * @return
	 * @see {@link #getSQLTable()}
	 */
	@Override
	public String getTable() {
		return table;
	}
	/**数据库里的真实Table名
	 * 通过 {@link #TABLE_KEY_MAP} 映射
	 * @return
	 */
	@JSONField(serialize = false)
	@Override
	public String getSQLTable() {
		return TABLE_KEY_MAP.containsKey(table) ? TABLE_KEY_MAP.get(table) : table;
	}
	@JSONField(serialize = false)
	@Override
	public String getTablePath() {
		return getSchema() + "." + getSQLTable();
	}
	@Override
	public AbstractSQLConfig setTable(String table) {
		this.table = table;
		return this;
	}

	@Override
	public boolean isAbout() {
		return about;
	}
	@Override
	public AbstractSQLConfig setAbout(boolean about) {
		this.about = about;
		return this;
	}

	@Override
	public String getGroup() {
		return group;
	}
	public AbstractSQLConfig setGroup(String... keys) {
		return setGroup(StringUtil.getString(keys));
	}
	@Override
	public AbstractSQLConfig setGroup(String group) {
		this.group = group;
		return this;
	}
	@JSONField(serialize = false)
	public String getGroupString() {
		group = StringUtil.getTrimedString(group);
		if (group.isEmpty()) {
			return "";
		}
		
		if (isPrepared()) { //不能通过 ? 来代替，因为SQLExecutor statement.setString后 GROUP BY 'userId' 有单引号，只能返回一条数据，必须去掉单引号才行！
			String[] keys = StringUtil.split(group);
			if (keys != null && keys.length > 0) {
				for (int i = 0; i < keys.length; i++) {
					if (StringUtil.isName(keys[i]) == false) {
						throw new IllegalArgumentException("@group:value 中 value里面用 , 分割的每一项都必须是1个单词！");
					}
				}
			}
		}

		return " GROUP BY " + group;
	}

	@Override
	public String getHaving() {
		return having;
	}
	public AbstractSQLConfig setHaving(String... conditions) {
		return setHaving(StringUtil.getString(conditions));
	}
	@Override
	public AbstractSQLConfig setHaving(String having) {
		this.having = having;
		return this;
	}
	@JSONField(serialize = false)
	public String getHavingString() {
		having = StringUtil.getTrimedString(having);
		if(having.isEmpty()) {
			return ""; 
		}
		if (isPrepared()) {
			throw new UnsupportedOperationException("预编译模式下不允许传 @having:\"condition\" !");
		}
		return " HAVING " + having;
	}

	@Override
	public String getOrder() {
		return order;
	}
	public AbstractSQLConfig setOrder(String... conditions) {
		return setOrder(StringUtil.getString(conditions));
	}
	@Override
	public AbstractSQLConfig setOrder(String order) {
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



	@Override
	public String getColumn() {
		return column;
	}
	public AbstractSQLConfig setColumn(String... keys) {
		return setColumn(StringUtil.getString(keys));
	}
	@Override
	public AbstractSQLConfig setColumn(String column) {
		this.column = column;
		return this;
	}
	@JSONField(serialize = false)
	public String getColumnString() throws NotExistException {
		switch (getMethod()) {
		case HEAD:
		case HEADS:
			if (StringUtil.isEmpty(column, true) == false && StringUtil.isName(column) == false) {
				throw new IllegalArgumentException("HEAD请求: @column:value 中 value必须是1个单词！");
			}
			return SQL.count(column);
		case POST:
			if (StringUtil.isEmpty(column, true)) {
				throw new NotExistException(TAG + "getColumnString  getMethod() = POST"
						+ " >> StringUtil.isEmpty(column, true)");
			}
			
			if (isPrepared()) { //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
				String[] keys = StringUtil.split(column);
				if (keys != null && keys.length > 0) {
					for (int i = 0; i < keys.length; i++) {
						if (StringUtil.isName(keys[i]) == false) {
							throw new IllegalArgumentException("POST请求: 每一个 key:value 中的key都必须是1个单词！");
						}
					}
				}
			}
			
			return "(" + column + ")";
		default:
			column = StringUtil.getString(column);
			if (column.isEmpty()) {
				return "*";
			}
			
			if (isPrepared()) { //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
				String[] keys = StringUtil.split(column);
				if (keys != null && keys.length > 0) {
					String origin;
					String alias;
					int index;
					for (int i = 0; i < keys.length; i++) {
						index = keys[i].indexOf(":"); //StringUtil.split返回数组中，子项不会有null
						origin = index < 0 ? keys[i] : keys[i].substring(0, index);
						alias = index < 0 ? null : keys[i].substring(index + 1);
						
						if (StringUtil.isName(origin) == false || (alias != null && StringUtil.isName(alias) == false)) {
							throw new IllegalArgumentException("GET请求: 预编译模式下 @column:value 中 value里面用 , 分割的每一项 column:alias 中 column必须是1个单词！如果有alias，则alias也必须为1个单词！");
						}
					}
				}
			}
			
			return column.contains(":") == false ? column : column.replaceAll(":", " AS ");//不能在这里改，后续还要用到:
		}
	}


	@Override
	public String getValues() {
		return values;
	}
	@JSONField(serialize = false)
	public String getValuesString() {
		return values;
	}
	public AbstractSQLConfig setValues(String[][] valuess) {
		String s = "";
		if (valuess != null && valuess.length > 0) {
			String[] items = new String[valuess.length];
			for (int i = 0; i < valuess.length; i++) {
				items[i] = "(" + StringUtil.getString(valuess[i]) + ")";
			}
			s = StringUtil.getString(items);
		}
		return setValues(s);
	}
	@Override
	public AbstractSQLConfig setValues(String values) {
		this.values = values;
		return this;
	}

	@Override
	public Map<String, Object> getContent() {
		return content;
	}
	@Override
	public AbstractSQLConfig setContent(Map<String, Object> content) {
		this.content = content;
		return this;
	}

	@Override
	public int getCount() {
		return count;
	}
	@Override
	public AbstractSQLConfig setCount(int count) {
		this.count = count;
		return this;
	}
	@Override
	public int getPage() {
		return page;
	}
	@Override
	public AbstractSQLConfig setPage(int page) {
		this.page = page;
		return this;
	}
	@Override
	public int getPosition() {
		return position;
	}
	@Override
	public AbstractSQLConfig setPosition(int position) {
		this.position = position;
		return this;
	}

	@Override
	public int getQuery() {
		return query;
	}
	@Override
	public AbstractSQLConfig setQuery(int query) {
		this.query = query;
		return this;
	}
	@Override
	public int getType() {
		return type;
	}
	@Override
	public AbstractSQLConfig setType(int type) {
		this.type = type;
		return this;
	}

	@Override
	public boolean isTest() {
		return test;
	}
	@Override
	public AbstractSQLConfig setTest(boolean test) {
		this.test = test;
		return this;
	}
	@Override
	public boolean isCacheStatic() {
		return cacheStatic;
	}
	@Override
	public AbstractSQLConfig setCacheStatic(boolean cacheStatic) {
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
	@Override
	public Map<String, Object> getWhere() {
		return where;
	}
	@Override
	public AbstractSQLConfig setWhere(Map<String, Object> where) {
		this.where = where;
		return this;
	}
	/**
	 * noFunctionChar = false
	 * @param key
	 * @return
	 */
	@JSONField(serialize = false)
	@Override
	public Object getWhere(String key) {
		return getWhere(key, false);
	}
	/**
	 * @param key
	 * @param exactMatch
	 * @return
	 */
	@JSONField(serialize = false)
	@Override
	public Object getWhere(String key, boolean exactMatch) {
		if (exactMatch) {
			return where == null ? null : where.get(key);
		}

		Set<String> set = key == null || where == null ? null : where.keySet();
		if (set != null) {
			synchronized (where) {
				if (where != null) {
					int index;
					for (String k : set) {
						index = k.indexOf(key);
						if (index >= 0 && StringUtil.isName(k.substring(index)) == false) {
							return where.get(k);
						}
					}
				}
			}
		}
		return null;
	}
	@Override
	public AbstractSQLConfig putWhere(String key, Object value) {
		if (key != null) {
			if (where == null) {
				where = new LinkedHashMap<String, Object>();	
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
		return getWhereString(getMethod(), getWhere(), ! isTest());
	}
	/**获取WHERE
	 * @param method
	 * @param where
	 * @return
	 * @throws Exception 
	 */
	public String getWhereString(RequestMethod method, Map<String, Object> where, boolean verifyName) throws Exception {
		Map<String, Object> where2 = where == null || where.isEmpty() ? null : new LinkedHashMap<String, Object>();
		if (where2 == null) {
			return "";
		}

		//强制排序，把id,id{},userId,userId{}放最前面，保证安全、优化性能
		Object id = where.remove(KEY_ID);
		Object idIn = where.remove(KEY_ID_IN);
		Object userId = where.remove(KEY_USER_ID);
		Object userIdIn = where.remove(KEY_USER_ID_IN);

		where2.put(KEY_ID, id);
		where2.put(KEY_ID_IN, idIn);
		where2.put(KEY_USER_ID, userId);
		where2.put(KEY_USER_ID_IN, userIdIn);
		where2.putAll(where);


		Set<Entry<String, Object>> set = where2.entrySet();

		boolean isFirst = true;
		String condition;
		String whereString = "";

		for (Entry<String, Object> entry : set) {
			if (entry == null) {
				continue;
			}
			condition = getWhereItem(entry.getKey(), entry.getValue(), method, verifyName);

			if (StringUtil.isEmpty(condition, true)) {//避免SQL条件连接错误
				continue;
			}

			whereString += (isFirst ? "" : AND) + "(" + condition + ")";

			isFirst = false;
		}

		//还原where，后续可能用到
		where.put(KEY_ID, id);
		where.put(KEY_ID_IN, idIn);
		where.put(KEY_USER_ID, userId);
		where.put(KEY_USER_ID_IN, userIdIn);

		String s = whereString.isEmpty() ? "" : " WHERE " + whereString;

		if (s.isEmpty() && RequestMethod.isQueryMethod(method) == false) {
			throw new UnsupportedOperationException("写操作请求必须带条件！！！");
		}

		return s;
	}

	/**
	 * @param key
	 * @param value
	 * @param method
	 * @param verifyName
	 * @return
	 * @throws Exception
	 */
	private String getWhereItem(String key, Object value
			, RequestMethod method, boolean verifyName) throws Exception {
		Log.d(TAG, "getWhereItem  key = " + key);
		//避免筛选到全部	value = key == null ? null : where.get(key);
		if (key == null || value == null || key.startsWith("@") || key.endsWith("()")) {//关键字||方法, +或-直接报错
			Log.d(TAG, "getWhereItem  key == null || value == null"
					+ " || key.startsWith(@) || key.endsWith(()) >> continue;");
			return null;
		}
		if (key.endsWith("@")) {//引用
			//	key = key.substring(0, key.lastIndexOf("@"));
			throw new IllegalArgumentException(TAG + ".getWhereItem: 字符 " + key + " 不合法！");
		}

		int keyType;
		if (key.endsWith("$")) {
			keyType = 1;
		} 
		else if (key.endsWith("?")) {
			keyType = 2;
		}
		else if (key.endsWith("{}")) {
			keyType = 3;
		}
		else if (key.endsWith("<>")) {
			keyType = 4;
		}
		else { //else绝对不能省，避免再次踩坑！ keyType = 0; 写在for循环外面都没注意！
			keyType = 0;
		}
		key = getRealKey(method, key, false, true, verifyName);

		switch (keyType) {
		case 1:
			return getSearchString(key, value);
		case 2:
			return getRegExpString(key, value);
		case 3:
			return getRangeString(key, value);
		case 4:
			return getContainString(key, value);
		default: //TODO MySQL JSON类型的字段对比 key='[]' 会无结果！ key LIKE '[1, 2, 3]'  //TODO MySQL , 后面有空格！
			return getEqualString(key, value);
		}
	}


	private String getEqualString(String key, Object value) {
		return (key + "=" + getValue(value));
	}


	/**
	 * 使用prepareStatement预编译，值为 ? ，后续动态set进去
	 */
	private List<Object> preparedValues = new ArrayList<>();
	private Object getValue(@NotNull Object value) {
		if (isPrepared()) {
			preparedValues.add(value);
			return "?";
		}
		return "'" + value + "'";
	}
	@Override
	public List<Object> getPreparedValueList() {
		return preparedValues;
	}

	//$ search <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**search key match value
	 * @param in
	 * @return {@link #getSearchString(String, Object[], int)}
	 * @throws IllegalArgumentException 
	 */
	public String getSearchString(String key, Object value) throws IllegalArgumentException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getSearchString key = " + key);

		JSONArray arr = newJSONArray(value);
		if (arr.isEmpty()) {
			return "";
		}
		return getSearchString(key, arr.toArray(), logic.getType());
	}
	/**search key match values
	 * @param in
	 * @return LOGIC [  key LIKE 'values[i]' ]
	 * @throws IllegalArgumentException 
	 */
	public String getSearchString(String key, Object[] values, int type) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "$\":value 中value的类型只能为String或String[]！");
			}
			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getLikeString(key, values[i]);
		}

		return getCondition(Logic.isNot(type), condition);
	}

	/**WHERE key LIKE 'value'
	 * @param key endsWith("!") ? key = key.substring(0, key.length() - 1) + NOT;
	 * @param value
	 * @return key LIKE 'value'
	 */
	public String getLikeString(String key, Object value) {
		return key + " LIKE "  + getValue(value);
	}
	//$ search >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//$ search <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**search key match RegExps value
	 * @param in
	 * @return {@link #getRegExpString(String, Object[], int)}
	 * @throws IllegalArgumentException 
	 */
	public String getRegExpString(String key, Object value) throws IllegalArgumentException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRegExpString key = " + key);

		JSONArray arr = newJSONArray(value);
		if (arr.isEmpty()) {
			return "";
		}
		return getRegExpString(key, arr.toArray(), logic.getType());
	}
	/**search key match RegExp values
	 * @param in
	 * @return LOGIC [  key REGEXP 'values[i]' ]
	 * @throws IllegalArgumentException 
	 */
	public String getRegExpString(String key, Object[] values, int type) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "$\":value 中value的类型只能为String或String[]！");
			}
			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getRegExpString(key, (String) values[i]);
		}

		return getCondition(Logic.isNot(type), condition);
	}

	/**WHERE key REGEXP 'value'
	 * @param key
	 * @param value
	 * @return key REGEXP 'value'
	 */
	public String getRegExpString(String key, String value) {
		return key + " REGEXP " + getValue(value);
	}
	//$ search >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//{} range <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE key > 'key0' AND key <= 'key1' AND ...
	 * @param key
	 * @param range "condition0,condition1..."
	 * @return key condition0 AND key condition1 AND ...
	 * @throws Exception 
	 */
	public String getRangeString(String key, Object range) throws Exception {
		Log.i(TAG, "getRangeString key = " + key);
		if (range == null) {//依赖的对象都没有给出有效值，这个存在无意义。如果是客户端传的，那就能在客户端确定了。
			throw new NotExistException(TAG + "getRangeString(" + key + ", " + range
					+ ") range == null");
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRangeString key = " + key);

		if (range instanceof List) {
			if (logic.isOr() || logic.isNot()) {
				return key + getInString(key, ((List<?>) range).toArray(), logic.isNot());
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

			if (isPrepared()) {
				throw new UnsupportedOperationException("预编译模式下不允许传 key{}:\"condition\" !");
			}

			return getCondition(logic.isNot(), condition);
		}

		throw new IllegalArgumentException(key + "{}:range 类型为" + range.getClass().getSimpleName()
				+ "！range只能是 用','分隔条件的字符串 或者 可取选项JSONArray！");
	}
	/**WHERE key IN ('key0', 'key1', ... )
	 * @param in
	 * @return IN ('key0', 'key1', ... )
	 * @throws NotExistException 
	 */
	public String getInString(String key, Object[] in, boolean not) throws NotExistException {
		String condition = "";
		if (in != null) {//返回 "" 会导致 id:[] 空值时效果和没有筛选id一样！
			for (int i = 0; i < in.length; i++) {
				condition += ((i > 0 ? "," : "") + getValue(in[i]));
			}
		}
		if (condition.isEmpty()) {//条件如果存在必须执行，不能忽略。条件为空会导致出错，又很难保证条件不为空(@:条件)，所以还是这样好
			throw new NotExistException(TAG + ".getInString(" + key + ", [], " + not
					+ ") >> condition.isEmpty() >> IN()");
		}
		return (not ? NOT : "") + " IN (" + condition + ")";
	}
	//{} range >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//<> contain <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE key contains value
	 * @param key
	 * @param value
	 * @return 	{@link #getContainString(String, Object[], int)}
	 * @throws NotExistException
	 */
	public String getContainString(String key, Object value) throws NotExistException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getRangeString key = " + key);

		//TODO 直接调Like性能最好！
		return getContainString(key, newJSONArray(value).toArray(), logic.getType());
	}
	/**WHERE key contains childs
	 * @param key
	 * @param childs null ? "" : (empty ? no child : contains childs)
	 * @param type |, &, !
	 * @return LOGIC [  ( key LIKE '[" + childs[i] + "]'  OR  key LIKE '[" + childs[i] + ", %'
	 *   OR  key LIKE '%, " + childs[i] + ", %'  OR  key LIKE '%, " + childs[i] + "]' )  ]
	 * @throws IllegalArgumentException 
	 */
	public String getContainString(String key, Object[] childs, int type) throws IllegalArgumentException {
		boolean not = Logic.isNot(type);
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
							+ "JSON_CONTAINS(" + key + ", " + getValue(childs[i]) + ")";
				}
			}
			if (condition.isEmpty()) {
				condition = (SQL.isNull(key, true) + OR + getLikeString(key, "[]")); // key = '[]' 无结果！
			} else {
				condition = (SQL.isNull(key, false) + AND + "(" + condition + ")");
			}
		}
		if (condition.isEmpty()) {
			return "";
		}
		return getCondition(not, condition);
	}
	//<> contain >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**拼接条件
	 * @param not
	 * @param condition
	 * @return
	 */
	private static String getCondition(boolean not, String condition) {
		return not ? NOT + "(" + condition + ")" : condition;
	}


	/**转为JSONArray
	 * @param tv
	 * @return
	 */
	@NotNull
	public static JSONArray newJSONArray(Object obj) {
		JSONArray array = new JSONArray();
		if (obj != null) {
			if (obj instanceof Collection) {
				array.addAll((Collection<?>) obj);
			} else {
				array.add(obj);
			}
		}
		return array;
	}

	//WHERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//SET <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**获取SET
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	public String getSetString() throws Exception {
		return getSetString(getMethod(), getContent(), ! isTest());
	}
	/**获取SET
	 * @param method
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	public static String getSetString(RequestMethod method, Map<String, Object> content, boolean verifyName) throws Exception {
		Set<String> set = content == null ? null : content.keySet();
		if (set != null && set.size() > 0) {
			String setString = "";
			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - +, 2 - -
			Object value;
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : content.get(key);
				if (key == null || KEY_ID.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				}
				value = content.get(key);
				key = getRealKey(method, key, false, true, verifyName);

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
	/**SET key = replace(key, 'value', '')
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
			return SQL.replace(key, (String) value, "");// " replace(" + key + ", '" + value + "', '') ";
		}
		throw new IllegalArgumentException(key + "- 对应的值 " + value + " 不是Number,String,Array中的任何一种！");
	}
	//SET >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	/**
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	@Override
	public String getSQL(boolean prepared) throws Exception {
		return getSQL(this.setPrepared(prepared));
	}
	/**
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	public static String getSQL(AbstractSQLConfig config) throws Exception {
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
	private static String getConditionString(String column, String table, AbstractSQLConfig config) throws Exception {
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
		//		//结果错误！SELECT * FROM User AS t0 INNER JOIN
		//      (SELECT id FROM User ORDER BY date ASC LIMIT 20, 10) AS t1 ON t0.id = t1.id
		//		//common case, inner join
		//		condition += config.getLimitString();
		//		return table + " AS t0 INNER JOIN (SELECT id FROM " + condition + ") AS t1 ON t0.id = t1.id";
	}

	/**获取查询配置
	 * @param table
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static AbstractSQLConfig newSQLConfig(RequestMethod method, String table, JSONObject request, Callback callback) throws Exception {
		if (request == null) { // User:{} 这种空内容在查询时也有效
			throw new NullPointerException(TAG + ": newSQLConfig  request == null!");
		}
		AbstractSQLConfig config = callback.getSQLConfig(method, table);

		if (request.isEmpty()) { // User:{} 这种空内容在查询时也有效
			return config; //request.remove(key); 前都可以直接return，之后必须保证 put 回去
		}

		Object idIn = request.get(KEY_ID_IN); //可能是 id{}:">0"

		if (method == POST) {
			if (idIn != null) { //不能在这里确定[]的长度，只能在外面传进来
				if ((idIn instanceof List == false) || ((List<?>)idIn).isEmpty()) { // id{}:[] 表示同时插入多条记录
					throw new IllegalArgumentException("POST请求，生成多条记录请用 id{}:[] ！ [] 类型为JSONArray且不能为空！");
				}
			} else if (request.get(KEY_ID) == null) {
				request.put(KEY_ID, System.currentTimeMillis());
			}
		}

		//对id和id{}处理，这两个一定会作为条件
		Long id = request.getLong(KEY_ID);
		if (id != null) { //null无效
			if (id <= 0) { //一定没有值
				throw new NotExistException(TAG + ": newSQLConfig " + table + ".id <= 0");
			}

			if (idIn != null && idIn instanceof List) { //共用idArr场景少性能差
				if (idIn != null && ((List<?>) idIn).contains(id) == false) {//empty有效  BaseModel.isEmpty(idArr) == false) {
					Log.w(TAG, "newSQLConfig  id > 0 >> idInObj != null && idInObj.contains(id) == false >> return null;");
					throw new NotExistException(TAG + ": newSQLConfig  idIn != null && ((JSONArray) idIn).contains(id) == false");
				}
			}
		}


		String role = request.getString(KEY_ROLE);
		String schema = request.getString(KEY_SCHEMA);
		boolean about = request.getBooleanValue(KEY_ABOUT);
		String condition = request.getString(KEY_CONDITION);
		String column = request.getString(KEY_COLUMN);
		String group = request.getString(KEY_GROUP);
		String having = request.getString(KEY_HAVING);
		String order = request.getString(KEY_ORDER);

		//强制作为条件且放在最前面优化性能
		request.remove(KEY_ID);
		request.remove(KEY_ID_IN);
		//关键词
		request.remove(KEY_ROLE);
		request.remove(KEY_SCHEMA);
		request.remove(KEY_ABOUT);
		request.remove(KEY_CONDITION);
		request.remove(KEY_COLUMN);
		request.remove(KEY_GROUP);
		request.remove(KEY_HAVING);
		request.remove(KEY_ORDER);


		Map<String, Object> tableWhere = new LinkedHashMap<String, Object>();//保证顺序好优化 WHERE id > 1 AND name LIKE...
		if (about) { //查询字段属性
			if (RequestMethod.isQueryMethod(method) == false) {
				throw new UnsupportedOperationException(config.getTable()
						+ " 被 " + KEY_ABOUT + " 标注，只能进行 GET,HEAD 等查询操作！");
			}

			tableWhere.put(TABLE_SCHEMA, schema);
			tableWhere.put(TABLE_NAME, config.getSQLTable());
			config.setTable(Column.class.getSimpleName());

			schema = SCHEMA_INFORMATION;

			column = StringUtil.getString(column);
			if (column.isEmpty() && RequestMethod.isHeadMethod(method, true) == false) {
				column = "column_name,column_type,is_nullable,column_default,column_comment";
			}
		}


		//已经remove了id和id{}，以及@key
		Set<String> set = request.keySet(); //前面已经判断request是否为空
		if (method == POST) {//POST操作
			if (set != null && set.isEmpty() == false) { //不能直接return，要走完下面的流程
				List<Object> idList;
				if (id != null) { //单条记录
					if (idIn != null) {
						throw new IllegalArgumentException("POST请求中 id 和 id{} 不能同时存在!");
					}

					idList = new ArrayList<Object>(1);
					idList.add(id);
				} else { //多条记录
					idList = new ArrayList<Object>((JSONArray) idIn);
				}

				//idIn不为空时，valuesString有多条，唯一的区别就是id
				String[] columns = set.toArray(new String[]{});

				Collection<Object> valueCollection = request.values();
				Object[] values = valueCollection == null ? null : valueCollection.toArray();

				if (values == null || values.length != columns.length) {
					throw new Exception("服务器内部错误:\n" + TAG
							+ " newSQLConfig  values == null || values.length != columns.length !");
				}
				column = KEY_ID + "," + StringUtil.getString(columns); //set已经判断过不为空
				final int size = columns.length + 1; //以key数量为准

				String[][] valuess = new String[idList.size()][]; // [idList.size()][]
				String[] items; //(item0, item1, ...)
				for (int i = 0; i < idList.size(); i++) {
					items = new String[size];
					items[0] = "'" + idList.get(i) + "'"; //第0个就是id。所有的值都加 '' 避免SQL注入风险
					for (int j = 1; j < size; j++) {
						items[j] = "'" + values[j-1] + "'"; //从第1个开始，允许"null"
					}
					valuess[i] = items;
				}
				config.setValues(valuess);
			}
		} 
		else { //非POST操作
			final boolean isWhere = method != PUT;//除了POST,PUT，其它全是条件！！！

			//条件<<<<<<<<<<<<<<<<<<<
			List<String> conditionList = null;
			if (isWhere == false) { //减少不必要的步骤
				if (method == PUT || method == DELETE) {
					String[] conditions = StringUtil.split(condition);
					//Arrays.asList()返回值不支持add方法！
					conditionList = conditions == null || conditions.length <= 0 ? null : Arrays.asList(conditions);
				}
			}
			//条件>>>>>>>>>>>>>>>>>>>

			//强制作为条件且放在最前面优化性能
			if (id != null) {
				tableWhere.put(KEY_ID, id);
			}
			if (idIn != null) {
				tableWhere.put(KEY_ID_IN, idIn);
			}

			Map<String, Object> tableContent = new HashMap<String, Object>();
			Object value;
			for (String key : set) {
				value = request.get(key);
				if (value instanceof JSONObject == false) {//只允许常规Object
					//解决AccessVerifier新增userId没有作为条件，而是作为内容，导致PUT，DELETE出错
					if (isWhere || (conditionList != null && conditionList.contains(key))) {
						tableWhere.put(key, value);
					} else {
						tableContent.put(key, value);//一样 instanceof JSONArray ? JSON.toJSONString(value) : value);
					}
				}
			}

			config.setContent(tableContent);
		}


		config.setWhere(tableWhere);					

		config.setId(id == null ? 0 : id);
		//在	tableWhere 第0个		config.setIdIn(idIn);

		config.setRole(role);
		//TODO condition组合，优先 |		config.setCondition(condition);
		config.setSchema(schema);
		config.setAbout(about);
		config.setColumn(column);
		config.setGroup(group);
		config.setHaving(having);
		config.setOrder(order);

		//后面还可能用到，要还原
		//id或id{}条件
		request.put(KEY_ID, id);
		request.put(KEY_ID_IN, idIn);
		//关键词
		request.put(KEY_ROLE, role);
		request.put(KEY_SCHEMA, schema);
		request.put(KEY_ABOUT, about);
		request.put(KEY_CONDITION, condition);
		request.put(KEY_COLUMN, column);
		request.put(KEY_GROUP, group);
		request.put(KEY_HAVING, having);
		request.put(KEY_ORDER, order);


		return config;
	}



	/**获取客户端实际需要的key
	 * verifyName = true
	 * @param method
	 * @param originKey
	 * @param isTableKey
	 * @param saveLogic 保留逻辑运算符 & | !
	 * @return
	 */
	public static String getRealKey(RequestMethod method, String originKey
			, boolean isTableKey, boolean saveLogic) throws Exception {
		return getRealKey(method, originKey, isTableKey, saveLogic, true);
	}
	/**获取客户端实际需要的key
	 * @param method
	 * @param originKey
	 * @param isTableKey
	 * @param saveLogic 保留逻辑运算符 & | !
	 * @param verifyName 验证key名是否符合代码变量/常量名
	 * @return
	 */
	public static String getRealKey(RequestMethod method, String originKey
			, boolean isTableKey, boolean saveLogic, boolean verifyName) throws Exception {
		Log.i(TAG, "getRealKey  saveLogic = " + saveLogic + "; originKey = " + originKey);
		if (originKey == null || originKey.startsWith("`") || zuo.biao.apijson.JSONObject.isArrayKey(originKey)) {
			Log.w(TAG, "getRealKey  originKey == null || originKey.startsWith(`)"
					+ " || zuo.biao.apijson.JSONObject.isArrayKey(originKey) >>  return originKey;");
			return originKey;
		}

		String key = new String(originKey);
		if (key.endsWith("$")) {//搜索，查询时处理
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("?")) {//匹配正则表达式，查询时处理
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("{}")) {//被包含，或者说key对应值处于value的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("<>")) {//包含，或者说value处于key对应值的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("()")) {//方法，查询完后处理，先用一个Map<key,function>保存？
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("@")) {//引用，引用对象查询完后处理。fillTarget中暂时不用处理，因为非GET请求都是由给定的id确定，不需要引用
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("+")) {//延长，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.length() - 1);
			}
		} 
		else if (key.endsWith("-")) {//缩减，PUT查询时处理
			if (method == PUT) {//不为PUT就抛异常
				key = key.substring(0, key.length() - 1);
			}
		}

		String last = null;//不用Logic优化代码，否则 key 可能变为 key| 导致 key=value 变成 key|=value 而出错
		if (RequestMethod.isQueryMethod(method)) {//逻辑运算符仅供GET,HEAD方法使用
			last = key.isEmpty() ? "" : key.substring(key.length() - 1);
			if ("&".equals(last) || "|".equals(last) || "!".equals(last)) {
				key = key.substring(0, key.length() - 1);
			} else {
				last = null;//避免key + StringUtil.getString(last)错误延长
			}
		}

		//"User:toUser":User转换"toUser":User, User为查询同名Table得到的JSONObject。交给客户端处理更好
		if (isTableKey) {//不允许在column key中使用Type:key形式
			key = Pair.parseEntry(key, true).getKey();//table以左边为准
		} else {
			key = Pair.parseEntry(key).getValue();//column以右边为准
		}

		if (verifyName && StringUtil.isName(key.startsWith("@") ? key.substring(1) : key) == false) {
			throw new IllegalArgumentException(method + "请求，字符 " + originKey + " 不合法！");
		}

		if (saveLogic && last != null) {
			key = key + last;
		}
		Log.i(TAG, "getRealKey  return key = " + key);
		return key;
	}


	public interface Callback {
		AbstractSQLConfig getSQLConfig(RequestMethod method, String table);
	}

}
