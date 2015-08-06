package org.yeastrc.nrseq_fasta_importer.dto;

import java.util.Date;

/**
 * table YRC_NRSEQ.tblProteinDatabase
 *
 */
public class YRC_NRSEQ_tblProteinDatabaseDTO {

	private int id;
	private int proteinID;
	private int databaseID;
	private String accessionString;
	private String description;
	private String url;
	private Date timestamp;
	private String isCurrent;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProteinID() {
		return proteinID;
	}
	public void setProteinID(int proteinID) {
		this.proteinID = proteinID;
	}
	public int getDatabaseID() {
		return databaseID;
	}
	public void setDatabaseID(int databaseID) {
		this.databaseID = databaseID;
	}
	public String getAccessionString() {
		return accessionString;
	}
	public void setAccessionString(String accessionString) {
		this.accessionString = accessionString;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}


}


//	CREATE TABLE tblProteinDatabase (
//			  id int(10) unsigned NOT NULL AUTO_INCREMENT,
//			  proteinID int(10) unsigned NOT NULL DEFAULT '0',
//			  databaseID int(10) unsigned NOT NULL DEFAULT '0',
//			  accessionString varchar(500) NOT NULL,
//			  description varchar(2500) DEFAULT NULL,
//			  URL varchar(255) DEFAULT NULL,
//			  timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//			  isCurrent enum('T','F') NOT NULL DEFAULT 'T',

