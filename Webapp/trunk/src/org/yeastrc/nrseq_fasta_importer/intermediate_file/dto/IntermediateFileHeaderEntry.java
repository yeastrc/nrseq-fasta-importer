package org.yeastrc.nrseq_fasta_importer.intermediate_file.dto;

public class IntermediateFileHeaderEntry {

	private String headerFullString;
	private String headerName;
	private String headerDescription;
	private int taxonomyId;
	
	
	public String getHeaderFullString() {
		return headerFullString;
	}
	public void setHeaderFullString(String headerFullString) {
		this.headerFullString = headerFullString;
	}
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
