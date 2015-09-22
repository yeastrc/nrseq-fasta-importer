package org.yeastrc.nrseq_fasta_importer.www.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.FileNameAndDirectoryNameConstants;
import org.yeastrc.nrseq_fasta_importer.constants.FileUploadConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingFileIdCreatorDAO;
import org.yeastrc.nrseq_fasta_importer.dao.TempUploadFileIdCreatorDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.fasta_importer_work_dir.Get_FASTA_Importer_Work_Directory_And_SubDirs;
import org.yeastrc.nrseq_fasta_importer.objects.ImportFASTAServletResponse;
import org.yeastrc.nrseq_fasta_importer.threads.ProcessImportFASTAFileThread;
import org.yeastrc.nrseq_fasta_importer.utils.SHA1SumCalculator;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * 
 *  This Servlet should be considered a webservice as it returns JSON
 */
public class UploadFASTAFileServlet extends HttpServlet {


	private static final Logger log = Logger.getLogger(UploadFASTAFileServlet.class);


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

//	public static final String UPLOAD_TEMP_DIRECTORY = "/data/run_space/NRSEQ_FASTA_Importer_RunSpace/TempDir/";
	

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//  For multipart forms (which is what is passed to this servlet), 
		//  	request.getParameter(...) only comes from the query string
//		String uploadType = request.getParameter( "uploadTypeQueryString" ); 
		
		
		File uploadedFileOnDisk = null;

		try {
			
			String requestURL = request.getRequestURL().toString();
			
			
			String filename = request.getParameter( "filename" );
			String import_decoy_sequences = request.getParameter( "import_decoy_sequences" );
			String require_confirm_before_insert = request.getParameter( "require_confirm_before_insert" );

			if ( StringUtils.isEmpty( filename ) ) {
				

				log.error( "'filename' query parameter is not sent or is empty" );
				
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );

				throw new FailResponseSentException();
			}
			
			
			
			if ( StringUtils.isEmpty( import_decoy_sequences ) ) {
				

				log.error( "'import_decoy_sequences' query parameter is not sent or is empty" );
				
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );

