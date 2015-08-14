package org.yeastrc.nrseq_fasta_importer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;
import org.yeastrc.nrseq_fasta_importer.dto.LocalCache_NCBI_Protein_By_GIAccesssionID_DTO;



/**
 * table local_cache_ncbi_protein_by_gi_accession_id
 * 
 * Caches local DB for data from NCBI Protein by gi accession id webservice
 *
 */
public class LocalCache_NCBI_Protein_By_GIAccesssionID_DAO {

	private static final Logger log = Logger.getLogger(LocalCache_NCBI_Protein_By_GIAccesssionID_DAO.class);
	

	//  private constructor
	private LocalCache_NCBI_Protein_By_GIAccesssionID_DAO() { }
	
	/**
	 * @return newly created instance
	 */
	public static LocalCache_NCBI_Protein_By_GIAccesssionID_DAO getInstance() { 
		return new LocalCache_NCBI_Protein_By_GIAccesssionID_DAO(); 
	}
	
	

	/**
	 * @param ncbiAccesssionId
	 * @return 
	 * @throws Exception
	 */
	public LocalCache_NCBI_Protein_By_GIAccesssionID_DTO getTaxonomyIdForAccesssion( int ncbiAccesssionId ) throws Exception {


		LocalCache_NCBI_Protein_By_GIAccesssionID_DTO result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		final String sql = "SELECT taxonomy_id FROM local_cache_ncbi_protein_by_gi_accession_id WHERE gi_accession_id = ?";
		
		//CREATE TABLE local_cache_ncbi_protein_by_gi_accession_id (
//		  gi_accession_id INT NOT NULL,
//		  taxonomy_id INT NULL,

		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setInt( 1, ncbiAccesssionId );
			
			rs = pstmt.executeQuery();
			
			if ( rs.next() ) {
				
				result = new LocalCache_NCBI_Protein_By_GIAccesssionID_DTO();
				result.setNcbiAccesssionId( ncbiAccesssionId );
				result.setTaxonomyId( rs.getInt( "taxonomy_id" ) );
				if ( rs.wasNull() ) {
					result.setTaxonomyId( null ); 
				}
			}
			
		} catch ( Exception e ) {
			
			String msg = "Failed to select LocalCache_NCBI_Protein_By_GIAccesssionID_DTO, ncbiAccesssionId: " + ncbiAccesssionId + ", sql: " + sql;
			
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
	 * @param accession
	 * @param taxonomyId
	 * @throws Exception
	 */
	public void saveOrUpdateOnGiAccessionId( LocalCache_NCBI_Protein_By_GIAccesssionID_DTO item  ) throws Exception {
		
		
		Connection dbConnection = null;

		try {
			
			dbConnection = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );

			saveOrUpdateOnGiAccessionId( item, dbConnection );

		} finally {
			
			if( dbConnection != null ) {
				try { dbConnection.close(); } catch( Throwable t ) { ; }
				dbConnection = null;
			}
			
		}
		
	}

	/**
	 * @param accession
	 * @param taxonomyId
	 * @param dbConnection
	 * @throws Exception
	 */
	public void saveOrUpdateOnGiAccessionId( LocalCache_NCBI_Protein_By_GIAccesssionID_DTO item, Connection dbConnection ) throws Exception {
		
		PreparedStatement pstmt = null;

		//CREATE TABLE local_cache_ncbi_protein_by_gi_accession_id (
//		  gi_accession_id INT NOT NULL,
//		  taxonomy_id INT NULL,


		final String sql = "INSERT INTO local_cache_ncbi_protein_by_gi_accession_id (gi_accession_id, taxonomy_id)" +
				" VALUES ( ?, ? ) ON DUPLICATE KEY UPDATE taxonomy_id = ?";

		try {
			
			
			pstmt = dbConnection.prepareStatement( sql );
			
			int counter = 0;
			
			counter++;
			pstmt.setInt( counter, item.getNcbiAccesssionId() );

			counter++;
			if ( item.getTaxonomyId() != null ) {
				pstmt.setInt( counter, item.getTaxonomyId() );
			} else {
				pstmt.setNull( counter, java.sql.Types.INTEGER );
			}

			counter++;
			if ( item.getTaxonomyId() != null ) {
				pstmt.setInt( counter, item.getTaxonomyId() );
			} else {
				pstmt.setNull( counter, java.sql.Types.INTEGER );
			}
			
			pstmt.executeUpdate();
			

			
		} catch ( Exception e ) {
			
			String msg = "Failed to insert LocalCache_NCBI_Protein_By_GIAccesssionID_DTO, sql: " + sql;
			
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
	
	
	/**
	 * @param item
	 * @throws Exception
	 */
	public void updateTaxonomyId( LocalCache_NCBI_Protein_By_GIAccesssionID_DTO item ) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		final String sql = "UPDATE local_cache_ncbi_protein_by_gi_accession_id SET taxonomy_id = ? WHERE gi_accession_id = ?";


		//CREATE TABLE local_cache_ncbi_protein_by_gi_accession_id (
//		  gi_accession_id INT NOT NULL,
//		  taxonomy_id INT NULL,
		
		try {
			
			conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
			
			pstmt = conn.prepareStatement( sql );
			
			int counter = 0;

			counter++;
			if ( item.getTaxonomyId() != null ) {
				pstmt.setInt( counter, item.getTaxonomyId() );
			} else {
				pstmt.setNull( counter, java.sql.Types.INTEGER );
			}

			counter++;
			pstmt.setInt( counter, item.getNcbiAccesssionId());

			pstmt.executeUpdate();
			
		} catch ( Exception e ) {
			
			String msg = "Failed to update taxonomy_id, sql: " + sql;
			
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
