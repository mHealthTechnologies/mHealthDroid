package communicationManager.dataStructure;

import java.util.Hashtable;
import java.util.Set;

public class ObjectData {

	public String name;
	public Hashtable<SensorType, Data> hashData;

	public enum SensorType {

		ACCELEROMETER_X("AccX"), ACCELEROMETER_Y("AccY"), ACCELEROMETER_Z("AccZ"), 
		ACCELEROMETER("Acc"), MAGNETOMETER_X("MagX"), MAGNETOMETER_Y("MagY"), MAGNETOMETER_Z("MagZ"),
		MAGNETOMETER("Mag"), GYROSCOPE_X("GyrX"), GYROSCOPE_Y("GyrY"), GYROSCOPE_Z("GyrZ"), 
		GYROSCOPE("Gyr"), GSR("Gsr"), ECG_RALL("EcgR"), ECG_LALL("EcgL"), ECG("Ecg"), EMG("Emg"),
		STRAIN_GAUGE_HIGH("SGH"), STRAIN_GAUGE_LOW("SGL"), STRAIN("SG"), HEART_RATE("HeartRate"),
		EXP_BOARDA0("ExpBA0"), EXP_BOARDA7("ExpBA7"), TIME_STAMP("TimeStamp"), 
		ORIENTATION_X("OrientX"), ORIENTATION_Y("OrientY"), ORIENTATION_Z("OrientZ"), 
		ORIENTATION("Orient"), AMBIENT_TEMPERATURE("Temperature"), GRAVITY_X("GravX"), 
		GRAVITY_Y("GravY"), GRAVITY_Z("GravZ"), GRAVITY("Grav"), LIGHT("Light"), 
		LINEAR_ACCELERATION_X("LinAccX"), LINEAR_ACCELERATION_Y("LinAccY"), 
		LINEAR_ACCELERATION_Z("LinAccZ"), LINEAR_ACCELERATION("LinAcc"), PRESSURE("Pressure"),
		PROXIMITY("Proximity"), HUMIDITY("Humidity"), ROTATION_VECTOR_X("RotVecX"),
		ROTATION_VECTOR_Y("RotVecY"), ROTATION_VECTOR_Z("RotVecZ"), ROTATION_VECTOR("RotVec");

		private String abbreviature;

		/**
		 * Constructor. Creates a new SensorType object
		 * @param abbreviature abbreviature for the SensorType
		 */
		SensorType(String abbreviature) {
			this.abbreviature = abbreviature;
		}

		/**
		 * Method to get a SensorType abbreviature
		 * @return
		 */
		public String getAbbreviature() {
			return abbreviature;
		}

	}

	/**
	 * Constructor. Creates a new ObjectData object
	 */
	public ObjectData() {
		super();
		hashData = new Hashtable<SensorType, Data>();
	}

	/**
	 * Constructor. Creates a new ObjectData object
	 * @param name name for the ObjectData object
	 */
	public ObjectData(String name) {
		super();
		this.name = name;
		hashData = new Hashtable<SensorType, Data>();
	}

	/**
	 * Constructor. Creates a new ObjectData as copy of other ObjectData
	 * @param od ObjectData which will be copied
	 */
	public ObjectData(ObjectData od) {

		this.name = od.name;
		this.hashData = new Hashtable<ObjectData.SensorType, Data>();

		Set<SensorType> set = od.hashData.keySet();
		for (SensorType sensor : set) {
			Data d = od.hashData.get(sensor);
			this.hashData.put(sensor, d);
		}
	}
}
