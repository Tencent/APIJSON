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

import static zuo.biao.apijson.JSONObject.KEY_COLUMN;
import static zuo.biao.apijson.JSONObject.KEY_COMBINE;
import static zuo.biao.apijson.JSONObject.KEY_DATABASE;
import static zuo.biao.apijson.JSONObject.KEY_EXPLAIN;
import static zuo.biao.apijson.JSONObject.KEY_CACHE;
import static zuo.biao.apijson.JSONObject.KEY_FROM;
import static zuo.biao.apijson.JSONObject.KEY_GROUP;
import static zuo.biao.apijson.JSONObject.KEY_HAVING;
import static zuo.biao.apijson.JSONObject.KEY_ID;
import static zuo.biao.apijson.JSONObject.KEY_ORDER;
import static zuo.biao.apijson.JSONObject.KEY_ROLE;
import static zuo.biao.apijson.JSONObject.KEY_SCHEMA;
import static zuo.biao.apijson.JSONObject.KEY_USER_ID;
import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEADS;
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
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import zuo.biao.apijson.JSON;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.NotNull;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.RequestRole;
import zuo.biao.apijson.SQL;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.model.Column;
import zuo.biao.apijson.server.model.ExtendedProperty;
import zuo.biao.apijson.server.model.PgAttribute;
import zuo.biao.apijson.server.model.PgClass;
import zuo.biao.apijson.server.model.SysColumn;
import zuo.biao.apijson.server.model.SysTable;
import zuo.biao.apijson.server.model.Table;

/**config sql for JSON Request
 * @author Lemon
 */
public abstract class AbstractSQLConfig implements SQLConfig {
	private static final String TAG = "AbstractSQLConfig";

	public static String DEFAULT_DATABASE = DATABASE_MYSQL;
	public static String DEFAULT_SCHEMA = "sys";
	public static String PREFFIX_DISTINCT = "DISTINCT ";

	/**
	 * 表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	 */
	public static final Map<String, String> TABLE_KEY_MAP;
	public static final List<String> DATABASE_LIST;
	static {
		TABLE_KEY_MAP = new HashMap<String, String>();
		TABLE_KEY_MAP.put(Table.class.getSimpleName(), Table.TABLE_NAME);
		TABLE_KEY_MAP.put(Column.class.getSimpleName(), Column.TABLE_NAME);
		TABLE_KEY_MAP.put(PgClass.class.getSimpleName(), PgClass.TABLE_NAME);
		TABLE_KEY_MAP.put(PgAttribute.class.getSimpleName(), PgAttribute.TABLE_NAME);
		TABLE_KEY_MAP.put(SysTable.class.getSimpleName(), SysTable.TABLE_NAME);
		TABLE_KEY_MAP.put(SysColumn.class.getSimpleName(), SysColumn.TABLE_NAME);
		TABLE_KEY_MAP.put(ExtendedProperty.class.getSimpleName(), ExtendedProperty.TABLE_NAME);

		DATABASE_LIST = new ArrayList<>();
		DATABASE_LIST.add(DATABASE_MYSQL);
		DATABASE_LIST.add(DATABASE_POSTGRESQL);
		DATABASE_LIST.add(DATABASE_SQLSERVER);
		DATABASE_LIST.add(DATABASE_ORACLE);
	}


	@NotNull
	@Override
	public String getIdKey() {
		return KEY_ID;
	}
	@NotNull
	@Override
	public String getUserIdKey() {
		return KEY_USER_ID;
	}


	private Object id; //Table的id
	private RequestMethod method; //操作方法
	private boolean prepared = true; //预编译
	private boolean main = true;
	/**
	 * TODO 被关联的表通过就忽略关联的表？(这个不行 User:{"sex@":"/Comment/toId"})
	 */
	private RequestRole role; //发送请求的用户的角色
	private boolean distinct = false;
	private String database; //表所在的数据库类型
	private String schema; //表所在的数据库名
	private String table; //表名
	private String alias; //表别名
	private String group; //分组方式的字符串数组，','分隔
	private String having; //聚合函数的字符串数组，','分隔
	private String order; //排序方式的字符串数组，','分隔
	private Subquery from; //子查询临时表
	private List<String> column; //表内字段名(或函数名，仅查询操作可用)的字符串数组，','分隔
	private List<List<Object>> values; //对应表内字段的值的字符串数组，','分隔
	private Map<String, Object> content; //Request内容，key:value形式，column = content.keySet()，values = content.values()
	private Map<String, Object> where; //筛选条件，key:value形式
	private Map<String, List<String>> combine; //条件组合，{ "&":[key], "|":[key], "!":[key] }


	//array item <<<<<<<<<<
	private int count; //Table数量
	private int page; //Table所在页码
	private int position; //Table在[]中的位置
	private int query; //JSONRequest.query
	private int type; //ObjectParser.type
	private int cache;
	private boolean explain;

	private List<Join> joinList; //连表 配置列表
	//array item >>>>>>>>>>
	private boolean test; //测试

	private String procedure;

	public SQLConfig setProcedure(String procedure) {
		this.procedure = procedure;
		return this;
	}
	public String getProcedure() {
		return procedure;
	}

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
	public boolean isMain() {
		return main;
	}
	@Override
	public AbstractSQLConfig setMain(boolean main) {
		this.main = main;
		return this;
	}


