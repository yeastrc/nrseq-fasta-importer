package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto;

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
