package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table general_import_error
 *
 */
public class GeneralImportErrorDTO {

	private int id;
	private int fastaImportTrackingId;
	private String message;
	
	
	
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

//CREATE TABLE general_import_error (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  message VARCHAR(3000) NULL,
	