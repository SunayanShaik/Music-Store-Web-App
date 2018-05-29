package cs636.music.presentation.web;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.service.SalesService;

public class SalesDispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SalesService salesService;
	private UserViewController userViewController;
	private RegisterUserController registerUserConroller;
	private InvoiceViewController invoiceViewController;
	private String responseUrl;
	
	private static final String status ="success";
	private static final String userUrl ="/sales/userForCatalog";
	private static final String catalogListenView = "/catalog/listen"; 
	private static final String registerUserUrl = "/sales/register"; 
	private static final String salesUserInvoice = "/sales/generateInvoice"; 
	private static final String catalogCartDataInvoice = "/catalog/cartDataInvoice"; 
	private static final String salesInvoiceUrl = "/sales/cartDataInvoice";
	private static final String salesInvoiceJsp = "/WEB-INF/jsp/invoice.jsp";
	private static final String salesAuthUrl = "/sales/authenticate";
//	private static final String checkoutLoginUrl = "/sales/checkoutLoginCheck";
	private static final String userRegisteration = "/WEB-INF/jsp/register.jsp";
	private static final String checkoutregisterJsp = "/WEB-INF/jsp/checkOutRegister.jsp";
	private static final String checkoutLoginUrl = "/sales/checkoutLogin";
	private static final String getCartForInvoice ="/catalog/cartDataItem";
	private static final String salesCartData = "/sales/cartData";

	
	
	
	@Override
	public void init() throws ServletException{
		if(MusicSystemConfig.getSalesService()== null){
			try{
				ServletContext context = getServletContext();
				String salesDbName = "hsql";
				String salesPath = context.getRealPath("/salesDbName");
				System.out.println("database name from: "+ salesPath);
				try{
					Scanner in = new Scanner(new File(salesPath));
					salesDbName = in.next();
					System.out.println("sales Database: " + salesDbName);
					in.close(); 
				}catch(Exception e){
					System.out.println("error reading catalogDbName file");
					System.out.println(MusicSystemConfig.exceptionReport(e));
					throw new ServletException("Err from init of salesDispactherServlet: reading salesDbName ");
					}
					MusicSystemConfig.configureSalesService(salesDbName); 
			}catch(Exception e){
				System.out.println("problem starting the databse as mentioned in file catalogDbName");
				System.out.println(MusicSystemConfig.exceptionReport(e));
				throw new ServletException("problem starting the databse as mentioned in file catalogDbName");
			}
			salesService = MusicSystemConfig.getSalesService();
			if(salesService == null)
				throw new ServletException("database not initialized");
			
			userViewController = new UserViewController(salesService);
			registerUserConroller = new RegisterUserController(salesService);
			invoiceViewController = new InvoiceViewController(salesService);
			/*
			 * ------------------------------------------------------------------------------
			 * initialize the different controllers;
			 * ----------------------------------------------------------------------------
			 * 
			 * */
		}
		
	}
	
	@Override
	public void destroy() {
		System.out.println("SalesDispatcherServlet: shutting down");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestUrl = request.getRequestURI();
		System.out.println("requested url: " +requestUrl);
		String forwardUrl= null;
	// user validation for download starts 	
		if(requestUrl.contains(registerUserUrl)){
			if(registerUserConroller.handleRequest(request, response).equals(status))
				forwardUrl = catalogListenView;
			}
		
		if(requestUrl.contains(salesAuthUrl)){// after listening to validate user registeration.
				responseUrl = registerUserConroller.CheckUser(request, response);
			if(responseUrl.equals(status)){
				forwardUrl = catalogListenView;// got the user so call the listen
				}else{
				if(responseUrl.equals(userUrl)){ // got the user from cookie
					if(userViewController.handleRequest(request, response).equals(status)){
						forwardUrl = catalogListenView;
						}else{
						forwardUrl = userRegisteration;
						}
					}else{// new user ask for registeration
						forwardUrl = userRegisteration;//catalogListenView;
				}
			} 
		}
		// user validation for download ends
		
/* check out part starts*/		
		if(requestUrl.contains(salesCartData)){///change
			/* got it from cookie*/
		
			String responseUrl = registerUserConroller.CheckUser(request, response);// user could be in session, in cookie or totally new
			if(responseUrl.equals(userUrl)){
				if(userViewController.handleRequest(request, response).equals(status)){
					if(invoiceViewController.handleRequest(request, response).equals(status)){
						forwardUrl= salesInvoiceJsp;
					}
				}else{// this is called if user is there in cookie but not in database(idiotic check)
					forwardUrl = checkoutregisterJsp;
				}
			}/*have to register or if too old get it from database*/
			else if(responseUrl.equals(userRegisteration)){
				forwardUrl = checkoutregisterJsp;
	
			}
			else{/*found it in the session*/
				if(invoiceViewController.handleRequest(request, response).equals(status)){
					forwardUrl= salesInvoiceJsp;
				}
			}
		}
		
		
		if(requestUrl.contains(checkoutLoginUrl)){
			if(registerUserConroller.handleRequest(request, response).equals(status)){
				if(invoiceViewController.handleRequest(request, response).equals(status)){
					forwardUrl= salesInvoiceJsp;
				}
			}
		}
	
		if(requestUrl.contains(salesUserInvoice)){ // called after checkout is called it save cart information in the session
			forwardUrl = getCartForInvoice;// as cart is cleared in the invoice. after done calles salesCartData 
		}
		/*checkout ends*/
		
		System.out.println("sales DispatcherServlet: forwarding to "+ forwardUrl);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(forwardUrl);
		dispatcher.forward(request, response);	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
