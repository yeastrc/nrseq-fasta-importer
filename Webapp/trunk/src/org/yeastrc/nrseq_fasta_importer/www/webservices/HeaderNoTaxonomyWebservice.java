package org.yeastrc.nrseq_fasta_importer.www.webservices;

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
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceErrorMessageConstants;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;

@Path("/headerNoTaxonomy")
public class HeaderNoTaxonomyWebservice {


	private static final Logger log = Logger.getLogger(HeaderNoTaxonomyWebservice.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list") 
	
	public List<FASTAHeaderNoTaxIdDeterminedDTO> list( @QueryParam( "fastaImportTrackingId" ) int fastaImportTrackingId,
			@QueryParam( "idGreatThan" ) Integer idGreatThan, // If populated, only return ids > idGreatThan
			@Context HttpServletRequest request )
	throws Exception {

//		if (true)
//		throw new Exception("Forced Error");
		
		try {
			
			List<FASTAHeaderNoTaxIdDeterminedDTO>  returnList = null;
			
			if ( idGreatThan == null ) {
				returnList = FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingId( fastaImportTrackingId );
			} else {
				
				returnList = FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingId_idGreatThan( fastaImportTrackingId, idGreatThan );
			}
			
			return returnList;
		

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
	
	

}
