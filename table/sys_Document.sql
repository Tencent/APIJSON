-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 39.108.143.172    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Document`
--

DROP TABLE IF EXISTS `Document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Document` (
  `id` bigint(15) NOT NULL COMMENT '唯一标识',
  `userId` bigint(15) NOT NULL COMMENT '用户id\n应该用adminId，只有当登录账户是管理员时才能操作文档。\n需要先建Admin表，新增登录等相关接口。',
  `version` tinyint(4) NOT NULL DEFAULT '2' COMMENT '接口版本号\n<=0 - 不限制版本，任意版本都可用这个接口\n>0 - 在这个版本添加的接口',
  `name` varchar(50) NOT NULL COMMENT '接口名称',
  `url` varchar(250) NOT NULL COMMENT '请求地址',
  `request` text NOT NULL COMMENT '请求\n用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用例文档\n后端开发者在测试好后，把选好的测试用例上传，这样就能共享给前端/客户端开发者';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Document`
--

LOCK TABLES `Document` WRITE;
/*!40000 ALTER TABLE `Document` DISABLE KEYS */;
INSERT INTO `Document` VALUES (1,0,1,'登录','/login','{\"type\": 0, \"phone\": \"13000082001\", \"version\": 1, \"password\": \"123456\"}','2017-11-26 07:35:19'),(2,0,1,'注册(先获取验证码type:1)','/register','{\n    \"Privacy\": {\n        \"phone\": \"13000083333\",\n        \"_password\": \"123456\"\n    },\n    \"User\": {\n        \"name\": \"APIJSONUser\"\n    },\n    \"verify\": \"6840\"\n}',NULL),(3,0,1,'退出登录','/logout','{}','2017-11-26 09:56:10'),(1511689914599,0,1,'获取用户隐私信息','/gets','{\"tag\": \"Privacy\", \"Privacy\": {\"id\": 82001}}','2017-11-26 09:51:54'),(1511796155277,0,1,'获取验证码','/post/verify','{\"type\": 0, \"phone\": \"13000082001\"}','2017-11-27 15:22:35'),(1511796208670,0,1,'检查验证码是否存在','/heads/verify','{\"type\": 0, \"phone\": \"13000082001\"}','2017-11-27 15:23:28'),(1511796589079,0,1,'修改登录密码(先获取验证码type:2)-手机号+验证码','/put/password','{\"verify\": \"10322\", \"Privacy\": {\"phone\": \"13000082001\", \"_password\": \"666666\"}}','2017-11-27 15:29:49'),(1511796882184,0,1,'充值(需要支付密码)/提现','/put/balance','{\"tag\": \"Privacy\", \"Privacy\": {\"id\": 82001, \"balance+\": 100.15, \"_payPassword\": \"123456\"}}','2017-11-27 15:34:42'),(1511963330795,0,2,'获取文档列表(即在线解析网页上的共享)-API调用方式','/get','{\n    \"Document[]\": {\n        \"Document\": {\n            \"@role\": \"login\",\n            \"@order\": \"version-,date-\"\n        }\n    }\n}','2017-11-29 13:48:50'),(1511963677325,82001,1,'获取用户','/get','{\"User\": {\"id\": 82001}}','2017-11-29 13:54:37'),(1511963722970,82001,1,'获取用户列表(\"id{}\":contactIdList)-朋友页','/get','{\n    \"User[]\": {\n        \"count\": 10,\n        \"page\": 0,\n        \"User\": {\n            \"@column\": \"id,sex,name,tag,head\",\n            \"@order\": \"name+\",\n            \"id{}\": [\n                82002,\n                82004,\n                70793\n            ]\n        }\n    }\n}','2017-11-29 13:55:22'),(1511963990072,82001,1,'获取动态Moment+User+praiseUserList','/get','{\n    \"Moment\": {\n        \"id\": 15\n    },\n    \"User\": {\n        \"id@\": \"Moment/userId\",\n        \"@column\": \"id,name,head\"\n    },\n    \"User[]\": {\n        \"count\": 10,\n        \"User\": {\n            \"id{}@\": \"Moment/praiseUserIdList\",\n            \"@column\": \"id,name\"\n        }\n    }\n}','2017-11-29 13:59:50'),(1511964176689,82001,1,'获取评论列表-动态详情页Comment+User','/get','{\n    \"[]\": {\n        \"count\": 20,\n        \"page\": 0,\n        \"Comment\": {\n            \"@order\": \"date+\",\n            \"momentId\": 15\n        },\n        \"User\": {\n            \"id@\": \"/Comment/userId\",\n            \"@column\": \"id,name,head\"\n        }\n    }\n}','2017-11-29 14:02:56'),(1511967853340,82001,1,'获取动态列表Moment+User+User:parise[]+Comment[]','/get','{\n    \"[]\": {\n        \"count\": 5,\n        \"page\": 0,\n        \"Moment\": {\n            \"@order\": \"date-\"\n        },\n        \"User\": {\n            \"id@\": \"/Moment/userId\",\n            \"@column\": \"id,name,head\"\n        },\n        \"User[]\": {\n            \"count\": 10,\n            \"User\": {\n                \"id{}@\": \"[]/Moment/praiseUserIdList\",\n                \"@column\": \"id,name\"\n            }\n        },\n        \"[]\": {\n            \"count\": 6,\n            \"Comment\": {\n                \"@order\": \"date+\",\n                \"momentId@\": \"[]/Moment/id\"\n            },\n            \"User\": {\n                \"id@\": \"/Comment/userId\",\n                \"@column\": \"id,name\"\n            }\n        }\n    }\n}','2017-11-29 15:04:13'),(1511969181104,82001,1,'添加朋友','/put','{\n    \"User\": {\n        \"id\": 82001,\n        \"contactIdList+\": [93793]\n    },\n    \"tag\": \"User\"\n}','2017-11-29 15:26:21'),(1511969417633,82001,1,'点赞/取消点赞','/put','{\n    \"Moment\": {\n        \"id\": 15,\n        \"praiseUserIdList-\": [\n            82001\n        ]\n    },\n    \"tag\": \"Moment\"\n}','2017-11-29 15:30:17'),(1511969630372,82001,1,'新增评论','/post','{\n    \"Comment\": {\n        \"userId\": 82001,\n        \"momentId\": 15,\n        \"content\": \"测试新增评论\"\n    },\n    \"tag\": \"Comment\"\n}','2017-11-29 15:33:50'),(1511970009072,82001,1,'新增动态','/post','{\n    \"Moment\": {\n        \"userId\": 82001,\n        \"content\": \"测试新增动态\",\n        \"pictureList\": [\"http://static.oschina.net/uploads/user/48/96331_50.jpg\"\n        ]\n    },\n    \"tag\": \"Moment\"\n}','2017-11-29 15:40:09'),(1511970224333,82001,1,'修改用户信息','/put','{\n    \"User\": {\n        \"id\": 82001,\n        \"name\": \"测试改名\"\n    },\n    \"tag\": \"User\"\n}','2017-11-29 15:43:44'),(1512216131855,0,1,'获取文档列表(即在线解析网页上的文档)-表和字段、请求格式限制','/get','{\n    \"[]\": {\n        \"Table\": {\n            \"TABLE_SCHEMA\": \"sys\",\n            \"TABLE_TYPE\": \"BASE TABLE\",\n            \"TABLE_NAME!$\": [\n                \"\\\\_%\",\n                \"sys\\\\_%\",\n                \"system\\\\_%\"\n            ],\n            \"@order\": \"TABLE_NAME+\",\n            \"@column\": \"TABLE_NAME,TABLE_COMMENT\"\n        },\n        \"Column[]\": {\n            \"Column\": {\n                \"TABLE_NAME@\": \"[]/Table/TABLE_NAME\",\n                \"@column\": \"COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT\"\n            }\n        }\n    },\n    \"Request[]\": {\n        \"Request\": {\n            \"@order\": \"version-,method-\"\n        }\n    }\n}','2017-12-02 12:02:11'),(1514362302911,82001,2,'get请求','/get','{\n    \"[]\": {\n        \"User\": {\n            \"sex\": 1\n        }\n    }\n}','2017-12-27 08:11:42');
/*!40000 ALTER TABLE `Document` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-30 17:36:25
