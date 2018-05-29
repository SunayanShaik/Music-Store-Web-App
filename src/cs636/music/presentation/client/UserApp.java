package cs636.music.presentation.client;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Cart;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.presentation.PresentationUtils;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.ServiceException;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

/**
 * 
 * Line-oriented client code for Music Site user. To be replaced by JSP pages
 * later. 
 * 
 */
/**
 * @author eoneil
 * 
 */
public class UserApp {
	/**
	 * 
	 * Since the page flow in this app is tree-like, plus back arrows to the
	 * topmost Catalog page, we can implement it easily with methods so that
	 * execution calls down the page tree and returns back to the top level. In
	 * a more general page flow, we would need to have one top-level command
	 * loop dispatching to various commands each of which returns with a value
	 * that specifies which command to do next, and possibly arguments for it.
	 * (Actually, this page flow is not quite a tree, because you can reach the
	 * Cart page from several other pages, but since you always go back to the
	 * Catalog page after that, we can still just return from wherever after
	 * calling the Cart page method.)l
	 * 
	 */

	private CatalogService catalogService;
	private SalesService salesService;

	static private Scanner in; // the user input source

	// state held in the presentation layer -- user-private data
	private UserData user = null; // the user once registered (immutable part of user data)
	private Cart cart = null; // the shopping cart for the user

	// also note the product variable in processProduct page, also held across
	// service calls, but note that Products and their Tracks never change in this app.
	// (If Product were changeable, you would use a productId instead of a Product 
	//  object to remember the product across service calls)

	public UserApp() throws Exception {
		in = new Scanner(System.in);
		System.out.println("starting User app");
		String salesDbName = PresentationUtils.readEntry(in,"Please Enter the salesDbName (hsql, dbs3, or mysql)");
		String catalogDbName = PresentationUtils.readEntry(in,"Please Enter the catalogDbName (hsql, dbs3, or mysql)");
		// configure both parts of system for this top-leval app
		MusicSystemConfig.configureCatalogService(catalogDbName);
		MusicSystemConfig.configureSalesService(salesDbName);

		catalogService = MusicSystemConfig.getCatalogService();
		salesService = MusicSystemConfig.getSalesService();
	}

	public static void main(String[] args) {
		try {
			UserApp myApp = new UserApp();
			System.out.println("starting User app");
			myApp.handleCatalogPage();
			System.out.println("Thanks for visiting the Band's site.");
		} catch (Exception e) {
			System.out.println("Error in run: StackTrace for it: ");
			e.printStackTrace();
			System.out.println("Error in run, shorter report: " + MusicSystemConfig.exceptionReport(e));
		}
	}

	public void handleCatalogPage() throws IOException, ServiceException {
		Set<Product> prodlist;
		Product prod = null;
		while (true) {
			System.out.println("---Catalog Page---");
			prodlist = this.catalogService.getProductList();
			System.out.println("Displaying list of CDs: product codes and descriptions");
			PresentationUtils.displayCDCatlog(prodlist, System.out);
			String productCode = PresentationUtils.readEntry(in,"Enter the product code of product to see info, or S to show cart or Q to quit");
			if (productCode.equalsIgnoreCase("S")) {
				if (cart == null)
					cart = catalogService.createCart();
				handleCartPage(0); // no real productId
				continue;
			} else if (productCode.equalsIgnoreCase("Q"))
				return;
			// Now product is chosen, use it on Product page
			prod = this.catalogService.getProductByCode(productCode);
			if (prod != null) {
				handleProduct(prod.getId());
			}
			else {
				System.out.println(" Can't find the product: " + productCode + " !!!");
			}
				
		}
	}

	/**
	 * Here when user has selected a particular product. Let them work with it,
	 * listening to tracks, registering, etc. and when done, we'll return to
	 * Catalog page.
	 * 
	 * Since the Catalog page and the Sound page share so many user commands, we
	 * use this one method to handle both, with a variable "listening" to tell
	 * which one we're handling at the moment: listening == false: Product Page
	 * commands A S L B listening == true: Sound Page commands T A S B
	 * 
	 * @throws IOException, ServiceException
	 */
	public void handleProduct(long productId) throws IOException, ServiceException{
		// listening == true means the user has used the L command to listen
		// to tracks since first coming here from the Catalog page
		boolean listening = false;
		

		while (true) {
			Product product = catalogService.getProduct(productId); 
			if (listening) {
				System.out.println("---Sound Page---");
				System.out.println(" Displaying list of tracks: track# and title");
				PresentationUtils.displayTracks(product, System.out);			
			} else {
				System.out.println("---Product Page--");
				System.out.println("displaying product info for this product");
				PresentationUtils.displayProductInfo(product, System.out);
			}

			String command = null;
			// creating empty Cart if none yet
			// Note: calling service layer to create cart
			if (cart == null)
				cart = this.catalogService.createCart();
			System.out.println("Possible Commands");
			if (listening){
				System.out.println("T: Track# to play");
			}
			System.out.println("A: Add CD to Cart");
			System.out.println("S: Show Cart - has other options like CheckOut");
			if (!listening) {
				System.out.println("L: Listen to Sample ");
			}
			System.out.println("B: Browse Catalog");

			command = PresentationUtils.readEntry(in,"Please Enter the Command");

			if (command.equalsIgnoreCase("A")) {
				// add this CD to cart
				handleCartPage(productId);
				return; // redisplay whole catalog after add-to-cart
			} else if (command.equalsIgnoreCase("S")) {
				// show cart
				handleCartPage(0);
				return; // redisplay whole catalog after display of cart

			} else if (command.equalsIgnoreCase("L") && (!listening)) {
				// creating User if necessary (required for download)
				if (user == null)
				   user = userUI(); // do simple registration if needed
				listening = true; // switch this method to the Sound page

			} else if (command.equalsIgnoreCase("T") && listening) {
				String trackNumberString = PresentationUtils.readEntry(in,
						"Please enter track# to download & play");
				int trackNumber = Integer.parseInt(trackNumberString);
				System.out.println("getting Track " + trackNumber	
				 	+ ", recording download and fake-playing track");
				// user stays on Sound page (listening == true)
				// Get fresh product object (really not needed if products are immutable)
				product = catalogService.getProduct(productId); 
				Track track = product.findTrackByNumber(trackNumber);
				if (track != null) {
					PresentationUtils.playTrack(track, System.out);
					catalogService.addDownload(user.getEmailAddress(), track);
				} else{
					System.out.println("Can't find track #" + trackNumber);
				}
			} else if (command.equalsIgnoreCase("B")) {  // browse command
				return;  // back to catalog page
			} else
				System.out.println("Invalid Command: " + command + "! Try again...");
		}
	}

