package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblDatabaseDTO;


/**
 * table YRC_NRSEQ.tblDatabase
 *
 */
public class YRC_NRSEQ_tblDatabaseDAO {

	private static final Logger log = Logger.getLogger(YRC_NRSEQ_tblDatabaseDAO.class);

	//  private constructor
	private YRC_NRSEQ_tblDatabaseDAO() { }

	/**
	 * @return newly created instance
	 */
	public static YRC_NRSEQ_tblDatabaseDAO getInstance() { 
		return new YRC_NRSEQ_tblDatabaseDAO(); 
	}

	
	
	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblDatabaseDTO getForId( int id ) throws Exception {


		YRC_NRSEQ_tblDatabaseDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tblDatabase WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblDatabaseDTO, id: " + id + ", sql: " + sql;
			
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
	 * @param name
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblDatabaseDTO getForName( String name ) throws Exception {


		YRC_NRSEQ_tblDatabaseDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tblDatabase WHERE name = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, name );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblDatabaseDTO, name: " + name + ", sql: " + sql;
			
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
	 * @param name
	 * @return 
	 * @throws Exception
	 */
	public Integer getIdForName( String name ) throws Exception {


		Integer result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT id FROM tblDatabase WHERE name = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setString( 1, name );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = rs.getInt( "id" );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select id, name: " + name + ", sql: " + sql;
			
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
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private YRC_NRSEQ_tblDatabaseDTO populateResultObject(ResultSet rs) throws SQLException {
		
		YRC_NRSEQ_tblDatabaseDTO returnItem = new YRC_NRSEQ_tblDatabaseDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setName( rs.getString( "name" ) );
		returnItem.setDescription( rs.getString( "description" ) );
		
		return returnItem;
	}
	
	

	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( YRC_NRSEQ_tblDatabaseDTO item ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );

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
	public void save( YRC_NRSEQ_tblDatabaseDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		final String sql = "INSERT INTO tblDatabase (name, description)" +
				" VALUES ( ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, item.getName() );
			counter++;
			pstmt.setString( counter, item.getDescription() );
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert YRC_NRSEQ_tblDatabaseDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert YRC_NRSEQ_tblDatabaseDTO, sql: " + sql;
			
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
	 * @param id
	 * @throws Exception
	 */
	public void deleteForId( int id ) throws Exception {
		

		Connection dbConnection = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "DELETE FROM tblDatabase WHERE id = ?";
		
		try {
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );

			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, id );
			
			pstmt.executeUpdate();
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to delete, sql: " + sql;
			
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
