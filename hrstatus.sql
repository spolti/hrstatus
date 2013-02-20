/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * @author spolti
 */

CREATE DATABASE  IF NOT EXISTS `hrstatus` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hrstatus`;

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
-- Table structure for table `Configurations`
--

DROP TABLE IF EXISTS `Configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dests` varchar(255) DEFAULT NULL,
  `difference` int(11) DEFAULT NULL,
  `jndiMail` varchar(255) DEFAULT NULL,
  `mailFrom` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Configurations`
--

LOCK TABLES `Configurations` WRITE;
/*!40000 ALTER TABLE `Configurations` DISABLE KEYS */;
INSERT INTO `Configurations` VALUES (1,'hrstatus@example.com.br',70,'java:jboss/mail/HrstatusMail','hrstatus@example.com.br','NO REPLY - Status Horario de Verao');
/*!40000 ALTER TABLE `Configurations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PassExpire`
--

DROP TABLE IF EXISTS `PassExpire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PassExpire` (
  `username` varchar(255) NOT NULL,
  `changeTime` varchar(255) DEFAULT NULL,
  `expireTime` varchar(255) DEFAULT NULL,
  `newPwd` varchar(255) DEFAULT NULL,
  `oldPwd` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PassExpire`
--

LOCK TABLES `PassExpire` WRITE;
/*!40000 ALTER TABLE `PassExpire` DISABLE KEYS */;
/*!40000 ALTER TABLE `PassExpire` ENABLE KEYS */;
UNLOCK TABLES;

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
  `difference` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `username` varchar(255) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `firstLogin` bit(1),
  `mail` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `mail` (`mail`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES ('admin','ROLE_ADMIN','','','admin@example.com.br','Administrador do Sistema','89794b621a313bb59eed0d9f0f4e8205');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lockedResources`
--

DROP TABLE IF EXISTS `lockedResources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lockedResources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recurso` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- User Configurations
CREATE USER 'hrstatus'@'%' IDENTIFIED BY 'P@ssw0rd';
GRANT SELECT,INSERT,UPDATE,DELETE ON hrstatus.* TO 'hrstatus'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;