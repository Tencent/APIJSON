Tencent is pleased to support the open source community by making APIJSON available.   <br/>
Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved. <br/>
This source code is licensed under the Apache License Version 2.0 <br/>


<h1 align="center" style="text-align:center;">
  APIJSON
</h1>

<p align="center">零代码、热更新、全自动 ORM 库<br />🚀 后端接口和文档零代码，前端(客户端) 定制返回 JSON 的数据和结构</p>

<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/MySQL-5.7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/PostgreSQL"><img src="https://img.shields.io/badge/PostgreSQL-9.5%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/SQLServer"><img src="https://img.shields.io/badge/SQLServer-2012%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/Oracle"><img src="https://img.shields.io/badge/Oracle-12C%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/andream7/apijson-db2"><img src="https://img.shields.io/badge/DB2-7.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/MySQL"><img src="https://img.shields.io/badge/TiDB-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/chenyanlann/APIJSONDemo_ClickHouse"><img src="https://img.shields.io/badge/ClickHouse-21.1%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Java-Server"><img src="https://img.shields.io/badge/Java-1.8%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/liaozb/APIJSON.NET"><img src="https://img.shields.io/badge/CSharp-2.1%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/qq547057827/apijson-php"><img src="https://img.shields.io/badge/PHP-7.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/kevinaskin/apijson-node"><img src="https://img.shields.io/badge/Node.js-ES6%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/zhangchunlin/uliweb-apijson"><img src="https://img.shields.io/badge/Python-3%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Android"><img src="https://img.shields.io/badge/Android-4.0%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-iOS"><img src="https://img.shields.io/badge/iOS-7%2B-brightgreen.svg?style=flat"></a>
  <a href="https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-JavaScript"><img src="https://img.shields.io/badge/JavaScript-ES6%2B-brightgreen.svg?style=flat"></a>
</p>
<p align="center" >
  <a href="https://github.com/Tencent/APIJSON/blob/master/README-English.md">English</a>
  <a href="https://github.com/Tencent/APIJSON/blob/master/Document.md">通用文档</a>
  <a href="https://search.bilibili.com/all?keyword=APIJSON&from_source=webtop_search&spm_id_from=333.999">视频教程</a>
  <a href="http://apijson.cn/api">在线体验</a>
</p>

<p align="center" >
  <img src="https://oscimg.oschina.net/oscnet/up-3299d6e53eb0534703a20e96807727fac63.png" />
</p>

---

