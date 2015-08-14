
package org.yeastrc.nrseq_fasta_importer.searchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.yeastrc.nrseq_fasta_importer.db.DBConnectionFactory;


/**
 * 
 *
 */
public class NCBITaxonomySearcher {

	private NCBITaxonomySearcher() { }
	
	public static NCBITaxonomySearcher getInstance() {
		return new NCBITaxonomySearcher();
	}
	
	
	
	/**
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public int getSpeciesByName( String name ) throws Exception {
		int id = 0;
		
		// !!! NOTE: for the YRC's setup this should return a connection to the "yrc" database
		//           since NCBI_Taxonomy is in the "yrc" database.
//		Connection conn = DatabaseHelper.getNcbiTaxConnection(false);
		
		
		//  TODO  TEMP GET value from yrc.NCBI_Taxonomy
		
//		Connection conn = DBConnectionFactory.getConnection( DBConnectionFactory.YRC );
		
		Connection conn = DBConnectionFactory.getConnection( DBConnectionFactory.NRSEQ_FASTA_IMPORTER );
		
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT id FROM NCBI_Taxonomy WHERE name = ?";
		
			stmt = conn.prepareStatement( sql );
			stmt.setString( 1, name );
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt( 1 );
			}

			rs.close(); rs = null;
			stmt.close(); stmt = null;
			conn.close(); conn = null;
			
		} finally {
			
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (Exception e) { ; }
			}

			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				} catch (Exception e) { ; }
			}
			
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (Exception e) { ; }
			}
		}
		
		return id;
	}
	
}
