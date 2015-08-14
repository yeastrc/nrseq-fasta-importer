package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="eFetchResult")
public class NcbiTaxonomyErrorRootDTO {

	private String error;

	@XmlElement(name="ERROR")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
