package cs636.music.domain;



/**
 * Track POJO: not in Murach's setup, pg. 649
 * Like Product, never changes once created
 * We could add constructor(s), drop setters
 */
public class Track {
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private long id;
	private Product product;  	
	private int trackNumber;	
	private String title;
	private String sampleFilename;

	public Track(){}
	public long getId() {
		return id;
	}

	public void setId(long track_id) {
		this.id = track_id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSampleFilename() {
		return sampleFilename;
	}

	public void setSampleFilename(String sampleFilename) {
		this.sampleFilename = sampleFilename;
	}
}