<b >导航目录：</b> 项目简介 [上手使用](#%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B) [社区生态](#%E6%8A%80%E6%9C%AF%E4%BA%A4%E6%B5%81)  &nbsp;&nbsp;&nbsp;&nbsp;  完整详细的导航目录 [点这里查看](/Navigation.md) <br />


APIJSON 是一种专为 API 而生的 JSON 网络传输协议 以及 基于这套协议实现的 ORM 库。<br />
为各种增删改查提供了完全自动化的万能 API，零代码实时满足千变万化的各种新增和变更需求。<br />
能大幅降低开发和沟通成本，简化开发流程，缩短开发周期。<br />
适合中小型前后端分离的项目，尤其是 初创项目、内部项目、低代码/零代码、小程序、BaaS、Serverless 等。<br />

通过万能的 API，前端可以定制任何数据、任何结构。<br />
大部分 HTTP 请求后端再也不用写接口了，更不用写文档了。<br />
前端再也不用和后端沟通接口或文档问题了。再也不会被文档各种错误坑了。<br />
后端再也不用为了兼容旧接口写新版接口和文档了。再也不会被前端随时随地没完没了地烦了。

### 特点功能

#### 对于前端
* 不用再向后端催接口、求文档
* 数据和结构完全定制，要啥有啥
* 看请求知结果，所求即所得
* 可一次获取任何数据、任何结构
* 能去除重复数据，节省流量提高速度

#### 对于后端
* 提供通用接口，大部分 API 不用再写
* 自动生成文档，不用再编写和维护
* 自动校验权限、自动管理版本、自动防 SQL 注入
* 开放 API 无需划分版本，始终保持兼容
* 支持增删改查、复杂查询、跨库连表、远程函数等

<br />

### APIJSON 接口展示
#### Postman 展示 APIJSON
![](https://static.oschina.net/uploads/img/201711/12230359_f7fQ.jpg)
<br/>

#### APIAuto 展示 APIJSON
使用 APIAuto-机器学习接口工具 来管理和测试 HTTP API 可大幅提升接口联调效率<br/>
(注意网页工具界面是 APIAuto，里面的 URL+JSON 才是 APIJSON 的 HTTP API)： <br/>
<br />
<p align="center" >
  <a >APIJSON 多表关联查询、结构自由组合，APIAuto 多个测试账号、一键共享测试用例</a>
</p> 

![](https://oscimg.oschina.net/oscnet/up-bbbec4fc5edc472be127c02a4f3cd8f4ec2.JPEG) 

<br />
<p align="center" >
  <a >APIAuto 自动生成前端(客户端)请求代码 和 Python 测试用例代码，一键下载</a>
</p> 

![](https://oscimg.oschina.net/oscnet/up-637193bbd89b41c3264827786319e842aee.JPEG) 

<br />
<p align="center" >
  <a >APIAuto 自动保存请求记录、自动生成接口文档，可添加常用请求、快捷查看一键恢复</a>
</p> 

![](https://oscimg.oschina.net/oscnet/up-7dcb4ae71bd3892a909e4ffa37ba7c1d92a.JPEG) 

<br />
<p align="center" >
  <a >APIAuto 一键自动接口回归测试，不需要写任何代码(注解、注释等全都不要)</a>
</p> 

![](https://oscimg.oschina.net/oscnet/up-c1ba774f8e7fcc5adcdb05cad5bd414d766.JPEG) 

<br />
<p align="center" >
  <a >一图胜千言 - APIJSON 部分基础功能概览</a>
</p> 

![](https://oscimg.oschina.net/oscnet/up-e21240ef3770326ee6015e052226d0da184.JPEG) 

<br /><br />

### APIJSON App 演示
使用 APIJSON + ZBLibrary 开发的 Android 客户端 Demo (以下 Gif 图看起来比较卡，实际上运行很流畅)：
<br />
![](https://oscimg.oschina.net/oscnet/up-a3f167e593080e8a3fc09c3d5fc09330c98.gif) 
![](https://oscimg.oschina.net/oscnet/up-141abcb5dabc01c890d70c461bd1fdc751f.gif) 
![](https://oscimg.oschina.net/oscnet/up-58aecc2701c2c4ea33e53f246e427773b09.gif)

<br />

### APIJSON 分享演讲
#### APIJSON-零代码接口与文档 ORM 库（国际开源谷 Gitee Meetup）

https://www.bilibili.com/video/BV1Tv411t74v

![image](http://apijson.cn/images/comparison/APIJSON_vs_PreviousWays.jpg)


#### APIJSON 和 APIAuto-零代码开发和测试（QECon 全球软件质量&效能大会）

https://www.bilibili.com/video/BV1yv411p7Y4

<img width="1360" alt="wecom-temp-377bbd0daf5aed716baf7ebcb003d94c" src="https://user-images.githubusercontent.com/5738175/121370207-1b35de00-c96f-11eb-840e-cc2ff2995888.png">


<br />

### 为什么选择 APIJSON？
前后端 关于接口的 开发、文档、联调 等 10 个痛点解析 <br />
https://github.com/Tencent/APIJSON/wiki

* **解决十个痛点** (APIJSON 可帮助用户 提振开发效率、杜绝联调扯皮、规避文档缺陷、节省流量带宽 等)
* **开发提速很大** (CRUD 零代码热更新自动化，APIJSONBoot 对比 SSM、SSH 等保守估计可提速 20 倍以上)
* **腾讯官方开源** (使用 GitHub、Gitee、工蜂 等平台的官方账号开源，微信公众号、腾讯云+社区 等官方公告)
* **社区影响力大** (GitHub 1W+ Star 在 350W Java 项目中排名前 140，远超 FLAG, BAT 等国内外绝大部分开源项目)
* **多样用户案例** (腾讯内部用户包含 互娱、音乐、云与智慧，外部用户包含 500 强上市公司、数千亿资本国企 等)
* **适用场景广泛** (社交聊天、阅读资讯、影音视频、办公学习 等各种 App、网站、公众号、小程序 等非金融类项目)
* **周边生态丰富** (Android, iOS, Web 等各种 Demo、继承 JSON 的海量生态、零代码 接口测试 和 单元测试 工具等)
* **文档视频齐全** (项目介绍、快速上手、安装部署 等后端、前端、客户端的 图文解说、视频教程、代码注释 等)
* **功能丰富强大** (增删改查、分页排序、分组聚合、各种 JOIN、各种子查询、跨库跨表、性能分析 等零代码实现)
* **使用安全简单** (自动增删改查、自动生成文档、自动管理版本、自动控制权限、自动校验参数、自动防SQL注入等)
* **灵活定制业务** (在后端编写 远程函数，可以拿到 session、version、当前 JSON 对象 等，然后自定义处理)
* **高质可靠代码** (代码严谨规范，商业分析软件源伞 Pinpoint 代码扫描报告平均每行代码 Bug 率低至 0.15%)
* **兼容各种项目** (协议不限 HTTP，与其它库无冲突，对各类 Web 框架集成友好且提供 SpringBoot, JFinal 的 Demo)
* **工程轻量小巧** (仅依赖 fastjson，Jar 仅 280KB，Java 文件仅 59 个共 13719 行代码，例如 APIJSONORM 4.3.1)
* **多年持续迭代** (自 2016 年开源至今已连续维护 4 年，累计 2000+ Commits、70+ Releases，不断更新迭代中...)


### 常见问题
#### 1.如何定制业务逻辑？
在后端编写 远程函数，可以拿到 session、version、当前 JSON 对象、参数名称 等，然后对查到的数据自定义处理 <br />
https://github.com/Tencent/APIJSON/issues/101

#### 2.如何控制权限？
在 Access 表配置校验规则，默认不允许访问，需要对 每张表、每种角色、每种操作 做相应的配置，粒度细分到行级 <br />
https://github.com/Tencent/APIJSON/issues/12

#### 3.如何校验参数？
在 Request 表配置校验规则 structure，提供 MUST、TYPE、VERIFY 等通用方法，可通过 远程函数 来完全自定义 <br />
https://github.com/Tencent/APIJSON/wiki#%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86

更多常见问题及提问前必看 <br />
https://github.com/Tencent/APIJSON/issues/36
<br />

### 注意事项
请求参数 JSON 中表名、字段名、关键词及对应的值都是大小写敏感、逗号敏感、分号敏感、空格敏感、换行敏感， <br />
大部分情况都不允许空格和换行，表名以大写字母开头，不要想当然，请严格按照 [设计规范](https://github.com/Tencent/APIJSON/blob/master/Document.md#3) 来调用 API
[#181](https://github.com/Tencent/APIJSON/issues/181)
<br />
<br />
<br />
<br />

<b >导航目录：</b> [项目简介](#--apijson) 上手使用 [社区生态](#%E6%8A%80%E6%9C%AF%E4%BA%A4%E6%B5%81)  &nbsp;&nbsp;&nbsp;&nbsp;  完整详细的导航目录 [点这里查看](/Navigation.md) <br />

### 快速上手

#### 1.后端上手
可以跳过这个步骤，直接用APIJSON服务器IP地址 apijson.cn:8080 来测试接口。<br />
见&nbsp; [APIJSON后端上手 - Java](https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Java-Server)<br />

#### 2.前端上手
可以跳过这个步骤，直接使用 [APIAuto-机器学习HTTP接口工具](https://github.com/TommyLemon/APIAuto) 或 下载客户端App。<br />
见&nbsp; [Android](https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Android) &nbsp;或&nbsp; [iOS](https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-iOS) &nbsp;或&nbsp; [JavaScript](https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-JavaScript)<br />


### 下载客户端 App

仿微信朋友圈动态实战项目<br />
[APIJSONApp.apk](http://files.cnblogs.com/files/tommylemon/APIJSONApp.apk)

测试及自动生成代码工具<br />
[APIJSONTest.apk](http://files.cnblogs.com/files/tommylemon/APIJSONTest.apk)

### 开源许可
使用 [Apache License 2.0](/LICENSE)，对 公司、团队、个人 等 商用、非商用 都自由免费且非常友好，请放心使用和登记

### 使用登记
如果您在使用 APIJSON，请让我们知道，您的使用对我们非常重要(按登记顺序排列)：<br />
https://github.com/Tencent/APIJSON/issues/187 
<div style="float:left">
   <a href="https://ieg.tencent.com"><img src="https://user-images.githubusercontent.com/5738175/126525534-461c3e33-57b1-4630-af7f-f1238ca4ab98.png" height="75"></a>
  <a href="https://cloud.tencent.com"><img src="https://user-images.githubusercontent.com/5738175/126525251-c05e64c6-6b60-4457-a46e-dea7dcfb80cd.png" height="75"></a>
  <a href="https://www.tencentmusic.com"><img src="https://user-images.githubusercontent.com/5738175/126524713-b38a8f02-3086-45d4-91db-6f5389811d51.png" height="75"></a>
  <br />

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


### 贡献者们
主项目 APIJSON 的贡献者们(6 个腾讯工程师、1 个知乎基础研发架构师、1 个圆通工程师 等)：<br />
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
</div>
<br />

生态周边项目的作者们(2 个腾讯工程师、1 个字节跳动工程师 等)：<br />
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
  <a href="https://github.com/APIJSON/apijson_todo_demo"><img src="https://avatars.githubusercontent.com/u/17522475?v=4"  height="54" width="54" ></a>
</div>
<br />

还有为 APIJSON 扫描代码贡献 Issue 的 [奇安信代码卫士](https://github.com/QiAnXinCodeSafe) 和 [源伞科技](https://www.sourcebrella.com) 
<div style="float:left">
  <a href="https://github.com/QiAnXinCodeSafe" style="margin-right: 20px"><img src="https://avatars.githubusercontent.com/u/39950310?s=460&u=8c4ad8c2174ba78c0604614a6e2feced07521ce6&v=4"  height="90" ></a>
  <a href="https://www.sourcebrella.com" style="padding: 5px"><img src="https://www.sourcebrella.com/static/img/sb_logo_zh.svg"  height="81" ></a>
</div>
<br />

感谢大家的贡献。


### 规划及路线图
新增功能、强化安全、提高性能、增强稳定、完善文档、丰富周边、推广使用 <br />
https://github.com/Tencent/APIJSON/blob/master/Roadmap.md

理论上所有支持 SQL 与 JDBC/ODBC 的软件，都可以用本项目对接 CRUD，待测试: <br />
[DB2](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.1.0/com.ibm.db2.luw.sql.ref.doc/doc/r0059224.html), [Elasticsearch](https://www.elastic.co/cn/what-is/elasticsearch-sql), [ClickHouse](https://clickhouse.tech/docs/zh/sql-reference/syntax/), [OceanBase](https://www.oceanbase.com/docs/oceanbase/V2.2.50/ss-sr-select_daur3l), [Presto](https://prestodb.io/docs/current/admin/function-namespace-managers.html), [Spark](http://spark.apache.org/sql/), [Hive](https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Select)(延伸支持 Hadoop, Spark), [Phoenix](http://phoenix.apache.org/language/index.html#select)(延伸支持 HBase), [Presto/Trino](https://prestodb.io/docs/current/sql/select.html)(延伸支持 Redis, Hive, Kafka, Elasticsearch, Thrift, Cassandra, MySQL, PostgreSQL, Oracle, MongoDB...)

### 我要赞赏
如果你喜欢 APIJSON，感觉 APIJSON 帮助到了你，可以点右上角 ⭐Star 支持一下，谢谢 ^_^ <br />
<br />
<br />
<br />

<b >导航目录：</b> [项目简介](#--apijson) [上手使用](#%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B) 社区生态  &nbsp;&nbsp;&nbsp;&nbsp;  完整详细的导航目录 [点这里查看](/Navigation.md)<br />

### 技术交流
如果有什么问题或建议可以 [提ISSUE](https://github.com/Tencent/APIJSON/issues) 或 加群，交流技术，分享经验。 <br >
如果你解决了某些bug，或者新增了一些功能，欢迎 [贡献代码](https://github.com/Tencent/APIJSON/pulls)，感激不尽~ <br >
https://github.com/Tencent/APIJSON/blob/master/CONTRIBUTING.md

<b >QQ 技术群</b>： 734652054（新）、607020115（旧）

如果你为 APIJSON 做出了以下任何一个贡献： <br >
[提交了 PR 且被合并](https://github.com/Tencent/APIJSON/pull/92)、[提交了优质 Issue](https://github.com/Tencent/APIJSON/issues/189)、[发表了优质文章](https://blog.csdn.net/qq_41829492/article/details/88670940)、[开发了可用的生态项目](https://github.com/zhangchunlin/uliweb-apijson) 或 [登记了你的公司](https://github.com/Tencent/APIJSON/issues/187)，可以加 <br >
<b >贡献者微信群</b>，注意联系 LonelyExplorer，加好友描述中附上贡献链接，谢谢


### 相关推荐
[APIJSON, 让接口和文档见鬼去吧！](https://my.oschina.net/tommylemon/blog/805459)

[仿QQ空间和微信朋友圈，高解耦高复用高灵活](https://my.oschina.net/tommylemon/blog/885787)

[后端开挂:3行代码写出8个接口！](https://my.oschina.net/tommylemon/blog/1574430)

[后端自动化版本管理，再也不用改URL了！](https://my.oschina.net/tommylemon/blog/1576587)

[3步创建APIJSON后端新表及配置](https://my.oschina.net/tommylemon/blog/889074)

[APIJSON对接分布式HTAP数据库TiDB](https://asktug.com/t/htap-tidb/395)

[APIJSON教程（一）：上手apijson项目，学习apijson语法，并实现持久层配置](https://zhuanlan.zhihu.com/p/375681893)

[apijson简单demo](https://blog.csdn.net/dmw412724/article/details/113558115)

[apijson简单使用](https://www.cnblogs.com/greyzeng/p/14311995.html)

[APIJSON简单部署和使用](https://blog.csdn.net/m450744192/article/details/108462611)

[学习自动化接口APIJSON](https://www.jianshu.com/p/981a2a630c7b)

[APIJSON 接口调试实践](https://github.com/Tencent/APIJSON/issues/189)

[APIJSON使用例子总结](https://blog.csdn.net/weixin_41077841/article/details/110518007)

[APIJSON 自动化接口和文档的快速开发神器 （一）](https://blog.csdn.net/qq_41829492/article/details/88670940)

[APIJSON在mac电脑环境下配置去连接SQL Server](https://juejin.im/post/5e16d21ef265da3e2e4f4956)

[APIJSON复杂业务深入实践（类似12306订票系统）](https://blog.csdn.net/aa330233789/article/details/105309571)

[全国行政区划数据抓取与处理](https://www.xlongwei.com/detail/21032616)

[新手搭建 APIJSON 项目指北](https://github.com/jerrylususu/apijson_todo_demo/blob/master/FULLTEXT.md)  
  
### 生态项目
[APIJSON-Demo](https://github.com/APIJSON/APIJSON-Demo) APIJSON 各种语言、各种框架 的 使用示例项目、上手文档、测试数据 SQL 文件 等

[apijson-orm](https://github.com/APIJSON/apijson-orm) APIJSON ORM 库，可通过 Maven, Gradle 等远程依赖

[apijson-framework](https://github.com/APIJSON/apijson-framework) APIJSON 服务端框架，可通过 Maven, Gradle 等远程依赖

[apijson-column](https://github.com/APIJSON/apijson-column) APIJSON 的字段插件，支持 字段名映射 和 !key 反选字段

[APIAuto](https://github.com/TommyLemon/APIAuto) 敏捷开发最强大易用的 HTTP 接口工具，机器学习零代码测试、生成代码与静态检查、生成文档与光标悬浮注释

[UnitAuto](https://github.com/TommyLemon/UnitAuto) 机器学习单元测试平台，零代码、全方位、自动化 测试 方法/函数 的正确性和可用性

[apijson-doc](https://github.com/vincentCheng/apijson-doc) APIJSON 官方文档，提供排版清晰、搜索方便的文档内容展示，包括设计规范、图文教程等

[APIJSONdocs](https://github.com/ruoranw/APIJSONdocs) APIJSON 英文文档，提供排版清晰的文档内容展示，包括详细介绍、设计规范、使用方式等

[apijson.org](https://github.com/APIJSON/apijson.org) APIJSON 官方网站，提供 APIJSON 的 功能简介、登记用户、作者与贡献者、相关链接 等

[APIJSON.NET](https://github.com/liaozb/APIJSON.NET) C# 版 APIJSON ，支持 MySQL, PostgreSQL, SQL Server, Oracle, SQLite

[apijson-go](https://gitee.com/tiangao/apijson-go) Go 版 APIJSON ，支持单表查询、数组查询、多表一对一关联查询、多表一对多关联查询 等

[APIJSON-php](https://github.com/xianglong111/APIJSON-php) PHP 版 APIJSON，基于 ThinkPHP，支持 MySQL, PostgreSQL, SQL Server, Oracle 等

[apijson-php](https://github.com/qq547057827/apijson-php) PHP 版 APIJSON，基于 ThinkPHP，支持 MySQL, PostgreSQL, SQL Server, Oracle 等

[apijson-node](https://github.com/kevinaskin/apijson-node) Node.ts 版 APIJSON，提供 nestjs 和 typeorm 的 Demo，由字节跳动工程师开发

[uliweb-apijson](https://github.com/zhangchunlin/uliweb-apijson) Python 版 APIJSON，支持 MySQL, PostgreSQL, SQL Server, Oracle, SQLite 等

[APIJSONParser](https://github.com/Zerounary/APIJSONParser) 第三方 APIJSON 解析器，将 JSON 动态解析成 SQL

[FfApiJson](https://gitee.com/own_3_0/ff-api-json) 用 JSON 格式直接生成 SQL，借鉴 APIJSON 支持多数据源

[APIJSON-ToDo-Demo](https://github.com/jerrylususu/apijson_todo_demo) 一个简单的 todo 示例项目，精简数据，简化上手流程，带自定义鉴权逻辑 
  
[apijson-learn](https://github.com/rainboy-learn/apijson-learn) APIJSON 学习笔记和源码解析

[apijson-sample](https://gitee.com/greyzeng/apijson-sample) APIJSON 简单使用 Demo 及教程

[apijson-examples](https://gitee.com/drone/apijson-examples) APIJSON 的前端、业务后端、管理后端 Demo

[light4j](https://github.com/xlongwei/light4j) 整合 APIJSON 和微服务框架 light-4j 的 Demo，同时接入了 Redis

[SpringServer1.2-APIJSON](https://github.com/Airforce-1/SpringServer1.2-APIJSON) 智慧党建服务器端，提供 上传 和 下载 文件的接口

[apijson-examples](https://gitee.com/drone/apijson-examples) APIJSON 的前端、业务后端、管理后端 Demo

[ApiJsonByJFinal](https://gitee.com/zhiyuexin/ApiJsonByJFinal) 整合 APIJSON 和 JFinal 的 Demo

[APIJSONDemo_ClickHouse](https://github.com/chenyanlann/APIJSONDemo_ClickHouse) APIJSON + SpringBoot 连接 ClickHouse 使用的 Demo

[apijson-builder](https://github.com/pengxianggui/apijson-builder) 一个方便为 APIJSON 构建 RESTful 请求的 JavaScript 库

[AbsGrade](https://github.com/APIJSON/AbsGrade) 列表级联算法，支持微信朋友圈单层评论、QQ空间双层评论、百度网盘多层(无限层)文件夹等

[APIJSON-Android-RxJava](https://github.com/TommyLemon/APIJSON-Android-RxJava) 仿微信朋友圈动态实战项目，ZBLibrary(UI) + APIJSON(HTTP) + RxJava(Data)

[Android-ZBLibrary](https://github.com/TommyLemon/Android-ZBLibrary) Android MVP快速开发框架，Demo全面，注释详细，使用简单，代码严谨
  
感谢热心的作者们的贡献，点 ⭐Star 支持下他们吧。

### 腾讯犀牛鸟开源人才培养计划
https://github.com/Tencent/APIJSON/issues/229


####  qiujunlin **2.接入 presto/hive/clickhouse/db2 任意一个**

APIJSON 接入 clickhouse 使用demo <br />
https://github.com/qiujunlin/APIJSONDemo

#### zhangshukun 2.接入 presto/hive/clickhouse/db2 任意一个 
APIJSON-Demo接入db2 <br />
https://github.com/andream7/apijson-db2

#### hanxu 1.完善入门介绍视频 
重构 APIJSON 文档 <br /> 
https://hanxu2018.github.io/APIJSON-DOC/ <br /> 
文档源码  <br /> 
https://github.com/HANXU2018/APIJSON-DOC  <br /> 
配套评论区 apijson-doc-Comment  <br /> 
https://github.com/HANXU2018/apijson-doc-Comment

#### chenyanlan 2.接入 presto/hive/clickhouse/db2 任意一个 
APIJSON + SpringBoot连接ClickHouse使用的Demo <br /> 
https://github.com/chenyanlann/APIJSONDemo_ClickHouse

#### zhaoqiming 1.完善入门介绍视频  
APIJSON 后端教程（1）：简介
https://www.bilibili.com/video/BV1vL411W7yd

APIJSON 后端教程（2）：数据库 
https://www.bilibili.com/video/BV1eB4y1N77s

APIJSON 后端教程（3）：Demo
https://www.bilibili.com/video/BV1FX4y1c7ug

APIJSON 后端教程（4）：Boot
https://www.bilibili.com/video/BV18h411z7FK

APIJSON 后端教程（5）：Final
https://www.bilibili.com/video/BV1GM4y1N7XJ

APIJSON 后端教程（6）：uliweb_apijson
https://www.bilibili.com/video/BV1yb4y1S79v/

APIJSON 后端教程（7）：问题答疑
https://www.bilibili.com/video/BV1dQ4y1h7Df
  
APIJSON配套文档：
https://github.com/kenlig/apijsondocs

#### huwen 2.接入 presto/hive/clickhouse/db2 任意一个
APIJSON-Demo 接入presto
https://github.com/hclown9804/APIJSONDemo_presto

#### zhanghaoling 1.完善入门介绍视频
APIJSON结合已有项目，简化开发流程
https://github.com/haolingzhang1/APIJson--demo

说明文档
https://github.com/haolingzhang1/APIJson--demo/tree/main/APIJson集成项目说明
  
(1)官方demo
https://github.com/haolingzhang1/APIJson--demo/blob/main/APIJson集成项目说明/APIJson集成现有项目（1）-%20官方demo.pdf
  
(2)单表配置
https://github.com/haolingzhang1/APIJson--demo/blob/main/APIJson集成项目说明/APIJson集成现有项目（2）-%20单表配置.pdf 
  
#### zhoukaile 1.完善入门介绍视频

视频链接：https://www.bilibili.com/video/BV1Uh411z7kZ/

文档链接：https://gitee.com/funkiz/apijson_camp

#### lintao 1.完善入门介绍视频

APIJSON 上手教程：https://www.bilibili.com/video/BV1Pq4y1n7rJ

### 持续更新

https://github.com/Tencent/APIJSON/commits/master

### 工蜂主页
https://git.code.tencent.com/Tencent_Open_Source/APIJSON

### 码云主页
https://gitee.com/Tencent/APIJSON
