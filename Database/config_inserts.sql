

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('uploaded_files_temp_dir', '??????');


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



INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_smtp_server_url', '??????');

INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
	VALUES ('email_from_address', '??????');

