package org.yeastrc.nrseq_fasta_importer.www.webservices;

import java.util.ArrayList;
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
import org.yeastrc.nrseq_fasta_importer.objects.FASTAHeaderNoTaxIdDeterminedResponseItem;
import org.yeastrc.nrseq_fasta_importer.objects.FASTAHeaderNoTaxIdDeterminedSuggestionItem;
import org.yeastrc.nrseq_fasta_importer.objects.GenericWebserviceResponse;
import org.yeastrc.nrseq_fasta_importer.objects.UserProvidedTaxonomyId;



@Path("/headerNoTaxonomy")
public class HeaderNoTaxonomyWebservice {


	private static final Logger log = Logger.getLogger(HeaderNoTaxonomyWebservice.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list") 
	
	public List<FASTAHeaderNoTaxIdDeterminedResponseItem> list( @QueryParam( "fastaImportTrackingId" ) int fastaImportTrackingId,
			@QueryParam( "idGreatThan" ) Integer idGreatThan, // If populated, only return ids > idGreatThan
			@Context HttpServletRequest request )
	throws Exception {

//		if (true)
//		throw new Exception("Forced Error");
		
		try {
			
			List<FASTAHeaderNoTaxIdDeterminedDTO>  dbList = null;
			
			if ( idGreatThan == null ) {
				dbList = FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingId( fastaImportTrackingId );
			} else {
				
				dbList = FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingId_idGreatThan( fastaImportTrackingId, idGreatThan );
			}
			
			List<FASTAHeaderNoTaxIdDeterminedResponseItem> responseItemList = new ArrayList<>( dbList.size() );
			
			//  Get recommended taxonomy id / species id for each entry
			for ( FASTAHeaderNoTaxIdDeterminedDTO item : dbList ) {
				
				FASTAHeaderNoTaxIdDeterminedResponseItem responseItem = new FASTAHeaderNoTaxIdDeterminedResponseItem();
				responseItemList.add( responseItem );
				
				responseItem.setItem( item );
				
				//  If user hasn't entered a taxonomy id, suggest one
				
//				if ( item.getUserAssignedTaxId() == null ) {

//					if ( "Spc97-yeast".equals( item.getHeaderName() ) ) {
//
//						int z = 0;
//					}

//					if ( "T08A9.3_Modified".equals( item.getHeaderName() ) ) {
//	
//						int z = 0;
//					}
				
					

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
						
						if ( yrc_NRSEQ_tblProteinDTOList != null && ( ! yrc_NRSEQ_tblProteinDTOList.isEmpty() ) ) {
							
							List<FASTAHeaderNoTaxIdDeterminedSuggestionItem> suggestionItemList = new ArrayList<>();
							responseItem.setSuggestions(suggestionItemList);

							for ( YRC_NRSEQ_tblProteinDTO yrc_NRSEQ_tblProteinDTO : yrc_NRSEQ_tblProteinDTOList ) {

								if ( yrc_NRSEQ_tblProteinDTO.getSpeciesID() == 0 ) {
									
									continue;  //  Skip records where species id == 0
								}

//								FASTAHeaderNoTaxIdDeterminedSuggestionItem suggestionItem = new FASTAHeaderNoTaxIdDeterminedSuggestionItem();
//
//								suggestionItem.setTaxonomyId( yrc_NRSEQ_tblProteinDTO.getSpeciesID() );
//								suggestionItemList.add( suggestionItem );
								
								//  WAS
								List<String> accessionStringList =
										YRC_NRSEQ_tblProteinDatabaseDAO.getInstance().getAccessionStringListForProteinId( yrc_NRSEQ_tblProteinDTO.getId() );

								for ( String accessionString : accessionStringList ) {

									if ( item.getHeaderName().equals( accessionString ) ) {

										FASTAHeaderNoTaxIdDeterminedSuggestionItem suggestionItem = new FASTAHeaderNoTaxIdDeterminedSuggestionItem();

										suggestionItem.setTaxonomyId( yrc_NRSEQ_tblProteinDTO.getSpeciesID() );

										suggestionItemList.add( suggestionItem );
									}
								}
							}

						}
					}
//				}				
			}
			
			return responseItemList;
		

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
