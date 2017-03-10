# APIJSON

[English Document](https://github.com/TommyLemon/APIJSON/blob/master/README(English).md)

APIJSON是一种JSON传输结构协议。<br />

客户端可以定义任何JSON结构去向服务端发起请求，服务端就会返回对应结构的JSON字符串，所求即所得。<br />
一次请求任意结构任意数据，方便灵活，不需要专门接口或多次请求。<br />
支持增删改查、模糊搜索、远程函数调用等。还能去除重复数据，节省流量提高速度！<br />

从此HTTP传输JSON数据没有接口，更不需要文档！<br />
客户端再也不用和服务端沟通接口或文档问题了！再也不会被文档各种错误坑了！<br />
服务端再也不用为了兼容旧版客户端写新版接口和文档了！再也不会被客户端随时随地没完没了地烦了！

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_0.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_1.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_2.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_3.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/server_idea_log_complex.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_request.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_user.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_moment.jpg) 


举个栗子（查询类似微信朋友圈动态列表）:

### 请求：

<p>{<br />
&nbsp; &nbsp; &quot;[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //请求一个array<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 0, &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//array条件<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 2, &nbsp; &nbsp; &nbsp; &nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//请求查询名为User的table，返回名为User的JSONObject<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;: 0 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //object条件<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Moment&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId@&quot;: &ldquo;/User/id&rdquo; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //缺省依赖路径，从同级object的路径开始<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //请求一个名为Comment的array&nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 2,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&quot;momentId@&quot;: &ldquo;[]/Moment/id&rdquo; &nbsp; //完整依赖路径<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;}<br />
&nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; }<br />
}</p>

