package org.yeastrc.nrseq_fasta_importer.www.webservices;


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
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceErrorMessageConstants;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblDatabaseDAO;

@Path("/nrseqIdForFilename")
public class Get_NRSEQ_ID_For_Filename_Webservice {


	private static final Logger log = Logger.getLogger(Get_NRSEQ_ID_For_Filename_Webservice.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get") 
	
	public Get_NRSEQ_ID_For_Filename_WebserviceResult get( @QueryParam( "filename" ) String filename,
			@Context HttpServletRequest request )
	throws Exception {

//		if (true)
//		throw new Exception("Forced Error");
		
		try {
			
			Get_NRSEQ_ID_For_Filename_WebserviceResult result = new Get_NRSEQ_ID_For_Filename_WebserviceResult();
			
			Integer nrseqFileId = YRC_NRSEQ_tblDatabaseDAO.getInstance().getIdForName( filename );
			
			result.setNrseqFileId( nrseqFileId );
			
			return result;
		

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
	
	
	public static class Get_NRSEQ_ID_For_Filename_WebserviceResult {
		
		private Integer nrseqFileId;

		public Integer getNrseqFileId() {
			return nrseqFileId;
		}

		public void setNrseqFileId(Integer nrseqFileId) {
			this.nrseqFileId = nrseqFileId;
		}
	}

}
