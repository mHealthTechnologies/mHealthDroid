package communicationManager.storage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.dataStructure.ObjectMetadata;
import communicationManager.dataStructure.ObjectMetadata.FormatType;
import android.content.Context;
import android.text.format.Time;
import android.util.Pair;


public class Storage {

	private static DBAdapter dbadapter;

	/**
	 * Constructor. Creates a new DBAdapter object
	 * @param context IU current activity context
	 */
	public Storage(Context context) {

		dbadapter = new DBAdapter(context);
	}

	/**
	 * Method to open the DB
	 * @return a write/read valid DB
	 */
	public DBAdapter open() {

		dbadapter.open();
		return dbadapter;
	}

	/**
	 * Method to close the DB
	 */
	public void close() {

		dbadapter.close();
	}

	

	/**
	 * Method to insert signal rows from the device shimmer 2
	 * @param list objectData list with all the rows to insert in the table
	 * @param label labeling for the rows
	 * @param nameTable table name
	 * @param index buffer index
	 * @param complete boolean to know if the buffer sample is complete (check communication manager buffer)
	 * @param numSamples is the number of samples that store the buffer
	 */
	public void insertShimmer2(ArrayList<ObjectData> list, String label, String nameTable, int index, boolean complete, int numSamples) {


		double[][] matrix;
		int j = 0;
		int init = 0;

		if (complete == true) {
			init = index - numSamples;
			matrix = new double[numSamples][29];
		} else {
			init = index - (index % numSamples);
			matrix = new double[index % numSamples][29];
		}

		for (int i = 0; i < matrix.length; i++)
			for (int k = 0; k < 29; k++)
				matrix[i][k] = Double.NaN;
		
		for (int i = init; i < index; i++) {
			Set<SensorType> sensors = list.get(i).hashData.keySet();
			for (SensorType s : sensors) {
				switch (s) {
				case ACCELEROMETER_X:
					matrix[j][0] = list.get(i).hashData.get(s).data;
					break;
				case ACCELEROMETER_Y:
					matrix[j][1] = list.get(i).hashData.get(s).data;
					break;
				case ACCELEROMETER_Z:
					matrix[j][2] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_X:
					matrix[j][3] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Y:
					matrix[j][4] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Z:
					matrix[j][5] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_X:
					matrix[j][6] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Y:
					matrix[j][7] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Z:
					matrix[j][8] = list.get(i).hashData.get(s).data;
					break;
				case GSR:
					matrix[j][9] = list.get(i).hashData.get(s).data;
					break;
				case ECG_RALL:
					matrix[j][10] = list.get(i).hashData.get(s).data;
					break;
				case ECG_LALL:
					matrix[j][11] = list.get(i).hashData.get(s).data;
					break;
				case EMG:
					matrix[j][12] = list.get(i).hashData.get(s).data;
					break;
				case STRAIN_GAUGE_HIGH:
					matrix[j][13] = list.get(i).hashData.get(s).data;
					break;
				case STRAIN_GAUGE_LOW:
					matrix[j][14] = list.get(i).hashData.get(s).data;
					break;
				case HEART_RATE:
					matrix[j][15] = list.get(i).hashData.get(s).data;
					break;
				case EXP_BOARDA0:
					matrix[j][16] = list.get(i).hashData.get(s).data;
					break;
				case EXP_BOARDA7:
					matrix[j][17] = list.get(i).hashData.get(s).data;
					break;
				case V_SENSE_REG:
					matrix[j][18] = list.get(i).hashData.get(s).data;
					break;
				case V_SENSE_BATT:
					matrix[j][19] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_A:
					matrix[j][20] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_X:
					matrix[j][21] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_Y:
					matrix[j][22] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_Z:
					matrix[j][23] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION0:
					matrix[j][24] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION1:
					matrix[j][25] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION2:
					matrix[j][26] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION3:
					matrix[j][27] = list.get(i).hashData.get(s).data;
					break;
				case TIME_STAMP:
					matrix[j][28] = list.get(i).hashData.get(s).data;
					break;

				}

			}
			j++;
		}

		dbadapter.insertShimmer2Signal(matrix,label, nameTable);
	}
	
