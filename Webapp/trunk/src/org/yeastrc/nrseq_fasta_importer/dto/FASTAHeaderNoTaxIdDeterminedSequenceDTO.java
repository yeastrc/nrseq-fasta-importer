package org.yeastrc.nrseq_fasta_importer.dto;


/**
 * table fasta_header_no_tax_id_determined_sequence
 *
 */
public class FASTAHeaderNoTaxIdDeterminedSequenceDTO {

	private int fastaHeaderNoTaxIdDeterminedId;
	private String sequence;
	
	
	@Override
	public String toString() {
		return "FASTAHeaderNoTaxIdDeterminedSequenceDTO [fastaHeaderNoTaxIdDeterminedId="
				+ fastaHeaderNoTaxIdDeterminedId
				+ ", sequence="
				+ sequence
				+ "]";
	}
	
	
	public int getFastaHeaderNoTaxIdDeterminedId() {
		return fastaHeaderNoTaxIdDeterminedId;
	}
	public void setFastaHeaderNoTaxIdDeterminedId(int fastaHeaderNoTaxIdDeterminedId) {
		this.fastaHeaderNoTaxIdDeterminedId = fastaHeaderNoTaxIdDeterminedId;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}



}

//CREATE TABLE fasta_header_no_tax_id_determined_sequence (
//		  fasta_header_no_tax_id_determined_id INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,