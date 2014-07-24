package communicationManager.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import communicationManager.dataStructure.ObjectData.SensorType;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Pair;

public class DBAdapter {

	public static final String ID = "id";
	public static final String ACCELEROMETER_X = "AccX";
	public static final String ACCELEROMETER_Y = "AccY";
	public static final String ACCELEROMETER_Z = "AccZ";
	public static final String ACCELEROMETER = "Acc";
	public static final String LOW_NOISE_ACCELEROMETER = "LNAcc";
	public static final String WIDE_RANGE_ACCELEROMETER = "WRAcc";
	public static final String MAGNOMETER_X = "MagX";
	public static final String MAGNOMETER_Y = "MagY";
	public static final String MAGNOMETER_Z = "MagZ";
	public static final String MAGNOMETER = "Mag";
	public static final String GYROSCOPE_X = "GyrX";
	public static final String GYROSCOPE_Y = "GyrY";
	public static final String GYROSCOPE_Z = "GyrZ";
	public static final String GYROSCOPE = "Gyr";
	public static final String GSR = "Gsr";
	public static final String ECG_RALL = "EcgR";
	public static final String ECG_LALL = "EcgL";
	public static final String ECG = "Ecg";
	public static final String EMG = "Emg";
	public static final String STRAIN_GAUGE_HIGH = "SGH";
	public static final String STRAIN_GAUGE_LOW = "SGL";
	public static final String STRAIN_GAUGE = "SG";
	public static final String HEART_RATE = "HeartRate";
	public static final String EXP_BOARDA0 = "ExpBA0";
	public static final String EXP_BOARDA7 = "ExpBA7";
	
	public static final String ANGLE_AXIS_A = "AAA";
	public static final String ANGLE_AXIS_X = "AAX";
	public static final String ANGLE_AXIS_Y = "AAY";
	public static final String ANGLE_AXIS_Z = "AAZ";
	public static final String QUARTENION0 = "Q0";
	public static final String QUARTENION1 = "Q1";
	public static final String QUARTENION2 = "Q2";
	public static final String QUARTENION3 = "Q3";
	public static final String ORIENTATION_3D = "Orientation";
	public static final String V_SENSE_REG = "VSReg";
	public static final String V_SENSE_BATT = "VSBatt";
	
	public static final String LOW_NOISE_ACCELEROMETER_X = "LNAccX";
	public static final String LOW_NOISE_ACCELEROMETER_Y = "LNAccY";
	public static final String LOW_NOISE_ACCELEROMETER_Z = "LNAccZ";
	public static final String WIDE_RANGE_ACCELEROMETER_X = "WRAccX";
	public static final String WIDE_RANGE_ACCELEROMETER_Y = "WRAccY";
	public static final String WIDE_RANGE_ACCELEROMETER_Z = "WRAccZ";
	public static final String TEMPERATURE = "Temp";
	public static final String EXTERNAL_ADC_A6 = "ExADCA6";
	public static final String EXTERNAL_ADC_A7 = "ExADCA7";
	public static final String EXTERNAL_ADC_A15 = "ExADCA15";
	public static final String INTERNAL_ADC_A1 = "InADCA1";
	public static final String INTERNAL_ADC_A12 = "InADCA12";
	public static final String INTERNAL_ADC_A13 = "InADCA13";
	public static final String INTERNAL_ADC_A14 = "InADCA14";
	public static final String ECG_LLRA = "EcgLL";
	public static final String ECG_LARA = "EcgLA";
	public static final String EMG_CH1 = "EmgCh1";
	public static final String EMG_CH2 = "EmgCh2";
	public static final String EXG1_CH1 = "EXg1Ch1";
	public static final String EXG1_CH2 = "Exg1Ch2";
	public static final String EXG1_CH1_16BIT = "Exg1Ch1_16Bit";
	public static final String EXG1_CH2_16BIT = "Exg1Ch2_16Bit";
	public static final String EXG2_CH1 = "Exg2Ch1";
	public static final String EXG2_CH2 = "Exg2Ch2";
	public static final String ECG_VxRL = "EcgVxRL";
	public static final String EXG2_CH1_16BIT = "Exg2Ch1_16Bit";
	public static final String EXG2_CH2_16BIT = "Exg2Ch2_16Bit";
	public static final String EXG1_STATUS = "Exg1Status";
	public static final String EXG2_STATUS = "Exg2Status";
	public static final String EXG1_24BIT = "Exg1_24Bit";
	public static final String EXG2_24BIT = "Exg2_24Bit";
	public static final String EXG1_16BIT = "Exg1_16Bit";
	public static final String EXG2_16BIT = "Exg2_16Bit";
	public static final String BMP180 = "BMP180"; // this is the sensor for the pressure and the temperature
	
