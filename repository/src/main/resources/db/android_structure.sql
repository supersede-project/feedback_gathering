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
  PRIMARY KEY (`id`),
  KEY (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;

-- ----------------------------
-- (TODO: Maybe remove) Table structure for `android_application`
-- ----------------------------
DROP TABLE IF EXISTS `android_application`;
CREATE TABLE `android_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `android_application_id` varchar(255) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`android_application_id`),
  FOREIGN KEY (`application_id`) REFERENCES `feedback` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
