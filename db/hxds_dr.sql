-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hxds_dr
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `hxds_dr`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `hxds_dr` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hxds_dr`;

--
-- Table structure for table `tb_driver`
--

DROP TABLE IF EXISTS `tb_driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_driver` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `open_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '小程序长期授权',
  `nickname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '昵称',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别',
  `photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像',
  `pid` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '身份证号码',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `tel` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `mail_address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '收信地址',
  `contact_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '应急联系人',
  `contact_tel` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '应急联系人电话',
  `real_auth` tinyint DEFAULT NULL COMMENT '1未认证，2已认证，3审核中',
  `idcard_address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '身份证地址',
  `idcard_expiration` date DEFAULT NULL COMMENT '身份证有效期',
  `idcard_front` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '身份证正面',
  `idcard_back` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '身份证背面',
  `idcard_holding` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手持身份证',
  `drcard_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '驾驶证类型',
  `drcard_expiration` date DEFAULT NULL COMMENT '驾驶证有效期',
  `drcard_issue_date` date DEFAULT NULL COMMENT '驾驶证初次领证日期',
  `drcard_front` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '驾驶证正面',
  `drcard_back` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '驾驶证背面',
  `drcard_holding` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手持驾驶证',
  `home` json DEFAULT NULL COMMENT '家庭地址，包含地址和定位，用于回家导航',
  `summary` json DEFAULT NULL COMMENT '摘要信息，level等级，totalOrder接单数，weekOrder周接单，weekComment周好评，appeal正在申诉量',
  `archive` tinyint(1) NOT NULL COMMENT '是否在腾讯云归档存放司机面部信息',
  `status` tinyint NOT NULL COMMENT '状态，1正常，2禁用，3.降低接单量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_open_id` (`open_id`) USING BTREE,
  UNIQUE KEY `unq_pid` (`pid`) USING BTREE,
  KEY `idx_real_auth` (`real_auth`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='代驾司机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_driver`
--

LOCK TABLES `tb_driver` WRITE;
/*!40000 ALTER TABLE `tb_driver` DISABLE KEYS */;
INSERT INTO `tb_driver` VALUES (1,'o8CUG7mC6GWHsLX41dQBCB1Bn3T4','微信用户',NULL,NULL,'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',0,1,'2025-08-19 16:06:50'),(2,'dev_openid_driver1','testdriver1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',1,1,'2026-03-23 08:36:29'),(3,'dev_openid_driver2','testdriver2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',0,1,'2026-03-23 08:39:56'),(4,'dev_openid_driver_test1','测试司机',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',0,1,'2026-05-15 01:53:57'),(5,'dev_openid_driver_driver1','测试司机driver1',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',0,1,'2026-05-21 08:36:46'),(6,'dev_openid_driver_1','测试司机1',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{\"level\": 0, \"appeal\": 0, \"weekOrder\": 0, \"totalOrder\": 0, \"weekComment\": 0}',0,1,'2026-05-22 08:20:45');
/*!40000 ALTER TABLE `tb_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_driver_fine`
--

DROP TABLE IF EXISTS `tb_driver_fine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_driver_fine` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `amount` decimal(10,2) NOT NULL COMMENT '罚款金额',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '备注信息',
  `status` tinyint NOT NULL COMMENT '1未缴纳，2已缴纳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机罚款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_driver_fine`
--

LOCK TABLES `tb_driver_fine` WRITE;
/*!40000 ALTER TABLE `tb_driver_fine` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_driver_fine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_driver_lockdown`
--

DROP TABLE IF EXISTS `tb_driver_lockdown`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_driver_lockdown` (
  `id` bigint NOT NULL COMMENT '主键',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `reason` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '原因',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `start_date` date NOT NULL COMMENT '起始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `days` smallint NOT NULL COMMENT '天数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_start_date` (`start_date`) USING BTREE,
  KEY `idx_end_date` (`end_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机禁闭表（禁止接单）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_driver_lockdown`
--

LOCK TABLES `tb_driver_lockdown` WRITE;
/*!40000 ALTER TABLE `tb_driver_lockdown` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_driver_lockdown` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_driver_recognition`
--

DROP TABLE IF EXISTS `tb_driver_recognition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_driver_recognition` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `date` date NOT NULL COMMENT '检测日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_complex_1` (`driver_id`,`date`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_date` (`date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=698343989501734913 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机人脸识别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_driver_recognition`
--

LOCK TABLES `tb_driver_recognition` WRITE;
/*!40000 ALTER TABLE `tb_driver_recognition` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_driver_recognition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_driver_settings`
--

DROP TABLE IF EXISTS `tb_driver_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_driver_settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `settings` json NOT NULL COMMENT '个人设置',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_driver_id` (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_driver_settings`
--

LOCK TABLES `tb_driver_settings` WRITE;
/*!40000 ALTER TABLE `tb_driver_settings` DISABLE KEYS */;
INSERT INTO `tb_driver_settings` VALUES (1,1,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}'),(2,2,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}'),(3,3,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}'),(4,4,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}'),(5,5,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}'),(6,6,'{\"autoAccept\": false, \"orientation\": \"\", \"listenService\": true, \"orderDistance\": 0, \"rangeDistance\": 5}');
/*!40000 ALTER TABLE `tb_driver_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_wallet`
--

DROP TABLE IF EXISTS `tb_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `balance` decimal(12,2) NOT NULL COMMENT '钱包金额',
  `password` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '支付密码，如果为空，不能支付，提示用户设置支付密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_driver_id` (`driver_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_wallet`
--

LOCK TABLES `tb_wallet` WRITE;
/*!40000 ALTER TABLE `tb_wallet` DISABLE KEYS */;
INSERT INTO `tb_wallet` VALUES (1,1,0.00,NULL),(2,2,0.00,NULL),(3,3,0.00,NULL),(4,4,0.00,NULL),(5,5,0.00,NULL),(6,6,0.00,NULL);
/*!40000 ALTER TABLE `tb_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_wallet_income`
--

DROP TABLE IF EXISTS `tb_wallet_income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_wallet_income` (
  `id` bigint NOT NULL COMMENT '主键',
  `uuid` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'uuid字符串',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `amount` decimal(7,2) NOT NULL COMMENT '金额',
  `type` tinyint NOT NULL COMMENT '1充值，2奖励，3补贴',
  `prepay_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '预支付订单ID',
  `status` tinyint NOT NULL COMMENT '1未支付，2已支付，3已到账',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_prepay_id` (`prepay_id`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='钱包收入表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_wallet_income`
--

LOCK TABLES `tb_wallet_income` WRITE;
/*!40000 ALTER TABLE `tb_wallet_income` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_wallet_income` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_wallet_payment`
--

DROP TABLE IF EXISTS `tb_wallet_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_wallet_payment` (
  `id` bigint NOT NULL COMMENT '主键',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `amount` decimal(7,2) NOT NULL COMMENT '支付金额',
  `type` tinyint NOT NULL COMMENT '1话费，2罚款，3抽奖，4缴费，5其他',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机钱包付款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_wallet_payment`
--

LOCK TABLES `tb_wallet_payment` WRITE;
/*!40000 ALTER TABLE `tb_wallet_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_wallet_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AT transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'hxds_dr'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-26 23:31:05
