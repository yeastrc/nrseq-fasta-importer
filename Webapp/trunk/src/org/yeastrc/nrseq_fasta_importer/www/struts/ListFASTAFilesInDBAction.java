package org.yeastrc.nrseq_fasta_importer.www.struts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.yeastrc.nrseq_fasta_importer.constants.StrutsGlobalForwardNames;
import org.yeastrc.nrseq_fasta_importer.dao.YRC_NRSEQ_tblDatabaseDAO;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblDatabaseDTO;

/**
 * 
 *
 */
public class ListFASTAFilesInDBAction  extends Action {

	private static final Logger log = Logger.getLogger(ListFASTAFilesInDBAction.class);
	

	public ActionForward execute( ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response )
					  throws Exception {
				
		try {
			
			List<YRC_NRSEQ_tblDatabaseDTO> resultList = 
					YRC_NRSEQ_tblDatabaseDAO.getInstance().getAll();
			
			// sort alpha on filename
			
			Collections.sort( resultList, new Comparator<YRC_NRSEQ_tblDatabaseDTO>() {

				@Override
				public int compare(YRC_NRSEQ_tblDatabaseDTO o1,
						YRC_NRSEQ_tblDatabaseDTO o2) {

					return o1.getName().compareTo( o2.getName() );
				}
			});
			
			request.setAttribute( "resultList", resultList );
			
			return mapping.findForward( "Success" );
			
		} catch ( Exception e ) {
			
			String msg = "Exception caught: " + e.toString();
			
			log.error( msg, e );
			
			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
	}
}
