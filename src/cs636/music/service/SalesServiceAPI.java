package cs636.music.service;

import java.util.Set;

import cs636.music.domain.Cart;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

public interface SalesServiceAPI {
	
	/**
	 * Register user if the email does not exist in the db
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @throws ServiceException
	 */
	public void registerUser(String firstname, String lastname, String email)
			throws ServiceException;
	
	/**
	 * Add address to user already in the db
	 * 
	 * @param uid
	 * @param user address
	 * @return the user info
	 * @throws ServiceException
	 */
	public void addUserAddress(long userId, String address) throws ServiceException;

	/**
	 * Get user info by given id
	 * 
	 * @param userId
	 * @return the user info found, return null if not found
	 * @throws ServiceException
	 */
	public UserData getUserInfo(long userId) throws ServiceException;

	/**
	 * Get public user info by given email address
	 * 
	 * @param email
	 * @return the user info found, return null if not found
	 * @throws ServiceException
	 */
	public UserData getUserInfoByEmail(String email) throws ServiceException;
	
	/**
	 * Get user by given email address and check if we have an address
	 * (i.e., the user is a customer)
	 * 
	 * @param email
	 * @return true if we have an address
	 * @throws ServiceException
	 */

	public boolean userIsCustomer(String email) throws ServiceException;
	
	/**
	 * Check out the cart from the user order and then generate an invoice for
	 * this order. Empty the cart after
	 * 
	 * @param cart
	 * @param userId
	 * @return the new invoice object, complete with new id
	 * @throws ServiceException
	 */
	public InvoiceData checkout(Cart cart, long userId) throws ServiceException;
	
	/* ====================== Admin Actions ===================== */
	/**
	 * Check login user
	 * @param username
	 * @param password
	 * @return true if useranme and password exist, otherwise return false
	 * @throws ServiceException
	 */
	public Boolean checkAdminLogin(String username,String password) throws ServiceException;
	
	/**
	 * Clean all user table, not product and system table to empty and then set the
	 * index numbers back to 1
	 * 
	 * @throws ServiceException
	 */
	public void initializeDB() throws ServiceException;

	/**
	 * process the invoice
	 * 
	 * @param invoiceId
	 * @throws ServiceException
	 */
	public void processInvoice(long invoice_id) throws ServiceException;
	
	/**
	 * Get a list of all invoices
	 * 
	 * @return list of all invoices
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofInvoices() throws ServiceException;

	/**
	 * Get a list of all unprocessed invoices
	 * 
	 * @return list of all unprocessed invoices
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofUnprocessedInvoices() throws ServiceException;
	
	/**
	 * get particular Invoice
	 * */
	
	public InvoiceData findInvoice(long invoiceId) throws ServiceException;
}
