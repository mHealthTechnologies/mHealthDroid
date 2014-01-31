package systemManager.services;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;

public class Services {

	/**
	 * Constructor. Creates a new Services object
	 */
	public Services() {
		super();
	}

	/**
	 * Method to do a phone call
	 * @param number phone number to call
	 * @param activity current activity
	 */
	public void call(String number, Activity activity) {

		String finalNumber = "tel:" + number;
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(finalNumber));
		activity.startActivity(intent);
	}

	/**
	 * Method to send a SMS message
	 * @param phoneNumber phone number which will received the message
	 * @param message
	 */
	public void sendSMS(String phoneNumber, String message) {

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
	/**
	 * Method to send a simple notification. This notification just have a simple text. It will disappear after click on it.
	 * @param title The title of the notification
	 * @param text The text which will show the notification
	 * @param notificationId The id of the notification
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 */
	public void sendSimpleNotification(String title, String text, int notificationId, int icon, Context context){
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		
		 CharSequence tickerText = text;
		 long when = System.currentTimeMillis();
		 Notification notification = new Notification(icon, tickerText, when);
		 
		 Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 Intent notificationIntent = new Intent();
		 PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		 
		 notification.sound = soundUri;
		 notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 CharSequence contentTitle = title;
		 CharSequence contentText = text;
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 mNotificationManager.notify(notificationId, notification);
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
	public void sendComplexNotification(String title, String text, int notificationId, int flags, 
						PendingIntent contentIntent, int icon, Context context){
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		
		CharSequence tickerText = text;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);		
		notification.sound = soundUri;
		notification.flags = flags;
		CharSequence contentTitle = title;
		CharSequence contentText = text;
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(notificationId, notification);
		
	}
	
	/**
	 * Method to send a complex notification. This notification do something specified in the pendingIntent when
	 * it is cliked. The sound it is also customized
	 * @param title The title of the notificacion 
	 * @param text The text that the notification will show
	 * @param notificationId The notification's id
	 * @param flags The notification's flags
	 * @param contentIntent The intent that will be runned when the notification is cliked
	 * @param soundUri The sound that will be played when the notification is sent
	 * @param icon The icon that will show the notification
	 * @param context The UI Activity Context
	 */
	public void sendComplexNotificationCustomSound(String title, String text, int notificationId, int flags, 
			PendingIntent contentIntent,Uri soundUri, int icon, Context context){

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		
		CharSequence tickerText = text;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		notification.sound = soundUri;
		notification.flags = flags;
		CharSequence contentTitle = title;
		CharSequence contentText = text;
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(notificationId, notification);

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
	public void scheduledSimpleNotification(String title, String text, int notificationId, int icon, Context context, Calendar date){
		 
		 // Prepare the intent which should be launched at the date
		 Intent intent = new Intent(context, ScheduledTask.class);
		 
		 intent.putExtra("title", title);
		 intent.putExtra("text", text);
		 intent.putExtra("icon", icon);
		 intent.putExtra("notificationID", notificationId);
		 intent.putExtra("type", 1);

		 // Prepare the pending intent
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		 // Retrieve alarm manager from the system
		 AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 // Register the alert in the system. 
		 alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
		
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
	public void scheduledComplexNotification(String title, String text, int notificationId, int flags, 
			PendingIntent contentIntent, int icon, Context context, Calendar date){
		
		Intent intent = new Intent(context, ScheduledTask.class);
		 
		 Bundle bundle = new Bundle();
		 bundle.putParcelable("intent", contentIntent);
		 intent.putExtra("title", title);
		 intent.putExtra("text", text);
		 intent.putExtra("icon", icon);
		 intent.putExtra("notificationID", notificationId);
		 intent.putExtra("flags", flags);
		 intent.putExtra("bundle", bundle);
		 intent.putExtra("type", 2);
		 
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
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
	public void scheduledComplexNotificationCustomSound(String title, String text, int notificationId, int flags, 
			PendingIntent contentIntent, Uri soundUri, int icon, Context context, Calendar date){
		
		Intent intent = new Intent(context, ScheduledTask.class);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("intent", contentIntent);
		bundle.putParcelable("sound", soundUri);
		intent.putExtra("title", title);
		intent.putExtra("text", text);
		intent.putExtra("icon", icon);
		intent.putExtra("notificationID", notificationId);
		intent.putExtra("flags", flags);
		intent.putExtra("bundle", bundle);
		intent.putExtra("type", 3);

		 
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
	}
	
	/**
	 * Method that scheduled an audio to be played
	 * @param path The path where the audio file is stored
	 * @param requestCode 
	 * @param context The UI Activity Context
	 * @param date Day and hour when the notification will be thrown
	 */
	public void scheduledAudio(String path, Context context, Calendar date){
		
		 // Prepare the intent which should be launched at the date
		 Intent intent = new Intent(context, ScheduledTask.class);

		 intent.putExtra("path", path);
		 intent.putExtra("type", 4);

		 // Prepare the pending intent
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		 // Retrieve alarm manager from the system
		 AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 // Register the alert in the system. 
		 alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
	}
}
