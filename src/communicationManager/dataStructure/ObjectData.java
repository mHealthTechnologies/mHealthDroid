package communicationManager.dataStructure;

import java.util.Hashtable;
import java.util.Set;

public class ObjectData {

	public String name;
	public Hashtable<SensorType, Data> hashData;

	
	public enum SensorType {

		ACCELEROMETER_X("AccX"),
		ACCELEROMETER_Y("AccY"), 
		ACCELEROMETER_Z("AccZ"), 
		ACCELEROMETER("Acc"), 
		MAGNETOMETER_X("MagX"), 
		MAGNETOMETER_Y("MagY"),
		MAGNETOMETER_Z("MagZ"),
		MAGNETOMETER("Mag"),
		GYROSCOPE_X("GyrX"),
		GYROSCOPE_Y("GyrY"),
		GYROSCOPE_Z("GyrZ"), 
		GYROSCOPE("Gyr"),
		GSR("Gsr"), 
		ECG_RALL("EcgR"),
		ECG_LALL("EcgL"),
		ECG("Ecg"), 
		EMG("Emg"),
		STRAIN_GAUGE_HIGH("SGH"),
		STRAIN_GAUGE_LOW("SGL"),
		STRAIN("SG"),
		HEART_RATE("HeartRate"),
		EXP_BOARDA0("ExpBA0"),
		EXP_BOARDA7("ExpBA7"),
		TIME_STAMP("TimeStamp"), 
		ORIENTATION_X("OrientX"),
		ORIENTATION_Y("OrientY"),
		ORIENTATION_Z("OrientZ"), 
		ORIENTATION("Orient"),
		AMBIENT_TEMPERATURE("Temperature"),
		GRAVITY_X("GravX"), 
		GRAVITY_Y("GravY"), 
		GRAVITY_Z("GravZ"), 
		GRAVITY("Grav"),
		LIGHT("Light"), 
		LINEAR_ACCELERATION_X("LinAccX"),
		LINEAR_ACCELERATION_Y("LinAccY"), 
		LINEAR_ACCELERATION_Z("LinAccZ"),
		LINEAR_ACCELERATION("LinAcc"),
		PRESSURE("Pressure"),
		PROXIMITY("Proximity"),
		HUMIDITY("Humidity"), 
		ROTATION_VECTOR_X("RotVecX"),
		ROTATION_VECTOR_Y("RotVecY"), 
		ROTATION_VECTOR_Z("RotVecZ"),
		ROTATION_VECTOR("RotVec"),
		V_SENSE_REG("VSenseReg"),
		V_SENSE_BATT("VSenseBatt"),
		QUARTENION0("Quart0"),
		QUARTENION1("Quart1"),
		QUARTENION2("Quart2"),
		QUARTENION3("Quart3"),
		ANGLE_AXIS_A("AngAxA"),
		ANGLE_AXIS_X("AngAxX"),
		ANGLE_AXIS_Y("AngAxY"),
		ANGLE_AXIS_Z("AngAxZ"),
		LOW_NOISE_ACCELEROMETER("LNAcc"),
		LOW_NOISE_ACCELEROMETER_X("LNAccX"),
		LOW_NOISE_ACCELEROMETER_Y("LNAccY"),
		LOW_NOISE_ACCELEROMETER_Z("LNAccZ"),
		WIDE_RANGE_ACCELEROMETER("WRAcc"),
		WIDE_RANGE_ACCELEROMETER_X("WRAccX"),
		WIDE_RANGE_ACCELEROMETER_Y("WRAccY"),
		WIDE_RANGE_ACCELEROMETER_Z("WRAccZ"),
		EXTERNAL_ADC_A7("ExtADCA7"),
		EXTERNAL_ADC_A6("ExtADCA6"),
		EXTERNAL_ADC_A15("ExtADCA15"),
		INTERNAL_ADC_A1("IntADCA1"),
		INTERNAL_ADC_A12("IntADCA12"),
		INTERNAL_ADC_A13("IntADCA13"),
		INTERNAL_ADC_A14("IntADCA14"),
		BMP180("Bmp180"),
		TEMPERATURE("Temp"),
		EXG1_24B("Exg1.24B"),
		EXG2_24B("Exg2.24B"),
		EXG1_16B("Exg1.16B"),
		EXG2_16B("Exg2.16B"),
		EXG1_STATUS("Exg1Stat"),
		EXG2_STATUS("Exg2Stat"),
		ECG_LLRA("EcgLLRA"),
		ECG_LARA("EcgLARA"),
		EMG_CH1("EmgCh1"),
		EMG_CH2("EmgCh2"),
		EXG1_CH1("EXg1Ch1"),
		EXG1_CH2("EXg1Ch2"),
		ECG_VXRL("EcgVxrl"),
		EXG2_CH1("EXg2Ch1"),
		EXG2_CH2("EXg2Ch2"),
		EXG1_CH1_16B("EXg1Ch1.16b"),
		EXG1_CH2_16B("EXg1Ch2.16b"),
		EXG2_CH1_16B("EXg2Ch1.16b"),
		EXG2_CH2_16B("EXg2Ch2.16b");
		

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
