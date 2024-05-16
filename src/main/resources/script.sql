CREATE DATABASE IF NOT EXISTS `mini_proj_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

drop TABLE `member`;

CREATE TABLE `member` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`pwd` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`oauth` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`roles` VARCHAR(200) NOT NULL DEFAULT 'USER' COLLATE 'utf8mb4_unicode_ci',
	`accountExpired` CHAR(1) NOT NULL DEFAULT 'N' COLLATE 'utf8mb4_unicode_ci',
	`accountLocked` CHAR(1) NOT NULL DEFAULT 'N' COLLATE 'utf8mb4_unicode_ci',
	`loginCount` INT(11) NOT NULL DEFAULT '0',
	`last_login_time` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
	`register_time` TIMESTAMP NOT NULL DEFAULT current_timestamp(),
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `email` (`email`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;





CREATE TABLE IF NOT EXISTS `board` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `title` varchar(200) NOT NULL COMMENT '제목',
  `content` text NOT NULL COMMENT '내용',
  `read_cnt` int(11) NOT NULL DEFAULT 0 COMMENT '조회수',
  `register_id` VARCHAR(100) NOT NULL COMMENT '작성자',
  `register_time` DATETIME NULL DEFAULT NULL COMMENT '작성일',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`id`)
)
 COLLATE='utf8mb4_unicode_ci'
 ENGINE=InnoDB
;