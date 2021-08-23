-- MySQL dump 10.13  Distrib 8.0.21, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: unidb
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `professor_id` int unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `professor_id` (`professor_id`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1010,10728479,'Biochemistry'),(1011,10728480,'Biology'),(1012,10728478,'Introductory Psychology'),(1013,10728482,'English Literature'),(1014,10728481,'Web Applications');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `course_id` int unsigned NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `course_id_idx` (`course_id`),
  CONSTRAINT `course_id` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1214 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
INSERT INTO `exam` VALUES (1204,1014,'2021-07-02'),(1205,1014,'2021-07-21'),(1206,1010,'2021-07-08'),(1207,1010,'2021-07-19'),(1208,1012,'2021-07-01'),(1209,1012,'2021-07-09'),(1210,1013,'2021-07-26'),(1211,1013,'2021-07-03'),(1212,1011,'2021-07-10'),(1213,1011,'2021-07-29');
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_register`
--

DROP TABLE IF EXISTS `exam_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_register` (
  `student_id` int unsigned NOT NULL,
  `exam_id` int unsigned NOT NULL,
  `grade` int NOT NULL,
  `id_report` int unsigned DEFAULT NULL,
  `state` varchar(45) NOT NULL,
  PRIMARY KEY (`student_id`,`exam_id`),
  KEY `exam_register_ibfk_3` (`id_report`),
  KEY `exam_register_ibfk_2_idx` (`exam_id`),
  CONSTRAINT `exam_register_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `exam_register_ibfk_2` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `id_report` FOREIGN KEY (`id_report`) REFERENCES `report` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_register`
--

LOCK TABLES `exam_register` WRITE;
/*!40000 ALTER TABLE `exam_register` DISABLE KEYS */;
INSERT INTO `exam_register` VALUES (10626489,1206,1,7890,'recorded'),(10626489,1207,31,1976,'recorded'),(10626489,1208,25,1323,'recorded'),(10626489,1212,0,7891,'recorded'),(10626489,1213,20,1144,'recorded'),(10626490,1204,0,7893,'recorded'),(10626490,1205,21,1123,'recorded'),(10626490,1206,1,7890,'recorded'),(10626490,1207,20,1976,'recorded'),(10626490,1211,2,7892,'recorded'),(10626491,1208,29,1323,'recorded'),(10626491,1210,29,1100,'recorded'),(10626491,1212,1,7891,'recorded'),(10626491,1213,25,1144,'recorded'),(10626492,1204,1,7893,'recorded'),(10626492,1205,1,1123,'recorded'),(10626492,1210,30,1100,'recorded'),(10626492,1212,1,7891,'recorded'),(10626492,1213,1,1144,'recorded'),(10626493,1204,31,7893,'recorded'),(10626493,1208,18,1323,'recorded'),(10626493,1210,30,1100,'recorded'),(10626493,1211,1,7892,'recorded');
/*!40000 ALTER TABLE `exam_register` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mail` varchar(254) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `department` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10728483 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

LOCK TABLES `professor` WRITE;
/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES (10728478,'norman.bates@unimail.com','norman.bates','Norman','Bates','Psychology'),(10728479,'walter.white@unimail.com','walter.white','Walter','White','Chemistry'),(10728480,'leslie.artz@unimail.com','leslie.artz','Leslie','Artz','Natural Sciences'),(10728481,'carlo.lesti@unimail.com','carlo.lesti','Carlo','Lesti','Computer Science'),(10728482,'john.keating@unimail.com','john.keating','John','Keating','English Literature');
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `exam_id` int unsigned NOT NULL,
  `date_recorded` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7895 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (1005,1213,'2021-08-22 16:13:55'),(1100,1210,'2021-08-23 08:28:37'),(1122,1213,'2021-08-22 16:14:00'),(1123,1205,'2021-08-23 08:25:30'),(1125,1213,'2021-08-22 16:13:59'),(1144,1213,'2021-08-22 16:14:04'),(1195,1213,'2021-08-22 16:13:46'),(1251,1213,'2021-08-22 16:13:57'),(1323,1208,'2021-08-18 18:13:30'),(1387,1213,'2021-08-18 08:01:35'),(1409,1213,'2021-08-22 16:13:32'),(1631,1213,'2021-08-22 16:13:59'),(1633,1213,'2021-08-22 16:14:01'),(1785,1213,'2021-08-22 16:14:03'),(1918,1213,'2021-08-18 08:05:17'),(1952,1213,'2021-08-22 16:13:49'),(1976,1207,'2021-08-20 08:08:16'),(7890,1206,'2021-07-16 00:00:00'),(7891,1212,'2021-07-20 00:00:00'),(7892,1211,'2021-07-18 00:00:00'),(7893,1204,'2021-07-15 00:00:00'),(7894,1204,'2021-07-15 00:00:00');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `mail` varchar(254) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `school` varchar(45) NOT NULL,
  `degree` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10626494 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (10626489,'fogell.mclovin@stud.unimail.com','fogell.mclovin','Fogell','McLovin','Empire State University','Engineering Management'),(10626490,'greg.heffley@stud.unimail.com','greg.heffley','Greg','Heffley','Empire State University','Chemical Engineering'),(10626491,'lisa.simpson@stud.unimail.com','lisa.simpson','Lisa','Simpson','Empire State University','Psychology'),(10626492,'martin.mcfly@stud.unimail.com','martin.mcfly','Martin','McFly','Empire State University','Bioengineering'),(10626493,'sarah.connor@stud.unimail.com','sarah.connor','Sarah','Connor','Empire State University','Computer Science');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_plan`
--

DROP TABLE IF EXISTS `study_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `study_plan` (
  `student_id` int unsigned NOT NULL,
  `course_id` int unsigned NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `id` (`course_id`),
  CONSTRAINT `id` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `study_plan_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_plan`
--

LOCK TABLES `study_plan` WRITE;
/*!40000 ALTER TABLE `study_plan` DISABLE KEYS */;
INSERT INTO `study_plan` VALUES (10626489,1010),(10626490,1010),(10626492,1010),(10626489,1011),(10626491,1011),(10626492,1011),(10626489,1012),(10626491,1012),(10626493,1012),(10626490,1013),(10626491,1013),(10626492,1013),(10626493,1013),(10626490,1014),(10626492,1014),(10626493,1014);
/*!40000 ALTER TABLE `study_plan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-23  9:04:14
