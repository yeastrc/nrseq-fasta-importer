package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.GeneralImportErrorDTO;

/**
 * 
 * table general_import_error
 */
public class GeneralImportErrorDAO {

	private static final Logger log = Logger.getLogger(GeneralImportErrorDAO.class);
	

	//  private constructor
	private GeneralImportErrorDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static GeneralImportErrorDAO getInstance() { 
		return new GeneralImportErrorDAO(); 
	}
	
	
	/**
	 * @return 
	 * @throws Exception
	 */
	public List<GeneralImportErrorDTO> getAll(  ) throws Exception {


		List<GeneralImportErrorDTO>  returnList = new ArrayList<GeneralImportErrorDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM general_import_error  ORDER BY id";
		

		//CREATE TABLE general_import_error (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  message VARCHAR(3000) NULL,

	
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				GeneralImportErrorDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select GeneralImportErrorDTO, sql: " + sql;
			
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
		
		return returnList;
	}
	
	

	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public GeneralImportErrorDTO getForId( int id ) throws Exception {


		GeneralImportErrorDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM general_import_error WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select GeneralImportErrorDTO, id: " + id + ", sql: " + sql;
			
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
	 * @param fastaImportTrackingId
	 * @return
	 * @throws Exception
	 */
	public List<GeneralImportErrorDTO> getForFastaImportTrackingId( int fastaImportTrackingId ) throws Exception {

		return getForFastaImportTrackingId( fastaImportTrackingId, 0, false /* idGreatThanPopulated */ );
	}
	

	/**
	 * @param fastaImportTrackingId
	 * @return
	 * @throws Exception
	 */
	public List<GeneralImportErrorDTO> getForFastaImportTrackingId_idGreatThan( int fastaImportTrackingId, int idGreatThan ) throws Exception {

		return getForFastaImportTrackingId( fastaImportTrackingId, idGreatThan, true /* idGreatThanPopulated */ );
	}
	
	/**
	 * @param fastaImportTrackingId
	 * @return
	 * @throws Exception
	 */
	private List<GeneralImportErrorDTO> getForFastaImportTrackingId( int fastaImportTrackingId, int idGreatThan, boolean idGreatThanPopulated ) throws Exception {


	
		List<GeneralImportErrorDTO>  returnList = new ArrayList<GeneralImportErrorDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM general_import_error where fasta_import_tracking_id = ? ORDER BY id ";
		
		if ( idGreatThanPopulated ) {
			
			sql = "SELECT * FROM general_import_error where fasta_import_tracking_id = ? AND id > ? ORDER BY id ";
		}

		//CREATE TABLE general_import_error (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  message VARCHAR(3000) NULL,

			
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setInt( 1, fastaImportTrackingId );

			if ( idGreatThanPopulated ) {
				
				pstmt.setInt( 2, idGreatThan );
			}
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				GeneralImportErrorDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select GeneralImportErrorDTO, sql: " + sql;
			
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
		
		return returnList;
	}
	
	

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private GeneralImportErrorDTO populateResultObject(ResultSet rs) throws SQLException {
		
		GeneralImportErrorDTO returnItem = new GeneralImportErrorDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setFastaImportTrackingId( rs.getInt( "fasta_import_tracking_id" ) );

		returnItem.setMessage( rs.getString( "message" ) );


		return returnItem;
	}
	

	//CREATE TABLE general_import_error (
//	  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//	  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//	  message VARCHAR(3000) NULL,



	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( GeneralImportErrorDTO item ) throws Exception {
		
		
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
	public void save( GeneralImportErrorDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;


		//CREATE TABLE general_import_error (
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  message VARCHAR(3000) NULL,
	
			

		final String sql = "INSERT INTO general_import_error "
				+ "(fasta_import_tracking_id, message )" +
				" VALUES ( ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getFastaImportTrackingId());
			counter++;
			pstmt.setString( counter, item.getMessage() );
			
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert GeneralImportErrorDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert GeneralImportErrorDTO, sql: " + sql;
			
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
	
	
	
}
