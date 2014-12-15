package visualizationManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;


import android.content.Context;
import android.widget.LinearLayout;


public class Plot {

	public GraphView myGraphView; //Object of the library used to draw graphs
	public Hashtable<String, GraphViewSeries> hashSeries; // set of the series in the graph
	public Hashtable<String, Integer> hashCont; // set of counters of the series that represents the coordinate X of the serie
	public int numMax; //maximum number of the data to be visualized in the graph
	
	
	public enum GraphType {
		LINE, BAR;
	}

	/**
	 * Constructor. Initialize the variables.
	 * @param graph_type Is the type of graph to represent
	 * @param name Is the name given to the graph
	 */
	public Plot(GraphType graph_type, Context myContext, String name) {

		super();
		if (graph_type == GraphType.BAR)
			this.myGraphView = new BarGraphView(myContext, name);
		else
			this.myGraphView = new LineGraphView(myContext, name);

		hashSeries = new Hashtable<String, GraphViewSeries>();
		hashCont = new Hashtable<String, Integer>();
		numMax = 0;
	}

	/**
	 * Add a Serie (set of values) to be represented in the graph
	 * @param nameSerie Is the name given to the serie
	 * @param data Is an array with the values to be represented
	 */
	public boolean addSeries(String nameSerie, ArrayList<Float> data) {

		if (hashSeries.get(nameSerie) == null) {
			GraphViewData[] viewData = new GraphViewData[data.size()];
			for (int i = 0; i < data.size(); i++) {
				viewData[i] = new GraphViewData(i, data.get(i));
			}
			Integer c = data.size();
			hashCont.put(nameSerie, c);
			GraphViewSeries serie = new GraphViewSeries(viewData);
			myGraphView.addSeries(serie);
			hashSeries.put(nameSerie, serie);
			return true;
		} else
			return false;

	}

	/**
	 * Add a Serie (set of values) to be represented in the graph.
	 * It also add a description, an specific color and thickness
	 * @param nameSerie Is the name given to the serie
	 * @param data Is an array with the values to be represented
	 * @param description Is a brief description about the data
	 * @param color Is the serie's color 
	 * @param thickness Is the thickness of the line/bar drawn in the graph
	 */
	public boolean addSeries(String nameSerie, ArrayList<Float> data, String description,
			int color, int thickness) {

		if (hashSeries.get(nameSerie) == null) {
			GraphViewData[] viewData = new GraphViewData[data.size()];
			for (int i = 0; i < data.size(); i++) {
				viewData[i] = new GraphViewData(i, data.get(i));
			}
			Integer c = data.size();
			hashCont.put(nameSerie, c);
			GraphViewSeries serie = new GraphViewSeries(description, new GraphViewSeriesStyle(color, thickness), viewData);
			myGraphView.addSeries(serie);
			hashSeries.put(nameSerie, serie);
			return true;
		} else
			return false;
	}

	/**
	 * Add a value at the end of the serie. If the serie doesn't exist a null exception will be thrown
	 * @param nameSerie Is the serie's name
	 * @param data Is the value to be inserted
	 * @param scrollToEnd Indicates if the graph scroll to the end
	 * @param maxData Is the maximum number of data to be visualized
	 */
	public void appendData(String nameSerie, float data, boolean scrollToEnd, int maxData) {

		Integer c = hashCont.get(nameSerie);
		c++;
		hashSeries.get(nameSerie).appendData(new GraphViewData(c, data), scrollToEnd, maxData);
		hashCont.put(nameSerie, c);
	}

//	/**
//	 * Append a value at the end of the serie. If the serie doesn't exist a null exception will be thrown.
//	 * This method doesn't increment the size of the serie. It swaps the values from the first to the second-to-last
//	 * and introduce the new data at last position. That's it: X[0]=X[1], X[1]=X[2]...X[n-1]=X[n], X[n]=data
//	 * @param nameSerie Is the serie's name
//	 * @param data Is the value to be inserted
//	 * @param scrollToEnd Indicates if the graph scroll to the end
//	 */
//	public void appendData2(String nameSerie, float data, boolean scrollToEnd) {
//
//		Integer c = hashCont.get(nameSerie);
//		hashSeries.get(nameSerie).appendData2(new GraphViewData(c, data), scrollToEnd);
//	}


	/**
	 * Reset the serie. It remove all the values and introduces a new set
	 * If the serie doesn't exist this function doesn't do anything
	 * @param nameSerie Is the serie's name
	 * @param data Is the arrayList with the set of new values of the serie
	 */
	public void resetData(String nameSerie, ArrayList<Float> data) {

		if (hashSeries.containsKey(nameSerie)) {
			GraphViewData[] values = new GraphViewData[data.size()];
			for (int i = 0; i < data.size(); i++) {
				values[i] = new GraphViewData(i, data.get(i));
			}
			hashSeries.get(nameSerie).resetData(values);
			hashCont.put(nameSerie, data.size());
		}
	}

