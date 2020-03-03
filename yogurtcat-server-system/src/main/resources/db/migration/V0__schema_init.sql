DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint NOT NULL,
  `component` varchar(255) DEFAULT NULL,
  `create_time` bigint DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `pid` bigint DEFAULT NULL,
  `sort` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `message_config`;
CREATE TABLE `message_config` (
  `id` bigint NOT NULL,
  `access_key_id` varchar(255) DEFAULT NULL,
  `access_key_secret` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL,
  `arg` blob,
  `create_time` datetime(6) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `log_code` varchar(255) DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` bigint NOT NULL,
  `api_secret` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `cert_name` varchar(255) DEFAULT NULL,
  `cert_stream` blob,
  `create_date` datetime(6) DEFAULT NULL,
  `mch_id` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint NOT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pid` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `rabbit_mq_config`;
CREATE TABLE `rabbit_mq_config` (
  `id` bigint NOT NULL,
  `enabled` bit(1) NOT NULL,
  `host` varchar(255) DEFAULT NULL,
  `interval` int DEFAULT NULL,
  `last_modify_date` datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `port` int DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint NOT NULL,
  `business_type` int DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` bigint DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `storage_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `roles_menus`;
CREATE TABLE `roles_menus` (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `roles_permissions`;
CREATE TABLE `roles_permissions` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `roles_users`;
CREATE TABLE `roles_users` (
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage` (
  `id` bigint NOT NULL,
  `access_key_id` varchar(255) DEFAULT NULL,
  `access_key_secret` varchar(255) DEFAULT NULL,
  `bucketname` varchar(255) DEFAULT NULL,
  `business_type` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `endpoint` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_time` datetime(6) DEFAULT NULL,
  `status` int NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
