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
public class TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription.class);
	
	Pattern keratinp = Pattern.compile( "^\\S*KERATIN\\d+$" );

	Pattern ubiquitinp = Pattern.compile( "^\\S*UBIQUITIN\\d+$" );

	
	private TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription() { }
	public static TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription getInstance() { 
		return new TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription(); 
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

			Matcher gm = null;
			
			// Check for general human contaminants KERATIN and UBIQUITIN
			gm = this.keratinp.matcher( headerName );
			if (gm.matches()) {
				taxonomyId =  9606;
			}
			gm = this.ubiquitinp.matcher( headerName );
			if (gm.matches()) {
				taxonomyId =  9606;
			}
			
		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
