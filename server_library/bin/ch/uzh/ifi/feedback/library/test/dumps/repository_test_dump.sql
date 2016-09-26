CREATE DATABASE  IF NOT EXISTS `feedback_repository_test` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `feedback_repository_test`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: feedback_repository_test
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
  CONSTRAINT `fk_audio_feedback0` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment_feedbacks`
--

LOCK TABLES `attachment_feedbacks` WRITE;
/*!40000 ALTER TABLE `attachment_feedbacks` DISABLE KEYS */;
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
  CONSTRAINT `fk_audio_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audio_feedbacks`
--

LOCK TABLES `audio_feedbacks` WRITE;
/*!40000 ALTER TABLE `audio_feedbacks` DISABLE KEYS */;
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
  CONSTRAINT `fk_audio_feedback00` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_feedbacks`
--

LOCK TABLES `category_feedbacks` WRITE;
/*!40000 ALTER TABLE `category_feedbacks` DISABLE KEYS */;
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
  CONSTRAINT `fk_feedback_comments` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (54,'Feedback JUnit','u8102390','EN','2016-09-02 13:03:21',NULL,1,NULL,1),(55,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:10',NULL,1,NULL,1),(56,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:47',NULL,1,NULL,1),(57,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:55',NULL,1,NULL,1),(58,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:55',NULL,1,NULL,1),(59,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:56',NULL,1,NULL,1),(60,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:56',NULL,1,NULL,1),(61,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:57',NULL,1,NULL,1),(62,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:57',NULL,1,NULL,1),(63,'Feedback JUnit','u8102390','EN','2016-09-02 13:22:58',NULL,1,NULL,1);
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
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rating_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_rating_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_feedbacks`
--

LOCK TABLES `rating_feedbacks` WRITE;
/*!40000 ALTER TABLE `rating_feedbacks` DISABLE KEYS */;
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
  CONSTRAINT `fk_screenshot_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screenshot_feedbacks`
--

LOCK TABLES `screenshot_feedbacks` WRITE;
/*!40000 ALTER TABLE `screenshot_feedbacks` DISABLE KEYS */;
/*!40000 ALTER TABLE `screenshot_feedbacks` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_feedbacks`
--

LOCK TABLES `text_feedbacks` WRITE;
/*!40000 ALTER TABLE `text_feedbacks` DISABLE KEYS */;
INSERT INTO `text_feedbacks` VALUES (41,'This is the feedback text',1,54),(42,'This is the feedback text',1,55),(43,'This is the feedback text',1,56),(44,'This is the feedback text',1,57),(45,'This is the feedback text',1,58),(46,'This is the feedback text',1,59),(47,'This is the feedback text',1,60),(48,'This is the feedback text',1,61),(49,'This is the feedback text',1,62),(50,'This is the feedback text',1,63);
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

-- Dump completed on 2016-09-15 16:24:02
