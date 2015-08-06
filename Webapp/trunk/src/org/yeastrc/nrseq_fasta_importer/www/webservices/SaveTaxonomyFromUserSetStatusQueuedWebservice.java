package org.yeastrc.nrseq_fasta_importer.www.webservices;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceErrorMessageConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.objects.GenericWebserviceResponse;
import org.yeastrc.nrseq_fasta_importer.objects.UserProvidedTaxonomyIds;
import org.yeastrc.nrseq_fasta_importer.threads.ProcessImportFASTAFileThread;

@Path("/saveTaxonomyFromUser")
public class SaveTaxonomyFromUserSetStatusQueuedWebservice {


	private static final Logger log = Logger.getLogger(SaveTaxonomyFromUserSetStatusQueuedWebservice.class);
	

	
	@POST
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/save")
	public GenericWebserviceResponse save( SaveTaxonomyFromUserSetStatusQueuedRequest saveTaxonomyFromUserSetStatusQueuedRequest,
			@Context HttpServletRequest request ) throws Exception {


//		if (true)
//		throw new Exception("Forced Error");
		
		GenericWebserviceResponse genericWebserviceResponse = new GenericWebserviceResponse();
		
		try {
			
			List<UserProvidedTaxonomyIds> userProvidedTaxonomyIds = saveTaxonomyFromUserSetStatusQueuedRequest.getUserProvidedTaxonomyIds();
			
			FASTAHeaderNoTaxIdDeterminedDAO.getInstance().update_userAssignedTaxId( userProvidedTaxonomyIds );
			
			String newStatus = ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, saveTaxonomyFromUserSetStatusQueuedRequest.getFasta_import_tracking_id() );

			ProcessImportFASTAFileThread.getInstance().awaken();
			

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
		
		genericWebserviceResponse.setStatus(true);
		
		return genericWebserviceResponse;
		
	}
	
	public static class SaveTaxonomyFromUserSetStatusQueuedRequest {
		
		private int fasta_import_tracking_id;
		
		private List<UserProvidedTaxonomyIds> userProvidedTaxonomyIds;

		public int getFasta_import_tracking_id() {
			return fasta_import_tracking_id;
		}

		public void setFasta_import_tracking_id(int fasta_import_tracking_id) {
			this.fasta_import_tracking_id = fasta_import_tracking_id;
		}

		public List<UserProvidedTaxonomyIds> getUserProvidedTaxonomyIds() {
			return userProvidedTaxonomyIds;
		}

		public void setUserProvidedTaxonomyIds(
				List<UserProvidedTaxonomyIds> userProvidedTaxonomyIds) {
			this.userProvidedTaxonomyIds = userProvidedTaxonomyIds;
		}

		
	}
	

}
