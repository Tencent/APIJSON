# APIJSON通用文档 
后端开发者可以先看这个详细的 [图文入门教程](https://vincentcheng.github.io/apijson-doc/zh)

* ### [1.示例](#1)
* ### [2.对比传统方式](#2)
* [2.1 开发流程](#2.1)
* [2.2 前端请求](#2.2)
* [2.3 后端操作](#2.3)
* [2.4 前端解析](#2.4)
* [2.5 对应不同需求的请求](#2.5)
* [2.6 对应不同请求的结果](#2.6)
* ### [3.设计规范](#3)
* [3.1 操作方法](#3.1)
* [3.2 功能符](#3.2)


## <h2 id="1">1.示例<h2/>

#### 获取用户
请求：
<pre><code class="language-json">{
  "User":{
  }
}
</code></pre>

[点击这里测试](http://apijson.cn:8080/get/{"User":{}})

返回：
<pre><code class="language-json">{
  "User":{
    "id":38710,
    "sex":0,
    "name":"TommyLemon",
    "tag":"Android&Java",
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

#### 获取用户列表
请求：
<pre><code class="language-json">{
  "[]":{
    "count":3,             //只要3个
    "User":{
      "@column":"id,name"  //只要id,name这两个字段
    }
  }
}
</code></pre>

[点击这里测试](http://apijson.cn:8080/get/{"[]":{"count":3,"User":{"@column":"id,name"}}})

返回：
<pre><code class="language-json">{
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

#### 获取动态及发布者用户
请求：
<pre><code class="language-json">{
  "Moment":{
  },
  "User":{
    "id@":"Moment/userId"  //User.id = Moment.userId
  }
}
</code></pre>

[点击这里测试](http://apijson.cn:8080/get/{"Moment":{},"User":{"id@":"Moment%252FuserId"}})

返回：
<pre><code class="language-json">{
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

#### 获取类似微信朋友圈的动态列表
请求：
<pre><code class="language-json">{
  "[]":{                             //请求一个数组
    "page":0,                        //数组条件
    "count":2,
    "Moment":{                       //请求一个名为Moment的对象
      "content$":"%a%"               //对象条件，搜索content中包含a的动态
    },
    "User":{
      "id@":"/Moment/userId",        //User.id = Moment.userId  缺省引用赋值路径，从所处容器的父容器路径开始
      "@column":"id,name,head"       //指定返回字段
    },
    "Comment[]":{                    //请求一个名为Comment的数组，并去除Comment包装
      "count":2,
      "Comment":{
        "momentId@":"[]/Moment/id"   //Comment.momentId = Moment.id  完整引用赋值路径
      }
    }
  }
}
</code></pre>

[点击这里测试](http://apijson.cn:8080/get/{"[]":{"page":0,"count":2,"Moment":{"content$":"%2525a%2525"},"User":{"id@":"%252FMoment%252FuserId","@column":"id,name,head"},"Comment[]":{"count":2,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})

返回：
<pre><code class="language-json">{
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

[在线测试](http://apijson.org/auto)

<br />
<br />
 
## <h2 id="2">2.对比传统RESTful方式<h2/>

### <h3 id="2.1">2.1 开发流程<h3/>
 开发流程 | 传统方式 | APIJSON
-------- | ------------ | ------------
 接口传输 | 等后端编辑接口，然后更新文档，前端再按照文档编辑请求和解析代码 | 前端按照自己的需求编辑请求和解析代码。<br />没有接口，更不需要文档！前端再也不用和后端沟通接口或文档问题了！
 兼容旧版 | 后端增加新接口，用v2表示第2版接口，然后更新文档 | 什么都不用做！
 
 <br />
 
### <h3 id="2.2">2.2 前端请求<h3/>
 前端请求 | 传统方式 | APIJSON
-------- | ------------ | ------------
 要求 | 前端按照文档在对应URL后面拼接键值对 | 前端按照自己的需求在固定URL后拼接JSON
 URL | 不同的请求对应不同的URL，基本上有多少个不同的请求就得有多少个接口URL | 相同的操作方法(增删改查)都用同一个URL，<br />大部分请求都用7个通用接口URL的其中一个
 键值对 | key=value | key:value
 结构 | 同一个URL内table_name只能有一个 <br /><br /> base_url/get/table_name?<br />key0=value0&key1=value1... | 同一个URL后TableName可传任意数量个 <br /><br /> base_url/get/<br />{<br > &nbsp;&nbsp; TableName0:{<br > &nbsp;&nbsp;&nbsp;&nbsp; key0:value0,<br > &nbsp;&nbsp;&nbsp;&nbsp; key1:value1,<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; TableName1:{<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; }<br > &nbsp;&nbsp; ...<br > }
 
 <br />
 
### <h3 id="2.3">2.3 后端操作<h3/>
 后端操作 | 传统方式 | APIJSON
-------- | ------------ | ------------
 解析和返回 | 取出键值对，把键值对作为条件用预设的的方式去查询数据库，最后封装JSON并返回给前端 | 把Parser#parse方法的返回值返回给前端就行
 返回JSON结构的设定方式 | 由后端设定，前端不能修改 | 由前端设定，后端不能修改
 
 <br />
 
### <h3 id="2.4">2.4 前端解析<h3/>
 前端解析 | 传统方式 | APIJSON
-------- | ------------ | ------------
 查看方式 | 查文档或问后端，或等请求成功后看日志 | 看请求就行，所求即所得，不用查、不用问、不用等。也可以等请求成功后看日志
 解析方法 | 用JSON解析器来解析JSONObject | 可以用JSONResponse解析JSONObject，或使用传统方式
 
 <br />
 
### <h3 id="2.5">2.5 前端对应不同需求的请求<h3/>
 前端的请求 | 传统方式 | APIJSON
-------- | ------------ | ------------
 User | base_url/get/user?id=38710 | [base_url/get/<br >{<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"User":{"id":38710}})
 Moment和对应的User | 分两次请求<br />Moment: <br /> base_url/get/moment?userId=38710<br /><br />User: <br /> base_url/get/user?id=38710 | [base_url/get/<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp; }, <br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"Moment":{"userId":38710},"User":{"id":38710}})
 User列表 | base_url/get/user/list?<br />page=0&count=3&sex=0 | [base_url/get/<br >{<br > &nbsp;&nbsp; "User[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp;  "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "sex":0<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"User[]":{"page":0,"count":3,"User":{"sex":0}}})
 Moment列表，<br />每个Moment包括<br />1.发布者User<br />2.前3条Comment | Moment里必须有<br />1.User对象<br >2.Comment数组<br /><br /> base_url/get/moment/list?<br />page=0&count=3&commentCount=3 | [base_url/get/<br >{<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0, <br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{}, <br > &nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id@":"/Moment/userId"<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"[]":{"page":0,"count":3,"Moment":{},"User":{"id@":"%252FMoment%252FuserId"},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})
 User发布的Moment列表，<br /> 每个Moment包括<br /> 1.发布者User<br /> 2.前3条Comment | 1.Moment里必须有User对象和Comment数组<br > 2.字段名必须查接口文档，例如评论数量字段名可能是<br /> commentCount,comment_count或者简写cmt_count等各种奇葩写法... <br /><br /> base_url/get/moment/list?<br />page=0&count=3<br />&commentCount=3&userId=38710 | 有以下几种方式:<br /><br /> ① 把以上请求里的<br >"Moment":{}, "User":{"id@":"/Moment/userId"}<br >改为<br >["Moment":{"userId":38710}, "User":{"id":38710}](http://apijson.cn:8080/get/{"[]":{"page":0,"count":3,"Moment":{"userId":38710},"User":{"id":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}}) <br /><br /> ② 或把User放在上面的最外层省去重复的User<br />[base_url/get/<br >{<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp;&nbsp;&nbsp; }, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"User":{"id":38710},"[]":{"page":0,"count":3,"Moment":{"userId":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})<br /><br /> ③ 如果User之前已经获取到了，还可以不传User来节省请求和返回数据的流量并提升速度<br />[base_url/get/<br >{<br > &nbsp;&nbsp; "[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "page":0,<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":3, <br > &nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "count":3,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "momentId@":"[]/Moment/id"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp;&nbsp;&nbsp; }<br > &nbsp;&nbsp; }<br >}](http://apijson.cn:8080/get/{"[]":{"page":0,"count":3,"Moment":{"userId":38710},"Comment[]":{"count":3,"Comment":{"momentId@":"[]%252FMoment%252Fid"}}}})
 
 <br />
 
### <h3 id="2.6">2.6 后端对应不同请求的返回结果<h3/>
 后端的返回结果 | 传统方式 | APIJSON
-------- | ------------ | ------------
 User | {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | {<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 Moment和对应的User | 分别返回两次请求的结果，获取到Moment后取出userId作为User的id条件去查询User <br /><br /> Moment: <br > {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} <br /><br /> User: <br > {<br > &nbsp;&nbsp; "data":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | 一次性返回，没有传统方式导致的 长时间等待结果、两次结果间关联、线程多次切换 等问题 <br /><br /> {<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 User列表 | {<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":82001,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | {<br > &nbsp;&nbsp; "User[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":82001,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 Moment列表，每个Moment包括发布者User和前3条Comment | Moment里必须有<br />1.User对象<br />2.Comment数组 <br /><br /> {<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":301,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | 1.高灵活，可任意组合<br />2.低耦合，逻辑很清晰<br /><br />{<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":301,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
 User发布的Moment列表，每个Moment包括发布者User和前3条Comment | 1.大量重复User，浪费流量和服务器性能<br />2.优化很繁琐，需要后端扩展接口、写好文档，前端/前端再配合优化<br /><br />{<br > &nbsp;&nbsp; "data":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":470,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":511,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":595,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} | 以上不同请求方式的结果:<br /><br /> ① 常规请求 <br > {<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy"<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br /><br /> ② 省去重复的User <br > {<br > &nbsp;&nbsp; "User":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "name":"Tommy",<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br /><br /> ③ 不查询已获取到的User <br > {<br > &nbsp;&nbsp; "[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp; {<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "content":"xxx",<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Comment[]":[<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ]<br > &nbsp;&nbsp;&nbsp;&nbsp; },<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; ],<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}


1.base_url指基地址，一般是顶级域名，其它分支url都是在base_url后扩展。如base_url:http://apijson.cn:8080/ ，对应的GET分支url:http://apijson.cn:8080/get/ 。下同。<br >
2.请求中的key或value任意一个为null值时，这个 key:value键值对 被视为无效。下同。<br >
3.请求中的 / 需要转义。JSONRequest.java已经用URLEncoder.encode转义，不需要再写；但如果是浏览器或Postman等直接输入url/request，需要把request中的所有 / 都改成 %252F 。下同。<br >
4.code，指返回结果中的状态码，200表示成功，其它都是错误码，值全部都是HTTP标准状态码。下同。<br >
5.msg，指返回结果中的状态信息，对成功结果或错误原因的详细说明。下同。<br >
6.code和msg总是在返回结果的同一层级成对出现。对所有请求的返回结果都会在最外层有一对总结式code和msg。对非GET类型的请求，返回结果里面的每个JSONObject里都会有一对code和msg说明这个JSONObject的状态。下同。<br >
7.id等字段对应的值仅供说明，不一定是数据库里存在的，请求里用的是真实存在的值。下同。

<br />
<br />

## <h2 id="3">3.设计规范<h2/>

### <h3 id="3.1">3.1 操作方法<h3/>

  方法及说明 | URL | Request | Response
------------ | ------------ | ------------ | ------------
GET: <br > 普通获取数据，<br > 可用浏览器调试 | base_url/get/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; … <br > &nbsp;&nbsp; }<br >} <br > {…}内为限制条件<br ><br > 例如获取一个 id = 235 的 Moment：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235<br > &nbsp;&nbsp; }<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; ...<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br >例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br > }
HEAD: <br > 普通获取数量，<br > 可用浏览器调试 | base_url/head/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; }<br > } <br > {…}内为限制条件 <br ><br > 例如获取一个 id = 38710 的 User 所发布的 Moment 总数：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710<br > &nbsp;&nbsp; }<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":10<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >} <br > 例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "count":10<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;  "msg":"success"<br >}
GETS: <br > 安全/私密获取数据，<br > 用于获取钱包等<br >对安全性要求高的数据 | base_url/gets/ | 最外层加一个 "tag":tag，其它同GET | 同GET
HEADS: <br > 安全/私密获取数量，<br > 用于获取银行卡数量等<br >对安全性要求高的数据总数 | base_url/heads/ | 最外层加一个 "tag":tag，其它同HEAD | 同HEAD
POST: <br > 新增数据 | base_url/post/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…}中id由后端生成，不能传 <br ><br >例如一个 id = 38710 的 User 发布一个新 Moment：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "userId":38710,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Moment"<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":38710<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br >例如<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":120<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}
PUT: <br > 修改数据，<br > 只修改所传的字段 | base_url/put/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":id,<br > &nbsp;&nbsp;&nbsp;&nbsp; …<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…} 中 id 或 id{} 至少传一个 <br ><br >例如修改 id = 235 的 Moment 的 content：<br >{<br > &nbsp;&nbsp; "Moment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":235,<br > &nbsp;&nbsp;&nbsp;&nbsp; "content":"APIJSON,let interfaces and documents go to hell !"<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Moment"<br >} | 同POST
DELETE: <br > 删除数据 | base_url/delete/ | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id":id<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":tag<br >} <br > {…} 中 id 或 id{} 至少传一个，一般只传 id 或 id{} <br ><br >例如批量删除 id = 100,110,120 的 Comment：<br >{<br > &nbsp;&nbsp; "Comment":{<br > &nbsp;&nbsp;&nbsp;&nbsp; "id{}":[100,110,120]<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "tag":"Comment[]"<br >} | {<br > &nbsp;&nbsp; TableName:{<br > &nbsp;&nbsp;&nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp;&nbsp;&nbsp; "msg":"success",<br > &nbsp;&nbsp;&nbsp;&nbsp; "id[]":[100,110,120]<br >&nbsp;&nbsp; &nbsp;&nbsp; "count":3<br > &nbsp;&nbsp; },<br > &nbsp;&nbsp; "code":200,<br > &nbsp;&nbsp; "msg":"success"<br >}<br >例如<br >{<br >&nbsp;&nbsp; "Comment":{<br >&nbsp;&nbsp; &nbsp;&nbsp; "code":200,<br >&nbsp;&nbsp; &nbsp;&nbsp; "msg":"success",<br >&nbsp;&nbsp; &nbsp;&nbsp; "id[]":[100,110,120],<br >&nbsp;&nbsp; &nbsp;&nbsp; "count":3<br >&nbsp;&nbsp; },<br >&nbsp;&nbsp; "code":200,<br >&nbsp;&nbsp; "msg":"success"<br >}


1.TableName指要查询的数据库表Table的名称字符串。第一个字符为大写字母，剩下的字符要符合英语字母、数字、下划线中的任何一种。对应的值的类型为JSONObject，结构是 {...}，里面放的是Table的字段(列名)。下同。<br >
2."tag":tag 后面的tag是非GET、HEAD请求中匹配请求的JSON结构的key，一般是要查询的table的名称，由后端Request表中指定。下同。<br >
3.GET、HEAD请求是开放请求，可任意组合任意嵌套。其它请求为受限制的安全/私密请求，对应的 方法、tag、结构 都必须和 后端Request表中所指定的 一一对应，否则请求将不被通过。下同。<br >
4.GETS与GET、HEADS与HEAD分别为同一类型的操作方法，请求稍有不同但返回结果相同。下同。<br >
5.在HTTP通信中，自动化接口(get,gets,head,heads,post,put,delete) 全用HTTP POST请求。下同。<br >
6.所有JSONObject都视为容器(或者文件夹)，结构为 {...} ，里面可以放普通对象或子容器。下同。<br >
7.每个对象都有一个唯一的路径(或者叫地址)，假设对象名为refKey，则用 key0/key1/.../refKey 表示。下同。

<br >

### <h3 id="3.2">3.2 功能符<h3/>
 
 功能 | 键值对格式 | 使用示例
------------ | ------------ | ------------
 查询数组 | "key[]":{}，后面是JSONObject，key可省略。当key和里面的Table名相同时，Table会被提取出来，即 {Table:{Content}} 会被转化为 {Content} | [{"User[]":{"User":{}}}](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{}}})，查询一个User数组。这里key和Table名都是User，User会被提取出来，即 {"User":{"id", ...}} 会被转化为 {"id", ...} 
 匹配选项范围 | "key{}":[]，后面是JSONArray，作为key可取的值的选项 | ["id{}":[38710,82001,70793]](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"id{}":[38710,82001,70793]}}})，对应SQL是`id IN(38710,82001,70793)`，查询id符合38710,82001,70793中任意一个的一个User数组
 匹配条件范围 | "key{}":"条件0,条件1..."，条件为任意SQL比较表达式字符串，非Number类型必须用''包含条件的值，如'a' | ["id{}":"<=80000,\>90000"](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"id{}":"<=80000,\>90000"}}})，对应SQL是`id<=80000 OR id>90000`，查询id符合id\<=80000 \| id>90000的一个User数组
 包含选项范围 | "key<\>":Object  =>  "key<\>":[Object]，key对应值的类型必须为JSONArray，Object类型不能为JSON |  ["contactIdList<\>":38710](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"contactIdList<\>":38710}}})，对应SQL是`json_contains(contactIdList,38710)`，查询contactIdList包含38710的一个User数组
 判断是否存在 | "key}{@":{<br /> &nbsp;&nbsp; "from":"Table",<br /> &nbsp;&nbsp; "Table":{ ... }<br />}<br />其中：<br />}{ 表示 EXISTS；<br /> key 用来标识是哪个判断；<br /> @ 后面是 子查询 对象，具体见下方 子查询 的说明。 | ["id}{@":{<br /> &nbsp;&nbsp; "from":"Comment",<br /> &nbsp;&nbsp; "Comment":{<br /> &nbsp;&nbsp;  &nbsp;&nbsp; "momentId":15 <br /> &nbsp;&nbsp; }<br />}](http://apijson.cn:8080/get/{"User":{"id}{@":{"from":"Comment","Comment":{"momentId":15}}}})<br /> WHERE EXISTS(SELECT * FROM Comment WHERE momentId=15)
 远程调用函数 | "key()":"函数表达式"，函数表达式为 function(key0,key1...)，会调用后端对应的函数 function(JSONObject request, String key0, String key1...)，实现 参数校验、数值计算、数据同步、消息推送、字段拼接、结构变换 等特定的业务逻辑处理，<br>可使用 - 和 + 表示优先级，解析 key-() > 解析当前对象 > 解析 key() > 解析子对象 > 解析 key+()  | ["isPraised()":"isContain(praiseUserIdList,userId)"](http://apijson.cn:8080/get/{"Moment":{"id":301,"isPraised()":"isContain(praiseUserIdList,userId)"}})，会调用 boolean isContain(JSONObject request, String array, String value) 函数，然后变为 "isPraised":true 这种（假设点赞用户id列表包含了userId，即这个User点了赞）
 引用赋值 | "key@":"key0/key1/.../refKey"，引用路径为用/分隔的字符串。以/开头的是缺省引用路径，从声明key所处容器的父容器路径开始；其它是完整引用路径，从最外层开始。<br /> 被引用的refKey必须在声明key的上面。如果对refKey的容器指定了返回字段，则被引用的refKey必须写在@column对应的值内，例如 "@column":"refKey,key1,..." | ["Moment":{<br /> &nbsp;&nbsp; "userId":38710<br />},<br />"User":{<br /> &nbsp;&nbsp; "id@":"/Moment/userId"<br />}](http://apijson.cn:8080/get/{"Moment":{"userId":38710},"User":{"id@":"%252FMoment%252FuserId"}})<br /> User内的id引用了与User同级的Moment内的userId，<br />即User.id = Moment.userId，请求完成后<br > "id@":"/Moment/userId" 会变成 "id":38710
 子查询 | "key@":{<br /> &nbsp;&nbsp; "range": "ALL", <br /> &nbsp;&nbsp; "from":"Table",<br /> &nbsp;&nbsp; "Table":{ ... }<br />}<br />其中：<br />range 可为 ALL,ANY；<br />from 为目标表 Table 的名称；<br />@ 后面的对象类似数组对象，可使用 count 和 join 等功能。 | ["id@":{<br /> &nbsp;&nbsp; "from":"Comment",<br /> &nbsp;&nbsp; "Comment":{<br /> &nbsp;&nbsp;  &nbsp;&nbsp; "@column":"min(userId)" <br /> &nbsp;&nbsp; }<br />}](http://apijson.cn:8080/get/{"User":{"id@":{"from":"Comment","Comment":{"@column":"min(userId)"}}}})<br /> WHERE id=(SELECT min(userId) FROM Comment)
 模糊搜索 | "key$":"SQL搜索表达式"  =>  "key$":["SQL搜索表达式"]，任意SQL搜索表达式字符串，如 %key%(包含key), key%(以key开始), %k%e%y%(包含字母k,e,y) 等，%表示任意字符 | ["name$":"%m%"](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"name$":"%2525m%2525"}}})，对应SQL是`name LIKE '%m%'`，查询name包含"m"的一个User数组
 正则匹配 | "key~":"正则表达式"  =>  "key~":["正则表达式"]，任意正则表达式字符串，如 ^[0-9]+$ ，*~ 忽略大小写，可用于高级搜索 | ["name~":"^[0-9]+$"](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"name~":"^[0-9]%252B$"}}})，对应SQL是`name REGEXP '^[0-9]+$'`，查询name中字符全为数字的一个User数组
 连续范围 | "key%":"start,end"  =>  "key%":["start,end"]，其中 start 和 end 都只能为 Boolean, Number, String 中的一种，如 "2017-01-01,2019-01-01" ，["1,90000", "82001,100000"] ，可用于连续范围内的筛选 | ["date%":"2017-10-01,2018-10-01"](http://apijson.cn:8080/get/{"User[]":{"count":3,"User":{"date%2525":"2017-10-01,2018-10-01"}}})，对应SQL是`date BETWEEN '2017-10-01' AND '2018-10-01'`，查询在2017-10-01和2018-10-01期间注册的用户的一个User数组
 新建别名 | "name:alias"，name映射为alias，用alias替代name。可用于 column,Table,SQL函数 等。只用于GET类型、HEAD类型的请求 | ["@column":"toId:parentId"](http://apijson.cn:8080/get/{"Comment":{"@column":"id,toId:parentId","id":51}})，对应SQL是`toId AS parentId`，将查询的字段toId变为parentId返回
 增加 或 扩展 | "key+":Object，Object的类型由key指定，且类型为Number,String,JSONArray中的一种。如 82001,"apijson",["url0","url1"] 等。只用于PUT请求 | "praiseUserIdList+":[82001]，对应SQL是`json_insert(praiseUserIdList,82001)`，添加一个点赞用户id，即这个用户点了赞
 减少 或 去除 | "key-":Object，与"key+"相反 | "balance-":100.00，对应SQL是`balance = balance - 100.00`，余额减少100.00，即花费了100元
 逻辑运算 | &, \|, ! 逻辑运算符，对应数据库 SQL 中的 AND, OR, NOT。 <br />横或纵与：同一字段的值内条件默认 \| 或连接，不同字段的条件默认 & 与连接。 <br /><br />① & 可用于"key&{}":"条件"等<br /><br />② \| 可用于"key\|{}":"条件", "key\|{}":[]等，一般可省略<br /><br />③ ! 可单独使用，如"key!":Object，也可像&,\|一样配合其他功能符使用 |  ① ["id&{}":">80000,<=90000"](http://apijson.cn:8080/head/{"User":{"id&{}":">80000,<=90000"}})，对应SQL是`id>80000 AND id<=90000`，即id满足id>80000 & id<=90000<br /><br /> ② ["id\|{}":">90000,<=80000"](http://apijson.cn:8080/head/{"User":{"id\|{}":">90000,<=80000"}})，同"id{}":">90000,<=80000"，对应SQL是`id>80000 OR id<=90000`，即id满足id>90000 \| id<=80000<br /><br /> ③ ["id!{}":[82001,38710]](http://apijson.cn:8080/head/{"User":{"id!{}":[82001,38710]}})，对应SQL是`id NOT IN(82001,38710)`，即id满足 ! (id=82001 \| id=38710)，可过滤黑名单的消息
 数组关键词，可自定义 | "key":Object，key为 "[]":{} 中{}内的关键词，Object的类型由key指定<br /><br />① "count":Integer，查询数量，0 表示最大值，默认最大值为100 <br /><br />② "page":Integer，查询页码，从0开始，默认最大值为100，一般和count一起用<br /><br />③ "query":Integer，查询内容<br />0-对象，1-总数，2-以上全部<br />总数关键词为total，和query同级，通过引用赋值得到，如 "total@":"/[]/total" <br />这里query及total仅为GET类型的请求提供方便，一般可直接用HEAD类型的请求获取总数<br /><br />④ "join":"&/Table0/key0@,\</Table1/key1@"<br />多表连接方式：<br /> "\<" - LEFT JOIN <br /> ">" - RIGHT JOIN <br /> "&" - INNER JOIN <br /> "\|" - FULL JOIN <br />  "!" - OUTTER JOIN <br /> "@" - APP JOIN <br />其中 @ APP JOIN 为应用层连表，会从已查出的主表里取得所有副表 key@ 关联的主表内的 refKey 作为一个数组 refKeys: [value0, value1...]，然后把原来副表 count 次查询 key=$refKey 的 SQL 用 key IN($refKeys) 的方式合并为一条 SQL 来优化性能；<br /> 其它 JOIN 都是 SQL JOIN，具体功能和 MySQL,PostgreSQL 等数据库的 JOIN 一一对应，<br />"ViceTable":{ "key@:".../MainTable/refKey" }<br />会对应生成<br />MainTable ... JOIN ViceTable ON ViceTable.key=MainTable.refKey。 <br /><br />⑤ "otherKey": Object，自定义关键词，名称和以上系统关键词不一样，且原样返回上传的值  | ① 查询User数组，最多5个：<br />["count":5](http://apijson.cn:8080/get/{"[]":{"count":5,"User":{}}})<br /><br /> ② 查询第3页的User数组，每页5个：<br />["count":5,<br />"page":3](http://apijson.cn:8080/get/{"[]":{"count":5,"page":3,"User":{}}})<br /><br /> ③ 查询User数组和对应的User总数：<br />["[]":{<br /> &nbsp;&nbsp; "query":2,<br /> &nbsp;&nbsp; "User":{}<br />},<br />"total@":"/[]/total"](http://apijson.cn:8080/get/{"[]":{"query":2,"count":5,"User":{}},"total@":"%252F[]%252Ftotal"})<br />总页数、是否有下一页等信息可通过 total,count,page 得出，<br />总页数 int totalPage = Math.ceil(total \/ count)<br />是否有下一页 boolean hasNextPage = total > count\*page<br />是否为第一页 boolean isFirstPage = page <= 0<br />是否为最后一页 boolean isLastPage = total <= count\*page<br />... <br /><br /> ④ Moment INNER JOIN User LEFT JOIN Comment：<br />["[]":{<br /> &nbsp;&nbsp; "join": "&/User/id@,\</Comment/momentId@",<br /> &nbsp;&nbsp; "Moment":{},<br /> &nbsp;&nbsp; "User":{<br /> &nbsp;&nbsp;&nbsp;&nbsp; "name~":"t",<br /> &nbsp;&nbsp;&nbsp;&nbsp; "id@": "/Moment/userId"<br /> &nbsp;&nbsp; },<br /> &nbsp;&nbsp; "Comment":{<br /> &nbsp;&nbsp;&nbsp;&nbsp; "momentId@": "/Moment/id"<br /> &nbsp;&nbsp; }<br />}](http://apijson.cn:8080/get/{"[]":{"count":5,"join":"&%252FUser%252Fid@,\<%252FComment%252FmomentId@","Moment":{"@column":"id,userId,content"},"User":{"name~":"t","id@":"%252FMoment%252FuserId","@column":"id,name,head"},"Comment":{"momentId@":"%252FMoment%252Fid","@column":"id,momentId,content"}}})<br /><br /> ⑤ 每一层都加当前用户名：<br />["User":{},<br />"[]":{<br /> &nbsp;&nbsp; "name@":"User/name", //自定义关键词<br /> &nbsp;&nbsp; "Moment":{}<br />}](http://apijson.cn:8080/get/{"User":{},"[]":{"name@":"User%252Fname","Moment":{}}})
 对象关键词，可自定义 | "@key":Object，@key为 Table:{} 中{}内的关键词，Object的类型由@key指定<br /><br />① "@combine":"&key0,&key1,\|key2,key3,<br />!key4,!key5,&key6,key7..."，条件组合方式，\| 可省略。会自动把同类的合并，外层按照 & \| ! 顺序，内层的按传参顺序组合成<br /> (key0 & key1 & key6 & 其它key) & (key2 \| key3 \| key7) & !(key4 \| key5) <br />这种连接方式，其中 "其它key" 是指与 @combine 在同一对象，且未被它声明的条件 key，默认都是 & 连接 <br /><br />② "@column":"column;function(arg)..."，返回字段<br /><br />③ "@order":"column0+,column1-..."，排序方式<br /><br />④ "@group":"column0,column1..."，分组方式。如果@column里声明了Table的id，则id也必须在@group中声明；其它情况下必须满足至少一个条件:<br />1.分组的key在@column里声明<br />2.Table主键在@group中声明 <br /><br />⑤ "@having":"function0(...)?value0;function1(...)?value1;function2(...)?value2..."，SQL函数条件，一般和@group一起用，函数一般在@column里声明<br /><br />⑥ "@schema":"sys"，集合空间(模式)，非默认的值可通过它来指定，可以在最外层作为全局默认配置<br /><br />⑦ "@database":"POSTGRESQL"，跨数据库，非默认的值可通过它来指定，可以在最外层作为全局默认配置<br /><br />⑧ "@role": "OWNER"，来访角色，包括<br />UNKNOWN,LOGIN,CONTACT,CIRCLE,OWNER,ADMIN，<br />可以在最外层作为全局默认配置，<br />可自定义其它角色并重写 Verifier.verify 等相关方法来自定义校验。 <br /><br />⑨ "@explain": true，性能分析，可以在最外层作为全局默认配置 <br /><br />⑩ "@otherKey": Object，自定义关键词，名称和以上系统关键词不一样，且原样返回上传的值 | ① 搜索name或tag任何一个字段包含字符a的User列表：<br />["name~":"a",<br />"tag~":"a",<br />"@combine":"name~,tag~"](http://apijson.cn:8080/get/{"User[]":{"count":10,"User":{"@column":"id,name,tag","name~":"a","tag~":"a","@combine":"name~,tag~"}}})<br /><br /> ② 只查询id,sex,name这几列并且请求结果也按照这个顺序：<br />["@column":"id,sex,name"](http://apijson.cn:8080/get/{"User":{"@column":"id,sex,name","id":38710}})<br /><br /> ③ 查询按 name降序、id默认顺序 排序的User数组：<br />["@order":"name-,id"](http://apijson.cn:8080/get/{"[]":{"count":10,"User":{"@column":"name,id","@order":"name-,id"}}})<br /><br /> ④ 查询按userId分组的Moment数组：<br />["@group":"userId,id"](http://apijson.cn:8080/get/{"[]":{"count":10,"Moment":%7B"@column":"userId,id","@group":"userId,id"}}})<br /><br /> ⑤ 查询 按userId分组、id最大值>=100 的Moment数组：<br />["@column":"userId;max(id)",<br />"@group":"userId",<br />"@having":"max(id)>=100"](http://apijson.cn:8080/get/{"[]":{"count":10,"Moment":{"@column":"userId%253Bmax(id)","@group":"userId","@having":"max(id)>=100"}}})<br />还可以指定函数返回名：<br />["@column":"userId;max(id):maxId",<br />"@group":"userId",<br />"@having":"maxId>=100"](http://apijson.cn:8080/get/{"[]":{"count":10,"Moment":{"@column":"userId%253Bmax(id):maxId","@group":"userId","@having":"maxId>=100"}}})<br /><br /> ⑥ 查询 sys 内的 User 表：<br />["@schema":"sys"](http://apijson.cn:8080/get/{"User":{"@schema":"sys"}})<br /><br /> ⑦ 查询 PostgreSQL 数据库的 User 表：<br />["@database":"POSTGRESQL"](http://apijson.cn:8080/get/{"User":{"@database":"POSTGRESQL"}})<br /><br /> ⑧ 查询当前用户的动态：<br />["@role":"OWNER"](http://apijson.cn:8080/get/{"[]":{"Moment":{"@role":"OWNER"}}})<br /><br /> ⑨ 开启性能分析：<br />["@explain":true](http://apijson.cn:8080/get/{"[]":{"Moment":{"@explain":true}}})<br /><br /> ⑩ 从pictureList获取第0张图片：<br />["@position":0, //自定义关键词<br />"firstPicture()":"getFromArray(pictureList,@position)"](http://apijson.cn:8080/get/{"User":{"id":38710,"@position":0,"firstPicture()":"getFromArray(pictureList,@position)"}})

<br />
