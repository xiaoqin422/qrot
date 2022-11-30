CREATE TABLE `user`
(
    `id`          bigint                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`    varchar(20) DEFAULT ''       NOT NULL COMMENT '用户名',
    `password`    varchar(20) DEFAULT '123456' NOT NULL COMMENT '密码',
    `sex`         tinyint     DEFAULT 0 COMMENT '性别 0未知 1女 2男',
    `name`        varchar(10) DEFAULT '' COMMENT '姓名',
    `q_code`      varchar(10) DEFAULT '' COMMENT 'QQ号码',
    `email`       varchar(20) DEFAULT '' COMMENT '邮箱',
    `phone`       varchar(11) DEFAULT '' COMMENT '电话',
    `roles`       JSON COMMENT '角色集合',
    `is_disable`  tinyint     DEFAULT 0 COMMENT '是否被禁用 1禁用',
    `create_time` timestamp   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint     DEFAULT 0 COMMENT '是否删除 1删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_index` (`username`) USING BTREE,
    UNIQUE KEY `qCode_index` (`q_code`) USING BTREE
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '用户表';

CREATE TABLE `role`
(
    `id`                  bigint                                                             NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`                varchar(20)                                             DEFAULT '' NOT NULL COMMENT '角色名称',
    `permissions`         json COMMENT '权限集合',
    `desc`                varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
    `default_permissions` json                                                    DEFAULT NULL COMMENT '系统角色内置权限',
    `systemic`            tinyint(1)                                              DEFAULT '0' COMMENT '是否为系统内置角色',
    PRIMARY KEY (`id`)
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '角色表';

CREATE TABLE `permission`
(
    `id`        bigint                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code`      varchar(30) DEFAULT '' NOT NULL COMMENT '权限CODE',
    `name`      varchar(20) DEFAULT '' NOT NULL COMMENT '权限名称',
    `module_id` bigint                 NOT NULL COMMENT '模块ID',
    PRIMARY KEY (`id`)
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '权限表';

CREATE TABLE `module`
(
    `id`   bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code` varchar(30) NOT NULL COMMENT '模块编号',
    `name` varchar(20) NOT NULL COMMENT '模块名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='模块资源表';

CREATE TABLE `task_health`
(
    `id`          bigint                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id`     varchar(30)            NOT NULL COMMENT '任务ID',
    `u_id`        bigint                 NOT NULL COMMENT '用户ID',
    `pid`         varchar(20)            NOT NULL COMMENT '身份证号',
    `address`     varchar(50) DEFAULT '' NOT NULL COMMENT '打卡地址',
    `temperature` DOUBLE      DEFAULT 36.5 COMMENT '体温',
    `is_random`   tinyint     DEFAULT 0 COMMENT '体温是否随机 1随机',
    `is_timing`   tinyint     DEFAULT 0 COMMENT '是否定时 1定时',
    PRIMARY KEY (`id`),
    UNIQUE KEY `pid_index` (`pid`) USING BTREE
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '体温打卡表';

CREATE TABLE `task_listen`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id`      varchar(30) NOT NULL COMMENT '任务ID',
    `u_id`         bigint      NOT NULL COMMENT '用户ID（任务负责人）',
    `account_code` varchar(20) NOT NULL COMMENT '监听账号',
    `password`     varchar(20) NOT NULL COMMENT '监听密码',
    `listen_url`   varchar(50) DEFAULT '' COMMENT '监听URL',
    `listen_token` varchar(50) DEFAULT '' COMMENT '用户token',
    `is_timing`    tinyint     DEFAULT 0 COMMENT '是否定时 1定时',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uid_code_index` (`u_id`, `account_code`) USING BTREE
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '监听任务表';

CREATE TABLE `task_health_listen`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `listen_id`    bigint      NOT NULL COMMENT '监听任务ID',
    `sno`          varchar(15) NOT NULL COMMENT '学号',
    `account_code` varchar(10) NOT NULL COMMENT 'QQ号码',
    `name`         varchar(20) NOT NULL COMMENT '学生姓名',
    PRIMARY KEY (`id`)
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '健康打卡提醒表';

CREATE TABLE `group`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_code` varchar(20) NOT NULL COMMENT '群聊账号',
    `is_at`      tinyint DEFAULT 0 COMMENT '通知是否At',
    `task_list`  JSON COMMENT '任务列表 存放taskID',
    PRIMARY KEY (`id`)
) ENGINE = innoDB
  DEFAULT CHARSET = utf8 COMMENT = '群聊表';