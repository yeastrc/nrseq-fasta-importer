package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.Tmp_FASTA_sequence_DTO;



/**
 * table tmp_fasta_sequence
 * 
 * While validating the FASTA file, this table is inserted into
 * 
 * This table is truncated before and after each file is validated.
 * 
 * This table is locked from before validation begins until validation ends
 * 
 */
public class Tmp_FASTA_sequence_DAO {

	private static final Logger log = Logger.getLogger(Tmp_FASTA_sequence_DAO.class);
	

	//  private constructor
	private Tmp_FASTA_sequence_DAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static Tmp_FASTA_sequence_DAO getInstance() { 
		return new Tmp_FASTA_sequence_DAO(); 
	}
	
	



	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Tmp_FASTA_sequence_DTO getForId( int id ) throws Exception {
		

		Tmp_FASTA_sequence_DTO result = null;
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			result = getForId( id, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
		return result;
	}

	


	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public Tmp_FASTA_sequence_DTO getForId( int id, Connection dbConnection ) throws Exception {


		Tmp_FASTA_sequence_DTO result = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tmp_fasta_sequence WHERE id = ?";
		
		//CREATE TABLE  nrseq_fasta_importer.tmp_fasta_sequence (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  header_line_number INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,
		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new Tmp_FASTA_sequence_DTO();
				result.setId( id );
				result.setFastaImportTrackingId( rs.getInt( "fasta_import_tracking_id" ) );
				result.setHeaderLineNumber( rs.getInt( "header_line_number" ) );
				result.setSequence( rs.getString( "sequence" ) );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select Tmp_FASTA_sequence_DTO, id: " + id + ", sql: " + sql;
			
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
			
			
		}
		
		return result;
	}
	
	


	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( Tmp_FASTA_sequence_DTO item  ) throws Exception {
		
		
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
	 * @param dbConnection
	 * @throws Exception
	 */
	public void save( Tmp_FASTA_sequence_DTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		//CREATE TABLE  nrseq_fasta_importer.tmp_fasta_sequence (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  header_line_number INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,


		final String sql = "INSERT INTO tmp_fasta_sequence ( fasta_import_tracking_id, header_line_number, sequence )" +
				" VALUES ( ?, ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getFastaImportTrackingId() );
			counter++;
			pstmt.setInt( counter, item.getHeaderLineNumber() );
			counter++;
			pstmt.setString( counter, item.getSequence() );
			
			pstmt.executeUpdate();
			

			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert FASTAImportTrackingDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert Tmp_FASTA_sequence_DTO, sql: " + sql;
			
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
			

		}
		
	}
	
	
	

	/**
	 * @throws Exception
	 */
	public void truncate(  ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			truncate( dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}


	/**
	 * @param dbConnection
	 * @throws Exception
	 */
	public void truncate( Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;

		//CREATE TABLE  nrseq_fasta_importer.tmp_fasta_sequence (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  header_line_number INT UNSIGNED NOT NULL,
//		  sequence MEDIUMTEXT NOT NULL,


		final String sql = "TRUNCATE TABLE tmp_fasta_sequence";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql );
			
			pstmt.executeUpdate();
			

			
		} catch ( Exception e ) {
			
			String msg = "Failed to TRUNCATE, sql: " + sql;
			
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
