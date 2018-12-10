-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 47.98.196.224    Database: sys
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
-- Table structure for table `Access`
--

DROP TABLE IF EXISTS `Access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Access` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `schema` varchar(20) NOT NULL DEFAULT 'sys' COMMENT '集合空间',
  `name` varchar(50) NOT NULL COMMENT '实际表名，例如 apijson_user',
  `alias` varchar(20) DEFAULT NULL COMMENT '外部调用的表别名，例如 User',
  `get` varchar(100) NOT NULL DEFAULT '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]' COMMENT '允许 get 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]\n用 JSON 类型不能设置默认值，反正权限对应的需求是明确的，也不需要自动转 JSONArray。',
  `head` varchar(100) NOT NULL DEFAULT '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]' COMMENT '允许 head 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `gets` varchar(100) NOT NULL DEFAULT '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]' COMMENT '允许 gets 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `heads` varchar(100) NOT NULL DEFAULT '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]' COMMENT '允许 heads 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `post` varchar(100) NOT NULL DEFAULT '["OWNER", "ADMIN"]' COMMENT '允许 post 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `put` varchar(100) NOT NULL DEFAULT '["OWNER", "ADMIN"]' COMMENT '允许 put 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `delete` varchar(100) NOT NULL DEFAULT '["OWNER", "ADMIN"]' COMMENT '允许 delete 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='权限配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Access`
--

LOCK TABLES `Access` WRITE;
/*!40000 ALTER TABLE `Access` DISABLE KEYS */;
INSERT INTO `Access` VALUES (1,'sys','Table',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:14'),(2,'sys','Column',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:14'),(3,'sys','Request',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:14'),(4,'sys','Response',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:15'),(5,'sys','Function',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:15'),(6,'sys','Document',NULL,'[\"LOGIN\", \"ADMIN\"]','[\"LOGIN\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:38:15'),(7,'sys','Test',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[]','[]','[]','2018-11-28 16:38:15'),(8,'sys','TestRecord',NULL,'[\"LOGIN\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:38:15'),(11,'sys','apijson_user','User','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:28:53'),(12,'sys','apijson_privacy','Privacy','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:29:48'),(13,'sys','Moment',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:29:19'),(14,'sys','Comment',NULL,'[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','[\"OWNER\", \"ADMIN\"]','2018-11-28 16:29:19'),(15,'sys','Login',NULL,'[]','[]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[ \"ADMIN\"]','[ \"ADMIN\"]','[\"ADMIN\"]','2018-11-28 16:29:48'),(16,'sys','Verify',NULL,'[]','[]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[\"UNKNOWN\", \"LOGIN\", \"CONTACT\", \"CIRCLE\", \"OWNER\", \"ADMIN\"]','[ \"ADMIN\"]','[\"ADMIN\"]','2018-11-28 16:29:48');
/*!40000 ALTER TABLE `Access` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-10 23:39:35
