SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `text_feedbacks`
-- ----------------------------
DROP TABLE IF EXISTS `text_feedbacks`;
CREATE TABLE `text_feedbacks` (
  `id` int(11) NOT NULL,
  `text` text NOT NULL,
  `mechanism_id` int(11) DEFAULT NULL,
  `feedback_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_id` (`feedback_id`),
  CONSTRAINT `feedback_id_foreign_key` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
