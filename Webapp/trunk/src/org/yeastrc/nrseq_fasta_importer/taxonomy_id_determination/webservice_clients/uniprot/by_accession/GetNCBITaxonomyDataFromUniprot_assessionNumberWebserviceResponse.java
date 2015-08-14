package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession;

public class GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse {

	private Integer taxonomyId;

	public Integer getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(Integer taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
}
