package org.yeastrc.nrseq_fasta_importer.process_fasta_file;


import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDuplicateFilenameException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterRemoteWebserviceCallErrorException;

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
	
	private volatile ValidateFASTAFileAndInitialProcessingOfData currentValidateFASTAFile;
	private volatile CheckFASTATaxonomyIds currentCheckFASTATaxonomyIds;
	private volatile ImportFASTAFile currentImportFASTAFile;

	private volatile int currentFASTAEntryCount = CURRENT_ENTRY_COUNT_NOT_SET;
	
	private volatile ImporterState importerState = ImporterState.NOT_IMPORTING;

	private volatile boolean keepRunning = true;
	
	

	private ProcessImportFASTAFile() { }
	public static ProcessImportFASTAFile getInstance() { 
		return new ProcessImportFASTAFile(); 
	}
	


	/**
	 * awaken thread to process request, calls "notify()"
	 */
	public void awaken() {

		if ( log.isDebugEnabled() ) {

			log.debug("awaken() called:  " );
		}

		synchronized (this) {
			
			notify();
		}

	}




	/**
	 * shutdown was received from the operating system.  This is called on a different thread.
	 */
	public void shutdown() {


		log.info("shutdown() called");


		synchronized (this) {

			this.keepRunning = false;

		}


		//  awaken this thread if it is in 'wait' state ( not currently processing a job )

		this.awaken();
	}


	
	
	
	/**
	 * @return
	 */
	public int getCurrentSequenceCount() {

		if ( importerState == ImporterState.VALIDATING && currentValidateFASTAFile != null ) {
			
			return currentValidateFASTAFile.getCurrentSequenceCount();
			
		} else if ( importerState == ImporterState.CHECKING_TAXONOMY_IDS && currentCheckFASTATaxonomyIds != null ) {
			
			return currentCheckFASTATaxonomyIds.getCurrentSequenceCount();
			
		} else if ( importerState == ImporterState.IMPORTING && currentImportFASTAFile != null ) {
			
			return currentImportFASTAFile.getCurrentSequenceCount();
			
		} else {
			
			return 0;
		}
	}
	
	
	/**
	 * @return
	 */
	public int getCurrentFASTAEntryCount() {

		if ( importerState == ImporterState.VALIDATING && currentValidateFASTAFile != null ) {
			
			return currentValidateFASTAFile.getTotalSequenceCount();
			
		} else {
		
			return currentFASTAEntryCount;
		}
	}

	
	/**
	 * @return
	 */
	public FASTAImportTrackingDTO getCurrentFastaImportTrackingDTO() {

		return currentFastaImportTrackingDTO;
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



					if ( ImportStatusContants.STATUS_VALIDATION_STARTED.equals( currentFastaImportTrackingDTO.getStatus() ) ) {
						
						importerState = ImporterState.VALIDATING;

						currentValidateFASTAFile = ValidateFASTAFileAndInitialProcessingOfData.getInstance();
						
						//  throws exception if error encountered
						currentValidateFASTAFile.validateFASTAFile( currentFastaImportTrackingDTO );
						
						//  validation passed
						
						currentValidateFASTAFile = null;
						


						String newStatus = ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED;

						currentFastaImportTrackingDTO.setStatus( newStatus );

						FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, currentFastaImportTrackingDTO.getId() );


					}					

					
					
					currentFASTAEntryCount = currentFastaImportTrackingDTO.getFastaEntryCount();


					//  currentFastaImportTrackingDTO.status set to ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS
					//             if validation passes

					if ( ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

						currentCheckFASTATaxonomyIds = CheckFASTATaxonomyIds.getInstance(); 

						importerState = ImporterState.CHECKING_TAXONOMY_IDS;

//						CheckFASTATaxonomyIdsResult checkFASTATaxonomyIdsResult =
								currentCheckFASTATaxonomyIds.checkFASTATaxonomyIds( currentFastaImportTrackingDTO );

						currentCheckFASTATaxonomyIds = null;
						
						
						//  Removed since now in currentCheckFASTATaxonomyIds.checkFASTATaxonomyIds(...)

//						String newStatus = ImportStatusContants.STATUS_IMPORT_STARTED;
//						
//						if ( currentFastaImportTrackingDTO.isRequire_confirm_before_insert() ) {
//
//							newStatus = ImportStatusContants.STATUS_USER_CONFIRMATION_REQUIRED;
//						}
//						
//						currentFastaImportTrackingDTO.setStatus( newStatus );
//
//						FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, currentFastaImportTrackingDTO.getId() );

					}
					

					//  currentFastaImportTrackingDTO.status set to ImportStatusContants.STATUS_QUEUED_FOR_IMPORT
					//             if Find Taxonomy Ids finds all taxonomy Ids

					if ( ImportStatusContants.STATUS_IMPORT_STARTED.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

						currentImportFASTAFile = ImportFASTAFile.getInstance(); 

						importerState = ImporterState.IMPORTING;

						currentImportFASTAFile.importFASTAFile( currentFastaImportTrackingDTO );
					}
				}

				

			} catch ( FASTAImporterDataErrorException e ) {


			} catch ( FASTAImporterDuplicateFilenameException e ) {
				
				
			} catch ( FASTAImporterRemoteWebserviceCallErrorException e ) {

				synchronized (this) {

					try {
						
						wait( 10000 );  //  sleep 10 seconds so don't quickly and repeatedly generate errors.

					} catch (InterruptedException e2) {

						log.info("wait() interrupted with InterruptedException");

					}
				}

			} catch ( Exception e ) {

				if ( currentFastaImportTrackingDTO != null ) {


				} else {




				}

				log.error( "Exception", e );
				

				synchronized (this) {

					try {
						
						wait( 10000 );  //  sleep 10 seconds so don't quickly and repeatedly generate system errors.

					} catch (InterruptedException e2) {

						log.info("wait() interrupted with InterruptedException");

					}
				}

//				throw e;

			} finally {

				importerState = ImporterState.NOT_IMPORTING;

				currentFastaImportTrackingDTO = null;

				currentValidateFASTAFile = null;
				
				currentCheckFASTATaxonomyIds = null;

				currentImportFASTAFile = null;
			}
			
		}
		
	}
	
	
	
}
