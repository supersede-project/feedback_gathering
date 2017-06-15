BEGIN;
INSERT INTO `api_user` VALUES ('1', 'super_admin', '$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy');
COMMIT;

BEGIN;
INSERT INTO `api_user_api_user_role` VALUES ('1', '2', '1');
COMMIT;