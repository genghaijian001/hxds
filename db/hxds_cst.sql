-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hxds_cst
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
-- Current Database: `hxds_cst`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `hxds_cst` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hxds_cst`;

--
-- Table structure for table `tb_customer`
--

DROP TABLE IF EXISTS `tb_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `open_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '小程序授权字符串',
  `nickname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '客户昵称',
  `sex` char(1) NOT NULL DEFAULT '无',
  `photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像',
  `tel` char(11) DEFAULT '',
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NOT NULL COMMENT '1有效，2禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unq_open_id` (`open_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_customer`
--

LOCK TABLES `tb_customer` WRITE;
/*!40000 ALTER TABLE `tb_customer` DISABLE KEYS */;
INSERT INTO `tb_customer` VALUES (1,'dev_openid_cust1','testuser1','无','','',NULL,1,'2026-03-23 08:39:44'),(2,'dev_openid_cust2','testuser2','无','','',NULL,1,'2026-03-23 08:39:56'),(3,'dev_openid_testdirect2','测试','','','13800008888',NULL,1,'2026-05-15 01:08:43'),(4,'dev_openid_ctx99','测试','无','','13800007777',NULL,1,'2026-05-15 01:13:48'),(5,'dev_openid_flowtest','????','','','',NULL,1,'2026-05-15 01:36:26'),(6,'o8CUG7mC6GWHsLX41dQBCB1Bn3T4','哈喽 123','男','','19333213939',NULL,1,'2026-05-15 01:38:24');
/*!40000 ALTER TABLE `tb_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_customer_car`
--

DROP TABLE IF EXISTS `tb_customer_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_customer_car` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `car_plate` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '车牌号',
  `car_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '车型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_cid_plate` (`customer_id`,`car_plate`),
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_car_plate` (`car_plate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='客户车辆表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_customer_car`
--

LOCK TABLES `tb_customer_car` WRITE;
/*!40000 ALTER TABLE `tb_customer_car` DISABLE KEYS */;
INSERT INTO `tb_customer_car` VALUES (1,6,'京A12345','SUV');
/*!40000 ALTER TABLE `tb_customer_car` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_customer_fine`
--

DROP TABLE IF EXISTS `tb_customer_fine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_customer_fine` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `amount` decimal(10,2) NOT NULL COMMENT '罚款金额',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '备注信息',
  `status` tinyint NOT NULL COMMENT '1未缴纳，2已缴纳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='客户罚款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_customer_fine`
--

LOCK TABLES `tb_customer_fine` WRITE;
/*!40000 ALTER TABLE `tb_customer_fine` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_customer_fine` ENABLE KEYS */;
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
-- Dumping routines for database 'hxds_cst'
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
