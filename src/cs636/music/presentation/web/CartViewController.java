package cs636.music.presentation.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Track;
import cs636.music.service.CatalogService;
import cs636.music.service.data.CartItemData;

/**
 * * CartViewController: implements all the task related to cart.
	-cartPageBuild: build the cart page
	-DeleteCart: delete the cart
	-handleRequest: creates cart also product is added to the cart
	-InvoicetoSales: for genarating invoices I do Total cost calculation and save attribute in the session.
	-UpdateCart: it updates the quantity of the product in the cart.
	-CalculateTotal: does BigDecimal Calculations
 * 
 * 
 * ***/

public class CartViewController implements Controller{
 
	private CatalogService catalogService;
	public CartViewController(CatalogService catalogServiceAPI){
		catalogService = catalogServiceAPI;
	}

	@Override
public String handleRequest(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		 Cart cart = null;
		 Track track = null;
		try{
			HttpSession session = request.getSession();
			cart = (Cart) session.getAttribute("cart");
			if(cart == null){/* first time product is added*/
				cart = catalogService.createCart();
				session.setAttribute("cart", cart);
				}
			/*adding to the cart*/
			Set<Track> tracks = (Set<Track>)session.getAttribute("track");
			/* getting single track to extract product info from the set */
			for(Track temptrack: tracks){
				track = temptrack;
				break;
			}
			catalogService.addItemtoCart(track.getProduct().getId(),cart,1);
			String cartStatus = (String)session.getAttribute("cartStatus");
		}catch(Exception e){
			System.out.println("err from CartViewController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from CartViewController.handleRequest");
		}
		return "success";
	}
	
	public String cartPageBuild(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			HttpSession session = request.getSession();
			CartItem dummyCartItem = null;
			Cart cart =(Cart)session.getAttribute("cart");
			if(cart== null){
				cart = catalogService.createCart();
				session.setAttribute("cart", cart);
			}
			Set<CartItem> tempCartItem = cart.getItems();
			for(CartItem item: tempCartItem){
					dummyCartItem = item;
					break;
			}
			if(dummyCartItem != null){
				Set<CartItemData> cartItemData  = catalogService.getCartInfo(cart);
				request.setAttribute("cartItemData",cartItemData);
				ArrayList<BigDecimal> amount = CalculateTotal(cartItemData);
				/*to do total amount calculation in the jsp*/
				BigDecimal totalAmount = amount.get(amount.size()-1) ;
				amount.remove(amount.size()-1);
				request.setAttribute("totalAmount",totalAmount);
				request.setAttribute("amount", amount);
	}
			/* to test if empty then show message in jsp*/
			request.setAttribute("dummyCartItem",dummyCartItem);
		}catch(Exception e){
			System.out.println("err from CartViewController.cartPageBuild");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from CartViewController.cartPageBuild");
		}
		return "success";
	}
	
	
	private ArrayList<BigDecimal> CalculateTotal(Set<CartItemData> dataItem){
		BigDecimal totalSum = BigDecimal.ZERO;
		BigDecimal total= BigDecimal.ZERO;
		ArrayList<BigDecimal> totalAmount = new ArrayList<BigDecimal>();
		for(CartItemData tempItemData: dataItem){
			 total = tempItemData.getPrice().multiply(new BigDecimal
					(tempItemData.getQuantity()));
			 totalAmount.add(total);
			 totalSum = totalSum.add(total);
		}
		totalAmount.add(totalSum);
		return totalAmount;
	} 

	public String UpdateCart(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			HttpSession session = request.getSession();
			Long id = Long.parseLong(request.getParameter("id").toString());
			int quantity= Integer.parseInt(request.getParameter("quantity").toString());
			Cart cart= (Cart)session.getAttribute("cart");
			catalogService.changeCart(id, cart, quantity);
			//session.setAttribute("cart", cart);
		}catch(Exception e){
			System.out.println("err from CartViewController.UpdateCart");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from CartViewController.UpdateCart");
		}
		return "success"; 
	}
	
	public String DeleteCart(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			HttpSession session = request.getSession();
			Long id = Long.parseLong(request.getParameter("id").toString());
			int quantity= 0;
			Cart cart= (Cart)session.getAttribute("cart");
			catalogService.changeCart(id, cart, quantity);
		//	session.setAttribute("cart", cart);
		}catch(Exception e){
			System.out.println("err from CartViewController.UpdateCart");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from CartViewController.UpdateCart");
		}
		return "success"; 
	}
	public String InvoicetoSales(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			Set<CartItemData> cartItemData  = catalogService.getCartInfo(cart);
			session.setAttribute("cartItemData",cartItemData);
			ArrayList<BigDecimal> amount = CalculateTotal(cartItemData);
			/*to do total amount calculation in the jsp*/
			BigDecimal totalAmount = amount.get(amount.size()-1) ;
			amount.remove(amount.size()-1);
			session.setAttribute("totalAmount",totalAmount);
			session.setAttribute("amount", amount);
			
		}catch(Exception e){
			System.out.println("err from CartViewController.InvoicetoSales");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from CartViewController.InvoicetoSales");
		}
		return "success";
	}
	
	
}
