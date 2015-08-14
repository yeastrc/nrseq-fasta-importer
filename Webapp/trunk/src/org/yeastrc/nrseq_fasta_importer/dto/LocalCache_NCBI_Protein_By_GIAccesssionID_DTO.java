package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table local_cache_ncbi_protein_by_gi_accession_id
 * 
 * Caches local DB for data from NCBI Protein by gi accession id webservice
 *
 */
public class LocalCache_NCBI_Protein_By_GIAccesssionID_DTO {
	
	private int ncbiAccesssionId;
	private Integer taxonomyId;
	
	
	public int getNcbiAccesssionId() {
		return ncbiAccesssionId;
	}
	public void setNcbiAccesssionId(int ncbiAccesssionId) {
		this.ncbiAccesssionId = ncbiAccesssionId;
	}
	public Integer getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}


}

//CREATE TABLE local_cache_ncbi_protein_by_gi_accession_id (
//		  gi_accession_id INT NOT NULL,
//		  taxonomy_id INT NULL,
