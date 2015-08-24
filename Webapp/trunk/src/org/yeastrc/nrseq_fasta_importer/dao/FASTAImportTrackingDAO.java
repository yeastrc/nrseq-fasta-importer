package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.constants.ImportStatusContants;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.FASTAImportTrackingDTO;



/**
 * 
 *
 */
public class FASTAImportTrackingDAO {

	private static final Logger log = Logger.getLogger(FASTAImportTrackingDAO.class);
	

	//  private constructor
	private FASTAImportTrackingDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static FASTAImportTrackingDAO getInstance() { 
		return new FASTAImportTrackingDAO(); 
	}
	
	


	/**
	 * @return 
	 * @throws Exception
	 */
	public List<FASTAImportTrackingDTO> getAll(  ) throws Exception {


		List<FASTAImportTrackingDTO>  returnList = new ArrayList<FASTAImportTrackingDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_import_tracking ORDER BY id DESC";
		


		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				FASTAImportTrackingDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAImportTrackingDTO, sql: " + sql;
			
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
	 * @return 
	 * @throws Exception
	 */
	public List<FASTAImportTrackingDTO> getByStatus( List<String> statusList ) throws Exception {


		List<FASTAImportTrackingDTO>  returnList = new ArrayList<FASTAImportTrackingDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		StringBuilder sqlSB = new StringBuilder( 1000 );
		
		sqlSB.append( "SELECT * FROM fasta_import_tracking " );
		
		if ( statusList != null && ! statusList.isEmpty() ) {
			
			sqlSB.append( " WHERE " );

			sqlSB.append( " ( status = ? " );
			
			for ( int counter = 1; counter < statusList.size(); counter++ ) {
				
				sqlSB.append( " OR status = ? " );
			}
			
			sqlSB.append( " ) " );
		}
		
		
		sqlSB.append( " ORDER BY id DESC" );
		
		
		final String sql = sqlSB.toString();
		
	
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			int paramCounter = 0;
			
			if ( statusList != null && ! statusList.isEmpty() ) {
								
				for ( String status : statusList ) {
					
					paramCounter++;
					pstmt.setString( paramCounter, status );
				}
				
			}
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				FASTAImportTrackingDTO returnItem = populateResultObject( rs );
				
				returnList.add(returnItem);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAImportTrackingDTO, sql: " + sql;
			
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
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public List<FASTAImportTrackingDTO> getAllInProgressForFilename( String filename ) throws Exception {

		List<FASTAImportTrackingDTO>  returnList = new ArrayList<FASTAImportTrackingDTO>();
		
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_import_tracking WHERE filename = ? AND ( status = ? OR status = ? OR status = ? OR status = ? OR status = ? OR status = ? OR status = ? ) ORDER BY ID";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			counter++;
			pstmt.setString( counter, filename );
			
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_VALIDATION_STARTED );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_USER_INPUT_REQUIRED );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_QUEUED_FOR_IMPORT );
			counter++;
			pstmt.setString( counter, ImportStatusContants.STATUS_IMPORT_STARTED );
			
			rs = pstmt.executeQuery();
			
