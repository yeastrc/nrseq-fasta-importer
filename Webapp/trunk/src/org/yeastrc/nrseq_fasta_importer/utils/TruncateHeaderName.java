package org.yeastrc.nrseq_fasta_importer.utils;

import org.yeastrc.nrseq_fasta_importer.constants.FASTA_DataTruncationConstants;

public class TruncateHeaderName {

	/**
	 * Truncate header name, if needed
	 * 
	 * @param headerName
	 * @return
	 */
	public static String truncateHeaderName( String headerName ) {
		
		
		//  Truncate header name, if needed
		
		if ( headerName != null ) {
			
			if ( headerName.length() > FASTA_DataTruncationConstants.HEADER_NAME_MAX_LENGTH ) {
				
				headerName = headerName.substring( 0, FASTA_DataTruncationConstants.HEADER_NAME_MAX_LENGTH );
			}

		}
		
		return headerName;
	}
}
