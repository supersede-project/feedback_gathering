/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : supersede_repository_spring

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 11/04/2017 10:49:59 AM
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
--  Table structure for `api_user_permission`
-- ----------------------------
DROP TABLE IF EXISTS `api_user_permission`;
CREATE TABLE `api_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_user_id` bigint(11) NOT NULL,
  `application_id` bigint(11) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `api_user_id` (`api_user_id`),
  CONSTRAINT `api_user_fk` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=big5;

-- ----------------------------
--  Records of `api_user_permission`
-- ----------------------------
BEGIN;
INSERT INTO `api_user_permission` VALUES ('1', '1', '1', b'1');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `attachment_feedback`
-- ----------------------------
BEGIN;
INSERT INTO `attachment_feedback` VALUES ('1', 'pdf', '4', 'test_file.pdf', '20000', '1'), ('2', 'png', '26', '7_99999999_1500463793774_Screen_Shot_2017-07-19_at_13.13.21.png', '21854', '16'), ('3', 'pdf', '26', '7_99999999_1500463793775_test_Kopie_15.pdf', '18290', '16'), ('4', 'pdf', '26', '7_99999999_1500463793775_test_Kopie_16.pdf', '18290', '16'), ('5', 'png', '26', '7_99999999_1500463905948_Screen_Shot_2017-07-19_at_13.13.21.png', '21854', '17'), ('6', 'pdf', '26', '7_99999999_1500463905949_test_Kopie_15.pdf', '18290', '17'), ('7', 'pdf', '26', '7_99999999_1500463905949_test_Kopie_16.pdf', '18290', '17'), ('8', 'pdf', '26', '7_99999999_1500463970926_test_Kopie_15.pdf', '18290', '18'), ('9', 'pdf', '26', '7_99999999_1500463970926_test_Kopie_16.pdf', '18290', '18'), ('10', 'png', '26', '7_99999999_1500463970927_Screen_Shot_2017-07-19_at_13.13.21.png', '21854', '18');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `category_feedback`
-- ----------------------------
BEGIN;
INSERT INTO `category_feedback` VALUES ('1', '4', '700', null, '1'), ('2', '4', null, 'Custom category', '1'), ('3', '0', '39', '', '16'), ('4', '0', '39', '', '18'), ('5', '0', '39', '', '19'), ('6', '0', '39', '', '24'), ('7', '0', '39', '', '25'), ('8', '0', '39', '', '26'), ('9', '0', '39', '', '29'), ('10', '0', '39', '', '34'), ('11', '0', '39', '', '35'), ('12', '0', '39', '', '36'), ('13', '0', '39', '', '37'), ('14', '0', '39', '', '38'), ('15', '0', '39', '', '39');
COMMIT;

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
  `meta_data` text,
  PRIMARY KEY (`id`),
  KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`),
  CONSTRAINT `FKgjes53m9wrcpnnq0kl898yjul` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `context_information`
-- ----------------------------
BEGIN;
INSERT INTO `context_information` VALUES ('1', null, 'CH', '2', '2017-05-31 20:38:51', 'ZH', '2000x1200', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', '1', null, null), ('2', null, null, '1', '2017-06-19 00:39:05', null, '1413x2560', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('3', null, null, '2', '2017-06-19 13:40:44', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('4', null, null, '2', '2017-06-19 13:41:47', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('5', null, null, '2', '2017-06-19 13:43:44', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('6', null, null, '2', '2017-06-19 13:47:01', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('7', null, null, '2', '2017-06-19 13:50:39', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('8', null, null, '2', '2017-06-19 13:52:16', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('9', null, null, '2', '2017-06-19 13:56:48', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('10', null, null, '2', '2017-06-19 13:57:18', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('11', null, null, '1', '2017-06-19 21:43:05', null, '1413x2560', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('12', null, null, '1', '2017-06-19 21:44:01', null, '1413x2560', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('13', null, null, '1', '2017-06-19 22:12:45', null, '1413x2560', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36', null, 'http://localhost/', null), ('14', null, null, '2', '2017-07-06 00:52:59', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36', '14', 'http://localhost/', null), ('15', null, null, '2', '2017-07-06 00:57:34', null, '873x1440', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36', '15', 'http://localhost/', null), ('16', null, null, '1', '2017-07-19 13:29:53', null, '2133x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36', '16', 'http://localhost/', null), ('17', null, null, '1', '2017-07-19 13:31:45', null, '2133x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36', '17', 'http://localhost/', null), ('18', null, null, '1', '2017-07-19 13:32:50', null, '2133x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36', '18', 'http://localhost/', null), ('19', null, null, '1', '2017-08-21 14:08:44', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '19', 'http://localhost/', null), ('20', null, null, '1', '2017-08-21 14:14:16', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '20', 'http://localhost/', null), ('21', null, null, '1', '2017-08-21 14:15:53', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '21', 'http://localhost/', null), ('22', null, null, '1', '2017-08-21 14:19:33', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '22', 'http://localhost/', null), ('23', null, null, '1', '2017-08-21 14:23:44', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '23', 'http://localhost/', null), ('24', null, null, '1', '2017-08-21 16:34:01', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '24', 'http://localhost/', null), ('25', null, null, '1', '2017-08-21 16:40:40', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '25', 'http://localhost/', null), ('26', null, null, '1', '2017-08-22 10:10:25', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '26', 'http://localhost/', null), ('27', null, null, '1', '2017-08-22 10:16:06', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '27', 'http://localhost/', null), ('28', null, null, '1', '2017-08-22 10:17:33', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '28', 'http://localhost/', null), ('29', null, null, '1', '2017-08-22 10:32:31', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '29', 'http://localhost/', null), ('30', null, null, '1', '2017-08-22 10:41:43', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '30', 'http://localhost/', null), ('31', null, null, '1', '2017-08-22 10:44:04', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '31', 'http://localhost/', null), ('32', null, null, '1', '2017-08-22 10:48:49', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '32', 'http://localhost/', null), ('33', null, null, '1', '2017-08-22 11:13:04', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '33', 'http://localhost/', null), ('34', null, null, '1', '2017-08-22 11:14:41', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '34', 'http://localhost/', null), ('35', null, null, '1', '2017-08-22 11:19:46', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '35', 'http://localhost/', null), ('36', null, null, '1', '2017-08-22 11:25:32', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '36', 'http://localhost/', null), ('37', null, null, '1', '2017-08-22 11:31:30', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '37', 'http://localhost/', null), ('38', null, null, '1', '2017-08-22 11:33:11', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '38', 'http://localhost/', null), ('39', null, null, '1', '2017-08-23 10:32:36', null, '2137x3840', '+0200', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36', '39', 'http://localhost/', null);
COMMIT;

-- ----------------------------
--  Table structure for `feedback`
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `configuration_id` bigint(20) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `user_identification` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `feedback`
-- ----------------------------
BEGIN;
INSERT INTO `feedback` VALUES ('1', '1', '2', '2017-05-31 19:57:54', 'en', 'Test feedback', '2017-05-31 19:58:05', 'u123456'), ('2', '1', '1', '2017-06-19 12:39:06', null, 'Feedback', null, '99999999'), ('3', '1', '1', '2017-06-19 13:40:45', null, 'Feedback', null, '99999999'), ('4', '1', '1', '2017-06-19 13:41:47', null, 'Feedback', null, '99999999'), ('5', '1', '1', '2017-06-19 13:43:45', null, 'Feedback', null, '99999999'), ('6', '1', '1', '2017-06-19 13:47:02', 'en', 'Feedback', null, '99999999'), ('7', '1', '1', '2017-06-19 13:51:53', 'en', 'Feedback', null, '99999999'), ('8', '1', '1', '2017-06-19 13:52:26', 'en', 'Feedback', null, '99999999'), ('9', '1', '1', '2017-06-19 13:56:49', 'en', 'Feedback', null, '99999999'), ('10', '1', '1', '2017-06-19 13:57:18', 'en', 'Feedback', null, '99999999'), ('11', '1', '1', '2017-06-19 21:43:05', 'en', 'Feedback', null, '99999999'), ('12', '1', '1', '2017-06-19 21:44:02', 'en', 'Feedback', null, '99999999'), ('13', '1', '1', '2017-06-19 22:12:45', 'en', 'Feedback', null, '99999999'), ('14', '1', '1', '2017-07-06 12:53:00', 'en', 'Feedback', null, '99999999'), ('15', '7', '7', '2017-07-06 12:57:35', 'en', 'Feedback', null, '99999999'), ('16', '7', '7', '2017-07-19 13:29:54', 'en', 'Feedback', null, '99999999'), ('17', '7', '7', '2017-07-19 13:31:46', 'en', 'Feedback', null, '99999999'), ('18', '7', '7', '2017-07-19 13:32:51', 'en', 'Feedback', null, '99999999'), ('19', '7', '7', '2017-08-21 14:08:45', 'en', 'Feedback', null, '99999999'), ('20', '7', '7', '2017-08-21 14:14:17', 'en', 'Feedback', null, '99999999'), ('21', '7', '7', '2017-08-21 14:15:53', 'en', 'Feedback', null, '99999999'), ('22', '7', '7', '2017-08-21 14:19:34', 'en', 'Feedback', null, '99999999'), ('23', '7', '7', '2017-08-21 14:23:44', 'en', 'Feedback', null, '99999999'), ('24', '7', '7', '2017-08-21 16:34:02', 'en', 'Feedback', null, '99999999'), ('25', '7', '7', '2017-08-21 16:40:41', 'en', 'Feedback', null, '99999999'), ('26', '7', '7', '2017-08-22 10:10:26', 'en', 'Feedback', null, '99999999'), ('27', '7', '7', '2017-08-22 10:16:07', 'en', 'Feedback', null, '99999999'), ('28', '7', '7', '2017-08-22 10:17:34', 'en', 'Feedback', null, '99999999'), ('29', '7', '7', '2017-08-22 10:32:31', 'en', 'Feedback', null, '99999999'), ('30', '7', '7', '2017-08-22 10:41:44', 'en', 'Feedback', null, '99999999'), ('31', '7', '7', '2017-08-22 10:44:05', 'en', 'Feedback', null, '99999999'), ('32', '7', '7', '2017-08-22 10:48:50', 'en', 'Feedback', null, '99999999'), ('33', '7', '7', '2017-08-22 11:13:05', 'en', 'Feedback', null, '99999999'), ('34', '7', '7', '2017-08-22 11:14:41', 'en', 'Feedback', null, '99999999'), ('35', '7', '7', '2017-08-22 11:19:46', 'en', 'Feedback', null, '99999999'), ('36', '7', '7', '2017-08-22 11:25:33', 'en', 'Feedback', null, '99999999'), ('37', '7', '7', '2017-08-22 11:31:31', 'en', 'Feedback', null, '99999999'), ('38', '7', '7', '2017-08-22 11:33:11', 'en', 'Feedback', null, '99999999'), ('39', '7', '7', '2017-08-23 10:32:37', 'en', 'Feedback', null, '99999999');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `rating_feedback`
-- ----------------------------
BEGIN;
INSERT INTO `rating_feedback` VALUES ('1', '3', '5', 'Please rate', '1'), ('2', '24', '5', 'Rate Grid Data UI', '15'), ('3', '24', '5', 'Rate Grid Data UI', '16'), ('4', '24', '0', 'Rate Grid Data UI', '17'), ('5', '24', '5', 'Rate Grid Data UI', '18'), ('6', '24', '5', 'Rate Grid Data UI', '19'), ('7', '24', '0', 'Rate Grid Data UI', '20'), ('8', '24', '0', 'Rate Grid Data UI', '21'), ('9', '24', '0', 'Rate Grid Data UI', '22'), ('10', '24', '0', 'Rate Grid Data UI', '23'), ('11', '24', '5', 'Rate Grid Data UI', '24'), ('12', '24', '5', 'Rate Grid Data UI', '25'), ('13', '24', '5', 'Rate Grid Data UI', '26'), ('14', '24', '0', 'Rate Grid Data UI', '27'), ('15', '24', '0', 'Rate Grid Data UI', '28'), ('16', '24', '5', 'Rate Grid Data UI', '29'), ('17', '24', '0', 'Rate Grid Data UI', '30'), ('18', '24', '0', 'Rate Grid Data UI', '31'), ('19', '24', '0', 'Rate Grid Data UI', '32'), ('20', '24', '0', 'Rate Grid Data UI', '33'), ('21', '24', '5', 'Rate Grid Data UI', '34'), ('22', '24', '5', 'Rate Grid Data UI', '35'), ('23', '24', '5', 'Rate Grid Data UI', '36'), ('24', '24', '5', 'Rate Grid Data UI', '37'), ('25', '24', '5', 'Rate Grid Data UI', '38'), ('26', '24', '1', 'Rate Grid Data UI', '39');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `screenshot_feedback`
-- ----------------------------
BEGIN;
INSERT INTO `screenshot_feedback` VALUES ('1', null, '6', 'screenshot_1_example.png', '805701', '1'), ('2', 'png', '2', '1_99999999_blob', '70728', null), ('3', 'png', '2', '1_99999999_blob', '40192', null), ('4', 'png', '2', '1_99999999_blob', '287149', null), ('5', 'png', '2', '1_99999999_blob.png', '287149', null), ('6', 'png', '2', '1_99999999_blob.png', '40020', null), ('7', 'png', '2', '1_99999999_blob.png', '68611', null), ('8', 'png', '2', '1_99999999_blob.png', '287140', '14'), ('9', 'png', '25', '7_99999999_blob.png', '287140', '15'), ('10', '', '25', '7_99999999_1500463793775_blob', '47688', '16'), ('11', '', '25', '7_99999999_1500463970927_blob', '47688', '18'), ('12', '', '25', '7_99999999_1503389425719_blob', '57250', '26'), ('13', '', '25', '7_99999999_1503390751128_blob', '58567', '29'), ('14', '', '25', '7_99999999_1503393281051_blob', '57250', '34'), ('15', '', '25', '7_99999999_1503393586198_blob', '57250', '35'), ('16', '', '25', '7_99999999_1503393932655_blob', '57250', '36'), ('17', '', '25', '7_99999999_1503394290833_blob', '57250', '37'), ('18', '', '25', '7_99999999_1503394391177_blob', '57250', '38');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `setting`
-- ----------------------------
BEGIN;
INSERT INTO `setting` VALUES ('1', '1', 'ronnieschaniel@gmail.com', null), ('2', '7', 'ronnieschaniel@gmail.com', null);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `status`
-- ----------------------------
BEGIN;
INSERT INTO `status` VALUES ('1', '1', '1', '1');
COMMIT;

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
  KEY `FKi8nbnsfcgsaujg8al7anyb1pr` (`screenshot_feedback_id`),
  CONSTRAINT `FKi8nbnsfcgsaujg8al7anyb1pr` FOREIGN KEY (`screenshot_feedback_id`) REFERENCES `screenshot_feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `text_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `text_feedback`;
CREATE TABLE `text_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `text` longtext,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtidcgd0wra4sxqlawcp4li155` (`feedback_id`),
  CONSTRAINT `FKtidcgd0wra4sxqlawcp4li155` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `text_feedback`
-- ----------------------------
BEGIN;
INSERT INTO `text_feedback` VALUES ('1', '1', 'My text feedback', '1'), ('2', '1', 'test', null), ('3', '1', 'test', null), ('4', '1', '', null), ('5', '1', '', null), ('6', '1', '', null), ('7', '1', '', null), ('8', '1', 'test with screenshot', null), ('9', '1', '', null), ('10', '1', 'test', null), ('11', '1', 'test', null), ('12', '1', '', null), ('13', '1', 'test', null), ('14', '1', 'Feedback to the new backend', '14'), ('15', '23', 'Feedback Siemens test data grid', '15'), ('16', '23', 'Test files', '16'), ('17', '23', '', '17'), ('18', '23', 'test file 3\n1 screenshot\n3 attachments ', '18'), ('19', '23', 'Feedback should be sent to repository. After that it is sent to the kafka topic raw-feedback. Then analysed. And finally it is stored in the kafka topic analysed-feedback. After that it is ready to be traced.', '19'), ('20', '23', '', '20'), ('21', '23', '', '21'), ('22', '23', '', '22'), ('23', '23', '', '23'), ('24', '23', 'This feedback is sent to the repository, then to kafka raw-feedback. After that it is analysed and sent to analysed-feedback. Then it would be ready to be traced onto requirements stored in a JIRA.', '24'), ('25', '23', 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', '25'), ('26', '23', 'Test feedback that should get stored in dl feedback storage. ', '26'), ('27', '23', '', '27'), ('28', '23', '', '28'), ('29', '23', 'Sample feedback. Stored in the dl', '29'), ('30', '23', '', '30'), ('31', '23', '', '31'), ('32', '23', '', '32'), ('33', '23', '', '33'), ('34', '23', 'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter. ', '34'), ('35', '23', 'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.', '35'), ('36', '23', 'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.', '36'), ('37', '23', 'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.', '37'), ('38', '23', 'Hello. I just want to say that the application is great. However, there is one little error with the heating diagram (see screenshot). And it would be great if you could add the functionality to login via twitter.', '38'), ('39', '23', 'The zoom function has a problem I think. Sometimes the screenshot disappears and I get a white box.', '39');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
