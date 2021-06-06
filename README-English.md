Tencent is pleased to support the open source community by making APIJSON available.   <br/>
Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved. <br/>
This source code is licensed under the Apache License Version 2.0 <br/>

<h1 align="center" style="text-align:center;">
  APIJSON
</h1>

<p align="center">🚀 A JSON Transmission Protocol and an ORM Library for providing APIs and Documents automatically. </p>


<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/MySQL-5.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/PostgreSQL"><img src="https://img.shields.io/badge/PostgreSQL-9.5%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/SQLServer"><img src="https://img.shields.io/badge/SQLServer-2012%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/Oracle"><img src="https://img.shields.io/badge/Oracle-11%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/TiDB-2.1%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Java-Server"><img src="https://img.shields.io/badge/Java-1.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://img.shields.io/badge/CSharp-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/qq547057827/apijson-php"><img src="https://img.shields.io/badge/PHP-7.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/kevinaskin/apijson-node"><img src="https://img.shields.io/badge/Node.js-ES6%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/crazytaxi824/APIJSON"><img src="https://img.shields.io/badge/Go-1.12.4%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/zhangchunlin/uliweb-apijson"><img src="https://img.shields.io/badge/Python-3%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Android"><img src="https://img.shields.io/badge/Android-4.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-iOS"><img src="https://img.shields.io/badge/iOS-7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-JavaScript"><img src="https://img.shields.io/badge/JavaScript-ES6%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/Tencent/APIJSON/blob/master/README.md">&nbsp;中文版&nbsp;</a>
  <a href="https://github.com/Tencent/APIJSON/blob/master/Document-English.md">&nbsp;Document&nbsp;</a>
  <a href="http://i.youku.com/apijson">&nbsp;Video&nbsp;</a>
  <a href="http://apijson.org/api">&nbsp;Test&nbsp;</a>
</p>

<p align="center" >
  <img src="https://raw.githubusercontent.com/Tencent/APIJSON/master/logo.png" />
</p>
<br />

#### A better online document is available at https://apijsondocs.readthedocs.io/

