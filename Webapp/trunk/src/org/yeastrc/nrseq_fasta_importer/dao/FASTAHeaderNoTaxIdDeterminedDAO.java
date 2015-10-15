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
import org.yeastrc.nrseq_fasta_importer.dto.FASTAHeaderNoTaxIdDeterminedDTO;
import org.yeastrc.nrseq_fasta_importer.objects.UserProvidedTaxonomyId;

/**
 * 
 * table fasta_header_no_tax_id_determined
 */
public class FASTAHeaderNoTaxIdDeterminedDAO {

	private static final Logger log = Logger.getLogger(FASTAHeaderNoTaxIdDeterminedDAO.class);
	

	//  private constructor
	private FASTAHeaderNoTaxIdDeterminedDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static FASTAHeaderNoTaxIdDeterminedDAO getInstance() { 
		return new FASTAHeaderNoTaxIdDeterminedDAO(); 
	}
	
	


	/**
	 * @return 
	 * @throws Exception
	 */
	public List<FASTAHeaderNoTaxIdDeterminedDTO> getAll(  ) throws Exception {


		List<FASTAHeaderNoTaxIdDeterminedDTO>  returnList = new ArrayList<FASTAHeaderNoTaxIdDeterminedDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_header_no_tax_id_determined  ORDER BY id";
		
//		  CREATE TABLE fasta_header_no_tax_id_determined (
		//
//			  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//			  header_name VARCHAR(600) NOT NULL,
//			  header_description VARCHAR(3000) NULL,
//			  header_line VARCHAR(6000) NULL,
//			  header_line_number INT NULL,
//			  message VARCHAR(3000) NULL,
//			  user_assigned_tax_id INT NULL,
			
			
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				FASTAHeaderNoTaxIdDeterminedDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAHeaderNoTaxIdDeterminedDTO, sql: " + sql;
			
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
	public FASTAHeaderNoTaxIdDeterminedDTO getForId( int id ) throws Exception {


		FASTAHeaderNoTaxIdDeterminedDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_header_no_tax_id_determined WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAHeaderNoTaxIdDeterminedDTO, id: " + id + ", sql: " + sql;
			
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
	public List<FASTAHeaderNoTaxIdDeterminedDTO> getForFastaImportTrackingId( int fastaImportTrackingId ) throws Exception {

		return getForFastaImportTrackingId( fastaImportTrackingId, 0, false /* idGreatThanPopulated */ );
	}
	

	/**
	 * @param fastaImportTrackingId
	 * @return
	 * @throws Exception
	 */
	public List<FASTAHeaderNoTaxIdDeterminedDTO> getForFastaImportTrackingId_idGreatThan( int fastaImportTrackingId, int idGreatThan ) throws Exception {

		return getForFastaImportTrackingId( fastaImportTrackingId, idGreatThan, true /* idGreatThanPopulated */ );
	}
	
	/**
	 * @param fastaImportTrackingId
	 * @return
	 * @throws Exception
	 */
	private List<FASTAHeaderNoTaxIdDeterminedDTO> getForFastaImportTrackingId( int fastaImportTrackingId, int idGreatThan, boolean idGreatThanPopulated ) throws Exception {


	
		List<FASTAHeaderNoTaxIdDeterminedDTO>  returnList = new ArrayList<FASTAHeaderNoTaxIdDeterminedDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM fasta_header_no_tax_id_determined where fasta_import_tracking_id = ? ORDER BY id ";
		
		if ( idGreatThanPopulated ) {
			
			sql = "SELECT * FROM fasta_header_no_tax_id_determined where fasta_import_tracking_id = ? AND id > ? ORDER BY id ";
		}
		
//		  CREATE TABLE fasta_header_no_tax_id_determined (
		//
//			  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//			  header_name VARCHAR(600) NOT NULL,
//			  header_description VARCHAR(3000) NULL,
//			  header_line VARCHAR(6000) NULL,
//			  header_line_number INT NULL,
//			  message VARCHAR(3000) NULL,
//			  user_assigned_tax_id INT NULL,
			
			
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setInt( 1, fastaImportTrackingId );

			if ( idGreatThanPopulated ) {
				
				pstmt.setInt( 2, idGreatThan );
			}
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				FASTAHeaderNoTaxIdDeterminedDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAHeaderNoTaxIdDeterminedDTO, sql: " + sql;
			
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
	 * @param fastaImportTrackingId
	 * @param headerName
	 * @param headerLineNumber
	 * @return
	 * @throws Exception
	 */
	public List<FASTAHeaderNoTaxIdDeterminedDTO> getForFastaImportTrackingIdHeaderName( int fastaImportTrackingId, String headerName, int headerLineNumber ) throws Exception {


	
		List<FASTAHeaderNoTaxIdDeterminedDTO>  returnList = new ArrayList<FASTAHeaderNoTaxIdDeterminedDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM fasta_header_no_tax_id_determined where fasta_import_tracking_id = ? AND header_name = ? AND header_line_number = ? ORDER BY id ";
		
		
//		  CREATE TABLE fasta_header_no_tax_id_determined (
		//
//			  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//			  header_name VARCHAR(600) NOT NULL,
//			  header_description VARCHAR(3000) NULL,
//			  header_line VARCHAR(6000) NULL,
//			  header_line_number INT NULL,
//			  message VARCHAR(3000) NULL,
//			  user_assigned_tax_id INT NULL,
			
			
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setInt( 1, fastaImportTrackingId );
			pstmt.setString( 2, headerName );
			pstmt.setInt( 3, headerLineNumber );

			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				FASTAHeaderNoTaxIdDeterminedDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAHeaderNoTaxIdDeterminedDTO, sql: " + sql;
			
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
	private FASTAHeaderNoTaxIdDeterminedDTO populateResultObject(ResultSet rs) throws SQLException {
		
		FASTAHeaderNoTaxIdDeterminedDTO returnItem = new FASTAHeaderNoTaxIdDeterminedDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setFastaImportTrackingId( rs.getInt( "fasta_import_tracking_id" ) );
		returnItem.setGetTaxonomyIdsPassNumber( rs.getInt( "get_taxonomy_ids_pass_number" ) );
		returnItem.setHeaderName( rs.getString( "header_name" ) );
		returnItem.setHeaderDescription( rs.getString( "header_description" ) );
		returnItem.setHeaderLine( rs.getString( "header_line" ) );
		returnItem.setHeaderLineNumber( rs.getInt( "header_line_number" ) );

		returnItem.setMessage( rs.getString( "message" ) );
		
		Integer userAssignedTaxId = null;
		
		int userAssignedTaxIdFromDB = rs.getInt( "user_assigned_tax_id" );
		if ( ! rs.wasNull() ) {
			
			userAssignedTaxId = userAssignedTaxIdFromDB;
		}

		returnItem.setUserAssignedTaxId( userAssignedTaxId );

		return returnItem;
	}
	

//	  CREATE TABLE fasta_header_no_tax_id_determined (
	//
//		  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		  get_taxonomy_ids_pass_number INT NOT NULL,
//		  header_name VARCHAR(600) NOT NULL,
//		  header_description VARCHAR(3000) NULL,
//		  header_line VARCHAR(6000) NULL,
//		  header_line_number INT NULL,
//		  message VARCHAR(3000) NULL,
//		  user_assigned_tax_id INT NULL,
		


	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( FASTAHeaderNoTaxIdDeterminedDTO item ) throws Exception {
		
		
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
	public void save( FASTAHeaderNoTaxIdDeterminedDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

//		  CREATE TABLE fasta_header_no_tax_id_determined (
		//
//			  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//			  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//			  get_taxonomy_ids_pass_number INT NOT NULL,
//			  header_name VARCHAR(600) NOT NULL,
//			  header_description VARCHAR(3000) NULL,
//			  header_line VARCHAR(6000) NULL,
//			  header_line_number INT NULL,
//			  message VARCHAR(3000) NULL,
//			  user_assigned_tax_id INT NULL,
			
			
			

		final String sql = "INSERT INTO fasta_header_no_tax_id_determined "
				+ "(fasta_import_tracking_id, get_taxonomy_ids_pass_number, header_name, header_description, header_line, header_line_number, message, user_assigned_tax_id )" +
				" VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getFastaImportTrackingId() );
			counter++;
			pstmt.setInt( counter, item.getGetTaxonomyIdsPassNumber() );
			counter++;
			pstmt.setString( counter, item.getHeaderName() );
			counter++;
			pstmt.setString( counter, item.getHeaderDescription() );
			counter++;
			pstmt.setString( counter, item.getHeaderLine() );
			counter++;
			pstmt.setInt( counter, item.getHeaderLineNumber() );
			counter++;
			pstmt.setString( counter, item.getMessage() );
			
			
			counter++;
			
			if ( item.getUserAssignedTaxId() != null ) {
				pstmt.setInt( counter, item.getUserAssignedTaxId() );
			} else {
				pstmt.setNull( counter, java.sql.Types.INTEGER );
			}
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert FASTAHeaderNoTaxIdDeterminedDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert FASTAHeaderNoTaxIdDeterminedDTO,"
					+ "FASTAHeaderNoTaxIdDeterminedDTO item: " + item.toString()
					+ "\n sql: " + sql;
			
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
	 * @param userAssignedTaxId
	 * @param id
	 * @throws Exception
	 */
	public void update_userAssignedTaxId( UserProvidedTaxonomyId userProvidedTaxonomyId ) throws Exception {
		
		Connection dbConnection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_header_no_tax_id_determined SET user_assigned_tax_id = ? WHERE id = ?";

		
		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			

//			  CREATE TABLE fasta_header_no_tax_id_determined (
			//
//				  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//				  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//				  get_taxonomy_ids_pass_number INT NOT NULL,
//				  header_name VARCHAR(600) NOT NULL,
//				  header_description VARCHAR(3000) NULL,
//				  header_line VARCHAR(6000) NULL,
//				  header_line_number INT NULL,
//				  message VARCHAR(3000) NULL,
//				  user_assigned_tax_id INT NULL,
				
				
			
			pstmt = dbConnection.prepareStatement( sql );

			int counter = 0;

			counter++;

			if ( userProvidedTaxonomyId.getTaxonomyId() != null ) {
				pstmt.setInt( counter, userProvidedTaxonomyId.getTaxonomyId() );
			} else {
				pstmt.setNull( counter, java.sql.Types.INTEGER );
			}
			
			
			counter++;
			pstmt.setInt( counter, userProvidedTaxonomyId.getNoTaxonomyIdRecordId() );
			
			
			pstmt.executeUpdate();

			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update user_assigned_tax_id, sql: " + sql;
			
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


			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
		}
	}
	
	
	
	
	/**
	 * @param userProvidedTaxonomyIds
	 * @param id
	 * @throws Exception
	 */
	public void update_userAssignedTaxIdForList( List<UserProvidedTaxonomyId> userProvidedTaxonomyIds ) throws Exception {
		
		Connection dbConnection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_header_no_tax_id_determined SET user_assigned_tax_id = ? WHERE id = ?";

		
		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			

			dbConnection.setAutoCommit(false);
			
//			  CREATE TABLE fasta_header_no_tax_id_determined (
			//
//				  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//				  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//				  get_taxonomy_ids_pass_number INT NOT NULL,
//				  header_name VARCHAR(600) NOT NULL,
//				  header_description VARCHAR(3000) NULL,
//				  header_line VARCHAR(6000) NULL,
//				  header_line_number INT NULL,
//				  message VARCHAR(3000) NULL,
//				  user_assigned_tax_id INT NULL,
				
				
			
			pstmt = dbConnection.prepareStatement( sql );
			
			for ( UserProvidedTaxonomyId item : userProvidedTaxonomyIds ) {
			
			
				int counter = 0;


				counter++;

				if ( item.getTaxonomyId() != null ) {
					pstmt.setInt( counter, item.getTaxonomyId() );
				} else {
					pstmt.setNull( counter, java.sql.Types.INTEGER );
				}
				
				counter++;
				pstmt.setInt( counter, item.getNoTaxonomyIdRecordId() );

				pstmt.executeUpdate();

			}

			dbConnection.commit();
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update user_assigned_tax_id, sql: " + sql;
			
			log.error( msg, e );
			
			
			if ( dbConnection != null ) {
				
				try {
					dbConnection.rollback();
				} catch (Exception ex) {
					String msg2 = "Failed dbConnection.rollback() in update_userAssignedTaxId(...)";

					log.error( msg2, ex );
				}
			}
			
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


			if( dbConnection != null ) {


				try {
					dbConnection.setAutoCommit(true);  /// reset for next user of connection
				} catch (Exception ex) {
					String msg = "Failed dbConnection.setAutoCommit(true) in update_userAssignedTaxId(...)";

					log.error( msg, ex );
				}

				if( dbConnection != null ) {
					try { dbConnection.close(); } catch( Throwable t ) { ; }
					dbConnection = null;
				}

			}
		}
	}
	
	
	

	/**
	 * @param fastaImportTrackingId
	 * @param getTaxonomyIdsPassNumber
	 * @throws Exception
	 */
	public void deleteFor_fastaImportTrackingId_getTaxonomyIdsPassNumber( int fastaImportTrackingId, int getTaxonomyIdsPassNumber ) throws Exception {
		
		Connection dbConnection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "DELETE FROM fasta_header_no_tax_id_determined WHERE fasta_import_tracking_id = ? AND get_taxonomy_ids_pass_number = ?";

		
		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
//			  CREATE TABLE fasta_header_no_tax_id_determined (
			//
//				  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//				  fasta_import_tracking_id INT UNSIGNED NOT NULL,
//				  get_taxonomy_ids_pass_number INT NOT NULL,
//				  header_name VARCHAR(600) NOT NULL,
//				  header_description VARCHAR(3000) NULL,
//				  header_line VARCHAR(6000) NULL,
//				  header_line_number INT NULL,
//				  message VARCHAR(3000) NULL,
//				  user_assigned_tax_id INT NULL,
				
				
			
			pstmt = dbConnection.prepareStatement( sql );

			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, fastaImportTrackingId );
			
			counter++;
			pstmt.setInt( counter, getTaxonomyIdsPassNumber );
			
			
			pstmt.executeUpdate();

			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to deleteFor_fastaImportTrackingId_getTaxonomyIdsPassNumber, sql: " + sql;
			
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


			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
		}
	}
	
	
}
