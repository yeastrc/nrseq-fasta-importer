package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.FileNameAndDirectoryNameConstants;
import org.yeastrc.nrseq_fasta_importer.constants.GeneralImportErrorConstants;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.constants.TaxonomyIdNotFoundMaxCountConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedSequenceDAO;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedSequenceDTO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterRemoteWebserviceCallErrorException;
import org.yeastrc.nrseq_fasta_importer.fasta_importer_work_dir.Get_FASTA_Importer_Work_Directory_And_SubDirs;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.dto.IntermediateFileEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.dto.IntermediateFileHeaderEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.writer_reader.IntermediateFileReader;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.writer_reader.IntermediateFileWriter;
import org.yeastrc.nrseq_fasta_importer.objects.FASTAHeaderImporterCopy;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailUserAttentionRequired;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyId;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;

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

//		FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

		Integer getTaxonomyIdsPassNumber =
				FASTAImportTrackingDAO.getInstance().updateStatusIncrementGetTaxonomyIdsPassNumber( newStatus, fastaImportTrackingDTO.getId() );
		
		if ( getTaxonomyIdsPassNumber == null ) {
			
			String msg = "updateStatusIncrementGetTaxonomyIdsPassNumber did not return a value for "
					+ "getTaxonomyIdsPassNumber.";
			
			log.error( msg );
			
			throw new Exception( msg );
		}
		
		fastaImportTrackingDTO.setGetTaxonomyIdsPassNumber( getTaxonomyIdsPassNumber );

		try {


			
			IntermediateFileReader intermediateFileReader = null;
			
			IntermediateFileWriter importFileWriter = null;

			try {

				
				File fasta_Importer_Work_Directory = Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().get_FASTA_Importer_Work_Directory();

				String dirNameForImportTrackingId =
						Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().getDirForImportTrackingId( fastaImportTrackingDTO.getId() );
				
				File dirForImportTrackingId  =  new File( fasta_Importer_Work_Directory , dirNameForImportTrackingId );
				
				File tempFilenameForGetTaxonomyIdsProcessingFile = new File( dirForImportTrackingId, FileNameAndDirectoryNameConstants.DATA_TO_GET_TAXONOMY_IDS_FILE );

				File importFile = new File( dirForImportTrackingId, FileNameAndDirectoryNameConstants.DATA_TO_IMPORT_FILE );

				
				
				intermediateFileReader = IntermediateFileReader.getInstance( tempFilenameForGetTaxonomyIdsProcessingFile );

				importFileWriter = IntermediateFileWriter.getInstance( importFile );


				while ( true ) {


					IntermediateFileEntry intermediateFileEntry = null;
					
					try {
						intermediateFileEntry = intermediateFileReader.readNext();
						
						
					} catch ( Exception e ) {
						
//						log.error( "Exception", e );
						
						throw e;
					}
					
					if ( intermediateFileEntry == null ) { 
						
						//  At End Of File
						
						break;  //  EARLY EXIT of LOOP
					}
					

					currentSequenceCount++;


					List<IntermediateFileHeaderEntry> intermediateFileHeaderEntryList = intermediateFileEntry.getImportFileHeaderEntryList();
				
					if ( intermediateFileHeaderEntryList == null ) {
						
						String msg = "Error: intermediateFileHeaderEntryList == null.  fastaImportTrackingDTO.id: " + fastaImportTrackingDTO.getId();
						
						log.error( msg );
						
						throw new Exception(msg);
					}

					
					IntermediateFileEntry importFileEntry = new IntermediateFileEntry();

					importFileEntry.setHeaderLineNumber( intermediateFileEntry.getHeaderLineNumber() );
					importFileEntry.setSequence( intermediateFileEntry.getSequence() );

					List<IntermediateFileHeaderEntry> importFileHeaderEntryList = new ArrayList<>();

					importFileEntry.setImportFileHeaderEntryList( importFileHeaderEntryList );

					
					for ( IntermediateFileHeaderEntry intermediateFileHeaderEntry : intermediateFileHeaderEntryList ) {
						
						String headerFullString = intermediateFileHeaderEntry.getHeaderFullString();
						String headerName = intermediateFileHeaderEntry.getHeaderName();
						String headerDescription = intermediateFileHeaderEntry.getHeaderDescription();
						
						

						//  Update headerName in the fastaHeaderImporterCopy object to remove contamin
						//  The headerName in the fastaHeaderImporterCopy is not what is saved to the YRC_NRSEQ database
						//  The headerName in the variable headerName is what is saved to the YRC_NRSEQ database

						String headerNameForGettingTaxonomyId = headerName;
						String headerFullStringForGettingTaxonomyId = headerFullString;
						
						// if header start with "contaminant_" strip it off
						
						final String CONTAMINANT_PREFIX = "contaminant_";
						
						if (headerNameForGettingTaxonomyId.startsWith( CONTAMINANT_PREFIX )) {
							headerNameForGettingTaxonomyId = headerNameForGettingTaxonomyId.substring( CONTAMINANT_PREFIX.length(), headerNameForGettingTaxonomyId.length() );
						}
						
						
						if (headerFullStringForGettingTaxonomyId.startsWith( CONTAMINANT_PREFIX )) {
							headerFullStringForGettingTaxonomyId = headerFullStringForGettingTaxonomyId.substring( CONTAMINANT_PREFIX.length(), headerFullStringForGettingTaxonomyId.length() );
						}


						FASTAHeaderImporterCopy fastaHeaderImporterCopy = new FASTAHeaderImporterCopy();

						fastaHeaderImporterCopy.setLine( headerFullString );
						fastaHeaderImporterCopy.setName( headerNameForGettingTaxonomyId );
						fastaHeaderImporterCopy.setDescription( headerDescription );


						try {
							
							//  taxonomyId which is the speciesID field in the tblProteinTable
							DetermineTaxonomyIdResult determineTaxonomyIdResult = 
									DetermineTaxonomyId.getInstance().getTaxonomyId( fastaHeaderImporterCopy, fastaImportTrackingDTO );

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
								fastaHeaderNoTaxIdDeterminedDTO.setGetTaxonomyIdsPassNumber( fastaImportTrackingDTO.getGetTaxonomyIdsPassNumber() );
								fastaHeaderNoTaxIdDeterminedDTO.setHeaderName( headerName );
								fastaHeaderNoTaxIdDeterminedDTO.setHeaderDescription( headerDescription );
								fastaHeaderNoTaxIdDeterminedDTO.setHeaderLine( headerFullString );
								fastaHeaderNoTaxIdDeterminedDTO.setHeaderLineNumber( intermediateFileEntry.getHeaderLineNumber() );
								fastaHeaderNoTaxIdDeterminedDTO.setMessage( determineTaxonomyIdResult.getMessage() );

								FASTAHeaderNoTaxIdDeterminedDAO.getInstance().save( fastaHeaderNoTaxIdDeterminedDTO );

								FASTAHeaderNoTaxIdDeterminedSequenceDTO fASTAHeaderNoTaxIdDeterminedSequenceDTO = new FASTAHeaderNoTaxIdDeterminedSequenceDTO();
								
								fASTAHeaderNoTaxIdDeterminedSequenceDTO.setFastaHeaderNoTaxIdDeterminedId( fastaHeaderNoTaxIdDeterminedDTO.getId() );
								fASTAHeaderNoTaxIdDeterminedSequenceDTO.setSequence( intermediateFileEntry.getSequence() );
								
								FASTAHeaderNoTaxIdDeterminedSequenceDAO.getInstance().save(fASTAHeaderNoTaxIdDeterminedSequenceDTO);

								headerMatchesNotFound = true;

							} else {


								//  Assume good Taxonomy id

								IntermediateFileHeaderEntry importFileHeaderEntry = new IntermediateFileHeaderEntry();

								importFileHeaderEntry.setHeaderName( headerName );
								importFileHeaderEntry.setHeaderDescription( headerDescription );
								importFileHeaderEntry.setTaxonomyId( taxonomyId );

								importFileHeaderEntryList.add(importFileHeaderEntry);

							}

						} catch ( FASTAImporterDataErrorException dataException ) {

							throw dataException;
						}

					}


					importFileWriter.insertToFile( importFileEntry );

				}

			} finally {

				if ( intermediateFileReader != null ) {

					try {

						intermediateFileReader.close();
					} catch ( Exception e ) {

						log.error( "Exception closing intermediateFileReader", e );
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
			

		} catch ( FASTAImporterRemoteWebserviceCallErrorException e ) {


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


			GeneralImportErrorDTO generalImportErrorDTO = new GeneralImportErrorDTO();
			
			generalImportErrorDTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
			generalImportErrorDTO.setMessage( GeneralImportErrorConstants.GENERAL_IMPORT_ERROR_MESSAGE_SYSTEM_ERROR );
			
			GeneralImportErrorDAO.getInstance().save(generalImportErrorDTO);
			
			

			newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;

			fastaImportTrackingDTO.setStatus( newStatus );

			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fastaImportTrackingDTO.getId() );

			log.error( "Exception", e );


			if ( StringUtils.isNotEmpty( fastaImportTrackingDTO.getEmail() ) ) {

				SendEmailSystemError.getInstance().sendEmailSystemError( fastaImportTrackingDTO );
			}

			throw e;

		} finally {


		}

		if ( log.isInfoEnabled() ) {

			log.info( "Finished Checking FASTA Taxonomy Ids, request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}


		checkFASTATaxonomyIdsResult.setHeaderMatchesNotFound( headerMatchesNotFound );


		return checkFASTATaxonomyIdsResult;
	}




}
