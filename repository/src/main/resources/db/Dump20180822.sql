-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: supersede_repository_spring_android
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
-- Table structure for table `android_user`
--

DROP TABLE IF EXISTS `android_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `android_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `is_developer` bit(1) NOT NULL,
  `is_blocked` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `android_user`
--

LOCK TABLES `android_user` WRITE;
/*!40000 ALTER TABLE `android_user` DISABLE KEYS */;
INSERT INTO `android_user` VALUES (1,1,'AUser1','','\0'),(3,1,'AUser2','\0',''),(4,1,'AUser3','\0',''),(5,1,'AUser2#1','\0','\0'),(6,1,'AUser2#2','\0','\0'),(7,1,'AUser2#3','','\0'),(8,1,'AUser2#4','','\0'),(9,1,'AUser2#5','','\0'),(11,-1037618203343976446,'dani#0','\0','\0'),(12,-1037618203343976446,'dani#1','\0','\0'),(13,-1037618203343976446,'dani#2','\0','\0'),(14,-1037618203343976446,'Jofroe#3','\0','\0'),(15,-1037618203343976446,'Jofroe#4','','\0'),(16,-1037618203343976446,'Jofore Developer#5','','\0'),(17,-1037618203343976446,'dani#6','\0','\0'),(18,-1037618203343976446,'dani#7','\0','\0'),(19,-1037618203343976446,'dani#8','\0','\0'),(20,-1037618203343976446,'dani#9','','\0'),(21,-1037618203343976446,'Marco#10','\0','\0'),(22,-1037618203343976446,'Marco#11','\0','\0'),(23,-1037618203343976446,'Marco#12','\0','\0'),(24,-1037618203343976446,'OfenUser#13','\0','\0'),(25,-1037618203343976446,'Marco#14','\0','\0'),(26,-1037618203343976446,'Marco#15','\0','\0'),(27,-1037618203343976446,'Jofroe Developer#16','\0','\0'),(28,-1037618203343976446,'Jofroe Developer#17','','\0'),(29,-1037618203343976446,'dani#18','\0','\0'),(30,-1037618203343976446,'daniel#19','\0','\0'),(31,-1037618203343976446,'daniel#20','','\0'),(32,-1037618203343976446,'Daniel#21','\0','\0'),(33,-1037618203343976446,'Daniel#22','\0','\0'),(34,-1037618203343976446,'Daniel#23','\0','\0'),(35,-1037618203343976446,'Marco#24','\0','\0'),(36,-1037618203343976446,'Marco#25','\0','\0'),(37,-1037618203343976446,'Marco#26','\0','\0'),(38,-1037618203343976446,'Marco#27','\0','\0'),(39,-1037618203343976446,'Marco (Developer)#28','\0','\0'),(40,-1037618203343976446,'Marco Dev#29','','\0'),(41,-1037618203343976446,'Marco Dev#30','','\0'),(42,-1037618203343976446,'Marco#31','\0','\0'),(43,-1037618203343976446,'Marco#32','\0','\0'),(44,-1037618203343976446,'Marco#33','\0','\0'),(45,-1037618203343976446,'Marco#34','\0','\0'),(46,-1037618203343976446,'Marco#35','\0','\0'),(47,-1037618203343976446,'Jofroe#36','\0','\0'),(48,-1037618203343976446,'ichhabebereitseinenuser#37','\0','\0'),(49,-1037618203343976446,'Jofroe#38','','\0'),(50,-1037618203343976446,'Jofrr#39','','\0'),(51,-1037618203343976446,'Marco#40','\0','\0');
/*!40000 ALTER TABLE `android_user` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user`
--

LOCK TABLES `api_user` WRITE;
/*!40000 ALTER TABLE `api_user` DISABLE KEYS */;
INSERT INTO `api_user` VALUES (1,'admin','$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy');
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
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_api_user_role`
--

LOCK TABLES `api_user_api_user_role` WRITE;
/*!40000 ALTER TABLE `api_user_api_user_role` DISABLE KEYS */;
INSERT INTO `api_user_api_user_role` VALUES (1,1,1);
/*!40000 ALTER TABLE `api_user_api_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_user_permission`
--

DROP TABLE IF EXISTS `api_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_permission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `api_user_id` bigint(11) NOT NULL,
  `application_id` bigint(11) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_user_id` (`api_user_id`),
  CONSTRAINT `api_user_fk` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_permission`
--

