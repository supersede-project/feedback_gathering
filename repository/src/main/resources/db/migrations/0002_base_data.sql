SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

BEGIN;
INSERT INTO `api_user` VALUES ('1', 'admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy');
COMMIT;

BEGIN;
INSERT INTO `api_user_api_user_role` VALUES ('1', '1', '1');
COMMIT;

BEGIN;
INSERT INTO `api_user_permission` VALUES ('1', '1', '1', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;