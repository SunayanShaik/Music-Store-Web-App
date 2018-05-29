package cs636.music.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import cs636.music.domain.User;
import cs636.music.domain.LineItem;

/**
 * Invoice, like Murach pg. 649 except:
 * --Murach calls the id "invoiceNumber" vs. "invoiceId" here
 * --We use Set instead of List
 * --We use BigDecimal for money instead of double to keep pennies exact.
 * However, it does not strictly qualify as a ordinary JavaBean (definition pg. 175) 
 * because of lack of some setters.
 * The more general JavaBean (see Java tutorial at http://docs.oracle.com/javase/tutorial/javabeans)
 * allows read-only properties like the four fields here.
 */
public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	private long invoiceId;
	private User user;
	private Date invoiceDate;
	private BigDecimal totalAmount;
	private boolean isProcessed;
	private Set<LineItem> lineItems;
	
	public Invoice() {}
	
	public Invoice(long id, User u, Date d, boolean isProc, Set<LineItem> items, BigDecimal totAmount) {
		invoiceId = id;
		user = u;
		invoiceDate = d;
		isProcessed = isProc;
		lineItems = items;
		totalAmount = totAmount;
	}
	
	public long getInvoiceId() {
		return invoiceId;
	}
	
	public void setInvoiceId(long invoice_id) {
		this.invoiceId = invoice_id;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setLineItems(Set<LineItem> items) {
		lineItems = items;
	}

	public Set<LineItem> getLineItems() {
		return lineItems;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totAmount) {
		this.totalAmount = totAmount;
	}
	
	// getter for boolean: isX, not getIsX
	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	
	// calculate the invoice total
//	public BigDecimal calculateTotal() {
//		BigDecimal total = new BigDecimal(0);
//		for (LineItem item: lineItems){
//			 total = total.add(item.calculateItemTotal());	
//		}
//		return total;
//	}
}
