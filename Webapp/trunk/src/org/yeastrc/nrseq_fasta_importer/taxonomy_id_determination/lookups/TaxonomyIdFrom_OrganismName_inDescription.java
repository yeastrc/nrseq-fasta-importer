package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.searchers.NCBITaxonomySearcher;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;




/**
 * 
 *
 */
public class TaxonomyIdFrom_OrganismName_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_OrganismName_inDescription.class);
	

	// looking for this form:  organism=Plasmodium_falciparum_3D7 
	Pattern bracketSpeciesPattern = Pattern.compile( "\\borganism=(\\S+)\\b" );
	
	private TaxonomyIdFrom_OrganismName_inDescription() { }
	public static TaxonomyIdFrom_OrganismName_inDescription getInstance() { 
		return new TaxonomyIdFrom_OrganismName_inDescription(); 
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

			Matcher gm = null;

			// Check to see if they have header ending with "[species name]" (bracket species pattern).  
			//                  If so, use that name
			gm = this.bracketSpeciesPattern.matcher( fullHeaderString );
			if (gm.find()) {

				
				String speciesName = gm.group( 1 );
				speciesName = speciesName.replaceAll( "_",  " " );

				try {


					// try the common names first
					if (speciesName.equals( "Escherichia coli K12")) taxonomyId = 83333;
					else if (speciesName.equals( "Escherichia coli" )) taxonomyId = 562;
					else if (speciesName.equals( "Arabidopsis thaliana" )) taxonomyId = 3702;
					else if (speciesName.equals( "Bos taurus" )) taxonomyId = 9913;
					else if (speciesName.equals( "Caenorhabditis elegans" )) taxonomyId = 6239;
					else if (speciesName.equals( "Chlamydomonas reinhardtii" )) taxonomyId = 3055;
					else if (speciesName.equals( "Danio rerio" )) taxonomyId = 7955;
					else if (speciesName.equals( "Dictyostelium discoideum" )) taxonomyId = 44689;
					else if (speciesName.equals( "Drosophila melanogaster" )) taxonomyId = 7227;
					else if (speciesName.equals( "Hepatitis C virus" )) taxonomyId = 11103;
					else if (speciesName.equals( "Homo sapiens" )) taxonomyId = 9606;
					else if (speciesName.equals( "Mus musculus" )) taxonomyId = 10090;
					else if (speciesName.equals( "Mycoplasma pneumoniae" )) taxonomyId = 2104;
					else if (speciesName.equals( "Oryza sativa" )) taxonomyId = 4530;
					else if (speciesName.equals( "Plasmodium falciparum" )) taxonomyId = 5833;
					else if (speciesName.equals( "Pneumocystis carinii" )) taxonomyId = 4754;
					else if (speciesName.equals( "Rattus norvegicus" )) taxonomyId = 10116;
					else if (speciesName.equals( "Saccharomyces cerevisiae" )) taxonomyId = 4932;
					else if (speciesName.equals( "Schizosaccharomyces pombe" )) taxonomyId = 4896;
					else if (speciesName.equals( "Takifugu rubripes" )) taxonomyId = 31033;
					else if (speciesName.equals( "Xenopus laevis" )) taxonomyId = 8355;
					else if (speciesName.equals( "Zea mays" )) taxonomyId = 4577;


					else {
						// having failed the common names, look it up in the database
						int id = NCBITaxonomySearcher.getInstance().getSpeciesByName( speciesName );
						if (id != 0) {
							taxonomyId = id;
						}
					}

				} catch ( Exception e ) {

					//  Never get this exception since the regex pattern only applies to digits after the "="

					String msg = "Error processing FASTA header that contains organism=some_string."
							+ "  species name value  (delim '|'): |" + speciesName + "|.";

					//				log.error( msg );
					
					log.error( msg, e );

					throw e;
				}

			}
		}
		
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}

}