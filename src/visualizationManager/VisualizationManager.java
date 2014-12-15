package visualizationManager;

import java.util.ArrayList;
import java.util.Hashtable;


import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewStyle;



import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;

import visualizationManager.Plot.GraphType;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

public class VisualizationManager extends Activity {

	private static VisualizationManager instance; // Unique instance of this singleton class
	private CommunicationManager cManager; // Instance of the communication manager

	private static Hashtable<String, ObjectVisualization> Graphs; // set of graphs

	private static int[] colors = { Color.BLUE, Color.RED, Color.GREEN,
			Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.LTGRAY };

	
	
	class ObjectVisualization {

		Plot graph;
		ArrayList<SensorType> sensors;
		int cont;

		public ObjectVisualization(Plot p) {
			sensors = new ArrayList<ObjectData.SensorType>();
			graph = p;
			cont = 0;
		}

	}


	/**
	 * Constructor. Initialize the variables.
	 */
	private VisualizationManager() {
		super();
		Graphs = new Hashtable<String, ObjectVisualization>();
	}

	/**
	 * Add a new graph
	 * @param nameGraph Name given to the graph
	 * @param type Is the type of graph to represent
	 * @param context The UI Activity Context
	 */
	public boolean addGraph(String nameGraph, GraphType type, Context context) {

		if (!Graphs.contains(nameGraph)) {
			ObjectVisualization ov = new ObjectVisualization(new Plot(type,	context, nameGraph));
			Graphs.put(nameGraph, ov);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Remove a graph
	 * @param nameGraph The graph's name
	 * @return True in case of success, False otherwise
	 */
	public boolean removeGraph(String nameGraph){
		
		if (Graphs.contains(nameGraph)) {
			Graphs.remove(nameGraph);
			return true;
		}
		else 
			return false;
	}

	/**
	 * Put a graph into a specific layout in order to be painted
	 * @param nameGraph Name given to the graph
	 * @param layout Layout which contains the graph
	 */
	public void paint(String nameGraph, LinearLayout layout) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.paint(layout);
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * Add a Serie (set of values) to be represented in the graph. If there is no graph with this name
	 * the function throws a null pointer exception
	 * @param nameGraph Name given to the graph
	 * @param nameSerie Is the name given to the serie
	 * @param data Is an array with the values to be represented
	 */
	public boolean addSeries(String nameGraph, String nameSerie,ArrayList<Float> data) {

		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.addSeries(nameSerie, data);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Add a Serie (set of values) to be represented in the graph. If there is no graph with this name
	 * the function throws a null pointer exception
	 * @param nameGraph Name given to the graph
	 * @param nameSerie Is the name given to the serie
	 * @param data Is an array with the values to be represented
	 * @param color Is the serie's color 
	 * @param thickness Is the thickness of the line/bar drawn in the graph
	 */
	public boolean addSeries(String nameGraph, String nameSerie,ArrayList<Float> data, int color, int thickness, String description) {

		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.addSeries(nameSerie, data, description, color, thickness);
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * Add a value at the end of the serie. If the graph doesn't exist a null exception will be thrown
	 * @param nameSerie Is the serie's name
	 * @param data Is the value to be inserted
	 * @param scrollToEnd Indicates if the graph scroll to the end
	 */
	public void appendData(String nameGraph, String nameSerie, float data, boolean scrollToEnd, int numMax) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.appendData(nameSerie, data, scrollToEnd, numMax);
		else
			throw new NullPointerException("There is no such graph");
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			final ObjectData od = (ObjectData) msg.obj;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ArrayList<String> names = cManager.getObjectCommunication(od.name).nameGraphs;
					for (int i = 0; i < names.size(); i++) {
						ObjectVisualization ov = Graphs.get(names.get(i));
						for (SensorType s : ov.sensors) {
							if (od.hashData.containsKey(s)) {
								if (ov.cont < ov.graph.myGraphView.getViewportSize()){ 
									if (!Double.isNaN(od.hashData.get(s).data))
										ov.graph.appendData(s.toString(), (float) od.hashData.get(s).data, false, ov.graph.numMax);
								}
								else{
									if (!Double.isNaN(od.hashData.get(s).data))
										ov.graph.appendData(s.toString(), (float) od.hashData.get(s).data, true, ov.graph.numMax);
								}
							}
						}
						ov.cont++;
						ov.graph.myGraphView.redrawAll();
					}
				}
			});
		}
	};

	/**
	 * This function makes the whole work to visualize the data received in an online way
	 * @param nameGraph Is the graph's name
	 * @param nameDevice Is the device's name which will send the data
	 * @param sensors A list with the sensors to be visualized
	 * @param numMax Is the maximum number of data to be visualized
	 */
	public void visualizationOnline(String nameGraph, String nameDevice, ArrayList<SensorType> sensors, int numMax) {

		ObjectVisualization ov = Graphs.get(nameGraph);
		if (ov != null) {
			int i = 0;
			ov.sensors = sensors;
			ov.graph.numMax = numMax;
			ArrayList<Float> zero = new ArrayList<Float>();
			for (SensorType s : sensors) {
				ov.graph.addSeries(s.toString(), zero, s.toString(), colors[i], 1);
				i = (i + 1) % colors.length;
			}
			if (cManager == null)
				cManager = CommunicationManager.getInstance();

			cManager.setHandlerVisualization(myHandler);
			if (!cManager.getObjectCommunication(nameDevice).nameGraphs.contains(nameGraph))
				cManager.getObjectCommunication(nameDevice).nameGraphs.add(nameGraph);
			cManager.getObjectCommunication(nameDevice).monitoring = true;
		}
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * This function makes the whole work to visualize the data received in an online way
	 * @param nameGraph Is the graph's name
	 * @param nameDevice Is the device's name which will send the data
	 * @param sensors A list with the sensors to be visualized
	 * @param colors A list of colors. Each series will be painted with these colors
	 * @param thickness A list of thickness. Each series will have its thickness
	 * @param numMax Is the maximum number of data to be visualized
	 */
	public void visualizationOnline(String nameGraph, final String nameDevice,
			ArrayList<SensorType> sensors, int[] colors, int[] thickness, int numMax) {

		ObjectVisualization ov = Graphs.get(nameGraph);
		if (ov != null) {
			int i = 0;
			ov.sensors = sensors;
			ov.graph.numMax = numMax;
			ArrayList<Float> zero = new ArrayList<Float>();
			for (SensorType s : sensors) {
				ov.graph.addSeries(s.toString(), zero, s.toString(), colors[i], thickness[i]);
				i++;
			}
			if (cManager == null)
				cManager = CommunicationManager.getInstance();

			cManager.setHandlerVisualization(myHandler);
			if (!cManager.getObjectCommunication(nameDevice).nameGraphs.contains(nameGraph))
				cManager.getObjectCommunication(nameDevice).nameGraphs.add(nameGraph);
			cManager.getObjectCommunication(nameDevice).monitoring = true;
		}
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * This function stops the visualization online
	 * @param nameGraph Is the graph's name
	 * @param nameDevice Is the device's name which will send the data
	 */
	public void stopVisualizationOnline(String nameGraph, String nameDevice) {

		if (cManager == null)
			cManager = CommunicationManager.getInstance();
		//checks if the grpah is related with the device. In case it is, the grpah is removed from the
		//list of graphs realted with the device
		if (cManager.getObjectCommunication(nameDevice).nameGraphs.contains(nameGraph))
			cManager.getObjectCommunication(nameDevice).nameGraphs.remove(nameGraph);
		else
			throw new NullPointerException("Graph doesn't belong to the device");
		//if the device has no more graphs to hand data, then monitoring is stopped
		if(cManager.getObjectCommunication(nameDevice).nameGraphs.isEmpty())
			cManager.getObjectCommunication(nameDevice).monitoring = false;
		
		ObjectVisualization ov = Graphs.get(nameGraph);
		ov.cont = 0;
		ov.sensors.clear();
		//myHandler.removeMessages(0);
	}

	/**
	 * This function returns the unique instance of this manager
	 * @return An instance of this class
	 */
	public static VisualizationManager getInstance() {

		if (instance == null)
			instance = new VisualizationManager();

		return instance;
	}

	/**
	 * Sets whether the graph is scalable or not
	 * @param nameGraph It's the graph's name
	 * @param scrollable Boolean that indicates if it's scalable or not
	 */
	public void setScalable(String nameGraph, boolean scalable) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setScalable(scalable);
		else
			throw new NullPointerException("There is no such graph");
	}

	
	/**
	 * Sets whether the graph is scrollable or not
	 * @param nameGraph It's the graph's name
	 * @param scrollable Boolean that indicates if it's scrollable or not
	 */
	public void setScrollable(String nameGraph, boolean scrollable) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setScrollable(scrollable);
		else
			throw new NullPointerException("There is no such graph");
	}
	

	/**
	 * Sets the viewport 
	 * @param nameGraph It's the graph's name
	 * @param start It indicates where the viewport starts
	 * @param size It indicates the viewport's size
	 */
	public void setViewPort(String nameGraph, double start, double size) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setViewPort(start, size);
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * Set whether the bounds of the Y axis are introduced manually or it is calculated
	 * @param nameGraph It's the graph's name
	 * @param manualYAxis Boolean that indicates if the bounds are introduced manually
	 */
	public void setManualYAxis(String nameGraph, boolean manualYAxis) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setManualYAxis(manualYAxis);
		else
			throw new NullPointerException("There is no such graph");
	}

	/**
	 * Set the values of the Y axis' bounds
	 * @param nameGraph It's the graph's name
	 * @param max Is the maximum value of the bound
	 * @param min Is the minimum value of the bound
	 */
	public void setManualYAxisBounds(String nameGraph, double max, double min) {

		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setManualYAxisBounds(max, min);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Remove a series from a graph.
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param nameSerie Is the serie's name
	 */
	public void removeSeries(String nameGraph, String nameSerie){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.removeSeries(nameSerie);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Remove all series from the graph.
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 */
	public void removeAllSeries(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.removeAllSeries();
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Set the horizontal labels of the graph
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param labels Is the array with the labels
	 */
	public void setHorizontalLabels(String nameGraph, String[] labels){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setHorizontalLabels(labels);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Set the vertical labels of the graph
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param labels Is the array with the labels
	 */
	public void setVerticalLabels(String nameGraph, String[] labels){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setVerticalLabels(labels);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Set whether the graph must show the legend or not
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param scrollable Boolean that indicates if shows the legend or not
	 */
	public void setShowLegend(String nameGraph, boolean showLegend){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setShowLegend(showLegend);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Set the align of the legend in the graph
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param legendAlign The align where the legend must be placed 
	 */
	public void setLegendAlign(String nameGraph, LegendAlign align){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setLegendAlign(align);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Set the width of the legend
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @param size It is the width's size of the legend
	 */
	public void setLegendWidth(String nameGraph, float size){
		
		if (Graphs.get(nameGraph) != null)
			Graphs.get(nameGraph).graph.setLegendWidth(size);
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Get the align of the legend
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @return The legend's align
	 */
	public LegendAlign getLegendAlign(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.getLegendAlign();
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Get the width of the legend
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @return The legend's witdh
	 */
	public float getLegendWidth(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.getLegendWitdh();
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Get the graph's style
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @return The graph's style
	 */
	public GraphViewStyle getGraphStyle(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.getGraphStyle();
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Checks whether the graph is scrollable
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @return True in case the graph is scrollable, false otherwise
	 */
	public boolean isScrollable(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.isScrollable();
		else
			throw new NullPointerException("There is no such graph");
	}
	
	/**
	 * Checks whether the legend is showed
	 * If the graph doesn't exist a null exception will be thrown.
	 * @param nameGraph Is the graph's name
	 * @return True in case the legend is showed, false otherwise
	 */
	public boolean isShowLegend(String nameGraph){
		
		if (Graphs.get(nameGraph) != null)
			return Graphs.get(nameGraph).graph.isShowLegend();
		else
			throw new NullPointerException("There is no such graph");
	}

}
