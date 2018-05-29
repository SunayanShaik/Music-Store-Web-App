package cs636.music.service;

import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.CatalogDbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Download;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.DownloadData;

/**
 * 
 * Provide user level services to user app through accessing DAOs
 * 
 */
public class CatalogService implements CatalogServiceAPI {
	private CatalogDbDAO catalogDbDAO;
	private DownloadDAO downloadDb;
	private ProductDAO prodDb;

	/**
	 * construct a user service provider
	 * 
	 * @param productDao
	 * @param userDao
	 * @param downloadDao
	 * @param lineItemDao
	 * @param invoiceDao
	 */
	public CatalogService(CatalogDbDAO catalogDbDAO, ProductDAO productDao, DownloadDAO downloadDao) {
		this.catalogDbDAO = catalogDbDAO;
		downloadDb = downloadDao;
		prodDb = productDao;
	}

	/**
	 * Getting list of all products
	 * 
	 * @return list of all product
	 * @throws ServiceException
	 */
	public Set<Product> getProductList() throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Set<Product> prodList = prodDb.findAllProducts(connection);
			catalogDbDAO.commitTransaction(connection);
			return prodList;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find product list in db: ", e);
		}
	}

	/**
	 * Create a new cart
	 * 
	 * @return the cart
	 */
	public Cart createCart() {
		return new Cart();
	}
	
	/**
	 * Get cart info
	 * 
	 * @return the cart's content
	 */
	public Set<CartItemData> getCartInfo(Cart cart) throws ServiceException {
		Set<CartItemData> items = new HashSet<CartItemData>();
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			for (CartItem item : cart.getItems()) {
				Product product = prodDb.findProductById(connection, item.getProductId());
				CartItemData itemData = new CartItemData(item.getProductId(), item.getQuantity(), product.getCode(),
						product.getDescription(), product.getPrice());
				items.add(itemData);
			}
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find product in cart: ", e);
		}
		return items;

	}
	/**
	 * Add a product to the cart. If the product is already in the cart, add
	 * quantity. Otherwise, insert a new line item.
	 * 
	 * @param prod
	 * @param cart
	 * @param quantity
	 */
	public void addItemtoCart(long productId, Cart cart, int quantity) {
		CartItem item = cart.findItem(productId);
		if (item != null) { // product is already in the cart, add quantity
			int qty = item.getQuantity();
			item.setQuantity(qty + quantity);
		} else { // not in the cart, add new item with quantity
			item = new CartItem(productId, quantity);
			// cart.addItem(item);
			cart.getItems().add(item);
		}
	}

	/**
	 * Change the quantity of one item. If quantity <= 0 then delete this item.
	 * 
	 * @param prod
	 * @param cart
	 * @param quantity
	 */
	public void changeCart(long productId, Cart cart, int quantity) {
		CartItem item = cart.findItem(productId);
		if (item != null) {
			if (quantity > 0) {
				item.setQuantity(quantity);
			} else { // if the quantity was changed to 0 or less, remove the
						// product from cart
				cart.removeItem(productId);
			}
		}
	}

	/**
	 * Remove a product item from the cart
	 * 
	 * @param prod
	 * @param cart
	 */
	public void removeCartItem(long productId, Cart cart) {
		CartItem item = cart.findItem(productId);
		if (item != null) {
			cart.removeItem(productId);
		}
	}

	/**
	 * Return a product info by given product id
	 * 
	 * @param productId
	 * @return the product info
	 * @throws ServiceException
	 */
	public Product getProduct(long productId) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Product prd = prodDb.findProductById(connection, productId);
			if (prd == null)
				return null;
			// Apps need track info for a Product
			// so attach it now while the Connection is good
			for (Track track : prd.getTracks())
				track.getSampleFilename();
			catalogDbDAO.commitTransaction(connection);
			return prd;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error accessing product "+productId, e);
		}
	}
	/**
	 * Return a product info by given product code
	 * 
	 * @param prodCode
	 *            product code
	 * @return the product info
	 * @throws ServiceException
	 */
	public Product getProductByCode(String prodCode) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Product prd = prodDb.findProductByCode(connection, prodCode);
			// Apps need track info for a Product
			// so attach it now while the Connection is good
			for (Track track : prd.getTracks())
				track.getSampleFilename();
			catalogDbDAO.commitTransaction(connection);
			return prd;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error accessing product "+prodCode, e);
		}
	}

	/**
	 * Add one download history, record the user and track
	 * 
	 * @param emailAddress
	 *            email of user who downloaded the track
	 * @param track
	 *            the track which was downloaded
	 * @throws ServiceException
	 */
	public void addDownload(String emailAddress, Track track) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Download download = new Download();
			download.setEmailAddress(emailAddress);
			download.setTrack(track);
			download.setDownloadDate(new Date());
			downloadDb.insertDownload(connection, download);
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't add download: ", e);
		}
	}
	/* =============Admin action ======================= */
	/**
	 * Clean all user tables of rows created by app
	 * and then set the index numbers back to 1
	 * @throws ServiceException
	 */
	public void initializeDB()throws ServiceException {
		try {
			catalogDbDAO.initializeDb();
		} catch (Exception e) { // any exception
			throw new ServiceException(
					"Can't initialize DB: (probably need to load DB)", e);
		}
	}
	/**
	 * Get a list of all downloads
	 * @return list of all downloads
	 * @throws ServiceException
	 */
	public Set<DownloadData> getListofDownloads() throws ServiceException {
		Connection connection = null;
		
		Set<Download> downloads = null;
		try {
			connection = catalogDbDAO.startTransaction();
			downloads = downloadDb.findAllDownloads(connection);
			// track is to-one, then its Product is to-one
			// related user is to-one, so all eagerly loaded by default
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e)
		{
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find download list: ", e);
		}
		Set<DownloadData> downloads1 = new HashSet<DownloadData>();
		for (Download d: downloads) {
			downloads1.add(new DownloadData(d));
		}
		return downloads1;
	}
	
	
}
