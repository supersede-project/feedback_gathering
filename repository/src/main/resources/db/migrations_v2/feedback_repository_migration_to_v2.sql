/* 
Migration Script feedback_repository 

Migrates the old schema to the schema used in the Java Spring backend version.

Ronnie Schaniel
11.10.2017

ATTENTION: PASSWORDS IN api_user NEED TO BE SET MANUALLY!

*/

SET foreign_key_checks = 0;

/* Let's create the new tables */

SET NAMES utf8mb4;

-- ----------------------------
--  Table structure for `api_user`
-- ----------------------------
CREATE TABLE `api_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_scb81k0sobpewfaxhwyquccop` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `api_user_api_user_role`
-- ----------------------------
CREATE TABLE `api_user_api_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_user_role` int(11) DEFAULT NULL,
  `api_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmh0ci62ckvoi95vgx5nj4n0a3` (`api_user_id`),
  CONSTRAINT `FKmh0ci62ckvoi95vgx5nj4n0a3` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `api_user_permission`
-- ----------------------------
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
--  Table structure for `attachment_feedback`
-- ----------------------------
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
--  Table structure for `audio_feedback`
-- ----------------------------
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
CREATE TABLE `category_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) DEFAULT NULL,
  `parameter_id` bigint(20) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK83xs7iqr1bq6a5isel0ceded8` (`feedback_id`),
  CONSTRAINT `FK83xs7iqr1bq6a5isel0ceded8` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `context_information`
-- ----------------------------
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
--  Table structure for `feedback`
-- ----------------------------
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
--  Table structure for `rating_feedback`
-- ----------------------------
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
--  Table structure for `screenshot_feedback`
-- ----------------------------
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
--  Table structure for `setting`
-- ----------------------------
CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `feedback_email_receivers` varchar(510) NOT NULL,
  `kafka_topic_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `status`
-- ----------------------------
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
--  Table structure for `text_annotation`
-- ----------------------------
CREATE TABLE `text_annotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference_number` int(11) DEFAULT NULL,
  `text` text,
  `screenshot_feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi8nbnsfcgsaujg8al7anyb1pr` (`screenshot_feedback_id`),
  CONSTRAINT `FKi8nbnsfcgsaujg8al7anyb1pr` FOREIGN KEY (`screenshot_feedback_id`) REFERENCES `screenshot_feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `text_feedback`
-- ----------------------------
CREATE TABLE `text_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mechanism_id` bigint(20) NOT NULL,
  `text` longtext,
  `feedback_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtidcgd0wra4sxqlawcp4li155` (`feedback_id`),
  CONSTRAINT `FKtidcgd0wra4sxqlawcp4li155` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;



/* Now move the data */


-- ----------------------------
--  Data for `api_user`
-- ----------------------------

INSERT INTO `api_user` (`id`, `name`, `password`)
	SELECT `id`, `name`, `password` FROM `api_users`;

-- ----------------------------
--  Data for `api_user_api_user_role`
-- ----------------------------

INSERT INTO `api_user_api_user_role` (`api_user_role`, `api_user_id`)
	SELECT 1, `id` FROM `api_users` WHERE `role` = 'ADMIN';
INSERT INTO `api_user_api_user_role` (`api_user_role`, `api_user_id`)
	SELECT 2, `id` FROM `api_users` WHERE `role` = 'USER';


-- ----------------------------
--  Data for `api_user_permission`
-- ----------------------------

INSERT INTO `api_user_permission` (`id`, `api_user_id`, `application_id`, `has_permission`)
	SELECT `id`, `user_id`, `application_id`, `has_permission` FROM `api_user_permissions`;


-- ----------------------------
--  Data for `feedback`
-- ----------------------------

INSERT INTO `feedback` (`id`, `application_id`, `configuration_id`, `createdAt`, `language`, `title`, `updatedAt`, `user_identification`)
	SELECT `id`, `application_id`, `configuration_id`, from_unixtime(UNIX_TIMESTAMP(`createdAt`)), `language`, `title`, from_unixtime(UNIX_TIMESTAMP(`createdAt`)), `user_identification` FROM `feedbacks`;


