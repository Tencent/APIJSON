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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `version` tinyint(4) DEFAULT '0' COMMENT 'GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。\n其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。\n\n自动化版本管理：\nRequest JSON最外层可以传  “version”:Integer 。\n1.未传或 <= 0，用最新版。 “@order”:”version-“\n2.已传且 > 0，用version以上的可用版本的最低版本。 “@order”:”version+”, “version{}”:”>={version}”',
  `tag` varchar(20) NOT NULL COMMENT '标签',
  `method` varchar(10) DEFAULT 'GET' COMMENT '只限于GET,HEAD外的操作方法。',
  `structure` json NOT NULL COMMENT '结构',
  `description` varchar(10000) DEFAULT NULL COMMENT '描述',
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。\n\n每次启动服务器时加载整个表到内存。\n这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,0,'register','POST','{\"User\": {\"ADD\": {\"pictureList\": [], \"contactIdList\": []}, \"PUT\": {\"id@\": \"Privacy/id\"}, \"DISALLOW\": \"id\", \"NECESSARY\": \"name\"}, \"Privacy\": {\"UNIQUE\": \"phone\", \"VERIFY\": {\"phone?\": \"phone\"}, \"DISALLOW\": \"id\", \"NECESSARY\": \"_password,phone\"}}','\"User:toUser\":{}转化为\"toUser\":User的JSONObject ？','2017-02-01 11:19:51'),(2,0,'Moment','POST','{\"ADD\": {\"pictureList\": [], \"praiseUserIdList\": []}, \"DISALLOW\": \"id\", \"NECESSARY\": \"userId,pictureList\"}',NULL,'2017-02-01 11:19:51'),(3,0,'Comment','POST','{\"DISALLOW\": \"id\", \"NECESSARY\": \"userId,momentId,content\"}',NULL,'2017-02-01 11:19:51'),(4,0,'User','PUT','{\"ADD\": {\"@role\": \"owner\"}, \"DISALLOW\": \"phone\", \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(5,0,'Moment','DELETE','{\"PUT\": {\"Comment\": {\"@role\": \"admin\", \"momentId@\": \"Moment/id\"}}, \"Moment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}}','所有删除都要有currentUserId和loginPassword？','2017-02-01 11:19:51'),(6,0,'Comment','DELETE','{\"PUT\": {\"Comment:child\": {\"@role\": \"admin\", \"toId@\": \"Comment/id\"}}, \"Comment\": {\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(7,0,'Password','PUT','{\"Password\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"id,password\"}, \"NECESSARY\": \"oldPassword\"}','对安全要求高，不允许客户端改type,date等字段','2017-02-01 11:19:51'),(8,0,'User.phone','PUT','{\"User\": {\"ADD\": {\"@role\": \"owner\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id,phone\"}}','! 表示其它所有，这里指necessary所有未包含的字段','2017-02-01 11:19:51'),(9,0,'MomentWithComment','POST','{\"Moment\": {\"DISALLOW\": \"id\", \"NECESSARY\": \"userId,title,content,picture\"}, \"Comment\": {\"PUT\": {\"momentId@\": \"Moment/id\"}, \"DISALLOW\": \"id\", \"NECESSARY\": \"userId,workId,content\"}}','用的极少，可能根本用不上','2017-02-01 11:19:51'),(10,0,'Password','GETS','{\"DISALLOW\": \"!\", \"NECESSARY\": \"id,model\"}','String:table, Long: tableId 限制类型？','2017-02-18 12:03:17'),(11,0,'Wallet','GETS','{\"Wallet\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"id\"}}',NULL,'2017-02-18 14:20:43'),(12,0,'Login','GETS','{\"User\": {\"NECESSARY\": \"phone\"}, \"Password\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"table,phone,value\"}}',NULL,'2017-02-18 14:20:43'),(13,0,'Login','POST','{\"DISALLOW\": \"!\", \"NECESSARY\": \"userId,type\"}',NULL,'2017-02-18 14:20:43'),(14,0,'Verify','POST','{\"DISALLOW\": \"!\", \"NECESSARY\": \"phone,verify\"}',NULL,'2017-02-18 14:20:43'),(15,0,'Verify','GETS','{\"NECESSARY\": \"phone\"}',NULL,'2017-02-18 14:20:43'),(16,0,'Verify','HEADS','{}','允许任意内容','2017-02-18 14:20:43'),(17,0,'Moment','PUT','{\"DISALLOW\": \"userId,date\", \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(18,0,'Wallet','POST','{\"Wallet\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"id\"}, \"NECESSARY\": \"payPassword\"}',NULL,'2017-02-18 14:20:43'),(19,0,'Wallet','PUT','{\"Wallet\": {\"VERIFY\": {\"balance+&{}\": \">=-10000,<=10000\"}, \"DISALLOW\": \"!\", \"NECESSARY\": \"id,balance+\"}, \"Password\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"id,password,type\"}}',NULL,'2017-02-18 14:20:43'),(20,0,'Wallet','DELETE','{\"Wallet\": {\"DISALLOW\": \"!\", \"NECESSARY\": \"id\"}, \"NECESSARY\": \"payPassword\"}',NULL,'2017-02-18 14:20:43'),(21,0,'Login','HEADS','{\"DISALLOW\": \"!\", \"NECESSARY\": \"userId,type\"}',NULL,'2017-02-18 14:20:43'),(22,0,'User','GETS','{}','允许任意内容','2017-02-18 14:20:43'),(23,0,'Privacy','PUT','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(24,0,'Privacy','GETS','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}',NULL,'2017-06-12 16:05:51'),(25,0,'Praise','PUT','{\"NECESSARY\": \"id\"}',NULL,'2017-02-01 11:19:51'),(26,0,'Comment[]','DELETE','{\"Comment\": {\"ADD\": {\"@role\": \"owner\", \"NECESSARY\": \"id{}\"}}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(27,0,'Comment[]','PUT','{\"Comment\": {\"ADD\": {\"@role\": \"owner\", \"NECESSARY\": \"id{}\"}}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(28,0,'Comment','PUT','{\"ADD\": {\"@role\": \"owner\"}, \"NECESSARY\": \"id\"}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(29,0,'login','GETS','{\"Privacy\": {\"DISALLOW\": \"id\", \"NECESSARY\": \"phone,_password\"}}',NULL,'2017-10-15 10:04:52');
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

-- Dump completed on 2017-10-15 19:30:58
