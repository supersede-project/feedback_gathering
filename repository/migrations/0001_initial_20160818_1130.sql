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
-- Table structure for table `attachment_feedbacks`
--

DROP TABLE IF EXISTS `attachment_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment_feedbacks` (
  `id` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `file_extension` varchar(10) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
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
  `id` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `category_feedbacks_id` int(11) NOT NULL,
  `category_types_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_choices_choice_feedbacks1_idx` (`category_feedbacks_id`),
  KEY `fk_categories_category_types1_idx` (`category_types_id`),
  CONSTRAINT `fk_categories_category_types1` FOREIGN KEY (`category_types_id`) REFERENCES `feedback_orchestrator`.`category_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_choices_category_feedbacks1` FOREIGN KEY (`category_feedbacks_id`) REFERENCES `category_feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_feedbacks`
--

DROP TABLE IF EXISTS `category_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_feedbacks` (
  `id` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
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
-- Table structure for table `category_types`
--

DROP TABLE IF EXISTS `category_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_types` (
  `id` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `language` varchar(3) NOT NULL,
  `text` varchar(255) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_types`
--

LOCK TABLES `category_types` WRITE;
/*!40000 ALTER TABLE `category_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `context_informations`
--

DROP TABLE IF EXISTS `context_informations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context_informations` (
  `id` int(11) NOT NULL,
  `resolution` varchar(255) DEFAULT NULL COMMENT '		',
  `user_agent` varchar(255) DEFAULT NULL,
  `operating_system` varchar(255) DEFAULT NULL,
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
  `id` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
  `comment` text NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
  `config_version` decimal(2,0) NOT NULL,
  `text` text,
  `user_identification` varchar(255) NOT NULL,
  `language` varchar(3) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `application_id` int(11) NOT NULL,
  `context_informations_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_feedbacks_application1_idx` (`application_id`),
  KEY `fk_feedbacks_context_informations1_idx` (`context_informations_id`),
  CONSTRAINT `fk_feedbacks_context_informations1` FOREIGN KEY (`context_informations_id`) REFERENCES `context_informations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;



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
  PRIMARY KEY (`id`),
  KEY `fk_rating_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_rating_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;


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
  PRIMARY KEY (`id`),
  KEY `fk_screenshot_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_screenshot_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-18 11:20:49
