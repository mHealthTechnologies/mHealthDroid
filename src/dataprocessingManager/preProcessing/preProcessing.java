package dataprocessingManager.preProcessing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import android.util.Pair;

import communicationManager.dataStructure.ObjectData.SensorType;

public class preProcessing {

	public preProcessing() {

	}

	public enum PreProcessingType {
		DOWNSAMPLING, UPSAMPLING;
	}

	/**
	 * Method that make an Downsampling for a set of sensors and devices given
	 * @param sensors List which contains hashtables with the data of each sensor and device
	 * @param factor Factor of downsampling
	 * @return A list of hashtables with the new data for each sensor
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> downSampling(
				ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> sensors, int factor) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> list = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();
		for (int i = 0; i < sensors.size(); i++) {
			Hashtable<SensorType, ArrayList<Double>> hash = downSampling(sensors.get(i).second, factor);
			list.add(new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(sensors.get(i).first, hash));
		}

		return list;
	}

	/**
	 * Method that make an Upsampling for a set of sensors and devices given
	 * @param sensors List which contains hashtables with the data of each sensor and device
	 * @param factor Factor of upsampling
	 * @return A list of hashtables with the new data for each sensor
	 */
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> upSampling(
				ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> sensors, int factor) {

		ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> list = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();
		for (int i = 0; i < sensors.size(); i++) {
			Hashtable<SensorType, ArrayList<Double>> hash = upSampling(sensors.get(i).second, factor);
			list.add(new Pair<String, Hashtable<SensorType, ArrayList<Double>>>(sensors.get(i).first, hash));
		}

		return list;
	}

	/**
	 * Method that make a Downsampling for a set of sensors given
	 * @param sensors Hashtable which contains the data of each sensor
	 * @param factor Factor of downsampling
	 * @return A hashtable with the new data for each sensor
	 */
	public Hashtable<SensorType, ArrayList<Double>> downSampling(Hashtable<SensorType, ArrayList<Double>> sensors, int factor) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();
		Set<SensorType> keys = sensors.keySet();
		for (SensorType s : keys) {
			ArrayList<Double> data = sensors.get(s);
			ArrayList<Double> newData = new ArrayList<Double>();
			for (int i = 0; i < data.size(); i += factor)
				newData.add(data.get(i));

			hash.put(s, newData);
		}

		return hash;
	}

	/**
	 * Method that make an Upsampling for a set of sensors given
	 * @param sensors Hashtable which contains the data of each sensor
	 * @param factor Factor of upsampling
	 * @return A hashtable with the new data for each sensor
	 */
	public Hashtable<SensorType, ArrayList<Double>> upSampling(Hashtable<SensorType, ArrayList<Double>> sensors, int factor) {

		Hashtable<SensorType, ArrayList<Double>> hash = new Hashtable<SensorType, ArrayList<Double>>();
		Set<SensorType> keys = sensors.keySet();
		for (SensorType s : keys) {
			ArrayList<Double> data = sensors.get(s);
			ArrayList<Double> newData = new ArrayList<Double>();
			for (int i = 0; i < data.size() - 1; i++) {
				newData.add(data.get(i));
				double n = (data.get(i+1) - data.get(i)) / factor;
				for (int f = 1; f < factor; f++)
					newData.add(data.get(i)+f*n);
			}
			newData.add(data.get(data.size() - 1));
			hash.put(s, newData);
		}

		return hash;
	}

}