	/**
	 * Method to insert signal rows from the device shimmer 3
	 * @param list objectData list with all the rows to insert in the table
	 * @param label labeling for the rows
	 * @param nameTable table name
	 * @param index buffer index
	 * @param complete boolean to know if the buffer sample is complete (check communication manager buffer)
	 * @param numSamples is the number of samples that store the buffer
	 */
	public void insertShimmer3(ArrayList<ObjectData> list, String label, String nameTable, int index, boolean complete, int numSamples) {

		
		double[][] matrix;
		int j = 0;
		int init = 0;

		if (complete == true) {
			init = index - numSamples;
			matrix = new double[numSamples][47];
		} else {
			init = index - (index % numSamples);
			matrix = new double[index % numSamples][47];
		}

		for (int i = 0; i < matrix.length; i++)
			for (int k = 0; k < 47; k++)
				matrix[i][k] = Double.NaN;

		for (int i = init; i < index; i++) {
			Set<SensorType> sensors = list.get(i).hashData.keySet();
			for (SensorType s : sensors) {
				switch (s) {
				case LOW_NOISE_ACCELEROMETER_X:
					matrix[j][0] = list.get(i).hashData.get(s).data;
					break;
				case LOW_NOISE_ACCELEROMETER_Y:
					matrix[j][1] = list.get(i).hashData.get(s).data;
					break;
				case LOW_NOISE_ACCELEROMETER_Z:
					matrix[j][2] = list.get(i).hashData.get(s).data;
					break;
				case WIDE_RANGE_ACCELEROMETER_X:
					matrix[j][3] = list.get(i).hashData.get(s).data;
					break;
				case WIDE_RANGE_ACCELEROMETER_Y:
					matrix[j][4] = list.get(i).hashData.get(s).data;
					break;
				case WIDE_RANGE_ACCELEROMETER_Z:
					matrix[j][5] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_X:
					matrix[j][6] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Y:
					matrix[j][7] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Z:
					matrix[j][8] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_X:
					matrix[j][9] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Y:
					matrix[j][10] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Z:
					matrix[j][11] = list.get(i).hashData.get(s).data;
					break;
				case GSR:
					matrix[j][12] = list.get(i).hashData.get(s).data;
					break;
				case V_SENSE_BATT:
					matrix[j][13] = list.get(i).hashData.get(s).data;
					break;
				case EXTERNAL_ADC_A6:
					matrix[j][14] = list.get(i).hashData.get(s).data;
					break;
				case EXTERNAL_ADC_A7:
					matrix[j][15] = list.get(i).hashData.get(s).data;
					break;
				case EXTERNAL_ADC_A15:
					matrix[j][16] = list.get(i).hashData.get(s).data;
					break;
				case INTERNAL_ADC_A1:
					matrix[j][17] = list.get(i).hashData.get(s).data;
					break;
				case INTERNAL_ADC_A12:
					matrix[j][18] = list.get(i).hashData.get(s).data;
					break;
				case INTERNAL_ADC_A13:
					matrix[j][19] = list.get(i).hashData.get(s).data;
					break;
				case INTERNAL_ADC_A14:
					matrix[j][20] = list.get(i).hashData.get(s).data;
					break;
				case PRESSURE:
					matrix[j][21] = list.get(i).hashData.get(s).data;
					break;
				case TEMPERATURE:
					matrix[j][22] = list.get(i).hashData.get(s).data;
					break;
				case EXG1_STATUS:
					matrix[j][23] = list.get(i).hashData.get(s).data;
					break;
				case EXG2_STATUS:
					matrix[j][24] = list.get(i).hashData.get(s).data;
					break;
				case ECG_LLRA:
					matrix[j][25] = list.get(i).hashData.get(s).data;
					break;
				case ECG_LARA:
					matrix[j][26] = list.get(i).hashData.get(s).data;
					break;
				case EMG_CH1:
					matrix[j][27] = list.get(i).hashData.get(s).data;
					break;
				case EMG_CH2:
					matrix[j][28] = list.get(i).hashData.get(s).data;
					break;
				case EXG1_CH1:
					matrix[j][29] = list.get(i).hashData.get(s).data;
					break;
				case EXG1_CH2:
					matrix[j][30] = list.get(i).hashData.get(s).data;
					break;
				case EXG2_CH1:
					matrix[j][31] = list.get(i).hashData.get(s).data;
					break;
				case ECG_VXRL:
					matrix[j][32] = list.get(i).hashData.get(s).data;
					break;
				case EXG2_CH2:
					matrix[j][33] = list.get(i).hashData.get(s).data;
					break;
				case EXG1_CH1_16B:
					matrix[j][34] = list.get(i).hashData.get(s).data;
					break;
				case EXG1_CH2_16B:
					matrix[j][35] = list.get(i).hashData.get(s).data;
					break;
				case EXG2_CH1_16B:
					matrix[j][36] = list.get(i).hashData.get(s).data;
					break;
				case EXG2_16B:
					matrix[j][37] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_A:
					matrix[j][38] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_X:
					matrix[j][39] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_Y:
					matrix[j][40] = list.get(i).hashData.get(s).data;
					break;
				case ANGLE_AXIS_Z:
					matrix[j][41] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION0:
					matrix[j][42] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION1:
					matrix[j][43] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION2:
					matrix[j][44] = list.get(i).hashData.get(s).data;
					break;
				case QUARTENION3:
					matrix[j][45] = list.get(i).hashData.get(s).data;
					break;
				case TIME_STAMP:
					matrix[j][46] = list.get(i).hashData.get(s).data;
					break;

				}

			}
			j++;
		}

		dbadapter.insertShimmer3Signal(matrix,label, nameTable);
	}
	

