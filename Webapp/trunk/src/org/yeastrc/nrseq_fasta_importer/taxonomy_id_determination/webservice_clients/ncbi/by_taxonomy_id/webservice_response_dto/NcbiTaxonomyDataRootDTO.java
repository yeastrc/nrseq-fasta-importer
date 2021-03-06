package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TaxaSet")
public class NcbiTaxonomyDataRootDTO {

	
	private List<NcbiTaxonomyDataTaxonDTO> ncbiTaxonomyDataTaxonDTOList;

	@XmlElement(name="Taxon")
	public List<NcbiTaxonomyDataTaxonDTO> getNcbiTaxonomyDataTaxonDTOList() {
		return ncbiTaxonomyDataTaxonDTOList;
	}

	public void setNcbiTaxonomyDataTaxonDTOList(
			List<NcbiTaxonomyDataTaxonDTO> ncbiTaxonomyDataTaxonDTOList) {
		this.ncbiTaxonomyDataTaxonDTOList = ncbiTaxonomyDataTaxonDTOList;
	}
}

