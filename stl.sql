/*
 Navicat Premium Data Transfer

 Source Server         : 本地链接
 Source Server Type    : MySQL
 Source Server Version : 80405 (8.4.5)
 Source Host           : localhost:3306
 Source Schema         : stl

 Target Server Type    : MySQL
 Target Server Version : 80405 (8.4.5)
 File Encoding         : 65001

 Date: 23/08/2025 10:35:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `phone` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '15963294666', NULL, 'BqRg2WxlhDo4kD9KZIydB6yVtUbbC8wjnvCyBoRpIGI8A/LEaHbOGMkh7t3fN5g/', 'admin1', '/images/coach_avatar/20250818194454-883.png', NULL);
INSERT INTO `admin` VALUES (2, '15963294777', NULL, 'Rn1FbWRr1K88KkJYAVBcCAcMELLaUlwy5DAqmVoVoWIrykTZX5xVcfoMphOyQYZE', 'admin2', '/images/coach_avatar/20250818194459-179.png', NULL);
INSERT INTO `admin` VALUES (3, '15963294888', NULL, 'IbrK0GhQV8/HB/cihG7RgiuO0+1E51KkNVpjDfLNHVxFmRuj6zem0sPTMyjc3zk4', 'admin3', '/images/coach_avatar/20250818194505-961.png', NULL);
INSERT INTO `admin` VALUES (4, '12345678912', NULL, '123456', 'aaa', '/images/coach_avatar/20250818194511-898.png', NULL);
INSERT INTO `admin` VALUES (5, '13312341234', NULL, 'ICy5YqxZB1uWSwcVLSNLcA==', '管理员', '/images/coach_avatar/20250818194517-283.png', NULL);
INSERT INTO `admin` VALUES (6, '12312312345', NULL, 'nctd4YD+wv2sgDnvOTIqU/TWOgdpxMpZHE50xYD3ZBJGUEN686pW44ByVXCVQZGZ', '背背背', '/images/coach_avatar/20250818194527-757.png', NULL);
INSERT INTO `admin` VALUES (7, '17515217506', NULL, '9HWr6h8XxYJh2fnmAubkiavng+qsKZaFa9AJYh8smDuqDWBaTIUjVxZKbY5YjntZ', 'asdad', '/images/coach_avatar/20250818200444-698.png', NULL);
INSERT INTO `admin` VALUES (8, NULL, 'sxk2535665310@163.com', 'JmoCufa21FY53Dwg3i0FQ4su3vs6J+buPSPAeoqxY+zkfQAJU7S8PlyhwetIFGHV', NULL, NULL, '174575');
INSERT INTO `admin` VALUES (14, NULL, '3109895332@qq.com', 'PKfLkAn9/OMoMJ8PzUkBV2z0HzvDgt3Ij5v5J7A15TYLQrrxE9c+89+mHSmKd2WK', NULL, NULL, '120853');
INSERT INTO `admin` VALUES (15, NULL, '1744860696@qq.com', 'dy8xJ50WYAzhCLWwwT643zph/JJdu5PJoR1r6TY1iCoeudVPJVnzRfZAU0iYMdvX', NULL, NULL, '261263');

-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `phone` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `wechat` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `recommend` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `avatar` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coach
-- ----------------------------
INSERT INTO `coach` VALUES (1, '强森', '13212341234', '13212341234', '兄弟们，撸铁~~~~', '/images/coach_avatar/20250817222235-667.png');
INSERT INTO `coach` VALUES (2, '小小', '13311221122', '13311221122', '知之愈明，则行之愈笃;行之愈笃，则知之益明。', '/images/coach_avatar/20250818194436-669.png');
INSERT INTO `coach` VALUES (3, '龙妈', '13366776677', '13366776677', '坦格利安家族的风暴降生丹尼莉丝坦格利安一世、不焚者、弥林女王、安达尔人和先民的女王、七国统治者暨全境守护者、草原上的卡丽熙、打碎镣铐者以及龙之母~~\n驭龙之道，在明明德~~~', '/images/coach_avatar/20250818194220-615.png');
INSERT INTO `coach` VALUES (4, '魔鬼筋肉人', '13312341234', '13312341234', '中国古拳法第十三代传人\n1974年，第一次在东南亚打自由搏击就得了冠军；\n1980年打赢了日本重炮手雷龙，\n接着连续三年打败所有日本空手道高手，赢得全日本自由搏击冠军\n中国古拳法，被喻为空手道克星，绰号“魔鬼筋肉人”！', '/images/coach_avatar/20250818194250-214.png');
INSERT INTO `coach` VALUES (5, '孙悟空', '15655660000', '15655660000', '来自贝吉塔行星的赛亚人，幼时以“下级战士”之身份被送往地球，并被武道家孙悟饭收养，因失控变为巨猿将孙悟饭踩死后独自生活在深山，后因结识布尔玛从而踏上寻找龙珠之旅。梦想是不断变强，为追求力量而刻苦修行。', '/images/coach_avatar/20250818194316-716.png');
INSERT INTO `coach` VALUES (6, '猪八戒', '14567891231', 'zbj@123.com', '人参果好吃', '/images/coach_avatar/20250818194344-709.png');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `begin` timestamp NOT NULL,
  `length` int NOT NULL,
  `max_count` int NOT NULL,
  `room` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `avatar` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `coach_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 90 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, '铁人三项', '2024-05-11 08:30:00', 90, 10, '铁人屋', '18:30:', 1);
INSERT INTO `course` VALUES (2, '驭龙之术', '2024-05-09 09:00:00', 90, 20, '龙石岛', '/upload/7beefea4-b411-4872-baa6-702fe5c51a45.gif', 3);
INSERT INTO `course` VALUES (3, '牧马放羊周游世界', '2024-05-17 09:00:00', 90, 20, '呼伦贝尔', '/upload/d0c262a8-e5e5-4caa-bf17-b2af4a33660c.jpg', 2);
INSERT INTO `course` VALUES (5, '无敌风火轮', '2024-05-18 09:00:00', 90, 20, '十八盘', '/upload/14801826-8279-4b01-b02f-6ac056b41460.png', 4);
INSERT INTO `course` VALUES (6, '驭龙之术', '2024-05-18 10:00:00', 90, 20, '龙石岛', '/upload/83eb4c6f-cb0b-4862-babd-f0bb2c2e5276.gif', 3);
INSERT INTO `course` VALUES (7, '撸铁', '2024-05-18 14:00:00', 90, 20, '铁人屋', '/upload/982a3306-0205-4412-95e0-ed4c8518ef9c.png', 1);
INSERT INTO `course` VALUES (8, 'lesmills莱美tone课程:痛并快乐着练起来吧', '2024-05-18 16:00:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (9, '铁人三项', '2024-05-18 08:00:00', 90, 20, '撸铁办公室', '/upload/ba9a197c-d506-4672-8eda-3e1e86cb0514.png', 1);
INSERT INTO `course` VALUES (10, '界王拳', '2024-05-19 08:00:00', 90, 20, '界王星', '/upload/4cc3fa7d-c5a3-42b5-a3d1-d5836082f9fe.png', 5);
INSERT INTO `course` VALUES (11, '自在极意功', '2024-05-19 10:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (12, '瑜伽', '2024-05-01 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (13, '瑜伽', '2024-05-02 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (14, '瑜伽', '2024-05-03 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (15, '瑜伽', '2024-05-04 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (16, '瑜伽', '2024-05-05 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (17, '瑜伽', '2024-05-06 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (18, '瑜伽', '2024-05-07 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (19, '瑜伽', '2024-05-08 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (20, '瑜伽', '2024-05-09 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (21, '瑜伽', '2024-05-10 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (22, '瑜伽', '2024-05-11 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (23, '瑜伽', '2024-05-12 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (24, '瑜伽', '2024-05-13 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (25, '瑜伽', '2024-05-14 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (26, '瑜伽', '2024-05-15 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (27, '瑜伽', '2024-05-16 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (28, '瑜伽', '2024-05-17 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (29, '瑜伽', '2024-05-18 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (30, '瑜伽', '2024-05-19 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (31, '瑜伽', '2024-05-20 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (32, '瑜伽', '2024-05-21 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (33, '瑜伽', '2024-05-22 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (34, '瑜伽', '2024-05-23 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (35, '瑜伽', '2024-05-24 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (37, '瑜伽', '2024-05-26 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (38, '瑜伽', '2024-05-27 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (39, '瑜伽', '2024-05-28 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (40, '瑜伽', '2024-05-29 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (41, '瑜伽', '2024-05-30 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (42, '瑜伽', '2024-05-31 18:30:00', 90, 20, '瑜伽室', '/upload/ac1ecd61-ea34-4a29-a308-edc2be4b599b.png', 2);
INSERT INTO `course` VALUES (43, '铁人三项', '2024-05-01 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (44, '铁人三项', '2024-05-02 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (45, '铁人三项', '2024-05-03 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (46, '铁人三项', '2024-05-04 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (47, '铁人三项', '2024-05-05 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (48, '铁人三项', '2024-05-06 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (49, '铁人三项', '2024-05-07 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (50, '铁人三项', '2024-05-08 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (51, '铁人三项', '2024-05-09 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (52, '铁人三项', '2024-05-10 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (53, '铁人三项', '2024-05-11 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (54, '铁人三项', '2024-05-12 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (55, '铁人三项', '2024-05-13 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (56, '铁人三项', '2024-05-14 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (57, '铁人三项', '2024-05-15 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (58, '铁人三项', '2024-05-16 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (59, '铁人三项', '2024-05-17 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (60, '铁人三项', '2024-05-18 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (61, '铁人三项', '2024-05-19 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (62, '铁人三项', '2024-05-20 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (63, '铁人三项', '2024-05-21 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (64, '铁人三项', '2024-05-22 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (65, '铁人三项', '2024-05-23 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (66, '铁人三项', '2024-05-24 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (67, '铁人三项', '2024-05-25 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (68, '铁人三项', '2024-05-26 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (69, '铁人三项', '2024-05-27 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (70, '铁人三项', '2024-05-28 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (71, '铁人三项', '2024-05-29 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (72, '铁人三项', '2024-05-30 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (73, '铁人三项', '2024-05-31 16:00:00', 90, 20, '铁人屋', '/upload/0273c47b-dc2b-4956-9e73-3f6f3922ab87.jpeg', 1);
INSERT INTO `course` VALUES (74, '自在极意功', '2024-05-01 20:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (75, '自在极意功', '2024-05-08 20:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (76, '自在极意功', '2024-05-15 20:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (77, '自在极意功', '2024-05-22 20:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (78, '自在极意功', '2024-05-29 20:00:00', 90, 20, '精神时光屋', '/upload/1b08d05a-9047-40fb-ada5-49ab3449944f.png', 5);
INSERT INTO `course` VALUES (79, '驭龙之术', '2024-05-01 08:00:00', 90, 20, '龙石岛', '/upload/83eb4c6f-cb0b-4862-babd-f0bb2c2e5276.gif', 3);
INSERT INTO `course` VALUES (80, '驭龙之术', '2024-05-08 08:00:00', 90, 20, '龙石岛', '/images/course_avatar/20250818193548-581.png', 3);
INSERT INTO `course` VALUES (81, '驭龙之术', '2024-05-15 08:00:00', 90, 20, '龙石岛', '/images/course_avatar/20250818193542-25.png', 3);
INSERT INTO `course` VALUES (82, '驭龙之术', '2024-05-22 08:00:00', 90, 20, '龙石岛', '/images/course_avatar/20250818193538-696.png', 3);
INSERT INTO `course` VALUES (83, '驭龙之术', '2024-05-29 08:00:00', 90, 20, '龙石岛', '/images/course_avatar/20250818193533-703.png', 3);
INSERT INTO `course` VALUES (84, '界王拳', '2024-05-03 08:00:00', 90, 20, '界王星', '/images/course_avatar/20250818193454-940.png', 5);
INSERT INTO `course` VALUES (85, '界王拳', '2024-05-10 08:00:00', 90, 20, '界王星', '/images/course_avatar/20250818193449-716.png', 5);
INSERT INTO `course` VALUES (86, '界王拳', '2024-05-17 08:00:00', 90, 20, '界王星', '/images/course_avatar/20250818193439-454.png', 5);
INSERT INTO `course` VALUES (87, '界王拳', '2024-05-24 08:00:00', 90, 20, '界王星', '/images/course_avatar/20250818193432-292.png', 5);
INSERT INTO `course` VALUES (88, '界王拳', '2024-05-31 00:00:00', 90, 20, '界王星', '/images/course_avatar/20250818193425-356.png', 5);
INSERT INTO `course` VALUES (89, '72变', '2025-08-13 00:00:00', 90, 20, '花果山', '/images/course_avatar/20250817221538-888.png', 5);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, '13855660000', 'ICy5YqxZB1uWSwcVLSNLcA==', '爷爷', '2024-03-13 00:00:00', '/images/member_avatar/20250818193636-22.png');
INSERT INTO `member` VALUES (2, '13855660001', 'ICy5YqxZB1uWSwcVLSNLcA==', '大娃', '2024-04-13 10:15:23', '/images/member_avatar/20250818193717-526.png');
INSERT INTO `member` VALUES (3, '13855660002', 'ICy5YqxZB1uWSwcVLSNLcA==', '二娃', '2024-05-03 10:15:22', '/images/member_avatar/20250818193745-639.png');
INSERT INTO `member` VALUES (4, '13855660003', 'ICy5YqxZB1uWSwcVLSNLcA==', '三娃', '2024-05-03 10:15:22', '/images/member_avatar/20250818193818-710.png');
INSERT INTO `member` VALUES (5, '13855660004', 'ICy5YqxZB1uWSwcVLSNLcA==', '四娃', '2024-05-13 10:15:22', '/images/member_avatar/20250818193847-11.png');
INSERT INTO `member` VALUES (6, '13855660005', 'ICy5YqxZB1uWSwcVLSNLcA==', '五娃', '2024-05-13 10:15:22', '/images/member_avatar/20250818193916-563.png');
INSERT INTO `member` VALUES (7, '13855660006', 'ICy5YqxZB1uWSwcVLSNLcA==', '六娃', '2024-05-13 10:15:22', '/images/member_avatar/20250818193944-599.png');
INSERT INTO `member` VALUES (8, '13855660007', 'ICy5YqxZB1uWSwcVLSNLcA==', '七娃', '2024-05-13 10:15:22', '/images/member_avatar/20250818194010-974.png');
INSERT INTO `member` VALUES (9, '13855660008', 'ICy5YqxZB1uWSwcVLSNLcA==', '小金钢', '2024-05-13 10:15:22', '/images/member_avatar/20250818194047-702.png');
INSERT INTO `member` VALUES (10, '13855660009', 'ICy5YqxZB1uWSwcVLSNLcA==', '穿山甲', '2024-05-13 10:15:22', '/images/member_avatar/20250818194113-908.png');
INSERT INTO `member` VALUES (11, '13877880001', 'ICy5YqxZB1uWSwcVLSNLcA==', '蛇精', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (12, '13877880002', 'ICy5YqxZB1uWSwcVLSNLcA==', '蝎子大王', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (13, '13877880003', 'ICy5YqxZB1uWSwcVLSNLcA==', '蛤蟆头领', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (14, '13877880005', 'ICy5YqxZB1uWSwcVLSNLcA==', '蜈蚣头领', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (15, '13877880006', 'ICy5YqxZB1uWSwcVLSNLcA==', '蝙蝠小兵', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (16, '13877880007', 'ICy5YqxZB1uWSwcVLSNLcA==', '乞酒蝗虫', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (17, '13877880008', 'ICy5YqxZB1uWSwcVLSNLcA==', '青蛇精', '2024-05-13 10:15:22', NULL);
INSERT INTO `member` VALUES (18, '1321531', 'ICy5YqxZB1uWSwcVLSNLcA==', 'asda', '2025-08-15 20:12:48', NULL);
INSERT INTO `member` VALUES (19, '12345679812', NULL, '老汪', '2025-08-17 22:18:34', NULL);

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员姓名',
  `course` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约课程',
  `coach` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程教练',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `length` int NULL DEFAULT NULL COMMENT '课程时长',
  `reserve_time` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `score` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评分',
  `recommend` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评语',
  `evaluate_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  `status` int NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES (3, '爷爷', '瑜伽', '小小', '2025-08-17 16:55:03', 90, '2025-08-29 00:00:00', '8', 'ok', '2025-08-17 22:36:06', 1);
INSERT INTO `reservation` VALUES (4, '爷爷', '驭龙之术', '龙妈', '2025-08-17 16:55:22', 90, '2025-08-20 00:00:00', NULL, NULL, NULL, 1);
INSERT INTO `reservation` VALUES (5, '大娃', '牧马放羊周游世界', '小小', '2025-08-17 17:25:30', 90, '2025-08-31 02:00:00', '10', 'good', '2025-08-17 22:35:53', 1);
INSERT INTO `reservation` VALUES (6, '二娃', '无敌风火轮', '魔鬼筋肉人', '2025-08-17 17:26:02', 90, '2025-08-29 00:00:00', NULL, NULL, NULL, 1);
INSERT INTO `reservation` VALUES (7, '三娃', '撸铁', '强森', '2025-08-17 17:26:19', 90, '2025-08-28 00:00:00', '2', '太差了下次不来了', '2025-08-18 19:48:24', 1);
INSERT INTO `reservation` VALUES (8, '四娃', '瑜伽', '小小', '2025-08-17 17:26:57', 90, '2025-09-25 00:00:00', '7', '总体还可以', '2025-08-18 19:48:12', 1);
INSERT INTO `reservation` VALUES (9, '穿山甲', '撸铁', '强森', '2025-08-18 19:46:04', 90, '2025-08-31 07:02:02', '5', '感觉不是很好', '2025-08-18 19:48:01', 1);
INSERT INTO `reservation` VALUES (10, '六娃', '牧马放羊周游世界', '小小', '2025-08-18 19:47:05', 90, '2025-11-20 11:02:02', '8', '还可以挺不错的', '2025-08-18 19:47:46', 1);
INSERT INTO `reservation` VALUES (11, '五娃', '无敌风火轮', '魔鬼筋肉人', '2025-08-19 11:27:39', 90, '2026-08-03 02:03:01', NULL, NULL, NULL, 1);
INSERT INTO `reservation` VALUES (12, '五娃', '撸铁', '强森', '2025-08-19 14:01:44', 90, '2025-08-20 00:00:00', NULL, NULL, NULL, 1);
INSERT INTO `reservation` VALUES (13, '二娃', '瑜伽', NULL, '2025-08-19 15:25:33', 90, '2025-08-30 00:00:00', NULL, NULL, NULL, 1);
INSERT INTO `reservation` VALUES (14, '乞酒蝗虫', '自在极意功', '孙悟空', '2025-08-19 19:22:28', 90, '2025-08-31 00:00:00', NULL, NULL, NULL, 0);
INSERT INTO `reservation` VALUES (15, '青蛇精', 'lesmills莱美tone课程:痛并快乐着练起来吧', '小小', '2025-08-19 19:22:43', 90, '2025-09-01 00:00:00', '1', '...', '2025-08-20 16:54:45', 1);
INSERT INTO `reservation` VALUES (16, '三娃', '自在极意功', NULL, '2025-08-20 17:01:55', 90, '2025-08-21 00:00:00', NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
