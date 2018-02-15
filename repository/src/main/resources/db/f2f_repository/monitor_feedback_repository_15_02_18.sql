-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 15. Feb 2018 um 15:01
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
CREATE DATABASE IF NOT EXISTS `monitor_feedback_repository` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `monitor_feedback_repository`;

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
(1, 20, b'1', 1),
(2, 20, b'1', 2),
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
(1, 0, 516, '', 173),
(2, 0, 516, '', 174),
(3, 0, 516, '', 175),
(4, 0, 516, '', 176),
(5, 0, 516, '', 177),
(6, 0, 516, '', 178),
(7, 0, 516, '', 179),
(8, 0, 516, '', 180),
(9, 0, 516, '', 181),
(10, 0, 516, '', 182),
(11, 0, 516, '', 183),
(12, 0, 516, '', 184),
(13, 0, 516, '', 185),
(14, 0, 516, '', 186),
(15, 0, 516, '', 187),
(16, 0, 516, '', 188),
(17, 0, 516, '', 189),
(18, 0, 516, '', 190),
(19, 0, 516, '', 191),
(20, 0, 516, '', 192),
(21, 0, 516, '', 193),
(22, 0, 516, '', 194),
(23, 0, 516, '', 195),
(24, 0, 516, '', 196),
(25, 0, 516, '', 197),
(26, 0, 516, '', 198),
(27, 0, 516, '', 199),
(28, 0, 516, '', 200),
(29, 0, 516, '', 201),
(30, 0, 516, '', 202),
(31, 0, 516, '', 203),
(32, 0, 516, '', 204),
(33, 0, 516, '', 205),
(34, 0, 516, '', 206),
(35, 0, 516, '', 207),
(36, 0, 516, '', 208),
(37, 0, 516, '', 209),
(38, 0, 516, '', 210),
(39, 0, 516, '', 211),
(40, 0, 516, '', 212),
(41, 0, 516, '', 213),
(42, 0, 516, '', 214),
(43, 0, 516, '', 215),
(44, 0, 516, '', 216),
(45, 0, 516, '', 217),
(46, 0, 516, '', 218),
(47, 0, 516, '', 219),
(48, 0, 516, '', 220),
(49, 0, 516, '', 221),
(50, 0, 516, '', 222),
(51, 0, 516, '', 223),
(52, 0, 516, '', 224),
(53, 0, 516, '', 225),
(54, 0, 663, '', 226),
(55, 0, 663, '', 227),
(56, 0, 663, '', 228),
(57, 0, 515, '', 229),
(58, 0, 661, '', 230);

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
-- Daten für Tabelle `comment_feedback`
--

