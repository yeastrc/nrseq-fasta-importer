package org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto;

public class ImportFileHeaderEntry {

	private String headerName;
	private String headerDescription;
	private int taxonomyId;
	
	
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getHeaderDescription() {
		return headerDescription;
	}
	public void setHeaderDescription(String headerDescription) {
		this.headerDescription = headerDescription;
	}
	public int getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(int taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
}
