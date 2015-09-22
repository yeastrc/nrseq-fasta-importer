package org.yeastrc.nrseq_fasta_importer.www.webservices;



import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.objects.GenericWebserviceResponse;
import org.yeastrc.nrseq_fasta_importer.threads.ProcessImportFASTAFileThread;

@Path("/confirmDoImport")
public class ConfirmDoImportWebservice {


	private static final Logger log = Logger.getLogger(ConfirmDoImportWebservice.class);
	

	
	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public GenericWebserviceResponse update( @FormParam( "fasta_import_tracking_id" ) Integer fasta_import_tracking_id,
			@Context HttpServletRequest request ) throws Exception {

		
		GenericWebserviceResponse genericWebserviceResponse = new GenericWebserviceResponse();
		
		try {
			
			if ( fasta_import_tracking_id == null ) {

				throw new WebApplicationException(
						Response.status( WebServiceErrorMessageConstants.INVALID_PARAMETER_STATUS_CODE )  //  Send HTTP code
						.entity( WebServiceErrorMessageConstants.INVALID_PARAMETER_TEXT ) // This string will be passed to the client
						.build()
						);
			}
			
			String newStatus = ImportStatusContants.STATUS_QUEUED_FOR_IMPORT;
			
			FASTAImportTrackingDAO.getInstance().updateStatus( newStatus, fasta_import_tracking_id );

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
	
	

}
