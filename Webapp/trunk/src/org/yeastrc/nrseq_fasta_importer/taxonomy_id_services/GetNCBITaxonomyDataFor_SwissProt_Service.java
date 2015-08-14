package org.yeastrc.nrseq_fasta_importer.taxonomy_id_services;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dao.LocalCache_Swiss_prot_DAO;
import org.yeastrc.nrseq_fasta_importer.dto.LocalCache_Swiss_prot_DTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse;


/**
 * 
 *
 */
public class GetNCBITaxonomyDataFor_SwissProt_Service {

	
	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFor_SwissProt_Service.class);
	
	private GetNCBITaxonomyDataFor_SwissProt_Service() { }
	public static GetNCBITaxonomyDataFor_SwissProt_Service getInstance() { 
		return new GetNCBITaxonomyDataFor_SwissProt_Service(); 
	}


	public GetNCBITaxonomyDataFor_SwissProt_ServiceResponse getNCBITaxonomyDataFrom_Uniprot_Service( String accessionPart1, String accessionPart2 ) throws Exception {
		
		GetNCBITaxonomyDataFor_SwissProt_ServiceResponse response = new GetNCBITaxonomyDataFor_SwissProt_ServiceResponse();
	
		Integer taxonomyId = null;
		

		/*
		 * The second part of the entry name denotes a specific organism.
		 * However, if it starts with 9, it denotes an taxonomy GROUP, and does not
		 * uniquely identify an organism. (if it starts with 9, we have to lookup
		 * via web services, done later.)
		 * 
		 * See: http://web.expasy.org/docs/userman.html for more information
		 * 
		 * Attempt a local lookup of the taxonomy ID based on the second part
		 */
		if( accessionPart2 != null && ! accessionPart2.startsWith( "9" ) ) {				

			LocalCache_Swiss_prot_DTO cacheValue = 
					LocalCache_Swiss_prot_DAO.getInstance().getTaxonomyIdForAccesssion( accessionPart2 );

			if ( cacheValue != null && cacheValue.getTaxonomyId() != null ) {

				taxonomyId = cacheValue.getTaxonomyId();
			}
		}
		
		if ( taxonomyId == null ) {
			

			/*
			 * If it could not be found locally, or if the second part began with
			 * a "9", then lookup via web services from uniprot
			 */
			
			String lookupAccession =  accessionPart1 + "_" + accessionPart2;
			
			GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse webserviceResponse = 
					GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice.getInstance().getTaxonomyID( lookupAccession );
			
			if ( webserviceResponse.getTaxonomyId() != null ) {

				taxonomyId = webserviceResponse.getTaxonomyId();
				
				
				if( accessionPart2 != null && ! accessionPart2.startsWith( "9" ) ) {
					
					//  Only cache if accessionPart2 does not start with a "9"

					LocalCache_Swiss_prot_DTO cacheValue = new LocalCache_Swiss_prot_DTO();

					cacheValue.setAccession( accessionPart2 ); // cache on accessionPart2 only
					cacheValue.setTaxonomyId( taxonomyId );

					LocalCache_Swiss_prot_DAO.getInstance().saveOrUpdateOnAccession(cacheValue);
				}				
			}
		}
		
		response.setTaxonomyId( taxonomyId );
		
		return response;
	}
}
