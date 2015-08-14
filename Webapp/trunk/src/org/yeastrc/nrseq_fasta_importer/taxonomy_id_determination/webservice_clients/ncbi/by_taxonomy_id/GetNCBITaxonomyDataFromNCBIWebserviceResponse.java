package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id;

import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyDataRootDTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyErrorRootDTO;

public class GetNCBITaxonomyDataFromNCBIWebserviceResponse {

	private NcbiTaxonomyDataRootDTO ncbiTaxonomyDataRootDTO;
	private NcbiTaxonomyErrorRootDTO ncbiTaxonomyErrorRootDTO;
	
	
	public NcbiTaxonomyDataRootDTO getNcbiTaxonomyDataRootDTO() {
		return ncbiTaxonomyDataRootDTO;
	}
	public void setNcbiTaxonomyDataRootDTO(
			NcbiTaxonomyDataRootDTO ncbiTaxonomyDataRootDTO) {
		this.ncbiTaxonomyDataRootDTO = ncbiTaxonomyDataRootDTO;
	}
	public NcbiTaxonomyErrorRootDTO getNcbiTaxonomyErrorRootDTO() {
		return ncbiTaxonomyErrorRootDTO;
	}
	public void setNcbiTaxonomyErrorRootDTO(
			NcbiTaxonomyErrorRootDTO ncbiTaxonomyErrorRootDTO) {
		this.ncbiTaxonomyErrorRootDTO = ncbiTaxonomyErrorRootDTO;
	}
}
