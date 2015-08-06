package org.yeastrc.nrseq_fasta_importer.dto;

/**
 * table YRC_NRSEQ.tblProtein
 *
 */
public class YRC_NRSEQ_tblProteinDTO {

	private int id;
	private int sequenceID;
	private int speciesID;
	

	public int getSequenceID() {
		return sequenceID;
	}
	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}
	public int getSpeciesID() {
		return speciesID;
	}
	public void setSpeciesID(int speciesID) {
		this.speciesID = speciesID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
