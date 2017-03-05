# APIJSON

[查看中文文档](https://github.com/TommyLemon/APIJSON/blob/master/README.md)

APIJSON is a JSON Transmission Structure Protocol.

You can set any JSON structure and request your server, and the server will return a JSON String with the structure you had set.<br />
You can get any data with any JSON structure by requesting server just once. It's very convenient and flexible, and does not require a special interface or multiple requests.<br />
It provides additions and deletions, fuzzy search, remote function calls and so on. And you can save duplicate data and improve transmission speed as well!<br />

Now you can realize JSON Transmissions without any interface or doc anymore!<br />
Client developers will no longer be suffered from various error in docs, and don't have to communicate with server developers about interfaces or docs anymore!<br />
And server developers no longer have to write new interfaces and docs for compatibility with legacy apps! And they will no longer be endlessly disturbed by client developers at any time!

Here is an example(Request an Moment list like Wechat friend circle data):

### Request: 

<p>{<br />
&nbsp; &nbsp; &quot;[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //request an array<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 1, &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//array condition<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 2, &nbsp; &nbsp; &nbsp; &nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;//request an object from the table named User<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;: 0 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //object condition<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId@&quot;: &ldquo;/User/id&rdquo; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //rely path with default parent path,starts from the same level object's path<br />
&nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment[]&quot;: { &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; //request an array named Comment&nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;page&quot;: 0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;count&quot;: 3,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Comment&quot;: {<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&quot;workId@&quot;: &ldquo;[]/Work/id&rdquo; &nbsp; //full rely path<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;}<br />
&nbsp; &nbsp; &nbsp; &nbsp; }<br />
&nbsp; &nbsp; }<br />
}</p>



### Response:

<p>{<br />
&nbsp; &nbsp; &quot;[]&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &quot;0&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;User&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:38710,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300038710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-38710&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:470,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-470&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:38710,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000&quot;<br />
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
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:70793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;sex&quot;:0,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;phone&quot;:&quot;1300070793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;name&quot;:&quot;Name-70793&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;head&quot;:&quot;http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000&quot;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;Work&quot;:{<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;id&quot;:170,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;title&quot;:&quot;Title-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;content&quot;:&quot;This is a Content...-73&quot;,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;userId&quot;:70793,<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &quot;picture&quot;:&quot;http://my.oschina.net/img/portrait.gif?t=1451961935000&quot;<br />
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



![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_0.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_1.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_2.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_3.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/server_idea_log_complex.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_request.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_user.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_moment.jpg) 

## Compare with Previous HTTP Transmission Way
 
 Process | Previous way | APIJSON
-------- | ------------ | ------------
 Transmission | Server developers edit interfaces and update docs, then client developers request server and parse responses according to the docs | Client developers request server and parse responses for their requirements. No inteface! No doc! No communication for any interface or doc between client and server developers! 
 Compatibility | Server developers add new interfaces tagged with v2 and update docs | Nothing need to do!
 
 Client request | Previous way | APIJSON
-------- | ------------ | ------------
 Requirement | Client developers append key-value pairs to an url for a request in docs | Client developers append JSON to the url for their requirements
 Structure | base_url/lowercase_table_name?key0=value0&key1=value1...<br />&currentUserId=100&loginPassword=1234<br /><br />The currentUserId and loginPassword is only for parts of interfaces | base_url/{TableName0:{key0:value0, key1:value1 ...}, TableName1:{...}...<br />, currentUserId:100, loginPassword:1234}<br /><br />The currentUserId and loginPassword is only for parts of interfaces
 URL | Different urls for different requests | One url for one method(GET,POST...)
 Key-Value Pair | key=value | key:value
 
 Server operation | Previous way | APIJSON
-------- | ------------ | ------------
 Parse and response | Get key-value pairs and query the database with them by the default way, then encapsulate a JSON, finally return the JSON to client | Just return what RequestParser#parse returned
 Way of setting JSON structure to return | Designed in server and cannot be modified by any client apps | Designed by client apps and cannot be modified by sever
 
 Client parse | Previous way | APIJSON
-------- | ------------ | ------------
 View structure | Search docs or view logs after responses for requests | Just view the requests, and viewing logs after responses for requests is also supported
 Operate | Parse JSON String from responses | Parse with JSONResponse or use previous way

 Client requests for different requirements | Previous way | APIJSON
-------- | ------------ | ------------
 User | http://localhost:8080/get/user?id=1 | http://localhost:8080/get/{"User":{"id":1}}
 User and his Work | Request twice<br />User: http://localhost:8080/get/user?id=1<br />Work: http://localhost:8080/get/work?userId=1 | http://localhost:8080/get/{"User":{"id":1}, "Work":{"userId@":"User/id"}}
 User list | http://localhost:8080/get/user/list?page=1&count=5&sex=0 | http://localhost:8080/get/{"[]":{"page":1, "count":5, "User":{"sex":0}}}
 Work list of which type is 1, each Work contains it's publisher User and top 3 Comments | The Work must contains the User Object and Comment Array<br /> http://localhost:8080/get/work/list?page=1&count=5&type=1&commentCount=3 | http://localhost:8080/get/{"[]":{"page":1, "count":5, "Work":{"type":1}, "User":{"workId@":"/Work/id"}, "[]":{"count":3, "Comment":{"workId@":"[]/Work/id"}}}}
 Work list of an User, each Work contains the publisher User and top 3 Comments | Change type=1 to userId=1 above | Here are several ways:<br />①Change  "Work":{"type":1}, "User":{"workId@":"/Work/id"} to "User":{"id":1}, "Work":{"userId@":"/User/id"} above<br /><br />②Or save 4 repeated User by this way<br />http://localhost:8080/get/{"User":{"id":1}, "[]":{"page":1, "count":5, "Work":{"userId@":"User/id"}, "[]":{"count":3, "Comment":{"workId@":"[]/Work/id"}}}}<br /><br />③If the User is already obtained, you can also save all repeated User by this way<br />http://localhost:8080/get/{"[]":{"page":1, "count":5, "Work":{"userId":1}, "[]":{"count":3, "Comment":{"workId@":"[]/Work/id"}}}}
 
 Server responses for different requests | Previous way | APIJSON
-------- | ------------ | ------------
 User | {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}}
 User and his Work | Reponse twice<br />User: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}}<br />Work: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}, "Work":{"id":1, "content":"xxx"...}}
 User list | {"status":200, "message":"success", "data":[{"id":1, "name":"xxx"...}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}}, "1":{"User":{"id":2...}}...}}
 Work list of which type is 1, each Work contains it's publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "User":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}
 Work list of an User, each Work contains the publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | Results for the ways above:<br />①{"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}, "Work":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />②{"status":200, "message":"success", "User":{...}, "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />③{"status":200, "message":"success", "[]":{"0":{"Work":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}

## Usage

### 1.Download and unzip APIJSON project

Clone or download > Download ZIP > Unzip to a path and remember it.

#### You can skip step 2 and 3, and test server response with my server IP address 139.196.140.118:8080.<br />

### 2.Import MySQL table files

This Server project needs MySQL Server and MySQLWorkbench. And you must ensure that both of them were installed.<br />
My config is Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 and OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8. The systems and softwares are all 64 bit.

Start MySQLWorkbench > Enter a connection > Click Server menu > Data Import > Select the path of APIJSON-Master/table > Start Import > Refresh SCHEMAS, and you'll see the tables were already added.

### 3.Run Server project with Eclipse for JavaEE or IntellIJ IDEA Ultimate

If you haven't installed any editor above, please download and install one before run.<br />
My config is Windows 7 + JDK 1.7.0_71 + Eclipse 4.6.1 + IntellIJ 2016.3 and OSX EI Capitan + JDK 1.8.0_91 + Eclipse 4.6.1 + IntellIJ 2016.2.5. The systems and softwares are all 64 bit.

#### Eclipse for JavaEE

1.Import<br />
File > Import > Maven > Existing Maven Projects > Next > Browse > Select the path of APIJSON-Master/APIJSON(Server)/APIJSON(Eclipse_JEE) > Finish

2.Run<br />
Run > Run As > Java Application > Select APIJSONApplication > OK

#### IntellIJ IDEA Ultimate

1.Import<br />
Open > Select the path of APIJSON-Master/APIJSON(Server)/APIJSON(Idea) > OK

2.Run<br />
Run > Run APIJSONApplication

### 4.Run Client project with ADT Bundle or Android Studio

You can skip this step and download the Client App below.

If you haven't installed any editor above, please download and install one before run.<br />
My config is Windows 7 + JDK 1.7.0_71 + ADT Bundle 20140702 + Android Studio 2.2 and OSX EI Capitan + (JDK 1.7.0_71 + ADT Bundle 20140702) + (JDK 1.8.0_91 + Android Studio 2.1.2). The systems and softwares are all 64 bit.

#### ADT Bundle

1.Import<br />
File > Import > Android > Existing Android Code Into Workspace > Next > Browse > Select the path of APIJSON-Master/APIJSON(Android)/APIJSON(ADT) > Finish

2.Run<br />
Run > Run As > Android Application

#### Android Studio

1.Import<br />
Open an existing Android Studio project > Select the path of APIJSON-Master/APIJSON(Android)/APIJSON(AndroidStudio) > OK

2.Run<br />
Run > Run app

### 5.Operate Client app

Select an APIJSON request to send to server and wait. It will show the result received.<br />
If the default url is not available, change it to an available one, such as an IPV4 address of a computer running APIJSON Server project. Then click the Query button to request again.

## Download Client App

[APIJSONClientApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSON%28ADT%29.apk)

### If you have any questions or suggestions about APIJSON, you can send me an e-mail to tommylemon@qq.com.

## Update Log
[https://github.com/TommyLemon/APIJSON/commits/master](https://github.com/TommyLemon/APIJSON/commits/master)

## Welcome star, welcome fork

[https://github.com/TommyLemon/APIJSON](https://github.com/TommyLemon/APIJSON) 

# APIJSON, let interfaces go to hell !
