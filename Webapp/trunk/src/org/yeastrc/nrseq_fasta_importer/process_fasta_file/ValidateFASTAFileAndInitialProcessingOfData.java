package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTADataErrorException;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.fasta.FASTAReader;
import org.yeastrc.nrseq_fasta_importer.constants.GeneralImportErrorConstants;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.GeneralImportErrorDAO;
import org.yeastrc.nrseq_fasta_importer.dao.LockValidationTempTablesDAO;
import org.yeastrc.nrseq_fasta_importer.dao.Tmp_FASTA_header_name_desc_seq_id_DAO;
import org.yeastrc.nrseq_fasta_importer.dao.Tmp_FASTA_sequence_DAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;
import org.yeastrc.nrseq_fasta_importer.dto.Tmp_FASTA_header_name_desc_seq_id_DTO;
import org.yeastrc.nrseq_fasta_importer.dto.Tmp_FASTA_sequence_DTO;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.dto.IntermediateFileEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.dto.IntermediateFileHeaderEntry;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.writer_reader.IntermediateFileWriter;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailFailedProcessing;
import org.yeastrc.nrseq_fasta_importer.send_email.SendEmailSystemError;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempDirForFileUploads;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempLocalFilenameForTempFilenameNumber;
import org.yeastrc.nrseq_fasta_importer.utils.FASTAValidator;
import org.yeastrc.nrseq_fasta_importer.utils.TruncateHeaderName;

/**
 * Validate FASTA file syntax and specific processing for duplicate header names
 *
 * Data in the FASTA file is truncated per the values in FASTA_DataTruncationConstants
 * before checking for duplicate header names  
 * and before writing out to the intermediate file
 */
public class ValidateFASTAFileAndInitialProcessingOfData {

	private static final Logger log = Logger.getLogger(ValidateFASTAFileAndInitialProcessingOfData.class);
	
	
	public static final int CURRENT_SEQUENCE_COUNT_NOT_SET = 0;
	public static final int TOTAL_SEQUENCE_COUNT_NOT_SET = 0;

	private ValidateFASTAFileAndInitialProcessingOfData() { }
	public static ValidateFASTAFileAndInitialProcessingOfData getInstance() { 
		return new ValidateFASTAFileAndInitialProcessingOfData(); 
	}
	
	


	private volatile int currentSequenceCount = CURRENT_SEQUENCE_COUNT_NOT_SET;
	

	public int getCurrentSequenceCount() {
		return currentSequenceCount;
	}
	

	private volatile int totalSequenceCount = TOTAL_SEQUENCE_COUNT_NOT_SET;
	

	public int getTotalSequenceCount() {
		return totalSequenceCount;
	}
	
	

	/**
	 * Throws FASTAImporterDataErrorException for data errors   
	 * 
	 * @param fastaImportTrackingDTO
	 * @throws FASTAImporterDataErrorException for data errors 
	 * @throws Exception 
	 */
	public void validateFASTAFile( FASTAImportTrackingDTO fastaImportTrackingDTO ) throws FASTAImporterDataErrorException, Exception {
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Starting Validating request id: " + fastaImportTrackingDTO.getId() + ", uploaded file: " + fastaImportTrackingDTO.getFilename() );
		}
		
		currentSequenceCount = CURRENT_SEQUENCE_COUNT_NOT_SET;
		
		
		int sequenceCount = 0;
		
//		Map<String, Long> headerNameLineNumberMap = new HashMap<>();
		
		
		String newStatusValidationStarted = ImportStatusContants.STATUS_VALIDATION_STARTED;
		
		FASTAImportTrackingDAO.getInstance().updateStatus( newStatusValidationStarted, fastaImportTrackingDTO.getId() );
		
		synchronized ( this ) {  //  ensure written to main memory
			
			fastaImportTrackingDTO.setStatus(newStatusValidationStarted);
		}
		
		
		Tmp_FASTA_header_name_desc_seq_id_DAO tmp_FASTA_header_name_desc_seq_id_DAO = Tmp_FASTA_header_name_desc_seq_id_DAO.getInstance();
		
		Tmp_FASTA_sequence_DAO tmp_FASTA_sequence_DAO = Tmp_FASTA_sequence_DAO.getInstance();
		
		int tmp_FASTA_header_name_desc_seq_id_RecordsInserted = 0;
		int tmp_FASTA_sequence_RecordsInserted = 0;
		

		int tempFilenameNumber = fastaImportTrackingDTO.getTempFilenameNumber();

		String tempFilename = GetTempLocalFilenameForTempFilenameNumber.getInstance().getTempLocalFileForUploadedFile( tempFilenameNumber );

