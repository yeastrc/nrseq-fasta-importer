package org.yeastrc.nrseq_fasta_importer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

public class SHA1SumCalculator {

	private static final Logger log = Logger.getLogger(SHA1SumCalculator.class);
	
	private SHA1SumCalculator() { }
	public static SHA1SumCalculator getInstance() { 
		return new SHA1SumCalculator(); 
	}
	
	public String getSHA1Sum( File f ) throws Exception {
		
		if ( log.isInfoEnabled() ) {
			log.info( "Computing SHA1 Sum for file: " + f.getAbsolutePath() );
		}
		
		String result = null;
		
		
		FileInputStream fis = null;
		
		try {

			MessageDigest md = MessageDigest.getInstance("SHA1");
			fis = new FileInputStream( f );
			byte[] dataBytes = new byte[1024];

			int nread = 0; 

			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			};

			byte[] mdbytes = md.digest();

			//convert the byte to hex format
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			result = sb.toString();
			
		} finally {
			
			if ( fis != null ) {
				
				fis.close();
			}
		}
		
		if ( log.isInfoEnabled() ) {
			log.info( "SHA1 Sum for file: " + f.getAbsolutePath() + " is: " + result );
		}
		
		
	    return result;
	}
	
}
