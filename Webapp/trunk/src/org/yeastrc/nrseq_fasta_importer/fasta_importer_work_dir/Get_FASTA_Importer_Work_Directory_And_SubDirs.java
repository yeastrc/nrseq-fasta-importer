package org.yeastrc.nrseq_fasta_importer.fasta_importer_work_dir;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ConfigSystemsKeysConstants;
import org.yeastrc.nrseq_fasta_importer.constants.FileNameAndDirectoryNameConstants;
import org.yeastrc.nrseq_fasta_importer.dao.ConfigSystemDAO;

/**
 * 
 *
 */
public class Get_FASTA_Importer_Work_Directory_And_SubDirs {
	
	
	private static final Logger log = Logger.getLogger(Get_FASTA_Importer_Work_Directory_And_SubDirs.class);

	//  private constructor
	private Get_FASTA_Importer_Work_Directory_And_SubDirs() { }
	
	/**
	 * @return newly created instance
	 */
	public static Get_FASTA_Importer_Work_Directory_And_SubDirs getInstance() { 
		return new Get_FASTA_Importer_Work_Directory_And_SubDirs(); 
	}

	/**
	 * @return true if valid, otherwise throws exception
	 * @throws Exception
	 */
	public boolean validate_FASTA_Importer_Work_Directory() throws Exception {
		
		File tempDir = get_FASTA_Importer_Work_Directory();
				
		return true;
	}
	
	/**
	 * @return File pointing to temp dir, otherwise throws Exception
	 * @throws Exception
	 */
	public File get_FASTA_Importer_Work_Directory() throws Exception {
		

		String tempDirString = null; 
				
		try {
			tempDirString = ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.FASTA_IMPORTER_WORK_DIRECTORY_KEY );
			

		} catch (Exception e ) {

			String msg = "Exception getting file upload temp dir for validation";
			log.error( msg, e );
			
			throw e;
		}
		
		
		if ( StringUtils.isEmpty(tempDirString) ) {
			
			String msg = "FASTA_Importer_Work_Directory not found in configuration table. config_key: " 
					+ ConfigSystemsKeysConstants.FASTA_IMPORTER_WORK_DIRECTORY_KEY ;
			
			log.error( msg );
			throw new Exception(msg);
		}
		
		File tempDir = new File( tempDirString );
		
		if ( ! tempDir.exists() ) {

			String msg = "FASTA_Importer_Work_Directory does not exist for 'config_key' in configuration table : '" 
					+ ConfigSystemsKeysConstants.FASTA_IMPORTER_WORK_DIRECTORY_KEY 
					+ "'.  FASTA_Importer_Work_Directory: " + tempDir.getAbsolutePath();
			
			log.error( msg );
			throw new Exception(msg);
		}
		
		return tempDir;
		
	}
	
	
	/**
	 * @return
	 */
	public String getDirForUploadFileTempDir( ) {

		String dirName = FileNameAndDirectoryNameConstants.UPLOAD_FILE_TEMP_DIR;
		
		return dirName;
	}
	
	
	/**
	 * @return
	 */
	public String getDirForImportTrackingId( int importTrackingId ) {

		String dirName = FileNameAndDirectoryNameConstants.IMPORT_DIR_FOR_ID + importTrackingId;
		
		return dirName;
	}
	
	
}
