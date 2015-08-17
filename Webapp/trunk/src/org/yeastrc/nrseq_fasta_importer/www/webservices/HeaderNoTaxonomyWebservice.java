package org.yeastrc.nrseq_fasta_importer.www.webservices;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedSequenceDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblProteinSequenceDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedSequenceDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinDTO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinSequenceDTO;
import org.yeastrc.nrseq_fasta_importer.objects.GenericWebserviceResponse;
import org.yeastrc.nrseq_fasta_importer.objects.UserProvidedTaxonomyId;



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
			
			//  Get recommended taxonomy id / species id for each entry
			for ( FASTAHeaderNoTaxIdDeterminedDTO item : returnList ) {
				
				
				//  If user hasn't entered a taxonomy id, suggest one
				
				if ( item.getUserAssignedTaxId() == null ) {

					if ( "Spc97-yeast".equals( item.getHeaderName() ) ) {

						int z = 0;
					}

					FASTAHeaderNoTaxIdDeterminedSequenceDTO fASTAHeaderNoTaxIdDeterminedSequenceDTO =
							FASTAHeaderNoTaxIdDeterminedSequenceDAO.getInstance().getForFastaHeaderNoTaxIdDeterminedId( item.getId() );

					if ( fASTAHeaderNoTaxIdDeterminedSequenceDTO == null ) {

						String msg = "Unable to get sequence record fASTAHeaderNoTaxIdDeterminedSequenceDTO for id " + item.getId();
						log.error( msg );
						throw new Exception(msg);
					}

					YRC_NRSEQ_tblProteinSequenceDTO yrc_NRSEQ_tblProteinSequenceDTO =
							YRC_NRSEQ_tblProteinSequenceDAO.getInstance().getForSequence( fASTAHeaderNoTaxIdDeterminedSequenceDTO.getSequence() );

					if ( yrc_NRSEQ_tblProteinSequenceDTO != null ) {

						int sequenceId = yrc_NRSEQ_tblProteinSequenceDTO.getId();

						List<YRC_NRSEQ_tblProteinDTO> yrc_NRSEQ_tblProteinDTOList = 
								YRC_NRSEQ_tblProteinDAO.getInstance().getForSequenceId( sequenceId );

						if ( yrc_NRSEQ_tblProteinDTOList.size() == 1 ) {

							YRC_NRSEQ_tblProteinDTO yrc_NRSEQ_tblProteinDTO = yrc_NRSEQ_tblProteinDTOList.get( 0 );

							List<String> accessionStringList =
									YRC_NRSEQ_tblProteinDatabaseDAO.getInstance().getAccessionStringListForProteinId( yrc_NRSEQ_tblProteinDTO.getId() );

							if ( accessionStringList.size() == 1 ) {

								item.setUserAssignedTaxId( yrc_NRSEQ_tblProteinDTO.getSpeciesID() );
							}

						}




					}
				}				
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
	

	@POST
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public GenericWebserviceResponse save( UserProvidedTaxonomyId userProvidedTaxonomyId,
			@Context HttpServletRequest request ) throws Exception {


//		if (true)
//		throw new Exception("Forced Error");
		
		GenericWebserviceResponse genericWebserviceResponse = new GenericWebserviceResponse();
		
		try {
			
			FASTAHeaderNoTaxIdDeterminedDAO.getInstance().update_userAssignedTaxId( userProvidedTaxonomyId );
			
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
