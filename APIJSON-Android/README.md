# APIJSON前端部署 - Android 

### 1.下载后解压APIJSON工程<h3/>

Clone or download &gt; Download ZIP &gt; 解压到一个路径并记住这个路径。


### 2.用Android Studio或ADT Bundle运行Android工程<h3/>


如果以上IDE一个都没安装，运行前先下载安装一个。<br />
我的配置是Windows 7 + JDK 1.7.0_71 + ADT Bundle 20140702 + Android Studio 2.2 和 OSX EI Capitan +（JDK 1.7.0_71 + ADT Bundle 20140702）+（JDK 1.8.0_91 + Android Studio 2.1.2），其中系统和软件都是64位的。


#### Android Studio

1)打开<br />
Open an existing Android Studio project > 选择刚才解压路径下的APIJSON-Master/APIJSON-Android/APIJSON-AndroidStudio/APIJSONApp （或APIJSONTest） > OK

2)运行<br />
Run > Run app

#### ADT Bundle

1)打开<br />
File > Import > Android > Existing Android Code Into Workspace > Next > Browse > 选择刚才解压路径下的APIJSON-Master/APIJSON-Android-/APIJSON-ADT > Finish

2)运行<br />
Run > Run As > Android Application


### 3.测试接口<h3/>

选择发送APIJSON请求并等待显示结果。<br />
如果默认url不可用，修改为一个可用的，比如正在运行APIJSON后端工程的电脑的IPV4地址，然后点击查询按钮重新请求。

<br />