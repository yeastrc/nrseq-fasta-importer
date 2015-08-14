package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table tmp_fasta_sequence
 * 
 * While validating the FASTA file, this table is inserted into
 *
 */
public class Tmp_FASTA_sequence_DTO {
	
	private int id;
	private int fastaImportTrackingId;
	private int headerLineNumber;
	private String sequence;
	
	
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
	public int getHeaderLineNumber() {
		return headerLineNumber;
	}
	public void setHeaderLineNumber(int headerLineNumber) {
		this.headerLineNumber = headerLineNumber;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}

//CREATE TABLE  nrseq_fasta_importer.tmp_fasta_sequence (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  header_line_number INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,
