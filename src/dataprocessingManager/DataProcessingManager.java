package dataprocessingManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

import android.app.Service;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.storage.Storage;

import dataprocessingManager.Classification.Classification;
import dataprocessingManager.Classification.Classification.classifierType;
import dataprocessingManager.acquisition.*;
import dataprocessingManager.featuresExtraction.FeaturesExtraction;
import dataprocessingManager.featuresExtraction.FeaturesExtraction.FeatureType;
import dataprocessingManager.featuresExtraction.FeaturesExtraction.ObjectFeature;
import dataprocessingManager.preProcessing.preProcessing;
import dataprocessingManager.preProcessing.preProcessing.PreProcessingType;
import dataprocessingManager.segmentation.Segmentation;
import dataprocessingManager.segmentation.Segmentation.ObjectSegmentation;

public class DataProcessingManager extends Service {

	private static DataProcessingManager instance;
	private static Acquisition acquisition;
	private static preProcessing preprocesing;
	private static Segmentation segmentation;
	private static FeaturesExtraction featuresExtraction;
	public static Classification classification;
	private static Storage storage;
	private CommunicationManager cm;
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> hash;
	public ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>> hashProcessed;
	public ArrayList<Pair<String, ArrayList<ObjectSegmentation>>> indexSegmentation;
	public ArrayList<ArrayList<Double>> featuresMatrix;
	public ArrayList<Double> featuresVector;
	public float seconds;
	private Hashtable<String, ObjectProcessing> inferenceOnDevices;
	private int buffersReady;
	private PreProcessingType preprocessingType;
	private ArrayList<ObjectFeature> features;
	private int classIndex, factor, typeClassAtribute;
	private classifierType classifier;
	private Handler onlineHandler;
	public double doubleClassified;
	public String stringClassified;

	public enum DataType{
		Processed, Raw;
	}
	
	
	/**
	 * Constructor. Creates a new DataProcessingManager object
	 */
	private DataProcessingManager() {

		cm = CommunicationManager.getInstance();
		acquisition = new Acquisition();
		preprocesing = new preProcessing();
		segmentation = new Segmentation();
		featuresExtraction = new FeaturesExtraction();
		hash = null;
		inferenceOnDevices = new Hashtable<String, DataProcessingManager.ObjectProcessing>();
		seconds = 0;
		inferenceOnDevices = new Hashtable<String, DataProcessingManager.ObjectProcessing>();
		buffersReady = 0;
		classification = new Classification();
		hash = new ArrayList<Pair<String, Hashtable<SensorType, ArrayList<Double>>>>();
		features = new ArrayList<FeaturesExtraction.ObjectFeature>();
	}

	/**
	 * Method to get(or create if it does not exists) the unique DataProcessingManager instance
	 * @return the DataProcessingManager instance
	 */
	public static DataProcessingManager getInstance() {

		if (instance == null)
			instance = new DataProcessingManager();

		return instance;
	}

	class ObjectProcessing {

		public ArrayList<ObjectData> buffer;
		public int windowSize; // number of samples that are acquired
		public int maxSize;
		public int cont;
		public Hashtable<SensorType, ArrayList<Double>> hashOnline;
		public ArrayList<SensorType> sensors;

		public ObjectProcessing(String nameDevice,
				ArrayList<ObjectData.SensorType> sensors) {
			super();
			// TODO Auto-generated constructor stub
			this.setWSize(nameDevice);
			maxSize = windowSize * 3;
			buffer = new ArrayList<ObjectData>(maxSize);
			cont = 0;
			initBuffer();
			this.sensors = sensors;
			hashOnline = new Hashtable<ObjectData.SensorType, ArrayList<Double>>();
		}

		private void setWSize(String nameDevice) {
			if (cm == null)
				cm = CommunicationManager.getInstance();

			double rate = cm.getRate(nameDevice);
			windowSize = (int) (rate * seconds);
		}

