CREATE DATABASE  IF NOT EXISTS `hrstatus` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hrstatus`;
-- MySQL dump 10.13  Distrib 5.5.28, for Linux (x86_64)
--
-- Host: localhost    Database: hrstatus
-- ------------------------------------------------------
-- Server version	5.5.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Servidores`
--

DROP TABLE IF EXISTS `Servidores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Servidores` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SO` varchar(255) DEFAULT NULL,
  `clientTime` varchar(255) DEFAULT NULL,
  `hostname` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `lastCheck` varchar(255) DEFAULT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `serverTime` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `trClass` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Servidores`
--

LOCK TABLES `Servidores` WRITE;
/*!40000 ALTER TABLE `Servidores` DISABLE KEYS */;
INSERT INTO `Servidores` VALUES (1,'LINUX','Tue Jan 15 11:02:09 BRST 2013\n','spolti','127.0.0.1','11:02:05 15/01/2013','26c24a2cc8b4e94b9df18d19a804ca7f',22,'Tue Jan 15 11:02:05 Brasilia Summer Time 2013','OK','success','spolti'),(2,'LINUX','Tue Jan 15 11:02:07 BRST 2013\n','jbosseap01hom','10.11.152.76','11:02:09 15/01/2013','-5248516da351323aea160a14f217b9c4',22,'Tue Jan 15 11:02:09 Brasilia Summer Time 2013','OK','success','spolti'),(4,'WINDOWS','Tue Jan 15 11:02:07 2013','terminal_service_123','10.32.14.206','11:02:10 15/01/2013','-4f7ae76b66d400f8b37cf21d10375356568d1c7ad7555e9c',23,'Tue Jan 15 11:02:10 2013','OK','success','network\\spolti'),(5,'LINUX','Tue Jan 15 11:02:08 BRST 2013\n','jbosseap02hom','10.11.152.77','11:02:11 15/01/2013','-5248516da351323aea160a14f217b9c4',22,'Tue Jan 15 11:02:11 Brasilia Summer Time 2013','OK','success','spolti'),(7,'LINUX','Tue Jan 15 11:02:09 BRST 2013\n','gifehmg01','10.11.136.17','11:02:11 15/01/2013','-5248516da351323aea160a14f217b9c4',22,'Tue Jan 15 11:02:11 Brasilia Summer Time 2013','OK','success','spolti'),(8,'OUTRO','Tue Jan 15 11:02:13 BRST 2013\n','localhost','127.0.0.1','11:02:13 15/01/2013','26c24a2cc8b4e94b9df18d19a804ca7f',22,'Tue Jan 15 11:02:13 Brasilia Summer Time 2013','OK','success','spolti'),(9,'LINUX','Tue Jan 15 11:02:14 BRST 2013\n','teste1','127.0.0.1','11:02:13 15/01/2013','26c24a2cc8b4e94b9df18d19a804ca7f',22,'Tue Jan 15 11:02:13 Brasilia Summer Time 2013','OK','success','spolti');
/*!40000 ALTER TABLE `Servidores` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-21  9:18:52
