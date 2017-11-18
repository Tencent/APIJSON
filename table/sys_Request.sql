-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 39.108.143.172    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.19-log

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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `version` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。\n其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。\n\n自动化版本管理：\nRequest JSON最外层可以传  “version”:Integer 。\n1.未传或 <= 0，用最新版。 “@order”:”version-“\n2.已传且 > 0，用version以上的可用版本的最低版本。 “@order”:”version+”, “version{}”:”>={version}”',
  `method` varchar(10) DEFAULT 'GETS' COMMENT '只限于GET,HEAD外的操作方法。',
  `tag` varchar(20) NOT NULL COMMENT '标签',
  `structure` json NOT NULL COMMENT '结构',
  `description` varchar(10000) DEFAULT NULL COMMENT '描述',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。\n\n每次启动服务器时加载整个表到内存。\n这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,1,'POST','register','{\"User\": {\"ADD\": {\"pictureList\": [], \"contactIdList\": []}, \"PUT\": {\"id@\": \"Privacy/id\"}, \"DISALLOW\": \"id\", \"NECESSARY\": \"name\"}, \"Privacy\": {\"UNIQUE\": \"phone\", \"VERIFY\": {\"phone?\": \"phone\"}, \"DISALLOW\": \"id\", \"NECESSARY\": \"_password,phone\"}}','\"User:toUser\":{}转化为\"toUser\":User的JSONObject ？','2017-02-01 11:19:51'),(2,1,'POST','Moment','{\"ADD\": {\"pictureList\": [], \"praiseUserIdList\": []}, \"DISALLOW\": \"id\", \"NECESSARY\": \"userId,pictureList\"}',NULL,'2017-02-01 11:19:51'),(3,1,'POST','Comment','{\"DISALLOW\": \"id\", \"NECESSARY\": \"userId,momentId,content\"}',NULL,'2017-02-01 11:19:51'),(4,1,'PUT','User','{\"ADD\": {\"@role\": \"owner\"}, \"DISALLOW\": \"phone\", \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(5,1,'DELETE','Moment','{\"PUT\": {\"Comment\": {\"@role\": \"admin\", \"momentId@\": \"Moment/id\"}}, \"Moment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}}','所有删除都要有currentUserId和loginPassword？','2017-02-01 11:19:51'),(6,1,'DELETE','Comment','{\"PUT\": {\"Comment:child\": {\"@role\": \"admin\", \"toId@\": \"Comment/id\"}}, \"Comment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(8,1,'PUT','User-phone','{\"User\": {\"ADD\": {\"@role\": \"owner\"}, \"PUT\": {\"@condition\": \"_password\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id,phone,_password\"}}','! 表示其它所有，这里指necessary所有未包含的字段','2017-02-01 11:19:51'),(14,1,'POST','Verify','{\"DISALLOW\": \"!\", \"NECESSARY\": \"phone,verify\"}',NULL,'2017-02-18 14:20:43'),(15,1,'GETS','Verify','{\"NECESSARY\": \"phone\"}',NULL,'2017-02-18 14:20:43'),(16,1,'HEADS','Verify','{}','允许任意内容','2017-02-18 14:20:43'),(17,1,'PUT','Moment','{\"DISALLOW\": \"userId,date\", \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(21,1,'HEADS','Login','{\"DISALLOW\": \"!\", \"NECESSARY\": \"userId,type\"}',NULL,'2017-02-18 14:20:43'),(22,1,'GETS','User','{}','允许任意内容','2017-02-18 14:20:43'),(23,1,'PUT','Privacy','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(24,1,'GETS','Privacy','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}',NULL,'2017-06-12 16:05:51'),(25,1,'PUT','Praise','{\"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(26,1,'DELETE','Comment[]','{\"Comment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id{}\"}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(27,1,'PUT','Comment[]','{\"Comment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id{}\"}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(28,1,'PUT','Comment','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(29,1,'GETS','login','{\"Privacy\": {\"DISALLOW\": \"id\", \"NECESSARY\": \"phone,_password\"}}',NULL,'2017-10-15 10:04:52'),(30,1,'PUT','balance+','{\"Privacy\": {\"VERIFY\": {\"balance+&{}\": \">=1,<=100000\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id,balance+\"}}',NULL,'2017-10-21 08:48:34'),(31,1,'PUT','balance-','{\"Privacy\": {\"PUT\": {\"@condition\": \"_password\"}, \"VERIFY\": {\"balance-&{}\": \">=1,<=10000\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id,balance-,_password\"}}',NULL,'2017-10-21 08:48:34'),(32,2,'GETS','Privacy','{\"ADD\": {\"@role\": \"owner\"}, \"DISALLOW\": \"_password,_payPassword\", \"NECESSARY\": \"id\"}',NULL,'2017-06-12 16:05:51'),(33,2,'GETS','Privacy-CIRCLE','{\"Privacy\": {\"PUT\": {\"@role\": \"CIRCLE\", \"@column\": \"phone\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id\"}}',NULL,'2017-06-12 16:05:51');
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

-- Dump completed on 2017-11-12 21:45:13
