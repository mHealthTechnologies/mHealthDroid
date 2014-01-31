package communicationManager.datareceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.Data;
import communicationManager.dataStructure.Metadata;
import communicationManager.dataStructure.ObjectMetadata.FormatType;
import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.dataStructure.ObjectMetadata;

import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;
import com.shimmerresearch.driver.Shimmer;

public class DeviceShimmer extends Activity implements Device {

	private Shimmer myShimmerDevice = null;
	private BluetoothAdapter myBluetoothAdapter = null;
	private BluetoothDevice device;
	private String bluetoothAddress;
	private Context myContext;
	private String myName;
	private Handler myHandlerManager;
	private FormatType format;
	public ObjectMetadata metadata;
	private boolean isStreaming;
	public ArrayList<ObjectData> buffer;
	public int cont;
	public long offsetSessionTimeStamp;
	public long offsetShimmerTimeStamp;
	public boolean firstSample;
	
	public int NUM_SAMPLE = 200; // size of the block of sample to work with
	public int MAX_SIZE = 4 * NUM_SAMPLE; //size of the buffer

	// Sensors available in a Shimmer device
	public static final int SENSOR_ACCEL = Shimmer.SENSOR_ACCEL;
	public static final int SENSOR_GYRO = Shimmer.SENSOR_GYRO;
	public static final int SENSOR_MAG = Shimmer.SENSOR_MAG;
	public static final int SENSOR_ECG = Shimmer.SENSOR_ECG;
	public static final int SENSOR_EMG = Shimmer.SENSOR_EMG;
	public static final int SENSOR_GSR = Shimmer.SENSOR_GSR;
	public static final int SENSOR_EXP_BOARD_A7 = Shimmer.SENSOR_EXP_BOARD_A7;
	public static final int SENSOR_EXP_BOARD_A0 = Shimmer.SENSOR_EXP_BOARD_A0;
	public static final int SENSOR_STRAIN = Shimmer.SENSOR_STRAIN;
	public static final int SENSOR_HEART = Shimmer.SENSOR_HEART;

	// Units of measure of the Shimmer's sensors. [0] = Calibrated, [1] =
	// Uncalibrated
	public static final String[] UNITS_TIME = { "ms", "u16" };
	public static final String[] UNITS_ACCEL = { "m/(sec^2)", "u12" };
	public static final String[] UNITS_GYRO = { "deg/s", "u12" };
	public static final String[] UNITS_MAG = { "local", "i16" };
	public static final String[] UNITS_GSR = { "kohms", "u16" };
	public static final String[] UNITS_ECG = { "mV", "u12" };
	public static final String[] UNITS_EMG = { "mV", "u12" };
	public static final String[] UNITS_STRAIN = { "mV", "u12" };
	public static final String[] UNITS_HEART = { "bmp", "" };
	public static final String[] UNITS_EXP_A0 = { "mV", "u12" };
	public static final String[] UNITS_EXP_A7 = { "mV", "u12" };

	static final int REQUEST_ENABLE_BT = 1;

	/**
     * Constructor. Create a new DeviceShimmer object.
     * @param context  The UI Activity Context
     * @param name  To allow the user to set a unique identifier for each Shimmer device
     * @param countiousSync A boolean value defining whether received packets should be checked continuously for the correct start and end of packet.
     */
	public DeviceShimmer(Context context, String name, boolean continuosSync) {
		super();
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		myShimmerDevice = new Shimmer(context, mHandler, name, continuosSync);
		myContext = context;
		myName = name;
		format = FormatType.CALIBRATED;
		metadata = new ObjectMetadata();
		isStreaming = false;
		cont = 0;
		buffer = new ArrayList<ObjectData>();
		firstSample = true;

		for (int i = 0; i < MAX_SIZE; i++)
			buffer.add(i, new ObjectData(myName));

	}


