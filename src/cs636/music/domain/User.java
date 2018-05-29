package cs636.music.domain;

/**
 * User: we are only using a subset of the database attributes
 * here, for simplicity, and to show this is a normal thing to do.
 *
 */
public class User {
	private long id;
	private String firstname;
	private String lastname;
	private String emailAddress;
	private String address;

	public long getId() {
		return id;
	}

	public void setId(long user_id) {
		this.id = user_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String email_address) {
		this.emailAddress = email_address;
	}
	
	// extra info for "customer"
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
