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
import org.yeastrc.nrseq_fasta_importer.constants.TaxonomyIdZeroTaxonomyLabelConstants;
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceErrorMessageConstants;
import org.yeastrc.nrseq_fasta_importer.objects.NcbiTaxonomyDataResponse;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.GetNCBITaxonomyDataFromNCBIWebservice;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.GetNCBITaxonomyDataFromNCBIWebserviceResponse;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyDataRootDTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyDataTaxonDTO;

@Path("/ncbiDataForTaxonomyId")
public class GetDataFromNCBIForTaxonomyIdWebservice {


	private static final Logger log = Logger.getLogger(GetDataFromNCBIForTaxonomyIdWebservice.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get") 
	
	public NcbiTaxonomyDataResponse get( @QueryParam( "taxonomyId" ) Long taxonomyId,
			@Context HttpServletRequest request )
	throws Exception {


		if ( taxonomyId == null ) {
		
			throw new WebApplicationException(
					Response.status( WebServiceErrorMessageConstants.INVALID_PARAMETER_STATUS_CODE )  //  Send HTTP code
					.entity( WebServiceErrorMessageConstants.INVALID_PARAMETER_TEXT ) // This string will be passed to the client
					.build()
					);
		}
		
		try {
			
//			 <ScientificName>Flaveria trinervia</ScientificName> 
			
			NcbiTaxonomyDataResponse ncbiTaxonomyDataResponse = new NcbiTaxonomyDataResponse();
			
			if ( taxonomyId == 0 ) {
				
				//   Hard coded response for taxonomy id zero
				
				ncbiTaxonomyDataResponse.setScientificNameFound(true);
				ncbiTaxonomyDataResponse.setScientificName( TaxonomyIdZeroTaxonomyLabelConstants.TAXONOMY_ID_ZERO_LABEL );
				
				return ncbiTaxonomyDataResponse;  //  EARLY RETURN
			}
			
			GetNCBITaxonomyDataFromNCBIWebserviceResponse getNCBITaxonomyDataFromNCBIWebserviceResponse =
					GetNCBITaxonomyDataFromNCBIWebservice.getInstance().getNCBITaxonomyDataFromNCBIWebservice( taxonomyId );
			
			if ( getNCBITaxonomyDataFromNCBIWebserviceResponse.getNcbiTaxonomyDataRootDTO() != null ) {
				
				NcbiTaxonomyDataRootDTO ncbiTaxonomyDataRootDTO = getNCBITaxonomyDataFromNCBIWebserviceResponse.getNcbiTaxonomyDataRootDTO();
				
				List<NcbiTaxonomyDataTaxonDTO> ncbiTaxonomyDataTaxonDTOList = ncbiTaxonomyDataRootDTO.getNcbiTaxonomyDataTaxonDTOList();

				if ( ncbiTaxonomyDataTaxonDTOList != null && ( ! ncbiTaxonomyDataTaxonDTOList.isEmpty() ) ) {
					
					NcbiTaxonomyDataTaxonDTO ncbiTaxonomyDataTaxonDTO = ncbiTaxonomyDataTaxonDTOList.get(0);
					
					String scientificName = ncbiTaxonomyDataTaxonDTO.getScientificName();
					
					ncbiTaxonomyDataResponse.setScientificName( scientificName );
					
					ncbiTaxonomyDataResponse.setScientificNameFound(true);
				}
			}

			return ncbiTaxonomyDataResponse;
		

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
