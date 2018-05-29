package cs636.music.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * A Cart is a memory-only POJO holding Products that the
 * user has chosen but not yet bought via "checkout".
 * At checkout, an Invoice with the same set of LineItems
 * is created and inserted in the database.
 * Like pg. 649, except:
 * --uses Set rather than List for items
 * --no setItems: caller should do addItem one by one.
 * --has findItem to find a certain product's item in the cart.
 *
 * This object holds user-private data, and belongs to the
 * presentation layer, but is created and changed only in the 
 * service layer during the actions of the app. The presentation 
 * layer code is allowed read access to its data. 
 */
public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;
	private Set<CartItem> items;
    /**
     * Construct a new Cart to hold items  
     */
	public Cart() {
		items = new HashSet<CartItem>();
	}
	
    /**
     * Obtain all items in this cart
     * @return all items in the cart 
     */
	public Set<CartItem> getItems() {
		return items;
	}
	
	/**
	 * Find an item of this cart through its product id.
	 * @param the product id
	 * @return the item in this cart with the given product id. 
	 */
	public CartItem findItem(long productId) {
		for (CartItem i : items) {
			if (i.getProductId() == productId) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Add an item into this cart. 
	 * To be called from service code only, where a LineItem can be created
	 * If the item already exists in the cart, only the quantity is changed. 
	 * @param item
	 */
	public void addItem(CartItem item) {
		if (items == null) {
			items = new HashSet<CartItem>();
		}
		// If the item already exists in the cart, only the quantity is changed.
		long prodId = item.getProductId();
		int quantity = item.getQuantity();

		for (CartItem l : items) {

			if (l.getProductId() == prodId) {
				l.setQuantity(quantity);
				return;
			}
		}
		// here if item not there yet
		items.add(item);
	}

	/**
	 * Remove an item with given product id from this cart 
	 * @param productId the product need to be removed 
	 */
	public void removeItem(long productId) {

		for (CartItem l : items) {
			if (l.getProductId() == productId) {
				items.remove(l);
				return;
			}
		}
	}
	

	/**
	 * clean out cart (for end of checkout)
	 */
	public void clear() {
		items.clear();
	}
}