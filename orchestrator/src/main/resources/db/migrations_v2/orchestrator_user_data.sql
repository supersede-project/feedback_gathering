/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost
 Source Database       : supersede_orchestrator_spring

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : utf-8

 Date: 01/30/2018 15:38:06 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Records of `api_user`
-- ----------------------------
BEGIN;
INSERT INTO `api_user` VALUES ('1', 'admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy'), ('2', 'superadmin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy');
COMMIT;

-- ----------------------------
--  Records of `api_user_api_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `api_user_api_user_role` VALUES ('1', '1', '1'), ('2', '2', '2');
COMMIT;


SET FOREIGN_KEY_CHECKS = 1;
