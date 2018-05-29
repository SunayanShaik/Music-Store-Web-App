
package cs636.music.presentation;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Cart;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;


/**
 * 
 *         This class tests the system.
 */
public class SystemTest {

	private CatalogService catalogService;
	private SalesService salesService;
	private String inFile;
	private Cart cart;

	public SystemTest(String inFile) throws Exception {
		this.inFile = inFile;
		catalogService = MusicSystemConfig.getCatalogService();
		salesService = MusicSystemConfig.getSalesService();
	}
	
	// Note that main runs only in client-server mode, to start up execution.
	// The corresponding start-up web code is in the servlet init() method
	public static void main(String[] args) {
		String salesDbName = null;
		String catalogDbName = null;
		String inFile = null;
		if (args.length == 0) {
			salesDbName = "hsql"; //--------------------------------------------------
			catalogDbName = "hsql";//--------------------------------------------
			// files holding which DB to use for sales, which for catalog--	
			// These are in the top-level web deployed directory, shared with web app
			String path1 = "WebContent/catalogDBName";
			String path2 = "WebContent/salesDBName";
			inFile = "WebContent/test.dat"; 
			try {
			Scanner in = new Scanner(new File(path1));
			catalogDbName = in.next(); // file has one token
			System.out.println("Using catalog DB "+catalogDbName);
			in.close();
			} catch (Exception e) {
				System.out.println("Can't access catalogDBName file, using hsql as DB for catalog");
			}
			try {
				Scanner in = new Scanner(new File(path2));
				salesDbName = in.next(); // file has one token
				System.out.println("Using sales DB "+salesDbName);
				in.close();
			} catch (Exception e) {
				System.out.println("Can't access salesDBName file, using hsql as DB for sales");
			}
		} else if (args.length == 3) {
			salesDbName =  args[0];
			catalogDbName = args[1];
			inFile = args[2];
		} else  {
			System.out.println("usage: no-args, or java <salesDbName> <catalogDbName> <inputFile> ");
			return;
		}
		System.out.println("SystemTest starting, using "+ catalogDbName + " for catalog services and "
				+ salesDbName + " for sales services");
		SystemTest test = null;
		try {
			// configure both parts of system for this overall test
			MusicSystemConfig.configureCatalogService(catalogDbName);
			MusicSystemConfig.configureSalesService(salesDbName);
			test = new SystemTest(inFile);  
			test.run();
			System.out.println("Run complete, exiting");
		} catch (Exception e) {
			System.out.println("Error in run of SystemTest: " );
			System.out.println(MusicSystemConfig.exceptionReport(e));
			e.printStackTrace();
		}
	}