	/**
	 * Reset the serie. It remove all the values and introduces a new set
	 * If the serie doesn't exist this function doesn't do anything
	 * @param nameSerie Is the serie's name
	 * @param data Is the arrayList with the set of new values of the serie
	 */
//	public void resetData(String nameSerie, ArrayList<Float> data, int init) {
//
//		int index = init;
//		if (hashSeries.containsKey(nameSerie)) {
//			GraphViewData[] values = new GraphViewData[(int) myGraphView.getViewportSize()];
//			for (int i = 0; i < (int) myGraphView.getViewportSize(); i++) {
//				values[i] = new GraphViewData(i, data.get(index));
//				index = (index + 1) % (2 * (int) myGraphView.getViewportSize());
//			}
//			hashSeries.get(nameSerie).resetData(values);
//			hashCont.put(nameSerie, (int) myGraphView.getViewportSize());
//		}
//	}
	
	/**
	 * Remove a serie from the graph.
	 * If the serie doesn't exist a null exception will be thrown.
	 * @param nameSerie Is the serie's name
	 */
	public void removeSeries(String nameSerie){
		
		GraphViewSeries series = hashSeries.get(nameSerie);
		myGraphView.removeSeries(series);
		hashSeries.remove(nameSerie);
		hashCont.remove(nameSerie);
	}
	
	/**
	 * Remove all series from the graph.
	 * If the serie doesn't exist a null exception will be thrown.
	 */
	public void removeAllSeries(){
		
		Set<String> keys = hashSeries.keySet();
		for(String k: keys)
			myGraphView.removeSeries(hashSeries.get(k));
		
		hashSeries.clear();
		hashCont.clear();
	}

	/**
	 * This function add the graph to the layout in order to paint it
	 * @param layout Is the layout which will paint the graph
	 */
	public void paint(LinearLayout layout) {

		layout.addView(myGraphView);
	}

	/**
	 * Set the horizontal labels of the graph
	 * @param labels Is the array with the labels
	 */
	public void setHorizontalLabels(String[] labels) {

		myGraphView.setHorizontalLabels(labels);
	}

	/**
	 * Set the vertical labels of the graph
	 * @param labels Is the array with the labels
	 */
	public void setVerticalLabels(String[] labels) {

		myGraphView.setVerticalLabels(labels);
	}

	/**
	 * Set the viewport 
	 * @param start It indicates where the viewport starts
	 * @param size It indicates the viewport's size
	 */
	public void setViewPort(double start, double size) {

		myGraphView.setViewPort(start, size);
	}

	/**
	 * Set whether the graph is scrollable or not
	 * @param scrollable Boolean that indicates if it's scrollable or not
	 */
	public void setScrollable(boolean scrollable) {

		myGraphView.setScrollable(scrollable);
	}

	/**
	 * Set whether the graph is scalable or not
	 * @param scrollable Boolean that indicates if it's scalable or not
	 */
	public void setScalable(boolean scalable) {

		myGraphView.setScalable(scalable);
	}

	/**
	 * Set whether the graph must show the legend or not
	 * @param scrollable Boolean that indicates if shows the legend or not
	 */
	public void setShowLegend(boolean legend) {

		myGraphView.setShowLegend(legend);
	}

	/**
	 * Set the align of the legend in the graph
	 * @param legendAlign The align where the legend must be placed 
	 */
	public void setLegendAlign(LegendAlign legendAlign) {

		myGraphView.setLegendAlign(legendAlign);
	}

	/**
	 * Set the width of the legend
	 * @param size It is the width's size of the legend
	 */
	public void setLegendWidth(float size) {

		myGraphView.setLegendWidth(size);
	}

	/**
	 * Set whether the bounds of the Y axis are introduced manually or it is calculated
	 * @param manualYAxis Boolean that indicates if the bounds are introduced manually
	 */
	public void setManualYAxis(boolean manualYAxis) {
		myGraphView.setManualYAxis(manualYAxis);
	}

	/**
	 * Set the values of the Y axis' bounds
	 * @param max Is the maximum value of the bound
	 * @param min Is the minimum value of the bound
	 */
	public void setManualYAxisBounds(double max, double min) {
		myGraphView.setManualYAxisBounds(max, min);
	}
	
	/**
	 * Get the align of the legend
	 * @return The legend's align
	 */
	public LegendAlign getLegendAlign(){
		return myGraphView.getLegendAlign();
	}
	
	/**
	 * Get the width of the legend
	 * @return The legend's witdh
	 */
	public float getLegendWitdh(){
		return myGraphView.getLegendWidth();
	}
	
	/**
	 * Get the graph's style
	 * @return The graph's style
	 */
	public GraphViewStyle getGraphStyle(){
		return myGraphView.getGraphViewStyle();
	}
	
	/**
	 * Checks whether the graph is scrollable
	 * @return True in case the graph is scrollable, false otherwise
	 */
	public boolean isScrollable(){
		return myGraphView.isScrollable();
	}
	
	/**
	 * Checks whether the legend is showed
	 * @return True in case the legend is showed, false otherwise
	 */
	public boolean isShowLegend(){
		return myGraphView.isShowLegend();
	}

}
