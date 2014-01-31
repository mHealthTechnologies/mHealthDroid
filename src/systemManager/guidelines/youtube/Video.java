package systemManager.guidelines.youtube;
import java.io.Serializable;


public class Video implements Serializable {
	// The title of the video
	private String title;
	// A link to the video on youtube
	private String url;
	// A link to a image of the youtube video
	private String thumbUrl;

	/**
	 * Constructor. Create a new Video object
	 * @param title the video title
	 * @param url the video url
	 * @param thumbUrl the url thumbnail video
	 */
	public Video(String title, String url, String thumbUrl) {
		super();
		this.title = title;
		this.url = url;
		this.thumbUrl = thumbUrl;
	}

	/**
	 * Method to get the video title
	 * @return a string representing the video title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method to get the video url
	 * @return a string representing the video url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Method to get the video thumbnail url
	 * @return a string representing the video thumbnail
	 */
	public String getThumbUrl() {
		return thumbUrl;
	}
}