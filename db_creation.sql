CREATE DATABASE  IF NOT EXISTS `exoapp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `exoapp`;
-- MySQL dump 10.13  Distrib 8.0.41, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: exoapp
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `exercise_sessions`
--

DROP TABLE IF EXISTS `exercise_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise_sessions` (
  `user_id` int NOT NULL,
  `date` date NOT NULL,
  `duration` float NOT NULL,
  PRIMARY KEY (`user_id`,`date`),
  CONSTRAINT `fk_id_session` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise_sessions`
--

LOCK TABLES `exercise_sessions` WRITE;
/*!40000 ALTER TABLE `exercise_sessions` DISABLE KEYS */;
INSERT INTO `exercise_sessions` VALUES (23,'2025-05-13',62),(23,'2025-05-19',33),(23,'2025-05-21',40),(23,'2025-05-25',45),(23,'2025-05-29',29.6),(23,'2025-06-04',20.7),(23,'2025-06-06',12),(23,'2025-06-08',61),(23,'2025-06-12',43.7),(23,'2025-06-16',23),(24,'2025-04-22',36),(24,'2025-05-02',11),(24,'2025-05-05',62.3),(24,'2025-05-08',44),(24,'2025-05-10',25),(24,'2025-05-12',22),(24,'2025-05-16',13.7),(24,'2025-05-22',27),(24,'2025-06-02',62),(24,'2025-06-04',20.3),(24,'2025-06-10',32),(24,'2025-06-12',13.2),(24,'2025-06-13',15.7),(24,'2025-06-14',20),(24,'2025-06-15',46.4),(24,'2025-06-16',2.3),(25,'2025-05-16',27),(25,'2025-05-19',7),(25,'2025-05-22',63),(25,'2025-05-27',30),(25,'2025-06-01',13),(26,'2025-06-04',2.2);
/*!40000 ALTER TABLE `exercise_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passive_mode`
--

DROP TABLE IF EXISTS `passive_mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passive_mode` (
  `user_id` int NOT NULL,
  `date` date NOT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`user_id`,`date`),
  CONSTRAINT `pm-fk-user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passive_mode`
--

LOCK TABLES `passive_mode` WRITE;
/*!40000 ALTER TABLE `passive_mode` DISABLE KEYS */;
INSERT INTO `passive_mode` VALUES (23,'2025-06-01',20),(23,'2025-06-02',20),(23,'2025-06-09',26),(23,'2025-06-10',27),(23,'2025-06-13',27),(23,'2025-06-15',28),(23,'2025-06-16',12),(24,'2025-06-03',27),(24,'2025-06-04',29),(24,'2025-06-07',30),(24,'2025-06-11',31),(24,'2025-06-12',32),(24,'2025-06-14',33),(24,'2025-06-16',56);
/*!40000 ALTER TABLE `passive_mode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supervisor_user`
--

DROP TABLE IF EXISTS `supervisor_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supervisor_user` (
  `user_id` int NOT NULL,
  `supervisor_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`supervisor_id`),
  KEY `id_idx` (`user_id`),
  KEY `fk_supervisor_id_idx` (`supervisor_id`),
  CONSTRAINT `fk_supervisor_id` FOREIGN KEY (`supervisor_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supervisor_user`
--

LOCK TABLES `supervisor_user` WRITE;
/*!40000 ALTER TABLE `supervisor_user` DISABLE KEYS */;
INSERT INTO `supervisor_user` VALUES (23,21),(24,21),(25,21),(26,22),(27,22),(29,22),(30,22),(31,22),(32,22),(41,21),(44,21);
/*!40000 ALTER TABLE `supervisor_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_information`
--

DROP TABLE IF EXISTS `user_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_information` (
  `user_id` int NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `stroke_type` varchar(100) DEFAULT NULL,
  `stroke_subtype` varchar(100) DEFAULT NULL,
  `stroke_date` date DEFAULT NULL,
  `rehab_start_date` date DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_user_if_info` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_information`
--

LOCK TABLES `user_information` WRITE;
/*!40000 ALTER TABLE `user_information` DISABLE KEYS */;
INSERT INTO `user_information` VALUES (23,'Georgiana Jones','1975-06-21','Ischemic','Lacunar','2024-11-28','2025-03-26'),(24,'Alexander Smith','1971-03-29','Hemorrhagic','Intracerebral','2024-06-10','2025-03-16'),(25,'Jane Darby','1960-04-10','Hemorrhagic','Intracerebral','2024-09-19','2025-02-17'),(26,'Christine Grey','1983-12-15','Ischemic','Lacunar','2025-02-18','2025-04-19'),(27,'Jolie Hunter','1981-06-12','Ischemic','Cardioembolic','2025-01-14','2025-04-04'),(29,'Todd Julius','1955-05-17','Hemorrhagic','Other','2024-09-16','2025-03-09'),(30,'Tessa Barnes','1960-01-07','Ischemic','Cardioembolic','2023-01-04','2025-05-05'),(31,'Clara Clements','1958-08-24','Ischemic','Lacunar','2023-06-23','2025-04-03'),(32,'Jason Jones','1948-12-30','Ischemic','Lacunar','2024-11-30','2025-04-09'),(41,'Andrea Jones','1994-05-08','Ischemic','Lacunar','2025-01-21','2025-04-06'),(44,'John Watson','1999-02-28','Ischemic','Large artery atherosclerosis','2025-04-09','2025-05-27');
/*!40000 ALTER TABLE `user_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` int NOT NULL,
  `pass_change` tinyint DEFAULT '0',
  `last_pass_change` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (21,'meredith_grey','$2a$10$wy71WvdJRKGeG1aaDCr/9Oa9SFGXOSEW.V9i4wDjbpCEp30NKjbg6',2,1,'2025-06-10'),(22,'alex_karev','$2a$10$0Fnj4ctSJGW3keESCE/B..j.IRjsNE/YjmIt.1D8rKaKIhB1g1FDK',2,1,'2025-05-15'),(23,'georgiana_jones','$2a$10$6LCQrqzQ0IEt1oCWQocBkuTag.2Mo4MFA2eEX0CWz9uRp328b3ydi',1,0,NULL),(24,'alex_smith','$2a$10$ayVbjeYYvCgaabIN98Reh.kIoVvYKvWVY.0P0V7G6x96YUrsli7WK',1,1,'2025-06-15'),(25,'jane_darby','$2a$10$uQigwp595lD35UqjgBy5qeZhfnTG9PYphinD4YX2.P0yExPBj2ndS',1,0,NULL),(26,'christine_grey','$2a$10$eP8eBS0TI83JyhCi.PYLWu14jzG7f8O7n0AbL77cR4PJO0pk8iIWe',1,1,'2025-06-05'),(27,'jolie_hunter','$2a$10$/ogCeZIEq/ZeijNhV1NUQeD.NL6GvRRVE9UmQlhcg6pSRkPFoI4Ia',1,1,'2025-05-15'),(29,'todd_julius','$2a$10$K6gkFoh52Fs09QymcHRUSOgDOyl2eQfSfp2MKG8FcylDuLRjLCkhq',1,0,NULL),(30,'tessa_barnes','$2a$10$6sUamFt7GQfcQYCx2YGiK.UynpSEFFvfedDsgdlkyLlBaGO.Jvjve',1,0,NULL),(31,'clara_clements','$2a$10$Ckh0TplPSQiAB3YTQ4494emAEtx4hxpYeNCZqyJhkuy1wMdK7.qPO',1,0,NULL),(32,'jason_jones','$2a$10$GZSGp7Tl8CexMjT5Xfm3S.1HczkcyWQOXc55RdWAVeA/QS/6BCpjC',1,0,NULL),(41,'andrea_jones','$2a$10$j8YOJxolRl8I895W9zdpnuKrsgHOZYRBPVGbwPyblJBVl.Yiiaaya',1,0,NULL),(44,'john_watson','$2a$10$C/bp3Lw6u6/0sK8qiMLXnOrUKp0d7ZfKqI/l5MCzE5nVVuZZk00IG',1,0,NULL);
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

-- Dump completed on 2025-06-16 19:47:49
