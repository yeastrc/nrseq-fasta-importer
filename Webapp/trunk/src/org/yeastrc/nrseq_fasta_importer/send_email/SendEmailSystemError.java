package org.yeastrc.nrseq_fasta_importer.send_email;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;



public class SendEmailSystemError {
	
	private static final SendEmailSystemError instance = new SendEmailSystemError();

	private SendEmailSystemError() { }
	public static SendEmailSystemError getInstance() { return instance; }

	
	private static final Logger log = Logger.getLogger(SendEmailSystemError.class);



	public void sendEmailSystemError( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception  {

		GetEmailConfig.validateEmailConfig();

		String urlToRecordId = GetURLForRecord.getURLForRecord( fastaImportTrackingDTO );
		
		String emailSubject = "System error while processing file " + fastaImportTrackingDTO.getFilename();
		
		String emailBody = "System error while processing " + fastaImportTrackingDTO.getFilename()
				 + "\n"
				 + urlToRecordId + "\n";

		SendEmailDTO sendEmailDTO = new SendEmailDTO();
		
		sendEmailDTO.setFromEmailAddress( GetEmailConfig.getFromAddress() );
		sendEmailDTO.setToEmailAddress( fastaImportTrackingDTO.getEmail() );
		
		sendEmailDTO.setEmailSubject( emailSubject );
		sendEmailDTO.setEmailBody( emailBody );
		
		SendEmail.getInstance().sendEmail( sendEmailDTO );
	}
}
