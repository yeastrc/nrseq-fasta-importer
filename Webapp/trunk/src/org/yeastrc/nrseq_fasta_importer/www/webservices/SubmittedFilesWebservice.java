package org.yeastrc.nrseq_fasta_importer.www.webservices;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceErrorMessageConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.process_fasta_file.ProcessImportFASTAFile;
import org.yeastrc.nrseq_fasta_importer.threads.ProcessImportFASTAFileThread;

@Path("/submittedFiles")
public class SubmittedFilesWebservice {


	private static final Logger log = Logger.getLogger(SubmittedFilesWebservice.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list") 
	
	public List<ListResultItem> list( @QueryParam( "status" ) List<String> statusList,
										  @Context HttpServletRequest request )
	throws Exception {

//		if (true)
//		throw new Exception("Forced Error");
		
		try {
			
			List<FASTAImportTrackingDTO>  fastaImportTrackingDTOList = 
					FASTAImportTrackingDAO.getInstance().getByStatus( statusList );
			
			
			List<ListResultItem> listResultItems = new ArrayList<>( fastaImportTrackingDTOList.size() );
			
			for ( FASTAImportTrackingDTO item : fastaImportTrackingDTOList ) {
				
				ListResultItem listResultItem = new ListResultItem();
				
				listResultItem.setItem( item );
				

				GetStatusResult getStatusResult = processStatus( item.getStatus(), item.getId() );

				listResultItem.setStatusData( getStatusResult );
				
				listResultItems.add(listResultItem);
			}
			
			
			return listResultItems;
		

		} catch ( WebApplicationException e ) {

			throw e;
			
		} catch ( Exception e ) {
			
			String msg = "Exception caught: " + e.toString();
			
			log.error( msg, e );
			
			throw new WebApplicationException(
					Response.status( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_STATUS_CODE )  //  Send HTTP code
					.entity( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_TEXT ) // This string will be passed to the client
					.build()
					);
		}
		
	}
	
	
	private class ListResultItem {
		
		FASTAImportTrackingDTO item;
		
		GetStatusResult statusData;

		public FASTAImportTrackingDTO getItem() {
			return item;
		}

		public void setItem(FASTAImportTrackingDTO item) {
			this.item = item;
		}

		public GetStatusResult getStatusData() {
			return statusData;
		}

		public void setStatusData(GetStatusResult statusData) {
			this.statusData = statusData;
		}
		
	}
	
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get") 
	
	public GetResult get( @QueryParam( "id" ) int id,
										  @Context HttpServletRequest request )
	throws Exception {

//		if (true)
//		throw new Exception("Forced Error");
		
		try {
			
			FASTAImportTrackingDTO  item = 
					FASTAImportTrackingDAO.getInstance().getForId( id );
			
			GetResult getResult = new GetResult();
			
			getResult.setItem( item );
			
			if ( item != null ) {
				
				GetStatusResult getStatusResult = processStatus( item.getStatus(), id );

				getResult.setStatusData( getStatusResult );	
				
			} else {
				
				GetStatusResult getStatusResult = new GetStatusResult();
				
				getStatusResult.setNoRecordFound(true);
				
				getResult.setStatusData( getStatusResult );	
			}
			
			return getResult;
		

		} catch ( WebApplicationException e ) {

			throw e;
			
		} catch ( Exception e ) {
			
			String msg = "Exception caught: " + e.toString();
			
			log.error( msg, e );
			
			throw new WebApplicationException(
					Response.status( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_STATUS_CODE )  //  Send HTTP code
					.entity( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_TEXT ) // This string will be passed to the client
					.build()
					);
		}
		
	}
	
	private class GetResult {
		
		FASTAImportTrackingDTO item;
		
		GetStatusResult statusData;
		

		
		public GetStatusResult getStatusData() {
			return statusData;
		}

		public void setStatusData(GetStatusResult statusData) {
			this.statusData = statusData;
		}

		public FASTAImportTrackingDTO getItem() {
			return item;
		}

		public void setItem(FASTAImportTrackingDTO item) {
			this.item = item;
		}
	}
	


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getStatus") 
	
	public GetStatusResult getStatus( @QueryParam( "id" ) int id,
										  @Context HttpServletRequest request )
	throws Exception {

		
		try {
			
			String status = 
					FASTAImportTrackingDAO.getInstance().getStatusForId( id );
			
			GetStatusResult getStatusResult = processStatus( status, id );
			
			return getStatusResult;
		

		} catch ( WebApplicationException e ) {

			throw e;
			
		} catch ( Exception e ) {
			
			String msg = "Exception caught: " + e.toString();
			
			log.error( msg, e );
			
			throw new WebApplicationException(
					Response.status( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_STATUS_CODE )  //  Send HTTP code
					.entity( WebServiceErrorMessageConstants.INTERNAL_SERVER_ERROR_TEXT ) // This string will be passed to the client
					.build()
					);
		}
		
	}
	
	
	
	/**
	 * @param status
	 * @return
	 */
	private GetStatusResult processStatus( String status, int id ) {
		
		GetStatusResult getStatusResult = new GetStatusResult();
		
		getStatusResult.setStatus( status );
		
		if ( status == null ) {
			
			getStatusResult.setNoRecordFound(true);
			
			return getStatusResult; // EARLY RETURN
		}
		
		
		if ( ImportStatusContants.STATUS_IMPORT_COMPLETE.equals( status ) ) {
			
			getStatusResult.setImportComplete(true);
			
		} else if ( ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION.equals( status )
				|| ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS.equals( status )
				|| ImportStatusContants.STATUS_QUEUED_FOR_IMPORT.equals( status )  ) {
			
			getStatusResult.setQueued(true);
			
		} else if ( ImportStatusContants.STATUS_VALIDATION_STARTED.equals( status ) ) {
			
			getStatusResult.setValidationProcessing(true);
			
		} else if ( ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED.equals( status ) ) {
			
			getStatusResult.setFindTaxIdsProcessing(true);
			
		} else if ( ImportStatusContants.STATUS_USER_INPUT_REQUIRED.equals( status ) ) {
			
			getStatusResult.setUserInputRequired(true);
			
		} else if ( ImportStatusContants.STATUS_IMPORT_STARTED.equals( status ) ) {
			
			getStatusResult.setImportProcessing(true);
			
		} else if ( ImportStatusContants.STATUS_VALIDATION_FAILED.equals( status )
				|| ImportStatusContants.STATUS_FIND_TAX_IDS_FAILED.equals( status )
				|| ImportStatusContants.STATUS_IMPORT_FAILED.equals( status )  ) {
			
			getStatusResult.setFailed(true);
		}

		//  get info on currently processing file.
		
		ProcessImportFASTAFileThread processImportFASTAFileThread = ProcessImportFASTAFileThread.getInstance();
		
		ProcessImportFASTAFile processImportFASTAFile = processImportFASTAFileThread.getProcessImportFASTAFile();
		
		if ( processImportFASTAFile != null ) {

			FASTAImportTrackingDTO currentFastaImportTrackingDTO = 
					processImportFASTAFile.getCurrentFastaImportTrackingDTO();

			if ( currentFastaImportTrackingDTO != null && currentFastaImportTrackingDTO.getId() == id ) {
				
				if ( ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED.equals( currentFastaImportTrackingDTO.getStatus() ) 
					  || ImportStatusContants.STATUS_IMPORT_STARTED.equals( currentFastaImportTrackingDTO.getStatus() ) ) {

					getStatusResult.setTotalCount( processImportFASTAFile.getCurrentFASTAEntryCount() );

					getStatusResult.setCurrentProcessCount( processImportFASTAFile.getCurrentSequenceCount() );
				}
			}

		}

		return getStatusResult;
		
	}
	

	private class GetStatusResult {
		
		private String status;
		
		private boolean noRecordFound;
		
		private boolean queued;
		private boolean validationProcessing;
		private boolean findTaxIdsProcessing;
		private boolean userInputRequired;
		private boolean importProcessing;
		private boolean failed;
		private boolean importComplete;
		
		
		
		private Integer currentProcessCount;
		private Integer totalCount;

		
		
		public Integer getCurrentProcessCount() {
			return currentProcessCount;
		}
		public void setCurrentProcessCount(Integer currentProcessCount) {
			this.currentProcessCount = currentProcessCount;
		}
		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}
		public boolean isFindTaxIdsProcessing() {
			return findTaxIdsProcessing;
		}
		public void setFindTaxIdsProcessing(boolean findTaxIdsProcessing) {
			this.findTaxIdsProcessing = findTaxIdsProcessing;
		}
		public boolean isImportProcessing() {
			return importProcessing;
		}
		public void setImportProcessing(boolean importProcessing) {
			this.importProcessing = importProcessing;
		}
		public boolean isNoRecordFound() {
			return noRecordFound;
		}
		public void setNoRecordFound(boolean noRecordFound) {
			this.noRecordFound = noRecordFound;
		}
		public boolean isQueued() {
			return queued;
		}
		public void setQueued(boolean queued) {
			this.queued = queued;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

		public boolean isValidationProcessing() {
			return validationProcessing;
		}
		public void setValidationProcessing(boolean validationProcessing) {
			this.validationProcessing = validationProcessing;
		}
		public boolean isUserInputRequired() {
			return userInputRequired;
		}
		public void setUserInputRequired(boolean userInputRequired) {
			this.userInputRequired = userInputRequired;
		}
		public boolean isFailed() {
			return failed;
		}
		public void setFailed(boolean failed) {
			this.failed = failed;
		}
		public boolean isImportComplete() {
			return importComplete;
		}
		public void setImportComplete(boolean importComplete) {
			this.importComplete = importComplete;
		}
	}

}
