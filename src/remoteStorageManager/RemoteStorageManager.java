package remoteStorageManager;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;
import communicationManager.storage.DBAdapter;
import communicationManager.storage.Storage;
import utilities.Utilities;;

public class RemoteStorageManager {

	private static RemoteStorageManager instance; //unique instance of this singleton class
	private static Storage storage; //variable thorough databases tasks are done
	String serverIP; //IP address where the server is hosted
	private CommunicationManager cm; //unique instance of communicationManager class
	RemoteStorageThread thread; //thread to upload data
	InputStream is; 
	String tableNameMobileUnits;
	String tableNameShimmerUnits;
	String tableNameSignals;
	String tableNameMetadata;
	int MOBILE_DEVICE = 0;
	int SHIMMER_DEVICE = 1;
	String mobileUnitsPath; //mobile units script path
	String shimmerUnitsPath; //shimmer units script path
	String mobileSignalsPath; //mobile signals script path
	String shimmerSignalsPath; //shimmer signals script path
	String mobileMetadataPath; //mobile metadata script path
	String shimmerMetadataPath; //shimmer metadata script path
	String lastIDPath; //last ID script path
	int tablesToUpload = 0;
	int tablesUploaded = 0;
	
	/**
	 * Constructor. Creates a new serverManager object
	 */
	public RemoteStorageManager() {

		thread = new RemoteStorageThread();
		thread.start();
		is = null;
		tableNameMobileUnits = "";
		tableNameShimmerUnits = "";
		tableNameSignals = "";
		tableNameMetadata = "";
		cm = CommunicationManager.getInstance();
		
		//Scripts paths
		mobileUnitsPath = "insert_mobile_units.php";
		shimmerUnitsPath = "insert_shimmer_units.php";
		mobileSignalsPath = "insert_mobile_signals.php";
		shimmerSignalsPath = "insert_shimmer_signals.php";
		mobileMetadataPath = "insert_mobile_metadata.php";
		shimmerMetadataPath = "insert_shimmer_metadata.php";
		lastIDPath = "get_last_ID.php";
	}

	/**
	 * Method to get/create the unique ServerManager instance
	 * @return serverManager instance
	 */
	public static RemoteStorageManager getInstance() {

		if (instance == null)
			instance = new RemoteStorageManager();

		return instance;
	}

	/**
	 * Method to create a storage instance if this does not exists already 
	 * @param context: current IU application context
	 */
	public static void CreateStorage(Context context) {
		if (storage == null)
			storage = new Storage(context);
	}
	
	/**
	 * Method to set the IP address where the server is hosted 
	 * @param addressIP: server address. Example: http://192.168.1.1
	 */
	public void setServerIP(String addressIP){
		
		this.serverIP = addressIP;	
	}

	/**
	 * Method to upload a unit table to the server (Shimmer or Mobile device indistinctly)
	 * @param deviceName device name which units table wants to be uploaded
	 */
	public void uploadUnitsTable(String deviceName) {
		
		//Mobile Device
		if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceMobile.class) {

			//One table more to be uploaded
			tablesToUpload++;
			
			//Data acquisition
			storage.open();
			this.tableNameMobileUnits = DBAdapter.TABLE_MOBILE_UNITS;
			ArrayList<Pair<String, String>> data = storage.getMobileUnitsTable(tableNameMobileUnits);

			if (!cm.isStoring())
				storage.close();
			
			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(0);
			messageToSend.obj = data;
			thread.myHandler.sendMessage(messageToSend);
		}

