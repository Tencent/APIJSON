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
-- Table structure for table `Moment`
--

DROP TABLE IF EXISTS `Moment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Moment` (
  `id` bigint(15) NOT NULL COMMENT '唯一标识',
  `userId` int(5) NOT NULL COMMENT '用户id',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `content` varchar(300) DEFAULT NULL COMMENT '内容',
  `praiseUserIdList` json NOT NULL COMMENT '点赞的用户id列表',
  `pictureList` json NOT NULL COMMENT '图片列表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Moment`
--

LOCK TABLES `Moment` WRITE;
/*!40000 ALTER TABLE `Moment` DISABLE KEYS */;
INSERT INTO `Moment` VALUES (12,70793,'2017-02-08 08:06:11','1111534034','[82003, 70793, 93793, 82006, 82044, 82040, 82055, 90814, 82001, 38710, 82002]','[\"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\"]'),(15,70793,'2017-02-08 08:06:11','APIJSON is a JSON Transmission Structure Protocol…','[82055, 82002, 82001]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(32,82002,'2017-02-08 08:06:11',NULL,'[38710, 82002, 82001]','[\"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\"]'),(58,90814,'2017-02-01 11:14:31','This is a Content...-435','[38710, 82003, 82005, 93793, 82006, 82044, 82001]','[\"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\"]'),(170,70793,'2017-02-01 11:14:31','This is a Content...-73','[82044, 82002, 82001]','[\"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\"]'),(235,38710,'2017-02-08 08:06:11',NULL,'[]','[\"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\", \"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000\"]'),(301,93793,'2017-02-01 11:14:31','This is a Content...-301','[38710, 93793, 82003, 82005, 82040, 82055, 82002, 82001]','[\"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]'),(371,82002,'2017-02-01 11:14:31','This is a Content...-371','[90814, 93793, 82003, 82005, 82006, 82040, 82002]','[\"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]'),(470,38710,'2017-02-01 11:14:31','This is a Content...-470','[]','[\"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\"]'),(511,38710,'2017-02-08 08:06:11',NULL,'[70793, 93793, 82001]','[\"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\"]'),(543,93793,'2017-02-08 08:06:11',NULL,'[]','[\"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]'),(551,70793,'2017-02-08 08:06:11','test','[82001]','[\"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\"]'),(594,82001,'2017-03-08 09:11:11','JSON!','[38710, 82002]','[\"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\"]'),(595,38710,'2017-03-05 05:29:19',NULL,'[70793, 82002, 82001]','[\"http://common.cnblogs.com/images/icon_weibo_24.png\", \"http://static.oschina.net/uploads/user/19/39085_50.jpg\"]'),(704,38710,'2017-03-12 09:39:44','APIJSON is a JSON Transmission Structure Protocol…','[82003, 82002, 82001]','[\"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000\", \"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000\"]'),(1491200468898,38710,'2017-04-03 06:21:08','APIJSON, let interfaces go to hell!','[]','[\"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000\", \"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000\"]'),(1493835799335,38710,'2017-05-03 18:23:19','APIJSON is a JSON Transmission Structure Protocol…','[82002, 82001]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(1508053625493,82001,'2017-10-15 07:47:05','秋高气爽','[82002, 82003]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(1508053649532,82001,'2017-10-15 07:47:29','测试发动态','[82001]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(1508053762227,82003,'2017-10-15 07:49:22','我也试试','[]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]');
/*!40000 ALTER TABLE `Moment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-15 19:30:59