	/**
	 * Method to insert a shimmer 2 metadata row (a session)
	 * @param mt ObjectData to insert in the table
	 * @param nameTable table name
	 */
	public void insertShimmer2Metadata(ObjectMetadata mt, String nameTable) {

		String format;
		int[] array = new int[15];

		if (mt.format == FormatType.CALIBRATED)
			format = "Calibrated";
		else
			format = "Uncalibrated";

		Set<SensorType> set = mt.hashMetadata.keySet();
		for (SensorType s : set) {
			switch (s) {
				case ACCELEROMETER:
					array[0] = 1;
				break;
				case MAGNETOMETER:
					array[1] = 1;
				break;
				case GYROSCOPE:
					array[2] = 1;
				break;
				case GSR:
					array[3] = 1;
				break;
				case ECG:
					array[4] = 1;
				break;
				case EMG:
					array[5] = 1;
				break;
				case STRAIN:
					array[6] = 1;
				break;
				case HEART_RATE:
					array[7] = 1;
				break;
				case EXP_BOARDA0:
					array[8] = 1;
				break;
				case EXP_BOARDA7:
					array[9] = 1;
				break;
				case V_SENSE_BATT:
					array[10] = 1;
				break;
				case V_SENSE_REG:
					array[11] = 1;
				break;
				case ORIENTATION:
					array[12]=1;
				break;
			}
		}

		array[13] = mt.firstIndex;
		array[14] = mt.lastIndex;

		dbadapter.insertShimmer2Metadata(array, nameTable, "Shimmer 2",  format, mt.startToString(), mt.finishToString(), mt.rate);
	}