* ### [1. About](#1)
* ### [2. Server-side deployment](#2)
* [2.1 Installing with Eclipse](#2.1)
* [2.2 Import MySQL table files](#2.2)
* [2.3 Installing with IntellIJ IDEA Ultimate](#2.3)`
* ### [3. Client-side deployment](#3)
* [3.1 For Android](#3.1)
* [3.2 For iOS](#3.2)
* [3.3 For Javascript](#3.3)
* ### [4.Contributing](#4)
* ### [5.Versioning](#5)
* ### [6.Author](#6)
* ### [7.Donating](#7)

<br />

## <h2 id= "1">1. About <h2/>

APIJSON is a JSON based internet communication protocol and an ORM library that largely simplifies the process of back-end API development. It also allows users to get data more quickly with self-defined form and fewer endpoints requests.

### Features:
#### For getting data:
You can get any data by defining the specific information you want and send it to the server.<br />
You can get different types of data by making just one request to the server. It's very convenient and flexible, and dosen't require different API endpoints with multiple requests.<br />
It provides CRUD(read and write), Fuzzy Search, Remote Function Calls，etc. You can also save duplicate data, see request history,etc.<br />

#### For API design:
APIJSON largely reduces API developers' workload by reducing most api design and documentation work.<br />
With APIJSON, client developers will no longer be suffered from possible errors in documents, and it saves communication between server developers and client developers about APIs or documentations .<br />
Server developers no longer need to worry about compatibility of APIs and documents with legacy apps.

### Examples:

#### Get a User
Request:

```js
{
  "User":{
  }
}
```

[Click here to test](http://apijson.cn:8080/get/{"User":{}})

Response:

```js
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
```
<br />

#### Get an Array of Users

Request:

```js
{
  "[]":{
    "count":3,             //just get 3 results
    "User":{
      "@column":"id,name"  //just get ids and names
    }
  }
}
```

[Click here to test](http://apijson.cn:8080/get/{"[]":{"count":3,"User":{"@column":"id,name"}}})

Response:

```js
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
```

<br />

[Test it online](http://apijson.cn/api)<br />

![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_get.jpg) 

![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_code.jpg) 
<p align="center" >


## <h2 id="2">2.Server-side deployment<h2/>
		
You can use either Eclipse for JavaEE or IntelllJ IDEA Ultimate to make installation. For both, first download the project and save it to a path.

### <h3 id="2.1">2.1 Installing with Eclipse<h3/>

#### <h4 id="2.1.1">2.1.1 prerequisites<h4/>
  
Java Development Kit(JDK): 1.8 or above 
[MAVEN](https://maven.apache.org/download.cgi): 3.0 or above
Mysql / Oracle
[Eclipse Java EE IDE](https://www.eclipse.org/downloads/)for Web Developers.Version: Mars.1 Release (4.5.1) 

#### <h4 id="2.1.2">2.1.2 Opening the project with Eclipse<h4/>
  
Open Eclipse> *File > Import > Maven > Existing Maven Projects > Next > Browse > Select the path of the project you saved / APIJSON-Java-Server / APIJSONBoot > check pom.xml...apijson-demo > Finish*
  
#### <h4 id="2.1.3">2.1.3 Preparing the library used in demo<h4/>
  
In the menu at the right, click libs, right click apijson-orm.jar,click add as library. Apply the same to the rest *.jar* files in libs.

#### <h4 id="2.1.4">2.1.4 Configuration<h4/>
  
Open apijson.demo.server.DemoSQLConfig. In line 40-61, change return values of `getDBUri`,`getDBAccount`,`getDBPassword`,`getSchema` to your own database.<br/>

```java
@Override
public String getDBUri() {
    //TODO: Change the return value to your own
    return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? "jdbc:postgresql://localhost:5432/postgres" : "jdbc:mysql://192.168.71.146:3306/";
}
@Override
public String getDBAccount() {
    //TODO: Change the return value to your own
    return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? "postgres" : "root";
}
@Override
public String getDBPassword() {
    //TODO: Change the return value to your own
    return DATABASE_POSTGRESQL.equalsIgnoreCase(getDatabase()) ? null : "root"; 
}
@Override
public String getSchema() {
    String s = super.getSchema();
    return StringUtil.isEmpty(s, true) ? "thea" : s; //TODO: Change the return value to your own. For here,change "thea" to "your database's name"
}
```

**Note**: Instead of this step, you can also [import your database](#2.2).
  
#### <h4 id="2.1.5">2.1.5 Running the application<h4/>

In Eclipse, in the menu on the top, click *Run>Run As>Java Application>choose APIJSONApplication>OK*

### <h3 id="2.2">2.2 Import MySQL table files<h3/>

This Server project needs [MySQL Server](https://dev.mysql.com/downloads/mysql/) and [MySQLWorkbench](https://www.mysql.com/products/workbench/). Please make sure that both of them are installed.<br />

My config is Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 and OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8. Systems and softwares are all 64 bit.

Start *MySQLWorkbench > Enter a connection > Click Server menu > Data Import > Select the path of your .sql file > Start Import > Refresh SCHEMAS*. Now you should see tables are added successfully.

### <h3 id="2.3">2.3 Installing with IntellIJ IDEA Ultimate<h3/>
  
#### <h4 id="2.3.1">2.3.1 Opening the project<h4/>

*Open > Select the path of the project/APIJSON-Java-Server/APIJSONBoot > OK*

#### <h4 id="2.3.2">2.3.2 Preparing the library used in demo<h4/>  
  
In libs, right-click *apijson-orm.jar >Add as Library>OK*. Apply this to all *.jar* files in libs.

#### <h4 id="2.3.3">2.3.3 Running the application<h4/>
  
In the menu on the top: *Run > Run > Edit Configurations > + > Application > Configuration*<br />
In *Main class* , choose *APIJSONApplication*;<br />
In *Use classpath of module* , choose *apijson-demo*.<br />
Click *Run* in the bottom.

**Note**: After running, you should see APIJSON test logs and in the last, it would show ‘APIJSON已启动’. If it shows ‘address already in use’, that means port 8080 has been used . You need tochange the port. See [how to change ports for a Spring Boot Application.](https://stackoverflow.com/questions/21083170/how-to-configure-port-for-a-spring-boot-application)

<br />

## <h2 id="3">3. Client-side deployment<h2/> 
	
### <h3 id="3.1">3.1 For Android<h3/>
	
Make sure you have either [ADT Bundle](https://stuff.mit.edu/afs/sipb/project/android/docs/sdk/installing/bundle.html) or [Android Studio](https://developer.android.com/studio) installed.<br />

My config:  Windows 7 + JDK 1.7.0_71 + ADT Bundle 20140702 + Android Studio 2.2 and OSX EI Capitan + (JDK 1.7.0_71 + ADT Bundle 20140702) + (JDK 1.8.0_91 + Android Studio 2.1.2). All the systems and software are 64 bit.<br />

* 1.Importing<br />
*Open an existing Android Studio project > Select the path of APIJSON-Master/APIJSON-Android/APIJSONApp(or APIJSONTest） > OK*

* 2.Running<br />
*Run > Run app*

* 3.Testing <br />
In the browser, send a request to the server. It should return with the result.
If the default url is not available, change it to an available one, such as an IPV4 address that is running the server of the APIJSON project. Then click the request button again.

### <h3 id="3.2">3.2 For iOS<h3/>
	
Open xCode, then *APIJSON-Master/APIJSON-iOS/APIJSON-Swift > Open*<br/>

In xCode, *Product > Run* 

### <h3 id="3.3">3.3 For Javascript<h3/>

You can use either an IDE or text editor like sublime, Atom, etc. Webstorm is recommended.<br/>
While using a text editor, you just open the .html file in the APIJSON-JS folder.<br/>
You can also open it with Vue javascript framework. Click [here](https://vuejs.org/) to learn more.

## <h2 id="4">4. Contributing<h2/> 
	
We are always looking for more developers to help implementing new features, fix bugs, etc. Please have a look at the [open issues](https://github.com/Tencent/APIJSON/issues) before opening a new one.<br />

Fork the project and send a pull request.<br />

Please also ⭐Star the project!
<br />

## <h2 id="5">5. Versioning<h2/> 
	
See the latest version [here](https://github.com/Tencent/APIJSON/commits/master).

<br />

## <h2 id="6">6. Auhtor<h2/> 	
	
Check out the author's [github account](https://github.com/TommyLemon) to see more related projects.<br>

If you have any questions or suggestions, you can [create an issue](https://github.com/Tencent/APIJSON/issues) or [send me an e-mail](mailto:tommylemon@qq.com).

<br />
<br />

### Users of this project:

<div style="float:left">
  <a href="http://www.transsion.com"><img src="http://apijson.cn/images/www.transsion.com.jpeg" height="75"></a>
  <a href="http://shebaochina.com"><img src="http://apijson.cn/images/shebaochina.com.png" height="75"></a>
  <a href="http://www.xmfish.com"><img src="http://apijson.cn/images/www.xmfish.com.gif" height="75"></a>
  <a href="http://www.xxwolo.com"><img src="http://apijson.cn/images/www.xxwolo.com.jpeg" height="75"></a>
  <a href="http://t-think.com"><img src="http://apijson.cn/images/t-think.com.png" height="75"></a>
  <a href="http://xm.juhu.com"><img src="http://apijson.cn/images/xm.juhu.com.png" height="75"></a>
  <a href="http://www.aipaipai-inc.com"><img src="http://apijson.cn/images/www.aipaipai-inc.com.png" height="75"></a>
  <a href="http://www.8soo.com"><img src="http://apijson.cn/images/www.8sso.com.jpeg" height="75"></a>
  <a href="https://www.shulian8.com"><img src="http://apijson.cn/images/www.shulian8.com.png" height="75"></a>
  <a href="http://www.hngtrust.com"><img src="http://apijson.cn/images/www.hngtrust.com.png" height="75"></a>
  <a href="http://www.hec-bang.com"><img src="http://apijson.cn/images/www.hec-bang.com.png" height="75"></a>
  <a href="https://www.toutou.com.cn"><img src="http://apijson.cn/images/www.toutou.com.cn.jpg" height="75"></a>
<br />

[More APIJSON Users](https://github.com/Tencent/APIJSON/issues/73)

### Contributers of APIJSON:
Here are the contributers of this project and authors of other projects for ecosystem of APIJSON:
<div style="float:left">
  <a href="https://github.com/TommyLemon"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4" 
 height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/41"><img src="https://avatars0.githubusercontent.com/u/39320217?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/119"><img src="https://avatars1.githubusercontent.com/u/25604004?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/43"><img src="https://avatars0.githubusercontent.com/u/23173448?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/47"><img src="https://avatars2.githubusercontent.com/u/31512287?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/70"><img src="https://avatars1.githubusercontent.com/u/22228201?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/74"><img src="https://avatars0.githubusercontent.com/u/1274536?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/92"><img src="https://avatars3.githubusercontent.com/u/6327228?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/103"><img src="https://avatars0.githubusercontent.com/u/25990237?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/194"><img src="https://avatars0.githubusercontent.com/u/3982329?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/190"><img src="https://avatars3.githubusercontent.com/u/25056767?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/69"><img src="https://avatars0.githubusercontent.com/u/13880474?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/72"><img src="https://avatars1.githubusercontent.com/u/10663804?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/33"><img src="https://avatars1.githubusercontent.com/u/5328313?s=460&v=4"  height="54" width="54" ></a>
  <br />
  <a href="https://github.com/APIJSON/apijson-orm"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  
 height="54" width="54" ></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://avatars3.githubusercontent.com/u/12622501?s=400&v=4"  
 height="54" width="54" ></a>
  <a href="https://github.com/qq547057827/apijson-php"><img src="https://avatars3.githubusercontent.com/u/1657532?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/xianglong111/json-api"><img src="https://avatars.githubusercontent.com/u/9738743?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/kevinaskin/apijson-node"><img src="https://avatars3.githubusercontent.com/u/20034891?s=400&v=4"
 height="54" width="54" ></a>
  <a href="https://github.com/TEsTsLA/apijson"><img src="https://avatars2.githubusercontent.com/u/17310639?s=400&v=4"
 height="54" width="54" ></a>
  <a href="https://github.com/zhangchunlin/uliweb-apijson"><img src="https://avatars0.githubusercontent.com/u/359281?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/crazytaxi824/APIJSON"><img src="https://avatars3.githubusercontent.com/u/16500384?s=400&v=4" 
 height="54" width="54" ></a>
  <a href="https://github.com/luckyxiaomo/APIJSONKOTLIN"><img src="https://avatars2.githubusercontent.com/u/42728605?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Zerounary/APIJSONParser"><img src="https://avatars2.githubusercontent.com/u/31512287?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/apijson-framework"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://gitee.com/greyzeng/apijson-sample"><img src="https://portrait.gitee.com/uploads/avatars/user/367/1102309_greyzeng_1578940307.png!avatar200"  height="54" width="54" ></a>
  <a href="https://gitee.com/zhiyuexin/ApiJsonByJFinal"><img src="https://avatar.gitee.com/uploads/90/490_zhiyuexin.jpg!avatar100?1368664499"  
 height="54" width="54" ></a>
  <a href="https://github.com/Airforce-1/SpringServer1.2-APIJSON"><img src="https://avatars3.githubusercontent.com/u/6212428?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://gitee.com/JinShuProject/JinShuApiJson"><img src="https://portrait.gitee.com/uploads/avatars/user/232/698672_maxiaoji_1578931055.jpg!avatar200"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/APIJSON-Android-RxJava"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/APIAuto"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/UnitAuto"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/vincentCheng/apijson-doc"><img src="https://avatars3.githubusercontent.com/u/6327228?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/ruoranw/APIJSONdocs"><img src="https://avatars.githubusercontent.com/u/25990237?s=460&u=2143b95e5ed39185f2a03d66fbb5638795e16d5a&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/apijson.org"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/pengxianggui/apijson-builder"><img src="https://avatars2.githubusercontent.com/u/16299169?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/AbsGrade"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/Android-ZBLibrary"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
</div>
<br />
Thanks to all contributers of APIJSON!

<br />
