package cs636.music.dao;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static cs636.music.dao.DBConstants.*;

/**
 * Database connection and initialization.
 * Implemented singleton on this class.
 * 
 */
public class CatalogDbDAO {
	
	private DataSource dataSource;  
	private DbUtils dbUtil;
		
	/**
	 *  Use to connect to databases through JDBC as specified in DataSource
	 */
	public CatalogDbDAO(DbUtils dbUtil, DataSource ds) throws SQLException {
		dataSource = ds;
		this.dbUtil = dbUtil;
	}

	/**
	*  bring DB back to original state
	*  @throws  SQLException
	**/
	public void initializeDb() throws SQLException {
		Connection connection = dataSource.getConnection();
		dbUtil.clearTable(connection, DOWNLOAD_TABLE);
		initSysTable(connection);
		connection.close();
	}
	
	/**
	*  Set all the id numbers used in catalog db tables (only download table) back to 1
	*  @throws  SQLException
	**/
	private void initSysTable(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.execute("update " + SYS_TABLE + " set download_id = 1");  // the value this database owns
		} finally {
			stmt.close();
		}
	}
	
	public Connection startTransaction() throws SQLException {
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	public void commitTransaction(Connection connection) throws SQLException {
		// the commit call can throw, and then the caller needs to rollback
		connection.commit();
		connection.close();
	}

	public void rollbackTransaction(Connection connection) throws SQLException {
		connection.rollback();
		connection.close();
	}
	
	// If the caller has already seen an exception, it probably
	// doesn't want to handle a failing rollback, so it can use this.
	// Then the caller should issue its own exception based on the
	// original exception.
	public void rollbackAfterException(Connection connection) {
		try {
			rollbackTransaction(connection);
		} catch (Exception e) {	
			// discard secondary exception--probably server can't be reached
		}
		try {
			connection.close();
		} catch (Exception e) {
			// discard secondary exception--probably server can't be reached
		}
	}

}
