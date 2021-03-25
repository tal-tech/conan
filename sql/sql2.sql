-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 10.90.72.126    Database: conan_performance
-- ------------------------------------------------------
-- Server version	5.7.28

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  `upwd` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  `role` int(11) DEFAULT NULL COMMENT '0 学生\n1 主讲老师 2 辅导老师',
  `stu_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  `teacher_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  `cookies` varchar(9999) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录之后返回的cookie',
  `psid` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '磐石ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_map`
--

DROP TABLE IF EXISTS `account_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_map` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '主讲，自增',
  `stu_cou_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '学生课程关联ID',
  `stuIRC_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '学生与IRC连接的ID',
  `teaIRC_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教师与IRC连接的ID',
  `account_id` int(255) NOT NULL COMMENT '新的学生账号信息',
  `live_basic_infos_id` int(255) NOT NULL COMMENT '直播基础信息表ID',
  `team_id` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `live_basic_infos`
--

DROP TABLE IF EXISTS `live_basic_infos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_basic_infos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_id` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '场次ID',
  `course_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '课程ID',
  `course_ware_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '课件ID',
  `task_id` int(11) DEFAULT NULL COMMENT '染色任务ID',
  `package_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  `class_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-05 15:02:29