LOCK TABLES `api_user_permission` WRITE;
/*!40000 ALTER TABLE `api_user_permission` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment_feedback`
--

LOCK TABLES `attachment_feedback` WRITE;
/*!40000 ALTER TABLE `attachment_feedback` DISABLE KEYS */;
INSERT INTO `attachment_feedback` VALUES (1,'pdf',4,'test_file.pdf',20000,1),(2,'png',26,'7_99999999_1500463793774_Screen_Shot_2017-07-19_at_13.13.21.png',21854,16),(3,'pdf',26,'7_99999999_1500463793775_test_Kopie_15.pdf',18290,16),(4,'pdf',26,'7_99999999_1500463793775_test_Kopie_16.pdf',18290,16),(5,'png',26,'7_99999999_1500463905948_Screen_Shot_2017-07-19_at_13.13.21.png',21854,17),(6,'pdf',26,'7_99999999_1500463905949_test_Kopie_15.pdf',18290,17),(7,'pdf',26,'7_99999999_1500463905949_test_Kopie_16.pdf',18290,17),(8,'pdf',26,'7_99999999_1500463970926_test_Kopie_15.pdf',18290,18),(9,'pdf',26,'7_99999999_1500463970926_test_Kopie_16.pdf',18290,18),(10,'png',26,'7_99999999_1500463970927_Screen_Shot_2017-07-19_at_13.13.21.png',21854,18);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audio_feedback`
--

LOCK TABLES `audio_feedback` WRITE;
/*!40000 ALTER TABLE `audio_feedback` DISABLE KEYS */;
INSERT INTO `audio_feedback` VALUES (1,15232,'',0,'-1037618203343976446_Daniel_23_1533730945046_audio',0,80),(3,3840,'',0,'-1037618203343976446_Daniel_23_1533736289483_audio',9151,82);
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
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_feedback`
--

