package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.fasta_header_evaluation.IsDecoyHeader;
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
		
		if ( IsDecoyHeader.getInstance().isDecoyHeader( headerName ) ) {
			
			taxonomyId = 0;
		}
		
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
