package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.YRC_NRSEQ_tblProteinSequenceDTO;

/**
 * table YRC_NRSEQ.tblProteinSequence
 *
 */
public class YRC_NRSEQ_tblProteinSequenceDAO {

	private static final Logger log = Logger.getLogger(YRC_NRSEQ_tblProteinSequenceDAO.class);
	

	//  private constructor
	private YRC_NRSEQ_tblProteinSequenceDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static YRC_NRSEQ_tblProteinSequenceDAO getInstance() { 
		return new YRC_NRSEQ_tblProteinSequenceDAO(); 
	}

	
	/**
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinSequenceDTO getForId( int id ) throws Exception {


		YRC_NRSEQ_tblProteinSequenceDTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT sequence FROM tblProteinSequence WHERE id = ?";
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new YRC_NRSEQ_tblProteinSequenceDTO();
				result.setId( id );
				result.setSequence( rs.getString( "sequence" ) );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblProteinSequenceDTO, id: " + id + ", sql: " + sql;
			
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
	 * If the sequence is in the table, retrieves it, otherwise inserts. Returns the id in the DTO
	 * @param sequence
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinSequenceDTO insertOrRetrieve( String sequence ) throws Exception {
		
		YRC_NRSEQ_tblProteinSequenceDTO result = null;
		
		Connection dbConnection = null;
		
		try {

			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );
			
			try {


				lockSequenceTableForWrite( dbConnection );

				result = getForSequence( sequence, dbConnection );

				if ( result == null ) {

					result = new YRC_NRSEQ_tblProteinSequenceDTO();

					result.setSequence( sequence );

					save( result, dbConnection );
				}
			} finally {
				

				unlockAllTable( dbConnection );
			}

		} finally {


			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}

		}
		
		return result;
	}



	private static String lockSequenceTableForWriteSQL = "LOCK TABLES tblProteinSequence WRITE";



	/**
	 * Lock sequence table for write
	 *
	 * All tables read or written to have to be locked with a single "LOCK TABLES" statement
	 * 
	 * @throws Exception
	 */
	public void lockSequenceTableForWrite( Connection connection ) throws Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = connection.prepareStatement( lockSequenceTableForWriteSQL );

			pstmt.executeUpdate();

		} catch (Exception sqlEx) {

			log.error("lockSequenceTableForWrite: Exception '" + sqlEx.toString() + ".\nSQL = " + lockSequenceTableForWriteSQL , sqlEx);

			throw sqlEx;

		} finally {

			if (pstmt != null) {

				try {

					pstmt.close();

				} catch (SQLException ex) {

					// ignore

				}

			}

		}

	}


	private static String unlockAllTableSQL = "UNLOCK TABLES";

	/**

	 * Unlock All Tables

	 * @throws Exception

	 */

	public void unlockAllTable( Connection connection ) throws Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = connection.prepareStatement( unlockAllTableSQL );

			pstmt.executeUpdate();

		} catch (Exception sqlEx) {

			log.error("unlockAllTable: Exception '" + sqlEx.toString() + ".\nSQL = " + unlockAllTableSQL , sqlEx);

			throw sqlEx;

		} finally {

			if (pstmt != null) {

				try {

					pstmt.close();

				} catch (SQLException ex) {

					// ignore

				}

			}


		}

	}




	/**
	 * @param sequence
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinSequenceDTO getForSequence( String sequence ) throws Exception {
		
		YRC_NRSEQ_tblProteinSequenceDTO result = null;
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.YRC_NRSEQ );

			result = getForSequence( sequence, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
		return result;
	}

	/**
	 * @param sequence
	 * @return 
	 * @throws Exception
	 */
	public YRC_NRSEQ_tblProteinSequenceDTO getForSequence( String sequence, Connection dbConnection ) throws Exception {


		YRC_NRSEQ_tblProteinSequenceDTO result = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT id FROM tblProteinSequence WHERE sequence = ?";
		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			pstmt.setString( 1, sequence );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new YRC_NRSEQ_tblProteinSequenceDTO();
				result.setId( rs.getInt( "id" ) );
				result.setSequence( sequence );
				
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select YRC_NRSEQ_tblProteinSequenceDTO, name: " + sequence + ", sql: " + sql;
			
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
	public void save( YRC_NRSEQ_tblProteinSequenceDTO item ) throws Exception {
		
		
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
	public void save( YRC_NRSEQ_tblProteinSequenceDTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		final String sql = "INSERT INTO tblProteinSequence (sequence)" +
				" VALUES ( ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setString( counter, item.getSequence() );
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert YRC_NRSEQ_tblProteinSequenceDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert YRC_NRSEQ_tblProteinSequenceDTO, sql: " + sql;
			
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
