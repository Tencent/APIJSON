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
  `version` tinyint(4) DEFAULT '0',
  `tag` varchar(20) NOT NULL,
  `method` varchar(10) DEFAULT 'GET',
  `structure` json NOT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。\n\n每次启动服务器时加载整个表到内存。\n这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,0,'User','POST','{\"User\": {\"disallow\": \"id\", \"necessary\": \"name,phone\"}, \"necessary\": \"password,verify\"}','\"User:toUser\":{}转化为\"toUser\":User的JSONObject ？','2017-02-01 11:19:51'),(2,0,'Moment','POST','{\"disallow\": \"id\", \"necessary\": \"userId,pictureList\"}',NULL,'2017-02-01 11:19:51'),(3,0,'Comment','POST','{\"disallow\": \"id\", \"necessary\": \"userId,momentId,content\"}',NULL,'2017-02-01 11:19:51'),(4,0,'User','PUT','{\"disallow\": \"phone\", \"necessary\": \"id\"}',NULL,'2017-02-01 11:19:51'),(6,0,'Moment','DELETE','{\"necessary\": \"id\"}','所有删除都要有currentUserId和loginPassword？','2017-02-01 11:19:51'),(7,0,'Comment','DELETE','{\"put\": {\"Comment:child\": {\"toId@\": \"Comment/id\"}}, \"Comment\": {\"necessary\": \"id\"}}','disallow没必要用于DELETE','2017-02-01 11:19:51'),(8,0,'Password','PUT','{\"Password\": {\"disallow\": \"!\", \"necessary\": \"id,password\"}, \"necessary\": \"oldPassword\"}','对安全要求高，不允许客户端改type,date等字段','2017-02-01 11:19:51'),(9,0,'User.phone','PUT','{\"User\": {\"disallow\": \"!\", \"necessary\": \"id,phone\"}}','! 表示其它所有，这里指necessary所有未包含的字段','2017-02-01 11:19:51'),(10,0,'MomentWithComment','POST','{\"Moment\": {\"Comment\": {\"disallow\": \"id\", \"necessary\": \"userId,workId,content\"}, \"disallow\": \"id\", \"necessary\": \"userId,title,content,picture\"}}','用的极少，可能根本用不上','2017-02-01 11:19:51'),(11,0,'User','DELETE','{\"necessary\": \"id\"}','','2017-02-01 11:19:51'),(12,0,'Password','POST_GET','{\"disallow\": \"!\", \"necessary\": \"id,model\"}','String:table, Long: tableId 限制类型？','2017-02-18 12:03:17'),(13,0,'Wallet','POST_GET','{\"Wallet\": {\"disallow\": \"!\", \"necessary\": \"userId\"}, \"necessary\": \"currentUserId,loginPassword\"}',NULL,'2017-02-18 14:20:43'),(14,0,'Login','POST_GET','{\"User\": {\"necessary\": \"phone\"}, \"Password\": {\"disallow\": \"!\", \"necessary\": \"table,phone,value\"}}',NULL,'2017-02-18 14:20:43'),(15,0,'Login','POST','{\"disallow\": \"!\", \"necessary\": \"userId,type\"}',NULL,'2017-02-18 14:20:43'),(16,0,'Verify','POST','{\"disallow\": \"!\", \"necessary\": \"id,code\"}',NULL,'2017-02-18 14:20:43'),(17,0,'Verify','POST_GET','{\"necessary\": \"id\"}',NULL,'2017-02-18 14:20:43'),(18,0,'Verify','DELETE','{\"disallow\": \"!\", \"necessary\": \"id\"}',NULL,'2017-02-18 14:20:43'),(19,0,'Verify','POST_HEAD','{}',NULL,'2017-02-18 14:20:43'),(20,0,'Moment','PUT','{\"disallow\": \"userId,date\", \"necessary\": \"id\"}',NULL,'2017-02-01 11:19:51'),(30,0,'Wallet','POST','{\"Wallet\": {\"disallow\": \"!\", \"necessary\": \"userId\"}, \"necessary\": \"payPassword\"}',NULL,'2017-02-18 14:20:43'),(31,0,'Wallet','PUT','{\"Wallet\": {\"disallow\": \"!\", \"necessary\": \"userId,balance+\"}, \"necessary\": \"payPassword,oldPassword\"}',NULL,'2017-02-18 14:20:43'),(32,0,'Wallet','DELETE','{\"Wallet\": {\"disallow\": \"!\", \"necessary\": \"userId\"}, \"necessary\": \"payPassword\"}',NULL,'2017-02-18 14:20:43'),(33,0,'Login','POST_HEAD','{\"disallow\": \"!\", \"necessary\": \"userId,type\"}',NULL,'2017-02-18 14:20:43');
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

-- Dump completed on 2017-05-08 15:54:34
