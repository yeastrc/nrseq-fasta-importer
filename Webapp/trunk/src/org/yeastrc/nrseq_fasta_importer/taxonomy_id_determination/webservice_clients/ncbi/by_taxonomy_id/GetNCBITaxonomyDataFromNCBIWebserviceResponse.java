package org.yeastrc.nrseq_fasta_importer.ncbi_taxonomy_data;

import org.yeastrc.nrseq_fasta_importer.ncbi_taxonomy_data_dto.NcbiTaxonomyDataRootDTO;
import org.yeastrc.nrseq_fasta_importer.ncbi_taxonomy_data_dto.NcbiTaxonomyErrorRootDTO;

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
