
DROP SCHEMA IF EXISTS `nrseq_fasta_importer` ;
CREATE SCHEMA IF NOT EXISTS `nrseq_fasta_importer` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `nrseq_fasta_importer` ;

-- -----------------------------------------------------
-- Table `config_system`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `config_system` ;

CREATE TABLE IF NOT EXISTS `config_system` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `config_key` VARCHAR(255) NOT NULL,
  `config_value` VARCHAR(4000) NULL,
  `comment` VARCHAR(4000) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fasta_import_tracking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fasta_import_tracking` ;

CREATE TABLE IF NOT EXISTS `fasta_import_tracking` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `filename` VARCHAR(512) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `sha1sum` VARCHAR(45) NOT NULL,
  `temp_filename` VARCHAR(255) NOT NULL,
  `upload_date_time` TIMESTAMP NOT NULL,
  `processing_start_date_time` TIMESTAMP NULL,
  `processing_complete_date_time` TIMESTAMP NULL,
  `processing_failed_date_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

