package systemManager;

import java.util.Calendar;

import android.app.Activity;
import android.app.PendingIntent;
import android.net.Uri;
import android.widget.VideoView;

import android.content.Context;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import systemManager.guidelines.audio.Audio;
import systemManager.guidelines.video.Video;
import systemManager.guidelines.youtube.Youtube;
import systemManager.services.Services;
import systemManager.setup.Setup;

public class SystemManager {

	static SystemManager instance;
	Audio audio;
	Youtube youtube;
	Video video;
	Services services;
	Setup setup;

	/**
	 * Constructor. Create a new SystemManager object
	 */
	public SystemManager() {

		services = new Services();
		setup = new Setup();
	}

	/**
	 * Method to get the unique SystemManager instance
	 * @return the SystemManager instance
	 */
	public static SystemManager getInstance() {

		if (instance == null)
			instance = new SystemManager();

		return instance;
	}

	/**
	 * Method to get/create a Audio object
	 * @return an Audio object
	 */
	public Audio getAudioPlayer() {

		audio = new Audio();
		return audio;
	}

	/**
	 * Method to get a Video object
	 * @param videoView view where videos will be reproduced
	 * @return a Video object
	 */
	public Video getVideoPlayer(VideoView videoView){
		
		video = new Video(videoView);
		return video;
	}
	
	/**
	 * Method to get a Youtube object
	 * @param context the IU current context
	 * @param youtubeView a youtubePlayerView view where videos will be reproduced
	 * @return a Youtube object
	 */
	public Youtube getYoutubePlayer(Context context,
			YouTubePlayerView youtubeView) {

		youtube = new Youtube(context, youtubeView);
		return youtube;
	}
	
	/**
	 * Method to do a phone call
	 * @param number phone number to call
	 * @param activity current activity
	 */
	public void call(String number, Activity activity){
		services.call(number, activity);
	}

	/**
	 * Method to send a SMS message
	 * @param phoneNumber phone number which will received the message
	 * @param message
	 */
	public void sendSMS(String phoneNumber, String message){
		services.sendSMS(phoneNumber, message);
	}
	
	/**
	 * Method to send a simple notification. This notification just have a simple text. It will disappear after click on it.
	 * @param title The title of the notification
	 * @param text The text which will show the notification
	 * @param notificationId The id of the notification
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 */
	public void sendSimpleNotification(String title, String text, int notificationID, int icon, Context context){
		services.sendSimpleNotification(title, text, notificationID, icon, context);
	}
	
	/**
	 * Method to send a complex notification. This notification do something specified in the pendingIntent when
	 * it is cliked 
	 * @param title The title of the notificacion 
	 * @param text The text that the notification will show
	 * @param notificationId The notification's id
	 * @param flags The notification's flags
	 * @param contentIntent The intent that will be runned when the notification is cliked
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 */
	public void sendComplexNotification(String title, String text, int notificationId, int icon, int flags,
										PendingIntent contentIntent, Context context){
		services.sendComplexNotification(title, text, notificationId, flags, contentIntent, icon, context);
	}
	
	/**
	 * Method to send a complex notification. This notification do something specified in the pendingIntent when
	 * it is cliked. The sound it is also customizable
	 * @param title The title of the notificacion 
	 * @param text The text that the notification will show
	 * @param notificationId The notification's id
	 * @param flags The notification's flags
	 * @param contentIntent The intent that will be runned when the notification is cliked
	 * @param soundUri The sound that will be played when the notification is sent
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 */
	public void sendComplexNotificationCustomSound(String title, String text, int notificationId, int icon, int flags,
										PendingIntent contentIntent, Uri soundUri, Context context){
		services.sendComplexNotificationCustomSound(title, text, notificationId, flags, contentIntent, soundUri, icon, context);
	}
	
	/**
	 * Method that scheduled a simple notification
	 * @see sendSimpleNotification
	 * @param title The title of the notification
	 * @param text The text which will show the notification
	 * @param notificationId The id of the notification
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 * @param date Day and hour when the notification will be thrown
	 */
	public void scheduledSimpleNotification(String title, String text, int notificationID, int icon, Context context, Calendar date){
		services.scheduledSimpleNotification(title, text, notificationID, icon, context, date);
	}
	
	/**
	 * Method that scheduled a complex notification
	 * @see sendComplexNotification
	 * @param title The title of the notificacion 
	 * @param text The text that the notification will show
	 * @param notificationId The notification's id
	 * @param flags The notification's flags
	 * @param contentIntent The intent that will be runned when the notification is cliked
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 * @param date Day and hour when the notification will be thrown
	 */
	public void scheduledComplexNotification(String title, String text, int notificationId, int icon,
									int flags, PendingIntent contentIntent, Context context, Calendar date){
		services.scheduledComplexNotification(title, text, notificationId, flags, contentIntent, icon, context, date);
	}
	
	/**
	 * Method that scheduled a complex notification with a customized sound
	 * @see sendComplexNotificationCustomSound
	 * @param title The title of the notificacion 
	 * @param text The text that the notification will show
	 * @param notificationId The notification's id
	 * @param flags The notification's flags
	 * @param contentIntent The intent that will be runned when the notification is cliked
	 * @param soundUri The sound that will be played when the notification is sent
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 * @param date Day and hour when the notification will be thrown
	 */
	public void scheduledComplexNotificationCustomSound(String title, String text, int notificationId, int icon,
									int flags, PendingIntent contentIntent, Uri soundUri, Context context, Calendar date){
		services.scheduledComplexNotificationCustomSound(title, text, notificationId, flags, contentIntent, soundUri, icon, context, date);
	}
	
	/**
	 * Method that scheduled an audio to be played
	 * @param path The path where the audio file is stored
	 * @param requestCode 
	 * @param context The UI Activity Context
	 * @param date Day and hour when the notification will be thrown
	 */
	public void scheduledAudio(String path, Context context, Calendar date){
		services.scheduledAudio(path, context, date);
	}
	
	/**
	 * Set the WIFI connection on/off
	 * @param wifi boolean representing the desired wifi status
	 * @param context the UI current context
	 * @return true if the operation succeeds, otherwise false
	 */
	public void setWifiEnabled(boolean enable, Context context){
		setup.setWifiEnabled(enable, context);
	}
	
	/**
	 * Set the bluetooth connection ON/OFF
	 * @param bluetooth boolean representing the desired bluetooth status
	 */
	public void setBluetooth(boolean bluetooth){
		setup.setBluetooth(bluetooth);
	}
	
	/**
	 * Set the screen brightness
	 * @param brightness A value representing the brightness. Less than 0 means the default value and 0-1 to adjust
	 * brightness from dark to full bright
	 * @param activity current activity
	 */
	public void setScreenBrightness(float brigthness, Activity activity){
		setup.setScreenBrightness(brigthness, activity);
	}
}
