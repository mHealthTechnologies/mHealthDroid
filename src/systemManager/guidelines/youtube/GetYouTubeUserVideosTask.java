package systemManager.guidelines.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GetYouTubeUserVideosTask implements Runnable {

	private final Handler replyTo;
	private final String playlist;


	/**
     * Constructor. Create a new GetYoutubeUserVideos task object, used to get all the videos of a Youtube playlist
     * @param reply To The handler to notify when the task finishes
     * @param playlist The playlist with the videos to get
     */
	public GetYouTubeUserVideosTask(Handler replyTo, String playlist) {
		this.replyTo = replyTo;
		this.playlist = playlist;
	}

	@Override
	public void run() {

		try {
			// Creation of HTTP client, request and execution
			HttpClient client = new DefaultHttpClient();

			HttpUriRequest request = new HttpGet(
					"https://gdata.youtube.com/feeds/api/playlists/" + playlist + "?v=2&alt=jsonc");

			HttpResponse response = client.execute(request);

			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			// Creation of a Json object
			JSONObject json = new JSONObject(jsonString);

			// Positioning the object into an JsonArray
			JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

			// Array where to put into videos
			ArrayList<Video> videosList = new ArrayList<Video>();

			// Loop round our JSON list of videos creating Video objects to use within our app
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonaux = jsonArray.getJSONObject(i).getJSONObject("video");
				String title = jsonaux.getString("title");
				// The url link back to YouTube, this checks if it has a mobile url
				// if it does1 not it gets the standard url
				String url = null;
				try {
					url = jsonaux.getJSONObject("player").getString("mobile");
				} catch (JSONException ignore) {
					url = jsonaux.getJSONObject("player").getString("default");
				}

				// String (url) for the thumbnail. It will need processing into a Image type
				String thumbUrl = jsonaux.getJSONObject("thumbnail").getString("sqDefault");
				videosList.add(new Video(title, url, thumbUrl));
			}

			// Creation and filling of a bundle to send it to VideosActivity
			Bundle data = new Bundle();
			data.putSerializable("VideosList", videosList);
			Message msg = Message.obtain();
			msg.setData(data);
			replyTo.sendMessage(msg);

			// Any error catching is done, just nothing will happen if this
			// task falls over

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
		}
	}
}
