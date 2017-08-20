# APIJSON [![Java API](https://img.shields.io/badge/Java-1.6%2B-brightgreen.svg?style=flat)](http://www.oracle.com/technetwork/java/api-141528.html) [![Android API](https://img.shields.io/badge/Android-15%2B-brightgreen.svg?style=flat)](https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels) [![Gradle Version](https://img.shields.io/badge/gradle-2.10-green.svg)](https://docs.gradle.org/current/release-notes) [![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


[Java-Server](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Java-Server)    [Android](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Android)    [JavaScript](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript)

[查看中文文档](https://github.com/TommyLemon/APIJSON/blob/master/README.md)

[Online Test](http://139.196.140.118)



APIJSON is a JSON Transmission Structure Protocol.

You can set any JSON structure and request your server, and the server will return a JSON String with the structure you had set.<br />
You can get any data with any JSON structure by requesting server just once. It's very convenient and flexible, and does not require a special interface or multiple requests.<br />
It provides additions and deletions, fuzzy search, remote function calls and so on. And you can save duplicate data and improve transmission speed as well!<br />

Now you can realize JSON Transmissions without any interface or doc anymore!<br />
Client developers will no longer be suffered from various error in docs, and don't have to communicate with server developers about interfaces or docs anymore!<br />
And server developers no longer have to write new interfaces and docs for compatibility with legacy apps! And they will no longer be endlessly disturbed by client developers at any time!



![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_0.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_1.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_2.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/apijson_all_pages_3.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/server_idea_log_complex.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_request.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_user.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/mysql_workbench_moment.jpg) 


### Here are some examples:

#### Get an User
Request:
<pre><code class="language-json">
{
  "User":{
  }
}
</code></pre>

[Click here to test](http://139.196.140.118:8080/get/{"User":{}})

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

[Click here to test](http://139.196.140.118:8080/get/{"[]":{"count":3,"User":{"@column":"id,name"}}})

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

#### Get an Moment and it's publisher
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

[Click here to test](http://139.196.140.118:8080/get/{"Moment":{},"User":{"id@":"Moment%252FuserId"}})

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
      "id@":"/Moment/userId",        //User.id = Moment.userId  reference path with default parent path, starts from it's grandfather's path
      "@column":"id,name,head"       //set the columns in the response
    },
    "Comment[]":{                    //request an Array named Comment and extract Comments
      "count":2,
      "Comment":{
        "momentId@":"[]/Moment/id"   //Comment.momentId = Moment.id  full reference path
      }
    }
  }
}
</code></pre>

[Click here to test](http://139.196.140.118:8080/get/{"[]":{"page":0,"count":2,"Moment":{"content$":"%2525a%2525"},"User":{"id@":"%252FMoment%252FuserId","@column":"id,name,head"},"Comment[]":{"count":2,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})

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

[Online Test](http://139.196.140.118)

<br />
<br />


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
 User and his Moment | Request twice<br />User: http://localhost:8080/get/user?id=1<br />Moment: http://localhost:8080/get/moment?userId=1 | http://localhost:8080/get/{"User":{"id":1}, "Moment":{"userId@":"User/id"}}
 User list | http://localhost:8080/get/user/list?page=1&count=5&sex=0 | http://localhost:8080/get/{"[]":{"page":1, "count":5, "User":{"sex":0}}}
 Moment list of which type is 1, each Moment contains it's publisher User and top 3 Comments | The Moment must contains the User Object and Comment Array<br /> http://localhost:8080/get/moment/list?page=1&count=5&type=1&commentCount=3 | http://localhost:8080/get/{"[]":{"page":1, "count":5, "Moment":{"type":1}, "User":{"momentId@":"/Moment/id"}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}
 Moment list of an User, each Moment contains the publisher User and top 3 Comments | Change type=1 to userId=1 above | Here are several ways:<br />①Change  "Moment":{"type":1}, "User":{"momentId@":"/Moment/id"} to "User":{"id":1}, "Moment":{"userId@":"/User/id"} above<br /><br />②Or save 4 repeated User by this way<br />http://localhost:8080/get/{"User":{"id":1}, "[]":{"page":1, "count":5, "Moment":{"userId@":"User/id"}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}<br /><br />③If the User is already obtained, you can also save all repeated User by this way<br />http://localhost:8080/get/{"[]":{"page":1, "count":5, "Moment":{"userId":1}, "[]":{"count":3, "Comment":{"momentId@":"[]/Moment/id"}}}}
 
 Server responses for different requests | Previous way | APIJSON
-------- | ------------ | ------------
 User | {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}}
 User and his Moment | Reponse twice<br />User: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}}<br />Moment: {"status":200, "message":"success", "data":{"id":1, "name":"xxx"...}} | {"status":200, "message":"success", "User":{"id":1, "name":"xxx"...}, "Moment":{"id":1, "content":"xxx"...}}
 User list | {"status":200, "message":"success", "data":[{"id":1, "name":"xxx"...}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}}, "1":{"User":{"id":2...}}...}}
 Moment list of which type is 1, each Moment contains it's publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | {"status":200, "message":"success", "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "User":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}
 Moment list of an User, each Moment contains the publisher User and top 3 Comments | {"status":200, "message":"success", "data":[{"id":1, "content":"xxx"..., "User":{...}, "Comment":[...]}, {"id":2...}...]} | Results for the ways above:<br />①{"status":200, "message":"success", "[]":{"0":{"User":{"id":1, "name":"xxx"...}, "Moment":{...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />②{"status":200, "message":"success", "User":{...}, "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}<br /><br />③{"status":200, "message":"success", "[]":{"0":{"Moment":{"id":1, "content":"xxx"...}, "[]":{"0":{"Comment":{...}...}}}, "1":{...}...}}

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
