package cs636.music.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs636.music.config.StandAloneDataSourceFactory;
import cs636.music.domain.Product;

public class ProductDAOTest {
	private static DbUtils dbDAO;
	private static CatalogDbDAO catalogDbDAO;
	private ProductDAO productdao;
	
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
	public void setup() throws Exception {
		catalogDbDAO.initializeDb(); //  no users, etc.
		productdao = new ProductDAO();
	}

	
	@AfterClass
	public static void tearDownClass() throws Exception {
// TODO		dbDAO.close();
	}

	
	@Test
	public void testFindProductByCode() throws Exception
	{
		Connection connection = catalogDbDAO.startTransaction();
		Product p2 = productdao.findProductByCode(connection, "8601");
		assertTrue(1 == p2.getId());
		catalogDbDAO.commitTransaction(connection);
	}
}