package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;

/**
 * Lock validation temp tables
 *
 */
public class LockValidationTempTablesDAO {

	private static final Logger log = Logger.getLogger(LockValidationTempTablesDAO.class);
	

	//  private constructor
	private LockValidationTempTablesDAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static LockValidationTempTablesDAO getInstance() { 
		return new LockValidationTempTablesDAO(); 
	}


	private static String lockTablesForWriteSQL = "LOCK TABLES tmp_fasta_header_name_desc_seq_id WRITE, tmp_fasta_sequence WRITE";



	/**
	 * Lock validation temp tables for write and return the connection
	 *
	 * All tables read or written to have to be locked with a single "LOCK TABLES" statement
	 * 
	 * @throws Exception
	 */
	public Connection lockValidationTempTablesAndReturnDBConnection( ) throws Exception {
		

		Connection dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

		lockValidationTempTables(dbConnection);
		
		return dbConnection;
	}

		
		

	/**
	 * Lock validation temp tables for write
	 *
	 * All tables read or written to have to be locked with a single "LOCK TABLES" statement
	 * 
	 * @throws Exception
	 */
	public void lockValidationTempTables( Connection connection ) throws Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = connection.prepareStatement( lockTablesForWriteSQL );

			pstmt.executeUpdate();

		} catch (Exception sqlEx) {

			log.error("lockValidationTempTables: Exception '" + sqlEx.toString() + ".\nSQL = " + lockTablesForWriteSQL , sqlEx);

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
	 * Unlock All Tables and close the connection
	 * 
	 * @param dbConnection
	 * @throws Exception
	 */
	public void unlockAllTableAndCloseConnection( Connection dbConnection ) throws Exception {

		
		try {
			
			unlockAllTable(dbConnection);
			
		} finally {
			
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
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



	
}
