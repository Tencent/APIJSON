-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: apijson.cn    Database: sys
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
-- Table structure for table `Random`
--

DROP TABLE IF EXISTS `Random`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Random` (
  `id` bigint(15) NOT NULL COMMENT '唯一标识',
  `userId` bigint(15) NOT NULL,
  `documentId` bigint(15) NOT NULL COMMENT '测试用例 Document 的 id',
  `count` int(3) NOT NULL DEFAULT '1' COMMENT '请求次数，默认 1',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `config` varchar(5000) NOT NULL COMMENT '配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='随机测试';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Random`
--

LOCK TABLES `Random` WRITE;
/*!40000 ALTER TABLE `Random` DISABLE KEYS */;
INSERT INTO `Random` VALUES (1575129210368,82001,1560244940013,2,'配置 1','User/id : RANDOM_IN(82001,82002,82003,70793,93793) //从这几个值随机取数\nUser/sex : RANDOM_INT(0,2) //从 0-2 随机取整数\n[]/count : RANDOM_IN(3,5,10,20,\'1\',\'s\',false,[],{}) //从这几个值随机取数\n[]/page : Math.round(10*Math.random())  //通过代码来自定义\n// []/Comment/toId : RANDOM_REAL  //从数据库随机取值，替换 User/id 键值对'),(1575129533653,82001,1560244940013,5,'count 和 page','[]/count : RANDOM_IN(3,5,10,20,\'1\',\'s\',false,[],{}) //从这几个值随机取数 []/page : Math.round(10*Math.random())  //通过代码来自定义 // []/Comment/toId : RANDOM_REAL  //从数据库随机取值，替换 User/id 键值对'),(1575134003381,82001,1560075285563,1,'[]/Comment/toId : RANDOM_IN(0,4,7,13,21)','[]/Comment/toId : RANDOM_IN(0,4,7,13,21)  //从数据库随机取值，替换 User/id 键值对'),(1575141548694,82001,1560244940013,1,'count & page','[]/count : RANDOM_IN(3,5,10,20)  //从这几个值随机取数\n[]/page : Math.round(10*Math.random())  //通过代码来自定义'),(1575141679778,82001,1560244940013,1,'随机配置 2019-12-01 03:21','[]/count : RANDOM_IN(3,5,10)  //从这几个值随机取数\n[]/page : Math.round(3*Math.random())  //通过代码来自定义');
/*!40000 ALTER TABLE `Random` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-01 23:16:14
