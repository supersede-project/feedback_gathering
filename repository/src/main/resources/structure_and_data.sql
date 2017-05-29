/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : supersede_orchestrator_spring

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 05/29/2017 13:42:06 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `api_user`
-- ----------------------------
DROP TABLE IF EXISTS `api_user`;
CREATE TABLE `api_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_scb81k0sobpewfaxhwyquccop` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `api_user`
-- ----------------------------
BEGIN;
INSERT INTO `api_user` VALUES ('1', 'admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy');
COMMIT;

-- ----------------------------
--  Table structure for `api_user_api_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `api_user_api_user_role`;
CREATE TABLE `api_user_api_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_user_role` int(11) DEFAULT NULL,
  `api_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmh0ci62ckvoi95vgx5nj4n0a3` (`api_user_id`),
  CONSTRAINT `FKmh0ci62ckvoi95vgx5nj4n0a3` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `api_user_api_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `api_user_api_user_role` VALUES ('1', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `application`
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `state` int(11) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `general_configuration_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgr1ty9ysqn7wjxja8s3oe3c7n` (`general_configuration_id`),
  CONSTRAINT `FKgr1ty9ysqn7wjxja8s3oe3c7n` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `application`
-- ----------------------------
BEGIN;
INSERT INTO `application` VALUES ('1', '2017-05-28 20:20:39', 'App 1', '1', '2017-05-28 20:20:46', '1');
COMMIT;

-- ----------------------------
--  Table structure for `configuration`
-- ----------------------------
DROP TABLE IF EXISTS `configuration`;
CREATE TABLE `configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  `general_configuration_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdmg5qpjmd9vjc35xqu229ifer` (`application_id`),
  KEY `FKh7to010nk73crcdcx9asyb0u0` (`general_configuration_id`),
  CONSTRAINT `FKdmg5qpjmd9vjc35xqu229ifer` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
  CONSTRAINT `FKh7to010nk73crcdcx9asyb0u0` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `configuration`
-- ----------------------------
BEGIN;
INSERT INTO `configuration` VALUES ('1', null, 'Conf 1', '0', null, '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `configuration_mechanism`
-- ----------------------------
DROP TABLE IF EXISTS `configuration_mechanism`;
CREATE TABLE `configuration_mechanism` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `order` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `configuration_id` bigint(20) DEFAULT NULL,
  `mechanism_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcwcp85xb3nhmb3wfo9gaqwm2f` (`configuration_id`),
  KEY `FKawk60ramkm20rwvamslgmfbd5` (`mechanism_id`),
  CONSTRAINT `FKawk60ramkm20rwvamslgmfbd5` FOREIGN KEY (`mechanism_id`) REFERENCES `mechanism` (`id`),
  CONSTRAINT `FKcwcp85xb3nhmb3wfo9gaqwm2f` FOREIGN KEY (`configuration_id`) REFERENCES `configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `configuration_mechanism`
-- ----------------------------
BEGIN;
INSERT INTO `configuration_mechanism` VALUES ('1', b'1', null, '1', null, '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `configuration_user_group`
-- ----------------------------
DROP TABLE IF EXISTS `configuration_user_group`;
CREATE TABLE `configuration_user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `configuration_id` bigint(20) DEFAULT NULL,
  `user_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnn84fh468kguhrgxwyusc7ob5` (`configuration_id`),
  KEY `FK4bsbu6bghmv7dow7y82qn45gx` (`user_group_id`),
  CONSTRAINT `FK4bsbu6bghmv7dow7y82qn45gx` FOREIGN KEY (`user_group_id`) REFERENCES `user_group` (`id`),
  CONSTRAINT `FKnn84fh468kguhrgxwyusc7ob5` FOREIGN KEY (`configuration_id`) REFERENCES `configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `general_configuration`
-- ----------------------------
DROP TABLE IF EXISTS `general_configuration`;
CREATE TABLE `general_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `general_configuration`
-- ----------------------------
BEGIN;
INSERT INTO `general_configuration` VALUES ('1', '2017-05-28 22:33:15', 'App 1 General Configuration', '2017-05-28 22:33:24');
COMMIT;

-- ----------------------------
--  Table structure for `mechanism`
-- ----------------------------
DROP TABLE IF EXISTS `mechanism`;
CREATE TABLE `mechanism` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `mechanism`
-- ----------------------------
BEGIN;
INSERT INTO `mechanism` VALUES ('1', null, '0', null);
COMMIT;

-- ----------------------------
--  Table structure for `monitor_configuration`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_configuration`;
CREATE TABLE `monitor_configuration` (
  `monitor_configuration_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(255) DEFAULT NULL,
  `config_sender` varchar(255) NOT NULL,
  `kafka_endpoint` varchar(255) NOT NULL,
  `kafka_topic` varchar(255) NOT NULL,
  `keyword_expression` varchar(255) DEFAULT NULL,
  `monitor_manager_configuration_id` int(11) DEFAULT NULL,
  `monitor_tool_id` int(11) DEFAULT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  `state` varchar(255) NOT NULL,
  `time_slot` varchar(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  PRIMARY KEY (`monitor_configuration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `monitor_tool`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_tool`;
CREATE TABLE `monitor_tool` (
  `monitor_tool_id` int(11) NOT NULL AUTO_INCREMENT,
  `monitor_name` varchar(255) DEFAULT NULL,
  `monitor_type_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`monitor_tool_id`),
  UNIQUE KEY `UK_h5regvm6pni3nkqxtw35yeipc` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `monitor_type`
-- ----------------------------
DROP TABLE IF EXISTS `monitor_type`;
CREATE TABLE `monitor_type` (
  `monitor_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`monitor_type_id`),
  UNIQUE KEY `UK_88x2tddwudgea4nt27qgjwmnu` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `parameter`
-- ----------------------------
DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `general_configuration_id` bigint(20) DEFAULT NULL,
  `mechanism_id` bigint(20) DEFAULT NULL,
  `parent_parameter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmb21havly9lxv0e9ni7x038f5` (`general_configuration_id`),
  KEY `FKfps9auc9gm7jl1rtmbip6f9qc` (`mechanism_id`),
  KEY `FKkd28xfrmcnn0mb00m1qvk5w39` (`parent_parameter_id`),
  CONSTRAINT `FKfps9auc9gm7jl1rtmbip6f9qc` FOREIGN KEY (`mechanism_id`) REFERENCES `mechanism` (`id`),
  CONSTRAINT `FKkd28xfrmcnn0mb00m1qvk5w39` FOREIGN KEY (`parent_parameter_id`) REFERENCES `parameter` (`id`),
  CONSTRAINT `FKmb21havly9lxv0e9ni7x038f5` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `parameter`
-- ----------------------------
BEGIN;
INSERT INTO `parameter` VALUES ('1', null, 'title', 'en', null, 'Title', null, '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_identification` varchar(255) DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  `user_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKloqygibpr0cjij9v6hm7a5cus` (`application_id`),
  KEY `FKd5uhmsqhax1l70pck9lmgphjr` (`user_group_id`),
  CONSTRAINT `FKd5uhmsqhax1l70pck9lmgphjr` FOREIGN KEY (`user_group_id`) REFERENCES `user_group` (`id`),
  CONSTRAINT `FKloqygibpr0cjij9v6hm7a5cus` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7nf0lyfnicwik0tpkd9lmk4ig` (`application_id`),
  CONSTRAINT `FK7nf0lyfnicwik0tpkd9lmk4ig` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
