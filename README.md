# APIJSON

[查看中文文档](https://github.com/TommyLemon/APIJSON/blob/master/README(%E4%B8%AD%E6%96%87).md)

A JSON Transmission Structure Protocal.

You can set any JSON structure and request your server, and the server will return a JSON String with the structure you had set like this: 

### Request: 
{<br />
&nbsp; &nbsp; &quot;[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //request an array<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 1, &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//array condition<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 2, &nbsp; &nbsp; &nbsp; &nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//request an object from the table named User<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;: 0 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //object condition<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;: &ldquo;/User/id&rdquo; &nbsp;//rely path with default parent path,starts from the same level object&#39;s path<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//request an array named Comment<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 3,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&quot;workId&quot;: &ldquo;[]/Work/id&rdquo; &nbsp;//full rely path<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;}<br />
&nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; }<br />
}</p>

### Response: 
{<br />
&nbsp; &nbsp; &quot;[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:&quot;38710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:&quot;0&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300038710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-38710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://www.tooopen.com/view/38710.html&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:38710,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;http://www.tooopen.com/view/470.html&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:4,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:310,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:14604,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-4&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-14604&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-93781&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;1&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:22,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:221,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:332,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:5904,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-22&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-5904&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-11679&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;2&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:47,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:4,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:10,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:5477,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-47&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-5477&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-80271&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;1&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:&quot;70793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:&quot;0&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300070793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-70793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://www.tooopen.com/view/70793.html&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:70793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;http://www.tooopen.com/view/73.html&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:44,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:7073,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:6378,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-44&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-6378&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-88645&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;1&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:54,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:3,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:62122,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-54&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-62122&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-82381&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;2&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:99,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:44,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;workId&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:7166,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-99&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-7166&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-22949&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; }<br />
}</p>


![](https://github.com/TommyLemon/APIJSON/blob/master/picture/apijson_all_pages.jpg?raw=true)
![](https://github.com/TommyLemon/APIJSON/blob/master/picture/server_idea_log_complex.jpg)
![](https://github.com/TommyLemon/APIJSON/blob/master/picture/complex_json_cn.jpg?raw=true)

## Compare with Previous HTTP Transmission Way
 
 Process | Previous way | APIJSON
-------- | ------------ | ------------
 Transmission | 等服务端编辑接口，然后更新文档，客户端再按照文档编辑请求和解析代码 | 客户端按照自己的需求编辑请求和解析代码
 Compatibility | 服务端编辑新接口，用v2表示第2版接口，然后更新文档 | do nothing
 
 Client request | Previous way | APIJSON
-------- | ------------ | ------------
 Requirement | 客户端按照文档在对应url后面拼接键值对 | 客户端按照自己的需求在固定url后拼接JSON
 Structure | base_url/lowercase_table_name?key0=value0&key1=value1...<br />&currentUserId=100&currentUserPassword=1234<br />其中currentUserId和currentUserPassword只在请求部分接口时需要 | base_url/{TableName0:{key0:value0, key1:value1 ...}, TableName1:{...}...<br />, currentUserId:100, currentUserPassword:1234}<br />其中currentUserId和currentUserPassword只在请求部分接口时需要
 URL | 不同请求方法(GET，POST等)或不同功能对应不同url | 相同请求方法(GET，POST等)都用同一个url
 Key-Value Pair | key=value | key:value
 
 Server operation | Previous way | APIJSON
-------- | ------------ | ------------
 Parse and response | 取出键值对，用键值对作为条件去查询预设的table，最后封装JSON并返回给客户端 | 把RequestParser#parse方法的返回值返回给客户端
 Way of setting JSON structure to return | 由服务端设定，客户端不能修改 | 由客户端设定，服务端不能修改
 
 Client parse | Previous way | APIJSON
-------- | ------------ | ------------
 View | 查文档或等请求成功后看log | 看请求，所求即所得。也可以等请求成功后看log
 Operate | 解析JSONObject | 可以用JSONResponse解析JSONObject或传统方式

 Client requests for different requirements | Previous way | APIJSON
-------- | ------------ | ------------
 User | http://localhost:8080/user?id=1 | http://localhost:8080/{"User":{"id":1}}
 User and his Work | 分两次请求<br />User: http://localhost:8080/user?id=1<br />Work: http://localhost:8080/work?userId=1 | http://localhost:8080/{"User":{"id":1}, "Work":{"userId":"User/id"}}
 User list | http://localhost:8080/user/list?page=1&count=5&sex=0 | http://localhost:8080/{"[]":{"page":1, "count":5, "User":{"sex":0}}}
 Work list of which type is 1, each Work contains publisher User and top 3 Comments | Work里必须有User的Object和Comment的Array<br /> http://localhost:8080/work/list?page=1&count=5&type=1&commentCount=3 | http://localhost:8080/{"[]":{"page":1, "count":5, "Work":{"type":1}, "User":{"workId":"/Work/id"}, "[]":{"count":3, "Comment":{"workId":"[]/Work/id"}}}}
 Work list of an User, each Work contains publisher User and top 3 Comments | 把以上请求里的type=1改为userId=1 | ①把以上请求里的"Work":{"type":1}, "User":{"workId":"/Work/id"}改为"User":{"id":1}, "Work":{"userId":"/User/id"}<br />②或这样省去4条重复User<br />http://localhost:8080/{"User":{"id":1}, "[]":{"page":1, "count":5, "Work":{"userId":"User/id"}, "[]":{"count":3, "Comment":{"workId":"[]/Work/id"}}}}<br />③如果User之前已经获取到了，还可以这样省去所有重复User<br />http://localhost:8080/{"[]":{"page":1, "count":5, "Work":{"userId":1}, "[]":{"count":3, "Comment":{"workId":"[]/Work/id"}}}}
 
 Server responses for different requests | Previous way | APIJSON
-------- | ------------ | ------------
 User | {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}}
 User and his Work | 分别返回两次请求的结果<br />User: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}}<br />Work: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}, "Work":{"id":1, "content":"xxx"...}}
 User list | {"status":200, "message":"success", "data":[{"id":1, "name":"xxx"...}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}}, "1":{"User":{"id":2...}}...}}
 Work list of which type is 1, each Work contains publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "User":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}
 Work list of an User, each Work contains publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | ①{"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}, "Work":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br />②{"status":200, "message":"success", "User":{...}, "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br />③{"status":200, "message":"success", "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}

## Usage

### 1.Download and unzip APIJSON project

Clone or download > Download ZIP > Unzip to a path and remember it.

### 2.Import MySQL table files

Start MySQLWorkbench > Enter a connection > Click Server menu > Data Import > Select the path

of APIJSON-Master/table > Start Import > Refresh SCHEMAS, and you'll see the tables were already added.

### 3.Run Server project with IntellIJ IDEA Ultimate

If you haven't installed it, please download and install before run.

### 4.Run Client project with ADT or Android Studio

If you haven't installed any editor above, please download and install one before run.

### 5.Operate Client app

Select an APIJSON request to send to server and wait. It will show the result received.


## Download Client App

[APIJSONClientApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSON%28ADT%29.apk)

### If you have any questions or suggestions about APIJSON, you can send me an e-mail to tommylemon@qq.com.


## Welcome star, welcome fork

[https://github.com/TommyLemon/APIJSON](https://github.com/TommyLemon/APIJSON) 


# APIJSON, let interfaces go to hell !
