/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import static apijson.JSONObject.KEY_CACHE;
import static apijson.JSONObject.KEY_COLUMN;
import static apijson.JSONObject.KEY_COMBINE;
import static apijson.JSONObject.KEY_DATABASE;
import static apijson.JSONObject.KEY_DATASOURCE;
import static apijson.JSONObject.KEY_EXPLAIN;
import static apijson.JSONObject.KEY_FROM;
import static apijson.JSONObject.KEY_GROUP;
import static apijson.JSONObject.KEY_HAVING;
import static apijson.JSONObject.KEY_ID;
import static apijson.JSONObject.KEY_JSON;
import static apijson.JSONObject.KEY_ORDER;
import static apijson.JSONObject.KEY_RAW;
import static apijson.JSONObject.KEY_ROLE;
import static apijson.JSONObject.KEY_SCHEMA;
import static apijson.JSONObject.KEY_USER_ID;
import static apijson.RequestMethod.DELETE;
import static apijson.RequestMethod.GET;
import static apijson.RequestMethod.GETS;
import static apijson.RequestMethod.HEAD;
import static apijson.RequestMethod.HEADS;
import static apijson.RequestMethod.POST;
import static apijson.RequestMethod.PUT;
import static apijson.SQL.AND;
import static apijson.SQL.NOT;
import static apijson.SQL.OR;

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

import apijson.JSON;
import apijson.JSONResponse;
import apijson.Log;
import apijson.NotNull;
import apijson.RequestMethod;
import apijson.SQL;
import apijson.StringUtil;
import apijson.orm.exception.NotExistException;
import apijson.orm.model.Access;
import apijson.orm.model.Column;
import apijson.orm.model.Document;
import apijson.orm.model.ExtendedProperty;
import apijson.orm.model.Function;
import apijson.orm.model.PgAttribute;
import apijson.orm.model.PgClass;
import apijson.orm.model.Request;
import apijson.orm.model.Response;
import apijson.orm.model.SysColumn;
import apijson.orm.model.SysTable;
import apijson.orm.model.Table;
import apijson.orm.model.TestRecord;

/**config sql for JSON Request
 * @author Lemon
 */
public abstract class AbstractSQLConfig implements SQLConfig {
	private static final String TAG = "AbstractSQLConfig";

	public static String DEFAULT_DATABASE = DATABASE_MYSQL;
	public static String DEFAULT_SCHEMA = "sys";
	public static String PREFFIX_DISTINCT = "DISTINCT ";

	// * 和 / 不能同时出现，防止 /* */ 段注释！ # 和 -- 不能出现，防止行注释！ ; 不能出现，防止隔断SQL语句！空格不能出现，防止 CRUD,DROP,SHOW TABLES等语句！
	private static final Pattern PATTERN_RANGE;
	private static final Pattern PATTERN_FUNCTION;
	private  static final Pattern PATTERN_STRING;

	/**
	 * 表名映射，隐藏真实表名，对安全要求很高的表可以这么做
	 */
	public static final Map<String, String> TABLE_KEY_MAP;
	public static final List<String> CONFIG_TABLE_LIST;
	public static final List<String> DATABASE_LIST;

	// 自定义原始 SQL 片段 Map<key, substring>：当 substring 为 null 时忽略；当 substring 为 "" 时整个 value 是 raw SQL；其它情况则只是 substring 这段为 raw SQL
	public static final Map<String, String> RAW_MAP;
	// 允许调用的 SQL 函数：当 substring 为 null 时忽略；当 substring 为 "" 时整个 value 是 raw SQL；其它情况则只是 substring 这段为 raw SQL
	public static final Map<String, String> SQL_FUNCTION_MAP;