	/**
	 * Method to insert a shimmer 3 metadata row (a session)
	 * @param mt ObjectData to insert in the table
	 * @param nameTable table name
	 */
	public void insertShimmer3Metadata(ObjectMetadata mt, String nameTable) {

		String format;
		int[] array = new int[21];

		if (mt.format == FormatType.CALIBRATED)
			format = "Calibrated";
		else
			format = "Uncalibrated";

		Set<SensorType> set = mt.hashMetadata.keySet();
		for (SensorType s : set) {
			switch (s) {
				case LOW_NOISE_ACCELEROMETER:
					array[0] = 1;
				break;
				case WIDE_RANGE_ACCELEROMETER:
					array[1] = 1;
				break;
				case MAGNETOMETER:
					array[2] = 1;
				break;
				case GYROSCOPE:
					array[3] = 1;
				break;
				case GSR:
					array[4] = 1;
				break;
				case BMP180:
					array[5] = 1;
				break;
				case EXG1_24B:
					array[6] = 1;
				break;
				case EXG2_24B:
					array[7] = 1;
				break;
				case EXG1_16B:
					array[8] = 1;
				break;
				case EXG2_16B:
					array[9] = 1;
				break;
				case V_SENSE_BATT:
					array[10] = 1;
				break;
				case EXTERNAL_ADC_A6:
					array[11] = 1;
				break;
				case EXTERNAL_ADC_A7:
					array[12] = 1;
				break;
				case EXTERNAL_ADC_A15:
					array[13] = 1;
				break;
				case INTERNAL_ADC_A1:
					array[14] = 1;
				break;
				case INTERNAL_ADC_A12:
					array[15] = 1;
				break;
				case INTERNAL_ADC_A13:
					array[16] = 1;
				break;
				case INTERNAL_ADC_A14:
					array[17] = 1;
				break;
				case ORIENTATION:
					array[18]=1;
				break;
			}
		}

		array[19] = mt.firstIndex;
		array[20] = mt.lastIndex;

		dbadapter.insertShimmer3Metadata(array, nameTable, "Shimmer 3",  format, mt.startToString(), mt.finishToString(), mt.rate);
	}
	
	/**
	 * Method to insert a mobile metadata row (a session)
	 * @param mt ObjectData to insert in the table
	 * @param nameTable table name
	 */
	public void insertMobileMetadata(ObjectMetadata metadata, String nameTable) {

		String format = "Unknown";
		int array[] = new int[13];

		if (metadata.format == FormatType.CALIBRATED)
			format = "Calibrated";
		else if (metadata.format == FormatType.UNCALIBRATED)
			format = "Uncalibrated";

		Set<SensorType> sensor = metadata.hashMetadata.keySet();
		for (SensorType s : sensor) {

			switch (s) {

			case ACCELEROMETER:
				array[0] = 1;
				break;
			case MAGNETOMETER:
				array[1] = 1;
				break;
			case GYROSCOPE:
				array[2] = 1;
				break;
			case AMBIENT_TEMPERATURE:
				array[3] = 1;
				break;
			case GRAVITY:
				array[4] = 1;
				break;
			case LIGHT:
				array[5] = 1;
				break;
			case LINEAR_ACCELERATION:
				array[6] = 1;
				break;
			case PRESSURE:
				array[7] = 1;
				break;
			case PROXIMITY:
				array[8] = 1;
				break;
			case HUMIDITY:
				array[9] = 1;
				break;
			case ROTATION_VECTOR:
				array[10] = 1;
				break;

			}
		}

		array[11] = metadata.firstIndex;
		array[12] = metadata.lastIndex;

		dbadapter.insertMobileMetadata(array, nameTable, format, metadata.startToString(), metadata.finishToString(), metadata.rate);
	}

	/**
	 * Method to check metadata table consistency with signals table
	 * @param nameMetadataTable metadata table name
	 * @param nameTable signals table name
	 */
	public void consistencyTestMetadata(String nameMetadataTable, String nameTable) {

		dbadapter.consistencyTestMetadata(nameMetadataTable, nameTable);
	}

	/**
	 * Method to fill a metadata row (when the session finishes)
	 * @param mt objectMetadata with the information to insert (finish date and last index)
	 * @param nameTable table name
	 */
	public void fillMetadataRow(ObjectMetadata mt, String nameTable) {

		int lastIndex = mt.lastIndex;
		String finishDate = mt.finishToString();
		dbadapter.fillMetadataRow(lastIndex, finishDate, nameTable);
	}


