package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;

/**
 * update status to failed for already inserted filename for any files "in progress" 
 * that have same filename that was just successfully imported
 *
 */
public class UpdateStatusFailedDuplicateFile {

	private static final Logger log = Logger.getLogger(UpdateStatusFailedDuplicateFile.class);

	private UpdateStatusFailedDuplicateFile() { }
	public static UpdateStatusFailedDuplicateFile getInstance() { 
		return new UpdateStatusFailedDuplicateFile(); 
	}
	
	/**
	 * 
	 * @param filename
	 * @throws Exception 
	 */
	public void updateStatusFailedDuplicateFile( String filename ) throws Exception {
		


		try {

			List<FASTAImportTrackingDTO>  inProgressSameFilenameList = 
					FASTAImportTrackingDAO.getInstance().getAllInProgressForFilename( filename );


			String errorMsg = "The filename '" + filename + "' already exists in the database.";

			for ( FASTAImportTrackingDTO fastaImportTrackingDTO : inProgressSameFilenameList ) {

				GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();

				generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
				generalImportErrorDTO.setMessage( errorMsg );

				GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);


				String newStatus = ImportStatusContants.STATUS_IMPORT_FAILED;


				FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );



				if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

					SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
				}
			}

		} catch ( Exception e ) {
			
			
			throw e;
		}
		
		
	}
}



