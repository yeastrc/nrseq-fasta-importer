package org.yeastrc.nrseq_fasta_importer.www.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.yeastrc.nrseq_fasta_importer.constants.StrutsGlobalForwardNames;
import org.yeastrc.nrseq_fasta_importer.send_email.GetEmailConfig;

/**
 * 
 *
 */
public class HomeAction  extends Action {

	private static final Logger log = Logger.getLogger(HomeAction.class);
	

	public ActionForward execute( ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response )
					  throws Exception {
				
		try {
			
			if ( GetEmailConfig.isSendEmail() ) {
				
				request.setAttribute( "send_email_configured", true );
			}
			
			return mapping.findForward( "Success" );
			
		} catch ( Exception e ) {
			
			String msg = "Exception caught: " + e.toString();
			
			log.error( msg, e );
			
			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
	}
}
