-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 05. Jan 2018 um 08:54
-- Server-Version: 5.7.14
-- PHP-Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `monitor_feedback_repository`
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

--
-- Daten für Tabelle `api_user`
--

INSERT INTO `api_user` (`id`, `name`, `password`) VALUES
(1, 'admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy'),
(2, 'app_admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy'),
(3, 'super_admin', '$2a$10$y9K.1fd6VgT26rftcoziV.Qm74r8Qe1Y0hv.Kw4L1e3IMsxEXdWJu');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `api_user_api_user_role`
--

CREATE TABLE `api_user_api_user_role` (
  `id` bigint(20) NOT NULL,
  `api_user_role` int(11) DEFAULT NULL,
  `api_user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `api_user_api_user_role`
--

INSERT INTO `api_user_api_user_role` (`id`, `api_user_role`, `api_user_id`) VALUES
(1, 1, 1),
(2, 0, 2),
(3, 1, 3);

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

--
-- Daten für Tabelle `api_user_permission`
--

INSERT INTO `api_user_permission` (`id`, `application_id`, `has_permission`, `api_user_id`) VALUES
(2, 20, b'1', 2),
(1, 20, b'1', 1),
(3, 20, b'1', 3);

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

--
-- Daten für Tabelle `category_feedback`
--

INSERT INTO `category_feedback` (`id`, `mechanism_id`, `parameter_id`, `text`, `feedback_id`) VALUES
(1, 0, 663, '', 156);

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
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `comment_feedback`
--

INSERT INTO `comment_feedback` (`id`, `active_status`, `bool_is_developer`, `comment_text`, `created_at`, `updated_at`, `feedback_id`, `fk_parent_comment`, `user_id`) VALUES
(1, NULL, NULL, 'test mocko', NULL, NULL, 1, NULL, 1);

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

--
-- Daten für Tabelle `context_information`
--

INSERT INTO `context_information` (`id`, `android_version`, `country`, `device_pixel_ratio`, `local_time`, `meta_data`, `region`, `resolution`, `time_zone`, `url`, `user_agent`, `feedback_id`) VALUES
(1, NULL, NULL, 1, '2017-12-22 00:57:45', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36', 156);

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
  `username` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `end_user`
--

INSERT INTO `end_user` (`id`, `application_id`, `created_at`, `phone_number`, `updated_at`, `username`) VALUES
(1, 20, NULL, 123, NULL, 'kaydin');

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
  `is_blocked` tinyint(1) DEFAULT '0',
  `like_count` int(11) NOT NULL,
  `unread_comment_count` int(11) NOT NULL,
  `published` tinyint(1) DEFAULT '0',
  `visibility` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `feedback`
--

INSERT INTO `feedback` (`id`, `application_id`, `configuration_id`, `created_at`, `language`, `title`, `updated_at`, `user_identification`, `comment_count`, `dislike_count`, `icon_path`, blocked, `like_count`, `unread_comment_count`, `published`, `visibility`) VALUES
(1, 20, 46, '2017-11-27 10:55:55', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(2, 20, 46, '2017-11-27 10:59:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(3, 20, 46, '2017-11-27 11:01:05', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(4, 20, 46, '2017-11-28 14:25:14', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(5, 20, 46, '2017-11-28 14:39:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(6, 20, 46, '2017-11-29 09:55:43', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(7, 20, 46, '2017-11-29 09:57:09', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(8, 20, 46, '2017-11-29 10:19:22', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(9, 20, 46, '2017-11-29 10:19:34', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(10, 20, 46, '2017-11-29 10:32:47', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(11, 20, 46, '2017-11-29 10:38:40', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(12, 20, 46, '2017-11-29 10:41:16', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(13, 20, 46, '2017-11-29 10:44:36', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(14, 20, 46, '2017-11-29 10:51:05', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(15, 20, 46, '2017-11-29 10:51:08', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(16, 20, 46, '2017-11-29 10:58:37', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(17, 20, 46, '2017-11-29 11:11:02', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(18, 20, 46, '2017-11-29 11:16:47', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(19, 20, 46, '2017-11-29 11:23:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(20, 20, 46, '2017-11-29 11:26:20', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(21, 20, 46, '2017-11-29 11:35:50', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(22, 20, 46, '2017-11-29 11:42:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(23, 20, 46, '2017-11-29 11:45:39', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(24, 20, 46, '2017-11-29 11:46:15', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(25, 20, 46, '2017-11-29 11:48:01', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(26, 20, 46, '2017-11-29 11:48:20', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(27, 20, 46, '2017-11-29 11:54:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(28, 20, 46, '2017-11-29 13:04:31', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(29, 20, 46, '2017-11-29 13:06:50', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(30, 20, 46, '2017-11-29 18:22:08', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(31, 20, 46, '2017-11-29 18:34:44', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(32, 20, 46, '2017-11-30 18:06:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(33, 20, 46, '2017-11-30 19:53:40', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(34, 20, 46, '2017-11-30 20:11:58', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(35, 20, 46, '2017-11-30 20:17:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(36, 20, 46, '2017-11-30 20:22:11', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(37, 20, 46, '2017-11-30 20:25:42', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(38, 20, 46, '2017-11-30 20:33:59', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(39, 20, 46, '2017-11-30 20:42:09', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(40, 20, 46, '2017-11-30 20:44:18', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(41, 20, 46, '2017-11-30 20:45:30', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(42, 20, 46, '2017-11-30 20:47:21', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(43, 20, 46, '2017-11-30 20:52:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(44, 20, 46, '2017-11-30 21:38:20', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(45, 20, 46, '2017-11-30 21:40:09', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(46, 20, 46, '2017-11-30 21:42:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(47, 20, 46, '2017-11-30 21:51:43', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(48, 20, 46, '2017-11-30 21:53:50', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(49, 20, 46, '2017-11-30 22:01:45', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(50, 20, 46, '2017-11-30 22:11:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(51, 20, 46, '2017-11-30 22:18:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(52, 20, 46, '2017-11-30 22:21:23', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(53, 20, 46, '2017-11-30 22:28:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(54, 20, 46, '2017-12-01 10:40:42', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(55, 20, 46, '2017-12-01 10:42:28', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(56, 20, 46, '2017-12-01 18:27:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(57, 20, 46, '2017-12-01 18:37:16', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(58, 20, 46, '2017-12-01 18:40:22', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(59, 20, 46, '2017-12-01 19:14:32', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(60, 20, 46, '2017-12-01 19:20:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(61, 20, 46, '2017-12-01 19:23:37', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(62, 20, 46, '2017-12-01 19:26:00', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(63, 20, 46, '2017-12-02 00:29:51', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(64, 20, 46, '2017-12-02 00:35:57', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(65, 20, 46, '2017-12-02 00:42:36', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(66, 20, 46, '2017-12-02 00:56:43', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(67, 20, 46, '2017-12-03 20:20:48', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(68, 20, 46, '2017-12-03 20:47:40', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(69, 20, 46, '2017-12-03 20:50:32', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(70, 20, 46, '2017-12-03 21:01:48', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(71, 20, 46, '2017-12-03 21:03:57', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(72, 20, 46, '2017-12-03 21:12:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(73, 20, 46, '2017-12-03 21:15:10', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(74, 20, 46, '2017-12-03 21:20:27', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(75, 20, 46, '2017-12-03 21:31:09', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(76, 20, 46, '2017-12-03 21:40:21', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(77, 20, 46, '2017-12-03 21:46:37', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(78, 20, 46, '2017-12-03 22:33:08', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(79, 20, 46, '2017-12-03 22:36:51', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(80, 20, 46, '2017-12-05 20:20:47', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(81, 20, 46, '2017-12-05 20:52:07', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(82, 20, 46, '2017-12-05 20:57:20', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(83, 20, 46, '2017-12-05 20:58:42', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(84, 20, 46, '2017-12-06 09:29:28', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(85, 20, 46, '2017-12-06 12:50:54', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(86, 20, 46, '2017-12-06 13:03:53', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(87, 20, 46, '2017-12-06 13:05:38', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(88, 20, 46, '2017-12-06 13:15:09', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(89, 20, 46, '2017-12-06 13:40:07', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(90, 20, 46, '2017-12-06 13:51:36', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(91, 20, 46, '2017-12-06 14:01:48', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(92, 20, 46, '2017-12-06 14:03:24', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(93, 20, 46, '2017-12-06 15:30:19', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(94, 20, 46, '2017-12-06 15:33:18', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(95, 20, 46, '2017-12-06 15:34:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(96, 20, 46, '2017-12-06 15:42:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(97, 20, 46, '2017-12-06 15:45:33', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(98, 20, 46, '2017-12-10 00:33:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(99, 20, 46, '2017-12-10 00:36:49', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(100, 20, 46, '2017-12-10 18:24:15', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(101, 20, 46, '2017-12-10 18:36:55', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(102, 20, 46, '2017-12-10 18:56:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(103, 20, 46, '2017-12-10 19:08:52', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(104, 20, 46, '2017-12-11 10:46:43', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(105, 20, 46, '2017-12-11 11:03:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(106, 20, 46, '2017-12-11 11:07:18', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(107, 20, 46, '2017-12-11 14:27:29', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(108, 20, 46, '2017-12-11 14:40:19', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(109, 20, 46, '2017-12-11 15:09:50', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(110, 20, 46, '2017-12-11 18:33:00', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(111, 20, 46, '2017-12-11 18:54:40', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(112, 20, 46, '2017-12-12 20:02:55', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(113, 20, 46, '2017-12-12 20:11:33', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(114, 20, 46, '2017-12-12 20:47:36', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(115, 20, 46, '2017-12-12 20:49:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(116, 20, 46, '2017-12-12 21:22:49', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(117, 20, 46, '2017-12-13 19:22:59', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(118, 20, 46, '2017-12-13 19:29:43', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(119, 20, 46, '2017-12-13 19:33:10', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(120, 20, 46, '2017-12-13 19:37:56', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(121, 20, 46, '2017-12-13 19:44:15', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(122, 20, 46, '2017-12-13 19:47:10', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(123, 20, 46, '2017-12-14 07:54:05', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(124, 20, 46, '2017-12-14 08:00:29', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(125, 20, 46, '2017-12-14 08:11:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(126, 20, 46, '2017-12-14 08:13:12', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(127, 20, 46, '2017-12-14 08:20:52', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(128, 20, 46, '2017-12-14 08:24:51', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(129, 20, 46, '2017-12-14 08:28:14', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(130, 20, 46, '2017-12-14 08:59:21', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(131, 20, 46, '2017-12-14 09:05:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(132, 20, 46, '2017-12-14 09:09:10', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(133, 20, 46, '2017-12-14 09:15:07', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(134, 20, 46, '2017-12-14 17:40:12', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(135, 20, 46, '2017-12-14 17:42:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(136, 20, 46, '2017-12-14 17:45:08', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(137, 20, 46, '2017-12-14 17:48:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(138, 20, 46, '2017-12-14 17:50:11', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(139, 20, 46, '2017-12-14 17:54:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(140, 20, 46, '2017-12-14 17:55:57', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(141, 20, 46, '2017-12-21 08:04:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(142, 20, 46, '2017-12-21 15:43:38', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(143, 20, 46, '2017-12-21 22:59:04', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(144, 20, 46, '2017-12-21 23:02:13', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(145, 20, 46, '2017-12-21 23:30:24', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(146, 20, 46, '2017-12-21 23:34:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(147, 20, 46, '2017-12-21 23:37:38', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(148, 20, 46, '2017-12-21 23:40:25', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(149, 20, 46, '2017-12-21 23:51:31', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(150, 20, 46, '2017-12-21 23:55:07', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(151, 20, 46, '2017-12-21 23:56:42', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(152, 20, 46, '2017-12-22 00:03:00', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(153, 20, 46, '2017-12-22 00:05:37', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(154, 20, 46, '2017-12-22 00:07:44', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(155, 20, 46, '2017-12-22 00:08:04', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(156, 20, 46, '2017-12-22 00:57:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1);

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
  `feedback_id` bigint(20) DEFAULT NULL
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

--
-- Daten für Tabelle `rating_feedback`
--

INSERT INTO `rating_feedback` (`id`, `mechanism_id`, `rating`, `title`, `feedback_id`) VALUES
(1, 90, 2, 'Bewerten Sie das Feature, das Sie gerade benutzt haben.', 156);

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
-- Tabellenstruktur für Tabelle `status_options`
--

CREATE TABLE `status_options` (
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

--
-- Daten für Tabelle `text_feedback`
--

INSERT INTO `text_feedback` (`id`, `mechanism_id`, `text`, `feedback_id`) VALUES
(1, 88, 'kjijm', 156);

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
-- Indizes für die Tabelle `context_information`
--
ALTER TABLE `context_information`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgjes53m9wrcpnnq0kl898yjul` (`feedback_id`);

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
-- Indizes für die Tabelle `status_options`
--
ALTER TABLE status_option
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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT für Tabelle `api_user_api_user_role`
--
ALTER TABLE `api_user_api_user_role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT für Tabelle `api_user_permission`
--
ALTER TABLE `api_user_permission`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `attachment_feedback`
--
ALTER TABLE `attachment_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `audio_feedback`
--
ALTER TABLE `audio_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `category_feedback`
--
ALTER TABLE `category_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `comment_feedback`
--
ALTER TABLE `comment_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `context_information`
--
ALTER TABLE `context_information`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `end_user`
--
ALTER TABLE `end_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=157;
--
-- AUTO_INCREMENT für Tabelle `feedback_chat_information`
--
ALTER TABLE `feedback_chat_information`
  MODIFY `feedback_chat_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `feedback_company`
--
ALTER TABLE `feedback_company`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `feedback_settings`
--
ALTER TABLE `feedback_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `feedback_status`
--
ALTER TABLE `feedback_status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `file_feedback`
--
ALTER TABLE `file_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `rating_feedback`
--
ALTER TABLE `rating_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `screenshot_feedback`
--
ALTER TABLE `screenshot_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `setting`
--
ALTER TABLE `setting`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `status`
--
ALTER TABLE `status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `status_options`
--
ALTER TABLE status_option
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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `userfbdislike`
--
ALTER TABLE `userfbdislike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `userfblike`
--
ALTER TABLE `userfblike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
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
