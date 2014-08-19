package communicationManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;

import communicationManager.dataStructure.ObjectData;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.dataStructure.ObjectMetadata;
import communicationManager.datareceiver.Device;
import communicationManager.datareceiver.DeviceMobile;
import communicationManager.datareceiver.DeviceShimmer;
import communicationManager.storage.Storage;

public class CommunicationManager extends Service {

	private static CommunicationManager instance; // Unique instance of this singleton class
	private static Hashtable<String, ObjectCommunication> devices; // This variable contains all the ObjectCommunication added
	private static Storage storage; // through this variable all databases tasks are done

	private static Handler mHandlerVisualization; //visualization manager's handler which receives the data from this manager
	private static Handler mHandlerDataProcessing; //data processing manager's handler which receives the data from this manager
	public static Handler mHandlerApp; //handler defined in the app. it receives the device's status changes from this manager
	
	public static final int DEVICE_MOBILE = 1;
	public static final int DEVICE_SHIMMER2 = 2;
	public static final int DEVICE_SHIMMER3 = 3;
	
	
	public static final int STATUS_NONE = 0;
	public static final int STATUS_CONNECTING = 1;
	public static final int STATUS_CONNECTED = 2;
	public static final int STATUS_STREAMING = 3;
	public static final int STATUS_DISCONNECTED = 4;
	
	public String label; //label for the activities


	public class ObjectCommunication {

		public Device device; // external device
		public CommunicationThread thread; // thread which handle the data received
		public boolean store; // boolean that indicates if the data must be stored
		public boolean isStoring; // boolean that indicates if this device is storing its data into the database
		public boolean monitoring; // boolean that indicates if the data must be sent to the Visualization Manager
		public boolean dataProcessing; // boolean that indicates if the data must be sent to the DataProcessing Manager
		public boolean firstSession; // boolean that indicates if it's the first session
		public ArrayList<String> nameGraphs; // List with the names of the graphs which will represent data from this device
		public boolean firstSample; //boolean that indicates if it is the first sample received
		public String macAddres; // String which stores the device's MAC address
		
		/*
		 * Constructor. Creates a new ObjectCommunication object
		 * @param d the device associated with the objectCommunication object
		 */
		public ObjectCommunication(Device d) {
			// TODO Auto-generated constructor stub
			device = d;
			thread = new CommunicationThread();
			store = true;
			isStoring = false;
			monitoring = false;
			dataProcessing = false;
			firstSession = true;
			nameGraphs = new ArrayList<String>();
		}

		/**
		 * Kill the objectCommunication thread
		 */
		public void killThread() {

			thread.interrupt();
			thread = null;
		}

	}

	/**
	 * Constructor. Initialize the variables.
	 */
	private CommunicationManager() {

		devices = new Hashtable<String, ObjectCommunication>();
		label=null;
	}

	/**
	 * Add a Shimmer device to the hashtable of devices
	 * @param myContext The UI Activity Context
	 * @param name Is the name assigned to the device
	 * @param continuosSync A boolean value defining whether received packets should be checked continuously for the correct start and end of packet.
	 * @return True in case the device is added susseccefully or false otherwise
	 */
	public boolean addDeviceShimmer(Context myContext, String name,	boolean continuosSync) {

		if (!devices.containsKey(name)) {
			ObjectCommunication oc = new ObjectCommunication(new DeviceShimmer(myContext, name, continuosSync));
			devices.put(name, oc);
			return true;
		} else
			return false;

	}

	/**
	 * Add a Mobile device to the hashtable of devices
	 * @param myContext The UI Activity Context
	 * @param name Is the name assigned to the device
	 * @return True in case the device is added susseccefully or false otherwise
	 */
	public boolean addDeviceMobile(Context myContext, String name) {

		if (!devices.containsKey(name)) {
			ObjectCommunication oc = new ObjectCommunication(new DeviceMobile(myContext, name));
			devices.put(name, oc);
			return true;
		} else
			return false;
	}
	
	/**
	 * Remove a device from the hahtable of devices
	 * @param deviceName The device's name
	 */
	public void removeDevice(String deviceName){
		
		devices.remove(deviceName);		
	}