			while ( rs.next() ) {

				FASTAImportTrackingDTO result = populateResultObject( rs );
				
				returnList.add(result);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed getAllInProgressIdsForFilename() , sql: " + sql;
			
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
	public FASTAImportTrackingDTO getForId( int id ) throws Exception {


		FASTAImportTrackingDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_import_tracking WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAImportTrackingDTO, id: " + id + ", sql: " + sql;
			
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
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public String getStatusForId( int id ) throws Exception {


		String result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT status FROM fasta_import_tracking WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = rs.getString( "status" );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select status, id: " + id + ", sql: " + sql;
			
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
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public Integer getGetTaxonomyIdsPassNumberForId( int id ) throws Exception {

		Integer result = null;

		Connection dbConnection = null;

		try {

			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			result = getGetTaxonomyIdsPassNumberForId( id, dbConnection );


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
	public Integer getGetTaxonomyIdsPassNumberForId( int id, Connection dbConnection ) throws Exception {

		Integer result = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT get_taxonomy_ids_pass_number FROM fasta_import_tracking WHERE id = ?";
		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = rs.getInt( "get_taxonomy_ids_pass_number" );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select get_taxonomy_ids_pass_number, id: " + id + ", sql: " + sql;
			
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
	 * @return
	 * @throws Exception
	 */
	public FASTAImportTrackingDTO getNextQueued( ) throws Exception {


		FASTAImportTrackingDTO result = null;
		
		Connection dbConnection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_import_tracking WHERE status = ? OR status = ? OR status = ? ORDER BY ID LIMIT 1 FOR UPDATE";
		
		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			dbConnection.setAutoCommit(false);
			
			pstmt = dbConnection.prepareStatement( sql );
			pstmt.setString( 1, ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION );
			pstmt.setString( 2, ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS );
			pstmt.setString( 3, ImportStatusContants.STATUS_QUEUED_FOR_IMPORT );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
			if ( result != null ) {
			
				if ( ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION.equals( result.getStatus() ) ) {
					

					String newStatus = ImportStatusContants.STATUS_VALIDATION_STARTED;

					result.setStatus( newStatus );

					updateStatus( newStatus, result.getId(), dbConnection );
					
				
				} else if ( ImportStatusContants.STATUS_QUEUED_FOR_FIND_TAX_IDS.equals( result.getStatus() ) ) {


					String newStatus = ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED;

					result.setStatus( newStatus );

					updateStatus( newStatus, result.getId(), dbConnection );
					

				} else if ( ImportStatusContants.STATUS_QUEUED_FOR_IMPORT.equals( result.getStatus() ) ) {
					

					String newStatus = ImportStatusContants.STATUS_IMPORT_STARTED;

					result.setStatus( newStatus );

					updateStatus( newStatus, result.getId(), dbConnection );
					
				}

			}

			dbConnection.commit();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select FASTAImportTrackingDTO, sql: " + sql;
			
			log.error( msg, e );
			
			
			if ( dbConnection != null ) {
				
				try {
					dbConnection.rollback();
				} catch (Exception ex) {
					String msg2 = "Failed dbConnection.rollback() in getNextQueued(...)";

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
					String msg = "Failed dbConnection.setAutoCommit(true) in getNextQueued(...)";

					log.error( msg, ex );
				}

				if( dbConnection != null ) {
					try { dbConnection.close(); } catch( Throwable t ) { ; }
					dbConnection = null;
				}

			}
			
		}
		
		return result;
	}
	
	

	/**
	 * @return
	 * @throws Exception
	 */
	public List<FASTAImportTrackingDTO> getAllInProgress( ) throws Exception {


		List<FASTAImportTrackingDTO>  returnList = new ArrayList<FASTAImportTrackingDTO>();
		
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM fasta_import_tracking WHERE status = ? OR status = ? OR status = ? ORDER BY ID LIMIT 1";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, ImportStatusContants.STATUS_VALIDATION_STARTED );
			pstmt.setString( 2, ImportStatusContants.STATUS_FIND_TAX_IDS_STARTED );
			pstmt.setString( 3, ImportStatusContants.STATUS_IMPORT_STARTED );
			
			rs = pstmt.executeQuery();
			
			while ( rs.next() ) {
				
				FASTAImportTrackingDTO result = populateResultObject( rs );
				
				returnList.add(result);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed getAllInProgress() , sql: " + sql;
			
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
	private FASTAImportTrackingDTO populateResultObject(ResultSet rs) throws SQLException {
		
		FASTAImportTrackingDTO returnItem = new FASTAImportTrackingDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setFilename( rs.getString( "filename" ) );
		returnItem.setDescription( rs.getString( "description" ) );
		returnItem.setEmail( rs.getString( "email" ) );
		returnItem.setStatus( rs.getString( "status" ) );
		returnItem.setInsertRequestURL( rs.getString( "insert_request_url" ) );
		returnItem.setSha1sum( rs.getString( "sha1sum" ) );
		returnItem.setFastaEntryCount( rs.getInt( "fasta_entry_count" ) );
		returnItem.setGetTaxonomyIdsPassNumber( rs.getInt( "get_taxonomy_ids_pass_number" ) );
		
		int yrc_nrseq_tblDatabase_id = rs.getInt( "yrc_nrseq_tblDatabase_id" );
		
		if ( ! rs.wasNull() ) {
			returnItem.setYrc_nrseq_tblDatabase_id( yrc_nrseq_tblDatabase_id );
		}
		
		returnItem.setUploadDateTime( rs.getDate( "upload_date_time" ) );
		returnItem.setLastUpdatedDateTime( rs.getDate( "last_updated_date_time" ) );
		
		return returnItem;
	}
	


	//CREATE TABLE fasta_import_tracking (
//			  id INT UNSIGNED NOT NULL ,
//			  filename VARCHAR(512) NOT NULL,
//			  description VARCHAR(500) NULL,
//			  email VARCHAR(255) NULL,
//			  status VARCHAR(45) NOT NULL,
//			  insert_request_url VARCHAR(255) NULL DEFAULT NULL
//			  sha1sum VARCHAR(45) NOT NULL,
//			  fasta_entry_count INT NULL,
//			  get_taxonomy_ids_pass_number INT NOT NULL DEFAULT 0,
//			  yrc_nrseq_tblDatabase_id INT NULL,
//			  upload_date_time TIMESTAMP NOT NULL,
//			  last_updated_date_time TIMESTAMP NULL,
			  
			  

	


	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( FASTAImportTrackingDTO item ) throws Exception {
		
		
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
	public void save( FASTAImportTrackingDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String status = ImportStatusContants.STATUS_QUEUED_FOR_VALIDATION;




		//CREATE TABLE fasta_import_tracking (
//				  id INT UNSIGNED NOT NULL ,
//				  filename VARCHAR(512) NOT NULL,
//				  description VARCHAR(500) NULL,
//				  email VARCHAR(255) NULL,
//				  status VARCHAR(45) NOT NULL,
//				  insert_request_url VARCHAR(255) NULL DEFAULT NULL
//				  sha1sum VARCHAR(45) NOT NULL,
//				  fasta_entry_count INT NULL,
//				  get_taxonomy_ids_pass_number INT NOT NULL DEFAULT 0,
//				  yrc_nrseq_tblDatabase_id INT NULL,
//				  upload_date_time TIMESTAMP NOT NULL,
//				  last_updated_date_time TIMESTAMP NULL,
				  
			


		final String sql = "INSERT INTO fasta_import_tracking ( id, filename, description, email, status, insert_request_url, sha1sum, last_updated_date_time )" +
				" VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW() )";

		try {
			
			
//			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getId() );
			counter++;
			pstmt.setString( counter, item.getFilename() );
			counter++;
			pstmt.setString( counter, item.getDescription() );
			counter++;
			pstmt.setString( counter, item.getEmail() );
			counter++;
			pstmt.setString( counter, status );
			counter++;
			pstmt.setString( counter, item.getInsertRequestURL() );
			counter++;
			pstmt.setString( counter, item.getSha1sum() );
			
			pstmt.executeUpdate();
			
//			rs = pstmt.getGeneratedKeys();
//
//			if( rs.next() ) {
//				item.setId( rs.getInt( 1 ) );
//			} else {
//				
//				String msg = "Failed to insert FASTAImportTrackingDTO, generated key not found.";
//				
//				log.error( msg );
//				
//				throw new Exception( msg );
//			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert FASTAImportTrackingDTO, sql: " + sql;
			
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
		

		FASTAImportTrackingHistoryDAO.getInstance().save( status, item.getId() /* fastaImportTrackingId */ );		
		
	}
	


	/**
	 * @param status
	 * @param id
	 * @return incremented get_taxonomy_ids_pass_number
	 * @throws Exception
	 */
	public Integer updateStatusIncrementGetTaxonomyIdsPassNumber( String status, int id ) throws Exception {
		
		Integer getTaxonomyIdsPassNumber = null;
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			updateStatus( status, id, dbConnection );
			
			incrementGetTaxonomyIdsPassNumber( id, dbConnection );
			
			getTaxonomyIdsPassNumber = getGetTaxonomyIdsPassNumberForId( id, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
		return getTaxonomyIdsPassNumber;
	}
	
	

	/**
	 * @param status
	 * @param id
	 * @throws Exception
	 */
	public void updateStatus( String status, int id ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			updateStatus( status, id, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}
	
	
	/**
	 * @param status
	 * @param id
	 * @throws Exception
	 */
	public void updateStatus( String status, int id, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_import_tracking SET status = ?, last_updated_date_time = NOW() WHERE id = ?";

		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			
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
			
			
		}
		
		FASTAImportTrackingHistoryDAO.getInstance().save( status, id /* fastaImportTrackingId */, dbConnection );		
	}
	
	

	/**
	 * @param id
	 * @param dbConnection
	 * @throws Exception
	 */
	public void incrementGetTaxonomyIdsPassNumber( int id, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_import_tracking SET get_taxonomy_ids_pass_number = get_taxonomy_ids_pass_number + 1 WHERE id = ?";

		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, id );
			
			pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to increment get_taxonomy_ids_pass_number, sql: " + sql;
			
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
	 * @param fastaEntryCount
	 * @param id
	 * @throws Exception
	 */
	public void updateFastaEntryCount( int fastaEntryCount, int id ) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_import_tracking SET fasta_entry_count = ? WHERE id = ?";

		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, fastaEntryCount );
			
			counter++;
			pstmt.setInt( counter, id );
			
			pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update fasta_entry_count, sql: " + sql;
			
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
		
	

	/**
	 * @param yrc_nrseq_tblDatabase_id
	 * @param id
	 * @throws Exception
	 */
	public void update_yrc_nrseq_tblDatabase_id( int yrc_nrseq_tblDatabase_id, int id ) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE fasta_import_tracking SET yrc_nrseq_tblDatabase_id = ? WHERE id = ?";

		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, yrc_nrseq_tblDatabase_id );
			
			counter++;
			pstmt.setInt( counter, id );
			
			pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update yrc_nrseq_tblDatabase_id, sql: " + sql;
			
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
