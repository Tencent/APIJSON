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
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL,
  `sex` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(14) DEFAULT NULL,
  `certified` tinyint(1) NOT NULL DEFAULT '0',
  `head` varchar(300) DEFAULT NULL,
  `picture` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (10000,1,'DeleteUserName','11111111111',0,'',NULL),(38710,0,'Name-38710','1300038710',0,'http://www.tooopen.com/view/38710.html',NULL),(70793,0,'Name-70793','1300070793',0,'http://www.tooopen.com/view/70793.html',''),(82001,0,'Name-82002','1300082002',1,'http://www.tooopen.com/view/82002.html',''),(82002,1,'Name-82002','1300082002',1,'http',''),(82003,1,'Name-82002','1300082002',1,'http://www.tooopen.com/view/82002.html',''),(82005,1,'Name-82002','1300082002',0,'http://www.tooopen.com/view/82002.html','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\",\"http://common.cnblogs.com/images/icon_weibo_24.png\",\"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000\"]'),(82006,1,'Name-82002','1300082002',0,'http://www.tooopen.com/view/82002.html',''),(82012,0,'Name-82002','1300082002',0,'http://www.tooopen.com/view/82002.html',''),(90814,0,'Name-90814','1300090814',1,'http://www.tooopen.com/view/90814.html',''),(93793,0,'Name-93793','1300093793',1,'http://www.tooopen.com/view/93793.html',''),(93794,0,'Lemon','999999999',0,NULL,NULL),(1485246481130,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-24 16:28:33
