package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table fasta_header_no_tax_id_determined
 *
 */
public class FASTAHeaderNoTaxIdDeterminedDTO {

	private int id;
	private int fastaImportTrackingId;
	private int getTaxonomyIdsPassNumber;
	private String headerName;
	private String headerDescription;
	private String headerLine;
	private int headerLineNumber;
	private String message;
	private Integer userAssignedTaxId;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFastaImportTrackingId() {
		return fastaImportTrackingId;
	}
	public void setFastaImportTrackingId(int fastaImportTrackingId) {
		this.fastaImportTrackingId = fastaImportTrackingId;
	}
	public int getGetTaxonomyIdsPassNumber() {
		return getTaxonomyIdsPassNumber;
	}
	public void setGetTaxonomyIdsPassNumber(int getTaxonomyIdsPassNumber) {
		this.getTaxonomyIdsPassNumber = getTaxonomyIdsPassNumber;
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
	public String getHeaderLine() {
		return headerLine;
	}
	public void setHeaderLine(String headerLine) {
		this.headerLine = headerLine;
	}
	public int getHeaderLineNumber() {
		return headerLineNumber;
	}
	public void setHeaderLineNumber(int headerLineNumber) {
		this.headerLineNumber = headerLineNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getUserAssignedTaxId() {
		return userAssignedTaxId;
	}
	public void setUserAssignedTaxId(Integer userAssignedTaxId) {
		this.userAssignedTaxId = userAssignedTaxId;
	}
	


}

//	  CREATE TABLE fasta_header_no_tax_id_determined (
//
//	  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//	  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//	  get_taxonomy_ids_pass_number INT NOT NULL,
//	  header_name VARCHAR(600) NOT NULL,
//	  header_description VARCHAR(3000) NULL,
//	  header_line VARCHAR(6000) NULL,
//	  header_line_number INT NULL,
//	  message VARCHAR(3000) NULL,
//	  user_assigned_tax_id INT NULL,
	