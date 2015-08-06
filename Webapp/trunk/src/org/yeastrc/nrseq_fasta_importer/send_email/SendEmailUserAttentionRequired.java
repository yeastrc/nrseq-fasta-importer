package org.yeastrc.nrseq_fasta_importer.send_email;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;

public class SendEmailUserAttentionRequired {
	
	private static final SendEmailUserAttentionRequired instance = new SendEmailUserAttentionRequired();

	private SendEmailUserAttentionRequired() { }
	public static SendEmailUserAttentionRequired getInstance() { return instance; }

	
	private static final Logger log = Logger.getLogger(SendEmailUserAttentionRequired.class);



	public void sendEmailUserAttentionRequired( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception  {

		GetEmailConfig.validateEmailConfig();

		String urlToRecordId = GetURLForRecord.getURLForRecord( fastaImportTrackingDTO );
		
		String emailSubject = "Import of file " + fastaImportTrackingDTO.getFilename()
				+ " requires user attention";
		
		String emailBody = "Import of file " + fastaImportTrackingDTO.getFilename() 
				+ " has status '" + fastaImportTrackingDTO.getStatus() + "'."
				+ urlToRecordId + "\n";
		

		SendEmailDTO sendEmailDTO = new SendEmailDTO();
		
		sendEmailDTO.setFromEmailAddress( GetEmailConfig.getFromAddress() );
		sendEmailDTO.setToEmailAddress( fastaImportTrackingDTO.getEmail() );
		
		sendEmailDTO.setEmailSubject( emailSubject );
		sendEmailDTO.setEmailBody( emailBody );
		
		SendEmail.getInstance().sendEmail( sendEmailDTO );
	}
}
