CREATE TABLE `text_annotations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `screenshot_feedbacks_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_text_annotations_1_idx` (`screenshot_feedbacks_id`),
  CONSTRAINT `fk_text_annotations_1` FOREIGN KEY (`screenshot_feedbacks_id`) REFERENCES `screenshot_feedbacks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=big5;
