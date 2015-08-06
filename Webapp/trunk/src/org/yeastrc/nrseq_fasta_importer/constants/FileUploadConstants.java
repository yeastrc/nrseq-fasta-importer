package org.yeastrc.nrseq_fasta_importer.constants;

import java.text.NumberFormat;

public class FileUploadConstants {


	 public static final int MAX_FILE_UPLOAD_SIZE = ( 2 * 1000 * 1000 * 1000 ); // 2GB max

//	public static final int MAX_FILE_UPLOAD_SIZE = ( 2 * 10 * 1000  ); // temp smaller max of 20KB

	
	public static final String MAX_FILE_UPLOAD_SIZE_FORMATTED = NumberFormat.getInstance().format(MAX_FILE_UPLOAD_SIZE);
	
	
	public static int get_MAX_FILE_UPLOAD_SIZE() {
		
		return MAX_FILE_UPLOAD_SIZE;
	}
	
	
	public static String get_MAX_FILE_UPLOAD_SIZE_FORMATTED() {
		
		return MAX_FILE_UPLOAD_SIZE_FORMATTED;
	}
	
	
	public static final String UPLOAD_FASTA_FILE_FIELD_NAME = "uploadFASTAFile";
}
