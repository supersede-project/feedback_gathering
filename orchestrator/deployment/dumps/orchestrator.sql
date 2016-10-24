CREATE DATABASE  IF NOT EXISTS `feedback_orchestrator` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `feedback_orchestrator`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: feedback_orchestrator
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
-- Table structure for table `api_user_permissions`
--

DROP TABLE IF EXISTS `api_user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_api_user_permissions_1_idx` (`user_id`),
  CONSTRAINT `fk_api_user_permissions_1` FOREIGN KEY (`user_id`) REFERENCES `api_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_user_permissions`
--

LOCK TABLES `api_user_permissions` WRITE;
/*!40000 ALTER TABLE `api_user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_users`
--

DROP TABLE IF EXISTS `api_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_users`
--

LOCK TABLES `api_users` WRITE;
/*!40000 ALTER TABLE `api_users` DISABLE KEYS */;
INSERT INTO `api_users` VALUES (1,'api_user','sha1:64000:18:7o4NKp8/is9NHCydnIEl0t1RmvT/deam:SgWwtvBSh29qXvAth0vruOik','ADMIN');
/*!40000 ALTER TABLE `api_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applications1`
--

DROP TABLE IF EXISTS `applications1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applications1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications1`
--

LOCK TABLES `applications1` WRITE;
/*!40000 ALTER TABLE `applications1` DISABLE KEYS */;
INSERT INTO `applications1` VALUES (3),(4);
/*!40000 ALTER TABLE `applications1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applications1_history`
--

DROP TABLE IF EXISTS `applications1_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applications1_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `state` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `general_configurations_id` int(11) DEFAULT NULL,
  `applications1_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_applications_1_idx` (`general_configurations_id`),
  KEY `fk_applications_history_2_idx` (`applications1_id`),
  CONSTRAINT `fk_applications1_history_1` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_applications1_history_2` FOREIGN KEY (`applications1_id`) REFERENCES `applications1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications1_history`
--

LOCK TABLES `applications1_history` WRITE;
/*!40000 ALTER TABLE `applications1_history` DISABLE KEYS */;
INSERT INTO `applications1_history` VALUES (3,'Test Website 20',1,'2016-09-24 13:55:06',6,3,'\0'),(4,'Test Website 21',0,'2016-09-24 14:02:10',6,3,'\0'),(5,'Test Website 20',1,'2016-10-05 12:50:56',10,4,'\0');
/*!40000 ALTER TABLE `applications1_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurations`
--

DROP TABLE IF EXISTS `configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations`
--

LOCK TABLES `configurations` WRITE;
/*!40000 ALTER TABLE `configurations` DISABLE KEYS */;
INSERT INTO `configurations` VALUES (4),(5),(6),(7),(8),(9);
/*!40000 ALTER TABLE `configurations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurations_history`
--

DROP TABLE IF EXISTS `configurations_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurations_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `applications_id` int(11) NOT NULL,
  `user_groups_id` int(11) DEFAULT NULL,
  `type` enum('PUSH','PULL') NOT NULL,
  `general_configurations_id` int(11) DEFAULT NULL,
  `configurations_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_configurations_1_idx` (`applications_id`),
  KEY `fk_configurations_2_idx` (`user_groups_id`),
  KEY `fk_configurations_3_idx` (`general_configurations_id`),
  KEY `fk_configurations_history_1_idx` (`configurations_id`),
  CONSTRAINT `fk_configurations_history_1` FOREIGN KEY (`configurations_id`) REFERENCES `configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_2` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_3` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_4` FOREIGN KEY (`applications_id`) REFERENCES `applications1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations_history`
--

LOCK TABLES `configurations_history` WRITE;
/*!40000 ALTER TABLE `configurations_history` DISABLE KEYS */;
INSERT INTO `configurations_history` VALUES (3,NULL,'2016-09-24 13:55:06',3,1,'PUSH',7,4,'\0'),(4,NULL,'2016-09-24 13:55:06',3,1,'PULL',8,5,'\0'),(5,NULL,'2016-09-24 13:55:06',3,1,'PULL',9,6,'\0'),(6,NULL,'2016-10-05 12:50:56',4,1,'PUSH',11,7,'\0'),(7,NULL,'2016-10-05 12:50:56',4,1,'PULL',12,8,'\0'),(8,NULL,'2016-10-05 12:50:56',4,1,'PULL',13,9,'\0');
/*!40000 ALTER TABLE `configurations_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurations_mechanisms`
--

DROP TABLE IF EXISTS `configurations_mechanisms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurations_mechanisms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations_mechanisms`
--

LOCK TABLES `configurations_mechanisms` WRITE;
/*!40000 ALTER TABLE `configurations_mechanisms` DISABLE KEYS */;
/*!40000 ALTER TABLE `configurations_mechanisms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurations_mechanisms_history`
--

DROP TABLE IF EXISTS `configurations_mechanisms_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurations_mechanisms_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `configurations_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `order` int(11) NOT NULL,
  `can_be_activated` bit(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mechanisms_history_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_configurations_mechanisms_1_idx` (`configurations_id`),
  KEY `fk_configurations_mechanisms_history_3_idx` (`mechanisms_history_id`),
  CONSTRAINT `fk_configurations_mechanisms_history_1` FOREIGN KEY (`configurations_id`) REFERENCES `configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_mechanisms_history_3` FOREIGN KEY (`mechanisms_history_id`) REFERENCES `mechanisms_history` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations_mechanisms_history`
--

LOCK TABLES `configurations_mechanisms_history` WRITE;
/*!40000 ALTER TABLE `configurations_mechanisms_history` DISABLE KEYS */;
INSERT INTO `configurations_mechanisms_history` VALUES (5,4,'',1,'\0','2016-09-24 13:55:06',5),(6,4,'\0',2,'\0','2016-09-24 13:55:06',6),(7,4,'',3,'\0','2016-09-24 13:55:06',7),(8,4,'',4,'\0','2016-09-24 13:55:06',8),(9,5,'',1,'\0','2016-09-24 13:55:06',9),(10,5,'',4,'\0','2016-09-24 13:55:06',10),(11,5,'',3,'\0','2016-09-24 13:55:06',11),(12,6,'',1,'\0','2016-09-24 13:55:06',12),(13,6,'',2,'\0','2016-09-24 13:55:06',13),(14,4,'',1,'\0','2016-09-24 14:02:10',14),(15,4,'\0',2,'\0','2016-09-24 14:02:10',15),(16,4,'',3,'\0','2016-09-24 14:02:10',16),(17,4,'',4,'\0','2016-09-24 14:02:10',17),(18,5,'',1,'\0','2016-09-24 14:02:10',18),(19,5,'',4,'\0','2016-09-24 14:02:10',19),(20,5,'',3,'\0','2016-09-24 14:02:10',20),(21,6,'',1,'\0','2016-09-24 14:02:10',21),(22,6,'',2,'\0','2016-09-24 14:02:10',22),(23,7,'',1,'\0','2016-10-05 12:50:56',23),(24,7,'\0',2,'\0','2016-10-05 12:50:56',24),(25,7,'',3,'\0','2016-10-05 12:50:56',25),(26,7,'',4,'\0','2016-10-05 12:50:56',26),(27,8,'',1,'\0','2016-10-05 12:50:56',27),(28,8,'',4,'\0','2016-10-05 12:50:56',28),(29,8,'',3,'\0','2016-10-05 12:50:56',29),(30,9,'',1,'\0','2016-10-05 12:50:56',30),(31,9,'',2,'\0','2016-10-05 12:50:56',31);
/*!40000 ALTER TABLE `configurations_mechanisms_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `general_configurations`
--

DROP TABLE IF EXISTS `general_configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `general_configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_configurations`
--

LOCK TABLES `general_configurations` WRITE;
/*!40000 ALTER TABLE `general_configurations` DISABLE KEYS */;
INSERT INTO `general_configurations` VALUES (6),(7),(8),(9),(10),(11),(12),(13);
/*!40000 ALTER TABLE `general_configurations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `general_configurations_history`
--

DROP TABLE IF EXISTS `general_configurations_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `general_configurations_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(45) DEFAULT NULL,
  `general_configurations_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_general_configurations_history_1_idx` (`general_configurations_id`),
  CONSTRAINT `fk_general_configurations_history_1` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_configurations_history`
--

LOCK TABLES `general_configurations_history` WRITE;
/*!40000 ALTER TABLE `general_configurations_history` DISABLE KEYS */;
INSERT INTO `general_configurations_history` VALUES (6,'2016-09-24 13:55:06',NULL,6,'\0'),(7,'2016-09-24 13:55:06',NULL,7,'\0'),(8,'2016-09-24 13:55:06',NULL,8,'\0'),(9,'2016-09-24 13:55:06',NULL,9,'\0'),(10,'2016-10-05 12:50:56',NULL,10,'\0'),(11,'2016-10-05 12:50:56',NULL,11,'\0'),(12,'2016-10-05 12:50:56',NULL,12,'\0'),(13,'2016-10-05 12:50:56',NULL,13,'\0');
/*!40000 ALTER TABLE `general_configurations_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mechanisms`
--

DROP TABLE IF EXISTS `mechanisms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mechanisms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mechanisms`
--

LOCK TABLES `mechanisms` WRITE;
/*!40000 ALTER TABLE `mechanisms` DISABLE KEYS */;
INSERT INTO `mechanisms` VALUES (5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),(21),(22);
/*!40000 ALTER TABLE `mechanisms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mechanisms_history`
--

DROP TABLE IF EXISTS `mechanisms_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mechanisms_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mechanisms_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_mechanisms_history_1_idx` (`mechanisms_id`),
  CONSTRAINT `fk_mechanisms_history_1` FOREIGN KEY (`mechanisms_id`) REFERENCES `mechanisms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mechanisms_history`
--

LOCK TABLES `mechanisms_history` WRITE;
/*!40000 ALTER TABLE `mechanisms_history` DISABLE KEYS */;
INSERT INTO `mechanisms_history` VALUES (5,'TEXT_TYPE','2016-09-24 13:55:06',5,''),(6,'AUDIO_TYPE','2016-09-24 13:55:06',6,''),(7,'SCREENSHOT_TYPE','2016-09-24 13:55:06',7,''),(8,'RATING_TYPE','2016-09-24 13:55:06',8,''),(9,'TEXT_TYPE','2016-09-24 13:55:06',9,''),(10,'RATING_TYPE','2016-09-24 13:55:06',10,''),(11,'SCREENSHOT_TYPE','2016-09-24 13:55:06',11,''),(12,'RATING_TYPE','2016-09-24 13:55:06',12,''),(13,'CATEGORY_TYPE','2016-09-24 13:55:06',13,''),(14,'TEXT_TYPE','2016-09-24 14:02:10',5,''),(15,'AUDIO_TYPE','2016-09-24 14:02:10',6,''),(16,'SCREENSHOT_TYPE','2016-09-24 14:02:10',7,''),(17,'RATING_TYPE','2016-09-24 14:02:10',8,''),(18,'TEXT_TYPE','2016-09-24 14:02:10',9,''),(19,'RATING_TYPE','2016-09-24 14:02:10',10,''),(20,'SCREENSHOT_TYPE','2016-09-24 14:02:10',11,''),(21,'RATING_TYPE','2016-09-24 14:02:10',12,''),(22,'CATEGORY_TYPE','2016-09-24 14:02:10',13,''),(23,'TEXT_TYPE','2016-10-05 12:50:56',14,''),(24,'AUDIO_TYPE','2016-10-05 12:50:56',15,''),(25,'SCREENSHOT_TYPE','2016-10-05 12:50:56',16,''),(26,'RATING_TYPE','2016-10-05 12:50:56',17,''),(27,'TEXT_TYPE','2016-10-05 12:50:56',18,''),(28,'RATING_TYPE','2016-10-05 12:50:56',19,''),(29,'SCREENSHOT_TYPE','2016-10-05 12:50:56',20,''),(30,'RATING_TYPE','2016-10-05 12:50:56',21,''),(31,'CATEGORY_TYPE','2016-10-05 12:50:56',22,'');
/*!40000 ALTER TABLE `mechanisms_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parameter_options`
--

DROP TABLE IF EXISTS `parameter_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameter_options` (
  `id` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `entity` enum('TEXT_MECHANISM','RATING_MECHANISM','SCREENSHOT_MECHANISM','CATEGORY_MECHANISM','AUDIO_MECHANISM','PULL_CONFIGURATION','PUSH_CONFIGURATION','APPLICATION_CONFIGURATION') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameter_options`
--

LOCK TABLES `parameter_options` WRITE;
/*!40000 ALTER TABLE `parameter_options` DISABLE KEYS */;
/*!40000 ALTER TABLE `parameter_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parameters`
--

DROP TABLE IF EXISTS `parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameters`
--

LOCK TABLES `parameters` WRITE;
/*!40000 ALTER TABLE `parameters` DISABLE KEYS */;
INSERT INTO `parameters` VALUES (40),(41),(42),(43),(44),(45),(46),(47),(48),(49),(50),(51),(52),(53),(54),(55),(56),(57),(58),(59),(60),(61),(62),(63),(64),(65),(66),(67),(68),(69),(70),(71),(72),(73),(74),(75),(76),(77),(78),(79),(80),(81),(82),(83),(84),(85),(86),(87),(88),(89),(90),(91),(92),(93),(94),(95),(96),(97),(98),(99),(100),(101),(102),(103),(104),(105),(106),(107),(108),(109),(110),(111),(112),(113),(114),(115),(116),(117),(118),(119),(120),(121),(122),(123),(124),(125),(126),(127),(128),(129),(130),(131),(132),(133),(134),(135),(136),(137),(138),(139),(140),(141),(142),(143),(144),(145),(146),(147),(148),(149),(150),(151),(152),(153),(154),(155),(156),(157),(158),(159),(160),(161),(162),(163),(164),(165),(166),(167),(168),(169),(170),(171),(172),(173),(174),(175),(176),(177),(178),(179),(180),(181),(182),(183),(184),(185),(186),(187),(188),(189);
/*!40000 ALTER TABLE `parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parameters_history`
--

DROP TABLE IF EXISTS `parameters_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameters_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mechanisms_id` int(11) DEFAULT NULL,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `default_value` varchar(255) DEFAULT NULL,
  `editable_by_user` bit(1) DEFAULT NULL,
  `parent_parameters_id` int(11) DEFAULT NULL,
  `language` varchar(3) NOT NULL DEFAULT 'en',
  `general_configurations_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `parameters_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `mechanism_constrained_idx` (`mechanisms_id`),
  KEY `fk_parameters_general_configurations1_idx` (`general_configurations_id`),
  KEY `fk_parameters_history_1_idx` (`parameters_id`),
  KEY `fk_parameters_history_2_idx` (`parent_parameters_id`),
  CONSTRAINT `fk_parameters_history_1` FOREIGN KEY (`parameters_id`) REFERENCES `parameters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parameters_history_2` FOREIGN KEY (`parent_parameters_id`) REFERENCES `parameters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parameters_history_3` FOREIGN KEY (`mechanisms_id`) REFERENCES `mechanisms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parameters_history_4` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameters_history`
--

LOCK TABLES `parameters_history` WRITE;
/*!40000 ALTER TABLE `parameters_history` DISABLE KEYS */;
INSERT INTO `parameters_history` VALUES (40,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',6,'2016-09-24 13:55:06',40,'\0'),(41,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',6,'2016-09-24 13:55:06',41,'\0'),(42,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',7,'2016-09-24 13:55:06',42,'\0'),(43,5,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',43,'\0'),(44,5,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',44,'\0'),(45,5,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',45,'\0'),(46,5,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',46,'\0'),(47,5,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',47,'\0'),(48,5,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',48,'\0'),(49,5,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',49,'\0'),(50,5,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',50,'\0'),(51,5,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',51,'\0'),(52,5,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',52,'\0'),(53,5,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',53,'\0'),(54,5,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',54,'\0'),(55,5,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',55,'\0'),(56,5,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',56,'\0'),(57,5,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',57,'\0'),(58,5,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',58,'\0'),(59,5,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',59,'\0'),(60,5,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',60,'\0'),(61,5,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',61,'\0'),(62,5,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',62,'\0'),(63,5,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',63,'\0'),(64,5,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',64,'\0'),(65,5,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',65,'\0'),(66,5,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',66,'\0'),(67,6,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-24 13:55:06',67,'\0'),(68,7,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',68,'\0'),(69,7,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-24 13:55:06',69,'\0'),(70,8,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',70,'\0'),(71,8,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',71,'\0'),(72,8,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',72,'\0'),(73,8,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-24 13:55:06',73,'\0'),(74,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',8,'2016-09-24 13:55:06',74,'\0'),(75,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',8,'2016-09-24 13:55:06',75,'\0'),(76,9,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',76,'\0'),(77,9,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',77,'\0'),(78,9,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',78,'\0'),(79,9,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',79,'\0'),(80,9,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',80,'\0'),(81,9,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',81,'\0'),(82,9,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',82,'\0'),(83,9,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',83,'\0'),(84,9,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',84,'\0'),(85,9,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',85,'\0'),(86,9,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',86,'\0'),(87,9,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',87,'\0'),(88,9,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',88,'\0'),(89,9,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',89,'\0'),(90,9,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',90,'\0'),(91,9,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',91,'\0'),(92,9,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',92,'\0'),(93,9,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',93,'\0'),(94,9,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',94,'\0'),(95,10,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',95,'\0'),(96,10,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',96,'\0'),(97,10,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',97,'\0'),(98,10,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-24 13:55:06',98,'\0'),(99,11,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',99,'\0'),(100,11,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-24 13:55:06',100,'\0'),(101,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',9,'2016-09-24 13:55:06',101,'\0'),(102,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',9,'2016-09-24 13:55:06',102,'\0'),(103,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',9,'2016-09-24 13:55:06',103,'\0'),(104,12,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',104,'\0'),(105,12,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',105,'\0'),(106,12,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',106,'\0'),(107,12,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-24 13:55:06',107,'\0'),(108,13,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',108,'\0'),(109,13,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',109,'\0'),(110,13,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',110,'\0'),(111,13,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-24 13:55:06',111,'\0'),(112,13,'BUG_CATEGORY','Bug',NULL,NULL,111,'en',NULL,'2016-09-24 13:55:06',112,'\0'),(113,13,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,111,'en',NULL,'2016-09-24 13:55:06',113,'\0'),(114,13,'GENERAL_CATEGORY','General Feedback',NULL,NULL,111,'en',NULL,'2016-09-24 13:55:06',114,'\0'),(115,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',10,'2016-10-05 12:50:56',115,'\0'),(116,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',10,'2016-10-05 12:50:56',116,'\0'),(117,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',11,'2016-10-05 12:50:56',117,'\0'),(118,14,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',118,'\0'),(119,14,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',119,'\0'),(120,14,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',120,'\0'),(121,14,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',121,'\0'),(122,14,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',122,'\0'),(123,14,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',123,'\0'),(124,14,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',124,'\0'),(125,14,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',125,'\0'),(126,14,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',126,'\0'),(127,14,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',127,'\0'),(128,14,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',128,'\0'),(129,14,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',129,'\0'),(130,14,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',130,'\0'),(131,14,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',131,'\0'),(132,14,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',132,'\0'),(133,14,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',133,'\0'),(134,14,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',134,'\0'),(135,14,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',135,'\0'),(136,14,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',136,'\0'),(137,14,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',137,'\0'),(138,14,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',138,'\0'),(139,14,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',139,'\0'),(140,14,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',140,'\0'),(141,14,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',141,'\0'),(142,15,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-10-05 12:50:56',142,'\0'),(143,16,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',143,'\0'),(144,16,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-10-05 12:50:56',144,'\0'),(145,17,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',145,'\0'),(146,17,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',146,'\0'),(147,17,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',147,'\0'),(148,17,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-10-05 12:50:56',148,'\0'),(149,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',12,'2016-10-05 12:50:56',149,'\0'),(150,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',12,'2016-10-05 12:50:56',150,'\0'),(151,18,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',151,'\0'),(152,18,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',152,'\0'),(153,18,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',153,'\0'),(154,18,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',154,'\0'),(155,18,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',155,'\0'),(156,18,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',156,'\0'),(157,18,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',157,'\0'),(158,18,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',158,'\0'),(159,18,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',159,'\0'),(160,18,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',160,'\0'),(161,18,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',161,'\0'),(162,18,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',162,'\0'),(163,18,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',163,'\0'),(164,18,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',164,'\0'),(165,18,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',165,'\0'),(166,18,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',166,'\0'),(167,18,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',167,'\0'),(168,18,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',168,'\0'),(169,18,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',169,'\0'),(170,19,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',170,'\0'),(171,19,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',171,'\0'),(172,19,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',172,'\0'),(173,19,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-10-05 12:50:56',173,'\0'),(174,20,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',174,'\0'),(175,20,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-10-05 12:50:56',175,'\0'),(176,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',13,'2016-10-05 12:50:56',176,'\0'),(177,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',13,'2016-10-05 12:50:56',177,'\0'),(178,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',13,'2016-10-05 12:50:56',178,'\0'),(179,21,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',179,'\0'),(180,21,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',180,'\0'),(181,21,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',181,'\0'),(182,21,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-10-05 12:50:56',182,'\0'),(183,22,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',183,'\0'),(184,22,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',184,'\0'),(185,22,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',185,'\0'),(186,22,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-10-05 12:50:56',186,'\0'),(187,22,'BUG_CATEGORY','Bug',NULL,NULL,186,'en',NULL,'2016-10-05 12:50:56',187,'\0'),(188,22,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,186,'en',NULL,'2016-10-05 12:50:56',188,'\0'),(189,22,'GENERAL_CATEGORY','General Feedback',NULL,NULL,186,'en',NULL,'2016-10-05 12:50:56',189,'\0');
/*!40000 ALTER TABLE `parameters_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups`
--

DROP TABLE IF EXISTS `user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` VALUES (1);
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups_history`
--

DROP TABLE IF EXISTS `user_groups_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_groups_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_groups_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_user_groups_history_1_idx` (`user_groups_id`),
  CONSTRAINT `fk_user_groups_history_1` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups_history`
--

LOCK TABLES `user_groups_history` WRITE;
/*!40000 ALTER TABLE `user_groups_history` DISABLE KEYS */;
INSERT INTO `user_groups_history` VALUES (1,1,'default','2016-09-16 12:59:36','');
/*!40000 ALTER TABLE `user_groups_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_history`
--

DROP TABLE IF EXISTS `users_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `user_groups_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  `users_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_history_1_idx` (`user_groups_id`),
  KEY `fk_users_history_2_idx` (`users_id`),
  CONSTRAINT `fk_users_history_1` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_history_2` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_history`
--

LOCK TABLES `users_history` WRITE;
/*!40000 ALTER TABLE `users_history` DISABLE KEYS */;
INSERT INTO `users_history` VALUES (1,'u1234',1,'2016-09-21 13:14:13','',1);
/*!40000 ALTER TABLE `users_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-24 14:49:22
