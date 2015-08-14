/*
 * FASTAValidator.java
 * Created on Aug 29, 2006
 * Created by Michael Riffle <mriffle@u.washington.edu>
 */

package org.yeastrc.nrseq_fasta_importer.utils;

import java.util.Arrays;

import org.yeastrc.nrseq_fasta_importer.exception.FASTAImporterDataErrorException;

/**
 * Description of class goes here.
 * 
 * @author Michael Riffle <mriffle@u.washington.edu>
 * @version Aug 29, 2006
 */

public class FASTAValidator {

	private static final String[] validProteinResidues = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			                                               "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	/**
	 * Check supplied sequence to see if it is a valid protein sequence
	 * according to the FASTA format definition
	 * @param sequence
	 * @return
	 */
	public static boolean validProteinSequence(String sequence) throws Exception {

		/*
			A  alanine                         P  proline
		    B  aspartate or asparagine         Q  glutamine
		    C  cystine                         R  arginine
		    D  aspartate                       S  serine
		    E  glutamate                       T  threonine
		    F  phenylalanine                   U  selenocysteine
		    G  glycine                         V  valine
		    H  histidine                       W  tryptophan
		    I  isoleucine                      Y  tyrosine
		    K  lysine                          Z  glutamate or glutamine
		    L  leucine                         X  any
		    M  methionine                      *  translation stop
		    N  asparagine                      -  gap of indeterminate length
		    J  
		 */
		
		//String[] residues = sequence.split("");
		
		char[] residues = sequence.toCharArray();
		
		for (int index = 0; index < residues.length; index++) {
			if (Arrays.binarySearch( validProteinResidues, String.valueOf(residues[index])) < 0) {
				if (residues[index] != '*' && residues[index] != '-' ) {
					int indexStartAtOne = index + 1;
					throw new FASTAImporterDataErrorException( "Invalid sequence.  Invalid residue was: '" + residues[index] + "' at position " + indexStartAtOne + " (sequence starts at position 1).  " );
					//return false;
				}
			}
		}
		
		return true;
	}
	
}
