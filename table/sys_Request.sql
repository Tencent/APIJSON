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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,'POST','User','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"name,phone\"}',NULL),(2,'POST','Work','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,title,content,picture\"}',NULL),(3,'POST','Comment','{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,workId,content\"}',NULL),(4,'PUT','User','{\"disallowColumns\":\"phone\", \"necessaryColumns\":\"id\"}',NULL),(6,'PUT','userPhone','{\"User\":{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id,phone\"}}','! 表示其它所有，这里指necessaryColumns所有未包含的字段'),(7,'DELETE','Work','{\"id\":\"\", \"currentUserId\":\"\", \"loginPassword\":\"\"}',NULL),(8,'DELETE','Comment','{\"id\":\"\", \"currentUserId\":\"\", \"loginPassword\":\"\"}',NULL),(9,'PUT','loginPassword','{\"currentUserId\":\"\", \"oldPassword\":\"\", \"newPassword\":\"\"}',NULL),(10,'PUT','payPassword','{\"currentUserId\":\"\", \"oldPassword\":\"\", \"newPassword\":\"\"}',NULL),(12,'DELETE','Work','{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id\"}',NULL),(13,'POST','WorkWithComment','{\"Work\":{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,title,content,picture\", \"Comment\":{\"disallowColumns\":\"id\", \"necessaryColumns\":\"userId,workId,content\"}}}','用的极少，可能根本用不上'),(14,'DELETE','Comment[]','{table:\"Comment\", \"id[]\":\",\"}','用的极少。多个table不好处理'),(15,'POST','User[]','{\"id[]\":\",\"}','用的比较少，导入联系人、添加进群常用。特殊处理：服务端指定table'),(16,'POST','User[]','{\"[]\":{\"User\":{\"id\":\"\"}}}','不用RequetParser，用单独的方法批量处理'),(17,'POST','User[]','{\"User\":[\"id\":\"\"]}','解释成16这种'),(18,'DELETE','User','{\"disallowColumns\":\"!\", \"necessaryColumns\":\"id\"}',NULL);
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

-- Dump completed on 2017-01-24 16:19:11
