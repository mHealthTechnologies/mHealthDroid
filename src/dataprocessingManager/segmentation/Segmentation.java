package dataprocessingManager.segmentation;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Pair;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;

public class Segmentation {

	private CommunicationManager cm;

	/**
	 * This class represents a data segmentation. The data is stored in an array and this object indicates where
	 * starts and finishes the segment. It also indicate if the segment is complete or not, that's it, if  either the
	 * segment has the windows size or it is smaller
	 */
	public class ObjectSegmentation {

		public int start;
		public int finish;
		public boolean complete;

		public ObjectSegmentation() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ObjectSegmentation(int start, int finish, boolean complete) {

			this.start = start;
			this.finish = finish;
			this.complete = complete;
		}

	}

	/**
	 * Constructor. Initialize the variables.
	 */
	public Segmentation() {

		cm = CommunicationManager.getInstance();
	}

	/**
	 * Method that makes the windowing without overlap the windows for a list of devices
	 * @param sensors A list of pairs that contain the data of each sensor and device
	 * @param seconds The size of the windows, measure in seconds
	 * @return A list of ObjectSegmentation with the segmentations
	 */
	public ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> windowing_noOverlap(
			ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> sensors, float seconds, ArrayList<Float> sampleRates) {

		ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> index = new ArrayList<Pair<String, ArrayList<ObjectSegmentation>>>();
		for (int i = 0; i < sensors.size(); i++) {
//			float rate = (float) cm.getRate(sensors.get(i).first);
			float rate = sampleRates.get(i);
			//float window = rate * seconds;
			ArrayList<ObjectSegmentation> os = windowing_noOverlap(sensors.get(i).second, seconds, rate);
			index.add(new Pair<String, ArrayList<ObjectSegmentation>>(sensors.get(i).first, os));
		}

		return index;
	}

	/**
	 * Method that makes the windowing overlapping the windows for a list of devices
	 * @param sensors The data given by the sensors
	 * @param seconds The size of the windows, measure in seconds
	 * @param secondsPreviousWindow The size of the overlap, measure in seconds of the previous window overlapped
	 * @return A list of ObjectSegmentation with the segmentations
	 */
	public ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> windowing_Overlap(
						ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> sensors,
							float seconds, float secondsPreviousWindow, ArrayList<Float> sampleRates){

		ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> index = new ArrayList<Pair<String, ArrayList<ObjectSegmentation>>>();
		for (int i = 0; i < sensors.size(); i++) {
			//float rate = (float) cm.getRate(sensors.get(i).first);
			//float window = rate * seconds;
			//float samplePrevious = secondsPreviousWindow * rate;
			float rate = sampleRates.get(i);
			ArrayList<ObjectSegmentation> os = windowing_Overlap(sensors.get(i).second, seconds, 
					secondsPreviousWindow, rate);
			index.add(new Pair<String, ArrayList<ObjectSegmentation>>(sensors.get(i).first, os));
		}

		return index;
	}

	/**
	 * Method that makes the windowing without overlap for a set of sensor (and its data) given
	 * @param sensors Hashtable with the type of sensor and its data
	 * @param windowSize The size of the windows, measure in seconds
	 * @return A list of ObjectSegmentation
	 */
	public ArrayList<ObjectSegmentation> windowing_noOverlap(Hashtable<SensorType, ArrayList<Double>> sensors, 
													float windowSize, float sampleRate) {

		int windowInSamples = (int) (windowSize*sampleRate);
		ArrayList<ObjectSegmentation> index = new ArrayList<ObjectSegmentation>();
		SensorType s = sensors.keys().nextElement();
		if (sensors.get(s).size() < windowInSamples)
			throw new IllegalStateException("The size of the window is bigger than the number of samples");
		int pieces = sensors.get(s).size() / windowInSamples;
		for (int i = 0; i < pieces; i++) {
			int start = i * windowInSamples;
			int finish = i * windowInSamples + (windowInSamples - 1);
			index.add(new ObjectSegmentation(start, finish, true));
		}
		int tam = sensors.get(s).size();
		float resto = tam % windowInSamples;
		if ((sensors.get(s).size() % windowInSamples) != 0) {
			int start = pieces * windowInSamples;
			int finish = sensors.get(s).size() - 1;
			index.add(new ObjectSegmentation(start, finish, false));
		}

		return index;
	}

	/**
	 * Method that makes the windowing overlapping for a set of sensor (and its data) given
	 * @param sensors Hashtable with the type of sensor and its data
	 * @param windowSize The size of the windows, measure in seconds
	 * @return A list of ObjectSegmentation
	 */
	public ArrayList<ObjectSegmentation> windowing_Overlap(Hashtable<SensorType, ArrayList<Double>> sensors, 
												float windowSize, float secondsPreviousWindow, float sampleRate) {

		if (windowSize <= secondsPreviousWindow)
			throw new IllegalStateException("samplePreviousWindow can not be bigger than windowSize");

		ArrayList<ObjectSegmentation> index = new ArrayList<ObjectSegmentation>();
		SensorType s = sensors.keys().nextElement();
		if (sensors.get(s).size() <= windowSize)
			throw new IllegalStateException("The size of the window is bigger or equal than the number of samples");

		int windowInSamples = (int) (windowSize*sampleRate);
		int samplePreviousWindow = (int) (secondsPreviousWindow*sampleRate);
		int start = 0;
		int finish = windowInSamples - 1;
		while (finish <= (sensors.get(s).size()-1)) {
			index.add(new ObjectSegmentation(start, finish, true));
			start = finish - samplePreviousWindow;
			finish = start + windowInSamples;
		}
		if ((start - samplePreviousWindow + windowInSamples) != sensors.get(s).size() - 1)
			index.add(new ObjectSegmentation(start, sensors.get(s).size() - 1, false));

		return index;
	}
}
