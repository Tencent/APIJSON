-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 47.98.196.224    Database: sys
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
-- Table structure for table `Function`
--

DROP TABLE IF EXISTS `Function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Function` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `userId` bigint(15) NOT NULL COMMENT '管理员用户Id',
  `name` varchar(20) NOT NULL COMMENT '方法名',
  `arguments` varchar(100) DEFAULT NULL COMMENT '参数列表，每个参数的类型都是 String。\n用 , 分割的字符串 比 [JSONArray] 更好，例如 array,item ，更直观，还方便拼接函数。',
  `demo` json NOT NULL COMMENT '可用的示例。',
  `detail` varchar(1000) DEFAULT NULL COMMENT '详细描述',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='远程函数。强制在启动时校验所有demo是否能正常运行通过';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Function`
--

LOCK TABLES `Function` WRITE;
/*!40000 ALTER TABLE `Function` DISABLE KEYS */;
INSERT INTO `Function` VALUES (3,0,'countArray','array','{\"array\": [1, 2, 3]}','获取数组长度。没写调用键值对，会自动补全 \"result()\": \"countArray(array)\"','2018-10-13 08:23:23'),(4,0,'countObject','object','{\"object\": {\"key0\": 1, \"key1\": 2}}','获取对象长度。','2018-10-13 08:23:23'),(5,0,'isContain','array,value','{\"array\": [1, 2, 3], \"value\": 2}','判断是否数组包含值。','2018-10-13 08:23:23'),(6,0,'isContainKey','object,key','{\"key\": \"id\", \"object\": {\"id\": 1}}','判断是否对象包含键。','2018-10-13 08:30:31'),(7,0,'isContainValue','object,value','{\"value\": 1, \"object\": {\"id\": 1}}','判断是否对象包含值。','2018-10-13 08:30:31'),(8,0,'getFromArray','array,position','{\"array\": [1, 2, 3], \"result()\": \"getFromArray(array,1)\"}','根据下标获取数组里的值。position 传数字时直接作为值，而不是从所在对象 request 中取值','2018-10-13 08:30:31'),(9,0,'getFromObject','object,key','{\"key\": \"id\", \"object\": {\"id\": 1}}','根据键获取对象里的值。','2018-10-13 08:30:31');
/*!40000 ALTER TABLE `Function` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-14  0:56:54
