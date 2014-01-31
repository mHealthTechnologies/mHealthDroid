package systemManager.guidelines.audio;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.TrackInfo;
import android.util.Log;

public class Audio {

	MediaPlayer mediaPlayer;
	String pathSong;

	/**
     * Constructor. Create a new Audio object.
     * @param context The UI Activity Context
     * @param name To allow the user to set a unique identifier for each Shimmer device
     */
	public Audio() {

		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {

				mediaPlayer.reset();
				return false;
			}

		});
	}
	
	/**
     * Method to load a file using its path
     * @param path The file path
     */
	public void loadFile(String path) {

		try {
			mediaPlayer.setDataSource(path);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Method to prepare the player to reproduce the file
     */
	public void prepare() {

		mediaPlayer.prepareAsync();
	}

	/**
     * Method to reproduce audio when the player is prepared
     */
	public void play() {

		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mediaPlayer.start();
			}
		});
	}

	/**
     * Method to stop audio
     */
	public void stop() {

		mediaPlayer.stop();
	}

	/**
     * Method to pause audio
     */
	public void pause() {

		mediaPlayer.pause();
	}

	/**
     * Method to get the duration of the current file
     * @return	An int with the duration of the file in milliseconds 
     */
	public int getDuration() {

		return mediaPlayer.getDuration();
	}

	/**
     * Method to get the mediaPlayer object being used
     * @return mediaPlayer instance
     */
	public MediaPlayer getMediaPlayer() {

		return mediaPlayer;
	}

	/**
     * Method to get information about the current file
     * @return a TrackInfo array with the information
     */
	public TrackInfo[] getTrackInfo() {

		return mediaPlayer.getTrackInfo();
	}
}