
package cs636.music.dao;

import static cs636.music.dao.DBConstants.PRODUCT_TABLE;
import static cs636.music.dao.DBConstants.TRACK_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cs636.music.domain.Product;
import cs636.music.domain.Track;

/**
 * 
 * Access product and track table through this class. 
 */
public class ProductDAO {
	
	/**
	 * An Data Access Object for product table and track table 
	 * @param db the database connection
	 * @throws SQLException
	 */
	public ProductDAO() {
	}
	
	/**
	 * Find a product with its tracks from given product id 
	 * (For use from DAO to turn a product_id FK into a Product)
	 * @param productId
	 * @return the product found, return null if not found
	 * @throws SQLException
	 */
	public Product findProductById(Connection connection, long productId) throws SQLException{
		Product prod = null;
		Statement stmt = connection.createStatement();
		try 
		{
			String sqlString =  " select * from " + 
			PRODUCT_TABLE + " p, " +
			TRACK_TABLE + " t " +		
			" where p.product_id = " + productId + 
			" and p.product_id = t.product_id order by t.track_number";
			ResultSet set = stmt.executeQuery(sqlString);
			if (set.next()){ // if the result is not empty
				prod = new Product(set.getInt("product_id"), set.getString("product_code"), 
						set.getString("product_description"), set.getBigDecimal("product_price"), null ); 
				Set<Track> tracks = new HashSet<Track>();
				Track track = new Track();
				track.setId(set.getInt("track_id"));
				track.setProduct(prod);
				track.setSampleFilename(set.getString("sample_filename"));
				track.setTitle(set.getString("title"));
				track.setTrackNumber(set.getInt("track_number"));
				tracks.add(track);
				while (set.next()){// if the product has more than one track
					track = new Track();
					track.setId(set.getInt("track_id"));
					track.setProduct(prod);
					track.setSampleFilename(set.getString("sample_filename"));
					track.setTitle(set.getString("title"));
					track.setTrackNumber(set.getInt("track_number"));
					tracks.add(track);
				}
				prod.setTracks(tracks);
			}
			set.close();
		} finally {
			stmt.close();
		}
		
		return prod;
	}
	
	/**
	 * Find a product with its tracks from given product code 
	 * @param prod_id
	 * @return the product found, return null if not found
	 * @throws SQLException
	 */
	public Product findProductByCode(Connection connection, String prodCode) throws SQLException{
		Product prod = null;
		Statement stmt = connection.createStatement();
		try 
		{   String sqlString = " select * from " + 
			PRODUCT_TABLE + " p, " +
			TRACK_TABLE + " t " +		
			" where p.product_code = '" + prodCode + "'" + 
			" and p.product_id = t.product_id  order by t.track_number";
		    ResultSet set = stmt.executeQuery(sqlString);
			if (set.next()){ // if the result is not empty
				prod = new Product(set.getInt("product_id"), set.getString("product_code"), 
						set.getString("product_description"), set.getBigDecimal("product_price"), null ); 
				Set<Track> tracks = new HashSet<Track>();
				Track track = new Track();
				track.setId(set.getInt("track_id"));
				track.setProduct(prod);
				track.setSampleFilename(set.getString("sample_filename"));
				track.setTitle(set.getString("title"));
				track.setTrackNumber(set.getInt("track_number"));
				tracks.add(track);
				while (set.next()){// if the product has more than one track
					track = new Track();
					track.setId(set.getInt("track_id"));
					track.setProduct(prod);
					track.setSampleFilename(set.getString("sample_filename"));
					track.setTitle(set.getString("title"));
					track.setTrackNumber(set.getInt("track_number"));
					tracks.add(track);
				}
				prod.setTracks(tracks);
			}
			set.close();
		} finally {
			stmt.close();
		}
		
		return prod;
	}

	/**
	 * Find all product in product table without tracks
	 * @return all products in a set
	 * @throws SQLException
	 */
	public Set<Product> findAllProducts(Connection connection) throws SQLException{
		Set<Product> prods = new HashSet<Product>();
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE );
			while (set.next()){ // if the result is not empty
				Product prod = new Product(set.getInt("product_id"), set.getString("product_code"), 
						set.getString("product_description"), set.getBigDecimal("product_price"), null ); 
				prods.add(prod);
			}
			set.close();
		} finally {
			stmt.close();
		}
		
		return prods;
	}
	
	/**
	 * find a track by given track id
	 * (for use from DAO turn track_id FK value into Track)
	 * @param trackId given track id
	 * @return the track found with given track id
	 * @throws SQLException
	 */
	public Track findTrackByTID(Connection connection, int trackId) throws SQLException{
		Product prod;
		Track track_found = null;
		Statement stmt = connection.createStatement();
		try 
		{
			String sqlString =  " select * from " + 
			PRODUCT_TABLE + " p, " +
			TRACK_TABLE + " t " +		
			" where t.track_id = " + trackId + 
			" and p.product_id = t.product_id order by t.track_number";
			ResultSet set = stmt.executeQuery(sqlString);
			if (set.next()){ // if the result is not empty
				prod = this.findProductById(connection, set.getInt("product_id"));
				if (prod != null) {
					track_found = prod.findTrackbyID(trackId);
				}
			}
			set.close();
		} finally {
			stmt.close();
		}
		
		return track_found;
	}
	
}
