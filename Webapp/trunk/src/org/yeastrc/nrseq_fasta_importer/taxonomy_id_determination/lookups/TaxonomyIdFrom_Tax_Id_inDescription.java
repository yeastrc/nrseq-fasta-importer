package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;




/**
 * 
 *
 */
public class TaxonomyIdFrom_Tax_Id_inDescription {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_Tax_Id_inDescription.class);
	

//	private static final String HEADER_NAME_PREFIX_TAX_ID = "Tax_Id=";
	

	private Pattern taxidPattern = Pattern.compile( "^.+\\s+Tax_Id=(\\d+)\\s+.+$" );
	
	
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
	public DetermineTaxonomyIdResult getTaxonomyId( FASTAHeader header, FASTAEntry fastaEntry ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = new DetermineTaxonomyIdResult();
		
		
		Integer taxonomyId = null;
		
		
		String headerName = header.getName();
		String headerDescription = header.getDescription();

		if ( headerDescription == null ) {
			
			
			
		} else {

			Matcher gm = null;

			// Check to see if they have a Tax_Id=# as part of their desc.  If so, use that #
			gm = this.taxidPattern.matcher( headerDescription );
			if (gm.matches()) {

				String taxIdString = gm.group( 1 );

				try {

					taxonomyId = Integer.parseInt( taxIdString );

				} catch ( Exception e ) {

					//  Never get this exception since the regex pattern only applies to digits after the "="

					String msg = "FASTA header that contains 'Tax_Id=' does not have an integer for the value."
							+ "  Tax_Id value  (delim '|'): |" + taxIdString + "|.";

					//				log.error( msg );

					throw new FASTAImporterDataErrorException( msg );
				}

			}
		}
		
		//  Set fake taxonomy id, for testing only
//		if ( taxonomyId == null ) {
//			
//			taxonomyId = 99999;
//		}
		
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}
