/*
 Navicat Premium Data Transfer

 Source Server         : conan-test
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : 192.168.32.16:3306
 Source Schema         : conandb

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 21/06/2019 17:16:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api
-- ----------------------------
DROP TABLE IF EXISTS `api`;
CREATE TABLE `api` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `domain_id` int(11) NOT NULL COMMENT '所属域名id',
  `name` varchar(2048) NOT NULL COMMENT '接口名',
  `method` int(11) NOT NULL COMMENT 'http方法',
  `is_online` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否已上线(0-已下线，1-已上线)',
  `is_read` tinyint(1) DEFAULT NULL COMMENT '是否读接口(0-否，1-是)',
  `is_enable` tinyint(1) DEFAULT NULL COMMENT '是否启用(0-禁用，1-启用)',
  `recordable_count` int(11) DEFAULT '0' COMMENT '可录制请求条数',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL DEFAULT '0' COMMENT '更新人',
  `department_id` int(11) NOT NULL,
  `product_line_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `domain_id` (`domain_id`) USING BTREE,
  KEY `fk_api` (`create_by`),
  KEY `fk_api_1` (`update_by`),
  CONSTRAINT `fk_api` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_api_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口信息表';

-- ----------------------------
-- Table structure for api_diff_scheme_relation
-- ----------------------------
DROP TABLE IF EXISTS `api_diff_scheme_relation`;
CREATE TABLE `api_diff_scheme_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_id` int(11) NOT NULL COMMENT '接口id',
  `diff_scheme_id` int(11) NOT NULL COMMENT '比对模式id',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `fk_interface_diff_scheme_relation` (`api_id`),
  KEY `fk_interface_diff_scheme_relation_1` (`diff_scheme_id`),
  KEY `fk_api_diff_scheme_relation` (`create_by`),
  KEY `fk_api_diff_scheme_relation_1` (`update_by`),
  CONSTRAINT `fk_api_diff_scheme_relation` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_api_diff_scheme_relation_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_interface_diff_scheme_relation_1` FOREIGN KEY (`diff_scheme_id`) REFERENCES `diff_scheme` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口比对模式关系表';

-- ----------------------------
-- Table structure for bulletin
-- ----------------------------
DROP TABLE IF EXISTS `bulletin`;
CREATE TABLE `bulletin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `link` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '超链接',
  `msg` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '公告信息',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL COMMENT '更新时间',
  `update_by` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '部门名称',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT '0' COMMENT '更新人',
  `update_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) USING BTREE,
  KEY `fk_department` (`create_by`),
  KEY `fk_department_1` (`update_by`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='业务线信息表';

-- ----------------------------
-- Table structure for diff
-- ----------------------------
DROP TABLE IF EXISTS `diff`;
CREATE TABLE `diff` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `task_execution_id` int(11) NOT NULL COMMENT '任务执行id',
  `record_id` int(11) DEFAULT NULL COMMENT '录制id',
  `replay_id` int(11) DEFAULT NULL COMMENT '回放id',
  `operate_by` int(11) DEFAULT NULL COMMENT '操作人',
  `start_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `success_rate` varchar(255) DEFAULT NULL COMMENT '比对成功率',
  PRIMARY KEY (`id`),
  KEY `task_execution_id` (`task_execution_id`) USING BTREE,
  KEY `fk_compare_2` (`record_id`),
  KEY `fk_compare_3` (`replay_id`),
  KEY `fk_compare_1` (`operate_by`),
  CONSTRAINT `fk_compare` FOREIGN KEY (`task_execution_id`) REFERENCES `task_execution` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_compare_1` FOREIGN KEY (`operate_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_compare_2` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_compare_3` FOREIGN KEY (`replay_id`) REFERENCES `replay` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='比对记录表';

-- ----------------------------
-- Table structure for diff_detail
-- ----------------------------
DROP TABLE IF EXISTS `diff_detail`;
CREATE TABLE `diff_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_count` int(11) DEFAULT NULL,
  `api_id` int(11) NOT NULL,
  `diff_id` int(11) NOT NULL,
  `expect_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for diff_result
-- ----------------------------
DROP TABLE IF EXISTS `diff_result`;
CREATE TABLE `diff_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `diff_id` int(11) DEFAULT NULL,
  `diff_result_id` int(11) DEFAULT NULL,
  `record_result_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for diff_scheme
-- ----------------------------
DROP TABLE IF EXISTS `diff_scheme`;
CREATE TABLE `diff_scheme` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` int(11) DEFAULT '0' COMMENT '模式类型(0-JSON比对，1-字段匹配)',
  `description` text NOT NULL COMMENT '模式描述',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为缺省比对模式(0-否，1-是)',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COMMENT='比对模式信息表';

-- ----------------------------
-- Table structure for domain
-- ----------------------------
DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '域名',
  `online` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否已上线(0-否，1-是)',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) USING BTREE,
  KEY `fk_domain_2` (`create_by`),
  KEY `fk_domain_3` (`update_by`),
  CONSTRAINT `fk_domain_2` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_domain_3` FOREIGN KEY (`update_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='域名信息表';

-- ----------------------------
-- Table structure for product_line
-- ----------------------------
DROP TABLE IF EXISTS `product_line`;
CREATE TABLE `product_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '业务组名称',
  `department_id` int(11) NOT NULL COMMENT '所属部门id',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) USING BTREE,
  KEY `fk_business_group` (`department_id`),
  KEY `fk_product_line` (`create_by`),
  KEY `fk_product_line_1` (`update_by`),
  CONSTRAINT `fk_business_group` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='业务组信息表';

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `task_execution_id` int(11) NOT NULL COMMENT '任务执行id',
  `api_count` int(11) NOT NULL COMMENT '录制接口数',
  `operate_by` int(11) DEFAULT NULL COMMENT '操作人',
  `start_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `success_rate` varchar(255) DEFAULT NULL COMMENT '录制成功率',
  PRIMARY KEY (`id`),
  KEY `task_execution_id` (`task_execution_id`) USING BTREE,
  KEY `operate_by` (`operate_by`) USING BTREE,
  CONSTRAINT `fk_transcribe` FOREIGN KEY (`task_execution_id`) REFERENCES `task_execution` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transcribe_1` FOREIGN KEY (`operate_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='录制记录表';

-- ----------------------------
-- Table structure for record_detail
-- ----------------------------
DROP TABLE IF EXISTS `record_detail`;
CREATE TABLE `record_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_count` int(11) DEFAULT NULL,
  `api_id` int(11) NOT NULL,
  `expect_count` int(11) NOT NULL,
  `record_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for record_result
-- ----------------------------
DROP TABLE IF EXISTS `record_result`;
CREATE TABLE `record_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) DEFAULT NULL,
  `record_id` int(11) DEFAULT NULL,
  `request_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for replay
-- ----------------------------
DROP TABLE IF EXISTS `replay`;
CREATE TABLE `replay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `task_execution_id` int(11) NOT NULL COMMENT '任务执行id',
  `record_id` int(11) DEFAULT NULL COMMENT '录制id',
  `operate_by` int(11) DEFAULT NULL COMMENT '操作人',
  `start_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_at` datetime DEFAULT NULL COMMENT '结束时间',
  `success_rate` varchar(256) DEFAULT NULL COMMENT '流量回放成功率',
  PRIMARY KEY (`id`),
  KEY `task_execution_id` (`task_execution_id`) USING BTREE,
  KEY `fk_playback_1` (`operate_by`),
  KEY `fk_playback_2` (`record_id`),
  CONSTRAINT `fk_playback` FOREIGN KEY (`task_execution_id`) REFERENCES `task_execution` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_playback_1` FOREIGN KEY (`operate_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_playback_2` FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='回放记录表';

-- ----------------------------
-- Table structure for replay_detail
-- ----------------------------
DROP TABLE IF EXISTS `replay_detail`;
CREATE TABLE `replay_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_count` int(11) DEFAULT NULL,
  `api_id` int(11) NOT NULL,
  `expect_count` int(11) NOT NULL,
  `replay_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for replay_result
-- ----------------------------
DROP TABLE IF EXISTS `replay_result`;
CREATE TABLE `replay_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_result_id` int(11) DEFAULT NULL,
  `replay_id` int(11) DEFAULT NULL,
  `response_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for request_scanner
-- ----------------------------
DROP TABLE IF EXISTS `request_scanner`;
CREATE TABLE `request_scanner` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `is_active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活',
  `is_busy` tinyint(1) NOT NULL COMMENT '是否正在扫描流量',
  `offset` int(11) NOT NULL DEFAULT '0' COMMENT 'ES中开始扫描的偏移',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '任务类型',
  `name` varchar(255) NOT NULL COMMENT '任务名',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0-可执行，1-录制中，2-回访中，3-比对中)',
  `department_id` int(11) NOT NULL COMMENT '所属部门id',
  `product_line_id` int(11) NOT NULL COMMENT '所属产品线id',
  `notify_list` text COMMENT '消息通知人员列表',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `create_by` (`create_by`) USING BTREE,
  KEY `department_id` (`department_id`,`product_line_id`) USING BTREE,
  KEY `fk_task_1` (`product_line_id`),
  KEY `fk_task_3` (`update_by`),
  CONSTRAINT `fk_task` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_1` FOREIGN KEY (`product_line_id`) REFERENCES `product_line` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_2` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_3` FOREIGN KEY (`update_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COMMENT='任务信息表';

-- ----------------------------
-- Table structure for task_api_relation
-- ----------------------------
DROP TABLE IF EXISTS `task_api_relation`;
CREATE TABLE `task_api_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL,
  `record_count` int(11) DEFAULT NULL,
  `record_from` int(11) DEFAULT NULL,
  `api_diff_scheme_relation_id` int(11) NOT NULL,
  `record_to` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `create_by` int(11) DEFAULT NULL,
  `update_at` datetime NOT NULL,
  `update_by` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for task_execution
-- ----------------------------
DROP TABLE IF EXISTS `task_execution`;
CREATE TABLE `task_execution` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `task_id` int(11) NOT NULL COMMENT '任务id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0-未开始，1-录制中，2-录制完成，3-录制失败，4-回放中，5-回放完成，6-回放失败)',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`) USING BTREE,
  KEY `create_by` (`create_by`) USING BTREE,
  KEY `fk_task_execution_1` (`update_by`),
  CONSTRAINT `fk_task_execute` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_task_execute_1` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_execution` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_execution_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='任务执行记录表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '姓名',
  `employee_id` int(11) NOT NULL COMMENT '员工id',
  `account` varchar(255) NOT NULL COMMENT '员工账号',
  `department_id` int(11) NOT NULL COMMENT '所属部门id',
  `product_line_id` int(11) NOT NULL COMMENT '所属产品线id',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`,`product_line_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
