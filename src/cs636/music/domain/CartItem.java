package cs636.music.domain;

/**
 * CartItem: memory-only object for item in the cart
 * suitable to being in the presentation layer
 * i.e., no domain objects, just a product id
 * Data is moved to a LineItem object in checkout
 * For API calls, see related POJO service.data/CartItemData.java
 *
 */
public class CartItem {

	private long productId;
	private int quantity;
	
	// no-args constructor, to be proper JavaBean
	public CartItem() {}
	
	public CartItem (long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long id) {
		this.productId = id;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