INSERT INTO `comment_feedback` (`id`, `active_status`, `bool_is_developer`, `comment_text`, `created_at`, `updated_at`, `feedback_id`, `fk_parent_comment`, `user_id`, `anonymous`) VALUES
(6, NULL, NULL, 'test comment', '2018-02-02 17:08:42', NULL, 1, NULL, 1, b'1'),
(7, b'0', b'1', 'test anonymous comment', '2018-02-02 18:54:16', NULL, 1, 6, 1, b'1');

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
  `comment_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `comment_viewed`
--

INSERT INTO `comment_viewed` (`id`, `created_at`, `comment_id`, `user_id`) VALUES
(1, '2018-02-02 17:26:48', 6, 1);

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
(1, NULL, NULL, 1, '2018-01-26 19:21:39', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 173),
(2, NULL, NULL, 1, '2018-01-26 19:24:35', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 174),
(3, NULL, NULL, 1, '2018-01-26 19:27:49', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 175),
(4, NULL, NULL, 1, '2018-01-26 19:30:48', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 176),
(5, NULL, NULL, 1, '2018-01-26 19:37:59', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 177),
(6, NULL, NULL, 1, '2018-01-26 19:41:48', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 178),
(7, NULL, NULL, 1, '2018-01-26 19:44:26', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 179),
(8, NULL, NULL, 1, '2018-01-26 19:57:07', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 180),
(9, NULL, NULL, 1, '2018-01-26 19:59:38', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 181),
(10, NULL, NULL, 1, '2018-01-26 20:12:53', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 182),
(11, NULL, NULL, 1, '2018-01-26 21:04:06', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 183),
(12, NULL, NULL, 1, '2018-01-26 21:07:58', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 184),
(13, NULL, NULL, 1, '2018-01-26 21:10:45', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 185),
(14, NULL, NULL, 1, '2018-01-26 21:18:52', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 186),
(15, NULL, NULL, 1, '2018-01-26 21:24:44', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 187),
(16, NULL, NULL, 1, '2018-01-26 21:35:33', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 188),
(17, NULL, NULL, 1, '2018-01-26 21:42:40', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 189),
(18, NULL, NULL, 1, '2018-01-26 22:15:03', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 190),
(19, NULL, NULL, 1, '2018-01-27 00:36:18', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 191),
(20, NULL, NULL, 1, '2018-01-27 00:58:10', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 192),
(21, NULL, NULL, 1, '2018-01-27 01:00:30', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 193),
(22, NULL, NULL, 1, '2018-01-27 01:01:04', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 194),
(23, NULL, NULL, 1, '2018-01-27 08:57:04', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=5&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 195),
(24, NULL, NULL, 1, '2018-01-27 09:02:08', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=5&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 196),
(25, NULL, NULL, 1, '2018-01-27 09:03:21', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=5&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 197),
(26, NULL, NULL, 1, '2018-01-27 09:03:58', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=5&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 198),
(27, NULL, NULL, 1, '2018-01-27 09:07:44', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=6&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 199),
(28, NULL, NULL, 1, '2018-01-27 09:08:51', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=6&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 200),
(29, NULL, NULL, 1, '2018-01-27 09:12:31', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=6&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 201),
(30, NULL, NULL, 1, '2018-01-27 09:13:06', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=6&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 202),
(31, NULL, NULL, 1, '2018-01-27 09:16:35', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=7&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 203),
(32, NULL, NULL, 1, '2018-01-27 09:17:15', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=7&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 204),
(33, NULL, NULL, 1, '2018-01-27 09:25:26', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=8&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 205),
(34, NULL, NULL, 1, '2018-01-27 09:26:03', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=8&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 206),
(35, NULL, NULL, 1, '2018-01-27 09:38:38', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=9&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 207),
(36, NULL, NULL, 1, '2018-01-27 09:47:59', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=10&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 208),
(37, NULL, NULL, 1, '2018-01-27 09:56:59', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=11&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 209),
(38, NULL, NULL, 1, '2018-01-27 09:59:00', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=11&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 210),
(39, NULL, NULL, 1, '2018-01-27 10:01:24', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=12&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 211),
(40, NULL, NULL, 1, '2018-01-27 10:11:50', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=13&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 212),
(41, NULL, NULL, 1, '2018-01-27 10:12:56', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=13&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 213),
(42, NULL, NULL, 1, '2018-01-27 10:36:10', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=14&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 214),
(43, NULL, NULL, 1, '2018-01-27 10:42:29', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=15&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 215),
(44, NULL, NULL, 1, '2018-01-27 10:54:40', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=16&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 216),
(45, NULL, NULL, 1, '2018-01-27 10:55:24', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=16&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 217),
(46, NULL, NULL, 1, '2018-01-27 11:01:06', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=17&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 218),
(47, NULL, NULL, 1, '2018-01-27 11:01:48', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=17&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 219),
(48, NULL, NULL, 1, '2018-01-27 11:05:28', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=18&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 220),
(49, NULL, NULL, 1, '2018-01-27 11:05:54', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=18&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 221),
(50, NULL, NULL, 1, '2018-01-27 15:04:08', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=19&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 222),
(51, NULL, NULL, 1, '2018-01-27 15:05:09', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=19&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 223),
(52, NULL, NULL, 1, '2018-01-27 15:05:41', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=19&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 224),
(53, NULL, NULL, 1, '2018-01-27 15:06:08', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=19&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 225),
(54, NULL, NULL, 1, '2018-01-27 22:37:35', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:3000/', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 226),
(55, NULL, NULL, 1, '2018-01-27 22:39:05', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:3000/', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 227),
(56, NULL, NULL, 1, '2018-01-27 22:39:39', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:3000/', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 228),
(57, NULL, NULL, 1, '2018-01-30 14:42:21', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:9090/index.php?user_id=99999999&application_id=20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 229),
(58, NULL, NULL, 1, '2018-02-04 11:37:11', '{"diagram":"diagram X 02","section":"section#diagramSection"}', NULL, '728x1366', '+0100', 'http://localhost:3000/', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36', 230);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `email_unsubscribed`
--

CREATE TABLE `email_unsubscribed` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `email_unsubscribed`
--

INSERT INTO `email_unsubscribed` (`id`, `created_at`, `email`, `user_id`) VALUES
(1, '2018-01-31 15:34:03', 'f2f_central@hotmail.com', 1),
(3, '2018-01-31 23:09:15', 'f2f_central@hotmail.com', 5),
(4, '2018-01-31 23:09:16', 'f2f_central@hotmail.com', 10),
(5, '2018-01-31 23:09:17', 'f2f_central@hotmail.com', 13),
(6, '2018-01-31 23:09:18', 'f2f_central@hotmail.com', 16),
(7, '2018-01-31 23:09:44', 'f2f_central@hotmail.com', 18),
(8, '2018-01-31 23:37:25', 'f2f_central@hotmail.com', 2),
(9, '2018-01-31 23:37:27', 'f2f_central@hotmail.com', 3);

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

--
-- Daten für Tabelle `end_user`
--

INSERT INTO `end_user` (`id`, `application_id`, `created_at`, `phone_number`, `updated_at`, `username`, `email`) VALUES
(1, 20, NULL, 123, NULL, 'kaydin1', 'f2f_central@hotail.com'),
(2, 20, NULL, 123, NULL, 'kaydin2', 'f2f_central@hotmail.com'),
(3, 20, NULL, 123, NULL, 'kaydin3', 'f2f_central@hotmail.com'),
(99999999, 20, NULL, 123, NULL, 'test_user', 'f2f_central@hotmail.com'),
(5, 20, NULL, 123, NULL, 'kaydin5', 'f2f_central@hotmail.com'),
(6, 20, NULL, 123, NULL, 'kaydin6', 'f2f_central@hotmail.com'),
(7, 20, NULL, 12, NULL, 'kaydin7', 'f2f_central@hotmail.com'),
(8, 20, NULL, 12, NULL, 'kaydin8', 'f2f_central@hotmail.com'),
(9, 20, NULL, 12, NULL, 'kaydin9', 'f2f_central@hotmail.com'),
(10, 20, NULL, 12, NULL, 'kaydin10', 'f2f_central@hotmail.com'),
(11, 20, NULL, 12, NULL, 'kaydin11', 'f2f_central@hotmail.com'),
(12, 20, NULL, 12, NULL, 'kaydin12', 'f2f_central@hotmail.com'),
(13, 20, NULL, 12, NULL, 'kaydin13', 'f2f_central@hotmail.com'),
(14, 20, NULL, 12, NULL, 'kaydin14', 'f2f_central@hotmail.com'),
(15, 20, NULL, 12, NULL, 'kaydin15', 'f2f_central@hotmail.com'),
(16, 20, NULL, 12, NULL, 'kaydin16', 'f2f_central@hotmail.com'),
(17, 20, NULL, 2, NULL, 'kaydin17', 'f2f_central@hotmail.com'),
(18, 20, NULL, 123, NULL, 'kaydin18', 'f2f_central@hotmail.com'),
(22, 20, NULL, 123, NULL, 'kaydin22', 'f2f_central@hotmail.com'),
(23, 20, NULL, 123, NULL, 'kaydin23', 'f2f_central@hotmail.com'),
(24, 20, NULL, 123, NULL, 'kaydin24', 'f2f_central@hotmail.com');

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

--
-- Daten für Tabelle `feedback`
--

INSERT INTO `feedback` (`id`, `application_id`, `configuration_id`, `created_at`, `language`, `title`, `updated_at`, `user_identification`, `comment_count`, `dislike_count`, `icon_path`, `blocked`, `like_count`, `unread_comment_count`, `published`, `visibility`) VALUES
(1, 20, 46, '2017-11-27 10:55:55', 'en', 'Feedback', NULL, 99999999, 2, 1, NULL, 0, 0, 0, 0, 0),
(2, 20, 46, '2017-11-27 10:59:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(3, 20, 46, '2017-11-27 11:01:05', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
(4, 20, 46, '2017-11-28 14:25:14', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 0),
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
(156, 20, 46, '2017-12-22 00:57:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(157, 20, 46, '2018-01-20 13:40:15', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(158, 20, 46, '2018-01-20 13:43:15', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 1, 0, 0, 0),
(159, 20, 46, '2018-01-20 14:29:44', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(160, 20, 46, '2018-01-20 14:35:17', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(161, 20, 46, '2018-01-20 14:37:11', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(162, 20, 46, '2018-01-20 14:41:05', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(163, 20, 46, '2018-01-20 21:15:41', 'en', 'Feedback', NULL, 1, 0, 0, NULL, 0, 0, 0, 0, 1),
(164, 20, 46, '2018-01-20 21:16:15', 'en', 'Feedback', NULL, 1, 0, 0, NULL, 0, 0, 0, 0, 1),
(165, 20, 46, '2018-01-20 21:16:51', 'en', 'Feedback', NULL, 1, 0, 0, NULL, 0, 0, 0, 0, 1),
(166, 20, 46, '2018-01-20 21:18:27', 'en', 'Feedback', NULL, 2, 0, 0, NULL, 0, 0, 0, 0, 1),
(167, 20, 46, '2018-01-20 21:18:54', 'en', 'Feedback', NULL, 2, 0, 0, NULL, 0, 0, 0, 0, 1),
(168, 20, 46, '2018-01-20 21:19:17', 'en', 'Feedback', '2018-01-21 23:10:06', 2, 0, 0, NULL, 0, 0, 0, 0, 1),
(169, 20, 46, '2018-01-20 21:20:42', 'en', 'Feedback', '2018-01-21 23:43:13', 3, 0, 0, NULL, 0, 0, 0, 0, 1),
(170, 20, 46, '2018-01-20 21:21:05', 'en', 'Feedback', NULL, 3, 0, 0, NULL, 0, 0, 0, 0, 1),
(171, 20, 46, '2018-01-20 21:22:40', 'en', 'Feedback', NULL, 4, 0, 0, NULL, 0, 0, 0, 0, 1),
(172, 20, 46, '2018-01-20 21:23:00', 'en', 'Feedback', '2018-01-22 00:23:20', 4, 0, 0, NULL, 0, 0, 0, 0, 1),
(173, 20, 46, '2018-01-26 19:21:40', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(174, 20, 46, '2018-01-26 19:24:35', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(175, 20, 46, '2018-01-26 19:27:50', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(176, 20, 46, '2018-01-26 19:30:49', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(177, 20, 46, '2018-01-26 19:38:00', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(178, 20, 46, '2018-01-26 19:41:48', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(179, 20, 46, '2018-01-26 19:44:26', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(180, 20, 46, '2018-01-26 19:57:08', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(181, 20, 46, '2018-01-26 19:59:39', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(182, 20, 46, '2018-01-26 20:12:54', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(183, 20, 46, '2018-01-26 21:04:06', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(184, 20, 46, '2018-01-26 21:07:58', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(185, 20, 46, '2018-01-26 21:10:46', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(186, 20, 46, '2018-01-26 21:18:52', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(187, 20, 46, '2018-01-26 21:24:45', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(188, 20, 46, '2018-01-26 21:35:33', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(189, 20, 46, '2018-01-26 21:42:41', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(190, 20, 46, '2018-01-26 22:15:04', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(191, 20, 46, '2018-01-27 00:36:19', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(192, 20, 46, '2018-01-27 00:58:11', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(193, 20, 46, '2018-01-27 01:00:31', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(194, 20, 46, '2018-01-27 01:01:04', 'en', 'Feedback', NULL, 99999999, 0, 0, NULL, 0, 0, 0, 0, 1),
(195, 20, 46, '2018-01-27 08:57:05', 'en', 'Feedback', NULL, 5, 0, 0, NULL, 0, 0, 0, 0, 1),
(196, 20, 46, '2018-01-27 09:02:09', 'en', 'Feedback', NULL, 5, 0, 0, NULL, 0, 0, 0, 0, 1),
(197, 20, 46, '2018-01-27 09:03:21', 'en', 'Feedback', NULL, 5, 0, 0, NULL, 0, 0, 0, 0, 1),
(198, 20, 46, '2018-01-27 09:03:59', 'en', 'Feedback', NULL, 5, 0, 0, NULL, 0, 0, 0, 0, 1),
(201, 20, 46, '2018-01-27 09:12:32', 'en', 'Feedback', NULL, 6, 0, 0, NULL, 0, 0, 0, 0, 1),
(202, 20, 46, '2018-01-27 09:13:07', 'en', 'Feedback', NULL, 6, 0, 0, NULL, 0, 0, 0, 0, 1),
(203, 20, 46, '2018-01-27 09:16:35', 'en', 'Feedback', NULL, 7, 0, 0, NULL, 0, 0, 0, 0, 1),
(204, 20, 46, '2018-01-27 09:17:15', 'en', 'Feedback', NULL, 7, 0, 0, NULL, 0, 0, 0, 0, 1),
(205, 20, 46, '2018-01-27 09:25:26', 'en', 'Feedback', NULL, 8, 0, 0, NULL, 0, 0, 0, 0, 1),
(206, 20, 46, '2018-01-27 09:26:03', 'en', 'Feedback', NULL, 8, 0, 0, NULL, 0, 0, 0, 0, 1),
(207, 20, 46, '2018-01-27 09:38:39', 'en', 'Feedback', NULL, 9, 0, 0, NULL, 0, 0, 0, 0, 1),
(208, 20, 46, '2018-01-27 09:48:00', 'en', 'Feedback', NULL, 10, 0, 0, NULL, 0, 0, 0, 0, 1),
(209, 20, 46, '2018-01-27 09:57:00', 'en', 'Feedback', NULL, 11, 0, 0, NULL, 0, 0, 0, 0, 1),
(210, 20, 46, '2018-01-27 09:59:01', 'en', 'Feedback', NULL, 11, 0, 0, NULL, 0, 0, 0, 0, 1),
(211, 20, 46, '2018-01-27 10:01:25', 'en', 'Feedback', NULL, 12, 0, 0, NULL, 0, 0, 0, 0, 1),
(212, 20, 46, '2018-01-27 10:11:50', 'en', 'Feedback', NULL, 13, 0, 0, NULL, 0, 0, 0, 0, 1),
(213, 20, 46, '2018-01-27 10:12:57', 'en', 'Feedback', NULL, 13, 0, 0, NULL, 0, 0, 0, 0, 1),
(214, 20, 46, '2018-01-27 10:36:10', 'en', 'Feedback', NULL, 14, 0, 0, NULL, 0, 0, 0, 0, 1),
(215, 20, 46, '2018-01-27 10:42:29', 'en', 'Feedback', NULL, 15, 0, 0, NULL, 0, 0, 0, 0, 1),
(216, 20, 46, '2018-01-27 10:54:41', 'en', 'Feedback', NULL, 16, 0, 0, NULL, 0, 0, 0, 0, 1),
(217, 20, 46, '2018-01-27 10:55:24', 'en', 'Feedback', NULL, 16, 0, 0, NULL, 0, 0, 0, 0, 1),
(218, 20, 46, '2018-01-27 11:01:07', 'en', 'Feedback', NULL, 17, 0, 0, NULL, 0, 0, 0, 0, 1),
(219, 20, 46, '2018-01-27 11:01:49', 'en', 'Feedback', NULL, 17, 0, 0, NULL, 0, 0, 0, 0, 1),
(220, 20, 46, '2018-01-27 11:05:29', 'en', 'Feedback', NULL, 18, 0, 0, NULL, 0, 0, 0, 0, 1),
(221, 20, 46, '2018-01-27 11:05:55', 'en', 'Feedback', NULL, 18, 0, 0, NULL, 0, 0, 0, 0, 1),
(222, 20, 46, '2018-01-27 15:04:09', 'en', 'Feedback', NULL, 24, 0, 0, NULL, 0, 0, 0, 0, 1),
(223, 20, 46, '2018-01-27 15:05:09', 'en', 'Feedback', NULL, 24, 0, 0, NULL, 0, 0, 0, 0, 1),
(224, 20, 46, '2018-01-27 15:05:42', 'en', 'Feedback', NULL, 24, 0, 0, NULL, 0, 0, 0, 0, 1),
(225, 20, 46, '2018-01-27 15:06:09', 'en', 'Feedback', '2018-02-08 13:41:04', 24, 0, 0, NULL, 0, 0, 0, 0, 1),
(226, 20, 46, '2018-01-27 22:37:36', 'en', 'Feedback', '2018-02-08 13:40:59', 2, 0, 0, NULL, 1, 0, 0, 1, 1),
(227, 20, 46, '2018-01-27 22:39:06', 'en', 'Feedback', '2018-02-08 13:40:55', 99999999, 0, 0, NULL, 1, 0, 0, 1, 1),
(228, 20, 46, '2018-01-27 22:39:39', 'en', 'Feedback', '2018-02-08 13:40:51', 2, 0, 0, NULL, 1, 0, 0, 1, 1),
(229, 20, 46, '2018-01-30 14:42:22', 'en', 'Feedback', '2018-02-08 13:40:46', 2, 0, 0, NULL, 1, 0, 0, 1, 0),
(230, 20, 46, '2018-02-04 11:37:12', 'en', 'Feedback', '2018-02-08 13:40:27', 2, 0, 0, NULL, 0, 0, 0, 1, 1);

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

--
-- Daten für Tabelle `feedback_chat_information`
--

INSERT INTO `feedback_chat_information` (`feedback_chat_id`, `chat_date`, `chat_text`, `initated_by_user`, `feedback_id`, `user_id`) VALUES
(1, '2018-02-01 17:56:32', 'test test', b'0', 1, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `feedback_company`
--

CREATE TABLE `feedback_company` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `promote` bit(1) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `feedback_company`
--

INSERT INTO `feedback_company` (`id`, `created_at`, `promote`, `status`, `text`, `updated_at`) VALUES
(1, '2018-02-01 19:53:53', NULL, 'upcoming', 'hey ho', NULL),
(2, '2018-02-04 15:44:06', b'1', 'upcoming', 'hey ho', NULL),
(3, NULL, b'0', 'upcoming', 'hey ho', NULL),
(4, NULL, b'1', 'upcoming', 'hey ho', NULL),
(5, NULL, b'1', 'upcoming', 'hey ho', NULL),
(6, NULL, b'0', 'upcoming', 'hey ho', NULL),
(7, '2018-02-04 15:47:00', b'1', 'planned', 'crazy feature planned', NULL);

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

--
-- Daten für Tabelle `feedback_settings`
--

INSERT INTO `feedback_settings` (`id`, `feedback_query`, `feedback_query_channel`, `global_feedback_setting`, `status_updates`, `status_updates_contact_channel`, `feedback_id`, `user_id`) VALUES
(1, b'0', '', b'0', b'1', 'Email', 182, 99999999),
(2, b'1', 'Email', b'1', b'0', '', 191, 99999999),
(3, b'1', 'Email', b'1', b'1', 'Feedback-To-Feedback Central', 193, 99999999),
(4, b'0', '', b'1', b'1', 'Feedback-To-Feedback Central', 196, 5),
(5, b'0', '', b'1', b'0', '', 197, 5),
(6, b'0', '', b'1', b'1', 'Feedback-To-Feedback Central', 201, 6),
(7, b'0', '', b'1', b'1', 'Email', 203, 7),
(8, b'0', '', b'1', b'0', '', 205, 8),
(17, b'0', '', b'1', b'0', '', 212, 13),
(18, b'0', '', b'1', b'0', '', 216, 16),
(19, b'0', '', b'1', b'0', '', 218, 17),
(20, b'0', '', b'1', b'0', '', 220, 18),
(28, b'0', '', b'0', b'1', 'Feedback-To-Feedback Central', 230, 99999999),
(27, b'0', '', b'1', b'0', '', 160, 99999999),
(24, b'1', 'Email', b'0', b'1', 'Feedback-To-Feedback Central', 226, 99999999),
(25, b'0', '', b'1', b'0', '', 227, 99999999),
(26, b'1', 'Feedback-To-Feedback Central', b'0', b'1', 'Email', 229, 99999999);

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

--
-- Daten für Tabelle `feedback_status`
--

INSERT INTO `feedback_status` (`id`, `date`, `status`, `feedback_id`) VALUES
(1, '2018-02-07 15:18:15', 'declined', 229),
(2, '2018-02-04 11:37:12', 'declined', 230),
(3, '2018-02-07 15:18:47', 'reject this bullshit', 10);

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

--
-- Daten für Tabelle `feedback_viewed`
--

INSERT INTO `feedback_viewed` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES
(1, '2018-02-02 17:40:14', 1, 1),
(2, '2018-02-08 13:43:09', 99999999, 226),
(3, '2018-02-08 13:43:19', 99999999, 228),
(4, '2018-02-08 13:43:23', 99999999, 230);

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
(1, 90, 4, 'Rate your experience with the feature you just used.', 173),
(2, 90, 4, 'Rate your experience with the feature you just used.', 174),
(3, 90, 5, 'Rate your experience with the feature you just used.', 175),
(4, 90, 4, 'Rate your experience with the feature you just used.', 176),
(5, 90, 5, 'Rate your experience with the feature you just used.', 177),
(6, 90, 4, 'Rate your experience with the feature you just used.', 178),
(7, 90, 5, 'Rate your experience with the feature you just used.', 179),
(8, 90, 5, 'Rate your experience with the feature you just used.', 180),
(9, 90, 5, 'Rate your experience with the feature you just used.', 181),
(10, 90, 5, 'Rate your experience with the feature you just used.', 182),
(11, 90, 5, 'Rate your experience with the feature you just used.', 183),
(12, 90, 5, 'Rate your experience with the feature you just used.', 184),
(13, 90, 3, 'Rate your experience with the feature you just used.', 185),
(14, 90, 4, 'Rate your experience with the feature you just used.', 186),
(15, 90, 3, 'Rate your experience with the feature you just used.', 187),
(16, 90, 4, 'Rate your experience with the feature you just used.', 188),
(17, 90, 3, 'Rate your experience with the feature you just used.', 189),
(18, 90, 5, 'Rate your experience with the feature you just used.', 190),
(19, 90, 5, 'Rate your experience with the feature you just used.', 191),
(20, 90, 5, 'Rate your experience with the feature you just used.', 192),
(21, 90, 5, 'Rate your experience with the feature you just used.', 193),
(22, 90, 5, 'Rate your experience with the feature you just used.', 194),
(23, 90, 5, 'Rate your experience with the feature you just used.', 195),
(24, 90, 4, 'Rate your experience with the feature you just used.', 196),
(25, 90, 3, 'Rate your experience with the feature you just used.', 197),
(26, 90, 5, 'Rate your experience with the feature you just used.', 198),
(27, 90, 4, 'Rate your experience with the feature you just used.', 199),
(28, 90, 4, 'Rate your experience with the feature you just used.', 200),
(29, 90, 5, 'Rate your experience with the feature you just used.', 201),
(30, 90, 4, 'Rate your experience with the feature you just used.', 202),
(31, 90, 4, 'Rate your experience with the feature you just used.', 203),
(32, 90, 4, 'Rate your experience with the feature you just used.', 204),
(33, 90, 4, 'Rate your experience with the feature you just used.', 205),
(34, 90, 3, 'Rate your experience with the feature you just used.', 206),
(35, 90, 3, 'Rate your experience with the feature you just used.', 207),
(36, 90, 5, 'Rate your experience with the feature you just used.', 208),
(37, 90, 5, 'Rate your experience with the feature you just used.', 209),
(38, 90, 4, 'Rate your experience with the feature you just used.', 210),
(39, 90, 4, 'Rate your experience with the feature you just used.', 211),
(40, 90, 4, 'Rate your experience with the feature you just used.', 212),
(41, 90, 3, 'Rate your experience with the feature you just used.', 213),
(42, 90, 4, 'Rate your experience with the feature you just used.', 214),
(43, 90, 4, 'Rate your experience with the feature you just used.', 215),
(44, 90, 4, 'Rate your experience with the feature you just used.', 216),
(45, 90, 5, 'Rate your experience with the feature you just used.', 217),
(46, 90, 3, 'Rate your experience with the feature you just used.', 218),
(47, 90, 4, 'Rate your experience with the feature you just used.', 219),
(48, 90, 3, 'Rate your experience with the feature you just used.', 220),
(49, 90, 5, 'Rate your experience with the feature you just used.', 221),
(50, 90, 4, 'Rate your experience with the feature you just used.', 222),
(51, 90, 3, 'Rate your experience with the feature you just used.', 223),
(52, 90, 2, 'Rate your experience with the feature you just used.', 224),
(53, 90, 3, 'Rate your experience with the feature you just used.', 225),
(54, 90, 4, 'Bewerten Sie das Feature, das Sie gerade benutzt haben.', 226),
(55, 90, 4, 'Bewerten Sie das Feature, das Sie gerade benutzt haben.', 227),
(56, 90, 3, 'Bewerten Sie das Feature, das Sie gerade benutzt haben.', 228),
(57, 90, 4, 'Rate your experience with the feature you just used.', 229),
(58, 90, 5, 'Bewerten Sie das Feature, das Sie gerade benutzt haben.', 230);

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

--
-- Daten für Tabelle `text_feedback`
--

INSERT INTO `text_feedback` (`id`, `mechanism_id`, `text`, `feedback_id`) VALUES
(1, 88, 'Text for Feedback 163', 163),
(2, 88, 'Text for Feedback 164 - PUBLISHED', 164),
(3, 88, 'Text for Feedback 165', 165),
(4, 88, 'Text for Feedback 166 - PUBLISHED', 166),
(5, 88, 'Text for Feedback 167', 167),
(6, 88, 'Text for Feedback 168 - PUBLISHED', 168),
(7, 88, 'Text for Feedback 169', 169),
(8, 88, 'Text for Feedback 170 - PUBLISHED', 170),
(9, 88, 'Text for Feedback 171', 171),
(10, 88, 'Text for Feedback 172 - PUBLISHED', 172),
(11, 88, 'test web_library', 173),
(12, 88, 'test web library', 174),
(13, 88, 'test web library', 175),
(14, 88, 'test web library', 176),
(15, 88, 'test web librrary', 177),
(16, 88, 'test wweb library', 178),
(17, 88, 'test web library', 179),
(18, 88, 'test web library', 180),
(19, 88, 'test web library', 181),
(20, 88, 'test web library', 182),
(21, 88, 'test web library', 183),
(22, 88, 'test web library', 184),
(23, 88, 'test web library\n', 185),
(24, 88, 'test web library', 186),
(25, 88, 'test web library', 187),
(26, 88, 'test web library', 188),
(27, 88, 'test web library', 189),
(28, 88, 'test web library', 190),
(29, 88, 'test web library', 191),
(30, 88, 'test web library retrieve previous settings', 192),
(31, 88, 'test web library', 193),
(32, 88, 'test web library', 194),
(33, 88, 'test web library', 195),
(34, 88, 'test web library', 196),
(35, 88, 'test web library', 197),
(36, 88, 'asdfwefe', 198),
(37, 88, 'asfefefEF', 199),
(38, 88, 'asfelfjaewifkeawjfwef', 200),
(39, 88, 'asdfefqwefef', 201),
(40, 88, 'asdwefqwefewf', 202),
(41, 88, 'test web library user 7', 203),
(42, 88, 'test web library user 7 reload settings false', 204),
(43, 88, 'test web library user 8', 205),
(44, 88, 'test web library user 8', 206),
(45, 88, 'test web library user 9', 207),
(46, 88, 'test user 10', 208),
(47, 88, 'test user 11', 209),
(48, 88, 'test user 11', 210),
(49, 88, 'test user 12', 211),
(50, 88, 'test user 13', 212),
(51, 88, 'test user 13', 213),
(52, 88, 'test user 14', 214),
(53, 88, 'test user 15', 215),
(54, 88, 'test user 16', 216),
(55, 88, 'test user 16', 217),
(56, 88, 'test user 17', 218),
(57, 88, 'test user 17', 219),
(58, 88, 'test user 18', 220),
(59, 88, 'test user 18', 221),
(60, 88, 'test user 19', 222),
(61, 88, 'test user 19', 223),
(62, 88, 'test user 19', 224),
(63, 88, 'test user 19', 225),
(64, 88, 'test test test', 226),
(65, 88, 'test test test test', 227),
(66, 88, 'asflwekjfaweif afkawef', 228),
(67, 88, 'test feedback status ', 229),
(68, 88, 'asdjfaejfei', 230);

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
-- Daten für Tabelle `userfbdislike`
--

INSERT INTO `userfbdislike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES
(1, '2018-02-02 17:40:44', 1, 1);

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
-- Daten für Tabelle `userfblike`
--

