package org.yeastrc.nrseq_fasta_importer.objects;

public class UserProvidedTaxonomyIds {
	
	private int noTaxonomyIdRecordId;
	private int taxonomyId;
	
	
	public int getNoTaxonomyIdRecordId() {
		return noTaxonomyIdRecordId;
	}
	public void setNoTaxonomyIdRecordId(int noTaxonomyIdRecordId) {
		this.noTaxonomyIdRecordId = noTaxonomyIdRecordId;
	}
	public int getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(int taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
}
