
DROP SCHEMA IF EXISTS `nrseq_fasta_importer` ;
CREATE SCHEMA IF NOT EXISTS `nrseq_fasta_importer` DEFAULT CHARACTER SET latin1 ;
USE `nrseq_fasta_importer` ;

-- -----------------------------------------------------
-- Table `NCBI_Taxonomy`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `NCBI_Taxonomy` ;

CREATE TABLE IF NOT EXISTS `NCBI_Taxonomy` (
  `id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `name` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE INDEX `name` ON `NCBI_Taxonomy` (`name` ASC);


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
  `id` INT UNSIGNED NOT NULL,
  `filename` VARCHAR(512) NOT NULL,
  `description` VARCHAR(500) NULL,
  `email` VARCHAR(255) NULL,
  `status` VARCHAR(45) NOT NULL,
  `insert_request_url` VARCHAR(255) NULL,
  `sha1sum` VARCHAR(45) NOT NULL,
  `fasta_entry_count` INT NULL,
  `get_taxonomy_ids_pass_number` INT NOT NULL DEFAULT 0,
  `yrc_nrseq_tblDatabase_id` INT NULL,
  `import_decoy_sequences` TINYINT NULL,
  `require_confirm_before_insert` TINYINT NULL,
  `upload_date_time` TIMESTAMP NOT NULL,
  `last_updated_date_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `temp_upload_file_id_creator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `temp_upload_file_id_creator` ;

CREATE TABLE IF NOT EXISTS `temp_upload_file_id_creator` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fasta_import_tracking_status_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fasta_import_tracking_status_history` ;

CREATE TABLE IF NOT EXISTS `fasta_import_tracking_status_history` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fasta_import_tracking_id` INT UNSIGNED NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `status_timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fasta_import_tracking_status_history__f_i_t_id_fk`
    FOREIGN KEY (`fasta_import_tracking_id`)
    REFERENCES `fasta_import_tracking` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fasta_import_tracking_status_history__fasta_import_tracking_idx` ON `fasta_import_tracking_status_history` (`fasta_import_tracking_id` ASC);


-- -----------------------------------------------------
-- Table `fasta_header_no_tax_id_determined`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fasta_header_no_tax_id_determined` ;

CREATE TABLE IF NOT EXISTS `fasta_header_no_tax_id_determined` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fasta_import_tracking_id` INT UNSIGNED NOT NULL,
  `get_taxonomy_ids_pass_number` INT NOT NULL,
  `header_name` VARCHAR(4000) NOT NULL,
  `header_description` VARCHAR(4000) NULL,
  `header_line` VARCHAR(6000) NULL,
  `header_line_number` INT NULL,
  `message` VARCHAR(3000) NULL,
  `user_assigned_tax_id` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fasta_header_no_tax_id_determined__f_i_t_id_fk`
    FOREIGN KEY (`fasta_import_tracking_id`)
    REFERENCES `fasta_import_tracking` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fasta_header_no_tax_id_determined__fasta_import_tracking_id_idx` ON `fasta_header_no_tax_id_determined` (`fasta_import_tracking_id` ASC);


-- -----------------------------------------------------
-- Table `general_import_error`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `general_import_error` ;

CREATE TABLE IF NOT EXISTS `general_import_error` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fasta_import_tracking_id` INT UNSIGNED NOT NULL,
  `message` VARCHAR(3000) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `general_import_error__f_i_t_id_fk`
    FOREIGN KEY (`fasta_import_tracking_id`)
    REFERENCES `fasta_import_tracking` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `general_import_error__fasta_import_tracking_id_fk_idx` ON `general_import_error` (`fasta_import_tracking_id` ASC);


-- -----------------------------------------------------
-- Table `local_cache_uniprot_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `local_cache_uniprot_data` ;

CREATE TABLE IF NOT EXISTS `local_cache_uniprot_data` (
  `accession` VARCHAR(255) NOT NULL,
  `taxonomy_id` INT NULL,
  PRIMARY KEY (`accession`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `local_cache_swiss_prot_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `local_cache_swiss_prot_data` ;

CREATE TABLE IF NOT EXISTS `local_cache_swiss_prot_data` (
  `accession` VARCHAR(255) NOT NULL,
  `taxonomy_id` INT NULL,
  PRIMARY KEY (`accession`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `local_cache_ncbi_protein_by_gi_accession_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `local_cache_ncbi_protein_by_gi_accession_id` ;

CREATE TABLE IF NOT EXISTS `local_cache_ncbi_protein_by_gi_accession_id` (
  `gi_accession_id` INT NOT NULL,
  `taxonomy_id` INT NULL,
  PRIMARY KEY (`gi_accession_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tmp_fasta_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tmp_fasta_sequence` ;

CREATE TABLE IF NOT EXISTS `tmp_fasta_sequence` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fasta_import_tracking_id` INT UNSIGNED NOT NULL,
  `header_line_number` INT UNSIGNED NOT NULL,
  `sequence` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `tmp_fasta_sequence_fasta_import_tracking_id_fk`
    FOREIGN KEY (`fasta_import_tracking_id`)
    REFERENCES `fasta_import_tracking` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `tmp_fasta_sequence_fasta_import_tracking_id_fk_idx` ON `tmp_fasta_sequence` (`fasta_import_tracking_id` ASC);


-- -----------------------------------------------------
-- Table `tmp_fasta_header_name_desc_seq_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tmp_fasta_header_name_desc_seq_id` ;

CREATE TABLE IF NOT EXISTS `tmp_fasta_header_name_desc_seq_id` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fasta_import_tracking_id` INT UNSIGNED NOT NULL,
  `header_line_number` INT UNSIGNED NOT NULL,
  `tmp_fasta_sequence_id_fk` INT NOT NULL,
  `header_name_hash_code` INT NOT NULL,
  `header_name` VARCHAR(1000) NOT NULL,
  `header_description` VARCHAR(3000) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `tmp_fasta_header_name_desc_seq_id_fasta_import_tracking_id_fk`
    FOREIGN KEY (`fasta_import_tracking_id`)
    REFERENCES `fasta_import_tracking` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `tmp_fasta_sequence_fasta_import_tracking_id_fk_idx` ON `tmp_fasta_header_name_desc_seq_id` (`fasta_import_tracking_id` ASC);

CREATE INDEX `tmp_fasta_sequence__f_i_t_i__header_name_idx` ON `tmp_fasta_header_name_desc_seq_id` (`fasta_import_tracking_id` ASC, `header_name_hash_code` ASC, `header_name`(20) ASC);


-- -----------------------------------------------------
-- Table `fasta_header_no_tax_id_determined_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fasta_header_no_tax_id_determined_sequence` ;

CREATE TABLE IF NOT EXISTS `fasta_header_no_tax_id_determined_sequence` (
  `fasta_header_no_tax_id_determined_id` INT UNSIGNED NOT NULL,
  `sequence` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`fasta_header_no_tax_id_determined_id`),
  CONSTRAINT `fasta_header_no_tax_id_determined__sequence_id_fk`
    FOREIGN KEY (`fasta_header_no_tax_id_determined_id`)
    REFERENCES `fasta_header_no_tax_id_determined` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `fasta_import_tracking_id_creator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fasta_import_tracking_id_creator` ;

CREATE TABLE IF NOT EXISTS `fasta_import_tracking_id_creator` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
