package org.yeastrc.nrseq_fasta_importer.uploaded_file;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ConfigSystemsKeysConstants;
import org.yeastrc.nrseq_fasta_importer.dao.ConfigSystemDAO;

/**
 * 
 *
 */
public class GetTempDirForFileUploads {
	
	
	private static final Logger log = Logger.getLogger(GetTempDirForFileUploads.class);

	//  private constructor
	private GetTempDirForFileUploads() { }
	
	/**
	 * @return newly created instance
	 */
	public static GetTempDirForFileUploads getInstance() { 
		return new GetTempDirForFileUploads(); 
	}

	/**
	 * @return true if valid, otherwise throws exception
	 * @throws Exception
	 */
	public boolean validateTempDirForFileUploads() throws Exception {
		
		File tempDir = getTempDirForFileUploads();
				
		return true;
	}
	
	/**
	 * @return File pointing to temp dir, otherwise throws Exception
	 * @throws Exception
	 */
	public File getTempDirForFileUploads() throws Exception {
		

		String tempDirString = null; 
				
		try {
			tempDirString = ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.UPLOADED_FILES_TEMP_DIRECTORY_KEY );
			

		} catch (Exception e ) {

			String msg = "Exception getting file upload temp dir for validation";
			log.error( msg, e );
			
			throw e;
		}
		
		
		if ( StringUtils.isEmpty(tempDirString) ) {
			
			String msg = "Upload files temp Dir not found in configuration table. config_key: " 
					+ ConfigSystemsKeysConstants.UPLOADED_FILES_TEMP_DIRECTORY_KEY ;
			
			log.error( msg );
			throw new Exception(msg);
		}
		
		File tempDir = new File( tempDirString );
		
		if ( ! tempDir.exists() ) {

			String msg = "Upload files temp dir does not exist for 'config_key' in configuration table : '" 
					+ ConfigSystemsKeysConstants.UPLOADED_FILES_TEMP_DIRECTORY_KEY 
					+ "'.  Upload files temp dir: " + tempDir.getAbsolutePath();
			
			log.error( msg );
			throw new Exception(msg);
		}
		
		return tempDir;
		
	}
}