INSERT INTO `userfblike` (`id`, `created_at`, `user_id`, `feedback_id`) VALUES
(1, '2018-01-24 23:00:07', 1, 158);

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
  ADD KEY `FKt7aa1dhhfvrlyukmtktgdxraf` (`comment_id`),
  ADD KEY `FKamfb2bvnx3tj8stougb7hsuyq` (`user_id`);

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
  ADD PRIMARY KEY (`id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `api_user_api_user_role`
--
ALTER TABLE `api_user_api_user_role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT für Tabelle `comment_feedback`
--
ALTER TABLE `comment_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT für Tabelle `comment_viewed`
--
ALTER TABLE `comment_viewed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `context_information`
--
ALTER TABLE `context_information`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT für Tabelle `email_unsubscribed`
--
ALTER TABLE `email_unsubscribed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT für Tabelle `end_user`
--
ALTER TABLE `end_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100000000;
--
-- AUTO_INCREMENT für Tabelle `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=231;
--
-- AUTO_INCREMENT für Tabelle `feedback_chat_information`
--
ALTER TABLE `feedback_chat_information`
  MODIFY `feedback_chat_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `feedback_company`
--
ALTER TABLE `feedback_company`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT für Tabelle `feedback_settings`
--
ALTER TABLE `feedback_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
--
-- AUTO_INCREMENT für Tabelle `feedback_status`
--
ALTER TABLE `feedback_status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `feedback_viewed`
--
ALTER TABLE `feedback_viewed`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `file_feedback`
--
ALTER TABLE `file_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `rating_feedback`
--
ALTER TABLE `rating_feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;
--
-- AUTO_INCREMENT für Tabelle `userfbdislike`
--
ALTER TABLE `userfbdislike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `userfblike`
--
ALTER TABLE `userfblike`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
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