	/**
	 * Method to insert mobile signal rows
	 * @param list objectData list with all the rows to insert in the table
	 * @param label labeling for the rows
	 * @param nameTable table name
	 * @param index buffer index
	 * @param complete boolean to know if the buffer sample is complete (check communication manager buffer)
	 * @param numSamples is the number of samples that store the buffer
	 */
	public void insertMobile(ArrayList<ObjectData> list, String label, String nameTable, int index, boolean complete, int numSamples) {


		double[][] matrix;
		int j = 0;
		int init = 0;

		if (complete == true) {
			init = index - numSamples;
			matrix = new double[numSamples][24];
		} else {
			init = index - (index % numSamples);
			matrix = new double[index % numSamples][24];
		}

		for (int i = 0; i < matrix.length; i++)
			for (int k = 0; k < 24; k++)
				matrix[i][k] = Double.NaN;

		for (int i = init; i < index; i++) {
			Set<SensorType> sensors = list.get(i).hashData.keySet();
			for (SensorType s : sensors) {
				switch (s) {
				case ACCELEROMETER_X:
					matrix[j][0] = list.get(i).hashData.get(s).data;
					break;
				case ACCELEROMETER_Y:
					matrix[j][1] = list.get(i).hashData.get(s).data;
					break;
				case ACCELEROMETER_Z:
					matrix[j][2] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_X:
					matrix[j][3] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Y:
					matrix[j][4] = list.get(i).hashData.get(s).data;
					break;
				case MAGNETOMETER_Z:
					matrix[j][5] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_X:
					matrix[j][6] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Y:
					matrix[j][7] = list.get(i).hashData.get(s).data;
					break;
				case GYROSCOPE_Z:
					matrix[j][8] = list.get(i).hashData.get(s).data;
					break;
				case LIGHT:
					matrix[j][9] = list.get(i).hashData.get(s).data;
					break;
				case PRESSURE:
					matrix[j][10] = list.get(i).hashData.get(s).data;
					break;
				case PROXIMITY:
					matrix[j][11] = list.get(i).hashData.get(s).data;
					break;
				case GRAVITY_X:
					matrix[j][12] = list.get(i).hashData.get(s).data;
					break;
				case GRAVITY_Y:
					matrix[j][13] = list.get(i).hashData.get(s).data;
					break;
				case GRAVITY_Z:
					matrix[j][14] = list.get(i).hashData.get(s).data;
					break;
				case LINEAR_ACCELERATION_X:
					matrix[j][15] = list.get(i).hashData.get(s).data;
					break;
				case LINEAR_ACCELERATION_Y:
					matrix[j][16] = list.get(i).hashData.get(s).data;
					break;
				case LINEAR_ACCELERATION_Z:
					matrix[j][17] = list.get(i).hashData.get(s).data;
					break;
				case ROTATION_VECTOR_X:
					matrix[j][18] = list.get(i).hashData.get(s).data;
					break;
				case ROTATION_VECTOR_Y:
					matrix[j][19] = list.get(i).hashData.get(s).data;
					break;
				case ROTATION_VECTOR_Z:
					matrix[j][20] = list.get(i).hashData.get(s).data;
					break;
				case HUMIDITY:
					matrix[j][21] = list.get(i).hashData.get(s).data;
					break;
				case AMBIENT_TEMPERATURE:
					matrix[j][22] = list.get(i).hashData.get(s).data;
					break;
				case TIME_STAMP:
					matrix[j][23] = list.get(i).hashData.get(s).data;
					break;

				}
			}
			j++;
		}

		dbadapter.insertMobileSignal(matrix, label, nameTable);
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
	public void insertUserProfile(String login, String password, String sex, int age, float weight, float height, String email){
		
		dbadapter.insertUserProfile(login, password, sex, age, height, weight, email);
	}

	/**
	 * Method to create a shimmer 2 signal table
	 * @param name table name
	 */
	public void createShimmer2Table(String name) {

		dbadapter.createShimmer2Table(name);
	}
	
	/**
	 * Method to create a shimmer 3 signal table
	 * @param name table name
	 */
	public void createShimmer3Table(String name) {

		dbadapter.createShimmer3Table(name);
	}

	/**
	 * Method to create a shimmer 2 signal table
	 * @param name table name
	 */
	public void createShimmer2TableMetadata(String name) {

		dbadapter.createShimmer2TableMetadata(name);
	}
	
	/**
	 * Method to create a shimmer 3 signal table
	 * @param name table name
	 */
	public void createShimmer3TableMetadata(String name) {

		dbadapter.createShimmer3TableMetadata(name);
	}
	
	/**
	 * Method to create a shimmer units table
	 */
	public void createShimmerTableUnits() {

		dbadapter.createShimmerTableUnits();
	}

	/**
	 * Method to create a mobile signals table
	 * @param name table name
	 */
	public void createMobileTable(String name) {

		dbadapter.createMobileTable(name);
	}

	/**
	 * Method to create a mobile metadata table
	 * @param name table name
	 */
	public void createMobileTableMetadata(String name) {

		dbadapter.createMobileTableMetadata(name);
	}

	/**
	 * Method to get the max index of a table
	 * @param nameTable table name
	 * @return an integer with the max index
	 */
	public int getMaxIndex(String nameTable) {

		return dbadapter.getMaxIndex(nameTable);
	}


	public void createMobileTableUnits() {

		dbadapter.createMobileTableUnits();
	}
	
	/**
	 * Method to create an user profile table
	 */
	public void createUserProfileTable(){
		
		dbadapter.createUserProfileTable();
	}

	/**
	 * Method to check if the shimmer units table is empty
	 * @return a boolean true if is empty, otherwise false
	 */
	public boolean isEmptyTableShimmerUnits() {

		return dbadapter.isEmptyTableShimmerUnits();
	}

	/**
	 * Method to check if the mobile units table is empty
	 * @return a boolean true if is empty, otherwise false
	 */
	public boolean isEmptyTableMobileUnits() {

		return dbadapter.isEmptyTableMobileUnits();
	}

	/**
	 * Method to fill a shimmer units table
	 */
	public void fillShimmerTableUnits() {

		dbadapter.fillTableShimmerUnits();
	}

	/**
	 * Method to fill a mobile units table
	 */
	public void fillMobileTableUnits() {

		dbadapter.fillTableMobileUnits();
	}

	/**
	 * Method to retrieve all the signals rows between two indexes
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @param start start ID
	 * @param end end ID
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationByID(ArrayList<SensorType> sensors, String nameTable, int start, int end) {

		return dbadapter.retrieveInformationByID(sensors, nameTable, start, end);
	}

	/**
	 * Method to get the starting and ending ID of a session
	 * @param nameTable table name
	 * @param session integer with the number of the sessio
	 * @return a pair, which has as first element the starting ID and as second element the finishing ID
	 */
	public Pair<Integer, Integer> getSessionIDs(String nameTable, int session) {

		return dbadapter.getSessionsIds(nameTable, session);
	}

	/**
	 * Method to get the starting and finishing IDs of a group of consecutive sessions
	 * @param nameTable table name
	 * @param sessionStart number of a starting session
	 * @param sessionEnd number of a finishing session
	 * @return a pair which has as first element the starting ID and as second element the finishing ID
	 */
	public Pair<Integer, Integer> getIntervalSessionsID(String nameTable, int sessionStart, int sessionEnd) {

		return dbadapter.getIntervalSessionsID(nameTable, sessionStart,	sessionEnd);
	}

	/**
	 * Method to retrieve all the data existing in the database (all rows of a table)
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveAllInformation(ArrayList<SensorType> sensors, String nameTable) {

		return dbadapter.retrieveAllInformation(sensors, nameTable);
	}

	/**
	 * Method to retrieve data belonging to the last X seconds stored
	 * @param seconds last seconds to retrieve 
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @param nameTable table name
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationLastSeconds(long seconds, String nameTable, ArrayList<SensorType> sensors) {

		return dbadapter.retrieveInformationLastSeconds(seconds, nameTable, sensors);
	}

	/**
	 * Method to retrieve all the data streamed between a starting and finishing Time
	 * @param start starting time
	 * @param end finishing time
	 * @param nameTable table name
	 * @param sensors sensors (columns/attributes) to be retrieved
	 * @return a hashtable with the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> retrieveInformationByDates(Time start, Time end, String nameTable, 
																		ArrayList<SensorType> sensors) {

		return dbadapter.retrieveInformationByDates(start, end, nameTable, sensors);
	}

	/**
	 * Method to retrieve all the data belonging to a mobile unit table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Pair<String, String>> getMobileUnitsTable(String nameTable) {

		return dbadapter.getMobileUnitsTable(nameTable);
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer unit table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<ArrayList<String>> getShimmerUnitsTable(String nameTable) {

		return dbadapter.getShimmerUnitsTable(nameTable);
	}

	/**
	 * Method to retrieve all the data belonging to a mobile signal table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> getMobileSignalsTable(String nameTable) {

		return dbadapter.getMobileSignalsTable(nameTable);
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer signal table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public Hashtable<SensorType, ArrayList<Double>> getShimmerSignalsTable(String nameTable) {

		return dbadapter.getShimmerSignalsTable(nameTable);
	}

	/**
	 * Method to retrieve all the data belonging to a mobile metadata table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Hashtable<String, String>> getMobileMetadataTable(String nameTable) {

		return dbadapter.getMobileMetadataTable(nameTable);
	}

	/**
	 * Method to retrieve all the data belonging to a shimmer metadata table
	 * @param nameTable table name
	 * @return an arraylist with all the data
	 */
	public ArrayList<Hashtable<String, String>> getShimmerMetadataTable(String nameTable) {

		return dbadapter.getShimmerMetadataTable(nameTable);
	}

	/**
	 * Method to check if a user exists
	 * @param login user login
	 * @return true if the user exists, otherwise false
	 */
	public boolean existsLogin(String login){
		
		return dbadapter.existsLogin(login);
	}
	
	/**
	 * Method to check if a password is truly the user password
	 * @param login user login
	 * @param password user password to be checked
	 * @return true if the password introduced is correct, otherwise false
	 */
	public boolean checkPassword(String login, String password){
		
		return dbadapter.checkPassword(login, password);
	}
	
	/**
	 * Method to get the user password
	 * @param login user login
	 * @return a string with the password
	 */
	public String getPassword(String login){
		
		return dbadapter.getPassword(login);
	}

	/**
	 * Method to get the user sex
	 * @param login user login
	 * @return a string with the sex
	 */
	public String getSex(String login){
		
		return dbadapter.getSex(login);
	}
	
	/**
	 * Method to get the user email
	 * @param login user login
	 * @return a string with the email
	 */
	public String getEmail(String login){
		
		return dbadapter.getEmail(login);
	}
	
	/**
	 * Method to get the user age
	 * @param login user login
	 * @return a string with the age
	 */
	public int getAge(String login){
		
		return dbadapter.getAge(login);
	}
	
	/**
	 * Method to get the user login
	 * @param login user login
	 * @return a string with the login
	 */
	public float getHeight(String login){
		
		return dbadapter.getHeight(login);
	}
	
	/**
	 * Method to get the user weight
	 * @param login user login
	 * @return a string with the weight
	 */
	public float getWeight(String login){
		
		return dbadapter.getWeight(login);
	}
		
	/**
	 * Method to get the user login using its email
	 * @param login user login
	 * @return a string with the login
	 */
	public String getLoginByEmail(String email){
		
		return dbadapter.getLoginByEmail(email);
	}
	
	/**
	 * Method to get the user password using its email
	 * @param login user login
	 * @return a string with the password
	 */
	public String getPasswordByEmail(String email){
		
		return dbadapter.getPasswordByEmail(email);
	}
	
	/**
	 * Method to set the user password
	 * @param login user login
	 * @return a string with the password
	 */
	public void setPassword(String login, String newPassword){
		
		dbadapter.setPassword(login, newPassword);
	}
	
	/**
	 * Method to set the user sex
	 * @param login user login
	 * @return a string with the sex
	 */
	public void setSex(String login, String newSex){
		
		dbadapter.setSex(login, newSex);
	}
	
	/**
	 * Method to set the user age
	 * @param login user login
	 * @return a string with the age
	 */
	public void setAge(String login, int newAge){
		
		dbadapter.setAge(login, newAge);
	}
	
	/**
	 * Method to set the user height
	 * @param login user login
	 * @return a string with the height
	 */
	public void setHeight(String login, float newHeight){
		
		dbadapter.setHeight(login, newHeight);
	}
	
	/**
	 * Method to set the user weight
	 * @param login user login
	 * @return a string with the weight
	 */
	public void setWeight(String login, float newWeight){
		
		dbadapter.setWeight(login, newWeight);
	}
	
	/**
	 * Method to set the user email
	 * @param login user login
	 * @return a string with the password
	 */
	public void setEmail(String login, String newEmail){
		
		dbadapter.setEmail(login, newEmail);
	}
}
