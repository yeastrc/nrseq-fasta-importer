package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination.webservice_clients.ncbi.by_protein.webservice_response_dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="eSummaryResult")
public class NCBIProteinResponseDTO {


	private String error;

	private DocSum docSum;
	
	@XmlElement(name="DocSum")
	public DocSum getDocSum() {
		return docSum;
	}

	public void setDocSum(DocSum docSum) {
		this.docSum = docSum;
	}
	
	@XmlElement(name="ERROR")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	

	/**
	 * 
	 *
	 */
	public static class DocSum {
		
		List<Item> items;

		@XmlElement(name="Item")
		public List<Item> getItems() {
			return items;
		}

		public void setItems(List<Item> items) {
			this.items = items;
		}
	}
	
	/**
	 * 
	 *
	 */
	public static class Item {
		
		private String name;
		
		private String value;

		@XmlAttribute(name="Name")
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		@XmlValue //  value/content of <Item> element
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
	}
}


