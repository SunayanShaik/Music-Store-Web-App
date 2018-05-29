package cs636.music.dao;
//Example JUnit4 test 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs636.music.config.StandAloneDataSourceFactory;
import cs636.music.domain.Download;
import cs636.music.domain.Product;

public class DownloadDAOTest {
	private static DbUtils dbDAO;
	private static CatalogDbDAO catalogDbDAO;
	private DownloadDAO downloaddao;
	private ProductDAO productdao;
	private static String FAKE_USER_EMAIL_ADDRESS = "foo@bar.com";
	
	@BeforeClass
	public static void setUpClass() throws SQLException {
		// we use HSQLDB as the db for testing
		// Note: need to start and load it first
		DataSource ds = null;
		try {
		   ds = StandAloneDataSourceFactory.createDataSource("hsql"); 
		} catch (Exception e) {
			System.out.println("Failed to create DataSource for hsql in JUnit teset");
		}
		dbDAO = new DbUtils();
		catalogDbDAO = new CatalogDbDAO(dbDAO, ds);	
	}

	@Before
	// each test runs in its own transaction, on same db setup
	public void setup() throws SQLException {
		catalogDbDAO.initializeDb(); 
		productdao = new ProductDAO();
		downloaddao = new DownloadDAO(productdao);
	}

	@After
	public void tearDown() {
	}
	@AfterClass
	public static void tearDownClass() throws SQLException {

	}
	
	@Test
	public void testInsertDownload() throws SQLException
	{
		Connection connection = catalogDbDAO.startTransaction();
		Product p = productdao.findProductByCode(connection, "8601");
		
		Download d = new Download();
		d.setDownloadDate(new Date());
		d.setEmailAddress(FAKE_USER_EMAIL_ADDRESS);
		d.setTrack(p.getTracks().iterator().next());
		
		downloaddao.insertDownload(connection, d);
		catalogDbDAO.commitTransaction(connection);
	}
	
	@Test
	public void testFindAllDownloads() throws SQLException {
		Connection connection = catalogDbDAO.startTransaction();
		Product p = productdao.findProductByCode(connection, "8601");
		
		Download d = new Download();
		d.setDownloadDate(new Date());
		d.setEmailAddress(FAKE_USER_EMAIL_ADDRESS);
		d.setTrack(p.getTracks().iterator().next());
		downloaddao.insertDownload(connection, d);
		
		Set<Download> downloads = downloaddao.findAllDownloads(connection);
		assertTrue(downloads.size()==1);
		assertEquals(FAKE_USER_EMAIL_ADDRESS, downloads.iterator().next().getEmailAddress());
		catalogDbDAO.commitTransaction(connection);

	}
}
