package org.yeastrc.nrseq_fasta_importer.dto;

/**
 * table YRC_NRSEQ.tblProteinSequence
 *
 */
public class YRC_NRSEQ_tblProteinSequenceDTO {
	
	private int id;
	private String sequence;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
