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
public class TaxonomyIdFrom_SGD_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_SGD_inDescription.class);

	// Example: >YAL001C TFC3 SGDID:S000000001, Chr I from 151006-147594,151166-151097, reverse complement, Verified ORF
	private static final Pattern yeastSGDPattern = Pattern.compile("^.*SGDID:S\\d{9}.*$");
	

	
	
	private TaxonomyIdFrom_SGD_inDescription() { }
	public static TaxonomyIdFrom_SGD_inDescription getInstance() { 
		return new TaxonomyIdFrom_SGD_inDescription(); 
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
		
		
		String fullHeaderString = determineTaxonomyIdParams.getHeaderFullString();

		if ( fullHeaderString == null ) {
			
			
			
		} else {

			// check to see if it's an SGD FASTA entry, if so use 4932
			Matcher m = yeastSGDPattern.matcher( fullHeaderString );
	        if(m.matches())  
	        	taxonomyId = 4932;

	        
		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
