-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: project_4_db
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `booking_details`
--

DROP TABLE IF EXISTS `booking_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_details` (
  `booking_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `check_in` datetime(6) DEFAULT NULL,
  `check_out` datetime(6) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `price` double DEFAULT NULL,
  `special_requests` varchar(1000) DEFAULT NULL,
  `status` enum('CANCELED','COMPLETED','CONFIRMED','PENDING') DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `booking_id` bigint DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`booking_detail_id`),
  KEY `FKkbcan6ybv86uappnh0qtdmvas` (`booking_id`),
  KEY `FK8pcpg1hs4kqwqcvq79i1esg5` (`room_id`),
  CONSTRAINT `FK8pcpg1hs4kqwqcvq79i1esg5` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `FKkbcan6ybv86uappnh0qtdmvas` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_details`
--

LOCK TABLES `booking_details` WRITE;
/*!40000 ALTER TABLE `booking_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deposit` double DEFAULT NULL,
  `status` enum('COMPLETED','DEPOSITED','FAILED','PAID','PENDING') DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `contact_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `message` varchar(1000) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','IN_PROGRESS','NEW') DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `FKna8bddygr3l3kq1imghgcskt8` (`user_id`),
  CONSTRAINT `FKna8bddygr3l3kq1imghgcskt8` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `image_id` bigint NOT NULL AUTO_INCREMENT,
  `image_file_name` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `reference_id` bigint DEFAULT NULL,
  `upload_date` date DEFAULT NULL,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (1,'8a3ca136-b0b9-43ba-994d-c57d994e35ae_Room_31-scaled.jpg','.\\upload_images\\8a3ca136-b0b9-43ba-994d-c57d994e35ae_Room_31-scaled.jpg','ROOM',1,'2024-11-17'),(2,'b6e6520e-27cf-445f-935d-75cb2e719c5e_Room_32-scaled.jpg','.\\upload_images\\b6e6520e-27cf-445f-935d-75cb2e719c5e_Room_32-scaled.jpg','ROOM',1,'2024-11-17'),(3,'f5c2488d-4fbb-4151-8330-ece12e3fe64b_Room_33-scaled.jpg','.\\upload_images\\f5c2488d-4fbb-4151-8330-ece12e3fe64b_Room_33-scaled.jpg','ROOM',1,'2024-11-17'),(4,'d14cac60-c43e-4802-a876-6249b332795a_Rooms_30-scaled.jpg','.\\upload_images\\d14cac60-c43e-4802-a876-6249b332795a_Rooms_30-scaled.jpg','ROOM',1,'2024-11-17'),(5,'1e0d9a98-8c5e-42d5-b013-75f6b86b7852_154280732.jpg','.\\upload_images\\1e0d9a98-8c5e-42d5-b013-75f6b86b7852_154280732.jpg','ROOM',2,'2024-11-17'),(6,'a8326845-3f82-4122-ba2b-8a85f0e4e36c_155202026.jpg','.\\upload_images\\a8326845-3f82-4122-ba2b-8a85f0e4e36c_155202026.jpg','ROOM',2,'2024-11-17'),(7,'b584e446-da8d-4c54-bf5e-215cba322b1b_164272906.jpg','.\\upload_images\\b584e446-da8d-4c54-bf5e-215cba322b1b_164272906.jpg','ROOM',2,'2024-11-17'),(8,'ee967188-f87f-4a0c-a524-933461789a25_592000912.jpg','.\\upload_images\\ee967188-f87f-4a0c-a524-933461789a25_592000912.jpg','ROOM',2,'2024-11-17'),(9,'05e342ca-a185-4435-a5fd-1da96ace3692_514887179.jpg','.\\upload_images\\05e342ca-a185-4435-a5fd-1da96ace3692_514887179.jpg','ROOM',3,'2024-11-17'),(10,'b4169190-6218-46a1-a40e-ab4e4c4504cc_514939105.jpg','.\\upload_images\\b4169190-6218-46a1-a40e-ab4e4c4504cc_514939105.jpg','ROOM',3,'2024-11-17'),(11,'09117281-d131-4651-b648-9d6d9c2f4efc_514939140.jpg','.\\upload_images\\09117281-d131-4651-b648-9d6d9c2f4efc_514939140.jpg','ROOM',3,'2024-11-17'),(12,'5b0841ae-3b11-4ec6-8177-3ac0fc0893db_514939197.jpg','.\\upload_images\\5b0841ae-3b11-4ec6-8177-3ac0fc0893db_514939197.jpg','ROOM',3,'2024-11-17'),(13,'15607114-f705-45fb-a35c-a57e6c971b98_601828098.jpg','.\\upload_images\\15607114-f705-45fb-a35c-a57e6c971b98_601828098.jpg','ROOM',4,'2024-11-17'),(14,'0afa9362-c54f-4d24-a925-9c287b968226_601828100.jpg','.\\upload_images\\0afa9362-c54f-4d24-a925-9c287b968226_601828100.jpg','ROOM',4,'2024-11-17'),(15,'48e28565-0811-4dea-966d-4142bcf02258_601828103.jpg','.\\upload_images\\48e28565-0811-4dea-966d-4142bcf02258_601828103.jpg','ROOM',4,'2024-11-17'),(16,'c80a4d9e-5295-4f2e-b717-94751d556fa6_601828104.jpg','.\\upload_images\\c80a4d9e-5295-4f2e-b717-94751d556fa6_601828104.jpg','ROOM',4,'2024-11-17'),(17,'8981e654-1962-45f7-b73d-cc2aefbb15ce_z4285810648780_4fd7c64cccbc8e2c096445c023faf13f.jpg','.\\upload_images\\8981e654-1962-45f7-b73d-cc2aefbb15ce_z4285810648780_4fd7c64cccbc8e2c096445c023faf13f.jpg','ROOM',5,'2024-11-17'),(18,'0329f968-f422-44c3-8a26-504e0952b632_z4285810650920_2c47d0c9e65e8bbb1b23f7f8aea00d9a.jpg','.\\upload_images\\0329f968-f422-44c3-8a26-504e0952b632_z4285810650920_2c47d0c9e65e8bbb1b23f7f8aea00d9a.jpg','ROOM',5,'2024-11-17'),(19,'6d5a986d-831f-4c9c-9c98-241e798beba7_z4285810674619_5e060452b26f7d90cc8f67cccdc6b014.jpg','.\\upload_images\\6d5a986d-831f-4c9c-9c98-241e798beba7_z4285810674619_5e060452b26f7d90cc8f67cccdc6b014.jpg','ROOM',5,'2024-11-17'),(20,'ee9f7bee-4765-4701-88ed-6b161527b442_z4285810696769_564a7fec7bd12ad33518c13eb621431c.jpg','.\\upload_images\\ee9f7bee-4765-4701-88ed-6b161527b442_z4285810696769_564a7fec7bd12ad33518c13eb621431c.jpg','ROOM',5,'2024-11-17'),(21,'abe33143-a5f2-445e-93ea-18951d45adfc_605896661.jpg','.\\upload_images\\abe33143-a5f2-445e-93ea-18951d45adfc_605896661.jpg','ROOM',6,'2024-11-17'),(22,'94525f3e-4266-41b5-9d5a-0ba387877c00_605896750.jpg','.\\upload_images\\94525f3e-4266-41b5-9d5a-0ba387877c00_605896750.jpg','ROOM',6,'2024-11-17'),(23,'0a4eacb7-59f3-4578-9dba-91b8018999c3_605896780.jpg','.\\upload_images\\0a4eacb7-59f3-4578-9dba-91b8018999c3_605896780.jpg','ROOM',6,'2024-11-17'),(24,'cd15a4d7-178b-454b-951f-f1af186f265e_605896784.jpg','.\\upload_images\\cd15a4d7-178b-454b-951f-f1af186f265e_605896784.jpg','ROOM',6,'2024-11-17'),(25,'e4fe0a78-8fb9-4aff-9928-47ca07a2f66c_224467334.jpg','.\\upload_images\\e4fe0a78-8fb9-4aff-9928-47ca07a2f66c_224467334.jpg','ROOM',7,'2024-11-17'),(26,'9457a70e-bfed-4c15-bd5f-5711fdf1b435_501731951.jpg','.\\upload_images\\9457a70e-bfed-4c15-bd5f-5711fdf1b435_501731951.jpg','ROOM',7,'2024-11-17'),(27,'b554f756-483c-4b5c-b5ef-44d4f07f1266_501732588.jpg','.\\upload_images\\b554f756-483c-4b5c-b5ef-44d4f07f1266_501732588.jpg','ROOM',7,'2024-11-17'),(28,'3a1443e4-c083-472b-8e01-b602d4dcb3c5_501734303.jpg','.\\upload_images\\3a1443e4-c083-472b-8e01-b602d4dcb3c5_501734303.jpg','ROOM',7,'2024-11-17'),(29,'3262b19b-21d7-4712-a649-084678de6cff_415854699.jpg','.\\upload_images\\3262b19b-21d7-4712-a649-084678de6cff_415854699.jpg','ROOM',8,'2024-11-17'),(30,'172b9995-4dcc-4bcf-b3cc-2d3d81e10ad9_415854705.jpg','.\\upload_images\\172b9995-4dcc-4bcf-b3cc-2d3d81e10ad9_415854705.jpg','ROOM',8,'2024-11-17'),(31,'72a85a0f-9983-40dc-aa3b-f7a4ec858ec5_415854721.jpg','.\\upload_images\\72a85a0f-9983-40dc-aa3b-f7a4ec858ec5_415854721.jpg','ROOM',8,'2024-11-17'),(32,'e1fe1183-d36e-4f75-aa52-71c7255d4bff_415854722.jpg','.\\upload_images\\e1fe1183-d36e-4f75-aa52-71c7255d4bff_415854722.jpg','ROOM',8,'2024-11-17'),(33,'6f3672dd-da67-4a71-b0e6-e72a2689e696_Rooms_28-scaled.jpg','.\\upload_images\\6f3672dd-da67-4a71-b0e6-e72a2689e696_Rooms_28-scaled.jpg','ROOM',9,'2024-11-17'),(34,'eb432f00-0c6f-4c2d-8a92-ddf5e5d9219e_Rooms_29-scaled.jpg','.\\upload_images\\eb432f00-0c6f-4c2d-8a92-ddf5e5d9219e_Rooms_29-scaled.jpg','ROOM',9,'2024-11-17'),(35,'88521dfd-3a0a-474d-833a-ef6227168f1f_Rooms_30-scaled.jpg','.\\upload_images\\88521dfd-3a0a-474d-833a-ef6227168f1f_Rooms_30-scaled.jpg','ROOM',9,'2024-11-17'),(36,'2b22e3ce-9f95-4e6b-bf9d-664238199feb_Rooms_31-scaled.jpg','.\\upload_images\\2b22e3ce-9f95-4e6b-bf9d-664238199feb_Rooms_31-scaled.jpg','ROOM',9,'2024-11-17'),(37,'651f49d1-e695-47e8-a7a9-8c0d7e088cff_Room_19-scaled.jpg','.\\upload_images\\651f49d1-e695-47e8-a7a9-8c0d7e088cff_Room_19-scaled.jpg','ROOM',10,'2024-11-17'),(38,'d6bda7e3-eaf5-4f5a-a772-7b6abeee986d_Room_20-scaled.jpg','.\\upload_images\\d6bda7e3-eaf5-4f5a-a772-7b6abeee986d_Room_20-scaled.jpg','ROOM',10,'2024-11-17'),(39,'2eba0ec1-bf19-4d08-93bf-ff0f6ee41de8_Room_21-scaled.jpg','.\\upload_images\\2eba0ec1-bf19-4d08-93bf-ff0f6ee41de8_Room_21-scaled.jpg','ROOM',10,'2024-11-17'),(40,'f87607ba-685b-42eb-b607-eb22b69d8f31_Room_24-scaled.jpg','.\\upload_images\\f87607ba-685b-42eb-b607-eb22b69d8f31_Room_24-scaled.jpg','ROOM',10,'2024-11-17'),(41,'15da4557-7165-4980-8878-53adeb715211_Room_41-scaled.jpg','.\\upload_images\\15da4557-7165-4980-8878-53adeb715211_Room_41-scaled.jpg','ROOM',11,'2024-11-17'),(42,'22d5df65-803f-4cb8-b2e1-95df3d978558_Room_43-scaled.jpg','.\\upload_images\\22d5df65-803f-4cb8-b2e1-95df3d978558_Room_43-scaled.jpg','ROOM',11,'2024-11-17'),(43,'971178a5-e13b-4b1c-9214-8fdc9fec6ac7_Room_45-scaled.jpg','.\\upload_images\\971178a5-e13b-4b1c-9214-8fdc9fec6ac7_Room_45-scaled.jpg','ROOM',11,'2024-11-17'),(44,'8171bfcd-5293-4aa1-8573-e9a06d0db757_Rooms_35-scaled.jpg','.\\upload_images\\8171bfcd-5293-4aa1-8573-e9a06d0db757_Rooms_35-scaled.jpg','ROOM',11,'2024-11-17'),(45,'ad51953d-4500-4db5-bd41-65e403ccf2a3_598528241.jpg','.\\upload_images\\ad51953d-4500-4db5-bd41-65e403ccf2a3_598528241.jpg','ROOM',12,'2024-11-17'),(46,'70c184d4-3219-4f2b-96ff-47dfc5b74536_598528249.jpg','.\\upload_images\\70c184d4-3219-4f2b-96ff-47dfc5b74536_598528249.jpg','ROOM',12,'2024-11-17'),(47,'11ad8275-ee12-4f9b-98bd-7855faae99c2_598529290.jpg','.\\upload_images\\11ad8275-ee12-4f9b-98bd-7855faae99c2_598529290.jpg','ROOM',12,'2024-11-17'),(48,'e7e975f9-f1d9-4de5-a91a-30b1ba8422e0_598529296.jpg','.\\upload_images\\e7e975f9-f1d9-4de5-a91a-30b1ba8422e0_598529296.jpg','ROOM',12,'2024-11-17'),(49,'c21efa6b-7667-449a-97d3-f827f5d64200_Room_34-scaled.jpg','.\\upload_images\\c21efa6b-7667-449a-97d3-f827f5d64200_Room_34-scaled.jpg','ROOM',13,'2024-11-17'),(50,'ec146cf5-c83e-498f-ae36-2fddafb18a86_Room_36-scaled.jpg','.\\upload_images\\ec146cf5-c83e-498f-ae36-2fddafb18a86_Room_36-scaled.jpg','ROOM',13,'2024-11-17'),(51,'b3281156-8f65-4d31-b342-f0abd9962874_Room_38-scaled.jpg','.\\upload_images\\b3281156-8f65-4d31-b342-f0abd9962874_Room_38-scaled.jpg','ROOM',13,'2024-11-17'),(52,'2c59dfab-205e-461b-988c-bef3901f582c_Rooms_31-scaled.jpg','.\\upload_images\\2c59dfab-205e-461b-988c-bef3901f582c_Rooms_31-scaled.jpg','ROOM',13,'2024-11-17'),(53,'17dd3c08-49ab-41b6-8901-a6e867523d8e_467531336.jpg','.\\upload_images\\17dd3c08-49ab-41b6-8901-a6e867523d8e_467531336.jpg','ROOM',14,'2024-11-17'),(54,'5cfaf2fd-a0af-46ef-b68e-52d602d7bc6e_467531397.jpg','.\\upload_images\\5cfaf2fd-a0af-46ef-b68e-52d602d7bc6e_467531397.jpg','ROOM',14,'2024-11-17'),(55,'f28331a5-75c4-4a40-a9d7-07bdd7b3018a_467531402.jpg','.\\upload_images\\f28331a5-75c4-4a40-a9d7-07bdd7b3018a_467531402.jpg','ROOM',14,'2024-11-17'),(56,'c5b281cb-ef96-4522-9ac7-7e278e40f0c1_467531418.jpg','.\\upload_images\\c5b281cb-ef96-4522-9ac7-7e278e40f0c1_467531418.jpg','ROOM',14,'2024-11-17'),(57,'4bf17aa0-169a-491a-8023-6c911d83496b_236348363.jpg','.\\upload_images\\4bf17aa0-169a-491a-8023-6c911d83496b_236348363.jpg','ROOM',15,'2024-11-17'),(58,'4356a1fb-a6fb-4c8a-9d02-a49560661bfd_236348394.jpg','.\\upload_images\\4356a1fb-a6fb-4c8a-9d02-a49560661bfd_236348394.jpg','ROOM',15,'2024-11-17'),(59,'baf2691e-46cd-4793-945f-7910104ac5a3_236348400.jpg','.\\upload_images\\baf2691e-46cd-4793-945f-7910104ac5a3_236348400.jpg','ROOM',15,'2024-11-17'),(60,'9b99b207-09bb-40ab-896a-4635864f4d93_437770803.jpg','.\\upload_images\\9b99b207-09bb-40ab-896a-4635864f4d93_437770803.jpg','ROOM',15,'2024-11-17'),(61,'84bcbb99-5e25-4dc1-bbea-8d07235fd4d8_541309770.jpg','.\\upload_images\\84bcbb99-5e25-4dc1-bbea-8d07235fd4d8_541309770.jpg','ROOM',16,'2024-11-17'),(62,'47e4b6fb-08ce-41af-9353-4522cec3cd9e_541309775.jpg','.\\upload_images\\47e4b6fb-08ce-41af-9353-4522cec3cd9e_541309775.jpg','ROOM',16,'2024-11-17'),(63,'5187b93c-39d3-4683-be20-b1868ac8f230_541310165.jpg','.\\upload_images\\5187b93c-39d3-4683-be20-b1868ac8f230_541310165.jpg','ROOM',16,'2024-11-17'),(64,'90bb27c9-ef1b-4a4d-abbd-46ac6b892351_541310441.jpg','.\\upload_images\\90bb27c9-ef1b-4a4d-abbd-46ac6b892351_541310441.jpg','ROOM',16,'2024-11-17'),(65,'894a1724-0769-41a0-a28d-2f96b5b42d17_eat.jpg','.\\upload_images\\894a1724-0769-41a0-a28d-2f96b5b42d17_eat.jpg','PROVISION',1,'2024-11-17'),(66,'90f62d6c-abe9-480c-b16d-77593c6704d6_','.\\upload_images\\90f62d6c-abe9-480c-b16d-77593c6704d6_','PROVISION',2,'2024-11-18'),(67,'74c628db-c8bc-40d3-aa83-1926feeb3495_gym.jpg','.\\upload_images\\74c628db-c8bc-40d3-aa83-1926feeb3495_gym.jpg','PROVISION',3,'2024-11-18'),(68,'294baccf-29e6-4ff2-9c80-a6cdd6a2ec92_be-boi-khach-san.jpg','.\\upload_images\\294baccf-29e6-4ff2-9c80-a6cdd6a2ec92_be-boi-khach-san.jpg','PROVISION',4,'2024-11-18'),(69,'84e4c12b-2e26-4670-bbfb-5a6e7e8b71ee_spa1.jpg','.\\upload_images\\84e4c12b-2e26-4670-bbfb-5a6e7e8b71ee_spa1.jpg','PROVISION',5,'2024-11-18'),(70,'c12f721e-6651-49f7-b7d5-bf05ceb2caaa_xe-dua-don-san-bay.jpg','.\\upload_images\\c12f721e-6651-49f7-b7d5-bf05ceb2caaa_xe-dua-don-san-bay.jpg','PROVISION',6,'2024-11-18'),(71,'a2b620bb-f89c-4bf2-b4d5-7503a553fdd0_phong_hop.jpg','.\\upload_images\\a2b620bb-f89c-4bf2-b4d5-7503a553fdd0_phong_hop.jpg','PROVISION',7,'2024-11-18');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `paid_amount` double DEFAULT NULL,
  `status` enum('DEPOSITED','PAID') DEFAULT NULL,
  `booking_id` bigint DEFAULT NULL,
  `payment_date` date DEFAULT NULL,
  `payment_reference` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `UKnuscjm6x127hkb15kcb8n56wo` (`booking_id`),
  CONSTRAINT `FKc52o2b1jkxttngufqp3t7jr3h` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provisions`
--

DROP TABLE IF EXISTS `provisions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provisions` (
  `provision_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `provision_name` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`provision_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provisions`
--

LOCK TABLES `provisions` WRITE;
/*!40000 ALTER TABLE `provisions` DISABLE KEYS */;
INSERT INTO `provisions` VALUES (1,'Dining services are a key feature offered by many accommodations on Booking.com, enhancing the guest experience by providing convenient and high-quality meal options. Guests can enjoy a range of dining choices, including complimentary or paid breakfast, often served as a buffet with diverse dishes or à la carte options. Many properties also offer lunch and dinner services, featuring local and international cuisines to cater to various tastes. For those with specific dietary requirements, such as vegetarian, gluten-free, or vegan diets, tailored menu options are often available upon request. Dining can be enjoyed in on-site restaurants, through room service, or even at outdoor dining areas, depending on the property\'s facilities. These services provide a seamless and enjoyable culinary experience during the stay.',10,'Food services','ACTIVE'),(2,'Laundry services are a convenient and practical offering available at many accommodations on Booking.com, designed to meet the needs of travelers who require clean and pressed clothing during their stay. These services typically include washing, drying, and ironing, with options for same-day service in some properties. For guests with delicate or high-end garments, dry cleaning is often available as well. Pricing is usually based on the number of items or by weight, and some accommodations provide laundry facilities for self-service, equipped with washing machines and dryers. This service ensures that guests can maintain their comfort and appearance effortlessly while traveling.',5,'Laudry','ACTIVE'),(3,'Gym services are a popular amenity offered by many accommodations on Booking.com, providing guests with a convenient way to maintain their fitness routine while traveling. These facilities typically include a variety of equipment, such as treadmills, stationary bikes, weight machines, and free weights, catering to different workout preferences. Some accommodations also offer additional features like yoga mats, personal training sessions, or group fitness classes for an enhanced experience. Access to the gym is often complimentary for guests or available for a nominal fee. With clean, well-maintained spaces and flexible operating hours, gym services ensure guests can stay active and healthy throughout their stay.',5,'Gym','ACTIVE'),(4,'Swimming pool services are a highly sought-after feature provided by many accommodations on Booking.com, offering guests a relaxing and enjoyable way to unwind during their stay. These facilities often include indoor or outdoor pools, with some properties featuring infinity pools, rooftop pools, or heated options for added luxury. Additional amenities may include sun loungers, poolside bars, and separate kiddie pools for families traveling with children. Many accommodations also organize poolside activities, such as aqua aerobics or evening events, enhancing the guest experience. Access to the swimming pool is typically included in the booking price, making it a popular choice for leisure and relaxation.',5,'Swimming pool','ACTIVE'),(5,'Spa and massage services are luxurious amenities offered by many accommodations on Booking.com, designed to provide guests with relaxation and rejuvenation during their stay. These services often include full-body massages, foot massages, hot stone therapy, aromatherapy, and facial treatments, catering to a variety of wellness needs. Many spas also feature additional facilities such as steam rooms, saunas, and jacuzzis to enhance the overall experience. Expert therapists and high-quality products ensure a premium level of care and comfort. Spa and massage services are usually available for an additional fee, with some packages offering discounts for guests. These amenities create a serene and indulgent retreat, perfect for unwinding after a busy day of travel or exploration.',50,'Spa','ACTIVE'),(6,'Airport transfer services are a convenient and stress-free option offered by many accommodations on Booking.com, designed to ensure smooth transportation to and from the airport. These services typically include private cars, shared shuttles, or luxury vehicles, depending on the guest\'s preferences and budget. Airport transfers often feature meet-and-greet services, where a driver awaits guests at the terminal with a personalized sign. Some properties also provide 24/7 availability, accommodating late-night or early-morning flights. Pricing varies based on distance, vehicle type, and group size, and these services can usually be booked in advance for added convenience. Airport transfers enhance the guest experience by eliminating the hassle of navigating unfamiliar transportation options.',30,'Airport Shuttle','ACTIVE'),(7,'Event planning services are an essential offering at many accommodations on Booking.com, catering to guests who wish to host meetings, conferences, weddings, or other special events during their stay. These services typically include access to versatile event spaces such as conference rooms, banquet halls, and outdoor venues, equipped with necessary facilities like projectors, sound systems, and Wi-Fi. Hotels and resorts often provide additional support, including catering, decor, and event coordination, ensuring that all details are handled professionally. Event packages may also include customized options, such as themed parties, team-building activities, or entertainment services. Pricing for event services can vary widely based on the size of the event, the type of venue, and the specific services requested, making it a flexible and convenient option for business and personal gatherings.',400,'Event','ACTIVE');
/*!40000 ALTER TABLE `provisions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rel_provision_bookingdetails`
--

DROP TABLE IF EXISTS `rel_provision_bookingdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rel_provision_bookingdetails` (
  `rel_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `price` double DEFAULT NULL,
  `status` enum('UNUSED','USED') DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `booking_detail_id` bigint DEFAULT NULL,
  `provision_id` bigint DEFAULT NULL,
  PRIMARY KEY (`rel_id`),
  KEY `FK14xymttccsxrtomwnbf4actpg` (`booking_detail_id`),
  KEY `FKh0swwnngdclu7ukjxde1fye1q` (`provision_id`),
  CONSTRAINT `FK14xymttccsxrtomwnbf4actpg` FOREIGN KEY (`booking_detail_id`) REFERENCES `booking_details` (`booking_detail_id`),
  CONSTRAINT `FKh0swwnngdclu7ukjxde1fye1q` FOREIGN KEY (`provision_id`) REFERENCES `provisions` (`provision_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rel_provision_bookingdetails`
--

LOCK TABLES `rel_provision_bookingdetails` WRITE;
/*!40000 ALTER TABLE `rel_provision_bookingdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `rel_provision_bookingdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `room_id` bigint NOT NULL AUTO_INCREMENT,
  `day_price` double DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `hour_price` double DEFAULT NULL,
  `room_number` varchar(255) NOT NULL,
  `room_type` enum('DELUXE','DOUBLE','FAMILY','SINGLE') DEFAULT NULL,
  `status` enum('AVAILABLE','BOOKED','MAINTENANCE') DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `UK7ljglxlj90ln3lbas4kl983m2` (`room_number`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,100,' If you desire a cozy, comfortable, and affordable room, the Double Room is perfect for you. The room offers a spacious 23 m² area, with a double bed or twin beds to suit your needs. It also includes an LCD TV, minibar, work desk, safe, and a private bathroom with full amenities. From the small balcony, you can enjoy the bustling and colorful view of city.',20,'101','DOUBLE','AVAILABLE'),(2,100,'With a shared lounge, a bar, and mountain view, the hotel is 2.8 km from the beach. In addition to family rooms, this property also offers guests a sun terrace. Karaoke and room service are provided. At the yacht, all rooms have a wardrobe. Besides a private bathroom with a bidet and complimentary toiletries, rooms also feature a sea view. At the property, rooms include bed linen and towels. A continental breakfast is served every morning. Guests at the yacht can enjoy activities in and around the area, such as fishing, canoeing, and cycling. Fluent in English and Vietnamese, the reception staff are always available to offer helpful area suggestions.',20,'201','DOUBLE','AVAILABLE'),(3,100,'The resort and hotel feature a seasonal outdoor pool, a garden, a restaurant, and a bar. Luggage storage is available. The property offers a 24-hour front desk, airport transfers, room service, and free WiFi throughout. Guests can enjoy a buffet or Asian breakfast.',20,'301','DOUBLE','AVAILABLE'),(4,100,'Located only 2.8 km from the beach, our hotel offers accommodation with an ocean view, a terrace, and concierge service. This air-conditioned accommodation includes free WiFi, and private parking is available on-site. The apartment features a kitchen, a seating area, and a flat-screen TV. Towels and bed linen are provided. Guests can enjoy the indoor pool at the apartment.',20,'401','DOUBLE','AVAILABLE'),(5,70,'The Single Room is an ideal choice for your stay at our hotel. The room includes a double bed or two single beds, an LCD TV, a minibar, a work desk, a safe, and a private bathroom. The room features a spacious balcony with a mountain view, offering a relaxing experience immersed in nature. The 35 m² room provides comfort and enjoyment within this space.',15,'102','SINGLE','AVAILABLE'),(6,70,'A 4-minute walk from the beach, our hotel offers accommodations with an outdoor swimming pool, a shared lounge, and room service. All units include a private bathroom, bidet, air conditioning, a flat-screen TV, and a refrigerator. Some units have a terrace and/or balcony. The apartment also provides bicycle and car rental services.',15,'202','SINGLE','AVAILABLE'),(7,70,'The hotel is a 4-minute walk from the beach, offering ocean views. All rooms feature flat-screen TVs and wardrobes. A continental breakfast is served daily. The hotel\'s restaurant offers Asian and Vietnamese cuisine, including gluten-free options. This 5-star accommodation boasts a hot tub and a terrace.',15,'302','SINGLE','AVAILABLE'),(8,70,'A 4-minute walk from the beach, the hotel offers accommodations with an outdoor pool, free private parking, a garden, and a private beach area. The property features amenities such as a terrace and a bar. Room service, a 24-hour front desk, and currency exchange are available. At the resort, rooms include air conditioning, a seating area, a flat-screen TV with satellite channels, a safety deposit box, a private bathroom with a shower, a hairdryer, and slippers. All rooms have an electric kettle, while some have balconies and others offer city views. Rooms are equipped with bed linen and towels. The property serves a buffet or Asian breakfast. Guests will find a restaurant serving Chinese and Asian cuisine with vegetarian and vegan options available.',15,'402','SINGLE','AVAILABLE'),(9,100,'Single room is a great choice for travelers who want to enjoy the beautiful scenery of nature. The room has an area of ​​25 m², fully equipped with amenities such as double bed or two single beds, LCD TV, minibar, desk, safe and private bathroom. The room has a spacious balcony facing the mountain, giving you a feeling of relaxation and being immersed in nature.',35,'103','FAMILY','AVAILABLE'),(10,100,'Family rooms are a unique and creative choice for travelers who want to explore nature in their own way. The room has an area of ​​42 m², fully equipped with amenities such as a double bed, LCD TV, minibar, desk, safe and private bathroom. The room has a small balcony overlooking the city, giving you a vibrant and dynamic feeling. Family rooms also have the advantage of being able to book online through the hotel\'s website, helping you save time and money.',35,'203','FAMILY','AVAILABLE'),(11,100,'Family rooms are a modern and convenient choice for travelers who want to experience nature in their own way. The room has an area of ​​60 m², fully equipped with amenities such as LCD TV, minibar, desk, safe and private bathroom. The room has a small balcony overlooking the city, giving you a vibrant and dynamic feeling. This room also has the advantage of being able to book online through the hotel\'s website, helping you save time and money.',35,'303','FAMILY','AVAILABLE'),(12,100,'With the Beach just a 4-minute walk away, our hotel offers accommodations, a restaurant, a private beach area, a terrace and a bar. Free WiFi is available throughout the property and private parking is available on site. Some units feature a flat-screen TV with satellite channels, a fully equipped kitchen with a fridge, and a private bathroom with a bidet and free toiletries.',35,'403','FAMILY','AVAILABLE'),(13,120,'Deluxe Room is a great choice for travelers who want to enjoy the majestic mountain scenery of nature. The room has an area of ​​25 m², fully equipped with amenities such as double bed or two single beds, LCD TV, minibar, desk, safe and private bathroom. The room has a spacious balcony facing the mountain, giving you a feeling of relaxation and being immersed in nature.',40,'104','DELUXE','AVAILABLE'),(14,120,'The hotel is located in a natural setting and features a bar. In addition to a restaurant, the hotel offers air-conditioned rooms with free Wi-Fi, each with a private bathroom. The property provides room service, a 24-hour front desk and currency exchange for guests. At the hotel, rooms have a balcony. All rooms are equipped with a flat-screen TV, while some rooms at the hotel have city views. Buffet and Asian breakfast options are available every morning.',40,'204','DELUXE','AVAILABLE'),(15,120,'Located on the beachfront, the hotel offers luxurious accommodation and features an outdoor swimming pool, a fitness centre and a shared lounge. Among the facilities of this property are room service and a 24-hour front desk, along with free Wi-Fi in all areas. The hotel has a terrace and views of the city, and guests can enjoy a meal at the restaurant or a drink at the bar. The hotel provides air-conditioned rooms with a desk, a kettle, a fridge, a minibar, a safe, a flat-screen TV and a private bathroom with a bidet/drip shower. Some units have a kitchen with a stovetop. Bed linen and towels are provided in all rooms. Buffet, à la carte or continental breakfast options are available daily. The property has a sauna. The area is popular for hiking and cycling. Car hire is available.',40,'304','DELUXE','AVAILABLE'),(16,120,'Featuring a terrace and a bar with a view, the hotel is just a 4-minute walk from the Beach. In addition to a garden, the hotel offers air-conditioned rooms with free Wi-Fi, each with a private bathroom. The property offers room service, a 24-hour front desk and currency exchange for guests. At the hotel, each room is equipped with a desk. All rooms are equipped with a kettle, while some rooms feature a patio and others have sea views. The living room has a wardrobe. The property offers a buffet, à la carte or continental breakfast every morning. Guests will find a restaurant serving Asian cuisine. Vegetarian, dairy-free and vegan options are available on request. The area is popular for hiking and cycling. Bike hire is available at the property.',40,'404','DELUXE','AVAILABLE');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER','EMPLOYEE','MANAGER') DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'123 Main St','tuan@example.com','Pham','Pham Tuan','Tuan','$2a$10$g4SZwgWGMQkPVC87KhxbyOfG26YfcU6bxUknF.TrHD5cKzKlmJzqm','0869813541','ADMIN','2024-11-17 14:26:20');
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

-- Dump completed on 2024-11-18 16:47:21
