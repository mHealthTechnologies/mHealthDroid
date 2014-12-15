package systemManager.guidelines.youtube;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class Youtube {

	YouTubePlayerView youtubeView;
	YouTubePlayer player;
	String keyDeveloper;
	ListView videosListView;
	int videoEntryViewID;
	int videoEntryTextSuperior;
	int videoEntryTextInferior;
	int videoThumbnail;
	Context context;
	String IDVideo;
	ImageDownloader imageDownloader;

	/**
	 * Constructor. Create a new Youtube object
	 * @param context the IU current context
	 * @param youtubeView a youtubePlayerView view where videos are reproduced
	 * @param keyDeveloper a google key developer
	 */
	public Youtube(Context context, YouTubePlayerView youtubeView) {

		this.context = context;
		this.youtubeView = youtubeView;
		player = null;
		imageDownloader = new ImageDownloader();
	}

	/**
	 * Method to reproduce a playlist, selecting videos from a listview
	 * @param videosListView listview with every video that can be reproduced
	 * @param videoEntryViewID view ID of a single entry
	 * @param playlist a string ID of the playlist to reproduce
	 */
	public void reproducePlaylistMode(ListView videosListView,
			int videoEntryViewID, int videoEntryTextSuperior, int videoEntryTextInferior, int videoThumbnail,
			String playlist) {

		this.videosListView = videosListView;
		this.videoEntryViewID = videoEntryViewID;
		this.videoEntryTextInferior = videoEntryTextInferior;
		this.videoEntryTextSuperior = videoEntryTextSuperior;
		this.videoThumbnail = videoThumbnail;
		// This thread gets every video in the playlist
		new Thread(new GetYouTubeUserVideosTask(myHandler, playlist)).start();

	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Once every video has been stored, is time to fill the listView
			fillListView(msg);
		}
	};

	/**
	 * Method to fill the list view with every video in the playlist
	 * @param msg message with the information belonging to every video
	 */
	private void fillListView(Message msg) {

		ArrayList<Video> listVideos = (ArrayList) msg.getData().get(
				"VideosList");

		// The adapter populate the listView inserting every video in an entry
		// of the listView
		videosListView.setAdapter(new AdapterList(context, videoEntryViewID,
				listVideos) {
			@Override
			public void onEntry(Object entry, View view) {
				if (entry != null) {
					TextView superiorText = (TextView) view
							.findViewById(videoEntryTextSuperior);
					if (superiorText != null)
						superiorText.setText(((Video) entry).getTitle());

					TextView inferiorText = (TextView) view
							.findViewById(videoEntryTextInferior);
					if (inferiorText != null)
						inferiorText.setText(((Video) entry).getUrl());

					ImageView thumb = (ImageView) view
							.findViewById(videoThumbnail);

					String url = ((Video) entry).getThumbUrl();

					//To avoid crazy listviews we set the url as tag of the imageView
					thumb.setTag(url);
					imageDownloader.download(url, thumb);
				}
			}
		});

		videosListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pariente, View view,
					int position, long id) {

				Video chosen = (Video) pariente.getItemAtPosition(position);
				String url = chosen.getUrl();
				IDVideo = getYoutubeVideoId(url);
				if(player != null){
					player.cueVideo(IDVideo);
				}
			}
		});
	}
		

	/**
     * Method to reproduce a single video
	 * @param fullUrl the video long url to be reproduced
	 */
	public void reproduceSingleVideoMode(String longUrl) {

		IDVideo = getYoutubeVideoId(longUrl);
		if(player != null){
			player.cueVideo(IDVideo);
		}
	}

	/**
	 * Method to get the video Youtube ID from a long url
	 * @param input video long url
	 * @return a string with the video Youtube ID
	 */
	public static String getYoutubeVideoId(String input) {

		Pattern p = Pattern
				.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*([a-zA-Z=_]*)");
		Matcher m = p.matcher(input);

		if (m.matches()) {
			input = m.group(1);
		}

		return input;
	}
	
	/**
	 * Method to set the YouTube player where videos will be played
	 * @param player the YouTube player where videos will be played
	 */
	public void setPlayer(YouTubePlayer player){
		
		this.player = player;
	}
}