package org.yeastrc.nrseq_fasta_importer.constants;

/**
 * values for the config_key field of the config_system table
 *
 */
public class ConfigSystemsKeysConstants {

	public static final String UPLOADED_FILES_TEMP_DIRECTORY_KEY = "uploaded_files_temp_dir";


	
	/////////   Server URL - Used in link in emails sent
	
	public static final String SERVER_URL_KEY = "server_url";
	
	
	
	
	/////////   Email Config
	
	public static final String EMAIL_SEND_EMAILS_KEY = "email_send_emails";
	
	//  Value for EMAIL_SEND_EMAILS_KEY:
	public static final String EMAIL_SEND_EMAILS_VALUE_TRUE = "true";
	
	//  Value for EMAIL_SEND_EMAILS_KEY:
	public static final String EMAIL_SEND_EMAILS_VALUE_FALSE = "false";
	
	
	public static final String EMAIL_WEBSERVICE_URL_KEY = "email_webservice_url";
	
	public static final String EMAIL_SMTP_SERVER_URL_KEY = "email_smtp_server_url";
	public static final String EMAIL_FROM_ADDRESS_URL_KEY = "email_from_address";
	
}



	
//	INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//    VALUES ('uploaded_files_temp_dir', '??????');


/////////   Server URL - Used in link in emails sent

//INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//VALUES ('server_url', '??????');

//  http://192.168.1.2:9080


// Email

//INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//	VALUES ('email_send_emails', 'true');

//INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//VALUES ('email_send_emails', 'false');

//INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//	VALUES ('email_smtp_server_url', '??????');

//INSERT INTO `nrseq_fasta_importer`.`config_system` (`config_key`, `config_value`) 
//	VALUES ('email_from_address', '??????');
