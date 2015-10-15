package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;

public class TaxonomyIdFromUser implements TaxonomyIdLookupIF {

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
	@Override
	public DetermineTaxonomyIdResult getTaxonomyId( DetermineTaxonomyIdParams determineTaxonomyIdParams ) throws Exception {
		
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = new DetermineTaxonomyIdResult();
		
		
		Integer taxonomyId = null;
		
		
		String headerName = determineTaxonomyIdParams.getHeaderName();
//		String headerDescription = header.getDescription();
		
		int fastaImportTrackingId = determineTaxonomyIdParams.getFastaImportTrackingDTOId();
		
		int headerLineNumber = determineTaxonomyIdParams.getHeaderLineNumber();
	
		List<FASTAHeaderNoTaxIdDeterminedDTO>  taxList = 
				FASTAHeaderNoTaxIdDeterminedDAO.getInstance().getForFastaImportTrackingIdHeaderName( fastaImportTrackingId, headerName, headerLineNumber );
		
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
