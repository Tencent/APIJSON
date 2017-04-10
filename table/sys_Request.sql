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
  `id` int(11) NOT NULL,
  `method` varchar(10) DEFAULT 'GET',
  `tag` varchar(20) NOT NULL,
  `structure` json NOT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,'POST','User','{\"User\": {\"disallowColumns\": \"id\", \"necessaryColumns\": \"name,phone\"}, \"necessaryColumns\": \"password,verify\"}','\"User:toUser\":{}转化为\"toUser\":User的JSONObject ？','2017-02-01 11:19:51'),(2,'POST','Moment','{\"disallowColumns\": \"id\", \"necessaryColumns\": \"userId,pictureList\"}',NULL,'2017-02-01 11:19:51'),(3,'POST','Comment','{\"disallowColumns\": \"id\", \"necessaryColumns\": \"userId,momentId,content\"}',NULL,'2017-02-01 11:19:51'),(4,'PUT','User','{\"disallowColumns\": \"phone\", \"necessaryColumns\": \"id\"}',NULL,'2017-02-01 11:19:51'),(6,'PUT','userPhone','{\"User\": {\"disallowColumns\": \"!\", \"necessaryColumns\": \"id,phone\"}}','! 表示其它所有，这里指necessaryColumns所有未包含的字段','2017-02-01 11:19:51'),(7,'DELETE','Moment','{\"necessaryColumns\": \"id\"}','所有删除都要有currentUserId和loginPassword？','2017-02-01 11:19:51'),(8,'DELETE','Comment','{\"necessaryColumns\": \"id\"}','disallowColumns没必要用于DELETE','2017-02-01 11:19:51'),(9,'PUT','Password','{\"Password\": {\"disallowColumns\": \"!\", \"necessaryColumns\": \"id,value\"}, \"necessaryColumns\": \"oldPassword\"}','对安全要求高，不允许客户端改type,date等字段','2017-02-01 11:19:51'),(10,'PUT','User.phone','{\"User\": {\"disallowColumns\": \"!\", \"necessaryColumns\": \"id,phone\"}}','! 表示其它所有，这里指necessaryColumns所有未包含的字段','2017-02-01 11:19:51'),(13,'POST','MomentWithComment','{\"Moment\": {\"Comment\": {\"disallowColumns\": \"id\", \"necessaryColumns\": \"userId,workId,content\"}, \"disallowColumns\": \"id\", \"necessaryColumns\": \"userId,title,content,picture\"}}','用的极少，可能根本用不上','2017-02-01 11:19:51'),(18,'DELETE','User','{\"necessaryColumns\": \"id\"}','','2017-02-01 11:19:51'),(19,'POST_GET','Password','{\"disallowColumns\": \"!\", \"necessaryColumns\": \"table,tableId\"}','String:table, Long: tableId 限制类型？','2017-02-18 12:03:17'),(20,'POST_GET','Wallet','{\"Wallet\": {\"disallowColumns\": \"!\", \"necessaryColumns\": \"userId\"}, \"necessaryColumns\": \"currentUserId,loginPassword\"}',NULL,'2017-02-18 14:20:43'),(22,'POST_GET','Login','{\"User\": {\"necessaryColumns\": \"phone\"}, \"Password\": {\"disallowColumns\": \"!\", \"necessaryColumns\": \"table,phone,value\"}}',NULL,'2017-02-18 14:20:43'),(23,'POST','Login','{\"disallowColumns\": \"!\", \"necessaryColumns\": \"userId,type\"}',NULL,'2017-02-18 14:20:43'),(24,'POST','Verify','{\"disallowColumns\": \"!\", \"necessaryColumns\": \"id,code\"}',NULL,'2017-02-18 14:20:43'),(25,'POST_GET','Verify','{\"necessaryColumns\": \"id\"}',NULL,'2017-02-18 14:20:43'),(26,'DELETE','Verify','{\"disallowColumns\": \"!\", \"necessaryColumns\": \"id\"}',NULL,'2017-02-18 14:20:43'),(27,'POST_HEAD','Verify','{}',NULL,'2017-02-18 14:20:43'),(28,'PUT','Moment','{\"disallowColumns\": \"userId,date\", \"necessaryColumns\": \"id\"}',NULL,'2017-02-01 11:19:51');
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

-- Dump completed on 2017-04-10 15:21:06
