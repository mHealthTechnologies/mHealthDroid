package systemManager.setup;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.WindowManager;

public class Setup {

	/**
	 * Constructor. Create a new Setup object
	 */
	public Setup() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Set the WIFI connection on/off
	 * @param wifi boolean representing the desired wifi status
	 * @param context the UI current context
	 * @return true if the operation succeeds, otherwise false
	 */
	public boolean setWifiEnabled(boolean wifi, Context context) {

		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return manager.setWifiEnabled(wifi);
	}

	/**
	 * Set the bluetooth connection ON/OFF
	 * @param bluetooth boolean representing the desired bluetooth status
	 */
	public void setBluetooth(boolean bluetooth) {

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (bluetooth)
			mBluetoothAdapter.enable();
		else
			mBluetoothAdapter.disable();
	}

	
	/**
	 * Set the screen brightness
	 * @param brightness A value representing the brightness. Less than 0 means the default value and 0-1 to adjust
	 * brightness from dark to full bright
	 * @param activity current activity
	 */
	public void setScreenBrightness(float brightness, Activity activity) {

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = brightness;
		activity.getWindow().setAttributes(lp);
	}
	
}
