Tencent is pleased to support the open source community by making APIJSON available.   <br/>
Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved. <br/>
This source code is licensed under the Apache License Version 2.0 <br/>

<h1 align="center" style="text-align:center;">
  APIJSON
</h1>


<p align="center">🏆 Tencent top 10 open source project, Achieved 5 awards inside & outside Tencent<br />🚀 A JSON Transmission Protocol and an ORM Library for providing APIs and Documents without writing any code. </p>

<p align="center" >
  <a href="https://github.com/Tencent/APIJSON/blob/master/README.md">&nbsp;中文版&nbsp;</a>
  <a href="https://github.com/Tencent/APIJSON/blob/master/Document-English.md">&nbsp;Document&nbsp;</a>
  <a href="https://search.bilibili.com/all?keyword=APIJSON">&nbsp;Video&nbsp;</a>
  <a href="http://apijson.cn/api">&nbsp;Test&nbsp;</a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/MySQL-5.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/PostgreSQL"><img src="https://img.shields.io/badge/PostgreSQL-9.5%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/SQLServer"><img src="https://img.shields.io/badge/SQLServer-2012%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/Oracle"><img src="https://img.shields.io/badge/Oracle-12C%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/andream7/apijson-db2"><img src="https://img.shields.io/badge/DB2-7.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/TiDB-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/qiujunlin/APIJSONDemo"><img src="https://img.shields.io/badge/ClickHouse-21.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/chenyanlann/APIJSONBoot_Hive"><img src="https://img.shields.io/badge/Hive-3.1.2%2B-brightgreen.svg?style=flat"></a>  
  <a href="https://github.com/chenyanlann/APIJSONBoot_Hive"><img src="https://img.shields.io/badge/Hadoop-3.1.3%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Java-Server"><img src="https://img.shields.io/badge/Java-1.8%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/j2go/apijson-go"><img src="https://img.shields.io/badge/Go-1.16%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://img.shields.io/badge/CSharp-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/kvnZero/hyperf-APIJSON"><img src="https://img.shields.io/badge/PHP-8.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/kevinaskin/apijson-node"><img src="https://img.shields.io/badge/Node.js-ES6%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/zhangchunlin/uliweb-apijson"><img src="https://img.shields.io/badge/Python-3%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/blob/master/APIJSON-Java-Server/APIJSONDemo/pom.xml#L41-L46"><img src="https://img.shields.io/badge/Spring-4.3.2%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/blob/master/APIJSON-Java-Server/APIJSONDemo/pom.xml#L41-L46"><img src="https://img.shields.io/badge/SpringBoot-1.4.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/blob/master/APIJSON-Java-Server/APIJSONFinal/pom.xml#L51-L61"><img src="https://img.shields.io/badge/JFinal-3.5%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Android"><img src="https://img.shields.io/badge/Android-4.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-iOS"><img src="https://img.shields.io/badge/iOS-7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-JavaScript"><img src="https://img.shields.io/badge/JavaScript-ES6%2B-brightgreen.svg?style=flat"></a>

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

APIJSON is a JSON based internet communication protocol and an ORM library  <br />
that largely simplifies the process of back-end API development.  <br />
It also allows users to get data more quickly with self-defined form and fewer endpoints requests.

### Features:
#### For getting data:
You can get any data by defining the specific information you want and send it to the server. <br />
You can get different types of data by making just one request to the server. <br />
It's very convenient and flexible, and dosen't require different API endpoints with multiple requests. <br />
It provides CRUD(read and write), Fuzzy Search, Remote Function Calls, etc. <br />
You can also save duplicate data, see request history, etc. <br />

#### For API design:
APIJSON largely reduces API developers' workload by reducing most api design and documentation work. <br />
With APIJSON, client developers will no longer be suffered from possible errors in documents, <br />
and it saves communication between server developers and client developers about APIs or documentations. <br />
Server developers no longer need to worry about compatibility of APIs and documents with legacy apps. <br />

