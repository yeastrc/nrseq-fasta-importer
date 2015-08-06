package org.yeastrc.nrseq_fasta_importer.constants;

public class ImportStatusContants {

	public static final String CORE_QUEUED_FOR = "queued for ";
	public static final String CORE_STARTED = " started";
	public static final String CORE_COMPLETE = " complete";
	public static final String CORE_FAILED = " failed";
	
	
	public static final String VALIDATION_CORE = "validation";
	public static final String STATUS_QUEUED_FOR_VALIDATION = CORE_QUEUED_FOR + VALIDATION_CORE;
	public static final String STATUS_VALIDATION_STARTED = VALIDATION_CORE + CORE_STARTED;
//	public static final String STATUS_VALIDATION_COMPLETE = VALIDATION_CORE + CORE_COMPLETE;
	public static final String STATUS_VALIDATION_FAILED = VALIDATION_CORE + CORE_FAILED;

	public static final String FIND_TAX_IDS_CORE = "find taxonomy ids";
	public static final String STATUS_QUEUED_FOR_FIND_TAX_IDS = CORE_QUEUED_FOR + FIND_TAX_IDS_CORE;
	public static final String STATUS_FIND_TAX_IDS_STARTED = FIND_TAX_IDS_CORE + CORE_STARTED;
//	public static final String STATUS_FIND_TAX_IDS_COMPLETE = FIND_TAX_IDS_CORE + CORE_COMPLETE;
	public static final String STATUS_FIND_TAX_IDS_FAILED = FIND_TAX_IDS_CORE + CORE_FAILED;
	
	
	public static final String STATUS_USER_INPUT_REQUIRED = "user input required";
	

	public static final String IMPORT_CORE = "import";
	public static final String STATUS_QUEUED_FOR_IMPORT = CORE_QUEUED_FOR + IMPORT_CORE;
	public static final String STATUS_IMPORT_STARTED = IMPORT_CORE + CORE_STARTED;
	public static final String STATUS_IMPORT_COMPLETE = IMPORT_CORE + CORE_COMPLETE;
	public static final String STATUS_IMPORT_FAILED = IMPORT_CORE + CORE_FAILED;

	
	public static final String STATUS_SYSTEM_ERROR_PROCESSING_FAILED = "system error, processing failed";

}
