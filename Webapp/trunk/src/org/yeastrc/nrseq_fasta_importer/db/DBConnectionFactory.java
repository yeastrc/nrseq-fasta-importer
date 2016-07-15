package org.yeastrc.nrseq_fasta_importer.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


/**
 * 
 *
 */
public class DBConnectionFactory {

	private static final Logger log = Logger.getLogger(DBConnectionFactory.class);
	

	public static final String NRSEQ_FASTA_IMPORTER = "NRSEQ_FASTA_IMPORTER";
	public static final String YRC_NRSEQ = "YRC_NRSEQ";

	

	private static final String JNDI_NAME_PREFIX = "java:comp/env/jdbc/";
	
	private static final String JNDI_NAME_SUFFIX_nrseq_fasta_importer = "nrseq_fasta_importer";

	private static final String JNDI_NAME_SUFFIX_nrseq = "nrseq";
	

	private static final String JNDI_NAME_nrseq_fasta_importer = JNDI_NAME_PREFIX + JNDI_NAME_SUFFIX_nrseq_fasta_importer;

	private static final String JNDI_NAME_JNDI_NAME_SUFFIX_nrseq = JNDI_NAME_PREFIX + JNDI_NAME_SUFFIX_nrseq;
	
	

	private static String nrseq_fasta_importer_JNDIName = JNDI_NAME_nrseq_fasta_importer;

	private static String nrseq_JNDIName = JNDI_NAME_JNDI_NAME_SUFFIX_nrseq;

	

	/**
	 * Change nrseq_fasta_importer JNDI name 
	 */
	public static void setNrseq_fasta_importer_JNDIName( String newNrseq_fasta_importer_JNDINameSuffix ) {
		
		nrseq_fasta_importer_JNDIName = JNDI_NAME_PREFIX + newNrseq_fasta_importer_JNDINameSuffix;

		log.warn("INFO:  setNrseq_fasta_importer_JNDIName() called. nrseq_fasta_importer_JNDIName now: " 
				+ nrseq_fasta_importer_JNDIName);
	}
	
	

	/**
	 * Change nrseq JNDI name 
	 */
	public static void setNrseq_JNDIName( String newNrseq_JNDINameSuffix ) {
		
		nrseq_JNDIName = JNDI_NAME_PREFIX + newNrseq_JNDINameSuffix;

		log.warn("INFO:  setNrseq_JNDIName() called. nrseq_JNDIName now: " + nrseq_JNDIName);
	}
	
	
	
		
	/**
	 * Get a connection to the specified database.
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String db) throws Exception {

//		if (dbConnectionFactoryImpl != null) {
//			return dbConnectionFactoryImpl.getConnection(db);
//		}

		return getConnectionWeb(db);
	}

	/**
	 * Get DataSource from JNDI as setup in Application Server and get database
	 * connection from it
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnectionWeb(String db) throws Exception {

		try {
			Context ctx = new InitialContext();
			DataSource ds;
			Connection conn;

			if (db.equals( NRSEQ_FASTA_IMPORTER )) {
				
				ds = (DataSource) ctx.lookup( nrseq_fasta_importer_JNDIName );
				
			} else if (db.equals(YRC_NRSEQ)) {
				
				ds = (DataSource) ctx.lookup( nrseq_JNDIName );
			}

			else {
				throw new SQLException(
						"Invalid database name passed into DBConnectionManager.  db: " + db );
			}

			if (ds != null) {
				conn = ds.getConnection();
				if (conn != null) {
					
//					boolean connectionAutoCommit = conn.getAutoCommit();

					return conn;
				} else {
					throw new SQLException("Got a null connection...");
				}
			}
			

			throw new SQLException("Got a null DataSource...");
			
		} catch (NamingException ne) {
			
			log.error( "ERROR: getting database connection: db: " + db, ne );

			throw new SQLException("Naming exception: " + ne.getMessage(), ne);
		
			
		} catch ( Exception e ) {
			
			log.error( "ERROR: getting database connection: db: " + db, e );
			
			throw e;
		}
	}
	
	
}