	/**
	 * Handle Cart page actions (pg. 664) No product is singled out at this
	 * point, except that the argued newProduct, if non-null, is being added to
	 * the cart here. Return to top level when done here.
	 * 
	 */

	public void handleCartPage(long productId) throws IOException, ServiceException {
		System.out.println("---Cart Page---");
		// if newProduct is non-null, adding it to Cart
		if (productId > 0){
			this.catalogService.addItemtoCart(productId, cart, 1);
		}

		while (true) {
			System.out.println("displaying Cart");
			Set<CartItemData> cartInfo = catalogService.getCartInfo(cart);
			PresentationUtils.displayCart(cartInfo, System.out);
			System.out.println("Possible Commands");
			System.out.println("C: Change to Cart");
			System.out.println("R: Remove from Cart ");
			System.out.println("O: Check Out ");
			System.out.println("B: Browse Catalog");
			String command = PresentationUtils.readEntry(in,
					"Please Enter the Command");

			if (command.equalsIgnoreCase("C")) {
				String productCode = PresentationUtils.readEntry(in,
						"Enter the product code of product to change");

				int quantity = Integer.parseInt(PresentationUtils.readEntry(in,
						"Enter the new quantity of the product"));
				System.out.println("calling service layer to update Cart for product"
								+ productCode + ", quantity " + quantity);
				Product prd = catalogService.getProductByCode(productCode);
				if (prd == null) {
					System.out.println(" Can't find the product: " + productCode + " !!!");
				} else {
					catalogService.changeCart(productId, cart, quantity);
				}
			} else if (command.equalsIgnoreCase("R")) {
				String productCode = PresentationUtils.readEntry(in,
						"Enter the product code for removal");
				System.out.println("calling service layer to remove product "
						+ productCode + " from cart");
				Product prd = catalogService.getProductByCode(productCode);
				if (prd == null) {
					System.out.println(" Can't find the product: " + productCode + " !!!");
				} else {
					catalogService.removeCartItem(productId, cart);
				}
			} else if (command.equalsIgnoreCase("O")) {
				handleCheckOut();
				return;
			} else if (command.equalsIgnoreCase("B")) {
				return;
			} else
				System.out.println("Invalid Command!");
		}
	}

	public void handleCheckOut() throws IOException, ServiceException {
		if (cart.getItems().size()>0)
		{		
			System.out.println("---CheckOut Page---");			
			if (user == null)
				user = userUI();
			String address = PresentationUtils.readEntry(in, "Address for order");
			salesService.addUserAddress(user.getId(), address);
			System.out.println("\nCreating order for cart items:");
			Set<CartItemData> cartInfo = catalogService.getCartInfo(cart);
			PresentationUtils.displayCart(cartInfo, System.out);
			System.out.println("calling service layer to checkout");
			// Note: passing userId to service layer here
			// so the service layer doesn't have to save user info itself
			// (idea of stateless service layer)
			InvoiceData newInvoice = salesService.checkout(cart, user.getId());
			System.out.println("\nThank you for your order, your cart is now empty.\n");
			PresentationUtils.displayInvoice(newInvoice, System.out);			
		} else {
			System.out.println("The cart is empty!!");
		}
		
	}

	public UserData userUI() throws IOException, ServiceException{
		System.out.println("---User Registration Page---");
		String fName = PresentationUtils.readEntry(in,
				"\n Give us a few registration details:\n\t\t FirstName");
		String lName = PresentationUtils.readEntry(in, "\n\t\t LastName");
		String eMail = PresentationUtils.readEntry(in, "\n\t\t Email");
		System.out.println("calling service layer to register user: " + fName
				+ " " + lName + " " + eMail);
		// Note: creating User object in service layer, not here!
		salesService.registerUser(fName,lName,eMail);
		UserData user = salesService.getUserInfoByEmail(eMail);
		PresentationUtils.displayUserInfo(user, System.out);
		return user;
	}

}
