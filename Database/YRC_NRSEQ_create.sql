
CREATE SCHEMA IF NOT EXISTS `YRC_NRSEQ` DEFAULT CHARACTER SET latin1 ;
USE `YRC_NRSEQ` ;

--
-- Table structure for table `tblDatabase`
--

DROP TABLE IF EXISTS `tblDatabase`;

CREATE TABLE `tblDatabase` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `tblProtein`
--

DROP TABLE IF EXISTS `tblProtein`;

CREATE TABLE `tblProtein` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sequenceID` int(10) unsigned NOT NULL DEFAULT '0',
  `speciesID` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sequenceID` (`sequenceID`,`speciesID`),
  KEY `sequenceID_2` (`sequenceID`),
  KEY `speciesID_2` (`speciesID`,`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `tblProteinDatabase`
--

DROP TABLE IF EXISTS `tblProteinDatabase`;

CREATE TABLE `tblProteinDatabase` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `proteinID` int(10) unsigned NOT NULL DEFAULT '0',
  `databaseID` int(10) unsigned NOT NULL DEFAULT '0',
  `accessionString` varchar(2000) DEFAULT NULL,
  `description` varchar(2500) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isCurrent` enum('T','F') NOT NULL DEFAULT 'T',
  PRIMARY KEY (`id`),
  UNIQUE KEY `databaseID_2` (`databaseID`,`proteinID`,`accessionString`(800)),
  KEY `proteinID` (`proteinID`),
  KEY `databaseID` (`databaseID`),
  KEY `isCurrent` (`isCurrent`),
  KEY `accessionString` (`accessionString`(1000))
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tblProteinSequence`
--

DROP TABLE IF EXISTS `tblProteinSequence`;

CREATE TABLE `tblProteinSequence` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sequence` mediumtext,
  PRIMARY KEY (`id`),
  KEY `sequence` (`sequence`(255))
) ENGINE=MyISAM AUTO_INCREMENT=26326391 DEFAULT CHARSET=latin1;