	public static final String TIME_STAMP = "TimeStamp";
	public static final String LIGHT = "Light";
	public static final String PRESSURE = "Pressure";
	public static final String PROXIMITY = "Proximity";
	public static final String GRAVITY = "Grav";
	public static final String GRAVITY_X = "GravX";
	public static final String GRAVITY_Y = "GravY";
	public static final String GRAVITY_Z = "GravZ";
	public static final String LINEAR_ACCELERATION = "LinAcc";
	public static final String LINEAR_ACCELERATION_X = "LinAccX";
	public static final String LINEAR_ACCELERATION_Y = "LinAccY";
	public static final String LINEAR_ACCELERATION_Z = "LinAccZ";
	public static final String ROTATION_VECTOR = "RotVec";
	public static final String ROTATION_VECTOR_X = "RotVecX";
	public static final String ROTATION_VECTOR_Y = "RotVecY";
	public static final String ROTATION_VECTOR_Z = "RotVecZ";
	public static final String HUMIDITY = "Humidity";
	public static final String AMBIENT_TEMPERATURE = "Temperature";
	public static final String LABEL = "Label";
	public static final String RATE = "Sample_Rate";
	public static final String NAME_DEVICE = "Device";
	public static final String FORMAT = "Format";
	public static final String START = "Start";
	public static final String FINISH = "Finish";
	public static final String FIRST_INDEX = "First";
	public static final String LAST_INDEX = "Last";
	public static final String CALIBRATED = "Calibrated";
	public static final String UNCALIBRATED = "Uncalibrated";
	public static final String SENSORS = "Sensors";
	public static final String LOGIN = "Login";
	public static final String PASSWORD = "Password";
	public static final String SEX = "Sex";
	public static final String AGE = "Age";
	public static final String HEIGHT = "Height";
	public static final String WEIGHT = "Weight";
	public static final String EMAIL = "Email";
	public static final String TYPE_DEVICE = "Device";
	public static final String TABLE_SHIMMER_UNITS = "Shimmer_Units";
	public static final String TABLE_MOBILE_UNITS = "Mobile_Units";
	public static final String TABLE_USER_PROFILE = "User_Profile";
	public static final String DATABASE_NAME = "MyDB";
	public static final int DATABASE_VERSION = 1;

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	/**
	 * Constructor. Creates a new DBAdapter object
	 * @param ctx IU current activity context
	 */
	public DBAdapter(Context ctx) {

		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	/**
	 * Method to open the DB
	 * @return a read/write valid database
	 * @throws SQLException
	 */
	public DBAdapter open() throws SQLException {

		db = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Method to close the DB
	 */
	public void close() {

		DBHelper.close();
	}

	/**
	 * Method to create a shimmer 2 signal table
	 * @param name table name
	 */
	public void createShimmer2Table(String name) {

		String sql = "create table if not exists " + name + " (" + ID
				+ " integer primary key autoincrement, " + ACCELEROMETER_X
				+ " double," + ACCELEROMETER_Y + " double," + ACCELEROMETER_Z
				+ " double," + MAGNOMETER_X + " double," + MAGNOMETER_Y
				+ " double," + MAGNOMETER_Z + " double," + GYROSCOPE_X
				+ " double," + GYROSCOPE_Y + " double," + GYROSCOPE_Z
				+ " double," + GSR + " double," + ECG_RALL + " double,"
				+ ECG_LALL + " double," + EMG + " double," + STRAIN_GAUGE_HIGH
				+ " double," + STRAIN_GAUGE_LOW + " double," + HEART_RATE
				+ " double," + EXP_BOARDA0 + " double," + EXP_BOARDA7
				+ " double," + V_SENSE_REG + " double," + V_SENSE_BATT 
				+ " double," + ANGLE_AXIS_A + " double," + ANGLE_AXIS_X
				+ " double," + ANGLE_AXIS_Y + " double," + ANGLE_AXIS_Z
				+ " double," + QUARTENION0 + " double," + QUARTENION1
				+ " double," + QUARTENION2 + " double," + QUARTENION3
				+ " double," + LABEL +" text,"+ TIME_STAMP + " double" + ");";

		db.execSQL(sql);
	}
	
	/**
	 * Method to create a shimmer 3 signal table
	 * @param name table name
	 */
	public void createShimmer3Table(String name) {

		String sql = "create table if not exists " + name + " (" + ID
				+ " integer primary key autoincrement, " + LOW_NOISE_ACCELEROMETER_X
				+ " double," + LOW_NOISE_ACCELEROMETER_Y + " double," + LOW_NOISE_ACCELEROMETER_Z
				+ " double," + WIDE_RANGE_ACCELEROMETER_X + " double," + WIDE_RANGE_ACCELEROMETER_Y
				+ " double," + WIDE_RANGE_ACCELEROMETER_Z
				+ " double," + MAGNOMETER_X + " double," + MAGNOMETER_Y
				+ " double," + MAGNOMETER_Z + " double," + GYROSCOPE_X
				+ " double," + GYROSCOPE_Y + " double," + GYROSCOPE_Z
				+ " double," + GSR + " double," + V_SENSE_BATT + " double," + EXTERNAL_ADC_A6
				+ " double," + EXTERNAL_ADC_A7 + " double," + EXTERNAL_ADC_A15
				+ " double," + INTERNAL_ADC_A1 + " double," + INTERNAL_ADC_A12
				+ " double," + INTERNAL_ADC_A13 + " double," + INTERNAL_ADC_A14
				+ " double," + PRESSURE + " double," + TEMPERATURE
				+ " double," + EXG1_STATUS + " double," + EXG2_STATUS
				+ " double," + ECG_LLRA + " double," + ECG_LARA
				+ " double," + EMG_CH1 + " double," + EMG_CH2
				+ " double," + EXG1_CH1 + " double," + EXG1_CH2
				+ " double," + EXG2_CH1 + " double," + ECG_VxRL
				+ " double," + EXG2_CH2
				+ " double," + EXG1_CH1_16BIT + " double," + EXG1_CH2_16BIT
				+ " double," + EXG2_CH1_16BIT + " double," + EXG2_CH2_16BIT
				+ " double," + ANGLE_AXIS_A + " double," + ANGLE_AXIS_X
				+ " double," + ANGLE_AXIS_Y + " double," + ANGLE_AXIS_Z
				+ " double," + QUARTENION0 + " double," + QUARTENION1
				+ " double," + QUARTENION2 + " double," + QUARTENION3
				+ " double," + LABEL +" text,"+ TIME_STAMP + " double" + ");";

		db.execSQL(sql);
	}

	/**
	 * Method to create a metadata signal table for the Shimmer 2
	 * @param nameMetadata table name
	 */
	public void createShimmer2TableMetadata(String nameMetadata) {

		String sql = "create table if not exists " + nameMetadata + " (" + ID
				+ " integer primary key autoincrement, " + ACCELEROMETER
				+ " integer," + MAGNOMETER + " integer," + GYROSCOPE
				+ " integer," + GSR + " integer," + ECG + " integer," + EMG
				+ " integer," + STRAIN_GAUGE + " integer," + HEART_RATE
				+ " integer," + EXP_BOARDA0 + " integer," + EXP_BOARDA7
				+ " integer," + V_SENSE_BATT + " integer," + V_SENSE_REG 
				+ " integer," + ORIENTATION_3D + " integer," + TYPE_DEVICE 
				+ " text," + FORMAT + " text," + START + " text," + FINISH
				+ " text," + FIRST_INDEX + " integer," + LAST_INDEX	
				+ " integer," + RATE + " integer"+ ");";

		db.execSQL(sql);
	}
	
	/**
	 * Method to create a metadata signal table for the Shimmer 3
	 * @param nameMetadata table name
	 */
	public void createShimmer3TableMetadata(String nameMetadata) {

		String sql = "create table if not exists " + nameMetadata + " (" + ID
				+ " integer primary key autoincrement, " + LOW_NOISE_ACCELEROMETER
				+ " integer," + WIDE_RANGE_ACCELEROMETER 
				+ " integer," + MAGNOMETER + " integer," + GYROSCOPE
				+ " integer," + GSR + " integer," + BMP180 
				+ " integer," + EXG1_24BIT + " integer," + EXG2_24BIT
				+ " integer," + EXG1_16BIT + " integer," + EXG2_16BIT
				+ " integer," + V_SENSE_BATT + " integer," + EXTERNAL_ADC_A6
				+ " integer," + EXTERNAL_ADC_A7 + " integer," + EXTERNAL_ADC_A15
				+ " integer," + INTERNAL_ADC_A1 + " integer," + INTERNAL_ADC_A12
				+ " integer," + INTERNAL_ADC_A13 + " integer," + INTERNAL_ADC_A14
				+ " integer," + ORIENTATION_3D + " integer," + TYPE_DEVICE
				+ " text," + FORMAT + " text," + START + " text," + FINISH
				+ " text," + FIRST_INDEX + " integer," + LAST_INDEX	
				+ " integer," + RATE + " integer"+ ");";

		db.execSQL(sql);
	}

	/**
	 * Method to create a shimmer units table
	 */
	public void createShimmerTableUnits() {

		String sql = "create table if not exists " + TABLE_SHIMMER_UNITS + " ("
				+ SENSORS + " text," + CALIBRATED + " text," + UNCALIBRATED
				+ " text" + ");";

		db.execSQL(sql);
	}

	/**
	 * Method to create a mobile units table
	 */
	public void createMobileTableUnits() {

		String sql = "create table if not exists " + TABLE_MOBILE_UNITS + " ("
				+ SENSORS + " text," + CALIBRATED + " text" + ");";

		db.execSQL(sql);
	}

	/**
	 * Method to create a signal mobile table
	 * @param name table name
	 */
	public void createMobileTable(String name) {

		String sql = "create table if not exists " + name + " (" + ID
				+ " integer primary key autoincrement, " + ACCELEROMETER_X
				+ " double," + ACCELEROMETER_Y + " double," + ACCELEROMETER_Z
				+ " double," + MAGNOMETER_X + " double," + MAGNOMETER_Y
				+ " double," + MAGNOMETER_Z + " double," + GYROSCOPE_X
				+ " double," + GYROSCOPE_Y + " double," + GYROSCOPE_Z
				+ " double," + LIGHT + " double," + PRESSURE + " double,"
				+ PROXIMITY + " double," + GRAVITY_X + " double," + GRAVITY_Y
				+ " double," + GRAVITY_Z + " double," + LINEAR_ACCELERATION_X
				+ " double," + LINEAR_ACCELERATION_Y + " double,"
				+ LINEAR_ACCELERATION_Z + " double," + ROTATION_VECTOR_X
				+ " double," + ROTATION_VECTOR_Y + " double,"
				+ ROTATION_VECTOR_Z + " double," + HUMIDITY + " double,"
				+ AMBIENT_TEMPERATURE + " double,"+ LABEL +" text," + TIME_STAMP + " double"
				+ ");";

		db.execSQL(sql);
	}

	/**
	 * Method to create a mobile metadata table
	 * @param nameMetadata table name
	 */
	public void createMobileTableMetadata(String nameMetadata) {

		String sql = "create table if not exists " + nameMetadata + " (" + ID
				+ " integer primary key autoincrement, " + ACCELEROMETER
				+ " integer," + MAGNOMETER + " integer," + GYROSCOPE
				+ " integer," + AMBIENT_TEMPERATURE + " integer," + GRAVITY
				+ " integer," + LIGHT + " integer," + LINEAR_ACCELERATION
				+ " integer," + PRESSURE + " integer," + PROXIMITY
				+ " integer," + HUMIDITY + " integer," + ROTATION_VECTOR
				+ " integer," + FORMAT + " text," + START + " text," + FINISH
				+ " text," + FIRST_INDEX + " integer," + LAST_INDEX
				+ " integer," + RATE + " integer" + ");";

		db.execSQL(sql);
	}
	
	/**
	 * Method to create an user profile table
	 */
	public void createUserProfileTable() {

		String sql = "create table if not exists " + TABLE_USER_PROFILE + " (" 
				+ LOGIN + " text primary key," + PASSWORD + " text not null," + SEX 
				+ " text," + AGE + " integer," + HEIGHT + " double," + WEIGHT 
				+ " double," + EMAIL + " text" + ");";

		db.execSQL(sql);
	}
	
	/**
	 * Method to insert user profile
	 * @param login user login
	 * @param password user password
	 * @param sex user sex
	 * @param age user age
	 * @param height user height
	 * @param weight user weight
	 * @param email user email 
	 */
	public void insertUserProfile(String login, String password, String sex, int age, float height, float weight, String email){
		
		String sql =  "INSERT INTO " + TABLE_USER_PROFILE + " (" + LOGIN + ", " 
				+ PASSWORD + ", " + SEX + ", " + AGE + ", " + HEIGHT + ", "
				+ WEIGHT + ", " + EMAIL + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);
		
		stmt.bindString(1, login);
		stmt.bindString(2, password);
		stmt.bindString(3, sex);
		stmt.bindLong(4, age);
		stmt.bindDouble(5, height);
		stmt.bindDouble(6, weight);
		stmt.bindString(7, email);
		stmt.executeInsert();
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to insert signal rows from the device shimmer 2
	 * @param array array with the information to insert in the table
	 * @param label labeling column for the rows
	 * @param nameTable table name
	 */
	public void insertShimmer2Signal(double[][] array, String label,  String nameTable) {

		String sql = "INSERT INTO "	+ nameTable	+ " (" + ACCELEROMETER_X + ", "	+ ACCELEROMETER_Y
				+ ", " + ACCELEROMETER_Z + ", "	+ MAGNOMETER_X + ", " + MAGNOMETER_Y + ", "
				+ MAGNOMETER_Z + ", " + GYROSCOPE_X	+ ", " + GYROSCOPE_Y + ", "	+ GYROSCOPE_Z
				+ ", " + GSR + ", "	+ ECG_RALL + ", " + ECG_LALL + ", " + EMG + ", "
				+ STRAIN_GAUGE_HIGH + ", " + STRAIN_GAUGE_LOW + ", " + HEART_RATE + ", "
				+ EXP_BOARDA0 + ", " + EXP_BOARDA7 	+ ", " + V_SENSE_REG + ", " + V_SENSE_BATT
				+ ", " + ANGLE_AXIS_A + ", " + ANGLE_AXIS_X + ", " + ANGLE_AXIS_Y
				+ ", " + ANGLE_AXIS_Z + ", " + QUARTENION0 + ", " + QUARTENION1 
				+ ", " + QUARTENION2 + ", " + QUARTENION3 + ", " + TIME_STAMP + ", "+ LABEL 
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		for (int i = 0; i < array.length; i++) {
			if (!Double.isNaN(array[i][0]))
				stmt.bindDouble(1, array[i][0]);
			else
				stmt.bindNull(1);
			if (!Double.isNaN(array[i][1]))
				stmt.bindDouble(2, array[i][1]);
			else
				stmt.bindNull(2);
			if (!Double.isNaN(array[i][2]))
				stmt.bindDouble(3, array[i][2]);
			else
				stmt.bindNull(3);
			if (!Double.isNaN(array[i][3]))
				stmt.bindDouble(4, array[i][3]);
			else
				stmt.bindNull(4);
			if (!Double.isNaN(array[i][4]))
				stmt.bindDouble(5, array[i][4]);
			else
				stmt.bindNull(5);
			if (!Double.isNaN(array[i][5]))
				stmt.bindDouble(6, array[i][5]);
			else
				stmt.bindNull(6);
			if (!Double.isNaN(array[i][6]))
				stmt.bindDouble(7, array[i][6]);
			else
				stmt.bindNull(7);
			if (!Double.isNaN(array[i][7]))
				stmt.bindDouble(8, array[i][7]);
			else
				stmt.bindNull(8);
			if (!Double.isNaN(array[i][8]))
				stmt.bindDouble(9, array[i][8]);
			else
				stmt.bindNull(9);
			if (!Double.isNaN(array[i][9]))
				stmt.bindDouble(10, array[i][9]);
			else
				stmt.bindNull(10);
			if (!Double.isNaN(array[i][10]))
				stmt.bindDouble(11, array[i][10]);
			else
				stmt.bindNull(11);
			if (!Double.isNaN(array[i][11]))
				stmt.bindDouble(12, array[i][11]);
			else
				stmt.bindNull(12);
			if (!Double.isNaN(array[i][12]))
				stmt.bindDouble(13, array[i][12]);
			else
				stmt.bindNull(13);
			if (!Double.isNaN(array[i][13]))
				stmt.bindDouble(14, array[i][13]);
			else
				stmt.bindNull(14);
			if (!Double.isNaN(array[i][14]))
				stmt.bindDouble(15, array[i][14]);
			else
				stmt.bindNull(15);
			if (!Double.isNaN(array[i][15]))
				stmt.bindDouble(16, array[i][15]);
			else
				stmt.bindNull(16);
			if (!Double.isNaN(array[i][16]))
				stmt.bindDouble(17, array[i][16]);
			else
				stmt.bindNull(17);
			if (!Double.isNaN(array[i][17]))
				stmt.bindDouble(18, array[i][17]);
			else
				stmt.bindNull(18);
			if (!Double.isNaN(array[i][18]))
				stmt.bindDouble(19, array[i][18]);
			else
				stmt.bindNull(19);
			if (!Double.isNaN(array[i][19]))
				stmt.bindDouble(20, array[i][19]);
			else
				stmt.bindNull(20);
			if (!Double.isNaN(array[i][20]))
				stmt.bindDouble(21, array[i][20]);
			else
				stmt.bindNull(21);
			if (!Double.isNaN(array[i][21]))
				stmt.bindDouble(22, array[i][21]);
			else
				stmt.bindNull(22);
			if (!Double.isNaN(array[i][22]))
				stmt.bindDouble(23, array[i][22]);
			else
				stmt.bindNull(23);
			if (!Double.isNaN(array[i][23]))
				stmt.bindDouble(24, array[i][23]);
			else
				stmt.bindNull(24);
			if (!Double.isNaN(array[i][24]))
				stmt.bindDouble(25, array[i][24]);
			else
				stmt.bindNull(25);
			if (!Double.isNaN(array[i][25]))
				stmt.bindDouble(26, array[i][25]);
			else
				stmt.bindNull(26);
			if (!Double.isNaN(array[i][26]))
				stmt.bindDouble(27, array[i][26]);
			else
				stmt.bindNull(27);
			if (!Double.isNaN(array[i][27]))
				stmt.bindDouble(28, array[i][27]);
			else
				stmt.bindNull(28);			
			if (!Double.isNaN(array[i][28]))
				stmt.bindDouble(29, array[i][28]);
			else
				stmt.bindNull(29);
			if (label!=null)
				stmt.bindString(30, label);
			else
				stmt.bindNull(30);

			stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	/**
	 * Method to insert signal rows from the device shimmer 2
	 * @param array array with the information to insert in the table
	 * @param label labeling column for the rows
	 * @param nameTable table name
	 */
	public void insertShimmer3Signal(double[][] array, String label,  String nameTable) {

		String sql = "INSERT INTO "	+ nameTable	
				+ " (" + LOW_NOISE_ACCELEROMETER_X + ", " + LOW_NOISE_ACCELEROMETER_Y + ", " + LOW_NOISE_ACCELEROMETER_Z 
				+ ", " + WIDE_RANGE_ACCELEROMETER_X + ", " + WIDE_RANGE_ACCELEROMETER_Y + ", " + WIDE_RANGE_ACCELEROMETER_Z
				+ ", " + MAGNOMETER_X + ", " + MAGNOMETER_Y + ", " + MAGNOMETER_Z 
				+ ", " + GYROSCOPE_X	+ ", " + GYROSCOPE_Y + ", "	+ GYROSCOPE_Z
				+ ", " + GSR + ", "	+ V_SENSE_BATT 
				+ ", " + EXTERNAL_ADC_A6 + ", " + EXTERNAL_ADC_A7 + ", " + EXTERNAL_ADC_A15
				+ ", " + INTERNAL_ADC_A1 + ", " + INTERNAL_ADC_A12 + ", " + INTERNAL_ADC_A13 + ", " + INTERNAL_ADC_A14
				+ ", " + PRESSURE + ", " + TEMPERATURE
				+ ", " + EXG1_STATUS + ", " + EXG2_STATUS
				+ ", " + ECG_LLRA + ", " + ECG_LARA + ", " + EMG_CH1 + ", " + EMG_CH2
				+ ", " + EXG1_CH1 + ", " + EXG1_CH2 
				+ ", " + EXG2_CH1 + ", " + ECG_VxRL + ", " + EXG2_CH2
				+ ", " + EXG1_CH1_16BIT + ", " + EXG1_CH2_16BIT
				+ ", " + EXG2_CH1_16BIT + ", " + EXG2_CH2_16BIT
				+ ", " + ANGLE_AXIS_A + ", " + ANGLE_AXIS_X + ", " + ANGLE_AXIS_Y
				+ ", " + ANGLE_AXIS_Z + ", " + QUARTENION0 + ", " + QUARTENION1 
				+ ", " + QUARTENION2 + ", " + QUARTENION3 + ", " + TIME_STAMP + ", "+ LABEL 
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
				+ ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		for (int i = 0; i < array.length; i++) {
			if (!Double.isNaN(array[i][0]))
				stmt.bindDouble(1, array[i][0]);
			else
				stmt.bindNull(1);
			if (!Double.isNaN(array[i][1]))
				stmt.bindDouble(2, array[i][1]);
			else
				stmt.bindNull(2);
			if (!Double.isNaN(array[i][2]))
				stmt.bindDouble(3, array[i][2]);
			else
				stmt.bindNull(3);
			if (!Double.isNaN(array[i][3]))
				stmt.bindDouble(4, array[i][3]);
			else
				stmt.bindNull(4);
			if (!Double.isNaN(array[i][4]))
				stmt.bindDouble(5, array[i][4]);
			else
				stmt.bindNull(5);
			if (!Double.isNaN(array[i][5]))
				stmt.bindDouble(6, array[i][5]);
			else
				stmt.bindNull(6);
			if (!Double.isNaN(array[i][6]))
				stmt.bindDouble(7, array[i][6]);
			else
				stmt.bindNull(7);
			if (!Double.isNaN(array[i][7]))
				stmt.bindDouble(8, array[i][7]);
			else
				stmt.bindNull(8);
			if (!Double.isNaN(array[i][8]))
				stmt.bindDouble(9, array[i][8]);
			else
				stmt.bindNull(9);
			if (!Double.isNaN(array[i][9]))
				stmt.bindDouble(10, array[i][9]);
			else
				stmt.bindNull(10);
			if (!Double.isNaN(array[i][10]))
				stmt.bindDouble(11, array[i][10]);
			else
				stmt.bindNull(11);
			if (!Double.isNaN(array[i][11]))
				stmt.bindDouble(12, array[i][11]);
			else
				stmt.bindNull(12);
			if (!Double.isNaN(array[i][12]))
				stmt.bindDouble(13, array[i][12]);
			else
				stmt.bindNull(13);
			if (!Double.isNaN(array[i][13]))
				stmt.bindDouble(14, array[i][13]);
			else
				stmt.bindNull(14);
			if (!Double.isNaN(array[i][14]))
				stmt.bindDouble(15, array[i][14]);
			else
				stmt.bindNull(15);
			if (!Double.isNaN(array[i][15]))
				stmt.bindDouble(16, array[i][15]);
			else
				stmt.bindNull(16);
			if (!Double.isNaN(array[i][16]))
				stmt.bindDouble(17, array[i][16]);
			else
				stmt.bindNull(17);
			if (!Double.isNaN(array[i][17]))
				stmt.bindDouble(18, array[i][17]);
			else
				stmt.bindNull(18);
			if (!Double.isNaN(array[i][18]))
				stmt.bindDouble(19, array[i][18]);
			else
				stmt.bindNull(19);
			if (!Double.isNaN(array[i][19]))
				stmt.bindDouble(20, array[i][19]);
			else
				stmt.bindNull(20);
			if (!Double.isNaN(array[i][20]))
				stmt.bindDouble(21, array[i][20]);
			else
				stmt.bindNull(21);
			if (!Double.isNaN(array[i][21]))
				stmt.bindDouble(22, array[i][21]);
			else
				stmt.bindNull(22);
			if (!Double.isNaN(array[i][22]))
				stmt.bindDouble(23, array[i][22]);
			else
				stmt.bindNull(23);
			if (!Double.isNaN(array[i][23]))
				stmt.bindDouble(24, array[i][23]);
			else
				stmt.bindNull(24);
			if (!Double.isNaN(array[i][24]))
				stmt.bindDouble(25, array[i][24]);
			else
				stmt.bindNull(25);
			if (!Double.isNaN(array[i][25]))
				stmt.bindDouble(26, array[i][25]);
			else
				stmt.bindNull(26);
			if (!Double.isNaN(array[i][26]))
				stmt.bindDouble(27, array[i][26]);
			else
				stmt.bindNull(27);
			if (!Double.isNaN(array[i][27]))
				stmt.bindDouble(28, array[i][27]);
			else
				stmt.bindNull(28);			
			if (!Double.isNaN(array[i][28]))
				stmt.bindDouble(29, array[i][28]);
			else
				stmt.bindNull(29);
			if (!Double.isNaN(array[i][29]))
				stmt.bindDouble(30, array[i][29]);
			else
				stmt.bindNull(30);
			if (!Double.isNaN(array[i][30]))
				stmt.bindDouble(31, array[i][30]);
			else
				stmt.bindNull(31);
			if (!Double.isNaN(array[i][31]))
				stmt.bindDouble(32, array[i][31]);
			else
				stmt.bindNull(32);
			if (!Double.isNaN(array[i][32]))
				stmt.bindDouble(33, array[i][32]);
			else
				stmt.bindNull(33);
			if (!Double.isNaN(array[i][33]))
				stmt.bindDouble(34, array[i][33]);
			else
				stmt.bindNull(34);
			if (!Double.isNaN(array[i][34]))
				stmt.bindDouble(35, array[i][34]);
			else
				stmt.bindNull(35);
			if (!Double.isNaN(array[i][35]))
				stmt.bindDouble(36, array[i][35]);
			else
				stmt.bindNull(36);
			if (!Double.isNaN(array[i][37]))
				stmt.bindDouble(38, array[i][37]);
			else
				stmt.bindNull(38);
			if (!Double.isNaN(array[i][38]))
				stmt.bindDouble(39, array[i][38]);
			else
				stmt.bindNull(39);
			if (!Double.isNaN(array[i][39]))
				stmt.bindDouble(40, array[i][39]);
			else
				stmt.bindNull(40);
			if (!Double.isNaN(array[i][40]))
				stmt.bindDouble(41, array[i][40]);
			else
				stmt.bindNull(41);
			if (!Double.isNaN(array[i][41]))
				stmt.bindDouble(42, array[i][41]);
			else
				stmt.bindNull(42);
			if (!Double.isNaN(array[i][42]))
				stmt.bindDouble(43, array[i][42]);
			else
				stmt.bindNull(43);
			if (!Double.isNaN(array[i][43]))
				stmt.bindDouble(44, array[i][43]);
			else
				stmt.bindNull(44);
			if (!Double.isNaN(array[i][44]))
				stmt.bindDouble(45, array[i][44]);
			else
				stmt.bindNull(45);
			if (!Double.isNaN(array[i][45]))
				stmt.bindDouble(46, array[i][45]);
			else
				stmt.bindNull(46);
			if (!Double.isNaN(array[i][46]))
				stmt.bindDouble(47, array[i][46]);
			else
				stmt.bindNull(47);			
			if (label!=null)
				stmt.bindString(48, label);
			else
				stmt.bindNull(48);

			stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to insert a shimmer 2 medatata row (a session)
	 * @param array array with metadata values related to Sensors activated
	 * @param nameTable table name
	 * @param format format (calibrated or uncalibrated)
	 * @param start session starting time
	 * @param finish session ending time
	 * @param rate session rate
	 */
	public void insertShimmer2Metadata(int[] array, String nameTable, String typeDevice,
			String format, String start, String finish, double rate) {

		String sql = "INSERT INTO " + nameTable + " (" + ACCELEROMETER + ", "
				+ MAGNOMETER + ", " + GYROSCOPE + ", " + GSR + ", " + ECG
				+ ", " + EMG + ", " + STRAIN_GAUGE + ", " + HEART_RATE + ", "
				+ EXP_BOARDA0 + ", " + EXP_BOARDA7 + ", " + V_SENSE_BATT
				+ ", " + V_SENSE_REG + ", " + ORIENTATION_3D + ", " + TYPE_DEVICE
				+ ", " + FORMAT + ", " + START + ", " + FINISH + ", " + FIRST_INDEX + ", "
				+ LAST_INDEX+ ", "+ RATE +") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		stmt.bindLong(1, array[0]);
		stmt.bindLong(2, array[1]);
		stmt.bindLong(3, array[2]);
		stmt.bindLong(4, array[3]);
		stmt.bindLong(5, array[4]);
		stmt.bindLong(6, array[5]);
		stmt.bindLong(7, array[6]);
		stmt.bindLong(8, array[7]);
		stmt.bindLong(9, array[8]);
		stmt.bindLong(10, array[9]);
		stmt.bindLong(11, array[10]);
		stmt.bindLong(12, array[11]);
		stmt.bindLong(13, array[12]);
		stmt.bindString(14, typeDevice);
		stmt.bindString(15, format);
		stmt.bindString(16, start);
		stmt.bindString(17, finish);
		stmt.bindLong(18, array[13]);
		stmt.bindLong(19, array[14]);
		stmt.bindDouble(20, rate);
		stmt.executeInsert();

		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	/**
	 * Method to insert a shimmer 3 medatata row (a session)
	 * @param array array with metadata values related to Sensors activated
	 * @param nameTable table name
	 * @param format format (calibrated or uncalibrated)
	 * @param start session starting time
	 * @param finish session ending time
	 * @param rate session rate
	 */
	public void insertShimmer3Metadata(int[] array, String nameTable, String typeDevice,
			String format, String start, String finish, double rate) {

		String sql = "INSERT INTO " + nameTable + " (" + LOW_NOISE_ACCELEROMETER 
				+ ", " + WIDE_RANGE_ACCELEROMETER + ", "+ MAGNOMETER + ", " + GYROSCOPE 
				+ ", " + GSR + ", " + BMP180 
				+ ", " + EXG1_24BIT + ", " + EXG2_24BIT
				+ ", " + EXG1_16BIT + ", " + EXG2_16BIT + ", " + V_SENSE_BATT
				+ ", " + EXTERNAL_ADC_A6 + ", " + EXTERNAL_ADC_A7 + ", " + EXTERNAL_ADC_A15
				+ ", " + INTERNAL_ADC_A1 + ", " + INTERNAL_ADC_A12
				+ ", " + INTERNAL_ADC_A13 + ", " + INTERNAL_ADC_A14
				+ ", " + ORIENTATION_3D + ", " + TYPE_DEVICE
				+ ", " + FORMAT + ", " + START + ", " + FINISH + ", " + FIRST_INDEX + ", "
				+ LAST_INDEX+ ", "+ RATE +") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		stmt.bindLong(1, array[0]);
		stmt.bindLong(2, array[1]);
		stmt.bindLong(3, array[2]);
		stmt.bindLong(4, array[3]);
		stmt.bindLong(5, array[4]);
		stmt.bindLong(6, array[5]);
		stmt.bindLong(7, array[6]);
		stmt.bindLong(8, array[7]);
		stmt.bindLong(9, array[8]);
		stmt.bindLong(10, array[9]);
		stmt.bindLong(11, array[10]);
		stmt.bindLong(12, array[11]);
		stmt.bindLong(13, array[12]);
		stmt.bindLong(14, array[13]);
		stmt.bindLong(15, array[14]);
		stmt.bindLong(16, array[15]);
		stmt.bindLong(17, array[16]);
		stmt.bindLong(18, array[17]);
		stmt.bindLong(19, array[18]);
		stmt.bindString(20, typeDevice);
		stmt.bindString(21, format);
		stmt.bindString(22, start);
		stmt.bindString(23, finish);
		stmt.bindLong(24, array[19]);
		stmt.bindLong(25, array[20]);
		stmt.bindDouble(26, rate);
		stmt.executeInsert();

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to insert mobile signal rows
	 * @param array array with the information to insert in the table
	 * @param label labeling column for the rows
	 * @param nameTable table name
	 */
	public void insertMobileSignal(double[][] array, String label, String nameTable) {

		String sql = "INSERT INTO " + nameTable + " (" + ACCELEROMETER_X + ", "	+ ACCELEROMETER_Y
				+ ", " + ACCELEROMETER_Z + ", " + MAGNOMETER_X + ", " + MAGNOMETER_Y + ", "
				+ MAGNOMETER_Z + ", " + GYROSCOPE_X	+ ", " + GYROSCOPE_Y + ", " + GYROSCOPE_Z + ", "
				+ LIGHT	+ ", " + PRESSURE + ", " + PROXIMITY + ", "	+ GRAVITY_X	+ ", " + GRAVITY_Y
				+ ", " + GRAVITY_Z	+ ", "	+ LINEAR_ACCELERATION_X	+ ", "	+ LINEAR_ACCELERATION_Y
				+ ", " + LINEAR_ACCELERATION_Z	+ ", "	+ ROTATION_VECTOR_X	+ ", " + ROTATION_VECTOR_Y
				+ ", " + ROTATION_VECTOR_Z + ", " + HUMIDITY + ", "	+ AMBIENT_TEMPERATURE + ", "
				+ TIME_STAMP + ", " + LABEL 
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (!Double.isNaN(array[i][j]))
					stmt.bindDouble(j + 1, array[i][j]);
				else
					stmt.bindNull(j + 1);
			}
			if(label!=null)
				stmt.bindString(25, label);
			else
				stmt.bindNull(25);
			
			stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to insert a mobile medatata row (a session)
	 * @param array array with metadata values related to Sensors activated
	 * @param nameTable table name
	 * @param format format (calibrated or uncalibrated)
	 * @param start session starting time
	 * @param finish session ending time
	 * @param rate session rate
	 */
	public void insertMobileMetadata(int[] array, String nameTable,	String format, String start, String finish, double rate) {

		String sql = "INSERT INTO " + nameTable + " (" + ACCELEROMETER + ", "
				+ MAGNOMETER + ", " + GYROSCOPE + ", " + AMBIENT_TEMPERATURE
				+ ", " + GRAVITY + ", " + LIGHT + ", " + LINEAR_ACCELERATION
				+ ", " + PRESSURE + ", " + PROXIMITY + ", " + HUMIDITY + ", "
				+ ROTATION_VECTOR + ", " + FORMAT + ", " + START + ", "
				+ FINISH + ", " + FIRST_INDEX + ", " + LAST_INDEX + ", " + RATE
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		stmt.bindLong(1, array[0]);
		stmt.bindLong(2, array[1]);
		stmt.bindLong(3, array[2]);
		stmt.bindLong(4, array[3]);
		stmt.bindLong(5, array[4]);
		stmt.bindLong(6, array[5]);
		stmt.bindLong(7, array[6]);
		stmt.bindLong(8, array[7]);
		stmt.bindLong(9, array[8]);
		stmt.bindLong(10, array[9]);
		stmt.bindLong(11, array[10]);
		stmt.bindString(12, format);
		stmt.bindString(13, start);
		stmt.bindString(14, finish);
		stmt.bindLong(15, array[11]);
		stmt.bindLong(16, array[12]);
		stmt.bindDouble(17, rate);
		stmt.executeInsert();

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to check metadata table consistency with signals table
	 * @param nameMetadataTable metadata table name
	 * @param nameTable signals table name
	 */
	public void consistencyTestMetadata(String nameMetadataTable, String nameTable) {

		// If the table of metadata is empty consistency test is not necessary
		String sql = "SELECT COUNT (ID) FROM " + nameMetadataTable + ";";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		int count = c.getInt(0);

		if (count != 0) {

			// Checking the table status
			int id = getMaxIndex(nameMetadataTable);
			String sql2 = "SELECT " + LAST_INDEX + " FROM " + nameMetadataTable	+ " " + "WHERE" + " ID=" + id + ";";
			Cursor c2 = db.rawQuery(sql2, null);
			c2.moveToFirst();
			int last = c2.getInt(0);

			// If consistency problem found, upgrade is necessary to fix it
			if (last == 0) {
				int lastIndex = getMaxIndex(nameTable);
				String sql3 = "UPDATE " + nameMetadataTable + " SET " + LAST_INDEX + "=" 
								+ lastIndex + " WHERE ID = " + id + ";";
				db.execSQL(sql3);
			}
		}
	}

	/**
	 * Method to fill a metadata row (when the session finishes)
	 * @param lastIndex last ID inserted in the signal table
	 * @param finishDate finishing date
	 * @param nameTable table name
	 */
	public void fillMetadataRow(int lastIndex, String finishDate, String nameTable) {

		int id = getMaxIndex(nameTable);
		String sql = "UPDATE " + nameTable + " SET " + LAST_INDEX + "="
				+ lastIndex + ", " + FINISH + "=" + "'" + finishDate + "'" + " WHERE ID = " + id + ";";

		db.execSQL(sql);
	}

	/**
	 * Method to get the max index of a table
	 * @param nameTable table name
	 * @return an integer with the max index
	 */
	public int getMaxIndex(String nameTable) {

		String sql = "select max(" + ID + ") as max_id from " + nameTable;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		return c.getInt(0);
	}

	/**
	 * Method to check if the shimmer units table is empty
	 * @return a boolean true if is empty, otherwise false
	 */
	public boolean isEmptyTableShimmerUnits() {

		String sql = "select * from " + TABLE_SHIMMER_UNITS;
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() == 0)
			return true;
		else
			return false;
	}

	/**
	 * Method to check if the mobile units table is empty
	 * @return a boolean true if is empty, otherwise false
	 */
	public boolean isEmptyTableMobileUnits() {

		String sql = "select * from " + TABLE_MOBILE_UNITS;
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() == 0)
			return true;
		else
			return false;
	}

	/**
	 * Method to fill a shimmer units table
	 */
	public void fillTableShimmerUnits() {

		String sql = "INSERT INTO " + TABLE_SHIMMER_UNITS + " (" + SENSORS
				+ ", " + CALIBRATED + ", " + UNCALIBRATED + ") VALUES (?, ?, ?)";

		String[] sensors = { "TIME", "ACC", "GYR", "MAG", "GSR", "ECG", "EMG", "STRAIN", "HEART", "EXP_A0", "EXP_A7", 
				"VSENSE_REG", "VSENSE_BATT", "3D_ORIENT", "LNACC", "WRACC", "EXT_AX", "INT_AX", "PRESS", "TEMP", "EXG"};
		String[] calibrated = { "ms", "m/(sec^2)", "deg/s", "local", "kOhms", "mV", "mV", "mV", "bmp", "mV", "mV", "mV", "mV",
				"local",  "m/(sec^2)",  "m/(sec^2)", "mV", "mV", "kPa", "C", "mV" };
		String[] uncalibrated = { "u12", "u12", "u12", "i16", "u16", "u12", "u12", "u12", "u12", "u12", "u12", "", "",
				"", "i16", "i16", "u12", "u12", "u24r", "u16r", ""};

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		for (int i = 0; i < sensors.length; i++) {
			stmt.bindString(1, sensors[i]);
			stmt.bindString(2, calibrated[i]);
			stmt.bindString(3, uncalibrated[i]);

			stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to fill a mobile units table
	 */
	public void fillTableMobileUnits() {

		String sql = "INSERT INTO " + TABLE_MOBILE_UNITS + "(" + SENSORS + ", "	+ CALIBRATED + ") VALUES (?, ?)";

		String[] sensors = { "Accelerometer", "Magnetometer", "Gyroscope", "Temperature", 
				"Gravity", "Light", "Linear Acceleration", "Pressure", "Proximity", "Humidity", "RotationVector" };
		String[] calibrated = { "m/(sec^2)", "tesla", "rad/s", "C", "m/(sec^2)", "lx", "m/(sec^2)", "hPa", " ", "%", " " };

		db.beginTransaction();
		SQLiteStatement stmt = db.compileStatement(sql);

		for (int i = 0; i < sensors.length; i++) {
			stmt.bindString(1, sensors[i]);
			stmt.bindString(2, calibrated[i]);

			stmt.executeInsert();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Method to retrieve all the signals rows between two indexes
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @param start start ID
	 * @param end end ID
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationByID(ArrayList<SensorType> sensors, 
			String nameTable, int start, int end) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();

		int numKeys = sensors.size();

		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);

		String sql = "select " + columns + " from " + nameTable + " where "	+ ID + " BETWEEN " + start + " AND " + end + ";";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c.isNull(i))
					arrays.get(i).add(Double.NaN);
				else
					arrays.get(i).add(c.getDouble(i));
			}

			c.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++)
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));

		return hash;
	}

	/**
	 * Method to get the columns names (in the DB) belonging to Sensor Types to use it as part of a sql query
	 * Example: ArrayList contains the Sensors Accelerometer X, Accelerometer Y and TimeStamp. The resulting 
	 * string is: "AccX, AccY, TimeStamp "
	 * @param sensors arraylist with the Sensors to be used
	 * @return an string ready to be part of a sql query
	 */
	public String getColumnsName(ArrayList<SensorType> sensors) {

		String aux = "";
		String columns = "";
		for (int i = 0; i < sensors.size(); i++) {
			aux = sensors.get(i).getAbbreviature();

			if (i != sensors.size() - 1)
				columns = columns + aux + ", ";

			else
				columns = columns + aux + " ";
		}

		return columns;
	}

	/**
	 * Method to get the starting and ending IDs of a session
	 * @param nameTable table name
	 * @param session integer with the number of the sessio
	 * @return a pair which has as first element the starting ID and as second element the finishing ID
	 */
	public Pair<Integer, Integer> getSessionsIds(String nameTable, int session) {

		String sql = "select " + FIRST_INDEX + ", " + LAST_INDEX + " from " + nameTable + " where " + ID + "=" + session + ";";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		int startID = c.getInt(0);
		int endID = c.getInt(1);
		Pair<Integer, Integer> pair = new Pair(startID, endID);

		return pair;
	}

	/**
	 * Method to get the starting and finishing IDs of a group of consecutive sessions
	 * @param nameTable table name
	 * @param sessionStart number of a starting session
	 * @param sessionEnd number of a finishing session
	 * @return a pair which has as first element the starting ID and as second element the finishing ID
	 */
	public Pair<Integer, Integer> getIntervalSessionsID(String nameTable,
			int sessionStart, int sessionEnd) {

		String sql1 = "select " + FIRST_INDEX + ", " + " from " + nameTable	+ " where " + ID + "=" + sessionStart + ";";

		Cursor c = db.rawQuery(sql1, null);
		c.moveToFirst();
		int startID = c.getInt(0);

		String sql2 = "select " + LAST_INDEX + ", " + " from " + nameTable + " where " + ID + "=" + sessionEnd + ";";

		c = db.rawQuery(sql2, null);
		c.moveToFirst();
		int endID = c.getInt(0);

		Pair<Integer, Integer> pair = new Pair(startID, endID);

		return pair;
	}

	/**
	 * Method to retrieve all the data existing in the database (all rows of a table)
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveAllInformation(ArrayList<SensorType> sensors, String nameTable) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();

		int numKeys = sensors.size();
		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);

		String sql = "select " + columns + " from " + nameTable + ";";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c.isNull(i))
					arrays.get(i).add(Double.NaN);
				else
					arrays.get(i).add(c.getDouble(i));
			}

			c.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++)
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));

		return hash;
	}

	/**
	 * Method to retrieve data belonging to the last X seconds stored
	 * @param seconds last seconds to retrieve 
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationLastSeconds(long seconds, String nameTable, ArrayList<SensorType> sensors) {

		int maxId = getMaxIndex(nameTable);
		String sql = "select " + TIME_STAMP + " from " + nameTable + " where " + ID + "=" + maxId + ";";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		long finishingInterval = c.getLong(0);

		// If the timestamp is given in ms...
		long secondsMs = seconds * 1000;
		long startingInterval = finishingInterval - secondsMs;
		if (startingInterval < 0) {
			startingInterval = 0;
		}

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();
		int numKeys = sensors.size();
		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);
		String sql2 = "select " + columns + " from " + nameTable + " where " + TIME_STAMP + " BETWEEN " 
				+ startingInterval + " AND " + finishingInterval + ";";

		Cursor c2 = db.rawQuery(sql2, null);
		c2.moveToFirst();

		while (!c2.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c2.isNull(i))
					arrays.get(i).add(Double.NaN);
				else
					arrays.get(i).add(c2.getDouble(i));
			}

			c2.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++)
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));

		return hash;
	}

	/**
	 * Method to retrieve all the data streamed between a starting and finishing Time
	 * @param start starting time
	 * @param end finishing time
	 * @param nameTable table name
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationByDates(
			Time start, Time end, String nameTable,
			ArrayList<SensorType> sensors) {

		// Given the timestamps in ms...
		long finishingInterval = end.toMillis(true);
		long startingInterval = start.toMillis(true);

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();
		int numKeys = sensors.size();
		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);
		String sql = "select " + columns + "from " + nameTable + " where "
				+ TIME_STAMP + " BETWEEN " + startingInterval + " AND "	+ finishingInterval + ";";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c.isNull(i))
					arrays.get(i).add(Double.NaN);
				else {
					arrays.get(i).add(c.getDouble(i));
				}
			}

			c.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++) {
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));
		}
		
		return hash;
	}

	/**
	 * Method to retrieve all the data belonging to a mobile unit table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Pair<String, String>> getMobileUnitsTable(String nameTable) {

		ArrayList<Pair<String, String>> array = new ArrayList<Pair<String, String>>();
		String sql = "SELECT " + SENSORS + ", " + CALIBRATED + " FROM "	+ nameTable + ";";
		Cursor c = db.rawQuery(sql, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {

			Pair<String, String> pair = new Pair<String, String>(c.getString(0), c.getString(1));
			array.add(pair);
			c.moveToNext();
		}

		return array;
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer unit table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<ArrayList<String>> getShimmerUnitsTable(String nameTable) {

		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		String sql = "SELECT " + SENSORS + ", " + CALIBRATED + ", "	+ UNCALIBRATED + " FROM " + nameTable + ";";
		Cursor c = db.rawQuery(sql, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {

			ArrayList<String> aux = new ArrayList<String>(Arrays.asList(c.getString(0), c.getString(1), c.getString(2)));
			array.add(aux);
			c.moveToNext();
		}

		return array;
	}

	/**
	 * Method to retrieve all the data belonging to a mobile signals table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> getMobileSignalsTable(String nameTable) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();

		// ArrayList with every signal in the mobile signals table
		ArrayList<SensorType> sensors = new ArrayList<SensorType>(
				Arrays.asList(SensorType.ACCELEROMETER_X,
						SensorType.ACCELEROMETER_Y, SensorType.ACCELEROMETER_Z,
						SensorType.MAGNETOMETER_X, SensorType.MAGNETOMETER_Y,
						SensorType.MAGNETOMETER_Z, SensorType.GYROSCOPE_X,
						SensorType.GYROSCOPE_Y, SensorType.GYROSCOPE_Z,
						SensorType.LIGHT, SensorType.PRESSURE,
						SensorType.PROXIMITY, SensorType.GRAVITY_X,
						SensorType.GRAVITY_Y, SensorType.GRAVITY_Z,
						SensorType.LINEAR_ACCELERATION_X,
						SensorType.LINEAR_ACCELERATION_Y,
						SensorType.LINEAR_ACCELERATION_Z,
						SensorType.ROTATION_VECTOR_X,
						SensorType.ROTATION_VECTOR_Y,
						SensorType.ROTATION_VECTOR_Z, SensorType.HUMIDITY,
						SensorType.AMBIENT_TEMPERATURE, SensorType.TIME_STAMP));

		int numKeys = sensors.size();

		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);

		String sql = "select " + columns + " from " + nameTable + ";";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c.isNull(i))
					arrays.get(i).add(Double.NaN);

				else
					arrays.get(i).add(c.getDouble(i));
			}

			c.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++)
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));

		return hash;
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer signal table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> getShimmerSignalsTable(String nameTable) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();

		// ArrayList with every signal in the shimmer signals table
		ArrayList<SensorType> sensors = new ArrayList<SensorType>(
				Arrays.asList(SensorType.ACCELEROMETER_X,
						SensorType.ACCELEROMETER_Y, SensorType.ACCELEROMETER_Z,
						SensorType.MAGNETOMETER_X, SensorType.MAGNETOMETER_Y,
						SensorType.MAGNETOMETER_Z, SensorType.GYROSCOPE_X,
						SensorType.GYROSCOPE_Y, SensorType.GYROSCOPE_Z,
						SensorType.GSR, SensorType.ECG_LALL,
						SensorType.ECG_RALL, SensorType.EMG,
						SensorType.STRAIN_GAUGE_HIGH,
						SensorType.STRAIN_GAUGE_LOW, SensorType.HEART_RATE,
						SensorType.EXP_BOARDA0, SensorType.EXP_BOARDA7,
						SensorType.TIME_STAMP));

		int numKeys = sensors.size();

		List<List<Double>> arrays = new ArrayList<List<Double>>();

		for (int i = 0; i < numKeys; i++) {
			ArrayList<Double> aux = new ArrayList<Double>();
			arrays.add(aux);
		}

		String columns = getColumnsName(sensors);

		String sql = "select " + columns + " from " + nameTable + ";";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {

			for (int i = 0; i < numKeys; i++) {

				if (c.isNull(i))
					arrays.get(i).add(Double.NaN);

				else
					arrays.get(i).add(c.getDouble(i));
			}

			c.moveToNext();
		}

		for (int i = 0; i < sensors.size(); i++)
			hash.put(sensors.get(i), (ArrayList<Double>) arrays.get(i));

		return hash;
	}

	/**
	 * Method to retrieve all the data belonging to a mobile metadata table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Hashtable<String, String>> getMobileMetadataTable(String nameTable) {

		ArrayList<Hashtable<String, String>> data = new ArrayList<Hashtable<String, String>>();

		String sql = "SELECT " + ID + ", " + ACCELEROMETER + ", " + MAGNOMETER
				+ ", " + GYROSCOPE + ", " + AMBIENT_TEMPERATURE + ", "
				+ GRAVITY + ", " + LIGHT + ", " + LINEAR_ACCELERATION + ", "
				+ PRESSURE + ", " + PROXIMITY + ", " + HUMIDITY + ", "
				+ ROTATION_VECTOR + ", " + FORMAT + ", " + START + ", "
				+ FINISH + ", " + FIRST_INDEX + ", " + LAST_INDEX  + ", " + RATE + " FROM "
				+ nameTable + ";";

		Cursor c = db.rawQuery(sql, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {

			Hashtable<String, String> hashAux = new Hashtable<String, String>();

			ArrayList<String> aux = new ArrayList<String>();
			for (int i = 0; i < c.getColumnCount(); i++) {
				hashAux.put(c.getColumnName(i), c.getString(i));
				aux.add(c.getString(i));
			}

			data.add(hashAux);
			c.moveToNext();
		}

		return data;
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer metadata table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Hashtable<String, String>> getShimmerMetadataTable(String nameTable) {

		ArrayList<Hashtable<String, String>> data = new ArrayList<Hashtable<String, String>>();

		String sql = "SELECT " + ID + ", " + ACCELEROMETER + ", " + MAGNOMETER
				+ ", " + GYROSCOPE + ", " + GSR + ", " + ECG + ", " + EMG
				+ ", " + STRAIN_GAUGE + ", " + HEART_RATE + ", " + EXP_BOARDA0
				+ ", " + EXP_BOARDA7 + ", " + FORMAT + ", " + START + ", "
				+ FINISH + ", " + FIRST_INDEX + ", " + LAST_INDEX  + ", " + RATE + " FROM "
						+ nameTable + ";";

		Cursor c = db.rawQuery(sql, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {

			Hashtable<String, String> hashAux = new Hashtable<String, String>();

			ArrayList<String> aux = new ArrayList<String>();
			for (int i = 0; i < c.getColumnCount(); i++) {
				hashAux.put(c.getColumnName(i), c.getString(i));
				aux.add(c.getString(i));
			}

			data.add(hashAux);
			c.moveToNext();
		}

		return data;
	}
	
	/**
	 * Method to check if a user exists
	 * @param login user login
	 * @return true if the user exists, otherwise false
	 */
	public boolean existsLogin(String login){
		
		boolean exists = false;
		
		String sql = "SELECT * FROM " + TABLE_USER_PROFILE + " WHERE " +
					LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		if(c.getCount()!=0)
			exists = true;
		
		return exists;
	}
	
	/**
	 * Method to check if a password is truly the user password
	 * @param login user login
	 * @param password user password to be checked
	 * @return true if the password introduced is correct, otherwise false
	 */
	public boolean checkPassword(String login, String password){
		
		boolean match = true;
		
		String sql = "SELECT " + PASSWORD + " FROM " + TABLE_USER_PROFILE + " WHERE " 
						+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		if(!password.equals(c.getString(0)))
			match = false;
		
		return match;
	}
	
	/**
	 * Method to get the user password
	 * @param login user login
	 * @return a string with the password
	 */
	public String getPassword(String login){
		
		String sql = "SELECT " + PASSWORD + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getString(0);
	}
	
	/**
	 * Method to get the user sex
	 * @param login user login
	 * @return a string with the sex
	 */
	public String getSex(String login){
		
		String sql = "SELECT " + SEX + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getString(0);
	}
	
	/**
	 * Method to get the user age
	 * @param login user login
	 * @return a string with the age
	 */
	public int getAge(String login){
		
		String sql = "SELECT " + AGE + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return (int) c.getLong(0);
	}
	
	/**
	 * Method to get the user height
	 * @param login user login
	 * @return a string with the height
	 */
	public float getHeight(String login){
		
		String sql = "SELECT " + HEIGHT + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getFloat(0);
	}
	
	/**
	 * Method to get the user weight
	 * @param login user login
	 * @return a string with the weight
	 */
	public float getWeight(String login){
		
		String sql = "SELECT " + WEIGHT + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getFloat(0);
	}
	
	/**
	 * Method to get the user email
	 * @param login user login
	 * @return a string with the email
	 */
	public String getEmail(String login){
		
		String sql = "SELECT " + EMAIL + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ LOGIN + " = '" + login + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getString(0);
	}
	
	/**
	 * Method to get the user login using its email
	 * @param login user login
	 * @return a string with the login
	 */
	public String getLoginByEmail(String email){
		
		String sql = "SELECT " + LOGIN + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ EMAIL + " = '" + email + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getString(0);
	}
	
	/**
	 * Method to get the user password using its email
	 * @param login user login
	 * @return a string with the password
	 */
	public String getPasswordByEmail(String email){
		
		String sql = "SELECT " + PASSWORD + " FROM " + TABLE_USER_PROFILE + " WHERE " 
				+ EMAIL + " = '" + email + "';";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		
		return c.getString(0);
	}
	
	/**
	 * Method to set the user password
	 * @param login user login
	 * @param newPassword new user password
	 */
	public void setPassword(String login, String newPassword){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + PASSWORD + "= '" 
				+ newPassword + "' WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
	
	/**
	 * Method to set the user age
	 * @param login user login
	 * @param newAge new user age
	 */
	public void setAge(String login, int newAge){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + AGE + "=" 
				+ newAge + " WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
	
	/**
	 * Method to set the user sex
	 * @param login user login
	 * @param newSex new user sex
	 */
	public void setSex(String login, String newSex){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + SEX + "= '" 
				+ newSex + "' WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
	
	/**
	 * Method to set the user height
	 * @param login user login
	 * @param newHeight new user height
	 */
	public void setHeight(String login, float newHeight){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + HEIGHT + "=" 
				+ newHeight + " WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
	
	/**
	 * Method to set the user weight
	 * @param login user login
	 * @param newWeight new user weight
	 */
	public void setWeight(String login, float newWeight){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + WEIGHT + "=" 
				+ newWeight + " WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
	
	/**
	 * Method to set the user email
	 * @param login user login
	 * @param newEmail new user email
	 */
	public void setEmail(String login, String newEmail){
		
		String sql3 = "UPDATE " + TABLE_USER_PROFILE + " SET " + EMAIL + "= '" 
				+ newEmail + "' WHERE " + LOGIN + " = '" + login + "';";
		
		db.execSQL(sql3);
	}
}
