package org.yeastrc.nrseq_fasta_importer.www.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.yeastrc.nrseq_fasta_importer.constants.FileNameAndDirectoryNameConstants;
import org.yeastrc.nrseq_fasta_importer.constants.StrutsGlobalForwardNames;
import org.yeastrc.nrseq_fasta_importer.dao.FASTAImportTrackingDAO;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;
import org.yeastrc.nrseq_fasta_importer.fasta_importer_work_dir.Get_FASTA_Importer_Work_Directory_And_SubDirs;

/**
 * Reads the XML file generated from find taxonomy ids step and downloads it 
 *
 */
public class TaxonomyIdMappingDisplayAction extends Action {

	private static final Logger log = Logger.getLogger(TaxonomyIdMappingDisplayAction.class);
	
	
	private static final int BUFFER_SIZE = 4 * 1024 * 1024;  // 4MB
	

	public ActionForward execute( ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response )
					  throws Exception {
				
		String fastaImportTrackingIdString = request.getParameter( "fastaImportTrackingId" );
		
		int fastaImportTrackingId = 0;
		
		try {
			fastaImportTrackingId = Integer.parseInt(fastaImportTrackingIdString);
			
		} catch ( Exception e ) {
			

			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
		
		File inputToImportFile = null;
		
		FASTAImportTrackingDTO fastaImportTrackingDTO = null; 
				
		try {
			fastaImportTrackingDTO = FASTAImportTrackingDAO.getInstance().getForId(fastaImportTrackingId);
			
			if ( fastaImportTrackingDTO == null ) {

				return mapping.findForward( "NoRecord" );
			}
			
			
			
			File fasta_Importer_Work_Directory = Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().get_FASTA_Importer_Work_Directory();

			String dirNameForImportTrackingId =
					Get_FASTA_Importer_Work_Directory_And_SubDirs.getInstance().getDirForImportTrackingId( fastaImportTrackingDTO.getId() );
			
			File dirForImportTrackingId  =  new File( fasta_Importer_Work_Directory , dirNameForImportTrackingId );

			inputToImportFile = new File( dirForImportTrackingId, FileNameAndDirectoryNameConstants.DATA_TO_IMPORT_FILE );

			if ( ! inputToImportFile.exists() ) {
			
				return mapping.findForward( "NoFile" );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Error retrieving record for fastaImportTrackingId " + fastaImportTrackingId;
			log.error( msg );

			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
		}
		
		String outputFilename = "ImportMappingFor_" + fastaImportTrackingDTO.getFilename() + "_.xml";
		
		
		//  At this point, committed to writing to the response output stream:
		
		
		
		//  Set header for browser to display in the browser
//		response.setHeader("Content-Type", "text/xml;charset=" + IntermediateImportFileConstants.XML_CHARSET_ISO_8859_1 );
				
		//  Set header for download
		response.setHeader("Content-Disposition", "attachment; filename=" + outputFilename);

		InputStream inputStream = null;
		
		OutputStream outputStream = null; 
		

		try {

			outputStream = response.getOutputStream();
			
			inputStream = new FileInputStream(inputToImportFile);
			
			byte[] buffer = new byte[ BUFFER_SIZE ];


			while ( true ) {

				
				int bytesRead = inputStream.read( buffer );
				
				if ( bytesRead == -1 ) {
					
					break;
				}
				
				if ( bytesRead > 0 ) {
				
					outputStream.write(buffer, 0, bytesRead);
				}
				
			}
			
			
			
		} catch ( Exception e ) {
			
			String msg = "Error processing file for fastaImportTrackingId " + fastaImportTrackingId;
			log.error( msg );

			
			throw new Exception( "Error writing response" );
			
			//  This will likely fail since the response has started writing to the output
//			return mapping.findForward( StrutsGlobalForwardNames.GENERAL_ERROR );
			
			
		} finally {
			

			if ( inputStream != null ) {

				try {

					inputStream.close();
				} catch ( Exception e ) {

					log.error( "Exception closing inputStream", e );
				}
			}

			if ( outputStream != null ) {

				try {

					outputStream.close();
				} catch ( Exception e ) {

					log.error( "Exception closing outputStream", e );
				}
			}
		}
		
	

		return null;		
	}
	
	
}
