CREATE DATABASE  IF NOT EXISTS `feedback_orchestrator_test` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `feedback_orchestrator_test`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: feedback_orchestrator_test
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications1`
--

LOCK TABLES `applications1` WRITE;
/*!40000 ALTER TABLE `applications1` DISABLE KEYS */;
INSERT INTO `applications1` VALUES (27),(28),(29),(30),(31),(32),(33),(34),(35);
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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications1_history`
--

LOCK TABLES `applications1_history` WRITE;
/*!40000 ALTER TABLE `applications1_history` DISABLE KEYS */;
INSERT INTO `applications1_history` VALUES (64,'Test Website 11',1,'2016-09-16 08:48:59',116,27,'\0'),(65,'Test Website 12',1,'2016-09-16 08:55:35',120,28,'\0'),(66,'Test Website 13',1,'2016-09-16 08:56:05',124,29,'\0'),(67,'Test Website 14',1,'2016-09-16 08:56:13',128,30,'\0'),(68,'Test Website 15',1,'2016-09-16 08:56:19',132,31,'\0'),(69,'Test Website 16',1,'2016-09-16 08:56:26',136,32,'\0'),(70,'Test Website 17',1,'2016-09-16 08:56:34',140,33,'\0'),(71,'Test Website 18',1,'2016-09-16 08:56:41',144,34,'\0'),(72,'Test Website 19',1,'2016-09-16 08:56:49',148,35,'\0');
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
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations`
--

LOCK TABLES `configurations` WRITE;
/*!40000 ALTER TABLE `configurations` DISABLE KEYS */;
INSERT INTO `configurations` VALUES (56),(57),(58),(59),(60),(61),(62),(63),(64),(65),(66),(67),(68),(69),(70),(71),(72),(73),(74),(75),(76),(77),(78),(79),(80),(81),(82);
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
  `active` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_configurations_1_idx` (`applications_id`),
  KEY `fk_configurations_2_idx` (`user_groups_id`),
  KEY `fk_configurations_3_idx` (`general_configurations_id`),
  KEY `fk_configurations_history_1_idx` (`configurations_id`),
  CONSTRAINT `fk_configurations_history_1` FOREIGN KEY (`configurations_id`) REFERENCES `configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_2` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_3` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_history_4` FOREIGN KEY (`applications_id`) REFERENCES `applications1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations_history`
--

LOCK TABLES `configurations_history` WRITE;
/*!40000 ALTER TABLE `configurations_history` DISABLE KEYS */;
INSERT INTO `configurations_history` VALUES (90,NULL,'2016-09-16 08:48:59',27,1,'PUSH',117,56,'\0',''),(91,NULL,'2016-09-16 08:49:00',27,1,'PULL',118,57,'\0',''),(92,NULL,'2016-09-16 08:49:00',27,1,'PULL',119,58,'\0',''),(93,NULL,'2016-09-16 08:55:35',28,1,'PUSH',121,59,'\0',''),(94,NULL,'2016-09-16 08:55:35',28,1,'PULL',122,60,'\0',''),(95,NULL,'2016-09-16 08:55:36',28,1,'PULL',123,61,'\0',''),(96,NULL,'2016-09-16 08:56:05',29,1,'PUSH',125,62,'\0',''),(97,NULL,'2016-09-16 08:56:05',29,1,'PULL',126,63,'\0',''),(98,NULL,'2016-09-16 08:56:05',29,1,'PULL',127,64,'\0',''),(99,NULL,'2016-09-16 08:56:13',30,1,'PUSH',129,65,'\0',''),(100,NULL,'2016-09-16 08:56:13',30,1,'PULL',130,66,'\0',''),(101,NULL,'2016-09-16 08:56:13',30,1,'PULL',131,67,'\0',''),(102,NULL,'2016-09-16 08:56:19',31,1,'PUSH',133,68,'\0',''),(103,NULL,'2016-09-16 08:56:19',31,1,'PULL',134,69,'\0',''),(104,NULL,'2016-09-16 08:56:19',31,1,'PULL',135,70,'\0',''),(105,NULL,'2016-09-16 08:56:26',32,1,'PUSH',137,71,'\0',''),(106,NULL,'2016-09-16 08:56:26',32,1,'PULL',138,72,'\0',''),(107,NULL,'2016-09-16 08:56:26',32,1,'PULL',139,73,'\0',''),(108,NULL,'2016-09-16 08:56:34',33,1,'PUSH',141,74,'\0',''),(109,NULL,'2016-09-16 08:56:34',33,1,'PULL',142,75,'\0',''),(110,NULL,'2016-09-16 08:56:34',33,1,'PULL',143,76,'\0',''),(111,NULL,'2016-09-16 08:56:41',34,1,'PUSH',145,77,'\0',''),(112,NULL,'2016-09-16 08:56:41',34,1,'PULL',146,78,'\0',''),(113,NULL,'2016-09-16 08:56:41',34,1,'PULL',147,79,'\0',''),(114,NULL,'2016-09-16 08:56:49',35,1,'PUSH',149,80,'\0',''),(115,NULL,'2016-09-16 08:56:49',35,1,'PULL',150,81,'\0',''),(116,NULL,'2016-09-16 08:56:49',35,1,'PULL',151,82,'\0','');
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
) ENGINE=InnoDB AUTO_INCREMENT=1845 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations_mechanisms_history`
--

LOCK TABLES `configurations_mechanisms_history` WRITE;
/*!40000 ALTER TABLE `configurations_mechanisms_history` DISABLE KEYS */;
INSERT INTO `configurations_mechanisms_history` VALUES (1763,56,'',1,'\0','2016-09-16 08:48:59',1765),(1764,56,'\0',2,'\0','2016-09-16 08:48:59',1766),(1765,56,'',3,'\0','2016-09-16 08:48:59',1767),(1766,56,'',4,'\0','2016-09-16 08:48:59',1768),(1767,57,'',1,'\0','2016-09-16 08:49:00',1769),(1768,57,'',4,'\0','2016-09-16 08:49:00',1770),(1769,57,'',3,'\0','2016-09-16 08:49:00',1771),(1770,58,'',1,'\0','2016-09-16 08:49:00',1772),(1771,58,'',2,'\0','2016-09-16 08:49:00',1773),(1772,56,'\0',1,'\0','2016-09-16 08:51:03',1774),(1773,59,'',1,'\0','2016-09-16 08:55:35',1775),(1774,59,'\0',2,'\0','2016-09-16 08:55:35',1776),(1775,59,'',3,'\0','2016-09-16 08:55:35',1777),(1776,59,'',4,'\0','2016-09-16 08:55:35',1778),(1777,60,'',1,'\0','2016-09-16 08:55:35',1779),(1778,60,'',4,'\0','2016-09-16 08:55:35',1780),(1779,60,'',3,'\0','2016-09-16 08:55:35',1781),(1780,61,'',1,'\0','2016-09-16 08:55:36',1782),(1781,61,'',2,'\0','2016-09-16 08:55:36',1783),(1782,62,'',1,'\0','2016-09-16 08:56:05',1784),(1783,62,'\0',2,'\0','2016-09-16 08:56:05',1785),(1784,62,'',3,'\0','2016-09-16 08:56:05',1786),(1785,62,'',4,'\0','2016-09-16 08:56:05',1787),(1786,63,'',1,'\0','2016-09-16 08:56:05',1788),(1787,63,'',4,'\0','2016-09-16 08:56:05',1789),(1788,63,'',3,'\0','2016-09-16 08:56:05',1790),(1789,64,'',1,'\0','2016-09-16 08:56:05',1791),(1790,64,'',2,'\0','2016-09-16 08:56:05',1792),(1791,65,'',1,'\0','2016-09-16 08:56:13',1793),(1792,65,'\0',2,'\0','2016-09-16 08:56:13',1794),(1793,65,'',3,'\0','2016-09-16 08:56:13',1795),(1794,65,'',4,'\0','2016-09-16 08:56:13',1796),(1795,66,'',1,'\0','2016-09-16 08:56:13',1797),(1796,66,'',4,'\0','2016-09-16 08:56:13',1798),(1797,66,'',3,'\0','2016-09-16 08:56:13',1799),(1798,67,'',1,'\0','2016-09-16 08:56:13',1800),(1799,67,'',2,'\0','2016-09-16 08:56:13',1801),(1800,68,'',1,'\0','2016-09-16 08:56:19',1802),(1801,68,'\0',2,'\0','2016-09-16 08:56:19',1803),(1802,68,'',3,'\0','2016-09-16 08:56:19',1804),(1803,68,'',4,'\0','2016-09-16 08:56:19',1805),(1804,69,'',1,'\0','2016-09-16 08:56:19',1806),(1805,69,'',4,'\0','2016-09-16 08:56:19',1807),(1806,69,'',3,'\0','2016-09-16 08:56:19',1808),(1807,70,'',1,'\0','2016-09-16 08:56:19',1809),(1808,70,'',2,'\0','2016-09-16 08:56:19',1810),(1809,71,'',1,'\0','2016-09-16 08:56:26',1811),(1810,71,'\0',2,'\0','2016-09-16 08:56:26',1812),(1811,71,'',3,'\0','2016-09-16 08:56:26',1813),(1812,71,'',4,'\0','2016-09-16 08:56:26',1814),(1813,72,'',1,'\0','2016-09-16 08:56:26',1815),(1814,72,'',4,'\0','2016-09-16 08:56:26',1816),(1815,72,'',3,'\0','2016-09-16 08:56:26',1817),(1816,73,'',1,'\0','2016-09-16 08:56:26',1818),(1817,73,'',2,'\0','2016-09-16 08:56:26',1819),(1818,74,'',1,'\0','2016-09-16 08:56:34',1820),(1819,74,'\0',2,'\0','2016-09-16 08:56:34',1821),(1820,74,'',3,'\0','2016-09-16 08:56:34',1822),(1821,74,'',4,'\0','2016-09-16 08:56:34',1823),(1822,75,'',1,'\0','2016-09-16 08:56:34',1824),(1823,75,'',4,'\0','2016-09-16 08:56:34',1825),(1824,75,'',3,'\0','2016-09-16 08:56:34',1826),(1825,76,'',1,'\0','2016-09-16 08:56:34',1827),(1826,76,'',2,'\0','2016-09-16 08:56:34',1828),(1827,77,'',1,'\0','2016-09-16 08:56:41',1829),(1828,77,'\0',2,'\0','2016-09-16 08:56:41',1830),(1829,77,'',3,'\0','2016-09-16 08:56:41',1831),(1830,77,'',4,'\0','2016-09-16 08:56:41',1832),(1831,78,'',1,'\0','2016-09-16 08:56:41',1833),(1832,78,'',4,'\0','2016-09-16 08:56:41',1834),(1833,78,'',3,'\0','2016-09-16 08:56:41',1835),(1834,79,'',1,'\0','2016-09-16 08:56:41',1836),(1835,79,'',2,'\0','2016-09-16 08:56:41',1837),(1836,80,'',1,'\0','2016-09-16 08:56:49',1838),(1837,80,'\0',2,'\0','2016-09-16 08:56:49',1839),(1838,80,'',3,'\0','2016-09-16 08:56:49',1840),(1839,80,'',4,'\0','2016-09-16 08:56:49',1841),(1840,81,'',1,'\0','2016-09-16 08:56:49',1842),(1841,81,'',4,'\0','2016-09-16 08:56:49',1843),(1842,81,'',3,'\0','2016-09-16 08:56:49',1844),(1843,82,'',1,'\0','2016-09-16 08:56:49',1845),(1844,82,'',2,'\0','2016-09-16 08:56:49',1846);
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
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_configurations`
--