		//Shimmer Device
		else if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceShimmer.class) {

			//One table more to be uploaded
			tablesToUpload++;

			//Data acquisition
			storage.open();
			this.tableNameShimmerUnits = DBAdapter.TABLE_SHIMMER_UNITS;
			// Different way to proceed, there are calibrated and uncalibrated data, so its necessary an array
			ArrayList<ArrayList<String>> data = storage.getShimmerUnitsTable(tableNameShimmerUnits);

			if (!cm.isStoring())
				storage.close();

			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(1);
			messageToSend.obj = data;
			thread.myHandler.sendMessage(messageToSend);
		}
	}

	/**
	 * Method to upload a signal table to the server (Shimmer or Mobile device indistinctly) 
	 * @param deviceName: device name which units table wants to be uploaded
	 */
	public void uploadSignalsTable(String deviceName) {

		//Mobile Device
		if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceMobile.class) {

			//One table more to be uploaded
			tablesToUpload++;
			
			//Data acquisition
			storage.open();
			String tableName = cm.getDevice(deviceName).getTableName();
			this.tableNameSignals = tableName;
			Hashtable<SensorType, ArrayList<Double>> data = storage
					.getMobileSignalsTable(tableNameSignals);
			if (!cm.isStoring())
				storage.close();

			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(2);
			messageToSend.obj = data;
			messageToSend.arg1 = MOBILE_DEVICE;
			thread.myHandler.sendMessage(messageToSend);
		}

		//Shimmer Device
		else if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceShimmer.class) {

			//One table more to be uploaded
			tablesToUpload++;
			
			//Data acquisition
			storage.open();
			String tableName = cm.getDevice(deviceName).getTableName();
			this.tableNameSignals = tableName;
			Hashtable<SensorType, ArrayList<Double>> data = storage
					.getShimmerSignalsTable(tableNameSignals);
			if (!cm.isStoring())
				storage.close();

			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(2);
			messageToSend.obj = data;
			messageToSend.arg1 = SHIMMER_DEVICE;
			thread.myHandler.sendMessage(messageToSend);
		}
	}

	/**
	 * Method to upload a metadata table to the server (Shimmer or Mobile device indistinctly)
	 * @param deviceName device name which units table wants to be uploaded
	 */
	public void uploadMetadataTable(String deviceName) {

		//Mobile Device
		if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceMobile.class) {

			//One table more to be uploaded
			tablesToUpload++;
			
			//Data acquisition
			storage.open();
			String tableName = cm.getDevice(deviceName).getMetadataTableName();
			this.tableNameMetadata = tableName;
			ArrayList<Hashtable<String, String>> data = storage
					.getMobileMetadataTable(tableNameMetadata);
			if (!cm.isStoring())
				storage.close();

			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(3);
			messageToSend.obj = data;
			messageToSend.arg1 = MOBILE_DEVICE;
			thread.myHandler.sendMessage(messageToSend);
		}

		//Shimmer Device
		else if (cm.getDevice(deviceName).getClass() == communicationManager.datareceiver.DeviceShimmer.class) {

			//One table more to be uploaded
			tablesToUpload++;
			
			//Data acquisition
			storage.open();
			String tableName = cm.getDevice(deviceName).getMetadataTableName();
			this.tableNameMetadata = tableName;
			ArrayList<Hashtable<String, String>> data = storage
					.getShimmerMetadataTable(tableNameMetadata);
			if (!cm.isStoring())
				storage.close();

			//Message sent to serverThread
			Message messageToSend = thread.myHandler.obtainMessage(3);
			messageToSend.obj = data;
			messageToSend.arg1 = SHIMMER_DEVICE;
			thread.myHandler.sendMessage(messageToSend);
		}

	}

	class RemoteStorageThread extends Thread {

		/**
		 * Constructor. Creates a new serverThread object
		 */
		public RemoteStorageThread() {

			super();
		}

		public Handler myHandler;
		
		public void run() {
			Looper.prepare();
			
			myHandler = new Handler() {
				public void handleMessage(Message msg) {

					String url = "";

					switch (msg.what) {

					case 0: // Mobile Units Table

						//Acquisition of data and script path inside the server
						ArrayList<Pair<String, String>> data = (ArrayList<Pair<String, String>>) msg.obj;
						url = serverIP + "/" + mobileUnitsPath;
						Log.d("Server", "Path es " + url + " TableName es " + tableNameMobileUnits);

						try {
							
							//Http client and httpPost necessary to do the remittance of data to the server
							DefaultHttpClient httpClient = new DefaultHttpClient();
							HttpPost httpPost = new HttpPost(url);
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("TableName", tableNameMobileUnits));
							
							//Creation of the JSON Object which is sent and two JSON Arrays, one for sensors name and 
							//another for calibrated formats		 
							JSONObject json = new JSONObject();
							JSONArray sensors = new JSONArray();
							JSONArray calibrated = new JSONArray();
							
							//Loop to fill JSON object with the data acquisited
							for (int i = 0; i < data.size(); i++) {
								Pair<String, String> pair = data.get(i);
								sensors.put(pair.first);
								calibrated.put(pair.second);							
							}

							//Adding both JSON Array to the JSON object
							json.put("Sensors", sensors);
							json.put("Calibrated", calibrated);
										
							String jsonString = json.toString();
							Log.d("JSON", jsonString);
							nameValuePairs.add(new BasicNameValuePair("Json", jsonString));

							//Unifying nameValuePairs with the httpPost method and execution
							httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							HttpResponse httpResponse = httpClient.execute(httpPost);
							//httpResponse.getEntity().consumeContent();

							//To log the result using JSON
							String jsonResult =  Utilities.inputStreamToString(httpResponse.getEntity().getContent()).toString();	
							//logStatusAndJSON(jsonResult);
							//One table uploaded more
							tablesUploaded++;
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						break;

					case 1: // Shimmer Units Table

						//Acquisition of data and script path inside the server
						ArrayList<ArrayList<String>> data1 = (ArrayList<ArrayList<String>>) msg.obj;
						url = serverIP + "/" + shimmerUnitsPath;
						Log.d("JSON", "URL ES " + url);
						try {
							
							//Http client and httpPost necessary to do the remittance of data to the server
							DefaultHttpClient httpClient = new DefaultHttpClient();
							HttpPost httpPost = new HttpPost(url);
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("TableName", tableNameShimmerUnits));
							
							Log.d("Server", "Path es " + url + " TableName es " + tableNameShimmerUnits);

							//Creation of the JSON Object which is sent and three JSON Arrays, one for sensors name and 
							//anothers for calibrated and uncalibrated formats
							JSONObject json = new JSONObject();
							JSONArray sensors = new JSONArray();
							JSONArray calibrated = new JSONArray();
							JSONArray uncalibrated = new JSONArray();
									
							//Loop to fill JSON object with the data acquisited
							for (int i = 0; i < data1.size(); i++) {
								
								ArrayList<String> aux = data1.get(i);
								sensors.put(aux.get(0));
								calibrated.put(aux.get(1));
								uncalibrated.put(aux.get(2));
								
							}
							
							//Adding both JSON Array to the JSON object
							json.put("Sensors", sensors);
							json.put("Calibrated", calibrated);
							json.put("Uncalibrated", uncalibrated);
						
							String jsonString = json.toString();
							Log.d("JSON", jsonString);
							nameValuePairs.add(new BasicNameValuePair("Json", jsonString));
							
							//Unifying nameValuePairs with the httpPost method and execution	
							httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							HttpResponse httpResponse = httpClient.execute(httpPost);

							// To log the result using JSON
							String jsonResult = Utilities.inputStreamToString(httpResponse.getEntity().getContent()).toString();
							//logStatusAndJSON(jsonResult);

							//One table uploaded more
							tablesUploaded++;
							
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						break;

					case 2: // Mobile&Shimmer Signals Table

						//Acquisition of data and script path inside the server
						Hashtable<SensorType, ArrayList<Double>> data2 = (Hashtable<SensorType, ArrayList<Double>>) msg.obj;

						if (msg.arg1 == MOBILE_DEVICE)
							url = serverIP + "/" + mobileSignalsPath;

						else if (msg.arg1 == SHIMMER_DEVICE)
							url = serverIP + "/" + shimmerSignalsPath;

						try {

							DefaultHttpClient httpClient = new DefaultHttpClient();

							// First its necessary to check the last ID that was uploaded (if there is any)
							String urlGetID = serverIP + "/" + lastIDPath;
							
							//Http client and httpPost necessary to do the remittance of data to the server
							HttpPost httpPost = new HttpPost(urlGetID);
							List<NameValuePair> table = new ArrayList<NameValuePair>(1);
							table.add(new BasicNameValuePair("TableName", tableNameSignals));
							httpPost.setEntity(new UrlEncodedFormEntity(table));
							HttpResponse httpResponse = httpClient.execute(httpPost);
							String jsonResult = Utilities.inputStreamToString(httpResponse.getEntity().getContent()).toString();
							JSONObject json = new JSONObject(jsonResult);
							
							//Last ID existing in the table database
							int lastID = Integer.parseInt(json.getString("LastID"));
							
							HttpPost httpPost2 = new HttpPost(url);
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("TableName", tableNameSignals));
							
							//Creation of the JSON Object which is sent
							JSONObject json2 = new JSONObject();
							Set<SensorType> signals = data2.keySet();
								
							for(SensorType s: signals){
								
								JSONArray aux = new JSONArray();
								
								 //The loop goes from the last ID in the server(mysql) to the last ID in local
								// database(sqlite)
								for(int i = lastID; i < data2.get(s).size(); i++){
									
									if(data2.get(s).get(i).isNaN())
										aux.put(null);
									
									else
										aux.put(data2.get(s).get(i));
								}
								
								json2.put(s.getAbbreviature(), aux);
							}
							
							String jsonString = json2.toString();
							Log.d("JSON", jsonString);
							nameValuePairs.add(new BasicNameValuePair("Json", jsonString));
							
							//Unifying nameValuePairs with the httpPost method and execution	
							httpPost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							HttpResponse httpResponse2 = httpClient.execute(httpPost2);

							// To log the result using JSON
							String jsonResult2 = Utilities.inputStreamToString(httpResponse2.getEntity().getContent()).toString();
							//logStatusAndJSON(jsonResult2);

							//One table uploaded more
							tablesUploaded++;
							
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						break;

					case 3: // Mobile&Shimmer Metadata Table

						//Acquisition of data and script path inside the server
						ArrayList<Hashtable<String, String>> data3 = (ArrayList<Hashtable<String, String>>) msg.obj;

						if (msg.arg1 == MOBILE_DEVICE)
							url = serverIP + "/" + mobileMetadataPath;
							
						else if (msg.arg1 == SHIMMER_DEVICE)
							url = serverIP + "/" + shimmerMetadataPath;

						try {

							DefaultHttpClient httpClient = new DefaultHttpClient();

							// First its necessary to check the last ID that was uploaded (if there is any)
							String urlGetID = serverIP + "/" + lastIDPath;
							
							HttpPost httpPost = new HttpPost(urlGetID);
							List<NameValuePair> table = new ArrayList<NameValuePair>(1);
							table.add(new BasicNameValuePair("TableName", tableNameMetadata));
							httpPost.setEntity(new UrlEncodedFormEntity(table));
							HttpResponse httpResponse = httpClient.execute(httpPost);
							String jsonResult = Utilities.inputStreamToString(httpResponse.getEntity().getContent()).toString();
							JSONObject json = new JSONObject(jsonResult);
							
							//Last ID existing in the table database
							int lastID = Integer.parseInt(json.getString("LastID"));
							
							HttpPost httpPost2 = new HttpPost(url);
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("TableName", tableNameMetadata));
							
							//Creation of the JSON Object which is sent
							JSONObject json2 = new JSONObject();
							Set<String> columns = data3.get(0).keySet();
							
							for (String c : columns){
								
								JSONArray array = new JSONArray();
								
								// The loop goes from the last ID in the server(mysql) to the last ID
								//in local database(sqlite)
								for(int i = lastID; i < data3.size(); i++)
									
									array.put(data3.get(i).get(c));
									
								json2.put(c, array);
								
							}
							
							String jsonString = json2.toString();
							Log.d("JSON", jsonString);
							nameValuePairs.add(new BasicNameValuePair("Json", jsonString));

							//Unifying nameValuePairs with the httpPost method and execution	
							httpPost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));			
							HttpResponse httpResponse2 = httpClient.execute(httpPost2);

							// To log the result using JSON
							jsonResult = Utilities.inputStreamToString(httpResponse2.getEntity().getContent()).toString();
							//logStatusAndJSON(httpResponse2.getStatusLine(),jsonResult);																	

							//One table uploaded more
							tablesUploaded++;
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();						
						}

						break;
					}
				}
			};

			Looper.loop();
		}
	}
	
	
	/**
	 * Helper method to log a JSON sent by the server with information about the uploading
	 * @param jsonResult: the string corresponding to the JSON object sent
	 */
	private void logStatusAndJSON(String jsonResult) throws JSONException {

		Log.d("Server", "jsonResult: " + jsonResult);
		JSONObject jsonObject;
		jsonObject = new JSONObject(jsonResult);
		String success = jsonObject.getString("Success");
		String message = jsonObject.getString("Message");
		String rows = jsonObject.getString("Rows");
		Log.d("Server", "Success: " + success);
		Log.d("Server", "Message: " + message);
		Log.d("Server", "Rows uploaded: " + rows);	
	}
	
	/**
	 * Method to finish the uploading process 
	 */
	public void finishProcess() {

		thread.myHandler.getLooper().quit();
		thread.interrupt();
	}
	
	/**
	 * Method to set the path for the uploading mobile units script
	 * @param path script path
	 */
	public void setMobileUnitsPath(String path){
		
		mobileUnitsPath = path;
	}
	
	/**
	 * Method to set the path for the uploading shimmer units script
	 * @param path script path
	 */
	public void setShimmerUnitsPath(String path){
		
		shimmerUnitsPath = path;
	}	

	/**
	 * Method to set the path for the uploading mobile signals script
	 * @param path script path
	 */
	public void setMobileSignalsPath(String path){
		
		mobileSignalsPath = path;
	}
	
	/**
	 * Method to set the path for the uploading shimmer signals script
	 * @param path script path
	 */
	public void setShimmerSignalsPath(String path){
		
		shimmerSignalsPath = path;
	}
	
	/**
	 * Method set the path for the uploading mobile metadata script
	 * @param path script path
	 */
	public void setMobileMetadataPath(String path){
		
		mobileMetadataPath = path;
	}

	/**
	 * Method to set the path for the uploading shimmer metadata script
	 * @param path script path
	 */
	public void setShimmerMetadataPath(String path){
		
		shimmerMetadataPath = path;
	}
	
	/**
	 * Method to set the path for the script which check the last existing ID of a table
	 * @param path: script path
	 */
	public void setLastIDPath(String path){
		
		lastIDPath = path;
	}
	
	public boolean isProcessFinished(){
	
		if((tablesToUpload != 0) && (tablesToUpload == tablesUploaded)){

			return true;
		}
		
		else
			return false;
	}
}