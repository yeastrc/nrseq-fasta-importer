package org.yeastrc.nrseq_fasta_importer.exception;

/**
 * Duplicate Filename encountered on database insert
 *
 */
public class FASTAImporterDuplicateFilenameException extends Exception {

	private static final long serialVersionUID = 1L;

	public FASTAImporterDuplicateFilenameException() {
		
		super();
	}
	
	public FASTAImporterDuplicateFilenameException( String message ) {
		
		super(message);
	}

}
