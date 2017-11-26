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
  `version` tinyint(4) NOT NULL DEFAULT '2' COMMENT '版本号',
  `name` varchar(50) NOT NULL,
  `url` varchar(250) DEFAULT NULL,
  `request` json NOT NULL COMMENT '请求',
  `description` varchar(1000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用例文档\n后端开发者在测试好后，把选好的测试用例上传，这样就能共享给前端/客户端开发者';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Document`
--

LOCK TABLES `Document` WRITE;
/*!40000 ALTER TABLE `Document` DISABLE KEYS */;
INSERT INTO `Document` VALUES (1,82001,2,'登录','/login','{\"type\": 0, \"phone\": \"13000082001\", \"version\": 1, \"password\": \"123456\"}',NULL,'2017-11-26 07:35:19'),(2,82002,2,'注册','/register','{\"User\": {\"name\": \"APIJSONUser\"}, \"verify\": \"1234\", \"Privacy\": {\"phone\": \"13000082222\", \"_password\": \"123456\"}}',NULL,NULL),(3,0,2,'退出登录','/logout','{}',NULL,'2017-11-26 09:56:10'),(1511689914599,82001,2,'获取用户隐私信息','/gets','{\"tag\": \"Privacy\", \"Privacy\": {\"id\": 82001}}',NULL,'2017-11-26 09:51:54');
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

-- Dump completed on 2017-11-26 19:43:31
