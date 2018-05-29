package cs636.music.dao;

import static cs636.music.dao.DBConstants.LINEITEM_TABLE;
import static cs636.music.dao.DBConstants.SYS_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs636.music.domain.LineItem;

/**
 * 
 * Access line item table through this class. 
 * This code could be moved into InvoiceDAO.
 */
public class LineItemDAO {
		
	/**
	 * An Data Access Object for LineItem table
	 * @param db the database connection
	 * @throws SQLException
	 */
	public LineItemDAO(DbUtils db) {

	}
	
	/**
	 * Insert a line item into an given (by invoice id) invoice
	 * @param invoiceID invoice id
	 * @param item new line item
	 * @throws SQLException
	 */
	public void insertLineItem(Connection connection, long invoiceID, LineItem item) throws SQLException{
		Statement stmt = connection.createStatement();
		int lineitem_id = getNextLineItemID(connection);
		item.setId(lineitem_id);
		try {
			String sqlString = "insert into " + LINEITEM_TABLE + 
			" (lineitem_id, invoice_id, product_code, quantity) values ("
			+ item.getId() + ", " + invoiceID + ", '"
			+ item.getProductCode() + "', " + item.getQuantity() + ") ";
			stmt.execute(sqlString);
		} finally {
			stmt.close();
		}
	}
	
	/**
	 * Increase lineitem_id by 1 in the system table
	 * @throws SQLException
	 */
	private void advanceLineItemID(Connection connection) throws SQLException
	{
		Statement stmt = connection.createStatement();
		try {
			stmt.executeUpdate(" update " + SYS_TABLE
					+ " set lineitem_id = lineitem_id + 1");
		} finally {
			stmt.close();
		}
	}
	
	/**
	 * Get the available line item id 
	 * @return the line item id available 
	 * @throws SQLException
	 */
	private int getNextLineItemID(Connection connection) throws SQLException
	{
		int nextLID;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select lineitem_id from " + SYS_TABLE);
			set.next();
			nextLID = set.getInt("lineitem_id");
		} finally {
			stmt.close();
		}
		advanceLineItemID(connection); // the id has been taken, so set +1 for next one
		return nextLID;
	}
}
