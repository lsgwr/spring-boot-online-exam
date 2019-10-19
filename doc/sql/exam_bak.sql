/*
 Navicat MySQL Data Transfer

 Source Server         : 122.112.151.149
 Source Server Type    : MySQL
 Source Server Version : 50715
 Source Host           : 122.112.151.149
 Source Database       : exam

 Target Server Type    : MySQL
 Target Server Version : 50715
 File Encoding         : utf-8

 Date: 06/23/2019 09:23:13 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `action`
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `action_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '前端页面操作表主键id',
  `action_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '前端操作的名字',
  `action_description` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '页面操作的描述',
  `default_check` tinyint(1) NOT NULL DEFAULT '0' COMMENT '当前操作是否需要校验,true为1,0为false',
  PRIMARY KEY (`action_id`),
  UNIQUE KEY `action_name` (`action_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='前端操作比如增删改查等的权限表';

-- ----------------------------
--  Records of `action`
-- ----------------------------
BEGIN;
INSERT INTO `action` VALUES ('1', 'add', '新增', '0'), ('2', 'query', '查询', '0'), ('3', 'get', '详情', '0'), ('4', 'update', '修改', '0'), ('5', 'delete', '删除', '0'), ('6', 'import', '导入', '0'), ('7', 'export', '导出', '0');
COMMIT;

-- ----------------------------
--  Table structure for `exam`
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `exam_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '考试表的主键',
  `exam_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '考试名称',
  `exam_avatar` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '考试的预览图',
  `exam_description` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '考试描述',
  `exam_question_ids` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '当前考试下的题目的id用-连在一起地字符串',
  `exam_question_ids_radio` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '当前考试下的题目单选题的id用-连在一起地字符串',
  `exam_question_ids_check` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '当前考试下的题目多选题的id用-连在一起地字符串',
  `exam_question_ids_judge` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '当前考试下的题目判断题的id用-连在一起地字符串',
  `exam_score` int(11) NOT NULL DEFAULT '0' COMMENT '当前考试的总分数',
  `exam_score_radio` int(11) NOT NULL DEFAULT '0' COMMENT '当前考试每个单选题的分数',
  `exam_score_check` int(11) NOT NULL DEFAULT '0' COMMENT '当前考试每个多选题的分数',
  `exam_score_judge` int(11) NOT NULL DEFAULT '0' COMMENT '当前考试每个判断题的分数',
  `exam_creator_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '考试创建者的用户id',
  `exam_time_limit` int(11) NOT NULL DEFAULT '0' COMMENT '考试的时间限制，单位为分钟',
  `exam_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '考试有效期开始时间',
  `exam_end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '考试有效期结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='考试的详细信息表';

-- ----------------------------
--  Records of `exam`
-- ----------------------------
BEGIN;
INSERT INTO `exam` VALUES ('139883c96ec44c6c9c5c2b4b85a7b54a', 'Angular学习', 'https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png', 'Angular小测验', '', '23491b7dbbdf47dcb09ece779ff44c92-2f8b20630b10471395b6c379a465cca8', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '6cabac6ec4ef44068e66cf2547543658-8f3e02d0f2a1402abd3278adc05ffb10', '90', '5', '10', '20', 'a1b661031adf4a8f969f1869d479fe74', '90', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('258bf9410e854f34bba9c9a08f4dd313', 'Ant Design Pro，Ant最佳实践', 'https://gw.alipayobjects.com/zos/rmsportal/sfjbOqnsXXJgNCjCzDBL.png', 'Ant最佳实践', '', '3864178819534aa7862b26e893aa2e62-9199009718ec4685a0a500d23ab814c5', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '305f726e7f1f4636b88bf20de0093745-8f3e02d0f2a1402abd3278adc05ffb10', '100', '10', '10', '10', '68042014e23c4ebea7234cb9c77cee5c', '150', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('3cf0b79c367a4448af8eef6737a5d0b3', 'Ant Design，阿里前端框架', 'https://gw.alipayobjects.com/zos/rmsportal/dURIMkkrRFpPgTuzkwnB.png', 'Ant前端学习', '', '31e9ad43e84e484eb8b77b7e7a76de91-334cfcd5ce47468a9babdce5c97def28', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', 'e283ac11c7064ccf976f9c48d68c6a67-ecdfbdd2a9f24017b3fe1aa0947ca146', '70', '5', '5', '5', '68042014e23c4ebea7234cb9c77cee5c', '100', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('59a3cb205f3745338c0b7e9d26ce2fe5', 'Angular学习', 'https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png', 'Angular小测验', '', '23491b7dbbdf47dcb09ece779ff44c92-2f8b20630b10471395b6c379a465cca8', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '6cabac6ec4ef44068e66cf2547543658-8f3e02d0f2a1402abd3278adc05ffb10', '90', '5', '10', '20', 'a1b661031adf4a8f969f1869d479fe74', '90', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('61f6198b88a744aead6f25d04200025e', '阿里考试', 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png', '阿里巴巴2019校招', '', '14cc31707d53433f81841058a97575da-2223b2eb8c2942459344c06ce1d3ed1d', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '305f726e7f1f4636b88bf20de0093745-4247752901bd47fba86379a1cd4b87d4', '100', '5', '6', '7', 'a1b661031adf4a8f969f1869d479fe74', '60', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('7f36f47a75184adeb3df4fb8c4058a5a', '阿里考试', 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png', '阿里巴巴2019校招', '', '14cc31707d53433f81841058a97575da-2223b2eb8c2942459344c06ce1d3ed1d', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '305f726e7f1f4636b88bf20de0093745-4247752901bd47fba86379a1cd4b87d4', '100', '5', '6', '7', 'a1b661031adf4a8f969f1869d479fe74', '60', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('8112f71b21734607b8706648f070b048', 'Ant Design，阿里前端框架', 'https://gw.alipayobjects.com/zos/rmsportal/dURIMkkrRFpPgTuzkwnB.png', 'Ant前端学习', '', '31e9ad43e84e484eb8b77b7e7a76de91-334cfcd5ce47468a9babdce5c97def28', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', 'e283ac11c7064ccf976f9c48d68c6a67-ecdfbdd2a9f24017b3fe1aa0947ca146', '70', '5', '5', '5', '68042014e23c4ebea7234cb9c77cee5c', '100', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('8a2e373a1b3e4b06b0b343aba742c224', 'BootStrap实践', 'https://gw.alipayobjects.com/zos/rmsportal/siCrBXXhmvTQGWPNLBow.png', '前端鼻祖', '', 'a12eb101321b451bbc54a136e98acfd0-bb22f19338174d1f9333f9aebd6ffeb6', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '4247752901bd47fba86379a1cd4b87d4-e283ac11c7064ccf976f9c48d68c6a67', '120', '15', '5', '10', '79392778a90d4639a297dbd0bae0f779', '120', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('8d810a3aca3d43e5961997d37dfe8f9f', 'BootStrap实践', 'https://gw.alipayobjects.com/zos/rmsportal/siCrBXXhmvTQGWPNLBow.png', '前端鼻祖', '', 'a12eb101321b451bbc54a136e98acfd0-bb22f19338174d1f9333f9aebd6ffeb6', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '4247752901bd47fba86379a1cd4b87d4-e283ac11c7064ccf976f9c48d68c6a67', '120', '15', '5', '10', '79392778a90d4639a297dbd0bae0f779', '120', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('b188e54770a74481a4d3de7862ad1c31', '阿里考试', 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png', '阿里巴巴2019校招', '', '14cc31707d53433f81841058a97575da-2223b2eb8c2942459344c06ce1d3ed1d', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '305f726e7f1f4636b88bf20de0093745-4247752901bd47fba86379a1cd4b87d4', '100', '5', '6', '7', 'a1b661031adf4a8f969f1869d479fe74', '60', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('c597bd5d65c741ddb5853dad9c8d6327', 'Ant Design Pro，Ant最佳实践', 'https://gw.alipayobjects.com/zos/rmsportal/sfjbOqnsXXJgNCjCzDBL.png', 'Ant最佳实践', '', '3864178819534aa7862b26e893aa2e62-9199009718ec4685a0a500d23ab814c5', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '305f726e7f1f4636b88bf20de0093745-8f3e02d0f2a1402abd3278adc05ffb10', '100', '10', '10', '10', '68042014e23c4ebea7234cb9c77cee5c', '150', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('c8ca3f0a816042d5919d924a96cb1ae8', 'Ant Design，阿里前端框架', 'https://gw.alipayobjects.com/zos/rmsportal/dURIMkkrRFpPgTuzkwnB.png', 'Ant前端学习', '', '31e9ad43e84e484eb8b77b7e7a76de91-334cfcd5ce47468a9babdce5c97def28', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', 'e283ac11c7064ccf976f9c48d68c6a67-ecdfbdd2a9f24017b3fe1aa0947ca146', '70', '5', '5', '5', '68042014e23c4ebea7234cb9c77cee5c', '100', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('de5d55138e0e4c3a94943afe98f1fd96', 'Angular学习', 'https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png', 'Angular小测验', '', '23491b7dbbdf47dcb09ece779ff44c92-2f8b20630b10471395b6c379a465cca8', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '6cabac6ec4ef44068e66cf2547543658-8f3e02d0f2a1402abd3278adc05ffb10', '90', '5', '10', '20', 'a1b661031adf4a8f969f1869d479fe74', '90', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('e5c1aac03a5b43a289b8c0caee037615', 'BootStrap实践', 'https://gw.alipayobjects.com/zos/rmsportal/siCrBXXhmvTQGWPNLBow.png', '前端鼻祖', '', 'a12eb101321b451bbc54a136e98acfd0-bb22f19338174d1f9333f9aebd6ffeb6', '38085807d06948ca8ad6d8eaca522c85-396b55534851427590e089fb9cc040cc', '4247752901bd47fba86379a1cd4b87d4-e283ac11c7064ccf976f9c48d68c6a67', '120', '15', '5', '10', '79392778a90d4639a297dbd0bae0f779', '120', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20'), ('ff221a5c379b40bda60677e7c54bce02', 'Ant Design Pro，Ant最佳实践', 'https://gw.alipayobjects.com/zos/rmsportal/sfjbOqnsXXJgNCjCzDBL.png', 'Ant最佳实践', '', '3864178819534aa7862b26e893aa2e62-9199009718ec4685a0a500d23ab814c5', '85047be0b2cf4077a5ec66e92ebf442b-9fe1cafeebf44e20a80f504cff60ef3f', '305f726e7f1f4636b88bf20de0093745-8f3e02d0f2a1402abd3278adc05ffb10', '100', '10', '10', '10', '68042014e23c4ebea7234cb9c77cee5c', '150', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20', '2019-06-22 12:24:20');
COMMIT;

-- ----------------------------
--  Table structure for `exam_record`
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record` (
  `exam_record_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '考试记录表的主键',
  `exam_joiner_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '考试参与者的用户id',
  `exam_join_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '参加考试的时间',
  `exam_time_cost` int(11) NOT NULL DEFAULT '0' COMMENT '完成考试所用的时间,单位分钟',
  `exam_join_score` int(11) NOT NULL DEFAULT '0' COMMENT '参与考试的实际得分',
  `exam_result_level` int(11) NOT NULL DEFAULT '0' COMMENT '考试结果的等级',
  PRIMARY KEY (`exam_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='考试记录表';

-- ----------------------------
--  Table structure for `exam_record_level`
-- ----------------------------
DROP TABLE IF EXISTS `exam_record_level`;
CREATE TABLE `exam_record_level` (
  `exam_record_level_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '考试结果等级表的主键',
  `exam_record_level_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '考试结果等级的名称',
  `exam_record_level_description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '考试结果等级的详细阐述',
  PRIMARY KEY (`exam_record_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='考试结果的等级';

-- ----------------------------
--  Records of `exam_record_level`
-- ----------------------------
BEGIN;
INSERT INTO `exam_record_level` VALUES ('1', 'excellent', '优秀'), ('2', 'good', '良好'), ('3', 'normal', '一般'), ('4', 'pass', '及格'), ('5', 'fail', '不及格');
COMMIT;

-- ----------------------------
--  Table structure for `page`
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `page_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '前端页面表主键id',
  `page_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '页面的名称,要唯一',
  `page_description` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '页面的功能性描述',
  `action_ids` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '页面对应的操作权限列表，用-连接action的id',
  PRIMARY KEY (`page_id`),
  UNIQUE KEY `page_name` (`page_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='前端页面表';

-- ----------------------------
--  Records of `page`
-- ----------------------------
BEGIN;
INSERT INTO `page` VALUES ('1', 'dashboard', '仪表盘', '1-2-3-4-5'), ('2', 'exception', '异常页', '1-2-3-4-5'), ('3', 'result', '结果页', '1-2-3-4-5'), ('4', 'profile', '详情页', '1-2-3-4-5'), ('5', 'table', '列表页', '1-6-3-4'), ('6', 'form', '表单页', '1-2-3-4-5'), ('7', 'order', '订单页', '1-2-3-4-5'), ('8', 'permission', '权限管理页', '1-3-4-5'), ('9', 'role', '角色页', '1-3-4-5'), ('10', 'user', '个人页', '1-6-3-4-5-7'), ('11', 'support', '超级管理员', '1-2-3-4-5-6-7');
COMMIT;

-- ----------------------------
--  Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `question_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '题目的主键',
  `question_name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '题目的名字',
  `question_score` int(11) NOT NULL DEFAULT '0' COMMENT '题目的分数',
  `question_creator_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '题目创建者的用户id',
  `question_level_id` int(11) NOT NULL DEFAULT '0' COMMENT '题目难易度级别',
  `question_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '题目的类型，比如单选、多选、判断等',
  `question_category_id` int(11) NOT NULL DEFAULT '0' COMMENT '题目的类型，比如数学、英语、政治等',
  `question_description` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '题目额外的描述',
  `question_option_ids` varchar(256) COLLATE utf8_bin NOT NULL COMMENT '题目的选项，用选项的id用-连在一起表示答案',
  `question_answer_option_ids` varchar(256) COLLATE utf8_bin NOT NULL COMMENT '题目的答案，用选项的id用-连在一起表示答案',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='考试题目表';

-- ----------------------------
--  Records of `question`
-- ----------------------------
BEGIN;
INSERT INTO `question` VALUES ('14cc31707d53433f81841058a97575da', '《红楼梦》的作者是谁？', '3', 'a1b661031adf4a8f969f1869d479fe74', '1', '1', '10', '红楼梦相关', '2df31aa7a344475ea8a0b2897c9754f0-6f0631ce414c49519a162af9062073ca-015147df62774a879388f924e1746f81-4d5c32e598cc41abb003f2f37dab210e', '015147df62774a879388f924e1746f81-', '2019-05-25 01:46:47', '2019-05-25 09:36:06'), ('2223b2eb8c2942459344c06ce1d3ed1d', '与他人在正式场合交谈时要严肃认真,还要注意语言的', '5', '68042014e23c4ebea7234cb9c77cee5c', '2', '1', '10', '言谈举止', 'bd8ecfb670364c1b8cee33607423066f-09153c0ac73a4d2ea60ebb8c833d0eef-c517d89483b94ffead440fb408efce11', 'bd8ecfb670364c1b8cee33607423066f', '2019-05-25 01:46:47', '2019-05-25 01:46:47'), ('23491b7dbbdf47dcb09ece779ff44c92', '你的名字是什么？', '5', 'a1b661031adf4a8f969f1869d479fe74', '2', '1', '9', '雷锋', 'afbbf97056c64199b1b44f02e8c557f5-249c3e3f31ed443e83ab4379b506be24-57e749d644044a53b48ed12f84bba0b3-83c56af7662249dd968e04bf3afbee23-', '83c56af7662249dd968e04bf3afbee23-', '2019-06-13 06:19:58', '2019-06-13 06:19:58'), ('2f8b20630b10471395b6c379a465cca8', '在参加各种社交宴请宾客中,要注意从座椅的（）侧入座,动作应轻而缓,轻松自然', '5', 'a1b661031adf4a8f969f1869d479fe74', '3', '1', '10', '就餐注意事项', '647fb34e45474ae3b064a836b436a031-77e434759f314ce08413b65b3c36e6a0-ab85c51b6e91402385aeda6551aea608', '77e434759f314ce08413b65b3c36e6a0', '2019-05-25 01:46:47', '2019-05-25 04:06:43'), ('305f726e7f1f4636b88bf20de0093745', '地球是圆地吗？', '5', 'a1b661031adf4a8f969f1869d479fe74', '3', '3', '1', '是的', 'd8fd1ca48ed74d0cb51295ad84760dd6-52e0ccb2fc4545028988681db9858f70-', 'd8fd1ca48ed74d0cb51295ad84760dd6-', '2019-06-13 06:21:16', '2019-06-13 06:21:16'), ('31e9ad43e84e484eb8b77b7e7a76de91', '在机场、商厦、地铁等公共场所乘自动扶梯时应靠（）站立,另一侧供有急事赶路的人快行', '3', 'a1b661031adf4a8f969f1869d479fe74', '1', '1', '9', '走电梯常识', '4140a14f3f6b4067962eac1c939675c9-5ac788b163b04bc9b159f11d1cb1abba-d36bd089207f48cfb5d806d9cf882009', '5ac788b163b04bc9b159f11d1cb1abba', '2019-05-25 01:46:47', '2019-05-25 04:06:49'), ('334cfcd5ce47468a9babdce5c97def28', '递接文件或名片时应当注意字体的', '5', 'a1b661031adf4a8f969f1869d479fe74', '2', '1', '10', '生活常识', 'c4aeda64b2024d9cbaaba88ac1dcdb97-995440729c4f48758920dd0f9c31f44f-c317ef23e1de49369900869764991eb0', 'c4aeda64b2024d9cbaaba88ac1dcdb97', '2019-05-25 01:46:47', '2019-05-25 01:46:47'), ('38085807d06948ca8ad6d8eaca522c85', '以接待对象为标准划分的接待类型有', '5', '68042014e23c4ebea7234cb9c77cee5c', '3', '2', '9', '商务接待', 'bed28153b51d410aafa64a6cc9c6fbb9-20ce13d67d544efeba548f7eb9642e8f-bbe3df2c7ba44b13b4b77cbd929a8fb1-22de7c7537564d7b841ea1b41a298a59-81b7ae4a705f4fc39e576085a55fccc0', 'bed28153b51d410aafa64a6cc9c6fbb9-20ce13d67d544efeba548f7eb9642e8f-bbe3df2c7ba44b13b4b77cbd929a8fb1-81b7ae4a705f4fc39e576085a55fccc0', '2019-05-25 01:46:47', '2019-05-25 04:07:16'), ('3864178819534aa7862b26e893aa2e62', '一般性的拜访多以（）为最佳交往时间', '7', '68042014e23c4ebea7234cb9c77cee5c', '3', '1', '9', '交际', '9cd1e597ae4144938be500efa21eec9b-e4697cfd242c4328b28a084e4ecaaf3c-9b14c29c2d514b10af620655ff13a204', 'e4697cfd242c4328b28a084e4ecaaf3c', '2019-05-25 01:46:47', '2019-05-25 04:07:50'), ('396b55534851427590e089fb9cc040cc', 'Zeus', '5', 'a1b661031adf4a8f969f1869d479fe74', '2', '2', '5', 'desfeeeesf', '25e3179bb90f4ef4bc550c2cb7f3562f-85aeb40a28f14735a48505d29d7c05b9-ccf9a11d724046dd964660b688d9dc28-9c511fa9d5a74090967cdb52b29060a2-92ed63ab069d4f0bae0bbf630a03d34d-', '85aeb40a28f14735a48505d29d7c05b9-9c511fa9d5a74090967cdb52b29060a2-', '2019-06-13 00:55:56', '2019-06-13 00:57:11'), ('4247752901bd47fba86379a1cd4b87d4', '与他人交谈完毕就可以立即转身离开', '10', '68042014e23c4ebea7234cb9c77cee5c', '2', '3', '10', '社交礼仪', 'b8e2e6300ab04016a82481fff15750e0-d15f9106bb9941eaab1e37356af083b3', 'd15f9106bb9941eaab1e37356af083b3', '2019-05-25 01:46:47', '2019-06-22 04:20:33'), ('6cabac6ec4ef44068e66cf2547543658', '行握手礼时,与多人同时握手时,可以交叉握手', '6', 'a1b661031adf4a8f969f1869d479fe74', '1', '3', '10', '礼仪', 'b8e2e6300ab04016a82481fff15750e0-d15f9106bb9941eaab1e37356af083b3', 'd15f9106bb9941eaab1e37356af083b3', '2019-05-25 01:46:47', '2019-06-22 04:20:45'), ('85047be0b2cf4077a5ec66e92ebf442b', '名片的作用有', '5', 'a1b661031adf4a8f969f1869d479fe74', '3', '2', '10', '名片', '61f3d24b130749a88626b5035e9708e9-b7926212674b4b71b93dcffca102f578-21eccfb3bc43464b905eb0a8837ce094-317914052c94400f8e45a814896c9cd9', '61f3d24b130749a88626b5035e9708e9-b7926212674b4b71b93dcffca102f578-317914052c94400f8e45a814896c9cd9', '2019-05-25 01:46:47', '2019-05-25 04:08:27'), ('8f3e02d0f2a1402abd3278adc05ffb10', '有人问路可以用手指指示方向', '8', '68042014e23c4ebea7234cb9c77cee5c', '2', '3', '9', '礼仪', 'b8e2e6300ab04016a82481fff15750e0-d15f9106bb9941eaab1e37356af083b3', 'b8e2e6300ab04016a82481fff15750e0', '2019-05-25 01:46:47', '2019-06-22 04:20:55'), ('9199009718ec4685a0a500d23ab814c5', '日常生活中邻里之间应', '2', 'a1b661031adf4a8f969f1869d479fe74', '1', '1', '9', '生活邻里', '233c13ee72c9456989788201fd108c8e-785ad33574f747fd870dd6eadff303ab-e4142da9d88b457b91249a3090f35d6b', '233c13ee72c9456989788201fd108c8e-', '2019-05-25 01:46:47', '2019-05-25 09:32:32'), ('9fe1cafeebf44e20a80f504cff60ef3f', '以目的的不同为标准划分的拜访类型有', '5', '68042014e23c4ebea7234cb9c77cee5c', '2', '2', '9', '拜访', 'ab96efeec3144ecea3bf955d4e52980f-287ce730f7804743935f504a18cd8538-044cf714b7024a1689cbc9def3591f51-88ec4c60df0842c7b603cf82450b12cb-bc4e6c276e6d4b80871badf7d2a9087c', 'ab96efeec3144ecea3bf955d4e52980f-044cf714b7024a1689cbc9def3591f51-88ec4c60df0842c7b603cf82450b12cb-bc4e6c276e6d4b80871badf7d2a9087c', '2019-05-25 01:46:47', '2019-05-25 01:46:47'), ('a12eb101321b451bbc54a136e98acfd0', '客人来访时,我们要为客人打开房门。当房门向外开时()进', '5', '68042014e23c4ebea7234cb9c77cee5c', '1', '1', '9', '礼仪', '8c0f6801448b4185b3446d92b0721c40-251eb27617ce458697f6e966bbf87bed-228f57a595a2416aa6cf7df66095236a', '8c0f6801448b4185b3446d92b0721c40', '2019-05-25 01:46:47', '2019-05-25 01:46:47'), ('bb22f19338174d1f9333f9aebd6ffeb6', '做客时入座动作要', '8', '68042014e23c4ebea7234cb9c77cee5c', '1', '1', '9', '礼仪相关', '0c83f68e3d9948e38d65eb22c257bf7f-0dda4965171b4f9680a6acfab9af9625-9da49e7309604e81bacfdc7bb9044bc1', '9da49e7309604e81bacfdc7bb9044bc1', '2019-05-25 01:46:47', '2019-05-25 04:09:19'), ('cff7d16da6ab428e893c748d5c759cb2', '拜访他人应选择（）,并应提前打招呼', '5', 'a1b661031adf4a8f969f1869d479fe74', '2', '1', '9', '交往礼节', 'ca45b95620ae4d33986cb8067ef2525c-db43d6e8819047539922b510e0a039b7-8c95b512240b4a47b85743a64f65c0ba', '8c95b512240b4a47b85743a64f65c0ba', '2019-05-25 01:46:47', '2019-05-25 04:09:30'), ('e1d16aebc4124790bb9435973b89f105', '正式交往场合我们的仪表仪容要给人()的感觉', '5', '68042014e23c4ebea7234cb9c77cee5c', '3', '1', '10', '仪表仪容', 'cbfebe5f94124c0dbd8ad280d63351b0-42eee2c478c043168bd7ed8d7c2dcafe-2eaf5866748148ce901d12897fed9c11', '2eaf5866748148ce901d12897fed9c11', '2019-05-25 01:46:47', '2019-05-25 01:46:47'), ('e283ac11c7064ccf976f9c48d68c6a67', '与他人交谈时,要盯着他人的双眉到鼻尖的三角区域内', '5', '68042014e23c4ebea7234cb9c77cee5c', '2', '3', '9', '礼仪', 'b8e2e6300ab04016a82481fff15750e0-d15f9106bb9941eaab1e37356af083b3', 'b8e2e6300ab04016a82481fff15750e0', '2019-05-25 01:46:47', '2019-06-22 04:21:07'), ('ecdfbdd2a9f24017b3fe1aa0947ca146', '使用手机时,手机不宜握在手里或挂在腰带上', '5', 'a1b661031adf4a8f969f1869d479fe74', '2', '3', '9', '生活细节', 'b8e2e6300ab04016a82481fff15750e0-d15f9106bb9941eaab1e37356af083b3', 'b8e2e6300ab04016a82481fff15750e0', '2019-05-25 01:46:47', '2019-06-22 04:21:15');
COMMIT;

-- ----------------------------
--  Table structure for `question_category`
-- ----------------------------
DROP TABLE IF EXISTS `question_category`;
CREATE TABLE `question_category` (
  `question_category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '问题类别表的主键',
  `question_category_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '问题类别名称',
  `question_category_description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '问题类别的描述',
  PRIMARY KEY (`question_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='题目类别表';

-- ----------------------------
--  Records of `question_category`
-- ----------------------------
BEGIN;
INSERT INTO `question_category` VALUES ('1', '天文', '地球与宇宙的探索'), ('2', '数学', '所有学科的基础'), ('3', '物理', '体会牛顿与麦克斯韦的伟大'), ('4', '生物', '从宏观到微观了解生命'), ('5', '地理', '踏遍大好河山'), ('6', '化学', '分子与原子的碰撞'), ('7', '英语', '出门旅游必备'), ('8', '历史', '体会悠悠岁月'), ('9', '人文', '生活与交际'), ('10', '生活', '人与社会的交互');
COMMIT;

-- ----------------------------
--  Table structure for `question_level`
-- ----------------------------
DROP TABLE IF EXISTS `question_level`;
CREATE TABLE `question_level` (
  `question_level_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '题目难易度的主键',
  `question_level_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '题目难易度名称',
  `question_level_description` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '题目难易度的描述',
  PRIMARY KEY (`question_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='问题的难易度级别';

-- ----------------------------
--  Records of `question_level`
-- ----------------------------
BEGIN;
INSERT INTO `question_level` VALUES ('1', 'high', '难'), ('2', 'middle', '中'), ('3', 'low', '易');
COMMIT;

-- ----------------------------
--  Table structure for `question_option`
-- ----------------------------
DROP TABLE IF EXISTS `question_option`;
CREATE TABLE `question_option` (
  `question_option_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '题目选项表的主键',
  `question_option_content` varchar(512) COLLATE utf8_bin NOT NULL COMMENT '选项的内容',
  `question_option_description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '选项的额外描述，可以用于题目答案解析',
  PRIMARY KEY (`question_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='题目的选项';

-- ----------------------------
--  Records of `question_option`
-- ----------------------------
BEGIN;
INSERT INTO `question_option` VALUES ('015147df62774a879388f924e1746f81', '曹雪芹', '《红楼梦》的作者是谁？'), ('044cf714b7024a1689cbc9def3591f51', '情感拜访', '以目的的不同为标准划分的拜访类型有'), ('09153c0ac73a4d2ea60ebb8c833d0eef', '慢条斯理', '与他人在正式场合交谈时要严肃认真,还要注意语言的'), ('0c83f68e3d9948e38d65eb22c257bf7f', '快捷', '做客时入座动作要'), ('0dda4965171b4f9680a6acfab9af9625', '轻稳', '做客时入座动作要'), ('20ce13d67d544efeba548f7eb9642e8f', '商务接待', '以接待对象为标准划分的接待类型有'), ('21eccfb3bc43464b905eb0a8837ce094', '代替请柬', '名片的作用有'), ('228f57a595a2416aa6cf7df66095236a', '同时进门', '客人来访时,我们要为客人打开房门。当房门向外开时()进'), ('22de7c7537564d7b841ea1b41a298a59', '消费接待', '以接待对象为标准划分的接待类型有'), ('233c13ee72c9456989788201fd108c8e', '互尊互谅', '日常生活中邻里之间应'), ('249c3e3f31ed443e83ab4379b506be24', '焦恩俊', null), ('251eb27617ce458697f6e966bbf87bed', '我们先进', '客人来访时,我们要为客人打开房门。当房门向外开时()进'), ('25e3179bb90f4ef4bc550c2cb7f3562f', 'A', null), ('287ce730f7804743935f504a18cd8538', '政治拜访', '以目的的不同为标准划分的拜访类型有'), ('2df31aa7a344475ea8a0b2897c9754f0', '罗贯中', '《红楼梦》的作者是谁？'), ('2eaf5866748148ce901d12897fed9c11', '端庄、大方、美观', '正式交往场合我们的仪表仪容要给人()的感觉'), ('317914052c94400f8e45a814896c9cd9', '便于通知', '名片的作用有'), ('4140a14f3f6b4067962eac1c939675c9', '左侧', '在机场、商厦、地铁等公共场所乘自动扶梯时应靠（）站立,另一侧供有急事赶路的人快行'), ('42eee2c478c043168bd7ed8d7c2dcafe', '漂亮、美观、时髦', '正式交往场合我们的仪表仪容要给人()的感觉'), ('4d5c32e598cc41abb003f2f37dab210e', '吴承恩', '《红楼梦》的作者是谁？'), ('52e0ccb2fc4545028988681db9858f70', '错误', null), ('57e749d644044a53b48ed12f84bba0b3', '白求恩', null), ('5ac788b163b04bc9b159f11d1cb1abba', '右侧', '在机场、商厦、地铁等公共场所乘自动扶梯时应靠（）站立,另一侧供有急事赶路的人快行'), ('61f3d24b130749a88626b5035e9708e9', '代替通话', '名片的作用有'), ('647fb34e45474ae3b064a836b436a031', '前侧', '在参加各种社交宴请宾客中,要注意从座椅的（）侧入座,动作应轻而缓,轻松自然。'), ('6f0631ce414c49519a162af9062073ca', '施耐庵', '《红楼梦》的作者是谁？'), ('77e434759f314ce08413b65b3c36e6a0', '左侧', '在参加各种社交宴请宾客中,要注意从座椅的（）侧入座,动作应轻而缓,轻松自然'), ('785ad33574f747fd870dd6eadff303ab', '互不来往', '日常生活中邻里之间应'), ('81b7ae4a705f4fc39e576085a55fccc0', '朋友接待', '以接待对象为标准划分的接待类型有'), ('83c56af7662249dd968e04bf3afbee23', '雷锋', null), ('85aeb40a28f14735a48505d29d7c05b9', 'B', null), ('88ec4c60df0842c7b603cf82450b12cb', '礼节性拜访', '以目的的不同为标准划分的拜访类型有'), ('8c0f6801448b4185b3446d92b0721c40', '客人先进', '客人来访时,我们要为客人打开房门。当房门向外开时()进'), ('8c95b512240b4a47b85743a64f65c0ba', '节假日的下午或平日的晚饭后', '拜访他人应选择（）,并应提前打招呼'), ('92ed63ab069d4f0bae0bbf630a03d34d', 'E', null), ('995440729c4f48758920dd0f9c31f44f', '侧面朝向对方', '递接文件或名片时应当注意字体的'), ('9b14c29c2d514b10af620655ff13a204', '十分钟左右', '一般性的拜访多以（）为最佳交往时间'), ('9c511fa9d5a74090967cdb52b29060a2', 'D', null), ('9cd1e597ae4144938be500efa21eec9b', '1小时左右', '一般性的拜访多以（）为最佳交往时间'), ('9da49e7309604e81bacfdc7bb9044bc1', '缓慢', '做客时入座动作要'), ('ab85c51b6e91402385aeda6551aea608', '右侧', '在参加各种社交宴请宾客中,要注意从座椅的（）侧入座,动作应轻而缓,轻松自然'), ('ab96efeec3144ecea3bf955d4e52980f', '商业拜访', '以目的的不同为标准划分的拜访类型有'), ('afbbf97056c64199b1b44f02e8c557f5', '董存瑞', null), ('b7926212674b4b71b93dcffca102f578', '代替便函', '名片的作用有'), ('b8e2e6300ab04016a82481fff15750e0', '正确', '判断题专用选项'), ('bbe3df2c7ba44b13b4b77cbd929a8fb1', '上访接待', '以接待对象为标准划分的接待类型有'), ('bc4e6c276e6d4b80871badf7d2a9087c', '公务拜访', '以目的的不同为标准划分的拜访类型有'), ('bd8ecfb670364c1b8cee33607423066f', '准确规范', '与他人在正式场合交谈时要严肃认真,还要注意语言的'), ('bed28153b51d410aafa64a6cc9c6fbb9', '公务接待', '以接待对象为标准划分的接待类型有'), ('c317ef23e1de49369900869764991eb0', '反面朝向对方', '递接文件或名片时应当注意字体的'), ('c4aeda64b2024d9cbaaba88ac1dcdb97', '正面朝向对方', '递接文件或名片时应当注意字体的'), ('c517d89483b94ffead440fb408efce11', '声音洪亮', '与他人在正式场合交谈时要严肃认真,还要注意语言的'), ('ca45b95620ae4d33986cb8067ef2525c', '清晨', '拜访他人应选择（）,并应提前打招呼'), ('cbfebe5f94124c0dbd8ad280d63351b0', '随意、整齐、干净', '正式交往场合我们的仪表仪容要给人()的感觉'), ('ccf9a11d724046dd964660b688d9dc28', 'C', null), ('d15f9106bb9941eaab1e37356af083b3', '错误', '判断题专用选项'), ('d36bd089207f48cfb5d806d9cf882009', '中间', '在机场、商厦、地铁等公共场所乘自动扶梯时应靠（）站立,另一侧供有急事赶路的人快行'), ('d8fd1ca48ed74d0cb51295ad84760dd6', '正确', null), ('db43d6e8819047539922b510e0a039b7', '用餐时间 ', '拜访他人应选择（）,并应提前打招呼'), ('e4142da9d88b457b91249a3090f35d6b', '不必考虑邻里关系', '日常生活中邻里之间应'), ('e4697cfd242c4328b28a084e4ecaaf3c', '半小时左右', '一般性的拜访多以（）为最佳交往时间');
COMMIT;

-- ----------------------------
--  Table structure for `question_type`
-- ----------------------------
DROP TABLE IF EXISTS `question_type`;
CREATE TABLE `question_type` (
  `question_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '题目类型表的主键',
  `question_type_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '题目类型名称',
  `question_type_description` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '题目类型的描述',
  PRIMARY KEY (`question_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='问题类型';

-- ----------------------------
--  Records of `question_type`
-- ----------------------------
BEGIN;
INSERT INTO `question_type` VALUES ('1', 'single', '单选题'), ('2', 'multi', '多选题'), ('3', 'judge', '判断题');
COMMIT;

-- ----------------------------
--  Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色表主键id',
  `role_name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `role_description` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '角色的描述',
  `role_detail` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '角色的详细功能阐述',
  `role_page_ids` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '当前角色所能访问的页面的id集合',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
--  Records of `role`
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', 'admin', '管理员', '拥有教师和学生的所有权限', '1-2-3-4-5-6-7-8-9-10-11'), ('2', 'teacher', '教师', '出题、组试卷、管理学生和试卷', '1-2-3-4-5-6-7'), ('3', 'student', '学生', '参与考试，查看分数', '8-9-10-11');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户id,主键，字符串型',
  `user_username` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `user_nickname` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户昵称',
  `user_password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户秘密',
  `user_role_id` int(11) NOT NULL COMMENT '当前用户的角色的id',
  `user_avatar` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '用户的头像地址',
  `user_description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '用户的自我描述',
  `user_email` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '用户邮箱',
  `user_phone` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '用户手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_username` (`user_username`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `user_phone` (`user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('68042014e23c4ebea7234cb9c77cee5c', '王蕊', '暮雪飞扬', 'MTk5MjEwMjN3cg==', '2', 'http://122.112.151.149:8888/group1/M00/00/00/wKgAkF0I_uyAUjlbAAAO1qHbugM426.png', '快乐就好', '1648266192@qq.com', '15261897332', '2019-05-06 18:03:27', '2019-06-18 15:29:18'), ('79392778a90d4639a297dbd0bae0f779', '李华', '红领巾', 'YWRtaW4xMjM=', '3', 'http://122.112.151.149:8888/group1/M00/00/00/wKgAkF0I_uyAUjlbAAAO1qHbugM426.png', '好好学习，天天向上', 'liangshanguang@huawei.com', '17712345678', '2019-05-06 18:07:14', '2019-06-18 15:29:27'), ('a1b661031adf4a8f969f1869d479fe74', '梁山广', '水刃', 'MTk5MjEwMjN3cg==', '1', 'http://122.112.151.149:8888/group1/M00/00/00/wKgAkF0I_uyAUjlbAAAO1qHbugM426.png', '绳锯木断，水滴石穿', 'liangshanguang2@gmail.com', '17601324488', '2019-05-06 17:57:44', '2019-06-18 15:29:32');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
