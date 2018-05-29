
package cs636.music.dao;

import static cs636.music.dao.DBConstants.SYS_TABLE;
import static cs636.music.dao.DBConstants.USER_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs636.music.domain.User;

/**
 * 
 * Access site_user table through this class.
 * @author Chung-Hsien (Jacky) Yu
 */
public class UserDAO {
	
	public UserDAO() {
	}
	
	/**
	 * 
	 * @param usr the site_user domain contains user data
	 * @throws SQLException
	 */
	public void insertUser(Connection connection, User usr) throws SQLException  {
		Statement stmt = connection.createStatement();
		int userId = getNextUserID(connection);
		usr.setId(userId);
		try {
			String sqlString = "insert into " + USER_TABLE + 
			" (user_id, firstname, lastname, email_address, address1) values ("
			+ usr.getId() + ", '" + usr.getFirstname() + "', '"
			+ usr.getLastname() + "', '" + usr.getEmailAddress() +  "', '"
			+ usr.getAddress() + "') ";
			stmt.execute(sqlString);
			System.out.println("$$$$$$$$$$$$$$$$4....user updated from insertUser...");
		} finally {
			stmt.close();
		}
	}
	
	/**
	 * 
	 * @param usr the site_user domain object
	 * now with an address: add address to DB
	 * @throws SQLException
	 */
	public void updateUserAddress(Connection connection, User usr) throws SQLException  {
		Statement stmt = connection.createStatement();
		try {
			String sqlString = "update " + USER_TABLE + 
			" set address1 = '" + usr.getAddress() + "'" +
			" where email_address = '" + usr.getEmailAddress()+ "'" ;
			stmt.execute(sqlString);
		} finally {
			stmt.close();
		}
	}
	/**
	 * Increase user_id by 1 in the system table
	 * @throws SQLException
	 */
	private void advanceUserID(Connection connection) throws SQLException
	{
		Statement stmt = connection.createStatement();
		try {
			stmt.executeUpdate(" update " + SYS_TABLE
					+ " set user_id = user_id + 1");
		} finally {
			stmt.close();
		}
	}
	
	/**
	 * Get the available user id 
	 * @return the user id available 
	 * @throws SQLException
	 */
	public int getNextUserID(Connection connection) throws SQLException
	{
		int nextUID;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select user_id from " + SYS_TABLE);
			set.next();
			nextUID = set.getInt("user_id");
		} finally {
			stmt.close();
		}
		advanceUserID(connection); // the id has been taken, so set +1 for next one
		return nextUID;
	}
	
	/**
	 * Find a user from site user table by its id
	 * (for use by DAOs to turn user_id FK into User)
	 * @param userId the user id of the user we try to find
	 * @return an User object if exist, or return null 
	 * @throws SQLException
	 */
	public User findUserByID(Connection connection, long userId)throws SQLException{
		User usr = null;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + USER_TABLE +
					" where user_id = " + userId);
			if (set.next()){ // if the result is not empty
				usr = new User();
				usr.setId(set.getInt("user_id"));
				usr.setFirstname(set.getString("firstname"));
				usr.setLastname(set.getString("lastname"));
				usr.setEmailAddress(set.getString("email_address"));
				usr.setAddress(set.getString("address1"));
				set.close();
			}
		} finally {
			stmt.close();
		}
		return usr;
	}
	
	/**
	 * Find a user from site user table by its email
	 * @param email user's email we try to find
	 * @return an User object if exist, or return null 
	 * @throws SQLException
	 */
	public User findUserByEmail(Connection connection, String email) throws SQLException{
		User usr = null; 
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + USER_TABLE +
					" where email_address = '" + email + "'");
			if (set.next()){ // if the result is not empty
				usr = new User();
				usr.setId(set.getInt("user_id"));
				usr.setFirstname(set.getString("firstname"));
				usr.setLastname(set.getString("lastname"));
				usr.setEmailAddress(set.getString("email_address"));
				usr.setAddress(set.getString("address1"));
				set.close();
			}
		} finally {
			stmt.close();
		}
		return usr;
	}
	
}
