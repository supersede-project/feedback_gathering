CREATE TABLE `comment_feedback` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `comment_text` text NOT NULL,
  `feedbacks_id` int(20) NOT NULL,
  `bool_is_developer` int(1) NOT NULL,
  `username` varchar(45) NOT NULL,
  `avatar_path` varchar(255) NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_feedback_idx` (`feedback_id`),
  CONSTRAINT `fk_comment_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
