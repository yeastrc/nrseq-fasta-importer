package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_services.GetNCBITaxonomyDataFromNCBI_ProteinDB_Service;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_services.GetNCBITaxonomyDataFromNCBI_ProteinDB_ServiceResponse;




/**
 * 
 *
 */
public class TaxonomyIdFrom_gi_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_gi_inDescription.class);
	
	Pattern giPattern = Pattern.compile( "^(gi\\|\\d+).*$" );


	
	
	private TaxonomyIdFrom_gi_inDescription() { }
	public static TaxonomyIdFrom_gi_inDescription getInstance() { 
		return new TaxonomyIdFrom_gi_inDescription(); 
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
		

		int id = 0;
        
		Matcher gm = null;

		
        
        
        // if this header starts with a gi#, check the local database for that specific gi (will hit NR and NCBI)
        // if this header starts with a gi#, check the local database for that specific gi (will hit NR and NCBI)
        gm = this.giPattern.matcher( headerName );
        if (gm.matches()) {
            
            /*
            id = NR_SEQAccSearcher.getInstance().getSpecies( gm.group( 1 ) );
            //System.out.println( "##FOUND GI NUMBER:\t" + gm.group( 1 ) );
            
            if (id != 0) return id;
            */
            
            // try searching the gi2taxonomy table in the NCBI database for this gi number
            String[] gifields = gm.group( 1 ).split( "\\|" );
            
            if ( gifields.length == 2 ) {
            	
            	int giFieldsPart2 = 0;
            	
            	try {
            		
            		giFieldsPart2 = Integer.parseInt( gifields[ 1 ] );


				} catch ( Exception e ) {

					String msg = "FASTA header that contains 'gi\\|\\d+' does not have an integer for the value."
							+ "  gi\\|\\d+ second part value  (delim '`'): |" + gifields[ 1 ] + "`.";

					//				log.error( msg );

					throw new FASTAImporterDataErrorException( msg );
				}
            	
            	
            	
//                id = NCBITaxonomySearcher.getInstance().getSpeciesByGi( giFieldsPart2 );
            	
            	int giAccessionNumber = giFieldsPart2;
            	
            	GetNCBITaxonomyDataFromNCBI_ProteinDB_ServiceResponse response = 
            			GetNCBITaxonomyDataFromNCBI_ProteinDB_Service.getInstance().getNCBITaxonomyDataFromNCBIProteinService( giAccessionNumber );
            	
            	if ( response.getTaxonomyId() != null ) {
            		
            		id = response.getTaxonomyId();
            	}
            	
                //System.out.println( "###CHECKING GI NUMBER IN gi2taxonomy:\t" + gifields[1] );
            }
            
            if ( id != 0 ) return id;           
        }
		
		
		return 0;

	}

}
