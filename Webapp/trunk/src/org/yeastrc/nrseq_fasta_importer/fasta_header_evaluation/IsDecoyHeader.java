package org.yeastrc.nrseq_fasta_importer.fasta_header_evaluation;

import org.apache.log4j.Logger;


/**
 * Is this header a decoy header
 *
 */
public class IsDecoyHeader {

	private static final Logger log = Logger.getLogger(IsDecoyHeader.class);
	
	
	private IsDecoyHeader() { }
	public static IsDecoyHeader getInstance() { 
		return new IsDecoyHeader(); 
	}
	
	
	

	/**
	 * @param headerName
	 * @return
	 * @throws Exception
	 */
	public boolean isDecoyHeader( String headerName ) throws Exception {
		

		if ( headerName == null ) {
			
			
			
		} else {
			
			// If this is a reversed sequence, return 0 (no species)
			if ( headerName.startsWith( "Reverse_" ) || headerName.startsWith("rev_") ) {
				
				return true;
			}

			// If this is a randomized sequence, return 0
			else if ( headerName.startsWith( "random_seq_" ) 
					|| headerName.startsWith( "rand_" )
					
					// If this is a scrambled sequence, return 0
					|| headerName.startsWith( "Scramble_" )
					) {
				
				return true;
			}
			
			// if this is a decoy sequence, return 0
			else if( headerName.startsWith( "DECOY_" ) )
				return true;
		}
		
		return false;
	}
}
