# APIJSON后端部署 - Java

### 1.下载后解压APIJSON工程<h3/>

Clone or download &gt; Download ZIP &gt; 解压到一个路径并记住这个路径。

#### 你可以跳过 步骤2 和 步骤3，用我的服务器IP地址 39.108.143.172:8080 来测试后端对前端请求的返回结果。

<br />

### 2.导入表文件到数据库<h3/>

后端需要MySQL Server和MySQLWorkbench，没有安装的都先下载安装一个。<br />
我的配置是Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 和 OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8，其中系统和软件都是64位的。

启动MySQLWorkbench &gt; 进入一个Connection &gt; 点击Server菜单 &gt; Data Import &gt; 选择刚才解压路径下的APIJSON-Master/table &gt; Start Import &gt; 刷新SCHEMAS, 左下方sys/tables会出现添加的table。

<br />

### 3.用IntellIJ IDEA Ultimate或Eclipse for JavaEE运行后端工程<h3/>

如果以上编辑器一个都没安装，运行前先下载安装一个。<br />
我的配置是Windows 7 + JDK 1.7.0_71 + Eclipse 4.6.1 + IntellIJ 2016.3 和 OSX EI Capitan + JDK 1.8.0_91 + Eclipse 4.6.1 + IntellIJ 2016.2.5


#### IntellIJ IDEA Ultimate

1)导入<br />
Open > 选择刚才解压路径下的APIJSON-Master/APIJSON(Server)/APIJSON(Idea) > OK

2)配置(已有默认配置，可跳过)<br />
打开 zuo.biao.apijson.server.sql.SQLConfig 类，编辑 MYSQL_URI，MYSQL_SCHEMA，MYSQL_ACCOUNT，MYSQL_PASSWORD 为你自己数据库的配置。

3)运行<br />
Run > Run APIJSONApplication


#### Eclipse for JavaEE

1)导入<br />
File > Import > Maven > Existing Maven Projects > Next > Browse > 选择刚才解压路径下的APIJSON-Master/APIJSON(Server)/APIJSON(Eclipse_JEE) > Finish

2)配置(已有默认配置，可跳过)<br />
打开 zuo.biao.apijson.server.sql.SQLConfig 类，编辑 MYSQL_URI，MYSQL_SCHEMA，MYSQL_ACCOUNT，MYSQL_PASSWORD 为你自己数据库的配置。

3)运行<br />
Run > Run As > Java Application > 选择APIJSONApplication > OK

<br />

### 4.测试接口<br />
直接使用 [APIJSON在线工具](http://39.108.143.172/) 或 下载主页提供的 [客户端App](https://github.com/TommyLemon/APIJSON)。

<br />
