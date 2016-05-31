package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.uniprot.by_accession;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceClientRetryMaxConstants;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterRemoteWebserviceCallErrorException;


/**
 * Get taxonomy id for accesssion string from Uniprot webservice
 *
 */
public class GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice {
	
	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice.class);

	private final String SEARCH_URL = "http://www.ebi.ac.uk/cgi-bin/dbfetch?db=uniprotkb&style=raw&id=";
	

	private GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice() { }
	public static GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice getInstance() { 
		return new GetNCBITaxonomyDataFromUniprot_assessionNumberWebservice(); 
	}
	
	
	/**
	 * Get the taxonomy ID for the supplied uniprot/swissprot database accession
	 * string.
	 * @param accession
	 * @return The NCBI taxonomy ID number, 0 if not found
	 * @throws Exception
	 */
	public GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse getTaxonomyID( String accession ) throws Exception {
		
		GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse response = new GetNCBITaxonomyDataFromUniprot_assessionNumberWebserviceResponse();
		

		boolean successful = false;
		int retryCount = 0;
		
		
		while ( ! successful ) {  // exit by Exception if retry count exceeded


			if ( log.isDebugEnabled() ) {

				log.debug( "Trying Uniprot network lookup for " + accession + "..." );
			}
			
			String urlString = SEARCH_URL + accession; 

			InputStream is = null;
			BufferedReader br = null;

			try {

				URL url = new URL( urlString );
				is = url.openStream();
				br = new BufferedReader( new InputStreamReader( is ) );
				String line;

				while ((line = br.readLine()) != null) {
					if( !line.startsWith( "OX" ) ) continue;
					if( line.startsWith( "//" ) ) break;

					Pattern p = Pattern.compile( "^OX\\s+NCBI_TaxID=(\\d+)\\D.*$" );
					Matcher m = p.matcher( line );

					if( !m.matches() ) continue;

					int taxid = Integer.parseInt( m.group( 1 ) );

					response.setTaxonomyId(taxid);

					break;
				}
				

				successful = true;
				

			} catch ( Exception e ) {


				String msg = "Failed to get Uniprot network lookup for accession: " + accession + ", URL = " + urlString;

				log.error( msg, e );
				
				retryCount++;
				
				if ( retryCount > WebServiceClientRetryMaxConstants.WEBSERVICE_CLIENT_RETRY_MAX_COUNT ) {

					throw new FASTAImporterRemoteWebserviceCallErrorException( msg );
				}


			} finally {

				if ( br != null ) {
					try {
						br.close();
					} catch ( Throwable t ) {
						log.error("Exception closing buffered reader: ", t );
					}
				}
				if ( is != null ) {
					try {
						is.close();
					} catch ( Throwable t ) {
						log.error("Exception closing input stream: ", t );
					}
				}
			}
			
			if ( ! successful ) {

				Thread.sleep( 3000 ); // in milliseconds.  Sleep before retry
			}
			
		}
		
		//System.out.println( " Got tax id " + taxid );
		
		
		
		return response;
	}
}
