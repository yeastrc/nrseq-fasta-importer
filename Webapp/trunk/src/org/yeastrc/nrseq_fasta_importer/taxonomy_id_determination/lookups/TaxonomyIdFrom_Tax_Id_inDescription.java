package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;




/**
 * 
 *
 */
public class TaxonomyIdFrom_Tax_Id_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_Tax_Id_inDescription.class);


//	private Pattern taxidPattern = Pattern.compile( "^.+\\s+Tax_Id=(\\d+)\\s+.+$" );
//
//	private Pattern taxonomy_id_Pattern = Pattern.compile( "^.+\\s+Taxonomy_Id=(\\d+)\\s+.+$" );

	

	private Pattern[]  taxIdPatterns = {
			
			//  Tax_Id=
			
			// MiddleOfLine
			Pattern.compile( "^.*\\s+Tax_Id=(\\d+)\\s+.*$" ),
			// StartOfLine = 
			Pattern.compile( "^Tax_Id=(\\d+)\\s+.*$" ),
			// EndOfLine = 
			Pattern.compile( "^.*\\s+Tax_Id=(\\d+)$" ),
			// AllOfLine = 
			Pattern.compile( "^Tax_Id=(\\d+)$" ),

			//  Taxonomy_Id=
			
			// MiddleOfLine = 
			Pattern.compile( "^.*\\s+Taxonomy_Id=(\\d+)\\s+.*$" ),
			// StartOfLine = 
			Pattern.compile( "^Taxonomy_Id=(\\d+)\\s+.*$" ),
			// EndOfLine = 
			Pattern.compile( "^.*\\s+Taxonomy_Id=(\\d+)$" ),
			// AllOfLine = 
			Pattern.compile( "^Taxonomy_Id=(\\d+)$" )
	};

	
	private TaxonomyIdFrom_Tax_Id_inDescription() { }
	public static TaxonomyIdFrom_Tax_Id_inDescription getInstance() { 
		return new TaxonomyIdFrom_Tax_Id_inDescription(); 
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
		
		
//		String headerName = fastaHeaderImporterCopy.getName();
		String headerDescription = determineTaxonomyIdParams.getHeaderDescription();

		if ( headerDescription == null ) {
			
			
			
		} else {
			
			for ( int index = 0; index < taxIdPatterns.length; index++ ) {
				
				Pattern taxIdPattern = taxIdPatterns[ index ];

				Matcher gm = null;

				// Check to see if they have a Tax_Id=# as part of their desc.  If so, use that #
				gm = taxIdPattern.matcher( headerDescription );
				if (gm.matches()) {

					String taxIdString = gm.group( 1 );

					try {

						taxonomyId = Integer.parseInt( taxIdString );

					} catch ( Exception e ) {

						//  Never get this exception since the regex pattern only applies to digits after the "="

						String taxIdLabel = "Tax_Id";
						
						if ( index > 3 ) {
							
							taxIdLabel = "Taxonomy_Id";
						}
						
						String msg = "FASTA header that contains '" + taxIdLabel 
								+ "=' does not have an integer for the value."
								+ "  " + taxIdLabel + " value  (delim '|'): |" + taxIdString + "|.";

						//				log.error( msg );

						throw new FASTAImporterDataErrorException( msg );
					}

					break;  //  EARLY EXIT loop
				}
			}
			
			
		}
				
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
