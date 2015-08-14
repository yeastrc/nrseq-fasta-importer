package org.yeastrc.nrseq_fasta_importer.dto;

import java.sql.Date;

/**
 * table fasta_import_tracking
 *
 */
public class FASTAImportTrackingDTO {

	private int id;
	private String filename;
	private String description;
	private String email;
	private String status;
	private String insertRequestURL;
	private String sha1sum;
	private int tempFilenameNumber;
	private int fastaEntryCount;
	private int getTaxonomyIdsPassNumber;
	private Integer yrc_nrseq_tblDatabase_id;
	
	private Date uploadDateTime;
	private Date lastUpdatedDateTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInsertRequestURL() {
		return insertRequestURL;
	}
	public void setInsertRequestURL(String insertRequestURL) {
		this.insertRequestURL = insertRequestURL;
	}
	public String getSha1sum() {
		return sha1sum;
	}
	public void setSha1sum(String sha1sum) {
		this.sha1sum = sha1sum;
	}
	public int getTempFilenameNumber() {
		return tempFilenameNumber;
	}
	public void setTempFilenameNumber(int tempFilenameNumber) {
		this.tempFilenameNumber = tempFilenameNumber;
	}
	public int getFastaEntryCount() {
		return fastaEntryCount;
	}
	public void setFastaEntryCount(int fastaEntryCount) {
		this.fastaEntryCount = fastaEntryCount;
	}
	public int getGetTaxonomyIdsPassNumber() {
		return getTaxonomyIdsPassNumber;
	}
	public void setGetTaxonomyIdsPassNumber(int getTaxonomyIdsPassNumber) {
		this.getTaxonomyIdsPassNumber = getTaxonomyIdsPassNumber;
	}
	public Integer getYrc_nrseq_tblDatabase_id() {
		return yrc_nrseq_tblDatabase_id;
	}
	public void setYrc_nrseq_tblDatabase_id(Integer yrc_nrseq_tblDatabase_id) {
		this.yrc_nrseq_tblDatabase_id = yrc_nrseq_tblDatabase_id;
	}
	public Date getUploadDateTime() {
		return uploadDateTime;
	}
	public void setUploadDateTime(Date uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}
	public Date getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	
	
}


//CREATE TABLE fasta_import_tracking (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  filename VARCHAR(512) NOT NULL,
//		  description VARCHAR(500) NULL,
//		  email VARCHAR(255) NULL,
//		  status VARCHAR(45) NOT NULL,
//		  insert_request_url VARCHAR(255) NULL DEFAULT NULL
//		  sha1sum VARCHAR(45) NOT NULL,
//		  temp_filename_number INT NOT NULL,
//		  fasta_entry_count INT NULL,
//		  get_taxonomy_ids_pass_number INT NOT NULL DEFAULT 0,
//		  yrc_nrseq_tblDatabase_id INT NULL,
//		  upload_date_time TIMESTAMP NOT NULL,
//		  last_updated_date_time TIMESTAMP NULL,
		  