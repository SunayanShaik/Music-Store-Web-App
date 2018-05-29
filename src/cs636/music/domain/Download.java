package cs636.music.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Download 
 * Like Murach, pg. 649 except:
 * --instead of productCode, has ref to the specific Track
 * --exposes download id as a property, in case we want to use it.
 * This class has setters, as a convenience for creating objects,
 * but these objects, once created, never change.
 * For two-DB implementation, replace User ref with unique key emailAddress.
 * A query accessing downloads and users is a distributed query,
 * and needs special handling.
 */
public class Download implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String emailAddress; // avoiding ref between catalogDB and salesDB objects 
	private Track track;

	private Date downloadDate;

	public Download() {
		emailAddress = null;
		downloadDate = new Date();
	}

	public long getDownloadId() {
		return id;
	}

	public void setDownloadId(long download_id) {
		this.id = download_id;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String email) {
		emailAddress = email;
	}
	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date download_date) {
		this.downloadDate = download_date;
	}

}
