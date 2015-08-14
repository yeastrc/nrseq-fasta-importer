package org.yeastrc.nrseq_fasta_importer.taxonomy_id_services;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dao.LocalCache_Uniprot_DAO;
import org.yeastrc.nrseq_fasta_importer.dto.LocalCache_Uniprot_DTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse;


/**
 * 
 *
 */
public class GetNCBITaxonomyDataFrom_Uniprot_Service {

	
	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFrom_Uniprot_Service.class);
	
	private GetNCBITaxonomyDataFrom_Uniprot_Service() { }
	public static GetNCBITaxonomyDataFrom_Uniprot_Service getInstance() { 
		return new GetNCBITaxonomyDataFrom_Uniprot_Service(); 
	}

	
	/**
	 * @param accession - The Uniprot accession number
	 * @return
	 * @throws Exception
	 */
	public GetNCBITaxonomyDataFrom_Uniprot_ServiceResponse getNCBITaxonomyDataFrom_Uniprot_Service( String accession ) throws Exception {
		
		GetNCBITaxonomyDataFrom_Uniprot_ServiceResponse response = new GetNCBITaxonomyDataFrom_Uniprot_ServiceResponse();
	
		Integer taxonomyId = null;
		
		
		LocalCache_Uniprot_DTO cacheValue = 
				LocalCache_Uniprot_DAO.getInstance().getTaxonomyIdForAccesssion( accession );
		
		if ( cacheValue != null && cacheValue.getTaxonomyId() != null ) {

			taxonomyId = cacheValue.getTaxonomyId();
		}
		
		if ( taxonomyId == null ) {
			
			GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse webserviceResponse = 
					GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice.getInstance().getTaxonomyID( accession );
			
			if ( webserviceResponse.getTaxonomyId() != null ) {

				taxonomyId = webserviceResponse.getTaxonomyId();
				
				cacheValue = new LocalCache_Uniprot_DTO();
				
				cacheValue.setAccession( accession );
				cacheValue.setTaxonomyId( taxonomyId );
				
				LocalCache_Uniprot_DAO.getInstance().saveOrUpdateOnAccession(cacheValue);
				
			}
		}
		
		response.setTaxonomyId( taxonomyId );
		
		return response;
	}
}
