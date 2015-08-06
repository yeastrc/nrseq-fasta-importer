package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;


import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;




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
	 * @param header
	 * @param fastaEntry
	 * @return
	 * @throws Exception
	 */
	public DetermineTaxonomyIdResult getTaxonomyId( FASTAHeader header, FASTAEntry fastaEntry, FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = null;
		
		
//		String headerName = header.getName();
//		String headerDescription = header.getDescription();
		
		determineTaxonomyIdResult = 
				TaxonomyIdFromUser.getInstance().getTaxonomyId( header, fastaEntry, fastaImportTrackingDTO );

		if ( determineTaxonomyIdResult.getTaxonomyId() != null ) {
			
			return determineTaxonomyIdResult;
		}
		
		determineTaxonomyIdResult = TaxonomyIdFrom_Tax_Id_inDescription.getInstance().getTaxonomyId( header, fastaEntry );

		if ( determineTaxonomyIdResult.getTaxonomyId() != null ) {
			
			return determineTaxonomyIdResult;
		}

		//  Taxonomy Id not determined so return empty object
		
		determineTaxonomyIdResult = new DetermineTaxonomyIdResult();

		return determineTaxonomyIdResult;
		
	}

}
