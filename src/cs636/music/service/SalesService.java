package cs636.music.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.AdminUserDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.SalesDbDAO;
import cs636.music.dao.UserDAO;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Invoice;
import cs636.music.domain.LineItem;
import cs636.music.domain.Product;
import cs636.music.domain.User;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

/**
 * 
 * Provide user level services to user app through accessing DAOs
 * 
 */

public class SalesService {
	private CatalogService catalogService;
	private SalesDbDAO salesDbDAO;
	private InvoiceDAO invoiceDb;
	private UserDAO userDb;
	private AdminUserDAO adminUserDb;

	/**
	 * construct a user service provider
	 * @param catalogService  --needed in checkout
	 * @param salesDbDAO
	 * @param productDao
	 * @param userDao
	 * @param downloadDao
	 * @param lineItemDao
	 * @param invoiceDao
	 */
	public SalesService(CatalogService catalogService, SalesDbDAO salesDbDAO, UserDAO userDao, AdminUserDAO adminUserDao, InvoiceDAO invoiceDao) {
		this.catalogService = catalogService;
		this.salesDbDAO = salesDbDAO;
		invoiceDb = invoiceDao;
		userDb = userDao;
		adminUserDb = adminUserDao;
	}

	/**
	 * Register user if the email does not exist in the db
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @return the user info
	 * @throws ServiceException
	 */
	public void registerUser(String firstname, String lastname, String email) throws ServiceException {
		User user = null;
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			user = userDb.findUserByEmail(connection, email);
			if (user == null) { // this user has not registered yet
				user = new User();
				user.setFirstname(firstname);
				user.setLastname(lastname);
				user.setEmailAddress(email);
				userDb.insertUser(connection, user);
			}
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error while registering user: ", e);
		}
	}

	/**
	 * Add address to user already in the db
	 * 
	 * @param uid
	 * @param user
	 *            address
	 * @return the user info
	 * @throws ServiceException
	 */
	public void addUserAddress(long userId, String address) throws ServiceException {
		User user = null;
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			user = userDb.findUserByID(connection, userId);
			if (user == null) { // this user has not registered yet
				throw new ServiceException("Can't add address to unregistered user " + userId);
			}
			user.setAddress(address);
			userDb.updateUserAddress(connection, user);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error while registering user: ", e);
		}
	}

	/**
	 * Get user info by given id
	 * 
	 * @param userId
	 * @return the user info found, return null if not found
	 * @throws ServiceException
	 */
	public UserData getUserInfo(long userId) throws ServiceException {
		User user = null;
		UserData user1 = null;
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			user = userDb.findUserByID(connection, userId);
			user1 = new UserData(user);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error while getting user info: ", e);
		}
		if (user != null)
			user1 = new UserData(user);
		return user1;
	}


	/**
	 * Get user info by given email address
	 * 
	 * @param email
	 * @return the user info found, return null if not found
	 * @throws ServiceException
	 */
	public UserData getUserInfoByEmail(String email) throws ServiceException {
		User user = null;
		UserData user1 = null;
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			user = userDb.findUserByEmail(connection, email);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error while getting user info: ", e);
		}
		if (user != null)
			user1 = new UserData(user);
		return user1;
	}

	/**
	 * Get user by given email address and check if we have an address (i.e., the
	 * user is a customer)
	 * 
	 * @param email
	 * @return true if we have an address
	 * @throws ServiceException
	 */
	public boolean userIsCustomer(String email) throws ServiceException {
		User user = null;
		Connection connection = null;

		try {
			connection = salesDbDAO.startTransaction();
			user = userDb.findUserByEmail(connection, email);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error while getting user info: ", e);
		}
		if (user != null) {
			System.out.println("customer name = " + user.getFirstname());
			System.out.println("customer addr = " + user.getAddress());
			System.out.println("returning " + (user != null && user.getAddress() != null
					&& user.getAddress().length() > 0 && !user.getAddress().equalsIgnoreCase("null")));
		}
		return user != null && user.getAddress() != null && user.getAddress().length() > 0
				&& !user.getAddress().equalsIgnoreCase("null");
	}
	/**
	 * Check out the cart from the user order and then generate an invoice for this
	 * order. Empty the cart after
	 * 
	 * @param cart
	 * @param userId
	 * @return new invoice transfer object, complete with newly assigned id
	 * @throws ServiceException
	 */
	public InvoiceData checkout(Cart cart, long userId) throws ServiceException {
		Connection connection = null;
		Invoice invoice = null;
		try {
			// leave user in invoice null for now, add later--	
			// The following code has to use the CatalogDb, so avoid using SalesDb yet
			invoice = new Invoice(-1L, null, new Date(), false, null, null);
			Set<LineItem> invItems = new HashSet<LineItem>();
			BigDecimal invoiceTotal = new BigDecimal(0);
			for (CartItem item : cart.getItems()) {
				// We don't use CatalogDB's DAOs, which are private to Catalog, so we use its public API:
				// In a more distributed implementation, this would involve a web request
				// This runs its own transaction in the catalog db
				Product product = catalogService.getProduct(item.getProductId());
				LineItem invItem = new LineItem();
				invItem.setProductCode(product.getCode());
				invItem.setInvoice(invoice);
				invItem.setQuantity(item.getQuantity());
				invItems.add(invItem);	
				BigDecimal price = product.getPrice();
				BigDecimal quantity = new BigDecimal(item.getQuantity());
				invoiceTotal = invoiceTotal.add(price.multiply(quantity));	
			}
			invoice.setLineItems(invItems);
			invoice.setTotalAmount(invoiceTotal);
			
			// now use the sales DB and its DAOs, as usual in this salesDb service code
			connection = salesDbDAO.startTransaction();
			User user = userDb.findUserByID(connection, userId);
			if (user == null) {
				throw new ServiceException("Checkout failed: can't find user");
			}
			invoice.setUser(user); // add user to invoice now
			String address = user.getAddress();
			if (address == null || address.isEmpty() || address.equalsIgnoreCase("null")) {
				//throw new ServiceException("Checkout failed: no address for user "+user.getEmailAddress());
			}

			invoiceDb.insertInvoice(connection, invoice);
			salesDbDAO.commitTransaction(connection);
			cart.clear();
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't check out: ", e);
		}
		return new InvoiceData(invoice);
	}

	/* ====================== Admin Actions ===================== */
	/**
	 * Check login user
	 * @param username
	 * @param password
	 * @return true if useranme and password exist, otherwise return false
	 * @throws ServiceException
	 */
	public Boolean checkAdminLogin(String username,String password) throws ServiceException {
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			Boolean b = adminUserDb.findAdminUser(connection, username, password);
			salesDbDAO.commitTransaction(connection);
			return b;
		} catch (Exception e)
		{
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Check login error: ", e);
		}
	}
	
	/**
	 * Clean all user tables of rows created by app and then set the
	 * index numbers back to 1
	 * 
	 * @throws ServiceException
	 */
	public void initializeDB() throws ServiceException {
		try {
			salesDbDAO.initializeDb();
		} catch (Exception e) { // any exception
			throw new ServiceException("Can't initialize DB: (probably need to load DB)", e);
		}
	}

	/**
	 * process the invoice
	 * 
	 * @param invoiceId
	 * @throws ServiceException
	 */
	public void processInvoice(long invoice_id) throws ServiceException {
		Connection connection = null;
		try {
			connection = salesDbDAO.startTransaction();
			Invoice invoice = invoiceDb.findInvoice(connection, invoice_id);
			invoice.setProcessed(true);
			invoiceDb.updateInvoice(connection, invoice);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Invoice was not processed successfully: ", e);
		}
	}

	/**
	 * Get a list of all invoices
	 * 
	 * @return list of all invoices
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofInvoices() throws ServiceException {
		Connection connection = null;
		Set<Invoice> invoices = null;
		try {
			connection = salesDbDAO.startTransaction();
			invoices = invoiceDb.findAllInvoices(connection);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find invoice list in DB: ", e);
		}
		Set<InvoiceData> invoices1 = new HashSet<InvoiceData>();
		for (Invoice i : invoices) {
			invoices1.add(new InvoiceData(i));
		}
		return invoices1;
	}

	/**
	 * Get a list of all unprocessed invoices
	 * 
	 * @return list of all unprocessed invoices
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofUnprocessedInvoices() throws ServiceException {
		Connection connection = null;
		Set<Invoice> invoices = null;
		try {
			connection = salesDbDAO.startTransaction();
			invoices = invoiceDb.findAllUnprocessedInvoices(connection);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find unprocessed invoice list: ", e);
		}
		Set<InvoiceData> invoices1 = new HashSet<InvoiceData>();
		for (Invoice i : invoices) {
			invoices1.add(new InvoiceData(i));
		}
		return invoices1;
	}
	
	public InvoiceData findInvoice(long invoiceId) throws ServiceException {
		Connection connection = null;
		Invoice invoice = null;
		InvoiceData invoiceData;
		try {
			connection = salesDbDAO.startTransaction();
			invoice = invoiceDb.findInvoice(connection, invoiceId);
			salesDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			salesDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find invoices: ", e);
		}
		invoiceData = new InvoiceData(invoice);
		return invoiceData;
	}

}
