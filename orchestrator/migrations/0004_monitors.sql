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

CREATE TABLE `monitor_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

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

CREATE TABLE `monitor_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `monitor_configuration_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_configuration_id` int(11) NOT NULL,
  `monitor_tool_id` int(11) NOT NULL,
  `monitor_manager_configuration_id` int(11) NOT NULL,
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