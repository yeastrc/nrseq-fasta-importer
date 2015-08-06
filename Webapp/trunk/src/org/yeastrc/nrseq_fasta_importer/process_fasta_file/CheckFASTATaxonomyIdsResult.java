package org.yeastrc.nrseq_fasta_importer.process_fasta_file;

public class CheckFASTATaxonomyIdsResult {

	private boolean headerMatchesNotFound;

	public boolean isHeaderMatchesNotFound() {
		return headerMatchesNotFound;
	}

	public void setHeaderMatchesNotFound(boolean headerMatchesNotFound) {
		this.headerMatchesNotFound = headerMatchesNotFound;
	}
}
