
<h1 align="center" style="text-align:center;">
  APIJSON 规划及路线图
</h1>


### 完善功能
* 新增支持 Case
实现远程函数也不方便的 SQL 内字段转换 CASE WHEN THEN ，<br />
暂时还没有想好如何设计。如果是 SQL 原来的写法，则有点繁琐。<br />

* 新增支持 @having& 
来实现内部条件 AND 连接，原来的 @having 由 AND 连接变为 OR 连接，保持 横或纵与 的统一规则。<br />
@having! 必须性不大，可通过反转内部条件来实现，但如果实现简单、且不影响原来的功能，则可以顺便加上。<br />

* 新增支持 @column!
这个只在 APIJSONFramework 支持，需要配置每个接口版本、每张表所拥有的全部字段，然后排除掉 @column! 的。<br />
需要注意的是，可能前端传参里既有 @column 又有 @column! ，碰到这种情况：<br />
如果没有重合字段就忽略 @column! ，只让 @column 生效；<br />
如果有有重合字段，则抛异常，转为错误码和错误信息返回。<br />


* 新增支持分布式
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


* 新增支持 Union
虽然可以通过 INNER JOIN + 条件 OR 连接来替代它的功能，但没法达到它能用索引的性能。<br />
支持 UNION 很有必要，但 UNION ALL 几乎没有需求，如果实现简单、且不影响原来的功能，则可以顺便加上。

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


* 新增支持 With
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



### 保障安全
* 防越权操作
目前有 RBAC 自动化权限管理。

* 防 SQL 注入
目前有 预编译 + 白名单 校验机制。

* 防恶意复杂请求
目前有限流机制，getMaxQueryCount, getMaxUpdateCount, getMaxObjectCount, getMaxSQLCount 等。



### 优化性能
* 解析 JSON
优化 AbstractParser 和 AbstractObjectParser 解析 JSON 性能。

* 封装 JSON
优化 AbstractSQLExecutor 封装 JSON 性能。

* 拼接 SQL
优化 AbstractSQLConfig 拼接 SQL 性能。<br />
#完善功能 中 Union 和 With 也是优化 SQL 性能的方式。

* 读写缓存
在 AbstractParser 使用了 HashMap<String, JSONObject> queryResultMap 存已解析的对象、总数等数据。<br />
在 AbstractSQLExecutor 使用了 HashMap<String, JSONObject> cacheMap 存已通过 SQL 查出的结果集。<br />

* ...  //欢迎补充



### 增强稳定

* 减少 Bug
#### 1.APIAuto 上统计的 bug
http://apijson.org:8000/auto/

#### 2.其它发现的 Bug
https://github.com/APIJSON/APIJSON/issues?q=is%3Aissue+is%3Aopen+label%3Abug

* 完善测试
1.在 APIAuto-机器学习自动化接口管理平台 上传更多、更全面、更细致的测试用例、动态参数等

2.接入 UnitAuto-机器学习自动化单元测试平台，每次启动都自动测试所有可测方法并输出报告



### 完善文档
* 中文文档

#### 1.设计规范

#### 2.配置与部署

#### ...  //欢迎补充

* English Document
Translation for Chinese document.



### 丰富周边
* Go, Kotlin, Ruby 等其它语言 ORM 库


* Demo
JavaScript 前端，TypeScript 前端，微信等小程序，<br />
Android 客户端，iOS 客户端，C# 游戏客户端等。<br />

* 扩展

#### 1.基于或整合 APIJSONORM 或 APIJSONFramework 来实现的库/框架

#### 2.扩展 APIJSONORM 或 APIJSONFramework 功能的插件
可以通过扩展对象关键词 @key，数组关键词 key，远程函数，重写部分方法等来实现。<br />

#### 3.前端/客户端 封装/解析 APIJSON 的库/框架
因为 APIJSON 基于 JSON，大部分情况下都可以直接用 fastjson 等 JSON 封装/解析库或其它工具等，<br />
只是 APIJSON 有部分功能需要在 key 里放 [], @ 等特殊符号，返回 [] 在某些情况下不方便解析，<br />
目前可使用 "format": true 让后端格式化后返回，但也会对服务器性能有一些损耗，<br />
如果 前端/客户端 有对应的格式化工具等（例如 APIJSONORM 可供 Android 使用 format），选择就会更多一些。<br />



### 推广使用
* 为 APIJSON 编写/发表 博客/文章/资讯 等

* 登记正在使用 APIJSON 的公司或项目

* 在社交技术群、论坛等聊天或评论时提及 APIJSON

* ...  //欢迎补充