LOCK TABLES `category_feedback` WRITE;
/*!40000 ALTER TABLE `category_feedback` DISABLE KEYS */;
INSERT INTO `category_feedback` VALUES (1,4,700,NULL,1),(2,4,NULL,'Custom category',1),(3,0,39,'',16),(4,0,39,'',18),(5,0,39,'',19),(6,0,39,'',24),(7,0,39,'',25),(8,0,39,'',26),(9,0,39,'',29),(10,0,39,'',34),(11,0,39,'',35),(12,0,39,'',36),(13,0,39,'',37),(14,0,39,'',38),(15,0,39,'',39);
/*!40000 ALTER TABLE `category_feedback` ENABLE KEYS */;
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
  `region` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  `url` varchar(510) DEFAULT NULL,
  `meta_data` text,
  PRIMARY KEY (`id`),
  KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`),
  CONSTRAINT `FKgjes53m9wrcpnnq0kl898yjul` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `context_information`
--

LOCK TABLES `context_information` WRITE;
/*!40000 ALTER TABLE `context_information` DISABLE KEYS */;
INSERT INTO `context_information` VALUES (1,NULL,'CH',2,'2017-05-31 20:38:51','ZH','2000x1200','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',1,NULL,NULL),(2,NULL,NULL,1,'2017-06-19 00:39:05',NULL,'1413x2560','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(3,NULL,NULL,2,'2017-06-19 13:40:44',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(4,NULL,NULL,2,'2017-06-19 13:41:47',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(5,NULL,NULL,2,'2017-06-19 13:43:44',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(6,NULL,NULL,2,'2017-06-19 13:47:01',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(7,NULL,NULL,2,'2017-06-19 13:50:39',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(8,NULL,NULL,2,'2017-06-19 13:52:16',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(9,NULL,NULL,2,'2017-06-19 13:56:48',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(10,NULL,NULL,2,'2017-06-19 13:57:18',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(11,NULL,NULL,1,'2017-06-19 21:43:05',NULL,'1413x2560','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(12,NULL,NULL,1,'2017-06-19 21:44:01',NULL,'1413x2560','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(13,NULL,NULL,1,'2017-06-19 22:12:45',NULL,'1413x2560','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',NULL,'http://localhost/',NULL),(14,NULL,NULL,2,'2017-07-06 00:52:59',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36',14,'http://localhost/',NULL),(15,NULL,NULL,2,'2017-07-06 00:57:34',NULL,'873x1440','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36',15,'http://localhost/',NULL),(16,NULL,NULL,1,'2017-07-19 13:29:53',NULL,'2133x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36',16,'http://localhost/',NULL),(17,NULL,NULL,1,'2017-07-19 13:31:45',NULL,'2133x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36',17,'http://localhost/',NULL),(18,NULL,NULL,1,'2017-07-19 13:32:50',NULL,'2133x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36',18,'http://localhost/',NULL),(19,NULL,NULL,1,'2017-08-21 14:08:44',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',19,'http://localhost/',NULL),(20,NULL,NULL,1,'2017-08-21 14:14:16',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',20,'http://localhost/',NULL),(21,NULL,NULL,1,'2017-08-21 14:15:53',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',21,'http://localhost/',NULL),(22,NULL,NULL,1,'2017-08-21 14:19:33',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',22,'http://localhost/',NULL),(23,NULL,NULL,1,'2017-08-21 14:23:44',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',23,'http://localhost/',NULL),(24,NULL,NULL,1,'2017-08-21 16:34:01',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',24,'http://localhost/',NULL),(25,NULL,NULL,1,'2017-08-21 16:40:40',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',25,'http://localhost/',NULL),(26,NULL,NULL,1,'2017-08-22 10:10:25',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',26,'http://localhost/',NULL),(27,NULL,NULL,1,'2017-08-22 10:16:06',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',27,'http://localhost/',NULL),(28,NULL,NULL,1,'2017-08-22 10:17:33',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',28,'http://localhost/',NULL),(29,NULL,NULL,1,'2017-08-22 10:32:31',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',29,'http://localhost/',NULL),(30,NULL,NULL,1,'2017-08-22 10:41:43',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',30,'http://localhost/',NULL),(31,NULL,NULL,1,'2017-08-22 10:44:04',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',31,'http://localhost/',NULL),(32,NULL,NULL,1,'2017-08-22 10:48:49',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',32,'http://localhost/',NULL),(33,NULL,NULL,1,'2017-08-22 11:13:04',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',33,'http://localhost/',NULL),(34,NULL,NULL,1,'2017-08-22 11:14:41',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',34,'http://localhost/',NULL),(35,NULL,NULL,1,'2017-08-22 11:19:46',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',35,'http://localhost/',NULL),(36,NULL,NULL,1,'2017-08-22 11:25:32',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',36,'http://localhost/',NULL),(37,NULL,NULL,1,'2017-08-22 11:31:30',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',37,'http://localhost/',NULL),(38,NULL,NULL,1,'2017-08-22 11:33:11',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',38,'http://localhost/',NULL),(39,NULL,NULL,1,'2017-08-23 10:32:36',NULL,'2137x3840','+0200','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36',39,'http://localhost/',NULL),(40,'7.1.1','US',NULL,'2018-07-22 15:04:06',NULL,'1080x1920','GMT',NULL,53,NULL,'Android SDK built for x86'),(42,'7.1.1','US',NULL,'2018-07-22 18:41:53',NULL,'1080x1920','GMT',NULL,55,NULL,'Android SDK built for x86'),(43,'7.1.1','US',NULL,'2018-07-23 18:23:20',NULL,'1080x1920','GMT',NULL,56,NULL,'Android SDK built for x86'),(44,'7.1.1','US',NULL,'2018-07-23 19:55:16',NULL,'1080x1920','GMT',NULL,57,NULL,'Android SDK built for x86'),(45,'7.1.1','US',NULL,'2018-07-25 14:25:27',NULL,'1080x1920','GMT',NULL,58,NULL,'Android SDK built for x86'),(46,'7.1.1','US',NULL,'2018-07-25 14:37:55',NULL,'1080x1920','GMT',NULL,59,NULL,'Android SDK built for x86'),(48,'7.1.1','US',NULL,'2018-07-25 14:49:31',NULL,'1080x1920','GMT',NULL,61,NULL,'Android SDK built for x86'),(49,'7.1.1','US',NULL,'2018-07-25 15:13:43',NULL,'1080x1920','GMT',NULL,62,NULL,'Android SDK built for x86'),(50,'7.1.1','US',NULL,'2018-07-25 20:10:48',NULL,'1080x1920','GMT',NULL,63,NULL,'Android SDK built for x86'),(53,'7.1.1','US',NULL,'2018-08-05 15:17:42',NULL,'1080x1920','GMT',NULL,75,NULL,'Android SDK built for x86'),(54,'7.1.1','US',NULL,'2018-08-05 16:09:53',NULL,'1080x1920','GMT',NULL,76,NULL,'Android SDK built for x86'),(55,'7.1.1','US',NULL,'2018-08-05 16:31:05',NULL,'1080x1920','GMT',NULL,77,NULL,'Android SDK built for x86'),(56,'7.1.1','US',NULL,'2018-08-05 16:38:13',NULL,'1080x1920','GMT',NULL,78,NULL,'Android SDK built for x86'),(57,'7.1.1','US',NULL,'2018-08-08 10:27:53',NULL,'1440x2880','GMT',NULL,79,NULL,'Android SDK built for x86'),(58,'7.1.1','US',NULL,'2018-08-08 12:22:25',NULL,'1440x2880','GMT',NULL,80,NULL,'Android SDK built for x86'),(60,'7.1.1','US',NULL,'2018-08-08 13:51:29',NULL,'1440x2880','GMT',NULL,82,NULL,'Android SDK built for x86'),(62,'4.4.2','US',NULL,'2018-08-19 14:27:37',NULL,'720x1280','GMT',NULL,84,NULL,'Android SDK built for x86'),(63,'4.4.2','US',NULL,'2018-08-19 14:31:12',NULL,'720 x 1280, density: 2.0, densityDpi: 320','GMT',NULL,85,NULL,'MANUFACTURER:unknown;HARDWARE:ranchu;MODEL:Android SDK built for x86;DEVICE:generic_x86;BRAND:generic_x86;TYPE:eng'),(64,'4.4.2','US',NULL,'2018-08-20 15:55:41',NULL,'720 x 1280, Density: 2.0, DPI: 320','GMT',NULL,86,NULL,'MANUFACTURER:unknown;HARDWARE:ranchu;MODEL:Android SDK built for x86;DEVICE:generic_x86;BRAND:generic_x86;TYPE:eng'),(65,'7.1.1','US',NULL,'2018-08-22 13:16:12',NULL,'1440 x 2880, Density: 3.5, DPI: 560','GMT',NULL,87,NULL,'MANUFACTURER:Google;HARDWARE:ranchu;MODEL:Android SDK built for x86;DEVICE:generic_x86;BRAND:google;TYPE:userdebug');
/*!40000 ALTER TABLE `context_information` ENABLE KEYS */;
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
  `user_identification` varchar(255) DEFAULT NULL,
  `is_public` bit(1) DEFAULT b'0',
  `feedback_status` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_status_idx` (`feedback_status`),
  CONSTRAINT `feedback_status` FOREIGN KEY (`feedback_status`) REFERENCES `feedback_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,1,2,'2017-05-31 19:57:54','en','Test feedback','2017-05-31 19:58:05','AUser2#1','',1),(2,1,1,'2017-06-19 12:39:06','en','Feedback',NULL,'AUser2#1','',1),(3,1,1,'2017-06-19 13:40:45','en','Feedback',NULL,'AUser2#3','\0',1),(4,1,1,'2017-06-19 13:41:47','en','Feedback','2018-05-07 22:00:12','AUser2#3','',1),(5,1,1,'2017-06-19 13:43:45','en','Feedback',NULL,'AUser2#3','',1),(6,1,1,'2017-06-19 13:47:02','en','Feedback',NULL,'99999999','',1),(7,1,1,'2017-06-19 13:51:53','en','Feedback',NULL,'AUser2#3','\0',1),(8,1,1,'2017-06-19 13:52:26','en','Feedback',NULL,'99999999','',1),(9,1,1,'2017-06-19 13:56:49','en','Feedback',NULL,'99999999','',1),(10,1,1,'2017-06-19 13:57:18','en','Feedback',NULL,'99999999','',1),(11,1,1,'2017-06-19 21:43:05','en','Feedback',NULL,'99999999','',1),(12,1,1,'2017-06-19 21:44:02','en','Feedback',NULL,'99999999','',1),(13,1,1,'2017-06-19 22:12:45','en','Feedback',NULL,'99999999','',1),(14,1,1,'2017-07-06 12:53:00','en','Feedback',NULL,'99999999','',1),(15,7,7,'2017-07-06 12:57:35','en','Feedback',NULL,'99999999','',1),(16,7,7,'2017-07-19 13:29:54','en','Feedback',NULL,'99999999','',1),(17,7,7,'2017-07-19 13:31:46','en','Feedback',NULL,'99999999','',1),(18,7,7,'2017-07-19 13:32:51','en','Feedback',NULL,'99999999','',1),(19,7,7,'2017-08-21 14:08:45','en','Feedback',NULL,'99999999','',1),(20,7,7,'2017-08-21 14:14:17','en','Feedback',NULL,'99999999','',1),(21,7,7,'2017-08-21 14:15:53','en','Feedback',NULL,'99999999','',1),(22,7,7,'2017-08-21 14:19:34','en','Feedback',NULL,'99999999','',1),(23,7,7,'2017-08-21 14:23:44','en','Feedback',NULL,'99999999','',1),(24,7,7,'2017-08-21 16:34:02','en','Feedback',NULL,'99999999','',1),(25,7,7,'2017-08-21 16:40:41','en','Feedback',NULL,'99999999','',1),(26,7,7,'2017-08-22 10:10:26','en','Feedback',NULL,'99999999','',1),(27,7,7,'2017-08-22 10:16:07','en','Feedback',NULL,'99999999','',1),(28,7,7,'2017-08-22 10:17:34','en','Feedback',NULL,'99999999','',1),(29,7,7,'2017-08-22 10:32:31','en','Feedback',NULL,'99999999','',1),(30,7,7,'2017-08-22 10:41:44','en','Feedback',NULL,'99999999','',1),(31,7,7,'2017-08-22 10:44:05','en','Feedback',NULL,'99999999','',1),(32,7,7,'2017-08-22 10:48:50','en','Feedback',NULL,'99999999','',1),(33,7,7,'2017-08-22 11:13:05','en','Feedback',NULL,'99999999','',1),(34,7,7,'2017-08-22 11:14:41','en','Feedback',NULL,'99999999','',1),(35,7,7,'2017-08-22 11:19:46','en','Feedback','2018-07-04 16:27:40','99999999','\0',3),(36,7,7,'2017-08-22 11:25:33','en','Feedback',NULL,'99999999','\0',1),(37,7,7,'2017-08-22 11:31:31','en','Feedback',NULL,'99999999','',1),(38,7,7,'2017-08-22 11:33:11','en','Feedback',NULL,'99999999','\0',1),(39,7,7,'2017-08-23 10:32:37','en','Feedback',NULL,'99999999','',1),(46,7,1,'2018-07-04 15:56:55','EN','test_feedback',NULL,'huhu','\0',1),(49,7,1,'2018-07-04 16:26:19','EN','test_feedback',NULL,'huhu','\0',1),(50,7,1,'2018-07-22 13:15:53','EN','test_feedback',NULL,'huhu','\0',1),(51,7,1,'2018-07-22 14:12:40','EN','test_feedback',NULL,'huhu','\0',1),(52,7,1,'2018-07-22 14:12:57','EN','test_feedback',NULL,'huhu','\0',1),(53,-1037618203343976446,0,'2018-07-22 15:04:06','en','Please improve stability','2018-08-19 20:55:38','dani#2','\0',1),(55,-1037618203343976446,0,'2018-07-23 17:58:53','en','Add a chat',NULL,'dani#2','',4),(56,-1037618203343976446,0,'2018-07-23 18:23:20','en','Improvement idea',NULL,'dani#2','',4),(57,-1037618203343976446,0,'2018-07-23 19:55:16','en','Battery drain',NULL,'dani#2','',3),(58,-1037618203343976446,0,'2018-07-25 14:25:27','en','Not sure about the new feature','2018-08-19 14:52:53','dani#7','\0',4),(59,-1037618203343976446,0,'2018-07-25 14:37:55','en','Data Usage',NULL,'dani#7','',1),(61,-1037618203343976446,0,'2018-07-25 14:49:31','en','Does not scale',NULL,'dani#7','\0',2),(62,-1037618203343976446,0,'2018-07-25 15:13:43','en','Needs more Tags',NULL,'dani#7','\0',1),(63,-1037618203343976446,0,'2018-07-25 20:10:48','en','Does not scale',NULL,'dani#8','\0',1),(64,7,1,'2018-07-27 16:25:55','EN','test_feedback',NULL,'huhu','\0',1),(68,-1037618203343976446,1,'2018-07-27 16:31:47','EN','DELETE-TEST',NULL,'dani#2','\0',1),(75,-1037618203343976446,0,'2018-08-05 15:17:42','en','Additional feature','2018-08-05 15:52:43','dani#18','',1),(76,-1037618203343976446,0,'2018-08-05 16:09:53','en','Why is it green?',NULL,'dani#18','\0',1),(77,-1037618203343976446,0,'2018-08-05 16:31:05','en','Please add this feature','2018-08-05 16:31:49','dani#18','',4),(78,-1037618203343976446,0,'2018-08-05 16:38:13','en','Found a bug','2018-08-05 16:38:42','dani#18','',1),(79,-1037618203343976446,0,'2018-08-08 10:27:53','en','App is too colorful','2018-08-08 10:28:42','Daniel#23','',2),(80,-1037618203343976446,0,'2018-08-08 12:22:25','en','Audio test','2018-08-17 15:06:22','Daniel#23','',1),(82,-1037618203343976446,0,'2018-08-08 13:51:29','en','Audio test','2018-08-17 15:05:41','Daniel#23','',1),(84,-1037618203343976446,0,'2018-08-19 14:27:37','en','Metadata Test',NULL,'Marco#33','\0',1),(85,-1037618203343976446,0,'2018-08-19 14:31:12','en','Metadata',NULL,'Marco#34','\0',1),(86,-1037618203343976446,0,'2018-08-20 15:55:41','en','Bubbletest',NULL,'Marco#40','\0',1),(87,-1037618203343976446,0,'2018-08-22 13:16:12','en','Needs a better GUI','2018-08-22 13:16:26','Daniel#23','',1);
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_report`
--

