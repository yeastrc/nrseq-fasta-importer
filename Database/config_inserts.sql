

--   Directory on server where this FASTA importer writes files to

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('fasta_importer_work_directory', '??????');


--   Server URL - Used in link in emails sent

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('server_url', '??????');

--  example http:192.168.1.2:8080


--  Email

--  	choose one of the next 2 inserts:

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_send_emails', 'true');

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_send_emails', 'false');

--  If set email_send_emails to 'true', set these 2 values

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_smtp_server_url', '??????');

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_from_address', '??????');