[点击这里测试](http://139.196.140.118:8080/get/%7B%22%5B%5D%22%3A%7B%22User%22%3A%7B%22sex%22%3A0%7D%2C%22Moment%22%3A%7B%22userId%40%22%3A%22%252FUser%252Fid%22%7D%2C%22Comment%5B%5D%22%3A%7B%22Comment%22%3A%7B%22momentId%40%22%3A%22%255B%255D%252FMoment%252Fid%22%7D%2C%22count%22%3A2%2C%22page%22%3A0%7D%2C%22count%22%3A2%2C%22page%22%3A0%7D%7D)

### 返回：

<p>{<br />
&nbsp; &nbsp; &quot;[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:38710,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300038710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-38710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Moment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:38710,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;pictureList&quot;:[&quot;http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000&quot;]<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:4,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;momentId&quot;:470,<br />
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
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;momentId&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:332,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:5904,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-22&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-5904&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-11679&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;1&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:70793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300070793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-70793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Moment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:70793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;pictureList&quot;:[&quot;http://my.oschina.net/img/portrait.gif?t=1451961935000&quot;]<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:44,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;parentId&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;momentId&quot;:170,<br />
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
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;momentId&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:3,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserId&quot;:62122,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-54&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;targetUserName&quot;:&quot;targetUserName-62122&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userName&quot;:&quot;userName-82381&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; }<br />
}</p>


 
## 对比传统HTTP传输方式
 
 开发流程 | 传统方式 | APIJSON
-------- | ------------ | ------------
 接口传输 | 等服务端编辑接口，然后更新文档，客户端再按照文档编辑请求和解析代码 | 客户端按照自己的需求编辑请求和解析代码。没有接口，更不需要文档！客户端再也不用和服务端沟通接口或文档问题了！
 兼容旧版 | 服务端增加新接口，用v2表示第2版接口，然后更新文档 | 什么都不用做！
 
 客户端请求 | 传统方式 | APIJSON
-------- | ------------ | ------------
 要求 | 客户端按照文档在对应url后面拼接键值对 | 客户端按照自己的需求在固定url后拼接JSON
 结构 | base_url/lowercase_table_name?key0=value0&key1=value1...<br />&currentUserId=100&loginPassword=1234<br /><br />其中currentUserId和loginPassword只在请求部分接口时需要 | base_url/{TableName0:{key0:value0, key1:value1 ...}, TableName1:{...}...<br />, currentUserId:100, loginPassword:1234}<br /><br />其中currentUserId和loginPassword只在请求部分接口时需要
 URL | 不同的请求对应不同的url | 相同的请求方法(GET，POST等)都用同一个url
 键值对 | key=value | key:value
 
 服务端操作 | 传统方式 | APIJSON
-------- | ------------ | ------------
 解析和返回 | 取出键值对，把键值对作为条件用预设的的方式去查询数据库，最后封装JSON并返回给客户端 | 把RequestParser#parse方法的返回值返回给客户端就行
 返回JSON结构的设定方式 | 由服务端设定，客户端不能修改 | 由客户端设定，服务端不能修改
 
 客户端解析 | 传统方式 | APIJSON
-------- | ------------ | ------------
 查看方式 | 查文档或等请求成功后看log | 看请求就行，所求即所得。也可以等请求成功后看log
 方法 | 解析JSONObject | 可以用JSONResponse解析JSONObject或传统方式

 客户端对应不同需求的请求 | 传统方式 | APIJSON
-------- | ------------ | ------------
 User | base_url/get/user?id=1 | [base_url/get/{"User":{"id":1}}](http://139.196.140.118:8080/get/{"User":{"id":38710}})
 Moment和对应的User | 分两次请求<br />Moment: base_url/get/moment?userId=1<br />User: base_url/get/user?id=1 | [base_url/get/{"Moment":{"userId":1}, "User":{"id":1}}](http://139.196.140.118:8080/get/{"Moment":{"userId":38710},"User":{"id":38710}})
 User列表 | base_url/get/user/list?page=0&count=3&sex=0 | [base_url/get/{"[]":{"page":0, "count":3, "User":{"sex":0}}}](http://139.196.140.118:8080/get/{"[]":{"page":0,"count":3,"User":{"sex":0}}})
 Moment列表，每个Moment包括发布者User和前3条Comment | Moment里必须有User的Object和Comment的Array<br /> base_url/get/moment/list?page=0&count=3&commentCount=3 | [base_url/get/{"[]":{"page":0, "count":3, "Moment":{}, "User":{"momentId@":"/Moment/id"}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}](http://139.196.140.118:8080/get/%7B%22%5B%5D%22%3A%7B%22Moment%22%3A%7B%7D%2C%22User%22%3A%7B%22id%40%22%3A%22%252FMoment%252FuserId%22%7D%2C%22Comment%5B%5D%22%3A%7B%22Comment%22%3A%7B%22momentId%40%22%3A%22%255B%255D%252FMoment%252Fid%22%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%7D)
 User发布的Moment列表，每个Moment包括发布者User和前3条Comment | base_url/get/moment/list?page=0&count=3&commentCount=3&userId=1 | 有以下几种方法:<br />①把以上请求里的"Moment":{}, "User":{"momentId@":"/Moment/id"}改为["Moment":{"userId":1}, "User":{"id":1}](http://139.196.140.118:8080/get/%7B%22%5B%5D%22%3A%7B%22Moment%22%3A%7B%22userId%22%3A38710%7D%2C%22User%22%3A%7B%22id%22%3A38710%7D%2C%22%5B%5D%22%3A%7B%22Comment%22%3A%7B%22momentId%40%22%3A%22%255B%255D%252FMoment%252Fid%22%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%7D) <br /><br />②或这样省去4条重复User<br />[base_url/get/{"User":{"id":1}, "[]":{"page":0, "count":3, "Moment":{"userId":1}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}](http://139.196.140.118:8080/get/%7B%22User%22%3A%7B%22id%22%3A38710%7D%2C%22%5B%5D%22%3A%7B%22Moment%22%3A%7B%22userId%22%3A38710%7D%2C%22%5B%5D%22%3A%7B%22Comment%22%3A%7B%22momentId%40%22%3A%22%255B%255D%252FMoment%252Fid%22%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%7D)<br /><br />③如果User之前已经获取到了，还可以这样省去所有重复User<br />[base_url/get/{"[]":{"page":0, "count":3, "Moment":{"userId":1}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}](http://139.196.140.118:8080/get/%7B%22%5B%5D%22%3A%7B%22Moment%22%3A%7B%22userId%22%3A38710%7D%2C%22%5B%5D%22%3A%7B%22Comment%22%3A%7B%22momentId%40%22%3A%22%255B%255D%252FMoment%252Fid%22%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%7D)
 
 服务端对应不同请求的返回结果 | 传统方式 | APIJSON
-------- | ------------ | ------------
 User | {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}}
 User和对应的Moment | 分别返回两次请求的结果<br />User: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}}<br />Moment: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}, "Moment":{"id":1, "content":"xxx"...}}
 User列表 | {"status":200, "message":"success", "data":[{"id":1, "name":"xxx"...}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}}, "1":{"User":{"id":2...}}...}}
 Moment列表，每个Moment包括发布者User和前3条Comment | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "User":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}
 User发布的Moment列表，每个Moment包括发布者User和前3条Comment | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | 以上不同请求方法的结果:<br />①{"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}, "Moment":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />②{"status":200, "message":"success", "User":{...}, "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />③{"status":200, "message":"success", "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}


1.APIJSON不需要接口、文档及兼容旧版客户端的特性仅针对GET和HEAD请求，好在这两个在所有请求里占大部分。<br >
2.base_url指基地址，一般是顶级域名，其它分支url都是在base_url后扩展。如base_url:http://www.google.com/ ，对应的GET分支url:http://www.google.com/get/ ，下同。<br >
3.status，指返回结果中的状态码，200表示成功，其它都是错误码，值全部都是HTTP标准状态码。下同。<br >
4.message，指返回结果中的状态信息，对成功结果或错误原因的详细说明。下同。<br >
5.status和message总是在返回结果的同一层级成对出现。对所有请求的返回结果都会在最外层有一对总结式status和message。对非GET、HEAD请求，返回结果里面的每个JSONObject里都会有一对status和message说明这个JSONObject的状态。下同。



## 请求方法、URL、Request、Response对应关系总览

  方法及说明 | URL | Request | Response
------------ | ------------ | ------------ | ------------
GET：普通获取请求，明文，可用浏览器调试 | base_url/get/ | {TableName:{…}}，{…}内为限制条件。<br >例如获取一个id为1的Moment：<br >{"Moment":{"id":1}} | {TableName:{...}, "status":200, "message":"success"}<br >例如<br >{"Moment":{"id":1, "userId":1, "content":"APIJSON,let interfaces and documents go to hell !"}, "status":200, "message":"success"}
HEAD：普通获取数量请求，明文，可用浏览器调试 | base_url/head/ | {TableName:{…}}，{…}内为限制条件。<br >例如<br >{"Moment":{"userId":1}}，获取一个id为1的User所发布的Moment总数 | {TableName:{"status":200, "message":"success", "count":10}, "status":200, "message":"success"}<br >例如<br >{"Moment":{"status":200, "message":"success", "count":10}, "status":200, "message":"success"}
POST_GET：安全/私密获取请求，非明文，用于获取密码、钱包等对安全性要求高的数据 | base_url/post_get/ | 最外层加一个"tag":tag，其它同GET | 同GET
POST_HEAD：安全/私密获取数量请求，非明文，用于获取银行卡数量等对安全性要求高的数据 | base_url/post_head/ | 最外层加一个"tag":tag，其它同HEAD | 同HEAD
POST：新增数据，非明文 | base_url/post/ | {TableName:{…}, "tag":tag}，{…}中id由服务端生成，客户端不能传。<br >例如<br >{"Moment":{"userId":1, "content":"APIJSON,let interfaces and documents go to hell !"}, "tag":"Moment"} | {TableName:{"status":200, "message":"success", "id":1}, "status":200, "message":"success"}<br >例如<br >{"Moment":{"status":200, "message":"success", "id":1}, "status":200, "message":"success"}
PUT：修改数据，非明文 | base_url/put/ | {TableName:{"id":id,…}, "tag":tag}，{…}中id必传。<br >例如<br >{"Moment":{"id":1,"content":"APIJSON,let interfaces and documents go to hell !"}, "tag":"Moment"} | 同POST
DELETE：删除数据，非明文 | base_url/delete/ | {TableName:{"id":id}, "tag":tag}，{…}中id必传，一般只传id。<br >例如<br >{"Moment":{"id":1}, "tag":"Moment"} | 同POST


1.TableName指要查询的table的名称字符串。第一个字符为大写字母，剩下的字符要符合英语字母、数字、下划线中的任何一种。对应的值为内部所传字段符合对应Table的JSONObject，结构是{...}<br >
2."tag":tag 后面的tag是非GET、HEAD请求中匹配请求的JSON结构的key，一般是要查询的table的名称，由服务端Request表中指定。<br >
3.非GET、HEAD请求的方法、tag、结构必须和服务端Request表中指定的一一对应，否则请求将不被通过。



## 功能符
 
  键值对格式 | 功能与作用 | 使用示例
------------ | ------------ | ------------
 "key[]":{}，后面是标准的JSONObject | 查询数组 | ["User[]":{"User":{"sex":1}}](http://139.196.140.118:8080/get/{"User[]":{"count":3,"User":{"sex":1}}})，查询性别为女的一个User数组，请求完成后会变为 "User[]":{"0":{"User":{"id":38710,"sex":1,"name":"Tommy"...}}, "1":{"User":{"id":82001,"sex":1,"name":"Lemon"...}} ...}
 "key{}":[]，后面是标准的JSONArray，作为key可取的值的选项 | 匹配选项范围 | "id{}":[38710,82001,70793]，查询id符合38710,82001,70793中任意一个的Object。一般用于查询一个数组。请求[{"[]":{"User":{"id{}":[38710,82001,70793]}}}](http://139.196.140.118:8080/get/{"[]":{"count":3,"User":{"id{}":[38710,82001,70793]}}})会返回一个User数组，例如上面那个。
 "key{}":"条件0,条件1..."，条件为任意SQL比较表达式字符串，非Number类型必须用''包含条件的值，如'a' | 匹配条件范围 | "id{}":">=80000,\<=100000"，查询id在80000,100000中任意一个的Object。一般用于查询一个数组。请求[{"[]":{"User":{"id{}":">=80000,\<=100000"}}}](http://139.196.140.118:8080/get/{"[]":{"count":3,"User":{"id{}":">=80000,\<=100000"}}})会返回一个User数组，例如上面那个。
 "key()":"函数表达式"， 函数表达式为 function(Type0:value0,Type1:value1...) | 远程调用函数 |  ["isPraised()":"contains(Collection:praiseUserIdList,userId)"](http://139.196.140.118:8080/get/{"Moment":{"id":301,"isPraised()":"contains(Collection:praiseUserIdList,userId)"}})，请求完成后会调用 boolean contains(Collection collection, Object object) 函数，然后变为 "isPraised":true 这种（假设点赞用户id列表包含了userId，即这个User点了赞）。函数参数类型为Object时可用 value 替代 Object:value。
 "key@":"依赖路径"，依赖路径为用/分隔的字符串 | 依赖引用 | ["userId@":"/User/id"](http://139.196.140.118:8080/get/%7B%22User%22%3A%7B%22id%22%3A38710%7D%2C%22Moment%22%3A%7B%22userId%40%22%3A%22%252FUser%252Fid%22%7D%7D)，userId依赖引用同级User内的id值，假设id=1，则请求完成后会变成 "userId":1
 "key$":"SQL搜索表达式"，任意SQL搜索表达式字符串，如 %key%, %k%e%y% 等 | 模糊搜索 | "name$":"%m%"，搜索包含m的名字。一般用于查询一个数组。请求 [{"[]":{"User":{"name$":"%m%"}}}](http://139.196.140.118:8080/get/%7B%22%5B%5D%22%3A%7B%22User%22%3A%7B%22name%24%22%3A%22%2525m%2525%22%7D%2C%22count%22%3A3%2C%22page%22%3A0%7D%7D) 会返回name包含"m"的User数组。
 "@key":key指定类型的Object | @key为JSONObject中的关键字，作用各不相同，但都不作为查询匹配条件 | ① 只查询id,sex,name这几列并且请求结果也按照这个顺序：<br />["@columns":"id,sex,name"](http://139.196.140.118:8080/get/{"User":{"@columns":"id,sex,name","id":38710}})<br />返回<br />{<br /> &nbsp; "id":1,<br /> &nbsp; "sex":0,<br /> &nbsp; "name":"Lemon"<br />}<br /> ② 从pictureList获取第0张图片：<br />[{<br /> &nbsp; "pictureList":["url0","url1"],<br /> &nbsp; "@position":0, //这里@position为自定义关键词<br /> &nbsp; "firstPicture()":"get(Collection:pictureList,int:@position)"<br />}](http://139.196.140.118:8080/get/{"User":{"id":38710,"@position":0,"firstPicture()":"get(Collection:pictureList,int:@position)"}})<br />返回<br />{<br /> &nbsp; "pictureList":["url0","url1"],<br /> &nbsp; "@position":0,<br /> &nbsp; "firstPicture":"url0"<br />}<br /> ...

## 使用方法

### 1.下载后解压APIJSON工程

Clone or download &gt; Download ZIP &gt; 解压到一个路径并记住这个路径。

#### 你可以跳过步骤2和步骤3，用我的服务器IP地址 139.196.140.118:8080 来测试服务端对客户端请求的返回结果。

### 2.导入MySQL table文件

服务端需要MySQL Server和MySQLWorkbench，没有安装的都先下载安装一个。<br />
我的配置是Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 和 OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8，其中系统和软件都是64位的。

启动MySQLWorkbench &gt; 进入一个Connection &gt; 点击Server菜单 &gt; Data Import &gt; 选择刚才解压路径下的APIJSON-Master/table &gt; Start Import &gt; 刷新SCHEMAS, 左下方sys/tables会出现添加的table。

### 3.用Eclipse for JavaEE或IntellIJ IDEA Ultimate运行服务端工程

如果以上编辑器一个都没安装，运行前先下载安装一个。<br />
我的配置是Windows 7 + JDK 1.7.0_71 + Eclipse 4.6.1 + IntellIJ 2016.3 和 OSX EI Capitan + JDK 1.8.0_91 + Eclipse 4.6.1 + IntellIJ 2016.2.5

#### Eclipse for JavaEE

1.导入<br />
File > Import > Maven > Existing Maven Projects > Next > Browse > 选择刚才解压路径下的APIJSON-Master/APIJSON(Server)/APIJSON(Eclipse_JEE) > Finish

2.运行<br />
Run > Run As > Java Application > 选择APIJSONApplication > OK

#### IntellIJ IDEA Ultimate

1.导入<br />
Open > 选择刚才解压路径下的APIJSON-Master/APIJSON(Server)/APIJSON(Idea) > OK

2.运行<br />
Run > Run APIJSONApplication

### 4.用ADT Bundle或Android Studio运行客户端工程

可以跳过这个步骤，直接下载下方提供的客户端App。

如果以上编辑器一个都没安装，运行前先下载安装一个。<br />
我的配置是Windows 7 + JDK 1.7.0_71 + ADT Bundle 20140702 + Android Studio 2.2 和 OSX EI Capitan +（JDK 1.7.0_71 + ADT Bundle 20140702）+（JDK 1.8.0_91 + Android Studio 2.1.2），其中系统和软件都是64位的。

#### ADT Bundle

1.导入<br />
File > Import > Android > Existing Android Code Into Workspace > Next > Browse > 选择刚才解压路径下的APIJSON-Master/APIJSON(Android)/APIJSON(ADT) > Finish

2.运行<br />
Run > Run As > Android Application

#### Android Studio

1.导入<br />
Open an existing Android Studio project > 选择刚才解压路径下的APIJSON-Master/APIJSON(Android)/APIJSON(AndroidStudio) > OK

2.运行<br />
Run > Run app

### 5.操作客户端App

选择发送APIJSON请求并等待显示结果。<br />
如果默认url不可用，修改为一个可用的，比如正在运行APIJSON服务端工程的电脑的IPV4地址，然后点击查询按钮重新请求。

##关于作者
TommyLemon：[https://github.com/TommyLemon](https://github.com/TommyLemon)<br />
如果有什么问题或建议可以发我邮件，交流技术，分享经验 ^_^

## 下载试用客户端App

[APIJSONClientApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSONApp.apk)


## 更新日志
[https://github.com/TommyLemon/APIJSON/commits/master](https://github.com/TommyLemon/APIJSON/commits/master)

## 欢迎Star，欢迎Fork

[https://github.com/TommyLemon/APIJSON](https://github.com/TommyLemon/APIJSON)
