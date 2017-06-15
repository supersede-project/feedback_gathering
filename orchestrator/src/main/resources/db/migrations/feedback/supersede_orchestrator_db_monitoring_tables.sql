/*
 Navicat Premium Data Transfer

 Source Server         : Supersede
 Source Server Type    : MySQL
 Source Server Version : 50634
 Source Host           : localhost
 Source Database       : monitoring_orchestrator

 Target Server Type    : MySQL
 Target Server Version : 50634
 File Encoding         : utf-8

 Date: 06/15/2017 11:15:18 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `monitor_configuration`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_configuration`;
CREATE TABLE `monitor_configuration` (
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
  `keyword_expression` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_monitor_tool_id` (`monitor_tool_id`),
  KEY `fk_monitor_configuration_idx` (`monitor_configuration_id`),
  CONSTRAINT `fk_monitor_configuration_1` FOREIGN KEY (`monitor_configuration_id`) REFERENCES `monitor_configuration` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_tool_id_1` FOREIGN KEY (`monitor_tool_id`) REFERENCES `monitor_tool` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `monitor_configuration`
-- ----------------------------
BEGIN;
INSERT INTO `monitor_configuration` VALUES ('7', '2017-02-10 13:40:14.888', '6', '1', '1', 'WP4', 'Sat June 08 02:16:57 2016', '500', 'http://localhost:9092', 'olympicGamesTwitterMonitoring', 'active', 'keyword1 AND keyword2', null, null), ('8', '2017-02-10 13:40:29.761', '7', '1', '2', 'WP4', 'Sat June 08 02:16:57 2016', '500', 'http://localhost:9092', 'olympicGamesTwitterMonitoring', 'active', 'keyword1 AND keyword2', null, null), ('9', '2017-03-13 16:54:50.080', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '300', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword1 AND keyword2', null, null), ('10', '2017-03-13 16:55:12.327', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '320', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword3 AND keyword4', null, null), ('11', '2017-03-13 16:55:34.557', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '350', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword5 AND keyword6', null, null), ('12', '2017-03-13 16:55:49.150', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '320', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword7 AND keyword7', null, null), ('13', '2017-03-13 16:56:08.517', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '310', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword2 AND keyword2', null, null), ('14', '2017-03-13 16:56:21.401', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '320', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword5 AND keyword2', null, null), ('15', '2017-03-13 16:56:48.862', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '3420', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword8 AND keyword8', null, null), ('16', '2017-03-13 16:57:06.716', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '340', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword9 AND keyword9', null, null), ('17', '2017-03-13 16:57:24.796', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '300', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword4 AND keyword4', null, null), ('18', '2017-03-13 16:57:47.132', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '304', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword4 AND keyword AND keyword4', null, null), ('19', '2017-03-13 16:58:00.279', '8', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '304', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword4 AND keyword AND keyword5', null, null), ('20', '2017-03-17 10:02:24.661', '6', '1', '1', 'manual', 'Sat June 08 02:16:57 2016', '304', 'http://localhost:9092', 'ignoreMe', 'active', 'keyword4 AND keyword AND keyword5', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `monitor_tool`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_tool`;
CREATE TABLE `monitor_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_tool_id` int(11) NOT NULL,
  `monitor_type_id` int(11) NOT NULL,
  `monitor_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_monitor_tool_idx` (`monitor_tool_id`),
  KEY `fk_monitor_type_id` (`monitor_type_id`),
  CONSTRAINT `fk_monitor_tool_1` FOREIGN KEY (`monitor_tool_id`) REFERENCES `monitor_tool` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_monitor_type_id_1` FOREIGN KEY (`monitor_type_id`) REFERENCES `monitor_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `monitor_tool`
-- ----------------------------
BEGIN;
INSERT INTO `monitor_tool` VALUES ('1', 'TwitterAPI', '2016-11-01 13:55:06.000', '1', '1', 'Twitter'), ('2', 'GooglePlayAPI', '2016-11-01 13:55:06.000', '2', '2', 'GooglePlay'), ('3', 'AppTweak-GooglePlay', '2016-11-01 13:55:06.000', '3', '2', 'GooglePlay'), ('4', 'AppStoreAPI', '2016-11-01 13:55:06.000', '4', '2', 'AppStore'), ('5', 'AppTweak-AppStore', '2016-11-01 13:55:06.000', '5', '2', 'AppStore'), ('7', 'TwitterAPIForTest3', '2017-02-20 17:49:06.432', '7', '5', 'TwitterAPIMonitorForTest3');
COMMIT;

-- ----------------------------
--  Table structure for `monitor_type`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_type`;
CREATE TABLE `monitor_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_monitor_type_idx` (`monitor_type_id`),
  CONSTRAINT `fk_monitor_type_1` FOREIGN KEY (`monitor_type_id`) REFERENCES `monitor_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `monitor_type`
-- ----------------------------
BEGIN;
INSERT INTO `monitor_type` VALUES ('1', 'SocialNetworks', '2016-11-01 13:55:06.000', '1'), ('2', 'MarketPlaces', '2016-11-01 13:55:06.000', '2'), ('5', 'MonitorTypeForTest3', '2017-02-20 17:49:06.409', '5');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
