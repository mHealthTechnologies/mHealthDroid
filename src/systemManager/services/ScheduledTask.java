package systemManager.services;

import systemManager.guidelines.audio.Audio;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


public class ScheduledTask extends BroadcastReceiver {

	Services service = new Services();
	Audio audio = new Audio();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		int type = (int) intent.getIntExtra("type", 0);
		switch(type){
			case 1: // simple notification
				String title = (String) intent.getCharSequenceExtra("title");
				String text = (String) intent.getCharSequenceExtra("text");
				int icon = intent.getIntExtra("icon", 0);
				int notificationId = intent.getIntExtra("notificationID", 0);
				service.sendSimpleNotification(title, text, notificationId, icon, context);
			break;
			case 2: // complex notification
				String title2 = (String) intent.getCharSequenceExtra("title");
				String text2 = (String) intent.getCharSequenceExtra("text");
				int icon2 = intent.getIntExtra("icon", 0);
				int notificationId2 = intent.getIntExtra("notificationID", 0);
				int flags2 = intent.getIntExtra("flags", 0);
				Bundle bundle2 = intent.getBundleExtra("bundle");
				PendingIntent contentIntent2 = (PendingIntent) bundle2.getParcelable("intent");
				service.sendComplexNotification(title2, text2, notificationId2, flags2, contentIntent2,
													icon2, context);
			break;
			case 3: // complex notification with custom sound
				String title3 = (String) intent.getCharSequenceExtra("title");
				String text3 = (String) intent.getCharSequenceExtra("text");
				int icon3 = intent.getIntExtra("icon", 0);
				int notificationId3 = intent.getIntExtra("notificationID", 0);
				int flags3 = intent.getIntExtra("flags", 0);
				Bundle bundle3 = intent.getBundleExtra("bundle");
				PendingIntent contentIntent3 = (PendingIntent) bundle3.getParcelable("intent");
				Uri soundUri = bundle3.getParcelable("sound");
				service.sendComplexNotificationCustomSound(title3, text3, notificationId3, flags3,
												contentIntent3, soundUri, icon3, context);
			break;
			case 4: // play sound
				String path = (String) intent.getCharSequenceExtra("path");
				audio.loadFile(path);
				audio.prepare();
				audio.play();
			break;
		}
		
	}

}
