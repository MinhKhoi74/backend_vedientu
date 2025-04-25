CREATE DATABASE  IF NOT EXISTS `vexedientu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `vexedientu`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: vexedientu
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buses`
--

DROP TABLE IF EXISTS `buses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(20) NOT NULL,
  `route` varchar(100) NOT NULL,
  `driver_id` bigint(20) DEFAULT NULL,
  `capacity` int(11) NOT NULL,
  `model` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_plate` (`license_plate`),
  KEY `driver_id` (`driver_id`),
  CONSTRAINT `buses_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buses`
--

LOCK TABLES `buses` WRITE;
/*!40000 ALTER TABLE `buses` DISABLE KEYS */;
INSERT INTO `buses` VALUES (23,'59B - 125.32','quận 8 - quận 9',59,32,'Honda'),(25,'59B - 123.54','Quận 3 - Quận 2',63,35,'Honda'),(26,'59B - 162.23','Quận 3 - Quận 2',45,35,'Honda'),(27,'59B - 123.21','Quận 3 - Quận 8',60,35,'Honda');
/*!40000 ALTER TABLE `buses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qr_codes`
--

DROP TABLE IF EXISTS `qr_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qr_codes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ticket_id` bigint(20) NOT NULL,
  `qr_code` longtext DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `qr_code` (`qr_code`) USING HASH,
  KEY `ticket_id` (`ticket_id`),
  CONSTRAINT `qr_codes_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qr_codes`
--

LOCK TABLES `qr_codes` WRITE;
/*!40000 ALTER TABLE `qr_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `qr_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ride_log`
--

DROP TABLE IF EXISTS `ride_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ride_time` datetime(6) DEFAULT NULL,
  `ticket_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `status` enum('VALID','INVALID') NOT NULL DEFAULT 'VALID',
  `driver_id` bigint(20) DEFAULT NULL,
  `bus_id` bigint(20) DEFAULT NULL,
  `route` varchar(255) DEFAULT NULL,
  `bus_code` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `bus_capacity` int(11) DEFAULT NULL,
  `bus_model` varchar(255) DEFAULT NULL,
  `bus_route` varchar(255) DEFAULT NULL,
  `trip_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcsqdip9vpoigotewvfsj73r6m` (`user_id`),
  KEY `FKdhnvpa80xjjuoddaevlr67tix` (`driver_id`),
  KEY `FK5lquh5pa7nv0fgolnk4x4eoyv` (`ticket_id`),
  KEY `fk_bus` (`bus_id`),
  KEY `FKhyo6epwp8ua5vlbbea2uayeq5` (`trip_id`),
  CONSTRAINT `FKcsqdip9vpoigotewvfsj73r6m` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKdhnvpa80xjjuoddaevlr67tix` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKhyo6epwp8ua5vlbbea2uayeq5` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`),
  CONSTRAINT `fk_bus` FOREIGN KEY (`bus_id`) REFERENCES `buses` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_log`
--

LOCK TABLES `ride_log` WRITE;
/*!40000 ALTER TABLE `ride_log` DISABLE KEYS */;
INSERT INTO `ride_log` VALUES (36,'2025-04-14 02:11:12.000000',74,46,'VALID',45,NULL,'d',NULL,NULL,NULL,NULL,NULL,NULL),(37,'2025-04-14 11:11:38.000000',73,58,'VALID',45,NULL,'d',NULL,NULL,NULL,NULL,NULL,NULL),(38,'2025-04-15 12:21:44.000000',73,58,'VALID',45,NULL,'d',NULL,NULL,NULL,NULL,NULL,NULL),(39,'2025-04-15 12:21:51.000000',74,46,'VALID',45,NULL,'d',NULL,NULL,NULL,NULL,NULL,NULL),(40,'2025-04-15 12:25:29.000000',73,58,'VALID',45,NULL,'d','123','khoi',NULL,NULL,NULL,NULL),(41,'2025-04-15 15:46:36.000000',73,58,'VALID',45,NULL,'d','123','khoi',NULL,NULL,NULL,NULL),(42,'2025-04-16 15:50:09.000000',76,46,'VALID',45,NULL,'d','123','quang',NULL,NULL,NULL,NULL),(43,'2025-04-16 15:52:23.000000',77,46,'VALID',45,NULL,'d','123','quang',NULL,NULL,NULL,NULL),(44,'2025-04-16 16:06:54.000000',76,46,'VALID',45,NULL,'d','123','quang',NULL,NULL,NULL,NULL),(45,'2025-04-17 16:59:43.000000',76,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,NULL),(46,'2025-04-17 17:06:45.000000',76,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,NULL),(47,'2025-04-19 13:26:32.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,NULL),(48,'2025-04-20 15:07:13.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,2),(49,'2025-04-20 15:15:28.000000',73,58,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','khoi',NULL,NULL,NULL,2),(50,'2025-04-20 15:15:33.000000',76,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,2),(51,'2025-04-20 20:16:03.000000',73,58,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','khoi',NULL,NULL,NULL,3),(52,'2025-04-21 00:08:37.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,7),(53,'2025-04-21 00:29:50.000000',81,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,8),(54,'2025-04-21 00:30:14.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,8),(55,'2025-04-21 00:30:24.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,8),(56,'2025-04-21 13:45:29.000000',83,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,9),(57,'2025-04-22 21:40:17.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,11),(58,'2025-04-22 21:40:54.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,11),(59,'2025-04-22 21:40:56.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,11),(60,'2025-04-22 21:40:58.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,11),(61,'2025-04-22 22:06:02.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,12),(62,'2025-04-22 22:06:21.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,12),(63,'2025-04-22 22:10:17.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,12),(64,'2025-04-22 22:54:12.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(65,'2025-04-22 22:54:44.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(66,'2025-04-22 22:55:24.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(67,'2025-04-22 22:56:21.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(68,'2025-04-22 22:57:42.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(69,'2025-04-22 23:07:26.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,14),(70,'2025-04-23 11:21:46.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(71,'2025-04-23 11:21:54.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(72,'2025-04-23 11:22:06.000000',83,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(73,'2025-04-23 11:22:21.000000',78,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(74,'2025-04-23 11:29:54.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(75,'2025-04-23 11:29:55.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(76,'2025-04-23 11:29:56.000000',80,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,17),(77,'2025-04-25 11:06:52.000000',97,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,18),(78,'2025-04-25 11:07:01.000000',97,46,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','quang',NULL,NULL,NULL,18),(79,'2025-04-25 11:07:44.000000',73,58,'VALID',59,23,'quận 8 - quận 9','59B - 125.32','khoi',NULL,NULL,NULL,18);
/*!40000 ALTER TABLE `ride_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `ticket_type` enum('SINGLE','VIP','MONTHLY') DEFAULT NULL,
  `remaining_rides` int(11) DEFAULT 0,
  `expiry_date` datetime(6) DEFAULT NULL,
  `purchased_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `price` double NOT NULL,
  `purchase_date` datetime(6) DEFAULT NULL,
  `qr_code` varchar(255) DEFAULT NULL,
  `is_hidden` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `qr_code` (`qr_code`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (73,58,'MONTHLY',2147483640,'2025-05-12 17:57:25.000000','2025-04-12 10:57:25',200000,'2025-04-12 17:57:25.000000','1f76011e-b5f3-4e4f-a32a-de7fa0b54d89',NULL),(76,46,'VIP',1,'2025-05-16 15:46:50.000000','2025-04-16 08:46:50',50000,'2025-04-16 15:46:50.000000','12379ac1-190f-4bb1-9f23-4f55b5761615',_binary ''),(77,46,'SINGLE',0,'2025-05-16 15:46:55.000000','2025-04-16 08:46:55',6000,'2025-04-16 15:46:55.000000','78746aa8-1cf8-489d-b947-50aa49d334c5',_binary ''),(78,46,'VIP',4,'2025-05-19 13:24:18.000000','2025-04-19 06:24:18',50000,'2025-04-19 13:24:18.000000','0fcfd4a4-2d4d-463b-ad4a-9f3f7179be84',_binary ''),(79,46,'SINGLE',1,'2025-05-21 00:27:29.000000','2025-04-20 17:27:29',6000,'2025-04-21 00:27:29.000000','754417d4-279f-4fa1-a070-e43f123beb21',NULL),(80,46,'MONTHLY',2147483629,'2025-05-21 00:27:58.000000','2025-04-20 17:27:58',200000,'2025-04-21 00:27:58.000000','4c778aed-fd44-49c6-964a-186ca3c57db7',NULL),(81,46,'SINGLE',0,'2025-05-21 00:28:14.000000','2025-04-20 17:28:14',6000,'2025-04-21 00:28:14.000000','f1453dd0-9943-47df-b6bc-00b083cf9f2d',_binary ''),(82,46,'VIP',10,'2025-05-21 13:31:11.000000','2025-04-21 06:31:11',50000,'2025-04-21 13:31:11.000000','cc7710d1-51a4-48af-a290-10b04e50a9a9',_binary ''),(83,46,'VIP',8,'2025-05-21 13:44:23.000000','2025-04-21 06:44:23',50000,'2025-04-21 13:44:23.000000','dd8c463e-ec00-4cbf-a7f8-9c4bb2cefa7a',_binary ''),(84,46,'SINGLE',1,'2025-05-24 15:15:52.000000','2025-04-24 08:15:52',6000,'2025-04-24 15:15:52.000000','d861e1bf-3cf6-41e6-b64e-4c7158254d9e',_binary '\0'),(85,46,'SINGLE',1,'2025-05-24 15:15:55.000000','2025-04-24 08:15:56',6000,'2025-04-24 15:15:55.000000','44e80ba4-ebff-48a2-aa56-a8f58de7df6e',_binary '\0'),(86,46,'SINGLE',1,'2025-05-24 15:15:58.000000','2025-04-24 08:15:58',6000,'2025-04-24 15:15:58.000000','a73beb09-c42a-44a3-94de-87dc3885fd4d',_binary '\0'),(87,46,'SINGLE',1,'2025-05-24 15:16:00.000000','2025-04-24 08:16:00',6000,'2025-04-24 15:16:00.000000','2a1825bf-8fa1-43de-8633-fb255518956b',_binary ''),(88,46,'SINGLE',1,'2025-05-24 15:16:02.000000','2025-04-24 08:16:02',6000,'2025-04-24 15:16:02.000000','5785c7be-e288-4b37-9ae7-3c536a4b3099',_binary ''),(89,46,'SINGLE',1,'2025-05-24 15:16:04.000000','2025-04-24 08:16:04',6000,'2025-04-24 15:16:04.000000','09591ada-f8c9-4440-9289-0aabc9da965f',_binary ''),(90,46,'SINGLE',1,'2025-05-24 15:16:06.000000','2025-04-24 08:16:06',6000,'2025-04-24 15:16:06.000000','5ca85910-f0e2-48ee-ad02-ed5bad8dc689',_binary ''),(91,46,'SINGLE',1,'2025-05-24 15:16:08.000000','2025-04-24 08:16:08',6000,'2025-04-24 15:16:08.000000','65e80e02-137e-4cb6-a49f-e1f50d5bbf9b',_binary ''),(92,46,'SINGLE',1,'2025-05-24 15:16:10.000000','2025-04-24 08:16:10',6000,'2025-04-24 15:16:10.000000','b0f3c5bc-2ac6-4f63-8b30-53c40836609e',_binary '\0'),(93,46,'SINGLE',1,'2025-05-24 15:16:12.000000','2025-04-24 08:16:12',6000,'2025-04-24 15:16:12.000000','e33c6d17-7338-4788-87b0-deaa8f7f4955',_binary '\0'),(94,46,'SINGLE',1,'2025-05-24 15:16:16.000000','2025-04-24 08:16:16',6000,'2025-04-24 15:16:16.000000','f366d876-2582-4a91-8a4e-1c87244825d3',_binary '\0'),(95,46,'MONTHLY',2147483647,'2025-05-25 03:15:51.000000','2025-04-24 20:15:51',200000,'2025-04-25 03:15:51.000000','fa4d881c-6536-49fd-b8c4-a14a456a055c',_binary '\0'),(96,46,'MONTHLY',2147483647,'2025-05-25 03:49:55.000000','2025-04-24 20:49:55',200000,'2025-04-25 03:49:55.000000','15bac274-332b-43a1-a004-0332616e7c50',_binary '\0'),(97,46,'MONTHLY',2147483645,'2025-05-25 03:51:18.000000','2025-04-24 20:51:18',200000,'2025-04-25 03:51:18.000000','e8c074a3-3ec5-4718-903c-8c6e63eda911',_binary '\0'),(98,46,'SINGLE',1,'2025-05-25 03:51:37.000000','2025-04-24 20:51:37',6000,'2025-04-25 03:51:37.000000','d079a1a9-4372-4e24-bc32-5069c08229cb',_binary '\0'),(99,46,'SINGLE',1,'2025-05-25 03:51:59.000000','2025-04-24 20:51:59',6000,'2025-04-25 03:51:59.000000','6e5fda28-3540-4bef-9bf8-acb9da991156',_binary ''),(100,46,'SINGLE',1,'2025-05-25 03:52:44.000000','2025-04-24 20:52:44',6000,'2025-04-25 03:52:44.000000','00824900-7d7a-4211-b2a9-b1b0e3637f45',_binary '\0'),(101,46,'SINGLE',1,'2025-05-25 03:53:07.000000','2025-04-24 20:53:07',6000,'2025-04-25 03:53:07.000000','da0bcf6e-8959-41d9-a6bf-f3b2d631ee2c',_binary '\0'),(102,46,'MONTHLY',2147483647,'2025-05-25 03:54:45.000000','2025-04-24 20:54:45',200000,'2025-04-25 03:54:45.000000','19d501ff-377f-4bb9-a5d4-59a1c7f04722',_binary '');
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `ticket_id` bigint(20) NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `payment_method` enum('CASH','CREDIT_CARD','MOMO','BANK_TRANSFER') NOT NULL,
  `status` enum('PENDING','COMPLETED','FAILED') DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `ticket_id` (`ticket_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (11,58,73,200000.00,'2025-04-12 10:57:25','CASH','COMPLETED'),(14,46,76,50000.00,'2025-04-16 08:46:50','CASH','COMPLETED'),(15,46,77,6000.00,'2025-04-16 08:46:55','CASH','COMPLETED'),(16,46,78,50000.00,'2025-04-19 06:24:18','CASH','COMPLETED'),(17,46,79,6000.00,'2025-04-20 17:27:29','CASH','COMPLETED'),(18,46,80,200000.00,'2025-04-20 17:27:58','CASH','COMPLETED'),(19,46,81,6000.00,'2025-04-20 17:28:14','CASH','COMPLETED'),(20,46,82,50000.00,'2025-04-21 06:31:11','CASH','COMPLETED'),(21,46,83,50000.00,'2025-04-21 06:44:23','CASH','COMPLETED'),(22,46,84,6000.00,'2025-04-24 08:15:52','CASH','COMPLETED'),(23,46,85,6000.00,'2025-04-24 08:15:56','CASH','COMPLETED'),(24,46,86,6000.00,'2025-04-24 08:15:58','CASH','COMPLETED'),(25,46,87,6000.00,'2025-04-24 08:16:00','CASH','COMPLETED'),(26,46,88,6000.00,'2025-04-24 08:16:02','CASH','COMPLETED'),(27,46,89,6000.00,'2025-04-24 08:16:04','CASH','COMPLETED'),(28,46,90,6000.00,'2025-04-24 08:16:06','CASH','COMPLETED'),(29,46,91,6000.00,'2025-04-24 08:16:08','CASH','COMPLETED'),(30,46,92,6000.00,'2025-04-24 08:16:10','CASH','COMPLETED'),(31,46,93,6000.00,'2025-04-24 08:16:12','CASH','COMPLETED'),(32,46,94,6000.00,'2025-04-24 08:16:16','CASH','COMPLETED'),(33,46,95,200000.00,'2025-04-24 20:15:51','CASH','COMPLETED'),(34,46,96,200000.00,'2025-04-24 20:49:56','CASH','COMPLETED'),(35,46,97,200000.00,'2025-04-24 20:51:18','CASH','COMPLETED'),(36,46,98,6000.00,'2025-04-24 20:51:37','CASH','COMPLETED'),(37,46,99,6000.00,'2025-04-24 20:51:59','CASH','COMPLETED'),(38,46,100,6000.00,'2025-04-24 20:52:44','CASH','COMPLETED'),(39,46,101,6000.00,'2025-04-24 20:53:07','CASH','COMPLETED'),(40,46,102,200000.00,'2025-04-24 20:54:45','CASH','COMPLETED');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trip`
--

DROP TABLE IF EXISTS `trip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_time` datetime(6) DEFAULT NULL,
  `route` varchar(255) NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `bus_id` bigint(20) NOT NULL,
  `driver_id` bigint(20) NOT NULL,
  `status` enum('OPEN','CLOSED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKawcec7mpfr7svr4gx2x8f2rgr` (`bus_id`),
  KEY `FKrm4qy9h6bw4vj5p41bpr0jf5m` (`driver_id`),
  CONSTRAINT `FKawcec7mpfr7svr4gx2x8f2rgr` FOREIGN KEY (`bus_id`) REFERENCES `buses` (`id`),
  CONSTRAINT `FKrm4qy9h6bw4vj5p41bpr0jf5m` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trip`
--

LOCK TABLES `trip` WRITE;
/*!40000 ALTER TABLE `trip` DISABLE KEYS */;
INSERT INTO `trip` VALUES (1,'2025-04-20 15:04:56.000000','quận 8 - quận 9','2025-04-20 15:04:32.000000',23,59,'CLOSED'),(2,'2025-04-20 20:15:09.000000','quận 8 - quận 9','2025-04-20 15:05:49.000000',23,59,'CLOSED'),(3,'2025-04-20 21:41:24.000000','quận 8 - quận 9','2025-04-20 20:15:20.000000',23,59,'CLOSED'),(5,'2025-04-20 21:49:02.000000','quận 8 - quận 9','2025-04-20 21:43:37.000000',23,59,'CLOSED'),(6,'2025-04-21 00:03:55.000000','quận 8 - quận 9','2025-04-20 21:49:12.000000',23,59,'CLOSED'),(7,'2025-04-21 00:09:16.000000','quận 8 - quận 9','2025-04-21 00:04:11.000000',23,59,'CLOSED'),(8,'2025-04-21 00:33:55.000000','quận 8 - quận 9','2025-04-21 00:29:44.000000',23,59,'CLOSED'),(9,'2025-04-21 13:45:46.000000','quận 8 - quận 9','2025-04-21 13:45:15.000000',23,59,'CLOSED'),(10,'2025-04-22 21:23:57.000000','quận 8 - quận 9','2025-04-22 21:23:52.000000',23,59,'CLOSED'),(11,'2025-04-22 21:41:27.000000','quận 8 - quận 9','2025-04-22 21:40:08.000000',23,59,'CLOSED'),(12,'2025-04-22 22:18:24.000000','quận 8 - quận 9','2025-04-22 22:05:57.000000',23,59,'CLOSED'),(13,'2025-04-22 22:23:00.000000','quận 8 - quận 9','2025-04-22 22:20:07.000000',23,59,'CLOSED'),(14,'2025-04-22 23:09:42.000000','quận 8 - quận 9','2025-04-22 22:23:13.000000',23,59,'CLOSED'),(15,NULL,'Quận 8 - Quận Gò Vấp','2025-04-22 22:37:17.000000',26,45,'OPEN'),(16,'2025-04-22 23:51:50.000000','quận 8 - quận 9','2025-04-22 23:51:31.000000',23,59,'CLOSED'),(17,'2025-04-23 11:30:10.000000','quận 8 - quận 9','2025-04-23 11:21:13.000000',23,59,'CLOSED'),(18,'2025-04-25 11:07:59.000000','quận 8 - quận 9','2025-04-25 11:06:43.000000',23,59,'CLOSED');
/*!40000 ALTER TABLE `trip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `role` enum('CUSTOMER','DRIVER','ADMIN') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (44,'Admin','admin@example.com','$2a$10$Puc1enh6VFPdqQGEM.rpOeslN1Y/2PiIxGQ6dctzq1ywAlA64/lkC','3456789','ADMIN','2025-03-17 08:08:47'),(45,'taixe','taixe@example.com','$2a$10$Puc1enh6VFPdqQGEM.rpOeslN1Y/2PiIxGQ6dctzq1ywAlA64/lkC','349','DRIVER','2025-03-17 14:34:02'),(46,'quang','quang@gmail.com','$2a$10$zWuB6J53aL9NA2x0fTCw.uGeJfTp0mpjA9C.KcUzMpchW23qQSUW.','123000','CUSTOMER','2025-03-18 06:39:26'),(47,'quân','quan@gmail.com','$2a$10$D8C2QzUIE3vsZH3//oWdauzEpXXGWgRUNIY1EB0o//idhOy/1OY8W','023123','CUSTOMER','2025-03-18 06:44:29'),(58,'khoi','khoi@gmail.com','$2a$10$UI6qF6xKWpvMe0H6j0Jvt.06BZzQNWwgxw0UrNIjbU/eKSknUmuYS','0215479','CUSTOMER','2025-04-11 12:37:30'),(59,'khôi','khoi1@example.com','$2a$10$CCOYmyWBjEodFgEhSN1/u.PHAxTBUITWj6ksCtGxbQg4uPFk7OlOW','03698521','DRIVER','2025-04-12 06:46:35'),(60,'tường','tuong@example.com','$2a$10$Uhf4M8GU1r50rAFpa0Ubw.Vd8HC6lgvSdOc5g7V4oQDPazUl8GBhO','0321564','DRIVER','2025-04-12 06:53:23'),(62,'phú','phu@gmail.com','$2a$10$9qdS9cug7XyQmOU71buGIet2tiB647bH3HaUgoeOWkQ4uNTvcpy0C','02315876','CUSTOMER','2025-04-17 07:44:00'),(63,'Phở','pho@gmail.com','$2a$10$71ASVNKCdWYuijdu9gFzBOQDHwuVX7W8jPLyJmOHEX.4rA5V910OO','0985421','DRIVER','2025-04-22 11:09:13'),(64,'Huy','Huy@gmail.com','$2a$10$jde3SFdXl0gExFk4d74zVOtaCnqg8S3ps54vI2.QIXDugUs9Eo8hm','0794669563','CUSTOMER','2025-04-25 04:00:47'),(65,'Hoa','Hoa@gmail.com','$2a$10$JvMTsUFj4z3TZysIZ4CNsudAxfo9FlI1XBg7Zep98asSMSUwwSW/C','0794669561','CUSTOMER','2025-04-25 04:04:52');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-25 11:12:37
