package org.yeastrc.nrseq_fasta_importer.objects;

import java.util.List;

import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;

/**
 * Response from HeaderNoTaxonomyWebservice is a list of these objects
 *
 */
public class FASTAHeaderNoTaxIdDeterminedResponseItem {

	private FASTAHeaderNoTaxIdDeterminedDTO item;
	
	private List<FASTAHeaderNoTaxIdDeterminedSuggestionItem> suggestions;

	public FASTAHeaderNoTaxIdDeterminedDTO getItem() {
		return item;
	}

	public void setItem(FASTAHeaderNoTaxIdDeterminedDTO item) {
		this.item = item;
	}

	public List<FASTAHeaderNoTaxIdDeterminedSuggestionItem> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(
			List<FASTAHeaderNoTaxIdDeterminedSuggestionItem> suggestions) {
		this.suggestions = suggestions;
	}
}
