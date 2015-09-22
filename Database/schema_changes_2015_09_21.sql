
ALTER TABLE `nrseq_fasta_importer`.`fasta_import_tracking` 
ADD COLUMN `import_decoy_sequences` TINYINT(4) NULL DEFAULT NULL AFTER `yrc_nrseq_tblDatabase_id`,
ADD COLUMN `require_confirm_before_insert` TINYINT(4) NULL DEFAULT NULL AFTER `import_decoy_sequences`;
