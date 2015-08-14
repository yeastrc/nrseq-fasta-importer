package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_services.GetNCBITaxonomyDataFor_SwissProt_Service;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_services.GetNCBITaxonomyDataFor_SwissProt_ServiceResponse;




/**
 * 
 *
 */
public class TaxonomyIdFrom_SwissProt_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_SwissProt_inDescription.class);
	

	// find patterns matching swiss-prot IDs (e.g. B6UN75_DROME) in accession strings of FASTA header lines
	Pattern newSwissprotPattern = Pattern.compile( "\\W*([A-Z0-9]+)_([A-Z0-9]+)\\W*" );	
	
	
	
	private TaxonomyIdFrom_SwissProt_inDescription() { }
	public static TaxonomyIdFrom_SwissProt_inDescription getInstance() { 
		return new TaxonomyIdFrom_SwissProt_inDescription(); 
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
//		String headerDescription = header.getDescription();

		if ( headerName == null ) {
			
			
			
		} else {

			int taxonomyIdLocal = getTaxonomyIdLocal( headerName );
			
			if ( taxonomyIdLocal != 0 ) {
				
				taxonomyId = taxonomyIdLocal;
			}
			
		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}
	
	

	/**
	 * @param headerName
	 * @return
	 * @throws Exception 
	 */
	private int getTaxonomyIdLocal( String headerName ) throws Exception {
		
		Matcher gm = null;
		gm = this.newSwissprotPattern.matcher( headerName );
		if( gm.find() ) {

			String accessionPart1 = gm.group( 1 );
			String accessionPart2 = gm.group( 2 );

			GetNCBITaxonomyDataFor_SwissProt_ServiceResponse response = 
					GetNCBITaxonomyDataFor_SwissProt_Service.getInstance().
						getNCBITaxonomyDataFrom_Uniprot_Service( accessionPart1, accessionPart2 );
			
    		Integer taxonomyId = response.getTaxonomyId();
    		if( taxonomyId != null ) 
    			return taxonomyId;
		}
		
		return 0;

	}

}
