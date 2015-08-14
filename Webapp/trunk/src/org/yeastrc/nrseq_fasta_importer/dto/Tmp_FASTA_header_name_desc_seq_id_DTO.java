package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table tmp_fasta_header_name_desc_seq_id
 * 
 * While validating the FASTA file, this table is inserted into
 *
 */
public class Tmp_FASTA_header_name_desc_seq_id_DTO {
	
	private int id;
	private int fastaImportTrackingId;
	private int headerLineNumber;
	private int tmpSequenceId;
	private int headerNameHashCode;
	private boolean headerNameHashCodeSet;
	private String headerName;
	private String headerDescription;

	public int getHeaderNameHashCode() {
		
		if ( ! headerNameHashCodeSet ) {
			
			headerNameHashCodeSet = true;
			if ( headerName == null ) {
				
				throw new IllegalStateException( "Cannot call getHeaderNameHashCode() when headerName == null" );
			}

			headerNameHashCode = headerName.hashCode();
		}
		
		return headerNameHashCode;
	}
	public void setHeaderNameHashCode(int headerNameHashCode) {
		this.headerNameHashCode = headerNameHashCode;
	}

	
	
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
	public int getTmpSequenceId() {
		return tmpSequenceId;
	}
	public void setTmpSequenceId(int tmpSequenceId) {
		this.tmpSequenceId = tmpSequenceId;
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
	
}

//CREATE TABLE tmp_fasta_header_name_desc_seq_id (
//			id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			fasta_import_tracking_id INT UNSIGNED NOT NULL,
//			header_line_number INT UNSIGNED NOT NULL,
//			tmp_fasta_sequence_id_fk INT NOT NULL,
//			header_name_hash_code INT NOT NULL,
//			header_name VARCHAR(1000) NOT NULL,
//			header_description VARCHAR(3000) NULL,
