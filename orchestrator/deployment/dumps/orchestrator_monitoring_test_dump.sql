CREATE DATABASE  IF NOT EXISTS `monitoring_orchestrator` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `monitoring_orchestrator`;

/*Drop all tables*/
DROP TABLE IF EXISTS `monitor_configuration_history`;
DROP TABLE IF EXISTS `monitor_configuration`;
DROP TABLE IF EXISTS `monitor_tool_history`;
DROP TABLE IF EXISTS `monitor_tool`;
DROP TABLE IF EXISTS `monitor_type_history`;
DROP TABLE IF EXISTS `monitor_type`;

CREATE TABLE `monitor_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_type` WRITE;
/*!40000 ALTER TABLE `monitor_type` DISABLE KEYS */;
INSERT INTO `monitor_type` VALUES (1),(2);
/*!40000 ALTER TABLE `monitor_type` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `monitor_type_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_type_id` int(11) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY(`id`),
  KEY `fk_monitor_type_history_idx` (`monitor_type_id`),
  CONSTRAINT `fk_monitor_type_history_1` FOREIGN KEY (`monitor_type_id`) REFERENCES `monitor_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_type_history` WRITE;
/*!40000 ALTER TABLE `monitor_type_history` DISABLE KEYS */;
INSERT INTO `monitor_type_history` VALUES 
(1,'SocialNetworks','2016-11-01 13:55:06.000',1,'\0'),
(2,'MarketPlaces','2016-11-01 13:55:06.000',2,'\0');
/*!40000 ALTER TABLE `monitor_type_history` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `monitor_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_tool` WRITE;
/*!40000 ALTER TABLE `monitor_tool` DISABLE KEYS */;
INSERT INTO `monitor_tool` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `monitor_tool` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `monitor_tool_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_tool_id` int(11) NOT NULL,
  `monitor_type_id` int(11) NOT NULL,
  `monitor_name` varchar(255) NOT NULL,
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY(`id`),
  KEY `fk_monitor_tool_history_idx` (`monitor_tool_id`),
  KEY `fk_monitor_type_id` (`monitor_type_id`),
  CONSTRAINT `fk_monitor_tool_history_1` FOREIGN KEY (`monitor_tool_id`) REFERENCES `monitor_tool` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_type_id_1` FOREIGN KEY (`monitor_type_id`) REFERENCES `monitor_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_tool_history` WRITE;
/*!40000 ALTER TABLE `monitor_tool_history` DISABLE KEYS */;
INSERT INTO `monitor_tool_history` VALUES 
(1,'TwitterAPI','2016-11-01 13:55:06.000',1,1,'Twitter','\0'),
(2,'GooglePlayAPI','2016-11-01 13:55:06.000',2,2,'GooglePlay','\0'),
(3,'AppTweak-GooglePlay','2016-11-01 13:55:06.000',3,2,'GooglePlay','\0'),
(4,'AppStoreAPI','2016-11-01 13:55:06.000',4,2,'AppStore','\0'),
(5,'AppTweak-GooglePlay','2016-11-01 13:55:06.000',5,2,'AppStore','\0');
/*!40000 ALTER TABLE `monitor_tool_history` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `monitor_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_configuration` WRITE;
/*!40000 ALTER TABLE `monitor_configuration` DISABLE KEYS */;
INSERT INTO `monitor_configuration` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `monitor_configuration` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `monitor_configuration_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_configuration_id` int(11) NOT NULL,
  `monitor_tool_id` int(11) NOT NULL,
  `config_sender` varchar(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  `time_slot` varchar(255) NOT NULL,
  `kafka_endpoint` varchar(255) NOT NULL,
  `kafka_topic` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `keyword_expression` varchar(255),
  `app_id` varchar(255),
  `package_name` varchar(255),
  `current_version` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY(`id`),
  KEY `fk_monitor_tool_id` (`monitor_tool_id`),
  KEY `fk_monitor_configuration_history_idx` (`monitor_configuration_id`),
  CONSTRAINT `fk_monitor_tool_id_1` FOREIGN KEY (`monitor_tool_id`) REFERENCES `monitor_tool` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_configuration_history_1` FOREIGN KEY (`monitor_configuration_id`) REFERENCES `monitor_configuration` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

LOCK TABLES `monitor_configuration_history` WRITE;
/*!40000 ALTER TABLE `monitor_configuration_history` DISABLE KEYS */;
INSERT INTO `monitor_configuration_history` VALUES 
(1,'2016-11-01 13:55:06.000',1,1,'WP4','2016-11-01 13:55:06.000','30','http://localhost:9092','Twitter','ON','twitter AND facebook',null,null,'\0'),
(2,'2016-11-01 13:55:06.000',2,2,'WP4','2016-11-01 13:55:06.000','60','http://localhost:9092','Twitter','OFF','streaming AND olympics',null,null,'\0'),
(3,'2016-11-01 13:55:06.000',3,3,'WP4','2016-11-01 13:55:06.000','120','http://localhost:9092','GooglePlay1','ON',null,null,'com.adobe.reader','\0'),
(4,'2016-11-01 13:55:06.000',4,4,'WP4','2016-11-01 13:55:06.000','100','http://localhost:9092','GooglePlay2','ON',null,null,'com.adobe.photoshop','\0'),
(5,'2016-11-01 13:55:06.000',5,5,'WP4','2016-11-01 13:55:06.000','50','http://localhost:9092','AppStore-x','ON',null,'239abDh829',null,'\0');
/*!40000 ALTER TABLE `monitor_configuration_history` ENABLE KEYS */;
UNLOCK TABLES;