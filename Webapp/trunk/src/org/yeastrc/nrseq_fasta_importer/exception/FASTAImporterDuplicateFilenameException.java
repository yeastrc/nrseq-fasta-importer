package org.yeastrc.nrseq_fasta_importer.exception;

/**
 * Duplicate Filename encountered on database insert
 *
 */
public class DuplicateFilenameException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateFilenameException() {
		
		super();
	}
	
	public DuplicateFilenameException( String message ) {
		
		super(message);
	}

}