DROP TABLE IF EXISTS `feedback_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reporter_id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `reason` text,
  PRIMARY KEY (`id`),
  KEY `reporter_id` (`reporter_id`),
  KEY `feedback_id` (`feedback_id`),
  CONSTRAINT `feedback_iddf` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `reporter_id` FOREIGN KEY (`reporter_id`) REFERENCES `android_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_report`
--

LOCK TABLES `feedback_report` WRITE;
/*!40000 ALTER TABLE `feedback_report` DISABLE KEYS */;
INSERT INTO `feedback_report` VALUES (1,8,1,'This is bullshit!'),(2,18,53,'This is offensive');
/*!40000 ALTER TABLE `feedback_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_response`
--

DROP TABLE IF EXISTS `feedback_response`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_response` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `feedback_id` (`feedback_id`),
  CONSTRAINT `feedback_id` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `android_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_response`
--

LOCK TABLES `feedback_response` WRITE;
/*!40000 ALTER TABLE `feedback_response` DISABLE KEYS */;
INSERT INTO `feedback_response` VALUES (1,5,1,'2018-07-04 18:28:29',NULL,'This is a good idea. I liek'),(2,18,53,'2018-07-27 10:59:51',NULL,'I like this idea very much!'),(3,16,55,'2018-07-27 12:27:49',NULL,'Response test 123'),(5,16,59,'2018-07-28 15:51:34',NULL,'Here, my response'),(6,16,59,'2018-07-28 15:51:49',NULL,'accidentaly another responnsse'),(7,16,56,'2018-07-30 21:30:40',NULL,'responses test'),(20,28,58,'2018-08-03 13:40:38',NULL,'some other response'),(23,29,53,'2018-08-05 16:41:17',NULL,'No, this is not a good idea'),(24,29,59,'2018-08-05 16:42:16',NULL,'This is amazing!'),(25,25,79,'2018-08-08 10:35:53',NULL,'I agree!'),(26,17,79,'2018-08-08 10:47:53',NULL,'It\'s hideous!'),(27,34,53,'2018-08-08 12:00:20',NULL,'Yes please do this'),(28,34,59,'2018-08-08 12:01:20',NULL,'The usage is insane!'),(29,35,59,'2018-08-17 09:29:45',NULL,'Response scaling test'),(30,35,53,'2018-08-17 10:46:27',NULL,'This is a test Feedback'),(33,48,77,'2018-08-19 19:22:31',NULL,'My \nresponse\nis \nthis'),(34,48,80,'2018-08-19 19:34:24',NULL,'Cooles Audio'),(35,49,53,'2018-08-19 20:00:25',NULL,'Developer here!'),(36,49,53,'2018-08-19 20:01:58',NULL,'          ');
/*!40000 ALTER TABLE `feedback_response` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_status`
--

DROP TABLE IF EXISTS `feedback_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_status`
--

LOCK TABLES `feedback_status` WRITE;
/*!40000 ALTER TABLE `feedback_status` DISABLE KEYS */;
INSERT INTO `feedback_status` VALUES (1,'OPEN'),(2,'IN_PROGRESS'),(3,'CLOSED'),(4,'REJECTED'),(5,'DUPLICATE');
/*!40000 ALTER TABLE `feedback_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_tag`
--

DROP TABLE IF EXISTS `feedback_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feedback_id` bigint(20) NOT NULL,
  `tag` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_vote` (`feedback_id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_tag`
--

LOCK TABLES `feedback_tag` WRITE;
/*!40000 ALTER TABLE `feedback_tag` DISABLE KEYS */;
INSERT INTO `feedback_tag` VALUES (1,37,'color'),(2,37,'gui'),(3,38,'bug'),(4,39,'bug'),(5,36,'improvement'),(6,36,'performance'),(7,35,'gui'),(8,35,'usability'),(9,35,'improvement'),(10,34,'improvement'),(11,34,'usability'),(13,2,'tag'),(28,50,'tag'),(29,50,'usability'),(30,51,'tag'),(31,51,'usability'),(32,52,'tag'),(33,52,'usability'),(35,55,'improvement'),(36,55,'repo'),(37,55,'orchestrator'),(39,59,'usability'),(40,59,'orchestrator'),(41,59,'colors'),(43,62,'improvement'),(44,62,'performance'),(45,64,'battery'),(46,64,'audio'),(53,68,'color'),(54,68,'improvement'),(65,75,'compatibility'),(66,76,'compatibility'),(67,77,'cpu'),(68,78,'cpu'),(69,79,'colors'),(70,79,'ugly'),(71,80,'audio'),(73,82,'audio'),(75,84,'tag'),(76,85,'metadata'),(77,86,'bubble'),(78,87,'ugly'),(79,87,'gui');
/*!40000 ALTER TABLE `feedback_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_vote`
--

DROP TABLE IF EXISTS `feedback_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_vote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `voter_user_id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `vote` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_id` (`feedback_id`),
  KEY `voter_user_id` (`voter_user_id`),
  CONSTRAINT `feedback_vote_ibfk_1` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`),
  CONSTRAINT `feedback_vote_ibfk_2` FOREIGN KEY (`voter_user_id`) REFERENCES `android_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_vote`
--

LOCK TABLES `feedback_vote` WRITE;
/*!40000 ALTER TABLE `feedback_vote` DISABLE KEYS */;
INSERT INTO `feedback_vote` VALUES (1,7,1,-1),(2,7,2,-1),(3,5,4,10),(4,5,5,-1),(5,1,1,1),(6,8,1,1),(7,9,1,1),(8,9,4,-1),(9,9,4,5),(10,18,53,10),(11,18,55,-10),(12,16,53,-1),(13,20,59,12),(15,16,56,1),(16,28,58,1),(17,20,53,23),(18,29,53,-1),(19,29,59,1),(20,20,57,9),(21,20,75,-2),(22,20,77,3),(23,20,79,11),(24,20,78,5),(25,34,53,1),(26,34,59,1),(27,34,55,-1),(28,35,53,1),(29,35,56,-1),(30,35,59,-1),(31,35,57,1),(32,35,80,1),(34,35,82,1),(35,41,59,-5),(36,41,82,-700),(37,41,53,-2),(39,47,53,1),(40,48,53,-1),(41,48,79,-1),(42,48,77,-1),(44,48,80,1),(45,48,59,-1),(46,49,53,-11),(47,49,59,11),(48,50,59,12),(49,50,53,282),(50,50,56,-11),(51,50,82,-300);
/*!40000 ALTER TABLE `feedback_vote` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`id`),
  KEY `FKa5merp6x61nokyyvx17snw5bb` (`feedback_id`),
  CONSTRAINT `FKa5merp6x61nokyyvx17snw5bb` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_feedback`
--

LOCK TABLES `rating_feedback` WRITE;
/*!40000 ALTER TABLE `rating_feedback` DISABLE KEYS */;
INSERT INTO `rating_feedback` VALUES (1,3,5,'Please rate',1),(2,24,5,'Rate Grid Data UI',15),(3,24,5,'Rate Grid Data UI',16),(4,24,0,'Rate Grid Data UI',17),(5,24,5,'Rate Grid Data UI',18),(6,24,5,'Rate Grid Data UI',19),(7,24,0,'Rate Grid Data UI',20),(8,24,0,'Rate Grid Data UI',21),(9,24,0,'Rate Grid Data UI',22),(10,24,0,'Rate Grid Data UI',23),(11,24,5,'Rate Grid Data UI',24),(12,24,5,'Rate Grid Data UI',25),(13,24,5,'Rate Grid Data UI',26),(14,24,0,'Rate Grid Data UI',27),(15,24,0,'Rate Grid Data UI',28),(16,24,5,'Rate Grid Data UI',29),(17,24,0,'Rate Grid Data UI',30),(18,24,0,'Rate Grid Data UI',31),(19,24,0,'Rate Grid Data UI',32),(20,24,0,'Rate Grid Data UI',33),(21,24,5,'Rate Grid Data UI',34),(22,24,5,'Rate Grid Data UI',35),(23,24,5,'Rate Grid Data UI',36),(24,24,5,'Rate Grid Data UI',37),(25,24,5,'Rate Grid Data UI',38),(26,24,1,'Rate Grid Data UI',39),(86,5,4,'Test rating',46),(87,6,5,'Test rating 2',46),(92,5,4,'Test rating',49),(93,6,5,'Test rating 2',49),(94,5,4,'Test rating',50),(95,6,5,'Test rating 2',50),(96,5,4,'Test rating',51),(97,6,5,'Test rating 2',51),(98,5,4,'Test rating',52),(99,6,5,'Test rating 2',52),(100,0,0,NULL,53),(102,0,0,NULL,55),(103,0,0,NULL,56),(104,0,0,NULL,57),(105,0,0,NULL,58),(106,0,0,NULL,59),(108,0,0,NULL,61),(109,0,0,NULL,62),(110,0,0,NULL,63),(111,5,4,'Test rating',64),(112,6,5,'Test rating 2',64),(119,5,4,'Test rating',68),(120,6,5,'Test rating 2',68),(131,0,0,NULL,75),(132,0,0,NULL,76),(133,0,0,NULL,77),(134,0,0,NULL,78),(135,0,0,NULL,79),(136,0,0,NULL,80),(138,0,0,NULL,82),(140,0,4,NULL,84),(141,0,3,NULL,85),(142,0,4,NULL,86),(143,0,3,NULL,87);
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
  PRIMARY KEY (`id`),
  KEY `FKsekb4tg5bivjepodpeyt7eplc` (`feedback_id`),
  CONSTRAINT `FKsekb4tg5bivjepodpeyt7eplc` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screenshot_feedback`
--

LOCK TABLES `screenshot_feedback` WRITE;
/*!40000 ALTER TABLE `screenshot_feedback` DISABLE KEYS */;
INSERT INTO `screenshot_feedback` VALUES (1,NULL,6,'screenshot_1_example.png',805701,1),(2,'png',2,'1_99999999_blob',70728,NULL),(3,'png',2,'1_99999999_blob',40192,NULL),(4,'png',2,'1_99999999_blob',287149,NULL),(5,'png',2,'1_99999999_blob.png',287149,NULL),(6,'png',2,'1_99999999_blob.png',40020,NULL),(7,'png',2,'1_99999999_blob.png',68611,NULL),(8,'png',2,'1_99999999_blob.png',287140,14),(9,'png',25,'7_99999999_blob.png',287140,15),(10,'',25,'7_99999999_1500463793775_blob',47688,16),(11,'',25,'7_99999999_1500463970927_blob',47688,18),(12,'',25,'7_99999999_1503389425719_blob',57250,26),(13,'',25,'7_99999999_1503390751128_blob',58567,29),(14,'',25,'7_99999999_1503393281051_blob',57250,34),(15,'',25,'7_99999999_1503393586198_blob',57250,35),(16,'',25,'7_99999999_1503393932655_blob',57250,36),(17,'',25,'7_99999999_1503394290833_blob',57250,37),(18,'',25,'7_99999999_1503394391177_blob',57250,38),(38,'',0,'-1037618203343976446_dani_2_1532284913586_screenshot',45813,55),(39,'',0,'-1037618203343976446_dani_2_1532370200476_screenshot',45778,56),(40,'',0,'-1037618203343976446_dani_2_1532375716875_screenshot',48789,57),(41,'',0,'-1037618203343976446_dani_7_1532528727147_screenshot',45854,58),(42,'',0,'-1037618203343976446_dani_7_1532529475679_screenshot',46830,59),(44,'',0,'-1037618203343976446_dani_7_1532530171212_screenshot',46838,61),(45,'',0,'-1037618203343976446_dani_7_1532531623723_screenshot',48722,62),(46,'',0,'-1037618203343976446_dani_8_1532549448976_screenshot',46889,63),(49,'',0,'-1037618203343976446_dani_18_1533482262942_screenshot',47389,75),(50,'',0,'-1037618203343976446_dani_18_1533485393648_screenshot',48132,76),(51,'',0,'-1037618203343976446_dani_18_1533486665266_screenshot',48556,77),(52,'',0,'-1037618203343976446_dani_18_1533487093731_screenshot',47035,78),(53,'',0,'-1037618203343976446_Daniel_22_1533724073841_screenshot',59413,79),(54,'',0,'-1037618203343976446_Daniel_23_1533730945048_screenshot',62826,80),(56,'',0,'-1037618203343976446_Daniel_23_1533736289483_screenshot',61875,82),(58,'',0,'-1037618203343976446_Marco_33_1534688857360_screenshot',31304,84),(59,'',0,'-1037618203343976446_Marco_34_1534689072672_screenshot',29108,85),(60,'',0,'-1037618203343976446_Marco_40_1534780541509_screenshot',31880,86),(61,'',0,'-1037618203343976446_Daniel_23_1534943772358_screenshot',62154,87);
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
  `feedback_email_receivers` varchar(510) NOT NULL,
  `kafka_topic_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=425 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
