package cs636.music.service.data;

import cs636.music.domain.User;

/**
 * UserData: for service API use: immutable, small
 * contains no private info on the user, like password or credit card info
 * for better security, since it reaches the presentation layer code
 */
public class UserData {
	private long id;
	private String firstname;
	private String lastname;
	private String emailAddress;

	public UserData () {};
	
	public UserData (User u) {
		id = u.getId();
		firstname = u.getFirstname();
		lastname = u.getLastname();
		emailAddress = u.getEmailAddress();
	}
	
	public long getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
}
