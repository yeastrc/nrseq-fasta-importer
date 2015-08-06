package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinSequenceDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblDatabaseDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinDatabaseDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinSequenceDTO;
import org.yeastrc.nrseq_fasta_importer.exception.DuplicateFilenameException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileHeaderEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.writer_reader.ImportFileReader;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailImportComplete;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempDirForFileUploads;

/**
 * 
 *
 */
public class ImportFASTAFile {

	private static final Logger log = Logger.getLogger(ImportFASTAFile.class);

	private ImportFASTAFile() { }
	public static ImportFASTAFile getInstance() { 
		return new ImportFASTAFile(); 
	}
	
	
	
	private volatile int currentSequenceCount;

	public int getCurrentSequenceCount() {
		return currentSequenceCount;
	}
	
	

	
	/**
	 * @param fastaImportTrackingDTO
	 * @throws Exception 
	 */
	public void importFASTAFile( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws FASTAImporterDataErrorException, Exception {
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Started Importing request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		

		
		String newStatus = ImportStatusContants.STATUS_IMPORT_STARTED;

		fastaImportTrackingDTO.setStatus( newStatus );
		
		FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

		
		YRC_NRSEQ_tblDatabaseDTO yrc_NRSEQ_tblDatabaseDTO = null;
		
		ImportFileReader importFileReader = null;
		
		try {
			
			File tempDir = GetTempDirForFileUploads.getInstance().getTempDirForFileUploads();
			
			File importFile = new File( tempDir, fastaImportTrackingDTO.getTempFilenameForImport() );
			
			importFileReader = ImportFileReader.getInstance( importFile );
			
			yrc_NRSEQ_tblDatabaseDTO = new YRC_NRSEQ_tblDatabaseDTO();
			
			yrc_NRSEQ_tblDatabaseDTO.setName( fastaImportTrackingDTO.getFilename() );
			yrc_NRSEQ_tblDatabaseDTO.setDescription( fastaImportTrackingDTO.getDescription() );
			
			try {
			
				YRC_NRSEQ_tblDatabaseDAO.getInstance().save( yrc_NRSEQ_tblDatabaseDTO );
				
			} catch ( SQLException e ) {
				
				String exceptionMessage = e.getMessage();
				
				if ( exceptionMessage.contains( "Duplicate entry" ) ) {
					
					throw new DuplicateFilenameException( "The filename '" + fastaImportTrackingDTO.getFilename() + "' already exists in the database." );
				}
				
				throw e;
			
			} catch ( Exception e ) {
				
				throw e;
			}
			
			//  Immediately store the yrc_NRSEQ_tblDatabaseDTO.id in the FASTAImportTracking record
			FASTAImportTrackingDAO.getInstance().update_yrc_nrseq_tblDatabase_id( yrc_NRSEQ_tblDatabaseDTO.getId(), fastaImportTrackingDTO.getId() );
			
			
			while ( true ) {
				
				
				//  fastaReader.readNext()  throws exception for invalid data format
				
				ImportFileEntry importFileEntry = null;
				
				try {
					importFileEntry = importFileReader.readNext();
					
					
				} catch ( Exception e ) {
					
//					log.error( "Exception", e );
					
					throw e;
				}
				
				if ( importFileEntry == null ) { 
					
					//  At End Of File
					
					break;  //  EARLY EXIT of LOOP
				}
				

				currentSequenceCount++;
				
				
				
				String sequenceString = importFileEntry.getSequence();

				if ( sequenceString.length() == 0 ) {
					
					log.error( "sequence length == zero for id: " + fastaImportTrackingDTO.getId() 
							+ ", header line number " + importFileEntry.getHeaderLineNumber() );
				}
				
				
				YRC_NRSEQ_tblProteinSequenceDTO yrc_NRSEQ_tblProteinSequenceDTO =
						YRC_NRSEQ_tblProteinSequenceDAO.getInstance().insertOrRetrieve( sequenceString );
				
				
				List<ImportFileHeaderEntry> importFileHeaderEntryList = importFileEntry.getImportFileHeaderEntryList();
			
				if ( importFileHeaderEntryList == null ) {
					
					String msg = "Error: importFileHeaderEntryList == null.  fastaImportTrackingDTO.id: " + fastaImportTrackingDTO.getId();
					
					log.error( msg );
					
					throw new Exception(msg);
				}
				
				for ( ImportFileHeaderEntry importFileHeaderEntry : importFileHeaderEntryList ) {
					
					String headerName = importFileHeaderEntry.getHeaderName();
					String headerDescription = importFileHeaderEntry.getHeaderDescription();
					
					int taxonomyId = importFileHeaderEntry.getTaxonomyId();
					
					int sequenceID = yrc_NRSEQ_tblProteinSequenceDTO.getId();
					int speciesID = taxonomyId;
					
					YRC_NRSEQ_tblProteinDTO yrc_NRSEQ_tblProteinDTO =
							YRC_NRSEQ_tblProteinDAO.getInstance().getForSequenceIdSpeciesId( sequenceID, speciesID );
				
					if ( yrc_NRSEQ_tblProteinDTO == null ){
						
						yrc_NRSEQ_tblProteinDTO = new YRC_NRSEQ_tblProteinDTO();
						
						yrc_NRSEQ_tblProteinDTO.setSequenceID( sequenceID );
						yrc_NRSEQ_tblProteinDTO.setSpeciesID( speciesID );
						
						YRC_NRSEQ_tblProteinDAO.getInstance().save( yrc_NRSEQ_tblProteinDTO );
					}
					
					YRC_NRSEQ_tblProteinDatabaseDTO yrc_NRSEQ_tblProteinDatabaseDTO = new YRC_NRSEQ_tblProteinDatabaseDTO();
					
					yrc_NRSEQ_tblProteinDatabaseDTO.setProteinID( yrc_NRSEQ_tblProteinDTO.getId() );
					yrc_NRSEQ_tblProteinDatabaseDTO.setDatabaseID( yrc_NRSEQ_tblDatabaseDTO.getId() );
					yrc_NRSEQ_tblProteinDatabaseDTO.setAccessionString( headerName );
					yrc_NRSEQ_tblProteinDatabaseDTO.setDescription( headerDescription );
					
					YRC_NRSEQ_tblProteinDatabaseDAO.getInstance().save( yrc_NRSEQ_tblProteinDatabaseDTO );
				}
			}
			
			

			newStatus = ImportStatusContants.STATUS_IMPORT_COMPLETE;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
	
			
			
			UpdateStatusFailedDuplicateFile.getInstance().updateStatusFailedDuplicateFile( fastaImportTrackingDTO.getFilename() );
			

			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailImportComplete.getInstance().sendEmailImportComplete( fastaImportTrackingDTO );
			}
			
			if ( log.isInfoEnabled() ) {
				
				log.info( "Finished Importing request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
			}
			

		} catch ( DuplicateFilenameException e ) {


			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			
			
			newStatus = ImportStatusContants.STATUS_IMPORT_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

			

			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
			}
			
			
			throw e; 
			
		} catch ( Exception e ) {
			
			if ( yrc_NRSEQ_tblDatabaseDTO != null && yrc_NRSEQ_tblDatabaseDTO.getId() != 0 ) {

				try {

					deleteNRSeqInsertedData( yrc_NRSEQ_tblDatabaseDTO.getId() );


				} catch ( Exception eCleanup ) {

					log.error( "Exception cleaning up after failed Import: for request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename(), e );
				}

			}
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			
			

			newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

			log.error( "Exception for request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename(), e );
			
			
			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailSystemError.getInstance().sendEmailSystemError( fastaImportTrackingDTO );
			}
			
			throw e;
		
		} finally {
			
			if ( importFileReader != null ) {
				
				try {
					
					importFileReader.close();
				} catch ( Exception e ) {

					log.error( "Exception closing importFileReader file", e );
				}
			}
			
		}
	}
	
	
	/**
	 * @param tblProteinDatabase_id
	 * @throws Exception 
	 */
	private void deleteNRSeqInsertedData( int tblProteinDatabase_id ) throws Exception {
		
		YRC_NRSEQ_tblProteinDatabaseDAO.getInstance().deleteFor_tblProteinDatabase_id( tblProteinDatabase_id );

		YRC_NRSEQ_tblDatabaseDAO.getInstance().deleteForId( tblProteinDatabase_id ); 		
		
	}

}
