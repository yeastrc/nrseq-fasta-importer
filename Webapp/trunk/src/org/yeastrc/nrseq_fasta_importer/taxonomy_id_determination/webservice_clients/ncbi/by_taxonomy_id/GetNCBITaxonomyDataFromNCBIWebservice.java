package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.NCBITaxonomyDataConstants;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyDataRootDTO;
import org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_taxonomy_id.webservice_response_dto.NcbiTaxonomyErrorRootDTO;




/**
 * 
 *
 */
public class GetNCBITaxonomyDataFromNCBIWebservice {

	private static final Logger log = Logger.getLogger(GetNCBITaxonomyDataFromNCBIWebservice.class);
	
	private GetNCBITaxonomyDataFromNCBIWebservice() { }
	public static GetNCBITaxonomyDataFromNCBIWebservice getInstance() { 
		return new GetNCBITaxonomyDataFromNCBIWebservice(); 
	}
	
	/**
	 * @param taxonomyId
	 * @return
	 * @throws Exception 
	 */
	public GetNCBITaxonomyDataFromNCBIWebserviceResponse getNCBITaxonomyDataFromNCBIWebservice( long taxonomyId ) throws Exception {
		
		GetNCBITaxonomyDataFromNCBIWebserviceResponse response = new GetNCBITaxonomyDataFromNCBIWebserviceResponse();
		
		
		String ncbiUrl = NCBITaxonomyDataConstants.URL_BASE + taxonomyId;
		

		HttpClient client = null;
		HttpGet httpGet = null;

		HttpResponse httpResponse = null;
		
		InputStream inputStreamHttpResponse = null;

		Unmarshaller unmarshaller = null;

		try {

			JAXBContext jc = JAXBContext.newInstance( NcbiTaxonomyDataRootDTO.class, NcbiTaxonomyErrorRootDTO.class );

			unmarshaller = jc.createUnmarshaller();
			

			client = new DefaultHttpClient();

			httpGet = new HttpGet( ncbiUrl );

			httpResponse = client.execute(httpGet);

			inputStreamHttpResponse = httpResponse.getEntity().getContent(); 

			Object returnedObject = unmarshaller.unmarshal( inputStreamHttpResponse );
			
			if ( returnedObject instanceof NcbiTaxonomyDataRootDTO ) {
				
				NcbiTaxonomyDataRootDTO item = (NcbiTaxonomyDataRootDTO) returnedObject;
				
				response.setNcbiTaxonomyDataRootDTO(item);
				
			} else if ( returnedObject instanceof NcbiTaxonomyErrorRootDTO ) {

				NcbiTaxonomyErrorRootDTO item = (NcbiTaxonomyErrorRootDTO) returnedObject;
				
				response.setNcbiTaxonomyErrorRootDTO(item);
				
			} else {
				
				String msg = "Failed to deserialize response into expected classes";
				log.error( msg );
				throw new Exception(msg);
			}

		} catch (Exception e) {

			log.error("Failed to get NCBI data for taxonomy id.  URL = " + ncbiUrl, e );
			throw e;

		} finally { 

			if ( inputStreamHttpResponse != null ) {
				inputStreamHttpResponse.close();
			}
		}

		
		return response;
	}
}
