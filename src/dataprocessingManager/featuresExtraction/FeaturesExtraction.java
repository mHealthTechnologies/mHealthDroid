package dataprocessingManager.featuresExtraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.util.Pair;

import communicationManager.dataStructure.ObjectData.SensorType;
import dataprocessingManager.segmentation.Segmentation.ObjectSegmentation;

public class FeaturesExtraction {

	public enum FeatureType {
		MEAN, VARIANCE, MEDIAN, STANDARD_DEVIATION, ZERO_CROSSING_RATE, MEAN_CROSSING_RATE, MAXIMUM, MINIMUM;
	}
	
	/**
	 * This object represents a feature. The object indicates what kind of feautere is, and over which device and sensor
	 * must be calculated
	 */
	public class ObjectFeature {

		public String nameDevice;
		public SensorType sensor;
		public FeatureType feature;

		public ObjectFeature() {
			// TODO Auto-generated constructor stub
			super();
		}

		public ObjectFeature(String nameDevice, SensorType sensor,
				FeatureType feature) {
			this.nameDevice = nameDevice;
			this.sensor = sensor;
			this.feature = feature;
		}
	}

	/**
	 * Consctrucor of the class.
	 */
	public FeaturesExtraction() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Method that calculated the features given as parameter from the data of one or more sensors and devices.
	 * @param data The data received from the devices
	 * @param features List of features to be extracted from the data
	 * @return An array with the features calculated
	 */
	public ArrayList<Double> feature_extraction(ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data,
					ArrayList<ObjectFeature> features) {

		ArrayList<Double> feature_vector = new ArrayList<Double>();
		for (int i = 0; i < features.size(); i++) {
			double f = 0;
			switch (features.get(i).feature) {
			case MEAN:
				Hashtable<SensorType, ArrayList<Double>> hashMean = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashMean != null)
					f = Mean(hashMean, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case MEDIAN:
				Hashtable<SensorType, ArrayList<Double>> hashMedian = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashMedian != null)
					f = Median(hashMedian, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case STANDARD_DEVIATION:
				Hashtable<SensorType, ArrayList<Double>> hashStandard = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashStandard != null)
					f = Std(hashStandard, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case VARIANCE:
				Hashtable<SensorType, ArrayList<Double>> hashVariance = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashVariance != null)
					f = Variance(hashVariance, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case ZERO_CROSSING_RATE:
				Hashtable<SensorType, ArrayList<Double>> hashZero = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashZero != null)
					f = ZeroCrossing(hashZero, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case MEAN_CROSSING_RATE:
				Hashtable<SensorType, ArrayList<Double>> hashMeanCrossing = getHashByNameDevice(data, features.get(i).nameDevice);
				if (hashMeanCrossing != null)
					f = MeanCrossing(hashMeanCrossing, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case MAXIMUM:
				Hashtable<SensorType, ArrayList<Double>> hashMax = getHashByNameDevice(data, features.get(i).nameDevice);
				if(hashMax != null)
					f = Maximum(hashMax, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			case MINIMUM:
				Hashtable<SensorType, ArrayList<Double>> hashMin = getHashByNameDevice(data, features.get(i).nameDevice);
				if(hashMin != null)
					f = Minimum(hashMin, features.get(i).sensor);
				else
					throw new IllegalStateException("There's no Device called "	+ features.get(i).nameDevice);
				break;
			}
			feature_vector.add(f);
		}

		return feature_vector;
	}

	/**
	 * Method that calculated the features given as parameter from the data of one or more sensors and devices.
	 * The method is to be used with segmented data
	 * @param data The data received from the devices
	 * @param features List of features to be extracted from the data
	 * @param index List with the segmentations
	 * @param incompleteWindow Boolean which indicates if the last and incomplete window must be included in the calculation
	 * @return A matrix where the rows are arrays with the features calculated for each segment 
	 */
	public ArrayList<ArrayList<Double>> feature_extraction(
			ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data,	ArrayList<ObjectFeature> features,
			ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> index, boolean incompleteWindow) {

		ArrayList<ArrayList<Double>> aux_matrix = new ArrayList<ArrayList<Double>>();

		for (int i = 0; i < features.size(); i++) {
			// feature_matrix.add(new ArrayList<Double>());
			ArrayList<Double> f = new ArrayList<Double>();
			switch (features.get(i).feature) {
			case MEAN:
				ArrayList<ObjectSegmentation> indMean = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashMean = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indMean.size(); j++) {
					if (indMean.get(j).complete || incompleteWindow) // if the window is complete or 
																	  //the incomplete window must be add
						f.add(Mean(hashMean, features.get(i).sensor, indMean.get(j)));
					
				}
				break;
			case MEDIAN:
				ArrayList<ObjectSegmentation> indMedian = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashMedian = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indMedian.size(); j++) {
					if (indMedian.get(j).complete || incompleteWindow) // if the window is complete or the
																		// incomplete window must be add
						f.add(Median(hashMedian, features.get(i).sensor, indMedian.get(j)));
				}
				break;
			case STANDARD_DEVIATION:
				ArrayList<ObjectSegmentation> indStandard = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashStandard = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indStandard.size(); j++) {
					if (indStandard.get(j).complete || incompleteWindow) // if the window is complete or the
																			// incomplete window must be add
						f.add(Std(hashStandard, features.get(i).sensor, indStandard.get(j)));
				}
				break;
			case VARIANCE:
				ArrayList<ObjectSegmentation> indVariance = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashVariance = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indVariance.size(); j++) {
					if (indVariance.get(j).complete || incompleteWindow) // if the window is complete or the
																			// incomplete window must be add
						f.add(Variance(hashVariance, features.get(i).sensor, indVariance.get(j)));
				}
				break;
			case ZERO_CROSSING_RATE:
				ArrayList<ObjectSegmentation> indZero = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashZero = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indZero.size(); j++) {
					if (indZero.get(j).complete || incompleteWindow) // if the window is complete or the
																		// incomplete window must be add
						f.add((double) ZeroCrossing(hashZero, features.get(i).sensor, indZero.get(j)));
				}
				break;
			case MEAN_CROSSING_RATE:
				ArrayList<ObjectSegmentation> indMeanCrossing = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashMeanCrossing = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indMeanCrossing.size(); j++) {
					if (indMeanCrossing.get(j).complete || incompleteWindow) // if the window is complete or the
																				// incomplete window must be add
						f.add((double) MeanCrossing(hashMeanCrossing, features.get(i).sensor, indMeanCrossing.get(j)));
				}
				break;
			case MAXIMUM:
				ArrayList<ObjectSegmentation> indMax = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashMax = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indMax.size(); j++) {
					if (indMax.get(j).complete || incompleteWindow) // if the window is complete or the
																				// incomplete window must be add
						f.add((double) Maximum(hashMax, features.get(i).sensor, indMax.get(j)));
				}
				break;
			case MINIMUM:
				ArrayList<ObjectSegmentation> indMin = getIndexByNameDevice(index, features.get(i).nameDevice);
				Hashtable<SensorType, ArrayList<Double>> hashMin = 
								getHashByNameDevice(data, features.get(i).nameDevice);
				for (int j = 0; j < indMin.size(); j++) {
					if (indMin.get(j).complete || incompleteWindow) // if the window is complete or the
																				// incomplete window must be add
						f.add((double) Minimum(hashMin, features.get(i).sensor, indMin.get(j)));
				}
				break;
			}
			aux_matrix.add(f);
		}

		ArrayList<ArrayList<Double>> feature_matrix = new ArrayList<ArrayList<Double>>();
		for (int j = 0; j < aux_matrix.get(0).size(); j++) {
			ArrayList<Double> feature_vector = new ArrayList<Double>();
			for (int i = 0; i < features.size(); i++) {
				feature_vector.add(aux_matrix.get(i).get(j));
			}
			feature_matrix.add(feature_vector);
		}

		return feature_matrix;
	}

