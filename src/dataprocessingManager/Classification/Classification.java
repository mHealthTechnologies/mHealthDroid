package dataprocessingManager.Classification;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Classification {

	public enum classifierType {

		NAIVE_BAYES, J48, ADABOOST, ZEROR, LINEAR_REGRESSION;
	}

	private ArffLoader atf;
	private Instances trainInstances;
	private Instances testInstances;
	private Classifier cModel;
	private Evaluation eTest;
	private ArrayList<Attribute> attributes;

	/**
	 * Constructor. Creates a new Classification object
	 */
	public Classification() {

		atf = null;
		trainInstances = null;
		testInstances = null;
		cModel = null;
		eTest = null;
		attributes = new ArrayList<Attribute>();
	}

	/**
	 * Method to read a weka file
	 * @param dir directory file
	 * @param fileName arff file name  
	 */
	public void readFile(File dir, String fileName) {

		File file = new File(dir, fileName);
		// StringBuilder texto = readFile(file);
		// return texto;

		atf = new ArffLoader();
		try {
			atf.setFile(file);
		} catch (IOException e) {
			Log.d("ERROR", "Error 1");
			e.printStackTrace();
		}
	}

	/**
	 * Method to set train instances using a loaded arff file
	 */
	public void setTrainInstances() {

		if (atf != null)
			try {
				trainInstances = atf.getDataSet();
			} catch (IOException e) {
				e.printStackTrace();
			}

		for (int i = 0; i < trainInstances.numAttributes(); i++)

			attributes.add(trainInstances.attribute(i));
	}

	/**
	 * Method to set test instances using a loaded arff file
	 * @throws IOException
	 */
	public void setTestInstances() throws IOException {

		if (atf != null)
			testInstances = atf.getDataSet();
	}

	/**
	 * Method to get a train instances summary
	 * @return a string with the summary of the instances
	 */
	public String getTrainInstancesSummary() {

		String trainSummary = "UNKNOWN";

		if (trainInstances != null)
			trainSummary = trainInstances.toSummaryString();

		return trainSummary;
	}

	/**
	 * Method to train a classifier
	 * @param classIndex index of the class attribute
	 * @param classifier classifier to be used
	 */
	public void trainClassifier(int classIndex, classifierType classifier) {

		trainInstances.setClassIndex(classIndex);

		switch (classifier) {

		case NAIVE_BAYES:
			cModel = new NaiveBayes();
			break;

		case ADABOOST:

			cModel = new AdaBoostM1();
			break;

		case J48:

			cModel = new J48();
			break;

		case LINEAR_REGRESSION:

			cModel = new LinearRegression();
			break;

		case ZEROR:

			cModel = new ZeroR();
			break;
		}

		try {
			cModel.buildClassifier(trainInstances);

		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("ERROR", "Error 3");
		}
	}

	/**
	 * Method to test a classifier
	 */
	public void testClassifier() {

		if (testInstances != null && cModel != null) {

			try {
				eTest = new Evaluation(testInstances);
				eTest.evaluateModel(cModel, testInstances);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to get a summary description of the classifier evaluation
	 * @return 
	 */
	public String getTestSummary() {

		String testSummary = "";

		if (eTest != null)
			testSummary = eTest.toSummaryString();

		return testSummary;
	}

	/**
	 * Method to get the confusion matrix
	 * @return a matrix with the confusion matrix
	 */
	public double[][] getTestConfusionMatrix() {

		double[][] confusionMatrix = null;

		if (eTest != null) {

			confusionMatrix = new double[eTest.confusionMatrix().length][];
			confusionMatrix = eTest.confusionMatrix();
		}

		return confusionMatrix;
	}

	/**
	 * Method to get a Instances object from a featureVector
	 * @param featureVector the featureVector to convert in Instances
	 * @return an Instances object (in other words a set of Instance)
	 */
	public Instances featureVectorToInstances(ArrayList<Double> featureVector) {

		Instances instances = new Instances("Instances", attributes, 0);
		DenseInstance instance = new DenseInstance(attributes.size());

		for (int i = 0; i < featureVector.size(); i++)

			instance.setValue(i, featureVector.get(i));

		// instance.setValue(featureVector.size(), -1);
		instances.add(instance);
		// Set class attribute
		instances.setClassIndex(attributes.size() - 1);

		return instances;
	}

	/**
	 * Method to get a Instances object from a featureMatrix
	 * @param featureMatrix the featureMatrix to convert in Instances
	 * @return an Instances object (in other words a set of Instance)
	 */
	public Instances featureMatrixToInstances(
			ArrayList<ArrayList<Double>> featureMatrix) {

		Instances instances = new Instances("Instances", attributes, 0);
		DenseInstance instance = new DenseInstance(attributes.size());

		for (int i = 0; i < featureMatrix.size(); i++) {

			for (int j = 0; j < featureMatrix.size(); j++) {

				instance.setValue(j, featureMatrix.get(i).get(j));
			}
			// instance.setValue(featureVector.size(), -1);
			instances.add(instance);
			// Set class attribute
			instances.setClassIndex(attributes.size() - 1);
		}

		return instances;
	}

	/**
	 * Method to classify an Instance
	 * @param unlabeled an unlabeled instance.
	 * @return a double with the new attribute value (If the corresponding
	 * attribute is nominal (or a string) then this is the new value's
	 * index as a double).
	 */
	public double classifyInstanceToDouble(Instance unlabeled) {

		double clsLabel = -1;
		try {
			clsLabel = cModel.classifyInstance(unlabeled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		unlabeled.setClassValue(clsLabel);
		return clsLabel;
		// Log.d("Classifying", clsLabel + " -> " +
		// unlabeled.classAttribute().value((int)clsLabel));
	}

	/**
	 * Method to classify an Instance
	 * @param unlabeled an unlabeled instance
	 * @return a string with the result of classifying the unlabeled instance
	 */
	public String classifyInstanceToString(Instance unlabeled) {

		double clsLabel = -1;
		try {
			clsLabel = cModel.classifyInstance(unlabeled);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		unlabeled.setClassValue(clsLabel);
		return unlabeled.classAttribute().value((int) clsLabel);
		// Log.d("Classifying", clsLabel + " -> " +
		// unlabeled.classAttribute().value((int)clsLabel));
	}

	/**
	 * Method to load a model
	 * @param classifier classifier used to load the model
	 */
	public void loadModel(Classifier classifier) {

		cModel = classifier;
	}
	
	/**
	 * Method to get the list of attributes
	 * @return an arraylist with all the attributes
	 */
	public ArrayList<Attribute> getAttributes(){
		
		return attributes;
	}
}
