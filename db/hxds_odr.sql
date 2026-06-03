-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hxds_odr
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
-- Current Database: `hxds_odr`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `hxds_odr` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hxds_odr`;

--
-- Table structure for table `tb_order`
--

DROP TABLE IF EXISTS `tb_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单序列号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `start_place` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '起始地点',
  `start_place_location` json NOT NULL COMMENT '起始地点坐标信息',
  `end_place` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '结束地点',
  `end_place_location` json NOT NULL COMMENT '结束地点坐标信息',
  `expects_mileage` decimal(10,2) NOT NULL COMMENT '预估里程',
  `real_mileage` decimal(10,2) DEFAULT NULL COMMENT '实际里程',
  `return_mileage` decimal(10,2) DEFAULT NULL COMMENT '返程里程',
  `expects_fee` decimal(10,2) NOT NULL COMMENT '预估订单价格',
  `favour_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '顾客好处费',
  `incentive_fee` decimal(10,2) DEFAULT '0.00' COMMENT '系统奖励费',
  `real_fee` decimal(10,2) DEFAULT NULL COMMENT '实际订单价格',
  `driver_id` bigint DEFAULT NULL COMMENT '司机ID',
  `date` date NOT NULL COMMENT '订单日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `accept_time` datetime DEFAULT NULL COMMENT '司机接单时间',
  `arrive_time` datetime DEFAULT NULL COMMENT '司机到达时间',
  `start_time` datetime DEFAULT NULL COMMENT '代驾开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '代驾结束时间',
  `waiting_minute` smallint DEFAULT NULL COMMENT '代驾等时分钟数',
  `prepay_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微信预支付单ID',
  `pay_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微信支付单ID',
  `pay_time` datetime DEFAULT NULL COMMENT '微信付款时间',
  `charge_rule_id` bigint NOT NULL COMMENT '费用规则ID',
  `cancel_rule_id` bigint DEFAULT NULL COMMENT '订单取消规则ID',
  `car_plate` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '车牌号',
  `car_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '车型',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1等待接单，2已接单，3司机已到达，4开始代驾，5结束代驾，6未付款，7已付款，8订单已结束，9顾客撤单，10司机撤单，11事故关闭，12其他',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '订单备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_uuid` (`uuid`) USING BTREE,
  UNIQUE KEY `idx_prepay_id` (`prepay_id`) USING BTREE,
  UNIQUE KEY `idx_pay_id` (`pay_id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_date` (`date`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_charge_rule_id` (`charge_rule_id`) USING BTREE,
  KEY `idx_cancel_rule_id` (`cancel_rule_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order`
--

LOCK TABLES `tb_order` WRITE;
/*!40000 ALTER TABLE `tb_order` DISABLE KEYS */;
INSERT INTO `tb_order` VALUES (2,'e7e856ca7c3645ab8f15c39adfd323f6',6,'天安门','{\"latitude\": \"39.908823\", \"longitude\": \"116.397470\"}','河北邯郸第一中学','{\"latitude\": \"36.601769\", \"longitude\": \"114.490686\"}',448.48,NULL,NULL,1.00,0.00,0.00,NULL,4,'2026-05-15','2026-05-15 02:47:17','2026-05-15 02:48:00','2026-05-15 02:49:13',NULL,NULL,NULL,NULL,NULL,NULL,714601916034166785,NULL,'京A12345','SUV',9,NULL),(3,'f590a5e750d111f1a791b4f18c43db86',6,'天安门广场','{\"latitude\": \"39.908823\", \"longitude\": \"116.397470\"}','河北省邯郸市第一中学','{\"latitude\": \"36.614\", \"longitude\": \"114.541\"}',444.44,0.00,0.00,1.00,0.00,NULL,1.00,4,'2026-05-15','2026-05-15 03:19:42','2026-05-15 03:20:31','2026-05-15 19:53:02','2026-05-15 19:54:13','2026-05-16 08:07:23',1,'wx16082512747927557f6caa8f6a3bb20000','test_pay_id_001','2026-05-16 10:50:12',714601916034166785,NULL,'京A12345','SUV',7,NULL),(4,'5d80f707fdec4736af97e3fe326668d1',1,'天安门','{\"latitude\": \"39.908823\", \"longitude\": \"116.39747\"}','邯郸一中','{\"latitude\": \"36.6095\", \"longitude\": \"114.4785\"}',449.50,NULL,NULL,1.00,0.00,0.00,NULL,5,'2026-05-21','2026-05-21 13:37:10','2026-05-21 13:39:19','2026-05-21 14:50:57','2026-05-21 14:50:58','2026-05-21 14:53:52',0,NULL,NULL,NULL,714601916034166785,NULL,'京A12345','经济型',5,NULL),(6,'31889a9c00564c5c8809636344824bc8',6,'天安门城楼','{\"latitude\": \"39.908675\", \"longitude\": \"116.397505\"}','故宫-石狮','{\"latitude\": \"39.908054\", \"longitude\": \"116.39697\"}',2.29,0.10,0.00,1.00,0.00,NULL,1.00,6,'2026-05-22','2026-05-22 09:40:13','2026-05-22 09:46:40','2026-05-22 11:11:18','2026-05-22 16:21:21','2026-05-22 16:26:07',310,NULL,NULL,NULL,714601916034166785,NULL,'京A12345','SUV',9,NULL),(7,'159571a0a1c94fb38698438fb2853f64',6,'天安门城楼','{\"latitude\": \"39.908675\", \"longitude\": \"116.397505\"}','惠州杨学强公寓(西长安街分店)','{\"latitude\": \"39.90854\", \"longitude\": \"116.397512\"}',0.10,0.10,0.00,1.00,0.00,NULL,1.00,6,'2026-05-26','2026-05-26 19:00:55','2026-05-26 19:01:13','2026-05-26 19:01:32','2026-05-26 19:20:25','2026-05-26 19:21:12',18,'wx26192207324754096f1fe1b34f1b7e0000',NULL,NULL,714601916034166785,NULL,'京A12345','SUV',9,NULL),(9,'9ca7813d4374443f99c61a3af7eb0098',6,'天安门城楼','{\"latitude\": \"39.908675\", \"longitude\": \"116.397505\"}','惠州杨学强公寓(西长安街分店)','{\"latitude\": \"39.90854\", \"longitude\": \"116.397512\"}',0.10,0.10,0.00,1.00,0.00,NULL,1.00,6,'2026-05-26','2026-05-26 20:04:54','2026-05-26 20:05:57','2026-05-26 20:06:10','2026-05-26 20:06:15','2026-05-26 20:06:39',0,'wx262022174589907f6c10a520b7e8c60001','4200003133202605266501172066','2026-05-26 20:22:55',714601916034166785,NULL,'京A12345','SUV',7,NULL),(11,'edafa781267340bb8c1fdec922637257',6,'故宫-石狮','{\"latitude\": \"39.908054\", \"longitude\": \"116.39697\"}','洛阳雨田公寓(天安门分店)','{\"latitude\": \"39.90854\", \"longitude\": \"116.397512\"}',2.30,0.10,0.00,1.00,0.00,NULL,1.00,6,'2026-05-26','2026-05-26 21:41:57','2026-05-26 21:43:11','2026-05-26 21:43:27','2026-05-26 21:43:31','2026-05-26 21:43:39',0,'wx262249517550540e0a0f7cfbb194050001','4200003078202605262555208988','2026-05-26 22:50:35',714601916034166785,NULL,'京A12345','SUV',7,NULL);
/*!40000 ALTER TABLE `tb_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_appeal`
--

DROP TABLE IF EXISTS `tb_order_appeal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_appeal` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `detail` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '申诉内容',
  `result` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '处理结果',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '申诉状态，1申诉中，2申诉成功，3申诉失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='司机申诉表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_appeal`
--

LOCK TABLES `tb_order_appeal` WRITE;
/*!40000 ALTER TABLE `tb_order_appeal` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_order_appeal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_bill`
--

DROP TABLE IF EXISTS `tb_order_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_bill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `total` decimal(10,2) DEFAULT '0.00' COMMENT '总金额',
  `real_pay` decimal(10,2) DEFAULT '0.00' COMMENT '实付款金额',
  `mileage_fee` decimal(10,2) DEFAULT '0.00' COMMENT '里程费',
  `tel_fee` decimal(10,2) DEFAULT '0.00' COMMENT '通话费',
  `waiting_fee` decimal(10,2) DEFAULT '0.00' COMMENT '等时费用',
  `toll_fee` decimal(10,2) DEFAULT '0.00' COMMENT '路桥费',
  `parking_fee` decimal(10,2) DEFAULT '0.00' COMMENT '停车费',
  `other_fee` decimal(10,2) DEFAULT '0.00' COMMENT '其他费用',
  `return_fee` decimal(10,2) DEFAULT '0.00' COMMENT '返程费',
  `favour_fee` decimal(10,2) DEFAULT '0.00' COMMENT '顾客好处费',
  `incentive_fee` decimal(10,2) DEFAULT '0.00' COMMENT '系统奖励费',
  `voucher_fee` decimal(10,2) DEFAULT '0.00' COMMENT '代金券面额',
  `detail` json DEFAULT NULL COMMENT '详情',
  `base_mileage` smallint NOT NULL COMMENT '基础里程（公里）',
  `base_mileage_price` decimal(10,2) NOT NULL COMMENT '基础里程价格',
  `exceed_mileage_price` decimal(10,2) NOT NULL COMMENT '超出基础里程的价格',
  `base_minute` smallint NOT NULL COMMENT '基础分钟',
  `exceed_minute_price` decimal(10,2) NOT NULL COMMENT '超出基础分钟的价格',
  `base_return_mileage` smallint NOT NULL COMMENT '基础返程里程（公里）',
  `exceed_return_price` decimal(10,2) NOT NULL COMMENT '超出基础返程里程的价格',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单账单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_bill`
--

LOCK TABLES `tb_order_bill` WRITE;
/*!40000 ALTER TABLE `tb_order_bill` DISABLE KEYS */;
INSERT INTO `tb_order_bill` VALUES (2,2,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(3,3,1.00,1.00,0.00,0.00,0.00,NULL,NULL,NULL,0.00,0.00,NULL,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(4,4,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(6,6,1.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(7,7,1.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(9,9,1.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00),(11,11,1.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,NULL,0.00,NULL,8,85.00,3.50,10,1.00,8,1.00);
/*!40000 ALTER TABLE `tb_order_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_call`
--

DROP TABLE IF EXISTS `tb_order_call`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_call` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `minute` smallint NOT NULL COMMENT '通话分钟数',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `fee` decimal(10,2) NOT NULL COMMENT '通话费',
  `date` date NOT NULL COMMENT '日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_date` (`date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单通话记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_call`
--

LOCK TABLES `tb_order_call` WRITE;
/*!40000 ALTER TABLE `tb_order_call` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_order_call` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_comment`
--

DROP TABLE IF EXISTS `tb_order_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `customer_id` bigint NOT NULL COMMENT '顾客ID',
  `rate` tinyint NOT NULL COMMENT '评分，1星~5星',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '差评备注',
  `status` tinyint NOT NULL COMMENT '状态，1未申诉，2已申诉，3申诉失败，4申诉成功',
  `instance_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '申诉工作流ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_order_id` (`order_id`) USING BTREE,
  KEY `idx_driver_id` (`driver_id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_comment`
--

LOCK TABLES `tb_order_comment` WRITE;
/*!40000 ALTER TABLE `tb_order_comment` DISABLE KEYS */;
INSERT INTO `tb_order_comment` VALUES (1,3,4,6,5,'好评',1,NULL,'2026-05-16 11:07:12'),(2,7,6,6,5,'好评',1,NULL,'2026-05-26 19:22:53'),(3,9,6,6,5,'好评',1,NULL,'2026-05-26 20:23:04'),(4,11,6,6,5,'好评',1,NULL,'2026-05-26 22:50:46');
/*!40000 ALTER TABLE `tb_order_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_monitoring`
--

DROP TABLE IF EXISTS `tb_order_monitoring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_monitoring` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `recording` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '录音文档云存储网址',
  `content` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '谈话文字内容',
  `tag` json DEFAULT NULL COMMENT '谈话内容的标签，比如辱骂、挑逗、开房、包养等',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单监控表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_monitoring`
--

LOCK TABLES `tb_order_monitoring` WRITE;
/*!40000 ALTER TABLE `tb_order_monitoring` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_order_monitoring` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_profitsharing`
--

DROP TABLE IF EXISTS `tb_order_profitsharing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_profitsharing` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `amount_fee` decimal(10,2) NOT NULL COMMENT '总费用',
  `payment_rate` decimal(10,2) NOT NULL COMMENT '微信支付渠道费率',
  `payment_fee` decimal(10,2) NOT NULL COMMENT '微信支付渠道费',
  `tax_rate` decimal(10,2) NOT NULL COMMENT '为代驾司机代缴税率',
  `tax_fee` decimal(10,2) NOT NULL COMMENT '税率支出',
  `system_income` decimal(10,2) NOT NULL COMMENT '企业分账收入',
  `driver_income` decimal(10,2) NOT NULL COMMENT '司机分账收入',
  `status` tinyint NOT NULL COMMENT '分账状态，1未分账，2已分账',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_order_id` (`order_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_rule_id` (`rule_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单分账表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_profitsharing`
--

LOCK TABLES `tb_order_profitsharing` WRITE;
/*!40000 ALTER TABLE `tb_order_profitsharing` DISABLE KEYS */;
INSERT INTO `tb_order_profitsharing` VALUES (1,3,1111,1.00,0.78,0.00,0.00,0.00,0.00,1.00,1),(2,6,1111,1.00,0.78,0.00,0.00,0.00,0.00,1.00,1),(3,7,1111,1.00,0.78,0.00,0.00,0.00,0.00,1.00,1),(4,9,1111,1.00,0.78,0.00,0.00,0.00,0.00,1.00,1),(5,11,1111,1.00,0.78,0.00,0.00,0.00,0.00,1.00,1);
/*!40000 ALTER TABLE `tb_order_profitsharing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_order_violation`
--

DROP TABLE IF EXISTS `tb_order_violation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_violation` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `type` tinyint NOT NULL COMMENT '违纪类型，1服务，2驾驶',
  `reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '违纪原因',
  `status` tinyint NOT NULL COMMENT '状态，1未申诉，2已申诉，3申诉失败，4申诉成功',
  `instance_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '申诉工作流实例ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单违规表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_order_violation`
--

LOCK TABLES `tb_order_violation` WRITE;
/*!40000 ALTER TABLE `tb_order_violation` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_order_violation` ENABLE KEYS */;
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
-- Dumping routines for database 'hxds_odr'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-26 23:58:01
