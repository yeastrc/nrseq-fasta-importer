package org.yeastrc.nrseq_fasta_importer.uploaded_file;


import org.apache.log4j.Logger;

/**
 * Returns File object for local file that uploaded file will be saved as.
 *
 */
public class GetTempLocalFilenameForTempFilenameNumber {
	
	private static final Logger log = Logger.getLogger(GetTempLocalFilenameForTempFilenameNumber.class);

	//  private constructor
	private GetTempLocalFilenameForTempFilenameNumber() { }
	
	/**
	 * @return newly created instance
	 */
	public static GetTempLocalFilenameForTempFilenameNumber getInstance() { 
		return new GetTempLocalFilenameForTempFilenameNumber(); 
	}
	
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getTempLocalFileForUploadedFile( int tempFilenameNumber ) {

		String tempFilename = "FASTA_to_process_" + tempFilenameNumber + "_uploaded_fasta.fasta";
		
		return tempFilename;
	}
	
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getTempLocalFileForImport( int tempFilenameNumber ) {
		
		String tempFilenameForImport = "FASTA_to_process_"  + tempFilenameNumber + "_to_import.xml";

		return tempFilenameForImport;
	}
	
	
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getTempLocalFileForGetTaxonomyIdsProcessing( int tempFilenameNumber ) {
		
		String tempFilenameForImport = "FASTA_to_process_"  + tempFilenameNumber + "_to_get_taxonomy_ids.xml";

		return tempFilenameForImport;
	}
	
	
	
}
