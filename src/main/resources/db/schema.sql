DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `account_id` VARCHAR(100),
    `name` VARCHAR(50),
    `token` CHAR(36),
    `gmt_create` BIGINT,
    `gmt_modified` BIGINT,
    `bio` VARCHAR(256) NULL
);

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
    `id` INT,
    `title` VARCHAR(50),
    `description` TEXT,
    `tag` TEXT,
    `gmt_create` BIGINT,
    `gmt_modified` BIGINT,
    `creator_id` INT,
    `comment_count` INT DEFAULT 0,
    `view_count` INT DEFAULT 0,
    `like_count` INT DEFAULT 0
);