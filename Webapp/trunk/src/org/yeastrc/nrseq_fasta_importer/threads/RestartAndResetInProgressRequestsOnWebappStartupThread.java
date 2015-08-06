package org.yeastrc.nrseq_fasta_importer.threads;

import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;

/**
 * 
 *
 */
public class RestartAndResetInProgressRequestsOnWebappStartupThread extends Thread {

	private static final Logger log = Logger.getLogger(RestartAndResetInProgressRequestsOnWebappStartupThread.class);

	private RestartAndResetInProgressRequestsOnWebappStartupThread() { }
	public static RestartAndResetInProgressRequestsOnWebappStartupThread getInstance() { 
		return new RestartAndResetInProgressRequestsOnWebappStartupThread(); 
	}

	/**
	 * 
	 */
	public void run() {


		FASTAImportTrackingDAO fastaImportTrackingDAO = FASTAImportTrackingDAO.getInstance();

		List<FASTAImportTrackingDTO>  inProgressList = null;

		try {
			inProgressList = fastaImportTrackingDAO.getAllInProgress();

		} catch ( Exception e ) {

			log.error( "Exception Getting In Progress items that need to be restarted at web app startup", e );
		}
		
		if ( inProgressList != null && ( ! inProgressList.isEmpty() ) ) {

			for ( FASTAImportTrackingDTO item : inProgressList ) {

				try {

					if ( ImportStatusContants.STATUS_VALIDATION_STARTED.equals( item.getStatus() ) ) {

						fastaImportTrackingDAO.updateStatus( ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION, item.getId() );

					} else if ( ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED.equals( item.getStatus() ) ) {

						fastaImportTrackingDAO.updateStatus( ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS, item.getId() );

					} else if ( ImportStatusContants.STATUS_IMPORT_STARTED.equals( item.getStatus() ) ) {
						
						if ( item.getYrc_nrseq_tblDatabase_id() == null ) {
							
							log.error( "item.getYrc_nrseq_tblDatabase_id() == null for item.id: " + item.getId() );
						} else {

							YRC_NRSEQ_tblProteinDatabaseDAO.getInstance().deleteFor_tblProteinDatabase_id( item.getYrc_nrseq_tblDatabase_id() );

							YRC_NRSEQ_tblDatabaseDAO.getInstance().deleteForId( item.getYrc_nrseq_tblDatabase_id() );
						
						}

						fastaImportTrackingDAO.updateStatus( ImportStatusContants.STATUS_QUEUED_FOR_IMPORT, item.getId() );
					}


				} catch ( Exception e ) {

					log.error( "Exception resetting in progress item  to be restarted at web app startup" + item, e );
				}

			}
		
			ProcessImportFASTAFileThread.getInstance().awaken();
		}

	}
}
