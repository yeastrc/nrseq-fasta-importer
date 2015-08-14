package org.yeastrc.nrseq_fasta_importer.objects;


/**
 * Importer copy to allow modification
 *
 */
public class FASTAHeaderImporterCopy {
	
	private String name;
	private String description;
	private String line;
//	private FASTAHeader originalFASTAHeader;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
}
