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
public class TaxonomyIdFrom_Flybase_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_Flybase_inDescription.class);

	
	private TaxonomyIdFrom_Flybase_inDescription() { }
	public static TaxonomyIdFrom_Flybase_inDescription getInstance() { 
		return new TaxonomyIdFrom_Flybase_inDescription(); 
	}
	


	// Example FBpp0070640
	Pattern flyBasePattern = Pattern.compile("^FBpp\\d{7}+$");
	
	
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

			// Check if this is a FlyBase id
			Matcher fb = this.flyBasePattern.matcher(headerName);
			if(fb.matches()) {
				taxonomyId = 7227;
			}

		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
