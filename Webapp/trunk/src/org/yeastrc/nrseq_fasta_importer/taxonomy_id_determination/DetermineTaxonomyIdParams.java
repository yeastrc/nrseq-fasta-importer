package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;

/**
 * params to classes implementing TaxonomyIdLookupIF
 *
 */
public class DetermineTaxonomyIdParams {
	
	private String headerFullString;
	private String headerName;
	private String headerDescription;
	private int fastaImportTrackingDTOId;
	private int headerLineNumber;
	
	
	public int getHeaderLineNumber() {
		return headerLineNumber;
	}
	public void setHeaderLineNumber(int headerLineNumber) {
		this.headerLineNumber = headerLineNumber;
	}
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
	public int getFastaImportTrackingDTOId() {
		return fastaImportTrackingDTOId;
	}
	public void setFastaImportTrackingDTOId(int fastaImportTrackingDTOId) {
		this.fastaImportTrackingDTOId = fastaImportTrackingDTOId;
	}

}
