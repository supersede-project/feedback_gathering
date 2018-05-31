/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : supersede_repository_spring_test

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 06/20/2017 21:20:25 PM
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
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `api_user_permission`
-- ----------------------------
DROP TABLE IF EXISTS `api_user_permission`;
CREATE TABLE `api_user_permission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `api_user_id` bigint(11) NOT NULL,
  `application_id` bigint(11) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_user_id` (`api_user_id`),
  CONSTRAINT `api_user_fk` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=big5;

-- ----------------------------
--  Table structure for `attachment_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `attachment_feedback`;
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

-- ----------------------------
--  Table structure for `audio_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `audio_feedback`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `category_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `category_feedback`;
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

-- ----------------------------
--  Table structure for `context_information`
-- ----------------------------
DROP TABLE IF EXISTS `context_information`;
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
  PRIMARY KEY (`id`),
  KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`),
  CONSTRAINT `FKgjes53m9wrcpnnq0kl898yjul` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `feedback`
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `configuration_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_identification` varchar(255) DEFAULT NULL,
  `android_user_id` BIGINT(20) DEFAULT NULL,
  `is_public` BIT(1) DEFAULT NULL,
  `feedback_status` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`application_id`),
  FOREIGN KEY (`android_user_id`) REFERENCES `android_user` (`id`),
  FOREIGN KEY (`feedback_status`) REFERENCES `feedback_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `feedback` VALUES ('1', '1', '2', '2017-05-31 19:57:54', 'en', 'Test feedback', '2017-05-31 19:58:05', 'u123456', null, b'1', null), ('2', '1', '1', '2017-06-19 12:39:06', null, 'Feedback', null, '99999999', null, b'1', null), ('3', '1', '1', '2017-06-19 13:40:45', null, 'Feedback', null, '99999999', null, b'1', null), ('4', '1', '1', '2017-06-19 13:41:47', null, 'Feedback', null, '99999999', null, b'1', null), ('5', '1', '1', '2017-06-19 13:43:45', null, 'Feedback', null, '99999999', null, b'1', null), ('6', '1', '1', '2017-06-19 13:47:02', 'en', 'Feedback', null, '99999999', null, b'1', null), ('7', '1', '1', '2017-06-19 13:51:53', 'en', 'Feedback', null, '99999999', null, b'1', null), ('8', '1', '1', '2017-06-19 13:52:26', 'en', 'Feedback', null, '99999999', null, b'1', null), ('9', '1', '1', '2017-06-19 13:56:49', 'en', 'Feedback', null, '99999999', null, b'1', null), ('10', '1', '1', '2017-06-19 13:57:18', 'en', 'Feedback', null, '99999999', null, b'1', null), ('11', '1', '1', '2017-06-19 21:43:05', 'en', 'Feedback', null, '99999999', null, b'1', null), ('12', '1', '1', '2017-06-19 21:44:02', 'en', 'Feedback', null, '99999999', null, b'1', null), ('13', '1', '1', '2017-06-19 22:12:45', 'en', 'Feedback', null, '99999999', null, b'1', null), ('14', '1', '1', '2017-07-06 12:53:00', 'en', 'Feedback', null, '99999999'), ('15', '7', '7', '2017-07-06 12:57:35', 'en', 'Feedback', null, '99999999'), ('16', '7', '7', '2017-07-19 13:29:54', 'en', 'Feedback', null, '99999999'), ('17', '7', '7', '2017-07-19 13:31:46', 'en', 'Feedback', null, '99999999'), ('18', '7', '7', '2017-07-19 13:32:51', 'en', 'Feedback', null, '99999999'), ('19', '7', '7', '2017-08-21 14:08:45', 'en', 'Feedback', null, '99999999'), ('20', '7', '7', '2017-08-21 14:14:17', 'en', 'Feedback', null, '99999999'), ('21', '7', '7', '2017-08-21 14:15:53', 'en', 'Feedback', null, '99999999'), ('22', '7', '7', '2017-08-21 14:19:34', 'en', 'Feedback', null, '99999999'), ('23', '7', '7', '2017-08-21 14:23:44', 'en', 'Feedback', null, '99999999'), ('24', '7', '7', '2017-08-21 16:34:02', 'en', 'Feedback', null, '99999999'), ('25', '7', '7', '2017-08-21 16:40:41', 'en', 'Feedback', null, '99999999'), ('26', '7', '7', '2017-08-22 10:10:26', 'en', 'Feedback', null, '99999999'), ('27', '7', '7', '2017-08-22 10:16:07', 'en', 'Feedback', null, '99999999'), ('28', '7', '7', '2017-08-22 10:17:34', 'en', 'Feedback', null, '99999999'), ('29', '7', '7', '2017-08-22 10:32:31', 'en', 'Feedback', null, '99999999'), ('30', '7', '7', '2017-08-22 10:41:44', 'en', 'Feedback', null, '99999999'), ('31', '7', '7', '2017-08-22 10:44:05', 'en', 'Feedback', null, '99999999'), ('32', '7', '7', '2017-08-22 10:48:50', 'en', 'Feedback', null, '99999999'), ('33', '7', '7', '2017-08-22 11:13:05', 'en', 'Feedback', null, '99999999'), ('34', '7', '7', '2017-08-22 11:14:41', 'en', 'Feedback', null, '99999999'), ('35', '7', '7', '2017-08-22 11:19:46', 'en', 'Feedback', null, '99999999'), ('36', '7', '7', '2017-08-22 11:25:33', 'en', 'Feedback', null, '99999999'), ('37', '7', '7', '2017-08-22 11:31:31', 'en', 'Feedback', null, '99999999'), ('38', '7', '7', '2017-08-22 11:33:11', 'en', 'Feedback', null, '99999999'), ('39', '7', '7', '2017-08-23 10:32:37', 'en', 'Feedback', null, '99999999');
COMMIT;

-- ----------------------------
--  Table structure for `android_user`
-- ----------------------------
DROP TABLE IF EXISTS `android_user`;
CREATE TABLE `android_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `is_developer` bit(1) NOT NULL,
  `is_blocked` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `feedback_votes`
