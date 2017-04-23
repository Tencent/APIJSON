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
  `_index` int(11) NOT NULL AUTO_INCREMENT,
  `id` bigint(15) NOT NULL,
  `sex` tinyint(2) NOT NULL DEFAULT '0',
  `name` varchar(20) DEFAULT NULL,
  `certified` tinyint(1) NOT NULL DEFAULT '0',
  `tag` varchar(45) DEFAULT NULL,
  `phone` varchar(14) DEFAULT NULL,
  `head` varchar(300) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `friendIdList` json DEFAULT NULL COMMENT '联系人id列表',
  `pictureList` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `_index_UNIQUE` (`_index`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,38710,0,'TommyLemon',1,'Android&Java','13000038710','http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000','2017-02-01 11:21:50','[82003, 82005, 90814, 82004, 82009, 82002, 82044, 93793, 70793]','[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\", \"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(2,70793,0,'Strong',0,'djdj','13000070793','http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000','2017-02-01 11:21:50','[38710, 82002]','[\"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/788c0a7e11a\", \"https://camo.githubusercontent.com/f513f67\"]'),(3,82001,0,'Android',1,'Android Dev','13000082001','http://static.oschina.net/uploads/user/19/39085_50.jpg','2017-02-01 11:21:50','[82004, 82021, 82023, 82025, 82039, 82035, 82027, 82002, 93793, 70793]','[\"http://common.cnblogs.com/images/icon_weibo_24.png\"]'),(4,82002,1,'Happy',1,'iOS','13000082002','http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000','2017-02-01 11:21:50','[82005, 70793]',NULL),(5,82003,1,'Wechat',1,NULL,'13000082003','http://common.cnblogs.com/images/wechat.png','2017-02-01 11:21:50','[82001, 93793]',NULL),(6,82004,0,'Tommy',0,'fasef','13000082004','http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000','2017-02-01 11:21:50',NULL,NULL),(7,82005,1,'Jan',0,'AG','13000082005','http://my.oschina.net/img/portrait.gif?t=1451961935000','2017-02-01 11:21:50','[82001, 38710]',NULL),(8,82006,1,'Meria',0,NULL,'13000082006','http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000','2017-02-01 11:21:50',NULL,NULL),(9,82012,0,'Steve',0,'FEWE','13000082012','http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000','2017-02-01 11:21:50','[82004, 82002, 93793]',NULL),(10,82020,0,'ORANGE',0,NULL,'12345678900','http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000','2017-02-01 11:21:50',NULL,NULL),(11,82021,1,'Tommy',0,NULL,'12345678901','http://static.oschina.net/uploads/user/19/39085_50.jpg','2017-02-01 11:21:50',NULL,NULL),(12,82022,0,'Internet',0,NULL,'12345678902','http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000','2017-02-01 11:21:50',NULL,NULL),(13,82023,0,'No1',0,NULL,'12345678903','http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000','2017-02-01 11:21:50',NULL,NULL),(14,82024,0,'Lemon',0,NULL,'12345678904','http://static.oschina.net/uploads/user/427/855532_50.jpg?t=1435030876000','2017-02-01 11:21:50',NULL,NULL),(15,82025,1,'Tommy',0,NULL,'12345678905','http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000','2017-02-01 11:21:50',NULL,NULL),(16,82026,0,'iOS',0,NULL,'12345678906','http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000','2017-02-01 11:21:50',NULL,NULL),(17,82027,0,'Yong',0,NULL,'12345678907','http://my.oschina.net/img/portrait.gif?t=1451961935000','2017-02-01 11:21:50',NULL,NULL),(18,82028,1,'gaeg',0,NULL,'12345678908','http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000','2017-02-01 11:21:50',NULL,NULL),(19,82029,0,'GASG',0,NULL,'12345678909','http://common.cnblogs.com/images/wechat.png','2017-02-01 11:21:50',NULL,NULL),(20,82030,1,'Fun',0,NULL,'12345678910','http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000','2017-02-01 11:21:50',NULL,NULL),(21,82031,0,'Lemon',0,NULL,'12345678911','http://static.oschina.net/uploads/user/48/96331_50.jpg','2017-02-01 11:21:50',NULL,NULL),(22,82032,0,'Stack',0,'fasdg','12345678912','http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000','2017-02-01 11:21:50',NULL,NULL),(23,82033,1,'GAS',0,NULL,'12345678913','http://my.oschina.net/img/portrait.gif?t=1451961935000','2017-02-01 11:21:50',NULL,NULL),(24,82034,1,'Jump',0,NULL,'12345678914','http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000','2017-02-01 11:21:50',NULL,NULL),(25,82035,1,'Tab',0,NULL,'12345678915','http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000','2017-02-01 11:21:50',NULL,NULL),(26,82036,0,'SAG',0,NULL,'12345678916','http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000','2017-02-01 11:21:50',NULL,NULL),(27,82037,0,'Test',0,NULL,'12345678917','http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000','2017-02-01 11:21:50',NULL,NULL),(28,82038,0,'Battle',0,NULL,'12345678918','http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000','2017-02-01 11:21:50',NULL,NULL),(29,82039,1,'Everyday',0,NULL,'12345678919','http://common.cnblogs.com/images/icon_weibo_24.png','2017-02-19 13:57:56',NULL,NULL),(30,82040,1,'Dream',0,NULL,'13000082019','/storage/emulated/0/output_image.jpg','2017-03-02 16:44:26','[70793]',NULL),(31,82041,0,'Holo',0,NULL,'13000082015','/storage/emulated/0/output_image.jpg','2017-03-04 09:59:34','[38710, 82001]',NULL),(32,82042,1,'Why',0,NULL,'13000082016','http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000','2017-03-04 10:04:33',NULL,NULL),(33,82043,0,'Holiday',0,NULL,'13000082017','http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000','2017-03-04 10:05:04','[70793, 82006]',NULL),(34,82044,1,'Love',0,NULL,'13000082018','http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000','2017-03-04 10:20:27','[82006]',NULL),(35,82045,0,'Green',0,NULL,'13000082020','http://common.cnblogs.com/images/wechat.png','2017-03-04 10:22:39','[82001, 82002, 82003, 1485246481130]',NULL),(36,82046,0,'Team',0,NULL,'13000082010','/storage/emulated/0/output_image.jpg','2017-03-04 15:11:17','[38710, 82002, 1485246481130]',NULL),(37,82047,0,'Tesla',0,NULL,'13000082021','http://common.cnblogs.com/images/wechat.png','2017-03-04 16:02:05',NULL,NULL),(38,82048,0,'Moto',0,NULL,'13000038711','http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000','2017-03-04 16:04:02',NULL,NULL),(39,82049,0,'ITMan',0,NULL,'13000038712','http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000','2017-03-05 09:51:51',NULL,NULL),(40,82050,0,'Parl',0,NULL,'13000038713','http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000','2017-03-05 09:52:52',NULL,NULL),(41,82051,0,'Girl',0,NULL,'13000038714','http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000','2017-03-05 09:53:37',NULL,NULL),(42,82052,0,'Unbrella',0,NULL,'13000038715','http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000','2017-03-05 09:57:54',NULL,NULL),(43,82053,0,'Alice',0,NULL,'13000038720','http://common.cnblogs.com/images/wechat.png','2017-03-05 15:25:42',NULL,NULL),(44,82054,0,'Harvey',0,NULL,'13000038721','http://static.oschina.net/uploads/user/19/39085_50.jpg','2017-03-06 12:29:03',NULL,NULL),(45,82055,1,'Solid',0,NULL,'13000082030','http://static.oschina.net/uploads/user/19/39085_50.jpg','2017-03-11 15:04:00','[38710, 82002]',NULL),(46,82056,1,'IronMan',0,NULL,'13000082040','http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000','2017-03-11 15:32:25',NULL,NULL),(47,82057,0,'NullPointerExeption',0,NULL,'13000038730','http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000','2017-03-12 06:01:23',NULL,NULL),(48,82058,0,'StupidBird',0,NULL,'13000038750','/storage/emulated/0/zblibrary.demo/image/output_image1489317829932.jpg','2017-03-12 11:23:04','[82001, 82002]',NULL),(49,82059,1,'He&She',0,NULL,'13000082033','http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000','2017-03-19 14:49:15',NULL,NULL),(50,82060,1,'Anyway~',0,NULL,'13000082050','http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000','2017-03-21 14:10:18',NULL,NULL),(51,90814,0,'007',1,NULL,'13000090814','http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000','2017-02-01 11:21:50',NULL,NULL),(52,93793,0,'Mike',1,'GES','13000093793','http://static.oschina.net/uploads/user/48/96331_50.jpg','2017-02-01 11:21:50',NULL,NULL),(53,93794,0,'Lemon',0,NULL,'99999999999','http://static.oschina.net/uploads/user/48/97721_50.jpg?t=1451544779000','2017-02-01 11:21:50',NULL,NULL),(54,1490109742863,1,'APIJSONUser',0,NULL,'13000082100',NULL,'2017-03-21 15:22:22',NULL,NULL),(55,1490109845208,0,'APIJSONUser',0,NULL,'13000082101',NULL,'2017-03-21 15:24:05',NULL,NULL),(56,1490420651686,1,'APIJSONUser',0,NULL,'13000038716',NULL,'2017-03-25 05:44:11','[70793]',NULL),(57,1490427139175,0,'APIJSONUser',0,NULL,'13000038717',NULL,'2017-03-25 07:32:19','[38710, 70793]',NULL),(58,1490427577823,0,'APIJSONUser',0,NULL,'13000082102',NULL,'2017-03-25 07:39:37',NULL,NULL),(59,1490584952968,0,'APIJSONUser',0,NULL,'13000038790',NULL,'2017-03-27 03:22:32',NULL,NULL),(60,1490973670928,1,'APIJSONUser',0,NULL,'13000082051','/storage/emulated/0/zblibrary.demo/image/output_image1490974049707.jpg','2017-03-31 15:21:10','[70793, 93793]',NULL);
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

-- Dump completed on 2017-04-23 16:15:45
