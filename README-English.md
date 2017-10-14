# APIJSON 
[![GitHub release](https://img.shields.io/github/release/TommyLemon/APIJSON.svg)](https://github.com/TommyLemon/APIJSON/releases)
[![Java API](https://img.shields.io/badge/Java-1.6%2B-brightgreen.svg?style=flat)](http://www.oracle.com/technetwork/java/api-141528.html) 
[![Android API](https://img.shields.io/badge/Android-15%2B-brightgreen.svg?style=flat)](https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels) 
[![JavaScrpit API](https://img.shields.io/badge/JavaScript-ES5%2B-brightgreen.svg?style=flat)](http://www.ecma-international.org/publications/standards/Standard.htm)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

<br />

[Java-Server](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Java-Server)    [Android](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Android)    [JavaScript](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript)    [Vue.js](https://github.com/TommyLemon/APIJSON-JS-Vue)

[查看中文文档](https://github.com/TommyLemon/APIJSON/blob/master/README.md)

[Online Test](http://39.108.143.172)

<br />

* ### [1.About](#1)
* ### [2.Compare](#2)
* [2.1 Process](#2.1)
* [2.2 Client request](#2.2)
* [2.3 Server operate](#2.3)
* [2.4 Client resolve](#2.4)
* [2.5 Requests](#2.5)
* [2.6 Responses](#2.6)
* ### [3.Overview](#3)
* [3.1 Operation](#3.1)
* [3.2 Function](#3.2)
* ### [4.Usage](#4)
* [4.1 Download and unzip](#4.1)
* [4.2 Import tables](#4.2)
* [4.3 Run server](#4.3)
* [4.4 Run client](#4.4)
* [4.4 Operate app](#4.5)
* ### [5.Extra](#5)
* [5.1 Recommend](#5.1)
* [5.2 Author](#5.2)
* [5.3 Download](#5.3)
* [5.4 Update](#5.4)
* [5.5 Star&Fork](#5.5)

## <h2 id="1">1.About<h2/>

APIJSON is a JSON Transmission Structure Protocol.

You can set any JSON structure and request your server, and the server will response a JSON with the structure you had set.<br />
You can get any data by requesting server just once. It's very convenient and flexible, and does not require a special api or multiple requests.<br />
It provides CRUD(read and write), Fuzzy Search, Remote Function Calls, Rights Management and so on. And you can save duplicate data and improve transmission speed as well!<br />

Now you can realize JSON Transmissions without any api or document anymore!<br />
Client developers will no longer be suffered from various error in documents, and don't have to communicate with server developers about apis or documents anymore!<br />
And server developers no longer have to write new apis and documents for compatibility with legacy apps! And they will no longer be endlessly disturbed by client developers at any time!



![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_0.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_1.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_2.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_3.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/server_idea_log_complex.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_request.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_user.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_moment.jpg) 

<br />
<br />

### Here are some examples:

#### Get an User
Request:
<pre><code class="language-json">
{
  "User":{
  }
}
</code></pre>

[Click here to test](http://39.108.143.172:8080/get/{"User":{}})

Response:
<pre><code class="language-json">
{
  "User":{
    "id":38710,
    "sex":0,
    "name":"TommyLemon",
    "certified":true,
    "tag":"Android&Java",
    "phone":13000038710,
    "head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000",
    "date":1485948110000,
    "pictureList":[
      "http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000",
      "http://common.cnblogs.com/images/icon_weibo_24.png"
    ]
  },
  "code":200,
  "msg":"success"
}
</code></pre>

<br />


#### Get an Array of Users
Request:
<pre><code class="language-json">
{
  "[]":{
    "count":3,             //just get 3 results
    "User":{
      "@column":"id,name"  //just get ids and names
    }
  }
}
</code></pre>

[Click here to test](http://39.108.143.172:8080/get/{"[]":{"count":3,"User":{"@column":"id,name"}}})

Response:
<pre><code class="language-json">
{
  "[]":[
    {
      "User":{
        "id":38710,
        "name":"TommyLemon"
      }
    },
    {
      "User":{
        "id":70793,
        "name":"Strong"
      }
    },
    {
      "User":{
        "id":82001,
        "name":"Android"
      }
    }
  ],
  "code":200,
  "msg":"success"
}
</code></pre>

<br />

#### Get an Moment and it's publisher(User)
Request:
<pre><code class="language-json">
{
  "Moment":{
  },
  "User":{
    "id@":"Moment/userId"  //User.id = Moment.userId
  }
}
</code></pre>

[Click here to test](http://39.108.143.172:8080/get/{"Moment":{},"User":{"id@":"Moment%252FuserId"}})

Response:
<pre><code class="language-json">
{
  "Moment":{
    "id":12,
    "userId":70793,
    "date":"2017-02-08 16:06:11.0",
    "content":"1111534034"
  },
  "User":{
    "id":70793,
    "sex":0,
    "name":"Strong",
    "tag":"djdj",
    "head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000",
    "contactIdList":[
      38710,
      82002
    ],
    "date":"2017-02-01 19:21:50.0"
  },
  "code":200,
  "msg":"success"
}
</code></pre>

<br />


#### Get an Array of Moments
Request:
<pre><code class="language-json">
{
  "[]":{                             //request an Array
    "page":0,                        //Array condition
    "count":2,
    "Moment":{                       //request an Object named Moment
      "content$":"%a%"               //Object condition, search the Moments in which their contents contain 'a'
    },
    "User":{
      "id@":"/Moment/userId",        //User.id = Moment.userId, reference path with it's grandfather's path omitted
      "@column":"id,name,head"       //set the columns in the response
    },
    "Comment[]":{                    //request an Array named Comment and extract Comments
      "count":2,
      "Comment":{
        "momentId@":"[]/Moment/id"   //Comment.momentId = Moment.id, full reference path
      }
    }
  }
}
</code></pre>

[Click here to test](http://39.108.143.172:8080/get/{"[]":{"page":0,"count":2,"Moment":{"content$":"%2525a%2525"},"User":{"id@":"%252FMoment%252FuserId","@column":"id,name,head"},"Comment[]":{"count":2,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})

Response:
<pre><code class="language-json">
{
  "[]":[
    {
      "Moment":{
        "id":15,
        "userId":70793,
        "date":1486541171000,
        "content":"APIJSON is a JSON Transmission Structure Protocol…",
        "praiseUserIdList":[
          82055,
          82002,
          82001
        ],
        "pictureList":[
          "http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000",
          "http://common.cnblogs.com/images/icon_weibo_24.png"
        ]
      },
      "User":{
        "id":70793,
        "name":"Strong",
        "head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"
      },
      "Comment[]":[
        {
          "id":176,
          "toId":166,
          "userId":38710,
          "momentId":15,
          "date":1490444883000,
          "content":"thank you"
        },
        {
          "id":1490863469638,
          "toId":0,
          "userId":82002,
          "momentId":15,
          "date":1490863469000,
          "content":"Just do it"
        }
      ]
    },
    {
      "Moment":{
        "id":58,
        "userId":90814,
        "date":1485947671000,
        "content":"This is a Content...-435",
        "praiseUserIdList":[
          38710,
          82003,
          82005,
          93793,
          82006,
          82044,
          82001
        ],
        "pictureList":[
          "http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"
        ]
      },
      "User":{
        "id":90814,
        "name":7,
        "head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000"
      },
      "Comment[]":[
        {
          "id":13,
          "toId":0,
          "userId":82005,
          "momentId":58,
          "date":1485948050000,
          "content":"This is a Content...-13"
        },
        {
          "id":77,
          "toId":13,
          "userId":93793,
          "momentId":58,
          "date":1485948050000,
          "content":"This is a Content...-77"
        }
      ]
    }
  ],
  "code":200,
  "msg":"success"
}
</code></pre>


<br />

[Online Test](http://39.108.143.172)

<br />
<br />


## <h2 id="2">2.Compare with Previous HTTP Transmission Way<h2/>
 
### <h3 id="2.1">2.1 Process<h3/>
 Process | Previous way | APIJSON
-------- | ------------ | ------------
 Transmission | Server developers edit apis and update documents, then client developers request server and parse responses according to the documents | Client developers request server and parse responses for their requirements. No inteface! No document! No communication for any api or document between client and server developers! 
 Compatibility | Server developers add new apis tagged with v2 and update documents | Nothing need to do!
 
<br />

### <h3 id="2.2">2.2 Client request<h3/>
 Client request | Previous way | APIJSON
-------- | ------------ | ------------
 Requirement | Client developers append key-value pairs to an url for a request in documents | Client developers append JSON to the url for their requirements
 Structure | base_url/get/table_name?<br />key0=value0&key1=value1...<br /><br />Only one table_name can be contained in an URL | base_url/get/<br />{<br > &nbsp;&nbsp; TableName0:{<br > &nbsp;&nbsp;&nbsp;&nbsp; key0:value0,<br > &nbsp;&nbsp;&nbsp;&nbsp; key1:value1,<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; TableName1:{<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; }<br > &nbsp;&nbsp; ...<br > }<br /><br />You can add TableNames as many as you want after an URL
 URL | Different urls for different requests. The more diffirent kinds of requests, the more different urls | One url for one method(GET,POST...), most requests use the same URL of the 7 common ones
 Key-Value Pair | key=value | key:value

<br />
 
 ### <h3 id="2.3">2.3 Server operate<h3/>
 Server operate | Previous way | APIJSON
-------- | ------------ | ------------
 Parse and response | Get key-value pairs and query the database with them by the default way, then encapsulate a JSON, finally return the JSON to clients | Just return what Parser#parse returned
 Way of setting JSON structure of Response | Designed in servers and cannot be modified by clients | Designed by clients and cannot be modified by servers

<br />
 
 ### <h3 id="2.4">2.4 Client resolve<h3/>
 Client resolve | Previous way | APIJSON
-------- | ------------ | ------------
 View structure | Search documents or view logs after responses for requests | Just view the requests, and viewing logs after responses for requests is also supported
 Operate | Parse JSON String from responses | Parse with JSONResponse or use previous way

<br />

### <h3 id="2.5">2.5 Requests<h3/>
 Requests | Previous way | APIJSON
-------- | ------------ | ------------
 User | base_url/get/user?id=38710 | [base_url/get/<br >{<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"User":{"id":38710}})
 Moment and it's publisher(User) | Request twice<br />Moment: <br /> base_url/get/moment?userId=38710<br /><br />User: <br /> base_url/get/user?id=38710 | Just request once<br />[base_url/get/<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp; }, <br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"Moment":{"userId":38710},"User":{"id":38710}})
 User list | base_url/get/user/list?<br />page=0&count=3&sex=0 | [base_url/get/<br >{<br > &nbsp;&nbsp; "User[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp;  "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "sex":0<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"User[]":{"page":0,"count":3,"User":{"sex":0}}})
 A list, each item contains<br /> a Moment, a publisher(User)<br /> and a list of top 3 Comments | The Moment must contains an User Object and a Comment Array<br /><br /> base_url/get/moment/list?<br />page=0&count=3&commentCount=3 | [base_url/get/<br >{<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0, <br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{}, <br > &nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id@":"/Moment/userId"<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"[]":{"page":0,"count":3,"Moment":{},"User":{"id@":"%252FMoment%252FuserId"},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})
 A list, each item contains<br /> a Moment, the same publisher(User)<br /> and a list of top 3 Comments | Each Moment must contains an User Object and a Comment Array <br /><br /> base_url/get/moment/list?<br />page=0&count=3<br />&commentCount=3&userId=38710 | Here are several ways:<br /> ① Change  <br >"Moment":{}, "User":{"id@":"/Moment/userId"}<br > to <br >["Moment":{"userId":38710}, "User":{"id":38710}](http://39.108.143.172:8080/get/{"[]":{"page":0,"count":3,"Moment":{"userId":38710},"User":{"id":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}}) <br /><br /> ② Or save repeated Users by this way<br />[base_url/get/<br >{<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp;&nbsp;&nbsp; }, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"User":{"id":38710},"[]":{"page":0,"count":3,"Moment":{"userId":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})<br /><br /> ③ If the User is already obtained, you can also save all repeated User by this way<br />[base_url/get/<br >{<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://39.108.143.172:8080/get/{"[]":{"page":0,"count":3,"Moment":{"userId":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})

<br />
 
 ### <h3 id="2.6">2.6 Responses<h3/>
 Responses | Previous way | APIJSON
-------- | ------------ | ------------
 User | {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | {<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 Moment and it's publisher(User) | Get the Moment from the first response,<br /> and take it's userId as the value of User's id,<br /> then send the second request to get the User<br /><br /> Moment: <br > {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} <br /><br /> User: <br > {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | Response only once, and no longer needs to waiting too long, relating 2 responses, switching threads and so on <br /><br /> {<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 User list | {<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":82001,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | {<br > &nbsp;&nbsp; "User[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":82001,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 A list, each item contains<br /> a Moment, a publisher(User)<br /> and a list of top 3 Comments | Eech Moment must contains<br > it's publisher(User)<br > and a list of top 3 Comments <br /><br /> {<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":301,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | 1.Flexible structures, you can combine the Objects and Arrays as you want<br />2.Loose couplings, the structure is clearer<br /><br />{<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":301,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 A list, each item contains<br /> a Moment, the same publisher(User)<br /> and a list of top 3 Comments | 1.Many repeated Users, a waste of data traffic and server performance<br />2.Difficult to optimize since the needs of expanding apis and writing documents, and then calling the apis according to the documents <br /><br />{<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":470,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":511,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":595,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | Differences responses for the requests above:<br /><br /> ① Common request <br > {<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br /><br /> ② Save repeated Users <br > {<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br /><br /> ③ Save all repeated Users <br > {<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}

<br />
<br />

## <h2 id="3">3.Overview<h2/>

### <h3 id="3.1">3.1 Operation<h3/>

  Method | URL | Request | Response
------------ | ------------ | ------------ | ------------
GET: <br > 普通获取数据，<br > 明文，<br > 可用浏览器调试 | base_url/get/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; … <br > &nbsp;&nbsp; }<br >} <br > {…}内为限制条件<br ><br > 例如获取一个id为235的Moment：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235<br > &nbsp;&nbsp; }<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br >例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br > }
HEAD: <br > 普通获取数量，<br > 明文，<br > 可用浏览器调试 | base_url/head/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; }<br > } <br > {…}内为限制条件 <br ><br > 例如获取一个id为38710的User所发布的Moment总数：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp; }<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":10<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} <br > 例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":10<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;  "msg":"success"<br >}
GETS: <br > 安全/私密获取数据，<br >非明文，<br > 用于获取钱包等<br >对安全性要求高的数据 | base_url/post_get/ | 最外层加一个"tag":tag，其它同GET | 同GET
HEADS: <br > 安全/私密获取数量，<br >非明文，<br > 用于获取银行卡数量等<br >对安全性要求高的数据总数 | base_url/post_head/ | 最外层加一个"tag":tag，其它同HEAD | 同HEAD
POST: <br > 新增数据，<br > 非明文 | base_url/post/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…}中id由服务端生成，不能传 <br ><br >例如一个id为38710的User发布一个新Moment：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Moment"<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br >例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":120<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
PUT: <br > 修改数据，<br > 非明文，<br > 只修改所传的字段 | base_url/put/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":id,<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…}中id必传 <br ><br >例如修改id为235的Moment的content：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Moment"<br >} | 同POST
DELETE: <br > 删除数据，<br > 非明文 | base_url/delete/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":id<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…}中id必传，一般只传id <br ><br >例如删除id为120的Moment：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":120<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Moment"<br >} | 同POST


1.TableName指要查询的数据库表Table的名称字符串。第一个字符为大写字母，剩下的字符要符合英语字母、数字、下划线中的任何一种。对应的值的类型为JSONObject，结构是 {...}，里面放的是Table的字段(列名)。下同。<br >
2."tag":tag 后面的tag是非GET、HEAD请求中匹配请求的JSON结构的key，一般是要查询的table的名称，由服务端Request表中指定。下同。<br >
3.GET、HEAD请求是开放请求，可任意组合任意嵌套。其它请求为受限制的安全/私密请求，对应的 方法、tag、结构 都必须和 服务端Request表中所指定的 一一对应，否则请求将不被通过。下同。<br >
4.GETS与GET、HEADS与HEAD分别为同一类型的操作方法，请求稍有不同但返回结果相同。下同。<br >
5.在HTTP通信中，GET、HEAD方法一般用HTTP GET请求，其它一般用HTTP POST请求。下同。<br >
6.所有JSONObject都视为容器(或者文件夹)，结构为 {...} ，里面可以放普通对象或子容器。下同。<br >
7.每个对象都有一个唯一的路径(或者叫地址)，假设对象名为refKey，则用 key0/key1/.../refKey 表示。下同。

<br >

### <h3 id="3.2">3.2 Function<h3/>
 
 Function | Key-Value | Example
------------ | ------------ | ------------
 Array | "key[]":{}，后面是JSONObject，key可省略。当key和里面的Table名相同时，Table会被提取出来，即 {Table:{Content}} 会被转化为 {Content} | [{"User[]":{"User":{}}}](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{}}})，查询一个User数组。这里key和Table名都是User，User会被提取出来，即 {"User":{"id", ...}} 会被转化为 {"id", ...} 
 Options | "key{}":[]，后面是JSONArray，作为key可取的值的选项 | ["id{}":[38710,82001,70793]](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{"id{}":[38710,82001,70793]}}})，查询id符合38710,82001,70793中任意一个的一个User数组
 Conditions | "key{}":"条件0,条件1..."，条件为任意SQL比较表达式字符串，非Number类型必须用''包含条件的值，如'a' | ["id{}":"<=80000,\>90000"](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{"id{}":"<=80000,\>90000"}}})，查询id符合id\<=80000 \| id>90000的一个User数组
 Contain | "key<\>":Object  =>  "key<\>":[Object]，key对应值的类型必须为JSONArray，Object类型不能为JSON |  ["contactIdList<\>":38710](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{"contactIdList<\>":38710}}})，查询contactIdList包含38710的一个User数组
 Function | "key()":"函数表达式"，函数表达式为 function(Type0:value0,Type1:value1...)。函数参数类型为Object或泛型时可省略类型，即 Object:value 改写为 value | ["isPraised()":"isContain(Collection:praiseUserIdList,userId)"](http://39.108.143.172:8080/get/{"Moment":{"id":301,"isPraised()":"isContain(Collection:praiseUserIdList,userId)"}})，请求完成后会调用 boolean isContain(Collection collection, Object object) 函数，然后变为 "isPraised":true 这种（假设点赞用户id列表包含了userId，即这个User点了赞）
 Reference | "key@":"引用路径"，引用路径为用/分隔的字符串。以/开头的是缺省引用路径，从声明key所处容器的父容器路径开始；其它是完整引用路径，从最外层开始。<br /> 被引用的refKey必须在声明key的上面。如果对refKey的容器指定了返回字段，则被引用的refKey必须写在@column对应的值内，例如 "@column":"refKey,key1,..." | ["Moment":{<br /> &nbsp;&nbsp; "userId":38710<br />},<br />"User":{<br /> &nbsp;&nbsp; "id@":"/Moment/userId"<br />}](http://39.108.143.172:8080/get/{"Moment":{"userId":38710},"User":{"id@":"%252FMoment%252FuserId"}})<br /> User内的id引用了与User同级的Moment内的userId，<br />即User.id = Moment.userId，请求完成后<br > "id@":"/Moment/userId" 会变成 "id":38710
 Search | "key$":"SQL搜索表达式"  =>  "key$":["SQL搜索表达式"]，任意SQL搜索表达式字符串，如 %key%(包含key), key%(以key开始), %k%e%y%(包含字母k,e,y) 等，%表示任意字符 | ["name$":"%m%"](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{"name$":"%2525m%2525"}}})，查询name包含"m"的一个User数组
 RegEx | "key?":"正则表达式"  =>  "key?":["正则表达式"]，任意正则表达式字符串，如 ^[0-9]+$ ，可用于高级搜索 | ["name?":"^[0-9]+$"](http://39.108.143.172:8080/get/{"User[]":{"count":3,"User":{"name%253F":"^[0-9]%252B$"}}})，查询name中字符全为数字的一个User数组
 Alias | "name:alias"，name映射为alias，用alias替代name。可用于 column,Table,SQL函数 等。只用于GET类型、HEAD类型的请求 | ["@column":"toId:parentId"](http://39.108.143.172:8080/get/{"Comment":{"@column":"id,toId:parentId","id":51}})，将查询的字段toId变为parentId返回
 Add | "key+":Object，Object的类型由key指定，且类型为Number,String,JSONArray中的一种。如 82001,"apijson",["url0","url1"] 等。只用于PUT请求 | "praiseUserIdList+":[82001]，添加一个点赞用户id，即这个用户点了赞
 Remove | "key-":Object，与"key+"相反 | "balance-":100.00，余额减少100.00，即花费了100元
 Logic | &, \|, ! 逻辑运算符。<br />① & 可用于"key&{}":"条件"等<br />② \| 可用于"key\|{}":"条件", "key\|{}":[]等，一般可省略<br />③ ! 可单独使用，如"key!":Object，也可像&,\|一样配合其他功能符使用 |  ① ["id&{}":">80000,<=90000"](http://39.108.143.172:8080/head/{"User":{"id&{}":">80000,<=90000"}})，即id满足id>80000 & id<=90000<br /> ② ["id\|{}":">90000,<=80000"](http://39.108.143.172:8080/head/{"User":{"id\|{}":">90000,<=80000"}})，同"id{}":">90000,<=80000"，即id满足id>90000 \| id<=80000<br /> ③ ["id!{}":[82001,38710]](http://39.108.143.172:8080/head/{"User":{"id!{}":[82001,38710]}})，即id满足 ! (id=82001 \| id=38710)，可过滤黑名单的消息
 Keywords in array | "key":Object，key为 "[]":{} 中{}内的关键词，Object的类型由key指定<br />① "count":Integer，指定查询数量，假设允许查询数组的最大数量为max，则当count在1~max范围内时，查询count个；否则查询max个 <br />② "page":Integer，指定查询页码，从0开始，一般和count一起用<br />③ "query":Integer，指定查询内容<br />0-对象，1-总数，2-以上全部<br />总数关键词为total，和query同级，通过引用赋值得到，如 "total@":"/[]/total" <br />这里query及total仅为GET类型的请求提供方便，一般可直接用HEAD类型的请求获取总数 | ① 查询User数组，最多5个：<br />["count":5](http://39.108.143.172:8080/get/{"[]":{"count":5,"User":{}}})<br /> ② 查询第3页的User数组，每页5个：<br />["count":5,<br />"page":3](http://39.108.143.172:8080/get/{"[]":{"count":5,"page":3,"User":{}}})<br /> ③ 查询User数组和对应的User总数：<br />["[]":{<br /> &nbsp;&nbsp; "query":2,<br /> &nbsp;&nbsp; "User":{}<br />},<br />"total@":"/[]/total"](http://39.108.143.172:8080/get/{"[]":{"query":2,"count":5,"User":{}},"total@":"%252F[]%252Ftotal"})
 Keywords in object | "@key":Object，@key为 Table:{} 中{}内的关键词，Object的类型由@key指定<br />① "@about":true, 查询字段属性<br />② "@column":"key0,key1...", 指定返回字段<br />③ "@order":"key0,key1+,key2-..."，指定排序方式<br />④ "@group":"key0,key1,key2..."，指定分组方式。如果@column里声明了Table的id，则id也必须在@group中声明；其它情况下必须满足至少一个条件:<br />1.分组的key在@column里声明<br />2.Table主键在@group中声明 <br />⑤ "@having":"function0(...)?valu0,function1(...)?valu1,function2(...)?value2..."，指定SQL函数条件，一般和@group一起用，函数一般在@column里声明 | ① 查询User表中字段的属性：<br />["@about":true](http://39.108.143.172:8080/get/{"User[]":{"User":{"@about":true}}})<br /> ② 只查询id,sex,name这几列并且请求结果也按照这个顺序：<br />["@column":"id,sex,name"](http://39.108.143.172:8080/get/{"User":{"@column":"id,sex,name","id":38710}})<br /> ③ 查询按 name降序、id默认顺序 排序的User数组：<br />["@order":"name-,id"](http://39.108.143.172:8080/get/{"[]":{"count":10,"User":{"@column":"name,id","@order":"name-,id"}}})<br /> ④ 查询按userId分组的Moment数组：<br />["@group":"userId,id"](http://39.108.143.172:8080/get/{"[]":{"count":10,"Moment":%7B"@column":"userId,id","@group":"userId,id"}}})<br /> ⑤ 查询 按userId分组、id最大值>=100 的Moment数组：<br />["@column":"userId,max(id)",<br />"@group":"userId",<br />"@having":"max(id)>=100"](http://39.108.143.172:8080/get/{"[]":{"count":10,"Moment":{"@column":"userId,max(id)","@group":"userId","@having":"max(id)>=100"}}})<br />还可以指定函数返回名：<br />["@column":"userId,max(id):maxId",<br />"@group":"userId",<br />"@having":"maxId>=100"](http://39.108.143.172:8080/get/{"[]":{"count":10,"Moment":{"@column":"userId,max(id):maxId","@group":"userId","@having":"maxId>=100"}}})<br /> ⑥ 从pictureList获取第0张图片：<br />["@position":0, //这里@position为自定义关键词<br />"firstPicture()":"get(Collection:pictureList,int:@position)"](http://39.108.143.172:8080/get/{"User":{"id":38710,"@position":0,"firstPicture()":"get(Collection:pictureList,int:@position)"}})<br /> ...

<br >
<br >


## <h2 id="4">4.Usage<h2/>

### <h3 id="4.1">4.1 Download and unzip APIJSON project<h3/>

Clone or download > Download ZIP > Unzip to a path and remember it.

#### You can skip step 2 and 3, and test server response with my server IP address 39.108.143.172:8080.<br />

### <h3 id="4.2">4.2 Import MySQL table files<h3/>

This Server project needs MySQL Server and MySQLWorkbench. And you must ensure that both of them were installed.<br />
My config is Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 and OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8. The systems and softwares are all 64 bit.

Start MySQLWorkbench > Enter a connection > Click Server menu > Data Import > Select the path of APIJSON-Master/table > Start Import > Refresh SCHEMAS, and you'll see the tables were already added.


### <h3 id="4.3">4.3 Run Server project with Eclipse for JavaEE or IntellIJ IDEA Ultimate<h3/>

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

### <h3 id="4.4">4.4 Run Client project with ADT Bundle or Android Studio<h3/>

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

### <h3 id="4.5">4.5 Operate app<h3/>

Select an APIJSON request to send to server and wait. It will show the result received.<br />
If the default url is not available, change it to an available one, such as an IPV4 address of a computer running APIJSON Server project. Then click the Query button to request again.

<br />
<br />

## <h2 id="5">5.Extra<h2/>

### <h3 id="5.1">5.1 Recommend<h3/>
[APIJSON, 让接口和文档见鬼去吧！](https://my.oschina.net/tommylemon/blog/805459)

[仿QQ空间和微信朋友圈，高解耦高复用高灵活](https://my.oschina.net/tommylemon/blog/885787)

[3步创建APIJSON服务端新表及配置](https://my.oschina.net/tommylemon/blog/889074)

### <h3 id="5.2">5.2 Author<h3/>
### TommyLemon:[https://github.com/TommyLemon](https://github.com/TommyLemon)<br />

If you have any questions or suggestions, you can [create an issue](https://github.com/TommyLemon/APIJSON/issues) or [send me an e-mail](mailto:tommylemon@qq.com).<br >
If you fixed some bugs or added some functions, I would greatly appreciate it if you [contribute your code](https://github.com/TommyLemon/APIJSON/pulls).


### <h3 id="5.3">5.3 Download<h3/>

App<br />
[APIJSONApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSONApp.apk)

Test<br />
[APIJSONTest.apk](http://files.cnblogs.com/files/tommylemon/APIJSONTest.apk)


### <h3 id="5.4">5.4 Update<h3/>
[https://github.com/TommyLemon/APIJSON/commits/master](https://github.com/TommyLemon/APIJSON/commits/master)

### <h3 id="5.5">5.5 Star & Fork<h3/>

[https://github.com/TommyLemon/APIJSON](https://github.com/TommyLemon/APIJSON) 
