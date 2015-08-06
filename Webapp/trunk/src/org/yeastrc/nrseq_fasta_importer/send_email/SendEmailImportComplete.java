package org.yeastrc.nrseq_fasta_importer.send_email;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;

public class SendEmailImportComplete {
	
	private static final SendEmailImportComplete instance = new SendEmailImportComplete();

	private SendEmailImportComplete() { }
	public static SendEmailImportComplete getInstance() { return instance; }

	
	private static final Logger log = Logger.getLogger(SendEmailImportComplete.class);



	public void sendEmailImportComplete( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception  {
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "SendEmailImportComplete.sendEmailImportComplete(...) called, fastaImportTrackingDTO.id: " + fastaImportTrackingDTO.getId() );
		}
		
		GetEmailConfig.validateEmailConfig();
		

		String urlToRecordId = GetURLForRecord.getURLForRecord( fastaImportTrackingDTO );
		
		
		String emailSubject = "Successful Import of file " + fastaImportTrackingDTO.getFilename();
		
		String emailBody = "Successful Import of file " + fastaImportTrackingDTO.getFilename()
				 + "\n"
				 + " " + urlToRecordId + "\n";


		SendEmailDTO sendEmailDTO = new SendEmailDTO();
		
		sendEmailDTO.setFromEmailAddress( GetEmailConfig.getFromAddress() );
		sendEmailDTO.setToEmailAddress( fastaImportTrackingDTO.getEmail() );
		
		sendEmailDTO.setEmailSubject( emailSubject );
		sendEmailDTO.setEmailBody( emailBody );
		
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "sendEmailImportComplete(...):  Calling SendEmail.getInstance().sendEmail, fastaImportTrackingDTO.id: " + fastaImportTrackingDTO.getId() );
		}
		SendEmail.getInstance().sendEmail( sendEmailDTO );
	}
}
