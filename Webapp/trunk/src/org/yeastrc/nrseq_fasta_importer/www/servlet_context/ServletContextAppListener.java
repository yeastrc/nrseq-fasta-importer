package org.yeastrc.nrseq_fasta_importer.www.servlet_context;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.WebConstants;
import org.yeastrc.nrseq_fasta_importer.send_email.GetEmailConfig;
import org.yeastrc.nrseq_fasta_importer.server_url.GetServerURLConfig;
import org.yeastrc.nrseq_fasta_importer.threads.ProcessImportFASTAFileThread;
import org.yeastrc.nrseq_fasta_importer.uploaded_file.GetTempDirForFileUploads;


/**
 * This class is loaded and the method "contextInitialized" is called when the web application is first loaded by the container
 *
 */
public class ServletContextAppListener extends HttpServlet implements ServletContextListener {

	private static Logger log = Logger.getLogger( ServletContextAppListener.class );
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		
		log.warn( "INFO:  !!!!!!!!!!!!!!!   Start up of web app  'NRSEQ FASTA Importer' beginning  !!!!!!!!!!!!!!!!!!!! " );


		

		ServletContext context = event.getServletContext();

		String contextPath = context.getContextPath();

		context.setAttribute( WebConstants.APP_CONTEXT_CONTEXT_PATH, contextPath );

		CurrentContext.setCurrentWebAppContext( contextPath );
		
		
		try {
			GetTempDirForFileUploads.getInstance().validateTempDirForFileUploads(); // throws Exception if error
		} catch (Exception e) {
			//  already logged
			throw new RuntimeException( e );
		} 
		
		try {
			GetEmailConfig.validateEmailConfig(); // throws Exception if error
		} catch (Exception e) {
			//  already logged
			throw new RuntimeException( e );
		} 

		
		try {
			GetServerURLConfig.validateServerURLConfig(); // throws Exception if error
		} catch (Exception e) {
			//  already logged
			throw new RuntimeException( e );
		} 
		
//		RestartAndResetInProgressRequestsOnWebappStartupThread.getInstance().start();

		ProcessImportFASTAFileThread.getInstance().start();
		

		log.warn( "INFO:  !!!!!!!!!!!!!!!   Start up of web app  'NRSEQ FASTA Importer' complete  !!!!!!!!!!!!!!!!!!!! " );

		log.warn( "INFO: Application context values set.  Key = " + WebConstants.APP_CONTEXT_CONTEXT_PATH + ": value = " + contextPath
				+ "" );


	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {

		//ServletContext context = event.getServletContext();

		ProcessImportFASTAFileThread.getInstance().shutdown();

	}



}