-- ----------------------------

DROP TABLE IF EXISTS `feedback_votes`;
CREATE TABLE `feedback_votes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `voter_user_id` bigint(20) NOT NULL,
  `voted_user_id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `vote` tinyint(1),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`),
  FOREIGN KEY (`voter_user_id`) REFERENCES `android_user` (`id`),
  FOREIGN KEY (`voted_user_id`) REFERENCES `android_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `feedback_status`
-- ----------------------------

DROP TABLE IF EXISTS `feedback_status`;
CREATE TABLE `feedback_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `feedback_status` (`status_type`) VALUES ('OPEN'), ('IN_PROGRESS'), ('CLOSED'), ('REJECTED'), ('DUPLICATE'), ('DELETED');

-- ----------------------------
--  Table structure for `feedback_comment`
-- ----------------------------

DROP TABLE IF EXISTS `feedback_comment`;
CREATE TABLE `feedback_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime,
  `comment` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`user_id`) REFERENCES `android_user` (`id`),
  FOREIGN KEY(`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `file_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `file_feedback`;
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

-- ----------------------------
--  Table structure for `rating_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `rating_feedback`;
CREATE TABLE `rating_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa5merp6x61nokyyvx17snw5bb` (`feedback_id`),
  CONSTRAINT `FKa5merp6x61nokyyvx17snw5bb` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `screenshot_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `screenshot_feedback`;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `setting`
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `feedback_email_receivers` varchar(510) NOT NULL,
  `kafka_topic_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=425 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `status`
-- ----------------------------
DROP TABLE IF EXISTS `status`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `status_options`
-- ----------------------------
DROP TABLE IF EXISTS `status_options`;
CREATE TABLE `status_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `order` int(11) NOT NULL,
  `user_specific` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
--  Table structure for `text_annotation`
-- ----------------------------
DROP TABLE IF EXISTS `text_annotation`;
CREATE TABLE `text_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference_number` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `screenshot_feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKki0o2ssgvt18agpq19o98h8ir` (`screenshot_feedback_id`),
  CONSTRAINT `FKki0o2ssgvt18agpq19o98h8ir` FOREIGN KEY (`screenshot_feedback_id`) REFERENCES `file_feedback` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `text_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `text_feedback`;
CREATE TABLE `text_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `text` text,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtidcgd0wra4sxqlawcp4li155` (`feedback_id`),
  CONSTRAINT `FKtidcgd0wra4sxqlawcp4li155` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=349 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
