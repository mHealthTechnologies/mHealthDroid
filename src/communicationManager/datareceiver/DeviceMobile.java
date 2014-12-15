package communicationManager.datareceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import communicationManager.CommunicationManager;
import communicationManager.dataStructure.Data;
import communicationManager.dataStructure.Metadata;
import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.dataStructure.ObjectMetadata;
import communicationManager.dataStructure.ObjectMetadata.FormatType;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class DeviceMobile extends Device implements SensorEventListener {

	private final SensorManager mSensorManager;
	private ArrayList<Sensor> mobileSensors = new ArrayList<Sensor>();
	private Handler myHandlerManager;
	ArrayList<ObjectData> bufferSignals = new ArrayList<ObjectData>();
	int sensorDelay;
	public ArrayList<ObjectData> buffer;
	String macAddress;

	
	// Available Sensors in a mobile device (until Android 4.0)
	public static final int SENSOR_ACCEL = Sensor.TYPE_ACCELEROMETER;
	public static final int SENSOR_GYRO = Sensor.TYPE_GYROSCOPE;
	public static final int SENSOR_MAG = Sensor.TYPE_MAGNETIC_FIELD;
	public static final int SENSOR_TEMP = Sensor.TYPE_AMBIENT_TEMPERATURE;
	public static final int SENSOR_GRAV = Sensor.TYPE_GRAVITY;
	public static final int SENSOR_LIGHT = Sensor.TYPE_LIGHT;
	public static final int SENSOR_LIN_ACC = Sensor.TYPE_LINEAR_ACCELERATION;
	public static final int SENSOR_PRESS = Sensor.TYPE_PRESSURE;
	public static final int SENSOR_PROX = Sensor.TYPE_PROXIMITY;
	public static final int SENSOR_HUMID = Sensor.TYPE_RELATIVE_HUMIDITY;
	public static final int SENSOR_ROT_VECT = Sensor.TYPE_ROTATION_VECTOR;
	// public static final int SENSOR_ORIENT = Sensor.TYPE_ORIENTATION;

	// Units of measure of the mobile´s sensors
	public static final String UNITS_ACCEL = "m/(sec^2)";
	public static final String UNITS_GYRO = "rad/s";
	public static final String UNITS_MAG = "tesla";
	public static final String UNITS_TEMP = "ºC";
	public static final String UNITS_GRAV = "m/(sec^2)";
	public static final String UNITS_LIGHT = "lx";
	public static final String UNITS_LIN_ACC = "m/(sec^2)";
	public static final String UNITS_PRESS = "hPa";
	public static final String UNITS_PROX = "";
	public static final String UNITS_HUMID = "%";
	public static final String UNITS_ROT_VECT = "";

	// Available Sensors Delay
	public static final int DELAY_FASTEST = SensorManager.SENSOR_DELAY_FASTEST; // 0 ms aprox. Constant Value 0
	public static final int DELAY_GAME = SensorManager.SENSOR_DELAY_GAME; // 0.02 s aprox. Constant Value 1
	public static final int DELAY_UI = SensorManager.SENSOR_DELAY_UI; // 0.06 s aprox. Constant Value 2
	public static final int DELAY_NORMAL = SensorManager.SENSOR_DELAY_NORMAL; // 0.20 s aprox. Constant Value 3

	/**
     * Constructor. Create a new DeviceMobile object.
     * @param context  The UI Activity Context
     * @param name  To allow the user to set a unique identifier for each Shimmer device
     */
	public DeviceMobile(Context context, String name) {

		super();
		myContext = context;
		myName = name;
		isStreaming = false;
		mSensorManager = (SensorManager) myContext
				.getSystemService(Context.SENSOR_SERVICE);
		metadata = new ObjectMetadata();
		metadata.format = FormatType.CALIBRATED;
		sensorDelay = DELAY_NORMAL;
		// Creation of every sensor existing on the mobile
		List<Sensor> listaAux = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		contBuffer = 0;
		setMacAddress();
		buffer = new ArrayList<ObjectData>();

		for (int i = 0; i < MAX_SIZE; i++)
			buffer.add(i, new ObjectData(myName));

		for (int i = 0; i < listaAux.size(); i++)

			mobileSensors.add(listaAux.get(i));

	}

	@Override
	public boolean connect(String adress) {
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_CONNECTED, myName).sendToTarget();
		return true;
	}

	@Override
	public boolean disconnect() {
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_DISCONNECTED, myName).sendToTarget();
		return true;
	}

	@Override
	public void startStreaming() {

		initBuffer();
		contBuffer = 0;

		// Every sensor available on the device starts streaming
		for (int i = 0; i < mobileSensors.size(); i++) {
			Sensor sensorAux = mobileSensors.get(i);
			mSensorManager.registerListener(this, sensorAux, sensorDelay);
		}

		isStreaming = true;
		metadata.start.setToNow();
		metadata.rate = getRate();
		setMetadata();
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_STREAMING, myName).sendToTarget();
	}

	@Override
	public void stopStreaming() {

		metadata.finish.setToNow();
		mSensorManager.unregisterListener(this);
		isStreaming = false;
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_CONNECTED, myName).sendToTarget();
	}


	/**
	 *  This function sets the sample rate of the external device
	 * @param rate Is the new rate. This parameter should be in the range 0 and 3 
	 * (delay_fastest 0 (100 hz aprox), delay_game 1 (50 hz aprox),
	 * delay_ui 2(16,7 hz aprox) and delay_normal 3 (5 hz aprox)
	 */
	@Override
	public void setRate(double rate) {

		int delay = (int) rate;
		if (delay >= 0 && delay <= 3)
			sensorDelay = delay;

	}

	@Override
	public double getRate() {

		double rate = 0;

		switch (sensorDelay) {

		case 0: // SensorManager.SENSOR_DELAY_FASTEST 0 ms aprox
			rate = 100;
			break;
		case 1: // SensorManager.SENSOR_DELAY_GAME 0.02 s aprox
			rate = 50;
			break;
		case 2: // SensorManager.SENSOR_DELAY_UI 0.06 s aprox
			rate = 16.7;
			break;
		case 3: // SensorManager.SENSOR_DELAY_NORMAL 0.2 s aprox
			rate = 5;
			break;
		}
		return rate;
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// To not blocked this listener we use the function gatherData
		gatherData(event);
	}

	/**
     * This function get the data sent by the device sensors and store them in the buffer
     */
	public void gatherData(SensorEvent event) {

		ObjectData myData = new ObjectData(myName);

		int sensorAux = event.sensor.getType();

		switch (sensorAux) {

		case 1: // SENSOR.TYPE_ACCELERATION

			myData.hashData.put(SensorType.ACCELEROMETER_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.ACCELEROMETER_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.ACCELEROMETER_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));

			break;

		case 2: // SENSOR.TYPE_MAGNETIC_FIELD

			myData.hashData.put(SensorType.MAGNETOMETER_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.MAGNETOMETER_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.MAGNETOMETER_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));

			break;

		case 3: // SENSOR.TYPE_ORIENTATION

			// DEPRECATED

			/*
			 * myData.hashData.put(SensorType.ORIENTATION_X, new Data(event.values[0]));
			 * myData.hashData.put(SensorType.ORIENTATION_Y, new Data(event.values[1]));
			 * myData.hashData.put(SensorType.ORIENTATION_Z, new  Data(event.values[2]));
			 * myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			 * break;
			 */
			break;

		case 4: // SENSOR.TYPE_GYROSCOPE

			myData.hashData.put(SensorType.GYROSCOPE_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.GYROSCOPE_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.GYROSCOPE_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));

			break;

		case 5: // SENSOR.TYPE_LIGHT

			myData.hashData.put(SensorType.LIGHT, new Data(event.values[0]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 6: // SENSOR.TYPE_PRESSURE

			myData.hashData.put(SensorType.PRESSURE, new Data(event.values[0]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 7: // SENSOR.TYPE_TEMPERATURE

			// Deprecated. Use SENSOR.AMBIENT_TEMPERATURE instead

		case 8: // SENSOR.TYPE_PROXIMITY

			myData.hashData.put(SensorType.PROXIMITY, new Data(event.values[0]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 9: // SENSOR.TYPE_GRAVITY

			myData.hashData.put(SensorType.GRAVITY_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.GRAVITY_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.GRAVITY_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 10: // SENSOR.TYPE_LINEAR_ACCELERATION

			myData.hashData.put(SensorType.LINEAR_ACCELERATION_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.LINEAR_ACCELERATION_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.LINEAR_ACCELERATION_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 11: // SENSOR.TYPE_ROTATION_VECTOR

			myData.hashData.put(SensorType.ROTATION_VECTOR_X, new Data(event.values[0]));
			myData.hashData.put(SensorType.ROTATION_VECTOR_Y, new Data(event.values[1]));
			myData.hashData.put(SensorType.ROTATION_VECTOR_Z, new Data(event.values[2]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 12: // SENSOR.TYPE_RELATIVE_HUMIDITY

			myData.hashData.put(SensorType.HUMIDITY, new Data(event.values[0]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;

		case 13: // SENSOR.AMBIENT_TEMPERATURE

			myData.hashData.put(SensorType.AMBIENT_TEMPERATURE, new Data(event.values[0]));
			myData.hashData.put(SensorType.TIME_STAMP, new Data(event.timestamp));
			break;
		}

		// Just to check if the case was neither type_temperature nor type_orientation (DEPRECATED)
		if (myData.hashData.keySet().size() != 0) {

			boolean found = false;
			float currentTimeStamp = event.timestamp;

			// Search for the same timestamp value in the arraylist
			for (int i = 0; i < bufferSignals.size() && !found; i++) {

				long timeStampAux = (long) bufferSignals.get(i).hashData.get(SensorType.TIME_STAMP).data;

				// if timestamp value already exists (found the timestamp value)
				if (timeStampAux == currentTimeStamp) {
					found = true;
					Set<SensorType> sensors = myData.hashData.keySet();
					for (SensorType s : sensors)
						bufferSignals.get(i).hashData.put(s, myData.hashData.get(s));
				}
			}

			// If not found and there are less than 5 elements in the array...
			if (!found && bufferSignals.size() < 5)
				bufferSignals.add(myData);

			// If not found and there are more than 5 elements in the array, we remove the first one and add the last one
			else if (!found && bufferSignals.size() >= 5) {

				/*
				 * The timestamp is given in two different formats, depending on the device manufacturer:
				 * -Epoch time given in milliseconds
				 * -Uptime date, which is the milliseconds passed since the operating system was loaded
				 * To have the same format regardless the mobile device manufacturer, the system method
				 * System.currentTimeMillis() is used
				 */
				
				bufferSignals.get(0).hashData.get(SensorType.TIME_STAMP).data = System.currentTimeMillis();

				// Adding the objectData to the main buffer, which feed storage
				buffer.set(contBuffer, bufferSignals.get(0));

				// Updating the auxiliar circular buffer
				bufferSignals.remove(0);
				bufferSignals.add(myData);

				// Remittance of the message to the communicationManager
				myHandlerManager.obtainMessage(CommunicationManager.DEVICE_MOBILE, contBuffer, 0, buffer.get(contBuffer)).sendToTarget();
				contBuffer = (contBuffer + 1) % MAX_SIZE;

			}
		}
	}

	@Override
	public String getTableName() {

		String aux[] = macAddress.split(":");
		String nameTable = "Table_";
		for (int i = 0; i < aux.length; i++) {

			nameTable = nameTable + aux[i];

		}

		return nameTable;
	}

	@Override
	public boolean isConnected() {

		return true;
	}

	@Override
	public ArrayList<SensorType> getEnabledSensors() {

		ArrayList<SensorType> sensors = new ArrayList<SensorType>();

		for (int i = 0; i < mobileSensors.size(); i++) {

			Sensor aux = mobileSensors.get(i);

			switch (aux.getType()) {

			case 1: // SENSOR_TYPE.ACCELEROMETER
				sensors.add(SensorType.ACCELEROMETER);
				break;
			case 2: // SENSOR_TYPE.MAGNETIC_FIELD
				sensors.add(SensorType.MAGNETOMETER);
				break;
			case 4: // SENSOR.TYPE_GYROSCOPE
				sensors.add(SensorType.GYROSCOPE);
				break;
			case 5: // SENSOR.TYPE_LIGHT
				sensors.add(SensorType.LIGHT);
				break;
			case 6: // SENSOR.TYPE_PRESSURE
				sensors.add(SensorType.PRESSURE);
				break;
			case 8: // SENSOR.TYPE_PROXIMITY
				sensors.add(SensorType.PROXIMITY);
				break;
			case 9: // SENSOR.TYPE_GRAVITY
				sensors.add(SensorType.GRAVITY);
				break;
			case 10: // SENSOR.TYPE_LINEAR_ACCELERATION
				sensors.add(SensorType.LINEAR_ACCELERATION);
				break;
			case 11: // SENSOR.TYPE_ROTATION_VECTOR
				sensors.add(SensorType.ROTATION_VECTOR);
				break;
			case 12: // SENSOR.TYPE_RELATIVE_HUMIDITY
				sensors.add(SensorType.HUMIDITY);
				break;
			case 13: // SENSOR.AMBIENT_TEMPERATURE
				sensors.add(SensorType.AMBIENT_TEMPERATURE);
				break;
			}
		}

		return sensors;
	}

	@Override
	public String getMetadataTableName() {

		// MAC Address is already loaded, so there is no need in calling getMacAddress function
		String aux[] = macAddress.split(":");
		String nameTable = "Metadata_";
		for (int i = 0; i < aux.length; i++) {

			nameTable = nameTable + aux[i];

		}

		return nameTable;
	}

	@Override
	public void setMetadata() {

		metadata.hashMetadata.clear();
		for (int i = 0; i < mobileSensors.size(); i++) {

			Sensor aux = mobileSensors.get(i);

			switch (aux.getType()) {

			case 1: // SENSOR_TYPE.ACCELEROMETER

				metadata.hashMetadata.put(SensorType.ACCELEROMETER,	new Metadata(UNITS_ACCEL));
				break;

			case 2: // SENSOR_TYPE.MAGNETIC_FIELD

				metadata.hashMetadata.put(SensorType.MAGNETOMETER, new Metadata(UNITS_MAG));
				break;

			case 4: // SENSOR.TYPE_GYROSCOPE

				metadata.hashMetadata.put(SensorType.GYROSCOPE, new Metadata(UNITS_GYRO));
				break;

			case 5: // SENSOR.TYPE_LIGHT

				metadata.hashMetadata.put(SensorType.LIGHT, new Metadata(UNITS_LIGHT));
				break;

			case 6: // SENSOR.TYPE_PRESSURE

				metadata.hashMetadata.put(SensorType.PRESSURE, new Metadata(UNITS_PRESS));
				break;

			case 8: // SENSOR.TYPE_PROXIMITY

				metadata.hashMetadata.put(SensorType.PROXIMITY, new Metadata(UNITS_PROX));
				break;

			case 9: // SENSOR.TYPE_GRAVITY

				metadata.hashMetadata.put(SensorType.GRAVITY, new Metadata(UNITS_GRAV));
				break;

			case 10: // SENSOR.TYPE_LINEAR_ACCELERATION

				metadata.hashMetadata.put(SensorType.LINEAR_ACCELERATION, new Metadata(UNITS_LIN_ACC));
				break;

			case 11: // SENSOR.TYPE_ROTATION_VECTOR

				metadata.hashMetadata.put(SensorType.ROTATION_VECTOR, new Metadata(UNITS_ROT_VECT));
				break;

			case 12: // SENSOR.TYPE_RELATIVE_HUMIDITY

				metadata.hashMetadata.put(SensorType.HUMIDITY, new Metadata(UNITS_HUMID));
				break;

			case 13: // SENSOR.AMBIENT_TEMPERATURE

				metadata.hashMetadata.put(SensorType.AMBIENT_TEMPERATURE, new Metadata(UNITS_TEMP));
				break;
			}
		}
	}

	/**
     * Initialize the buffer of the driver
     */
	private void initBuffer() {

		ArrayList<SensorType> s = getEnabledSensors();
		ArrayList<SensorType> sensors = new ArrayList<SensorType>();
		for (int j = 0; j < s.size(); j++) {
			switch (s.get(j)) {
			case ACCELEROMETER:
				sensors.add(SensorType.ACCELEROMETER_X);
				sensors.add(SensorType.ACCELEROMETER_Y);
				sensors.add(SensorType.ACCELEROMETER_Z);
				break;
			case MAGNETOMETER:
				sensors.add(SensorType.MAGNETOMETER_X);
				sensors.add(SensorType.MAGNETOMETER_Y);
				sensors.add(SensorType.MAGNETOMETER_Z);
				break;
			case GYROSCOPE:
				sensors.add(SensorType.GYROSCOPE_X);
				sensors.add(SensorType.GYROSCOPE_Y);
				sensors.add(SensorType.GYROSCOPE_Z);
				break;
			case ORIENTATION:
				sensors.add(SensorType.ORIENTATION_X);
				sensors.add(SensorType.ORIENTATION_Y);
				sensors.add(SensorType.ORIENTATION_Z);
				break;
			case GRAVITY:
				sensors.add(SensorType.GRAVITY_X);
				sensors.add(SensorType.GRAVITY_Y);
				sensors.add(SensorType.GRAVITY_Z);
				break;
			case AMBIENT_TEMPERATURE:
				sensors.add(SensorType.AMBIENT_TEMPERATURE);
				break;
			case LIGHT:
				sensors.add(SensorType.LIGHT);
				break;
			case LINEAR_ACCELERATION:
				sensors.add(SensorType.LINEAR_ACCELERATION);
				break;
			case PRESSURE:
				sensors.add(SensorType.PRESSURE);
				break;
			case PROXIMITY:
				sensors.add(SensorType.PROXIMITY);
				break;
			case HUMIDITY:
				sensors.add(SensorType.HUMIDITY);
				break;
			case ROTATION_VECTOR:
				sensors.add(SensorType.GRAVITY_X);
				sensors.add(SensorType.GRAVITY_Y);
				sensors.add(SensorType.GRAVITY_Z);
				break;
			}
		}

		for (int i = 0; i < MAX_SIZE; i++) {
			buffer.get(i).hashData.clear();
			for (int j = 0; j < sensors.size(); j++) {
				buffer.get(i).hashData.put(sensors.get(j), new Data(0));
			}
		}
	}

	@Override
	public void writeEnabledSensors(ArrayList<SensorType> sensorsToEnable) {

		mobileSensors.clear();
		List<Sensor> aux = new ArrayList<Sensor>();

		for (int i = 0; i < sensorsToEnable.size(); i++) {

			switch (sensorsToEnable.get(i)) {

			case ACCELEROMETER:
				aux = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case MAGNETOMETER:
				aux = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case GYROSCOPE:
				aux = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case ORIENTATION:
				// aux = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
				// if(aux.size()!=0)
				// mobileSensors.add(aux.get(0));
				// break;
			case GRAVITY:
				aux = mSensorManager.getSensorList(Sensor.TYPE_GRAVITY);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case AMBIENT_TEMPERATURE:
				aux = mSensorManager
						.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case LIGHT:
				aux = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case LINEAR_ACCELERATION:
				aux = mSensorManager
						.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case PRESSURE:
				aux = mSensorManager.getSensorList(Sensor.TYPE_PRESSURE);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case PROXIMITY:
				aux = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case HUMIDITY:
				aux = mSensorManager
						.getSensorList(Sensor.TYPE_RELATIVE_HUMIDITY);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			case ROTATION_VECTOR:
				aux = mSensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
				if (aux.size() != 0)
					mobileSensors.add(aux.get(0));
				break;
			}
		}
	}

	/**
     * Set the device's MAC address
     */
	public void setMacAddress() {

		// WIFI must be on to get the wifi mac, or at least had been once since the last time mobile was on
		WifiManager manager = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);

		if (manager.isWifiEnabled()) { // Wifi is ON
			//Log.d("WIFI", "ON");
			WifiInfo info = manager.getConnectionInfo();
			String address = info.getMacAddress();
			this.macAddress = address;
		}

		else { // Wifi is OFF, so its necessary to turn it on, get the mac and
				// turn it off again
			//Log.d("WIFI", "OFF");
			manager.setWifiEnabled(true);
			WifiInfo info = manager.getConnectionInfo();
			String address = info.getMacAddress();
			manager.setWifiEnabled(false);
			this.macAddress = address;
		}
	}

}
