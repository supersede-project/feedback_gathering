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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_feedback`
--

LOCK TABLES `category_feedback` WRITE;
/*!40000 ALTER TABLE `category_feedback` DISABLE KEYS */;
INSERT INTO `category_feedback` (`id`, `mechanism_id`, `parameter_id`, `text`, `feedback_id`) VALUES (1,0,663,'',149),(2,0,661,'',150),(3,0,661,'',151),(4,0,662,'',152),(5,0,662,'',153),(6,0,663,'',153),(7,0,NULL,'Support',154),(8,0,661,'',155),(9,0,661,'',156),(10,0,663,'',157);
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
  PRIMARY KEY (`id`),
  KEY `FKsjg1n0mnkm7haiyt6k8pqbd4x` (`feedback_id`),
  KEY `FKh1fyv8nt5no6b2yw1q3a7j1kt` (`fk_parent_comment`),
  KEY `FK7rsl9hwpd3dh6ucn5pxuo6j4d` (`user_id`),
  CONSTRAINT `FK7rsl9hwpd3dh6ucn5pxuo6j4d` FOREIGN KEY (`user_id`) REFERENCES `end_user` (`id`),
  CONSTRAINT `FKh1fyv8nt5no6b2yw1q3a7j1kt` FOREIGN KEY (`fk_parent_comment`) REFERENCES `comment_feedback` (`id`),
  CONSTRAINT `FKsjg1n0mnkm7haiyt6k8pqbd4x` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_feedback`
--

