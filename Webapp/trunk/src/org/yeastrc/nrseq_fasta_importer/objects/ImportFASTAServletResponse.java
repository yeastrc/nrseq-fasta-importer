package org.yeastrc.nrseq_fasta_importer.objects;


/**
 * Response from UploadFASTAFileServlet, serialized to JSON
 *
 */
public class ImportFASTAServletResponse {

	private boolean statusSuccess;
	private Integer id; // assigned id for import, null if upload failed
	
	//  These are populated for FileSizeLimitExceededException exception
	private boolean fileSizeLimitExceeded;
	private long maxSize;
	private String maxSizeFormatted;
	
	private boolean uploadFile_fieldNameInvalid;
	private boolean moreThanOneuploadedFile;
	private boolean filenameAlreadyInDB;
	private boolean filenameInFormNotMatchFilenameInQueryString;
	private boolean noUploadedFile;
	
	public boolean isNoUploadedFile() {
		return noUploadedFile;
	}
	public void setNoUploadedFile(boolean noUploadedFile) {
		this.noUploadedFile = noUploadedFile;
	}
	public boolean isFilenameInFormNotMatchFilenameInQueryString() {
		return filenameInFormNotMatchFilenameInQueryString;
	}
	public void setFilenameInFormNotMatchFilenameInQueryString(
			boolean filenameInFormNotMatchFilenameInQueryString) {
		this.filenameInFormNotMatchFilenameInQueryString = filenameInFormNotMatchFilenameInQueryString;
	}
	public boolean isFilenameAlreadyInDB() {
		return filenameAlreadyInDB;
	}
	public void setFilenameAlreadyInDB(boolean filenameAlreadyInDB) {
		this.filenameAlreadyInDB = filenameAlreadyInDB;
	}
	public boolean isMoreThanOneuploadedFile() {
		return moreThanOneuploadedFile;
	}
	public void setMoreThanOneuploadedFile(boolean moreThanOneuploadedFile) {
		this.moreThanOneuploadedFile = moreThanOneuploadedFile;
	}
	public boolean isUploadFile_fieldNameInvalid() {
		return uploadFile_fieldNameInvalid;
	}
	public void setUploadFile_fieldNameInvalid(boolean uploadFile_fieldNameInvalid) {
		this.uploadFile_fieldNameInvalid = uploadFile_fieldNameInvalid;
	}
	public boolean isStatusSuccess() {
		return statusSuccess;
	}
	public void setStatusSuccess(boolean statusSuccess) {
		this.statusSuccess = statusSuccess;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isFileSizeLimitExceeded() {
		return fileSizeLimitExceeded;
	}
	public void setFileSizeLimitExceeded(boolean fileSizeLimitExceeded) {
		this.fileSizeLimitExceeded = fileSizeLimitExceeded;
	}
	public long getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	public String getMaxSizeFormatted() {
		return maxSizeFormatted;
	}
	public void setMaxSizeFormatted(String maxSizeFormatted) {
		this.maxSizeFormatted = maxSizeFormatted;
	}
	
	
}
