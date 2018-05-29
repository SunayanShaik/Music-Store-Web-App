package cs636.music.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection and initialization.
 * Implemented singleton on this class.
 * 
 */
public class DbUtils {

	public DbUtils() {
	}
	
	/**
	*  Delete all records from the given table
	*  @param tableName table name from which to delete records
	*  @throws  SQLException
	**/
	public void clearTable(Connection connection, String tableName) throws SQLException {
		//System.out.println("Clearing table " + tableName);
		Statement stmt = connection.createStatement();
		try {
			stmt.execute("delete from " + tableName);
		} finally {
			stmt.close();
		}
	}

}
