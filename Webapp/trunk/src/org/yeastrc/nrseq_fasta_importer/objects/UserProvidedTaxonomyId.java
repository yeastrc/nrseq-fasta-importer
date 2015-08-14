package org.yeastrc.nrseq_fasta_importer.objects;

public class UserProvidedTaxonomyId {
	
	private int noTaxonomyIdRecordId;
	private Integer taxonomyId;
	
	
	public int getNoTaxonomyIdRecordId() {
		return noTaxonomyIdRecordId;
	}
	public void setNoTaxonomyIdRecordId(int noTaxonomyIdRecordId) {
		this.noTaxonomyIdRecordId = noTaxonomyIdRecordId;
	}
	public Integer getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
}
