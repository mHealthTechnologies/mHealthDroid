package communicationManager.dataStructure;

import java.util.Hashtable;
import java.util.Set;

import android.text.format.Time;

import communicationManager.dataStructure.ObjectData.SensorType;

public class ObjectMetadata {

	public Hashtable<SensorType, Metadata> hashMetadata;
	public FormatType format;
	public Time start;
	public Time finish;
	public int firstIndex;
	public int lastIndex;
	public double rate;

	public enum FormatType {
		
		CALIBRATED, UNCALIBRATED;
	}

	/**
	 * Constructor. Creates a new ObjectMetadata object
	 * @param hashMetadata hashMetadata to insert in the new object
	 * @param format ObjectMetadata format
	 */
	public ObjectMetadata(Hashtable<SensorType, Metadata> hashMetadata, FormatType format) {
		
		super();
		this.hashMetadata = hashMetadata;
		this.format = format;
		start = new Time(Time.getCurrentTimezone());
		finish = new Time(Time.getCurrentTimezone());
	}

	/**
	 * Constructor. Creates a new ObjectMetadata object
	 */
	public ObjectMetadata() {
		
		super();
		hashMetadata = new Hashtable<ObjectData.SensorType, Metadata>();
		start = new Time(Time.getCurrentTimezone());
		finish = new Time(Time.getCurrentTimezone());
	}

	/**
	 * Constructor. Creates a new ObjectMetadata as copy of other ObjectMetadata 
	 * @param om ObjectMetadata which will be copied
	 */
	public ObjectMetadata(ObjectMetadata om) {

		Set<SensorType> set = om.hashMetadata.keySet();
		for (SensorType sensor : set) {
			Metadata mt = new Metadata(om.hashMetadata.get(sensor).units);
			this.hashMetadata.put(sensor, mt);
		}
		
		this.format = om.format;
		this.start = new Time(om.start);
		this.finish = new Time(om.finish);
		this.firstIndex = om.firstIndex;
		this.lastIndex = om.lastIndex;
		this.rate = om.rate;
	}

	/**
	 * Method to convert the Start attribute (Time type) to a string
	 * @return a string representing the Start time
	 */
	public String startToString() {

		String s = "";
		String minute = "";
		String second = "";
		if (start.minute < 10)
			minute += "0" + start.minute;
		else
			minute += start.minute;

		if (start.second < 10)
			second += "0" + start.second;
		else
			second += start.second;

		// Month value is in the interval [0-11], thats why its necessary to add one
		int month = start.month + 1;
		s += start.hour + ":" + minute + ":" + second + " -- " + start.year
				+ "/" + month + "/" + start.monthDay;
		return s;
	}

	/**
	 * Method to convert the Finish attribute (Time type) to a string
	 * @return a string representing the Finish time
	 */
	public String finishToString() {

		String s = "";
		String minute = "";
		String second = "";
		if (finish.minute < 10)
			minute += "0" + finish.minute;
		else
			minute += finish.minute;

		if (finish.second < 10)
			second += "0" + finish.second;
		else
			second += finish.second;

		// Month value is in the interval [0-11], thats why its necessary to add one
		int month = finish.month + 1;
		s += finish.hour + ":" + minute + ":" + second + " -- " + finish.year
				+ "/" + month + "/" + finish.monthDay;
		return s;
	}
}