LOCK TABLES `general_configurations` WRITE;
/*!40000 ALTER TABLE `general_configurations` DISABLE KEYS */;
INSERT INTO `general_configurations` VALUES (116),(117),(118),(119),(120),(121),(122),(123),(124),(125),(126),(127),(128),(129),(130),(131),(132),(133),(134),(135),(136),(137),(138),(139),(140),(141),(142),(143),(144),(145),(146),(147),(148),(149),(150),(151);
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
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_configurations_history`
--

LOCK TABLES `general_configurations_history` WRITE;
/*!40000 ALTER TABLE `general_configurations_history` DISABLE KEYS */;
INSERT INTO `general_configurations_history` VALUES (138,'2016-09-16 08:48:59',NULL,116,'\0'),(139,'2016-09-16 08:48:59',NULL,117,'\0'),(140,'2016-09-16 08:49:00',NULL,118,'\0'),(141,'2016-09-16 08:49:00',NULL,119,'\0'),(142,'2016-09-16 08:55:35',NULL,120,'\0'),(143,'2016-09-16 08:55:35',NULL,121,'\0'),(144,'2016-09-16 08:55:35',NULL,122,'\0'),(145,'2016-09-16 08:55:36',NULL,123,'\0'),(146,'2016-09-16 08:56:05',NULL,124,'\0'),(147,'2016-09-16 08:56:05',NULL,125,'\0'),(148,'2016-09-16 08:56:05',NULL,126,'\0'),(149,'2016-09-16 08:56:05',NULL,127,'\0'),(150,'2016-09-16 08:56:13',NULL,128,'\0'),(151,'2016-09-16 08:56:13',NULL,129,'\0'),(152,'2016-09-16 08:56:13',NULL,130,'\0'),(153,'2016-09-16 08:56:13',NULL,131,'\0'),(154,'2016-09-16 08:56:19',NULL,132,'\0'),(155,'2016-09-16 08:56:19',NULL,133,'\0'),(156,'2016-09-16 08:56:19',NULL,134,'\0'),(157,'2016-09-16 08:56:19',NULL,135,'\0'),(158,'2016-09-16 08:56:26',NULL,136,'\0'),(159,'2016-09-16 08:56:26',NULL,137,'\0'),(160,'2016-09-16 08:56:26',NULL,138,'\0'),(161,'2016-09-16 08:56:26',NULL,139,'\0'),(162,'2016-09-16 08:56:34',NULL,140,'\0'),(163,'2016-09-16 08:56:34',NULL,141,'\0'),(164,'2016-09-16 08:56:34',NULL,142,'\0'),(165,'2016-09-16 08:56:34',NULL,143,'\0'),(166,'2016-09-16 08:56:41',NULL,144,'\0'),(167,'2016-09-16 08:56:41',NULL,145,'\0'),(168,'2016-09-16 08:56:41',NULL,146,'\0'),(169,'2016-09-16 08:56:41',NULL,147,'\0'),(170,'2016-09-16 08:56:49',NULL,148,'\0'),(171,'2016-09-16 08:56:49',NULL,149,'\0'),(172,'2016-09-16 08:56:49',NULL,150,'\0'),(173,'2016-09-16 08:56:49',NULL,151,'\0');
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
) ENGINE=InnoDB AUTO_INCREMENT=836 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mechanisms`
--

