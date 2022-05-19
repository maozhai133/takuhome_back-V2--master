/*
 Navicat Premium Data Transfer

 Source Server         : nekotaku
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : takuhome

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 28/03/2022 16:30:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comm_article
-- ----------------------------
DROP TABLE IF EXISTS `comm_article`;
CREATE TABLE `comm_article`  (
  `comm_id` int NOT NULL AUTO_INCREMENT COMMENT '博文相关的评论id',
  `comm_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论名',
  `parent_id` int NOT NULL COMMENT '父级id',
  `comm_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `comm_likenum` int NOT NULL COMMENT '评论点赞数',
  `comm_createtime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论发表时间',
  `artcle_id` int NOT NULL COMMENT '博文id(外键)',
  INDEX `artcle_id`(`artcle_id`) USING BTREE,
  INDEX `comm_id`(`comm_id`) USING BTREE,
  CONSTRAINT `comm_article_ibfk_1` FOREIGN KEY (`artcle_id`) REFERENCES `taku_article` (`article_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comm_article_ibfk_2` FOREIGN KEY (`comm_id`) REFERENCES `taku_comment` (`comm_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评论表(与博文相关)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comm_article
-- ----------------------------

-- ----------------------------
-- Table structure for taku_admin
-- ----------------------------
DROP TABLE IF EXISTS `taku_admin`;
CREATE TABLE `taku_admin`  (
  `adm_id` tinyint NOT NULL AUTO_INCREMENT COMMENT 'ID值',
  `adm_username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名(登录用)',
  `adm_password` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `adm_nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称(作者名显示用)',
  `adm_headimg` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `adm_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户邮箱',
  `adm_createTime` datetime NOT NULL COMMENT '创建时间',
  `adm_updateTime` datetime NOT NULL COMMENT '更新时间',
  `adm_status` int NOT NULL COMMENT '状态(是否启用)(2为超级管理员,1启用,0停用)',
  `adm_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户简介',
  PRIMARY KEY (`adm_id`) USING BTREE,
  UNIQUE INDEX `adm_username`(`adm_username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_admin
-- ----------------------------
INSERT INTO `taku_admin` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '/asset/headImg/admin/2021-12-08/5b017c99d24a4b3781a2ed47e5ac2cd6.jpg', 'maozhai133@163.com', '2021-11-17 11:25:10', '2021-12-16 15:27:46', 2, '管理员账户');
INSERT INTO `taku_admin` VALUES (10, 'ceshi', 'e10adc3949ba59abbe56e057f20f883e', 'NIBA22', '/asset/headImg/ceshi/2021-12-14/020c70ef39b646ff9eb83a13bf46632a.jpg', '3346683848@qq.com', '2021-12-06 10:52:32', '2021-12-14 15:53:36', 1, '喵喵喵111');
INSERT INTO `taku_admin` VALUES (11, 'lvgongwen', 'e10adc3949ba59abbe56e057f20f883e', '8L3hXfVp01', '/asset/headImg/defaultImage/cat.jpg', '1846394486@qq.com', '2021-12-06 15:19:12', '2021-12-06 15:19:12', 1, '这个人很懒，还没有留下任何讯息');
INSERT INTO `taku_admin` VALUES (15, 'maomao', '3b8a1c4f965bbf8df2629a12e787d248', '猫猫人', '/asset/headImg/maomao/2021-12-14/97238f084d214a84a51de56979b9b0b8.jpg', '1033187729@qq.com', '2021-12-06 16:36:08', '2021-12-14 16:39:36', 1, '这个人很懒，还没有留下任何讯息');
INSERT INTO `taku_admin` VALUES (16, 'ceshi2', '3b8a1c4f965bbf8df2629a12e787d248', 'Sky', '/asset/headImg/ceshi2/2021-12-14/101a55c1fa104079b69fd09afdc82c35.jpg', '2101726857@qq.com', '2021-12-14 17:38:26', '2021-12-14 17:41:48', 0, '这个人很懒，还没有留下任何讯息');
INSERT INTO `taku_admin` VALUES (17, 'senjiang', 'e10adc3949ba59abbe56e057f20f883e', '菠萝男123', '/asset/headImg/senjiang/2022-02-21/f8a7b1eda20d4ca384c4a4782efb8387.jpg', '1193790271@qq.com', '2022-02-21 16:12:38', '2022-02-21 16:13:25', 1, '这个人很懒，还没有留下任何讯息');

-- ----------------------------
-- Table structure for taku_article
-- ----------------------------
DROP TABLE IF EXISTS `taku_article`;
CREATE TABLE `taku_article`  (
  `article_id` int NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `article_content` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章内容',
  `article_likenum` int NOT NULL COMMENT '文章点赞数',
  `article_views` int NOT NULL COMMENT '文章查看数',
  `article_image` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章封面',
  `article_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章标题',
  `article_createtime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章发表时间',
  `article_isTop` tinyint(1) NOT NULL COMMENT '文章是否置顶',
  `category_id` int NOT NULL COMMENT '文章分类',
  `article_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章描述',
  `count_comment` int NOT NULL COMMENT '文章评论数量',
  `article_tag1` int NOT NULL COMMENT '文章标签1',
  `article_tag2` int NOT NULL COMMENT '文章标签2',
  `article_tag3` int NOT NULL COMMENT '文章标签3',
  `category_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  `adm_username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`article_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  CONSTRAINT `taku_article_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `taku_category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '博文表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_article
-- ----------------------------
INSERT INTO `taku_article` VALUES (1, '<h1 label=\"标题居中\" style=\"font-size: 32px; font-weight: bold; border-bottom: 2px solid rgb(204, 204, 204); padding: 0px 4px 0px 0px; text-align: center; margin: 0px 0px 20px;\"><strong><em><span style=\"font-family: 宋体, SimSun;\">测试文章一修改</span></em></strong><span style=\"font-family: 宋体, SimSun;\"></span></h1><p><em><span style=\"font-family: 宋体, SimSun; font-size: 24px;\"></span></em><img src=\"/asset/2021-11-17/66cdae053b2f4bfaac7311ae46ec299e.jpg\" title=\"66cdae053b2f4bfaac7311ae46ec299e.jpg\" alt=\"66cdae053b2f4bfaac7311ae46ec299e.jpg\" width=\"662\" height=\"374\" style=\"width: 662px; height: 374px;\"/><strong><span style=\"font-size: 20px; font-family: 隶书, SimLi; text-decoration: underline;\"><em>测试内容一:</em></span></strong><em><span style=\"font-family: 宋体, SimSun; font-size: 24px;\"><br/></span></em></p><pre class=\"brush:java;toolbar:false\">system.out.println(&quot;hello,world&quot;);</pre><p><strong><span style=\"font-size: 20px; font-family: 隶书, SimLi; text-decoration: underline;\"></span></strong>haele</p>', 0, 0, '/asset/2021-11-15/45483e4be0054e22ae19d6d75f1ec8c5.jpg', '测试修改', '1648455947000', 0, 10, '测试简介修改222', 0, 1, 0, 0, 'Java', 'admin');
INSERT INTO `taku_article` VALUES (2, '<p>测试</p><p><br/></p><p><img src=\"/asset/2021-11-15/825708861c9e4c06a71f1b7dfcd513e5.jpg\" title=\"825708861c9e4c06a71f1b7dfcd513e5.jpg\" alt=\"825708861c9e4c06a71f1b7dfcd513e5.jpg\" width=\"991\" height=\"365\" style=\"width: 991px; height: 365px;\"/></p>', 0, 0, '/asset/2021-11-15/923f2cb887b54d28a0f58004b644889d.png', '测试文章标题二', '1648456019000', 1, 10, '测试修改', 0, 3, 10, 0, 'Java', 'admin');
INSERT INTO `taku_article` VALUES (12, '<p><img src=\"/asset/2021-12-07/3435de65592a4ff2937385ee6cbea528.jpg\" title=\"3435de65592a4ff2937385ee6cbea528.jpg\" alt=\"3435de65592a4ff2937385ee6cbea528.jpg\" width=\"413\" height=\"274\" style=\"width: 413px; height: 274px;\"/>22222</p>', 0, 0, '/asset/2021-12-07/1f189bab2d574b539c81c28994cdaede.jpg', '图片测试', '1648430772000', 0, 38, '测试图片上传', 0, 9, 14, 10, '动画', 'admin');

-- ----------------------------
-- Table structure for taku_category
-- ----------------------------
DROP TABLE IF EXISTS `taku_category`;
CREATE TABLE `taku_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int NOT NULL COMMENT '分类父级',
  `category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类二级名',
  `parent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类父级名',
  `adm_username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名(唯一)',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_category
-- ----------------------------
INSERT INTO `taku_category` VALUES (2, 0, '技术笔记', '无', 'admin');
INSERT INTO `taku_category` VALUES (3, 0, '杂谈', '无', 'admin');
INSERT INTO `taku_category` VALUES (4, 0, '框架', '无', 'admin');
INSERT INTO `taku_category` VALUES (5, 0, 'ACG', '无', 'admin');
INSERT INTO `taku_category` VALUES (10, 2, 'Java', '技术笔记', 'admin');
INSERT INTO `taku_category` VALUES (14, 2, 'MySQL', '技术笔记', 'admin');
INSERT INTO `taku_category` VALUES (16, 4, 'SpringBoot', '框架', 'admin');
INSERT INTO `taku_category` VALUES (20, 4, 'MyBatis', '框架', 'admin');
INSERT INTO `taku_category` VALUES (28, 2, '数据结构C', '技术笔记', 'admin');
INSERT INTO `taku_category` VALUES (29, 5, '游戏', 'ACG', 'admin');
INSERT INTO `taku_category` VALUES (38, 5, '动画', 'ACG', 'admin');
INSERT INTO `taku_category` VALUES (39, 5, 'MAD', 'ACG', 'admin');
INSERT INTO `taku_category` VALUES (58, 0, '111', '无', 'lvgongwen');
INSERT INTO `taku_category` VALUES (59, 58, '222', '111', 'lvgongwen');
INSERT INTO `taku_category` VALUES (60, 2, 'HTML', '技术笔记', 'admin');

-- ----------------------------
-- Table structure for taku_comment
-- ----------------------------
DROP TABLE IF EXISTS `taku_comment`;
CREATE TABLE `taku_comment`  (
  `comm_id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `comm_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论姓名()',
  `comm_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评论内容',
  `comm_likenum` int NULL DEFAULT NULL COMMENT '评论点赞数',
  `comm_createtime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论发表时间',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级id(涉及回复)',
  PRIMARY KEY (`comm_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '博文评论表(与系统相关)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_comment
-- ----------------------------

-- ----------------------------
-- Table structure for taku_friend
-- ----------------------------
DROP TABLE IF EXISTS `taku_friend`;
CREATE TABLE `taku_friend`  (
  `friend_id` int NOT NULL AUTO_INCREMENT COMMENT '友链id',
  `friend_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '友链名',
  `friend_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '友链地址',
  PRIMARY KEY (`friend_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_friend
-- ----------------------------

-- ----------------------------
-- Table structure for taku_tag
-- ----------------------------
DROP TABLE IF EXISTS `taku_tag`;
CREATE TABLE `taku_tag`  (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
  `adm_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_tag
-- ----------------------------
INSERT INTO `taku_tag` VALUES (1, 'Java', 'admin');
INSERT INTO `taku_tag` VALUES (2, 'MySql', 'admin');
INSERT INTO `taku_tag` VALUES (3, 'HTML', 'admin');
INSERT INTO `taku_tag` VALUES (4, 'C#', 'admin');
INSERT INTO `taku_tag` VALUES (9, '游戏', 'admin');
INSERT INTO `taku_tag` VALUES (10, '动画', 'admin');
INSERT INTO `taku_tag` VALUES (14, '音乐', 'admin');

-- ----------------------------
-- Table structure for taku_userlog
-- ----------------------------
DROP TABLE IF EXISTS `taku_userlog`;
CREATE TABLE `taku_userlog`  (
  `logId` int NOT NULL COMMENT '用户日志id',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志信息',
  `createTime` timestamp NOT NULL COMMENT '日志时间',
  PRIMARY KEY (`logId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taku_userlog
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
