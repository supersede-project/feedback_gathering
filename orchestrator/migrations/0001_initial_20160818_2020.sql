/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : feedback_orchestrator

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 08/18/2016 20:20:05 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `applications`
-- ----------------------------
DROP TABLE IF EXISTS `applications`;
CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `state` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `general_configuration_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_applications_1_idx` (`general_configuration_id`),
  CONSTRAINT `fk_applications_1` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `configurations`
-- ----------------------------
DROP TABLE IF EXISTS `configurations`;
CREATE TABLE `configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `application_id` int(11) NOT NULL,
  `user_group_id` int(11) DEFAULT NULL,
  `type` enum('PUSH','PULL') NOT NULL,
  `general_configuration_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `application_id` (`application_id`,`user_group_id`),
  KEY `fk_configurations_1_idx` (`application_id`),
  KEY `fk_configurations_2_idx` (`user_group_id`),
  KEY `fk_configurations_3_idx` (`general_configuration_id`),
  CONSTRAINT `fk_configurations_1` FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_2` FOREIGN KEY (`user_group_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_3` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `configurations_mechanisms`
-- ----------------------------
DROP TABLE IF EXISTS `configurations_mechanisms`;
CREATE TABLE `configurations_mechanisms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `configuration_id` int(11) NOT NULL,
  `mechanism_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `order` int(11) NOT NULL,
  `can_be_activated` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_configurations_mechanisms_1_idx` (`configuration_id`),
  KEY `fk_configurations_mechanisms_2_idx` (`mechanism_id`),
  CONSTRAINT `fk_configurations_mechanisms_1` FOREIGN KEY (`configuration_id`) REFERENCES `configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_mechanisms_2` FOREIGN KEY (`mechanism_id`) REFERENCES `mechanisms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `general_configurations`
-- ----------------------------
DROP TABLE IF EXISTS `general_configurations`;
CREATE TABLE `general_configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `mechanisms`
-- ----------------------------
DROP TABLE IF EXISTS `mechanisms`;
CREATE TABLE `mechanisms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `parameter_options`
-- ----------------------------
DROP TABLE IF EXISTS `parameter_options`;
CREATE TABLE `parameter_options` (
  `id` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `entity` enum('TEXT_MECHANISM','RATING_MECHANISM','SCREENSHOT_MECHANISM','CATEGORY_MECHANISM','AUDIO_MECHANISM','PULL_CONFIGURATION','PUSH_CONFIGURATION','APPLICATION_CONFIGURATION') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `parameters`
-- ----------------------------
DROP TABLE IF EXISTS `parameters`;
CREATE TABLE `parameters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mechanism_id` int(11) DEFAULT NULL,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `default_value` varchar(255) DEFAULT NULL,
  `editable_by_user` bit(1) DEFAULT NULL,
  `parameters_id` int(11) DEFAULT NULL,
  `language` varchar(3) NOT NULL DEFAULT 'en',
  `general_configurations_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_2` (`key`,`mechanism_id`,`parameters_id`),
  UNIQUE KEY `key_3` (`key`,`general_configurations_id`,`parameters_id`),
  UNIQUE KEY `key_4` (`key`,`parameters_id`),
  KEY `mechanism_constrained_idx` (`mechanism_id`),
  KEY `fk_parameters_parameters1_idx` (`parameters_id`),
  KEY `fk_parameters_general_configurations1_idx` (`general_configurations_id`),
  CONSTRAINT `fk_parameters_general_configurations1` FOREIGN KEY (`general_configurations_id`) REFERENCES `general_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parameters_parameters1` FOREIGN KEY (`parameters_id`) REFERENCES `parameters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `mechanism_constrained` FOREIGN KEY (`mechanism_id`) REFERENCES `mechanisms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_groups`
-- ----------------------------
DROP TABLE IF EXISTS `user_groups`;
CREATE TABLE `user_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `user_group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_1_idx` (`user_group_id`),
  CONSTRAINT `fk_users_1` FOREIGN KEY (`user_group_id`) REFERENCES `user_groups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
