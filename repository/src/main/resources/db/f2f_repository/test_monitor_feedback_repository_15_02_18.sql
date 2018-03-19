-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 15. Feb 2018 um 15:02
-- Server-Version: 5.7.14
-- PHP-Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `test_monitor_feedback_repository`
--
CREATE DATABASE IF NOT EXISTS `test_monitor_feedback_repository` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `test_monitor_feedback_repository`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `api_user`
--

CREATE TABLE `api_user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `api_user_api_user_role`
--

CREATE TABLE `api_user_api_user_role` (
  `id` bigint(20) NOT NULL,
  `api_user_role` int(11) DEFAULT NULL,
  `api_user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `api_user_permission`
--

CREATE TABLE `api_user_permission` (
  `id` bigint(20) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `has_permission` bit(1) NOT NULL,
  `api_user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `attachment_feedback`
--

CREATE TABLE `attachment_feedback` (
  `id` bigint(20) NOT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `audio_feedback`
--

CREATE TABLE `audio_feedback` (
  `id` bigint(20) NOT NULL,
  `duration` int(11) NOT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `category_feedback`
--

CREATE TABLE `category_feedback` (
  `id` bigint(20) NOT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `parameter_id` bigint(20) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `comment_feedback`
--

CREATE TABLE `comment_feedback` (
  `id` bigint(20) NOT NULL,
  `active_status` bit(1) DEFAULT NULL,
  `bool_is_developer` bit(1) DEFAULT NULL,
  `comment_text` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `fk_parent_comment` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `anonymous` bit(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Trigger `comment_feedback`
--
DELIMITER $$
CREATE TRIGGER `update_comment` AFTER INSERT ON `comment_feedback` FOR EACH ROW BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_comment2` AFTER DELETE ON `comment_feedback` FOR EACH ROW BEGIN
    UPDATE feedback
    SET comment_count = (SELECT count(*) FROM comment_feedback WHERE feedback_id=feedback.id);
  END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `comment_viewed`
--

CREATE TABLE `comment_viewed` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `comment_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `context_information`
--

CREATE TABLE `context_information` (
  `id` bigint(20) NOT NULL,
  `android_version` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `device_pixel_ratio` float DEFAULT NULL,
  `local_time` datetime DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_unsubscribed`
--

CREATE TABLE `email_unsubscribed` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `end_user`
--

CREATE TABLE `end_user` (
  `id` bigint(20) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `phone_number` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback`
--

CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `configuration_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_identification` bigint(11) DEFAULT NULL,
  `comment_count` int(11) NOT NULL,
  `dislike_count` int(11) NOT NULL,
  `icon_path` varchar(255) DEFAULT NULL,
  `blocked` tinyint(1) DEFAULT '0',
  `like_count` int(11) NOT NULL,
  `unread_comment_count` int(11) NOT NULL,
  `published` tinyint(1) DEFAULT '0',
  `visibility` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_chat_information`
--

CREATE TABLE `feedback_chat_information` (
  `feedback_chat_id` bigint(20) NOT NULL,
  `chat_date` datetime DEFAULT NULL,
  `chat_text` varchar(255) DEFAULT NULL,
  `initated_by_user` bit(1) DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_company`
--

CREATE TABLE `feedback_company` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  `promote` bit(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_settings`
--

CREATE TABLE `feedback_settings` (
  `id` bigint(20) NOT NULL,
  `feedback_query` bit(1) NOT NULL,
  `feedback_query_channel` varchar(255) NOT NULL,
  `global_feedback_setting` bit(1) NOT NULL,
  `status_updates` bit(1) NOT NULL,
  `status_updates_contact_channel` varchar(255) NOT NULL,
  `feedback_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_status`
--

CREATE TABLE `feedback_status` (
  `id` bigint(20) NOT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_viewed`
--

CREATE TABLE `feedback_viewed` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `file_feedback`
--

CREATE TABLE `file_feedback` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `mechanism_id` bigint(20) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rating_feedback`
--

CREATE TABLE `rating_feedback` (
  `id` bigint(20) NOT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `screenshot_feedback`
--

CREATE TABLE `screenshot_feedback` (
  `id` bigint(20) NOT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `setting`
--

CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `feedback_email_receivers` varchar(255) DEFAULT NULL,
  `kafka_topic_id` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `setting`
--

INSERT INTO `setting` (`id`, `application_id`, `feedback_email_receivers`, `kafka_topic_id`) VALUES
(125, 20, '', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `status`
--

CREATE TABLE `status` (
  `id` bigint(20) NOT NULL,
  `api_user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL,
  `status_option_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `status_option`
--

CREATE TABLE `status_option` (
  `id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `order` int(11) NOT NULL,
  `user_specific` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `text_annotation`
--

CREATE TABLE `text_annotation` (
  `id` bigint(20) NOT NULL,
  `reference_number` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `screenshot_feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `text_feedback`
--

CREATE TABLE `text_feedback` (
  `id` bigint(20) NOT NULL,
  `mechanism_id` bigint(20) NOT NULL,
  `text` longtext,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userfbdislike`
--

CREATE TABLE `userfbdislike` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Trigger `userfbdislike`
--
DELIMITER $$
CREATE TRIGGER `update_dislike` AFTER INSERT ON `userfbdislike` FOR EACH ROW BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_dislike2` AFTER DELETE ON `userfbdislike` FOR EACH ROW BEGIN
    UPDATE feedback
    SET dislike_count = (SELECT count(*) FROM userfbdislike WHERE feedback_id = feedback.id);
  END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userfblike`
--

CREATE TABLE `userfblike` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `feedback_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Trigger `userfblike`
--
DELIMITER $$
CREATE TRIGGER `update_like` AFTER INSERT ON `userfblike` FOR EACH ROW BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_like2` AFTER DELETE ON `userfblike` FOR EACH ROW BEGIN
    UPDATE feedback
    SET like_count = (SELECT count(*) FROM userfblike WHERE feedback_id = feedback.id);
  END
$$
DELIMITER ;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `api_user`
--
ALTER TABLE `api_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_scb81k0sobpewfaxhwyquccop` (`name`);

--
-- Indizes für die Tabelle `api_user_api_user_role`
--
ALTER TABLE `api_user_api_user_role`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmh0ci62ckvoi95vgx5nj4n0a3` (`api_user_id`);

--
-- Indizes für die Tabelle `api_user_permission`
--
ALTER TABLE `api_user_permission`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3eosps1v8ku1jdk3m3k8vohxy` (`api_user_id`);

--
-- Indizes für die Tabelle `attachment_feedback`
--
ALTER TABLE `attachment_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKoq4taf5qmyusdvl1m5kj4rgco` (`feedback_id`);

--
-- Indizes für die Tabelle `audio_feedback`
--
ALTER TABLE `audio_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9bfrsrtg3qdk4ima6jlbb6e4m` (`feedback_id`);

--
-- Indizes für die Tabelle `category_feedback`
--
ALTER TABLE `category_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK83xs7iqr1bq6a5isel0ceded8` (`feedback_id`);

--
-- Indizes für die Tabelle `comment_feedback`
--
ALTER TABLE `comment_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsjg1n0mnkm7haiyt6k8pqbd4x` (`feedback_id`),
  ADD KEY `FKh1fyv8nt5no6b2yw1q3a7j1kt` (`fk_parent_comment`),
  ADD KEY `FK7rsl9hwpd3dh6ucn5pxuo6j4d` (`user_id`);

--
-- Indizes für die Tabelle `comment_viewed`
--
ALTER TABLE `comment_viewed`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKamfb2bvnx3tj8stougb7hsuyq` (`user_id`),
  ADD KEY `FKt7aa1dhhfvrlyukmtktgdxraf` (`comment_id`);

--
-- Indizes für die Tabelle `context_information`
--
ALTER TABLE `context_information`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`);

--
-- Indizes für die Tabelle `email_unsubscribed`
--
ALTER TABLE `email_unsubscribed`
  ADD PRIMARY KEY (`id`),
  ADD KEY `email_unsubscribed_end_user_id_fk` (`user_id`);

--
-- Indizes für die Tabelle `end_user`
--
ALTER TABLE `end_user`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `feedback_chat_information`
--
ALTER TABLE `feedback_chat_information`
  ADD PRIMARY KEY (`feedback_chat_id`),
  ADD KEY `FK499lsagiuhb2k5jkg40peuhet` (`feedback_id`),
  ADD KEY `FK3knnwken9u06o1w2i285ubki0` (`user_id`);

--
-- Indizes für die Tabelle `feedback_company`
--
ALTER TABLE `feedback_company`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5yada2i1ca48okpp3yegv5xxf` (`feedback_id`);

--
-- Indizes für die Tabelle `feedback_settings`
--
ALTER TABLE `feedback_settings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp6kkf7ntjn9xyrxmxw1ko86x4` (`feedback_id`),
  ADD KEY `FKgn66krh9w9ker8j9dbfuu95s7` (`user_id`);

--
-- Indizes für die Tabelle `feedback_status`
--
ALTER TABLE `feedback_status`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK83nnfbwocw4hwq7l4h3tv6k59` (`feedback_id`);

--
-- Indizes für die Tabelle `feedback_viewed`
--
ALTER TABLE `feedback_viewed`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8b4kr36xe7xqkc6mr4yavlxjn` (`user_id`),
  ADD KEY `FKabbwmf5dldpp256m5b2jgdj0t` (`feedback_id`);

--
-- Indizes für die Tabelle `file_feedback`
--
ALTER TABLE `file_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa93pglqlwxt6kxxqn248si979` (`feedback_id`);

--
-- Indizes für die Tabelle `rating_feedback`
--
ALTER TABLE `rating_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa5merp6x61nokyyvx17snw5bb` (`feedback_id`);

--
-- Indizes für die Tabelle `screenshot_feedback`
--
ALTER TABLE `screenshot_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsekb4tg5bivjepodpeyt7eplc` (`feedback_id`);

--
-- Indizes für die Tabelle `setting`
--
ALTER TABLE `setting`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKho80mb8i1oa2guwbceli8s974` (`api_user_id`),
  ADD KEY `FKgcq19tbyg1cqwehj46h5mx4we` (`feedback_id`),
  ADD KEY `FKom9esrrv51wom5clx294iehoo` (`status_option_id`);

--
-- Indizes für die Tabelle `status_option`
--
ALTER TABLE `status_option`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `text_annotation`
--
ALTER TABLE `text_annotation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKi8nbnsfcgsaujg8al7anyb1pr` (`screenshot_feedback_id`);

--
-- Indizes für die Tabelle `text_feedback`
--
ALTER TABLE `text_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKtidcgd0wra4sxqlawcp4li155` (`feedback_id`);

--
-- Indizes für die Tabelle `userfbdislike`
--
ALTER TABLE `userfbdislike`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKtlnqx1jqmqfaqn4j6i6wqryu9` (`user_id`),
  ADD KEY `FKkr3ipjy9wac2fy4yqke642j5u` (`feedback_id`);

--
-- Indizes für die Tabelle `userfblike`
--
ALTER TABLE `userfblike`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK88ilib4q2rbk1qyqwg9jfenno` (`user_id`),
  ADD KEY `FKlyp3v77sj8c15xksh618r5s61` (`feedback_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `api_user`
--
ALTER TABLE `api_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1971;
--
-- AUTO_INCREMENT für Tabelle `api_user_api_user_role`
--
ALTER TABLE `api_user_api_user_role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1966;
--
-- AUTO_INCREMENT für Tabelle `api_user_permission`
--
ALTER TABLE `api_user_permission`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=958;
--
-- AUTO_INCREMENT für Tabelle `attachment_feedback`
--
ALTER TABLE `attachment_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT für Tabelle `audio_feedback`
--
ALTER TABLE `audio_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `category_feedback`
--
ALTER TABLE `category_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
--
-- AUTO_INCREMENT für Tabelle `comment_feedback`
--
ALTER TABLE `comment_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1352;
--
-- AUTO_INCREMENT für Tabelle `comment_viewed`
--
ALTER TABLE `comment_viewed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;
--
-- AUTO_INCREMENT für Tabelle `context_information`
--
ALTER TABLE `context_information`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
--
-- AUTO_INCREMENT für Tabelle `email_unsubscribed`
--
ALTER TABLE `email_unsubscribed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;
--
-- AUTO_INCREMENT für Tabelle `end_user`
--
ALTER TABLE `end_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1230;
--
-- AUTO_INCREMENT für Tabelle `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `feedback_chat_information`
--
ALTER TABLE `feedback_chat_information`
  MODIFY `feedback_chat_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;
--
-- AUTO_INCREMENT für Tabelle `feedback_company`
--
ALTER TABLE `feedback_company`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=140;
--
-- AUTO_INCREMENT für Tabelle `feedback_settings`
--
ALTER TABLE `feedback_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120;
--
-- AUTO_INCREMENT für Tabelle `feedback_status`
--
ALTER TABLE `feedback_status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123;
--
-- AUTO_INCREMENT für Tabelle `feedback_viewed`
--
ALTER TABLE `feedback_viewed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;
--
-- AUTO_INCREMENT für Tabelle `file_feedback`
--
ALTER TABLE `file_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `rating_feedback`
--
ALTER TABLE `rating_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT für Tabelle `screenshot_feedback`
--
ALTER TABLE `screenshot_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT für Tabelle `setting`
--
ALTER TABLE `setting`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=126;
--
-- AUTO_INCREMENT für Tabelle `status`
--
ALTER TABLE `status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `status_option`
--
ALTER TABLE `status_option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `text_annotation`
--
ALTER TABLE `text_annotation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `text_feedback`
--
ALTER TABLE `text_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=344;
--
-- AUTO_INCREMENT für Tabelle `userfbdislike`
--
ALTER TABLE `userfbdislike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=200;
--
-- AUTO_INCREMENT für Tabelle `userfblike`
--
ALTER TABLE `userfblike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=195;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `file_feedback`
--
ALTER TABLE `file_feedback`
  ADD CONSTRAINT `FKa93pglqlwxt6kxxqn248si979` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
