# APIJSON后端部署 - Java

### 1.下载后解压APIJSON工程

[打开APIJSON的GitHub主页](https://github.com/TommyLemon/APIJSON) &gt; Clone or download &gt; [Download ZIP](https://github.com/TommyLemon/APIJSON/archive/master.zip) &gt; 解压到一个路径并记住这个路径。


<br />

### 2.用Eclipse for JavaEE或IntellIJ IDEA Ultimate运行后端工程

如果以上编辑器一个都没安装，运行前先下载安装一个。<br />
我的配置是Windows 7 + JDK 1.7.0_71 + Eclipse 4.6.1 + IntellIJ 2016.3 和 OSX EI Capitan + JDK 1.8.0_91 + Eclipse 4.6.1 + IntellIJ 2016.2.5


#### Eclipse for JavaEE

<h5>1)打开</h5>
File > Import > Maven > Existing Maven Projects > Next > Browse <br />
 > 选择刚才解压路径下的APIJSON-Master/APIJSON-Java-Server/APIJSON-Eclipse <br />
 > 勾选 APIJSONDemo 和 APIJSONLibrary > Finish

<h5>2)配置(如果完成下方步骤4，导入APIJSON的表，则可跳过)</h5>
打开 zuo.biao.apijson.server.sql.SQLConfig 类，编辑 MYSQL_URI，MYSQL_SCHEMA，MYSQL_ACCOUNT，MYSQL_PASSWORD 为你自己数据库的配置。

<h5>3)运行</h5>
Run > Run As > Java Application > 选择APIJSONApplication > OK


#### IntellIJ IDEA Ultimate

<h5>1)打开</h5>
Open > 选择刚才解压路径下的APIJSON-Master/APIJSON-Java-Server/APIJSON-Idea/APIJSONDemo > OK

如果有错误，一般是Idea没有给Module分配JDK， <br />
在Project Structure最下方的Problems会有project SDK is not defined报错。 <br /><br />
File > Project Structure > Project > Project SDK 选已安装的JDK <br />
如果没有，则继续 <br />
New... > JDK > 选择JDK的安装路径 > Open <br />
最后在底部 <br />
Apply 或 OK <br />

<h5>2)Demo依赖Library</h5>
右键APIJSONDemo > Open Module Settings > Modules > apijsondemo > Dependencies <br />
\> 左下方 + > Module Dependency > 选择apijsonlibrary > OK > OK

<h5>3)配置(如果完成下方步骤4，导入APIJSON的表，则可跳过)</h5>
打开 zuo.biao.apijson.server.sql.SQLConfig 类，编辑 MYSQL_URI，MYSQL_SCHEMA，MYSQL_ACCOUNT，MYSQL_PASSWORD 为你自己数据库的配置。 <br />

<h5>4)运行</h5>
Run > Run APIJSONApplication <br />

如果弹窗里只有Edit Configurations...这个选项，则点进去，然后 <br />
\+ > Application > Main class 选apijson.demo.server.APIJSONApplication <br />
最后在底部 <br />
Apply 或 Run <br />

<h4>运行后会出现 APIJSON的测试日志，最后显示 "APIJSON已启动" ，说明已启动完成。</h4>

如果是Address already in use，说明8080端口被占用，<br />
可以关闭占用这个端口的程序(可能就是已运行的APIJSON工程) <br />
或者 改下APIJSON工程的端口号，参考[SpringBoot改端口](https://stackoverflow.com/questions/21083170/spring-boot-how-to-configure-port)。<br />
其它问题请谷歌或百度。

<br />

### 3.测试连接<br />
在浏览器输入 [http://localhost:8080/get/{}](http://localhost:8080/get/{}) <br />
如果出现
```json
{
  "code": 200,
  "msg": "success"
}
```
则说明已连接上。<br />

如果是404 Not Found，请把防火墙关闭，以便外网能够访问你的电脑或服务器。<br />
其它问题请谷歌或百度。

<br />

### 4.导入表文件到数据库<h3/>

<h4>可以先跳过，用Table, Column或者其它 你自己数据库中已有的表 来测试。</h4>

后端需要MySQL Server和MySQLWorkbench，没有安装的都先下载安装一个。<br />
我的配置是Windows 7 + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.7 和 OSX EI Capitan + MySQL Community Server 5.7.16 + MySQLWorkbench 6.3.8，其中系统和软件都是64位的。

启动MySQLWorkbench &gt; 进入一个Connection &gt; 点击Server菜单 &gt; Data Import &gt; 选择刚才解压路径下的APIJSON-Master/table &gt; Start Import &gt; 刷新SCHEMAS, 左下方sys/tables会出现添加的table。

<br />

### 5.测试接口<br />
直接使用 [APIJSON在线工具](http://39.108.143.172/) 或 下载主页提供的 [客户端App](https://github.com/TommyLemon/APIJSON)。

<br />