	@Override
	public Object getId() {
		return id;
	}
	@Override
	public AbstractSQLConfig setId(Object id) {
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
	public boolean isDistinct() {
		return distinct;
	}
	@Override
	public SQLConfig setDistinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	@Override
	public String getDatabase() {
		return database;
	}
	@Override
	public SQLConfig setDatabase(String database) {
		this.database = database;
		return this;
	}
	/**
	 * @return db == null ? DEFAULT_DATABASE : db
	 */
	@NotNull
	public String getSQLDatabase() {
		String db = getDatabase();
		return db == null ? DEFAULT_DATABASE : db;  // "" 表示已设置，不需要用全局默认的 StringUtil.isEmpty(db, false)) {
	}

	@Override
	public boolean isMySQL() {
		return isMySQL(getSQLDatabase());
	}
	public static boolean isMySQL(String db) {
		return DATABASE_MYSQL.equals(db);
	}
	@Override
	public boolean isPostgreSQL() {
		return isPostgreSQL(getSQLDatabase());
	}
	public static boolean isPostgreSQL(String db) {
		return DATABASE_POSTGRESQL.equals(db);
	}
	@Override
	public boolean isSQLServer() {
		return isSQLServer(getSQLDatabase());
	}
	public static boolean isSQLServer(String db) {
		return DATABASE_SQLSERVER.equals(db);
	}
	@Override
	public boolean isOracle() {
		return isOracle(getSQLDatabase());
	}
	public static boolean isOracle(String db) {
		return DATABASE_ORACLE.equals(db);
	}

	@Override
	public String getQuote() {
		return isMySQL() ? "`" : "\"";
	}

	@Override
	public String getSchema() {
		return schema;
	}
	/**
	 * @param sqlTable
	 * @return
	 */
	@NotNull
	public String getSQLSchema() {
		String table = getTable();
		//强制，避免因为全局默认的 @schema 自动填充进来，导致这几个类的 schema 为 sys 等其它值
		if (Table.TAG.equals(table) || Column.TAG.equals(table)) {
			return SCHEMA_INFORMATION; //MySQL, PostgreSQL, SQL Server 都有的
		}
		if (PgClass.TAG.equals(table) || PgAttribute.TAG.equals(table)) {
			return ""; //PostgreSQL 的 pg_class 和 pg_attribute 表好像不属于任何 Schema
		}
		if (SysTable.TAG.equals(table) || SysColumn.TAG.equals(table) || ExtendedProperty.TAG.equals(table)) {
			return SCHEMA_SYS; //SQL Server 在 sys 中的属性比 information_schema 中的要全，能拿到注释
		}

		String sch = getSchema();
		return sch == null ? DEFAULT_SCHEMA : sch;
	}
	@Override
	public AbstractSQLConfig setSchema(String schema) {
		if (schema != null) {
			String quote = getQuote();
			String s = schema.startsWith(quote) && schema.endsWith(quote) ? schema.substring(1, schema.length() - 1) : schema;
			if (StringUtil.isEmpty(s, true) == false && StringUtil.isName(s) == false) {
				throw new IllegalArgumentException("@schema:value 中value必须是1个单词！");
			}
		}
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
		//		String t = TABLE_KEY_MAP.containsKey(table) ? TABLE_KEY_MAP.get(table) : table;
		//如果要强制小写，则可在子类重写这个方法再 toLowerCase		return DATABASE_POSTGRESQL.equals(getDatabase()) ? t.toLowerCase() : t;
		return TABLE_KEY_MAP.containsKey(table) ? TABLE_KEY_MAP.get(table) : table;
	}
	@JSONField(serialize = false)
	@Override
	public String getTablePath() {
		String q = getQuote();

		String sch = getSQLSchema();
		String sqlTable = getSQLTable();

		return (StringUtil.isEmpty(sch, true) ? "" : q + sch + q + ".") + q + sqlTable + q + ( isKeyPrefix() ? " AS " + getAliasWithQuote() : "");
	}
	@Override
	public AbstractSQLConfig setTable(String table) { //Table已经在Parser中校验，所以这里不用防SQL注入
		this.table = table;
		return this;
	}

	@Override
	public String getAlias() {
		return alias;
	}
	@Override
	public AbstractSQLConfig setAlias(String alias) {
		this.alias = alias;
		return this;
	}
	public String getAliasWithQuote() {
		String a = getAlias();
		if (StringUtil.isEmpty(a, true)) {
			a = getTable();
		}
		String q = getQuote();
		//getTable 不能小写，因为Verifier用大小写敏感的名称判断权限		
		//如果要强制小写，则可在子类重写这个方法再 toLowerCase  return q + (DATABASE_POSTGRESQL.equals(getDatabase()) ? a.toLowerCase() : a) + q;
		return q + a + q;
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
	public String getGroupString(boolean hasPrefix) {
		//加上子表的 group
		String joinGroup = "";
		if (joinList != null) {
			SQLConfig cfg;
			String c;
			boolean first = true;
			for (Join j : joinList) {
				if (j.isAppJoin()) {
					continue;
				}

				cfg = j.isLeftOrRightJoin() ? j.getOutterConfig() : j.getJoinConfig();
				if (StringUtil.isEmpty(cfg.getAlias(), true)) {
					cfg.setAlias(cfg.getTable());
				}

				c = ((AbstractSQLConfig) cfg).getGroupString(false);
				if (StringUtil.isEmpty(c, true) == false) {
					joinGroup += (first ? "" : ", ") + c;
					first = false;
				}

			}
		}


		group = StringUtil.getTrimedString(group);
		String[] keys = StringUtil.split(group);
		if (keys == null || keys.length <= 0) {
			return StringUtil.isEmpty(joinGroup, true) ? "" : (hasPrefix ? " GROUP BY " : "") + joinGroup;
		}

		for (int i = 0; i < keys.length; i++) {
			if (isPrepared()) { //不能通过 ? 来代替，因为SQLExecutor statement.setString后 GROUP BY 'userId' 有单引号，只能返回一条数据，必须去掉单引号才行！
				if (StringUtil.isName(keys[i]) == false) {
					throw new IllegalArgumentException("@group:value 中 value里面用 , 分割的每一项都必须是1个单词！并且不要有空格！");
				}
			}

			keys[i] = getKey(keys[i]);
		}

		return (hasPrefix ? " GROUP BY " : "") + StringUtil.concat(StringUtil.getString(keys), joinGroup, ", ");
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
	/**
	 * @return HAVING conditoin0 AND condition1 OR condition2 ...
	 */
	@JSONField(serialize = false)
	public String getHavingString(boolean hasPrefix) {
		//加上子表的 having
		String joinHaving = "";
		if (joinList != null) {
			SQLConfig cfg;
			String c;
			boolean first = true;
			for (Join j : joinList) {
				if (j.isAppJoin()) {
					continue;
				}

				cfg = j.isLeftOrRightJoin() ? j.getOutterConfig() : j.getJoinConfig();
				if (StringUtil.isEmpty(cfg.getAlias(), true)) {
					cfg.setAlias(cfg.getTable());
				}

				c = ((AbstractSQLConfig) cfg).getHavingString(false);
				if (StringUtil.isEmpty(c, true) == false) {
					joinHaving += (first ? "" : ", ") + c;
					first = false;
				}

			}
		}

		having = StringUtil.getTrimedString(having);
		String[] keys = StringUtil.split(having, ";");
		if (keys == null || keys.length <= 0) {
			return StringUtil.isEmpty(joinHaving, true) ? "" : (hasPrefix ? " HAVING " : "") + joinHaving;
		}

		String expression;
		String method;
		//暂时不允许 String prefix;
		String suffix;

		//fun0(arg0,arg1,...);fun1(arg0,arg1,...)
		for (int i = 0; i < keys.length; i++) {

			//fun(arg0,arg1,...)
			expression = keys[i];

			int start = expression.indexOf("(");
			if (start < 0) {
				if (isPrepared() && PATTERN_HAVING.matcher(expression).matches() == false) {
					throw new UnsupportedOperationException("字符串 " + expression + " 不合法！"
							+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
							+ " 中 column?value 必须符合正则表达式 ^[A-Za-z0-9%!=<>]+$ ！不允许空格！");
				}
				continue;
			}

			int end = expression.indexOf(")");
			if (start >= end) {
				throw new IllegalArgumentException("字符 " + expression + " 不合法！"
						+ "@having:value 中 value 里的 SQL函数必须为 function(arg0,arg1,...) 这种格式！");
			}

			method = expression.substring(0, start);

			if (StringUtil.isName(method) == false) {
				throw new IllegalArgumentException("字符 " + method + " 不合法！"
						+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
						+ " 中SQL函数名 function 必须符合正则表达式 ^[0-9a-zA-Z_]+$ ！");
			}

			suffix = expression.substring(end + 1, expression.length());

			if (isPrepared() && PATTERN_HAVING_SUFFIX.matcher((String) suffix).matches() == false) {
				throw new UnsupportedOperationException("字符串 " + suffix + " 不合法！"
						+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
						+ " 中 ?value 必须符合正则表达式 ^[0-9%!=<>]+$ ！不允许空格！");
			}

			String[] ckeys = StringUtil.split(expression.substring(start + 1, end));

			if (ckeys != null) {
				for (int j = 0; j < ckeys.length; j++) {

					if (isPrepared() && (StringUtil.isName(ckeys[j]) == false || ckeys[j].startsWith("_"))) {
						throw new IllegalArgumentException("字符 " + ckeys[j] + " 不合法！"
								+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
								+ " 中所有 arg 都必须是1个不以 _ 开头的单词！并且不要有空格！");
					}

					ckeys[j] = getKey(ckeys[j]);
				}
			}

			keys[i] = method + "(" + StringUtil.getString(ckeys) + ")" + suffix;
		}

		//TODO 支持 OR, NOT 参考 @combine:"&key0,|key1,!key2"
		return (hasPrefix ? " HAVING " : "") + StringUtil.concat(StringUtil.getString(keys, AND), joinHaving, AND);
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
	public String getOrderString(boolean hasPrefix) {
		//加上子表的 order
		String joinOrder = "";
		if (joinList != null) {
			SQLConfig cfg;
			String c;
			boolean first = true;
			for (Join j : joinList) {
				if (j.isAppJoin()) {
					continue;
				}

				cfg = j.isLeftOrRightJoin() ? j.getOutterConfig() : j.getJoinConfig();
				if (StringUtil.isEmpty(cfg.getAlias(), true)) {
					cfg.setAlias(cfg.getTable());
				}

				c = ((AbstractSQLConfig) cfg).getOrderString(false);
				if (StringUtil.isEmpty(c, true) == false) {
					joinOrder += (first ? "" : ", ") + c;
					first = false;
				}

			}
		}


		String order = StringUtil.getTrimedString(getOrder());

		if (isOracle() || isSQLServer()) { // Oracle 和 SQL Server 的 OFFSET 必须加 ORDER BY

			//			String[] ss = StringUtil.split(order);
			if (StringUtil.isEmpty(order, true)) {
				String idKey = getIdKey();
				if (StringUtil.isEmpty(idKey, true)) {
					idKey = "id"; //ORDER BY NULL 不行，SQL Server 会报错，必须要有排序，才能使用 OFFSET FETCH，如果没有 idKey，请求中指定 @order 即可
				}
				order = idKey; //让数据库调控默认升序还是降序  + "+";
			}

			//不用这么全面，毕竟没有语法问题还浪费性能，如果有其它问题，让前端传的 JSON 直接加上 @order 来解决
			//			boolean contains = false;
			//			if (ss != null) {
			//				for (String s : ss) {
			//					if (s != null && s.startsWith(idKey)) {
			//						s = s.substring(idKey.length());
			//						if ("+".equals(s) || "-".equals(s)) {// || " ASC ".equals(s) || " DESC ".equals(s)) {
			//							contains = true;
			//							break;
			//						}
			//					}
			//				}
			//			}

			//			if (contains == false) {
			//				order = (ss == null || ss.length <= 0 ? "" : order + ",") + idKey + "+";
			//			}
		}


		if (order.contains("+")) {//replace没有包含的replacement会崩溃
			order = order.replaceAll("\\+", " ASC ");
		}
		if (order.contains("-")) {
			order = order.replaceAll("-", " DESC ");
		}

		String[] keys = StringUtil.split(order);
		if (keys == null || keys.length <= 0) {
			return StringUtil.isEmpty(joinOrder, true) ? "" : (hasPrefix ? " ORDER BY " : "") + joinOrder;
		}

		String origin;
		String sort;
		int index;
		for (int i = 0; i < keys.length; i++) {
			index = keys[i].trim().endsWith(" ASC") ? keys[i].lastIndexOf(" ASC") : -1; //StringUtil.split返回数组中，子项不会有null
			if (index < 0) {
				index = keys[i].trim().endsWith(" DESC") ? keys[i].lastIndexOf(" DESC") : -1;
				sort = index <= 0 ? "" : " DESC ";
			} else {
				sort = " ASC ";
			}
			origin = index < 0 ? keys[i] : keys[i].substring(0, index);

			if (isPrepared()) { //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
				//这里既不对origin trim，也不对 ASC/DESC ignoreCase，希望前端严格传没有任何空格的字符串过来，减少传输数据量，节约服务器性能
				if (StringUtil.isName(origin) == false) {
					throw new IllegalArgumentException("预编译模式下 @order:value 中 value里面用 , 分割的每一项"
							+ " column+ / column- 中 column必须是1个单词！并且不要有多余的空格！");
				}
			}
			keys[i] = getKey(origin) + sort;
		}

		return (hasPrefix ? " ORDER BY " : "") + StringUtil.concat(StringUtil.getString(keys), joinOrder, ", ");
	}


	@Override
	public Subquery getFrom() {
		return from;
	}
	@Override
	public AbstractSQLConfig setFrom(Subquery from) {
		this.from = from;
		return this;
	}

	@Override
	public List<String> getColumn() {
		return column;
	}
	@Override
	public AbstractSQLConfig setColumn(List<String> column) {
		this.column = column;
		return this;
	}
	@JSONField(serialize = false)
	public String getColumnString() throws Exception {
		return getColumnString(false);
	}
	@JSONField(serialize = false)
	public String getColumnString(boolean inSQLJoin) throws Exception {
		switch (getMethod()) {
		case HEAD:
		case HEADS: //StringUtil.isEmpty(column, true) || column.contains(",") 时SQL.count(column)会return "*"
			if (isPrepared() && column != null) {
				String origin;
				String alias;
				int index;
				for (String c : column) {
					index = c.lastIndexOf(":"); //StringUtil.split返回数组中，子项不会有null
					origin = index < 0 ? c : c.substring(0, index);
					alias = index < 0 ? null : c.substring(index + 1);
					if (StringUtil.isName(origin) == false || (alias != null && StringUtil.isName(alias) == false)) {
						throw new IllegalArgumentException("HEAD请求: 预编译模式下 @column:value 中 value里面用 , 分割的每一项"
								+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！并且不要有多余的空格！");
					}
				}
			}
			return SQL.count(column != null && column.size() == 1 ? getKey(Pair.parseEntry(column.get(0), true).getKey()) : "*");
		case POST:
			if (column == null || column.isEmpty()) {
				throw new IllegalArgumentException("POST 请求必须在Table内设置要保存的 key:value ！");
			}

			String s = "";
			boolean pfirst = true;
			for (String c : column) {
				if (isPrepared() && StringUtil.isName(c) == false) {  //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
					throw new IllegalArgumentException("POST请求: 每一个 key:value 中的key都必须是1个单词！");
				}
				s += ((pfirst ? "" : ",") + getKey(c));

				pfirst = false;
			}

			return "(" + s + ")";
		case GET:
		case GETS:
			boolean isQuery = RequestMethod.isQueryMethod(method); //TODO 这个有啥用？上面应是 getMethod 的值 GET 和 GETS 了。
			String joinColumn = "";
			if (isQuery && joinList != null) {
				SQLConfig ecfg;
				SQLConfig cfg;
				String c;
				boolean first = true;
				for (Join j : joinList) {
					if (j.isAppJoin()) {
						continue;
					}

					ecfg = j.getOutterConfig();
					if (ecfg != null && ecfg.getColumn() != null) { //优先级更高
						cfg = ecfg;
					}
					else {
						cfg = j.getJoinConfig();
					}

					if (StringUtil.isEmpty(cfg.getAlias(), true)) {
						cfg.setAlias(cfg.getTable());
					}

					c = ((AbstractSQLConfig) cfg).getColumnString(true);
					if (StringUtil.isEmpty(c, true) == false) {
						joinColumn += (first ? "" : ", ") + c;
						first = false;
					}

					inSQLJoin = true;
				}
			}

			String tableAlias = getAliasWithQuote();

			//			String c = StringUtil.getString(column); //id,name;json_length(contactIdList):contactCount;...

			String[] keys = column == null ? null : column.toArray(new String[]{}); //StringUtil.split(c, ";");
			if (keys == null || keys.length <= 0) {

				boolean noColumn = column != null && inSQLJoin;
				String mc = isKeyPrefix() == false ? (noColumn ? "" : "*") : (noColumn ? "" : tableAlias + ".*");

				return StringUtil.concat(mc, joinColumn, ", ", true);
			}


			String expression;
			String method = null;

			//...;fun0(arg0,arg1,...):fun0;fun1(arg0,arg1,...):fun1;...
			for (int i = 0; i < keys.length; i++) {

				//fun(arg0,arg1,...)
				expression = keys[i];

				int start = expression.indexOf("(");
				int end = 0;
				if (start >= 0) {
					end = expression.indexOf(")");
					if (start >= end) {
						throw new IllegalArgumentException("字符 " + expression + " 不合法！"
								+ "@having:value 中 value 里的 SQL函数必须为 function(arg0,arg1,...) 这种格式！");
					}

					method = expression.substring(0, start);
					boolean distinct = i <= 0 && method.startsWith(PREFFIX_DISTINCT);
					if (StringUtil.isName(distinct ? method.substring(PREFFIX_DISTINCT.length()) : method) == false) {
						throw new IllegalArgumentException("字符 " + method + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中SQL函数名 function 必须符合正则表达式 ^[0-9a-zA-Z_]+$ ！");
					}
				}

				boolean isColumn = start < 0;

				String[] ckeys = StringUtil.split(isColumn ? expression : expression.substring(start + 1, end));
				String quote = getQuote();

				//			if (isPrepared()) { //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
				if (ckeys != null && ckeys.length > 0) {

					boolean distinct;
					String origin;
					String alias;
					int index;
					for (int j = 0; j < ckeys.length; j++) {
						index = isColumn ? ckeys[j].lastIndexOf(":") : -1; //StringUtil.split返回数组中，子项不会有null
						origin = index < 0 ? ckeys[j] : ckeys[j].substring(0, index);
						alias = index < 0 ? null : ckeys[j].substring(index + 1);

						distinct = j <= 0 && origin.startsWith(PREFFIX_DISTINCT);
						if (distinct) {
							origin = origin.substring(PREFFIX_DISTINCT.length());
						}

						if (isPrepared()) {
							if (isColumn) {
								if (StringUtil.isName(origin) == false || (alias != null && StringUtil.isName(alias) == false)) {
									throw new IllegalArgumentException("字符 " + ckeys[j] + " 不合法！"
											+ "预编译模式下 @column:value 中 value里面用 , 分割的每一项"
											+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！"
											+ "DISTINCT 必须全大写，且后面必须有且只有 1 个空格！其它情况不允许空格！");
								}
							}
							else {
								if ((StringUtil.isName(origin) == false || origin.startsWith("_"))) {
									throw new IllegalArgumentException("字符 " + ckeys[j] + " 不合法！"
											+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
											+ " 中所有 arg 都必须是1个不以 _ 开头的单词！DISTINCT 必须全大写，且后面必须有且只有 1 个空格！其它情况不允许空格！");
								}
							}
						}

						//JOIN 副表不再在外层加副表名前缀 userId AS `Commet.userId`， 而是直接 userId AS `userId`
						origin = quote + origin + quote;
						if (isKeyPrefix()) {
							ckeys[j] = tableAlias + "." + origin;
							//							if (isColumn) {
							//								ckeys[j] += " AS " + quote + (isMain() ? "" : tableAlias + ".") + (StringUtil.isEmpty(alias, true) ? origin : alias) + quote;
							//							}
							if (isColumn && StringUtil.isEmpty(alias, true) == false) {
								ckeys[j] += " AS " + quote + alias + quote;
							}
						} else {
							ckeys[j] = origin + (StringUtil.isEmpty(alias, true) ? "" : " AS " + quote + alias + quote);
						}

						if (distinct) {
							ckeys[j] = PREFFIX_DISTINCT + ckeys[j];
						}
					}
					//				}

				}

				if (isColumn) {
					keys[i] = StringUtil.getString(ckeys);
				}
				else {
					String suffix = expression.substring(end + 1, expression.length()); //:contactCount
					String alias = suffix.startsWith(":") ? suffix.substring(1) : null; //contactCount

					if (StringUtil.isEmpty(alias, true)) {
						if (suffix.isEmpty() == false) {
							throw new IllegalArgumentException("GET请求: 预编译模式下 @column:value 中 value里面用 ; 分割的每一项"
									+ " function(arg0,arg1,...):alias 中 alias 如果有就必须是1个单词！并且不要有多余的空格！");
						}
					} 
					else {
						if (StringUtil.isEmpty(alias, true) == false && StringUtil.isName(alias) == false) {
							throw new IllegalArgumentException("GET请求: 预编译模式下 @column:value 中 value里面用 ; 分割的每一项"
									+ " function(arg0,arg1,...):alias 中 alias 必须是1个单词！并且不要有多余的空格！");
						}
					}


					String origin = method + "(" + StringUtil.getString(ckeys) + ")";
					//					if (isKeyPrefix()) {
					//						keys[i] = origin + " AS " + quote + (isMain() ? "" : tableAlias + ".") + (StringUtil.isEmpty(alias, true) ? method : alias) + quote;
					//					}
					//					else {
					keys[i] = origin + (StringUtil.isEmpty(alias, true) ? "" : " AS " + quote + alias + quote);
					//					}
				}

			}

			String c = StringUtil.getString(keys);
			c = (c.contains(":") == false ? c : c.replaceAll(":", " AS ")) + (StringUtil.isEmpty(joinColumn, true) ? "" : ", " + joinColumn);//不能在这里改，后续还要用到:
			return isMain() && isDistinct() ? PREFFIX_DISTINCT + c : c;
		default:
			throw new UnsupportedOperationException(
					"服务器内部错误：getColumnString 不支持 " + RequestMethod.getName(getMethod())
					+ " 等 [GET,GETS,HEAD,HEADS,POST] 外的ReuqestMethod！"
					);
		}
	}


	@Override
	public List<List<Object>> getValues() {
		return values;
	}
	@JSONField(serialize = false)
	public String getValuesString() {
		String s = "";
		if (values != null && values.size() > 0) {
			Object[] items = new Object[values.size()];
			List<Object> vs;
			for (int i = 0; i < values.size(); i++) {
				vs = values.get(i);
				if (vs == null) {
					continue;
				}

				items[i] = "(";
				for (int j = 0; j < vs.size(); j++) {
					items[i] += ((j <= 0 ? "" : ",") + getValue(vs.get(j)));
				}
				items[i] += ")";
			}
			s = StringUtil.getString(items);
		}
		return s;
	}
	@Override
	public AbstractSQLConfig setValues(List<List<Object>> valuess) {
		this.values = valuess;
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
	public int getCache() {
		return cache;
	}
	@Override
	public AbstractSQLConfig setCache(int cache) {
		this.cache = cache;
		return this;
	}

	public AbstractSQLConfig setCache(String cache) {
		int cache2;
		if (cache == null) {
			cache2 = JSONRequest.CACHE_ALL;
		}
		else {
			//			if (isSubquery) {
			//				throw new IllegalArgumentException("子查询内不支持传 " + JSONRequest.KEY_CACHE + "!");
			//			}

			switch (cache) {
			case "0":
			case JSONRequest.CACHE_ALL_STRING:
				cache2 = JSONRequest.CACHE_ALL;
				break;
			case "1":
			case JSONRequest.CACHE_ROM_STRING:
				cache2 = JSONRequest.CACHE_ROM;
				break;
			case "2":
			case JSONRequest.CACHE_RAM_STRING:
				cache2 = JSONRequest.CACHE_RAM;
				break;
			default:
				throw new IllegalArgumentException(getTable() + "/" + JSONRequest.KEY_CACHE + ":value 中 value 的值不合法！必须在 [0,1,2] 或 [ALL, ROM, RAM] 内 !");
			}
		}
		setCache(cache2);
		return this;
	}

	@Override
	public boolean isExplain() {
		return explain;
	}
	@Override
	public AbstractSQLConfig setExplain(boolean explain) {
		this.explain = explain;
		return this;
	}

	@Override
	public List<Join> getJoinList() {
		return joinList;
	}
	@Override
	public SQLConfig setJoinList(List<Join> joinList) {
		this.joinList = joinList;
		return this;
	}
	@Override
	public boolean hasJoin() {
		return joinList != null && joinList.isEmpty() == false;
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
		if (count <= 0 || RequestMethod.isHeadMethod(getMethod(), true)) {
			return "";
		}
		return getLimitString(getPage(), getCount(), isOracle() || isSQLServer());
	}
	/**获取限制数量
	 * @param limit
	 * @return
	 */
	public static String getLimitString(int page, int count, boolean isTSQL) {
		int offset = getOffset(page, count);

		if (isTSQL) {
			return " OFFSET " + offset + " ROWS FETCH FIRST " + count + " ROWS ONLY";
		}

		return " LIMIT " + count + " OFFSET " + offset;
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
	@NotNull
	@Override
	public Map<String, List<String>> getCombine() {
		List<String> andList = combine == null ? null : combine.get("&");
		if (andList == null) {
			andList = where == null ? new ArrayList<String>() : new ArrayList<String>(where.keySet());
			if (combine == null) {
				combine = new HashMap<>();
			}
			combine.put("&", andList);
		}
		return combine;
	}
	@Override
	public AbstractSQLConfig setCombine(Map<String, List<String>> combine) {
		this.combine = combine;
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
	public AbstractSQLConfig putWhere(String key, Object value, boolean prior) {
		if (key != null) {
			if (where == null) {
				where = new LinkedHashMap<String, Object>();	
			}
			if (value == null) {
				where.remove(key);
			} else {
				where.put(key, value);
			}

			combine = getCombine();
			List<String> andList = combine.get("&");
			if (value == null) {
				andList.remove(key);
			}
			else if (andList == null || andList.contains(key) == false) {
				int i = 0;
				if (andList == null) {
					andList = new ArrayList<>();
				}
				else if (prior && andList.isEmpty() == false) {

					String idKey = getIdKey();
					String idInKey = idKey + "{}";
					String userIdKey = getUserIdKey();
					String userIdInKey = userIdKey + "{}";

					if (andList.contains(idKey)) {
						i ++;
					}
					if (andList.contains(idInKey)) {
						i ++;
					}
					if (andList.contains(userIdKey)) {
						i ++;
					}
					if (andList.contains(userIdInKey)) {
						i ++;
					}
				}

				if (prior) {
					andList.add(i, key); //userId的优先级不能比id高  0, key);
				} else {
					andList.add(key); //AbstractSQLExecutor.onPutColumn里getSQL，要保证缓存的SQL和查询的SQL里 where 的 key:value 顺序一致
				}
			}
			combine.put("&", andList);
		}
		return this;
	}

	/**获取WHERE
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	@Override
	public String getWhereString(boolean hasPrefix) throws Exception {
		return getWhereString(hasPrefix, getMethod(), getWhere(), getCombine(), getJoinList(), ! isTest());
	}
	/**获取WHERE
	 * @param method
	 * @param where
	 * @return
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	public String getWhereString(boolean hasPrefix, RequestMethod method, Map<String, Object> where, Map<String, List<String>> combine, List<Join> joinList, boolean verifyName) throws Exception {
		Set<Entry<String, List<String>>> combineSet = combine == null ? null : combine.entrySet();
		if (combineSet == null || combineSet.isEmpty()) {
			Log.w(TAG, "getWhereString  combineSet == null || combineSet.isEmpty() >> return \"\";");
			return "";
		}

		List<String> keyList;

		String whereString = "";

		boolean isCombineFirst = true;
		int logic;

		boolean isItemFirst;
		String c;
		String cs;

		for (Entry<String, List<String>> ce : combineSet) {
			keyList = ce == null ? null : ce.getValue();
			if (keyList == null || keyList.isEmpty()) {
				continue;
			}

			if ("|".equals(ce.getKey())) {
				logic = Logic.TYPE_OR;
			}
			else if ("!".equals(ce.getKey())) {
				logic = Logic.TYPE_NOT;
			}
			else {
				logic = Logic.TYPE_AND;
			}


			isItemFirst = true;
			cs = "";
			for (String key : keyList) {
				c = getWhereItem(key, where.get(key), method, verifyName);

				if (StringUtil.isEmpty(c, true)) {//避免SQL条件连接错误
					continue;
				}

				cs += (isItemFirst ? "" : (Logic.isAnd(logic) ? AND : OR)) + "(" + c + ")";

				isItemFirst = false;
			}


			whereString += (isCombineFirst ? "" : AND) + (Logic.isNot(logic) ? NOT : "") + " (  " + cs + "  ) ";
			isCombineFirst = false;
		}


		if (joinList != null) {

			String newWs = "";
			String ws = "" + whereString;

			List<Object> newPvl = new ArrayList<>();
			List<Object> pvl = new ArrayList<>(preparedValueList);

			SQLConfig jc;
			String js;

			boolean changed = false;
			//各种 JOIN 没办法统一用 & | ！连接，只能按优先级，和 @combine 一样?
			for (Join j : joinList) {
				switch (j.getJoinType()) {
				case "@": // APP JOIN
				case "<": // LEFT JOIN
				case ">": // RIGHT JOIN
					break;

				case "":  // FULL JOIN 
				case "|": // FULL JOIN  不支持 <>, [] ，避免太多符号
				case "&": // INNER JOIN 
				case "!": // OUTTER JOIN
				case "^": // SIDE JOIN
				case "*": // CROSS JOIN
					jc = j.getJoinConfig();
					boolean isMain = jc.isMain();
					jc.setMain(false).setPrepared(isPrepared()).setPreparedValueList(new ArrayList<Object>());
					js = jc.getWhereString(false);
					jc.setMain(isMain);

					if (StringUtil.isEmpty(js, true)) {
						continue;
					}

					if (StringUtil.isEmpty(newWs, true) == false) {
						newWs += AND;
					}

					if ("^".equals(j.getJoinType())) { // (A & ! B) | (B & ! A)
						newWs += " (   ( " + ws + ( StringUtil.isEmpty(ws, true) ? "" : AND + NOT ) + " ( " + js + " ) ) "
								+ OR
								+ " ( " + js + AND + NOT + " ( " + ws + " )  )   ) ";

						newPvl.addAll(pvl);
						newPvl.addAll(jc.getPreparedValueList());
						newPvl.addAll(jc.getPreparedValueList());
						newPvl.addAll(pvl);
					}
					else {
						logic = Logic.getType(j.getJoinType());

						newWs += " ( "
								+ getCondition(
										Logic.isNot(logic), 
										ws
										+ ( StringUtil.isEmpty(ws, true) ? "" : (Logic.isAnd(logic) ? AND : OR) )
										+ " ( " + js + " ) "
										)
								+ " ) ";

						newPvl.addAll(pvl);
						newPvl.addAll(jc.getPreparedValueList());
					}

					changed = true;
					break;
				default:
					throw new UnsupportedOperationException("join:value 中 value 里的 " + j.getJoinType() + "/" + j.getPath() + "错误！不支持 " + j.getJoinType() + " 等 [@ APP, < LEFT, > RIGHT, | FULL, & INNER, ! OUTTER, ^ SIDE, * CROSS] 之外的JOIN类型 !");
				}
			}

			if (changed) {
				whereString = newWs;
				preparedValueList = newPvl;
			}
		}

		String s = whereString.isEmpty() ? "" : (hasPrefix ? " WHERE " : "") + whereString;

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
		else if (key.endsWith("~") || key.endsWith("?")) { //TODO ？可能以后会被废弃，全用 ~ 和 *~ 替代，更接近 PostgreSQL 语法 
			keyType = key.charAt(key.length() - 2) == '*' ? -2 : 2;  //FIXME StringIndexOutOfBoundsException
		}
		else if (key.endsWith("%")) {
			keyType = 3;
		}
		else if (key.endsWith("{}")) {
			keyType = 4;
		}
		else if (key.endsWith("}{")) {
			keyType = 5;
		}
		else if (key.endsWith("<>")) {
			keyType = 6;
		}
		else if (key.endsWith(">=")) {
			keyType = 7;
		}
		else if (key.endsWith("<=")) {
			keyType = 8;
		}
		else if (key.endsWith(">")) {
			keyType = 9;
		}
		else if (key.endsWith("<")) {
			keyType = 10;
		}
		else { //else绝对不能省，避免再次踩坑！ keyType = 0; 写在for循环外面都没注意！
			keyType = 0;
		}
		key = getRealKey(method, key, false, true, verifyName, getQuote());

		switch (keyType) {
		case 1:
			return getSearchString(key, value);
		case -2:
		case 2:
			return getRegExpString(key, value, keyType < 0);
		case 3:
			return getBetweenString(key, value);
		case 4:
			return getRangeString(key, value);
		case 5:
			return getExistsString(key, value);
		case 6:
			return getContainString(key, value);
		case 7:
			return getCompareString(key, value, ">=");
		case 8:
			return getCompareString(key, value, "<=");
		case 9:
			return getCompareString(key, value, ">");
		case 10:
			return getCompareString(key, value, "<");
		default: //TODO MySQL JSON类型的字段对比 key='[]' 会无结果！ key LIKE '[1, 2, 3]'  //TODO MySQL , 后面有空格！
			return getEqualString(key, value);
		}
	}


	@JSONField(serialize = false)
	public String getEqualString(String key, Object value) throws Exception {
		if (JSON.isBooleanOrNumberOrString(value) == false && value instanceof Subquery == false) {
			throw new IllegalArgumentException(key + ":value 中value不合法！非PUT请求只支持 [Boolean, Number, String] 内的类型 ！");
		}

		boolean not = key.endsWith("!"); // & | 没有任何意义，写法多了不好控制 
		if (not) {
			key = key.substring(0, key.length() - 1);
		}
		if (StringUtil.isName(key) == false) {
			throw new IllegalArgumentException(key + ":value 中key不合法！不支持 ! 以外的逻辑符 ！");
		}

		return getKey(key) + (not ? " != " : " = ") + (value instanceof Subquery ? getSubqueryString((Subquery) value) : getValue(value));
	}

	@JSONField(serialize = false)
	public String getCompareString(String key, Object value, String type) throws Exception {
		if (JSON.isBooleanOrNumberOrString(value) == false && value instanceof Subquery == false) {
			throw new IllegalArgumentException(key + type + ":value 中value不合法！比较运算 [>, <, >=, <=] 只支持 [Boolean, Number, String] 内的类型 ！");
		}
		if (StringUtil.isName(key) == false) {
			throw new IllegalArgumentException(key + type + ":value 中key不合法！比较运算 [>, <, >=, <=] 不支持 [&, !, |] 中任何逻辑运算符 ！");
		}

		return getKey(key) + " " + type + " " + (value instanceof Subquery ? getSubqueryString((Subquery) value) : getValue(value));
	}

	public String getKey(String key) {
		String q = getQuote();
		return (isKeyPrefix() ? getAliasWithQuote() + "." : "") + q  + key + q;
	}

	/**
	 * 使用prepareStatement预编译，值为 ? ，后续动态set进去
	 */
	private List<Object> preparedValueList = new ArrayList<>();
	private Object getValue(@NotNull Object value) {
		if (isPrepared()) {
			preparedValueList.add(value);
			return "?";
		}
		//		return (value instanceof Number || value instanceof Boolean) && DATABASE_POSTGRESQL.equals(getDatabase()) ? value :  "'" + value + "'";
		return (value instanceof Number || value instanceof Boolean) ? value :  "'" + value + "'"; //MySQL 隐式转换用不了索引
	}

	@Override
	public List<Object> getPreparedValueList() {
		return preparedValueList;
	}
	@Override
	public AbstractSQLConfig setPreparedValueList(List<Object> preparedValueList) {
		this.preparedValueList = preparedValueList;
		return this;
	}

	//$ search <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**search key match value
	 * @param in
	 * @return {@link #getSearchString(String, Object[], int)}
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
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
	@JSONField(serialize = false)
	public String getSearchString(String key, Object[] values, int type) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "$:value 中value的类型只能为String或String[]！");
			}
			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getLikeString(key, values[i]);
		}

		return getCondition(Logic.isNot(type), condition);
	}

	/**WHERE key LIKE 'value'
	 * @param key
	 * @param value
	 * @return key LIKE 'value'
	 */
	@JSONField(serialize = false)
	public String getLikeString(String key, Object value) {
		return getKey(key) + " LIKE "  + getValue(value);
	}

	//$ search >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//~ regexp <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**search key match RegExp values
	 * @param key
	 * @param value
	 * @param ignoreCase 
	 * @return {@link #getRegExpString(String, Object[], int, boolean)}
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
	public String getRegExpString(String key, Object value, boolean ignoreCase) throws IllegalArgumentException {
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
		return getRegExpString(key, arr.toArray(), logic.getType(), ignoreCase);
	}
	/**search key match RegExp values
	 * @param key
	 * @param values
	 * @param type 
	 * @param ignoreCase 
	 * @return LOGIC [  key REGEXP 'values[i]' ]
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
	public String getRegExpString(String key, Object[] values, int type, boolean ignoreCase) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "$:value 中value的类型只能为String或String[]！");
			}
			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getRegExpString(key, (String) values[i], ignoreCase);
		}

		return getCondition(Logic.isNot(type), condition);
	}

	/**WHERE key REGEXP 'value'
	 * @param key
	 * @param value
	 * @param ignoreCase
	 * @return key REGEXP 'value'
	 */
	@JSONField(serialize = false)
	public String getRegExpString(String key, String value, boolean ignoreCase) {
		if (isPostgreSQL()) {
			return getKey(key) + " ~" + (ignoreCase ? "* " : " ") + getValue(value);
		}
		return getKey(key) + " REGEXP " + (ignoreCase ? "" : "BINARY ") + getValue(value);
	}
	//~ regexp >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//% between <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**WHERE key BETWEEN 'start' AND 'end'
	 * @param key
	 * @param value 'start,end'
	 * @return LOGIC [ key BETWEEN 'start' AND 'end' ]
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
	public String getBetweenString(String key, Object value) throws IllegalArgumentException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getBetweenString key = " + key);

		JSONArray arr = newJSONArray(value);
		if (arr.isEmpty()) {
			return "";
		}
		return getBetweenString(key, arr.toArray(), logic.getType());
	}

	/**WHERE key BETWEEN 'start' AND 'end'
	 * @param key
	 * @param value 'start,end' TODO 在 '1,2' 和 ['1,2', '3,4'] 基础上新增支持 [1, 2] 和 [[1,2], [3,4]] ？
	 * @return LOGIC [ key BETWEEN 'start' AND 'end' ]
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
	public String getBetweenString(String key, Object[] values, int type) throws IllegalArgumentException {
		if (values == null || values.length <= 0) {
			return "";
		}

		String condition = "";
		String[] vs;
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof String == false) {
				throw new IllegalArgumentException(key + "%:value 中 value 的类型只能为 String 或 String[] ！");
			}

			vs = StringUtil.split((String) values[i]);
			if (vs == null || vs.length != 2) {
				throw new IllegalArgumentException(key + "%:value 中 value 不合法！类型为 String 时必须包括1个逗号 , 且左右两侧都有值！类型为 String[] 里面每个元素要符合前面类型为 String 的规则 ！");
			}

			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + "(" + getBetweenString(key, vs[0], vs[1]) + ")";
		}

		return getCondition(Logic.isNot(type), condition);
	}

	/**WHERE key BETWEEN 'start' AND 'end'
	 * @param key
	 * @param value 'start,end' TODO 在 '1,2' 和 ['1,2', '3,4'] 基础上新增支持 [1, 2] 和 [[1,2], [3,4]] ？
	 * @return key BETWEEN 'start' AND 'end'
	 * @throws IllegalArgumentException 
	 */
	@JSONField(serialize = false)
	public String getBetweenString(String key, Object start, Object end) throws IllegalArgumentException {
		if (JSON.isBooleanOrNumberOrString(start) == false || JSON.isBooleanOrNumberOrString(end) == false) {
			throw new IllegalArgumentException(key + "%:value 中 value 不合法！类型为 String 时必须包括1个逗号 , 且左右两侧都有值！类型为 String[] 里面每个元素要符合前面类型为 String 的规则 ！");
		}
		return getKey(key) + " BETWEEN " + getValue(start) + AND + getValue(end);
	}


	//% between >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//{} range <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// * 和 / 不能同时出现，防止 /* */ 段注释！ # 和 -- 不能出现，防止行注释！ ; 不能出现，防止隔断SQL语句！空格不能出现，防止 CRUD,DROP,SHOW TABLES等语句！
	private static final Pattern PATTERN_RANGE;
	private static final Pattern PATTERN_HAVING;
	private static final Pattern PATTERN_HAVING_SUFFIX;
	static {
		PATTERN_RANGE = Pattern.compile("^[0-9%!=<>,]+$"); // ^[a-zA-Z0-9_*%!=<>(),"]+$ 导致 exists(select*from(Comment)) 通过！
		PATTERN_HAVING = Pattern.compile("^[A-Za-z0-9%!=<>]+$"); //TODO 改成更好的正则，校验前面为单词，中间为操作符，后面为值
		PATTERN_HAVING_SUFFIX = Pattern.compile("^[0-9%!=<>]+$"); // ^[a-zA-Z0-9_*%!=<>(),"]+$ 导致 exists(select*from(Comment)) 通过！
	}


	/**WHERE key > 'key0' AND key <= 'key1' AND ...
	 * @param key
	 * @param range "condition0,condition1..."
	 * @return key condition0 AND key condition1 AND ...
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
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
				return getKey(key) + getInString(key, ((List<?>) range).toArray(), logic.isNot());
			}
			throw new IllegalArgumentException(key + "{}\":[] 中key末尾的逻辑运算符只能用'|','!'中的一种 ！");
		}
		else if (range instanceof String) {//非Number类型需要客户端拼接成 < 'value0', >= 'value1'这种
			if (isPrepared() && PATTERN_RANGE.matcher((String) range).matches() == false) {
				throw new UnsupportedOperationException("字符串 " + range + " 不合法！预编译模式下 key{}:\"condition\" 中 condition 必须符合正则表达式 ^[0-9%!=<>,]+$ ！不允许空格！");
			}

			String[] conditions = StringUtil.split((String) range);
			String condition = "";
			if (conditions != null) {
				int index;
				for (int i = 0; i < conditions.length; i++) {//对函数条件length(key)<=5这种不再在开头加key
					index = conditions[i] == null ? -1 : conditions[i].indexOf("(");
					condition += ((i <= 0 ? "" : (logic.isAnd() ? AND : OR))//连接方式
							+ (index >= 0 && index < conditions[i].indexOf(")") ? "" : getKey(key) + " ")//函数和非函数条件
							+ conditions[i]);//单个条件
				}
			}
			if (condition.isEmpty()) {
				return "";
			}

			return getCondition(logic.isNot(), condition);
		}
		else if (range instanceof Subquery) { //如果在 Parser 解析成 SQL 字符串再引用，没法保证安全性，毕竟可以再通过远程函数等方式来拼接再替代，最后引用的字符串就能注入
			return getKey(key) + (logic.isNot() ? NOT : "") + " IN " + getSubqueryString((Subquery) range);
		}

		throw new IllegalArgumentException(key + "{}:range 类型为" + range.getClass().getSimpleName()
				+ "！range 只能是 用','分隔条件的字符串 或者 可取选项JSONArray！");
	}
	/**WHERE key IN ('key0', 'key1', ... )
	 * @param in
	 * @return IN ('key0', 'key1', ... )
	 * @throws NotExistException 
	 */
	@JSONField(serialize = false)
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


	//}{ exists <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE EXISTS subquery
	 * 如果合并到 getRangeString，一方面支持不了 [1,2,2] 和 ">1" (转成 EXISTS(SELECT IN ) 需要static newSQLConfig，但它不能传入子类实例，除非不是 static)，另一方面多了子查询临时表性能会比 IN 差
	 * @param key
	 * @param value
	 * @return EXISTS ALL(SELECT ...)
	 * @throws NotExistException
	 */
	@JSONField(serialize = false)
	public String getExistsString(String key, Object value) throws Exception {
		if (value == null) {
			return "";
		}
		if (value instanceof Subquery == false) {
			throw new IllegalArgumentException(key + "}{:subquery 类型为" + value.getClass().getSimpleName()
					+ "！subquery 只能是 子查询JSONObejct！");
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getExistsString key = " + key);

		return (logic.isNot() ? NOT : "") + " EXISTS " + getSubqueryString((Subquery) value);
	}
	//}{ exists >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//<> contain <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**WHERE key contains value
	 * @param key
	 * @param value
	 * @return 	{@link #getContainString(String, Object[], int)}
	 * @throws NotExistException
	 */
	@JSONField(serialize = false)
	public String getContainString(String key, Object value) throws IllegalArgumentException {
		if (value == null) {
			return "";
		}

		Logic logic = new Logic(key);
		key = logic.getKey();
		Log.i(TAG, "getContainString key = " + key);

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
	@JSONField(serialize = false)
	public String getContainString(String key, Object[] childs, int type) throws IllegalArgumentException {
		boolean not = Logic.isNot(type);
		String condition = "";
		if (childs != null) {
			for (int i = 0; i < childs.length; i++) {
				if (childs[i] != null) {
					if (childs[i] instanceof JSON) {
						throw new IllegalArgumentException(key + "<>:value 中value类型不能为JSON！");
					}

					if (isPostgreSQL()) {
						condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR))
								+ getKey(key) + " @> " + getValue(newJSONArray(childs[i])); //operator does not exist: jsonb @> character varying  "[" + childs[i] + "]"); 
					} else {
						condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR))
								+ "json_contains(" + getKey(key) + ", " + getValue(childs[i].toString()) + ")";
					}
				}
			}
			if (condition.isEmpty()) {
				condition = (getKey(key) + SQL.isNull(true) + OR + getLikeString(key, "[]")); // key = '[]' 无结果！
			} else {
				condition = (getKey(key) + SQL.isNull(false) + AND + "(" + condition + ")");
			}
		}
		if (condition.isEmpty()) {
			return "";
		}
		return getCondition(not, condition);
	}
	//<> contain >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//key@:{} Subquery <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public String getSubqueryString(Subquery subquery) throws Exception {
		String range = subquery.getRange();
		SQLConfig cfg = subquery.getConfig();

		cfg.setPreparedValueList(new ArrayList<>());
		String sql = (range  == null || range.isEmpty() ? "" : range) + "(" + cfg.getSQL(isPrepared()) + ") ";

		preparedValueList.addAll(cfg.getPreparedValueList());

		return sql;
	}

	//key@:{} Subquery >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



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
	@JSONField(serialize = false)
	public String getSetString(RequestMethod method, Map<String, Object> content, boolean verifyName) throws Exception {
		Set<String> set = content == null ? null : content.keySet();
		String setString = "";

		if (set != null && set.size() > 0) {
			String quote = getQuote();

			boolean isFirst = true;
			int keyType = 0;// 0 - =; 1 - +, 2 - -
			Object value;

			String idKey = getIdKey();
			for (String key : set) {
				//避免筛选到全部	value = key == null ? null : content.get(key);
				if (key == null || idKey.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				}
				value = content.get(key);
				key = getRealKey(method, key, false, true, verifyName, quote);

				setString += (isFirst ? "" : ", ") + (getKey(key) + "=" + (keyType == 1 ? getAddString(key, value) : (keyType == 2
						? getRemoveString(key, value) : getValue(value)) ) );

				isFirst = false;
			}
		}

		if (setString.isEmpty()) {
			throw new IllegalArgumentException("PUT 请求必须在Table内设置要修改的 key:value ！");
		}
		return " SET " + setString;
	}

	/**SET key = CONCAT (key, 'value')
	 * @param key
	 * @param value
	 * @return CONCAT (key, 'value')
	 * @throws IllegalArgumentException
	 */
	@JSONField(serialize = false)
	public String getAddString(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			return getKey(key) + " + " + value;
		}
		if (value instanceof String) {
			return " CONCAT (" + getKey(key) + ", " + getValue(value) + ") ";
		}
		throw new IllegalArgumentException(key + "+ 对应的值 " + value + " 不是Number,String,Array中的任何一种！");
	}
	/**SET key = replace(key, 'value', '')
	 * @param key
	 * @param value
	 * @return REPLACE (key, 'value', '')
	 * @throws IllegalArgumentException
	 */
	@JSONField(serialize = false)
	public String getRemoveString(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			return getKey(key) + " - " + value;
		}
		if (value instanceof String) {
			return SQL.replace(getKey(key), (String) getValue(value), "");// " replace(" + key + ", '" + value + "', '') ";
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
		if (config == null) {
			Log.i(TAG, "getSQL  config == null >> return null;");
			return null;
		}

		//TODO procedure 改为 List<Procedure>  procedureList; behind : true; function: callFunction(); String key; ...
		// for (...) { Call procedure1();\n SQL \n; Call procedure2(); ... }
		// 貌似不需要，因为 ObjecParser 里就已经处理的顺序等，只是这里要解决下 Schema 问题。

		String sch = config.getSQLSchema();
		if (StringUtil.isNotEmpty(config.getProcedure(), true)) {
			String q = config.getQuote();
			return "CALL " + q + sch + q + "."+ config.getProcedure();
		}

		String tablePath = config.getTablePath();
		if (StringUtil.isNotEmpty(tablePath, true) == false) {
			Log.i(TAG, "getSQL  StringUtil.isNotEmpty(tablePath, true) == false >> return null;");
			return null;
		}

		switch (config.getMethod()) {
		case POST:
			return "INSERT INTO " + tablePath + config.getColumnString() + " VALUES" + config.getValuesString();
		case PUT:
			return "UPDATE " + tablePath + config.getSetString() + config.getWhereString(true);
		case DELETE:
			return "DELETE FROM " + tablePath + config.getWhereString(true);
		default:
			config.setPreparedValueList(new ArrayList<Object>());
			String column = config.getColumnString();
			return (config.isExplain() ? "EXPLAIN " : "") + "SELECT " + (config.getCache() == JSONRequest.CACHE_RAM ? "SQL_NO_CACHE " : "") + column + " FROM " + getConditionString(column, tablePath, config);
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
		String where = config.getWhereString(true);

		Subquery from = config.getFrom();
		if (from != null) {
			table = config.getSubqueryString(from) + " AS " + config.getAliasWithQuote() + " ";
		}

		String condition = table + config.getJoinString() + where + (
				RequestMethod.isGetMethod(config.getMethod(), true) == false ?
						"" : config.getGroupString(true) + config.getHavingString(true) + config.getOrderString(true)
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


	private boolean keyPrefix;
	@Override
	public boolean isKeyPrefix() {
		return keyPrefix;
	}
	@Override
	public AbstractSQLConfig setKeyPrefix(boolean keyPrefix) {
		this.keyPrefix = keyPrefix;
		return this;
	}



	public String getJoinString() throws Exception {
		String joinOns = "";

		if (joinList != null) {
			String quote = getQuote();
			List<Object> pvl = new ArrayList<>();
			boolean changed = false;

			String sql = null;
			SQLConfig jc;
			String jt;
			String tn;
			for (Join j : joinList) {
				if (j.isAppJoin()) { // APP JOIN，只是作为一个标记，执行完主表的查询后自动执行副表的查询 User.id IN($commentIdList)
					continue;
				}

				//LEFT JOIN sys.apijson_user AS User ON User.id = Moment.userId， 都是用 = ，通过relateType处理缓存
				// <"INNER JOIN User ON User.id = Moment.userId", UserConfig>  TODO  AS 放 getSQLTable 内
				jc = j.getJoinConfig();
				jc.setPrepared(isPrepared());

				jt = StringUtil.isEmpty(jc.getAlias(), true) ? jc.getTable() : jc.getAlias();
				tn = j.getTargetName();

				//如果要强制小写，则可在子类重写这个方法再 toLowerCase
				//				if (DATABASE_POSTGRESQL.equals(getDatabase())) {
				//					jt = jt.toLowerCase();
				//					tn = tn.toLowerCase();
				//				}

				switch (j.getJoinType()) { //TODO $ SELF JOIN
				//				case "@": // APP JOIN
				//					continue;

				case "<": // LEFT JOIN
				case ">": // RIGHT JOIN
					jc.setMain(true).setKeyPrefix(false);
					sql = ( ">".equals(j.getJoinType()) ? " RIGHT" : " LEFT") + " JOIN ( " + jc.getSQL(isPrepared()) + " ) AS "
							+ quote + jt + quote + " ON " + quote + jt + quote + "." + quote + j.getKey() + quote + " = "
							+ quote + tn + quote + "." + quote + j.getTargetKey() + quote;
					jc.setMain(false).setKeyPrefix(true);

					//					preparedValueList.addAll(jc.getPreparedValueList());

					pvl.addAll(jc.getPreparedValueList());
					changed = true;
					break;

				case "":  // FULL JOIN 
				case "|": // FULL JOIN  不支持 <>, [] ，避免太多符号
				case "&": // INNER JOIN 
				case "!": // OUTTER JOIN
				case "^": // SIDE JOIN
					//场景少且性能差，默认禁用	case "*": // CROSS JOIN
					sql = ("*".equals(j.getJoinType()) ? " CROSS JOIN " : " INNER JOIN ") + jc.getTablePath()
					+ " ON " + quote + jt + quote + "." + quote + j.getKey() + quote + " = " + quote + tn + quote + "." + quote + j.getTargetKey() + quote;
					break;
				default:
					throw new UnsupportedOperationException("join:value 中 value 里的 " + j.getJoinType() + "/" + j.getPath() + "错误！不支持 " + j.getJoinType() + " 等 [@ APP, < LEFT, > RIGHT, | FULL, & INNER, ! OUTTER, ^ SIDE, * CROSS] 之外的JOIN类型 !");
				}

				joinOns += "  \n  " + sql;
			}


			if (changed) {
				pvl.addAll(preparedValueList);
				preparedValueList = pvl;
			}

		}

		return joinOns;
	}

	/**新建SQL配置
	 * @param table
	 * @param request
	 * @param joinList
	 * @param isProcedure
	 * @param callback
	 * @return
	 * @throws Exception 
	 */
	public static AbstractSQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure, Callback callback) throws Exception {
		if (request == null) { // User:{} 这种空内容在查询时也有效
			throw new NullPointerException(TAG + ": newSQLConfig  request == null!");
		}

		boolean explain = request.getBooleanValue(KEY_EXPLAIN);
		if (explain && Log.DEBUG == false) { //不在 config.setExplain 抛异常，一方面处理更早性能更好，另一方面为了内部调用可以绕过这个限制
			throw new UnsupportedOperationException("DEBUG 模式下不允许传 " + KEY_EXPLAIN + " ！");
		}

		String database = request.getString(KEY_DATABASE);
		if (StringUtil.isEmpty(database, false) == false && DATABASE_LIST.contains(database) == false) {
			throw new UnsupportedDataTypeException("@database:value 中 value 错误，只能是 [" + StringUtil.getString(DATABASE_LIST.toArray()) + "] 中的一种！");
		}

		String schema = request.getString(KEY_SCHEMA);

		AbstractSQLConfig config = callback.getSQLConfig(method, database, schema, table);
		config.setAlias(alias);

		config.setDatabase(database); //不删，后面表对象还要用的，必须放在 parseJoin 前
		config.setSchema(schema); //不删，后面表对象还要用的

		if (isProcedure) {
			return config;
		}

		config = parseJoin(method, config, joinList, callback); //放后面会导致主表是空对象时 joinList 未解析

		if (request.isEmpty()) { // User:{} 这种空内容在查询时也有效
			return config; //request.remove(key); 前都可以直接return，之后必须保证 put 回去
		}


		String idKey = callback.getIdKey(database, schema, table);
		String idInKey = idKey + "{}";
		String userIdKey = callback.getUserIdKey(database, schema, table);
		String userIdInKey = userIdKey + "{}";

		Object idIn = request.get(idInKey); //可能是 id{}:">0"

		if (method == POST) {
			if (idIn != null) { //不能在这里确定[]的长度，只能在外面传进来
				if ((idIn instanceof List == false) || ((List<?>)idIn).isEmpty()) { // id{}:[] 表示同时插入多条记录
					throw new IllegalArgumentException("POST请求，生成多条记录请用 id{}:[] ！ [] 类型为JSONArray且不能为空！");
				}
			} else if (request.get(idKey) == null) {
				request.put(idKey, callback.newId(method, database, schema, table));
			}
		}

		//对id和id{}处理，这两个一定会作为条件
		Object id = request.get(idKey);
		if (id != null) { //null无效
			if (id instanceof Number) { 
				if (((Number) id).longValue() <= 0) { //一定没有值
					throw new NotExistException(TAG + ": newSQLConfig " + table + ".id <= 0");
				}
			}
			else if (id instanceof String) {
				if (StringUtil.isEmpty(id, true)) { //一定没有值
					throw new NotExistException(TAG + ": newSQLConfig StringUtil.isEmpty(" + table + ".id, true)");
				}
			}
			else if (id instanceof Subquery) {}
			else {
				throw new IllegalArgumentException(idKey + ":value 中 value 的类型只能是 Long , String 或 Subquery ！");
			}

			if (idIn instanceof List) { //共用idIn场景少性能差
				boolean contains = false;
				List<?> ids = ((List<?>) idIn);
				Object d;
				for (int i = 0; i < ids.size(); i++) { //不用 idIn.contains(id) 因为 idIn 里存到很可能是 Integer，id 又是 Long！
					d = ids.get(i);
					if (d != null && id.toString().equals(d.toString())) {
						contains = true;
						break;
					}
				}
				if (contains == false) {//empty有效  BaseModel.isEmpty(idIn) == false) {
					throw new NotExistException(TAG + ": newSQLConfig  idIn != null && (((List<?>) idIn).contains(id) == false");
				}
			}
		}


		String role = request.getString(KEY_ROLE);
		String cache = request.getString(KEY_CACHE);
		String combine = request.getString(KEY_COMBINE);
		Subquery from = (Subquery) request.get(KEY_FROM);
		String column = request.getString(KEY_COLUMN);
		String group = request.getString(KEY_GROUP);
		String having = request.getString(KEY_HAVING);
		String order = request.getString(KEY_ORDER);

		//强制作为条件且放在最前面优化性能
		request.remove(idKey);
		request.remove(idInKey);
		//关键词
		request.remove(KEY_ROLE);
		request.remove(KEY_EXPLAIN);
		request.remove(KEY_CACHE);
		request.remove(KEY_DATABASE);
		request.remove(KEY_SCHEMA);
		request.remove(KEY_COMBINE);
		request.remove(KEY_FROM);
		request.remove(KEY_COLUMN);
		request.remove(KEY_GROUP);
		request.remove(KEY_HAVING);
		request.remove(KEY_ORDER);


		Map<String, Object> tableWhere = new LinkedHashMap<String, Object>();//保证顺序好优化 WHERE id > 1 AND name LIKE...

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
				column = idKey + "," + StringUtil.getString(columns); //set已经判断过不为空
				final int size = columns.length + 1; //以key数量为准

				List<List<Object>> valuess = new ArrayList<>(idList.size()); // [idList.size()][]
				List<Object> items; //(item0, item1, ...)
				for (int i = 0; i < idList.size(); i++) {
					items = new ArrayList<>(size);
					items.add(idList.get(i)); //第0个就是id
					for (int j = 1; j < size; j++) {
						items.add(values[j-1]); //从第1个开始，允许"null"
					}
					valuess.add(items);
				}
				config.setValues(valuess);
			}
		} 
		else { //非POST操作
			final boolean isWhere = method != PUT;//除了POST,PUT，其它全是条件！！！

			//条件<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			List<String> whereList = null;

			Map<String, List<String>> combineMap = new LinkedHashMap<>();
			List<String> andList = new ArrayList<>();
			List<String> orList = new ArrayList<>();
			List<String> notList = new ArrayList<>();

			//强制作为条件且放在最前面优化性能
			if (id != null) {
				tableWhere.put(idKey, id);
				andList.add(idKey);
			}
			if (idIn != null) {
				tableWhere.put(idInKey, idIn);
				andList.add(idInKey);
			}

			String[] ws = StringUtil.split(combine);
			if (ws != null) {
				if (method == DELETE || method == GETS || method == HEADS) {
					throw new IllegalArgumentException("DELETE,GETS,HEADS 请求不允许传 @combine:\"conditons\" !");
				}
				whereList = new ArrayList<>();

				String w;
				for (int i = 0; i < ws.length; i++) { //去除 &,|,! 前缀
					w = ws[i];
					if (w != null) {
						if (w.startsWith("&")) {
							w = w.substring(1);
							andList.add(w);
						}
						else if (w.startsWith("|")) {
							if (method == PUT) {
								throw new IllegalArgumentException(table + ":{} 里的 @combine:value 中的value里条件 " + ws[i] + " 不合法！"
										+ "PUT请求的 @combine:\"key0,key1,...\" 不允许传 |key 或 !key !");
							}
							w = w.substring(1);
							orList.add(w);
						}
						else if (w.startsWith("!")) {
							if (method == PUT) {
								throw new IllegalArgumentException(table + ":{} 里的 @combine:value 中的value里条件 " + ws[i] + " 不合法！"
										+ "PUT请求的 @combine:\"key0,key1,...\" 不允许传 |key 或 !key !");
							}
							w = w.substring(1);
							notList.add(w);
						}
						else {
							orList.add(w);
						}

						if (w.isEmpty()) {
							throw new IllegalArgumentException(table + ":{} 里的 @combine:value 中的value里条件 " + ws[i] + " 不合法！不允许为空值！");
						}
						else {
							if (idKey.equals(w) || idInKey.equals(w) || userIdKey.equals(w) || userIdInKey.equals(w)) {
								throw new UnsupportedOperationException(table + ":{} 里的 @combine:value 中的value里 " + ws[i] + " 不合法！"
										+ "不允许传 [" + idKey + ", " + idInKey + ", " + userIdKey + ", " + userIdInKey + "] 其中任何一个！");
							}
						}

						whereList.add(w);
					}
					if (request.containsKey(w) == false) {
						throw new IllegalArgumentException(table + ":{} 里的 @combine:value 中的value里 " + ws[i] + " 对应的 " + w + " 不在它里面！");
					}
				}

			}

			//条件>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

			Map<String, Object> tableContent = new LinkedHashMap<String, Object>();
			Object value;
			for (String key : set) {
				value = request.get(key);

				if (value instanceof Map) {//只允许常规Object
					throw new IllegalArgumentException("不允许 " + key + " 等任何key的value类型为 {JSONObject} !");
				}

				//解决AccessVerifier新增userId没有作为条件，而是作为内容，导致PUT，DELETE出错
				if (isWhere) {
					tableWhere.put(key, value);
					if (whereList == null || whereList.contains(key) == false) {
						andList.add(key);
					}
				}
				else if (whereList != null && whereList.contains(key)) {
					tableWhere.put(key, value);
				}
				else {
					tableContent.put(key, value);//一样 instanceof JSONArray ? JSON.toJSONString(value) : value);
				}
			}

			combineMap.put("&", andList);
			combineMap.put("|", orList);
			combineMap.put("!", notList);
			config.setCombine(combineMap);

			config.setContent(tableContent);
		}

		boolean distinct = column == null ? false : column.startsWith(PREFFIX_DISTINCT);

		List<String> cs = new ArrayList<>();
		String[] fks = StringUtil.split(distinct ? column.substring(PREFFIX_DISTINCT.length()) : column, ";"); // key0,key1;fun0(key0,...);fun1(key0,...);key3;fun2(key0,...)
		if (fks != null) {
			String[] ks;
			for (String fk : fks) {
				if (fk.contains("(")) { //fun0(key0,...)
					cs.add(fk);
				}
				else { //key0,key1...
					ks = StringUtil.split(fk);
					if (ks != null && ks.length > 0) {
						cs.addAll(Arrays.asList(ks));
					}
				}
			}
		}

		config.setExplain(explain);
		config.setCache(cache);
		config.setFrom(from);
		config.setDistinct(distinct);
		config.setColumn(column == null ? null : cs); //解决总是 config.column != null，总是不能得到 *
		config.setWhere(tableWhere);					

		config.setId(id);
		//在	tableWhere 第0个		config.setIdIn(idIn);

		config.setRole(role);
		config.setGroup(group);
		config.setHaving(having);
		config.setOrder(order);

		//TODO 解析JOIN，包括 @column，@group 等要合并

		//后面还可能用到，要还原
		//id或id{}条件
		request.put(idKey, id);
		request.put(idInKey, idIn);
		//关键词
		request.put(KEY_DATABASE, database);
		request.put(KEY_ROLE, role);
		request.put(KEY_EXPLAIN, explain);
		request.put(KEY_CACHE, cache);
		request.put(KEY_SCHEMA, schema);
		request.put(KEY_COMBINE, combine);
		request.put(KEY_FROM, from);
		request.put(KEY_COLUMN, column);
		request.put(KEY_GROUP, group);
		request.put(KEY_HAVING, having);
		request.put(KEY_ORDER, order);

		return config;
	}



	/**
	 * @param method
	 * @param config
	 * @param joinList
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static AbstractSQLConfig parseJoin(RequestMethod method, AbstractSQLConfig config, List<Join> joinList, Callback callback) throws Exception {
		boolean isQuery = RequestMethod.isQueryMethod(method);
		config.setKeyPrefix(isQuery && config.isMain() == false);

		//TODO 解析出 SQLConfig 再合并 column, order, group 等
		if (joinList == null || joinList.isEmpty() || RequestMethod.isQueryMethod(method) == false) {
			return config;
		}


		String table;
		String alias;
		for (Join j : joinList) {
			table = j.getTable();
			alias = j.getAlias();
			//JOIN子查询不能设置LIMIT，因为ON关系是在子查询后处理的，会导致结果会错误
			SQLConfig joinConfig = newSQLConfig(method, table, alias, j.getRequest(), null, false, callback);
			SQLConfig cacheConfig = newSQLConfig(method, table, alias, j.getRequest(), null, false, callback).setCount(1);

			if (j.isAppJoin() == false) { //除了 @ APP JOIN，其它都是 SQL JOIN，则副表要这样配置
				if (joinConfig.getDatabase() == null) {
					joinConfig.setDatabase(config.getDatabase()); //解决主表 JOIN 副表，引号不一致
				}
				else if (joinConfig.getDatabase().equals(config.getDatabase()) == false) {
					throw new IllegalArgumentException("主表 " + config.getTable() + " 的 @database:" + config.getDatabase() + " 和它 SQL JOIN 的副表 " + table + " 的 @database:" + joinConfig.getDatabase() + " 不一致！");
				}
				if (joinConfig.getSchema() == null) {
					joinConfig.setSchema(config.getSchema()); //主表 JOIN 副表，默认 schema 一致
				}
				cacheConfig.setDatabase(joinConfig.getDatabase()).setSchema(joinConfig.getSchema()); //解决主表 JOIN 副表，引号不一致


				if (isQuery) {
					config.setKeyPrefix(true);
				}

				joinConfig.setMain(false).setKeyPrefix(true);

				if (j.isLeftOrRightJoin()) {
					SQLConfig outterConfig = newSQLConfig(method, table, alias, j.getOutter(), null, false, callback);
					outterConfig.setMain(false).setKeyPrefix(true).setDatabase(joinConfig.getDatabase()).setSchema(joinConfig.getSchema()); //解决主表 JOIN 副表，引号不一致
					j.setOutterConfig(outterConfig);
				}
			}

			//解决 query: 1/2 查数量时报错  
			/* SELECT  count(*)  AS count  FROM sys.Moment AS Moment  
			   LEFT JOIN ( SELECT count(*)  AS count FROM sys.Comment ) AS Comment ON Comment.momentId = Moment.id LIMIT 1 OFFSET 0 */
			if (RequestMethod.isHeadMethod(method, true)) {
				joinConfig.setMethod(GET); //子查询不能为 SELECT count(*) ，而应该是 SELECT momentId
				joinConfig.setColumn(Arrays.asList(j.getKey())); //优化性能，不取非必要的字段

				cacheConfig.setMethod(GET); //子查询不能为 SELECT count(*) ，而应该是 SELECT momentId
				cacheConfig.setColumn(Arrays.asList(j.getKey())); //优化性能，不取非必要的字段
			}

			j.setJoinConfig(joinConfig);
			j.setCacheConfig(cacheConfig);
		}

		config.setJoinList(joinList);

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
			, boolean isTableKey, boolean saveLogic, String quote) throws Exception {
		return getRealKey(method, originKey, isTableKey, saveLogic, true, quote);
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
			, boolean isTableKey, boolean saveLogic, boolean verifyName, String quote) throws Exception {
		Log.i(TAG, "getRealKey  saveLogic = " + saveLogic + "; originKey = " + originKey);
		if (originKey == null || originKey.startsWith(quote) || zuo.biao.apijson.JSONObject.isArrayKey(originKey)) {
			Log.w(TAG, "getRealKey  originKey == null || originKey.startsWith(`)"
					+ " || zuo.biao.apijson.JSONObject.isArrayKey(originKey) >>  return originKey;");
			return originKey;
		}

		String key = new String(originKey);
		if (key.endsWith("$")) {//搜索 LIKE，查询时处理
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("~") || key.endsWith("?")) {//匹配正则表达式 REGEXP，查询时处理  TODO ？可能以后会被废弃，全用 ~ 和 *~ 替代，更接近 PostgreSQL 语法 
			key = key.substring(0, key.length() - 1);
			if (key.endsWith("*")) {//忽略大小写
				key = key.substring(0, key.length() - 1);
			}
		}
		else if (key.endsWith("%")) {//数字、文本、日期范围 BETWEEN AND
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("{}")) {//被包含 IN，或者说key对应值处于value的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("}{")) {//被包含 EXISTS，或者说key对应值处于value的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("<>")) {//包含 json_contains，或者说value处于key对应值的范围内。查询时处理
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("()")) {//方法，查询完后处理，先用一个Map<key,function>保存
			key = key.substring(0, key.length() - 2);
		} 
		else if (key.endsWith("@")) {//引用，引用对象查询完后处理。fillTarget中暂时不用处理，因为非GET请求都是由给定的id确定，不需要引用
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith(">=")) {//比较。查询时处理
			key = key.substring(0, key.length() - 2);
		}
		else if (key.endsWith("<=")) {//比较。查询时处理
			key = key.substring(0, key.length() - 2);
		}
		else if (key.endsWith(">")) {//比较。查询时处理
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("<")) {//比较。查询时处理
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
			throw new IllegalArgumentException(method + "请求，字符 " + originKey + " 不合法！"
					+ " key:value 中的key只能关键词 '@key' 或 'key[逻辑符][条件符]' 或 PUT请求下的 'key+' / 'key-' ！");
		}

		if (saveLogic && last != null) {
			key = key + last;
		}
		Log.i(TAG, "getRealKey  return key = " + key);
		return key;
	}


	public static interface Callback {
		/**获取 SQLConfig 的实例
		 * @param method
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		AbstractSQLConfig getSQLConfig(RequestMethod method, String database, String schema, String table);


		/**为 post 请求新建 id， 只能是 Long 或 String
		 * @param method
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		Object newId(RequestMethod method, String database, String schema, String table);

		/**获取主键名
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		String getIdKey(String database, String schema, String table);

		/**获取 User 的主键名
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		String getUserIdKey(String database, String schema, String table);
	}

	public static abstract class SimpleCallback implements Callback {


		@Override
		public Object newId(RequestMethod method, String database, String schema, String table) {
			return System.currentTimeMillis();
		}

		@Override
		public String getIdKey(String database, String schema, String table) {
			return KEY_ID;
		}

		@Override
		public String getUserIdKey(String database, String schema, String table) {
			return KEY_USER_ID;
		}

	}

}
