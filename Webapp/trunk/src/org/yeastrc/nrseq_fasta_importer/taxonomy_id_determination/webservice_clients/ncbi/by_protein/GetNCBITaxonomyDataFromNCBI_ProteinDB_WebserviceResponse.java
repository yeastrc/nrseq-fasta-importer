package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein;

import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.webservice_response_dto.NCBIProteinResponseDTO;

/**
 * 
 *
 */
public class GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse {

	private Integer taxonomyId;
	private String errorMsg;

	private NCBIProteinResponseDTO ncbiProteinResponseDTO;
	
	
	
	public Integer getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public NCBIProteinResponseDTO getNcbiProteinResponseDTO() {
		return ncbiProteinResponseDTO;
	}

	public void setNcbiProteinResponseDTO(
			NCBIProteinResponseDTO ncbiProteinResponseDTO) {
		this.ncbiProteinResponseDTO = ncbiProteinResponseDTO;
	}

}
