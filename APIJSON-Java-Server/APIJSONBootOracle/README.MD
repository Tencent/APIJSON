# 针对Oracle，APIJSON需要修改源码

## 1. 处理`字段括号`的问题，`AbstractSQLConfig.getQuote`，196行

```java
@Override
public String getQuote() {
    return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? "\"" : "`";
}
```

所以使用的时候需要在`DemoSQLConfig`中添加一个方法

```java
@Override
public String getDatabase() {
    // TODO Auto-generated method stub
    return DATABASE_POSTGRESQL;
}
```




## 2. 修改`分页逻辑`

修改`AbstractSQLConfig.getConditionString`，785行

```java
LIMIT count OFFSET page*count  
不能改成 getWhereString 后面加  row_num > page*count AND row_num <= (page + 1)*count
需要将整个语句转变成子查询的模式
SELECT a.*
FROM (SELECT ROWNUM AS rid, t.*
FROM USERS t) a
```

Oracle 想要分页，需要子查询，不过如果子查询的话会多出一个字段。




## 3. 表名默认是小写，需要改成大写，否则会提示找不到表名的错误

修改`AbstractSQLConfig.getSQLTable`，234行

```java
@JSONField(serialize = false)
	@Override
	public String getSQLTable() {
		String t = TABLE_KEY_MAP.containsKey(table) ? TABLE_KEY_MAP.get(table) : table;
		return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? t.toUpperCase() : t;
	}
```

其中`toLowerCase`改为`toUpperCase`。

`AbstractSQLConfig.getColumnString`，562行

```java
for (int j = 0; j < ckeys.length; j++) {
    index = ckeys[j].lastIndexOf(":"); //StringUtil.split返回数组中，子项不会有null
    origin = index < 0 ? ckeys[j] : ckeys[j].substring(0, index);
    origin = origin.toUpperCase();
    alias = index < 0 ? null : ckeys[j].substring(index + 1);
    ....
```

origin取值的结果要变成大写，这样在前端请求的时候，会忽略字段的大小写。



## 4. 字段名被引号括起来之后，Oracle区分字段的大小写，需要将字段大写

过滤条件的字段`getWhereItem`，1095行

```java
key = getRealKey(method, key, false, true, verifyName, getQuote());
```

key的取值也变成大写



## 5. Order by时，字段名大写

`getOrderString`，483行

```java
origin = index < 0 ? keys[i] : keys[i].substring(0, index);
origin = origin.toUpperCase();
```



## 6. @Combine时，REGEXP函数需要修改

`getRegExpString`, 1254行

```java
@JSONField(serialize = false)
	public String getRegExpString(String key, String value) {
		return getKey(key) + " REGEXP " + getValue(value);
	}
改为
return "regexp_like(" + getKey(key) + " , " + getValue(value) + ")";
```



## 7. @Group时

`getKey`，1119行

```java
public String getKey(String key) {
    String q = getQuote();
    return (isKeyPrefix() ? getAlias() + "." : "") + q  + key + q;
}
改为
return (isKeyPrefix() ? getAlias() + "." : "") + q  + key.toUpperCase() + q;
```



## 8. 关联查询须知

从A表店铺（c_store）中，通过外键字段ID（modifierid）关联查询B表用户（Users）的数据

确保相关的实际表名已经配置好，两个表的接口都能单独查询。

请求：

```java
{
    "Store":{
        "@column":"id,name,modifierid"
    },
    "Users":{
        "id@":"Store/MODIFIERID",
        "@column":"id,name"
    }
}
```



首先，主数据`Store`的字段`modifierid`需要查询出来。其次，`Users`表的关联路径`Store/MODIFIERID`中`modifierid`字段需要大写。

响应：

```json
{
    "[]": [
        {
            "Store": {
                "CODE": "0003",
                "ID": 4,
                "NAME": "测试"
            },
            "Users": {
                "C_STORE_ID": 4,
                "ID": 1803,
                "NAME": "测试"
            }
        },
        ....
        {
            "Store": {
                "CODE": "999999",
                "ID": 1,
                "NAME": "总部仓库"
            },
            "Users": {
                "C_STORE_ID": 1,
                "ID": 1802,
                "NAME": "admin"
            }
        }
    ],
    "code": 200,
    "msg": "success"
}
```

