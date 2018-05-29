package cs636.music.dao;

import static cs636.music.dao.DBConstants.ADMIN_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * 
 * Access admin related tables through this class.
 */
public class AdminUserDAO {
	
	public AdminUserDAO (DbUtils db) {
	}
	
	
	/**
	 * check login user name and password
	 * @param uid
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public Boolean findAdminUser(Connection connection, String uid, String pwd) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + ADMIN_TABLE +
					" where username = '" + uid + "'" +
					" and password = '" + pwd + "'");
			if (set.next()){ // if the result is not empty
				set.close();
				return true;
			}
		} finally {
			stmt.close();
		}
		return false;
	}
}
