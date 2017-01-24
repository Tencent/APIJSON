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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sex` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(14) DEFAULT NULL,
  `certified` tinyint(1) NOT NULL DEFAULT '0',
  `head` varchar(300) DEFAULT NULL,
  `picture` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1485245724392 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (38710,0,'Lemon','1234560000',0,'','\"{\"id\":2}\"'),(93851,0,NULL,NULL,0,NULL,NULL),(93852,0,NULL,NULL,0,NULL,NULL),(93853,0,NULL,NULL,0,NULL,NULL),(93854,0,NULL,NULL,0,NULL,NULL),(93855,0,NULL,NULL,0,NULL,NULL),(93856,0,NULL,NULL,0,NULL,NULL),(93857,0,NULL,NULL,0,NULL,NULL),(93858,0,NULL,NULL,0,NULL,NULL),(93862,0,'Tommy','1234567890',0,'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000',NULL),(93863,0,'Tommy','1234567890',0,'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000',NULL),(93864,0,'Tommy','1234567890',0,'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000',NULL),(93865,0,'Tommy','1234567890',0,'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000',NULL),(93866,0,'Tommy','1234567890',0,'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000',NULL),(93867,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(93868,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485244029274,0,'Lemon','111111111',1,'',NULL),(1485244029275,1,NULL,NULL,0,NULL,NULL),(1485244239112,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485244740949,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245116758,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\",\"http://common.cnblogs.com/images/icon_weibo_24.png\",\"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000\"]'),(1485245228383,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245677197,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245703707,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245714355,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245716302,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245718554,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL),(1485245724391,0,'Tommy','1234567890',0,'http://common.cnblogs.com/images/icon_weibo_24.png',NULL);
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

-- Dump completed on 2017-01-24 16:19:12
