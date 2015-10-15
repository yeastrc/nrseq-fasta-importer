package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.Tmp_FASTA_header_name_desc_seq_id_DTO;



/**
 * table tmp_fasta_header_name_desc_seq_id
 * 
 * While validating the FASTA file, this table is inserted into
 * 
 * This table is truncated before and after each file is validated.
 * 
 * This table is locked from before validation begins until validation ends
 * 
 */
public class Tmp_FASTA_header_name_desc_seq_id_DAO {

	private static final Logger log = Logger.getLogger(Tmp_FASTA_header_name_desc_seq_id_DAO.class);
	

	//  private constructor
	private Tmp_FASTA_header_name_desc_seq_id_DAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static Tmp_FASTA_header_name_desc_seq_id_DAO getInstance() { 
		return new Tmp_FASTA_header_name_desc_seq_id_DAO(); 
	}
	
	



	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Tmp_FASTA_header_name_desc_seq_id_DTO getForId( int id ) throws Exception {
		

		Tmp_FASTA_header_name_desc_seq_id_DTO result = null;
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			result = getForId( id, dbConnection );

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
	public Tmp_FASTA_header_name_desc_seq_id_DTO getForId( int id, Connection dbConnection ) throws Exception {


		Tmp_FASTA_header_name_desc_seq_id_DTO result = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT * FROM tmp_fasta_header_name_desc_seq_id WHERE id = ?";
		

		//CREATE TABLE tmp_fasta_header_name_desc_seq_id (
//		id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		header_line_number INT UNSIGNED NOT NULL,
//		tmp_fasta_sequence_id_fk INT NOT NULL,
//		header_name_hash_code INT NOT NULL,
//		header_name VARCHAR(1000) NOT NULL,
//		header_description VARCHAR(3000) NULL,

		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );
			pstmt.setInt( 1, id );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new Tmp_FASTA_header_name_desc_seq_id_DTO();
				result.setId( id );
				result.setFastaImportTrackingId( rs.getInt( "fasta_import_tracking_id" ) );
				result.setHeaderLineNumber( rs.getInt( "header_line_number" ) );
				result.setTmpSequenceId( rs.getInt( "tmp_fasta_sequence_id_fk" ) );
				result.setHeaderName( rs.getString( "header_name" ) );
				result.setHeaderDescription( rs.getString( "header_description" ) );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select Tmp_FASTA_header_name_desc_seq_id_DTO, id: " + id + ", sql: " + sql;
			
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
	 * @param fastaImportTrackingId
	 * @param headerName
	 * @return
	 * @throws Exception
	 */
	public List<Tmp_FASTA_header_name_desc_seq_id_DTO> getAllForFastaImportTrackingIdAndHeaderName( int fastaImportTrackingId, String headerName ) throws Exception {
		

		List<Tmp_FASTA_header_name_desc_seq_id_DTO> resultList = null;
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			resultList = getAllForFastaImportTrackingIdAndHeaderName( fastaImportTrackingId, headerName, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
		return resultList;
	}


	/**
	 * @param fastaImportTrackingId
	 * @param headerName
	 * @param dbConnection
	 * @return
	 * @throws Exception
	 */
	public List<Tmp_FASTA_header_name_desc_seq_id_DTO> getAllForFastaImportTrackingIdAndHeaderName( int fastaImportTrackingId, String headerName, Connection dbConnection ) throws Exception {


		List<Tmp_FASTA_header_name_desc_seq_id_DTO> resultList = new ArrayList<>();
		
		int headerNameHashCode = headerName.hashCode();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT id, header_line_number, header_description, tmp_fasta_sequence_id_fk FROM tmp_fasta_header_name_desc_seq_id WHERE fasta_import_tracking_id = ? AND header_name_hash_code = ? AND header_name = ?";
		

		//CREATE TABLE tmp_fasta_header_name_desc_seq_id (
//		id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		header_line_number INT UNSIGNED NOT NULL,
//		tmp_fasta_sequence_id_fk INT NOT NULL,
//		header_name_hash_code INT NOT NULL,
//		header_name VARCHAR(1000) NOT NULL,
//		header_description VARCHAR(3000) NULL,

		
		try {
			
			pstmt = dbConnection.prepareStatement( sql );

			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, fastaImportTrackingId );
			counter++;
			pstmt.setInt( counter, headerNameHashCode );
			counter++;
			pstmt.setString( counter, headerName );

			
			rs = pstmt.executeQuery();
			
			while ( rs.next() ) {
				
				Tmp_FASTA_header_name_desc_seq_id_DTO result = new Tmp_FASTA_header_name_desc_seq_id_DTO();
				
				result.setId( rs.getInt( "id" ) );
				result.setFastaImportTrackingId( fastaImportTrackingId );
				result.setHeaderLineNumber( rs.getInt( "header_line_number" ) );
				result.setTmpSequenceId( rs.getInt( "tmp_fasta_sequence_id_fk" ) );
				result.setHeaderName( headerName );
				result.setHeaderDescription( rs.getString( "header_description" ) );
				
				resultList.add(result);
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select Tmp_FASTA_header_name_desc_seq_id_DTO, headerName: " + headerName + ", sql: " + sql;
			
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
		
		return resultList;
	}
	
	

	


	/**
	 * @param item
	 * @throws Exception
	 */
	public void save( Tmp_FASTA_header_name_desc_seq_id_DTO item  ) throws Exception {
		
		
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
	 * @param dbConnection
	 * @throws Exception
	 */
	public void save( Tmp_FASTA_header_name_desc_seq_id_DTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		//CREATE TABLE tmp_fasta_header_name_desc_seq_id (
//		id INT UNSIGNED NOT NULL AUTO_INCREMENT,
//		fasta_import_tracking_id INT UNSIGNED NOT NULL,
//		header_line_number INT UNSIGNED NOT NULL,
//		tmp_fasta_sequence_id_fk INT NOT NULL,
//		header_name_hash_code INT NOT NULL,
//		header_name VARCHAR(1000) NOT NULL,
//		header_description VARCHAR(3000) NULL,



		final String sql = "INSERT INTO tmp_fasta_header_name_desc_seq_id "
				+ " ( fasta_import_tracking_id, header_line_number, tmp_fasta_sequence_id_fk, header_name_hash_code, header_name, header_description )" 
				+ " VALUES ( ?, ?, ?, ?, ?, ? )";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getFastaImportTrackingId() );
			counter++;
			pstmt.setInt( counter, item.getHeaderLineNumber() );
			counter++;
			pstmt.setInt( counter, item.getTmpSequenceId() );
			counter++;
			pstmt.setInt( counter, item.getHeaderNameHashCode() );
			counter++;
			pstmt.setString( counter, item.getHeaderName() );
			counter++;
			pstmt.setString( counter, item.getHeaderDescription() );
			
			pstmt.executeUpdate();
			

			rs = pstmt.getGeneratedKeys();

			if( rs.next() ) {
				item.setId( rs.getInt( 1 ) );
			} else {
				
				String msg = "Failed to insert FASTAImportTrackingDTO, generated key not found.";
				
				log.error( msg );
				
				throw new Exception( msg );
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert Tmp_FASTA_header_name_desc_seq_id_DTO, item: " + item + "\n sql: " + sql;
			
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
	 * @throws Exception
	 */
	public void truncate(  ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			truncate( dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}


	/**
	 * @param dbConnection
	 * @throws Exception
	 */
	public void truncate( Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;

		final String sql = "TRUNCATE TABLE tmp_fasta_header_name_desc_seq_id";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql );
			
			pstmt.executeUpdate();
			

			
		} catch ( Exception e ) {
			
			String msg = "Failed to TRUNCATE, sql: " + sql;
			
			log.error( msg, e );
			
			throw e;
			
		} finally {
			
			// be sure database handles are closed
			
			
			if( pstmt != null ) {
				try { pstmt.close(); } catch( Throwable t ) { ; }
				pstmt = null;
			}
			

		}
		
	}
	
		
}
