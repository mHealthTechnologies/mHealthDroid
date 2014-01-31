package dataprocessingManager.acquisition;

import java.util.ArrayList;
import java.util.Hashtable;

import android.text.format.Time;
import android.util.Pair;
import communicationManager.CommunicationManager;
import communicationManager.storage.*;
import communicationManager.dataStructure.ObjectData.SensorType;

public class Acquisition {

	Storage storage;
	CommunicationManager cm;

	/**
	 * Constructor. Creates a new Adquisition object
	 */
	public Acquisition() {

		this.cm = CommunicationManager.getInstance();
	}

	/**
	 * Method to set a storage object to be used for the acquisition of data
	 * @param storage storage object that will be used
	 */
	public void setStorage(Storage storage) {

		this.storage = storage;
	}

	/**
	 * Method to retrieve the information between two indexes for one or more devices
	 * @param start starting ID
	 * @param end ending ID
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveInformationByID(
			int start, int end, ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		if ((start != 0) && (start < end)) {

			storage.open();

			for (int i = 0; i < sensorsAndDevices.size(); i++) {

				Hashtable<SensorType, ArrayList<Double>> hashAux;
				ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
				String nameDevice = sensorsAndDevices.get(i).second;
				String nameTable = cm.getDevice(nameDevice).getTableName();
				hashAux = storage.retrieveInformationByID(sensors, nameTable,
						start, end);

				Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
						nameDevice, hashAux);
				data.add(pairAux);
			}

			if (!cm.isStoring())
				storage.close();
		}

		return data;
	}

	/**
	 * Method to retrieve the information streamed in a particular session for one or more devices
	 * @param session number of the session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveInformationBySession(
			int session,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		if (session != 0) {

			storage.open();

			for (int i = 0; i < sensorsAndDevices.size(); i++) {

				Hashtable<SensorType, ArrayList<Double>> hashAux;
				ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
				String nameDevice = sensorsAndDevices.get(i).second;
				String nameMetadataTable = cm.getDevice(nameDevice)
						.getMetadataTableName();
				String nameTable = cm.getDevice(nameDevice).getTableName();
				Pair<Integer, Integer> IDsPair = storage.getSessionIDs(
						nameMetadataTable, session);
				hashAux = storage.retrieveInformationByID(sensors, nameTable,
						IDsPair.first, IDsPair.second);

				Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
						nameDevice, hashAux);
				data.add(pairAux);

				if (!cm.isStoring())
					storage.close();
			}
		}

		return data;
	}

	/**
	 * Method to retrieve the information streamed belonging to a group of consecutives sessions for one or more devices
	 * @param sessionStart number of the starting session
	 * @param sessionEnd number of the ending session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveInformationByIntervalSessions(
			int sessionStart, int sessionEnd,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		if (sessionStart < sessionEnd) {

			storage.open();

			for (int i = 0; i < sensorsAndDevices.size(); i++) {

				Hashtable<SensorType, ArrayList<Double>> hashAux;
				ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
				String nameDevice = sensorsAndDevices.get(i).second;
				String nameMetadataTable = cm.getDevice(nameDevice)
						.getMetadataTableName();
				String nameTable = cm.getDevice(nameDevice).getTableName();
				Pair<Integer, Integer> pair1 = storage.getSessionIDs(
						nameMetadataTable, sessionStart);
				Pair<Integer, Integer> pair2 = storage.getSessionIDs(
						nameMetadataTable, sessionEnd);
				hashAux = storage.retrieveInformationByID(sensors, nameTable,
						pair1.first, pair2.second);

				Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
						nameDevice, hashAux);
				data.add(pairAux);

			}

			if (!cm.isStoring())
				storage.close();
		}

		return data;
	}

	/**
	 * Method to retrieve all the information stored in the DB for one or more devices
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveAllInformation(
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		storage.open();

		for (int i = 0; i < sensorsAndDevices.size(); i++) {

			Hashtable<SensorType, ArrayList<Double>> hashAux;
			ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
			String nameDevice = sensorsAndDevices.get(i).second;
			String nameTable = cm.getDevice(nameDevice).getTableName();
			hashAux = storage.retrieveAllInformation(sensors, nameTable);

			Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
					nameDevice, hashAux);
			data.add(pairAux);
		}

		if (!cm.isStoring())
			storage.close();

		return data;
	}

	/**
	 * Method to retrieve the information streamed in the last X seconds for one or more devices
	 * @param seconds last seconds to retrieve
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveInformationLastSeconds(
			long seconds, ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		storage.open();

		for (int i = 0; i < sensorsAndDevices.size(); i++) {

			Hashtable<SensorType, ArrayList<Double>> hashAux;
			ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
			String nameDevice = sensorsAndDevices.get(i).second;
			String nameTable = cm.getDevice(nameDevice).getTableName();
			hashAux = storage.retrieveInformationLastSeconds(seconds,
					nameTable, sensors);

			// If the number of rows acquired is marginally lower or just higher
			// that the esteemed rows, it is necessary
			// to fill with NaN or to delete some rows, in order to obtain
			// windows(segmentation) with the same size
			double esteemedRows = cm.getDevice(nameDevice).getRate() * seconds;
			double percentage = 5;
			double differenceAccepted = (esteemedRows / 100) * percentage;
			int obtainedRows = hashAux.get(sensors.get(0)).size();

			// Less rows, therefore a fill up of NaN values is done in every arraylist inside the hash
			if ((obtainedRows < esteemedRows)
					&& (obtainedRows > esteemedRows - differenceAccepted)) {
				for (int j = obtainedRows; j < esteemedRows; j++) {
					for (int h = 0; h < sensors.size(); h++)
						hashAux.get(sensors.get(h)).add(Double.NaN);
				}
			}

			// More rows, therefore is necessary to delete rows
			if (obtainedRows > esteemedRows) {
				for (int j = obtainedRows - 1; j >= esteemedRows; j--) {
					for (int h = 0; h < sensors.size(); h++)
						hashAux.get(sensors.get(h)).remove(j);
				}
			}

			Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
					nameDevice, hashAux);
			data.add(pairAux);
		}

		if (!cm.isStoring())
			storage.close();

		return data;
	}

	/**
	 * Method to retrieve the information streamed between a starting and a finishing date
	 * @param session number of the session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 * @return an arraylist of pairs with all the information. First element of the pair is the device name and 
	 * the second is the data retrieved for that device
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> retrieveInformationByDates(
			Time start, Time end,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();

		storage.open();

		for (int i = 0; i < sensorsAndDevices.size(); i++) {

			Hashtable<SensorType, ArrayList<Double>> hashAux;
			ArrayList<SensorType> sensors = sensorsAndDevices.get(i).first;
			String nameDevice = sensorsAndDevices.get(i).second;
			String nameTable = cm.getDevice(nameDevice).getTableName();
			hashAux = storage.retrieveInformationByDates(start, end, nameTable,
					sensors);

			Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(
					nameDevice, hashAux);
			data.add(pairAux);
		}

		if (!cm.isStoring())
			storage.close();

		return data;
	}
}
