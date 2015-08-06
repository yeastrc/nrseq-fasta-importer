package org.yeastrc.nrseq_fasta_importer.server_url;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ConfigSystemsKeysConstants;
import org.yeastrc.nrseq_fasta_importer.dao.ConfigSystemDAO;

/**
 * 
 *
 */
public class GetServerURLConfig {

	private static final Logger log = Logger.getLogger(GetServerURLConfig.class);
	
	/**
	 * @return true if valid
	 * @throws Exception - If data not valid or error getting data
	 */
	public static boolean validateServerURLConfig() throws Exception {
		
		
		String serverURL = null;

		try {
			serverURL = GetServerURLConfig.getServerURL();

		} catch (Exception e ) {

			String msg = "Exception getting server config for validation";
			log.error( msg, e );
			
			throw e;
		}
		
		if ( StringUtils.isEmpty( serverURL ) ) {

			String msg = "Config for '" + ConfigSystemsKeysConstants.SERVER_URL_KEY
					+ "' cannot be empty.";
			log.error( msg );
			
			throw new Exception(msg);
		}
		
		

		return true;
	}

	
	
	public static String getServerURL() throws Exception {
		
		String webserviceURL =
		ConfigSystemDAO.getInstance().getConfigValueForConfigKey( ConfigSystemsKeysConstants.SERVER_URL_KEY );
		
		return webserviceURL;
	}

}
