CREATE TABLE `feedback_repository`.`statuses` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`api_user_id` int(10) NOT NULL,
	`feedback_id` int(10) NOT NULL,
	`status` varchar(255) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `statuses_api_user_id_foreign_key` FOREIGN KEY (`api_user_id`) REFERENCES `feedback_repository`.`api_users` (`id`)   ON UPDATE NO ACTION ON DELETE CASCADE,
	CONSTRAINT `statuses_feedback_id_foreign_key` FOREIGN KEY (`feedback_id`) REFERENCES `feedback_repository`.`feedbacks` (`id`)   ON UPDATE NO ACTION ON DELETE CASCADE
) COMMENT='';