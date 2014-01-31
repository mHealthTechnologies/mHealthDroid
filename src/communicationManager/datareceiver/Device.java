package communicationManager.datareceiver;

import java.util.ArrayList;

import communicationManager.dataStructure.ObjectData.SensorType;

import android.os.Handler;

public interface Device {

	/**
	 *  This function connects the external device with the Android device via Bluetooth
	 * @param address String which contains the external device's MAC address 
	 * @return True in case the connection is succeed, false otherwise
	 */
	public boolean connect(String address);

	/**
	 *  This function disconnects the external device from the Android device
	 */
	public boolean disconnect();

	/**
	 *  This function makes the device starts to stream
	 */
	public void startStreaming();

	/**
	 *  This function makes the device stops to stream
	 */
	public void stopStreaming();

	/**
	 *  This function sets the sample rate of the external device
	 * @param rate Is the new rate. If the device is a mobile device, this parameter
	 * should be in the range 0 and 3 (delay_fastest 0 (100 hz aprox), delay_game 1 (50 hz aprox),
	 * delay_ui 2(16,7 hz aprox) and delay_normal 3 (5 hz aprox)
	 */
	public void setRate(double rate);

	/**
	 *  This function gets the sample rate of the device
	 * @param address String which contains the external device's MAC address 
	 * @return True in case the connection is succeed, false otherwise
	 */
	public double getRate();

	/**
	 *  This function sets the handler used in the communication manager and contains in the ObjectCommunication.
	 *   This handler is who received the messages from the driver
	 * @param handler is the ObjectCommunication handler
	 */
	public void setHandlerManager(Handler handler);

	/**
	 *  This function gets the data table's name of the device
	 * @return the name of the table
	 */
	public String getTableName();

	/**
	 *  This function gets the medatada table's name of the device 
	 * @return the name of the table
	 */
	public String getMetadataTableName();

	/**
     * The function set the session's metadata. To do that first get the enabled sensors and then 
     * fill an ObjectMetada with these and some other values 
     */
	public void setMetadata();
	
	/**
	 *  This function checks if the device is connected 
	 * @return True in case the device is connected, false otherwise
	 */
	public boolean isConnected();

	/**
	 *  This function checks if the device is streaming
	 * @return True in case the device is streaming, false otherwise
	 */
	
	public boolean isStreaming();

	/**
	 *  This function gets the device's enabled sensors
	 * @return An arryList of SensorType with the enabled sensors
	 */
	
	public ArrayList<SensorType> getEnabledSensors();

	/**
	 *  This function sets the device's enabled sensors
	 * @param enabledSensors Is the list with the sensors to be enabled
	 */
	public void writeEnabledSensors(ArrayList<SensorType> enabledSensors);

	
	/**
	 * It sets the number of samples that will be store in the buffer
	 * The default value is 200. It is recommended don't use this function if you don't know what you're doing
	 * @param samples Int that indicate the number of samples to store
	 */
	public void setNumberOfSampleToStorage(int samples);
	
	/**
	 * This function gets the number of samples to store in the buffer
	 * @return the number of samples
	 */
	public int getNumberOfSamples();
	
	/**
	 * This function gets the size of the buffer
	 * @return the buffer's size
	 */
	public int getBufferSize();

}
