package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Cart;
import cs636.music.service.SalesService;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;

/**
 * InvoiceViewController: called after user Checkout from cart
	-handleRequest : updates Invoice table 
 * 
 * **/

public class InvoiceViewController implements Controller{
	private SalesService salesService;
	public InvoiceViewController(SalesService salesServiceAPI){
		salesService = salesServiceAPI;
	}
	
	public String handleRequest(HttpServletRequest request, 
			HttpServletResponse response)
	throws IOException, ServletException{
		try{
			HttpSession session = request.getSession();
			Cart cart =(Cart) session.getAttribute("cart");
			UserData userData =(UserData)session.getAttribute("userData");
			Long userId = userData.getId();
			InvoiceData  userInvoiceData = salesService.checkout(cart, userId);
			request.setAttribute("userInvoiceData", userInvoiceData);
		}catch(Exception e){
			System.out.println("err from InvoiceViewController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from InvoiceViewController.handleRequest");
		}
		return "success";
	}
	
}