//	private Hashtable<SensorType, Double> Mean(
//			Hashtable<SensorType, ArrayList<Double>> sensors) {
//
//		Hashtable<SensorType, Double> mean = new Hashtable<SensorType, Double>();
//		int nan = 0;
//		Set<SensorType> keys = sensors.keySet();
//		for (SensorType s : keys) {
//			ArrayList<Double> data = sensors.get(s);
//			double sum = 0;
//			for (int i = 0; i < data.size(); i++)
//				if (!Double.isNaN(data.get(i)))
//					sum += data.get(i);
//				else
//					nan++;
//
//			sum = sum / (data.size() - nan);
//			mean.put(s, sum);
//		}
//
//		return mean;
//	}

	/**
	 * Method that calculated the mean of a set of data
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The mean of the segment
	 */
	private double Mean(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		double mean;
		int nan = 0;
		ArrayList<Double> d = data.get(sensor);
		double sum = 0;
		for (int i = 0; i < d.size(); i++)
			if (!Double.isNaN(d.get(i)))
				sum += d.get(i);
			else
				nan++;

		mean = sum / (d.size() - nan);

		return mean;
	}

	/**
	 * Method that calculated the mean of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The mean of the segment
	 */
	private double Mean(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		double mean;
		int nan = 0;
		List<Double> d =  data.get(sensor).subList(index.start, index.finish+1);
		double sum = 0;
		for (int i = 0; i < d.size(); i++)
			if (!Double.isNaN(d.get(i)))
				sum += d.get(i);
			else
				nan++;

		mean = sum / (d.size() - nan);

		return mean;
	}

