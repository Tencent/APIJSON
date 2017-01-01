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
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `head` varchar(300) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `phone` varchar(14) DEFAULT NULL,
  `picture` varchar(3000) DEFAULT NULL,
  `sex` int(11) NOT NULL,
  `certified` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93835 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (38710,'http://www.tooopen.com/view/38710.html','Name-38710','1300038710',NULL,0,0),(70793,'http://www.tooopen.com/view/70793.html','Name-70793','1300070793','',0,0),(82001,'http://www.tooopen.com/view/82002.html','Name-82002','1300082002','',0,1),(82002,'http','Name-82002','1300082002','',1,1),(82003,'http://www.tooopen.com/view/82002.html','Name-82002','1300082002','',1,1),(82005,'http://www.tooopen.com/view/82002.html','Name-82002','1300082002','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\",\"http://common.cnblogs.com/images/icon_weibo_24.png\",\"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000\"]',1,0),(82006,'http://www.tooopen.com/view/82002.html','Name-82002','1300082002','',1,0),(82012,'http://www.tooopen.com/view/82002.html','Name-82002','1300082002','',0,0),(90814,'http://www.tooopen.com/view/90814.html','Name-90814','1300090814','',0,1),(93793,'http://www.tooopen.com/view/93793.html','Name-93793','1300093793','',0,1),(93794,NULL,'Lemon','999999999',NULL,0,0);
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

-- Dump completed on 2017-01-01 19:21:36
