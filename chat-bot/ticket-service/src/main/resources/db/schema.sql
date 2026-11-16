-- 创建数据库
DROP DATABASE IF EXISTS bit_chat_bot;
CREATE DATABASE IF NOT EXISTS bit_chat_bot DEFAULT CHARACTER SET utf8mb4;

-- 使用数据库
USE bit_chat_bot;

-- 创建工单表
DROP TABLE IF EXISTS `ticket_info`;
CREATE TABLE `ticket_info`
(
    `id`              int          NOT NULL AUTO_INCREMENT,
    `ticket_id`       varchar(128) NOT NULL COMMENT '工单ID',
    `title`           varchar(128) NOT NULL COMMENT '工单标题',
    `description`     varchar(255) NOT NULL COMMENT '工单详细描述',
    `related_chat_id` varchar(128) DEFAULT NULL COMMENT '关联会话ID',
    `status`          tinyint      DEFAULT '1' COMMENT '工单状态 1-新建 2-处理中 3-已解决 4-已关闭',
    `creator`         varchar(128) DEFAULT 'system' COMMENT '创建人',
    `assignee`        varchar(128) DEFAULT NULL COMMENT '分配给',
    `created_time`    datetime     DEFAULT CURRENT_TIMESTAMP,
    `updated_time`    datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `closed_time`     datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
)