	/**
	 * Initialize the variable Storage. It is necessary call this function in case it is wanted to do something related with database 
	 * @param myContext The UI Activity Context
	 */
	public void CreateStorage(Context context) {
		if (storage == null)
			storage = new Storage(context);
	}

	class CommunicationThread extends Thread {

		public CommunicationThread() {
			super();
		}

		public Handler myHandler;

		public void run() {
			Looper.prepare();
			myHandler = new Handler() {
				public void handleMessage(Message msg) {

					ObjectCommunication oc = null;
					try { // if the connection with the device is lost, the driver will send an String with the device's name
							// so it'll produce an exception and we handle it by inserting the last elements of the buffer in the DB
						oc = devices.get(((ObjectData) msg.obj).name);
					} catch (Exception e) {
						
						oc = devices.get((String) msg.obj);
						if(oc.store)
							storeLastInstance((String) msg.obj);
						fillMetadataRow((String) msg.obj);
						return;
					}

					if(oc.firstSample){ //when the first sample is received, the metada of this session is introduced into the database
						introduceMetadataRow(((ObjectData) msg.obj).name, oc.device.getTableName());
						oc.firstSample = false;
					}
					
					if (oc.monitoring) // if this device is been monitoring, we send the data to the VisualizationManager
						mHandlerVisualization.obtainMessage(0, msg.obj).sendToTarget();

					if (oc.dataProcessing)// if the data of the device is gonna be processed, we send them to the data processing manager
						mHandlerDataProcessing.obtainMessage(0, msg.obj).sendToTarget();

					if (oc.store) {
						//Log.d("hebra", "Cont = " + msg.arg1);
						int cont = msg.arg1 + 1;
						if (cont % oc.device.getNumberOfSamples() == 0) {
							int numSamples = oc.device.getNumberOfSamples();
							switch (msg.what) {
								case DEVICE_MOBILE:
									storage.open();
									oc.isStoring = true;
									storage.insertMobile(((DeviceMobile) oc.device).buffer, label, oc.device.getTableName(), cont, true, numSamples);
									oc.isStoring = false;
									if (!isStoring())
										storage.close();
								break;
								case DEVICE_SHIMMER2:
									storage.open();
									oc.isStoring = true;
									storage.insertShimmer2(((DeviceShimmer) oc.device).buffer, label, oc.device.getTableName(), cont, true, numSamples);
									oc.isStoring = false;
									if (!isStoring())
										storage.close();
								break;
								case DEVICE_SHIMMER3:
									storage.open();
									oc.isStoring = true;
									storage.insertShimmer3(((DeviceShimmer) oc.device).buffer, label, oc.device.getTableName(), cont, true, numSamples);
									oc.isStoring = false;
									if (!isStoring())
										storage.close();
								break;
							}
						}
					}

				}
			};

			Looper.loop();
		}

	}

	/**
	 * Method to get a device given its name
	 * @param name Is the device's name
	 * @return The device selected. If there is no device with the specified name, it will return null.
	 */
	public Device getDevice(String name) {

		Device d = null;
		if (devices.get(name) != null)
			d = devices.get(name).device;

		return d;
	}

	/**
	 * Method to get the number of devices added.
	 * @return An int with the number of devices added.
	 */
	public int getNumberOfDevice() {

		return devices.size();
	}
	
	/**
	 * Returns a set of the devices' names. 
	 * The set is backed by a Hashtable so changes to one are reflected by the other.
	 * The set does not support adding.
	 * @return a set of the keys. 
	 */
	public Set<String> setKey(){
		
		return devices.keySet();
	}

	/**
	 * Checks whether exist a device with that name
	 * @param deviceName The device's name to check
	 * @return True in case the device exists, false otherwise
	 */
	public boolean containsDevice(String deviceName){
		
		return devices.containsKey(deviceName);
	}
	
	/**
	 * Check if it is already added a Mobile device
	 * @return True in case the Mobile device is added or false otherwise
	 */
	public boolean existsMobileDevice() {

		boolean existe = false;
		Set<String> nombres = devices.keySet();
		Iterator<String> it = nombres.iterator();

		while (!existe && it.hasNext()) {

			if (devices.get(it.next()).device.getClass() == DeviceMobile.class)
				existe = true;

		}

		return existe;
	}

