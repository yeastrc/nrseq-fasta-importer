package org.yeastrc.nrseq_fasta_importer.exception;

public class FASTAImporterRemoteWebserviceCallErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	public FASTAImporterRemoteWebserviceCallErrorException() {
		
		super();
	}
	
	public FASTAImporterRemoteWebserviceCallErrorException( String message ) {
		
		super(message);
	}

}
