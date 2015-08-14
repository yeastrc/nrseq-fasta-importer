package org.yeastrc.nrseq_fasta_importer.intermediate_file.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.yeastrc.nrseq_fasta_importer.constants.IntermediateImportFileConstants;

@XmlRootElement(name=IntermediateImportFileConstants.ENTRY_ELEMENT_NAME)
public class IntermediateFileEntry {

	private int headerLineNumber;
	private String sequence;
	private List<IntermediateFileHeaderEntry> importFileHeaderEntryList;
	
	public int getHeaderLineNumber() {
		return headerLineNumber;
	}
	public void setHeaderLineNumber(int headerLineNumber) {
		this.headerLineNumber = headerLineNumber;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public List<IntermediateFileHeaderEntry> getImportFileHeaderEntryList() {
		return importFileHeaderEntryList;
	}
	public void setImportFileHeaderEntryList(
			List<IntermediateFileHeaderEntry> importFileHeaderEntryList) {
		this.importFileHeaderEntryList = importFileHeaderEntryList;
	}
}
