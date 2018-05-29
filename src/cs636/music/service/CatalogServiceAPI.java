package cs636.music.service;

import java.util.Set;

import cs636.music.domain.Cart;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.DownloadData;

public interface CatalogServiceAPI {
	
	/**
	 * Getting list of all products
	 * 
	 * @return list of all product
	 * @throws ServiceException
	 */
	public Set<Product> getProductList() throws ServiceException;
	
	/**
	 * Create a new cart
	 * 
	 * @return the cart
	 */
	public Cart createCart();
	
	/**
	 * Get cart info
	 * 
	 * @return the cart's content
	 */
	public Set<CartItemData> getCartInfo(Cart cart) throws ServiceException;
	
	/**
	 * Add a product to the cart. If the product is already in the cart, add
	 * quantity. Otherwise, insert a new line item.
	 * 
	 * @param productId
	 * @param cart
	 * @param quantity
	 */
	public void addItemtoCart(long productId, Cart cart, int quantity);
	
	/**
	 * Change the quantity of one item. If quantity <= 0 then delete this item.
	 * 
	 * @param productId
	 * @param cart
	 * @param quantity
	 */
	public void changeCart(long productId, Cart cart, int quantity);
	
	/**
	 * Remove a product item from the cart
	 * 
	 * @param productId
	 * @param cart
	 */
	public void removeCartItem(long productId, Cart cart);

	/**
	 * Return a product info by given product code
	 * 
	 * @param prodCode
	 *            product code
	 * @return the product info
	 * @throws ServiceException
	 */

	/**
	 * Return a product info by given product id
	 * 
	 * @param productId
	 * @return the product info
	 * @throws ServiceException
	 */
	public Product getProduct(long productId) throws ServiceException;

	public Product getProductByCode(String prodCode) throws ServiceException;
	/**
	 * Add one download history, record the user and track
	 * 
	 * @param emailAddress
	 *            email of user who downloaded the track
	 * @param track
	 *            the track which was downloaded
	 * @throws ServiceException
	 */
	public void addDownload(String emailAddress, Track track) throws ServiceException;
	
	/* =============Admin actions ======================= */
	/**
	 * Clean all user tables of rows created by app
	 * and then set the index numbers back to 1
	 * @throws ServiceException
	 */
	public void initializeDB()throws ServiceException;

	/**
	 * Get a list of all downloads
	 * @return list of all downloads
	 * @throws ServiceException
	 */
	public Set<DownloadData> getListofDownloads() throws ServiceException;

	
}
