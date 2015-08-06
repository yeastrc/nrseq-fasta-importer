package org.yeastrc.nrseq_fasta_importer.uploaded_file;

import java.io.File;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dao.TempUploadFileIdCreatorDAO;

/**
 * Returns File object for local file that uploaded file will be saved as.
 *
 */
public class GetTempLocalFileForUploadedFile {
	
	private static final Logger log = Logger.getLogger(GetTempLocalFileForUploadedFile.class);

	//  private constructor
	private GetTempLocalFileForUploadedFile() { }
	
	/**
	 * @return newly created instance
	 */
	public static GetTempLocalFileForUploadedFile getInstance() { 
		return new GetTempLocalFileForUploadedFile(); 
	}
	
	
	/**
	 * @return
	 * @throws Exception
	 */
	public GetTempLocalFileForUploadedFileResult getTempLocalFileForUploadedFile() throws Exception {
		
		GetTempLocalFileForUploadedFileResult getTempLocalFileForUploadedFileResult = new GetTempLocalFileForUploadedFileResult();


		int nextTempFileId = TempUploadFileIdCreatorDAO.getInstance().getNextId();
		
		String tempFilename = "FASTA_to_process_" + nextTempFileId + ".fasta";
		String tempFilenameForImport = "FASTA_to_process_"  + nextTempFileId + "_to_import.xml";
		

		getTempLocalFileForUploadedFileResult.setTempFilename( tempFilename );
		getTempLocalFileForUploadedFileResult.setTempFilenameForImport( tempFilenameForImport );
		
		return getTempLocalFileForUploadedFileResult;
	}
	
	
	
	
}
