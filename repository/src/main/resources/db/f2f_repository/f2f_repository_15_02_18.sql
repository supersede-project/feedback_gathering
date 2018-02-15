-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: mt.ronnieschaniel.com    Database: f2f_repository
-- ------------------------------------------------------
-- Server version	5.5.56-MariaDB

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
-- Table structure for table `api_user`
--

DROP TABLE IF EXISTS `api_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_scb81k0sobpewfaxhwyquccop` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user`
--

LOCK TABLES `api_user` WRITE;
/*!40000 ALTER TABLE `api_user` DISABLE KEYS */;
INSERT INTO `api_user` (`id`, `name`, `password`) VALUES (1,'admin','$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy'),(2,'app_admin','$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy'),(3,'super_admin','$2a$10$y9K.1fd6VgT26rftcoziV.Qm74r8Qe1Y0hv.Kw4L1e3IMsxEXdWJu');
/*!40000 ALTER TABLE `api_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_api_user_role`
--

DROP TABLE IF EXISTS `api_user_api_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_api_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_user_role` int(11) DEFAULT NULL,
  `api_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmh0ci62ckvoi95vgx5nj4n0a3` (`api_user_id`),
  CONSTRAINT `FKmh0ci62ckvoi95vgx5nj4n0a3` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_api_user_role`
--

LOCK TABLES `api_user_api_user_role` WRITE;
/*!40000 ALTER TABLE `api_user_api_user_role` DISABLE KEYS */;
INSERT INTO `api_user_api_user_role` (`id`, `api_user_role`, `api_user_id`) VALUES (1,1,1),(2,0,2),(3,1,3);
/*!40000 ALTER TABLE `api_user_api_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_permission`
--

DROP TABLE IF EXISTS `api_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  `api_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3eosps1v8ku1jdk3m3k8vohxy` (`api_user_id`),
  CONSTRAINT `FK3eosps1v8ku1jdk3m3k8vohxy` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_permission`
--

LOCK TABLES `api_user_permission` WRITE;
/*!40000 ALTER TABLE `api_user_permission` DISABLE KEYS */;
INSERT INTO `api_user_permission` (`id`, `application_id`, `has_permission`, `api_user_id`) VALUES (1,20,'',1),(2,20,'',2),(3,20,'',3);
/*!40000 ALTER TABLE `api_user_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment_feedback`
--

DROP TABLE IF EXISTS `attachment_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoq4taf5qmyusdvl1m5kj4rgco` (`feedback_id`),
  CONSTRAINT `FKoq4taf5qmyusdvl1m5kj4rgco` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment_feedback`
--

LOCK TABLES `attachment_feedback` WRITE;
/*!40000 ALTER TABLE `attachment_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audio_feedback`
--

DROP TABLE IF EXISTS `audio_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audio_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `duration` int(11) NOT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9bfrsrtg3qdk4ima6jlbb6e4m` (`feedback_id`),
  CONSTRAINT `FK9bfrsrtg3qdk4ima6jlbb6e4m` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audio_feedback`
--

LOCK TABLES `audio_feedback` WRITE;
/*!40000 ALTER TABLE `audio_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `audio_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_feedback`
--

DROP TABLE IF EXISTS `category_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `parameter_id` bigint(20) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK83xs7iqr1bq6a5isel0ceded8` (`feedback_id`),
  CONSTRAINT `FK83xs7iqr1bq6a5isel0ceded8` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_feedback`
--

LOCK TABLES `category_feedback` WRITE;
/*!40000 ALTER TABLE `category_feedback` DISABLE KEYS */;
INSERT INTO `category_feedback` (`id`, `mechanism_id`, `parameter_id`, `text`, `feedback_id`) VALUES (1,0,663,'',149),(2,0,661,'',150),(3,0,661,'',151),(4,0,662,'',152),(5,0,662,'',153),(6,0,663,'',153),(7,0,NULL,'Support',154),(8,0,661,'',155),(9,0,661,'',156),(10,0,663,'',157),(11,0,663,'',158),(12,0,663,'',159),(13,0,661,'',160),(14,0,663,'',161),(15,0,661,'',162),(16,0,662,'',163),(17,0,663,'',164),(18,0,661,'',165),(19,0,662,'',166),(20,0,663,'',167),(21,0,663,'',168),(22,0,663,'',169),(23,0,663,'',170),(24,0,663,'',171),(25,0,663,'',172),(26,0,663,'',173),(27,0,663,'',174),(28,0,663,'',175),(29,0,516,'',176),(30,0,514,'',177),(31,0,515,'',178),(32,0,516,'',179),(33,0,514,'',180),(34,0,516,'',181),(35,0,515,'',182);
/*!40000 ALTER TABLE `category_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_feedback`
--

DROP TABLE IF EXISTS `comment_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_status` bit(1) DEFAULT NULL,
  `bool_is_developer` bit(1) DEFAULT NULL,
  `comment_text` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `fk_parent_comment` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `anonymous` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsjg1n0mnkm7haiyt6k8pqbd4x` (`feedback_id`),
  KEY `FKh1fyv8nt5no6b2yw1q3a7j1kt` (`fk_parent_comment`),
  KEY `FK7rsl9hwpd3dh6ucn5pxuo6j4d` (`user_id`),
  CONSTRAINT `FK7rsl9hwpd3dh6ucn5pxuo6j4d` FOREIGN KEY (`user_id`) REFERENCES `end_user` (`id`),
  CONSTRAINT `FKh1fyv8nt5no6b2yw1q3a7j1kt` FOREIGN KEY (`fk_parent_comment`) REFERENCES `comment_feedback` (`id`),
  CONSTRAINT `FKsjg1n0mnkm7haiyt6k8pqbd4x` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_feedback`
--

LOCK TABLES `comment_feedback` WRITE;
/*!40000 ALTER TABLE `comment_feedback` DISABLE KEYS */;
INSERT INTO `comment_feedback` (`id`, `active_status`, `bool_is_developer`, `comment_text`, `created_at`, `updated_at`, `feedback_id`, `fk_parent_comment`, `user_id`, `anonymous`) VALUES (1,'\0','\0','test comment for Feedback 157','2018-01-22 10:00:13',NULL,157,NULL,1,NULL),(2,'\0','','test anonymous comment','2018-02-02 18:07:36',NULL,1,NULL,1,''),(3,'','\0','test parent comment','2018-02-04 21:55:14',NULL,157,2,1,'\0'),(4,'','\0','test null comment','2018-02-04 22:23:01',NULL,157,NULL,1,'\0'),(5,'','\0','testcomment','2018-02-04 22:24:43',NULL,178,NULL,99999999,'\0'),(6,'','\0','dfs','2018-02-04 22:40:47',NULL,178,5,99999999,'\0'),(7,'','\0','second comment','2018-02-04 22:45:15',NULL,178,NULL,99999999,'\0'),(8,'','\0','testcomment','2018-02-06 16:35:20',NULL,181,NULL,99999999,'\0'),(9,'','\0','secondtest','2018-02-06 16:35:27',NULL,181,8,99999999,'\0'),(10,'','\0','my subcomment','2018-02-06 16:58:59',NULL,181,8,99999999,'\0'),(11,'','\0','my level 1 comment','2018-02-06 16:59:21',NULL,181,NULL,99999999,'\0'),(12,'','\0','My initial comment','2018-02-06 17:37:29',NULL,156,NULL,99999999,'\0'),(13,'','\0','Nice one','2018-02-06 17:37:39',NULL,156,12,99999999,'\0'),(14,'','\0','Still parent','2018-02-06 17:37:49',NULL,156,NULL,99999999,'\0'),(15,'','\0','second child','2018-02-06 17:38:16',NULL,156,14,99999999,'\0'),(16,'','\0','hhh','2018-02-06 17:56:01',NULL,154,NULL,99999999,'\0'),(17,'','\0','test comment for feedback \"test send feedback on ...\"','2018-02-13 14:49:16',NULL,182,NULL,99999999,'\0'),(18,'','\0','test comment for feedback \"test send feedback on ...\"','2018-02-13 14:49:16',NULL,182,NULL,99999999,'\0'),(19,'','\0','reply as sub-comment','2018-02-13 14:50:12',NULL,182,18,99999999,'\0'),(20,'','\0','third comment','2018-02-13 14:50:25',NULL,182,NULL,99999999,'\0'),(21,'','\0','nice','2018-02-13 15:05:00',NULL,150,NULL,99999999,'\0'),(22,'','\0','test comment on feedback \"test second time\"','2018-02-13 15:09:27',NULL,180,NULL,99999999,'\0'),(23,'','\0','another comment for \"test second time\"','2018-02-13 15:10:46',NULL,180,NULL,99999999,'\0'),(24,'','\0','','2018-02-14 09:41:34',NULL,182,NULL,99999999,'\0');
/*!40000 ALTER TABLE `comment_feedback` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_comment */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_comment AFTER INSERT ON comment_feedback
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_comment2 */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_comment2 AFTER DELETE ON comment_feedback
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `comment_viewed`
--