	static {  // 凡是 SQL 边界符、分隔符、注释符 都不允许，例如 ' " ` ( ) ; # -- ，以免拼接 SQL 时被注入意外可执行指令
		PATTERN_RANGE = Pattern.compile("^[0-9%,!=\\<\\>/\\.\\+\\-\\*\\^]+$"); // ^[a-zA-Z0-9_*%!=<>(),"]+$ 导致 exists(select*from(Comment)) 通过！
		PATTERN_FUNCTION = Pattern.compile("^[A-Za-z0-9%,:_@&~`!=\\<\\>\\|\\[\\]\\{\\} /\\.\\+\\-\\*\\^\\?\\(\\)\\$]+$"); //TODO 改成更好的正则，校验前面为单词，中间为操作符，后面为值
		PATTERN_STRING = Pattern.compile("^[,#;\"`]+$");

		TABLE_KEY_MAP = new HashMap<String, String>();
		TABLE_KEY_MAP.put(Table.class.getSimpleName(), Table.TABLE_NAME);
		TABLE_KEY_MAP.put(Column.class.getSimpleName(), Column.TABLE_NAME);
		TABLE_KEY_MAP.put(PgClass.class.getSimpleName(), PgClass.TABLE_NAME);
		TABLE_KEY_MAP.put(PgAttribute.class.getSimpleName(), PgAttribute.TABLE_NAME);
		TABLE_KEY_MAP.put(SysTable.class.getSimpleName(), SysTable.TABLE_NAME);
		TABLE_KEY_MAP.put(SysColumn.class.getSimpleName(), SysColumn.TABLE_NAME);
		TABLE_KEY_MAP.put(ExtendedProperty.class.getSimpleName(), ExtendedProperty.TABLE_NAME);

		CONFIG_TABLE_LIST = new ArrayList<>();  // Table, Column 等是系统表 AbstractVerifier.SYSTEM_ACCESS_MAP.keySet());
		CONFIG_TABLE_LIST.add(Function.class.getSimpleName());
		CONFIG_TABLE_LIST.add(Request.class.getSimpleName());
		CONFIG_TABLE_LIST.add(Response.class.getSimpleName());
		CONFIG_TABLE_LIST.add(Access.class.getSimpleName());
		CONFIG_TABLE_LIST.add(Document.class.getSimpleName());
		CONFIG_TABLE_LIST.add(TestRecord.class.getSimpleName());


		DATABASE_LIST = new ArrayList<>();
		DATABASE_LIST.add(DATABASE_MYSQL);
		DATABASE_LIST.add(DATABASE_POSTGRESQL);
		DATABASE_LIST.add(DATABASE_SQLSERVER);
		DATABASE_LIST.add(DATABASE_ORACLE);
		DATABASE_LIST.add(DATABASE_DB2);
		DATABASE_LIST.add(DATABASE_CLICKHOUSE);


		RAW_MAP = new LinkedHashMap<>();  // 保证顺序，避免配置冲突等意外情况

		// MySQL 关键字
		RAW_MAP.put("AS", "");
		RAW_MAP.put("IS NOT NULL", "");
		RAW_MAP.put("IS NULL", "");
		RAW_MAP.put("IS", "");
		RAW_MAP.put("NULL", "");
		RAW_MAP.put("AND", "");
		RAW_MAP.put("OR", "");
		RAW_MAP.put("NOT", "");
		RAW_MAP.put("VALUE", "");
		RAW_MAP.put("DISTINCT", "");

		//时间
		RAW_MAP.put("now()", "");
		RAW_MAP.put("DATE", "");
		RAW_MAP.put("TIME", "");
		RAW_MAP.put("DATETIME", "");
		RAW_MAP.put("TIMESTAMP", "");
		RAW_MAP.put("DateTime", "");
		RAW_MAP.put("SECOND", "");
		RAW_MAP.put("MINUTE", "");
		RAW_MAP.put("HOUR", "");
		RAW_MAP.put("DAY", "");
		RAW_MAP.put("WEEK", "");
		RAW_MAP.put("MONTH", "");
		RAW_MAP.put("QUARTER", "");
		RAW_MAP.put("YEAR", "");
//		RAW_MAP.put("json", "");
//		RAW_MAP.put("unit", "");

		//MYSQL 数据类型 BINARY，CHAR，DATETIME，TIME，DECIMAL，SIGNED，UNSIGNED
		RAW_MAP.put("BINARY", "");
		RAW_MAP.put("SIGNED", "");
		RAW_MAP.put("DECIMAL", "");
		RAW_MAP.put("DOUBLE", "");
		RAW_MAP.put("FLOAT", "");
		RAW_MAP.put("BOOLEAN", "");
		RAW_MAP.put("ENUM", "");
		RAW_MAP.put("SET", "");
		RAW_MAP.put("POINT", "");
		RAW_MAP.put("BLOB", "");
		RAW_MAP.put("LONGBLOB", "");
		RAW_MAP.put("BINARY", "");
		RAW_MAP.put("UNSIGNED", "");
		RAW_MAP.put("BIT", "");
		RAW_MAP.put("TINYINT", "");
		RAW_MAP.put("SMALLINT", "");
		RAW_MAP.put("INT", "");
		RAW_MAP.put("BIGINT", "");
		RAW_MAP.put("CHAR", "");
		RAW_MAP.put("VARCHAR", "");
		RAW_MAP.put("TEXT", "");
		RAW_MAP.put("LONGTEXT", "");
		RAW_MAP.put("JSON", "");

		//窗口函数关键字
		RAW_MAP.put("OVER", "");
		RAW_MAP.put("INTERVAL", "");
		RAW_MAP.put("GROUP BY", ""); //往前
		RAW_MAP.put("GROUP", ""); //往前
		RAW_MAP.put("ORDER BY", ""); //往前
		RAW_MAP.put("ORDER", "");
		RAW_MAP.put("PARTITION BY", ""); //往前
		RAW_MAP.put("PARTITION", ""); //往前
		RAW_MAP.put("BY", "");
		RAW_MAP.put("DESC", "");
		RAW_MAP.put("ASC", "");
		RAW_MAP.put("FOLLOWING", "");//往后
		RAW_MAP.put("BETWEEN", "");
		RAW_MAP.put("AND", "");
		RAW_MAP.put("ROWS", "");

		RAW_MAP.put("AGAINST", "");
		RAW_MAP.put("IN NATURAL LANGUAGE MODE", "");
		RAW_MAP.put("IN BOOLEAN MODE", "");
		RAW_MAP.put("IN", "");
		RAW_MAP.put("BOOLEAN", "");
		RAW_MAP.put("NATURAL", "");
		RAW_MAP.put("LANGUAGE", "");
		RAW_MAP.put("MODE", "");

		

		SQL_FUNCTION_MAP = new LinkedHashMap<>();  // 保证顺序，避免配置冲突等意外情况

		//窗口函数
		SQL_FUNCTION_MAP.put("rank", "");//得到数据项在分组中的排名，排名相等的时候会留下空位
		SQL_FUNCTION_MAP.put("dense_rank", ""); //得到数据项在分组中的排名，排名相等的时候不会留下空位
		SQL_FUNCTION_MAP.put("row_number", "");//按照分组中的顺序生成序列，不存在重复的序列
		SQL_FUNCTION_MAP.put("ntile", "");//用于将分组数据按照顺序切分成N片，返回当前切片值，不支持ROWS_BETWEE
		SQL_FUNCTION_MAP.put("first_value", "");//取分组排序后，截止到当前行，分组内第一个值
		SQL_FUNCTION_MAP.put("last_value", "");//取分组排序后，截止到当前行，分组内的最后一个值
		SQL_FUNCTION_MAP.put("lag", "");//统计窗口内往上第n行值。第一个参数为列名，第二个参数为往上第n行（可选，默认为1），第三个参数为默认值（当往上第n行为NULL时候，取默认值，如不指定，则为NULL）
		SQL_FUNCTION_MAP.put("lead", "");//统计窗口内往下第n行值。第一个参数为列名，第二个参数为往下第n行（可选，默认为1），第三个参数为默认值（当往下第n行为NULL时候，取默认值，如不指定，则为NULL）
		SQL_FUNCTION_MAP.put("cume_dist", "");//)返回（小于等于当前行值的行数）/（当前分组内的总行数）
		SQL_FUNCTION_MAP.put("percent_rank", "");//返回（组内当前行的rank值-1）/（分组内做总行数-1）

		// MySQL 字符串函数
		SQL_FUNCTION_MAP.put("ascii", "");  // ASCII(s)	返回字符串 s 的第一个字符的 ASCII 码。
		SQL_FUNCTION_MAP.put("char_length", "");  // CHAR_LENGTH(s)	返回字符串 s 的字符数
		SQL_FUNCTION_MAP.put("character_length", "");  // CHARACTER_LENGTH(s)	返回字符串 s 的字符数
		SQL_FUNCTION_MAP.put("concat", "");  // CONCAT(s1, s2...sn)	字符串 s1,s2 等多个字符串合并为一个字符串
		SQL_FUNCTION_MAP.put("concat_ws", "");  // CONCAT_WS(x, s1, s2...sn)	同 CONCAT(s1, s2 ...) 函数，但是每个字符串之间要加上 x，x 可以是分隔符
		SQL_FUNCTION_MAP.put("field", "");  // FIELD(s, s1, s2...)	返回第一个字符串 s 在字符串列表 (s1, s2...)中的位置
		SQL_FUNCTION_MAP.put("find_in_set", "");  // FIND_IN_SET(s1, s2)	返回在字符串s2中与s1匹配的字符串的位置
		SQL_FUNCTION_MAP.put("format", "");  // FORMAT(x, n)	函数可以将数字 x 进行格式化 "#,###.##", 将 x 保留到小数点后 n 位，最后一位四舍五入。
		SQL_FUNCTION_MAP.put("insert", "");  // INSERT(s1, x, len, s2)	字符串 s2 替换 s1 的 x 位置开始长度为 len 的字符串
		SQL_FUNCTION_MAP.put("locate", "");  // LOCATE(s1, s)	从字符串 s 中获取 s1 的开始位置
		SQL_FUNCTION_MAP.put("lcase", "");  // LCASE(s)	将字符串 s 的所有字母变成小写字母
		SQL_FUNCTION_MAP.put("left", "");  // LEFT(s, n)	返回字符串 s 的前 n 个字符
		SQL_FUNCTION_MAP.put("length", "");  // LENGTH(s)	返回字符串 s 的字符数
		SQL_FUNCTION_MAP.put("lower", "");  // LOWER(s)	将字符串 s 的所有字母变成小写字母
		SQL_FUNCTION_MAP.put("lpad", "");  // LPAD(s1, len, s2)	在字符串 s1 的开始处填充字符串 s2，使字符串长度达到 len
		SQL_FUNCTION_MAP.put("ltrim", "");  // LTRIM(s)	去掉字符串 s 开始处的空格
		SQL_FUNCTION_MAP.put("mid", "");  // MID(s, n, len)	从字符串 s 的 n 位置截取长度为 len 的子字符串，同 SUBSTRING(s, n, len)
		SQL_FUNCTION_MAP.put("position", "");  // POSITION(s, s1);	从字符串 s 中获取 s1 的开始位置
		SQL_FUNCTION_MAP.put("repeat", "");  // REPEAT(s, n)	将字符串 s 重复 n 次
		SQL_FUNCTION_MAP.put("replace", "");  // REPLACE(s, s1, s2)	将字符串 s2 替代字符串 s 中的字符串 s1
		SQL_FUNCTION_MAP.put("reverse", "");  // REVERSE(s);  // )	将字符串s的顺序反过来
		SQL_FUNCTION_MAP.put("right", "");  // RIGHT(s, n)	返回字符串 s 的后 n 个字符
		SQL_FUNCTION_MAP.put("rpad", "");  // RPAD(s1, len, s2)	在字符串 s1 的结尾处添加字符串 s2，使字符串的长度达到 len
		SQL_FUNCTION_MAP.put("rtrim", "");  // RTRIM", "");  // )	去掉字符串 s 结尾处的空格
		SQL_FUNCTION_MAP.put("space", "");  // SPACE(n)	返回 n 个空格
		SQL_FUNCTION_MAP.put("strcmp", "");  // STRCMP(s1, s2)	比较字符串 s1 和 s2，如果 s1 与 s2 相等返回 0 ，如果 s1>s2 返回 1，如果 s1<s2 返回 -1
		SQL_FUNCTION_MAP.put("substr", "");  // SUBSTR(s, start, length)	从字符串 s 的 start 位置截取长度为 length 的子字符串
		SQL_FUNCTION_MAP.put("substring", "");  // STRING(s, start, length))	从字符串 s 的 start 位置截取长度为 length 的子字符串
		SQL_FUNCTION_MAP.put("substring_index", "");  // SUBSTRING_INDEX(s, delimiter, number)	返回从字符串 s 的第 number 个出现的分隔符 delimiter 之后的子串。
		SQL_FUNCTION_MAP.put("trim", "");  // TRIM(s)	去掉字符串 s 开始和结尾处的空格
		SQL_FUNCTION_MAP.put("ucase", "");  // UCASE(s)	将字符串转换为大写
		SQL_FUNCTION_MAP.put("upper", "");  // UPPER(s)	将字符串转换为大写

		// MySQL 数字函数
		SQL_FUNCTION_MAP.put("abs", "");  // ABS(x)	返回 x 的绝对值　　
		SQL_FUNCTION_MAP.put("acos", "");  // ACOS(x)	求 x 的反余弦值(参数是弧度)
		SQL_FUNCTION_MAP.put("asin", "");  // ASIN(x)	求反正弦值(参数是弧度)
		SQL_FUNCTION_MAP.put("atan", "");  // ATAN(x)	求反正切值(参数是弧度)
		SQL_FUNCTION_MAP.put("atan2", "");  // ATAN2(n, m)	求反正切值(参数是弧度)
		SQL_FUNCTION_MAP.put("avg", "");  // AVG(expression)	返回一个表达式的平均值，expression 是一个字段
		SQL_FUNCTION_MAP.put("ceil", "");  // CEIL(x)	返回大于或等于 x 的最小整数　
		SQL_FUNCTION_MAP.put("ceiling", "");  // CEILING(x)	返回大于或等于 x 的最小整数　
		SQL_FUNCTION_MAP.put("cos", "");  // COS(x)	求余弦值(参数是弧度)
		SQL_FUNCTION_MAP.put("cot", "");  // COT(x)	求余切值(参数是弧度)
		SQL_FUNCTION_MAP.put("count", "");  // COUNT(expression)	返回查询的记录总数，expression 参数是一个字段或者 * 号
		SQL_FUNCTION_MAP.put("degrees", "");  // DEGREES(x)	将弧度转换为角度　　
		SQL_FUNCTION_MAP.put("div", "");  // n DIV m	整除，n 为被除数，m 为除数
		SQL_FUNCTION_MAP.put("exp", "");  // EXP(x)	返回 e 的 x 次方　　
		SQL_FUNCTION_MAP.put("floor", "");  // FLOOR(x)	返回小于或等于 x 的最大整数　　
		SQL_FUNCTION_MAP.put("greatest", "");  // GREATEST(expr1, expr2, expr3, ...)	返回列表中的最大值
		SQL_FUNCTION_MAP.put("least", "");  // LEAST(expr1, expr2, expr3, ...)	返回列表中的最小值
		SQL_FUNCTION_MAP.put("ln", "");  // 2);  LN	返回数字的自然对数，以 e 为底。
		SQL_FUNCTION_MAP.put("log", "");  // LOG(x) 或 LOG(base, x)	返回自然对数(以 e 为底的对数)，如果带有 base 参数，则 base 为指定带底数。　　
		SQL_FUNCTION_MAP.put("log10", "");  // LOG10(x)	返回以 10 为底的对数　　
		SQL_FUNCTION_MAP.put("log2", "");  // LOG2(x)	返回以 2 为底的对数
		SQL_FUNCTION_MAP.put("max", "");  // MAX(expression)	返回字段 expression 中的最大值
		SQL_FUNCTION_MAP.put("min", "");  // MIN(expression)	返回字段 expression 中的最小值
		SQL_FUNCTION_MAP.put("mod", "");  // MOD(x,y)	返回 x 除以 y 以后的余数　
		SQL_FUNCTION_MAP.put("pi", "");  // PI()	返回圆周率(3.141593）　　
		SQL_FUNCTION_MAP.put("pow", "");  // POW(x,y)	返回 x 的 y 次方　
		SQL_FUNCTION_MAP.put("power", "");  // POWER(x,y)	返回 x 的 y 次方　
		SQL_FUNCTION_MAP.put("radians", "");  // RADIANS(x)	将角度转换为弧度　　
		SQL_FUNCTION_MAP.put("rand", "");  // RAND()	返回 0 到 1 的随机数　　
		SQL_FUNCTION_MAP.put("round", "");  // ROUND(x)	返回离 x 最近的整数
		SQL_FUNCTION_MAP.put("sign", "");  // SIGN(x)	返回 x 的符号，x 是负数、0、正数分别返回 -1、0 和 1　
		SQL_FUNCTION_MAP.put("sin", "");  // SIN(x)	求正弦值(参数是弧度)　　
		SQL_FUNCTION_MAP.put("sqrt", "");  // SQRT(x)	返回x的平方根　　
		SQL_FUNCTION_MAP.put("sum", "");  // SUM(expression)	返回指定字段的总和
		SQL_FUNCTION_MAP.put("tan", "");  // TAN(x)	求正切值(参数是弧度)
		SQL_FUNCTION_MAP.put("truncate", "");  // TRUNCATE(x,y)	返回数值 x 保留到小数点后 y 位的值（与 ROUND 最大的区别是不会进行四舍五入）

		// MySQL 时间与日期函数
		SQL_FUNCTION_MAP.put("adddate", "");  // ADDDATE(d,n)	计算起始日期 d 加上 n 天的日期
		SQL_FUNCTION_MAP.put("addtime", "");  // ADDTIME(t,n)	n 是一个时间表达式，时间 t 加上时间表达式 n
		SQL_FUNCTION_MAP.put("curdate", "");  // CURDATE()	返回当前日期
		SQL_FUNCTION_MAP.put("current_date", "");  // CURRENT_DATE()	返回当前日期
		SQL_FUNCTION_MAP.put("current_time", "");  // CURRENT_TIME	返回当前时间
		SQL_FUNCTION_MAP.put("current_timestamp", "");  // CURRENT_TIMESTAMP()	返回当前日期和时间
		SQL_FUNCTION_MAP.put("curtime", "");  // CURTIME()	返回当前时间
		SQL_FUNCTION_MAP.put("date", "");  // DATE()	从日期或日期时间表达式中提取日期值
		SQL_FUNCTION_MAP.put("datediff", "");  // DATEDIFF(d1,d2)	计算日期 d1->d2 之间相隔的天数
		SQL_FUNCTION_MAP.put("date_add", "");  // DATE_ADD(d，INTERVAL expr type)	计算起始日期 d 加上一个时间段后的日期
		SQL_FUNCTION_MAP.put("date_format", "");  // DATE_FORMAT(d,f)	按表达式 f的要求显示日期 d
		SQL_FUNCTION_MAP.put("date_sub", "");  // DATE_SUB(date,INTERVAL expr type)	函数从日期减去指定的时间间隔。
		SQL_FUNCTION_MAP.put("day", "");  // DAY(d)	返回日期值 d 的日期部分
		SQL_FUNCTION_MAP.put("dayname", "");  // DAYNAME(d)	返回日期 d 是星期几，如 Monday,Tuesday
		SQL_FUNCTION_MAP.put("dayofmonth", "");  // DAYOFMONTH(d)	计算日期 d 是本月的第几天
		SQL_FUNCTION_MAP.put("dayofweek", "");  // DAYOFWEEK(d)	日期 d 今天是星期几，1 星期日，2 星期一，以此类推
		SQL_FUNCTION_MAP.put("dayofyear", "");  // DAYOFYEAR(d)	计算日期 d 是本年的第几天
		SQL_FUNCTION_MAP.put("extract", "");  // EXTRACT(type FROM d)	从日期 d 中获取指定的值，type 指定返回的值。
		SQL_FUNCTION_MAP.put("from_days", "");  // FROM_DAYS(n)	计算从 0000 年 1 月 1 日开始 n 天后的日期
		SQL_FUNCTION_MAP.put("hour", "");  // 'HOUR(t)	返回 t 中的小时值
		SQL_FUNCTION_MAP.put("last_day", "");  // LAST_DAY(d)	返回给给定日期的那一月份的最后一天
		SQL_FUNCTION_MAP.put("localtime", "");  // LOCALTIME()	返回当前日期和时间
		SQL_FUNCTION_MAP.put("localtimestamp", "");  // LOCALTIMESTAMP()	返回当前日期和时间
		SQL_FUNCTION_MAP.put("makedate", "");  // MAKEDATE(year, day-of-year)	基于给定参数年份 year 和所在年中的天数序号 day-of-year 返回一个日期
		SQL_FUNCTION_MAP.put("maketime", "");  // MAKETIME(hour, minute, second)	组合时间，参数分别为小时、分钟、秒
		SQL_FUNCTION_MAP.put("microsecond", "");  // MICROSECOND(date)	返回日期参数所对应的微秒数
		SQL_FUNCTION_MAP.put("minute", "");  // MINUTE(t)	返回 t 中的分钟值
		SQL_FUNCTION_MAP.put("monthname", "");  // MONTHNAME(d)	返回日期当中的月份名称，如 November
		SQL_FUNCTION_MAP.put("month", "");  // MONTH(d)	返回日期d中的月份值，1 到 12
		SQL_FUNCTION_MAP.put("now", "");  // NOW()	返回当前日期和时间
		SQL_FUNCTION_MAP.put("period_add", "");  // PERIOD_ADD(period, number)	为 年-月 组合日期添加一个时段
		SQL_FUNCTION_MAP.put("period_diff", "");  // PERIOD_DIFF(period1, period2)	返回两个时段之间的月份差值
		SQL_FUNCTION_MAP.put("quarter", "");  // QUARTER(d)	返回日期d是第几季节，返回 1 到 4
		SQL_FUNCTION_MAP.put("second", "");  // SECOND(t)	返回 t 中的秒钟值
		SQL_FUNCTION_MAP.put("sec_to_time", "");  // SEC_TO_TIME", "");  // )	将以秒为单位的时间 s 转换为时分秒的格式
		SQL_FUNCTION_MAP.put("str_to_date", "");  // STR_TO_DATE", "");  // tring, format_mask)	将字符串转变为日期
		SQL_FUNCTION_MAP.put("subdate", "");  // SUBDATE(d,n)	日期 d 减去 n 天后的日期
		SQL_FUNCTION_MAP.put("subtime", "");  // SUBTIME(t,n)	时间 t 减去 n 秒的时间
		SQL_FUNCTION_MAP.put("sysdate", "");  // SYSDATE()	返回当前日期和时间
		SQL_FUNCTION_MAP.put("time", "");  // TIME(expression)	提取传入表达式的时间部分
		SQL_FUNCTION_MAP.put("time_format", "");  // TIME_FORMAT(t,f)	按表达式 f 的要求显示时间 t
		SQL_FUNCTION_MAP.put("time_to_sec", "");  // TIME_TO_SEC(t)	将时间 t 转换为秒
		SQL_FUNCTION_MAP.put("timediff", "");  // TIMEDIFF(time1, time2)	计算时间差值
		SQL_FUNCTION_MAP.put("timestamp", "");  // TIMESTAMP(expression, interval)	单个参数时，函数返回日期或日期时间表达式；有2个参数时，将参数加和
		SQL_FUNCTION_MAP.put("to_days", "");  // TO_DAYS(d)	计算日期 d 距离 0000 年 1 月 1 日的天数
		SQL_FUNCTION_MAP.put("week", "");  // WEEK(d)	计算日期 d 是本年的第几个星期，范围是 0 到 53
		SQL_FUNCTION_MAP.put("weekday", "");  // WEEKDAY(d)	日期 d 是星期几，0 表示星期一，1 表示星期二
		SQL_FUNCTION_MAP.put("weekofyear", "");  // WEEKOFYEAR(d)	计算日期 d 是本年的第几个星期，范围是 0 到 53
		SQL_FUNCTION_MAP.put("year", "");  // YEAR(d)	返回年份
		SQL_FUNCTION_MAP.put("yearweek", "");  // YEARWEEK(date, mode)	返回年份及第几周（0到53），mode 中 0 表示周天，1表示周一，以此类推
		SQL_FUNCTION_MAP.put("unix_timestamp", "");  // UNIX_TIMESTAMP(date)	获取UNIX时间戳函数，返回一个以 UNIX 时间戳为基础的无符号整数
		SQL_FUNCTION_MAP.put("from_unixtime", "");  // FROM_UNIXTIME(date)	将 UNIX 时间戳转换为时间格式，与UNIX_TIMESTAMP互为反函数

		// MYSQL JSON 函数
		SQL_FUNCTION_MAP.put("json_append", "");  // JSON_APPEND(json_doc, path, val[, path, val] ...)) 插入JSON数组
		SQL_FUNCTION_MAP.put("json_array", "");  // JSON_ARRAY(val1, val2...) 创建JSON数组
		SQL_FUNCTION_MAP.put("json_array_append", "");  // JSON_ARRAY_APPEND(json_doc, val) 将数据附加到JSON文档
		SQL_FUNCTION_MAP.put("json_array_insert", "");  // JSON_ARRAY_INSERT(json_doc, val) 插入JSON数组
		SQL_FUNCTION_MAP.put("json_contains", "");  // JSON_CONTAINS(json_doc, val) JSON文档是否在路径中包含特定对象
		SQL_FUNCTION_MAP.put("json_contains_path", "");  // JSON_CONTAINS_PATH(json_doc, path) JSON文档是否在路径中包含任何数据
		SQL_FUNCTION_MAP.put("json_depth", "");  // JSON_DEPTH(json_doc) JSON文档的最大深度
		SQL_FUNCTION_MAP.put("json_extract", "");  // JSON_EXTRACT(json_doc, path) 从JSON文档返回数据
		SQL_FUNCTION_MAP.put("json_insert", "");  // JSON_INSERT(json_doc, val) 将数据插入JSON文档
		SQL_FUNCTION_MAP.put("json_keys", "");  // JSON_KEYS(json_doc[, path]) JSON文档中的键数组
		SQL_FUNCTION_MAP.put("json_length", "");  // JSON_LENGTH(json_doc) JSON文档中的元素数
		SQL_FUNCTION_MAP.put("json_merge", "");  // JSON_MERGE(json_doc1, json_doc2) （已弃用） 合并JSON文档，保留重复的键。JSON_MERGE_PRESERVE（）的已弃用同义词
		SQL_FUNCTION_MAP.put("json_merge_patch", "");  // JSON_MERGE_PATCH(json_doc1, json_doc2) 合并JSON文档，替换重复键的值
		SQL_FUNCTION_MAP.put("json_merge_preserve", "");  // JSON_MERGE_PRESERVE(json_doc1, json_doc2) 合并JSON文档，保留重复的键
		SQL_FUNCTION_MAP.put("json_object", "");  // JSON_OBJECT(key1, val1, key2, val2...) 创建JSON对象
		SQL_FUNCTION_MAP.put("json_overlaps", "");  // JSON_OVERLAPS(json_doc1, json_doc2) （引入8.0.17） 比较两个JSON文档，如果它们具有相同的键值对或数组元素，则返回TRUE（1），否则返回FALSE（0）
		SQL_FUNCTION_MAP.put("json_pretty", "");  // JSON_PRETTY(json_doc) 以易于阅读的格式打印JSON文档
		SQL_FUNCTION_MAP.put("json_quote", "");  // JSON_QUOTE(json_doc1) 引用JSON文档
		SQL_FUNCTION_MAP.put("json_remove", "");  // JSON_REMOVE(json_doc1, path) 从JSON文档中删除数据
		SQL_FUNCTION_MAP.put("json_replace", "");  // JSON_REPLACE(json_doc1, val1, val2) 替换JSON文档中的值
		SQL_FUNCTION_MAP.put("json_schema_valid", "");  // JSON_SCHEMA_VALID(json_doc) （引入8.0.17） 根据JSON模式验证JSON文档；如果文档针对架构进行验证，则返回TRUE / 1；否则，则返回FALSE / 0
		SQL_FUNCTION_MAP.put("json_schema_validation_report", "");  // JSON_SCHEMA_VALIDATION_REPORT(json_doc, mode) （引入8.0.17） 根据JSON模式验证JSON文档；以JSON格式返回有关验证结果的报告，包括成功或失败以及失败原因
		SQL_FUNCTION_MAP.put("json_search", "");  // JSON_SEARCH(json_doc, val) JSON文档中值的路径
		SQL_FUNCTION_MAP.put("json_set", "");  // JSON_SET(json_doc, val) 将数据插入JSON文档
		//		SQL_FUNCTION_MAP.put("json_storage_free", "");  // JSON_STORAGE_FREE() 部分更新后，JSON列值的二进制表示形式中的可用空间
		//		SQL_FUNCTION_MAP.put("json_storage_size", "");  // JSON_STORAGE_SIZE() 用于存储JSON文档的二进制表示的空间
		SQL_FUNCTION_MAP.put("json_table", "");  // JSON_TABLE() 从JSON表达式返回数据作为关系表
		SQL_FUNCTION_MAP.put("json_type", "");  // JSON_TYPE(json_doc) JSON值类型
		SQL_FUNCTION_MAP.put("json_unquote", "");  // JSON_UNQUOTE(json_doc) 取消引用JSON值
		SQL_FUNCTION_MAP.put("json_valid", "");  // JSON_VALID(json_doc) JSON值是否有效
		SQL_FUNCTION_MAP.put("json_arrayagg", "");  // JSON_ARRAYAGG(key) 将每个表达式转换为 JSON 值，然后返回一个包含这些 JSON 值的 JSON 数组
		SQL_FUNCTION_MAP.put("json_objectagg", "");  // JSON_OBJECTAGG(key, val))  将每个表达式转换为 JSON 值，然后返回一个包含这些 JSON 值的 JSON 对象

		// MySQL 高级函数
		//		SQL_FUNCTION_MAP.put("bin", "");  // BIN(x)	返回 x 的二进制编码
		//		SQL_FUNCTION_MAP.put("binary", "");  // BINARY(s)	将字符串 s 转换为二进制字符串
		SQL_FUNCTION_MAP.put("case", "");  // CASE 表示函数开始，END 表示函数结束。如果 condition1 成立，则返回 result1, 如果 condition2 成立，则返回 result2，当全部不成立则返回 result，而当有一个成立之后，后面的就不执行了。
		SQL_FUNCTION_MAP.put("cast", "");  // CAST(x AS type)	转换数据类型
		SQL_FUNCTION_MAP.put("coalesce", "");  // COALESCE(expr1, expr2, ...., expr_n)	返回参数中的第一个非空表达式（从左向右）
		//		SQL_FUNCTION_MAP.put("conv", "");  // CONV(x,f1,f2)	返回 f1 进制数变成 f2 进制数
		//		SQL_FUNCTION_MAP.put("convert", "");  // CONVERT(s, cs)	函数将字符串 s 的字符集变成 cs
		SQL_FUNCTION_MAP.put("if", "");  // IF(expr,v1,v2)	如果表达式 expr 成立，返回结果 v1；否则，返回结果 v2。
		SQL_FUNCTION_MAP.put("ifnull", "");  // IFNULL(v1,v2)  如果 v1 的值不为 NULL，则返回 v1，否则返回 v2。
		SQL_FUNCTION_MAP.put("isnull", "");  // ISNULL(expression)	判断表达式是否为 NULL
		SQL_FUNCTION_MAP.put("nullif", "");  // NULLIF(expr1, expr2)  比较两个字符串，如果字符串 expr1 与 expr2 相等 返回 NULL，否则返回 expr1
		SQL_FUNCTION_MAP.put("group_concat", "");  // GROUP_CONCAT([DISTINCT], s1, s2...)  聚合拼接字符串
		SQL_FUNCTION_MAP.put("match", "");  // MATCH (name,tag) AGAINST ('a b' IN NATURAL LANGUAGE MODE)  全文检索





		//ClickHouse 字符串函数  注释的函数表示返回的格式暂时不支持，如：返回数组 ，同时包含因版本不同 clickhosue不支持的函数，版本
		SQL_FUNCTION_MAP.put("empty", "");  // empty(s) 对于空字符串s返回1，对于非空字符串返回0
		SQL_FUNCTION_MAP.put("notEmpty", "");  //notEmpty(s) 对于空字符串返回0，对于非空字符串返回1。
		SQL_FUNCTION_MAP.put("lengthUTF8", "");  //假定字符串以UTF-8编码组成的文本，返回此字符串的Unicode字符长度。如果传入的字符串不是UTF-8编码，则函数可能返回一个预期外的值
		SQL_FUNCTION_MAP.put("lcase", ""); //将字符串中的ASCII转换为小写
		SQL_FUNCTION_MAP.put("ucase", ""); //将字符串中的ASCII转换为大写。
		SQL_FUNCTION_MAP.put("lowerUTF8", "");  //将字符串转换为小写，函数假设字符串是以UTF-8编码文本的字符集。
		SQL_FUNCTION_MAP.put("upperUTF8", "");  //将字符串转换为大写，函数假设字符串是以UTF-8编码文本的字符集。
		SQL_FUNCTION_MAP.put("isValidUTF8", ""); // 检查字符串是否为有效的UTF-8编码，是则返回1，否则返回0。
		SQL_FUNCTION_MAP.put("toValidUTF8", "");//用�（U+FFFD）字符替换无效的UTF-8字符。所有连续的无效字符都会被替换为一个替换字符。
		SQL_FUNCTION_MAP.put("reverseUTF8", "");//以Unicode字符为单位反转UTF-8编码的字符串。
		SQL_FUNCTION_MAP.put("concatAssumeInjective", "");  // concatAssumeInjective(s1, s2, …) 与concat相同，区别在于，你需要保证concat(s1, s2, s3) -> s4是单射的，它将用于GROUP BY的优化。
		SQL_FUNCTION_MAP.put("substringUTF8", "");  // substringUTF8(s,offset,length)¶ 与’substring’相同，但其操作单位为Unicode字符，函数假设字符串是以UTF-8进行编码的文本。如果不是则可能返回一个预期外的结果（不会抛出异常）。
		SQL_FUNCTION_MAP.put("appendTrailingCharIfAbsent", ""); // appendTrailingCharIfAbsent(s,c) 如果’s’字符串非空并且末尾不包含’c’字符，则将’c’字符附加到末尾
		SQL_FUNCTION_MAP.put("convertCharset", ""); // convertCharset(s,from,to) 返回从’from’中的编码转换为’to’中的编码的字符串’s’。
		SQL_FUNCTION_MAP.put("base64Encode", ""); // base64Encode(s) 将字符串’s’编码成base64
		SQL_FUNCTION_MAP.put("base64Decode", ""); //base64Decode(s)  使用base64将字符串解码成原始字符串。如果失败则抛出异常。
		SQL_FUNCTION_MAP.put("tryBase64Decode", ""); //tryBase64Decode(s) 使用base64将字符串解码成原始字符串。但如果出现错误，将返回空字符串。
		SQL_FUNCTION_MAP.put("endsWith", ""); //endsWith(s,后缀) 返回是否以指定的后缀结尾。如果字符串以指定的后缀结束，则返回1，否则返回0。
		SQL_FUNCTION_MAP.put("startsWith", ""); //startsWith（s，前缀) 返回是否以指定的前缀开头。如果字符串以指定的前缀开头，则返回1，否则返回0。
		SQL_FUNCTION_MAP.put("trimLeft", ""); //trimLeft(s)返回一个字符串，用于删除左侧的空白字符。
		SQL_FUNCTION_MAP.put("trimRight", ""); //trimRight(s) 返回一个字符串，用于删除右侧的空白字符。
		SQL_FUNCTION_MAP.put("trimBoth", ""); //trimBoth(s)，用于删除任一侧的空白字符
		SQL_FUNCTION_MAP.put("extractAllGroups", ""); //extractAllGroups(text, regexp) 从正则表达式匹配的非重叠子字符串中提取所有组
		// SQL_FUNCTION_MAP.put("leftPad", "");  //leftPad('string', 'length'[, 'pad_string']) 用空格或指定的字符串从左边填充当前字符串(如果需要，可以多次)，直到得到的字符串达到给定的长度
		// SQL_FUNCTION_MAP.put("leftPadUTF8", ""); //leftPadUTF8('string','length'[, 'pad_string']) 用空格或指定的字符串从左边填充当前字符串(如果需要，可以多次)，直到得到的字符串达到给定的长度
		// SQL_FUNCTION_MAP.put("rightPad", ""); // rightPad('string', 'length'[, 'pad_string']) 用空格或指定的字符串(如果需要，可以多次)从右边填充当前字符串，直到得到的字符串达到给定的长度
		// SQL_FUNCTION_MAP.put("rightPadUTF8", "");// rightPadUTF8('string','length'[, 'pad_string'])  用空格或指定的字符串(如果需要，可以多次)从右边填充当前字符串，直到得到的字符串达到给定的长度。
		SQL_FUNCTION_MAP.put("normalizeQuery", ""); //normalizeQuery(x)  用占位符替换文字、文字序列和复杂的别名。
		SQL_FUNCTION_MAP.put("normalizedQueryHash", ""); //normalizedQueryHash(x) 为类似查询返回相同的64位散列值，但不包含文字值。有助于对查询日志进行分析
		SQL_FUNCTION_MAP.put("positionUTF8", ""); // positionUTF8(s, needle[, start_pos]) 返回在字符串中找到的子字符串的位置(以Unicode点表示)，从1开始。
		SQL_FUNCTION_MAP.put("multiSearchFirstIndex", ""); //multiSearchFirstIndex(s, [needle1, needle2, …, needlen]) 返回字符串s中最左边的needlei的索引i(从1开始)，否则返回0
		SQL_FUNCTION_MAP.put("multiSearchAny", ""); // multiSearchAny(s, [needle1, needle2, …, needlen])如果至少有一个字符串needlei匹配字符串s，则返回1，否则返回0。
		SQL_FUNCTION_MAP.put("match", ""); //match(s, pattern) 检查字符串是否与模式正则表达式匹配。re2正则表达式。re2正则表达式的语法比Perl正则表达式的语法更有局限性。
		SQL_FUNCTION_MAP.put("multiMatchAny", "");  //multiMatchAny(s, [pattern1, pattern2, …, patternn]) 与match相同，但是如果没有匹配的正则表达式返回0，如果有匹配的模式返回1
		SQL_FUNCTION_MAP.put("multiMatchAnyIndex", ""); //multiMatchAnyIndex(s, [pattern1, pattern2, …, patternn]) 与multiMatchAny相同，但返回与干堆匹配的任何索引
		SQL_FUNCTION_MAP.put("extract", ""); //  extract(s, pattern)  使用正则表达式提取字符串的片段
		SQL_FUNCTION_MAP.put("extractAll", ""); //extractAll(s, pattern) 使用正则表达式提取字符串的所有片段
		SQL_FUNCTION_MAP.put("like", ""); //like(s, pattern) 检查字符串是否与简单正则表达式匹配
		SQL_FUNCTION_MAP.put("notLike", "");// 和‘like’是一样的，但是是否定的
		SQL_FUNCTION_MAP.put("countSubstrings", ""); //countSubstrings(s, needle[, start_pos])返回子字符串出现的次数
		SQL_FUNCTION_MAP.put("countMatches", ""); //返回干s中的正则表达式匹配数。countMatches(s, pattern)
		SQL_FUNCTION_MAP.put("replaceOne", ""); //replaceOne(s, pattern, replacement)将' s '中的' pattern '子串的第一个出现替换为' replacement '子串。

		SQL_FUNCTION_MAP.put("replaceAll", ""); //replaceAll(s, pattern, replacement)/用' replacement '子串替换' s '中所有出现的' pattern '子串
		SQL_FUNCTION_MAP.put("replaceRegexpOne", ""); //replaceRegexpOne(s, pattern, replacement)使用' pattern '正则表达式进行替换
		SQL_FUNCTION_MAP.put("replaceRegexpAll", ""); //replaceRegexpAll(s, pattern, replacement)
		SQL_FUNCTION_MAP.put("regexpQuoteMeta", ""); //regexpQuoteMeta(s)该函数在字符串中某些预定义字符之前添加一个反斜杠

		//clickhouse日期函数
		SQL_FUNCTION_MAP.put("toYear", "");  //将Date或DateTime转换为包含年份编号（AD）的UInt16类型的数字。
		SQL_FUNCTION_MAP.put("toQuarter", ""); //将Date或DateTime转换为包含季度编号的UInt8类型的数字。
		SQL_FUNCTION_MAP.put("toMonth", ""); //Date或DateTime转换为包含月份编号（1-12）的UInt8类型的数字。
		SQL_FUNCTION_MAP.put("toDayOfYear", ""); //将Date或DateTime转换为包含一年中的某一天的编号的UInt16（1-366）类型的数字。
		SQL_FUNCTION_MAP.put("toDayOfMonth", "");//将Date或DateTime转换为包含一月中的某一天的编号的UInt8（1-31）类型的数字。
		SQL_FUNCTION_MAP.put("toDayOfWeek", ""); //将Date或DateTime转换为包含一周中的某一天的编号的UInt8（周一是1, 周日是7）类型的数字。
		SQL_FUNCTION_MAP.put("toHour", ""); //将DateTime转换为包含24小时制（0-23）小时数的UInt8数字。
		SQL_FUNCTION_MAP.put("toMinute", ""); //将DateTime转换为包含一小时中分钟数（0-59）的UInt8数字。
		SQL_FUNCTION_MAP.put("toSecond", ""); //将DateTime转换为包含一分钟中秒数（0-59）的UInt8数字。
		SQL_FUNCTION_MAP.put("toUnixTimestamp", ""); // 对于DateTime参数：将值转换为UInt32类型的数字-Unix时间戳
		SQL_FUNCTION_MAP.put("toStartOfYear", ""); //将Date或DateTime向前取整到本年的第一天。
		SQL_FUNCTION_MAP.put("toStartOfISOYear", "");  // 将Date或DateTime向前取整到ISO本年的第一天。
		SQL_FUNCTION_MAP.put("toStartOfQuarter", "");//将Date或DateTime向前取整到本季度的第一天。
		SQL_FUNCTION_MAP.put("toStartOfMonth", ""); //将Date或DateTime向前取整到本月的第一天。
		SQL_FUNCTION_MAP.put("toMonday", "");   //将Date或DateTime向前取整到本周的星期
		SQL_FUNCTION_MAP.put("toStartOfWeek", ""); //按mode将Date或DateTime向前取整到最近的星期日或星期一。
		SQL_FUNCTION_MAP.put("toStartOfDay", ""); //将DateTime向前取整到今天的开始。
		SQL_FUNCTION_MAP.put("toStartOfHour", ""); //将DateTime向前取整到当前小时的开始。
		SQL_FUNCTION_MAP.put("toStartOfMinute", ""); //将DateTime向前取整到当前分钟的开始。
		SQL_FUNCTION_MAP.put("toStartOfSecond", ""); //将DateTime向前取整到当前秒数的开始。
		SQL_FUNCTION_MAP.put("toStartOfFiveMinute", "");//将DateTime以五分钟为单位向前取整到最接近的时间点。
		SQL_FUNCTION_MAP.put("toStartOfTenMinutes", ""); //将DateTime以十分钟为单位向前取整到最接近的时间点。
		SQL_FUNCTION_MAP.put("toStartOfFifteenMinutes", ""); //将DateTime以十五分钟为单位向前取整到最接近的时间点。
		SQL_FUNCTION_MAP.put("toStartOfInterval", ""); //
		SQL_FUNCTION_MAP.put("toTime", ""); //将DateTime中的日期转换为一个固定的日期，同时保留时间部分。
		SQL_FUNCTION_MAP.put("toISOYear", ""); //将Date或DateTime转换为包含ISO年份的UInt16类型的编号。
		SQL_FUNCTION_MAP.put("toISOWeek", ""); //
		SQL_FUNCTION_MAP.put("toWeek", "");// 返回Date或DateTime的周数。
		SQL_FUNCTION_MAP.put("toYearWeek", ""); //返回年和周的日期
		SQL_FUNCTION_MAP.put("date_trunc", ""); //截断日期和时间数据到日期的指定部分
		SQL_FUNCTION_MAP.put("date_diff", ""); //回两个日期或带有时间值的日期之间的差值。

		SQL_FUNCTION_MAP.put("yesterday", ""); //不接受任何参数并在请求执行时的某一刻返回昨天的日期(Date)。
		SQL_FUNCTION_MAP.put("today", ""); //不接受任何参数并在请求执行时的某一刻返回当前日期(Date)。
		SQL_FUNCTION_MAP.put("timeSlot", ""); //将时间向前取整半小时。
		SQL_FUNCTION_MAP.put("toYYYYMM", ""); //
		SQL_FUNCTION_MAP.put("toYYYYMMDD", "");//
		SQL_FUNCTION_MAP.put("toYYYYMMDDhhmmss", ""); //
		SQL_FUNCTION_MAP.put("addYears", ""); // Function adds a Date/DateTime interval to a Date/DateTime and then return the Date/DateTime
		SQL_FUNCTION_MAP.put("addMonths", ""); //同上
		SQL_FUNCTION_MAP.put("addWeeks", ""); //同上
		SQL_FUNCTION_MAP.put("addDays", ""); //同上
		SQL_FUNCTION_MAP.put("addHours", ""); //同上
		SQL_FUNCTION_MAP.put("addMinutes", "");//同上
		SQL_FUNCTION_MAP.put("addSeconds", ""); //同上
		SQL_FUNCTION_MAP.put("addQuarters", ""); //同上
		SQL_FUNCTION_MAP.put("subtractYears", ""); //Function subtract a Date/DateTime interval to a Date/DateTime and then return the Date/DateTime
		SQL_FUNCTION_MAP.put("subtractMonths", ""); //同上
		SQL_FUNCTION_MAP.put("subtractWeeks", ""); //同上
		SQL_FUNCTION_MAP.put("subtractDays", ""); //同上
		SQL_FUNCTION_MAP.put("subtractours", "");//同上
		SQL_FUNCTION_MAP.put("subtractMinutes", ""); //同上
		SQL_FUNCTION_MAP.put("subtractSeconds", ""); //同上
		SQL_FUNCTION_MAP.put("subtractQuarters", ""); //同上
		SQL_FUNCTION_MAP.put("formatDateTime", ""); //函数根据给定的格式字符串来格式化时间
		SQL_FUNCTION_MAP.put("timestamp_add", ""); //使用提供的日期或日期时间值添加指定的时间值。
		SQL_FUNCTION_MAP.put("timestamp_sub", ""); //从提供的日期或带时间的日期中减去时间间隔。

		//ClickHouse json函数
		SQL_FUNCTION_MAP.put("visitParamHas", ""); //visitParamHas(params, name)检查是否存在«name»名称的字段
		SQL_FUNCTION_MAP.put("visitParamExtractUInt", ""); //visitParamExtractUInt(params, name)将名为«name»的字段的值解析成UInt64。
		SQL_FUNCTION_MAP.put("visitParamExtractInt", ""); //与visitParamExtractUInt相同，但返回Int64。
		SQL_FUNCTION_MAP.put("visitParamExtractFloat", ""); //与visitParamExtractUInt相同，但返回Float64。
		SQL_FUNCTION_MAP.put("visitParamExtractBool", "");//解析true/false值。其结果是UInt8类型的。
		SQL_FUNCTION_MAP.put("visitParamExtractRaw", ""); //返回字段的值，包含空格符。
		SQL_FUNCTION_MAP.put("visitParamExtractString", ""); //使用双引号解析字符串。这个值没有进行转义。如果转义失败，它将返回一个空白字符串。
		SQL_FUNCTION_MAP.put("JSONHas", ""); //如果JSON中存在该值，则返回1。
		SQL_FUNCTION_MAP.put("JSONLength", ""); //返回JSON数组或JSON对象的长度。
		SQL_FUNCTION_MAP.put("JSONType", ""); //返回JSON值的类型。
		SQL_FUNCTION_MAP.put("JSONExtractUInt", ""); //解析JSON并提取值。这些函数类似于visitParam*函数。
		SQL_FUNCTION_MAP.put("JSONExtractInt", ""); //
		SQL_FUNCTION_MAP.put("JSONExtractFloat", ""); //
		SQL_FUNCTION_MAP.put("JSONExtractBool", ""); //
		SQL_FUNCTION_MAP.put("JSONExtractString", ""); //解析JSON并提取字符串。此函数类似于visitParamExtractString函数。
		SQL_FUNCTION_MAP.put("JSONExtract", "");//解析JSON并提取给定ClickHouse数据类型的值。
		SQL_FUNCTION_MAP.put("JSONExtractKeysAndValues", ""); //从JSON中解析键值对，其中值是给定的ClickHouse数据类型
		SQL_FUNCTION_MAP.put("JSONExtractRaw", ""); //返回JSON的部分。
		SQL_FUNCTION_MAP.put("toJSONString", ""); //

		//ClickHouse 类型转换函数
		SQL_FUNCTION_MAP.put("toInt8", ""); //toInt8(expr)  转换一个输入值为Int类型
		SQL_FUNCTION_MAP.put("toInt16", "");
		SQL_FUNCTION_MAP.put("toInt32", "");
		SQL_FUNCTION_MAP.put("toInt64", "");
		SQL_FUNCTION_MAP.put("toInt8OrZero", ""); //toInt(8|16|32|64)OrZero 这个函数需要一个字符类型的入参，然后尝试把它转为Int (8 | 16 | 32 | 64)，如果转换失败直接返回0。
		SQL_FUNCTION_MAP.put("toInt16OrZero", "");
		SQL_FUNCTION_MAP.put("toInt32OrZero", "");
		SQL_FUNCTION_MAP.put("toInt64OrZero", "");
		SQL_FUNCTION_MAP.put("toInt8OrNull", "");//toInt(8|16|32|64)OrNull 这个函数需要一个字符类型的入参，然后尝试把它转为Int (8 | 16 | 32 | 64)，如果转换失败直接返回NULL
		SQL_FUNCTION_MAP.put("toInt16OrNull", "");
		SQL_FUNCTION_MAP.put("toInt32OrNull", "");
		SQL_FUNCTION_MAP.put("toInt64OrNull", "");
		SQL_FUNCTION_MAP.put("toUInt8", ""); //toInt8(expr)  转换一个输入值为Int类型
		SQL_FUNCTION_MAP.put("toUInt16", "");
		SQL_FUNCTION_MAP.put("toUInt32", "");
		SQL_FUNCTION_MAP.put("toUInt64", "");
		SQL_FUNCTION_MAP.put("toUInt8OrZero", ""); //toInt(8|16|32|64)OrZero 这个函数需要一个字符类型的入参，然后尝试把它转为Int (8 | 16 | 32 | 64)，如果转换失败直接返回0。
		SQL_FUNCTION_MAP.put("toUInt16OrZero", "");
		SQL_FUNCTION_MAP.put("toUInt32OrZero", "");
		SQL_FUNCTION_MAP.put("toUInt64OrZero", "");
		SQL_FUNCTION_MAP.put("toUInt8OrNull", "");//toInt(8|16|32|64)OrNull 这个函数需要一个字符类型的入参，然后尝试把它转为Int (8 | 16 | 32 | 64)，如果转换失败直接返回NULL
		SQL_FUNCTION_MAP.put("toUInt16OrNull", "");
		SQL_FUNCTION_MAP.put("toUInt32OrNull", "");
		SQL_FUNCTION_MAP.put("toUInt64OrNull", "");

		SQL_FUNCTION_MAP.put("toFloat32", "");
		SQL_FUNCTION_MAP.put("toFloat64", "");
		SQL_FUNCTION_MAP.put("toFloat32OrZero", "");
		SQL_FUNCTION_MAP.put("toFloat64OrZero", "");
		SQL_FUNCTION_MAP.put("toFloat32OrNull", "");
		SQL_FUNCTION_MAP.put("toFloat64OrNull", "");

		SQL_FUNCTION_MAP.put("toDate", ""); //
		SQL_FUNCTION_MAP.put("toDateOrZero", ""); //toInt16(expr)
		SQL_FUNCTION_MAP.put("toDateOrNull", ""); //toInt32(expr)
		SQL_FUNCTION_MAP.put("toDateTimeOrZero", ""); //toInt64(expr)
		SQL_FUNCTION_MAP.put("toDateTimeOrNull", ""); //toInt(8|16|32|64)OrZero 这个函数需要一个字符类型的入参，然后尝试把它转为Int (8 | 16 | 32 | 64)，如果转换失败直接返回0。

		SQL_FUNCTION_MAP.put("toDecimal32", "");
		SQL_FUNCTION_MAP.put("toFixedString", ""); // 将String类型的参数转换为FixedString(N)类型的值
		SQL_FUNCTION_MAP.put("toStringCutToZero", ""); //  接受String或FixedString参数,返回String，其内容在找到的第一个零字节处被截断。
		SQL_FUNCTION_MAP.put("toDecimal256", "");
		SQL_FUNCTION_MAP.put("toDecimal32OrNull", "");
		SQL_FUNCTION_MAP.put("toDecimal64OrNull", "");
		SQL_FUNCTION_MAP.put("toDecimal128OrNull", "");
		SQL_FUNCTION_MAP.put("toDecimal256OrNull", "");
		SQL_FUNCTION_MAP.put("toDecimal32OrZero", "");
		SQL_FUNCTION_MAP.put("toDecimal64OrZero", "");
		SQL_FUNCTION_MAP.put("toDecimal128OrZero", "");
		SQL_FUNCTION_MAP.put("toDecimal256OrZero", "");


		SQL_FUNCTION_MAP.put("toIntervalSecond", ""); //把一个数值类型的值转换为Interval类型的数据。
		SQL_FUNCTION_MAP.put("toIntervalMinute", "");
		SQL_FUNCTION_MAP.put("toIntervalHour", "");
		SQL_FUNCTION_MAP.put("toIntervalDay", "");
		SQL_FUNCTION_MAP.put("toIntervalWeek", "");
		SQL_FUNCTION_MAP.put("toIntervalMonth", "");
		SQL_FUNCTION_MAP.put("toIntervalQuarter", "");
		SQL_FUNCTION_MAP.put("toIntervalYear", "");
		SQL_FUNCTION_MAP.put("parseDateTimeBestEffort", ""); //把String类型的时间日期转换为DateTime数据类型。
		SQL_FUNCTION_MAP.put("parseDateTimeBestEffortOrNull", "");
		SQL_FUNCTION_MAP.put("parseDateTimeBestEffortOrZero", "");
		SQL_FUNCTION_MAP.put("toLowCardinality", "");



		////ClickHouse hash函数
		SQL_FUNCTION_MAP.put("halfMD5", ""); //计算字符串的MD5。然后获取结果的前8个字节并将它们作为UInt64（大端）返回
		SQL_FUNCTION_MAP.put("MD5", "");  //计算字符串的MD5并将结果放入FixedString(16)中返回

		//ClickHouse ip地址函数
		SQL_FUNCTION_MAP.put("IPv4NumToString", "");  //接受一个UInt32（大端）表示的IPv4的地址，返回相应IPv4的字符串表现形式，格式为A.B.C.D（以点分割的十进制数字）。
		SQL_FUNCTION_MAP.put("IPv4StringToNum", ""); //与IPv4NumToString函数相反。如果IPv4地址格式无效，则返回0。
		SQL_FUNCTION_MAP.put("IPv6NumToString", ""); //接受FixedString(16)类型的二进制格式的IPv6地址。以文本格式返回此地址的字符串。
		SQL_FUNCTION_MAP.put("IPv6StringToNum", "");  //与IPv6NumToString的相反。如果IPv6地址格式无效，则返回空字节字符串。
		SQL_FUNCTION_MAP.put("IPv4ToIPv6", ""); // 接受一个UInt32类型的IPv4地址，返回FixedString(16)类型的IPv6地址
		SQL_FUNCTION_MAP.put("cutIPv6", ""); //接受一个FixedString(16)类型的IPv6地址，返回一个String，这个String中包含了删除指定位之后的地址的文本格
		SQL_FUNCTION_MAP.put("toIPv4", "");  //IPv4StringToNum()的别名，
		SQL_FUNCTION_MAP.put("toIPv6", ""); //IPv6StringToNum()的别名
		SQL_FUNCTION_MAP.put("isIPAddressInRange", ""); //确定一个IP地址是否包含在以CIDR符号表示的网络中

		//ClickHouse Nullable处理函数
		SQL_FUNCTION_MAP.put("isNull", "");  //检查参数是否为NULL。
		SQL_FUNCTION_MAP.put("isNotNull", ""); //检查参数是否不为 NULL.
		SQL_FUNCTION_MAP.put("ifNull", ""); //如果第一个参数为«NULL»，则返回第二个参数的值。
		SQL_FUNCTION_MAP.put("assumeNotNull", ""); //将可为空类型的值转换为非Nullable类型的值。
		SQL_FUNCTION_MAP.put("toNullable", "");  //将参数的类型转换为Nullable。

		//ClickHouse UUID函数
		SQL_FUNCTION_MAP.put("generateUUIDv4", ""); // 生成一个UUID
		SQL_FUNCTION_MAP.put("toUUID", ""); //toUUID(x) 将String类型的值转换为UUID类型的值。

		//ClickHouse 系统函数
		SQL_FUNCTION_MAP.put("hostName", ""); //hostName()回一个字符串，其中包含执行此函数的主机的名称。
		SQL_FUNCTION_MAP.put("getMacro", ""); //从服务器配置的宏部分获取指定值。
		SQL_FUNCTION_MAP.put("FQDN", "");//返回完全限定的域名。
		SQL_FUNCTION_MAP.put("basename", ""); //提取字符串最后一个斜杠或反斜杠之后的尾随部分
		SQL_FUNCTION_MAP.put("currentUser", ""); //返回当前用户的登录。在分布式查询的情况下，将返回用户的登录，即发起的查询
		SQL_FUNCTION_MAP.put("version", ""); //以字符串形式返回服务器版本。
		SQL_FUNCTION_MAP.put("uptime", "");//以秒为单位返回服务器的正常运行时间。

		//ClickHouse 数学函数
		SQL_FUNCTION_MAP.put("least", ""); //返回a和b中最小的值。
		SQL_FUNCTION_MAP.put("greatest", ""); //返回a和b的最大值。
		SQL_FUNCTION_MAP.put("plus", "");  //plus(a, b), a + b operator¶计算数值的总和。
		SQL_FUNCTION_MAP.put("minus", ""); //minus(a, b), a - b operator 计算数值之间的差，结果总是有符号的。
		SQL_FUNCTION_MAP.put("multiply", "");//multiply(a, b), a * b operator 计算数值的乘积
		SQL_FUNCTION_MAP.put("divide", ""); //divide(a, b), a / b operator 计算数值的商。结果类型始终是浮点类型
		SQL_FUNCTION_MAP.put("intDiv", ""); //intDiv(a,b)计算数值的商，向下舍入取整（按绝对值）。
		SQL_FUNCTION_MAP.put("intDivOrZero", "");  //  intDivOrZero(a,b)与’intDiv’的不同之处在于它在除以零或将最小负数除以-1时返回零。
		SQL_FUNCTION_MAP.put("modulo", ""); //modulo(a, b), a % b operator 计算除法后的余数。
		SQL_FUNCTION_MAP.put("moduloOrZero", ""); //和modulo不同之处在于，除以0时结果返回0
		SQL_FUNCTION_MAP.put("negate", ""); //通过改变数值的符号位对数值取反，结果总是有符号
		SQL_FUNCTION_MAP.put("gcd", ""); //gcd(a,b) 返回数值的最大公约数。
		SQL_FUNCTION_MAP.put("lcm", ""); //lcm(a,b) 返回数值的最小公倍数
		SQL_FUNCTION_MAP.put("e", ""); //e() 返回一个接近数学常量e的Float64数字。
		SQL_FUNCTION_MAP.put("pi", ""); //pi() 返回一个接近数学常量π的Float64数字。
		SQL_FUNCTION_MAP.put("exp2", ""); //exp2(x)¶接受一个数值类型的参数并返回它的2的x次幂。
		SQL_FUNCTION_MAP.put("exp10", ""); //exp10(x)¶接受一个数值类型的参数并返回它的10的x次幂。
		SQL_FUNCTION_MAP.put("cbrt", ""); //cbrt(x) 接受一个数值类型的参数并返回它的立方根。
		SQL_FUNCTION_MAP.put("lgamma", ""); //lgamma(x) 返回x的绝对值的自然对数的伽玛函数。
		SQL_FUNCTION_MAP.put("tgamma", ""); //tgamma(x)¶返回x的伽玛函数。
		SQL_FUNCTION_MAP.put("intExp2", ""); //intExp2 接受一个数值类型的参数并返回它的2的x次幂（UInt64）
		SQL_FUNCTION_MAP.put("intExp10", ""); //intExp10 接受一个数值类型的参数并返回它的10的x次幂（UInt64）。
		SQL_FUNCTION_MAP.put("cosh", ""); // cosh(x)
		SQL_FUNCTION_MAP.put("cosh", ""); //cosh(x)
		SQL_FUNCTION_MAP.put("sinh", ""); //sinh(x)
		SQL_FUNCTION_MAP.put("asinh", ""); //asinh(x)
		SQL_FUNCTION_MAP.put("atanh", ""); //atanh(x)
		SQL_FUNCTION_MAP.put("atan2", ""); //atan2(y, x)
		SQL_FUNCTION_MAP.put("hypot", ""); //hypot(x, y)
		SQL_FUNCTION_MAP.put("log1p", ""); //log1p(x)
		SQL_FUNCTION_MAP.put("trunc", ""); //和truncate一样
		SQL_FUNCTION_MAP.put("roundToExp2", ""); //接受一个数字。如果数字小于1，它返回0。
		SQL_FUNCTION_MAP.put("roundDuration", ""); //接受一个数字。如果数字小于1，它返回0。
		SQL_FUNCTION_MAP.put("roundAge", ""); // 接受一个数字。如果数字小于18，它返回0。
		SQL_FUNCTION_MAP.put("roundDown", ""); //接受一个数字并将其舍入到指定数组中的一个元素
		SQL_FUNCTION_MAP.put("bitAnd", ""); //bitAnd(a,b)
		SQL_FUNCTION_MAP.put("bitOr", ""); //bitOr(a,b)

	}