		String tempFilenameForGetTaxonomyIdsProcessingString = 
				GetTempLocalFilenameForTempFilenameNumber.getInstance().getTempLocalFileForGetTaxonomyIdsProcessing( tempFilenameNumber );

		File tempDir = GetTempDirForFileUploads.getInstance().getTempDirForFileUploads();

		File fastaFile = new File( tempDir, tempFilename );
		File tempFilenameForGetTaxonomyIdsProcessing = new File( tempDir, tempFilenameForGetTaxonomyIdsProcessingString );

		
		Connection tmpValidationDBConnection = null;
		
		try {

			FASTAReader fastaReader = null;

			IntermediateFileWriter intermediateFileWriter = null;
			
			
			boolean allHeadersHaveUniqueHashCodes = true;
			
			
			////////////////////////////////////////////////
			
			//  First read file to get sequence count

			try {
				

				fastaReader = FASTAReader.getInstance( fastaFile );

				
				//  Track headerNameHashCodes in first read of file
				Set<Integer> headerNameHashCodes = new HashSet<>();
				

				while ( true ) {


					//  fastaReader.readNext()  throws exception for invalid data format

					FASTAEntry fastaEntry = null;

					try {
						fastaEntry = fastaReader.readNext();


						if ( fastaEntry == null ) { 

							//  At End Of File

							break;  //  EARLY EXIT of LOOP
						}

						
						// Check allHeadersHaveUniqueHashCodes to stop testing 
						//		for allHeadersHaveUniqueHashCodes as soon as find first duplicate

						if ( allHeadersHaveUniqueHashCodes ) {

							// the headers for this entry
							Set<FASTAHeader> headers = fastaEntry.getHeaders();


							for ( FASTAHeader header : headers ) {

								String headerName = header.getName();

								if ( headerName == null ) {

									String msg = "Header name cannot be null. HeaderLineNumber " 
											+ fastaEntry.getHeaderLineNumber() + ", filename: " + fastaImportTrackingDTO.getFilename();
									
									log.error( msg );
									
									throw new FASTAImporterDataErrorException( msg );
								}

								//  Truncate header name, if needed

								headerName = TruncateHeaderName.truncateHeaderName( headerName );
								

								int headerNameHashCode = headerName.hashCode();

								if ( ! headerNameHashCodes.add( headerNameHashCode ) ) {
									
									//  add returned false so already in the set 

									allHeadersHaveUniqueHashCodes = false;
								}
							}
						}

					} catch ( FASTADataErrorException e ) {

						throw e;

					} catch ( Exception e ) {

						//					log.error( "Exception", e );

						throw e;
					}

					sequenceCount++;
				}
				
			} finally {
				
				if ( fastaReader != null ) {
					
					try {
						
						fastaReader.close();
						
						fastaReader = null;
						
					} catch ( Exception e ) {

						log.error( "Exception closing fasta file", e );
					}
				}
				
				
			}
			
			
			if ( log.isInfoEnabled() ) {

				if ( allHeadersHaveUniqueHashCodes ) {

					log.info("After initial read, allHeadersHaveUniqueHashCodes is true.  filename: " + fastaImportTrackingDTO.getFilename() );
				} else { 	
					log.info("After initial read, allHeadersHaveUniqueHashCodes is false.  filename: " + fastaImportTrackingDTO.getFilename() );
				}
			}
			
			
			FASTAImportTrackingDAO.getInstance().updateFastaEntryCount( sequenceCount, fastaImportTrackingDTO.getId() );
			
			
			totalSequenceCount = sequenceCount;
			
			synchronized ( this ) {  //  Ensure value is written to main memory

				fastaImportTrackingDTO.setFastaEntryCount( sequenceCount );
			}
			

			//////////////////////////////////////////
			
			//  Read fasta file to perform validation 
			
			
			fastaReader = null;

			try {
				
				
				//  Track headerNameHashCodes in main validation read of file
				Set<Integer> headerNameHashCodes = new HashSet<>();
				


				fastaReader = FASTAReader.getInstance( fastaFile );


				intermediateFileWriter = IntermediateFileWriter.getInstance( tempFilenameForGetTaxonomyIdsProcessing );


				tmpValidationDBConnection = LockValidationTempTablesDAO.getInstance().lockValidationTempTablesAndReturnDBConnection();


				try {

					tmp_FASTA_header_name_desc_seq_id_DAO.truncate( tmpValidationDBConnection );

				} catch ( Exception e ) {

					log.error( "Exception tmp_FASTA_header_name_desc_seq_id_DAO.truncate( dbConnection )(", e );
				}


				try {

					tmp_FASTA_sequence_DAO.truncate( tmpValidationDBConnection );
				} catch ( Exception e ) {

					log.error( "Exception tmp_FASTA_sequence_DAO.truncate( dbConnection )", e );
				}


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

					String sequenceString = fastaEntry.getSequence();

					if ( sequenceString.length() == 0 ) {


						String msg = "sequence length == zero for id: " + fastaImportTrackingDTO.getId() 
								+ ", header line number " + fastaEntry.getHeaderLineNumber();

						log.error( msg );
						throw new FASTAImporterDataErrorException( msg );
					}



					boolean isSequenceValid = false;

					try {

						// validProteinSequence(...) throws FASTAImporterDataErrorException for errors

						isSequenceValid = FASTAValidator.validProteinSequence( sequenceString );

					} catch( FASTAImporterDataErrorException e ) {

						String msg = e.getMessage()
								+ "  Header line number " + fastaEntry.getHeaderLineNumber()
								+ ", sequence: " + sequenceString;

						//					log.error( msg );

						throw new FASTAImporterDataErrorException( msg );

					}

					if ( ! isSequenceValid ) {

						String msg = "Invalid protein sequence"
								+ "for header line number " + fastaEntry.getHeaderLineNumber()
								+ ", sequence: " + sequenceString;

						//					log.error( msg );

						throw new FASTAImporterDataErrorException( msg );
					}


					Tmp_FASTA_sequence_DTO tmp_FASTA_sequence_DTO = null;

					IntermediateFileEntry importFileEntry = null;

					for ( FASTAHeader header : headers ) {

						String headerFullString = header.getLine();
						String headerName = header.getName();
						String headerDescription = header.getDescription();
						
						//  Truncate header name, if needed
						
						headerName = TruncateHeaderName.truncateHeaderName( headerName );

						
						boolean duplicateHeaderName = false;
						

						int headerNameHashCode = headerName.hashCode();

						if ( ! headerNameHashCodes.add( headerNameHashCode ) ) {

							//  add returned false so already in the set so check in database

							List<Tmp_FASTA_header_name_desc_seq_id_DTO> tmp_FASTA_header_name_desc_seq_id_DTOList =
									tmp_FASTA_header_name_desc_seq_id_DAO.getAllForFastaImportTrackingIdAndHeaderName( fastaImportTrackingDTO.getId(), headerName, tmpValidationDBConnection );

							if ( ! tmp_FASTA_header_name_desc_seq_id_DTOList.isEmpty() ) {
								
								
								duplicateHeaderName = true;
								

								for ( Tmp_FASTA_header_name_desc_seq_id_DTO item : tmp_FASTA_header_name_desc_seq_id_DTOList ) {

									//  This header name was already processed in this file.
									//   The header name, header description, and sequence must match or it is an error.

									boolean exactMatch = false;

									if ( ( headerDescription == null && item.getHeaderDescription() == null )
											|| ( headerDescription.equals( item.getHeaderDescription() ) ) ) {

										//  descriptions match so compare sequences

										Tmp_FASTA_sequence_DTO tmp_FASTA_sequence_DTO_ForRetrievedHeaderName =
												tmp_FASTA_sequence_DAO.getForId( item.getTmpSequenceId(), tmpValidationDBConnection );

										if ( tmp_FASTA_sequence_DTO_ForRetrievedHeaderName == null ) {

											String msg = "Tmp_FASTA_sequence_DTO not found for id " + item.getTmpSequenceId();

											log.error( msg );

											throw new Exception( msg );
										}

										String sequenceForRetrievedHeaderName = tmp_FASTA_sequence_DTO_ForRetrievedHeaderName.getSequence();

										if ( sequenceString.equals( sequenceForRetrievedHeaderName ) ) {

											exactMatch = true;
										}
									}

									if ( ! exactMatch ) {


										String msg = "Header name '" + headerName + "' in the file more than once at line numbers "
												+ item.getHeaderLineNumber()
												+ " and " + fastaEntry.getHeaderLineNumber()
												+ ".";

										//								log.error( msg );

										throw new FASTAImporterDataErrorException( msg );
									}
								}

								//  Matches found and all are exact matches so no exception thrown 

							}
							
						}
						
						

						if ( ! duplicateHeaderName ) {

							//  Not a duplicate header name so process this header.

							if ( tmp_FASTA_sequence_DTO == null ) {

								//  Sequence not saved yet so save it for duplicate checking

								tmp_FASTA_sequence_DTO = new Tmp_FASTA_sequence_DTO();

								tmp_FASTA_sequence_DTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
								tmp_FASTA_sequence_DTO.setHeaderLineNumber( fastaEntry.getHeaderLineNumber() );
								tmp_FASTA_sequence_DTO.setSequence( sequenceString );

								tmp_FASTA_sequence_DAO.save( tmp_FASTA_sequence_DTO, tmpValidationDBConnection );

								tmp_FASTA_sequence_RecordsInserted++;

							}

							//  Save this header for duplicate checking

							Tmp_FASTA_header_name_desc_seq_id_DTO tmp_FASTA_header_name_desc_seq_id_DTO = new Tmp_FASTA_header_name_desc_seq_id_DTO();

							tmp_FASTA_header_name_desc_seq_id_DTO.setFastaImportTrackingId( fastaImportTrackingDTO.getId() );
							tmp_FASTA_header_name_desc_seq_id_DTO.setHeaderLineNumber(fastaEntry.getHeaderLineNumber());
							tmp_FASTA_header_name_desc_seq_id_DTO.setTmpSequenceId( tmp_FASTA_sequence_DTO.getId() );
							tmp_FASTA_header_name_desc_seq_id_DTO.setHeaderName( headerName );
							tmp_FASTA_header_name_desc_seq_id_DTO.setHeaderDescription( headerDescription );

							tmp_FASTA_header_name_desc_seq_id_DAO.save( tmp_FASTA_header_name_desc_seq_id_DTO, tmpValidationDBConnection );

							tmp_FASTA_header_name_desc_seq_id_RecordsInserted++;


							///  Add header to output processing

							if ( importFileEntry == null ) {

								//  First non-duplicate header so create the overall IntermediateFileEntry
								importFileEntry = new IntermediateFileEntry();

								importFileEntry.setHeaderLineNumber( fastaEntry.getHeaderLineNumber() );
								importFileEntry.setSequence( sequenceString );

								List<IntermediateFileHeaderEntry> importFileHeaderEntryList = new ArrayList<>();

								importFileEntry.setImportFileHeaderEntryList( importFileHeaderEntryList );
							}

							List<IntermediateFileHeaderEntry> importFileHeaderEntryList = importFileEntry.getImportFileHeaderEntryList();

							IntermediateFileHeaderEntry importFileHeaderEntry = new IntermediateFileHeaderEntry();

							importFileHeaderEntry.setHeaderFullString( headerFullString );
							importFileHeaderEntry.setHeaderName( headerName );
							importFileHeaderEntry.setHeaderDescription( headerDescription );
							//						importFileHeaderEntry.setTaxonomyId( taxonomyId );

							importFileHeaderEntryList.add(importFileHeaderEntry);
						}

					}

					if ( importFileEntry != null ) {

						intermediateFileWriter.insertToFile( importFileEntry );
					}

				}
				
			} finally {
				
				
				if ( fastaReader != null ) {
					
					try {
						
						fastaReader.close();
					} catch ( Exception e ) {

						log.error( "Exception closing fasta file", e );
					}
				}
				
				if ( intermediateFileWriter != null ) {
					
					try {
						
						intermediateFileWriter.close();
					} catch ( Exception e ) {


						String newStatus = ImportStatusContants.STATUS_SYSTEM_ERROR_PROCESSING_FAILED;

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
			generalImportErrorDTO.setMessage( GeneralImportErrorConstants.GENERAL_IMPORT_ERROR_MESSAGE_SYSTEM_ERROR );
			
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
			
			
			if ( tmpValidationDBConnection != null ) {


				try {

					tmp_FASTA_header_name_desc_seq_id_DAO.truncate( tmpValidationDBConnection );
					
				} catch ( Throwable e ) {

					log.error( "Exception tmp_FASTA_header_name_desc_seq_id_DAO.truncate( dbConnection )(", e );
				}
				

				try {

					tmp_FASTA_sequence_DAO.truncate( tmpValidationDBConnection );
				} catch ( Throwable e ) {

					log.error( "Exception tmp_FASTA_sequence_DAO.truncate( dbConnection )", e );
				}
				
				try {

					LockValidationTempTablesDAO.getInstance().unlockAllTableAndCloseConnection( tmpValidationDBConnection );
				} catch ( Exception e ) {

					log.error( "Exception unlocking temp tables or closing connection", e );
				}
			}
		}
		
		
		if ( log.isInfoEnabled() ) {
			
			log.info( "Finished Validating request id: " + fastaImportTrackingDTO.getId() 
					+ ", uploaded file: " + fastaImportTrackingDTO.getFilename()
					+ ", tmp_FASTA_header_name_desc_seq_id_RecordsInserted: " + tmp_FASTA_header_name_desc_seq_id_RecordsInserted 
					+ ", tmp_FASTA_sequence_RecordsInserted: " + tmp_FASTA_sequence_RecordsInserted );
			
		}
		
		
	}
	
	
			
			
}
