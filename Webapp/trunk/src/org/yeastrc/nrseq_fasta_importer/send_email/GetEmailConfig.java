package org.yeastrc.nrseq_fasta_importer.send_email;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ConfigSystemsKeysConstants;
import org.yeastrc.nrseq_fasta_importer.dao.ConfigSystemDAO;

/**
 * 
 *
 */
public class GetEmailConfig {

	private static final Logger log = Logger.getLogger(GetEmailConfig.class);
	
	/**
	 * @return true if valid
	 * @throws Exception - If data not valid or error getting data
	 */
	public static boolean validateEmailConfig() throws Exception {
		
		if ( ! isSendEmail() ) {
			
			return true;
		}
		
		
		
		String webserviceURL = null;
		String smtpServerURL = null;
		String fromAddress = null;

		try {
			webserviceURL = GetEmailConfig.getWebserviceURL();
			smtpServerURL = GetEmailConfig.getSmtpServerURL();
			fromAddress = GetEmailConfig.getFromAddress();

		} catch (Exception e ) {

			String msg = "Exception getting email config for validation";
			log.error( msg, e );
			
			throw e;
		}
		
		if ( StringUtils.isEmpty( webserviceURL ) && StringUtils.isEmpty( smtpServerURL ) ) {

			String msg = "Config for '" + ConfigSystemsKeysConstants.EMAIL_WEBSERVICE_URL_KEY
					+ "' and '" + ConfigSystemsKeysConstants.EMAIL_SMTP_SERVER_URL_KEY
					+ "' cannot both be empty.";
			log.error( msg );
			
			throw new Exception(msg);
		}
		
		
		if ( StringUtils.isNotEmpty( webserviceURL ) && StringUtils.isNotEmpty( smtpServerURL ) ) {

			String msg = "Config for '" + ConfigSystemsKeysConstants.EMAIL_WEBSERVICE_URL_KEY
					+ "' and '" + ConfigSystemsKeysConstants.EMAIL_SMTP_SERVER_URL_KEY
					+ "' cannot both be populated.";
			log.error( msg );
			
			throw new Exception(msg);
		}
		
		
		if ( StringUtils.isEmpty( fromAddress ) ) {

			String msg = "Config for '" + ConfigSystemsKeysConstants.EMAIL_FROM_ADDRESS_URL_KEY
					+ "' cannot be empty.";
			log.error( msg );
			
			throw new Exception(msg);
		}

		return true;
	}

	
	
	/**
	 * @return true if "email_send_emails" contains "true" ignoring case
	 * @throws Exception
	 */
	public static boolean isSendEmail() throws Exception {
		
		String sendEmailString =
		ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.EMAIL_SEND_EMAILS_KEY );
		
		boolean sendEmail = false;
		
		if ( ConfigSystemsKeysConstants.EMAIL_SEND_EMAILS_VALUE_TRUE.equalsIgnoreCase( sendEmailString ) ) {
			
			sendEmail = true;
		}
		
		return sendEmail;
	}
	
	
	public static String getWebserviceURL() throws Exception {
		
		String webserviceURL =
		ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.EMAIL_WEBSERVICE_URL_KEY );
		
		return webserviceURL;
	}
	
	public static String getSmtpServerURL() throws Exception {
		
		String smtpServerURL =
		ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.EMAIL_SMTP_SERVER_URL_KEY );
		
		return smtpServerURL;
	}
	
	public static String getFromAddress() throws Exception {
		
		String fromAddress =
		ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.EMAIL_FROM_ADDRESS_URL_KEY );
		
		return fromAddress;
	}
	
	
//	ConfigSystemsKeysConstants
	
		/////////   Email Config
	
//		public static final String EMAIL_SEND_EMAILS_KEY = "email_send_emails";
//		
//		//  Value for EMAIL_SEND_EMAILS_KEY:
//		public static final String EMAIL_SEND_EMAILS_VALUE_TRUE = "true";
//		
//		//  Value for EMAIL_SEND_EMAILS_KEY:
//		public static final String EMAIL_SEND_EMAILS_VALUE_FALSE = "false";
//	
//
//		public static final String EMAIL_WEBSERVICE_URL_KEY = "email_webservice_url";
//		
//		public static final String EMAIL_SMTP_SERVER_URL_KEY = "email_smtp_server_url";
//		public static final String EMAIL_FROM_ADDRESS_URL_KEY = "email_from_address";
//		
	

}
