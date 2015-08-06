package org.yeastrc.nrseq_fasta_importer.intermediate_import_file.writer_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.IntermediateImportFileConstants;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileEntry;

/**
 * Read the processed version of the FASTA file that the import step will use to insert the records
 *
 */
public class ImportFileReader {

	private static final Logger log = Logger.getLogger(ImportFileReader.class);

	private ImportFileReader() {}
	
	public static ImportFileReader getInstance( File importFile ) throws Exception {

		ImportFileReader importFileReader = new ImportFileReader();

		InputStream inStream = null;
		
		try {
			
			inStream = new FileInputStream(importFile);

			XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();

			importFileReader.xmlStreamReader = xmlInputFactory.createXMLStreamReader(inStream, IntermediateImportFileConstants.XML_CHARSET_UTF_8);
			
			JAXBContext jc = JAXBContext.newInstance( ImportFileEntry.class );

			importFileReader.unmarshaller = jc.createUnmarshaller();
//
//			//  Advance to first ImportFileEntry
//			
//			while( importFileReader.xmlStreamReader.hasNext() ){
//				
//				importFileReader.xmlStreamReader.next();
//
//				if( importFileReader.xmlStreamReader.isStartElement() 
//						&& importFileReader.xmlStreamReader.getLocalName().equals( IntermediateImportFileConstants.ENTRY_ELEMENT_NAME ) ){
//
//					break;
//				}
//			}


		} catch ( Exception e ) {
			
			log.error("Failed to create xmlStreamReader for file " + importFile.getAbsolutePath(), e );

			
			if ( inStream != null ) {
				
				try {
					
					inStream.close();
				} catch ( Exception e2 ) {
					
					log.error("Failed to close output stream for file " + importFile.getAbsolutePath(), e2 );
				}
			}
			
			throw e;
		}
		
		return importFileReader; 
	}
	
	private File importFile;
	private XMLStreamReader xmlStreamReader;
	private Unmarshaller unmarshaller = null;

	

	/**
	 * Close the connection with the FASTA file
	 * @throws Exception
	 */
	public void close() throws Exception {
		
		
		if( this.xmlStreamReader != null )
			this.xmlStreamReader.close();
		
		this.xmlStreamReader = null;
	}
	

	/**
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public ImportFileEntry readNext() throws Exception {
		

		//  Advance to next ImportFileEntry
		
		while( xmlStreamReader.hasNext() ){
			
			xmlStreamReader.next();

			if( xmlStreamReader.isStartElement() 
					&& xmlStreamReader.getLocalName().equals( IntermediateImportFileConstants.ENTRY_ELEMENT_NAME ) ){

				break;
			}
		}
		
//		try {
//			String localName = xmlStreamReader.getLocalName();
//			
//			int z = 0;
//			
//		} catch ( Exception e ) {
//			
//		} finally {
//			
//			
//		}
		
		if ( ! xmlStreamReader.hasNext() ) {
			
			return null;
		}
		
		Object object = unmarshaller.unmarshal( xmlStreamReader );
		
		if ( object == null ) {
			
			return null;
		}
		
		if ( ! ( object instanceof ImportFileEntry ) ) {
			
			String msg = "Unmarshalled object is not type ImportFileEntry, is type " + object.toString();
			
			log.error( msg );
			
			throw new Exception(msg);
		}
		
		ImportFileEntry importFileEntry = ( ImportFileEntry ) object;
		
		return importFileEntry;
	}
}
