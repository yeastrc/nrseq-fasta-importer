package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;


import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.objects.FASTAHeaderImporterCopy;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdForReverseRandomAndScrambled;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFromUser;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_BracketSpecies_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_Flybase_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_SGD_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_SwissProt_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_Tax_Id_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_UniProt_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_UniprotSpeciesString_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_Wormbase_inDescription;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups.TaxonomyIdFrom_gi_inDescription;




/**
 * 
 * Main Determine Tax Id for header processing
 */
public class DetermineTaxonomyId {

	private static final Logger log = Logger.getLogger(DetermineTaxonomyId.class);
	
	private DetermineTaxonomyId() { }
	public static DetermineTaxonomyId getInstance() { 
		return new DetermineTaxonomyId(); 
	}
	
	
	/**
	 * Array of taxonomy id searches
	 */
	private static TaxonomyIdLookupIF[] taxonomyLookupArray = {
		
		TaxonomyIdFromUser.getInstance(),
		
		
		TaxonomyIdForReverseRandomAndScrambled.getInstance(),
		
		TaxonomyIdFrom_Tax_Id_inDescription.getInstance(),
		
		TaxonomyIdFrom_BracketSpecies_inDescription.getInstance(), 
		
		TaxonomyIdFrom_UniprotSpeciesString_inDescription.getInstance(),
		
		TaxonomyIdFrom_KeratinUbiquitinContaminants_inDescription.getInstance(),
		
		TaxonomyIdFrom_gi_inDescription.getInstance(), 
		
		
		TaxonomyIdFrom_SwissProt_inDescription.getInstance(), 
		
		
		TaxonomyIdFrom_UniProt_inDescription.getInstance(),
		
		TaxonomyIdFrom_SGD_inDescription.getInstance(),
		TaxonomyIdFrom_Wormbase_inDescription.getInstance(),
		
		TaxonomyIdFrom_Flybase_inDescription.getInstance(),
		
	};
	
	


	/**
	 * @param fastaHeaderImporterCopy
	 * @param fastaImportTrackingDTO
	 * @return
	 * @throws Exception
	 */
	public DetermineTaxonomyIdResult getTaxonomyId( FASTAHeaderImporterCopy fastaHeaderImporterCopy, FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = null;
		
		
//		String headerName = header.getName();
//		String headerDescription = header.getDescription();
		
		DetermineTaxonomyIdParams determineTaxonomyIdParams = new DetermineTaxonomyIdParams();
		
		determineTaxonomyIdParams.setFastaImportTrackingDTOId( fastaImportTrackingDTO.getId() );
		determineTaxonomyIdParams.setHeaderFullString( fastaHeaderImporterCopy.getLine() );
		determineTaxonomyIdParams.setHeaderName( fastaHeaderImporterCopy.getName() );
		determineTaxonomyIdParams.setHeaderDescription( fastaHeaderImporterCopy.getDescription() );
		
		
		for ( int index = 0; index < taxonomyLookupArray.length; index++ ) {

			TaxonomyIdLookupIF taxonomyLookup =	taxonomyLookupArray[ index ];
		
			determineTaxonomyIdResult = 
					taxonomyLookup.getTaxonomyId( determineTaxonomyIdParams );
			
			//  When start adding "suggested taxonomy id" need to save that off and return as part of final response

			if ( determineTaxonomyIdResult.getTaxonomyId() != null ) {

				return determineTaxonomyIdResult; // EARLY EXIT   for first found taxonomy id
			}
		}
		
		//  Taxonomy Id not determined so return empty object
		
		determineTaxonomyIdResult = new DetermineTaxonomyIdResult();

		return determineTaxonomyIdResult;
		
	}

}
