package org.yeastrc.nrseq_fasta_importer.intermediate_file.writer_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.IntermediateImportFileConstants;
import org.yeastrc.nrseq_fasta_importer.intermediate_file.dto.IntermediateFileEntry;

/**
 * Read the processed version of the FASTA file that will be processed by the next step
 *
 */
public class IntermediateFileReader {

	private static final Logger log = Logger.getLogger(IntermediateFileReader.class);

	private IntermediateFileReader() {}
	
	public static IntermediateFileReader getInstance( File intermediateFile ) throws Exception {

		IntermediateFileReader intermediateFileReader = new IntermediateFileReader();

		InputStream inStream = null;
		
		try {
			
			inStream = new FileInputStream(intermediateFile);

			XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();

			intermediateFileReader.xmlStreamReader = xmlInputFactory.createXMLStreamReader( inStream, IntermediateImportFileConstants.XML_CHARSET_ISO_8859_1 );
			
			JAXBContext jc = JAXBContext.newInstance( IntermediateFileEntry.class );

			intermediateFileReader.unmarshaller = jc.createUnmarshaller();



		} catch ( Exception e ) {
			
			log.error("Failed to create xmlStreamReader for file " + intermediateFile.getAbsolutePath(), e );

			
			if ( inStream != null ) {
				
				try {
					
					inStream.close();
				} catch ( Exception e2 ) {
					
					log.error("Failed to close output stream for file " + intermediateFile.getAbsolutePath(), e2 );
				}
			}
			
			throw e;
		}
		
		return intermediateFileReader; 
	}
	
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
	public IntermediateFileEntry readNext() throws Exception {
		

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
		
		if ( ! ( object instanceof IntermediateFileEntry ) ) {
			
			String msg = "Unmarshalled object is not type ImportFileEntry, is type " + object.toString();
			
			log.error( msg );
			
			throw new Exception(msg);
		}
		
		IntermediateFileEntry importFileEntry = ( IntermediateFileEntry ) object;
		
		return importFileEntry;
	}
}
