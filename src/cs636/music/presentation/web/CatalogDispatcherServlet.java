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
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.service.CatalogService;

public class CatalogDispatcherServlet extends HttpServlet{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3086033693781710620L;
	private CatalogService catalogServiceHandler;
	private static final String status ="success";
	
	private static final String welcomeurl = "/catalog/welcome.jsp";
	private static final String welcomeView = "/welcome.jsp";
	private static final String catalogView = "/catalog/catalog";
	private static final String catalogViewJsp = "/WEB-INF/jsp/catalog.jsp";
	private static final String trackViewurl = "/catalog/track.jsp";
	private static final String trackViewjsp = "/WEB-INF/jsp/track.jsp";
	private static final String downloadurl = "/catalog/download";
	private static final String listenUrl = "/catalog/listen";
	private static final String productUrl = "/catalog/product.jsp";
	private static final String productJsp = "/WEB-INF/jsp/product.jsp";
	private static final String addToCartUrl = "/catalog/addtocart";
	private static final String cartPageUrl = "/catalog/cartpage";
	private static final String cartPageJsp = "/WEB-INF/jsp/cart.jsp";
	private static final String cartUpdateUrl = "/catalog/updateCart";
	private static final String cartDeleteUrl = "/catalog/deleteCart";
	private static final String salesInvoiceUrl = "/catalog/cartDataInvoice";
	private static final String salesInvoiceUrlReturn = "/sales/cartDataInvoice";
	private static final String salesAuthUrl = "/sales/authenticate";
	private static final String salesCartItemData = "/catalog/cartDataItem";
	private static final String salesCartData = "/sales/cartData";
	
	private CatalogViewController catalogViewController;
	private TrackViewController trackViewController;
	private ListenViewController listenViewController;
	private ProductViewController productViewController;
	private CartViewController cartViewController;
	
@Override
public void init() throws ServletException{
	if(MusicSystemConfig.getCatalogService()== null){
		try{
			ServletContext context = getServletContext();
			String catalogDbName = "hsql";
			String catalogPath = context.getRealPath("/catalogDbName");
			System.out.println("database name from: "+catalogPath);
			try{
				Scanner in = new Scanner(new File(catalogPath));
				catalogDbName = in.next();
				System.out.println("catalog Database: " + catalogDbName);
				in.close(); 
			}catch(Exception e){
				System.out.println("error reading catalogDbName file");
				System.out.println(MusicSystemConfig.exceptionReport(e));
				throw new ServletException(e);
				}
				MusicSystemConfig.configureCatalogService(catalogDbName);
		}catch(Exception e){
			System.out.println("problem starting the databse as mentioned in file catalogDbName");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException(e);
		}
	}
		catalogServiceHandler = MusicSystemConfig.getCatalogService();
		if(catalogServiceHandler == null)
			throw new ServletException("database not initialized");
		
			catalogViewController = new CatalogViewController(catalogServiceHandler);
			trackViewController = new TrackViewController(catalogServiceHandler);
			listenViewController = new ListenViewController(catalogServiceHandler);
			productViewController = new ProductViewController(catalogServiceHandler);
			cartViewController = new CartViewController(catalogServiceHandler);
}

@Override
public void destroy() {
	System.out.println("catalogDispatcherServlet: shutting down");
}

@Override
public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	String requestUrl = request.getRequestURI();
	HttpSession session = request.getSession();
	System.out.println("requested url: " +requestUrl);
	String forwardUrl= null;
	/* checking for user bean : if present that means user has loggedin*/
	//--------------------------------------------------------------------
	if(requestUrl.contains(welcomeurl))
		forwardUrl = welcomeView;
	
	if(requestUrl.contains(catalogView)){
		if(catalogViewController.handleRequest(request, response).equals(status))
			forwardUrl = catalogViewJsp;
	}
	
	if(requestUrl.contains(productUrl)){
		if(productViewController.handleRequest(request, response).equals(status))
			forwardUrl = productJsp;
	}
	
	
	if(requestUrl.contains(trackViewurl)){
		if(trackViewController.handleRequest(request, response).equals(status))
			forwardUrl = trackViewjsp;
	}
	
	if(requestUrl.contains(downloadurl)){
		if(listenViewController.handleRequest(request, response).equals(status))
			forwardUrl = salesAuthUrl;
	}
	
	
	if(requestUrl.contains(listenUrl)){
		forwardUrl = listenViewController.listenRequest(request, response);
	}
	
	if(requestUrl.contains(addToCartUrl)){
		if(cartViewController.handleRequest(request, response).equals(status)){
			String id = (String)request.getParameter("id");
			forwardUrl = catalogView;
		}
	}
	
	if(requestUrl.contains(cartPageUrl)){
		if(cartViewController.cartPageBuild(request, response).equals(status))	
			forwardUrl = cartPageJsp;
	}
	
	if(requestUrl.contains(cartUpdateUrl)){
		if(cartViewController.UpdateCart(request, response).equals(status))
			forwardUrl = cartPageUrl ;
	}
	
	if(requestUrl.contains(cartDeleteUrl)){
		if(cartViewController.DeleteCart(request, response).equals(status))
			forwardUrl = cartPageUrl;
	}
	
	if(requestUrl.contains(salesInvoiceUrl)){
		if(cartViewController.InvoicetoSales(request, response).equals(status))
			forwardUrl = salesInvoiceUrlReturn;
	}
	
	if(requestUrl.contains(salesCartItemData)){
		  cartViewController.InvoicetoSales(request, response);// save the cartDataItem in the session 
		  forwardUrl = salesCartData;
	}
	
	if(forwardUrl.contains("sound")){
		response.sendRedirect(request.getContextPath()+forwardUrl);
	}
	else{
	System.out.println("catalog DispatcherServlet: forwarding to "+ forwardUrl);
	RequestDispatcher dispatcher = getServletContext()
			.getRequestDispatcher(forwardUrl);
	dispatcher.forward(request, response);
	}
}

@Override
public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	doGet(request, response);
}

}