LOCK TABLES `comment_feedback` WRITE;
/*!40000 ALTER TABLE `comment_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_feedback` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `context_information`
--

LOCK TABLES `context_information` WRITE;
/*!40000 ALTER TABLE `context_information` DISABLE KEYS */;
INSERT INTO `context_information` (`id`, `android_version`, `country`, `device_pixel_ratio`, `local_time`, `meta_data`, `region`, `resolution`, `time_zone`, `url`, `user_agent`, `feedback_id`) VALUES (1,NULL,NULL,1,'2018-01-20 13:34:51','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'728x1366','+0100','http://localhost:9090/','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',149),(2,NULL,NULL,2,'2018-01-21 17:57:59','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'878x1440','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',150),(3,NULL,NULL,2,'2018-01-21 18:04:38','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',151),(4,NULL,NULL,2,'2018-01-21 18:11:55','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',152),(5,NULL,NULL,2,'2018-01-21 18:17:05','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',153),(6,NULL,NULL,2,'2018-01-21 18:22:47','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',154),(7,NULL,NULL,2,'2018-01-21 18:23:49','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',155),(8,NULL,NULL,2,'2018-01-21 18:26:11','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',156),(9,NULL,NULL,2,'2018-01-21 18:27:54','{\"diagram\":\"diagram X 02\",\"section\":\"section#diagramSection\"}',NULL,'1418x2560','+0100','http://f2f.ronnieschaniel.com/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36',157);
/*!40000 ALTER TABLE `context_information` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `end_user`
--

LOCK TABLES `end_user` WRITE;
/*!40000 ALTER TABLE `end_user` DISABLE KEYS */;
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
  `is_blocked` tinyint(1) DEFAULT '0',
  `like_count` int(11) NOT NULL,
  `unread_comment_count` int(11) NOT NULL,
  `published` tinyint(1) DEFAULT '0',
  `visibility` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` (`id`, `application_id`, `configuration_id`, `created_at`, `language`, `title`, `updated_at`, `user_identification`, `comment_count`, `dislike_count`, `icon_path`, `is_blocked`, `like_count`, `unread_comment_count`, `published`, `visibility`) VALUES (1,20,46,'2017-11-27 10:55:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(2,20,46,'2017-11-27 10:59:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(3,20,46,'2017-11-27 11:01:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(4,20,46,'2017-11-28 14:25:14','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(5,20,46,'2017-11-28 14:39:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(6,20,46,'2017-11-29 09:55:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(7,20,46,'2017-11-29 09:57:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(8,20,46,'2017-11-29 10:19:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(9,20,46,'2017-11-29 10:19:34','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(10,20,46,'2017-11-29 10:32:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(11,20,46,'2017-11-29 10:38:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(12,20,46,'2017-11-29 10:41:16','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(13,20,46,'2017-11-29 10:44:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(14,20,46,'2017-11-29 10:51:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(15,20,46,'2017-11-29 10:51:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(16,20,46,'2017-11-29 10:58:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(17,20,46,'2017-11-29 11:11:02','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(18,20,46,'2017-11-29 11:16:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(19,20,46,'2017-11-29 11:23:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(20,20,46,'2017-11-29 11:26:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(21,20,46,'2017-11-29 11:35:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(22,20,46,'2017-11-29 11:42:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(23,20,46,'2017-11-29 11:45:39','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(24,20,46,'2017-11-29 11:46:15','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(25,20,46,'2017-11-29 11:48:01','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(26,20,46,'2017-11-29 11:48:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(27,20,46,'2017-11-29 11:54:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(28,20,46,'2017-11-29 13:04:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(29,20,46,'2017-11-29 13:06:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(30,20,46,'2017-11-29 18:22:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(31,20,46,'2017-11-29 18:34:44','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(32,20,46,'2017-11-30 18:06:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(33,20,46,'2017-11-30 19:53:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(34,20,46,'2017-11-30 20:11:58','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(35,20,46,'2017-11-30 20:17:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(36,20,46,'2017-11-30 20:22:11','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(37,20,46,'2017-11-30 20:25:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(38,20,46,'2017-11-30 20:33:59','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(39,20,46,'2017-11-30 20:42:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(40,20,46,'2017-11-30 20:44:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(41,20,46,'2017-11-30 20:45:30','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(42,20,46,'2017-11-30 20:47:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(43,20,46,'2017-11-30 20:52:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(44,20,46,'2017-11-30 21:38:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(45,20,46,'2017-11-30 21:40:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(46,20,46,'2017-11-30 21:42:25','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(47,20,46,'2017-11-30 21:51:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(48,20,46,'2017-11-30 21:53:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(49,20,46,'2017-11-30 22:01:45','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(50,20,46,'2017-11-30 22:11:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(51,20,46,'2017-11-30 22:18:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(52,20,46,'2017-11-30 22:21:23','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(53,20,46,'2017-11-30 22:28:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(54,20,46,'2017-12-01 10:40:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(55,20,46,'2017-12-01 10:42:28','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(56,20,46,'2017-12-01 18:27:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(57,20,46,'2017-12-01 18:37:16','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(58,20,46,'2017-12-01 18:40:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(59,20,46,'2017-12-01 19:14:32','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(60,20,46,'2017-12-01 19:20:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(61,20,46,'2017-12-01 19:23:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(62,20,46,'2017-12-01 19:26:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(63,20,46,'2017-12-02 00:29:51','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(64,20,46,'2017-12-02 00:35:57','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(65,20,46,'2017-12-02 00:42:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(66,20,46,'2017-12-02 00:56:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(67,20,46,'2017-12-03 20:20:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(68,20,46,'2017-12-03 20:47:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(69,20,46,'2017-12-03 20:50:32','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(70,20,46,'2017-12-03 21:01:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(71,20,46,'2017-12-03 21:03:57','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(72,20,46,'2017-12-03 21:12:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(73,20,46,'2017-12-03 21:15:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(74,20,46,'2017-12-03 21:20:27','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(75,20,46,'2017-12-03 21:31:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(76,20,46,'2017-12-03 21:40:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(77,20,46,'2017-12-03 21:46:37','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(78,20,46,'2017-12-03 22:33:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(79,20,46,'2017-12-03 22:36:51','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(80,20,46,'2017-12-05 20:20:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(81,20,46,'2017-12-05 20:52:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(82,20,46,'2017-12-05 20:57:20','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(83,20,46,'2017-12-05 20:58:42','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(84,20,46,'2017-12-06 09:29:28','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(85,20,46,'2017-12-06 12:50:54','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(86,20,46,'2017-12-06 13:03:53','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(87,20,46,'2017-12-06 13:05:38','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(88,20,46,'2017-12-06 13:15:09','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(89,20,46,'2017-12-06 13:40:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(90,20,46,'2017-12-06 13:51:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(91,20,46,'2017-12-06 14:01:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(92,20,46,'2017-12-06 14:03:24','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(93,20,46,'2017-12-06 15:30:19','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(94,20,46,'2017-12-06 15:33:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(95,20,46,'2017-12-06 15:34:25','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(96,20,46,'2017-12-06 15:42:35','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(97,20,46,'2017-12-06 15:45:33','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(98,20,46,'2017-12-10 00:33:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(99,20,46,'2017-12-10 00:36:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(100,20,46,'2017-12-10 18:24:15','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(101,20,46,'2017-12-10 18:36:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(102,20,46,'2017-12-10 18:56:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(103,20,46,'2017-12-10 19:08:52','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(104,20,46,'2017-12-11 10:46:43','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(105,20,46,'2017-12-11 11:03:06','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(106,20,46,'2017-12-11 11:07:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(107,20,46,'2017-12-11 14:27:29','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(108,20,46,'2017-12-11 14:40:19','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(109,20,46,'2017-12-11 15:09:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(110,20,46,'2017-12-11 18:33:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(111,20,46,'2017-12-11 18:54:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(112,20,46,'2017-12-12 20:02:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(113,20,46,'2017-12-12 20:11:33','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(114,20,46,'2017-12-12 20:47:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(115,20,46,'2017-12-12 20:49:13','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(116,20,46,'2017-12-12 21:22:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(117,20,46,'2017-12-17 14:01:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(118,20,46,'2017-12-17 15:04:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(119,20,46,'2017-12-17 15:05:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(120,20,46,'2017-12-17 15:08:36','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(121,20,46,'2017-12-17 15:09:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(122,20,46,'2017-12-17 15:22:26','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(123,20,46,'2017-12-17 15:39:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(124,20,46,'2017-12-17 15:45:50','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(125,20,46,'2017-12-17 15:47:10','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(126,20,46,'2017-12-17 16:09:29','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(127,20,46,'2017-12-17 16:12:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(128,20,46,'2017-12-17 16:14:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(129,20,46,'2017-12-17 16:16:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(130,20,46,'2017-12-17 16:21:45','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(131,20,46,'2017-12-17 16:25:31','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(132,20,46,'2017-12-18 07:36:01','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(133,20,46,'2017-12-18 07:53:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(134,20,46,'2017-12-20 08:34:18','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(135,20,46,'2017-12-20 08:39:44','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(136,20,46,'2017-12-20 13:31:17','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(137,20,46,'2017-12-20 13:31:21','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(138,20,46,'2017-12-20 13:33:22','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(139,20,46,'2017-12-20 13:33:46','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(140,20,46,'2017-12-20 13:35:07','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(141,20,46,'2017-12-22 00:39:03','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(142,20,46,'2018-01-04 13:01:40','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(143,20,46,'2018-01-04 14:02:58','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(144,20,46,'2018-01-04 14:07:48','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(145,20,46,'2018-01-04 14:10:12','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(146,20,46,'2018-01-04 13:42:56','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(147,20,46,'2018-01-04 15:37:08','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(148,20,46,'2018-01-16 11:56:00','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(149,20,46,'2018-01-20 12:34:52','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(150,20,46,'2018-01-21 16:57:59','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(151,20,46,'2018-01-21 17:04:38','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(152,20,46,'2018-01-21 17:11:55','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(153,20,46,'2018-01-21 17:17:05','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(154,20,46,'2018-01-21 17:22:47','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(155,20,46,'2018-01-21 17:23:49','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,0),(156,20,46,'2018-01-21 17:26:11','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1),(157,20,46,'2018-01-21 17:27:54','en','Feedback',NULL,99999999,0,0,NULL,0,0,0,0,1);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_chat_information`
--

LOCK TABLES `feedback_chat_information` WRITE;
/*!40000 ALTER TABLE `feedback_chat_information` DISABLE KEYS */;
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
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5yada2i1ca48okpp3yegv5xxf` (`feedback_id`),
  CONSTRAINT `FK5yada2i1ca48okpp3yegv5xxf` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_company`
--

LOCK TABLES `feedback_company` WRITE;
/*!40000 ALTER TABLE `feedback_company` DISABLE KEYS */;
INSERT INTO `feedback_company` (`id`, `created_at`, `promote`, `status`, `text`, `updated_at`, `feedback_id`) VALUES (1,NULL,'','planned','crazy feature coming soon',NULL,1),(2,NULL,'\0','planned','planned feature X',NULL,NULL),(3,NULL,'','implemented','implemented feature Y',NULL,NULL),(4,NULL,'\0','implemented','implemented feature Z',NULL,NULL),(5,NULL,'','planned','planned feature A',NULL,NULL),(6,NULL,'\0','planned','planned feature B',NULL,NULL),(7,NULL,'\0','implemented','implemented feature C',NULL,NULL),(8,NULL,'','implemented','implemented feature D',NULL,NULL),(9,NULL,'\0','implemented','implemented feature E',NULL,NULL),(10,NULL,'','implemented','implemented feature F',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_settings`
--

LOCK TABLES `feedback_settings` WRITE;
/*!40000 ALTER TABLE `feedback_settings` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_status`
--

LOCK TABLES `feedback_status` WRITE;
/*!40000 ALTER TABLE `feedback_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_status` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_feedback`
--

LOCK TABLES `rating_feedback` WRITE;
/*!40000 ALTER TABLE `rating_feedback` DISABLE KEYS */;
INSERT INTO `rating_feedback` (`id`, `mechanism_id`, `rating`, `title`, `feedback_id`) VALUES (1,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',149),(2,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',150),(3,90,1,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',151),(4,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',152),(5,90,3,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',153),(6,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',154),(7,90,1,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',155),(8,90,2,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',156),(9,90,4,'Bewerten Sie das Feature, das Sie gerade benutzt haben.',157);
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
-- Table structure for table `status_options`
--

DROP TABLE IF EXISTS status_option;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `order` int(11) NOT NULL,
  `user_specific` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_options`
--

LOCK TABLES status_option WRITE;
/*!40000 ALTER TABLE status_option DISABLE KEYS */;
/*!40000 ALTER TABLE status_option ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_feedback`
--

LOCK TABLES `text_feedback` WRITE;
/*!40000 ALTER TABLE `text_feedback` DISABLE KEYS */;
INSERT INTO `text_feedback` (`id`, `mechanism_id`, `text`, `feedback_id`) VALUES (1,88,'test feedback mail hihi',149),(2,88,'The pictures of my household statistics are not displayed appropriately. I tried to resize them but it was not possible. Zooming in and out of the image does not work. I\'m using an iMac with HighSierra. But this worked fine before the last update. Please help.',150),(3,88,'My firefox crashes continuously when I try to open my invoices. Before the browser crashes there is a pop-up window but I\'m not able to read it since it closes automatically before I have the chance to react or do anything ',151),(4,88,'Application crash on clicking the SAVE button while creating a new contract. \nAnteater crude saliently much armadillo in bandicoot input a much haphazardly worm on proofread on the much one husky elaborate less far less and less elegant fleet because yikes rigorously jeepers.\n\nObliquely opossum wombat on some supportive away while and following removed excluding woolly including since but weasel incapably fruitlessly yet goodness alas.\n\nAs the insufferably on near insecurely cowardly forgetful wetted wow alas in nodded naively much bombastic fond timid ouch this banefully overate lantern lemming sudden that.',152),(5,88,'I want an export functionality to choose which format I want to have for my invoices. I cannot display doc format on all my devices. An pdf option would be nice. Much more outside conductive more much grumbled excluding floppily some within animated far the goodness overdrew rattlesnake ouch unavoidable far.',153),(6,88,'I want to deactivate the newsletter. Where do I find the setting to change that?',154),(7,88,'Game far stung buffalo sulky purposefully diplomatically when much yet much garrulous whale flipped monkey and panda hugely compactly.\n\nLess wow much before rethought obscurely immaculately random peculiar bore far salaciously some growled juggled hence animatedly saw much less noisy far hey as.\n\nGosh so without far lingering armadillo reflective a coherently oh led a far across this so frog indecently pointed oh rare so since.\n\nDoused darn one darn hence along dear away this that therefore besides so paid impeccable and crud parrot then following much gerbil past.',155),(8,88,'Due until a bird impartially absolute some trout unanimously unsaddled pangolin iguana less and quit crud metaphorically sought fantastically in the icily.\n\nSighed the inventoried gazed jeez when so gnu on agreeable a a inside so groundhog expeditious inside since groundhog outside madly added some goodness jeez and found however.\n\nRaccoon well this or some much regardless demonstrable much quizzically spacious wow yet supply amid towards up mammoth reluctant.',156),(9,88,'Owing aside yikes whimsically added oh darn pending more sadistic among altruistically.\n\nOne fresh cut as save hatchet fell maternal rabbit and hey crud then salmon deserved.\n\nTherefore a much aerially to yet this thought alas but other that darkly a ouch one spiteful well notably hello as.\n\nMuch oh that forthright and some boundlessly less this met rabbit far up additional barbarously black far and a jeepers a hence after alas then concentric near gull indisputably.\n\nSince goodness stung beyond and while crud together frugal bald began opossum and yet whistled cardinal far then well directed malicious as cardinal indisputable snooty then.',157);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userfbdislike`
--

LOCK TABLES `userfbdislike` WRITE;
/*!40000 ALTER TABLE `userfbdislike` DISABLE KEYS */;
INSERT INTO `userfbdislike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES (1,'2018-01-21 18:33:48',NULL,152),(2,'2018-01-21 18:33:49',NULL,152),(3,'2018-01-21 18:33:50',NULL,152),(4,'2018-01-21 18:33:52',NULL,152),(5,'2018-01-21 18:34:14',NULL,152),(6,'2018-01-21 18:35:47',NULL,152),(7,'2018-01-21 18:35:52',NULL,152),(8,'2018-01-21 18:36:26',NULL,157);
/*!40000 ALTER TABLE `userfbdislike` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userfblike`
--

LOCK TABLES `userfblike` WRITE;
/*!40000 ALTER TABLE `userfblike` DISABLE KEYS */;
INSERT INTO `userfblike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES (1,'2018-01-21 18:31:53',NULL,150),(2,'2018-01-21 18:32:27',NULL,150);
/*!40000 ALTER TABLE `userfblike` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-21 19:43:20