//	private Hashtable<SensorType, Double> Variance(
//			Hashtable<SensorType, ArrayList<Double>> sensors) {
//
//		int nan = 0;
//		Hashtable<SensorType, Double> variance = new Hashtable<SensorType, Double>();
//		Hashtable<SensorType, Double> mean = Mean(sensors);
//		Set<SensorType> keys = sensors.keySet();
//		for (SensorType s : keys) {
//			ArrayList<Double> data = sensors.get(s);
//			double v = 0;
//			for (int i = 0; i < data.size(); i++)
//				if (!Double.isNaN(data.get(i)))
//					v += (data.get(i) - mean.get(s))
//							* (data.get(i) - mean.get(s));
//				else
//					nan++;
//
//			v = v / (data.size() - nan);
//			variance.put(s, v);
//		}
//
//		return variance;
//	}

	/**
	 * Method that calculated the variance of a set of data
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The variance of the segment
	 */
	private double Variance(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		double variance = 0;
		double mean = Mean(data, sensor);
		int nan = 0;

		ArrayList<Double> d = data.get(sensor);
		for (int i = 0; i < d.size(); i++)
			if (!Double.isNaN(d.get(i)))
				variance += (d.get(i) - mean) * (d.get(i) - mean);
			else
				nan++;

		variance = variance / ((d.size()-1) - nan);

		return variance;
	}

	/**
	 * Method that calculated the variance of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The variance of the segment
	 */
	private double Variance(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		double variance = 0;
		double mean = Mean(data, sensor, index);
		int nan = 0;

		List<Double> d = data.get(sensor).subList(index.start, index.finish+1);
		for (int i = 0; i < d.size(); i++)
			if (!Double.isNaN(d.get(i)))
				variance += (d.get(i) - mean) * (d.get(i) - mean);
			else
				nan++;

		variance = variance / ((d.size()-1) - nan);

		return variance;
	}

