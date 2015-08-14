package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.lookups;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdParams;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.DetermineTaxonomyIdResult;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.TaxonomyIdLookupIF;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession.GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse;




/**
 * 
 *
 */
public class TaxonomyIdFrom_UniProt_inDescription implements TaxonomyIdLookupIF {

	private static final Logger log = Logger.getLogger(TaxonomyIdFrom_UniProt_inDescription.class);
	
	Pattern giPattern = Pattern.compile( "^(gi\\|\\d+).*$" );

	Pattern uniprotPattern = Pattern.compile( "^([ABCOPQ]\\w\\w\\w\\w\\w)$" );
	
	// Example >UniRef90_A0AUS7 Putative uncharacterized protein (Fragment) n=1 Tax=Xenopus laevis RepID=A0AUS7_XENLA
	Pattern uniprotPattern2 = Pattern.compile( "^UniRef.+RepID=(\\S+).*$" );
	

	
	
	private TaxonomyIdFrom_UniProt_inDescription() { }
	public static TaxonomyIdFrom_UniProt_inDescription getInstance() { 
		return new TaxonomyIdFrom_UniProt_inDescription(); 
	}
	
	
	

	/**
	 * @param header
	 * @param fastaEntry
	 * @return
	 * @throws Exception
	 */
	@Override
	public DetermineTaxonomyIdResult getTaxonomyId( DetermineTaxonomyIdParams determineTaxonomyIdParams ) throws Exception {
		
		String fullHeaderString = determineTaxonomyIdParams.getHeaderFullString();
		
		DetermineTaxonomyIdResult determineTaxonomyIdResult = new DetermineTaxonomyIdResult();
		
		
		Integer taxonomyId = null;
		
		
		String headerName = determineTaxonomyIdParams.getHeaderName();
//		String headerDescription = header.getDescription();

		if ( headerName == null ) {
			
			
			
		} else {

			Integer taxonomyIdLocal = getTaxonomyIdLocal( headerName, fullHeaderString );
			
			if ( taxonomyIdLocal != null ) {
				
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
	private Integer getTaxonomyIdLocal( String headerName, String fullHeaderString ) throws Exception {
		

		Integer taxonomyId = null;
        
		Matcher gm = null;

		
		//if it matches a gi, use the NCBI network searcher
		gm = this.giPattern.matcher( headerName );
        if (gm.matches()) {
        	
        } else {
        	
        	//if it matches a uniprot acc use the network uniprot searcher
        	gm = this.uniprotPattern.matcher( headerName );

        	if( gm.matches() ) {
        		GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice uns = GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice.getInstance();
        		GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse response =
        				uns.getTaxonomyID( headerName );
        		taxonomyId = response.getTaxonomyId();
        		if( taxonomyId != null ) 
        			return taxonomyId;
        	}
        	
        	//other uniprot pattern handler here
        	gm = this.uniprotPattern2.matcher( fullHeaderString );
        	if( gm.matches() ) {
        		GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice uns = GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice.getInstance();
        		GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse response =
        				uns.getTaxonomyID( gm.group( 1 ) );
        		taxonomyId = response.getTaxonomyId();
        		if( taxonomyId != null ) 
        			return taxonomyId;
        	}
        	
        }
		
        
		
		return null;

	}

}