-- ----------------------------
--  Data for `attachment_feedback`
-- ----------------------------

INSERT INTO `attachment_feedback` (`id`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id`)
	SELECT `id`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id` FROM `attachment_feedbacks`;


-- ----------------------------
--  Data for `audio_feedback`
-- ----------------------------

INSERT INTO `audio_feedback` (`id`, `duration`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id`)
	SELECT `id`, `duration`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id` FROM `audio_feedbacks`;



-- ----------------------------
--  Data for `category_feedback`
-- ----------------------------

INSERT INTO `category_feedback` (`id`, `mechanism_id`, `parameter_id`, `text`, `feedback_id`)
	SELECT `id`, NULL, `parameter_id`, `text`, `feedback_id` FROM `category_feedbacks`;


-- ----------------------------
--  Data for `context_information`
-- ----------------------------

INSERT INTO `context_information` (`id`, `android_version`, `country`, `device_pixel_ratio`, 
`local_time`, `region`, `resolution`, `time_zone`, `user_agent`, `feedback_id`, `url`, `meta_data`)
	SELECT c.`id`, c.`android_version`, c.`country`, c.`device_pixel_ratio`, 
	STR_TO_DATE(
		CONCAT(DATE(f.`createdAt`), ' ', c.`local_time`),
		'%Y-%m-%d %H:%i:%s'
	) as local_time, 
	c.`region`, 
		c.`resolution`, c.`time_zone`, c.`user_agent`, f.`id` AS feedback_id, `url`, ''

	FROM `context_informations` AS c, `feedbacks` AS f
	WHERE f.`context_informations_id` = c.`id`;


-- ----------------------------
--  Data for `rating_feedback`
-- ----------------------------

INSERT INTO `rating_feedback` (`id`, `mechanism_id`, `rating`, `title`, `feedback_id`)
	SELECT `id`, `mechanism_id`, `rating`, `title`, `feedback_id` FROM `rating_feedbacks`;


-- ----------------------------
--  Data for `screenshot_feedback`
-- ----------------------------

INSERT INTO `screenshot_feedback` (`id`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id`)
	SELECT `id`, `file_extension`, `mechanism_id`, `path`, `size`, `feedback_id` FROM `screenshot_feedbacks`;


-- ----------------------------
--  Data for `setting`
-- ----------------------------

INSERT INTO `setting` VALUES ('1', '20', 'energiesparkonto@co2online.de', '5ff7d393-e2a5-49fd-a4de-f4e1f7480bf4');


-- ----------------------------
--  Data for `status`
-- ----------------------------



-- ----------------------------
--  Data for `text_annotation`
-- ----------------------------

INSERT INTO `text_annotation` (`id`, `reference_number`, `text`, `screenshot_feedback_id`)
	SELECT `id`, `reference_number`, `text`, `screenshot_feedbacks_id` FROM `text_annotations`;


-- ----------------------------
--  Data for `text_feedback`
-- ----------------------------

INSERT INTO `text_feedback` (`id`, `mechanism_id`, `text`, `feedback_id`)
	SELECT `id`, `mechanism_id`, `text`, `feedback_id` FROM `text_feedbacks`;


/* Let's remove the old tables */

/*
DROP TABLES `api_user_permissions`;
DROP TABLES `api_users`;
DROP TABLES `attachment_feedbacks`;
DROP TABLES `audio_feedbacks`;
DROP TABLES `categories`;
DROP TABLES `category_feedbacks`;
DROP TABLES `category_types`;
DROP TABLES `context_informations`;
DROP TABLES `feedback_comments`;
DROP TABLES `feedback_states`;
DROP TABLES `feedbacks`;
DROP TABLES `rating_feedbacks`;
DROP TABLES `screenshot_feedbacks`;
DROP TABLES `status_options`;
DROP TABLES `statuses`;
DROP TABLES `text_annotations`;
DROP TABLES `text_feedbacks`;
*/

SET foreign_key_checks = 1;









