CREATE DATABASE  IF NOT EXISTS `feedback_repository` /*!40100 DEFAULT CHARACTER SET big5 */;
USE `feedback_repository`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: feedback_repository
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

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
-- Table structure for table `api_users`
--

DROP TABLE IF EXISTS `api_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_users`
--

LOCK TABLES `api_users` WRITE;
/*!40000 ALTER TABLE `api_users` DISABLE KEYS */;
INSERT INTO `api_users` VALUES (1,'api_user','password');
/*!40000 ALTER TABLE `api_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment_feedbacks`
--

DROP TABLE IF EXISTS `attachment_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `file_extension` varchar(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `part` varchar(255) DEFAULT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_audio_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_audio_feedback0` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment_feedbacks`
--

LOCK TABLES `attachment_feedbacks` WRITE;
/*!40000 ALTER TABLE `attachment_feedbacks` DISABLE KEYS */;
INSERT INTO `attachment_feedbacks` VALUES (1,67,'attachments/68_1475502771848.txt',68,'txt','attachment1',NULL,10),(2,67,'attachments/93_1475502771848.txt',93,'txt','attachment2',NULL,10),(3,68,'attachments/68_1475503580359.txt',68,'txt','attachment1',NULL,10),(4,68,'attachments/93_1475503580361.txt',93,'txt','attachment2',NULL,10);
/*!40000 ALTER TABLE `attachment_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audio_feedbacks`
--

DROP TABLE IF EXISTS `audio_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audio_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  `part` varchar(255) DEFAULT NULL,
  `file_extension` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_audio_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_audio_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audio_feedbacks`
--

LOCK TABLES `audio_feedbacks` WRITE;
/*!40000 ALTER TABLE `audio_feedbacks` DISABLE KEYS */;
INSERT INTO `audio_feedbacks` VALUES (1,67,'audios/1430174_1475502771840.mp3',1430174,'audio1',0,11,NULL,'mp3'),(2,68,'audios/1430174_1475503580354.mp3',1430174,'audio1',0,11,NULL,'mp3');
/*!40000 ALTER TABLE `audio_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_feedbacks`
--

DROP TABLE IF EXISTS `category_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_id` int(11) NOT NULL,
  `parameter_id` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_audio_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_audio_feedback00` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_feedbacks`
--

LOCK TABLES `category_feedbacks` WRITE;
/*!40000 ALTER TABLE `category_feedbacks` DISABLE KEYS */;
INSERT INTO `category_feedbacks` VALUES (1,67,12,'sample text'),(2,67,14,NULL),(3,68,12,'sample text'),(4,68,14,NULL);
/*!40000 ALTER TABLE `category_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `context_informations`
--

DROP TABLE IF EXISTS `context_informations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context_informations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resolution` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `android_version` varchar(45) DEFAULT NULL,
  `local_time` time DEFAULT NULL,
  `time_zone` varchar(45) DEFAULT NULL,
  `device_pixel_ratio` varchar(11) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `context_informations`
--

LOCK TABLES `context_informations` WRITE;
/*!40000 ALTER TABLE `context_informations` DISABLE KEYS */;
/*!40000 ALTER TABLE `context_informations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_comments`
--

DROP TABLE IF EXISTS `feedback_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_id` int(11) NOT NULL,
  `comment` text NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_feedback_comments_idx` (`feedback_id`),
  CONSTRAINT `fk_feedback_comments` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_comments`
--

LOCK TABLES `feedback_comments` WRITE;
/*!40000 ALTER TABLE `feedback_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedbacks`
--

DROP TABLE IF EXISTS `feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `user_identification` varchar(255) NOT NULL,
  `language` varchar(3) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `application_id` int(11) NOT NULL,
  `context_informations_id` int(11) DEFAULT NULL,
  `configuration_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_feedbacks_application1_idx` (`application_id`),
  KEY `fk_feedbacks_context_informations1_idx` (`context_informations_id`),
  CONSTRAINT `fk_feedbacks_context_informations1` FOREIGN KEY (`context_informations_id`) REFERENCES `context_informations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (54,'Feedback JUnit','u8102390','EN','2016-09-02 13:03:21',NULL,1,NULL,1),(55,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:10',NULL,1,NULL,1),(56,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:47',NULL,1,NULL,1),(58,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:55',NULL,1,NULL,1),(59,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:56',NULL,1,NULL,1),(60,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:56',NULL,1,NULL,1),(61,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:57',NULL,1,NULL,1),(62,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:57',NULL,1,NULL,1),(63,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:58',NULL,1,NULL,1),(64,'Feedback JUnit','u8102390','EN','2016-09-04 13:30:13',NULL,1,NULL,1),(65,'Feedback JUnit','u8102390','EN','2016-09-04 13:37:49',NULL,1,NULL,1),(66,'Feedback JUnit','u8102390','EN','2016-09-04 13:38:41',NULL,1,NULL,1),(67,'test_feedback','u8102390','EN','2016-10-03 13:52:51',NULL,1,NULL,1),(68,'test_feedback','u8102390','EN','2016-10-03 14:06:20',NULL,1,NULL,1);
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating_feedbacks`
--

DROP TABLE IF EXISTS `rating_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rating` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rating_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_rating_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_feedbacks`
--

LOCK TABLES `rating_feedbacks` WRITE;
/*!40000 ALTER TABLE `rating_feedbacks` DISABLE KEYS */;
INSERT INTO `rating_feedbacks` VALUES (1,4,67,5,'Test rating'),(2,5,67,6,'Test rating 2'),(3,4,68,5,'Test rating'),(4,5,68,6,'Test rating 2');
/*!40000 ALTER TABLE `rating_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screenshot_feedbacks`
--

DROP TABLE IF EXISTS `screenshot_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screenshot_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  `part` varchar(255) DEFAULT NULL,
  `file_extension` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_screenshot_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_screenshot_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screenshot_feedbacks`
--

LOCK TABLES `screenshot_feedbacks` WRITE;
/*!40000 ALTER TABLE `screenshot_feedbacks` DISABLE KEYS */;
INSERT INTO `screenshot_feedbacks` VALUES (1,67,'screenshots/7185_1475502771834.jpeg',7185,'annotatedImage1',9,NULL,'jpeg'),(2,67,'screenshots/13569_1475502771839.jpeg',13569,'annotatedImage2',9,NULL,'jpeg'),(3,68,'screenshots/7185_1475503580352.jpeg',7185,'annotatedImage1',9,NULL,'jpeg'),(4,68,'screenshots/13569_1475503580354.jpeg',13569,'annotatedImage2',9,NULL,'jpeg');
/*!40000 ALTER TABLE `screenshot_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_annotations`
--

DROP TABLE IF EXISTS `text_annotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_annotations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `screenshot_feedbacks_id` int(11) NOT NULL,
  `reference_number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_text_annotations_1_idx` (`screenshot_feedbacks_id`),
  CONSTRAINT `fk_text_annotations_1` FOREIGN KEY (`screenshot_feedbacks_id`) REFERENCES `screenshot_feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_annotations`
--

LOCK TABLES `text_annotations` WRITE;
/*!40000 ALTER TABLE `text_annotations` DISABLE KEYS */;
INSERT INTO `text_annotations` VALUES (1,'Too big',1,1),(2,'Unnecessary info',1,2),(3,'Too big',3,1),(4,'Unnecessary info',3,2);
/*!40000 ALTER TABLE `text_annotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_feedbacks`
--

DROP TABLE IF EXISTS `text_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_feedbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  `feedback_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_id` (`feedback_id`),
  CONSTRAINT `feedback_id_foreign_key` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_feedbacks`
--

LOCK TABLES `text_feedbacks` WRITE;
/*!40000 ALTER TABLE `text_feedbacks` DISABLE KEYS */;
INSERT INTO `text_feedbacks` VALUES (41,'This is the feedback text',1,54),(42,'This is the feedback text',1,55),(43,'This is the feedback text',1,56),(45,'This is the feedback text',1,58),(46,'This is the feedback text',1,59),(47,'This is the feedback text',1,60),(48,'This is the feedback text',1,61),(49,'This is the feedback text',1,62),(50,'This is the feedback text',1,63),(51,'This is the feedback text',1,64),(52,'This is the feedback text',1,65),(53,'This is the feedback text',1,66),(54,'This is the feedback text',1,67),(55,'This is the feedback text',1,68);
/*!40000 ALTER TABLE `text_feedbacks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-05 15:18:21
