/* 
Table structure for orchestrator v2

Ronnie Schaniel
12.10.2017

ATTENTION: PASSWORDS IN api_user NEED TO BE SET MANUALLY!

*/

SET foreign_key_checks = 0;

/* remove some tables that are not needed */

DROP TABLE IF EXISTS `application`;
DROP TABLE IF EXISTS `applications`;

/* The data on the production server is in applications1 and applications1_history */


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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `api_user_permission`
-- ----------------------------
CREATE TABLE `api_user_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_user_id` bigint(11) NOT NULL,
  `application_id` bigint(11) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_api_user_permissions_1_idx` (`api_user_id`),
  KEY `application_id` (`application_id`),
  CONSTRAINT `api_user_fk` FOREIGN KEY (`api_user_id`) REFERENCES `api_user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `application_fk` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;

-- ----------------------------
--  Table structure for `application`
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `configuration`
-- ----------------------------
CREATE TABLE `configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  `general_configuration_id` bigint(20) DEFAULT NULL,
  `pull_default` bit(1) NOT NULL,
  `push_default` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdmg5qpjmd9vjc35xqu229ifer` (`application_id`),
  KEY `FKh7to010nk73crcdcx9asyb0u0` (`general_configuration_id`),
  CONSTRAINT `FKdmg5qpjmd9vjc35xqu229ifer` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
  CONSTRAINT `FKh7to010nk73crcdcx9asyb0u0` FOREIGN KEY (`general_configuration_id`) REFERENCES `general_configuration` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `configuration_mechanism`
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `configuration_user_group`
-- ----------------------------
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
CREATE TABLE `general_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `mechanism`
-- ----------------------------
CREATE TABLE `mechanism` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;


-- ----------------------------
--  Table structure for `monitor_type`
-- ----------------------------
CREATE TABLE `monitor_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


-- ----------------------------
--  Table structure for `monitor_tool`
-- ----------------------------
CREATE TABLE `monitor_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_type_id` int(11) NOT NULL,
  `monitor_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_monitor_type_id` (`monitor_type_id`),
  CONSTRAINT `fk_monitor_type_id` FOREIGN KEY (`monitor_type_id`) REFERENCES `monitor_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;


-- ----------------------------
--  Table structure for `monitor_configuration`
-- ----------------------------
CREATE TABLE `monitor_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `monitor_tool_id` int(11) NOT NULL,
  `monitor_manager_id` int(11) NOT NULL,
  `config_sender` varchar(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  `time_slot` varchar(255) NOT NULL,
  `kafka_endpoint` varchar(255) NOT NULL,
  `kafka_topic` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `keyword_expression` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `package_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_monitor_tool_id` (`monitor_tool_id`),
  CONSTRAINT `fk_monitor_tool_id` FOREIGN KEY (`monitor_tool_id`) REFERENCES `monitor_tool` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;



-- ----------------------------
--  Table structure for `parameter`
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
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
CREATE TABLE `user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7nf0lyfnicwik0tpkd9lmk4ig` (`application_id`),
  CONSTRAINT `FK7nf0lyfnicwik0tpkd9lmk4ig` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


SET foreign_key_checks = 1;