DROP TABLE IF EXISTS `comment_viewed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_viewed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `comment_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt7aa1dhhfvrlyukmtktgdxraf` (`comment_id`),
  KEY `FKamfb2bvnx3tj8stougb7hsuyq` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_viewed`
--

LOCK TABLES `comment_viewed` WRITE;
/*!40000 ALTER TABLE `comment_viewed` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_viewed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `context_information`
--

DROP TABLE IF EXISTS `context_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context_information` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `android_version` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `device_pixel_ratio` float DEFAULT NULL,
  `local_time` datetime DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`),
  CONSTRAINT `FKgjes53m9wrcpnnq0kl898yjul` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `context_information`
--

LOCK TABLES `context_information` WRITE;
/*!40000 ALTER TABLE `context_information` DISABLE KEYS */;
INSERT INTO `context_information` (`id`, `android_version`, `country`, `device_pixel_ratio`, `local_time`, `meta_data`, `region`, `resolution`, `time_zone`, `url`, `user_agent`, `feedback_id`) VALUES (1,NULL,NULL,1,'2018-01-20 13:34:51','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:9090/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',149),(2,NULL,NULL,2,'2018-01-21 17:57:59','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'878x1440','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',150),(3,NULL,NULL,2,'2018-01-21 18:04:38','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',151),(4,NULL,NULL,2,'2018-01-21 18:11:55','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',152),(5,NULL,NULL,2,'2018-01-21 18:17:05','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',153),(6,NULL,NULL,2,'2018-01-21 18:22:47','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',154),(7,NULL,NULL,2,'2018-01-21 18:23:49','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',155),(8,NULL,NULL,2,'2018-01-21 18:26:11','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',156),(9,NULL,NULL,2,'2018-01-21 18:27:54','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',157),(10,NULL,NULL,2,'2018-01-23 09:30:17','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1080x1920','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',158),(11,NULL,NULL,1,'2018-01-23 09:37:48','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',159),(12,NULL,NULL,1,'2018-01-25 17:29:36','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',160),(13,NULL,NULL,0.333333,'2018-01-25 17:36:54','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',161),(14,NULL,NULL,2,'2018-01-25 17:42:29','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',162),(15,NULL,NULL,2,'2018-01-25 17:51:56','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',163),(16,NULL,NULL,2,'2018-01-25 17:53:50','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',164),(17,NULL,NULL,2,'2018-01-25 17:54:42','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',165),(18,NULL,NULL,2,'2018-01-25 17:55:36','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',166),(19,NULL,NULL,2,'2018-01-25 17:56:41','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',167),(20,NULL,NULL,0.333333,'2018-01-25 21:10:05','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',168),(21,NULL,NULL,0.333333,'2018-01-25 21:17:50','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',169),(22,NULL,NULL,1,'2018-01-26 11:38:00','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',170),(23,NULL,NULL,1,'2018-01-26 16:59:10','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',171),(24,NULL,NULL,1,'2018-01-26 17:02:10','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',172),(25,NULL,NULL,1,'2018-01-26 17:09:32','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',173),(26,NULL,NULL,1,'2018-01-26 17:14:02','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',174),(27,NULL,NULL,1,'2018-01-26 17:21:38','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:3000/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',175),(28,NULL,NULL,1,'2018-01-26 17:29:29','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:9090/index.php?user_id=99999999&application_id=20','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',176),(29,NULL,NULL,2,'2018-01-31 09:30:55','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/?admin_user=0','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',177),(30,NULL,NULL,2,'2018-01-31 09:32:52','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/?admin_user=0','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',178),(31,NULL,NULL,1,'2018-02-05 23:10:50','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',179),(32,NULL,NULL,1,'2018-02-05 23:12:04','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',180),(33,NULL,NULL,0.5,'2018-02-06 17:00:51','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1028x1680','+0100','http://localhost/index.php?admin_user=false&user_id=99998888','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',181),(34,NULL,NULL,1,'2018-02-10 15:26:47','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://f2f.ronnieschaniel.com/?admin_user=false','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',182);
/*!40000 ALTER TABLE `context_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_unsubscribed`
--

DROP TABLE IF EXISTS `email_unsubscribed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_unsubscribed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `email_unsubscribed_end_user_id_fk` (`user_id`),
  CONSTRAINT `email_unsubscribed_end_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `end_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_unsubscribed`
--

LOCK TABLES `email_unsubscribed` WRITE;
/*!40000 ALTER TABLE `email_unsubscribed` DISABLE KEYS */;
INSERT INTO `email_unsubscribed` (`id`, `created_at`, `user_id`, `email`) VALUES (1,'2018-01-31 15:23:41',1,'f2f_central@hotmail.com');
/*!40000 ALTER TABLE `email_unsubscribed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `end_user`
--

DROP TABLE IF EXISTS `end_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `end_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `phone_number` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100000001 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `end_user`
--

LOCK TABLES `end_user` WRITE;
/*!40000 ALTER TABLE `end_user` DISABLE KEYS */;
INSERT INTO `end_user` (`id`, `application_id`, `created_at`, `phone_number`, `updated_at`, `username`, `email`) VALUES (1,20,NULL,123,NULL,'end_user_1',NULL),(2,20,NULL,123,NULL,'end_user_2',NULL),(3,20,NULL,123,NULL,'end_user_3',NULL),(4,20,NULL,123,NULL,'end_user_4',NULL),(5,20,NULL,123,NULL,'end_user_5',NULL),(6,20,NULL,123,NULL,'end_user_6',''),(99999999,20,NULL,123,NULL,'test_user',NULL);
/*!40000 ALTER TABLE `end_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `configuration_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_identification` bigint(20) DEFAULT NULL,
  `comment_count` int(11) NOT NULL,
  `dislike_count` int(11) NOT NULL,
  `icon_path` varchar(255) DEFAULT NULL,
  `blocked` tinyint(1) DEFAULT '0',
  `like_count` int(11) NOT NULL,
  `unread_comment_count` int(11) NOT NULL,
  `published` tinyint(1) DEFAULT '0',
  `visibility` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` (`id`, `application_id`, `configuration_id`, `created_at`, `language`, `title`, `updated_at`, `user_identification`, `comment_count`, `dislike_count`, `icon_path`, `blocked`, `like_count`, `unread_comment_count`, `published`, `visibility`) VALUES (1,20,46,'2017-11-27 10:55:55','en','Feedback',NULL,99999999,1,0,NULL,0,0,0,0,0),(2,20,46,'2017-11-27 10:59:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(3,20,46,'2017-11-27 11:01:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(4,20,46,'2017-11-28 14:25:14','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(5,20,46,'2017-11-28 14:39:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(6,20,46,'2017-11-29 09:55:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(7,20,46,'2017-11-29 09:57:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(8,20,46,'2017-11-29 10:19:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(9,20,46,'2017-11-29 10:19:34','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(10,20,46,'2017-11-29 10:32:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(11,20,46,'2017-11-29 10:38:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(12,20,46,'2017-11-29 10:41:16','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(13,20,46,'2017-11-29 10:44:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(14,20,46,'2017-11-29 10:51:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(15,20,46,'2017-11-29 10:51:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(16,20,46,'2017-11-29 10:58:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(17,20,46,'2017-11-29 11:11:02','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(18,20,46,'2017-11-29 11:16:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(19,20,46,'2017-11-29 11:23:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(20,20,46,'2017-11-29 11:26:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(21,20,46,'2017-11-29 11:35:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(22,20,46,'2017-11-29 11:42:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(23,20,46,'2017-11-29 11:45:39','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(24,20,46,'2017-11-29 11:46:15','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(25,20,46,'2017-11-29 11:48:01','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(26,20,46,'2017-11-29 11:48:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(27,20,46,'2017-11-29 11:54:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(28,20,46,'2017-11-29 13:04:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(29,20,46,'2017-11-29 13:06:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(30,20,46,'2017-11-29 18:22:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(31,20,46,'2017-11-29 18:34:44','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(32,20,46,'2017-11-30 18:06:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(33,20,46,'2017-11-30 19:53:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(34,20,46,'2017-11-30 20:11:58','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(35,20,46,'2017-11-30 20:17:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(36,20,46,'2017-11-30 20:22:11','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(37,20,46,'2017-11-30 20:25:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(38,20,46,'2017-11-30 20:33:59','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(39,20,46,'2017-11-30 20:42:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(40,20,46,'2017-11-30 20:44:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(41,20,46,'2017-11-30 20:45:30','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(42,20,46,'2017-11-30 20:47:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(43,20,46,'2017-11-30 20:52:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(44,20,46,'2017-11-30 21:38:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(45,20,46,'2017-11-30 21:40:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(46,20,46,'2017-11-30 21:42:25','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(47,20,46,'2017-11-30 21:51:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(48,20,46,'2017-11-30 21:53:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(49,20,46,'2017-11-30 22:01:45','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(50,20,46,'2017-11-30 22:11:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(51,20,46,'2017-11-30 22:18:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(52,20,46,'2017-11-30 22:21:23','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(53,20,46,'2017-11-30 22:28:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(54,20,46,'2017-12-01 10:40:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(55,20,46,'2017-12-01 10:42:28','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(56,20,46,'2017-12-01 18:27:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(57,20,46,'2017-12-01 18:37:16','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(58,20,46,'2017-12-01 18:40:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(59,20,46,'2017-12-01 19:14:32','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(60,20,46,'2017-12-01 19:20:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(61,20,46,'2017-12-01 19:23:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(62,20,46,'2017-12-01 19:26:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(63,20,46,'2017-12-02 00:29:51','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(64,20,46,'2017-12-02 00:35:57','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(65,20,46,'2017-12-02 00:42:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(66,20,46,'2017-12-02 00:56:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(67,20,46,'2017-12-03 20:20:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(68,20,46,'2017-12-03 20:47:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(69,20,46,'2017-12-03 20:50:32','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(70,20,46,'2017-12-03 21:01:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(71,20,46,'2017-12-03 21:03:57','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(72,20,46,'2017-12-03 21:12:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(73,20,46,'2017-12-03 21:15:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(74,20,46,'2017-12-03 21:20:27','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(75,20,46,'2017-12-03 21:31:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(76,20,46,'2017-12-03 21:40:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(77,20,46,'2017-12-03 21:46:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(78,20,46,'2017-12-03 22:33:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(79,20,46,'2017-12-03 22:36:51','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(80,20,46,'2017-12-05 20:20:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(81,20,46,'2017-12-05 20:52:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(82,20,46,'2017-12-05 20:57:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(83,20,46,'2017-12-05 20:58:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(84,20,46,'2017-12-06 09:29:28','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(85,20,46,'2017-12-06 12:50:54','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(86,20,46,'2017-12-06 13:03:53','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(87,20,46,'2017-12-06 13:05:38','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(88,20,46,'2017-12-06 13:15:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(89,20,46,'2017-12-06 13:40:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(90,20,46,'2017-12-06 13:51:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(91,20,46,'2017-12-06 14:01:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(92,20,46,'2017-12-06 14:03:24','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(93,20,46,'2017-12-06 15:30:19','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(94,20,46,'2017-12-06 15:33:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(95,20,46,'2017-12-06 15:34:25','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(96,20,46,'2017-12-06 15:42:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(97,20,46,'2017-12-06 15:45:33','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(98,20,46,'2017-12-10 00:33:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(99,20,46,'2017-12-10 00:36:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(100,20,46,'2017-12-10 18:24:15','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(101,20,46,'2017-12-10 18:36:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(102,20,46,'2017-12-10 18:56:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(103,20,46,'2017-12-10 19:08:52','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(104,20,46,'2017-12-11 10:46:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(105,20,46,'2017-12-11 11:03:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(106,20,46,'2017-12-11 11:07:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(107,20,46,'2017-12-11 14:27:29','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(108,20,46,'2017-12-11 14:40:19','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(109,20,46,'2017-12-11 15:09:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(110,20,46,'2017-12-11 18:33:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(111,20,46,'2017-12-11 18:54:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(112,20,46,'2017-12-12 20:02:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(113,20,46,'2017-12-12 20:11:33','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(114,20,46,'2017-12-12 20:47:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(115,20,46,'2017-12-12 20:49:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(116,20,46,'2017-12-12 21:22:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(117,20,46,'2017-12-17 14:01:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(118,20,46,'2017-12-17 15:04:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(119,20,46,'2017-12-17 15:05:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(120,20,46,'2017-12-17 15:08:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(121,20,46,'2017-12-17 15:09:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(122,20,46,'2017-12-17 15:22:26','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(123,20,46,'2017-12-17 15:39:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(124,20,46,'2017-12-17 15:45:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(125,20,46,'2017-12-17 15:47:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(126,20,46,'2017-12-17 16:09:29','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(127,20,46,'2017-12-17 16:12:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(128,20,46,'2017-12-17 16:14:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(129,20,46,'2017-12-17 16:16:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(130,20,46,'2017-12-17 16:21:45','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(131,20,46,'2017-12-17 16:25:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(132,20,46,'2017-12-18 07:36:01','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(133,20,46,'2017-12-18 07:53:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(134,20,46,'2017-12-20 08:34:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(135,20,46,'2017-12-20 08:39:44','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(136,20,46,'2017-12-20 13:31:17','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(137,20,46,'2017-12-20 13:31:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(138,20,46,'2017-12-20 13:33:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(139,20,46,'2017-12-20 13:33:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(140,20,46,'2017-12-20 13:35:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(141,20,46,'2017-12-22 00:39:03','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(142,20,46,'2018-01-04 13:01:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(143,20,46,'2018-01-04 14:02:58','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(144,20,46,'2018-01-04 14:07:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(145,20,46,'2018-01-04 14:10:12','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(146,20,46,'2018-01-04 13:42:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(147,20,46,'2018-01-04 15:37:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(148,20,46,'2018-01-16 11:56:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,1,1),(149,20,46,'2018-01-20 12:34:52','en','Feedback','2018-01-23 10:02:43',99999999,0,1,NULL,0,2,0,1,1),(150,20,46,'2018-01-21 16:57:59','en','Feedback','2018-02-07 16:39:43',99999999,1,2,NULL,0,0,0,1,1),(151,20,46,'2018-01-21 17:04:38','en','Feedback','2018-02-02 14:57:03',99999999,0,4,NULL,0,1,0,1,1),(152,20,46,'2018-01-21 17:11:55','en','Feedback','2018-02-13 11:48:44',99999999,0,2,NULL,0,4,0,1,1),(153,20,46,'2018-01-21 17:17:05','en','Feedback','2018-02-13 14:44:06',99999999,0,0,NULL,0,1,0,1,1),(154,20,46,'2018-01-21 17:22:47','en','Feedback','2018-02-02 15:03:36',99999999,1,0,NULL,0,4,0,1,1),(155,20,46,'2018-01-21 17:23:49','en','Feedback','2018-02-13 14:44:11',99999999,0,3,NULL,0,2,0,1,1),(156,20,46,'2018-01-21 17:26:11','en','Feedback','2018-02-06 08:16:13',99999999,4,6,NULL,0,0,0,0,1),(157,20,46,'2018-01-21 17:27:54','en','Feedback','2018-02-15 11:47:53',99999999,3,2,NULL,0,0,0,0,1),(158,20,46,'2018-01-23 08:30:19','en','Feedback','2018-02-02 14:50:42',99999999,0,3,NULL,0,1,0,0,0),(159,20,46,'2018-01-23 08:37:50','en','Feedback','2018-01-23 08:48:55',99999999,0,2,NULL,0,3,0,0,0),(160,20,46,'2018-01-25 16:29:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(161,20,46,'2018-01-25 16:36:54','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(162,20,46,'2018-01-25 16:42:29','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(163,20,46,'2018-01-25 16:51:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(164,20,46,'2018-01-25 16:53:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(165,20,46,'2018-01-25 16:54:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(166,20,46,'2018-01-25 16:55:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(167,20,46,'2018-01-25 16:56:41','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,0),(168,20,46,'2018-01-25 20:10:06','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,0),(169,20,46,'2018-01-25 20:17:51','en','Feedback',NULL,99999999,0,0,NULL,0,1,0,0,0),(170,20,46,'2018-01-26 11:38:02','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(171,20,46,'2018-01-26 16:59:11','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(172,20,46,'2018-01-26 17:02:11','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(173,20,46,'2018-01-26 17:09:32','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,0),(174,20,46,'2018-01-26 17:14:02','en','Feedback',NULL,99999999,0,0,NULL,0,1,0,0,0),(175,20,46,'2018-01-26 17:21:38','en','Feedback','2018-02-02 14:51:13',99999999,0,0,NULL,0,0,0,0,0),(176,20,46,'2018-01-26 17:29:29','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,0),(177,20,46,'2018-01-31 08:30:55','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,0),(178,20,46,'2018-01-31 08:32:52','en','Feedback','2018-02-06 08:32:57',99999999,3,0,NULL,0,1,0,0,1),(179,20,46,'2018-02-05 22:10:51','en','Feedback',NULL,99999999,0,1,NULL,0,0,0,0,1),(180,20,46,'2018-02-05 22:12:05','en','Feedback',NULL,99999999,2,1,NULL,0,0,0,0,1),(181,20,46,'2018-02-06 16:00:52','en','Feedback',NULL,99998888,4,0,NULL,0,1,0,0,1),(182,20,46,'2018-02-10 14:26:49','en','Feedback',NULL,99999999,5,0,NULL,0,1,0,0,1);
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_chat_information`
--

DROP TABLE IF EXISTS `feedback_chat_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_chat_information` (
  `feedback_chat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chat_date` datetime DEFAULT NULL,
  `chat_text` varchar(255) DEFAULT NULL,
  `initated_by_user` bit(1) DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feedback_chat_id`),
  KEY `FK499lsagiuhb2k5jkg40peuhet` (`feedback_id`),
  KEY `FK3knnwken9u06o1w2i285ubki0` (`user_id`),
  CONSTRAINT `FK3knnwken9u06o1w2i285ubki0` FOREIGN KEY (`user_id`) REFERENCES `end_user` (`id`),
  CONSTRAINT `FK499lsagiuhb2k5jkg40peuhet` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_chat_information`
--

LOCK TABLES `feedback_chat_information` WRITE;
/*!40000 ALTER TABLE `feedback_chat_information` DISABLE KEYS */;
INSERT INTO `feedback_chat_information` (`feedback_chat_id`, `chat_date`, `chat_text`, `initated_by_user`, `feedback_id`, `user_id`) VALUES (1,'2018-02-02 15:41:33','hello what do you want','\0',151,1),(2,'2018-02-03 20:41:19','hi','\0',151,99999999),(3,'2018-02-04 22:41:18','test','\0',149,99999999),(4,'2018-02-05 16:21:25','testcomment1','\0',149,99999999),(5,'2018-02-05 23:33:37','testMessage','\0',150,99999999),(6,'2018-02-05 23:35:59','testmessage','\0',150,99999999),(7,'2018-02-05 23:50:21','newmessage','\0',150,99999999),(8,'2018-02-05 23:51:46','newmessage2','\0',150,99999999),(9,'2018-02-05 23:57:24','newmessage 3','\0',150,99999999),(10,'2018-02-06 00:09:42','newmessage 4','\0',150,99999999),(11,'2018-02-06 08:03:02','My response','\0',150,99999999),(12,'2018-02-06 12:19:30','Userfeedback','\0',149,99999999),(13,'2018-02-06 12:23:18','dddd','\0',149,99999999),(14,'2018-02-06 12:25:20','this is me ','\0',149,99999999),(15,'2018-02-06 12:33:31','blabla','\0',149,1),(16,'2018-02-06 12:52:41','companyfeedback','\0',154,1),(17,'2018-02-06 12:53:30','userfeed','\0',154,99999999),(18,'2018-02-06 13:15:01','comp','\0',154,1),(19,'2018-02-06 13:15:52','dd','\0',154,99999999),(20,'2018-02-06 13:52:13','companyfeed','\0',153,2),(21,'2018-02-06 13:52:49','userfeed','\0',153,99999999),(22,'2018-02-06 13:57:45','Help me','\0',155,99999999),(23,'2018-02-06 13:58:26','why why','\0',155,2),(24,'2018-02-06 17:29:00','my feedback','\0',149,99999999),(25,'2018-02-06 17:32:07','my response','\0',149,99999999),(26,'2018-02-13 14:54:37','admin comment','\0',182,99999999),(27,'2018-02-13 21:45:40','','\0',153,99999999),(28,'2018-02-14 09:26:41','test time','\0',149,99999999);
/*!40000 ALTER TABLE `feedback_chat_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_company`
--

DROP TABLE IF EXISTS `feedback_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `promote` bit(1) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_company`
--

LOCK TABLES `feedback_company` WRITE;
/*!40000 ALTER TABLE `feedback_company` DISABLE KEYS */;
INSERT INTO `feedback_company` (`id`, `created_at`, `promote`, `status`, `text`, `updated_at`) VALUES (1,NULL,'','planned','crazy feature coming soon',NULL),(2,NULL,'\0','planned','planned feature X',NULL),(3,NULL,'','implemented','implemented feature Y',NULL),(4,NULL,'\0','implemented','implemented feature Z',NULL),(5,NULL,'','planned','planned feature A',NULL),(6,NULL,'\0','planned','planned feature B',NULL),(7,NULL,'\0','implemented','implemented feature C',NULL),(8,NULL,'','implemented','implemented feature D',NULL),(9,NULL,'\0','implemented','implemented feature E',NULL),(10,NULL,'','implemented','implemented feature F',NULL),(11,'2018-02-11 18:50:15','','status','',NULL),(12,'2018-02-11 18:50:35','','status','t',NULL),(13,'2018-02-11 18:50:35','','status','te',NULL),(14,'2018-02-11 18:50:35','','status','tes',NULL),(15,'2018-02-11 18:50:35','','status','tesz',NULL),(16,'2018-02-11 18:50:37','','status','tes',NULL),(17,'2018-02-11 18:50:37','','status','test',NULL),(18,'2018-02-11 18:50:46','','inProgress','test',NULL),(19,'2018-02-11 21:21:08','','status','',NULL),(20,'2018-02-13 10:36:36','','inProgress','awesome new release',NULL),(21,'2018-02-13 10:36:47','','inProgress','awesome new release',NULL),(22,'2018-02-13 10:36:48','','inProgress','awesome new release',NULL),(23,'2018-02-13 10:36:48','','inProgress','awesome new release',NULL),(24,'2018-02-13 10:36:48','','inProgress','awesome new release',NULL),(25,'2018-02-13 10:36:48','','inProgress','awesome new release',NULL),(26,'2018-02-13 10:36:49','','inProgress','awesome new release',NULL),(27,'2018-02-13 10:36:49','','inProgress','awesome new release',NULL),(28,'2018-02-13 10:36:49','','inProgress','awesome new release',NULL),(29,'2018-02-13 10:36:49','','inProgress','awesome new release',NULL),(30,'2018-02-13 10:36:50','','inProgress','awesome new release',NULL),(31,'2018-02-13 10:36:50','','inProgress','awesome new release',NULL),(32,'2018-02-13 10:36:50','','inProgress','awesome new release',NULL),(33,'2018-02-13 10:36:50','','inProgress','awesome new release',NULL),(34,'2018-02-13 10:36:50','','inProgress','awesome new release',NULL),(35,'2018-02-13 11:40:28','','completed','test',NULL),(36,'2018-02-13 13:47:52','','completed','announcing new release of some crazy feature',NULL),(37,'2018-02-13 13:51:17','','inProgress','new entry',NULL),(38,'2018-02-13 13:52:31','','status','ddd',NULL),(39,'2018-02-13 13:52:37','','0','dddd',NULL),(40,'2018-02-13 13:53:07','','completed','',NULL),(41,'2018-02-13 13:55:33','','inProgress','test',NULL),(42,'2018-02-13 13:57:46','','inProgress','test',NULL),(43,'2018-02-13 13:58:56','','inProgress','ddddd',NULL),(44,'2018-02-13 14:20:53','','completed','gghggggg',NULL),(45,'2018-02-13 14:26:06','','completed','ddddddddd',NULL),(46,'2018-02-13 14:27:54','','completed','ppppfpfpfpff',NULL),(47,'2018-02-13 14:45:21','','inProgress','testing company entry, this feature will be stunning',NULL),(48,'2018-02-14 09:17:24','','inProgress','This is a new upcoming event',NULL);
/*!40000 ALTER TABLE `feedback_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_settings`
--

DROP TABLE IF EXISTS `feedback_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feedback_query` bit(1) NOT NULL,
  `feedback_query_channel` varchar(255) NOT NULL,
  `global_feedback_setting` bit(1) NOT NULL,
  `status_updates` bit(1) NOT NULL,
  `status_updates_contact_channel` varchar(255) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_settings`
--

LOCK TABLES `feedback_settings` WRITE;
/*!40000 ALTER TABLE `feedback_settings` DISABLE KEYS */;
INSERT INTO `feedback_settings` (`id`, `feedback_query`, `feedback_query_channel`, `global_feedback_setting`, `status_updates`, `status_updates_contact_channel`, `feedback_id`, `user_id`) VALUES (1,'\0','','\0','\0','',158,99999999),(2,'\0','','\0','\0','',159,1),(3,'','Feedback-To-Feedback Central','','','Email',161,1),(4,'','Email','','','Feedback-To-Feedback Central',162,1),(5,'\0','','\0','','Email',163,1),(6,'','Feedback-To-Feedback Central','','','Feedback-To-Feedback Central',164,1),(7,'','Feedback-To-Feedback Central','\0','','Email',165,1),(8,'','Email','','','Feedback-To-Feedback Central',166,1),(9,'\0','','\0','\0','',167,1),(10,'\0','','','\0','',169,99999999),(11,'\0','','\0','','Feedback-To-Feedback Central',170,99999999),(12,'\0','','\0','\0','',171,99999999),(13,'\0','','\0','\0','',173,99999999),(14,'\0','','\0','\0','',175,99999999),(15,'','Email ','\0','\0','',176,99999999),(16,'','Email','','','Feedback-To-Feedback Central',177,99999999),(17,'','Feedback-To-Feedback Central','\0','','Email',178,99999999),(18,'\0','','\0','','Feedback-To-Feedback Central',179,99999999),(19,'\0','','','\0','',182,99999999),(20,'','Email','\0','','Email',150,99999999),(21,'','Email','\0','','Email',149,99999999);
/*!40000 ALTER TABLE `feedback_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_status`
--

DROP TABLE IF EXISTS `feedback_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_status`
--

LOCK TABLES `feedback_status` WRITE;
/*!40000 ALTER TABLE `feedback_status` DISABLE KEYS */;
INSERT INTO `feedback_status` (`id`, `date`, `status`, `feedback_id`) VALUES (1,'2018-02-07 14:23:36','in_progress',149),(2,'2018-01-30 14:11:36','in_progress',150),(3,'2018-01-30 14:13:19','declined',151),(4,'2018-01-30 14:13:20','received',152),(5,'2018-01-30 14:13:21','completed',153),(6,'2018-01-30 14:13:22','in_progress',154),(7,'2018-01-30 14:13:24','declined',155),(8,'2018-01-30 14:13:24','received',156),(9,'2018-01-30 14:13:25','completed',157),(10,'2018-01-30 14:13:26','in_progress',158),(11,'2018-01-30 14:13:27','declined',159),(12,'2018-01-30 14:13:28','received',160),(13,'2018-01-30 14:13:29','completed',161),(14,'2018-01-30 14:13:30','in_progress',162),(15,'2018-01-30 14:13:31','declined',163),(16,'2018-01-30 14:13:32','received',164),(17,'2018-01-30 14:13:33','completed',165),(18,'2018-01-30 14:13:34','in_progress',166),(19,'2018-01-30 14:13:35','declined',167),(20,'2018-01-30 14:13:36','received',168),(21,'2018-01-30 14:13:36','completed',169),(22,'2018-01-30 14:13:37','in_progress',170),(23,'2018-01-30 14:13:38','declined',171),(24,'2018-01-30 14:13:39','received',172),(25,'2018-01-30 14:13:40','completed',173),(26,'2018-01-30 14:13:41','in_progress',174),(27,'2018-01-30 14:13:42','declined',175),(28,'2018-01-30 14:13:43','received',176),(29,'2018-01-31 08:30:55','completed',177),(30,'2018-01-31 08:32:52','declined',178),(31,'2018-02-05 22:10:51','completed',179),(32,'2018-02-05 22:12:05','declined',180),(33,'2018-02-06 16:00:52','declined',181),(34,'2018-02-07 16:41:33','in_progress',115),(35,'2018-02-10 14:26:49','declined',182);
/*!40000 ALTER TABLE `feedback_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_viewed`
--

DROP TABLE IF EXISTS `feedback_viewed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_viewed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8b4kr36xe7xqkc6mr4yavlxjn` (`user_id`),
  KEY `FKabbwmf5dldpp256m5b2jgdj0t` (`feedback_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_viewed`
--

LOCK TABLES `feedback_viewed` WRITE;
/*!40000 ALTER TABLE `feedback_viewed` DISABLE KEYS */;
INSERT INTO `feedback_viewed` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES (1,NULL,1,159);
/*!40000 ALTER TABLE `feedback_viewed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_feedback`
--

DROP TABLE IF EXISTS `file_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_feedback` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_extension` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `mechanism_id` bigint(20) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa93pglqlwxt6kxxqn248si979` (`feedback_id`),
  CONSTRAINT `FKa93pglqlwxt6kxxqn248si979` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_feedback`
--

LOCK TABLES `file_feedback` WRITE;
/*!40000 ALTER TABLE `file_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating_feedback`
--

DROP TABLE IF EXISTS `rating_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_feedback`
--

LOCK TABLES `rating_feedback` WRITE;
/*!40000 ALTER TABLE `rating_feedback` DISABLE KEYS */;
INSERT INTO `rating_feedback` (`id`, `mechanism_id`, `rating`, `title`, `feedback_id`) VALUES (1,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',149),(2,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',150),(3,90,1,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',151),(4,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',152),(5,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',153),(6,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',154),(7,90,1,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',155),(8,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',156),(9,90,4,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',157),(10,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',158),(11,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',159),(12,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',160),(13,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',161),(14,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',162),(15,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',163),(16,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',164),(17,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',165),(18,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',166),(19,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',167),(20,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',168),(21,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',169),(22,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',170),(23,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',171),(24,90,5,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',172),(25,90,4,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',173),(26,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',174),(27,90,4,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',175),(28,90,4,'Rate your experience with the feature you just used.',176),(29,90,3,'Rate your experience with the feature you just used.',177),(30,90,2,'Rate your experience with the feature you just used.',178),(31,90,4,'Rate your experience with the feature you just used.',179),(32,90,4,'Rate your experience with the feature you just used.',180),(33,90,3,'Rate your experience with the feature you just used.',181),(34,90,5,'Rate your experience with the feature you just used.',182);
/*!40000 ALTER TABLE `rating_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screenshot_feedback`
--

DROP TABLE IF EXISTS `screenshot_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screenshot_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screenshot_feedback`
--

LOCK TABLES `screenshot_feedback` WRITE;
/*!40000 ALTER TABLE `screenshot_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `screenshot_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `feedback_email_receivers` varchar(255) DEFAULT NULL,
  `kafka_topic_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  `status_option_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_option`
--

DROP TABLE IF EXISTS `status_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `order` int(11) NOT NULL,
  `user_specific` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_option`
--

LOCK TABLES `status_option` WRITE;
/*!40000 ALTER TABLE `status_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `status_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_annotation`
--

DROP TABLE IF EXISTS `text_annotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference_number` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `screenshot_feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_annotation`
--

LOCK TABLES `text_annotation` WRITE;
/*!40000 ALTER TABLE `text_annotation` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_annotation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_feedback`
--

DROP TABLE IF EXISTS `text_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `text` longtext,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_feedback`
--

LOCK TABLES `text_feedback` WRITE;
/*!40000 ALTER TABLE `text_feedback` DISABLE KEYS */;
INSERT INTO `text_feedback` (`id`, `mechanism_id`, `text`, `feedback_id`) VALUES (1,88,'test feedback mail hihi',149),(2,88,'The pictures of my household statistics are not displayed appropriately. I tried to resize them but it was not possible. Zooming in and out of the image does not work. I\'m using an iMac with HighSierra. But this worked fine before the last update. Please help.',150),(3,88,'My firefox crashes continuously when I try to open my invoices. Before the browser crashes there is a pop-up window but I\'m not able to read it since it closes automatically before I have the chance to react or do anything ',151),(4,88,'Application crash on clicking the SAVE button while creating a new contract. \nAnteater crude saliently much armadillo in bandicoot input a much haphazardly worm on proofread on the much one husky elaborate less far less and less elegant fleet because yikes rigorously jeepers.\n\nObliquely opossum wombat on some supportive away while and following removed excluding woolly including since but weasel incapably fruitlessly yet goodness alas.\n\nAs the insufferably on near insecurely cowardly forgetful wetted wow alas in nodded naively much bombastic fond timid ouch this banefully overate lantern lemming sudden that.',152),(5,88,'I want an export functionality to choose which format I want to have for my invoices. I cannot display doc format on all my devices. An pdf option would be nice. Much more outside conductive more much grumbled excluding floppily some within animated far the goodness overdrew rattlesnake ouch unavoidable far.',153),(6,88,'I want to deactivate the newsletter. Where do I find the setting to change that?',154),(7,88,'Game far stung buffalo sulky purposefully diplomatically when much yet much garrulous whale flipped monkey and panda hugely compactly.\n\nLess wow much before rethought obscurely immaculately random peculiar bore far salaciously some growled juggled hence animatedly saw much less noisy far hey as.\n\nGosh so without far lingering armadillo reflective a coherently oh led a far across this so frog indecently pointed oh rare so since.\n\nDoused darn one darn hence along dear away this that therefore besides so paid impeccable and crud parrot then following much gerbil past.',155),(8,88,'Due until a bird impartially absolute some trout unanimously unsaddled pangolin iguana less and quit crud metaphorically sought fantastically in the icily.\n\nSighed the inventoried gazed jeez when so gnu on agreeable a a inside so groundhog expeditious inside since groundhog outside madly added some goodness jeez and found however.\n\nRaccoon well this or some much regardless demonstrable much quizzically spacious wow yet supply amid towards up mammoth reluctant.',156),(9,88,'Owing aside yikes whimsically added oh darn pending more sadistic among altruistically.\n\nOne fresh cut as save hatchet fell maternal rabbit and hey crud then salmon deserved.\n\nTherefore a much aerially to yet this thought alas but other that darkly a ouch one spiteful well notably hello as.\n\nMuch oh that forthright and some boundlessly less this met rabbit far up additional barbarously black far and a jeepers a hence after alas then concentric near gull indisputably.\n\nSince goodness stung beyond and while crud together frugal bald began opossum and yet whistled cardinal far then well directed malicious as cardinal indisputable snooty then.',157),(10,88,'test melanie',158),(11,88,'test F2F Central 23_01_18',159),(12,88,'test feedback settings persistence',160),(13,88,'hey ho du do',161),(14,88,'I have a problem. Because until cynic so jeez overran touched so concentrically more sluggish hey much until skillful strongly vulnerable and groundhog the and a insect darn thanks and jeez listlessly marvelous.\n\nA enormously opposite other seal and up the ate while tamarin far grizzly more.\n\nTemperately much and this naked crud goodness beheld and frugally flirtatious jellyfish when away iguanodon that alas marginal after.',162),(15,88,'About much emphatic forewent unlike meadowlark much underneath met opposite until despicably near as alert where in much hey.\n\nThis and wherever more outside eagle gosh groggy leaned the darn squid cumulative darn far hare agilely sordidly some jeepers the purred hello tacitly.\n\nSlatternly far one jeepers gosh cardinal much jeepers surreptitious by ouch baboon cuddled hello chortled much outside hence augustly provident near ouch far intellectually flimsily radical.',163),(16,88,'This when and imminent swore cracked and ceaseless subconsciously strangely overlaid across far factious lost freely some trout less over gosh that.\n\nMuch more ouch boa then dear yet oh much and scantly reindeer jocosely alas uneasy impassive terse expeditiously paid heron emu boyish jeepers fox rewrote while much therefore the.\n\nThat some falcon unscrupulous grossly much literal between out tiger therefore yikes labrador.\n',164),(17,88,'Groundhog regardless before wallaby far in thus more and that glaringly much flapped wow saw the man-of-war as irritable less and awesome more.\n\nThe however aboard bandicoot goodness raptly much yet flashy some dear cuffed and oh so spoke through more upon one grotesquely one the drove in.\n\nVia from crud amid goodness whimpered mistook beaver less ouch abhorrently that.\n',165),(18,88,'Wow some dismounted composite until koala and and the pending much some vulture darn tortoise ouch.\n\nHatchet while wholeheartedly much while where until flamingo sensitively while yikes that.\n\nDipped opposite slept therefore and shuffled desperately formidably because respectfully wept lobster poor hey that a unsuccessful ladybug definitely a.\n\nYikes that active when furrowed more uniquely macaw longingly but gerbil rabbit foolish pessimistically hyena so behind testy much less fragrant after spoon-fed the hey sneered adept.\n\nHeedlessly one that alas a porpoise where gladly far alas one ouch goodness burst returned prior as impassive oppressively that far honey wasp more jeez.\n\nImmediately bucolic scallop far past legitimate stupid under after solemnly and.',166),(19,88,'Sedately while camel struck far smoked cracked the less hedgehog one well far gosh sleek light stupidly goodness unlike.\n\nWeakly repusively plankton yet much off meretriciously before so the ouch smirked and lantern one before greyhound while infinitesimal together.\n\nThat doubtful that alas dear and one a far pugnacious wedded agitated more frog kangaroo thickly much until one one titillating camel disagreed zebra much plankton regarding far pill.\n\nImmutably more much spluttered overpaid on much much dachshund irrespective indubitable jeez before ouch because unsafely mellifluous more this by sheep monkey gaudy.\n\nTonelessly darn attractive fought aurally a jolly unthinkingly intuitive alas less the when some far yet preparatory imprecise therefore and gull cannily frankly in wow.\n\nMore far less purred rethought abstrusely ethereally obscurely freshly leaned gosh.',167),(20,88,'hey ho was geht',168),(21,88,'test test test test test',169),(22,88,'test weblibrary',170),(23,88,'test web library second',171),(24,88,'test weblibrary',172),(25,88,'test web library',173),(26,88,'test web library',174),(27,88,'test web library',175),(28,88,'test web library',176),(29,88,'This is a test...',177),(30,88,'test 2',178),(31,88,'test feedback on 05.02.2018',179),(32,88,'test second time',180),(33,88,'I\'m a new user- My feedback is important',181),(34,88,'test send feedback on 10th february',182);
/*!40000 ALTER TABLE `text_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userfbdislike`
--

DROP TABLE IF EXISTS `userfbdislike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userfbdislike` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userfbdislike`
--

LOCK TABLES `userfbdislike` WRITE;
/*!40000 ALTER TABLE `userfbdislike` DISABLE KEYS */;
INSERT INTO `userfbdislike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES (64,'2018-01-25 13:13:09',1,150),(65,'2018-01-25 13:13:15',2,150),(66,'2018-01-25 13:13:34',1,151),(67,'2018-01-25 13:13:37',2,151),(68,'2018-01-25 13:13:39',3,151),(69,'2018-01-25 13:13:41',4,151),(70,'2018-01-25 13:14:09',5,152),(71,'2018-01-25 13:15:13',3,155),(72,'2018-01-25 13:15:16',4,155),(73,'2018-01-25 13:15:21',5,155),(74,'2018-01-25 13:16:00',1,156),(75,'2018-01-25 13:16:02',2,156),(76,'2018-01-25 13:16:05',3,156),(77,'2018-01-25 13:16:08',4,156),(78,'2018-01-25 13:16:10',5,156),(79,'2018-01-25 13:17:11',1,157),(80,'2018-01-25 13:17:14',2,157),(81,'2018-01-25 13:18:08',2,158),(82,'2018-01-25 13:18:11',3,158),(83,'2018-01-25 13:18:14',4,158),(84,'2018-01-25 13:19:03',4,159),(85,'2018-01-25 13:19:06',5,159),(93,'2018-01-25 20:23:16',99999999,168),(95,'2018-01-30 14:09:55',99999999,176),(97,'2018-01-30 14:11:41',99999999,173),(98,'2018-01-31 08:45:25',99999999,177),(100,'2018-01-31 08:46:21',99999999,152),(102,'2018-02-05 21:05:19',99999999,167),(103,'2018-02-06 13:35:39',99999999,180),(104,'2018-02-06 13:35:43',99999999,179),(105,'2018-02-06 13:35:47',99999999,156),(106,'2018-02-06 13:35:57',99999999,149);
/*!40000 ALTER TABLE `userfbdislike` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_dislike */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_dislike AFTER INSERT ON userfbdislike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_dislike2 */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_dislike2 AFTER DELETE ON userfbdislike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `userfblike`
--

DROP TABLE IF EXISTS `userfblike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userfblike` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userfblike`
--

LOCK TABLES `userfblike` WRITE;
/*!40000 ALTER TABLE `userfblike` DISABLE KEYS */;
INSERT INTO `userfblike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES (57,'2018-01-25 13:10:01',1,149),(58,'2018-01-25 13:10:13',2,149),(59,'2018-01-25 13:10:27',1,152),(60,'2018-01-25 13:10:30',2,152),(61,'2018-01-25 13:10:34',3,152),(62,'2018-01-25 13:10:39',4,152),(63,'2018-01-25 13:11:27',1,153),(64,'2018-01-25 13:11:47',1,154),(65,'2018-01-25 13:11:50',2,154),(66,'2018-01-25 13:11:53',3,154),(67,'2018-01-25 13:12:13',1,155),(68,'2018-01-25 13:12:15',2,155),(69,'2018-01-25 13:12:26',1,158),(70,'2018-01-25 13:12:32',1,159),(71,'2018-01-25 13:12:36',2,159),(72,'2018-01-25 13:12:39',3,159),(82,'2018-01-25 20:23:06',99999999,169),(89,'2018-01-30 14:11:35',99999999,174),(98,'2018-02-06 13:35:45',99999999,178),(99,'2018-02-06 13:35:52',99999999,154),(100,'2018-02-06 13:35:55',99999999,151),(103,'2018-02-13 17:17:17',99999999,182),(106,'2018-02-13 17:23:22',99999999,181);
/*!40000 ALTER TABLE `userfblike` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_like */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_like AFTER INSERT ON userfblike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_like2 */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_like2 AFTER DELETE ON userfblike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-15 15:56:43
