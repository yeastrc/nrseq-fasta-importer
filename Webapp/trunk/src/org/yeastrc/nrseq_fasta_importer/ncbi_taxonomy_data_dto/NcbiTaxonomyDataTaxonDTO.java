package org.yeastrc.nrseq_fasta_importer.ncbi_taxonomy_data_dto;

import javax.xml.bind.annotation.XmlElement;

public class NcbiTaxonomyDataTaxonDTO {

	private String scientificName;

	@XmlElement(name="ScientificName")
	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
}
