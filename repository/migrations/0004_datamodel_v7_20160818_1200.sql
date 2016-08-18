ALTER TABLE `feedback_repository`.`attachment_feedbacks` ADD COLUMN `part` varchar(255) AFTER `name`, ADD COLUMN `mechanism_id` int(11) AFTER `part`;

ALTER TABLE `feedback_repository`.`audio_feedbacks` ADD COLUMN `name` varchar(255) AFTER `size`, ADD COLUMN `duration` int(11) AFTER `name`, ADD COLUMN `mechanism_id` int(11) AFTER `duration`, ADD COLUMN `part` varchar(255) AFTER `mechanism_id`, ADD COLUMN `file_extension` varchar(10) AFTER `part`;

ALTER TABLE `feedback_repository`.`category_feedbacks` ADD COLUMN `mechanism_id` int(11) AFTER `feedback_id`;

ALTER TABLE `feedback_repository`.`context_informations` DROP COLUMN `operating_system`;

ALTER TABLE `feedback_repository`.`context_informations` ADD COLUMN `android_version` varchar(45) AFTER `user_agent`, ADD COLUMN `local_time` datetime(6) AFTER `android_version`, ADD COLUMN `time_zone` varchar(45) AFTER `local_time`, ADD COLUMN `device_pixel_ratio` varchar(11) AFTER `time_zone`, ADD COLUMN `country` varchar(45) AFTER `device_pixel_ratio`, ADD COLUMN `region` varchar(255) AFTER `country`;

ALTER TABLE `feedback_repository`.`rating_feedbacks` ADD COLUMN `mechanism_id` int(11) AFTER `feedback_id`;

ALTER TABLE `feedback_repository`.`screenshot_feedbacks` ADD COLUMN `mechanism_id` int(11) AFTER `name`, ADD COLUMN `part` varchar(255) AFTER `mechanism_id`, ADD COLUMN `file_extension` varchar(10) AFTER `part`;

ALTER TABLE `feedback_repository`.`feedbacks` DROP COLUMN `text`;

