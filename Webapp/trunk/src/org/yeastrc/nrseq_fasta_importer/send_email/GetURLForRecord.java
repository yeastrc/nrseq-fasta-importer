package org.yeastrc.nrseq_fasta_importer.send_email;

import org.yeastrc.nrseq_fasta_importer.constants.StrutsActionPathsConstants;
import org.yeastrc.nrseq_fasta_importer.constants.WebConstants;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.server_url.GetServerURLConfig;
import org.yeastrc.nrseq_fasta_importer.www.servlet_context.CurrentContext;

/**
 * 
 *
 */
public class GetURLForRecord {

	/**
	 * @param fastaImportTrackingDTO
	 * @return
	 * @throws Exception
	 */
	public static String getURLForRecord( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception {
		

		GetServerURLConfig.validateServerURLConfig();
		
		String serverURL = GetServerURLConfig.getServerURL();
				
		String urlToRecordId = 
				serverURL + 
				CurrentContext.getCurrentWebAppContext() 
				+ StrutsActionPathsConstants.HOME
				+ "?"
				+ WebConstants.PARAMETER_FASTA_IMPORT_TRACKING_ID 
				+ "="
				+ fastaImportTrackingDTO.getId();
		
		return urlToRecordId;
	}
}
