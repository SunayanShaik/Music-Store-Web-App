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
public class SalesDbDAO {
	
	private DataSource dataSource;  
	private DbUtils dbUtil;
		
	/**
	 *  Use to connect to databases through JDBC as specified in DataSource
	 */
	public SalesDbDAO(DbUtils dbUtil, DataSource ds) throws SQLException {
		dataSource = ds;
		this.dbUtil = dbUtil;
	}

	/**
	*  bring DB back to original state
	*  @throws  SQLException
	**/
	public void initializeDb() throws SQLException {
		Connection connection = dataSource.getConnection();
		dbUtil.clearTable(connection, LINEITEM_TABLE);
		dbUtil.clearTable(connection, INVOICE_TABLE);
		dbUtil.clearTable(connection, USER_TABLE);
		initSysTable(connection);
		connection.close();
	}

	
	/**
	*  Set all the id numbers used in sales db tables back to 1
	*  @throws  SQLException
	**/
	private void initSysTable(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		try { // update the values this database owns
			stmt.execute("update " + SYS_TABLE + " set invoice_id = 1, user_id = 1, lineitem_id = 1"); 
		} finally {
			stmt.close();
		}
	}
	
	// The following transaction control methods are duplicated
	// in the CatalogDbDAO, but need to be part of the particular DB's API,
	// so the duplication is tolerated.
	
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
