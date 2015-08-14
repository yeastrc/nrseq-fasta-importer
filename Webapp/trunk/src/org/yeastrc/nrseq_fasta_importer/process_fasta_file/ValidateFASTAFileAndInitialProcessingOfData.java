package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTADataErrorException;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.fasta.FASTAReader;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempDirForFileUploads;
import org.yeastrc.nrseq_fasta_importer.utils.FASTAValidator;

/**
 * Validate FASTA file syntax and no duplicate header names
 *
 */
public class ValidateFASTAFile {

	private static final Logger log = Logger.getLogger(ValidateFASTAFile.class);
	

	private ValidateFASTAFile() { }
	public static ValidateFASTAFile getInstance() { 
		return new ValidateFASTAFile(); 
	}
	

	/**
	 * @param fastaImportTrackingDTO
	 * @throws Exception 
	 */
	public ValidationResult validateFASTAFile( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws FASTAImporterDataErrorException, Exception {
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Starting Validating request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		
		ValidationResult validationResult = new ValidationResult();
		
		int sequenceCount = 0;
		
		Map<String, Long> headerNameLineNumberMap = new HashMap<>();
		
		
		FASTAImportTrackingDAO.getInstance().updateStatus( ImportStatusContants.STATUS_VALIDATION_STARTED, fastaImportTrackingDTO.getId() );
		
		FASTAReader fastaReader = null;
		
		try {
			
			File tempDir = GetTempDirForFileUploads.getInstance().getTempDirForFileUploads();
			
			File fastaFile = new File( tempDir, fastaImportTrackingDTO.getTempFilename() );
			
			fastaReader = FASTAReader.getInstance( fastaFile );
			
			
			while ( true ) {
				
				
				//  fastaReader.readNext()  throws exception for invalid data format
				
				FASTAEntry fastaEntry = null;
				
				try {
					fastaEntry = fastaReader.readNext();
					
				} catch ( FASTADataErrorException e ) {
					
					throw e;
					
				} catch ( Exception e ) {
					
//					log.error( "Exception", e );
					
					throw e;
				}
				
				if ( fastaEntry == null ) { 
					
					//  At End Of File
					
					break;  //  EARLY EXIT of LOOP
				}
				
				
				sequenceCount++;
				
			
				// the headers for this entry
				Set<FASTAHeader> headers = fastaEntry.getHeaders();
				
				StringBuffer sequence = fastaEntry.getSequence();

				if ( sequence.length() == 0 ) {


					String msg = "sequence length == zero for id: " + fastaImportTrackingDTO.getId() 
							+ ", header line number " + fastaEntry.getHeaderLineNumber();

					log.error( msg );
					throw new FASTAImporterDataErrorException( msg );
				}
				
				String sequenceString = sequence.toString();
				

				if ( ! FASTAValidator.validProteinSequence( sequenceString ) ) {

					String msg = "Invalid protein sequence"
							+ "for header line number " + fastaEntry.getHeaderLineNumber()
							+ ", sequence: " + sequenceString;

					log.error( msg );

					throw new FASTAImporterDataErrorException( msg );
				}
				
				
				
				for ( FASTAHeader header : headers ) {
					
					String headerName = header.getName();
//					String headerDescription = header.getDescription();
					
					if ( "YOR133W_W303".equals(headerName)) {
						
						int z = 0;
					}
					
					Long lineNumberFromMap = headerNameLineNumberMap.get( headerName );
					
					if ( lineNumberFromMap != null ) {

						String msg = "Header name '" + headerName + "' in the file more than once at line numbers "
								+ lineNumberFromMap
								+ " and " + fastaEntry.getHeaderLineNumber()
								+ ".";

						log.error( msg );

						throw new FASTAImporterDataErrorException( msg );
					}
					
					headerNameLineNumberMap.put( headerName, new Long( fastaEntry.getHeaderLineNumber() ) );
				}
				
			}
			
			

			String newStatus = ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

			fastaImportTrackingDTO.setStatus( newStatus );
			
			
		} catch ( FASTADataErrorException e ) {
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			

			String newStatus = ImportStatusContants.STATUS_VALIDATION_FAILED;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
			fastaImportTrackingDTO.setStatus( newStatus );
			

			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
			}

			log.error( "Data Exception", e );
			
			throw new FASTAImporterDataErrorException( e.getMessage() );

		} catch ( FASTAImporterDataErrorException e ) {
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			
			String newStatus = ImportStatusContants.STATUS_VALIDATION_FAILED;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
			fastaImportTrackingDTO.setStatus( newStatus );
			
			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
			}

			throw e;
			
		} catch ( Exception e ) {
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( "System Error" );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);

			String newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
			fastaImportTrackingDTO.setStatus( newStatus );
			
			log.error( "Exception", e );
			
			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailSystemError.getInstance().sendEmailSystemError( fastaImportTrackingDTO );
			}
			
			throw e;
		
		} finally {
			
			if ( fastaReader != null ) {
				
				try {
					
					fastaReader.close();
				} catch ( Exception e ) {

					log.error( "Exception closing fasta file", e );
				}
			}
			
		}
		
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Finished Validating request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		
		
		validationResult.setSequenceCount( sequenceCount );


		return validationResult;
	}
	
	
			
			
}
