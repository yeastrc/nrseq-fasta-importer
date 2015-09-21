package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;




/**
 * 
 *
 */
public class TaxonomyIdForReverseRandomAndScrambled implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdForReverseRandomAndScrambled.class);
	
	
	private TaxonomyIdForReverseRandomAndScrambled() { }
	public static TaxonomyIdForReverseRandomAndScrambled getInstance() { 
		return new TaxonomyIdForReverseRandomAndScrambled(); 
	}
	
	
	

	/**
	 * @param header
	 * @param fastaEntry
	 * @return
	 * @throws Exception
	 */
	@Override
	public DetermineTaxonomyIdResult getTaxonomyId( DetermineTaxonomyIdParams determineTaxonomyIdParams ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = new DetermineTaxonomyIdResult();
		
		
		Integer taxonomyId = null;
		
		
		String headerName = determineTaxonomyIdParams.getHeaderName();

		if ( headerName == null ) {
			
			
			
		} else {
			
			// If this is a reversed sequence, return 0 (no species)
			if ( headerName.startsWith( "Reverse_" ) || headerName.startsWith("rev_") ) {
				
				taxonomyId = 0;
			}

			// If this is a randomized sequence, return 0
			else if ( headerName.startsWith( "random_seq_" ) 
					|| headerName.startsWith( "rand_" )
					
					// If this is a scrambled sequence, return 0
					|| headerName.startsWith( "Scramble_" )
					) {
				
				taxonomyId = 0;
			}
			
			// if this is a decoy sequence, return 0
			else if( headerName.startsWith( "DECOY_" ) )
				taxonomyId = 0;
		}
		
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
