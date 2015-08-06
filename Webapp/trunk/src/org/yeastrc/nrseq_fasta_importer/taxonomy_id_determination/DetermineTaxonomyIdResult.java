package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;

public class DetermineTaxonomyIdResult {

	private Integer taxonomyId;
	
	private String message;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
}
