package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinDTO;


/**
 * table YRC_NRSEQ.tblProtein
 *
 */
public class YRC_NRSEQ_tblProteinDAO {

	private static final Logger log = Logger.getLogger(YRC_NRSEQ_tblProteinDAO.class);

	//  private constructor
	private YRC_NRSEQ_tblProteinDAO() { }

	/**
	 * @return newly created instance
	 */
	public static YRC_NRSEQ_tblProteinDAO getInstance() { 
		return new YRC_NRSEQ_tblProteinDAO(); 
	}

	
	
	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinDTO getForId( int id ) throws Exception {


		YRC_NRSEQ_tblProteinDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tblProtein WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblProteinDTO, id: " + id + ", sql: " + sql;
			
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
	 * @param sequenceID
	 * @param speciesID
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinDTO getForSequenceIdSpeciesId( int sequenceID, int speciesID ) throws Exception {


		YRC_NRSEQ_tblProteinDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tblProtein WHERE sequenceID = ? AND speciesID = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, sequenceID );
			pstmt.setInt( 2, speciesID );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = populateResultObject( rs );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblProteinDTO, sequenceID: " + sequenceID + ", sql: " + sql;
			
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
	private YRC_NRSEQ_tblProteinDTO populateResultObject(ResultSet rs) throws SQLException {
		
		YRC_NRSEQ_tblProteinDTO returnItem = new YRC_NRSEQ_tblProteinDTO();

		returnItem.setId( rs.getInt( "id" ) );
		
		returnItem.setSequenceID( rs.getInt( "sequenceID" ) );
		returnItem.setSpeciesID( rs.getInt( "speciesID" ) );
		
		return returnItem;
	}
	
	

	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( YRC_NRSEQ_tblProteinDTO item ) throws Exception {
		
		
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
	public void save( YRC_NRSEQ_tblProteinDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
//		CREATE TABLE `tblProtein` (
//				  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//				  `sequenceID` int(10) unsigned NOT NULL DEFAULT '0',
//				  `speciesID` int(10) unsigned NOT NULL DEFAULT '0',

	
		final String sql = "INSERT INTO tblProtein (sequenceID, speciesID)" +
				" VALUES ( ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getSequenceID() );
			counter++;
			pstmt.setInt( counter, item.getSpeciesID() );
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert YRC_NRSEQ_tblProteinDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert YRC_NRSEQ_tblProteinDTO, sql: " + sql;
			
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