LOCK TABLES `mechanisms` WRITE;
/*!40000 ALTER TABLE `mechanisms` DISABLE KEYS */;
INSERT INTO `mechanisms` VALUES (755),(756),(757),(758),(759),(760),(761),(762),(763),(764),(765),(766),(767),(768),(769),(770),(771),(772),(773),(774),(775),(776),(777),(778),(779),(780),(781),(782),(783),(784),(785),(786),(787),(788),(789),(790),(791),(792),(793),(794),(795),(796),(797),(798),(799),(800),(801),(802),(803),(804),(805),(806),(807),(808),(809),(810),(811),(812),(813),(814),(815),(816),(817),(818),(819),(820),(821),(822),(823),(824),(825),(826),(827),(828),(829),(830),(831),(832),(833),(834),(835);
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
) ENGINE=InnoDB AUTO_INCREMENT=1847 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mechanisms_history`
--

LOCK TABLES `mechanisms_history` WRITE;
/*!40000 ALTER TABLE `mechanisms_history` DISABLE KEYS */;
INSERT INTO `mechanisms_history` VALUES (1765,'TEXT_TYPE','2016-09-16 08:48:59',755,''),(1766,'AUDIO_TYPE','2016-09-16 08:48:59',756,''),(1767,'SCREENSHOT_TYPE','2016-09-16 08:48:59',757,''),(1768,'RATING_TYPE','2016-09-16 08:48:59',758,''),(1769,'TEXT_TYPE','2016-09-16 08:49:00',759,''),(1770,'RATING_TYPE','2016-09-16 08:49:00',760,''),(1771,'SCREENSHOT_TYPE','2016-09-16 08:49:00',761,''),(1772,'RATING_TYPE','2016-09-16 08:49:00',762,''),(1773,'CATEGORY_TYPE','2016-09-16 08:49:00',763,''),(1774,'TEXT_TYPE','2016-09-16 08:51:03',755,''),(1775,'TEXT_TYPE','2016-09-16 08:55:35',764,''),(1776,'AUDIO_TYPE','2016-09-16 08:55:35',765,''),(1777,'SCREENSHOT_TYPE','2016-09-16 08:55:35',766,''),(1778,'RATING_TYPE','2016-09-16 08:55:35',767,''),(1779,'TEXT_TYPE','2016-09-16 08:55:35',768,''),(1780,'RATING_TYPE','2016-09-16 08:55:35',769,''),(1781,'SCREENSHOT_TYPE','2016-09-16 08:55:35',770,''),(1782,'RATING_TYPE','2016-09-16 08:55:36',771,''),(1783,'CATEGORY_TYPE','2016-09-16 08:55:36',772,''),(1784,'TEXT_TYPE','2016-09-16 08:56:05',773,''),(1785,'AUDIO_TYPE','2016-09-16 08:56:05',774,''),(1786,'SCREENSHOT_TYPE','2016-09-16 08:56:05',775,''),(1787,'RATING_TYPE','2016-09-16 08:56:05',776,''),(1788,'TEXT_TYPE','2016-09-16 08:56:05',777,''),(1789,'RATING_TYPE','2016-09-16 08:56:05',778,''),(1790,'SCREENSHOT_TYPE','2016-09-16 08:56:05',779,''),(1791,'RATING_TYPE','2016-09-16 08:56:05',780,''),(1792,'CATEGORY_TYPE','2016-09-16 08:56:05',781,''),(1793,'TEXT_TYPE','2016-09-16 08:56:13',782,''),(1794,'AUDIO_TYPE','2016-09-16 08:56:13',783,''),(1795,'SCREENSHOT_TYPE','2016-09-16 08:56:13',784,''),(1796,'RATING_TYPE','2016-09-16 08:56:13',785,''),(1797,'TEXT_TYPE','2016-09-16 08:56:13',786,''),(1798,'RATING_TYPE','2016-09-16 08:56:13',787,''),(1799,'SCREENSHOT_TYPE','2016-09-16 08:56:13',788,''),(1800,'RATING_TYPE','2016-09-16 08:56:13',789,''),(1801,'CATEGORY_TYPE','2016-09-16 08:56:13',790,''),(1802,'TEXT_TYPE','2016-09-16 08:56:19',791,''),(1803,'AUDIO_TYPE','2016-09-16 08:56:19',792,''),(1804,'SCREENSHOT_TYPE','2016-09-16 08:56:19',793,''),(1805,'RATING_TYPE','2016-09-16 08:56:19',794,''),(1806,'TEXT_TYPE','2016-09-16 08:56:19',795,''),(1807,'RATING_TYPE','2016-09-16 08:56:19',796,''),(1808,'SCREENSHOT_TYPE','2016-09-16 08:56:19',797,''),(1809,'RATING_TYPE','2016-09-16 08:56:19',798,''),(1810,'CATEGORY_TYPE','2016-09-16 08:56:19',799,''),(1811,'TEXT_TYPE','2016-09-16 08:56:26',800,''),(1812,'AUDIO_TYPE','2016-09-16 08:56:26',801,''),(1813,'SCREENSHOT_TYPE','2016-09-16 08:56:26',802,''),(1814,'RATING_TYPE','2016-09-16 08:56:26',803,''),(1815,'TEXT_TYPE','2016-09-16 08:56:26',804,''),(1816,'RATING_TYPE','2016-09-16 08:56:26',805,''),(1817,'SCREENSHOT_TYPE','2016-09-16 08:56:26',806,''),(1818,'RATING_TYPE','2016-09-16 08:56:26',807,''),(1819,'CATEGORY_TYPE','2016-09-16 08:56:26',808,''),(1820,'TEXT_TYPE','2016-09-16 08:56:34',809,''),(1821,'AUDIO_TYPE','2016-09-16 08:56:34',810,''),(1822,'SCREENSHOT_TYPE','2016-09-16 08:56:34',811,''),(1823,'RATING_TYPE','2016-09-16 08:56:34',812,''),(1824,'TEXT_TYPE','2016-09-16 08:56:34',813,''),(1825,'RATING_TYPE','2016-09-16 08:56:34',814,''),(1826,'SCREENSHOT_TYPE','2016-09-16 08:56:34',815,''),(1827,'RATING_TYPE','2016-09-16 08:56:34',816,''),(1828,'CATEGORY_TYPE','2016-09-16 08:56:34',817,''),(1829,'TEXT_TYPE','2016-09-16 08:56:41',818,''),(1830,'AUDIO_TYPE','2016-09-16 08:56:41',819,''),(1831,'SCREENSHOT_TYPE','2016-09-16 08:56:41',820,''),(1832,'RATING_TYPE','2016-09-16 08:56:41',821,''),(1833,'TEXT_TYPE','2016-09-16 08:56:41',822,''),(1834,'RATING_TYPE','2016-09-16 08:56:41',823,''),(1835,'SCREENSHOT_TYPE','2016-09-16 08:56:41',824,''),(1836,'RATING_TYPE','2016-09-16 08:56:41',825,''),(1837,'CATEGORY_TYPE','2016-09-16 08:56:41',826,''),(1838,'TEXT_TYPE','2016-09-16 08:56:49',827,''),(1839,'AUDIO_TYPE','2016-09-16 08:56:49',828,''),(1840,'SCREENSHOT_TYPE','2016-09-16 08:56:49',829,''),(1841,'RATING_TYPE','2016-09-16 08:56:49',830,''),(1842,'TEXT_TYPE','2016-09-16 08:56:49',831,''),(1843,'RATING_TYPE','2016-09-16 08:56:49',832,''),(1844,'SCREENSHOT_TYPE','2016-09-16 08:56:49',833,''),(1845,'RATING_TYPE','2016-09-16 08:56:49',834,''),(1846,'CATEGORY_TYPE','2016-09-16 08:56:49',835,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=6712 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameters`
--

