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
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinDatabaseDTO;


/**
 * table YRC_NRSEQ.tblProteinDatabase
 *
 */
public class YRC_NRSEQ_tblProteinDatabaseDAO {

	private static final Logger log = Logger.getLogger(YRC_NRSEQ_tblProteinDatabaseDAO.class);

	//  private constructor
	private YRC_NRSEQ_tblProteinDatabaseDAO() { }

	/**
	 * @return newly created instance
	 */
	public static YRC_NRSEQ_tblProteinDatabaseDAO getInstance() { 
		return new YRC_NRSEQ_tblProteinDatabaseDAO(); 
	}

	
	
	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinDatabaseDTO getForId( int id ) throws Exception {


		YRC_NRSEQ_tblProteinDatabaseDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tblProteinDatabase WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblProteinDatabaseDTO, id: " + id + ", sql: " + sql;
			
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
	private YRC_NRSEQ_tblProteinDatabaseDTO populateResultObject(ResultSet rs) throws SQLException {
		
		YRC_NRSEQ_tblProteinDatabaseDTO returnItem = new YRC_NRSEQ_tblProteinDatabaseDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setProteinID( rs.getInt( "proteinID" ) );
		returnItem.setDatabaseID( rs.getInt( "databaseID" ) );
		returnItem.setAccessionString( rs.getString( "accessionString" ) );
		returnItem.setDescription( rs.getString( "description" ) );
		returnItem.setUrl( rs.getString( "URL" ) );
		returnItem.setTimestamp( rs.getDate( "timestamp" ) );
		returnItem.setIsCurrent( rs.getString( "isCurrent" ) );
		
		return returnItem;
	}




	/**
	 * @param proteinId
	 * @return 
	 * @throws Exception
	 */
	public List<String> getAccessionStringListForProteinId( int proteinId ) throws Exception {


		List<String> resultList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT DISTINCT( accessionString ) AS accessionString FROM tblProteinDatabase WHERE proteinID = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, proteinId );
			
			rs = pstmt.executeQuery();
			
			while ( rs.next() ) {
				
				String result = rs.getString( "accessionString" );
				
				resultList.add( result );

			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select accessionString, proteinId: " + proteinId + ", sql: " + sql;
			
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
		
		return resultList;
	}
	
	
	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( YRC_NRSEQ_tblProteinDatabaseDTO item ) throws Exception {
		
		
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
	public void save( YRC_NRSEQ_tblProteinDatabaseDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		
	
		final String sql = "INSERT INTO tblProteinDatabase (proteinID, databaseID, accessionString, description, URL )" +
				" VALUES ( ?, ?, ?, ?, ? )";



//		CREATE TABLE tblProteinDatabase (
//				  id int(10) unsigned NOT NULL AUTO_INCREMENT,
//				  proteinID int(10) unsigned NOT NULL DEFAULT '0',
//				  databaseID int(10) unsigned NOT NULL DEFAULT '0',
//				  accessionString varchar(500) NOT NULL,
//				  description varchar(2500) DEFAULT NULL,
//				  URL varchar(255) DEFAULT NULL,
//				  timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//				  isCurrent enum('T','F') NOT NULL DEFAULT 'T',


		
		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getProteinID() );
			counter++;
			pstmt.setInt( counter, item.getDatabaseID() );
			counter++;
			pstmt.setString( counter, item.getAccessionString() );
			counter++;
			pstmt.setString( counter, item.getDescription() );
			counter++;
			pstmt.setString( counter, item.getUrl() );
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert YRC_NRSEQ_tblProteinDatabaseDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert YRC_NRSEQ_tblProteinDatabaseDTO, sql: " + sql;
			
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
	 * @param tblProteinDatabase_id
	 * @throws Exception
	 */
	public void deleteFor_tblProteinDatabase_id( int tblProteinDatabase_id ) throws Exception {
		

		Connection dbConnection = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "DELETE FROM tblProteinDatabase WHERE databaseID = ?";
		
		try {
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );

			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, tblProteinDatabase_id );
			
			pstmt.executeUpdate();
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to delete YRC_NRSEQ_tblProteinDatabaseDTO, sql: " + sql;
			
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
