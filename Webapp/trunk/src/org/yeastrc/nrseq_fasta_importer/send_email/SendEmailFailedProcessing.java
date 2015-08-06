package org.yeastrc.nrseq_fasta_importer.send_email;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.server_url.GetServerURLConfig;

public class SendEmailFailedProcessing {
	
	private static final SendEmailFailedProcessing instance = new SendEmailFailedProcessing();

	private SendEmailFailedProcessing() { }
	public static SendEmailFailedProcessing getInstance() { return instance; }

	
	private static final Logger log = Logger.getLogger(SendEmailFailedProcessing.class);



	public void sendEmailFailedProcessing( FASTAImportTrackingDTO fastaImportTrackingDTO, GeneralImportErrorDTO generalImportErrorDTO ) throws Exception  {

		GetEmailConfig.validateEmailConfig();
		
		
		String urlToRecordId = GetURLForRecord.getURLForRecord( fastaImportTrackingDTO );
		
		String emailSubject = "Failed Import of file " + fastaImportTrackingDTO.getFilename();
		
		String emailBody = "Import of file " + fastaImportTrackingDTO.getFilename() 
				+ " has status '" + fastaImportTrackingDTO.getStatus() + "'.  \n" 
				+ " " + urlToRecordId + "\n"
				+ "Error message: " + generalImportErrorDTO.getMessage();
		

		SendEmailDTO sendEmailDTO = new SendEmailDTO();
		
		sendEmailDTO.setFromEmailAddress( GetEmailConfig.getFromAddress() );
		sendEmailDTO.setToEmailAddress( fastaImportTrackingDTO.getEmail() );
		
		sendEmailDTO.setEmailSubject( emailSubject );
		sendEmailDTO.setEmailBody( emailBody );
		
		SendEmail.getInstance().sendEmail( sendEmailDTO );
	}
}
