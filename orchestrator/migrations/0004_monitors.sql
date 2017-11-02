DROP TABLE IF EXISTS `monitor_configuration`;
DROP TABLE IF EXISTS `monitor_tool`;
DROP TABLE IF EXISTS `monitor_type`;

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
--  Records of `monitor_type`
-- ----------------------------
BEGIN;
INSERT INTO `monitor_type` VALUES ('1', 'SocialNetworks', '2016-11-01 13:55:06.000'), ('2', 'MarketPlaces', '2016-11-01 13:55:06.000'), ('3', 'QoS', '2017-02-20 17:49:06.409');
COMMIT;

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
--  Records of `monitor_tool`
-- ----------------------------
BEGIN;
INSERT INTO `monitor_tool` VALUES ('1', 'TwitterAPI', '2016-11-01 13:55:06.000', '1', 'Twitter'), ('2', 'GooglePlayAPI', '2016-11-01 13:55:06.000', '2', 'GooglePlay'), ('3', 'AppTweak-GooglePlay', '2016-11-01 13:55:06.000', '2', 'GooglePlay'), ('4', 'AppStoreAPI', '2016-11-01 13:55:06.000', '2', 'AppStore'), ('5', 'AppTweak-AppStore', '2016-11-01 13:55:06.000', '2', 'AppStore'), ('6', 'ApacheHttp', '2017-02-20 17:49:06.432', '3', 'Http');
COMMIT;

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
--  Records of `monitor_configuration`
-- ----------------------------
