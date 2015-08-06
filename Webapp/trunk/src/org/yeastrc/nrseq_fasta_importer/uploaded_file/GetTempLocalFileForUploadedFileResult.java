package org.yeastrc.nrseq_fasta_importer.uploaded_file;

public class GetTempLocalFileForUploadedFileResult {

	private String tempFilename;
	private String tempFilenameForImport;
	
	
	public String getTempFilename() {
		return tempFilename;
	}
	public void setTempFilename(String tempFilename) {
		this.tempFilename = tempFilename;
	}
	public String getTempFilenameForImport() {
		return tempFilenameForImport;
	}
	public void setTempFilenameForImport(String tempFilenameForImport) {
		this.tempFilenameForImport = tempFilenameForImport;
	}
}
