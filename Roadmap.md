
<h1 align="center" style="text-align:center;">
  APIJSON 规划及路线图
</h1>


### 完善功能
部分功能描述可在 [APIAuto](https://github.com/TommyLemon/APIAuto) 上查看 <br />
账号 13000002020 密码 123456 <br />
http://apijson.org:8000/auto/ <br />

#### 新增支持假删除
一般对于互联网项目，数据非常重要，基本不会物理删除，只是用 is_deleted 等字段来标记已删除，然后 CRUD 时默认过滤已标记的记录。 <br />
这个功能非常必要，可以通过重写 SQLConfig.isFakeDelete() 来标记，然后如果 true，则把 DELETE 改为 PUT 并且通过重写 <br />
SQLConfig.putFakeDelete(Map<String, Object> map) 来新增条件： <br />
GET: map.put("deleted", 0) <br />
PUT:  map.put("deleted", 0) <br />
DELETE:  map.put("deleted", 1) <br />
当然也可以再加一个删除时间 deletedTime 之类的。(POST 用不上)

#### 新增支持 @having& 

来实现内部条件 AND 连接，原来的 @having 由 AND 连接变为 OR 连接，保持 横或纵与 的统一规则。<br />
@having! 必须性不大，可通过反转内部条件来实现，但如果实现简单、且不影响原来的功能，则可以顺便加上。<br />

#### 新增支持 @column!

这个只在 [APIJSONFramework](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONFramework) 支持，需要配置每个接口版本、每张表所拥有的全部字段，然后排除掉 @column! 的。<br />
可新增一个 VersionedColumn 表记录来代替 HashMap 代码配置。<br />
需要注意的是，可能前端传参里既有 @column 又有 @column! ，碰到这种情况：<br />
如果没有重合字段就忽略 @column! ，只让 @column 生效；<br />
如果有有重合字段，则抛异常，转为错误码和错误信息返回。<br />

#### 新增支持 TSQL 的 @explain

目前 APIJSON 支持 [Oracle](https://github.com/APIJSON/APIJSON/tree/master/Oracle) 和 [SQL Server](https://github.com/APIJSON/APIJSON/tree/master/SQLServer) 这两种 TSQL 数据库（群友测试 IBM DB2 也行）。<br />
但是 "@explain": true 使用的是 SET STATISTICS PROFILE ON(具体见 [AbstractSQLConfig](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLConfig.java) 和 [AbstrctSQLExecutor](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstrctSQLExecutor.java))  <br />
执行后居然是 SELECT 查到的放在默认的 ResultSet，性能分析放在 moreResult，<br />
因为这个问题目前以上两个数据库的性能分析 @explain 实际并不可用，需要改用其它方式或解决现有方式的 bug。<br />

#### 新增支持 page 从 1 开始
目前只能从 0 开始，实际使用 1 更广泛，而且这方面用户习惯很强，支持它成本也不高。 <br />
[Parser](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/Parser.java) 新增 DEFAULT_QUERY_PAGE 和 getDefaultQueryPage， <br />
与 DEFAULT_QUERY_COUNT 和 getDefaultQueryCount 统一， <br />
方便前端直接用页码的值传参，以及 info.page 的值来渲染页码。 <br />
建议在 [AbstractParser](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractParser.java)，[AbstractSQLConfig](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLConfig.java) 用到 page 的地方判断 getDefaultQueryPage，做兼容处理。 <br />

#### 新增支持分布式

 "@url": "http://apijson.cn:8080/get"
```js
{
    "User": {
        "@url": "http://apijson.cn:8080/get"  //转发给其它服务器执行
    },
    "[]": {
        "Comment": {
            "userId@": "User/id"
        }
    },
    "@explain": true
}
```
考虑到引用赋值的依赖关系要保持先后顺序，必须同步阻塞处理。<br />
或许可以加一个 ~ 前缀表示异步？例如 "@url": "\~http://apijson.cn:8080/get"，由调用方保证没有被下方对象依赖。<br />


#### 新增支持 Union

虽然可以通过 INNER JOIN + 条件 OR 连接来替代它的功能，但没法达到它能用索引的性能。<br />
支持 UNION 很有必要，但 UNION ALL 几乎没有需求，如果实现简单、且不影响原来的功能，则可以顺便加上。 <br />

"@union": Integer  // 0 - 不使用，1 - UNION，2 - UNION ALL
```js
{
    "[]": {
        "User": {
            "name~": "a",
            "tag~": "a",
            "@combine": "name~,tag~",    
            "@union": 1  //将 @combine 中的 N 个 OR 连接字段用 UNION 替换，原本一条 SQL 需要拆分成 N 条 SQL 来 UNION 
        }
    },
    "@explain": true
}
```
生成的 SQL
```sql
SELECT * FROM `sys`.`apijson_user` WHERE ( (`name` REGEXP BINARY 'a') OR (`tag` REGEXP BINARY 'a') ) LIMIT 10 OFFSET 0
```
需要变为
```sql
SELECT * FROM `sys`.`apijson_user` WHERE ( (`name` REGEXP BINARY 'a') )
UNION
SELECT * FROM `sys`.`apijson_user` WHERE ( (`tag` REGEXP BINARY 'a') )
LIMIT 10 OFFSET 0
```


#### 新增支持 With

可以减少子查询执行次数，提高性能。
```js
{   //看看关注的人最近有什么动态（分享、评论）
    "sql@": {
        "with": true,  //生成 WITH(SELECT id ...) AS `sql`
        "from": "User",
        "User": {
            "@column": "id",
            "@role": "CONTACT"
        }
    },
    "Moment[]": {
        "Moment": {
            "userId{}@": "sql",
            "@order": "date-"
        }
    },
    "Comment[]": {
        "Comment": {
            "userId{}@": "sql",
            "@order": "date-"
        }
    },
    "@explain": true
}
```
生成的 SQL
```sql
SELECT * FROM `sys`.`Moment` WHERE ( (`userId` IN (SELECT `id` FROM `sys`.`User` WHERE `id` IN($contactIdList)) ) ) ORDER BY `date` DESC LIMIT 10 OFFSET 0
```
和
```sql
SELECT * FROM `sys`.`Moment` WHERE ( (`userId` IN (SELECT `id` FROM `sys`.`User` WHERE `id` IN($contactIdList)) ) ) ORDER BY `date` DESC LIMIT 10 OFFSET 0
```

需要变为
```sql
WITH (SELECT `id` FROM `sys`.`User` WHERE `id` IN($contactIdList)) AS `sql`
SELECT * FROM `sys`.`Moment` WHERE ( (`userId` IN `sql` ) ) ORDER BY `date` DESC LIMIT 10 OFFSET 0
```
和
```sql
WITH (SELECT `id` FROM `sys`.`User` WHERE `id` IN($contactIdList)) AS `sql`
SELECT * FROM `sys`.`Comment` WHERE ( (`userId` IN `sql` ) ) ORDER BY `date` DESC LIMIT 10 OFFSET 0
```


#### 新增支持 Case
【更新：不用实现了，直接按 SQL 的语法写 CASE WHEN，然后用 @raw】
实现远程函数也不方便的 SQL 内字段转换 CASE WHEN THEN ，<br />
暂时还没有想好如何设计。如果是 SQL 原来的写法，则有点繁琐。<br />


#### ...  //欢迎补充



### 保障安全
APIJSON 提供了各种安全机制，可在目前的基础上新增或改进。

#### 防越权操作

目前有 RBAC 自动化权限管理。<br />
APIJSONORM 提供 [@MethodAccess](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/MethodAccess.java) 注解来配置 <br />
APIJSONFramework 则使用 [Access 表](https://github.com/APIJSON/APIJSON/blob/master/MySQL/single/sys_Access.sql) 记录来配置 <br />
在 [AbstractVerifier](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractVerifier.java) 中，假定真实、强制匹配。 <br />


#### 防 SQL 注入

目前有 预编译 + 白名单 校验机制。具体见 [AbstractSQLExecutor](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLExecutor.java)  和 [AbstractSQLConfig](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLConfig.java) 。 <br />

#### 防恶意请求

目前有限流机制，getMaxQueryCount, getMaxUpdateCount, getMaxObjectCount, getMaxSQLCount, getMaxQueryDepth 等。 <br />
https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/Parser.java <br />

#### ...  //欢迎补充


### 优化性能

#### 解析 JSON

优化 [AbstractParser](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractParser.java) 和 [AbstractObjectParser](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractObjectParser.java) 解析 JSON 性能。 <br />

#### 封装 JSON

优化 [AbstractSQLExecutor](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLExecutor.java) 封装 JSON 性能。 <br />

#### 拼接 SQL

优化 [AbstractSQLConfig](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLConfig.java) 拼接 SQL 性能。<br />
[完善功能](https://github.com/APIJSON/APIJSON/blob/master/Roadmap.md#%E5%AE%8C%E5%96%84%E5%8A%9F%E8%83%BD) 中 Union 和 With 也是优化 SQL 性能的方式。 <br />

#### 读写缓存

在 [AbstractParser](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractParser.java) 使用了 HashMap<String, JSONObject> queryResultMap 存已解析的对象、总数等数据。<br />
在 [AbstractSQLExecutor](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM/src/main/java/apijson/orm/AbstractSQLExecutor.java) 使用了 HashMap<String, JSONObject> cacheMap 存已通过 SQL 查出的结果集。<br />

#### ...  //欢迎补充



### 增强稳定
APIJSON 代码经过商业分析软件 [源伞Pinpoint](https://www.sourcebrella.com/) 的质检，报告说明 APIJSON 非常可靠。<br />
https://github.com/APIJSON/APIJSON/issues/48 <br />
但我们需要再接再厉，尽可能做到 99.999% 可靠度，降低使用风险，让用户放心和安心。 <br />

#### 减少 Bug

##### [APIAuto](https://github.com/TommyLemon/APIAuto) 上统计的 bug
账号 13000002000 密码 123456 <br />
http://apijson.org:8000/auto/ <br />

##### 其它发现的 Bug
https://github.com/APIJSON/APIJSON/issues?q=is%3Aissue+is%3Aopen+label%3Abug <br />

#### 完善测试

##### 在 APIAuto-机器学习自动化接口管理平台 上传更多、更全面、更细致的测试用例、动态参数等
http://apijson.org:8000/auto/ <br />

##### 接入 UnitAuto-机器学习自动化单元测试平台，每次启动都自动测试所有可测方法并输出报告
https://gitee.com/TommyLemon/UnitAuto <br />


### 完善文档
#### 中文文档

##### 通用文档
https://github.com/APIJSON/APIJSON/blob/master/Document.md <br />

##### 配置与部署
https://github.com/APIJSON/APIJSON/tree/master/APIJSON-Java-Server <br />

##### ...  //欢迎补充


#### English Document

Translation for Chinese document. <br />
https://github.com/APIJSON/APIJSON/blob/master/README-English.md <br />
https://github.com/APIJSON/APIJSON/blob/master/Document-English.md <br />
https://github.com/ruoranw/APIJSONdocs <br />



### 丰富周边
#### 新增或完善各种语言 ORM 库

Go, Kotlin, Ruby 等。<br />
https://github.com/APIJSON/APIJSON#%E7%94%9F%E6%80%81%E9%A1%B9%E7%9B%AE <br />

#### 新增或完善 Demo

JavaScript 前端，TypeScript 前端，微信等小程序，<br />
Android 客户端，iOS 客户端，C# 游戏客户端等。<br />
Java, C#, Node, Python 等后端 Demo 及数据。<br />
https://github.com/APIJSON/APIJSON <br />

#### 新增扩展

##### 1.基于或整合 [APIJSONORM](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM) 或 [APIJSONFramework](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONFramework) 来实现的库/框架

##### 2.扩展 [APIJSONORM](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONORM) 或 [APIJSONFramework](https://github.com/APIJSON/APIJSON/blob/master/APIJSON-Java-Server/APIJSONFramework) 功能的插件
可以通过扩展对象关键词 @key，数组关键词 key，远程函数，重写部分方法等来实现。<br />

##### 3.前端/客户端 封装/解析 APIJSON 的库/框架
因为 APIJSON 基于 JSON，大部分情况下都可以直接用 fastjson 等 JSON 封装/解析库或其它工具等，<br />
只是 APIJSON 有部分功能需要在 key 里放 [], @ 等特殊符号，返回 [] 在某些情况下不方便解析，<br />
目前可使用 "format": true 让后端格式化后返回，但也会对服务器性能有一些损耗，<br />
如果 前端/客户端 有对应的格式化工具等（例如 [apijson-orm](https://github.com/APIJSON/apijson-orm) 可供 Android 使用 format），选择就会更多一些。<br />

##### ...  //欢迎补充

### 推广使用
#### 为 APIJSON 编写/发表 博客/文章/资讯 等

https://github.com/APIJSON/APIJSON#%E7%9B%B8%E5%85%B3%E6%8E%A8%E8%8D%90


#### 登记正在使用 APIJSON 的公司或项目

https://github.com/TommyLemon/APIJSON/issues/73

#### 在社交技术群、论坛等聊天或评论时推荐 APIJSON

#### ...  //欢迎补充


