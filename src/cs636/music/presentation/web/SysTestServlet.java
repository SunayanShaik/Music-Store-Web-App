package cs636.music.presentation.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.presentation.SystemTest;

// Quick way to run web app without JSPs

public class SysTestServlet extends HttpServlet {

	private static final long serialVersionUID = 3971217231726348088L;
	// Initialization of servlet: runs before any request is
	// handled in the web app. 
	@Override
	public void init() throws ServletException {
	    // This servlet is optional to the system, and
		// unlike DispatcherServlet, is configured in web.xml to 
		// load when needed, i.e, sometime after DispatcherServlet, 
		// which should have called configureServices already, but let's check...
		System.out.println("starting SysTestServlet");
		if (MusicSystemConfig.getSalesService() == null) {
			System.out.println(" SysTestServlet: found that configureSalesService has not been run yet");
			try {
				ServletContext context = getServletContext();
				String salesDbName = "hsql";
				String catalogDbName = "hsql";
				try {
					String path1 = context.getRealPath("/salesDbName");
					System.out.println("salesDbName file path = " + path1);
					Scanner in = new Scanner(new File(path1));
					salesDbName = in.next(); // file has one token
					System.out.println("Using sales DB "+salesDbName);
					String path2 = context.getRealPath("/catalogDbName");
					System.out.println("catalogDbName file path = " + path2);
					in.close();
					Scanner in2 = new Scanner(new File(path2));
					catalogDbName = in2.next(); // file has one token
					System.out.println("Using sales DB "+catalogDbName);
					in2.close();
				} catch (Exception e) {
					System.out.println("Can't read dbName file(s), defaulting to hsql");
				}
				MusicSystemConfig.configureCatalogService(catalogDbName); 
				MusicSystemConfig.configureSalesService(salesDbName); 
			} catch (Exception e) {
				System.out.println("SysTestServlet: configureServices threw");
				System.out.println(MusicSystemConfig.exceptionReport(e));
			}

		}
	}
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		String result;

		String contextPath = context.getContextPath();
		try {
			// in top-level directory deployed
			String path = context.getRealPath("/test.dat");
			System.out.println("infile path = "+path);
			SystemTest test = new SystemTest(path);
			test.run();
			result = "Success";
		} catch (Exception e) {
			System.out.println("SysTestServlet: doGet threw");
			result = MusicSystemConfig.exceptionReport(e);
			System.out.println(result);
		}

		System.out.println(contextPath);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		System.out.println("in doGet");
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet running SystemTest</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println(" <h2> SystemTest Result </h2>");
		out.println("<p> "+ result + "</p>");
		out.println("<p> for more info, see tomcat log </p>");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.close();
	}

}
