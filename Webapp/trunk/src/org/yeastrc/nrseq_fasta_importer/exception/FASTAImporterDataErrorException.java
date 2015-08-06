package org.yeastrc.nrseq_fasta_importer.exception;

public class FASTAImporterDataErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	public FASTAImporterDataErrorException() {
		super();
	}
	
	public FASTAImporterDataErrorException(String msg) {
		super(msg);
	}
}