	/**
	 * Get an instance of the class CommunicationManager. It is necessary to call this function in order to use this manager.
	 * @return An instance of the manager
	 */
	public static CommunicationManager getInstance() {

		if (instance == null)
			instance = new CommunicationManager();

		return instance;
	}


	/**
	 * Open the database
	 */
	public void openDatabase() {
		storage.open();
	}

	/**
	 * Close the database
	 */
	public void closeDatabase() {
		storage.close();
	}

	/**
	 * This function makes that an specific device starts to stream. 
	 * If the device's name is wrong the function will trhow an exception.
	 * @param deviceName Is the name assigned to the device.
	 */
	public void startStreaming(String deviceName) {

		if (devices.get(deviceName).device.isConnected()) {
			storage.open();
			//first we create the tables needed if they don't already exist and get the Max index of the signals inserctions

			String nameTable = devices.get(deviceName).device.getTableName();
			String nameMetadataTable = devices.get(deviceName).device.getMetadataTableName();
			if (devices.get(deviceName).device.getClass() == DeviceShimmer.class) {
				DeviceShimmer ds = (DeviceShimmer) (devices.get(deviceName)).device;
				if(ds.getShimmerVersion() == 4 || ds.getShimmerVersion() == 3){ // 3 = Shimmer 3, 4 = Shimmer 3R
					storage.createShimmer3Table(nameTable);
					storage.createShimmer3TableMetadata(nameMetadataTable);
				}
				else{
					storage.createShimmer2Table(nameTable);
					storage.createShimmer2TableMetadata(nameMetadataTable);
				}
				
				storage.createShimmerTableUnits();
				if (storage.isEmptyTableShimmerUnits())
					storage.fillShimmerTableUnits();
				
			} else if (devices.get(deviceName).device.getClass() == DeviceMobile.class) {
				storage.createMobileTable(nameTable);
				storage.createMobileTableMetadata(nameMetadataTable);
				storage.createMobileTableUnits();
				if (storage.isEmptyTableMobileUnits())
					storage.fillMobileTableUnits();

			}
			if (!isStoring())
				storage.close();

			if (devices.get(deviceName).thread == null)
				devices.get(deviceName).thread = new CommunicationThread();

			devices.get(deviceName).thread.start();
			// we have to wait until the thread starts, otherwise there will be
			// a nullPointerException because of the thread's Handler
			try {
				synchronized (this) {
					wait(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (devices.get(deviceName).firstSession) {
				devices.get(deviceName).firstSession = false;
				consistencyTestMetadata(nameMetadataTable, nameTable);
			}

			devices.get(deviceName).device.setHandlerManager(devices.get(deviceName).thread.myHandler);
			devices.get(deviceName).device.startStreaming();
//			introduceMetadataRow(deviceName, nameTable);
			devices.get(deviceName).firstSample = true;
		}
	}

	/**
	 * This function makes that a specific device stop to stream
	 * If the device's name is wrong the function will trhow an exception
	 * @param name Is the name assigned to the device
	 */
	public void stopStreaming(String deviceName) {

		devices.get(deviceName).device.stopStreaming();	
		storeLastInstance(deviceName);
		fillMetadataRow(deviceName);
	}

	/**
	 * This function makes a test of consistency to the database.
	 * @param nameMetadataTable Is the name of the metadata table to modify
	 * @param nameTable Is the name of the table to be consulted
	 */
	private void consistencyTestMetadata(String nameMetadataTable, String nameTable) {

		storage.open();
		storage.consistencyTestMetadata(nameMetadataTable, nameTable);

		if (!isStoring())
			storage.close();
	}

	/**
	 * It introduces the metadata's session into metadata table
	 * @param deviceName Is the name assigned to the device
	 * @param nameTable Is the name of the metadata table
	 */
	private void introduceMetadataRow(String deviceName, String nameTable) {

		storage.open();
		int i = storage.getMaxIndex(nameTable);
		i++;
		if (devices.get(deviceName).device.getClass() == DeviceShimmer.class) {
			ObjectMetadata mt = ((DeviceShimmer) devices.get(deviceName).device).metadata;
			mt.firstIndex = i;
			// Last index and finish time should be empty till the streaming
			// stop, when will be updated
			mt.lastIndex = 0;
			mt.finish = new Time();
			DeviceShimmer ds = (DeviceShimmer) (devices.get(deviceName)).device;
			if(ds.getShimmerVersion() == 4 || ds.getShimmerVersion() == 3) // 3 = Shimmer 3, 4 = Shimmer 3R
				storage.insertShimmer3Metadata(mt, devices.get(deviceName).device.getMetadataTableName());
			else
				storage.insertShimmer2Metadata(mt, devices.get(deviceName).device.getMetadataTableName());
		}
		if (devices.get(deviceName).device.getClass() == DeviceMobile.class) {
			ObjectMetadata mt = ((DeviceMobile) devices.get(deviceName).device).metadata;
			mt.firstIndex = i;
			// Last index and finish time should be empty till the streaming
			// stop, when will be updated
			mt.lastIndex = 0;
			mt.finish = new Time();
			storage.insertMobileMetadata(mt, devices.get(deviceName).device.getMetadataTableName());
		}

		if (!isStoring())
			storage.close();

	}

	/**
	 * It introduces session's remaining metadata in its table
	 * @param deviceName Is the name assigned to the device
	 */
	private void fillMetadataRow(String deviceName) {

		storage.open();
		ObjectMetadata mt = null;
		String nameTable = devices.get(deviceName).device.getTableName();
		int i = storage.getMaxIndex(nameTable);

		if (devices.get(deviceName).device.getClass() == DeviceShimmer.class) {
			((DeviceShimmer) devices.get(deviceName).device).metadata.lastIndex = i;
			mt = ((DeviceShimmer) devices.get(deviceName).device).metadata;
		}
		if (devices.get(deviceName).device.getClass() == DeviceMobile.class) {
			// LastIndex get updated. With finish time happens the same but in
			// stopStreaming of Device
			((DeviceMobile) devices.get(deviceName).device).metadata.lastIndex = i;
			mt = ((DeviceMobile) devices.get(deviceName).device).metadata;
		}

		storage.fillMetadataRow(mt, devices.get(deviceName).device.getMetadataTableName());

		if (!isStoring())
			storage.close();
	}

	/**
	 * It sets the device's sample rate
	 * If the device's name is wrong the function will throw a null exception.
	 * @param deviceName Is the name assigned to the device
	 * @param rate Is the new rate. If the device is a mobile device, this parameter
	 * should be in the range 0 and 3 (delay_fastest 0 (100 hz aprox), delay_game 1 (50 hz aprox),
	 * delay_ui 2(16,7 hz aprox) and delay_normal 3 (5 hz aprox)
	 */
	public void setRate(String deviceName, double rate) {

		devices.get(deviceName).device.setRate(rate);
	}

	/**
	 * It gets the device's sample rate
	 * If the device's name is wrong the function will throw a null exception.
	 * @param deviceName Is the name assigned to the device
	 * @return A double with the sample rate
	 */
	public double getRate(String deviceName) {

		return devices.get(deviceName).device.getRate();
	}

	/**
	 * It introduce into database the last set of samples when the device stop to streaming
	 * @param deviceName Is the name assigned to the device
	 */
	private void storeLastInstance(String deviceName) {

		devices.get(deviceName).thread.myHandler.getLooper().quit();
		devices.get(deviceName).killThread();
		ObjectCommunication oc = devices.get(deviceName);
		if (oc.store) { // if we are storing the data
			storage.open();
			oc.isStoring = true;
			int numSamples = oc.device.getNumberOfSamples();
			String nameTable = oc.device.getTableName();
			if (oc.device.getClass() == DeviceShimmer.class) {
				int cont = ((DeviceShimmer) oc.device).contBuffer + 1;
				DeviceShimmer ds = (DeviceShimmer) (devices.get(deviceName)).device;
				if(ds.getShimmerVersion() == 4 || ds.getShimmerVersion() == 3){ // 3 = Shimmer 3, 4 = Shimmer 3R
					if (cont % numSamples == 0) // we check if the buffer is filled
						storage.insertShimmer3(((DeviceShimmer) oc.device).buffer, label, nameTable, cont, true, numSamples);
					else
						storage.insertShimmer3(((DeviceShimmer) oc.device).buffer, label, nameTable, cont, false, numSamples);
				}
				else{
					if (cont % numSamples == 0) // we check if the buffer is filled
						storage.insertShimmer2(((DeviceShimmer) oc.device).buffer, label, nameTable, cont, true, numSamples);
					else
						storage.insertShimmer2(((DeviceShimmer) oc.device).buffer, label, nameTable, cont, false, numSamples);
				}
			}

			if (oc.device.getClass() == DeviceMobile.class) {
				int cont = ((DeviceMobile) oc.device).contBuffer + 1;
				if (cont % numSamples == 0) // checking if the buffer is filled
					// without cont-1 it inserts an empty row
					storage.insertMobile(((DeviceMobile) oc.device).buffer, label, oc.device.getTableName(), cont - 1, true, numSamples);
				else
					storage.insertMobile(((DeviceMobile) oc.device).buffer, label, oc.device.getTableName(), cont - 1, false, numSamples);
			}

			oc.isStoring = false;
			if (!isStoring())
				storage.close();
			
		}
	}

	/**
	 * It connects the external device with the Android device via Bluetooth
	 * If the device's name is wrong the function will throw a null exception.
	 * @param deviceName Is the name assigned to the device
	 * @param adress Is the device's MAC adress 
	 */
	public void connect(String deviceName, String address) {

		devices.get(deviceName).device.connect(address);
		devices.get(deviceName).macAddres = address;
	}
	
	public void connect(String deviceName){
		
		devices.get(deviceName).device.connect(devices.get(deviceName).macAddres);
	}
	
	public void disconnect(String deviceName){
		
		 devices.get(deviceName).device.disconnect();
	}

	/**
	 * It sets the device's enabled sensors
	 * If the device's name is wrong the function will throw a null exception.
	 * If the device don't support the sensors this function won't have effect
	 * @param deviceName Is the name assigned to the device
	 * @param enabledSensors Is an arrayList with a list of the sensors
	 */
	public void setEnabledSensors(String deviceName, ArrayList<SensorType> enabledSensors) {

		devices.get(deviceName).device.writeEnabledSensors(enabledSensors);
	}
	
	/**
	 * It gets the enabled sensors of an specific device
	 * @param deviceName The device's name
	 * @return A SensorType ArrayList with the enabled sensors 
	 */
	public ArrayList<SensorType> getEnabledSensors(String deviceName){
		
		return devices.get(deviceName).device.getEnabledSensors();
	}

	/**
	 * It gets an ObjectCommunication object
	 * If the device's name is wrong the function will throw a null exception.
	 * @param deviceName Is the name assigned to the device
	 * @return An ObjectCommuniction
	 */
	public ObjectCommunication getObjectCommunication(String deviceName) {

		return devices.get(deviceName);
	}

	/**
	 * It sets the Handler of the Visualization Manager
	 * Do NOT use this function if you don't know what you're doing
	 * @param mHandler The handler of the Visualization Manager
	 */
	public void setHandlerVisualization(Handler mHandler) {

		if (mHandlerVisualization == null)
			CommunicationManager.mHandlerVisualization = mHandler;
	}

	/**
	 * It sets the Handler of the Data Processing Manager
	 * Do NOT use this function if you don't know what you're doing
	 * @param mHandler The handler of the Data Processing Manager
	 */
	public void setHandlerDataProcessing(Handler mHandler) {

		if (mHandlerDataProcessing == null)
			CommunicationManager.mHandlerDataProcessing = mHandler;
	}
	
	/**
	 * It sets the Handler defined in the Android application.
	 * This function will send a message to this handler when 
	 * a device's status change is produced. The status is store in
	 * the message's variable "what". The possible values for "what" are the following ones:
	 * 0 -> Status = None,
	 * 1 -> Status = Connecting,
	 * 2 -> Status = Connected,
	 * 3 -> Status = Streaming,
	 * 4 -> Status = Disconnected.
	 * The device's name is sending in varible "obj" of the message
	 * @param mHandler The handler of the Android application
	 */
	public void setHandlerApp(Handler mHandler) {

		if (mHandlerApp == null)
			CommunicationManager.mHandlerApp = mHandler;
	}

	/**
	 * It sets whether the data received by the devices will be store
	 * If the device's name doesn't exist the function will throw a null exception
	 * @param deviceName  Is the name assigned to the device
	 * @param store It will be true in case the data wants to be store, false otherwise
	 */
	public void setStoreData(String deviceName, boolean store) {

		devices.get(deviceName).store = store;
	}
	
	/**
	 * This functions consults if the received data of the device is stored into the database
	 * @param deviceName The device's name
	 * @return True in case data is stored. False otherwise,
	 */
	public boolean getStoreData(String deviceName){
		return devices.get(deviceName).store;
	}

	/**
	 * It checks if there is any device storing data
	 * @return True if some device is storing data, false otherwise
	 */
	public boolean isStoring() {

		boolean storing = false;

		for (ObjectCommunication oc : devices.values()) {
			storing = storing || oc.isStoring;
		}

		return storing;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * It sets the vaiable label
	 * @param label String with the label
	 */
	public void setLabel(String label){
		this.label = label;
	}
	
	/**
	 * It creates the table User_Profile
	 */
	public void createUsersTable(){
		
		storage.createUserProfileTable();
	}
	
	/**
	 * Query that checks in the database if the login exist
	 * @param login String with the login to be checked
	 * @return True in case the login exist, false otherwise
	 */
	public boolean existsLogin(String login){
		
		return storage.existsLogin(login);
	}
	
	/**
	 * Query that checks if the password is right for a login given
	 * @param login String which contains the login
	 * @param password String which contains the password to be checked
	 * @return True in case the password is right, false otherwise
	 */
	public boolean checkPassword(String login, String password){
		
		return storage.checkPassword(login, password);
	}
	
	/**
	 * Statement that introduces a new user profile into the table User_Profile
	 * The login is a primary key
	 * @param login String which contains the user's login
	 * @param password String which contains the user's password
	 * @param age Int which contains the user's age
	 * @param sex String which contains the user's sex
	 * @param weight Float which contains the user's weight
	 * @param height Float which contains the user's height
	 * @param email String which contains the user's email
	 */
	public void addUserProfile(String login, String password, int age, String sex, float weight, float height, String email){
		
		storage.insertUserProfile(login, password, sex, age, weight, height, email);
	}
	
	/**
	 * Query that gets the password for a login given
	 * @param login String which contains the login
	 * @return String which contains the password
	 */
	public String getPassword(String login){
		
		return storage.getPassword(login);
	}
	
	/**
	 * Query that gets the sex for a login given
	 * @param login String which contains the login
	 * @return String which contains the sex
	 */
	public String getSex(String login){
		
		return storage.getSex(login);
	}
	
	/**
	 * Query that gets the age for a login given
	 * @param login String which contains the login
	 * @return Int which contains the age
	 */
	public int getAge(String login){
		
		return storage.getAge(login);
	}
	
	/**
	 * Query that gets the height for a login given
	 * @param login String which contains the login
	 * @return Float which contains the height
	 */
	public float getHeight(String login){
		
		return storage.getHeight(login);
	}
	
	/**
	 * Query that gets the weight for a login given
	 * @param login String which contains the login
	 * @return Float which contains the weight
	 */
	public float getWeight(String login){
		
		return storage.getWeight(login);
	}
	
	/**
	 * Query that gets the email for a login given
	 * @param login String which contains the login
	 * @return String which contains the email
	 */
	public String getEmail(String login){
		
		return storage.getEmail(login);
	}
	
	/**
	 * Query that gets the password for an email given
	 * @param email String which contains the email
	 * @return String which contains the password
	 */
	public String getPasswordByEmail(String email){
		
		return storage.getPasswordByEmail(email);
	}
	
	/**
	 * Query that gets the login for an email given
	 * @param email String which contains the email
	 * @return String which contains the login
	 */
	public String getLoginByEmail(String email){
		
		return storage.getLoginByEmail(email);
	}
	
	/**
	 * Statement that sets the password for a login given
	 * @param login String which contains the login
	 * @param newPassword String which contains the new password
	 */
	public void setPassword(String login, String newPassword){
		
		storage.setPassword(login, newPassword);
	}
	
	/**
	 *  Statement that sets the sex for a login given
	 * @param login String which contains the login
	 * @param newSex String which contains the new sex
	 */
	public void setSex(String login, String newSex){
		
		storage.setSex(login, newSex);
	}
	
	/**
	 *  Statement that sets the age for a login given
	 * @param login String which contains the login
	 * @param newAge Int which contains the new age
	 */
	public void setAge(String login, int newAge){
		
		storage.setAge(login, newAge);
	}
	
	/**
	 *  Statement that sets the height for a login given
	 * @param login String which contains the login
	 * @param newHeight Float which contains the new height
	 */
	public void setHeight(String login, float newHeight){
		
		storage.setHeight(login, newHeight);
	}
	
	/**
	 *  Statement that sets the weight for a login given
	 * @param login String which contains the login
	 * @param newWeight Float which contains the new weight
	 */
	public void setWeight(String login, float newWeight){
		
		storage.setWeight(login, newWeight);
	}
	
	/**
	 *  Statement that sets the email for a login given
	 * @param login String which contains the login
	 * @param newEmail String which contains the new email
	 */
	public void setEmail(String login, String newEmail){
		
		storage.setEmail(login, newEmail);
	}
	
	/**
	 * This function sets the number of samples that will be storage in the device's buffer
	 * @param deviceName The device's name
	 * @param samples The number of samples to be stored
	 */
	public void  setNumberOfSampleToStorage(String deviceName, int samples){
		
		devices.get(deviceName).device.setNumberOfSampleToStorage(samples);
	}
	
	/**
	 * This function gets the number of samples stored in the buffer for a specific device
	 * @param deviceName The device's name
	 * @return The number of the samples stored
	 */
	public int getNumberOfSamples(String deviceName){
		
		return devices.get(deviceName).device.getNumberOfSamples();
	}
	
	/**
	 * This function gets the size of the buffer of a specific device
	 * @param deviceName The device's name
	 * @return The buffer's size
	 */
	public int getBufferSize(String deviceName){
		
		return devices.get(deviceName).device.getBufferSize();
	}
	
	/**
	 * This function returns a hash with the state of every existing device
	 * @return a hashtable with the device name as key and an integer as value. 
	 * Integer values. 0: disconnected 1: connected but not streaming 2: connected
	 *  and streaming
	 */
	public Hashtable<String, Integer> getCurrentDevicesState(){
		
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		boolean connected, streaming = false;
		Set<String> keys = devices.keySet();
		
		for (String k : keys) {
			connected = false;
			streaming = false;
			connected = devices.get(k).device.isConnected();
//			String mac = devices.get(k).device.getTableName();
			if(connected){
				streaming = devices.get(k).device.isStreaming();
				if(streaming)
					hash.put(k, 2);			
				else
					hash.put(k, 1);
			}
			else
				hash.put(k, 0);
		}
		
		return hash;
	}
	
	/**
	 * This function checks whether or not a device is connected
	 * @param deviceName The device's name
	 * @return True in case the device is connected. False otherwise. 
	 */
	public boolean isConnected(String deviceName){
		
		return devices.get(deviceName).device.isConnected();
	}
	
	/**
	 * This function checks whehter or not a device is streaming
	 * @param deviceName The device's name
	 * @return True in case the device is streaming. False otherwise. 
	 */
	public boolean isStreaming(String deviceName){
		
		return devices.get(deviceName).device.isStreaming();
	}
	
	/**
	 * This function sets the device's MAC address
	 * @param deviceName The device's name
	 * @param macAddress The device's MAC address
	 */
	public void setMacAddres(String deviceName, String macAddress){
		
		devices.get(deviceName).macAddres = macAddress;
	}
}
