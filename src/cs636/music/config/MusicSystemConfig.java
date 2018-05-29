package cs636.music.config;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.naming.InitialContext;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

import cs636.music.dao.AdminUserDAO;
import cs636.music.dao.CatalogDbDAO;
import cs636.music.dao.DbUtils;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.LineItemDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.dao.SalesDbDAO;
import cs636.music.dao.UserDAO;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;

/**
 * @author Betty O'Neil
 *
 *         Configure the service objects, shut them down
 * 
 */

public class MusicSystemConfig {
	public static final String SOUND_BASE_URL = "http://www.cs.umb.edu/cs636/music1-setup/sound/";
	// the service objects in use, representing all lower layers to the app
	private static SalesService salesService;
	private static CatalogService catalogService;
	// the lower-level DAO objects--
	// for Sales DB--
	private static UserDAO userDao;
	private static AdminUserDAO adminUserDao;
	private static InvoiceDAO invoiceDao;
	private static LineItemDAO lineItemDao;
	// for Catalog DB--
	private static ProductDAO productDao;
	private static DownloadDAO downloadDao;

	private static DbUtils dbDAO;
	private static CatalogDbDAO catalogDbDAO;
	private static SalesDbDAO salesDbDAO;
	
	// set up service API, data access objects for catalogDB
	// web.xml ensures that the catalog servlet initializes before the sales servlet
	public static void configureCatalogService(String catalogDbName)
			throws Exception {
		dbDAO = new DbUtils();		
		adminUserDao = new AdminUserDAO(dbDAO);

		// configure service layer and DAO objects--
		// The service objects get what they need at creation-time
		// This is known as "constructor injection"
		DataSource catalogDs = null;
		try {
			catalogDs =  obtainDataSource(catalogDbName);
				
			catalogDbDAO = new CatalogDbDAO(dbDAO, catalogDs);
			productDao = new ProductDAO();
			downloadDao = new DownloadDAO(productDao);
			catalogService = new CatalogService(catalogDbDAO, productDao, downloadDao);

		} catch (Exception e) {
			System.out.println(exceptionReport(e));
			// e.printStackTrace(); // causes lots of output
			System.out.println("Problem with contacting DB: " + e);
			if (catalogDbName.contains("hsql"))
				System.out.println("HSQLDB not available: may need server startup");
			System.out.println("Problem with contacting DB: " + e);
			throw (e); // rethrow to notify caller (caller should print
						// exception details)
		}
	}
	// set up service API, data access objects
	public static void configureSalesService(String salesDbName)
			throws Exception {
		// configure service layer and DAO objects--
		// The service objects get what they need at creation-time
		// This is known as "constructor injection"
		DataSource salesDs = null;
		try {
			salesDs =  obtainDataSource(salesDbName);
			
			salesDbDAO= new SalesDbDAO(dbDAO, salesDs);
			userDao = new UserDAO();
			lineItemDao = new LineItemDAO(dbDAO);
			invoiceDao = new InvoiceDAO(lineItemDao, userDao, productDao);
			// some sales services need catalog service help
			salesService = new SalesService(catalogService, salesDbDAO, userDao, adminUserDao, invoiceDao);

		} catch (Exception e) {
			System.out.println(exceptionReport(e));
			// e.printStackTrace(); // causes lots of output
			System.out.println("Problem with contacting DB: " + e);
			if (salesDbName.contains("hsql"))
				System.out.println("HSQLDB not available: may need server startup");
			System.out.println("Problem with contacting DB: " + e);
			throw (e); // rethrow to notify caller (caller should print
						// exception details)
		}
	}
	// Compose an exception report
	// and return the string for callers to use
	public static String exceptionReport(Exception e) {
		String message = e.toString(); // exception name + message
		if (e.getCause() != null) {
			message += "\n  cause: " + e.getCause().toString();
			if (e.getCause().getCause() != null)
				message += "\n    cause's cause: "
						+ e.getCause().getCause().toString();
			message += exceptionStackTraceString(e);
		}
		return message;
	}
	
	private static String exceptionStackTraceString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	// Try to access tomcat's JNDI object repository for the needed DataSource object
	// A DataSource object provides a JDBC Connection pool for the webapp.
	// If the JNDI lookup fails, i.e., we're running outside tomcat, create a DataSource
	// from known properties and cs636 environment variables (ORACLE_USER, etc).
	private static DataSource obtainDataSource(String dbName) throws Exception {
		if (dbName == null) {
			System.out
					.println("MusicSystemConfig: " + dbName + " is null (defaulting to hsqldb)");
			dbName = "hsql";  // TODO change to hsqldb //can't change here error will occur in datasource lookup 
		}

		String jndiName = "jdbc/" + dbName;  // as seen in tomcat's context.xml
		DataSource dataSource = null;
		try {
			InitialContext ic = new InitialContext();  // finds tomcat's JNDI repository in its JVM					
			dataSource = (DataSource) ic.lookup("java:comp/env/" + jndiName);
		} catch (NoInitialContextException e) {
			//  not in tomcat JVM: fall through to handle as client-server execution 
		} catch (Exception e) { 
			System.out.println("Failed to lookup JndiName " + jndiName + ", error = " + e);
			return null;
		}
		if (dataSource == null) {
			System.out.println("JNDI lookup for "+ dbName +" failed, will create standalone DataSource");
			// use Apache "commons-dbcp2" support to build a DataSource from scratch
			// using cs636 environment variables like ORACLE_USER
			dataSource = StandAloneDataSourceFactory.createDataSource(dbName);  
		}
		return dataSource;
	}


	// Getters to let the apps get the business logic layer services
	// Also, the Sales service layer can get the Catalog service services and vice versa

	public static CatalogService getCatalogService() {
		return catalogService;
	}
	
	public static SalesService getSalesService() {
		return salesService;
	}
}
