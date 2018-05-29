package cs636.music.domain;

/**
 * LineItem: like Murach, pg. 649, except:
 * --The product field just the code, to avoid FKs from one DB to another
 * --The database id is exposed with getter/setter
 * --getTotal in Murach is called calculateItemTotal here
 * to signify it is not a table attribute.
 * --in prep. for music2, added the invoice field
 *
 */
public class LineItem {

	private long id;
	private String productCode;
	private int quantity;
	private Invoice invoice; 
	
	// no-args constructor, to be proper JavaBean
	public LineItem() {}
	
	// for DAO use: LineItem from DB
	public LineItem(long id, String productCode, Invoice invoice, int quantity) {
		this.id = id;
		this.productCode = productCode;
		this.invoice = invoice;
		this.quantity = quantity;
	}
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long lineitem_id) {
		this.id = lineitem_id;
	}

	public void setInvoice(Invoice inv){
		this.invoice = inv;
	}
	
	public Invoice getInvoice(){
		return invoice;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	// Note this is method returning a quantity that is not a database column
	// value from the lineitem table. To avoid problems later, we avoid "getXXX" 
	// naming for such methods.
	// Also, the name points out this is not just a value from the table.
	// This code no longer can be used: we don't have a product *object*

//	public BigDecimal calculateItemTotal() {
//		// We can't use * to multiply with BigDecimal, but it knows how--
//		BigDecimal total = product.getPrice().multiply(new BigDecimal(quantity));
//		return total;
//	}

}
