package org.yeastrc.nrseq_fasta_importer.taxonomy_id_services;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dao.LocalCache_NCBI_Protein_By_GIAccesssionID_DAO;
import org.yeastrc.nrseq_fasta_importer.dto.LocalCache_NCBI_Protein_By_GIAccesssionID_DTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse;


/**
 * 
 *
 */
public class GetNCBITaxonomyDataFromNCBI_ProteinDB_Service {

	
	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFromNCBI_ProteinDB_Service.class);
	
	private GetNCBITaxonomyDataFromNCBI_ProteinDB_Service() { }
	public static GetNCBITaxonomyDataFromNCBI_ProteinDB_Service getInstance() { 
		return new GetNCBITaxonomyDataFromNCBI_ProteinDB_Service(); 
	}

	
	/**
	 * @param giAccessionNumber - The NCBI gi accession number
	 * @return
	 * @throws Exception
	 */
	public GetNCBITaxonomyDataFromNCBI_ProteinDB_ServiceResponse getNCBITaxonomyDataFromNCBIProteinService( int giAccessionNumber ) throws Exception {
		
		GetNCBITaxonomyDataFromNCBI_ProteinDB_ServiceResponse response = new GetNCBITaxonomyDataFromNCBI_ProteinDB_ServiceResponse();
	
		Integer taxonomyId = null;
		
		LocalCache_NCBI_Protein_By_GIAccesssionID_DTO cacheValue = 
				LocalCache_NCBI_Protein_By_GIAccesssionID_DAO.getInstance().getTaxonomyIdForAccesssion( giAccessionNumber );

		if ( cacheValue != null && cacheValue.getTaxonomyId() != null ) {

			taxonomyId = cacheValue.getTaxonomyId();
		}
		
		
		if ( taxonomyId == null ) {
			
			GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse webserviceResponse = 
					GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice.getInstance().getNCBITaxonomyDataFromNCBIProteinWebservice( giAccessionNumber );
			
			if ( webserviceResponse.getTaxonomyId() != null ) {

				taxonomyId = webserviceResponse.getTaxonomyId();
				

				cacheValue = new LocalCache_NCBI_Protein_By_GIAccesssionID_DTO();

				cacheValue.setNcbiAccesssionId( giAccessionNumber );
				cacheValue.setTaxonomyId( taxonomyId );

				LocalCache_NCBI_Protein_By_GIAccesssionID_DAO.getInstance().saveOrUpdateOnGiAccessionId(cacheValue);
				
			}
		}
		
		response.setTaxonomyId( taxonomyId );
		
		return response;
	}
}
