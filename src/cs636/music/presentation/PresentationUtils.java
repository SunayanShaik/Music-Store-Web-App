package cs636.music.presentation;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

public class PresentationUtils {

	public static void displayInvoices(Set<InvoiceData> invList, PrintStream out) {
		out.println("---------- Invoices--------------");
		out.println("\nId\tUser\tAddress\tInvoice Date\tAmount");
		for (InvoiceData i : invList) {
			out.print("\n" + i.getInvoiceId() + "\t"
					+ i.getUserFullName() + "\t" + i.getUserAddress()  + "\t"
					+ i.getInvoiceDate() + "\t" + i.getTotalAmount() );
		}
		out.println("\n-------------------------------------");
	}
	
	public static void displayInvoice(InvoiceData inv, PrintStream out) {	
		out.println("\nId\tCustomer\tAddress\tInvoice Date\tAmount");
		out.print("\n" + inv.getInvoiceId() + "\t"
				+ inv.getUserFullName() + "\t" + inv.getUserAddress()  + "\t" +
				inv.getInvoiceDate() + "\t" + inv.getTotalAmount() + "\n" );
	}
	
	public static void displayProductInfo(Product p, PrintStream out) {
		out.println("----------PRODUCT INFORMATION--------------\n");
		out.print("\nProductCode : " + p.getCode() + "\n Description : "
				+ p.getDescription() + "\n Price : " + p.getPrice());

		out.println("\n-----------------------------------------\n");
	}

	public static void displayCDCatlog(Set<Product> cds, PrintStream out) {
		out.println("------------CD CATALOG-----------------");
		out.println("\n Prod Code \t Product Description");
		for (Product cd : cds) {
			out.print("\n" + cd.getCode() + "\t" + cd.getDescription());
		}
		out.println("\n-------------------------------------");
	}

	public static void downloadReport(Set<DownloadData> download, PrintStream out) {
		out.println("\n-----------------Download Report--------------");
		out.println("\nProd Code\tTrack title\tUserId\tDL Date");
		for (DownloadData d : download) {

			out.print("\n\t" 
					+ d.getProductCode() + "\t" + d.getTrackTitle()
					+ "\t " + d.getEmailAddress() + "\t"
					+ d.getDownloadDate());
		}
		out.println("\n---------------------------------------------------");
	}

	public static void displayCart(Set<CartItemData> cart, PrintStream out) {
		out.println("\n-----------------C A R T--------------");
		if (cart == null || cart.isEmpty())
			out.println("Nothing in cart!");
		else {
			out.println("\n\tProdId \tProdCode \tQuantity\t Price");
			for (CartItemData item : cart) {
				out.print("\n\t" + item.getProductId() + "\t" + item.getCode() + "\t" +
						+ item.getQuantity() + item.getPrice());
			}
		}
		out.println("\n----------------------------------------");
	}

	public static void displayTracks(Product product, PrintStream out) {
		Set<Track> tracks = product.getTracks();
		out.println("\n-----------------TRACKS for " + product.getCode()
				+ "--------------");
		out.println("\n\tTrackNo\tFileName\tTitle\tProd Code");
		for (Track t : tracks) {
			out.print("\n\t" + t.getTrackNumber() + "\t"
					+ t.getSampleFilename() + "\t" + t.getTitle() + "\t "
					+ product.getCode());
		}
		out.println("\n---------------------------------------------------");
	}

	public static void playTrack(Track track, PrintStream out) {
		out.println("\n---------------------------------------------------");
		out.println("\n Track URL: " + MusicSystemConfig.SOUND_BASE_URL +
				track.getProduct().getCode() + "/" + track.getSampleFilename());
		out.println("\n ...PLAYING...TRACK..." + track.getTitle() + "...");
		out.println("\n---------------------------------------------------");
	}

	public static void displayUserInfo(UserData u, PrintStream out) {
		out.println("\n---------------------------------------------------");
		out.println("\n USER INFORMATION \n");
		out.println("\n Name:" + u.getFirstname() + " " + u.getLastname());
		out.println("\n Email Addr : " + u.getEmailAddress());
		out.println("\n---------------------------------------------------");
	}

	public static String readEntry(Scanner in, String prompt)
			throws IOException {
		System.out.println(prompt + ": ");
		return in.nextLine().trim();
	}

}
