package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;

import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAHeader;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;

public class TaxonomyIdFromUser {

	private static final Logger log = Logger.getLogger(TaxonomyIdFromUser.class);
	
	private TaxonomyIdFromUser() { }
	public static TaxonomyIdFromUser getInstance() { 
		return new TaxonomyIdFromUser(); 
	}
	

	/**
	 * Get the taxonomy id the user entered 
	 * 
	 * @param header
	 * @param fastaEntry
	 * @return
	 * @throws Exception
	 */
	public DetermineTaxonomyIdResult getTaxonomyId( FASTAHeader header, FASTAEntry fastaEntry, FASTAImportTrackingDTO fastaImportTrackingDTO ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = new DetermineTaxonomyIdResult();
		
		
		Integer taxonomyId = null;
		
		
		String headerName = header.getName();
//		String headerDescription = header.getDescription();
		
		int fastaImportTrackingId = fastaImportTrackingDTO.getId();
	
		List<FASTAHeaderNoTaxIdDeterminedDTO>  taxList = 
				FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingIdHeaderName( fastaImportTrackingId, headerName );
		
		if ( taxList.isEmpty() ) {
			
			
		} else if ( taxList.size() > 1 ) {
			
			String msg = "More than one entry found in fasta_header_no_tax_id_determined for "
					+ "fastaImportTrackingId: " + fastaImportTrackingId
					+ " and headerName: " + headerName;
		
			log.error( msg );
			
			throw new Exception(msg);
			
		} else {
			
			FASTAHeaderNoTaxIdDeterminedDTO  taxEntry = taxList.get(0);
			
			if ( taxEntry.getUserAssignedTaxId() != null ) {
				
				taxonomyId = taxEntry.getUserAssignedTaxId();
			}
		}
	
		determineTaxonomyIdResult.setTaxonomyId( taxonomyId );
		
		return determineTaxonomyIdResult;
	}
}
