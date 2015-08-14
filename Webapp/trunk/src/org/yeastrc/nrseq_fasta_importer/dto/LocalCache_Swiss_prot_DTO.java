package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table local_cache_swiss_prot_data
 * 
 * Caches local DB for data for the Swissprot retrieved from the Uniprot webservice
 *
 */
public class LocalCache_Swiss_prot_DTO {
	
	private String accession;
	private Integer taxonomyId;
	
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public Integer getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}


}

//CREATE TABLE local_cache_swiss_prot_data (
//		  accession VARCHAR(255) NOT NULL,
//		  taxonomy_id INT NULL,
