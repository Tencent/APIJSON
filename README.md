# APIJSON 
[![Java-Server API](https://img.shields.io/badge/Java.Server-1.7%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Java-Server) 
[![CSharp-Server API](https://img.shields.io/badge/CSharp.Server-2.1%2B-brightgreen.svg?style=flat)](https://github.com/liaozb/APIJSON.NET) 
[![Android API](https://img.shields.io/badge/Android-4.0%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Android) 
[![iOS API](https://img.shields.io/badge/iOS-7%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-iOS) 
[![JavaScrpit API](https://img.shields.io/badge/JavaScript-ES6%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript) 
[![Vue.js API](https://img.shields.io/badge/Vue.js-2.0%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript/APIJSON-JS-Vue)
[![Python API](https://img.shields.io/badge/Python-2.7%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Python) <br />
[![MySQL API](https://img.shields.io/badge/MySQL-5.7%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/MySQL) 
[![PostgreSQL API](https://img.shields.io/badge/PostgreSQL-9.5%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/PostgreSQL) 
[![Oracle API](https://img.shields.io/badge/Oracle-11%2B-brightgreen.svg?style=flat)](https://github.com/TommyLemon/APIJSON/tree/master/Oracle) 


[English](https://github.com/TommyLemon/APIJSON/blob/master/Document-English.md)  &nbsp;  [通用文档](https://github.com/TommyLemon/APIJSON/blob/master/Document.md)  &nbsp;  [视频教程](http://i.youku.com/apijson)  &nbsp;  [在线工具](http://apijson.org)

![](https://raw.githubusercontent.com/TommyLemon/APIJSON/master/logo.png) 

APIJSON是一种为API而生的JSON网络传输协议。<br />
为 简单的增删改查、复杂的查询、简单的事务操作 提供了完全自动化的API。<br />
能大幅降低开发和沟通成本，简化开发流程，缩短开发周期。<br />
适合中小型前后端分离的项目，尤其是互联网创业项目和企业自用项目。<br />

通过自动化API，前端可以定制任何数据、任何结构！<br />
大部分HTTP请求后端再也不用写接口了，更不用写文档了！<br />
前端再也不用和后端沟通接口或文档问题了！再也不会被文档各种错误坑了！<br />
后端再也不用为了兼容旧接口写新版接口和文档了！再也不会被前端随时随地没完没了地烦了！

### 特点功能

#### 在线解析
* 自动生成文档，清晰可读永远最新
* 自动生成请求代码，支持Android和iOS
* 自动生成JavaBean文件，一键下载
* 自动管理与测试接口用例，一键共享
* 自动校验与格式化JSON，支持高亮和收展

#### 对于前端
* 不用再向后端催接口、求文档
* 数据和结构完全定制，要啥有啥
* 看请求知结果，所求即所得
* 可一次获取任何数据、任何结构
* 能去除重复数据，节省流量提高速度

#### 对于后端
* 提供通用接口，大部分API不用再写
* 自动生成文档，不用再编写和维护
* 自动校验权限、自动管理版本、自动防SQL注入
* 开放API无需划分版本，始终保持兼容
* 支持增删改查、模糊搜索、正则匹配、远程函数等

<br />

![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_get.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_code.jpg) 
![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_doc.jpg) 
<br /><br />
[以下Gif图看起来比较卡，实际在手机上App运行很流畅]
<br />
![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_App_MomentList_Circle.gif) 
![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_App_Moment_Name.gif) 
![](https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_App_Moment_Comment.gif)

<br />

### 为什么要用APIJSON？
[前后端10大痛点解析](https://github.com/TommyLemon/APIJSON/wiki)

### 快速上手

#### 1.后端部署
可以跳过这个步骤，直接用APIJSON服务器IP地址 apijson.cn:8080 来测试接口。<br />
见&nbsp; [APIJSON后端部署 - Java](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Java-Server)<br />

#### 2.前端部署
可以跳过这个步骤，直接使用 [APIJSON在线工具](http://apijson.org/) 或 下载客户端App。<br />
见&nbsp; [Android](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-Android) &nbsp;或&nbsp; [iOS](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-iOS) &nbsp;或&nbsp; [JavaScript](https://github.com/TommyLemon/APIJSON/tree/master/APIJSON-JavaScript)<br />

### 下载客户端App

仿微信朋友圈动态实战项目<br />
[APIJSONApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSONApp.apk)

测试及自动生成代码工具<br />
[APIJSONTest.apk](http://files.cnblogs.com/files/tommylemon/APIJSONTest.apk)


## 关于作者
<div style="float:left">
  <a href="https://github.com/TommyLemon"><img src="https://avatars1.githubusercontent.com/u/5738175?s=400&u=5b2f372f0c03fae8f249d2d754e38971c2e17b92&v=4" height="90" width="90" ></a>
  <a href="https://github.com/1906522096"><img src="https://avatars0.githubusercontent.com/u/39320217?s=460&v=4"  height="90" width="90" ></a>
  <a href="https://github.com/linlwqq"><img src="https://avatars1.githubusercontent.com/u/5328313?s=460&v=4"  height="90" width="90" ></a>
</div>

<br />

#### QQ技术交流群：607020115（群1） 739316921（群2）

如果有什么问题或建议可以[提ISSUE](https://github.com/TommyLemon/APIJSON/issues)、加群或者[发我邮件](https://github.com/TommyLemon)，交流技术，分享经验。<br >
如果你解决了某些bug，或者新增了一些功能，欢迎[贡献代码](https://github.com/TommyLemon/APIJSON/pulls)，感激不尽。


### 相关推荐
[APIJSON, 让接口和文档见鬼去吧！](https://my.oschina.net/tommylemon/blog/805459)

[仿QQ空间和微信朋友圈，高解耦高复用高灵活](https://my.oschina.net/tommylemon/blog/885787)

[后端开挂:3行代码写出8个接口！](https://my.oschina.net/tommylemon/blog/1574430)

[后端自动化版本管理，再也不用改URL了！](https://my.oschina.net/tommylemon/blog/1576587)

[3步创建APIJSON后端新表及配置](https://my.oschina.net/tommylemon/blog/889074)


### 其它项目
[APIJSONAuto](https://github.com/TommyLemon/APIJSONAuto) 自动化接口管理工具。

[APIJSON.NET](https://github.com/liaozb/APIJSON.NET) 使用 C# 开发的服务端库。


### 持续更新
[https://github.com/TommyLemon/APIJSON/commits/master](https://github.com/TommyLemon/APIJSON/commits/master)

### 我要赞赏
创作不易，右上角点 ⭐Star 支持下吧，谢谢 ^_^ <br />
[https://github.com/TommyLemon/APIJSON](https://github.com/TommyLemon/APIJSON)

 
