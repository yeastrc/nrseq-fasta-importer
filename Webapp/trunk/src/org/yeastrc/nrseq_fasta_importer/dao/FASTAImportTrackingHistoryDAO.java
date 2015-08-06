package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;

public class FASTAImportTrackingHistoryDAO {

	private static final Logger log = Logger.getLogger(FASTAImportTrackingHistoryDAO.class);
	

	//  private constructor
	private FASTAImportTrackingHistoryDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static FASTAImportTrackingHistoryDAO getInstance() { 
		return new FASTAImportTrackingHistoryDAO(); 
	}
	
	


	/**
	 * @param status
	 * @param fastaImportTrackingId
	 * @throws Exception
	 */
	public void save( String status, int fastaImportTrackingId  ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			save( status, fastaImportTrackingId, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}

	/**
	 * @param status
	 * @param fastaImportTrackingId
	 * @param dbConnection
	 * @throws Exception
	 */
	public void save( String status, int fastaImportTrackingId, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

//		CREATE TABLE fasta_import_tracking_status_history (
//				  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//				  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//				  status VARCHAR(45) NOT NULL,
//				  status_timestamp TIMESTAMP NOT NULL,


		final String sql = "INSERT INTO fasta_import_tracking_status_history ( fasta_import_tracking_id, status )" +
				" VALUES ( ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, fastaImportTrackingId );
			counter++;
			pstmt.setString( counter, status );
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				int id = rs.getInt( 1 );
			} else {
				
				String msg = "Failed to insert fasta_import_tracking_status_history, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert fasta_import_tracking_status_history, sql: " + sql;
			
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
	 * @param status
	 * @param id
	 * @throws Exception
	 */
	public void updateStatus( String status, int id ) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE project SET status = ?, last_updated_date_time = NOW() WHERE id = ?";

		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			//CREATE TABLE fasta_import_tracking (
//			  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			  filename VARCHAR(512) NOT NULL,
//			  status VARCHAR(45) NOT NULL,
//			  sha1sum VARCHAR(45) NOT NULL,
//			  temp_filename VARCHAR(255) NOT NULL,
//			  upload_date_time TIMESTAMP NOT NULL,
//			  last_updated_date_time TIMESTAMP NULL,
			
			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, status );
			
			counter++;
			pstmt.setInt( counter, id );
			
			pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update status, sql: " + sql;
			
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
		
	}
	
	
	
}
