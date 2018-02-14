-- MySQL Workbench Synchronization

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `default_schema`.`api_user_permission` 
DROP FOREIGN KEY `api_user_fk`;

ALTER TABLE `default_schema`.`rating_feedback` 
DROP FOREIGN KEY `FKa5merp6x61nokyyvx17snw5bb`;

ALTER TABLE `default_schema`.`screenshot_feedback` 
DROP FOREIGN KEY `FKsekb4tg5bivjepodpeyt7eplc`;

ALTER TABLE `default_schema`.`status` 
DROP FOREIGN KEY `FKho80mb8i1oa2guwbceli8s974`,
DROP FOREIGN KEY `FKgcq19tbyg1cqwehj46h5mx4we`;

ALTER TABLE `default_schema`.`text_annotation` 
DROP FOREIGN KEY `FKi8nbnsfcgsaujg8al7anyb1pr`;

ALTER TABLE `default_schema`.`text_feedback` 
DROP FOREIGN KEY `FKtidcgd0wra4sxqlawcp4li155`;

ALTER TABLE `default_schema`.`api_user_permission` 
CHARACTER SET = utf8 , COLLATE = utf8_general_ci ,
CHANGE COLUMN `api_user_id` `api_user_id` BIGINT(20) NULL DEFAULT NULL AFTER `has_permission`,
CHANGE COLUMN `application_id` `application_id` BIGINT(20) NOT NULL ,
ADD INDEX `FK3eosps1v8ku1jdk3m3k8vohxy` (`api_user_id` ASC),
DROP INDEX `api_user_id` ;

ALTER TABLE `default_schema`.`category_feedback` 
CHANGE COLUMN `mechanism_id` `mechanism_id` BIGINT(20) NOT NULL ;

CREATE TABLE IF NOT EXISTS `default_schema`.`comment_feedback` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_status` BIT(1) NULL DEFAULT NULL,
  `bool_is_developer` BIT(1) NULL DEFAULT NULL,
  `comment_text` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NOT NULL,
  `fk_parent_comment` BIGINT(20) NULL DEFAULT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `anonymous` BIT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKsjg1n0mnkm7haiyt6k8pqbd4x` (`feedback_id` ASC),
  INDEX `FKh1fyv8nt5no6b2yw1q3a7j1kt` (`fk_parent_comment` ASC),
  INDEX `FK7rsl9hwpd3dh6ucn5pxuo6j4d` (`user_id` ASC),
  CONSTRAINT `FK7rsl9hwpd3dh6ucn5pxuo6j4d`
    FOREIGN KEY (`user_id`)
    REFERENCES `default_schema`.`end_user` (`id`),
  CONSTRAINT `FKh1fyv8nt5no6b2yw1q3a7j1kt`
    FOREIGN KEY (`fk_parent_comment`)
    REFERENCES `default_schema`.`comment_feedback` (`id`),
  CONSTRAINT `FKsjg1n0mnkm7haiyt6k8pqbd4x`
    FOREIGN KEY (`feedback_id`)
    REFERENCES `default_schema`.`feedback` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`comment_viewed` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `comment_id` BIGINT(20) NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKt7aa1dhhfvrlyukmtktgdxraf` (`comment_id` ASC),
  INDEX `FKamfb2bvnx3tj8stougb7hsuyq` (`user_id` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;

ALTER TABLE `default_schema`.`context_information` 
CHANGE COLUMN `meta_data` `meta_data` VARCHAR(255) NULL DEFAULT NULL AFTER `local_time`,
CHANGE COLUMN `url` `url` VARCHAR(255) NULL DEFAULT NULL AFTER `time_zone`;

CREATE TABLE IF NOT EXISTS `default_schema`.`email_unsubscribed` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `email_unsubscribed_end_user_id_fk` (`user_id` ASC),
  CONSTRAINT `email_unsubscribed_end_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `default_schema`.`end_user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`end_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `application_id` BIGINT(20) NOT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `phone_number` INT(11) NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `username` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 100000001
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `default_schema`.`feedback` 
CHANGE COLUMN `user_identification` `user_identification` BIGINT(20) NULL DEFAULT NULL ,
ADD COLUMN `comment_count` INT(11) NOT NULL AFTER `user_identification`,
ADD COLUMN `dislike_count` INT(11) NOT NULL AFTER `comment_count`,
ADD COLUMN `icon_path` VARCHAR(255) NULL DEFAULT NULL AFTER `dislike_count`,
ADD COLUMN `blocked` TINYINT(1) NULL DEFAULT '0' AFTER `icon_path`,
ADD COLUMN `like_count` INT(11) NOT NULL AFTER `blocked`,
ADD COLUMN `unread_comment_count` INT(11) NOT NULL AFTER `like_count`,
ADD COLUMN `published` TINYINT(1) NULL DEFAULT '0' AFTER `unread_comment_count`,
ADD COLUMN `visibility` TINYINT(1) NULL DEFAULT '0' AFTER `published`;

