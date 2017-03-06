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
-- Table structure for table `Comment`
--

DROP TABLE IF EXISTS `Comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment` (
  `id` bigint(15) NOT NULL,
  `parentId` bigint(15) DEFAULT NULL,
  `momentId` bigint(15) NOT NULL,
  `userId` bigint(15) NOT NULL,
  `toUserId` bigint(15) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `workId` bigint(15) NOT NULL COMMENT '兼容测试以前的Work（被Moment替代）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
INSERT INTO `Comment` VALUES (3,0,371,82002,59960,'This is a Content...-3','2017-02-01 11:20:50',371),(4,0,470,38710,82002,'This is a Content...-4','2017-02-01 11:20:50',470),(13,0,58,82005,93793,'This is a Content...-13','2017-02-01 11:20:50',58),(22,221,470,82001,70793,'This is a Content...-22','2017-02-01 11:20:50',470),(44,0,170,82003,93793,'This is a Content...-44','2017-02-01 11:20:50',170),(45,0,301,93793,99700,'This is a Content...-45','2017-02-01 11:20:50',301),(47,4,470,70793,70793,'This is a Content...-47','2017-02-01 11:20:50',470),(51,45,301,82003,70793,'This is a Content...-51','2017-02-01 11:20:50',301),(54,0,170,82004,93793,'This is a Content...-54','2017-02-01 11:20:50',170),(68,0,371,82005,70793,'This is a Content...-68','2017-02-01 11:20:50',371),(76,45,301,93793,70793,'This is a Content...-76','2017-02-01 11:20:50',301),(77,13,58,93793,35234,'This is a Content...-77','2017-02-01 11:20:50',58),(97,13,58,82006,14326,'This is a Content...-97','2017-02-01 11:20:50',58),(99,44,170,70793,7166,'This is a Content...-99','2017-02-01 11:20:50',170),(110,0,371,93793,0,'This is a Content...-110',NULL,371),(114,0,371,82001,NULL,'This is a Content...-114','2017-03-02 05:56:06',371),(115,0,371,38710,0,'This is a Content...-115','2017-03-02 05:56:06',371),(116,0,371,70793,0,'This is a Content...-116','2017-03-02 05:56:06',371),(120,0,301,93793,0,'This is a Content...-110','2017-03-02 05:56:06',301),(124,0,301,82001,0,'This is a Content...-114','2017-03-02 05:56:06',301),(157,NULL,371,93793,70793,'This is a Content...-157','2017-02-01 11:20:50',371),(158,0,301,93793,70793,'This is a Content...-157',NULL,301),(160,0,235,82001,38710,'This is a Content...-160','2017-03-02 05:56:06',235),(161,NULL,592,93793,70793,'This is a Content...-161','2017-02-01 11:20:50',592),(162,0,12,93793,82001,'This is a Content...-162','2017-03-06 05:03:45',12),(163,0,235,82001,38710,'This is a Content...-163','2017-03-02 05:56:06',235),(164,0,12,93793,82001,'This is a Content...-164','2017-03-06 05:03:45',12);
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-06 13:25:29
