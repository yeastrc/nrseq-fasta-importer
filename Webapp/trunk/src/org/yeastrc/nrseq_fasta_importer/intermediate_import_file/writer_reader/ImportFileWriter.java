package org.yeastrc.nrseq_fasta_importer.intermediate_import_file.writer_reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.IntermediateImportFileConstants;
import org.yeastrc.nrseq_fasta_importer.intermediate_import_file.dto.ImportFileEntry;

/**
 * Create the processed version of the FASTA file that the import step will use to insert the records
 *
 */
public class ImportFileWriter {

	private static final Logger log = Logger.getLogger(ImportFileWriter.class);

	private ImportFileWriter() {}
	
	public static ImportFileWriter getInstance( File importFile ) throws Exception {

		ImportFileWriter importFileWriter = new ImportFileWriter();

		OutputStream outputStream = null;
		
		try {
			
			outputStream = new FileOutputStream(importFile);

			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

			importFileWriter.xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream, IntermediateImportFileConstants.XML_CHARSET_UTF_8 );
			
			importFileWriter.xmlStreamWriter.writeStartDocument( IntermediateImportFileConstants.XML_CHARSET_UTF_8, "1.0" /* version */ );
			
			importFileWriter.xmlStreamWriter.writeStartElement( IntermediateImportFileConstants.ROOT_ELEMENT_NAME );
			
			JAXBContext jc = JAXBContext.newInstance( ImportFileEntry.class );

			importFileWriter.marshaller = jc.createMarshaller();


			//		To turn off the <?xml ?> declaration set the following property on the Marshaller.
			importFileWriter.marshaller.setProperty( Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			importFileWriter.marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );


		} catch ( Exception e ) {
			
			log.error("Failed to create xmlStreamWriter for file " + importFile.getAbsolutePath(), e );

			
			if ( outputStream != null ) {
				
				try {
					
					outputStream.close();
				} catch ( Exception e2 ) {
					
					log.error("Failed to close output stream for file " + importFile.getAbsolutePath(), e2 );
				}
			}
			
			throw e;
		}
		
		return importFileWriter; 
	}
	
	private File importFile;
	private XMLStreamWriter xmlStreamWriter;
	private Marshaller marshaller = null;

	

	/**
	 * Close the connection with the FASTA file
	 * @throws Exception
	 */
	public void close() throws Exception {
		
		xmlStreamWriter.writeEndDocument();
		
		if( this.xmlStreamWriter != null )
			this.xmlStreamWriter.close();
		
		this.xmlStreamWriter = null;
	}
	

	/**
	 * add to file
	 * 
	 * @return
	 * @throws Exception
	 */
	public void add( ImportFileEntry importFileEntry  ) throws Exception {
		
		xmlStreamWriter.writeCharacters("\n");
		marshaller.marshal( importFileEntry, xmlStreamWriter );
		xmlStreamWriter.writeCharacters("\n");
	}
}