	public void run() throws Exception {
		String command = null;
		Scanner in = new Scanner(new File(inFile));
		while ((command = getNextCommand(in)) != null) {
			System.out.println("\n\n*************" + command
					+ "***************\n");
			if (command.equalsIgnoreCase("i")) { // admin init db
				System.out.println("Initializing system");				
				this.salesService.initializeDB();
				this.catalogService.initializeDB();
			} else if (command.equalsIgnoreCase("gp")) // get list of CDs
			{
				Set<Product> cdList = catalogService.getProductList();
				if (cdList != null)
					PresentationUtils.displayCDCatlog(cdList, System.out);

			} else if (command.startsWith("gui")) { // get info on user
				String usr = getTokens(command)[1];
				UserData user = salesService.getUserInfoByEmail(usr);
				if (user == null)
					System.out.println("\nNo such user" + usr +"\n");
				else
					PresentationUtils.displayUserInfo(user, System.out);
			} else if (command.startsWith("gpi")) { // get info for product
				String productCode = getTokens(command)[1];
				Product product = catalogService.getProductByCode(productCode);
				if (product == null)
					System.out.println("\nNo such product\n");
				else
					PresentationUtils.displayProductInfo(product, System.out);
			} else if (command.startsWith("ureg")) { // ureg fname lname email
				String userInfo[] = getTokens(command); // whitespace delim.
														// tokens
				System.out.println("Registering user: " + 
						userInfo[1] + " " + userInfo[2] + " " + userInfo[3]);
				salesService.registerUser(userInfo[1], userInfo[2], userInfo[3]);
			} else if (command.startsWith("gti")) {
				// gti prodcode:  list track info for CD
				String productCode = getTokens(command)[1];
				Product product = catalogService.getProductByCode(productCode);
				if (product == null)
					System.out.println("\nNo such product\n");
				else
					PresentationUtils.displayTracks(product, System.out);

			} else if (command.startsWith("dl")) {
				// record download by user x of product y (some track)
				String params[] = getTokens(command);
				String userEmail = params[1];
				String productCode = params[2];
				int trackNum = Integer.parseInt(params[3]);
				Product product = catalogService.getProductByCode(productCode);
				if (product == null)
					System.out.println("\nNo such product\n");
				else {
					Set<Track> tracks = product.getTracks();
					UserData user = salesService.getUserInfoByEmail(userEmail);
					if (user == null)
						System.out.println("\nNo such user\n");
					else {
						Track track0 = null;
						for (Track track: tracks ) {
							if (track.getTrackNumber() == trackNum){
								track0 = track;
							}
						}
						if ( track0 != null){
							System.out.println("Recording download for user");
							catalogService.addDownload(user.getEmailAddress(), track0);
						} else {
							System.out.println("\nNo such track\n");
						}
					}
				}
			} else if (command.startsWith("cc")) { // create cart
				cart = catalogService.createCart();
				System.out.println("\n cart created ");

			} else if (command.startsWith("sc")) { // show cart
				System.out.println("\n Now displaying Cart...");
				Set<CartItemData> cartInfo = catalogService.getCartInfo(cart);
				PresentationUtils.displayCart(cartInfo, System.out);

			} else if (command.startsWith("co")) { // checkout email address
				String params[] = getTokens(command);
				String email = params[1];
				String addr = params[2];
				UserData user = salesService.getUserInfoByEmail(email);		
				if (user == null)
					System.out.println("\nNo such user\n");
				else {
					salesService.addUserAddress(user.getId(), addr);
					user = salesService.getUserInfoByEmail(email);
					InvoiceData invoice = salesService.checkout(cart, user.getId());
					System.out.println("\n CDs Ordered...");
					PresentationUtils.displayInvoice(invoice, System.out);
					System.out.println();
				}

			} else if (command.startsWith("addli")) { // add to cart
				String params[] = getTokens(command);
				Product product = catalogService.getProductByCode(params[1]);
				if (product == null)
					System.out.println("\nNo such product\n");
				else {
					catalogService.addItemtoCart(product.getId(), cart, 1);
					System.out.println("\n Added to Cart..");
				}
			} else if (command.startsWith("setproc")) // process invoice
			{
				int params[] = getIntTokens(command);
				this.salesService.processInvoice(params[1]);
			} else if (command.equalsIgnoreCase("gi")) // get list of invoices
			{
				Set<InvoiceData> inv = salesService.getListofInvoices();
				PresentationUtils.displayInvoices(inv, System.out);
			} else if (command.startsWith("gd")) // get list of downloads
			{
				Set<DownloadData> dList = catalogService.getListofDownloads();
				PresentationUtils.downloadReport(dList, System.out);
			} else
				System.out.println("Invalid Command: " + command);
			System.out.println("----OK");
		}
		in.close();
	}

	// Return line or null if at end of file
	public String getNextCommand(Scanner in) throws IOException {
		String line = null;
		try {
			line = in.nextLine();
		} catch (NoSuchElementException e) { } // leave line null
		return (line != null) ? line.trim() : line;
	}
		
	// use powerful but somewhat mysterious split method of String
	private String[] getTokens(String command) {
		return command.split("\\s+"); // white space
	}

	private int[] getIntTokens(String command) {
		String tokens[] = getTokens(command);
		int returnValue[] = new int[tokens.length];
		for (int i = 1; i < tokens.length; i++)
			// skipping 0th, not an int
			returnValue[i] = Integer.parseInt(tokens[i]);
		return returnValue;
	}

}