	@Override
	public boolean connect(String adress) {
		// TODO Auto-generated method stub
		if (myBluetoothAdapter == null) {
			Toast.makeText(myContext,"Device does not support Bluetooth\nExiting...",Toast.LENGTH_LONG).show();
			return false;
		}

		if (!myBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		bluetoothAddress = adress;
		device = myBluetoothAdapter.getRemoteDevice(bluetoothAddress);
		myShimmerDevice.connect(device);
		Log.d("ConnectionStatus", "Trying");
		while (myShimmerDevice.getState() == Shimmer.STATE_CONNECTING) {}; // wait for the connecting state to finish
		if (myShimmerDevice.getState() == Shimmer.STATE_CONNECTED) {
			Log.d("ConnectionStatus", "Successful");
		} else {
			Log.d("ConnectionStatus", "Failed. Is the Bluetooth radio on the Android device switched on?");
			return false;
		}

		return true;
	}

	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		if (myShimmerDevice != null)
			myShimmerDevice.stop();
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_DISCONNECTED, myName).sendToTarget();
		return true;
	}

	@Override
	public void startStreaming() {

		cont = 0;
		initBuffer();
		firstSample = true;
		myShimmerDevice.startStreaming();
		isStreaming = true;
		setMetadata();
	}

	@Override
	public void stopStreaming() {
		
		isStreaming = false;
		myShimmerDevice.stopStreaming();
		metadata.finish.setToNow();
		if(CommunicationManager.mHandlerApp!=null)
			CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_CONNECTED, myName).sendToTarget();
	}

	// The Handler that gets the messages sent by the Shimmer device
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) { // handlers have a what identifier which is used
								// to identify the type of msg
				case Shimmer.MESSAGE_ACK_RECEIVED:
				break;
				case Shimmer.MESSAGE_DEVICE_NAME:
					Toast.makeText(myContext, "Connected to " + myShimmerDevice.getBluetoothAddress(), 
							Toast.LENGTH_SHORT).show();
				break;
				case Shimmer.MESSAGE_INQUIRY_RESPONSE:
					Toast.makeText(myContext,"Inquiry Response " + myShimmerDevice.getBluetoothAddress(), 
							Toast.LENGTH_SHORT).show();
				break;
				case Shimmer.MESSAGE_SAMPLING_RATE_RECEIVED:
				break;
				case Shimmer.MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case Shimmer.STATE_CONNECTED:
							if(CommunicationManager.mHandlerApp!=null)
								CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_CONNECTED, myName).sendToTarget();
						break;
						case Shimmer.STATE_CONNECTING:
							if(CommunicationManager.mHandlerApp!=null)
								CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_CONNECTING, myName).sendToTarget();
						break;
						case Shimmer.STATE_NONE:
							if (isStreaming) { //if it was streaming and the current state is none means that the connection was lost
								isStreaming = false;
								//the driver sends the name instead an objectData so that the communication manager know that the connection was lost
								myHandlerManager.obtainMessage(CommunicationManager.DEVICE_SHIMMER, myName).sendToTarget();
								metadata.finish.setToNow();
							}
							
							if(CommunicationManager.mHandlerApp!=null)
								CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_DISCONNECTED, myName).sendToTarget();
							
							break;
					}
					break;
				case Shimmer.MESSAGE_WRITE:
				break;
	
				case Shimmer.MESSAGE_READ:
					if ((msg.obj instanceof ObjectCluster)) { // within each msg an object can be include, objectclusters
															  // are used to represent the data structure of the shimmer device
						ObjectCluster objectCluster = (ObjectCluster) msg.obj;
						if(firstSample){ // this set the offset timestamp to the momment when the first sample is received					 
							offsetSessionTimeStamp = System.currentTimeMillis();
							metadata.start.setToNow();
							firstSample = false;
							offsetShimmerTimeStamp = (long) (objectCluster.returnFormatCluster(objectCluster.mPropertyCluster.get("TimeStamp"), "Calibrated").mData);
							if(CommunicationManager.mHandlerApp!=null) //send a message telling the device is already streaming
								CommunicationManager.mHandlerApp.obtainMessage(CommunicationManager.STATUS_STREAMING, myName).sendToTarget();
						}
						
						SensorType sensor = null;
						Set<String> keys = objectCluster.mPropertyCluster.keySet();
						for (String k : keys) { // introduce the objectCluster into our data structure
							if (k.equals("AccelerometerX"))
								sensor = SensorType.ACCELEROMETER_X;
							else if (k.equals("AccelerometerY"))
								sensor = SensorType.ACCELEROMETER_Y;
							else if (k.equals("AccelerometerZ"))
								sensor = SensorType.ACCELEROMETER_Z;
							else if (k.equals("GyroscopeX"))
								sensor = SensorType.GYROSCOPE_X;
							else if (k.equals("GyroscopeY"))
								sensor = SensorType.GYROSCOPE_Y;
							else if (k.equals("GyroscopeZ"))
								sensor = SensorType.GYROSCOPE_Z;
							else if (k.equals("MagnetometerX"))
								sensor = SensorType.MAGNETOMETER_X;
							else if (k.equals("MagnetometerY"))
								sensor = SensorType.MAGNETOMETER_Y;
							else if (k.equals("MagnetometerZ"))
								sensor = SensorType.MAGNETOMETER_Z;
							else if (k.equals("GSR"))
								sensor = SensorType.GSR;
							else if (k.equals("ECGRALL"))
								sensor = SensorType.ECG_RALL;
							else if (k.equals("ECGLALL"))
								sensor = SensorType.ECG_LALL;
							else if (k.equals("EMG"))
								sensor = SensorType.EMG;
							else if (k.equals("Strain Gauge High"))
								sensor = SensorType.STRAIN_GAUGE_HIGH;
							else if (k.equals("Strain Gauge Low"))
								sensor = SensorType.STRAIN_GAUGE_LOW;
							else if (k.equals("Heart Rate"))
								sensor = SensorType.HEART_RATE;
							else if (k.equals("ExpBoardA0"))
								sensor = SensorType.EXP_BOARDA0;
							else if (k.equals("ExpBoardA7"))
								sensor = SensorType.EXP_BOARDA7;
							else if (k.equals("TimeStamp"))
								sensor = SensorType.TIME_STAMP;
	
							if (sensor != SensorType.TIME_STAMP) {
								Collection<FormatCluster> formatCluster = objectCluster.mPropertyCluster.get(k);
								for (FormatCluster f : formatCluster) {
									if (f.mFormat.equals("Calibrated") && (format == FormatType.CALIBRATED))
										buffer.get(cont).hashData.get(sensor).data = (float) f.mData;
									if (f.mFormat.equals("Uncalibrated") && (format == FormatType.UNCALIBRATED))
										buffer.get(cont).hashData.get(sensor).data = (float) f.mData;
								}
							}
	
						}
	
						long shimmerTime = (long) (objectCluster.returnFormatCluster(objectCluster.mPropertyCluster.get("TimeStamp"), "Calibrated").mData);	
						long timeToStore = offsetSessionTimeStamp + (shimmerTime - offsetShimmerTimeStamp);					

						buffer.get(cont).hashData.get(SensorType.TIME_STAMP).data = timeToStore;
						// send our data structure with the information to the manager
						myHandlerManager.obtainMessage(CommunicationManager.DEVICE_SHIMMER, cont, 0, buffer.get(cont)).sendToTarget();

						//Log.d("device", "cont == " + shimmerTime);

						cont = (cont + 1) % MAX_SIZE;
					}
				break;
				case Shimmer.MESSAGE_TOAST:
					Log.d("toast", msg.getData().getString(Shimmer.TOAST));
					Toast.makeText(myContext, msg.getData().getString(Shimmer.TOAST), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(myContext, "Bluetooth is now enabled", Toast.LENGTH_SHORT).show();
			} else {
				// User did not enable Bluetooth or an error occured
				Toast.makeText(myContext, "Bluetooth not enabled\nExiting...", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
     * Gets the name given to the device
     * @return the device's name
     */
	public String getName() {
		return myName;
	}


	/**
     * Set the communication manager's handler contained in the ObjectCommunication
     * @param handler The handler contained in the ObjectCommunication
     */
	public void setHandlerManager(Handler handler) {

		this.myHandlerManager = handler;
	}

	/**
     * Sets the format of the data. It can be "calibrated" or "uncalibrated"
     * @param format It's the format of the data
     */
	public void setFormat(FormatType format) {

		if (!isStreaming())
			this.format = format;
	}
	
	public FormatType getFormat(){
		return this.format;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		String[] split = bluetoothAddress.split(":");
		String name = "Table_";
		for (int i = 0; i < split.length; i++)
			name += split[i];

		return name;
	}

	@Override
	public String getMetadataTableName() {
		// TODO Auto-generated method stub
		String[] split = bluetoothAddress.split(":");
		String name = "Metadata_";
		for (int i = 0; i < split.length; i++)
			name += split[i];

		return name;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		if (myShimmerDevice.getState() == Shimmer.STATE_CONNECTED)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean isStreaming() {

		return this.isStreaming;
	}

	@Override
	public ArrayList<SensorType> getEnabledSensors() {

		ArrayList<SensorType> sensors = new ArrayList<SensorType>();
		int s = myShimmerDevice.getEnabledSensors();

		if (((s & 0xFF) & SENSOR_ACCEL) > 0) {
			sensors.add(SensorType.ACCELEROMETER);
		}
		if (((s & 0xFF) & SENSOR_GYRO) > 0) {
			sensors.add(SensorType.GYROSCOPE);
		}
		if (((s & 0xFF) & SENSOR_MAG) > 0) {
			sensors.add(SensorType.MAGNETOMETER);
		}
		if (((s & 0xFF) & SENSOR_GSR) > 0) {
			sensors.add(SensorType.GSR);
		}
		if (((s & 0xFF) & SENSOR_ECG) > 0) {
			sensors.add(SensorType.ECG);
		}
		if (((s & 0xFF) & SENSOR_EMG) > 0) {
			sensors.add(SensorType.EMG);
		}
		if (((s & 0xFF) & SENSOR_STRAIN) > 0) {
			sensors.add(SensorType.STRAIN);
		}
		if (((s & 0xFF) & SENSOR_HEART) > 0) {
			sensors.add(SensorType.HEART_RATE);
		}
		if (((s & 0xFF) & SENSOR_EXP_BOARD_A0) > 0) {
			sensors.add(SensorType.EXP_BOARDA0);
		}
		if (((s & 0xFF) & SENSOR_EXP_BOARD_A7) > 0) {
			sensors.add(SensorType.EXP_BOARDA7);
		}

		return sensors;
	}

	@Override
	public double getRate() {

		return myShimmerDevice.getSamplingRate();
	}

	@Override
	public void setMetadata() {

		int sensors = myShimmerDevice.getEnabledSensors();
		metadata.format = format;

		if (format == FormatType.CALIBRATED)
			metadata.hashMetadata.put(SensorType.TIME_STAMP, new Metadata(UNITS_TIME[0]));
		else
			metadata.hashMetadata.put(SensorType.TIME_STAMP, new Metadata(UNITS_TIME[1]));

		if (((sensors & 0xFF) & SENSOR_ACCEL) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.ACCELEROMETER,	new Metadata(UNITS_ACCEL[0]));
			else
				metadata.hashMetadata.put(SensorType.ACCELEROMETER,	new Metadata(UNITS_ACCEL[1]));
		}
		if (((sensors & 0xFF) & SENSOR_GYRO) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.GYROSCOPE, new Metadata(UNITS_GYRO[0]));
			else
				metadata.hashMetadata.put(SensorType.GYROSCOPE, new Metadata(UNITS_GYRO[1]));
		}
		if (((sensors & 0xFF) & SENSOR_MAG) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.MAGNETOMETER,new Metadata(UNITS_MAG[0]));
			else
				metadata.hashMetadata.put(SensorType.MAGNETOMETER,new Metadata(UNITS_MAG[1]));
		}
		if (((sensors & 0xFF) & SENSOR_GSR) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.GSR, new Metadata(UNITS_GSR[0]));
			else
				metadata.hashMetadata.put(SensorType.GSR, new Metadata(UNITS_GSR[1]));
		}
		if (((sensors & 0xFF) & SENSOR_ECG) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.ECG, new Metadata(UNITS_ECG[0]));
			else
				metadata.hashMetadata.put(SensorType.ECG, new Metadata(UNITS_ECG[1]));
		}
		if (((sensors & 0xFF) & SENSOR_EMG) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.EMG, new Metadata(UNITS_EMG[0]));
			else
				metadata.hashMetadata.put(SensorType.EMG, new Metadata(UNITS_EMG[1]));
		}
		if (((sensors & 0xFF) & SENSOR_STRAIN) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.STRAIN, new Metadata(UNITS_STRAIN[0]));
			else
				metadata.hashMetadata.put(SensorType.STRAIN, new Metadata(UNITS_STRAIN[1]));
		}
		if (((sensors & 0xFF) & SENSOR_HEART) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.HEART_RATE, new Metadata(UNITS_HEART[0]));

		}
		if (((sensors & 0xFF) & SENSOR_EXP_BOARD_A0) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.EXP_BOARDA0, new Metadata(UNITS_EXP_A0[0]));
			else
				metadata.hashMetadata.put(SensorType.EXP_BOARDA0, new Metadata(UNITS_EXP_A0[1]));
		}
		if (((sensors & 0xFF) & SENSOR_EXP_BOARD_A7) > 0) {
			if (format == FormatType.CALIBRATED)
				metadata.hashMetadata.put(SensorType.EXP_BOARDA7, new Metadata(UNITS_EXP_A7[0]));
			else
				metadata.hashMetadata.put(SensorType.EXP_BOARDA7, new Metadata(UNITS_EXP_A7[1]));
		}
		
		metadata.rate = myShimmerDevice.getSamplingRate();

	}

	/**
	 *  This function sets the sample rate of the external device
	 * @param rate Is the new rate.
	 */
	@Override
	public void setRate(double rate) {
		myShimmerDevice.writeSamplingRate(rate);
	}

	/**
	 * Transmits a command to the Shimmer device to enable the sensors. To
	 * enable multiple sensors an or operator should be used (e.g.
	 * writeEnabledSensors
	 * (Shimmer.SENSOR_ACCEL|Shimmer.SENSOR_GYRO|Shimmer.SENSOR_MAG)). Command
	 * should not be used consecutively.
	 * 
	 * @param enabledSensors
	 *            e.g DeviceShimmer.SENSOR_ACCEL|DeviceShimmer.SENSOR_GYRO|
	 *            DeviceShimmer.SENSOR_MAG
	 * @param nameDevice
	 *            is the device's name
	 */
	@Override
	public void writeEnabledSensors(ArrayList<SensorType> enabledSensors) {
		// TODO Auto-generated method stub

		if (!isStreaming) {
			int sensors = 0;
			for (int i = 0; i < enabledSensors.size(); i++) {
				switch (enabledSensors.get(i)) {
				case ACCELEROMETER:
					sensors = sensors | SENSOR_ACCEL;
					break;
				case GYROSCOPE:
					sensors = sensors | SENSOR_GYRO;
					break;
				case MAGNETOMETER:
					sensors = sensors | SENSOR_MAG;
					break;
				case ECG:
					sensors = sensors | SENSOR_ECG;
					break;
				case EMG:
					sensors = sensors | SENSOR_EMG;
					break;
				case GSR:
					sensors = sensors | SENSOR_GSR;
					break;
				case EXP_BOARDA0:
					sensors = sensors | SENSOR_EXP_BOARD_A0;
					break;
				case EXP_BOARDA7:
					sensors = sensors | SENSOR_EXP_BOARD_A7;
					break;
				case STRAIN:
					sensors = sensors | SENSOR_STRAIN;
					break;
				case HEART_RATE:
					sensors = sensors | SENSOR_HEART;
					break;
				}
			}

			myShimmerDevice.writeEnabledSensors(sensors);
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
			case ECG:
				sensors.add(SensorType.ECG_LALL);
				sensors.add(SensorType.ECG_RALL);
				break;
			case EMG:
				sensors.add(SensorType.EMG);
				break;
			case GSR:
				sensors.add(SensorType.GSR);
				break;
			case EXP_BOARDA0:
				sensors.add(SensorType.EXP_BOARDA0);
				break;
			case EXP_BOARDA7:
				sensors.add(SensorType.EXP_BOARDA7);
				break;
			case STRAIN:
				sensors.add(SensorType.STRAIN_GAUGE_HIGH);
				sensors.add(SensorType.STRAIN_GAUGE_LOW);
				break;
			case HEART_RATE:
				sensors.add(SensorType.HEART_RATE);
				break;
			}
		}
		sensors.add(SensorType.TIME_STAMP);
		for (int i = 0; i < MAX_SIZE; i++) {
			buffer.get(i).hashData.clear();
			for (int j = 0; j < sensors.size(); j++) {
				buffer.get(i).hashData.put(sensors.get(j), new Data(0));
			}
		}
	}


	@Override
	public void setNumberOfSampleToStorage(int samples) {
		// TODO Auto-generated method stub
		this.NUM_SAMPLE = samples;
		this.MAX_SIZE = 4*NUM_SAMPLE;
	}


	@Override
	public int getNumberOfSamples() {
		// TODO Auto-generated method stub
		return this.NUM_SAMPLE;
	}


	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return this.MAX_SIZE;
	}
}
