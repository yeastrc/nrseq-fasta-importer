package org.yeastrc.nrseq_fasta_importer.process_fasta_file;


import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.exception.DuplicateFilenameException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;

/**
 * 
 *
 */
public class ProcessImportFASTAFile {

	private static final Logger log = Logger.getLogger(ProcessImportFASTAFile.class);

	
	private static final int CURRENT_ENTRY_COUNT_NOT_SET = -1;
	
	private static enum ImporterState {
		
		NOT_IMPORTING, VALIDATING, CHECKING_TAXONOMY_IDS, IMPORTING
	}
	
	
	
	
	private volatile FASTAImportTrackingDTO currentFastaImportTrackingDTO = null;
	
	private volatile CheckFASTATaxonomyIds currentCheckFASTATaxonomyIds;
	private volatile ImportFASTAFile currentImportFASTAFile;

	private volatile int currentFASTAEntryCount = CURRENT_ENTRY_COUNT_NOT_SET;
	
	private volatile ImporterState importerState = ImporterState.NOT_IMPORTING;
	

	private ProcessImportFASTAFile() { }
	public static ProcessImportFASTAFile getInstance() { 
		return new ProcessImportFASTAFile(); 
	}
	

	public int getCurrentSequenceCount() {
		
		if ( importerState == ImporterState.CHECKING_TAXONOMY_IDS ) {
			
			return currentCheckFASTATaxonomyIds.getCurrentSequenceCount();
			
		} else if ( importerState == ImporterState.IMPORTING ) {
			
			return currentImportFASTAFile.getCurrentSequenceCount();
			
		} else {
			
			return 0;
		}
	}
	
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void processNextFASTAFile() throws Exception {
		
		
		while ( true ) {  //  process loop until getNextQueued() returns null

			try {


				//  Process next FASTA file


				try {

					currentFastaImportTrackingDTO = FASTAImportTrackingDAO.getInstance().getNextQueued();

				} catch ( Exception e ) {

					log.error( "Exception getting next queued FASTA file to process", e );

					throw e;
				}

				if ( currentFastaImportTrackingDTO == null ) {

					break;   //  EARLY LOOP EXIT

				} else {


					ValidationResult validationResult = null;


					if ( ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

						//  throws exception if error encountered
						validationResult = ValidateFASTAFile.getInstance().validateFASTAFile( currentFastaImportTrackingDTO );
						
						//  validation passed

						FASTAImportTrackingDAO.getInstance().updateFastaEntryCount( validationResult.getSequenceCount(), currentFastaImportTrackingDTO.getId() );

						currentFastaImportTrackingDTO.setFastaEntryCount( validationResult.getSequenceCount() );
					}					

					
					
					currentFASTAEntryCount = currentFastaImportTrackingDTO.getFastaEntryCount();


					//  currentFastaImportTrackingDTO.status set to ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS
					//             if validation passes

					if ( ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

						currentCheckFASTATaxonomyIds = CheckFASTATaxonomyIds.getInstance(); 

						importerState = ImporterState.CHECKING_TAXONOMY_IDS;

//						CheckFASTATaxonomyIdsResult checkFASTATaxonomyIdsResult =
								currentCheckFASTATaxonomyIds.checkFASTATaxonomyIds( currentFastaImportTrackingDTO );

						currentCheckFASTATaxonomyIds = null;
						
					}
					

					//  currentFastaImportTrackingDTO.status set to ImportStatusContants.STATUS_QUEUED_FOR_IMPORT
					//             if Find Taxonomy Ids finds all taxonomy Ids

					if ( ImportStatusContants.STATUS_QUEUED_FOR_IMPORT.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

						currentImportFASTAFile = ImportFASTAFile.getInstance(); 

						importerState = ImporterState.IMPORTING;

						currentImportFASTAFile.importFASTAFile( currentFastaImportTrackingDTO );
					}
				}

				

			} catch ( FASTAImporterDataErrorException e ) {


			} catch ( DuplicateFilenameException e ) {


			} catch ( Exception e ) {

				if ( currentFastaImportTrackingDTO != null ) {

					//  Probably already set this status but do again

					GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
					
					generalImportErrorDTO.setFastaImportTrackingId( currentFastaImportTrackingDTO.getId() );
					generalImportErrorDTO.setMessage( "System Error" );
					
					GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);

					SendEmailSystemError.getInstance().sendEmailSystemError( currentFastaImportTrackingDTO );
					

				} else {




				}

				log.error( "Exception", e );

				throw e;

			} finally {

				importerState = ImporterState.NOT_IMPORTING;

				currentFastaImportTrackingDTO = null;

				currentCheckFASTATaxonomyIds = null;

				currentImportFASTAFile = null;
			}
			
		}
		
	}
	
	
	
	
	public FASTAImportTrackingDTO getCurrentFastaImportTrackingDTO() {
		return currentFastaImportTrackingDTO;
	}
	
	
	public int getCurrentFASTAEntryCount() {
		return currentFASTAEntryCount;
	}
	
}
