DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id VARCHAR(100),
    name VARCHAR(50),
    token CHAR(36),
    gmt_create BIGINT,
    gmt_modified BIGINT,
    bio VARCHAR(256) NULL,
    avatar_url VARCHAR(256) NULL
);

DROP TABLE IF EXISTS question;
CREATE TABLE question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50),
    description TEXT,
    tag TEXT,
    gmt_create BIGINT,
    gmt_modified BIGINT,
    creator_id BIGINT,
    comment_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL,
    type INT NOT NULL,
    content VARCHAR(1024),
    comment_count INT DEFAULT 0,
    commentator BIGINT NOT NULL,
    gmt_create BIGINT NOT NULL,
    gmt_modified BIGINT NOT NULL,
    like_count BIGINT DEFAULT 0
);

DROP TABLE IF EXISTS notification;
CREATE TABLE notification(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    outer_id BIGINT NOT NULL,
    notifier BIGINT NOT NULL,
    receiver BIGINT NOT NULL,
    type INT NOT NULL,
    content VARCHAR(256),
    gmt_create BIGINT NOT NULL,
    status INT NOT NULL
);
CREATE INDEX receiver ON notification(receiver);