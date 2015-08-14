package org.yeastrc.nrseq_fasta_importer.taxonomy_id_determination;


/**
 * 
 *
 */
public interface TaxonomyIdLookupIF {

	public DetermineTaxonomyIdResult getTaxonomyId( DetermineTaxonomyIdParams determineTaxonomyIdParams ) throws Exception;

}
