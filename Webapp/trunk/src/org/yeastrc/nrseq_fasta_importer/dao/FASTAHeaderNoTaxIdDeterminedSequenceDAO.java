package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedSequenceDTO;

/**
 * 
 * table fasta_header_no_tax_id_determined_sequence
 */
public class FASTAHeaderNoTaxIdDeterminedSequenceDAO {

	private static final Logger log = Logger.getLogger(FASTAHeaderNoTaxIdDeterminedSequenceDAO.class);
	

	//  private constructor
	private FASTAHeaderNoTaxIdDeterminedSequenceDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static FASTAHeaderNoTaxIdDeterminedSequenceDAO getInstance() { 
		return new FASTAHeaderNoTaxIdDeterminedSequenceDAO(); 
	}
	
	


	/**
	 * @param fastaHeaderNoTaxIdDeterminedId
	 * @return 
	 * @throws Exception
	 */
	public FASTAHeaderNoTaxIdDeterminedSequenceDTO getForFastaHeaderNoTaxIdDeterminedId( int fastaHeaderNoTaxIdDeterminedId ) throws Exception {


		FASTAHeaderNoTaxIdDeterminedSequenceDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT sequence FROM fasta_header_no_tax_id_determined_sequence WHERE fasta_header_no_tax_id_determined_id = ?";
		

		//CREATE TABLE fasta_header_no_tax_id_determined_sequence (
//				  fasta_header_no_tax_id_determined_id INT UNSIGNED NOT NULL,
//				  sequence MEDIUMTEXT NOT NULL,
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, fastaHeaderNoTaxIdDeterminedId );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new FASTAHeaderNoTaxIdDeterminedSequenceDTO();
				result.setFastaHeaderNoTaxIdDeterminedId( fastaHeaderNoTaxIdDeterminedId );
				result.setSequence( rs.getString( "sequence" ) );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAHeaderNoTaxIdDeterminedSequenceDTO, id: " + fastaHeaderNoTaxIdDeterminedId + ", sql: " + sql;
			
			log.error( msg, e );
			
			throw e;
			

		} finally {
			
			// be sure database handles are closed
			if( rs != null ) {
				try { rs.close(); } catch( Throwable t ) { ; }
				rs = null;
			}
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Throwable t ) { ; }
				pstmt = null;
			}
			
			if( conn != null ) {
				try { conn.close(); } catch( Throwable t ) { ; }
				conn = null;
			}
			
		}
		
		return result;
	}
	
	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( FASTAHeaderNoTaxIdDeterminedSequenceDTO item ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			save( item, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}

	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( FASTAHeaderNoTaxIdDeterminedSequenceDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;

		//CREATE TABLE fasta_header_no_tax_id_determined_sequence (
//		  fasta_header_no_tax_id_determined_id INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,
			
			
			

		final String sql = "INSERT INTO fasta_header_no_tax_id_determined_sequence "
				+ "(fasta_header_no_tax_id_determined_id, sequence )" +
				" VALUES ( ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getFastaHeaderNoTaxIdDeterminedId() );
			counter++;
			pstmt.setString( counter, item.getSequence() );
			
			pstmt.executeUpdate();
			
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert FASTAHeaderNoTaxIdDeterminedSequenceDTO,"
					+ "FASTAHeaderNoTaxIdDeterminedSequenceDTO item: " + item.toString()
					+ "\n sql: " + sql;
			
			log.error( msg, e );
			
			throw e;
			
		} finally {
			
			// be sure database handles are closed
			
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Throwable t ) { ; }
				pstmt = null;
			}
			

		}
		
	}
	

	
}
