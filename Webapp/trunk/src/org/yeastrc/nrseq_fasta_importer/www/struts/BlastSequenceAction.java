package org.yeastrc.nrseq_fasta_importer.www.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.yeastrc.nrseq_fasta_importer.constants.StrutsGlobalForwardNames;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAHeaderNoTaxIdDeterminedSequenceDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedSequenceDTO;

/**
 * Retrieve sequence and redirect to PDR BLAST page
 *
 */
public class BlastSequenceAction  extends Action {

	private static final Logger log = Logger.getLogger(BlastSequenceAction.class);
	
	private static final String PDR_BLAST_URL_BASE = "http://yeastrc.org/pdr/blastSearchInit.do?query=";
	

	public ActionForward execute( ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response )
					  throws Exception {
				
		String idString = request.getParameter( "id" );
		
		int id = 0;
		
		try {
			id = Integer.parseInt(idString);
			
		} catch ( Exception e ) {
			

			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
		
		String sequence = "";
		
		try {
			
			FASTAHeaderNoTaxIdDeterminedSequenceDTO result = FASTAHeaderNoTaxIdDeterminedSequenceDAO.getInstance().getForFastaHeaderNoTaxIdDeterminedId( id );
			
			if ( result == null ) {

				String msg = "No record found for id " + id;
				log.error( msg );

				return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
			}
			
			sequence = result.getSequence();
			
			
		} catch ( Exception e ) {
			
			String msg = "Error retrieving record for id " + id;
			log.error( msg );

			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
		
		
		String blastURL = PDR_BLAST_URL_BASE + sequence;
		
		response.sendRedirect( blastURL );
		
		return null;  // since set redirect
		
	}
}
