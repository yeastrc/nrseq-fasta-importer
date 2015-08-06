package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTADataErrorException;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.fasta.FASTAReader;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.constants.TaxonomyIdNotFoundMaxCountConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileHeaderEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.writer_reader.ImportFileWriter;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailUserAttentionRequired;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyId;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempDirForFileUploads;
import org.yeastrc.nrseq_fasta_importer.utils.FASTAValidator;

/**
 * Get Taxonomy Ids for all FASTA headers
 *
 */
public class CheckFASTATaxonomyIds {

	private static final Logger log = Logger.getLogger(CheckFASTATaxonomyIds.class);

	private CheckFASTATaxonomyIds() { }
	public static CheckFASTATaxonomyIds getInstance() { 
		return new CheckFASTATaxonomyIds(); 
	}
	
	
	
	private volatile int currentSequenceCount;

	public int getCurrentSequenceCount() {
		return currentSequenceCount;
	}
	
	
	

	/**
	 * @param fastaImportTrackingDTO
	 * @throws Exception 
	 */
	public CheckFASTATaxonomyIdsResult checkFASTATaxonomyIds( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws FASTAImporterDataErrorException, Exception {
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Starting Checking FASTA Taxonomy Ids, request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		
		CheckFASTATaxonomyIdsResult checkFASTATaxonomyIdsResult = new CheckFASTATaxonomyIdsResult();
		
		boolean headerMatchesNotFound = false;
		
		int taxonomyIdNotFoundMaxCount = 0;
		
		
		String newStatus = ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED;

		fastaImportTrackingDTO.setStatus( newStatus );
		
		FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
		
		FASTAReader fastaReader = null;
		
		ImportFileWriter importFileWriter = null;
		
		try {
			
			File tempDir = GetTempDirForFileUploads.getInstance().getTempDirForFileUploads();
			
			File fastaFile = new File( tempDir, fastaImportTrackingDTO.getTempFilename() );
			
			File importFile = new File( tempDir, fastaImportTrackingDTO.getTempFilenameForImport() );
			
			fastaReader = FASTAReader.getInstance( fastaFile );
			
			importFileWriter = ImportFileWriter.getInstance( importFile );
			
			
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
				
				
				currentSequenceCount++;
				
			
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
				
				
				ImportFileEntry importFileEntry = new ImportFileEntry();
				
				importFileEntry.setHeaderLineNumber( fastaEntry.getHeaderLineNumber() );
				importFileEntry.setSequence( sequenceString );
				
				List<ImportFileHeaderEntry> importFileHeaderEntryList = new ArrayList<>();
				
				importFileEntry.setImportFileHeaderEntryList( importFileHeaderEntryList );
				
				for ( FASTAHeader header : headers ) {
					
					String headerName = header.getName();
					String headerDescription = header.getDescription();
					
					try {

						//  taxonomyId which is the speciesID field in the tblProteinTable
						DetermineTaxonomyIdResult determineTaxonomyIdResult = 
								DetermineTaxonomyId.getInstance().getTaxonomyId( header, fastaEntry, fastaImportTrackingDTO );

						Integer taxonomyId = determineTaxonomyIdResult.getTaxonomyId();

						if ( taxonomyId == null ) {
							
							taxonomyIdNotFoundMaxCount++;
							
							if ( taxonomyIdNotFoundMaxCount > TaxonomyIdNotFoundMaxCountConstants.MAX_TAXONOMY_NOT_FOUND ) {
								
								String msg = "Number of headers with taxonomy id not found has exceeded " 
										+ TaxonomyIdNotFoundMaxCountConstants.MAX_TAXONOMY_NOT_FOUND
										+ ".  The file requires manual processing to handle identifying the taxonomy ids.";
								
								log.error( msg );
								
								throw new FASTAImporterDataErrorException( msg );
								
							}

							//  If not able to find taxonomy id for a header,
							
							FASTAHeaderNoTaxIdDeterminedDTO fastaHeaderNoTaxIdDeterminedDTO = new FASTAHeaderNoTaxIdDeterminedDTO();
							
							fastaHeaderNoTaxIdDeterminedDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
							fastaHeaderNoTaxIdDeterminedDTO.setHeaderName( headerName );
							fastaHeaderNoTaxIdDeterminedDTO.setHeaderDescription( headerDescription );
							fastaHeaderNoTaxIdDeterminedDTO.setHeaderLine( header.getLine() );
							fastaHeaderNoTaxIdDeterminedDTO.setHeaderLineNumber( fastaEntry.getHeaderLineNumber() );
							fastaHeaderNoTaxIdDeterminedDTO.setMessage( determineTaxonomyIdResult.getMessage() );
							
							FASTAHeaderNoTaxIdDeterminedDAO.getInstance().save( fastaHeaderNoTaxIdDeterminedDTO );
							
							
							
							headerMatchesNotFound = true;
							
						} else {


							//  Assume good Taxonomy id

							ImportFileHeaderEntry importFileHeaderEntry = new ImportFileHeaderEntry();

							importFileHeaderEntry.setHeaderName( headerName );
							importFileHeaderEntry.setHeaderDescription( headerDescription );
							importFileHeaderEntry.setTaxonomyId( taxonomyId );

							importFileHeaderEntryList.add(importFileHeaderEntry);

						}
					
					} catch ( FASTAImporterDataErrorException dataException ) {
						
						throw dataException;
					}
				
				}
				
				
				importFileWriter.add( importFileEntry );
				
			}
			

			if ( headerMatchesNotFound ) {
				
				newStatus = ImportStatusContants.STATUS_USER_INPUT_REQUIRED;

				fastaImportTrackingDTO.setStatus( newStatus );
				
				FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
				
				if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

					SendEmailUserAttentionRequired.getInstance().sendEmailUserAttentionRequired( fastaImportTrackingDTO );
				}
				
				
			} else {
				
				//   No data errors requiring user input so import the file
				
				newStatus = ImportStatusContants.STATUS_QUEUED_FOR_IMPORT;

				fastaImportTrackingDTO.setStatus( newStatus );
				
				FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			}
			
			
		} catch ( FASTADataErrorException e ) {
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			

			newStatus = ImportStatusContants.STATUS_FIND_TAX_IDS_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
			log.error( "Data Exception", e );
			
			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {
			
				SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
			}
			
			throw new FASTAImporterDataErrorException( e.getMessage() );

		} catch ( FASTAImporterDataErrorException e ) {
			

			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( e.getMessage() );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			
			
			newStatus = ImportStatusContants.STATUS_FIND_TAX_IDS_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailFailedProcessing.getInstance().sendEmailFailedProcessing( fastaImportTrackingDTO, generalImportErrorDTO );
			}
			
			throw e;
			
		} catch ( Exception e ) {
			
			
			newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
			
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
			
			
			if ( importFileWriter != null ) {
				
				try {
					
					importFileWriter.close();
				} catch ( Exception e ) {


					newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;

					fastaImportTrackingDTO.setStatus( newStatus );
					
					FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );
					
					log.error( "Exception closing importFileWriter file", e );
					
					if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

						SendEmailSystemError.getInstance().sendEmailSystemError( fastaImportTrackingDTO );
					}
					
					throw e;
				}
			}
			
		}
		
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Finished Checking FASTA Taxonomy Ids, request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		
		
		checkFASTATaxonomyIdsResult.setHeaderMatchesNotFound( headerMatchesNotFound );
		

		return checkFASTATaxonomyIdsResult;
	}
	
	
			
			
}