LOCK TABLES `parameters` WRITE;
/*!40000 ALTER TABLE `parameters` DISABLE KEYS */;
INSERT INTO `parameters` VALUES (6037),(6038),(6039),(6040),(6041),(6042),(6043),(6044),(6045),(6046),(6047),(6048),(6049),(6050),(6051),(6052),(6053),(6054),(6055),(6056),(6057),(6058),(6059),(6060),(6061),(6062),(6063),(6064),(6065),(6066),(6067),(6068),(6069),(6070),(6071),(6072),(6073),(6074),(6075),(6076),(6077),(6078),(6079),(6080),(6081),(6082),(6083),(6084),(6085),(6086),(6087),(6088),(6089),(6090),(6091),(6092),(6093),(6094),(6095),(6096),(6097),(6098),(6099),(6100),(6101),(6102),(6103),(6104),(6105),(6106),(6107),(6108),(6109),(6110),(6111),(6112),(6113),(6114),(6115),(6116),(6117),(6118),(6119),(6120),(6121),(6122),(6123),(6124),(6125),(6126),(6127),(6128),(6129),(6130),(6131),(6132),(6133),(6134),(6135),(6136),(6137),(6138),(6139),(6140),(6141),(6142),(6143),(6144),(6145),(6146),(6147),(6148),(6149),(6150),(6151),(6152),(6153),(6154),(6155),(6156),(6157),(6158),(6159),(6160),(6161),(6162),(6163),(6164),(6165),(6166),(6167),(6168),(6169),(6170),(6171),(6172),(6173),(6174),(6175),(6176),(6177),(6178),(6179),(6180),(6181),(6182),(6183),(6184),(6185),(6186),(6187),(6188),(6189),(6190),(6191),(6192),(6193),(6194),(6195),(6196),(6197),(6198),(6199),(6200),(6201),(6202),(6203),(6204),(6205),(6206),(6207),(6208),(6209),(6210),(6211),(6212),(6213),(6214),(6215),(6216),(6217),(6218),(6219),(6220),(6221),(6222),(6223),(6224),(6225),(6226),(6227),(6228),(6229),(6230),(6231),(6232),(6233),(6234),(6235),(6236),(6237),(6238),(6239),(6240),(6241),(6242),(6243),(6244),(6245),(6246),(6247),(6248),(6249),(6250),(6251),(6252),(6253),(6254),(6255),(6256),(6257),(6258),(6259),(6260),(6261),(6262),(6263),(6264),(6265),(6266),(6267),(6268),(6269),(6270),(6271),(6272),(6273),(6274),(6275),(6276),(6277),(6278),(6279),(6280),(6281),(6282),(6283),(6284),(6285),(6286),(6287),(6288),(6289),(6290),(6291),(6292),(6293),(6294),(6295),(6296),(6297),(6298),(6299),(6300),(6301),(6302),(6303),(6304),(6305),(6306),(6307),(6308),(6309),(6310),(6311),(6312),(6313),(6314),(6315),(6316),(6317),(6318),(6319),(6320),(6321),(6322),(6323),(6324),(6325),(6326),(6327),(6328),(6329),(6330),(6331),(6332),(6333),(6334),(6335),(6336),(6337),(6338),(6339),(6340),(6341),(6342),(6343),(6344),(6345),(6346),(6347),(6348),(6349),(6350),(6351),(6352),(6353),(6354),(6355),(6356),(6357),(6358),(6359),(6360),(6361),(6362),(6363),(6364),(6365),(6366),(6367),(6368),(6369),(6370),(6371),(6372),(6373),(6374),(6375),(6376),(6377),(6378),(6379),(6380),(6381),(6382),(6383),(6384),(6385),(6386),(6387),(6388),(6389),(6390),(6391),(6392),(6393),(6394),(6395),(6396),(6397),(6398),(6399),(6400),(6401),(6402),(6403),(6404),(6405),(6406),(6407),(6408),(6409),(6410),(6411),(6412),(6413),(6414),(6415),(6416),(6417),(6418),(6419),(6420),(6421),(6422),(6423),(6424),(6425),(6426),(6427),(6428),(6429),(6430),(6431),(6432),(6433),(6434),(6435),(6436),(6437),(6438),(6439),(6440),(6441),(6442),(6443),(6444),(6445),(6446),(6447),(6448),(6449),(6450),(6451),(6452),(6453),(6454),(6455),(6456),(6457),(6458),(6459),(6460),(6461),(6462),(6463),(6464),(6465),(6466),(6467),(6468),(6469),(6470),(6471),(6472),(6473),(6474),(6475),(6476),(6477),(6478),(6479),(6480),(6481),(6482),(6483),(6484),(6485),(6486),(6487),(6488),(6489),(6490),(6491),(6492),(6493),(6494),(6495),(6496),(6497),(6498),(6499),(6500),(6501),(6502),(6503),(6504),(6505),(6506),(6507),(6508),(6509),(6510),(6511),(6512),(6513),(6514),(6515),(6516),(6517),(6518),(6519),(6520),(6521),(6522),(6523),(6524),(6525),(6526),(6527),(6528),(6529),(6530),(6531),(6532),(6533),(6534),(6535),(6536),(6537),(6538),(6539),(6540),(6541),(6542),(6543),(6544),(6545),(6546),(6547),(6548),(6549),(6550),(6551),(6552),(6553),(6554),(6555),(6556),(6557),(6558),(6559),(6560),(6561),(6562),(6563),(6564),(6565),(6566),(6567),(6568),(6569),(6570),(6571),(6572),(6573),(6574),(6575),(6576),(6577),(6578),(6579),(6580),(6581),(6582),(6583),(6584),(6585),(6586),(6587),(6588),(6589),(6590),(6591),(6592),(6593),(6594),(6595),(6596),(6597),(6598),(6599),(6600),(6601),(6602),(6603),(6604),(6605),(6606),(6607),(6608),(6609),(6610),(6611),(6612),(6613),(6614),(6615),(6616),(6617),(6618),(6619),(6620),(6621),(6622),(6623),(6624),(6625),(6626),(6627),(6628),(6629),(6630),(6631),(6632),(6633),(6634),(6635),(6636),(6637),(6638),(6639),(6640),(6641),(6642),(6643),(6644),(6645),(6646),(6647),(6648),(6649),(6650),(6651),(6652),(6653),(6654),(6655),(6656),(6657),(6658),(6659),(6660),(6661),(6662),(6663),(6664),(6665),(6666),(6667),(6668),(6669),(6670),(6671),(6672),(6673),(6674),(6675),(6676),(6677),(6678),(6679),(6680),(6681),(6682),(6683),(6684),(6685),(6686),(6687),(6688),(6689),(6690),(6691),(6692),(6693),(6694),(6695),(6696),(6697),(6698),(6699),(6700),(6701),(6702),(6703),(6704),(6705),(6706),(6707),(6708),(6709),(6710),(6711);
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
) ENGINE=InnoDB AUTO_INCREMENT=6952 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameters_history`
--

LOCK TABLES `parameters_history` WRITE;
/*!40000 ALTER TABLE `parameters_history` DISABLE KEYS */;
INSERT INTO `parameters_history` VALUES (6276,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',116,'2016-09-16 08:48:59',6037,'\0'),(6277,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',116,'2016-09-16 08:48:59',6038,'\0'),(6278,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',117,'2016-09-16 08:48:59',6039,'\0'),(6279,755,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6040,'\0'),(6280,755,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6041,'\0'),(6281,755,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6042,'\0'),(6282,755,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6043,'\0'),(6283,755,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6044,'\0'),(6284,755,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6045,'\0'),(6285,755,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6046,'\0'),(6286,755,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6047,'\0'),(6287,755,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6048,'\0'),(6288,755,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6049,'\0'),(6289,755,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6050,'\0'),(6290,755,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6051,'\0'),(6291,755,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6052,'\0'),(6292,755,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6053,'\0'),(6293,755,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6054,'\0'),(6294,755,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6055,'\0'),(6295,755,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6056,'\0'),(6296,755,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6057,'\0'),(6297,755,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6058,'\0'),(6298,755,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6059,'\0'),(6299,755,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6060,'\0'),(6300,755,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6061,'\0'),(6301,755,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6062,'\0'),(6302,755,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6063,'\0'),(6303,756,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:48:59',6064,'\0'),(6304,757,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:48:59',6065,'\0'),(6305,757,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:48:59',6066,'\0'),(6306,758,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6067,'\0'),(6307,758,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6068,'\0'),(6308,758,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6069,'\0'),(6309,758,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:49:00',6070,'\0'),(6310,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',118,'2016-09-16 08:49:00',6071,'\0'),(6311,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',118,'2016-09-16 08:49:00',6072,'\0'),(6312,759,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6073,'\0'),(6313,759,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6074,'\0'),(6314,759,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6075,'\0'),(6315,759,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6076,'\0'),(6316,759,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6077,'\0'),(6317,759,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6078,'\0'),(6318,759,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6079,'\0'),(6319,759,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6080,'\0'),(6320,759,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6081,'\0'),(6321,759,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6082,'\0'),(6322,759,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6083,'\0'),(6323,759,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6084,'\0'),(6324,759,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6085,'\0'),(6325,759,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6086,'\0'),(6326,759,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6087,'\0'),(6327,759,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6088,'\0'),(6328,759,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6089,'\0'),(6329,759,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6090,'\0'),(6330,759,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6091,'\0'),(6331,760,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6092,'\0'),(6332,760,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6093,'\0'),(6333,760,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6094,'\0'),(6334,760,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:49:00',6095,'\0'),(6335,761,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6096,'\0'),(6336,761,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:49:00',6097,'\0'),(6337,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',119,'2016-09-16 08:49:00',6098,'\0'),(6338,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',119,'2016-09-16 08:49:00',6099,'\0'),(6339,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',119,'2016-09-16 08:49:00',6100,'\0'),(6340,762,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6101,'\0'),(6341,762,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6102,'\0'),(6342,762,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6103,'\0'),(6343,762,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:49:00',6104,'\0'),(6344,763,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6105,'\0'),(6345,763,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6106,'\0'),(6346,763,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6107,'\0'),(6347,763,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:49:00',6108,'\0'),(6348,763,'BUG_CATEGORY','Bug',NULL,NULL,6108,'en',NULL,'2016-09-16 08:49:00',6109,'\0'),(6349,763,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6108,'en',NULL,'2016-09-16 08:49:00',6110,'\0'),(6350,763,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6108,'en',NULL,'2016-09-16 08:49:00',6111,'\0'),(6351,755,'maxLength','100.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:51:03',6040,'\0'),(6352,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',120,'2016-09-16 08:55:35',6112,'\0'),(6353,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',120,'2016-09-16 08:55:35',6113,'\0'),(6354,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',121,'2016-09-16 08:55:35',6114,'\0'),(6355,764,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6115,'\0'),(6356,764,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6116,'\0'),(6357,764,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6117,'\0'),(6358,764,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6118,'\0'),(6359,764,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6119,'\0'),(6360,764,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6120,'\0'),(6361,764,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6121,'\0'),(6362,764,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6122,'\0'),(6363,764,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6123,'\0'),(6364,764,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6124,'\0'),(6365,764,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6125,'\0'),(6366,764,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6126,'\0'),(6367,764,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6127,'\0'),(6368,764,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6128,'\0'),(6369,764,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6129,'\0'),(6370,764,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6130,'\0'),(6371,764,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6131,'\0'),(6372,764,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6132,'\0'),(6373,764,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6133,'\0'),(6374,764,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6134,'\0'),(6375,764,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6135,'\0'),(6376,764,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6136,'\0'),(6377,764,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6137,'\0'),(6378,764,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6138,'\0'),(6379,765,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:55:35',6139,'\0'),(6380,766,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6140,'\0'),(6381,766,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:55:35',6141,'\0'),(6382,767,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6142,'\0'),(6383,767,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6143,'\0'),(6384,767,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6144,'\0'),(6385,767,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:55:35',6145,'\0'),(6386,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',122,'2016-09-16 08:55:35',6146,'\0'),(6387,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',122,'2016-09-16 08:55:35',6147,'\0'),(6388,768,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6148,'\0'),(6389,768,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6149,'\0'),(6390,768,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6150,'\0'),(6391,768,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6151,'\0'),(6392,768,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6152,'\0'),(6393,768,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6153,'\0'),(6394,768,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6154,'\0'),(6395,768,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6155,'\0'),(6396,768,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6156,'\0'),(6397,768,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6157,'\0'),(6398,768,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6158,'\0'),(6399,768,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6159,'\0'),(6400,768,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6160,'\0'),(6401,768,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6161,'\0'),(6402,768,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6162,'\0'),(6403,768,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6163,'\0'),(6404,768,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6164,'\0'),(6405,768,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6165,'\0'),(6406,768,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6166,'\0'),(6407,769,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6167,'\0'),(6408,769,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6168,'\0'),(6409,769,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6169,'\0'),(6410,769,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:55:35',6170,'\0'),(6411,770,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:35',6171,'\0'),(6412,770,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:55:36',6172,'\0'),(6413,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',123,'2016-09-16 08:55:36',6173,'\0'),(6414,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',123,'2016-09-16 08:55:36',6174,'\0'),(6415,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',123,'2016-09-16 08:55:36',6175,'\0'),(6416,771,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6176,'\0'),(6417,771,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6177,'\0'),(6418,771,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6178,'\0'),(6419,771,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:55:36',6179,'\0'),(6420,772,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6180,'\0'),(6421,772,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6181,'\0'),(6422,772,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6182,'\0'),(6423,772,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:55:36',6183,'\0'),(6424,772,'BUG_CATEGORY','Bug',NULL,NULL,6183,'en',NULL,'2016-09-16 08:55:36',6184,'\0'),(6425,772,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6183,'en',NULL,'2016-09-16 08:55:36',6185,'\0'),(6426,772,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6183,'en',NULL,'2016-09-16 08:55:36',6186,'\0'),(6427,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',124,'2016-09-16 08:56:05',6187,'\0'),(6428,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',124,'2016-09-16 08:56:05',6188,'\0'),(6429,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',125,'2016-09-16 08:56:05',6189,'\0'),(6430,773,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6190,'\0'),(6431,773,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6191,'\0'),(6432,773,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6192,'\0'),(6433,773,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6193,'\0'),(6434,773,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6194,'\0'),(6435,773,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6195,'\0'),(6436,773,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6196,'\0'),(6437,773,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6197,'\0'),(6438,773,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6198,'\0'),(6439,773,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6199,'\0'),(6440,773,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6200,'\0'),(6441,773,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6201,'\0'),(6442,773,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6202,'\0'),(6443,773,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6203,'\0'),(6444,773,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6204,'\0'),(6445,773,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6205,'\0'),(6446,773,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6206,'\0'),(6447,773,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6207,'\0'),(6448,773,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6208,'\0'),(6449,773,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6209,'\0'),(6450,773,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6210,'\0'),(6451,773,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6211,'\0'),(6452,773,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6212,'\0'),(6453,773,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6213,'\0'),(6454,774,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:05',6214,'\0'),(6455,775,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6215,'\0'),(6456,775,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:05',6216,'\0'),(6457,776,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6217,'\0'),(6458,776,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6218,'\0'),(6459,776,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6219,'\0'),(6460,776,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:05',6220,'\0'),(6461,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',126,'2016-09-16 08:56:05',6221,'\0'),(6462,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',126,'2016-09-16 08:56:05',6222,'\0'),(6463,777,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6223,'\0'),(6464,777,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6224,'\0'),(6465,777,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6225,'\0'),(6466,777,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6226,'\0'),(6467,777,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6227,'\0'),(6468,777,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6228,'\0'),(6469,777,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6229,'\0'),(6470,777,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6230,'\0'),(6471,777,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6231,'\0'),(6472,777,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6232,'\0'),(6473,777,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6233,'\0'),(6474,777,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6234,'\0'),(6475,777,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6235,'\0'),(6476,777,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6236,'\0'),(6477,777,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6237,'\0'),(6478,777,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6238,'\0'),(6479,777,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6239,'\0'),(6480,777,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6240,'\0'),(6481,777,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6241,'\0'),(6482,778,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6242,'\0'),(6483,778,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6243,'\0'),(6484,778,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6244,'\0'),(6485,778,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:05',6245,'\0'),(6486,779,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6246,'\0'),(6487,779,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:05',6247,'\0'),(6488,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',127,'2016-09-16 08:56:05',6248,'\0'),(6489,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',127,'2016-09-16 08:56:05',6249,'\0'),(6490,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',127,'2016-09-16 08:56:05',6250,'\0'),(6491,780,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6251,'\0'),(6492,780,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6252,'\0'),(6493,780,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6253,'\0'),(6494,780,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:05',6254,'\0'),(6495,781,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6255,'\0'),(6496,781,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6256,'\0'),(6497,781,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6257,'\0'),(6498,781,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:05',6258,'\0'),(6499,781,'BUG_CATEGORY','Bug',NULL,NULL,6258,'en',NULL,'2016-09-16 08:56:05',6259,'\0'),(6500,781,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6258,'en',NULL,'2016-09-16 08:56:05',6260,'\0'),(6501,781,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6258,'en',NULL,'2016-09-16 08:56:05',6261,'\0'),(6502,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',128,'2016-09-16 08:56:13',6262,'\0'),(6503,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',128,'2016-09-16 08:56:13',6263,'\0'),(6504,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',129,'2016-09-16 08:56:13',6264,'\0'),(6505,782,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6265,'\0'),(6506,782,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6266,'\0'),(6507,782,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6267,'\0'),(6508,782,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6268,'\0'),(6509,782,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6269,'\0'),(6510,782,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6270,'\0'),(6511,782,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6271,'\0'),(6512,782,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6272,'\0'),(6513,782,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6273,'\0'),(6514,782,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6274,'\0'),(6515,782,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6275,'\0'),(6516,782,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6276,'\0'),(6517,782,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6277,'\0'),(6518,782,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6278,'\0'),(6519,782,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6279,'\0'),(6520,782,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6280,'\0'),(6521,782,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6281,'\0'),(6522,782,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6282,'\0'),(6523,782,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6283,'\0'),(6524,782,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6284,'\0'),(6525,782,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6285,'\0'),(6526,782,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6286,'\0'),(6527,782,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6287,'\0'),(6528,782,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6288,'\0'),(6529,783,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:13',6289,'\0'),(6530,784,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6290,'\0'),(6531,784,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:13',6291,'\0'),(6532,785,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6292,'\0'),(6533,785,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6293,'\0'),(6534,785,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6294,'\0'),(6535,785,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:13',6295,'\0'),(6536,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',130,'2016-09-16 08:56:13',6296,'\0'),(6537,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',130,'2016-09-16 08:56:13',6297,'\0'),(6538,786,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6298,'\0'),(6539,786,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6299,'\0'),(6540,786,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6300,'\0'),(6541,786,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6301,'\0'),(6542,786,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6302,'\0'),(6543,786,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6303,'\0'),(6544,786,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6304,'\0'),(6545,786,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6305,'\0'),(6546,786,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6306,'\0'),(6547,786,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6307,'\0'),(6548,786,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6308,'\0'),(6549,786,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6309,'\0'),(6550,786,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6310,'\0'),(6551,786,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6311,'\0'),(6552,786,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6312,'\0'),(6553,786,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6313,'\0'),(6554,786,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6314,'\0'),(6555,786,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6315,'\0'),(6556,786,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6316,'\0'),(6557,787,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6317,'\0'),(6558,787,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6318,'\0'),(6559,787,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6319,'\0'),(6560,787,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:13',6320,'\0'),(6561,788,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6321,'\0'),(6562,788,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:13',6322,'\0'),(6563,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',131,'2016-09-16 08:56:13',6323,'\0'),(6564,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',131,'2016-09-16 08:56:13',6324,'\0'),(6565,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',131,'2016-09-16 08:56:13',6325,'\0'),(6566,789,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6326,'\0'),(6567,789,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6327,'\0'),(6568,789,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6328,'\0'),(6569,789,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:13',6329,'\0'),(6570,790,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6330,'\0'),(6571,790,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6331,'\0'),(6572,790,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6332,'\0'),(6573,790,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:13',6333,'\0'),(6574,790,'BUG_CATEGORY','Bug',NULL,NULL,6333,'en',NULL,'2016-09-16 08:56:13',6334,'\0'),(6575,790,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6333,'en',NULL,'2016-09-16 08:56:13',6335,'\0'),(6576,790,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6333,'en',NULL,'2016-09-16 08:56:13',6336,'\0'),(6577,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',132,'2016-09-16 08:56:19',6337,'\0'),(6578,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',132,'2016-09-16 08:56:19',6338,'\0'),(6579,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',133,'2016-09-16 08:56:19',6339,'\0'),(6580,791,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6340,'\0'),(6581,791,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6341,'\0'),(6582,791,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6342,'\0'),(6583,791,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6343,'\0'),(6584,791,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6344,'\0'),(6585,791,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6345,'\0'),(6586,791,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6346,'\0'),(6587,791,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6347,'\0'),(6588,791,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6348,'\0'),(6589,791,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6349,'\0'),(6590,791,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6350,'\0'),(6591,791,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6351,'\0'),(6592,791,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6352,'\0'),(6593,791,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6353,'\0'),(6594,791,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6354,'\0'),(6595,791,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6355,'\0'),(6596,791,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6356,'\0'),(6597,791,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6357,'\0'),(6598,791,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6358,'\0'),(6599,791,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6359,'\0'),(6600,791,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6360,'\0'),(6601,791,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6361,'\0'),(6602,791,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6362,'\0'),(6603,791,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6363,'\0'),(6604,792,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:19',6364,'\0'),(6605,793,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6365,'\0'),(6606,793,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:19',6366,'\0'),(6607,794,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6367,'\0'),(6608,794,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6368,'\0'),(6609,794,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6369,'\0'),(6610,794,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:19',6370,'\0'),(6611,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',134,'2016-09-16 08:56:19',6371,'\0'),(6612,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',134,'2016-09-16 08:56:19',6372,'\0'),(6613,795,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6373,'\0'),(6614,795,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6374,'\0'),(6615,795,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6375,'\0'),(6616,795,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6376,'\0'),(6617,795,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6377,'\0'),(6618,795,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6378,'\0'),(6619,795,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6379,'\0'),(6620,795,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6380,'\0'),(6621,795,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6381,'\0'),(6622,795,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6382,'\0'),(6623,795,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6383,'\0'),(6624,795,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6384,'\0'),(6625,795,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6385,'\0'),(6626,795,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6386,'\0'),(6627,795,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6387,'\0'),(6628,795,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6388,'\0'),(6629,795,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6389,'\0'),(6630,795,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6390,'\0'),(6631,795,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6391,'\0'),(6632,796,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6392,'\0'),(6633,796,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6393,'\0'),(6634,796,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6394,'\0'),(6635,796,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:19',6395,'\0'),(6636,797,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6396,'\0'),(6637,797,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:19',6397,'\0'),(6638,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',135,'2016-09-16 08:56:19',6398,'\0'),(6639,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',135,'2016-09-16 08:56:19',6399,'\0'),(6640,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',135,'2016-09-16 08:56:19',6400,'\0'),(6641,798,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6401,'\0'),(6642,798,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6402,'\0'),(6643,798,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6403,'\0'),(6644,798,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:19',6404,'\0'),(6645,799,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6405,'\0'),(6646,799,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6406,'\0'),(6647,799,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6407,'\0'),(6648,799,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:19',6408,'\0'),(6649,799,'BUG_CATEGORY','Bug',NULL,NULL,6408,'en',NULL,'2016-09-16 08:56:19',6409,'\0'),(6650,799,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6408,'en',NULL,'2016-09-16 08:56:19',6410,'\0'),(6651,799,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6408,'en',NULL,'2016-09-16 08:56:19',6411,'\0'),(6652,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',136,'2016-09-16 08:56:26',6412,'\0'),(6653,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',136,'2016-09-16 08:56:26',6413,'\0'),(6654,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',137,'2016-09-16 08:56:26',6414,'\0'),(6655,800,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6415,'\0'),(6656,800,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6416,'\0'),(6657,800,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6417,'\0'),(6658,800,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6418,'\0'),(6659,800,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6419,'\0'),(6660,800,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6420,'\0'),(6661,800,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6421,'\0'),(6662,800,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6422,'\0'),(6663,800,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6423,'\0'),(6664,800,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6424,'\0'),(6665,800,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6425,'\0'),(6666,800,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6426,'\0'),(6667,800,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6427,'\0'),(6668,800,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6428,'\0'),(6669,800,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6429,'\0'),(6670,800,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6430,'\0'),(6671,800,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6431,'\0'),(6672,800,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6432,'\0'),(6673,800,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6433,'\0'),(6674,800,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6434,'\0'),(6675,800,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6435,'\0'),(6676,800,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6436,'\0'),(6677,800,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6437,'\0'),(6678,800,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6438,'\0'),(6679,801,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:26',6439,'\0'),(6680,802,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6440,'\0'),(6681,802,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:26',6441,'\0'),(6682,803,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6442,'\0'),(6683,803,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6443,'\0'),(6684,803,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6444,'\0'),(6685,803,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:26',6445,'\0'),(6686,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',138,'2016-09-16 08:56:26',6446,'\0'),(6687,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',138,'2016-09-16 08:56:26',6447,'\0'),(6688,804,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6448,'\0'),(6689,804,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6449,'\0'),(6690,804,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6450,'\0'),(6691,804,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6451,'\0'),(6692,804,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6452,'\0'),(6693,804,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6453,'\0'),(6694,804,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6454,'\0'),(6695,804,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6455,'\0'),(6696,804,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6456,'\0'),(6697,804,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6457,'\0'),(6698,804,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6458,'\0'),(6699,804,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6459,'\0'),(6700,804,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6460,'\0'),(6701,804,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6461,'\0'),(6702,804,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6462,'\0'),(6703,804,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6463,'\0'),(6704,804,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6464,'\0'),(6705,804,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6465,'\0'),(6706,804,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6466,'\0'),(6707,805,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6467,'\0'),(6708,805,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6468,'\0'),(6709,805,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6469,'\0'),(6710,805,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:26',6470,'\0'),(6711,806,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6471,'\0'),(6712,806,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:26',6472,'\0'),(6713,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',139,'2016-09-16 08:56:26',6473,'\0'),(6714,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',139,'2016-09-16 08:56:26',6474,'\0'),(6715,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',139,'2016-09-16 08:56:26',6475,'\0'),(6716,807,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6476,'\0'),(6717,807,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6477,'\0'),(6718,807,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6478,'\0'),(6719,807,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:26',6479,'\0'),(6720,808,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6480,'\0'),(6721,808,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6481,'\0'),(6722,808,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6482,'\0'),(6723,808,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:26',6483,'\0'),(6724,808,'BUG_CATEGORY','Bug',NULL,NULL,6483,'en',NULL,'2016-09-16 08:56:26',6484,'\0'),(6725,808,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6483,'en',NULL,'2016-09-16 08:56:26',6485,'\0'),(6726,808,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6483,'en',NULL,'2016-09-16 08:56:26',6486,'\0'),(6727,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',140,'2016-09-16 08:56:34',6487,'\0'),(6728,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',140,'2016-09-16 08:56:34',6488,'\0'),(6729,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',141,'2016-09-16 08:56:34',6489,'\0'),(6730,809,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6490,'\0'),(6731,809,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6491,'\0'),(6732,809,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6492,'\0'),(6733,809,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6493,'\0'),(6734,809,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6494,'\0'),(6735,809,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6495,'\0'),(6736,809,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6496,'\0'),(6737,809,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6497,'\0'),(6738,809,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6498,'\0'),(6739,809,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6499,'\0'),(6740,809,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6500,'\0'),(6741,809,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6501,'\0'),(6742,809,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6502,'\0'),(6743,809,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6503,'\0'),(6744,809,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6504,'\0'),(6745,809,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6505,'\0'),(6746,809,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6506,'\0'),(6747,809,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6507,'\0'),(6748,809,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6508,'\0'),(6749,809,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6509,'\0'),(6750,809,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6510,'\0'),(6751,809,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6511,'\0'),(6752,809,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6512,'\0'),(6753,809,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6513,'\0'),(6754,810,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:34',6514,'\0'),(6755,811,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6515,'\0'),(6756,811,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:34',6516,'\0'),(6757,812,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6517,'\0'),(6758,812,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6518,'\0'),(6759,812,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6519,'\0'),(6760,812,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:34',6520,'\0'),(6761,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',142,'2016-09-16 08:56:34',6521,'\0'),(6762,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',142,'2016-09-16 08:56:34',6522,'\0'),(6763,813,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6523,'\0'),(6764,813,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6524,'\0'),(6765,813,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6525,'\0'),(6766,813,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6526,'\0'),(6767,813,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6527,'\0'),(6768,813,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6528,'\0'),(6769,813,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6529,'\0'),(6770,813,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6530,'\0'),(6771,813,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6531,'\0'),(6772,813,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6532,'\0'),(6773,813,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6533,'\0'),(6774,813,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6534,'\0'),(6775,813,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6535,'\0'),(6776,813,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6536,'\0'),(6777,813,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6537,'\0'),(6778,813,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6538,'\0'),(6779,813,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6539,'\0'),(6780,813,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6540,'\0'),(6781,813,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6541,'\0'),(6782,814,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6542,'\0'),(6783,814,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6543,'\0'),(6784,814,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6544,'\0'),(6785,814,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:34',6545,'\0'),(6786,815,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6546,'\0'),(6787,815,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:34',6547,'\0'),(6788,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',143,'2016-09-16 08:56:34',6548,'\0'),(6789,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',143,'2016-09-16 08:56:34',6549,'\0'),(6790,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',143,'2016-09-16 08:56:34',6550,'\0'),(6791,816,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6551,'\0'),(6792,816,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6552,'\0'),(6793,816,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6553,'\0'),(6794,816,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:34',6554,'\0'),(6795,817,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6555,'\0'),(6796,817,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6556,'\0'),(6797,817,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6557,'\0'),(6798,817,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:34',6558,'\0'),(6799,817,'BUG_CATEGORY','Bug',NULL,NULL,6558,'en',NULL,'2016-09-16 08:56:34',6559,'\0'),(6800,817,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6558,'en',NULL,'2016-09-16 08:56:34',6560,'\0'),(6801,817,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6558,'en',NULL,'2016-09-16 08:56:34',6561,'\0'),(6802,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',144,'2016-09-16 08:56:41',6562,'\0'),(6803,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',144,'2016-09-16 08:56:41',6563,'\0'),(6804,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',145,'2016-09-16 08:56:41',6564,'\0'),(6805,818,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6565,'\0'),(6806,818,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6566,'\0'),(6807,818,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6567,'\0'),(6808,818,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6568,'\0'),(6809,818,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6569,'\0'),(6810,818,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6570,'\0'),(6811,818,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6571,'\0'),(6812,818,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6572,'\0'),(6813,818,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6573,'\0'),(6814,818,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6574,'\0'),(6815,818,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6575,'\0'),(6816,818,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6576,'\0'),(6817,818,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6577,'\0'),(6818,818,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6578,'\0'),(6819,818,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6579,'\0'),(6820,818,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6580,'\0'),(6821,818,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6581,'\0'),(6822,818,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6582,'\0'),(6823,818,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6583,'\0'),(6824,818,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6584,'\0'),(6825,818,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6585,'\0'),(6826,818,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6586,'\0'),(6827,818,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6587,'\0'),(6828,818,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6588,'\0'),(6829,819,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:41',6589,'\0'),(6830,820,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6590,'\0'),(6831,820,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:41',6591,'\0'),(6832,821,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6592,'\0'),(6833,821,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6593,'\0'),(6834,821,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6594,'\0'),(6835,821,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:41',6595,'\0'),(6836,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',146,'2016-09-16 08:56:41',6596,'\0'),(6837,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',146,'2016-09-16 08:56:41',6597,'\0'),(6838,822,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6598,'\0'),(6839,822,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6599,'\0'),(6840,822,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6600,'\0'),(6841,822,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6601,'\0'),(6842,822,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6602,'\0'),(6843,822,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6603,'\0'),(6844,822,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6604,'\0'),(6845,822,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6605,'\0'),(6846,822,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6606,'\0'),(6847,822,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6607,'\0'),(6848,822,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6608,'\0'),(6849,822,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6609,'\0'),(6850,822,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6610,'\0'),(6851,822,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6611,'\0'),(6852,822,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6612,'\0'),(6853,822,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6613,'\0'),(6854,822,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6614,'\0'),(6855,822,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6615,'\0'),(6856,822,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6616,'\0'),(6857,823,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6617,'\0'),(6858,823,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6618,'\0'),(6859,823,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6619,'\0'),(6860,823,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:41',6620,'\0'),(6861,824,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6621,'\0'),(6862,824,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:41',6622,'\0'),(6863,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',147,'2016-09-16 08:56:41',6623,'\0'),(6864,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',147,'2016-09-16 08:56:41',6624,'\0'),(6865,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',147,'2016-09-16 08:56:41',6625,'\0'),(6866,825,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6626,'\0'),(6867,825,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6627,'\0'),(6868,825,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6628,'\0'),(6869,825,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:41',6629,'\0'),(6870,826,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6630,'\0'),(6871,826,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6631,'\0'),(6872,826,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6632,'\0'),(6873,826,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:41',6633,'\0'),(6874,826,'BUG_CATEGORY','Bug',NULL,NULL,6633,'en',NULL,'2016-09-16 08:56:41',6634,'\0'),(6875,826,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6633,'en',NULL,'2016-09-16 08:56:41',6635,'\0'),(6876,826,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6633,'en',NULL,'2016-09-16 08:56:41',6636,'\0'),(6877,NULL,'reviewActive','1.0',NULL,NULL,NULL,'en',148,'2016-09-16 08:56:49',6637,'\0'),(6878,NULL,'mainColor','#00ff00',NULL,NULL,NULL,'en',148,'2016-09-16 08:56:49',6638,'\0'),(6879,NULL,'aKey','aPushSpecificValue',NULL,NULL,NULL,'en',149,'2016-09-16 08:56:49',6639,'\0'),(6880,827,'maxLength','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6640,'\0'),(6881,827,'title','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6641,'\0'),(6882,827,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6642,'\0'),(6883,827,'label','Please write about your problem',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6643,'\0'),(6884,827,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6644,'\0'),(6885,827,'labelColor','#353535',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6645,'\0'),(6886,827,'labelFontSize','15.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6646,'\0'),(6887,827,'textareaFontColor','#7A7A7A',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6647,'\0'),(6888,827,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6648,'\0'),(6889,827,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6649,'\0'),(6890,827,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6650,'\0'),(6891,827,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6651,'\0'),(6892,827,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6652,'\0'),(6893,827,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6653,'\0'),(6894,827,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6654,'\0'),(6895,827,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6655,'\0'),(6896,827,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6656,'\0'),(6897,827,'fieldFontSize','13.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6657,'\0'),(6898,827,'fieldFontType','italic',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6658,'\0'),(6899,827,'fieldTextAlignment','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6659,'\0'),(6900,827,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6660,'\0'),(6901,827,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6661,'\0'),(6902,827,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6662,'\0'),(6903,827,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6663,'\0'),(6904,828,'maxTime','30.0','30.0','\0',NULL,'en',NULL,'2016-09-16 08:56:49',6664,'\0'),(6905,829,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6665,'\0'),(6906,829,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:49',6666,'\0'),(6907,830,'title','Rate your user experience on this page',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6667,'\0'),(6908,830,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6668,'\0'),(6909,830,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6669,'\0'),(6910,830,'defaultRating','3.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:49',6670,'\0'),(6911,NULL,'likelihood','0.4',NULL,NULL,NULL,'en',150,'2016-09-16 08:56:49',6671,'\0'),(6912,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',150,'2016-09-16 08:56:49',6672,'\0'),(6913,831,'maxLength','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6673,'\0'),(6914,831,'title','Feedback Pull',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6674,'\0'),(6915,831,'hint','Please enter your feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6675,'\0'),(6916,831,'label','Feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6676,'\0'),(6917,831,'labelPositioning','left',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6677,'\0'),(6918,831,'labelFontSize','12.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6678,'\0'),(6919,831,'mandatory','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6679,'\0'),(6920,831,'mandatoryReminder','Please fill in the text field',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6680,'\0'),(6921,831,'undoEnabled','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6681,'\0'),(6922,831,'undoSteps','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6682,'\0'),(6923,831,'clearInput','0.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6683,'\0'),(6924,831,'borderWidth','2.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6684,'\0'),(6925,831,'borderColor','#000000',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6685,'\0'),(6926,831,'fieldWidth','200.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6686,'\0'),(6927,831,'fieldHeight','50.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6687,'\0'),(6928,831,'fieldBackgroundColor','#ffffff',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6688,'\0'),(6929,831,'maxLengthVisible','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6689,'\0'),(6930,831,'validationRegex','.',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6690,'\0'),(6931,831,'validateOnSkip','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6691,'\0'),(6932,832,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6692,'\0'),(6933,832,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6693,'\0'),(6934,832,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6694,'\0'),(6935,832,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:49',6695,'\0'),(6936,833,'title','Title for screenshot feedback',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6696,'\0'),(6937,833,'defaultPicture','lastScreenshot','noImage','',NULL,'en',NULL,'2016-09-16 08:56:49',6697,'\0'),(6938,NULL,'reviewActive','0.0',NULL,NULL,NULL,'en',151,'2016-09-16 08:56:49',6698,'\0'),(6939,NULL,'likelihood','0.2',NULL,NULL,NULL,'en',151,'2016-09-16 08:56:49',6699,'\0'),(6940,NULL,'askOnAppStartup','0.0',NULL,NULL,NULL,'en',151,'2016-09-16 08:56:49',6700,'\0'),(6941,834,'title','Rate your user experience',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6701,'\0'),(6942,834,'ratingIcon','star',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6702,'\0'),(6943,834,'maxRating','5.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6703,'\0'),(6944,834,'defaultRating','2.0','0.0','\0',NULL,'en',NULL,'2016-09-16 08:56:49',6704,'\0'),(6945,835,'title','Please choose from the following categories',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6705,'\0'),(6946,835,'ownAllowed','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6706,'\0'),(6947,835,'multiple','1.0',NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6707,'\0'),(6948,835,'options',NULL,NULL,NULL,NULL,'en',NULL,'2016-09-16 08:56:49',6708,'\0'),(6949,835,'BUG_CATEGORY','Bug',NULL,NULL,6708,'en',NULL,'2016-09-16 08:56:49',6709,'\0'),(6950,835,'FEATURE_REQUEST_CATEGORY','Feature Request',NULL,NULL,6708,'en',NULL,'2016-09-16 08:56:49',6710,'\0'),(6951,835,'GENERAL_CATEGORY','General Feedback',NULL,NULL,6708,'en',NULL,'2016-09-16 08:56:49',6711,'\0');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` VALUES (1),(2);
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
  `name` varchar(45) NOT NULL,
  `user_groups_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_user_groups_history_1_idx` (`user_groups_id`),
  CONSTRAINT `fk_user_groups_history_1` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups_history`
--

LOCK TABLES `user_groups_history` WRITE;
/*!40000 ALTER TABLE `user_groups_history` DISABLE KEYS */;
INSERT INTO `user_groups_history` VALUES (1,'default',1,'2016-09-16 13:08:07',''),(2,'power_users',2,'2016-09-19 11:43:02','');
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
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  `users_id` int(11) NOT NULL,
  `user_groups_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_history_2_idx` (`user_groups_id`),
  KEY `fk_users_history_1_idx` (`users_id`),
  CONSTRAINT `fk_users_history_1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_history_2` FOREIGN KEY (`user_groups_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_history`
--

LOCK TABLES `users_history` WRITE;
/*!40000 ALTER TABLE `users_history` DISABLE KEYS */;
INSERT INTO `users_history` VALUES (1,'u1234','2016-09-16 14:42:23','',1,1);
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

-- Dump completed on 2016-11-11 12:15:25