![0F85206E116CCEE74DB68E5B9A3AEDAE](https://user-images.githubusercontent.com/5738175/196148099-d3a9e0ba-93e5-4e1a-a4f8-a714083c6f7e.jpg)
#### Song Firework-Katy Parry(Modified for APIJSON)
Do you ever feel like a backend slave <br />
Repeating CRUD, wanting to make a change? <br />
Do you ever feel, APIs' so paper thin <br />
Like a house of cards, one blow from cavin' in? <br />
Do you ever feel they always complain? <br />
Urging doc and feedback bugs, even ask your refactoring <br />
Do you know that there's still a chance for you? <br />
'Cause there's a powerful tool <br />
You just gotta depend and configure <br />
And let it init <br />
Just start APIs <br />
They are so easy to try <br />
'Cause baby, you're a firework <br />
Come on, show 'em what you're worth <br />
Make 'em go, "Oh, oh, oh" <br />
As you give 'em an A-T-M <br />
Baby, you're a firework <br />
Come on, let them serve themselves <br />
Make 'em go, "Oh, oh, oh" <br />
You're gonna leave 'em all in awe, awe, awe. <br />

<br/>
	
**Tired with endless arguments about HTTP API dev or API use?** <br/>
**Use APIJSON-the ORM for providing infinity codeless CRUD APIs that fit almost all your needs.**

	
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

## <h2 id="5">5. Releases<h2/> 
	
See the latest release [here](https://github.com/Tencent/APIJSON/releases/tag/5.2.0).

<br />

## <h2 id="6">6. Auhtor<h2/> 	
	
Check out the author's [github account](https://github.com/TommyLemon) to see more related projects.<br>

If you have any questions or suggestions, you can [create an issue](https://github.com/Tencent/APIJSON/issues) or [send me an e-mail](mailto:tommylemon@qq.com).

<br />
<br />

### Users of APIJSON:

https://github.com/Tencent/APIJSON/issues/187 
<div style="float:left">
  <img src="https://user-images.githubusercontent.com/5738175/126525534-461c3e33-57b1-4630-af7f-f1238ca4ab98.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/126525251-c05e64c6-6b60-4457-a46e-dea7dcfb80cd.png" height="75">
  <br />
  <img src="https://user-images.githubusercontent.com/5738175/195065513-c581e958-2386-4a34-8b78-f48e87d1e1f2.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195063764-dcc272a0-3c2c-4073-8f22-c501c22a0844.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195063874-9d37425d-f220-445f-8554-655d5c02931b.png" height="75">
  <br />
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.transsion.com.jpeg" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/shebaochina.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.xmfish.com.gif" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.xxwolo.com.jpeg" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/t-think.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/xm.juhu.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.aipaipai-inc.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.8sso.com.jpeg" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758356-fbc89569-8b34-49d4-9f8e-272a8406440d.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.shulian8.com.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758846-1c055ae1-c235-498b-a64c-902a6068af76.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.hngtrust.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.hec-bang.com.png" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.toutou.com.cn.jpg" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.yto.net.cn.jpg" height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.lepinyongche.com.jpg"  height="75">
  <img src="https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON/User/www.aupup.com.png"  height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758697-3267f031-a7bc-44f2-84bb-06a4a7e30a75.png"  height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758188-40294d75-ef7d-4ddc-9af8-5b8c195839cf.png"  height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758198-8ec01213-18f7-43d5-9942-7c49a898ccef.png"  height="75">
  <img src="https://user-images.githubusercontent.com/95326431/194802562-e7f92b39-edbb-401f-806a-1a22513e785e.png"  height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758742-28d79efd-6645-44ee-bb50-844aa39b25fe.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758753-0a3bb998-a533-4388-8224-4f9d743ff576.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758795-e49e3eae-12ba-4399-a8e1-75db94cb0a99.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758984-0fe2fcd9-5119-46d3-9e22-4632556c0b9e.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195758995-db762406-627b-4ea5-8397-b99bb5711cce.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759031-bdcf4146-34cb-470c-a576-37d4e8fdca24.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759040-c7db99ff-3404-411d-b9ba-23547aaf1509.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759093-927fd5c3-9e1e-4648-8a35-c9d97630d086.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759079-ffc4483e-46a6-4e28-a0e0-25186ea008ab.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759186-a90a04db-0bd4-47bc-bab0-c160dcf48e53.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759204-7bdb09f5-2194-41c1-8e59-1461bd5ff4c1.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759227-2e5d42ae-b42d-4702-801d-566e70809e79.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759318-b0edad0d-9f6c-44b9-97a4-6c566880bc4b.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759239-1cb44526-abfa-4800-8d65-233d04b7c0d3.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759268-b4ad2945-704e-495c-b2b0-d0166dc5e33a.png" height="75">
  <img src="https://user-images.githubusercontent.com/5738175/195759292-baa3924c-cf56-49cf-820c-d1e0a88cac3b.png" height="75">
<br />

[More APIJSON Users](https://github.com/Tencent/APIJSON/issues/73)

### Contributers of APIJSON:
Contributers for the APIJSON core project(6 Tencent engineers, 1 Microsoft engineer, 1 Zhihu architect, 1 NetEase engineer, 1 Zoom engineer, 1 YTO Express engineer, 1 Zhilian engineer, 1 UC student、3 SUSTech student, etc.): <br />
https://github.com/Tencent/APIJSON/blob/master/CONTRIBUTING.md <br />
<div style="float:left">
  <a href="https://github.com/TommyLemon"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4" 
 height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/41"><img src="https://avatars0.githubusercontent.com/u/39320217?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/119"><img src="https://avatars1.githubusercontent.com/u/25604004?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/223"><img src="https://avatars.githubusercontent.com/u/49295281?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/43"><img src="https://avatars0.githubusercontent.com/u/23173448?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/47"><img src="https://avatars2.githubusercontent.com/u/31512287?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/212"><img src="https://avatars.githubusercontent.com/u/8936328?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/70"><img src="https://avatars1.githubusercontent.com/u/22228201?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/74"><img src="https://avatars0.githubusercontent.com/u/1274536?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/92"><img src="https://avatars3.githubusercontent.com/u/6327228?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/103"><img src="https://avatars0.githubusercontent.com/u/25990237?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/194"><img src="https://avatars0.githubusercontent.com/u/3982329?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/219"><img src="https://avatars.githubusercontent.com/u/7135770?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/222"><img src="https://avatars.githubusercontent.com/u/49233056?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/221"><img src="https://avatars.githubusercontent.com/u/17545585?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/217"><img src="https://avatars.githubusercontent.com/u/30771966?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/190"><img src="https://avatars3.githubusercontent.com/u/25056767?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/69"><img src="https://avatars0.githubusercontent.com/u/13880474?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/72"><img src="https://avatars1.githubusercontent.com/u/10663804?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/33"><img src="https://avatars1.githubusercontent.com/u/5328313?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/235"><img src="https://avatars.githubusercontent.com/u/17243165?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/233"><img src="https://avatars.githubusercontent.com/u/1252459?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/250"><img src="https://avatars.githubusercontent.com/u/44310040?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/253"><img src="https://avatars.githubusercontent.com/u/19265050?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/278"><img src="https://avatars.githubusercontent.com/u/4099373?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/279"><img src="https://avatars.githubusercontent.com/u/28685375?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/280"><img src="https://avatars.githubusercontent.com/u/60541766?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/283"><img src="https://avatars.githubusercontent.com/u/50007106?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/284"><img src="https://avatars.githubusercontent.com/u/45117061?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/285"><img src="https://avatars.githubusercontent.com/u/32100214?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/287"><img src="https://avatars.githubusercontent.com/u/62465397?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/288"><img src="https://avatars.githubusercontent.com/u/55579125?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/291"><img src="https://avatars.githubusercontent.com/u/17522475?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/293"><img src="https://avatars.githubusercontent.com/u/53826144?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/295"><img src="https://avatars.githubusercontent.com/u/11210385?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/311"><img src="https://avatars.githubusercontent.com/u/22066942?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/325"><img src="https://avatars.githubusercontent.com/u/33931153?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/Tencent/APIJSON/pull/443"><img src="https://avatars.githubusercontent.com/u/95326431?s=40&v=4"  height="54" width="54" ></a>
</div>
<br />
	
Authors of other projects for ecosystem of APIJSON(2 Tencent engineers, 1 BAT(Baidu/Alibaba/Tencent) specialist, 1 Microsoft engineer, 1 Bytedance(TikTok) engineer, etc.): <br />
https://github.com/search?o=desc&q=apijson&s=stars&type=Repositories <br />
https://search.gitee.com/?skin=rec&type=repository&q=apijson&sort=stars_count <br />
<div style="float:left">
  <a href="https://github.com/APIJSON/apijson-orm"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  
 height="54" width="54" ></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://avatars3.githubusercontent.com/u/12622501?s=400&v=4"  
 height="54" width="54" ></a>
  <a href="https://gitee.com/tiangao/apijson-go"><img src="https://portrait.gitee.com/uploads/avatars/user/43/130007_tiangao_1578918889.png!avatar200"  
 height="54" width="54" ></a>
  <a href="https://github.com/qq547057827/apijson-php"><img src="https://avatars3.githubusercontent.com/u/1657532?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/xianglong111/APIJSON-php"><img src="https://avatars.githubusercontent.com/u/9738743?s=460&v=4"  height="54" width="54" ></a>
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
  <a href="https://github.com/APIJSON/apijson-column"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/jerrylususu/apijson_todo_demo"><img src="https://avatars.githubusercontent.com/u/17522475?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/vcoolwind/apijson-practice"><img src="https://avatars.githubusercontent.com/u/22070287?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/rainboy-learn/apijson-learn"><img src="https://avatars.githubusercontent.com/u/43025876?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://gitee.com/greyzeng/apijson-sample"><img src="https://portrait.gitee.com/uploads/avatars/user/367/1102309_greyzeng_1578940307.png!avatar200"  height="54" width="54" ></a>
  <a href="https://gitee.com/zhiyuexin/ApiJsonByJFinal"><img src="https://avatar.gitee.com/uploads/90/490_zhiyuexin.jpg!avatar100?1368664499"  
 height="54" width="54" ></a>
  <a href="https://github.com/Airforce-1/SpringServer1.2-APIJSON"><img src="https://avatars3.githubusercontent.com/u/6212428?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://gitee.com/JinShuProject/JinShuApiJson"><img src="https://portrait.gitee.com/uploads/avatars/user/232/698672_maxiaoji_1578931055.jpg!avatar200"  height="54" width="54" ></a>
  <a href="https://github.com/qiujunlin/APIJSONDemo"><img src="https://avatars.githubusercontent.com/u/50007106?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/chenyanlann/APIJSONDemo_ClickHouse"><img src="https://avatars.githubusercontent.com/u/62465397?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/andream7/apijson-db2"><img src="https://avatars.githubusercontent.com/u/60541766?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/APIJSON-Android-RxJava"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/APIAuto"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/UnitAuto"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/vincentCheng/apijson-doc"><img src="https://avatars3.githubusercontent.com/u/6327228?s=400&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/ruoranw/APIJSONdocs"><img src="https://avatars.githubusercontent.com/u/25990237?s=460&u=2143b95e5ed39185f2a03d66fbb5638795e16d5a&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/HANXU2018/APIJSON-DOC"><img src="https://avatars.githubusercontent.com/u/45117061?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/kenlig/apijsondocs"><img src="https://avatars.githubusercontent.com/u/28685375?v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/apijson.org"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/pengxianggui/apijson-builder"><img src="https://avatars2.githubusercontent.com/u/16299169?s=460&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/APIJSON/AbsGrade"><img src="https://avatars.githubusercontent.com/u/41146037?s=200&v=4"  height="54" width="54" ></a>
  <a href="https://github.com/TommyLemon/Android-ZBLibrary"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4"  height="54" width="54" ></a>
</div>
<br />

Thanks to all contributers of APIJSON!

<br />

### Statistics
Hundreds of employees from big famous companies(Tencent, Google, Microsoft, Amazon, Huawei, Alibaba, Paypal, Meituan, Bytedance, IBM, Baidu, JD, NetEase, Kuaishou, Shopee, etc.) starred, <br >
a lot of employees from big famous companies(Tencent, Huawei, Microsoft, Zoom, etc.) created PR/Issue, thank you all~ <br >
[![Stargazers over time](https://starchart.cc/Tencent/APIJSON.svg)](https://starchart.cc/Tencent/APIJSON)
<img width="945" alt="image" src="https://user-images.githubusercontent.com/5738175/195752361-fffc1d38-148b-4c50-8e5e-e3116787d50d.png">
<img width="947" alt="image" src="https://user-images.githubusercontent.com/5738175/195752839-554d0204-aa5d-48d8-b838-d1a0cb0e8690.png">
<img width="949" alt="image" src="https://user-images.githubusercontent.com/5738175/195752907-a09d9505-beb3-47a6-b7b9-079b58964b4d.png">


<br />