		private void initBuffer() {
			for (int i = 0; i < maxSize; i++)
				buffer.add(new ObjectData());
		}

	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			String nameDevice = ((ObjectData) msg.obj).name;
			ObjectProcessing op = inferenceOnDevices.get(nameDevice);
			op.buffer.set(op.cont, (ObjectData) msg.obj);
			op.cont = (op.cont + 1) % op.maxSize;
			if (op.cont % (op.windowSize) == 0) {
				op.hashOnline.clear();
				for (int i = 0; i < op.sensors.size(); i++) {
					ArrayList<Double> aux = new ArrayList<Double>();
					int index = op.cont - op.windowSize; // since the buffer is circular, we have to calculated the indexes
					int threshold = op.cont;
					if(index<0){ //if the index is negative, it means that the last buffer's block is full
						index = index + op.maxSize;
						threshold = op.maxSize;
					}
					for (int j = index; j < threshold; j++){ // build the array with the data of a specific sensor and device
						ObjectData odtemp = op.buffer.get(j);
						SensorType stTemp = op.sensors.get(i);
						double dataTemp = odtemp.hashData.get(stTemp).data;
						aux.add(op.buffer.get(j).hashData.get(op.sensors.get(i)).data);
						
					}
					
					op.hashOnline.put(op.sensors.get(i), aux); // de aqui pa bajo va fuera del for
				}
				
				buffersReady++;
				if (buffersReady == inferenceOnDevices.size()) { // when all the buffers are filled, the data
					buffersReady = 0;							 // are classified
					hash.clear();
					Set<String> keys = inferenceOnDevices.keySet();
					for (String k : keys) {
						Pair<String, Hashtable<SensorType, ArrayList<Double>>> p = 
								new Pair<String, Hashtable<SensorType, ArrayList<Double>>>
										(k, inferenceOnDevices.get(k).hashOnline);
						hash.add(p);
					}
					post(runnableOnline);
				}
			}
		}
	};

	//el typeClassAtribute representa el tipo de la clase atributo. 0 para String, 1 para Double
	public void inferenceOnline(float windowInSeconds,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices, PreProcessingType preprocessingType, 
			int factor, int typeClassAtribute) {

		
		this.seconds = windowInSeconds;
		this.preprocessingType = preprocessingType;
		this.typeClassAtribute = typeClassAtribute;
		this.classIndex = classIndex;
		this.factor = factor;
		
		// first we create an objectProcessing for any devices which we want to get their data
		for (int i = 0; i < sensorsAndDevices.size(); i++) {
			String nameDevice = sensorsAndDevices.get(i).second;
			inferenceOnDevices.put(nameDevice, new ObjectProcessing(nameDevice,	sensorsAndDevices.get(i).first));
		}

		// secondly we indicate to the devices trhought the Communication
		// Manager that we want to get the data
		cm.setHandlerDataProcessing(myHandler);
		for (int j = 0; j < sensorsAndDevices.size(); j++)
			cm.getObjectCommunication(sensorsAndDevices.get(j).second).dataProcessing = true;
		
	}
	
	//online inference sending in a message the result of the classification.
	//The what field of the message is equal to 0
	public void inferenceOnline(float windowInSeconds,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices, PreProcessingType preprocessingType, 
			int factor, int typeClassAtribute, Handler mHandler) {
	
			this.onlineHandler = mHandler;
			inferenceOnline(windowInSeconds, sensorsAndDevices, preprocessingType, factor, typeClassAtribute);
	}

	Runnable runnableOnline = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (preprocessingType != null) {
				switch (preprocessingType) {
				case DOWNSAMPLING:
					downSampling(factor);
					break;
				case UPSAMPLING:
					upSampling(factor);
					break;
				}
				feature_extraction(DataType.Processed, false, false);
				Instances instan = classification.featureVectorToInstances(featuresVector);
				if(typeClassAtribute==0){
					stringClassified = classification.classifyInstanceToString(instan.firstInstance());
					if(onlineHandler!=null)
						onlineHandler.obtainMessage(0, stringClassified).sendToTarget();
				}
				else{
					doubleClassified = classification.classifyInstanceToDouble(instan.firstInstance());
					if(onlineHandler!=null)
						onlineHandler.obtainMessage(0, doubleClassified).sendToTarget();
				}
				
			} else {
				feature_extraction(DataType.Raw, false, false);
				Instances instan = classification.featureVectorToInstances(featuresVector);
				if(instan == null)
					Log.d("null", "el instan es null");
				if(typeClassAtribute==0){
					stringClassified = classification.classifyInstanceToString(instan.firstInstance());
					if(onlineHandler!=null)
						onlineHandler.obtainMessage(0, stringClassified).sendToTarget();
				}
				else{
					doubleClassified = classification.classifyInstanceToDouble(instan.firstInstance());
					if(onlineHandler!=null)
						onlineHandler.obtainMessage(0, doubleClassified).sendToTarget();
				}
				
				
				Log.d("imprimir", "Actividad -> "+doubleClassified);
			}
		}
	};

	public void endInferenceOnline() {

		Set<String> keys = inferenceOnDevices.keySet();
		for(String k: keys)
			cm.getObjectCommunication(k).dataProcessing = false;
		
		myHandler.removeCallbacks(runnableOnline);
		myHandler.removeMessages(0);
		clearFeatures();
		inferenceOnDevices.clear();
		features.clear();
	}
	
	public void setInferenceOnlineHandler(Handler inferenceHandler){
		
		this.onlineHandler = inferenceHandler;
	}

	/**
	 * Method to retrieve the information between two indexes for one or more devices
	 * @param start starting ID
	 * @param end ending ID
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveByID(int start, int end,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveInformationByID(start, end, sensorsAndDevices);
		logAcquiredData();
	}

	/**
	 * Method to retrieve the information streamed in a particular session for one or more devices
	 * @param session number of the session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveBySession(int session,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveInformationBySession(session, sensorsAndDevices);
		logAcquiredData();

	}

	/**
	 * Method to retrieve the information streamed belonging to a group of consecutives sessions for one or more devices
	 * @param sessionStart number of the starting session
	 * @param sessionEnd number of the ending session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveInformationByIntervalSessions(int sessionStart, int sessionEnd,	
					ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveInformationByIntervalSessions(sessionStart, sessionEnd, sensorsAndDevices);
		logAcquiredData();
	}

	/**
	 * Method to retrieve all the information stored in the DB for one or more devices
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveAllInformation(ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveAllInformation(sensorsAndDevices);
		logAcquiredData();
	}

	/**
	 * Method to retrieve the information streamed in the last X seconds for one or more devices
	 * @param seconds last seconds to retrieve
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveInformationLastSeconds(long seconds, 
					ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveInformationLastSeconds(seconds, sensorsAndDevices);
		logAcquiredData();
	}
	
	/**
	 * Method to retrieve the information streamed between a starting and a finishing date
	 * @param session number of the session
	 * @param sensorsAndDevices arraylist of pairs consisting of a device name and Sensors for this device which
	 * wants to be retrieved
	 */
	public void retrieveInformationByDates(Time start, Time end,
			ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices) {

		hash = acquisition.retrieveInformationByDates(start, end, sensorsAndDevices);
		logAcquiredData();
	}

	/**
	 * Helper method to log the acquired data
	 */
	private void logAcquiredData() {

		// To log the Acquired data
		Log.d("Data", "Dentro de logAcquiredData");
		for (int i = 0; i < hash.size(); i++) {

			Pair<String, Hashtable<SensorType, ArrayList<Double>>> pairAux = hash
					.get(i);
			Log.d("Data", "Dispositivo " + hash.get(i).first);
			Set<SensorType> sensors = pairAux.second.keySet();
			for (SensorType s : sensors) {
				Log.d("Data", "Señal " + s.name());
				ArrayList<Double> dataValue = pairAux.second.get(s);
				Log.d("Data", "Numero de datos: " + dataValue.size());
				for (int j = 0; j < dataValue.size(); j++) {
					Log.d("Data", String.valueOf(dataValue.get(j)));
					if (s == SensorType.TIME_STAMP) {
						double t = dataValue.get(j);
						Date d = new Date((long) t);
						Log.d("hora", d.toString());
					}
				}
			}
		}
	}

	/**
	 * Method to create and set the storage DB object to be used for the acquisition methods
	 * @param context
	 */
	public void createAndSetStorage(Context context) {
		if (storage == null)
			storage = new Storage(context);

		acquisition.setStorage(storage);
	}


	public void clearFeatures(){
		features.clear();
	}
	
	public void addFeature(String nameDevice, SensorType sensor, FeatureType feature){
		
		features.add(new FeaturesExtraction().new ObjectFeature(nameDevice, sensor, feature));
	}
	
	public void removeFeature(int index){
		features.remove(index);
	}
	
	public void removeFeaturesByDevice(String nameDevice){
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0; i<features.size(); i++)
			if(features.get(i).nameDevice.equals(nameDevice))
				indexes.add(i);
		
		for(int j=0; j<indexes.size(); j++)
			features.remove(indexes.get(j));
	}
	
	public void upSampling(int factor) {

		if (hash != null)
			hashProcessed = preprocesing.upSampling(hash, factor);
		else
			throw new IllegalStateException("There is no acquired data");
	}

	public void downSampling(int factor) {

		if (hash != null)
			hashProcessed = preprocesing.downSampling(hash, factor);
		else
			throw new IllegalStateException("There is no acquired data");
	}

	public void windowing_overlap(DataType datatype, float seconds,	float secondsPreviousWindow, ArrayList<Float> sampleRate) {

		if (datatype == DataType.Processed) {
			if (hashProcessed != null)
				indexSegmentation = segmentation.windowing_Overlap(hashProcessed, seconds, secondsPreviousWindow, sampleRate);
			else
				throw new IllegalStateException("There is no acquired data");
		} else {
			if (hash != null)
				indexSegmentation = segmentation.windowing_Overlap(hash, seconds, secondsPreviousWindow, sampleRate);
			else
				throw new IllegalStateException("There is no acquired data");
		}
	}

	public void windowing_NoOverlap(DataType datatype, float seconds, ArrayList<Float> sampleRate) {

		if (datatype == DataType.Processed) {
			if (hashProcessed != null)
				indexSegmentation = segmentation.windowing_noOverlap(hashProcessed, seconds, sampleRate);
			else
				throw new IllegalStateException("There is no acquired data");
		} else {
			if (hash != null)
				indexSegmentation = segmentation.windowing_noOverlap(hash, seconds, sampleRate);
			else
				throw new IllegalStateException("There is no acquired data");
		}
	}

	public void feature_extraction(DataType datatype, boolean segmentation,	boolean incompleteWindow) {

		if (datatype == DataType.Processed) {
			if (hashProcessed != null)
				if (!segmentation)
					featuresVector = featuresExtraction.feature_extraction(hashProcessed, features);
				else
					featuresMatrix = featuresExtraction.feature_extraction(hashProcessed, features,	indexSegmentation, incompleteWindow);
			else
				throw new IllegalStateException("There is no acquired data");
		} else {
			if (hash != null)
				if (!segmentation)
					featuresVector = featuresExtraction.feature_extraction(hash, features);
				else
					featuresMatrix = featuresExtraction.feature_extraction(hash, features, indexSegmentation, incompleteWindow);
			else
				throw new IllegalStateException("There is no acquired data");
		}
	}

	public void setWindowSize(float seconds) {

		this.seconds = seconds;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Method to read a weka file
	 * @param dir directory file
	 * @param fileName arff file name  
	 */
	public void readFile(File dir, String path) {

		classification.readFile(dir, path);
	}

	/**
	 * Method to set train instances using a loaded arff file
	 */
	public void setTrainInstances() {

		classification.setTrainInstances();
	}

	/**
	 * Method to set test instances using a loaded arff file
	 * @throws IOException
	 */
	public void setTestInstances() {

		classification.setTrainInstances();
	}

	/**
	 * Method to get a train instances summary
	 * @return a string with the summary of the instances
	 */
	public void getTrainInstancesSummary() {

		classification.getTrainInstancesSummary();
	}

	/**
	 * Method to train a classifier
	 * @param classIndex index of the class attribute
	 * @param classifier classifier to be used
	 */
	public void trainClassifier(int classIndex, classifierType classifier) {

		classification.trainClassifier(classIndex, classifier);
	}

	/**
	 * Method to test a classifier
	 */
	public void testClassifier() {

		classification.testClassifier();
	}

	/**
	 * Method to get a summary description of the classifier evaluation
	 * @return 
	 */
	public void getTestSummary() {

		classification.getTestSummary();
	}
	
	/**
	 * Method to get the confusion matrix
	 * @return a matrix with the confusion matrix
	 */
	public double[][] getTestConfusionMatrix() {

		return classification.getTestConfusionMatrix();
	}

	/**
	 * Method to get a Instances object from a featureVector
	 * @param featureVector the featureVector to convert in Instances
	 * @return an Instances object (in other words a set of Instance)
	 */
	public Instances featureVectorToInstances(ArrayList<Double> featureVector) {

		return classification.featureVectorToInstances(featureVector);
	}

	/**
	 * Method to get a Instances object from a featureMatrix
	 * @param featureMatrix the featureMatrix to convert in Instances
	 * @return an Instances object (in other words a set of Instance)
	 */
	public Instances featureMatrixToInstances(ArrayList<Double> featureMatrix) {

		return classification.featureVectorToInstances(featureMatrix);
	}

	/**
	 * Method to classify an Instance
	 * @param unlabeled an unlabeled instance
	 * @return a double with the result of classifying the unlabeled instance
	 */
	public double classifyInstanceToDouble(Instance unlabeled) {

		return classification.classifyInstanceToDouble(unlabeled);
	}

	/**
	 * Method to classify an Instance
	 * @param unlabeled an unlabeled instance
	 * @return a string with the result of classifying the unlabeled instance
	 */
	public String classifyInstanceToString(Instance unlabeled) {

		return classification.classifyInstanceToString(unlabeled);
	}
	
	/**
	 * Method to load a model
	 * @param classifier classifier used to load the model
	 */
	public void loadModel(Classifier classifier) {

		classification.loadModel(classifier);
	}
}