				throw new FailResponseSentException();
			}
			
			
			
			if ( StringUtils.isEmpty( require_confirm_before_insert ) ) {
				

				log.error( "'require_confirm_before_insert' query parameter is not sent or is empty" );
				
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );

				throw new FailResponseSentException();
			}

			
			boolean import_decoy_sequencesBoolean = Boolean.parseBoolean( import_decoy_sequences );
			boolean require_confirm_before_insertBoolean = Boolean.parseBoolean( require_confirm_before_insert );

			{

				Integer tblDatabaseId = YRC_NRSEQ_tblDatabaseDAO.getInstance().getIdForName( filename );

				if ( tblDatabaseId != null ) {

					log.info( "Filename already in NRSEQ database: " + filename );

					response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );

					ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();

					importFASTAServletResponse.setStatusSuccess(false);

					importFASTAServletResponse.setFilenameAlreadyInDB(true);

					OutputStream responseOutputStream = response.getOutputStream();


					// send the JSON response 
					ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
					mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer

					responseOutputStream.flush();
					responseOutputStream.close();

					throw new FailResponseSentException();
				}

			}
			
			
			File fasta_Importer_Work_Directory = Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().get_FASTA_Importer_Work_Directory();

			
			String uploadFileTempDirString =
					Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().getDirForUploadFileTempDir();
			
			File uploadFileTempDir = new File( fasta_Importer_Work_Directory, uploadFileTempDirString );
			
			if ( ! uploadFileTempDir.exists() ) {
				
				boolean mkdirResult = uploadFileTempDir.mkdir();
				
				String msg = "mkdir for uploadFileTempDir failed.  uploadFileTempDir: " + uploadFileTempDir.getAbsolutePath();
				log.error( msg );
			}
			
			if ( ! uploadFileTempDir.exists() ) {
				
				String msg = "uploadFileTempDir does not exist after testing for it and attempting to create it.  uploadFileTempDir: " + uploadFileTempDir.getAbsolutePath();
				log.error( msg );
				
				throw new Exception(msg);
			}
			
			
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			
//					
//					In DiskFileItemFactory, if temp directory is not specified, it uses 
//					tempDir = new File(System.getProperty("java.io.tmpdir"));
//					
//					which on one Tomcat installation is
//					/data/webtools/apache-tomcat-7.0.53/temp

			log.info( "DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD: " + DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD );

			if ( diskFileItemFactory.getRepository() == null ) {

				log.info( "diskFileItemFactory.getRepository() == null" );
			} else {
				log.info( "diskFileItemFactory.getRepository().getAbsolutePath(): '" 
						+ diskFileItemFactory.getRepository().getAbsolutePath() + "'" );
			}
			log.info( "diskFileItemFactory.getSizeThreshold(): '" 
					+ diskFileItemFactory.getSizeThreshold() + "'" );
			
			File diskFileItemFactoryRepository = uploadFileTempDir; // Put diskFileItemFactory temp files in subdirectory directory
			
			diskFileItemFactory.setRepository( diskFileItemFactoryRepository );

			ServletFileUpload servletFileUpload = new ServletFileUpload( diskFileItemFactory );
			
			
		       // file upload size limit
			servletFileUpload.setFileSizeMax( FileUploadConstants.MAX_FILE_UPLOAD_SIZE );
			

			
			int filesUploadedCount = 0;
			
			FASTAImportTrackingDTO fastaImportTrackingDTO = null;
			
			String fastaDescription = null;
			String email = null;
            
			List<FileItem> multiparts = servletFileUpload.parseRequest( request );

			

			log.info( "multiparts size " + multiparts.size() );

			for ( FileItem item : multiparts ) {
				
			    if ( item.isFormField() ) {
			    	
			    	//  form field that is not a file upload
			    	
			    	String fieldName = item.getFieldName();
			    	String fieldValue = item.getString();
			    	
			    	if ( "fastaDescription".equals( fieldName ) ) {
			    		
			    		fastaDescription = fieldValue;
			    	
			    	} else if ( "email".equals( fieldName) ) {
			    		
			    		email = fieldValue;
			    	}
			        
			    	int z = 0;
			    	
			    } else {
			    	

					String fieldName = item.getFieldName();
					
					
					
					//  Only allow the expected field name
					
					if ( ! FileUploadConstants.UPLOAD_FASTA_FILE_FIELD_NAME.equals( fieldName ) ) {
						
						

						log.error( "File uploaded using field name other than allowed field name. " 
								+ "Allowed field name: " + FileUploadConstants.UPLOAD_FASTA_FILE_FIELD_NAME
								+ ", received field name: " + fieldName );
						

						response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );


						ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
						
						importFASTAServletResponse.setStatusSuccess(false);
						
						importFASTAServletResponse.setUploadFile_fieldNameInvalid(true);
						
						OutputStream responseOutputStream = response.getOutputStream();
						

						// send the JSON response 
						ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
						mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
						
						responseOutputStream.flush();
						responseOutputStream.close();
						

						
						throw new FailResponseSentException();
					}
					
					filesUploadedCount++;

					//  Only allow one file to be uploaded in the request
					
					if ( filesUploadedCount > 1 ) {
						

						log.error( "More than one file uploaded in the request." );
						
						response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );
						

						ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
						
						importFASTAServletResponse.setStatusSuccess(false);
						
						importFASTAServletResponse.setMoreThanOneuploadedFile(true);;
						
						OutputStream responseOutputStream = response.getOutputStream();
						

						// send the JSON response 
						ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
						mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
						
						responseOutputStream.flush();
						responseOutputStream.close();
						
						throw new FailResponseSentException();
					}
					
					
					
				    String fileNameForFormObject = item.getName();
				    String contentType = item.getContentType();
				    boolean isInMemory = item.isInMemory();
				    long sizeInBytes = item.getSize();
				    
				    
				    filename = fileNameForFormObject;  // re-assign filename to the filename from the form
				    
					{

						Integer tblDatabaseId = YRC_NRSEQ_tblDatabaseDAO.getInstance().getIdForName( fileNameForFormObject );

						if ( tblDatabaseId != null ) {

							log.info( "Filename already in NRSEQ database: " + fileNameForFormObject );

							response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );

							ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();

							importFASTAServletResponse.setStatusSuccess(false);

							importFASTAServletResponse.setFilenameAlreadyInDB(true);

							OutputStream responseOutputStream = response.getOutputStream();


							// send the JSON response 
							ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
							mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer

							responseOutputStream.flush();
							responseOutputStream.close();

							throw new FailResponseSentException();
						}

					}
					
				    
				    

					log.info( "started Upload for filename " + fileNameForFormObject );


					log.info( "item.getSize(): " + item.getSize() );

					
					
					int tempFilenameNumber = TempUploadFileIdCreatorDAO.getInstance().getNextId();
							
					String tempFilename =
							FileNameAndDirectoryNameConstants.UPLOAD_FILE_TEMP_FILENAME 
							+ tempFilenameNumber
							+ FileNameAndDirectoryNameConstants.UPLOAD_FILE_TEMP_FILENAME_SUFFIX;

					
					File tempDir = uploadFileTempDir;
					

					uploadedFileOnDisk = new File( tempDir, tempFilename );
					
					
					item.write( uploadedFileOnDisk );
					
					


					log.info( "Completed transfer to server for user uploaded filename " + fileNameForFormObject );

				}

			}

			
			if ( uploadedFileOnDisk == null ) {
				
				log.error( "No file uploaded." );
				
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );
				

				ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
				
				importFASTAServletResponse.setStatusSuccess(false);
				
				importFASTAServletResponse.setNoUploadedFile(true);
				
				OutputStream responseOutputStream = response.getOutputStream();
				

				// send the JSON response 
				ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
				mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
				
				responseOutputStream.flush();
				responseOutputStream.close();
				
				throw new FailResponseSentException();
			}
			
			///   Have a single file uploaded. Now create a importer work dir and move the file into it.
			
			
			int importTrackingId = FASTAImportTrackingFileIdCreatorDAO.getInstance().getNextId();
			
			String dirNameForImportTrackingId =
					Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().getDirForImportTrackingId( importTrackingId );
			
			File dirForImportTrackingId  =  new File( fasta_Importer_Work_Directory , dirNameForImportTrackingId );
			
			if ( dirForImportTrackingId.exists() ) {
				
				String msg = "dirForImportTrackingId already exists: " + dirForImportTrackingId.getAbsolutePath();
				log.error( msg );
				throw new Exception(msg);
			}
			
			if ( ! dirForImportTrackingId.mkdir() ) {
				
				String msg = "Failed to make dirForImportTrackingId: " + dirForImportTrackingId.getAbsolutePath();
				log.error( msg );
				throw new Exception(msg);
			}
			
			///   move the uploaded file into importer work dir.
			
			
			File uploadedFile_In_dirForImportTrackingId = new File( dirForImportTrackingId, FileNameAndDirectoryNameConstants.UPLOADED_FASTA_FILE );
			
			try {

				FileUtils.moveFile( uploadedFileOnDisk, uploadedFile_In_dirForImportTrackingId );
				
			} catch ( Exception e ) {
				
				String msg = "Failed to move uploaded file to dirForImportTrackingId.  Src file: " + uploadedFileOnDisk
						+ ", dest file: " + uploadedFile_In_dirForImportTrackingId;
				
				log.error( msg, e );
				
				throw new Exception(msg, e);
			}
			
			
			
			String uploadedFileSha1sum = SHA1SumCalculator.getInstance().getSHA1Sum( uploadedFile_In_dirForImportTrackingId );
			
			fastaImportTrackingDTO = new FASTAImportTrackingDTO();
			
			fastaImportTrackingDTO.setId( importTrackingId );
			
			fastaImportTrackingDTO.setFilename( filename );
			fastaImportTrackingDTO.setSha1sum( uploadedFileSha1sum );
			
			fastaImportTrackingDTO.setInsertRequestURL( requestURL );
			


			fastaImportTrackingDTO.setDescription( fastaDescription );
			fastaImportTrackingDTO.setEmail( email );
			
			fastaImportTrackingDTO.setImport_decoy_sequences(import_decoy_sequencesBoolean);
			fastaImportTrackingDTO.setRequire_confirm_before_insert( require_confirm_before_insertBoolean );

			FASTAImportTrackingDAO.getInstance().save( fastaImportTrackingDTO );
			
			
			ProcessImportFASTAFileThread.getInstance().awaken();
			
			
			ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
			
			importFASTAServletResponse.setStatusSuccess(true);
			
			importFASTAServletResponse.setId( fastaImportTrackingDTO.getId() );
			
			OutputStream responseOutputStream = response.getOutputStream();
			

			// send the JSON response 
			ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
			mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
			
			log.info( "Completed Uploads");
			
			
		} catch ( FailResponseSentException e ) {
			
			cleanupOnError( uploadedFileOnDisk );
			
			
		} catch (FileSizeLimitExceededException ex ) {

			ex.getActualSize();
			
			ex.getPermittedSize();
			

			log.error( "SizeLimitExceededException: " + ex.toString(), ex );
			

			response.setStatus( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );
			


			ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
			
			importFASTAServletResponse.setStatusSuccess(false);
			
			importFASTAServletResponse.setFileSizeLimitExceeded(true);
			importFASTAServletResponse.setMaxSize( ex.getPermittedSize() );
			importFASTAServletResponse.setMaxSizeFormatted( FileUploadConstants.MAX_FILE_UPLOAD_SIZE_FORMATTED );
			
			
			OutputStream responseOutputStream = response.getOutputStream();
			

			// send the JSON response 
			ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
			mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
			
			responseOutputStream.flush();
			responseOutputStream.close();
			

			cleanupOnError( uploadedFileOnDisk );
			
			//  response.sendError  sends a HTML page so don't use here since return JSON instead
			
//			response.sendError( HttpServletResponse.SC_BAD_REQUEST /* 400  */ );
			
//			response.sendError( HttpServletResponse.SC_BAD_REQUEST /* 400  */, responseJSONString );
			
			

//			throw new ServletException( "SizeLimitExceeded: ", ex );
			

		} catch (Exception ex){

			log.error( "Exception: " + ex.toString(), ex );
			

			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR /* 500  */ );
			

			ImportFASTAServletResponse importFASTAServletResponse = new ImportFASTAServletResponse();
			
			importFASTAServletResponse.setStatusSuccess(false);
			
			OutputStream responseOutputStream = response.getOutputStream();
			

			// send the JSON response 
			ObjectMapper mapper = new ObjectMapper();  //  Jackson JSON library object
			mapper.writeValue( responseOutputStream, importFASTAServletResponse ); // where first param can be File, OutputStream or Writer
			
			responseOutputStream.flush();
			responseOutputStream.close();
			

			cleanupOnError( uploadedFileOnDisk );
			
			//  response.sendError  sends a HTML page so don't use here since return JSON instead
			
//			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR /* 500  */ );
			
//			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR /* 500  */, responseJSONString );
			
			
//			throw new ServletException( ex );
		}


		
		//  Example for forwarding to a JSP
		
//        
//        //File uploaded successfully
//        request.setAttribute("message", "File Uploaded Successfully");
//     } catch (Exception ex) {
//        request.setAttribute("message", "File Upload Failed due to " + ex);
//     }          
//  
// }else{
//     request.setAttribute("message",
//                          "Sorry this Servlet only handles file upload request");
// }
//
// request.getRequestDispatcher("/result.jsp").forward(request, response);




	}
	
	private void cleanupOnError( File uploadedFileOnDisk ) {
		
		if ( uploadedFileOnDisk != null && uploadedFileOnDisk.exists() ) {
			
			uploadedFileOnDisk.delete();
		}
		
	}
	
	
	private class FailResponseSentException extends Exception {

		private static final long serialVersionUID = 1L;
		
		
	}

}