CREATE TABLE IF NOT EXISTS `default_schema`.`feedback_chat_information` (
  `feedback_chat_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `chat_date` DATETIME NULL DEFAULT NULL,
  `chat_text` VARCHAR(255) NULL DEFAULT NULL,
  `initated_by_user` BIT(1) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`feedback_chat_id`),
  INDEX `FK499lsagiuhb2k5jkg40peuhet` (`feedback_id` ASC),
  INDEX `FK3knnwken9u06o1w2i285ubki0` (`user_id` ASC),
  CONSTRAINT `FK3knnwken9u06o1w2i285ubki0`
    FOREIGN KEY (`user_id`)
    REFERENCES `default_schema`.`end_user` (`id`),
  CONSTRAINT `FK499lsagiuhb2k5jkg40peuhet`
    FOREIGN KEY (`feedback_id`)
    REFERENCES `default_schema`.`feedback` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`feedback_company` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `promote` BIT(1) NULL DEFAULT NULL,
  `status` VARCHAR(255) NULL DEFAULT NULL,
  `text` VARCHAR(255) NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`feedback_settings` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `feedback_query` BIT(1) NOT NULL,
  `feedback_query_channel` VARCHAR(255) NOT NULL,
  `global_feedback_setting` BIT(1) NOT NULL,
  `status_updates` BIT(1) NOT NULL,
  `status_updates_contact_channel` VARCHAR(255) NOT NULL,
  `feedback_id` BIGINT(20) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`feedback_status` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `date` DATETIME NULL DEFAULT NULL,
  `status` VARCHAR(255) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 34
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`feedback_viewed` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK8b4kr36xe7xqkc6mr4yavlxjn` (`user_id` ASC),
  INDEX `FKabbwmf5dldpp256m5b2jgdj0t` (`feedback_id` ASC))
ENGINE = MyISAM
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;

CREATE TABLE IF NOT EXISTS `default_schema`.`file_feedback` (
  `dtype` VARCHAR(31) NOT NULL,
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `file_extension` VARCHAR(255) NULL DEFAULT NULL,
  `path` VARCHAR(255) NULL DEFAULT NULL,
  `size` INT(11) NOT NULL,
  `mechanism_id` BIGINT(20) NULL DEFAULT NULL,
  `duration` INT(11) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKa93pglqlwxt6kxxqn248si979` (`feedback_id` ASC),
  CONSTRAINT `FKa93pglqlwxt6kxxqn248si979`
    FOREIGN KEY (`feedback_id`)
    REFERENCES `default_schema`.`feedback` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `default_schema`.`rating_feedback` 
DROP INDEX `FKa5merp6x61nokyyvx17snw5bb` ;

ALTER TABLE `default_schema`.`screenshot_feedback` 
DROP INDEX `FKsekb4tg5bivjepodpeyt7eplc` ;

ALTER TABLE `default_schema`.`setting` 
CHANGE COLUMN `feedback_email_receivers` `feedback_email_receivers` VARCHAR(255) NULL DEFAULT NULL ;

ALTER TABLE `default_schema`.`status` 
DROP INDEX `FKgcq19tbyg1cqwehj46h5mx4we` ,
DROP INDEX `FKho80mb8i1oa2guwbceli8s974` ;

CREATE TABLE IF NOT EXISTS `default_schema`.`status_option` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `order` INT(11) NOT NULL,
  `user_specific` BIT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

ALTER TABLE `default_schema`.`text_annotation` 
CHANGE COLUMN `text` `text` VARCHAR(255) NULL DEFAULT NULL ,
DROP INDEX `FKi8nbnsfcgsaujg8al7anyb1pr` ;

ALTER TABLE `default_schema`.`text_feedback` 
DROP INDEX `FKtidcgd0wra4sxqlawcp4li155` ;

CREATE TABLE IF NOT EXISTS `default_schema`.`userfbdislike` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 107
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `default_schema`.`userfblike` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `feedback_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 101
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `default_schema`.`api_user_permission` 
ADD CONSTRAINT `FK3eosps1v8ku1jdk3m3k8vohxy`
  FOREIGN KEY (`api_user_id`)
  REFERENCES `default_schema`.`api_user` (`id`);


DELIMITER $$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_comment AFTER INSERT ON comment_feedback
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END */$$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_comment2 AFTER DELETE ON comment_feedback
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END */$$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_dislike AFTER INSERT ON userfbdislike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END */$$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_dislike2 AFTER DELETE ON userfbdislike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END */$$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_like AFTER INSERT ON userfblike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END */$$

USE `default_schema`$$
/*!50003 CREATE*/ /*!50017 DEFINER=`f2f_repo`@`%`*/ /*!50003 TRIGGER update_like2 AFTER DELETE ON userfblike
FOR EACH ROW
  BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END */$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
