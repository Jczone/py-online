/*
 Navicat Premium Data Transfer

 Source Server         : jczone
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : py_online

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 20/07/2023 16:03:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for po_admin
-- ----------------------------
DROP TABLE IF EXISTS `po_admin`;
CREATE TABLE `po_admin`  (
  `admin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员工号',
  `password` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `telephone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号码',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_admin
-- ----------------------------
INSERT INTO `po_admin` VALUES ('admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', '15934670002');
INSERT INTO `po_admin` VALUES ('root', '7c4a8d09ca3762af61e59520943dc26494f8941b', '19834530001');

-- ----------------------------
-- Table structure for po_class_group
-- ----------------------------
DROP TABLE IF EXISTS `po_class_group`;
CREATE TABLE `po_class_group`  (
  `class_id` int(0) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_start` bit(1) NULL DEFAULT NULL COMMENT '1真0假',
  `is_public` bit(1) NULL DEFAULT NULL COMMENT '1真0假',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `stu_num` int(0) NULL DEFAULT NULL COMMENT '课程内的学生数',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`class_id`) USING BTREE,
  INDEX `courseforuser`(`user_id`) USING BTREE,
  CONSTRAINT `po_class_group_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `po_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_class_group
-- ----------------------------
INSERT INTO `po_class_group` VALUES (1, '22070141班-先锋班', '2000', b'1', b'1', '2022-2023学年Python实验班级', 45, '2023-04-02 14:19:34');
INSERT INTO `po_class_group` VALUES (2, '22070142班-先锋班', '2000', b'1', b'1', '2022-2023学年Python教学实验班级，设计课程16章，包含实验142项。', 52, '2023-04-02 15:37:53');
INSERT INTO `po_class_group` VALUES (9, '20170142班', '2000', b'0', b'1', '2022-2023学年Python教学实验班级，设计课程16章，包含实验142项。', 45, '2023-04-02 09:06:38');
INSERT INTO `po_class_group` VALUES (10, '20170241班', '2000', b'0', b'1', '2022-2023学年Python教学实验班级，设计课程16章，包含实验142项。', 46, '2023-04-02 09:06:41');
INSERT INTO `po_class_group` VALUES (24, '23170241班-实验班', '2000', b'0', b'1', '2022-2023学年Python教学实验班级，设计课程16章，包含实验142项。', 126, '2023-04-03 01:47:27');
INSERT INTO `po_class_group` VALUES (25, '23170141班-实验班-2023', '2000', b'0', b'1', '2022-2023学年Python教学实验班,2022-2023学年', 33, '2023-04-03 01:49:01');
INSERT INTO `po_class_group` VALUES (31, '测试班级123', '2000', b'0', b'1', '', 0, '2023-04-07 14:05:27');

-- ----------------------------
-- Table structure for po_class_user
-- ----------------------------
DROP TABLE IF EXISTS `po_class_user`;
CREATE TABLE `po_class_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `class_id` int(0) NOT NULL COMMENT '课程id',
  `user_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `exp_submit_num` int(0) NULL DEFAULT 0 COMMENT '实验提交数',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cuforcourse`(`class_id`) USING BTREE,
  INDEX `cuforuser`(`user_id`) USING BTREE,
  CONSTRAINT `po_class_user_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `po_class_group` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `po_class_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `po_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 193 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_class_user
-- ----------------------------
INSERT INTO `po_class_user` VALUES (182, 1, '2001', 0, '2023-04-07 12:34:50');
INSERT INTO `po_class_user` VALUES (183, 2, '2001', 0, '2023-04-07 12:34:53');
INSERT INTO `po_class_user` VALUES (189, 24, '2001', 0, '2023-04-07 12:35:46');
INSERT INTO `po_class_user` VALUES (191, 25, '2001', 0, '2023-04-07 13:06:39');

-- ----------------------------
-- Table structure for po_experiment
-- ----------------------------
DROP TABLE IF EXISTS `po_experiment`;
CREATE TABLE `po_experiment`  (
  `experiment_id` int(0) NOT NULL AUTO_INCREMENT,
  `section_id` int(0) NOT NULL COMMENT '所属章节id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验描述',
  `result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '运行结果',
  `score` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '实验分值',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`experiment_id`) USING BTREE,
  INDEX `section_id`(`section_id`) USING BTREE,
  CONSTRAINT `po_experiment_ibfk_1` FOREIGN KEY (`section_id`) REFERENCES `po_section` (`section_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_experiment
-- ----------------------------
INSERT INTO `po_experiment` VALUES (1, 1, '实验一 基本运算实验', '<ul>\n	<li>设计并完成一个完整的应用程序，完成加减乘除模等运算，功能多多益善。\n	<ul>\n		<li>基本四则运算</li>\n		<li>取模运算</li>\n		<li>求幂运算</li>\n		<li>计算简单的三角函数</li>\n		<li>求阶乘</li>\n		<li>解一元二次方程</li>\n	</ul>\n	</li>\n	<li>考核基本语法、判定语句、循环语句、逻辑运算等知识点</li>\n</ul>\n', '', 0, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (2, 1, '实验二 两数之和', '<div>给你两个数a = 3, b = 2，请你计算它们的和，并输出。</div>\n', '5', 20, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (3, 2, '第一实验', NULL, NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (4, 2, '第二实验', NULL, NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (5, 2, '第三实验', NULL, NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (6, 3, '第一实验', NULL, NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (7, 3, '第二实验', NULL, NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (8, 1, '实验三 列表排序', '<div>给你一个列表&nbsp;L, 对L进行<strong>升序</strong>排序并输出排序后的列表。</div>\n\n<div>例如：L = [8,2,50,3]</div>\n\n<div>则输出：[2,3,8,50]</div>\n', '', 23, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (9, 1, '实验四 输出字典key', '<div>给你一字典a，如a={1:1,2:2,3:3}，输出字典a的key，以&#39;,&#39;连接，如&lsquo;1,2,3&#39;。</div>\n\n<div>要求key按照<strong>字典序升序排列</strong>(注意key可能是字符串）。</div>\n\n<div>例如：a={1:1,2:2,3:3}, 则输出：1,2,3</div>\n', NULL, 5, '2023-04-05 13:41:05');
INSERT INTO `po_experiment` VALUES (28, 1, '实验五 最大公约数', '<p><strong>题目描述：</strong></p>\n\n<div>给你两个正整数a和b， 输出它们的最大公约数。</div>\n\n<div>例如：a = 3, b = 5</div>\n\n<div>则输出：1</div>\n\n<p><strong>示例：</strong></p>\n\n<div>输入：a = 3 b = 5</div>\n\n<div>输出：1</div>\n', '', 2, '2023-04-07 04:11:06');
INSERT INTO `po_experiment` VALUES (29, 1, '实验六 最小公倍数', '<p><strong>题目描述：</strong></p>\n\n<div>给你两个正整数a和b， 输出它们的最小公倍数。</div>\n\n<div>例如：a = 3, b = 5</div>\n\n<div>则输出：15</div>\n\n<p><strong>示例：</strong></p>\n\n<div>输入：a = 3 b = 5</div>\n\n<div>输出：15</div>\n', '', 3, '2023-04-07 04:13:43');
INSERT INTO `po_experiment` VALUES (30, 9, '常用工具类的使用', '<p>哈哈哈哈哈哈</p>\n', '123', 10, '2023-04-07 09:13:44');
INSERT INTO `po_experiment` VALUES (31, 1, '实验七 成绩分级', '<p><strong>题目：</strong>利用条件运算符的嵌套来完成此题：学习成绩&gt;=90分的同学用A表示，60-89分之间的用B表示，60分以下的用C表示。</p>\n\n<p><strong>程序分析：</strong>程序分析：(a&gt;b) ? a:b&nbsp;这是条件运算符的基本例子。</p>\n', '', 10, '2023-04-07 14:13:00');

-- ----------------------------
-- Table structure for po_notice
-- ----------------------------
DROP TABLE IF EXISTS `po_notice`;
CREATE TABLE `po_notice`  (
  `notice_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '通知序号',
  `class_id` int(0) NOT NULL COMMENT '班级序号',
  `title` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hits` int(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  CONSTRAINT `po_notice_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `po_class_group` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_notice
-- ----------------------------
INSERT INTO `po_notice` VALUES (1, 25, '2022-2023学年上学期Python实验安排', '<p>实验数目：每人有两次机会，总计6个实验项目</p>\n\n<p>实验时间：2022年12月23日-12月24日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 33, '2022-12-03 06:43:58');
INSERT INTO `po_notice` VALUES (3, 25, '第一章实验发布-基本数据类型', '<p>实验数目：每人有两次机会，总计8个实验项目</p>\n\n<p>实验时间：2023年03月10日-03月12日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 46, '2023-03-07 04:12:13');
INSERT INTO `po_notice` VALUES (4, 25, '第二章实验发布-操作符和运算符', '<p>实验数目：每人有两次机会，总计6个实验项目</p>\n\n<p>实验时间：2023年03月18日-03月20日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 46, '2023-03-15 06:34:10');
INSERT INTO `po_notice` VALUES (5, 25, '第三章实验发布-输入输出', '<p>实验数目：每人有两次机会，总计2个实验项目</p>\n\n<p>实验时间：2023年03月27日-03月39日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 53, '2023-03-23 04:22:08');
INSERT INTO `po_notice` VALUES (6, 25, '第四章实验发布-条件和循环', '<p>实验数目：每人有两次机会，总计7个实验项目</p>\n\n<p>实验时间：2023年04月5日-04月7日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 74, '2023-04-01 13:45:16');
INSERT INTO `po_notice` VALUES (7, 25, '期中测试实验发布', '<p>实验数目：每人有两次机会，总计1个实验项目</p>\n\n<p>实验时间：2023年04月12日-04月13日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 86, '2023-04-05 13:17:20');
INSERT INTO `po_notice` VALUES (8, 25, '第五章实验发布-面向对象简介', '<p>实验数目：每人有两次机会，总计5个实验项目</p>\n\n<p>实验时间：2023年04月23日-04月24日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 44, '2023-04-19 02:42:24');
INSERT INTO `po_notice` VALUES (9, 25, '第六章实验发布-继承和实现', '<p>实验数目：每人有两次机会，总计3个实验项目</p>\n\n<p>实验时间：2023年04月30日-05月1日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 34, '2023-04-28 03:12:27');
INSERT INTO `po_notice` VALUES (10, 25, '第七章实验发布-字符串', '<p>实验数目：每人有两次机会，总计2个实验项目</p>\n\n<p>实验时间：2023年05月8日-05月9日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 84, '2023-05-03 13:37:28');
INSERT INTO `po_notice` VALUES (11, 25, '第八章实验发布-文件存储', '<p>实验数目：每人有两次机会，总计1个实验项目</p>\n\n<p>实验时间：2023年05月14日-05月16日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 53, '2023-05-10 04:31:30');
INSERT INTO `po_notice` VALUES (12, 25, '第九章实验发布-异常机制', '<p>实验数目：每人有两次机会，总计2个实验项目</p>\n\n<p>实验时间：2023年05月23日-05月24日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 62, '2023-05-18 05:49:33');
INSERT INTO `po_notice` VALUES (13, 25, '2022-2023学年下学期Python实验安排', '<p>实验数目：每人有两次机会，总计1个实验项目</p>\n\n<p>实验时间：2023年06月5日-06月6日</p>\n\n<p>实验内容：具体请查看期末实验章节</p>\n', 108, '2023-05-23 13:44:35');
INSERT INTO `po_notice` VALUES (58, 25, '期末测试', '<p>2022-2023期末考试</p>\n', 0, '2023-04-07 14:15:46');

-- ----------------------------
-- Table structure for po_report
-- ----------------------------
DROP TABLE IF EXISTS `po_report`;
CREATE TABLE `po_report`  (
  `report_id` int(0) NOT NULL AUTO_INCREMENT,
  `experiment_id` int(0) NOT NULL,
  `user_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code_file` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验代码路径',
  `result_file` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '运行结果路径',
  `params_file` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验参数路径',
  `score` int(0) NULL DEFAULT NULL COMMENT '实验得分',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`report_id`) USING BTREE,
  INDEX `experiment_id`(`experiment_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `po_report_ibfk_1` FOREIGN KEY (`experiment_id`) REFERENCES `po_experiment` (`experiment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `po_report_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `po_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_report
-- ----------------------------
INSERT INTO `po_report` VALUES (31, 1, '2001', '/2001/code/1/1_code_2023_04_09173130711.py', '/2001/code/1/1_result_2023_04_09173130711.txt', '/2001/code/1/1_params_2023_04_09173130711.txt', 0, '2023-04-09 09:31:31');
INSERT INTO `po_report` VALUES (32, 1, '2001', '/2001/code/1/1_code_2023_04_09173543763.py', '/2001/code/1/1_result_2023_04_09173543763.txt', '/2001/code/1/1_params_2023_04_09173543763.txt', 0, '2023-04-09 09:35:44');
INSERT INTO `po_report` VALUES (33, 2, '2001', '/2001/code/2/2_code_2023_04_09173953591.py', '/2001/code/2/2_result_2023_04_09173953591.txt', '/2001/code/2/2_params_2023_04_09173953591.txt', 20, '2023-04-09 09:39:54');
INSERT INTO `po_report` VALUES (34, 2, '2001', '/2001/code/2/2_code_2023_04_09174447408.py', '/2001/code/2/2_result_2023_04_09174447408.txt', '/2001/code/2/2_params_2023_04_09174447408.txt', 20, '2023-04-09 09:44:48');
INSERT INTO `po_report` VALUES (35, 2, '2001', '/2001/code/2/2_code_2023_04_09175553826.py', '/2001/code/2/2_result_2023_04_09175553826.txt', '/2001/code/2/2_params_2023_04_09175553826.txt', 20, '2023-04-09 09:55:54');
INSERT INTO `po_report` VALUES (36, 1, '2001', '/2001/code/1/1_code_2023_04_09184820829.py', '/2001/code/1/1_result_2023_04_09184820829.txt', '/2001/code/1/1_params_2023_04_09184820829.txt', 0, '2023-04-09 10:48:21');
INSERT INTO `po_report` VALUES (37, 2, '2001', '/2001/code/2/2_code_2023_04_09184834432.py', '/2001/code/2/2_result_2023_04_09184834432.txt', '/2001/code/2/2_params_2023_04_09184834432.txt', 0, '2023-04-09 10:48:35');
INSERT INTO `po_report` VALUES (38, 1, '2001', '/2001/code/1/1_code_2023_04_09185956259.py', '/2001/code/1/1_result_2023_04_09185956259.txt', '/2001/code/1/1_params_2023_04_09185956259.txt', 0, '2023-04-09 10:59:56');

-- ----------------------------
-- Table structure for po_section
-- ----------------------------
DROP TABLE IF EXISTS `po_section`;
CREATE TABLE `po_section`  (
  `section_id` int(0) NOT NULL AUTO_INCREMENT,
  `class_id` int(0) NOT NULL COMMENT '所在班级序号',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `exp_num` int(0) NULL DEFAULT 0 COMMENT '包含实验数',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`section_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  CONSTRAINT `po_section_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `po_class_group` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_section
-- ----------------------------
INSERT INTO `po_section` VALUES (1, 25, '第一章 数据类型和运算', 2, '2023-04-05 10:44:22');
INSERT INTO `po_section` VALUES (2, 25, '第二章 运算符和操作符', 3, '2023-04-05 10:44:49');
INSERT INTO `po_section` VALUES (3, 25, '第三章 判断和循环', 2, '2023-04-05 10:45:16');
INSERT INTO `po_section` VALUES (4, 25, '第四章 面向对象', 4, '2023-04-05 10:45:34');
INSERT INTO `po_section` VALUES (9, 25, '第五章 继承和多态', 0, '2023-04-07 03:29:03');

-- ----------------------------
-- Table structure for po_user
-- ----------------------------
DROP TABLE IF EXISTS `po_user`;
CREATE TABLE `po_user`  (
  `user_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `username` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `question1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密保问题1',
  `question2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密保问题2',
  `answer1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密保答案1',
  `answer2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密保答案2',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `identity` bit(1) NOT NULL COMMENT '身份0-老师、1-学生',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_user
-- ----------------------------
INSERT INTO `po_user` VALUES ('2000', '赵宣廷', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'Python是一门什么语言', 'Python解释器的作用', '脚本语言', '解释并运行代码', '/user/2000/avatar/avatar_2023_07_17204901171.png', b'0');
INSERT INTO `po_user` VALUES ('2001', '赵长宏', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'Python中的基本数据类型有', 'Python中缩进有什么用', '整型、浮点型、字符串', '确定代码关系', '/user/2001/avatar/avatar_2023_07_17224455426.jpeg', b'1');
INSERT INTO `po_user` VALUES ('root', '秦晓晓', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'asd', 'asd', 'asd', 'asd', '/img/user/default.png', b'0');

-- ----------------------------
-- View structure for po_class_view
-- ----------------------------
DROP VIEW IF EXISTS `po_class_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `po_class_view` AS select `po_class_group`.`class_id` AS `class_id`,`po_class_group`.`class_name` AS `class_name`,`po_class_group`.`user_id` AS `user_id`,`po_class_group`.`is_start` AS `is_start`,`po_class_group`.`is_public` AS `is_public`,`po_class_group`.`describes` AS `describes`,`po_class_group`.`stu_num` AS `stu_num`,`po_class_group`.`create_time` AS `create_time`,`po_user`.`username` AS `username` from (`po_class_group` join `po_user` on((`po_class_group`.`user_id` = `po_user`.`user_id`)));

-- ----------------------------
-- View structure for po_notice_view
-- ----------------------------
DROP VIEW IF EXISTS `po_notice_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `po_notice_view` AS select `po_notice`.`notice_id` AS `notice_id`,`po_notice`.`class_id` AS `class_id`,`po_notice`.`title` AS `title`,`po_notice`.`content` AS `content`,`po_notice`.`hits` AS `hits`,`po_notice`.`create_time` AS `create_time`,`po_user`.`username` AS `username` from ((`po_notice` join `po_user`) join `po_class_group` on(((`po_class_group`.`user_id` = `po_user`.`user_id`) and (`po_notice`.`class_id` = `po_class_group`.`class_id`))));

-- ----------------------------
-- View structure for po_report_view
-- ----------------------------
DROP VIEW IF EXISTS `po_report_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `po_report_view` AS select `po_report`.`report_id` AS `report_id`,`po_report`.`experiment_id` AS `experiment_id`,`po_report`.`user_id` AS `user_id`,`po_report`.`code_file` AS `code_file`,`po_report`.`result_file` AS `result_file`,`po_report`.`params_file` AS `params_file`,`po_report`.`score` AS `score`,`po_report`.`create_time` AS `create_time`,`po_experiment`.`title` AS `title`,`po_user`.`username` AS `username`,`po_user`.`avatar_url` AS `avatar_url` from ((`po_experiment` join `po_report` on((`po_report`.`experiment_id` = `po_experiment`.`experiment_id`))) join `po_user` on((`po_report`.`user_id` = `po_user`.`user_id`)));

SET FOREIGN_KEY_CHECKS = 1;
