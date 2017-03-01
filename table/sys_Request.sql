-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.16

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
-- Table structure for table `Request`
--

DROP TABLE IF EXISTS `Request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `method` varchar(10) DEFAULT 'GET',
  `tag` varchar(20) NOT NULL,
  `structure` varchar(1000) NOT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mapedStructure` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,'POST','User','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"name,phone\"}','\"User:toUser\":{}转化为\"toUser\":User的JSONObject ？','2017-02-01 11:19:51',NULL),(2,'POST','Moment','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,title,content,picture\"}',NULL,'2017-02-01 11:19:51',NULL),(3,'POST','Comment','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,workId,content\"}',NULL,'2017-02-01 11:19:51',NULL),(4,'PUT','User','{\"disallowColumns\":\"phone\", \"necessaryColumns\":\"id\"}',NULL,'2017-02-01 11:19:51',NULL),(6,'PUT','userPhone','{\"User\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id,phone\"}}','! 表示其它所有，这里指necessaryColumns所有未包含的字段','2017-02-01 11:19:51',NULL),(7,'DELETE','Moment','{\"necessaryColumns\":\"id\"}','所有删除都要有currentUserId和loginPassword？','2017-02-01 11:19:51',NULL),(8,'DELETE','Comment','{\"necessaryColumns\":\"id\"}','disallowColumns没必要用于DELETE','2017-02-01 11:19:51',NULL),(9,'PUT','Password','{\"Password\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id,value\"}, \"necessaryColumns\":\"oldPassword\"}','对安全要求高，不允许客户端改type,date等字段','2017-02-01 11:19:51',NULL),(10,'PUT','User.phone','{\"User\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id,phone\"}}','! 表示其它所有，这里指necessaryColumns所有未包含的字段','2017-02-01 11:19:51',NULL),(13,'POST','MomentWithComment','{\"Moment\":{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,title,content,picture\", \"Comment\":{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,workId,content\"}}}','用的极少，可能根本用不上','2017-02-01 11:19:51',NULL),(14,'DELETE','Comment[]','{table:\"Comment\", \"id[]\":\",\"}','用的极少。多个table不好处理','2017-02-01 11:19:51',NULL),(15,'POST','User[]','{\"id[]\":\",\"}','用的比较少，导入联系人、添加进群常用。特殊处理：服务端指定table','2017-02-01 11:19:51',NULL),(16,'POST','User[]','{\"[]\":{\"User\":{\"id\":\"\"}}}','不用RequetParser，用单独的方法批量处理','2017-02-01 11:19:51',NULL),(17,'POST','User[]','{\"User\":[\"id\":\"\"]}','解释成16这种','2017-02-01 11:19:51',NULL),(18,'DELETE','User','{\"necessaryColumns\":\"id\"}','','2017-02-01 11:19:51',NULL),(19,'POST_GET','Password','{\"disallowColumns\":\"!\", \"necessaryColumns\":\"table,tableId\"}','table:String, tableId:Long 表示类型？还是String:table反过来写？','2017-02-18 12:03:17',NULL),(20,'POST_GET','Wallet','{\"Wallet\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"userId\"}, \"necessaryColumns\":\"currentUserId,loginPassword\"}',NULL,'2017-02-18 14:20:43',NULL),(21,'POST_GET','Wallet2','*','*表示接受任何形式的请求','2017-02-18 14:26:38',NULL),(22,'POST_GET','Login','{\"Password\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"table,phone,value\"}, \"User\":{\"necessaryColumns\":\"phone\"}}',NULL,'2017-02-18 14:20:43',NULL);
/*!40000 ALTER TABLE `Request` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-01 12:33:40
