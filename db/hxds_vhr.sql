-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hxds_vhr
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
-- Current Database: `hxds_vhr`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `hxds_vhr` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hxds_vhr`;

--
-- Table structure for table `tb_voucher`
--

DROP TABLE IF EXISTS `tb_voucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher` (
  `id` bigint NOT NULL COMMENT '主键',
  `uuid` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'UUID',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '代金券标题',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述文字',
  `tag` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '代金券标签，例如新人专用',
  `total_quota` int NOT NULL COMMENT '代金券数量，如果是0，则是无限量',
  `take_count` int NOT NULL DEFAULT '0' COMMENT '实际领取数量',
  `used_count` int NOT NULL DEFAULT '0' COMMENT '已经使用的数量',
  `discount` decimal(10,2) NOT NULL COMMENT '代金券面额',
  `with_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '最少消费金额才能使用代金券',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '代金券赠送类型，如果是1则通用券，用户领取；如果是2，则是注册赠券',
  `limit_quota` smallint NOT NULL DEFAULT '1' COMMENT '用户领券限制数量，如果是0，则是不限制；默认是1，限领一张',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '代金券状态，如果是1则是正常可用；如果是2则是过期; 如果是3则是下架',
  `time_type` tinyint DEFAULT NULL COMMENT '有效时间限制，如果是1，则基于领取时间的有效天数days；如果是2，则start_time和end_time是优惠券有效期；',
  `days` smallint DEFAULT NULL COMMENT '基于领取时间的有效天数days',
  `start_time` datetime DEFAULT NULL COMMENT '代金券开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '代金券结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_uuid` (`uuid`) USING BTREE,
  KEY `idx_tag` (`tag`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_time_type` (`time_type`) USING BTREE,
  KEY `idx_limit` (`limit_quota`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='代金券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher`
--

LOCK TABLES `tb_voucher` WRITE;
/*!40000 ALTER TABLE `tb_voucher` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_customer`
--

DROP TABLE IF EXISTS `tb_voucher_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_customer` (
  `id` bigint NOT NULL COMMENT '主键',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `voucher_id` bigint NOT NULL COMMENT '代金券ID',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '使用状态, 如果是1则未使用；如果是2则已使用；如果是3则已过期；如果是4则已经下架；',
  `used_time` datetime DEFAULT NULL COMMENT '使用代金券的时间',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期截至时间',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_voucher_id` (`voucher_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='代金券领取表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_customer`
--

LOCK TABLES `tb_voucher_customer` WRITE;
/*!40000 ALTER TABLE `tb_voucher_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_customer` ENABLE KEYS */;
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
-- Dumping routines for database 'hxds_vhr'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-26 23:31:06
