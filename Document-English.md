
<h1 align="center" style="text-align:center;">
  APIJSON
</h1>

<p align="center">🏆Gitee Most Valuable Project<br />🚀A JSON Transmission Protocol and an ORM Library for providing APIs and Documents automatically.</p>


<p align="center" >
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/MySQL"><img src="https://img.shields.io/badge/MySQL-5.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/PostgreSQL"><img src="https://img.shields.io/badge/PostgreSQL-9.5%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/Oracle"><img src="https://img.shields.io/badge/Oracle-11%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/MySQL"><img src="https://img.shields.io/badge/TiDB-2.1%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Java-Server"><img src="https://img.shields.io/badge/Java-1.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://img.shields.io/badge/CSharp-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/orchie/apijson"><img src="https://img.shields.io/badge/PHP-7.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TEsTsLA/apijson"><img src="https://img.shields.io/badge/Node.js-ES6%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/zhangchunlin/uliweb-apijson"><img src="https://img.shields.io/badge/Python-3%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Android"><img src="https://img.shields.io/badge/Android-4.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-iOS"><img src="https://img.shields.io/badge/iOS-7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript"><img src="https://img.shields.io/badge/JavaScript-ES6%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="http://apijson.org/">Chinese&nbsp;             </a>
  <a href="http://i.youku.com/apijson">Video&nbsp;             </a>
  <a href="http://apijson.cn/">Test</a>
</p>

<p align="center" >
  <img src="https://raw.githubusercontent.com/TommyLemon/APIJSON/master/logo.png" />
</p>

<br />

* ### [1. About](#1)
* ### [2. Server-side deployment](#2)
* [2.1 Installing with Eclipse](#2.1)
* [2.2 Import MySQL table files](#2.2)
* [2.3 Installing with IntellIJ IDEA Ultimate](#2.3)
* ### [3. Client-side deployment](#3)
* [3.1 For Android](#3.1)
* [3.2 For iOS](#3.2)
* [3.3 For Javascript](#3.3)

* ### [5.Extra](#5)
* [5.1 Recommend](#5.1)
* [5.2 Author](#5.2)
* [5.3 Download](#5.3)
* [5.4 Update](#5.4)
* [5.5 Star&Fork](#5.5)

## <h2 id="1">1.About<h2/>

APIJSON is a JSON based application that largely simplify the process of back-end API development. It allows front-end users to get data with self-defined form.

### Features:

You can set any JSON structure and send a request to your server, and the server will respond JSON codes with the structure you just set.<br />
You can get different types of data by making just one request to the server. It's very convenient and flexible, and dosen't require different APIs with multiple requests.<br />
It provides CRUD(read and write), Fuzzy Search, Remote Function Calls，etc. You can save duplicate data and improve data transmission speed as well!<br />

APIJSON enables server developers to realize JSON transmissions without most api design or document writing anymore!<br />
Client developers will no longer be suffered from various errors in documents, and don't have to communicate with server developers about APIs or documents anymore!<br />
Server developers no longer need to worry about compatibility of APIs and documents with legacy apps! APIJSON thus helps developers reducing time in developing APIs.

<br />

<br />

### Examples:

#### Get a User
Request:
<pre><code class="language-json">
{
  "User":{
  }
}
</code></pre>

[Click here to test](http://apijson.cn:8080/get/{"User":{}})

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

[Click here to test](http://apijson.cn:8080/get/{"[]":{"count":3,"User":{"@column":"id,name"}}})

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

[Test it online](http://apijson.cn/)

<br />

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

<pre><code class="language-java">
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
</code></pre>

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


## <h2 id="3">3. Client-side deployment<h2/>
	
### <h3 id="3.1">3.1 For Android<h3/>
	
Make sure you have either [ADT Bundle](https://stuff.mit.edu/afs/sipb/project/android/docs/sdk/installing/bundle.html) or [Android Studio](https://developer.android.com/studio) installed.<br />

My config:  Windows 7 + JDK 1.7.0_71 + ADT Bundle 20140702 + Android Studio 2.2 and OSX EI Capitan + (JDK 1.7.0_71 + ADT Bundle 20140702) + (JDK 1.8.0_91 + Android Studio 2.1.2). All the systems and software are 64 bit.<br />

* 1.Importing<br />
*Open an existing Android Studio project > Select the path of APIJSON-Master/APIJSON-Android/APIJSONApp(or APIJSONTest） > OK*

* 2.Running<br />
*Run > Run app*

* 3. Testing <br />
In the browser, send a request to the server. It should return with the result.
If the default url is not available, change it to an available one, such as an IPV4 address that is running the server of the APIJSON project. Then click the request button again.
<br />

### <h3 id="3.2">3.2 For iOS<h3/>
	
Open xCode, then *APIJSON-Master/APIJSON-iOS/APIJSON-Swift > Open*<br/>

In xCode, *Product > Run* 

### <h3 id="3.3">3.3 For Javascript<h3/>

You can use either an IDE or text editor like sublime, Atom, etc. Webstorm is recommended.<br/>
While using a text editor, you just open the .html file in the APIJSON-JS folder.<br/>
You can also open it with Vue javascript framework. Click [here](https://vuejs.org/) to learn more.

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