//	private Hashtable<SensorType, Double> Std(
//			Hashtable<SensorType, ArrayList<Double>> sensors) {
//
//		Hashtable<SensorType, Double> std = new Hashtable<SensorType, Double>();
//		Hashtable<SensorType, Double> variance = Variance(sensors);
//		Set<SensorType> keys = sensors.keySet();
//		for (SensorType s : keys) {
//			Double st = (Double) Math.sqrt(variance.get(s));
//			std.put(s, st);
//		}
//
//		return std;
//	}

	/**
	 * Method that calculated the standard deviation of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The standard deviation of the segment
	 */
	private double Std(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		double variance = Variance(data, sensor);
		double std = (Double) Math.sqrt(variance);

		return std;
	}

	/**
	 * Method that calculated the standard deviation of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The standard deviation of the segment
	 */
	private double Std(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		double variance = Variance(data, sensor, index);
		double std = (double) Math.sqrt(variance);

		return std;
	}

	// private Hashtable<SensorType, Double> Median (Hashtable<SensorType,
	// ArrayList<Double>> sensors){
	//
	// Hashtable<SensorType, Double> median = new Hashtable<SensorType,
	// Double>();
	// Set<SensorType> keys = sensors.keySet();
	// for(SensorType s: keys){
	// ArrayList<Double> data = sensors.get(s);
	// Collections.sort(data);
	// double m=0;
	// if(data.size()%2==0)
	// m = (data.get(data.size()/2)+data.get((data.size()/2)-1))/2;
	// else
	// m = data.get(data.size()/2);
	//
	// median.put(s, m);
	// }
	//
	// return median;
	// }

	/**
	 * Method that calculated the median of a set of data
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The median of the data
	 */
	private double Median(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		double median = 0;
		ArrayList<Double> d = new ArrayList<Double>();
		for (int i = 0; i < data.get(sensor).size(); i++)
			if (!Double.isNaN(data.get(sensor).get(i)))
				d.add(data.get(sensor).get(i));

		Collections.sort(d);
		if (d.size() % 2 == 0)
			median = (d.get(d.size() / 2) + d.get((d.size() / 2) - 1)) / 2;
		else
			median = d.get(d.size() / 2);

		return median;
	}

	/**
	 * Method that calculated the median of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The median of the segment
	 */
	private double Median(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		double median = 0;
		ArrayList<Double> d = new ArrayList<Double>();
		// in this occasion we have to create a new array because evertything
		// you do to the sublist will be reflect in the original
		// and we have to sort the sublist
		for (int i = index.start; i <= index.finish; i++)
			if (!Double.isNaN(data.get(sensor).get(i)))
				d.add(data.get(sensor).get(i));

		Collections.sort(d);
		if (d.size() % 2 == 0)
			median = (d.get(d.size() / 2) + d.get((d.size() / 2) - 1)) / 2;
		else
			median = d.get(d.size() / 2);

		return median;
	}

	/**
	 * Method that calculated the zero crossing of a set of data
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The number of times that the signal cross by the zero
	 */
	private int ZeroCrossing(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		int rate = 0;
		ArrayList<Double> d = data.get(sensor);
		for (int i = 0; i < d.size() - 1; i++)
			if ((d.get(i) <= 0 && d.get(i + 1) > 0)
					|| (d.get(i) >= 0 && d.get(i + 1) < 0))
				rate++;

		return rate;
	}

	/**
	 * Method that calculated the zero crossing of a specific segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The number of times that the signal cross by the zero
	 */
	private int ZeroCrossing(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		int rate = 0;
		List<Double> d = data.get(sensor).subList(index.start, index.finish+1);
		for (int i = 0; i < d.size() - 1; i++)
			if ((d.get(i) <= 0 && d.get(i + 1) > 0)
					|| (d.get(i) >= 0 && d.get(i + 1) < 0))
				rate++;

		return rate;
	}

	/**
	 * Method that calculated the mean crossing of a set of data
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @return The number of times that the signal cross by the mean
	 */
	private int MeanCrossing(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor) {

		Double mean = Mean(data, sensor);
		int rate = 0;
		ArrayList<Double> d = data.get(sensor);
		for (int i = 0; i < d.size() - 1; i++)
			if ((d.get(i) <= mean && d.get(i + 1) > mean)
					|| (d.get(i) >= mean && d.get(i + 1) < mean))
				rate++;

		return rate;
	}

	/**
	 * Method that calculated the mean crossing of a segment made previously
	 * @param data The data received of a set of sensors
	 * @param sensor The sensor whose data will be used to the calculation
	 * @param index ObjecteSegmentation with the index of the segmentation
	 * @return The number of times that the signal cross by the mean
	 */
	private int MeanCrossing(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index) {

		Double mean = Mean(data, sensor);
		int rate = 0;
		List<Double> d = data.get(sensor).subList(index.start, index.finish+1);
		for (int i = 0; i < d.size() - 1; i++)
			if ((d.get(i) <= mean && d.get(i + 1) > mean)
					|| (d.get(i) >= mean && d.get(i + 1) < mean))
				rate++;

		return rate;
	}
	
	private double Maximum(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor){
		
		return Collections.max(data.get(sensor));
	}
	
	private double Maximum(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index){
		
		List<Double> d = data.get(sensor).subList(index.start, index.finish+1);
		return Collections.max(d);
	}
	
	private double Minimum(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor){
		
		return Collections.min(data.get(sensor));
	}
	
	private double Minimum(Hashtable<SensorType, ArrayList<Double>> data,
			SensorType sensor, ObjectSegmentation index){
		
		List<Double> d = data.get(sensor).subList(index.start, index.finish+1);
		return Collections.min(d);
	}

	/**
	 * Method that gets the hashtable with the sensor's data of an specific device
	 * @param data List which contains hastables with the data of each sensor and device
	 * @param nameDevice THe device's name
	 * @return A hastable with the data of each sensor for an specific device
	 */
	private Hashtable<SensorType, ArrayList<Double>> getHashByNameDevice(
			ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> data,	String nameDevice) {

		Hashtable<SensorType, ArrayList<Double>> hash = null;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).first.equals(nameDevice)) {
				hash = data.get(i).second;
				return hash;
			}
		}
		return hash;
	}

	/**
	 * Method that gets the segmentation from an arraylist given the name of the device
	 * @param index List which contains the segmentation of each device
	 * @param nameDevice The device's name
	 * @return A list of ObjectSegmentation with the segmentations
	 */
	private ArrayList<ObjectSegmentation> getIndexByNameDevice(ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> index,
				String nameDevice) {

		ArrayList<ObjectSegmentation> os = null;
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).first.equals(nameDevice)) {
				os = index.get(i).second;
				return os;
			}
		}
		return os;
	}
}
