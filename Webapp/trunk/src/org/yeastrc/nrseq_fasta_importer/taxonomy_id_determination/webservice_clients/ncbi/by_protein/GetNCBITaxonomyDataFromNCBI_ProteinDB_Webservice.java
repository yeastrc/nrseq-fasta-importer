package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.yeastrc.nrseq_fasta_importer.constants.WebServiceClientRetryMaxConstants;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;
import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterRemoteWebserviceCallErrorException;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.webservice_response_dto.NCBIProteinResponseDTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.webservice_response_dto.NCBIProteinResponseDTO.DocSum;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.webservice_response_dto.NCBIProteinResponseDTO.Item;

/**
 * 
 *
 */
public class GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice {


	/** The URL to use to search NCBI's database via the web, only the gi # should be appended to the end */
	private static final String NCBI_URL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=protein&id=";
	
	//  "Name" attribute value for the taxonomy id
	private static final String TAX_ID_NAME_STRING = "TaxId";
	
	
	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice.class);
	
	private GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice() { }
	public static GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice getInstance() { 
		return new GetNCBITaxonomyDataFromNCBI_ProteinDB_Webservice(); 
	}

	
	/**
	 * @param giAccessionNumber - The NCBI gi accession number
	 * @return
	 * @throws Exception
	 */
	public GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse getNCBITaxonomyDataFromNCBIProteinWebservice( int giAccessionNumber ) throws Exception {
		
		GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse response = new GetNCBITaxonomyDataFromNCBI_ProteinDB_WebserviceResponse();
		
		
		String ncbiUrl = NCBI_URL + giAccessionNumber;
		
		boolean successful = false;
		int retryCount = 0;
		
		
		while ( ! successful ) {  // exit by Exception if retry count exceeded

			HttpClient client = null;
			HttpGet httpGet = null;

			HttpResponse httpResponse = null;

			InputStream inputStreamHttpResponse = null;

			Unmarshaller unmarshaller = null;

			try {

				JAXBContext jc = JAXBContext.newInstance( NCBIProteinResponseDTO.class );

				unmarshaller = jc.createUnmarshaller();


				client = new DefaultHttpClient();

				httpGet = new HttpGet( ncbiUrl );

				httpResponse = client.execute(httpGet);

				inputStreamHttpResponse = httpResponse.getEntity().getContent();
				

				//  Turn off loading external DTDs
				
				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				spf.setFeature("http://xml.org/sax/features/validation", false);

				XMLReader xmlReader = spf.newSAXParser().getXMLReader();
				InputSource inputSource = new InputSource( inputStreamHttpResponse );
				
				SAXSource saxSource = new SAXSource(xmlReader, inputSource);

				Object returnedObject = unmarshaller.unmarshal( saxSource );

				
				
				if ( returnedObject instanceof NCBIProteinResponseDTO ) {

					NCBIProteinResponseDTO item = (NCBIProteinResponseDTO) returnedObject;

					if ( item.getError() != null ) {

						String msg = "Error getting taxonomy id from GI number.  GI number: " + giAccessionNumber + ", URL = " + ncbiUrl;

						log.error( msg + ", error message: " + item.getError() );

						throw new FASTAImporterRemoteWebserviceCallErrorException(msg);
					}

					response.setErrorMsg( item.getError() );
					response.setNcbiProteinResponseDTO( item );

					Integer taxonomyId = getTaxonomyId( item, giAccessionNumber );


					response.setTaxonomyId( taxonomyId );
					
					successful = true;

				} else {

					String msg = "Failed to deserialize response into expected classes, GI number: " + giAccessionNumber + ", URL = " + ncbiUrl;
					log.error( msg );
					throw new FASTAImporterRemoteWebserviceCallErrorException(msg);
				}

			} catch ( FASTAImporterRemoteWebserviceCallErrorException e ) {


				retryCount++;
				
				if ( retryCount > WebServiceClientRetryMaxConstants.WEBSERVICE_CLIENT_RETRY_MAX_COUNT ) {

					throw e;
				}
				

			} catch (Exception e) {

				String msg = "Failed to get NCBI data for GI id. GI number: " + giAccessionNumber + ", URL = " + ncbiUrl;

				log.error( msg, e );
				
				retryCount++;
				
				if ( retryCount > WebServiceClientRetryMaxConstants.WEBSERVICE_CLIENT_RETRY_MAX_COUNT ) {

					throw new FASTAImporterRemoteWebserviceCallErrorException( msg );
				}

			} finally { 

				if ( inputStreamHttpResponse != null ) {
					inputStreamHttpResponse.close();
				}
			}
			
			if ( ! successful ) {

				Thread.sleep( 3000 ); // in milliseconds.  Sleep before retry
			}
			
		}

		
		return response;
	}

	/**
	 * @param ncbiProteinResponseDTO
	 * @return
	 * @throws FASTAImporterDataErrorException 
	 */
	private Integer getTaxonomyId( NCBIProteinResponseDTO ncbiProteinResponseDTO, long giAccessionNumber ) throws FASTAImporterDataErrorException {
		
		Integer taxonomyId = null;
		
		String taxonomyIdString = null;
		
		DocSum docSum = ncbiProteinResponseDTO.getDocSum();
		
		if ( docSum != null ) {
			
			List<Item> items = docSum.getItems();
			
			if ( items != null ) {
				
				for ( Item item : items ) {
				
					if ( TAX_ID_NAME_STRING.equals( item.getName() ) ) {
						
						taxonomyIdString = item.getValue();
						break;
					}
				}
			}
			
		}
		
		if ( taxonomyIdString != null ) {
			
			try {
				
				taxonomyId = Integer.parseInt(taxonomyIdString);
				
			} catch ( Exception e ) {
				
				String msg = "Taxonomy id from NCBI for GI is not an integer. GI: " + giAccessionNumber;
				
				throw new FASTAImporterDataErrorException( msg );
			}
			
			
		}
		
		
		
		return taxonomyId;
	}
}
