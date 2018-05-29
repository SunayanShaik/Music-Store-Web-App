package cs636.music.dao;

import static cs636.music.dao.DBConstants.INVOICE_TABLE;
import static cs636.music.dao.DBConstants.LINEITEM_TABLE;
import static cs636.music.dao.DBConstants.SYS_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cs636.music.domain.Invoice;
import cs636.music.domain.LineItem;

/**
 * 
 * Access Invoice table through this class. 
 */
public class InvoiceDAO {
	
	private UserDAO userdb;
	private LineItemDAO lineitemdb;
	
	/**
	 * An Data Access Object for Invoice table table
	 * @param db the database connection 
	 * @throws SQLException
	 */
	public InvoiceDAO(LineItemDAO linedb, UserDAO udb, ProductDAO prddb)  {
		lineitemdb = linedb;
		userdb = udb;
	}

	/**
	 * Increase invoice_id by 1 in the system table
	 * @throws SQLException
	 */
	private void advanceInvoiceID(Connection connection) throws SQLException
	{
		Statement stmt = connection.createStatement();
		try {
			stmt.executeUpdate(" update " + SYS_TABLE
					+ " set invoice_id = invoice_id + 1");
		} finally {
			stmt.close();
		}
	}
	
	/**
	 * Get the available invoice id 
	 * @return the invoice id available 
	 * @throws SQLException
	 */
	private int getNextInvoiceID(Connection connection) throws SQLException
	{
		int nextIID;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select invoice_id from " + SYS_TABLE);
			set.next();
			nextIID = set.getInt("invoice_id");
		} finally {
			stmt.close();
		}
		advanceInvoiceID(connection); // the id has been taken, so set +1 for next one
		return nextIID;
	}
	
	
	/**
	 * Insert invoice data into invoice and lineitme table
	 * uses current_time to set invoice_date timestamp
	 * @param invoice
	 * @throws SQLException
	 */
	public void insertInvoice(Connection connection, Invoice invoice)throws SQLException{
		Statement stmt = connection.createStatement();
		int invoiceID =  getNextInvoiceID(connection);
		invoice.setInvoiceId(invoiceID);
		String YN = "";
		if (invoice.isProcessed()){
			YN = "y";
		}else{
			YN = "n";
		}
		try{
			String sqlString = "insert into "+ INVOICE_TABLE + " values (" +
			invoiceID + ", " + 
			invoice.getUser().getId()+ " , " + "current_timestamp, " +
			// dbDAO.formatTimestamp(invoice.getInvoiceDate()) + ", " +   // another way
			invoice.getTotalAmount().toPlainString() + ", " +
			"'" + YN +"')";
			stmt.execute(sqlString);
			for (LineItem item: invoice.getLineItems()){
			   lineitemdb.insertLineItem(connection, invoiceID, item);
			}
		} finally {
			stmt.close();
		}
	}
	
	
	/**
	 * find one invoice and its items by given invoice id
	 * @param invoiceId
	 * @return an invoice data with its items, null if not found
	 * @throws SQLException
	 */
	public Invoice findInvoice(Connection connection, long invoiceId) throws SQLException{
		Invoice invoice = null;
		Statement stmt = connection.createStatement();
		try 
		{
			String sqlString =  " select * from " + 
			INVOICE_TABLE + " i, " +
			LINEITEM_TABLE + " l " +		
			" where i.invoice_id = " + invoiceId + 
			" and i.invoice_id = l.invoice_id ";
			ResultSet set = stmt.executeQuery(sqlString);
			if (set.next()){ // if the result is not empty
				// first row: set up Invoice from invoice columns
				invoice= new Invoice(set.getInt("invoice_id"),
						userdb.findUserByID(connection, set.getInt("user_id")),
						set.getTimestamp("invoice_date"),
						set.getString("is_processed") == "y",
						null,// items added below
						set.getBigDecimal("total_amount"));
				Set<LineItem> items = new HashSet<LineItem>();
				LineItem item = new LineItem(set.getLong("lineitem_id"), set.getString("product_code"), invoice, set.getInt("quantity"));
				items.add(item);
				while (set.next()){ // if the invoice has more than one item
					item = new LineItem(set.getLong("lineitem_id"), set.getString("product_code"), invoice, set.getInt("quantity"));
					items.add(item);
				}
				invoice.setLineItems(items);
			}
			set.close();
		} finally {
			stmt.close();
		}
		
		return invoice;
	}
	
	/**
	 * find all unprocessed invoice
	 * @return all unprocessed invoice in db
	 * @throws SQLException
	 */
	public Set<Invoice> findAllUnprocessedInvoices(Connection connection) throws SQLException{
		Set<Invoice> invoices = new HashSet<Invoice>();
		Statement stmt = connection.createStatement();
		Invoice invoice;
		
		String sqlString =  " select invoice_id from " + INVOICE_TABLE  +		
		" where is_processed = 'n'";
		try{
			ResultSet set = stmt.executeQuery(sqlString);
			while (set.next()){
				invoice = this.findInvoice(connection, set.getInt("invoice_id"));
				invoices.add(invoice);
			}
			set.close();
		}finally{
			stmt.close();
		}
		return invoices;
	}
	
	/**
	 * find all invoices
	 * @return all invoices in db
	 * @throws SQLException
	 */
	public Set<Invoice> findAllInvoices(Connection connection) throws SQLException{
		Set<Invoice> invoices = new HashSet<Invoice>();
		Statement stmt = connection.createStatement();
		Invoice invoice;
		
		String sqlString =  " select invoice_id from " + INVOICE_TABLE ;
		try{
			// if performance matters, we could do this retrieval all
			// in one SQL statement, rather than in a programmed loop of them
			ResultSet set = stmt.executeQuery(sqlString);
			while (set.next()){
				invoice = this.findInvoice(connection, set.getInt("invoice_id"));
				invoices.add(invoice);
			}
			set.close();
		}finally{
			stmt.close();
		}
		return invoices;
	}
	
	/**
	 * update the is_processed attribute of the invoice
	 * @param i  Invoice to update
	 * @throws SQLException
	 */
	public void updateInvoice(Connection connection, Invoice i)throws SQLException{
		Statement stmt = connection.createStatement();
		try{
			String sqlString = "update "+ INVOICE_TABLE + " set is_processed = 'y'" +
			" where invoice_id = " + i.getInvoiceId() ;
			stmt.execute(sqlString);
		} finally {
			stmt.close();
		}
	}
}
