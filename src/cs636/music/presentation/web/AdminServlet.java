package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.ServiceException;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SalesService salesService;
	private CatalogService catalogService; // for Downloads

	private static final String ADMIN_JSP_DIR = "/WEB-INF/admin/";
	private static final String ADMIN_URL = "/music3/adminController/adminBlock";
	private static final String allReportJsp = "/WEB-INF/admin/allReport.jsp";
	private static final String allInvoiceJsp = "/WEB-INF/admin/totalinvoice.jsp";
	private static final String allDownloadJsp = "/WEB-INF/admin/totaldownload.jsp";

	// Initialization of servlet: runs before any request is
	// handled in the web app. 
	@Override
	public void init() throws ServletException {
		System.out.println("Starting admin servlet initialization");
		System.out.println("admin servlet init disabled");
		// the user-pages dispatcher servlet(s) should have initialized system first
		// by load-on-startup settings in web.xml
		// but if not, call MusicSystemConfig here to set up the system (temporarily)
		salesService = MusicSystemConfig.getSalesService();
		catalogService = MusicSystemConfig.getCatalogService();
		if (salesService == null) {
			System.out.println("!!!!!!!!!!!!!!!AdminServlet initialization problem!!!!!!!!!");
			System.out.println("AdminServlet needs to call MusicSystemConfig if dispatcher servlet is not implemented");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = null;
		String requestURI = request.getRequestURI();
		System.out.println("doGet requestURI = " + requestURI);
		String uname = (String) request.getSession().getAttribute("adminUser");
		if (requestURI.contains("listVariables.html")) {
			url = ADMIN_JSP_DIR + "listVariables.jsp"; // allow this without login
		} else if (requestURI.contains("logout.html")) { // and this
			System.out.println("logging out...");
			request.getSession().invalidate(); // drop session
			url = ADMIN_JSP_DIR + "logout.jsp";
		} else if (uname == null && requestURI.contains(ADMIN_URL)) {
			requestURI = "adminWelcome.html"; // if not logged in, force login
			url = handleAdminLogin(request, response); // you can change these names if you want
			//url = ADMIN_JSP_DIR + "adminWelcome.jsp";
		} else if(uname!=null && requestURI.contains(ADMIN_URL)) {
			url = ADMIN_JSP_DIR + "adminWelcome.jsp";
		} else if (requestURI.contains("adminWelcome.html")) { // but this shows the expected functionality
			url = handleAdminLogin(request, response);
		} else if (requestURI.contains("processInvoices.html")) {
			url = handleProcessInvoices(request, response);
		} else if (requestURI.contains("initDB.html")) {
			url = initializeDB(request, response);
		} else if (requestURI.contains("displayReports.html")) {
			url = handleDisplayReports(request, response);
		} else if (requestURI.contains("viewInvoice.html")) {
			url = handleUserInvoice(request, response);
		} else if (requestURI.contains("processInvoice.html")) {
			url = handleProcessInvoice(request, response);
		} else if(requestURI.contains("logout.html")) {
			url = handleLogoutPage(request, request);
		} else if(requestURI.contains("welcome.html")) {
			url = handleWelcomeHomePage(request, response);
		}else if(requestURI.contains("totalinvoice.jsp")){
			url = handleTotalInvoice(request,response);
		} else if(requestURI.contains("totaldownload.jsp")){
			url = handleTotalDownload(request,response);
		}
		else {
			System.out.println("Unknown request URI: " + requestURI);
			throw new ServletException("Unknown request URI: " + requestURI);
		}

		// Need to check what needs to be done if the url is null
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);

	}

	private String handleWelcomeHomePage(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		url = "/welcome.jsp";
		return url;
	}

	private String handleLogoutPage(HttpServletRequest request, HttpServletRequest request2) {
		String url = null;
		request.getSession().setAttribute("adminUser", null);
		url = "/welcome.jsp";
		return url;
	}

	/*
	 * Returns the url of the page that needs to be forwarded to
	 */
	private String handleAdminLogin(HttpServletRequest request, HttpServletResponse response) {
		String url = null; // URL of admin menu if successful, else back to admin login page
		String username = null;
		String password = null;
		String error = null;
		HttpSession session = request.getSession();
		username = request.getParameter("username");
		password = request.getParameter("password");
		if(username!= null && password != null){
			try {
				Boolean adminUser = salesService.checkAdminLogin(username, password);
				
				if(adminUser==false) {
					url = ADMIN_JSP_DIR + "error.jsp";
				} else {
					session.setAttribute("adminUser", username);
					Cookie cookie = new Cookie("username",username);
					cookie.setMaxAge(-1);
					cookie.setPath("/");
					response.addCookie(cookie);
					url = ADMIN_JSP_DIR + "adminWelcome.jsp";
				}
			} catch (ServiceException e) {
				error = "Invalid credentials" + e;
				request.setAttribute(error, error);
				url = ADMIN_JSP_DIR + "error.jsp";
				System.out.println("Error in username, password in AdminService login");
				e.printStackTrace();
			} 

		} else {
			url = ADMIN_JSP_DIR + "adminLoginPage.jsp";
		}
		return url;
	}

	private String handleProcessInvoices(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String url = null;
		HttpSession session = request.getSession();
		Set<InvoiceData> unprocInvoiceList;
		try {
			unprocInvoiceList = salesService.getListofUnprocessedInvoices();
		} catch (ServiceException e) {
			System.out.println("err from the service layer: salesService.getListofUnprocessedInvoices()");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from the service layer: AdminServlet.handleProcessInvoices");
		}
		request.setAttribute("unprocInvList", unprocInvoiceList);
		url = ADMIN_JSP_DIR + "UnprocessedInvoices.jsp" ;
		return url;
	}

	private String initializeDB(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = null; // or whatever you call it
		try {
			salesService.initializeDB();
			catalogService.initializeDB();
			Cookie [] cookielist = request.getCookies();
			if(cookielist!= null){
				for (Cookie cookie : cookielist) {
				    cookie.setValue("");
				    cookie.setMaxAge(0);
				    cookie.setPath("/");
				    response.addCookie(cookie);
				}
			}
			HttpSession session = request.getSession(false);
			if (session != null) {
			    session.invalidate();
			}
		} catch (ServiceException e) {
			System.out.println("Init DB error");
			System.out.println(MusicSystemConfig.exceptionReport(e));
		}
		url = ADMIN_JSP_DIR + "initDB.jsp";
		return url;
	}

	private String handleDisplayReports(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		url = allReportJsp;
		return url;
	}
	
	private String handleTotalInvoice(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
		Set<InvoiceData> invoiceDataSet = salesService.getListofInvoices();
		request.setAttribute("invoiceDataSet", invoiceDataSet);
		for(InvoiceData i : invoiceDataSet){
			System.out.println("$$$$$$$$$$$$$$$..."+ i.isProcessed());
			}
		return allInvoiceJsp; 
		}catch(Exception e){
			System.out.println("err from AdminServlet.handleTotalInvoice");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from AdminServlet.handleTotalInvoice");
		}
	}
	
	private String handleTotalDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			Set<DownloadData> downloadDataSet = catalogService.getListofDownloads();
			request.setAttribute("downloadDataSet", downloadDataSet);
			return allDownloadJsp;
			
		}catch(Exception e){
			System.out.println("err from AdminServlet.handleTotalDownload");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from AdminServlet.handleTotalDownload");
		}
	}

	private String handleUserInvoice(HttpServletRequest request, HttpServletResponse response)throws ServletException {
		String url = null;
		String Id = request.getParameter("id");
		long invoiceId = Long.parseLong(Id);
		InvoiceData invoiceInfo = null;
		try {
			invoiceInfo = salesService.findInvoice(invoiceId);
		} catch (Exception e) {
			System.out.println("err from the service layer: salesService.findInvoice(invoiceId)");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from the service layer: salesService.findInvoice(invoiceId)");
		}
		request.setAttribute("invoiceInfo", invoiceInfo);
		url = ADMIN_JSP_DIR + "viewInvoice.jsp" ;
		return url;
	}

	private String handleProcessInvoice(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		String processInvoice = request.getParameter("id");
		long invoiceId = Long.parseLong(processInvoice);
		try {
			salesService.processInvoice(invoiceId);
		} catch (ServiceException e) {
			System.out.println("err from the service layer: salesService.processInvoice(invoiceId)");
			System.out.println(MusicSystemConfig.exceptionReport(e));
		}
		url = ADMIN_JSP_DIR + "processInvoice.jsp";
		return url;
	}

}