INSERT INTO `setting` VALUES (1,1,'ronnieschaniel@gmail.com',NULL),(2,7,'ronnieschaniel@gmail.com',NULL);
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
  PRIMARY KEY (`id`),
  KEY `FKho80mb8i1oa2guwbceli8s974` (`api_user_id`),
  KEY `FKgcq19tbyg1cqwehj46h5mx4we` (`feedback_id`),
  CONSTRAINT `FKgcq19tbyg1cqwehj46h5mx4we` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`),
  CONSTRAINT `FKho80mb8i1oa2guwbceli8s974` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,1,1,1);
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
-- Table structure for table `status_options`
--

DROP TABLE IF EXISTS `status_options`;
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

LOCK TABLES `status_options` WRITE;
/*!40000 ALTER TABLE `status_options` DISABLE KEYS */;
/*!40000 ALTER TABLE `status_options` ENABLE KEYS */;
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
  PRIMARY KEY (`id`),
  KEY `FKki0o2ssgvt18agpq19o98h8ir` (`screenshot_feedback_id`),
  CONSTRAINT `FKki0o2ssgvt18agpq19o98h8ir` FOREIGN KEY (`screenshot_feedback_id`) REFERENCES `file_feedback` (`id`) ON DELETE CASCADE
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
  `text` text,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtidcgd0wra4sxqlawcp4li155` (`feedback_id`),
  CONSTRAINT `FKtidcgd0wra4sxqlawcp4li155` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=397 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_feedback`
--

LOCK TABLES `text_feedback` WRITE;
/*!40000 ALTER TABLE `text_feedback` DISABLE KEYS */;
INSERT INTO `text_feedback` VALUES (1,1,'My text feedback',1),(2,1,'test',NULL),(3,1,'test',NULL),(4,1,'',NULL),(5,1,'',NULL),(6,1,'',NULL),(7,1,'',NULL),(8,1,'test with screenshot',NULL),(9,1,'',NULL),(10,1,'test',NULL),(11,1,'test',NULL),(12,1,'',NULL),(13,1,'test',NULL),(14,1,'Feedback to the new backend',14),(15,23,'Feedback Siemens test data grid',15),(16,23,'Test files',16),(17,23,'',17),(18,23,'test file 3\n1 screenshot\n3 attachments ',18),(19,23,'Feedback should be sent to repository. After that it is sent to the kafka topic raw-feedback. Then analysed. And finally it is stored in the kafka topic analysed-feedback. After that it is ready to be traced.',19),(20,23,'',20),(21,23,'',21),(22,23,'',22),(23,23,'',23),(24,23,'This feedback is sent to the repository, then to kafka raw-feedback. After that it is analysed and sent to analysed-feedback. Then it would be ready to be traced onto requirements stored in a JIRA.',24),(25,23,'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',25),(26,23,'Test feedback that should get stored in dl feedback storage. ',26),(27,23,'',27),(28,23,'',28),(29,23,'Sample feedback. Stored in the dl',29),(30,23,'',30),(31,23,'',31),(32,23,'',32),(33,23,'',33),(34,23,'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter. ',34),(35,23,'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.',35),(36,23,'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.',36),(37,23,'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.',37),(38,23,'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.',38),(39,23,'The zoom function has a problem I think. Sometimes the screenshot disappears and I get a white box.',39),(355,1,'This is the feedback text',46),(358,1,'This is the feedback text',49),(359,1,'This is the feedback text',50),(360,1,'This is the feedback text',51),(361,1,'This is the feedback text',52),(362,0,'The new repo is amazing',53),(364,0,'Give me screenshot',55),(365,0,'Looks nice!',56),(366,0,'Testsds',57),(367,0,'This is a feedback',58),(368,0,'Look at these tags',59),(370,0,'Test',61),(371,0,'Moar Tags plz!!',62),(372,0,'Test',63),(373,1,'This is the feedback text',64),(377,1,'This is the feedback text',68),(384,0,'Cxvbcxb',75),(385,0,'Derssr',76),(386,0,'Dggsd',77),(387,0,'Asdg',78),(388,0,'The colors do not match and are tough to look at',79),(389,0,'With audio',80),(391,0,'Huhu',82),(393,0,'Metadata test',84),(394,0,'Metadata testt',85),(395,0,'This is a Bubbletest',86),(396,0,'This App could be prettier',87);
/*!40000 ALTER TABLE `text_feedback` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-22 15:59:58