	@Override
	public boolean limitSQLCount() {
		return Log.DEBUG == false || AbstractVerifier.SYSTEM_ACCESS_MAP.containsKey(getTable()) == false;
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
	private String role; //发送请求的用户的角色
	private boolean distinct = false;
	private String database; //表所在的数据库类型
	private String schema; //表所在的数据库名
	private String datasource; //数据源
	private String table; //表名
	private String alias; //表别名
	private String group; //分组方式的字符串数组，','分隔
	private String having; //聚合函数的字符串数组，','分隔
	private String order; //排序方式的字符串数组，','分隔
	private List<String> raw; //需要保留原始 SQL 的字段，','分隔
	private List<String> json; //需要转为 JSON 的字段，','分隔
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
	public String getRole() {
		//不能 @NotNull , AbstractParser#getSQLObject 内当getRole() == null时填充默认值
		return role;
	}
	@Override
	public AbstractSQLConfig setRole(String role) {
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
	public boolean isDb2() {
		return isDb2(getSQLDatabase());
	}
	public static boolean isDb2(String db) {
		return DATABASE_DB2.equals(db);
	}
	@Override
	public boolean isClickHouse() {
		return isClickHouse(getSQLDatabase());
	}
	public static boolean isClickHouse(String db) {
		return DATABASE_CLICKHOUSE.equals(db);
	}

	@Override
	public String getQuote() {
		return isMySQL()||isClickHouse() ? "`" : "\"";
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

	@Override
	public String getDatasource() {
		return datasource;
	}
	@Override
	public SQLConfig setDatasource(String datasource) {
		this.datasource = datasource;
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

				cfg = j.isLeftOrRightJoin() ? j.getOuterConfig() : j.getJoinConfig();
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
	/**TODO @having 改为默认 | 或连接，且支持 @having: { "key1>": 1, "key{}": "length(key2)>0", "@combine": "key1,key2" }
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

				cfg = j.isLeftOrRightJoin() ? j.getOuterConfig() : j.getJoinConfig();
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

		String[] keys = StringUtil.split(getHaving(), ";");
		if (keys == null || keys.length <= 0) {
			return StringUtil.isEmpty(joinHaving, true) ? "" : (hasPrefix ? " HAVING " : "") + joinHaving;
		}

		String quote = getQuote();
		String tableAlias = getAliasWithQuote();

		List<String> raw = getRaw();
		boolean containRaw = raw != null && raw.contains(KEY_HAVING);

		String expression;
		String method;
		//暂时不允许 String prefix;
		String suffix;

		//fun0(arg0,arg1,...);fun1(arg0,arg1,...)
		for (int i = 0; i < keys.length; i++) {

			//fun(arg0,arg1,...)
			expression = keys[i];
			if (containRaw) {
				try {
					String rawSQL = getRawSQL(KEY_HAVING, expression);
					if (rawSQL != null) {
						keys[i] = rawSQL;
						continue;
					}
				} catch (Exception e) {
					Log.e(TAG, "newSQLConfig  rawColumnSQL == null >> try {  "
							+ "  String rawSQL = ((AbstractSQLConfig) config).getRawSQL(KEY_COLUMN, fk); ... "
							+ "} catch (Exception e) = " + e.getMessage());
				}
			}

			if (expression.length() > 50) {
				throw new UnsupportedOperationException("@having:value 的 value 中字符串 " + expression + " 不合法！"
						+ "不允许传超过 50 个字符的函数或表达式！请用 @raw 简化传参！");
			}

			int start = expression.indexOf("(");
			if (start < 0) {
				if (isPrepared() && PATTERN_FUNCTION.matcher(expression).matches() == false) {
					throw new UnsupportedOperationException("字符串 " + expression + " 不合法！"
							+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
							+ " 中 column?value 必须符合正则表达式 " + PATTERN_FUNCTION + " 且不包含连续减号 -- ！不允许空格！");
				}
				continue;
			}

			int end = expression.lastIndexOf(")");
			if (start >= end) {
				throw new IllegalArgumentException("字符 " + expression + " 不合法！"
						+ "@having:value 中 value 里的 SQL函数必须为 function(arg0,arg1,...) 这种格式！");
			}

			method = expression.substring(0, start);
			if (method.isEmpty() == false) {
				if (SQL_FUNCTION_MAP == null || SQL_FUNCTION_MAP.isEmpty()) {
					if (StringUtil.isName(method) == false) {
						throw new IllegalArgumentException("字符 " + method + " 不合法！"
								+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
								+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！");
					}
				}
				else if (SQL_FUNCTION_MAP.containsKey(method) == false) {
					throw new IllegalArgumentException("字符 " + method + " 不合法！"
							+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
							+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！且必须是后端允许调用的 SQL 函数!");
				}
			}

			suffix = expression.substring(end + 1, expression.length());

			if (isPrepared() && (((String) suffix).contains("--") || ((String) suffix).contains("/*") || PATTERN_RANGE.matcher((String) suffix).matches() == false)) {
				throw new UnsupportedOperationException("字符串 " + suffix + " 不合法！"
						+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
						+ " 中 ?value 必须符合正则表达式 " + PATTERN_RANGE + " 且不包含连续减号 -- 或注释符 /* ！不允许多余的空格！");
			}

			String[] ckeys = StringUtil.split(expression.substring(start + 1, end));

			if (ckeys != null) {
				for (int j = 0; j < ckeys.length; j++) {
					String origin = ckeys[j];

					if (isPrepared()) {
						if (origin.startsWith("_") || origin.contains("--") || PATTERN_FUNCTION.matcher(origin).matches() == false) {
							throw new IllegalArgumentException("字符 " + ckeys[j] + " 不合法！"
									+ "预编译模式下 @having:\"column?value;function(arg0,arg1,...)?value...\""
									+ " 中所有 column, arg 都必须是1个不以 _ 开头的单词 或者 符合正则表达式 " + PATTERN_FUNCTION + " 且不包含连续减号 -- ！不允许多余的空格！");
						}
					}

					//JOIN 副表不再在外层加副表名前缀 userId AS `Commet.userId`， 而是直接 userId AS `userId`
					boolean isName = false;
					if (StringUtil.isNumer(origin)) {
						//do nothing
					}
					else if (StringUtil.isName(origin)) {
						origin = quote + origin + quote;
						isName = true;
					} 
					else {
						origin = getValue(origin).toString();
					}

					ckeys[j] = (isName && isKeyPrefix() ? tableAlias + "." : "") + origin;
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

				cfg = j.isLeftOrRightJoin() ? j.getOuterConfig() : j.getJoinConfig();
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
		// SELECT * FROM sys.Moment ORDER BY userId ASC, rand();   前面的 userId ASC 和后面的 rand() 都有效
		//		if ("rand()".equals(order)) {
		//			return (hasPrefix ? " ORDER BY " : "") + StringUtil.concat(order, joinOrder, ", ");
		//		}

		if (getCount() > 0 && (isSQLServer() || isDb2())) { // Oracle, SQL Server, DB2 的 OFFSET 必须加 ORDER BY.去掉Oracle，Oracle里面没有offset关键字

			//			String[] ss = StringUtil.split(order);
			if (StringUtil.isEmpty(order, true)) {  //SQL Server 子查询内必须指定 OFFSET 才能用 ORDER BY
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


		String[] keys = StringUtil.split(order);
		if (keys == null || keys.length <= 0) {
			return StringUtil.isEmpty(joinOrder, true) ? "" : (hasPrefix ? " ORDER BY " : "") + joinOrder;
		}

		for (int i = 0; i < keys.length; i++) {
			String item = keys[i];
			if ("rand()".equals(item)) {
				continue;
			}

			int index = item.endsWith("+") ? item.length() - 1 : -1; //StringUtil.split返回数组中，子项不会有null
			String sort;
			if (index < 0) {
				index = item.endsWith("-") ? item.length() - 1 : -1;
				sort = index <= 0 ? "" : " DESC ";
			}
			else {
				sort = " ASC ";
			}

			String origin = index < 0 ? item : item.substring(0, index);

			if (isPrepared()) { //不能通过 ? 来代替，SELECT 'id','name' 返回的就是 id:"id", name:"name"，而不是数据库里的值！
				//这里既不对origin trim，也不对 ASC/DESC ignoreCase，希望前端严格传没有任何空格的字符串过来，减少传输数据量，节约服务器性能
				if (StringUtil.isName(origin) == false) {
					throw new IllegalArgumentException("预编译模式下 @order:value 中 " + item + " 不合法! value 里面用 , 分割的"
							+ "每一项必须是 随机函数 rand() 或 column+ / column- 且其中 column 必须是 1 个单词！并且不要有多余的空格！");
				}
			}

			keys[i] = getKey(origin) + sort;
		}

		return (hasPrefix ? " ORDER BY " : "") + StringUtil.concat(StringUtil.getString(keys), joinOrder, ", ");
	}

	@Override
	public List<String> getRaw() {
		return raw;
	}
	@Override
	public SQLConfig setRaw(List<String> raw) {
		this.raw = raw;
		return this;
	}

	/**获取原始 SQL 片段
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getRawSQL(String key, Object value) throws Exception {
		List<String> rawList = getRaw();
		boolean containRaw = rawList != null && rawList.contains(key);
		if (containRaw && value instanceof String == false) {
			throw new UnsupportedOperationException("@raw:value 的 value 中 " + key + " 不合法！"
					+ "对应的 " + key + ":value 中 value 类型只能为 String！");
		}

		String rawSQL = containRaw ? RAW_MAP.get(value) : null;
		if (containRaw) {
			if (rawSQL == null) {
				throw new UnsupportedOperationException("@raw:value 的 value 中 " + key + " 不合法！"
						+ "对应的 " + key + ":value 中 value 值 " + value + " 未在后端 RAW_MAP 中配置 ！");
			}

			if (rawSQL.isEmpty()) {
				return (String) value;
			}
		}

		return rawSQL;
	}


	@Override
	public List<String> getJson() {
		return json;
	}
	@Override
	public AbstractSQLConfig setJson(List<String> json) {
		this.json = json;
		return this;
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
		List<String> column = getColumn();

		switch (getMethod()) {
		case HEAD:
		case HEADS: //StringUtil.isEmpty(column, true) || column.contains(",") 时SQL.count(column)会return "*"
			if (isPrepared() && column != null) {

				List<String> raw = getRaw();
				boolean containRaw = raw != null && raw.contains(KEY_COLUMN);

				String origin;
				String alias;
				int index;

				for (String c : column) {
					if (containRaw) {
						// 由于 HashMap 对 key 做了 hash 处理，所以 get 比 containsValue 更快
						if ("".equals(RAW_MAP.get(c)) || RAW_MAP.containsValue(c)) {  // newSQLConfig 提前处理好的
							//排除@raw中的值，以避免使用date_format(date,'%Y-%m-%d %H:%i:%s') 时,冒号的解析出错
							//column.remove(c);
							continue;
						}
					}

					index = c.lastIndexOf(":"); //StringUtil.split返回数组中，子项不会有null
					origin = index < 0 ? c : c.substring(0, index);
					alias = index < 0 ? null : c.substring(index + 1);

					if (alias != null && StringUtil.isName(alias) == false) {
						throw new IllegalArgumentException("HEAD请求: 字符 " + alias + " 不合法！预编译模式下 @column:value 中 value里面用 , 分割的每一项"
								+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！并且不要有多余的空格！");
					}

					if (StringUtil.isName(origin) == false) {
						int start = origin.indexOf("(");
						if (start < 0 || origin.lastIndexOf(")") <= start) { 
							throw new IllegalArgumentException("HEAD请求: 字符" + origin + " 不合法！预编译模式下 @column:value 中 value里面用 , 分割的每一项"
									+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！并且不要有多余的空格！");
						}

						if (start > 0 && StringUtil.isName(origin.substring(0, start)) == false) {
							throw new IllegalArgumentException("HEAD请求: 字符 " + origin.substring(0, start) + " 不合法！预编译模式下 @column:value 中 value里面用 , 分割的每一项"
									+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！并且不要有多余的空格！");
						}
					}
				}
			}

			return SQL.count(column != null && column.size() == 1 && StringUtil.isName(column.get(0)) ? getKey(column.get(0)) : "*");
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

					ecfg = j.getOuterConfig();
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


			List<String> raw = getRaw();
			boolean containRaw = raw != null && raw.contains(KEY_COLUMN);

			//...;fun0(arg0,arg1,...):fun0;fun1(arg0,arg1,...):fun1;...
			for (int i = 0; i < keys.length; i++) {
				String expression = keys[i];  //fun(arg0,arg1,...)

				if (containRaw) {  // 由于 HashMap 对 key 做了 hash 处理，所以 get 比 containsValue 更快
					if ("".equals(RAW_MAP.get(expression)) || RAW_MAP.containsValue(expression)) {  // newSQLConfig 提前处理好的
						continue;
					}

					// 简单点， 后台配置就带上 AS
					//					int index = expression.lastIndexOf(":");
					//					String alias = expression.substring(index+1);
					//					boolean hasAlias = StringUtil.isName(alias);
					//					String pre = index > 0 && hasAlias ? expression.substring(0, index) : expression;
					//					if (RAW_MAP.containsValue(pre) || "".equals(RAW_MAP.get(pre))) {  // newSQLConfig 提前处理好的
					//						expression = pre + (hasAlias ? " AS " + alias : "");
					//						continue;
					//					}
				}

				if (expression.length() > 100) {
					throw new UnsupportedOperationException("@column:value 的 value 中字符串 " + expression + " 不合法！"
							+ "不允许传超过 100 个字符的函数或表达式！请用 @raw 简化传参！");
				}
				keys[i] = getColumnPrase(expression, containRaw);
			}

			String c = StringUtil.getString(keys);
			c = c + (StringUtil.isEmpty(joinColumn, true) ? "" : ", " + joinColumn);//不能在这里改，后续还要用到:
			return isMain() && isDistinct() ? PREFFIX_DISTINCT + c : c;
		default:
			throw new UnsupportedOperationException(
					"服务器内部错误：getColumnString 不支持 " + RequestMethod.getName(getMethod())
					+ " 等 [GET,GETS,HEAD,HEADS,POST] 外的ReuqestMethod！"
					);
		}
	}

	/**
	 * 解析@column 中以“;”分隔的表达式（"@column":"expression1;expression2;expression2;...."）中的expression
	 *
	 * @param expression
	 * @return
	 */
	public String getColumnPrase(String expression, boolean containRaw) {
		String quote = getQuote();
		int start = expression.indexOf('(');
		if (start < 0) {
			//没有函数 ,可能是字段,也可能是 DISTINCT xx
			String[] cks = parseArgsSplitWithComma(expression, true, containRaw);
			expression = StringUtil.getString(cks);
		} else {  // FIXME 用括号断开？ 如果少的话，用关键词加括号断开，例如  )OVER( 和 )AGAINST(
			// 窗口函数 rank() OVER (PARTITION BY id ORDER BY userId ASC)
			// 全文索引 math(name,tag) AGAINST ('a b +c -d' IN NATURALE LANGUAGE MODE)  // IN BOOLEAN MODE

			//有函数,但不是窗口函数
			int overIndex = expression.indexOf(")OVER(");  // 传参不传空格，拼接带空格  ") OVER (");
			int againstIndex = expression.indexOf(")AGAINST(");  // 传参不传空格，拼接带空格  ") AGAINST (");
			boolean containOver = overIndex > 0 && overIndex < expression.length() - ")OVER(".length();
			boolean containAgainst = againstIndex > 0 && againstIndex < expression.length() - ")AGAINST(".length();

			if (containOver && containAgainst) {
				throw new IllegalArgumentException("字符 " + expression + " 不合法！"
						+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
						+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！不能同时存在窗口函数关键词 OVER 和全文索引关键词 AGAINST！");
			}

			if (containOver == false && containAgainst == false) {
				int end = expression.lastIndexOf(")");
				if (start >= end) {
					throw new IllegalArgumentException("字符 " + expression + " 不合法！"
							+ "@column:value 中 value 里的 SQL函数必须为 function(arg0,arg1,...) 这种格式！");
				}
				String fun = expression.substring(0, start);
				if (fun.isEmpty() == false) {
					if (SQL_FUNCTION_MAP == null || SQL_FUNCTION_MAP.isEmpty()) {
						if (StringUtil.isName(fun) == false) {
							throw new IllegalArgumentException("字符 " + fun + " 不合法！"
									+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
									+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！");
						}
					} else if (SQL_FUNCTION_MAP.containsKey(fun) == false) {
						throw new IllegalArgumentException("字符 " + fun + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！且必须是后端允许调用的 SQL 函数!");
					}
				}

				String s = expression.substring(start + 1, end);
				boolean distinct = s.startsWith(PREFFIX_DISTINCT);
				if (distinct) {
					s = s.substring(PREFFIX_DISTINCT.length());
				}

				// 解析函数内的参数
				String ckeys[] = parseArgsSplitWithComma(s, false, containRaw);

				String suffix = expression.substring(end + 1, expression.length()); //:contactCount
				int index = suffix.lastIndexOf(":");
				String alias = index < 0 ? "" : suffix.substring(index + 1); //contactCount
				suffix = index < 0 ? suffix : suffix.substring(0, index);
				if (alias.isEmpty() == false && StringUtil.isName(alias) == false) {
					throw new IllegalArgumentException("字符串 " + alias + " 不合法！"
							+ "预编译模式下 @column:value 中 value里面用 ; 分割的每一项"
							+ " function(arg0,arg1,...):alias 中 alias 必须是1个单词！并且不要有多余的空格！");
				}

				if (suffix.isEmpty() == false && (((String) suffix).contains("--") || ((String) suffix).contains("/*") || PATTERN_RANGE.matcher((String) suffix).matches() == false)) {
					throw new UnsupportedOperationException("字符串 " + suffix + " 不合法！"
							+ "预编译模式下 @column:\"column?value;function(arg0,arg1,...)?value...\""
							+ " 中 ?value 必须符合正则表达式 " + PATTERN_RANGE + " 且不包含连续减号 -- 或注释符 /* ！不允许多余的空格！");
				}

				String origin = fun + "(" + (distinct ? PREFFIX_DISTINCT : "") + StringUtil.getString(ckeys) + ")" + suffix;
				expression = origin + (StringUtil.isEmpty(alias, true) ? "" : " AS " + quote + alias + quote);

			} else {
				//是窗口函数   fun(arg0,agr1) OVER (agr0 agr1 ...)
				int keyIndex = containOver ? overIndex : againstIndex;
				String s1 = expression.substring(0, keyIndex + 1); // OVER 前半部分
				String s2 = expression.substring(keyIndex + 1); // OVER 后半部分

				int index1 = s1.indexOf("("); //  函数 "(" 的起始位置
				String fun = s1.substring(0, index1); // 函数名称
				int end = s2.lastIndexOf(")"); // 后半部分 “)” 的位置

				if (index1 >= end) {
					throw new IllegalArgumentException("字符 " + expression + " 不合法！"
							+ "@column:value 中 value 里的 SQL函数必须为 function(arg0,arg1,...) 这种格式！");
				}
				if (fun.isEmpty() == false) {
					if (SQL_FUNCTION_MAP == null || SQL_FUNCTION_MAP.isEmpty()) {
						if (StringUtil.isName(fun) == false) {
							throw new IllegalArgumentException("字符 " + fun + " 不合法！"
									+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
									+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！");
						}
					} else if (SQL_FUNCTION_MAP.containsKey(fun) == false) {
						throw new IllegalArgumentException("字符 " + fun + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中 function 必须符合小写英文单词的 SQL 函数名格式！且必须是后端允许调用的 SQL 函数!");
					}
				}

				// 获取前半部分函数的参数解析   fun(arg0,agr1)
				String agrsString1[] = parseArgsSplitWithComma(s1.substring(index1 + 1, s1.lastIndexOf(")")), false, containRaw);

				int index2 = s2.indexOf("("); // 后半部分 “(”的起始位置
				String argString2 = s2.substring(index2 + 1, end); // 后半部分的参数
				// 别名
				String alias = s2.lastIndexOf(":") < s2.lastIndexOf(")") ? null : s2.substring(s2.lastIndexOf(":") + 1);
				// 获取后半部分的参数解析 (agr0 agr1 ...)
				String argsString2[] = parseArgsSplitWithComma(argString2, false, containRaw); 
				expression = fun + "(" + StringUtil.getString(agrsString1) + (containOver ? ") OVER (" : ") AGAINST (") // 传参不传空格，拼接带空格
						+ StringUtil.getString(argsString2) + ")" + (StringUtil.isEmpty(alias, true) ? "" : " AS " + quote + alias + quote);			}
		}

		return expression;
	}

	/**
	 * 解析函数参数或者字段,此函数对于解析字段 和 函数内参数通用
	 *
	 * @param param
	 * @param isColumn true:不是函数参数。false:是函数参数
	 * @return
	 */
	private String[] parseArgsSplitWithComma(String param, boolean isColumn, boolean containRaw) {
		// 以"," 分割参数
		String quote = getQuote();
		String tableAlias = getAliasWithQuote();
		String ckeys[] = StringUtil.split(param); // 以","分割参数
		if (ckeys != null && ckeys.length > 0) {
			String origin;
			String alias;
			int index;
			for (int i = 0; i < ckeys.length; i++) {
				String ck = ckeys[i];

				// 如果参数包含 "'" ,解析字符串
				if (ck.startsWith("`") && ck.endsWith("`")) {
					origin = ck.substring(1, ck.length() - 1);
					//sql 注入判断 判断
					if (StringUtil.isName(origin) == false) {
						throw new IllegalArgumentException("字符 " + ck + " 不合法！"
								+ "预编译模式下 @column:\"`column0`,`column1`:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中所有字符串 column 都必须必须为1个单词 ！");
					}

					ckeys[i] = getKey(origin).toString();
				}
				else if (ck.startsWith("'") && ck.endsWith("'")) {
					origin = ck.substring(1, ck.length() - 1);
					if (origin.contains("'")) {
						throw new IllegalArgumentException("字符串 " + ck + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中字符串参数不合法，必须以 ' 开头, ' 结尾,字符串中不能包含 ' ");
					}
					//sql 注入判断 判断
					if (origin.contains("--") || PATTERN_STRING.matcher(origin).matches() == true) {
						throw new IllegalArgumentException("字符 " + ck + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中所有字符串 arg 都必须不符合正则表达式 " + PATTERN_STRING + " 且不包含连续减号 -- ！");
					}
					
					// 1.字符串不是字段也没有别名,所以不解析别名 2. 是字符串，进行预编译，使用getValue() ,对字符串进行截取
					ckeys[i] = getValue(origin).toString();
				}
				else {
					// 参数不包含",",即不是字符串
					// 解析参数:1. 字段 ,2. 是以空格分隔的参数 eg: cast(now() as date)
					index = isColumn ? ck.lastIndexOf(":") : -1; //StringUtil.split返回数组中，子项不会有null
					origin = index < 0 ? ck : ck.substring(0, index); //获取 : 之前的
					alias = index < 0 ? null : ck.substring(index + 1);
					if (isPrepared()) {
						if (isColumn) {
							if (StringUtil.isName(origin) == false || (alias != null && StringUtil.isName(alias) == false)) {
								throw new IllegalArgumentException("字符 " + ck + " 不合法！"
										+ "预编译模式下 @column:value 中 value里面用 , 分割的每一项"
										+ " column:alias 中 column 必须是1个单词！如果有alias，则alias也必须为1个单词！"
										+ "关键字必须全大写，且以空格分隔的参数，空格必须只有 1 个！其它情况不允许空格！");
							}
						} else {
							if (origin.startsWith("_") || origin.contains("--") || PATTERN_FUNCTION.matcher(origin).matches() == false) {
								throw new IllegalArgumentException("字符 " + ck + " 不合法！"
										+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
										+ " 中所有 arg 都必须是1个不以 _ 开头的单词 或者符合正则表达式 " + PATTERN_FUNCTION + " 且不包含连续减号 -- ！DISTINCT 必须全大写，且后面必须有且只有 1 个空格！其它情况不允许空格！");
							}
						}
					}
					// 以空格分割参数
					String[] mkes = containRaw ? StringUtil.split(ck, " ", true) : new String[]{ ck };

					//如果参数中含有空格(少数情况) 比如  fun(arg1 arg2 arg3 ,arg4) 中的 arg1 arg2 arg3，比如 DISTINCT id
					if (mkes != null && mkes.length >= 2) {
						ckeys[i] = praseArgsSplitWithSpace(mkes);
					} else {
						boolean isName = false;

						String mk = RAW_MAP.get(origin);
						if (mk != null) {  // newSQLConfig 提前处理好的
							if (mk.length() > 0) {
								origin = mk;
							}
						} else if (StringUtil.isNumer(origin)) {
							//do nothing
						} else if (StringUtil.isName(origin)) {
							origin = quote + origin + quote;
							isName = true;
						} else {
							origin = getValue(origin).toString();
						}

						if (isName && isKeyPrefix()) {
							origin = tableAlias + "." + origin;
						}

						if (isColumn && StringUtil.isEmpty(alias, true) == false) {
							origin += " AS " + quote + alias + quote;
						}

						ckeys[i] = origin;
					}

				}
			}
		}
		return ckeys;
	}


	/**
	 * 只解析以空格分隔的参数
	 *
	 * @param mkes
	 * @return
	 */
	private String praseArgsSplitWithSpace(String mkes[]) {
		String quote = getQuote();
		String tableAlias = getAliasWithQuote();

		// 包含空格的参数  肯定不包含别名 不用处理别名
		if (mkes != null && mkes.length > 0) {
			for (int j = 0; j < mkes.length; j++) {
				// now()/AS/ DISTINCT/VALUE 等等放在RAW_MAP中
				String origin = mkes[j];

				String mk = RAW_MAP.get(origin);
				if (mk != null) {  // newSQLConfig 提前处理好的
					if (mk.length() > 0) {
						mkes[j] = mk;
					}
					continue;
				}

				//这里为什么还要做一次判断 是因为解析窗口函数调用的时候会判断一次
				if (isPrepared()) {
					if (origin.startsWith("_") || origin.contains("--") || PATTERN_FUNCTION.matcher(origin).matches() == false) {
						throw new IllegalArgumentException("字符 " + origin + " 不合法！"
								+ "预编译模式下 @column:\"column0,column1:alias;function0(arg0,arg1,...);function1(...):alias...\""
								+ " 中所有 arg 都必须是1个不以 _ 开头的单词 或者符合正则表达式 " + PATTERN_FUNCTION + " 且不包含连续减号 -- ！DISTINCT 必须全大写，且后面必须有且只有 1 个空格！其它情况不允许空格！");
					}
				}

				boolean isName = false;
				if (StringUtil.isNumer(origin)) {
					//do nothing
				} else if (StringUtil.isName(origin)) {
					origin = quote + origin + quote;
					isName = true;
				} else {
					origin = getValue(origin).toString();
				}

				if (isName && isKeyPrefix()) {
					origin = tableAlias + "." + origin;
				}

				mkes[j] = origin;
			}
		}
		// 返回重新以" "拼接后的参数
		return StringUtil.join(mkes, " ");
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
		return setCache(getCache(cache));
	}
	public static int getCache(String cache) {
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
				throw new IllegalArgumentException(JSONRequest.KEY_CACHE + ":value 中 value 的值不合法！必须在 [0,1,2] 或 [ALL, ROM, RAM] 内 !");
			}
		}
		return cache2;
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
		return getLimitString(getPage(), getCount(), isOracle() || isSQLServer() || isDb2(), isOracle());
	}
	/**获取限制数量
	 * @param limit
	 * @return
	 */
	public static String getLimitString(int page, int count, boolean isTSQL, boolean isOracle) {
		int offset = getOffset(page, count);

		if (isTSQL) {  // OFFSET FECTH 中所有关键词都不可省略, 另外 Oracle 数据库使用子查询加 where 分页
			return isOracle ? " WHERE ROWNUM BETWEEN "+ offset +" AND "+ (offset + count) : " OFFSET " + offset + " ROWS FETCH FIRST " + count + " ROWS ONLY";
		}

		return " LIMIT " + count + (offset <= 0 ? "" : " OFFSET " + offset);  // DELETE, UPDATE 不支持 OFFSET
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
	//CS304 Issue link: https://github.com/Tencent/APIJSON/issues/48
	/**
	 * @param key - the key passed in
	 * @param exactMatch - whether it is exact match
	 * @return
	 * <p>use entrySet+getValue() to replace keySet+get() to enhance efficiency</p>
	 */
	@JSONField(serialize = false)
	@Override
	public Object getWhere(String key, boolean exactMatch) {
		if (exactMatch) {
			return where == null ? null : where.get(key);
		}

		if (key == null || where == null){
			return null;
		}
		synchronized (where) {
			if (where != null) {
				int index;
				for (Entry<String,Object> entry : where.entrySet()) {
					String k = entry.getKey();
					index = k.indexOf(key);
					if (index >= 0 && StringUtil.isName(k.substring(index)) == false) {
						return entry.getValue();
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
				if (andList != null) {
					andList.remove(key);
				}
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

			if (StringUtil.isEmpty(cs, true)) {//避免SQL条件连接错误
				continue;
			}

			whereString += (isCombineFirst ? "" : AND) + (Logic.isNot(logic) ? NOT : "") + " (  " + cs + "  ) ";
			isCombineFirst = false;
		}


		if (joinList != null) {

			String newWs = "";
			String ws = whereString;

			List<Object> newPvl = new ArrayList<>();
			List<Object> pvl = new ArrayList<>(preparedValueList);

			SQLConfig jc;
			String js;

			boolean changed = false;
			//各种 JOIN 没办法统一用 & | ！连接，只能按优先级，和 @combine 一样?
			for (Join j : joinList) {
				String jt = j.getJoinType();

				switch (jt) {
				case "*": // CROSS JOIN
				case "@": // APP JOIN
				case "<": // LEFT JOIN
				case ">": // RIGHT JOIN
					break;

				case "&": // INNER JOIN: A & B 
				case "":  // FULL JOIN: A | B 
				case "|": // FULL JOIN: A | B 
				case "!": // OUTER JOIN: ! (A | B)
				case "^": // SIDE JOIN: ! (A & B)
				case "(": // ANTI JOIN: A & ! B
				case ")": // FOREIGN JOIN: B & ! A
					jc = j.getJoinConfig();
					boolean isMain = jc.isMain();
					jc.setMain(false).setPrepared(isPrepared()).setPreparedValueList(new ArrayList<Object>());
					js = jc.getWhereString(false);
					jc.setMain(isMain);

					boolean isOuterJoin = "!".equals(jt);
					boolean isSideJoin = "^".equals(jt);
					boolean isAntiJoin = "(".equals(jt);
					boolean isForeignJoin = ")".equals(jt);
					boolean isWsEmpty = StringUtil.isEmpty(ws, true);

					if (isWsEmpty) {
						if (isOuterJoin) { // ! OUTER JOIN: ! (A | B)
							throw new NotExistException("no result for ! OUTER JOIN( ! (A | B) ) when A or B is empty!");
						}
						if (isForeignJoin) { // ) FOREIGN JOIN: B & ! A
							throw new NotExistException("no result for ) FOREIGN JOIN( B & ! A ) when A is empty!");
						}
					}

					if (StringUtil.isEmpty(js, true)) {
						if (isOuterJoin) { // ! OUTER JOIN: ! (A | B)
							throw new NotExistException("no result for ! OUTER JOIN( ! (A | B) ) when A or B is empty!");
						}
						if (isAntiJoin) { // ( ANTI JOIN: A & ! B
							throw new NotExistException("no result for ( ANTI JOIN( A & ! B ) when B is empty!");
						}

						if (isWsEmpty) {
							if (isSideJoin) {
								throw new NotExistException("no result for ^ SIDE JOIN( ! (A & B) ) when both A and B are empty!");
							}
						}
						else {
							if (isSideJoin || isForeignJoin) {
								newWs += " ( " + getCondition(true, ws) + " ) ";

								newPvl.addAll(pvl);
								newPvl.addAll(jc.getPreparedValueList());
								changed = true;
							}
						}

						continue;
					}

					if (StringUtil.isEmpty(newWs, true) == false) {
						newWs += AND;
					}

					if (isAntiJoin) { // ( ANTI JOIN: A & ! B  
						newWs += " ( " + ( isWsEmpty ? "" : ws + AND ) + NOT + " ( " + js + " ) " + " ) ";
					}
					else if (isForeignJoin) { // ) FOREIGN JOIN: (! A) & B  // preparedValueList.add 不好反过来  B & ! A
						newWs += " ( " + NOT + " ( " + ws + " ) ) " + AND + " ( " + js + " ) ";
					}
					else if (isSideJoin) { // ^ SIDE JOIN:  ! (A & B)
						//MySQL 因为 NULL 值处理问题，(A & ! B) | (B & ! A) 与 ! (A & B) 返回结果不一样，后者往往更多
						newWs += " ( " + getCondition(
								true, 
								( isWsEmpty ? "" : ws + AND ) + " ( " + js + " ) "
								) + " ) ";
					}
					else {  // & INNER JOIN: A & B; | FULL JOIN: A | B; OUTER JOIN: ! (A | B)
						logic = Logic.getType(jt);
						newWs += " ( "
								+ getCondition(
										Logic.isNot(logic), 
										ws
										+ ( isWsEmpty ? "" : (Logic.isAnd(logic) ? AND : OR) )
										+ " ( " + js + " ) "
										)
								+ " ) ";
					}

					newPvl.addAll(pvl);
					newPvl.addAll(jc.getPreparedValueList());

					changed = true;
					break;
				default:
					throw new UnsupportedOperationException(
							"join:value 中 value 里的 " + jt + "/" + j.getPath()
							+ "错误！不支持 " + jt + " 等 [ @ APP, < LEFT, > RIGHT, * CROSS"
							+ ", & INNER, | FULL, ! OUTER, ^ SIDE, ( ANTI, ) FOREIGN ] 之外的 JOIN 类型 !"
							);
				}
			}

			if (changed) {
				whereString = newWs;
				preparedValueList = newPvl;
			}
		}

		String s = StringUtil.isEmpty(whereString, true) ? "" : (hasPrefix ? " WHERE " : "") + whereString;

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
	protected String getWhereItem(String key, Object value, RequestMethod method, boolean verifyName) throws Exception {
		Log.d(TAG, "getWhereItem  key = " + key);
		//避免筛选到全部	value = key == null ? null : where.get(key);
		if (key == null || value == null || key.endsWith("()") || key.startsWith("@")) { //关键字||方法, +或-直接报错
			Log.d(TAG, "getWhereItem  key == null || value == null"
					+ " || key.startsWith(@) || key.endsWith(()) >> continue;");
			return null;
		}
		if (key.endsWith("@")) {//引用
			//	key = key.substring(0, key.lastIndexOf("@"));
			throw new IllegalArgumentException(TAG + ".getWhereItem: 字符 " + key + " 不合法！");
		}

		// 原始 SQL 片段
		String rawSQL = getRawSQL(key, value);

		int keyType;
		if (key.endsWith("$")) {
			keyType = 1;
		} 
		else if (key.endsWith("~")) {
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
		} else {  // else绝对不能省，避免再次踩坑！ keyType = 0; 写在for循环外面都没注意！
			keyType = 0;
		}

		key = getRealKey(method, key, false, true, verifyName);

		switch (keyType) {
		case 1:
			return getSearchString(key, value, rawSQL);
		case -2:
		case 2:
			return getRegExpString(key, value, keyType < 0, rawSQL);
		case 3:
			return getBetweenString(key, value, rawSQL);
		case 4:
			return getRangeString(key, value, rawSQL);
		case 5:
			return getExistsString(key, value, rawSQL);
		case 6:
			return getContainString(key, value, rawSQL);
		case 7:
			return getCompareString(key, value, ">=", rawSQL);
		case 8:
			return getCompareString(key, value, "<=", rawSQL);
		case 9:
			return getCompareString(key, value, ">", rawSQL);
		case 10:
			return getCompareString(key, value, "<", rawSQL);
		default:  // TODO MySQL JSON类型的字段对比 key='[]' 会无结果！ key LIKE '[1, 2, 3]'  //TODO MySQL , 后面有空格！
			return getEqualString(key, value, rawSQL);
		}
	}


	@JSONField(serialize = false)
	public String getEqualString(String key, Object value, String rawSQL) throws Exception {
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

		return getKey(key) + (not ? " != " : " = ") + (value instanceof Subquery ? getSubqueryString((Subquery) value) : (rawSQL != null ? rawSQL : getValue(value)));
	}

	@JSONField(serialize = false)
	public String getCompareString(String key, Object value, String type, String rawSQL) throws Exception {
		if (JSON.isBooleanOrNumberOrString(value) == false && value instanceof Subquery == false) {
			throw new IllegalArgumentException(key + type + ":value 中value不合法！比较运算 [>, <, >=, <=] 只支持 [Boolean, Number, String] 内的类型 ！");
		}
		if (StringUtil.isName(key) == false) {
			throw new IllegalArgumentException(key + type + ":value 中key不合法！比较运算 [>, <, >=, <=] 不支持 [&, !, |] 中任何逻辑运算符 ！");
		}

		return getKey(key) + " " + type + " " + (value instanceof Subquery ? getSubqueryString((Subquery) value) : (rawSQL != null ? rawSQL : getValue(value)));
	}

	public String getKey(String key) {
		if (isTest()) {
			if (key.contains("'")) {  // || key.contains("#") || key.contains("--")) {
				throw new IllegalArgumentException("参数 " + key + " 不合法！key 中不允许有单引号 ' ！");
			}
			return getSQLValue(key).toString();
		}

		return getSQLKey(key);
	}
	public String getSQLKey(String key) {
		String q = getQuote();
		return (isKeyPrefix() ? getAliasWithQuote() + "." : "") + q + key + q;
	}

	/**
	 * 使用prepareStatement预编译，值为 ? ，后续动态set进去
	 */
	protected List<Object> preparedValueList = new ArrayList<>();
	protected Object getValue(@NotNull Object value) {
		if (isPrepared()) {
			preparedValueList.add(value);
			return "?";
		}
		return getSQLValue(value);
	}
	public Object getSQLValue(@NotNull Object value) {
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
	public String getSearchString(String key, Object value, String rawSQL) throws IllegalArgumentException {
		if (rawSQL != null) {
			throw new UnsupportedOperationException("@raw:value 中 " + key + " 不合法！@raw 不支持 key$ 这种功能符 ！只支持 key, key!, key<, key{} 等比较运算 和 @column, @having ！");
		}
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
			Object v = values[i];
			if (v instanceof String == false) {
				throw new IllegalArgumentException(key + "$:value 中 value 的类型只能为 String 或 String[]！");
			}
			if (((String) v).isEmpty()) {  // 允许查空格 StringUtil.isEmpty((String) v, true)
				throw new IllegalArgumentException(key + "$:value 中 value 值 " + v + "是空字符串，没有意义，不允许这样传！");
			}
			//			if (((String) v).contains("%%")) {  // 需要通过 %\%% 来模糊搜索 %
			//				throw new IllegalArgumentException(key + "$:value 中 value 值 " + v + " 中包含 %% ！不允许有连续的 % ！");
			//			}

			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + getLikeString(key, v);
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
	public String getRegExpString(String key, Object value, boolean ignoreCase, String rawSQL) throws IllegalArgumentException {
		if (rawSQL != null) {
			throw new UnsupportedOperationException("@raw:value 中 " + key + " 不合法！@raw 不支持 key~ 这种功能符 ！只支持 key, key!, key<, key{} 等比较运算 和 @column, @having ！");
		}
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
		if (isOracle()) {
			return "regexp_like(" + getKey(key) + ", " + getValue(value) + (ignoreCase ? ", 'i'" : ", 'c'") + ")";
		}
		if (isClickHouse()) {
			return "match(" + (ignoreCase ? "lower(" : "") + getKey(key) + (ignoreCase ? ")" : "") + ", " + (ignoreCase ? "lower(" : "") + getValue(value) + (ignoreCase ? ")" : "") + ")";
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
	public String getBetweenString(String key, Object value, String rawSQL) throws IllegalArgumentException {
		if (rawSQL != null) {
			throw new UnsupportedOperationException("@raw:value 中 " + key + " 不合法！@raw 不支持 key% 这种功能符 ！只支持 key, key!, key<, key{} 等比较运算 和 @column, @having ！");
		}
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

			condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR)) + "(" + getBetweenString(key, (Object) vs[0], (Object) vs[1]) + ")";
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


	/**WHERE key > 'key0' AND key <= 'key1' AND ...
	 * @param key
	 * @param range "condition0,condition1..."
	 * @return key condition0 AND key condition1 AND ...
	 * @throws Exception 
	 */
	@JSONField(serialize = false)
	public String getRangeString(String key, Object range, String rawSQL) throws Exception {
		Log.i(TAG, "getRangeString key = " + key);
		if (range == null) {//依赖的对象都没有给出有效值，这个存在无意义。如果是客户端传的，那就能在客户端确定了。
			throw new NotExistException(TAG + "getRangeString(" + key + ", " + range
					+ ") range == null");
		}

		Logic logic = new Logic(key);
		String k = logic.getKey();
		Log.i(TAG, "getRangeString k = " + k);

		if (range instanceof List) {
			if (rawSQL != null) {
				throw new UnsupportedOperationException("@raw:value 的 value 中 " + key + "{} 不合法！"
						+ "Raw SQL 不支持 key{}:[] 这种键值对！");
			}

			if (logic.isOr() || logic.isNot()) {
				List<?> l = (List<?>) range;
				if (logic.isNot() && l.isEmpty()) {
					return ""; // key!{}: [] 这个条件无效，加到 SQL 语句中 key IN() 会报错，getInString 里不好处理
				}
				return getKey(k) + getInString(k, l.toArray(), logic.isNot());
			}
			throw new IllegalArgumentException(key + "{}\":[] 中 {} 前面的逻辑运算符错误！只能用'|','!'中的一种 ！");
		}
		else if (range instanceof String) {//非Number类型需要客户端拼接成 < 'value0', >= 'value1'这种
			String condition = "";
			String[] cs = rawSQL != null ? null : StringUtil.split((String) range, false);

			if (rawSQL != null) {
				int index = rawSQL == null ? -1 : rawSQL.indexOf("(");
				condition = (index >= 0 && index < rawSQL.lastIndexOf(")") ? "" : getKey(k) + " ") + rawSQL;
			}

			// 还是只支持整段为 Raw SQL 比较好
			//			boolean appendRaw = false;
			//			if ("".equals(rawSQL)) {
			//				condition = rawSQL;
			//				cs = null;
			//			}
			//			else {
			//				if (rawSQL != null) { //先找出所有 rawSQL 的位置，然后去掉，再最后按原位置来拼接
			//					String[] rs = StringUtil.split((String) range, rawSQL, false);
			//
			//					if (rs != null && rs.length > 0) {
			//						String cond = "";
			//						for (int i = 0; i < rs.length; i++) {
			//							cond += rs[i];
			//						}
			//						range = cond;
			//						appendRaw = true;
			//					}
			//				}
			//
			//				cs = StringUtil.split((String) range, false);
			//			}

			if (cs != null) {
				String c;
				int index;
				for (int i = 0; i < cs.length; i++) {//对函数条件length(key)<=5这种不再在开头加key
					c = cs[i];

					if ("=null".equals(c)) {
						c = SQL.isNull();
					}
					else if ("!=null".equals(c)) {
						c = SQL.isNull(false);
					}
					else if (isPrepared() && (c.contains("--") || PATTERN_RANGE.matcher(c).matches() == false)) {
						throw new UnsupportedOperationException(key + "{}:value 的 value 中 " + c + " 不合法！"
								+ "预编译模式下 key{}:\"condition\" 中 condition 必须 为 =null 或 !=null 或 符合正则表达式 " + PATTERN_RANGE + " ！不允许连续减号 -- ！不允许空格！");
					}

					index = c == null ? -1 : c.indexOf("(");
					condition += ((i <= 0 ? "" : (logic.isAnd() ? AND : OR)) //连接方式
							+ (index >= 0 && index < c.lastIndexOf(")") ? "" : getKey(k) + " ") //函数和非函数条件
							+ c);  // 还是只支持整段为 Raw SQL 比较好  (appendRaw && index > 0 ? rawSQL : "") + c); //单个条件，如果有 Raw SQL 则按原来位置拼接
				}
			}
			if (condition.isEmpty()) {
				return "";
			}

			return getCondition(logic.isNot(), condition);
		}
		else if (range instanceof Subquery) { //如果在 Parser 解析成 SQL 字符串再引用，没法保证安全性，毕竟可以再通过远程函数等方式来拼接再替代，最后引用的字符串就能注入
			return getKey(k) + (logic.isNot() ? NOT : "") + " IN " + getSubqueryString((Subquery) range);
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
	public String getExistsString(String key, Object value, String rawSQL) throws Exception {
		if (rawSQL != null) {
			throw new UnsupportedOperationException("@raw:value 中 " + key + " 不合法！@raw 不支持 key}{ 这种功能符 ！只支持 key, key!, key<, key{} 等比较运算 和 @column, @having ！");
		}
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
	public String getContainString(String key, Object value, String rawSQL) throws IllegalArgumentException {
		if (rawSQL != null) {
			throw new UnsupportedOperationException("@raw:value 中 " + key + " 不合法！@raw 不支持 key<> 这种功能符 ！只支持 key, key!, key<, key{} 等比较运算 和 @column, @having ！");
		}
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
				Object c = childs[i];
				if (c != null) {
					if (c instanceof JSON) {
						throw new IllegalArgumentException(key + "<>:value 中value类型不能为JSON！");
					}

					condition += (i <= 0 ? "" : (Logic.isAnd(type) ? AND : OR));
					if (isPostgreSQL()) {
						condition += (getKey(key) + " @> " + getValue(newJSONArray(c))); //operator does not exist: jsonb @> character varying  "[" + c + "]"); 
					}
					else if (isOracle()) {
						condition += ("json_textcontains(" + getKey(key) + ", '$', " + getValue(c.toString()) + ")");
					}
					else {
						boolean isNum = c instanceof Number;
						String v = (isNum ? "" : "\"") + childs[i] + (isNum ? "" : "\"");
						if (isClickHouse()) {
							condition += condition + "has(JSONExtractArrayRaw(assumeNotNull(" + getKey(key) + "))" + ", " + getValue(v) + ")";
						}
						else {
							condition += ("json_contains(" + getKey(key) + ", " +  getValue(v) + ")");
						}
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
	//CS304 Issue link: https://github.com/Tencent/APIJSON/issues/48
	/**获取SET
	 * @param method -the method used
	 * @param content -the content map
	 * @return
	 * @throws Exception
	 * <p>use entrySet+getValue() to replace keySet+get() to enhance efficiency</p>
	 */
	@JSONField(serialize = false)
	public String getSetString(RequestMethod method, Map<String, Object> content, boolean verifyName) throws Exception {
		Set<String> set = content == null ? null : content.keySet();
		String setString = "";

		if (set != null && set.size() > 0) {
			boolean isFirst = true;
			int keyType;// 0 - =; 1 - +, 2 - -
			Object value;

			String idKey = getIdKey();
			for (Entry<String,Object> entry : content.entrySet()) {
				String key = entry.getKey();
				//避免筛选到全部	value = key == null ? null : content.get(key);
				if (key == null || idKey.equals(key)) {
					continue;
				}

				if (key.endsWith("+")) {
					keyType = 1;
				} else if (key.endsWith("-")) {
					keyType = 2;
				} else {
					keyType = 0; //注意重置类型，不然不该加减的字段会跟着加减
				}
				value = entry.getValue();
				key = getRealKey(method, key, false, true, verifyName);

				setString += (isFirst ? "" : ", ") + (getKey(key) + " = " + (keyType == 1 ? getAddString(key, value) : (keyType == 2
						? getRemoveString(key, value) : getValue(value)) ) );

				isFirst = false;
			}
		}

		if (setString.isEmpty()) {
			throw new IllegalArgumentException("PUT 请求必须在Table内设置要修改的 key:value ！");
		}
		return (isClickHouse()?" ":" SET ") + setString;
	}

	/**SET key = concat(key, 'value')
	 * @param key
	 * @param value
	 * @return concat(key, 'value')
	 * @throws IllegalArgumentException
	 */
	@JSONField(serialize = false)
	public String getAddString(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			return getKey(key) + " + " + value;
		}
		if (value instanceof String) {
			return SQL.concat(getKey(key), (String) getValue(value));
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
			return SQL.replace(getKey(key), (String) getValue(value), "''");// " replace(" + key + ", '" + value + "', '') ";
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
			if(config.isClickHouse()){
				return  "ALTER TABLE " +  tablePath + " UPDATE" + config.getSetString() + config.getWhereString(true);
			}
			return "UPDATE " + tablePath + config.getSetString() + config.getWhereString(true) + (config.isMySQL() ? config.getLimitString() : "");
		case DELETE:
			if(config.isClickHouse()){
				return  "ALTER TABLE " +  tablePath + " DELETE" + config.getWhereString(true);
			}
			return "DELETE FROM " + tablePath + config.getWhereString(true) + (config.isMySQL() ? config.getLimitString() : "");  // PostgreSQL 不允许 LIMIT
		default:
			String explain = (config.isExplain() ? (config.isSQLServer() || config.isOracle() ? "SET STATISTICS PROFILE ON  " : "EXPLAIN ") : "");
			if (config.isTest() && RequestMethod.isGetMethod(config.getMethod(), true)) {  // FIXME 为啥是 code 而不是 count ？
				String q = config.getQuote();  // 生成 SELECT  (  (24 >=0 AND 24 <3)  )  AS `code` LIMIT 1 OFFSET 0
				return explain + "SELECT " + config.getWhereString(false) + " AS " + q + JSONResponse.KEY_COUNT + q + config.getLimitString();
			}

			config.setPreparedValueList(new ArrayList<Object>());
			String column = config.getColumnString();
			if (config.isOracle()) {
				//When config's database is oracle,Using subquery since Oracle12 below does not support OFFSET FETCH paging syntax.
				//针对oracle分组后条数的统计
				if ((config.getMethod() == HEAD || config.getMethod() == HEADS)
						&& StringUtil.isNotEmpty(config.getGroup(),true)){
					return explain + "SELECT count(*) FROM (SELECT " + (config.getCache() == JSONRequest.CACHE_RAM ? "SQL_NO_CACHE " : "") + column + " FROM " + getConditionString(column, tablePath, config) + ") " + config.getLimitString();
				}
				return explain + "SELECT * FROM (SELECT " + (config.getCache() == JSONRequest.CACHE_RAM ? "SQL_NO_CACHE " : "") + column + " FROM " + getConditionString(column, tablePath, config) + ") " + config.getLimitString();
			}

			return explain + "SELECT " + (config.getCache() == JSONRequest.CACHE_RAM ? "SQL_NO_CACHE " : "") + column + " FROM " + getConditionString(column, tablePath, config) + config.getLimitString();
		}
	}

	/**获取条件SQL字符串
	 * @param column
	 * @param table
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	private static String getConditionString(String column, String table, AbstractSQLConfig config) throws Exception {
		String where = config.getWhereString(true);

		Subquery from = config.getFrom();
		if (from != null) {
			table = config.getSubqueryString(from) + " AS " + config.getAliasWithQuote() + " ";
		}

		//根据方法不同，聚合语句不同。GROUP  BY 和 HAVING 可以加在 HEAD 上, HAVING 可以加在 PUT, DELETE 上，GET 全加，POST 全都不加
		String aggregation = "";
		if (RequestMethod.isGetMethod(config.getMethod(), true)){
			aggregation = config.getGroupString(true) + config.getHavingString(true) +
					config.getOrderString(true);
		}
		if (RequestMethod.isHeadMethod(config.getMethod(), true)){
			aggregation = config.getGroupString(true) + config.getHavingString(true) ;
		}
		if (config.getMethod() == PUT || config.getMethod() == DELETE){
			aggregation = config.getHavingString(true) ;
		}

		String condition = table + config.getJoinString() + where + aggregation;
		; //+ config.getLimitString();

		//no need to optimize
		//		if (config.getPage() <= 0 || ID.equals(column.trim())) {
		return condition;  // config.isOracle() ? condition : condition + config.getLimitString();
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
			String tt;
			//  主表不用别名			String ta;
			for (Join j : joinList) {
				if (j.isAppJoin()) { // APP JOIN，只是作为一个标记，执行完主表的查询后自动执行副表的查询 User.id IN($commentIdList)
					continue;
				}
				String type = j.getJoinType();

				//LEFT JOIN sys.apijson_user AS User ON User.id = Moment.userId， 都是用 = ，通过relateType处理缓存
				// <"INNER JOIN User ON User.id = Moment.userId", UserConfig>  TODO  AS 放 getSQLTable 内
				jc = j.getJoinConfig();
				jc.setPrepared(isPrepared());

				jt = StringUtil.isEmpty(jc.getAlias(), true) ? jc.getTable() : jc.getAlias();
				tt = j.getTargetTable();

				//如果要强制小写，则可在子类重写这个方法再 toLowerCase
				//				if (DATABASE_POSTGRESQL.equals(getDatabase())) {
				//					jt = jt.toLowerCase();
				//					tn = tn.toLowerCase();
				//				}

				switch (type) {
				//前面已跳过				case "@": // APP JOIN
				//					continue;

				case "*": // CROSS JOIN
					onGetCrossJoinString(j);
				case "<": // LEFT JOIN
				case ">": // RIGHT JOIN
					jc.setMain(true).setKeyPrefix(false);
					sql = ( "<".equals(type) ? " LEFT" : (">".equals(type) ? " RIGHT" : " CROSS") )
							+ " JOIN ( " + jc.getSQL(isPrepared()) + " ) AS "
							+ quote + jt + quote + " ON " + quote + jt + quote + "." + quote + j.getKey() + quote + " = "
							+ quote + tt + quote + "." + quote + j.getTargetKey() + quote;
					jc.setMain(false).setKeyPrefix(true);

					pvl.addAll(jc.getPreparedValueList());
					changed = true;
					break;

				case "&": // INNER JOIN: A & B 
				case "":  // FULL JOIN: A | B 
				case "|": // FULL JOIN: A | B 
				case "!": // OUTER JOIN: ! (A | B)
				case "^": // SIDE JOIN: ! (A & B)
				case "(": // ANTI JOIN: A & ! B
				case ")": // FOREIGN JOIN: B & ! A
					sql = " INNER JOIN " + jc.getTablePath()
					+ " ON " + quote + jt + quote + "." + quote + j.getKey() + quote + " = " + quote + tt + quote + "." + quote + j.getTargetKey() + quote;
					break;
				default:
					throw new UnsupportedOperationException(
							"join:value 中 value 里的 " + jt + "/" + j.getPath()
							+ "错误！不支持 " + jt + " 等 [ @ APP, < LEFT, > RIGHT, * CROSS"
							+ ", & INNER, | FULL, ! OUTER, ^ SIDE, ( ANTI, ) FOREIGN ] 之外的 JOIN 类型 !"
							);
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

	protected void onGetCrossJoinString(Join j) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("已禁用 * CROSS JOIN ！性能很差、需求极少，如要取消禁用可在后端重写相关方法！");
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
	public static SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure, Callback callback) throws Exception {
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
		String datasource = request.getString(KEY_DATASOURCE);

		SQLConfig config = callback.getSQLConfig(method, database, schema, table);
		config.setAlias(alias);

		config.setDatabase(database); //不删，后面表对象还要用的，必须放在 parseJoin 前
		config.setSchema(schema); //不删，后面表对象还要用的
		config.setDatasource(datasource); //不删，后面表对象还要用的

		if (isProcedure) {
			return config;
		}

		config = parseJoin(method, config, joinList, callback); //放后面会导致主表是空对象时 joinList 未解析

		if (request.isEmpty()) { // User:{} 这种空内容在查询时也有效
			return config; //request.remove(key); 前都可以直接return，之后必须保证 put 回去
		}


		String idKey = callback.getIdKey(datasource, database, schema, table);
		String idInKey = idKey + "{}";
		String userIdKey = callback.getUserIdKey(datasource, database, schema, table);
		String userIdInKey = userIdKey + "{}";

		//对id和id{}处理，这两个一定会作为条件

		Object idIn = request.get(idInKey); //可能是 id{}:">0"
		if (idIn instanceof List) { // 排除掉 0, 负数, 空字符串 等无效 id 值
			List<?> ids = ((List<?>) idIn);
			List<Object> newIdIn = new ArrayList<>();
			Object d;
			for (int i = 0; i < ids.size(); i++) { //不用 idIn.contains(id) 因为 idIn 里存到很可能是 Integer，id 又是 Long！
				d = ids.get(i);
				if ((d instanceof Number && ((Number) d).longValue() > 0) || (d instanceof String && StringUtil.isNotEmpty(d, true))) {
					newIdIn.add(d);
				}
			}
			if (newIdIn.isEmpty()) {
				throw new NotExistException(TAG + ": newSQLConfig idIn instanceof List >> 去掉无效 id 后 newIdIn.isEmpty()");
			}
			idIn = newIdIn;

			if (method == DELETE || method == PUT) {
				config.setCount(newIdIn.size());
			}
		}

		Object id = request.get(idKey);
		boolean hasId = id != null;
		if (method == POST && hasId == false) {
			id = callback.newId(method, database, schema, table); // null 表示数据库自增 id
		}

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

			if (method == DELETE || method == PUT) {
				config.setCount(1);
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
		String raw = request.getString(KEY_RAW);
		String json = request.getString(KEY_JSON);

		try {
			//强制作为条件且放在最前面优化性能
			request.remove(idKey);
			request.remove(idInKey);
			//关键词
			request.remove(KEY_ROLE);
			request.remove(KEY_EXPLAIN);
			request.remove(KEY_CACHE);
			request.remove(KEY_DATASOURCE);
			request.remove(KEY_DATABASE);
			request.remove(KEY_SCHEMA);
			request.remove(KEY_COMBINE);
			request.remove(KEY_FROM);
			request.remove(KEY_COLUMN);
			request.remove(KEY_GROUP);
			request.remove(KEY_HAVING);
			request.remove(KEY_ORDER);
			request.remove(KEY_RAW);
			request.remove(KEY_JSON);

			String[] rawArr = StringUtil.split(raw);
			config.setRaw(rawArr == null || rawArr.length <= 0 ? null : new ArrayList<>(Arrays.asList(rawArr)));

			Map<String, Object> tableWhere = new LinkedHashMap<String, Object>();//保证顺序好优化 WHERE id > 1 AND name LIKE...

			//已经remove了id和id{}，以及@key
			Set<String> set = request.keySet(); //前面已经判断request是否为空
			if (method == POST) { //POST操作
				if (idIn != null) {
					throw new IllegalArgumentException("POST 请求中不允许传 " + idInKey + " !");
				}

				if (set != null && set.isEmpty() == false) { //不能直接return，要走完下面的流程
					String[] columns = set.toArray(new String[]{});

					Collection<Object> valueCollection = request.values();
					Object[] values = valueCollection == null ? null : valueCollection.toArray();

					if (values == null || values.length != columns.length) {
						throw new Exception("服务器内部错误:\n" + TAG
								+ " newSQLConfig  values == null || values.length != columns.length !");
					}
					column = (id == null ? "" : idKey + ",") + StringUtil.getString(columns); //set已经判断过不为空

					List<List<Object>> valuess = new ArrayList<>(1);
					List<Object> items; //(item0, item1, ...)
					if (id == null) { //数据库自增 id
						items = Arrays.asList(values); //FIXME 是否还需要进行 add 或 remove 操作？Arrays.ArrayList 不允许修改，会抛异常
					}
					else {
						int size = columns.length + (id == null ? 0 : 1); //以key数量为准

						items = new ArrayList<>(size);
						items.add(id); //idList.get(i)); //第0个就是id

						for (int j = 1; j < size; j++) {
							items.add(values[j-1]); //从第1个开始，允许"null"
						}
					}

					valuess.add(items);
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
						throw new IllegalArgumentException("DELETE,GETS,HEADS 请求不允许传 @combine:value !");
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

						// 可重写回调方法自定义处理 // 动态设置的场景似乎很少，而且去掉后不方便用户排错！//去掉判断，有时候不在没关系，如果是对增删改等非开放请求强制要求传对应的条件，可以用 Operation.NECESSARY
						if (request.containsKey(w) == false) {  //和 request.get(w) == null 没区别，前面 Parser 已经过滤了 null
							//	throw new IllegalArgumentException(table + ":{} 里的 @combine:value 中的value里 " + ws[i] + " 对应的 " + w + " 不在它里面！");
							callback.onMissingKey4Combine(table, request, combine, ws[i], w);
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
					if (isWhere || (StringUtil.isName(key.replaceFirst("[+-]$", "")) == false)) {
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


			List<String> cs = new ArrayList<>();

			List<String> rawList = config.getRaw();
			boolean containColumnRaw = rawList != null && rawList.contains(KEY_COLUMN);

			String rawColumnSQL = null;
			if (containColumnRaw) {
				try {
					rawColumnSQL = config.getRawSQL(KEY_COLUMN, column);
					if (rawColumnSQL != null) {
						cs.add(rawColumnSQL);
					}
				} catch (Exception e) {
					Log.e(TAG, "newSQLConfig  config instanceof AbstractSQLConfig >> try {  "
							+ "  rawColumnSQL = ((AbstractSQLConfig) config).getRawSQL(KEY_COLUMN, column); "
							+ "} catch (Exception e) = " + e.getMessage());
				}
			}

			boolean distinct = column == null || rawColumnSQL != null ? false : column.startsWith(PREFFIX_DISTINCT);
			if (rawColumnSQL == null) {
				String[] fks = StringUtil.split(distinct ? column.substring(PREFFIX_DISTINCT.length()) : column, ";"); // key0,key1;fun0(key0,...);fun1(key0,...);key3;fun2(key0,...)
				if (fks != null) {
					String[] ks;
					for (String fk : fks) {
						if (containColumnRaw) {
							try {
								String rawSQL = config.getRawSQL(KEY_COLUMN, fk);
								if (rawSQL != null) {
									cs.add(rawSQL);
									continue;
								}
							} catch (Exception e) {
								Log.e(TAG, "newSQLConfig  rawColumnSQL == null >> try {  "
										+ "  String rawSQL = ((AbstractSQLConfig) config).getRawSQL(KEY_COLUMN, fk); ... "
										+ "} catch (Exception e) = " + e.getMessage());
							}
						}

						if (fk.contains("(")) {  // fun0(key0,...)
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
			}

			config.setExplain(explain);
			config.setCache(getCache(cache));
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

			String[] jsonArr = StringUtil.split(json);
			config.setJson(jsonArr == null || jsonArr.length <= 0 ? null : new ArrayList<>(Arrays.asList(jsonArr)));

			//TODO 解析JOIN，包括 @column，@group 等要合并

		}
		finally {//后面还可能用到，要还原
			//id或id{}条件
			if (hasId) {
				request.put(idKey, id);
			}
			request.put(idInKey, idIn);
			//关键词
			request.put(KEY_DATABASE, database);
			request.put(KEY_ROLE, role);
			request.put(KEY_EXPLAIN, explain);
			request.put(KEY_CACHE, cache);
			request.put(KEY_DATASOURCE, datasource);
			request.put(KEY_SCHEMA, schema);
			request.put(KEY_COMBINE, combine);
			request.put(KEY_FROM, from);
			request.put(KEY_COLUMN, column);
			request.put(KEY_GROUP, group);
			request.put(KEY_HAVING, having);
			request.put(KEY_ORDER, order);
			request.put(KEY_RAW, raw);
			request.put(KEY_JSON, json);
		}

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
	public static SQLConfig parseJoin(RequestMethod method, SQLConfig config, List<Join> joinList, Callback callback) throws Exception {
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
			SQLConfig cacheConfig = j.canCacheViceTable() == false ? null : newSQLConfig(method, table, alias, j.getRequest(), null, false, callback).setCount(1);

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

				if (cacheConfig != null) {
					cacheConfig.setDatabase(joinConfig.getDatabase()).setSchema(joinConfig.getSchema()); //解决主表 JOIN 副表，引号不一致
				}


				if (isQuery) {
					config.setKeyPrefix(true);
				}

				joinConfig.setMain(false).setKeyPrefix(true);

				if (j.isLeftOrRightJoin()) {
					SQLConfig outterConfig = newSQLConfig(method, table, alias, j.getOuter(), null, false, callback);
					outterConfig.setMain(false).setKeyPrefix(true).setDatabase(joinConfig.getDatabase()).setSchema(joinConfig.getSchema()); //解决主表 JOIN 副表，引号不一致
					j.setOuterConfig(outterConfig);
				}
			}

			//解决 query: 1/2 查数量时报错  
			/* SELECT  count(*)  AS count  FROM sys.Moment AS Moment  
			   LEFT JOIN ( SELECT count(*)  AS count FROM sys.Comment ) AS Comment ON Comment.momentId = Moment.id LIMIT 1 OFFSET 0 */
			if (RequestMethod.isHeadMethod(method, true)) {
				joinConfig.setMethod(GET); //子查询不能为 SELECT count(*) ，而应该是 SELECT momentId
				joinConfig.setColumn(Arrays.asList(j.getKey())); //优化性能，不取非必要的字段

				if (cacheConfig != null) {
					cacheConfig.setMethod(GET); //子查询不能为 SELECT count(*) ，而应该是 SELECT momentId
					cacheConfig.setColumn(Arrays.asList(j.getKey())); //优化性能，不取非必要的字段
				}
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
		if (originKey == null || apijson.JSONObject.isArrayKey(originKey)) {
			Log.w(TAG, "getRealKey  originKey == null || apijson.JSONObject.isArrayKey(originKey) >>  return originKey;");
			return originKey;
		}

		String key = new String(originKey);
		if (key.endsWith("$")) {//搜索 LIKE，查询时处理
			key = key.substring(0, key.length() - 1);
		}
		else if (key.endsWith("~")) {//匹配正则表达式 REGEXP，查询时处理
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


	public static interface IdCallback {
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
		String getIdKey(String database, String schema, String datasource, String table);

		/**获取 User 的主键名
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		String getUserIdKey(String database, String schema, String datasource, String table);
	}

	public static interface Callback extends IdCallback {
		/**获取 SQLConfig 的实例
		 * @param method
		 * @param database
		 * @param schema
		 * @param table
		 * @return
		 */
		SQLConfig getSQLConfig(RequestMethod method, String database, String schema, String table);

		/**combine 里的 key 在 request 中 value 为 null 或不存在，即 request 中缺少用来作为 combine 条件的 key: value
		 * @param combine
		 * @param key
		 * @param request
		 */
		public void onMissingKey4Combine(String name, JSONObject request, String combine, String item, String key) throws Exception;
	}

	public static abstract class SimpleCallback implements Callback {


		@Override
		public Object newId(RequestMethod method, String database, String schema, String table) {
			return System.currentTimeMillis();
		}

		@Override
		public String getIdKey(String database, String schema, String datasource, String table) {
			return KEY_ID;
		}

		@Override
		public String getUserIdKey(String database, String schema, String datasource, String table) {
			return KEY_USER_ID;
		}

		@Override
		public void onMissingKey4Combine(String name, JSONObject request, String combine, String item, String key) throws Exception {
			throw new IllegalArgumentException(name + ":{} 里的 @combine:value 中的value里 " + item + " 对应的条件 " + key + ":value 中 value 不能为 null！");
		}

	}

}
