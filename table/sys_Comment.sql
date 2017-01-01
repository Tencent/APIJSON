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
  `id` int(5) NOT NULL,
  `parentId` int(5) NOT NULL,
  `workId` int(5) NOT NULL,
  `userId` int(5) NOT NULL,
  `targetUserId` int(5) NOT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `targetUserName` varchar(20) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `title` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
INSERT INTO `Comment` VALUES (3,0,371,82002,59960,'This is a Content...-3','targetUserName-59960','userName-85560',NULL),(4,0,470,310,14604,'This is a Content...-4','targetUserName-14604','userName-93781',NULL),(13,0,58,64,6914,'This is a Content...-13','targetUserName-6914','userName-70118',NULL),(22,221,470,332,5904,'This is a Content...-22','targetUserName-5904','userName-11679',NULL),(44,0,170,7073,6378,'This is a Content...-44','targetUserName-6378','userName-88645',NULL),(45,0,301,93793,99700,'This is a Content...-45','targetUserName-99700','userName-30075',NULL),(47,4,470,10,5477,'This is a Content...-47','targetUserName-5477','userName-80271',NULL),(51,45,301,903,8711,'This is a Content...-51','targetUserName-8711','userName-97675',NULL),(54,0,170,3,62122,'This is a Content...-54','targetUserName-62122','userName-82381',NULL),(68,0,371,2314,959,'This is a Content...-68','targetUserName-959','userName-92565',NULL),(76,45,301,93793,42688,'This is a Content...-76','targetUserName-42688','userName-20740',NULL),(77,13,58,90814,35234,'This is a Content...-77','targetUserName-35234','userName-94888',NULL),(97,13,58,90814,14326,'This is a Content...-97','targetUserName-14326','userName-6289',NULL),(99,44,170,793,7166,'This is a Content...-99','targetUserName-7166','userName-22949',NULL),(157,0,371,34,7162,'This is a Content...-13','targetUserName-7162','userName-5526',NULL);
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

-- Dump completed on 2017-01-01 19:21:37
