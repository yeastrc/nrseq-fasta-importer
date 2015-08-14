package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;




/**
 * 
 *
 */
public class TaxonomyIdFrom_Wormbase_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_Wormbase_inDescription.class);

	
	private TaxonomyIdFrom_Wormbase_inDescription() { }
	public static TaxonomyIdFrom_Wormbase_inDescription getInstance() { 
		return new TaxonomyIdFrom_Wormbase_inDescription(); 
	}
	
	Pattern p4 = Pattern.compile("^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(.+)$");

	

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
		
		
//		String headerName = header.getName();
//		String headerDescription = header.getDescription();
		
		String fullHeaderString = determineTaxonomyIdParams.getHeaderFullString();

		if ( fullHeaderString == null ) {
			
			
			
		} else {


			// Check to see if this is an entry from WormBase, if so, returned C Elegans
			Matcher m = this.p4.matcher( fullHeaderString );
			if (m.matches() && m.group( 2 ).startsWith( "CE") && m.group( 3 ).startsWith( "WBGene") ) {
				taxonomyId = 6239;
			}
	        
		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
