package org.yeastrc.nrseq_fasta_importer.objects;

public class NcbiTaxonomyDataResponse {

	private String scientificName;
	
	private boolean scientificNameFound;

	public boolean isScientificNameFound() {
		return scientificNameFound;
	}

	public void setScientificNameFound(boolean scientificNameFound) {
		this.scientificNameFound = scientificNameFound;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
}
