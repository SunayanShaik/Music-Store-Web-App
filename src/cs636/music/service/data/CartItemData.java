package cs636.music.service.data;

import java.math.BigDecimal;

/**
 * CartItemData: transfer object for item in the cart
 *
 */
public class CartItemData {

	private long productId;
	private int quantity;
	private String code;
	private String description;
	private BigDecimal price;  // using BigDecimal to preserve exact pennies
	
	// no-args constructor, to be proper JavaBean
	public CartItemData() {}
	
	public CartItemData (long productId, int quantity, String code, String description, BigDecimal price) {
		this.productId = productId;
		this.quantity = quantity;
		this.code = code;
		this.description = description;
		this.price = price;
	}
	
	public long getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getPrice() {
		return price;
	}

}
