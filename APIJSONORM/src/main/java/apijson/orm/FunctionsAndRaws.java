package apijson.orm;

import java.util.LinkedHashMap;
import java.util.Map;


public class FunctionsAndRaws {
    // 自定义原始 SQL 片段 Map<key, substring>：当 substring 为 null 时忽略；当 substring 为 "" 时整个 value 是 raw SQL；其它情况则只是 substring 这段为 raw SQL
    public static final Map<String, String> RAW_MAP;
    // 允许调用的 SQL 函数：当 substring 为 null 时忽略；当 substring 为 "" 时整个 value 是 raw SQL；其它情况则只是 substring 这段为 raw SQL
    public static final Map<String, String> SQL_FUNCTION_MAP;
    static {
        RAW_MAP = new LinkedHashMap<>();  // 保证顺序，避免配置冲突等意外情况

        // mysql关键字
        RAW_MAP.put("AS","");
        RAW_MAP.put("VALUE","");
        RAW_MAP.put("DISTINCT","");

        //时间
        RAW_MAP.put("DATE","");
        RAW_MAP.put("now()","");
        RAW_MAP.put("DATETIME","");
        RAW_MAP.put("DateTime","");
        RAW_MAP.put("SECOND","");
        RAW_MAP.put("MINUTE","");
        RAW_MAP.put("HOUR","");
        RAW_MAP.put("DAY","");
        RAW_MAP.put("WEEK","");
        RAW_MAP.put("MONTH","");
        RAW_MAP.put("QUARTER","");
        RAW_MAP.put("YEAR","");
        RAW_MAP.put("json","");
        RAW_MAP.put("unit","");

        //MYSQL 数据类型 BINARY，CHAR，DATETIME，TIME，DECIMAL，SIGNED，UNSIGNED
        RAW_MAP.put("BINARY","");
        RAW_MAP.put("SIGNED","");
        RAW_MAP.put("DECIMAL","");
        RAW_MAP.put("BINARY","");
        RAW_MAP.put("UNSIGNED","");
        RAW_MAP.put("CHAR","");
        RAW_MAP.put("TIME","");

        //窗口函数关键字
        RAW_MAP.put("OVER","");
        RAW_MAP.put("INTERVAL","");
        RAW_MAP.put("ORDER","");
        RAW_MAP.put("BY","");
        RAW_MAP.put("PARTITION",""); //往前
        RAW_MAP.put("DESC","");
        RAW_MAP.put("ASC","");
        RAW_MAP.put("FOLLOWING","");//往后
        RAW_MAP.put("BETWEEN","");
        RAW_MAP.put("AND","");
        RAW_MAP.put("ROWS","");


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
        SQL_FUNCTION_MAP.put("ifnull", "");  // IFNULL(v1,v2)	如果 v1 的值不为 NULL，则返回 v1，否则返回 v2。
        SQL_FUNCTION_MAP.put("isnull", "");  // ISNULL(expression)	判断表达式是否为 NULL
        SQL_FUNCTION_MAP.put("nullif", "");  // NULLIF(expr1, expr2)	比较两个字符串，如果字符串 expr1 与 expr2 相等 返回 NULL，否则返回 expr1
        SQL_FUNCTION_MAP.put("group_concat", "");  // GROUP_CONCAT([DISTINCT], s1, s2...)

        //clickhouse 字符串函数  注释的函数表示返回的格式暂时不支持，如：返回数组 ，同时包含因版本不同 clickhosue不支持的函数，版本
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

        //clickhouse json函数
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

        //clickhouse 类型转换函数
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



        ////clickhouse hash函数
        SQL_FUNCTION_MAP.put("halfMD5", ""); //计算字符串的MD5。然后获取结果的前8个字节并将它们作为UInt64（大端）返回
        SQL_FUNCTION_MAP.put("MD5", "");  //计算字符串的MD5并将结果放入FixedString(16)中返回

        //clickhouse ip地址函数
        SQL_FUNCTION_MAP.put("IPv4NumToString", "");  //接受一个UInt32（大端）表示的IPv4的地址，返回相应IPv4的字符串表现形式，格式为A.B.C.D（以点分割的十进制数字）。
        SQL_FUNCTION_MAP.put("IPv4StringToNum", ""); //与IPv4NumToString函数相反。如果IPv4地址格式无效，则返回0。
        SQL_FUNCTION_MAP.put("IPv6NumToString", ""); //接受FixedString(16)类型的二进制格式的IPv6地址。以文本格式返回此地址的字符串。
        SQL_FUNCTION_MAP.put("IPv6StringToNum", "");  //与IPv6NumToString的相反。如果IPv6地址格式无效，则返回空字节字符串。
        SQL_FUNCTION_MAP.put("IPv4ToIPv6", ""); // 接受一个UInt32类型的IPv4地址，返回FixedString(16)类型的IPv6地址
        SQL_FUNCTION_MAP.put("cutIPv6", ""); //接受一个FixedString(16)类型的IPv6地址，返回一个String，这个String中包含了删除指定位之后的地址的文本格
        SQL_FUNCTION_MAP.put("toIPv4", "");  //IPv4StringToNum()的别名，
        SQL_FUNCTION_MAP.put("toIPv6", ""); //IPv6StringToNum()的别名
        SQL_FUNCTION_MAP.put("isIPAddressInRange", ""); //确定一个IP地址是否包含在以CIDR符号表示的网络中

        //clickhouse Nullable处理函数
        SQL_FUNCTION_MAP.put("isNull", "");  //检查参数是否为NULL。
        SQL_FUNCTION_MAP.put("isNotNull", ""); //检查参数是否不为 NULL.
        SQL_FUNCTION_MAP.put("ifNull", ""); //如果第一个参数为«NULL»，则返回第二个参数的值。
        SQL_FUNCTION_MAP.put("assumeNotNull", ""); //将可为空类型的值转换为非Nullable类型的值。
        SQL_FUNCTION_MAP.put("toNullable", "");  //将参数的类型转换为Nullable。

        //clickhouse UUID函数
        SQL_FUNCTION_MAP.put("generateUUIDv4", ""); // 生成一个UUID
        SQL_FUNCTION_MAP.put("toUUID", ""); //toUUID(x) 将String类型的值转换为UUID类型的值。

        //clickhouse 系统函数
        SQL_FUNCTION_MAP.put("hostName", ""); //hostName()回一个字符串，其中包含执行此函数的主机的名称。
        SQL_FUNCTION_MAP.put("getMacro", ""); //从服务器配置的宏部分获取指定值。
        SQL_FUNCTION_MAP.put("FQDN", "");//返回完全限定的域名。
        SQL_FUNCTION_MAP.put("basename", ""); //提取字符串最后一个斜杠或反斜杠之后的尾随部分
        SQL_FUNCTION_MAP.put("currentUser", ""); //返回当前用户的登录。在分布式查询的情况下，将返回用户的登录，即发起的查询
        SQL_FUNCTION_MAP.put("version", ""); //以字符串形式返回服务器版本。
        SQL_FUNCTION_MAP.put("uptime", "");//以秒为单位返回服务器的正常运行时间。

        //clickhouse 数学函数
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
}
