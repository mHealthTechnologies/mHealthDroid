# **mHealthDroid**

## What is mHealthDroid? 

mHealhDroid is a framework designed to develop biomedical applications in an easy and rapid way, under the Android operating system and to be used along with portable biomedical devices or just every Android phone with built-in sensors.

This framework has been developed independently from the biomedical type device or communication protocol. Owing to that, it is necessary to create an intermediate driver for every portable biomedical device to be used which will work between the device and the framework. The drivers are included in the framework and remain unnoticed to the user. Thanks to this approach, an application can run simultaneously with different kinds of devices, providing a great flexibility.

Android target version: 4.2

Android minimum version: 2.3.3

## Table of Contents

- [What is mHealthDroid?](#what-is-mhealthdroid)
- [Features. What does mHealthDroid offers me?](#features-what-does-mhealthdroid-offers-me)
- [Installation instructions](#installation-instructions)
- [mHealthDroid structure](#mhealthdroid-structure)
- [Examples. How to use mHealthDroid](#examples-how-to-use-mhealthdroid)
- [Contribution guidelines](#contribution-guidelines)
- [Community](#community)
- [Team Members](#team-members)
	- [Authors](#authors)
	- [Contributors](#contributors)
- [Where can I get help?](#where-can-i-get-help)
- [Inspiration](#inspiration)
- [Copyright and license](#copyright-and-license)

## Features. What does mHealthDroid offers me?

* Rapid development of medical, health and wellbeing applications.

* Efficient and fast communication between portable biomedical devices and portable mobile devices to gather patient data like physiological or kinematic signals. Up to now, SHIMMER biomedical devices and mobile sensors can be used as portable biomedical devices.

* Development of applications capable of working with different portable health devices simultaneously.

* Efficient data transfer across the framework managers.

* Fast data storage either local or remote.

* Visualization of any kind of data stream like patient's vital signs or kinematic data, either online or offine.

* Knowledge inference procured through machine learning and statistical model.

* Guidelines support through Audio, Video or YouTube playlists, as well as notifications procedures.

* Easy system control and configuration tools to manage WiFi, 3G, Bluetooth, screen brightness, making phone calls or sending text messages.

* User management through login functionality, also supporting security and privacy.


## Installation instructions

Once mHealthDroid project is downloaded, it is necessary a few steps to get started.

1. Import the project to Eclipse or other IDE.    

2. Add the following libraries:

    - GraphView 3.0.jar
    - Guava 14.0.jar
    - Shimmer.jar
    - WekaSTRIPPED.jar
    - YouTubeAndroidPlayerApi.jar

3. Select mHealthDroid as library project.

4. Creates your new project and select mHealthDroid as library. 

5. Coding time!

All the libraries can be downloaded from our following repository: 

https://github.com/mHealthDroid/libraries

## mHealthDroid structure

<img src=https://github.com/mHealthDroid/mHealthDroid/blob/master/res/raw/frameworkdiagram.png?raw=true />

In the figure is shown the framework structure, with all the existing managers and how they interact with each other. All these managers are Singleton, which means that only exist one unique instance of each and this instance is available from every point of the framework. In short, is described each manager aim. More details of every manager and its development in our website soon.

- **Communication Manager**: heart of mHealthDroid. It is responsible for, among other things, the communication management with the biomedical devices, the data streaming, the local data storage and the broadcast of the received data to the rest of the framework.
- **Remote Storage Manager**: is aim is to upload the data avalaible in the local database to a remote storage. Although the framework only comprises the client side of the communication, it has also been offered a possible implementation to be used on the remote storage side to manage the received data from apps developed with _mHealthDroid_.
- **Visualization Manager**: allow either online or offline visualization. It has been developed using the GraphView library (http://android-graphview.org/). The source code of the compatible version for this library can be found in the following repository https://github.com/mHealthDroid/myGraphView
- **Data Processing Manager**: a really powerful manager, responsible for processing data and applying knowledge inference, either orline or offline.
- **System Manager**: miscellaneous manager which offers easy management of instrinsic aspect of Android devices; guidelines functionalities specially useful for health applications; and send alerts.


## Examples. How to use mHealthDroid

The aim of this section is to show basic examples of how to use each manager. More detailed information will be coming soon.

### Communication Manager

Every manager is singleton, which means it is necessary to get its instance like this:

``` java
CommunicationManager cm = CommunicationManager.getInstance();
```
Then, if one wants to storage data into the database, the variable storage must be initialized this way:

``` java
cm.CreateStorage(getApplicationContext());

```

To add devices to the manager, one just needs to call the appropriate functions:

``` java
cm.AddDeviceShimmer(getApplicationContext(), "Device Shimmer", true);
cm.AddDeviceMobile(getApplicationContext(), "Mobile Device");
```

Since the mobile device uses the device's sensor, it is not necessary to connect it. But
for the Shimmer devices, once they have been added, they need to be connected. This
work is done by the function _Connect_, giving as parameter the mac address:

``` java
cm.connect("Device Shimmer", address);
```

Before the device starts to stream, the enabled sensors of the devices can be set:

``` java
ArrayList<SensorType> sensors = newArrayList<SensorType>();
sensors.add(SensorType.ACCELEROMETER);
sensors.add(SensorType.MAGNETOMETER);
sensors.add(SensorType.GYROSCOPE);
cm.writeEnabledSensors("Mobile Device", sensors);
cm.writeEnabledSensors("Device Shimmer", sensors);
```

The number of samples to be stored in the buffer can be chosen too:

``` java
cm.setNumberOfSampleToStorage(100);
```

After all these things are done, it is the suitable moment to start (and stop whenever
is wanted) to stream:

``` java
cm.startStreaming("Mobile Device");
cm.startStreaming("Device Shimmer");
cm.stopStreaming("Mobile Device");
cm.stopStreaming("Device Shimmer");
``` 

There are a couple of features that can be set either before or during the data streaming
such as to select what device is going to store its data (by default, it is set to true) or to set a label.

``` java
cm.setStoreData("Mobile Device", false);
cm.setStoreData("device Shimmer", true);
cm.setLabel("walking");
```

There is a way to get the device in case it is wanted to access to its specific functionalities:

``` java
DeviceShimmerdS = (DeviceShimmer) cm.getDevice("Device Shimmer");
DeviceMobiledM = (DeviceMobile) cm.getDevice("Mobile Device");
```

To insert a new users profile there are a couple of things to consider. The database
must be open and the table created. It is also possible to check whether the login
already exists in order to avoid a SQL exception. Then the database should be closed.
All of this is possible by doing:

``` java
cm.openDatabase();
cm.createUsersTable();
if(!cm.existsLogin(login))
	cm.addUserProfile(login, password, age, sex, weight, height, email);
else
	Toast.makeText(getApplicationContext(), "this login already exists",Toast.LENGTH_SHORT).show();
cm.closeDatabase();
```
In case there are devices streaming, it is advisable to check whether any of them store
data before closing the database. It should be done in order to avoid a SQL exception.
To do that, it is only necessary to call the function of the Communication Manager
named _isStoring()_:
``` java
if(!cm.isStoring())
	cm.closeDatabase();
```

### Remote Storage Manager

The way to use this module is quite simple. RemoteStorageManager is a singleton class (class instantiation restricted to one object), so to obtain a RemoteStorageManager instance is necessary to proceed as follows:

``` java
ServerManager sm = ServerManager.getInstance();
```

It is also necessary to initialize the Storage variable belonging to the RemoteStorageManager class called storage. This is done by:

``` java
sm.CreateStorage(getApplicationContext());
```

Then the server IP address is set by means of the function _setServerIP_. Also, it
is compulsory to set the name and path (if necessary) of the scripts that will be executed in the server side. These scripts must be accessible from the server IP address.
For example, using XAMPP like in this example the scripts inside the _Htdocs_ can be
accessible by the address _http://serverIP/scriptName_.

``` java
sm.setServerIP("http://192.168.1.11");
sm.setMobileMetadataPath("insert_mobile_metadata.php");
sm.setMobileSignalsPath("insert_mobile_signals.php");
sm.setMobileUnitsPath("insert_mobile_units.php");
sm.setShimmerMetadataPath("insert_shimmer_metadata.php");
sm.setShimmerSignalsPath("insert_shimmer_signals.php");
sm.setShimmerUnitsPath("insert_shimmer_units.php");
sm.setLastIDPath("get_last_ID.php");
```

If WiFi connection is used, WiFi must be enabled. The user can turn it on through
the setWiFiEnabled function defined on the System Manager.

``` java
sysm.setWifiEnabled(true, getApplicationContext());
```

There are three functions for uploading, one for each database table: data, metadata
or units. For example, the uploading of the the database tables for the mobile device
("Mobile device") and the ones for a given wearable monitoring device ("device 1") is
defined as follows:

``` java
// To upload shimmer tables
sm.uploadUnitsTable("device 1");
sm.uploadMetadataTable("device 1");
sm.uploadSignalsTable("device 1");

// To upload mobile tables
sm.uploadUnitsTable("Mobile Device");
sm.uploadMetadataTable("Mobile Device");
sm.uploadSignalsTable("Mobile Device");
```

To know when the data are upload, it is needed to run within a thread the function
isProcessFinished(). An example of how to do this follows:

``` java
Handler mHandler = new Handler();
mHandler.post(isFinished);
Runnable isFinished = new Runnable(){
	@Override
	public void run() {
		if(rsm.isProcessFinished()){
			Toast.makeText(getActivity(), "Uploading completed", Toast.LENGTH_LONG).show();
}
		else
			mHandler.postDelayed(this, 1000);
	}
};

```

Once all the data are uploaded, the thread responsible for the uploading must be
stopped, to save the waste of resources.

``` java
sm.finishProcess();
```

To finish, according to users preferences, the WiFi connection could be turn off.
``` java
sysm.setWifiEnabled(false, getApplicationContext());
```

To upload the local database, some PHP scripts are necessaries, provided along
with the framework. It is just a simple implementation, different scripts could be used, but the framework user must be careful with the scripts name and the paths where these are located. By default, if the user does not change scripts names or paths, the files get _last ID.php, DB config.php, insert mobile metadata.php, insert mobile signals.php, insert mobile units.php, insert shimmer metadata.php, insert shimmer signals.php_ and _insert shimmer units.php_ must be located in a path where can be accessible by the address _http://serverIP/nameFile.php_ (for example, in the directory Htdocs for XAMPP).

Consequently, if the user wants to use dierent scripts or just for server reasons
needs to change their path, it can be achieved by means of the following functions: _set-
MobileUnitsPath(String path), setMobileSignalsPath(String path), setMobileMetadata-
Path(String path), setShimmerUnitsPath(String path), setShimmerSignalsPath(String
path), setShimmerMetadataPath(String path)_ and _setLastIDPath(String path)_.

### Visualization Manager

Since it is a singleton class, first it is necessary to get the instance:

``` java
VisualizationManager vm = VisualizationManager.getInstance();
```

Then, a graph must be created. A unique name to the graph and its type (Line or
Bars) must be selected. The UI context must also be indicated.

``` java
vm.addGraph("graph", GraphType.LINE, getApplicationContext());
```
Now, it depends on the approach desired, either online or offline. Here, both of them
are explained.

- Offline
	
  To create a series, first it is necessary to create an array with the values of the Y
coordinates. For this example, the Sine of a set of numbers will be used:                  	

	``` java
	float[] array = new float[250];
	float a = 0;
    for (int i = 0; i < 250; i++) {
		a += 0.2;
		array[i] = (float) Math.sin(a);
	}
	```
    
    To introduce the data into the graph, the following procedure must be used:
    ``` java
vm.addSerie("graph", "Sin", array);
```
or

  ``` java
  vm.addSerie("graph", "Sin", array, Color.BLUE, 1, "Sin from 0 to 250");
  ```
A series can be introduced with a description. To show it, one must activate the
option. Its alignment in the graph can be set as well:
``` java
vm.setShowLegend("graph", true);
vm.setLegendAlign("graph", LegendAlign.TOP);
```
The graph must be included in a layout In order to be painted:
```
LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
vm.paint("graph", layout);
```
Horizontal and vertical labels are created automatically, but they can be set like
this:
``` java
String [] month = {"January", "February", "March", "April", "May",
"June", "July", "August", "Sept.", "Oct.", "Nov.", "Dec."};
vm.setHorizontalLabels("graph", month);
vm.setVerticaltalLabels("graph", month);
```

- Online
	
    Like in the offine mode, it is necessary to add the graph into a layout:
``` java
LinearLayout layoutOnline = (LinearLayout)
findViewById(R.id.graphOnline);
vm.paint("graph", layoutOnline);
```
To visualize online data, it is necessary indicate the set of sensors desired:
``` java
ArrayList<SensorType> sensors = new ArrayList<SensorType>();
sensors.add(SensorType.ACCELEROMETER_X);
sensors.add(SensorType.ACCELEROMETER_Y);
sensors.add(SensorType.ACCELEROMETER_Z);
sensors.add(SensorType.GYROSCOPE_X);
sensors.add(SensorType.GYROSCOPE_Y);
sensors.add(SensorType.GYROSCOPE_Z);
sensors.add(SensorType.MAGNETOMETER_X);
sensors.add(SensorType.MAGNETOMETER_Y);
sensors.add(SensorType.MAGNETOMETER_Z);
```
The _scrollable_ feature of the graph MUST be activated. Otherwise online visualization
will NOT work:
``` java
vm.setScrollable("graph", true);
```
Now it is the time to use the function, which does all the work, to visualize the
data:
``` java
vm.visualizationOnline("graph", "nameDevice", sensors);
```
To stop the visualization process, the following method may be used:
``` java
vm.stopVisualizationOnline("graph", "nameDevice");
```
There are some features that are advisable to set in order to get a better visualization
in both modes:
- Make the graph scalable (advisable for offine mode):
``` java
vm.setScalable("graph", true);
```
- Set the init and the viewport (advisable for online mode):
``` java
vm.setViewPort("graph", 1, 200);
```
- Set the values of the Y axis (advisable for both modes):
``` java
vm.setManualYAxisBounds("graph", 10, -10);
```

### System Manager

System Manager is a singleton class. Thus, to obtain the unique instance that could
exists in an application it is necessary to use the getInstance function.
``` java
SystemManager sm = SystemManager.getInstance();
```

#### Services and Setup

Services and Setup modules are quite simple to use. To perform a mobile call or to
send a text message it could be done like follows:
``` java
sm.call(number, activity);
sm.sendSMS(number, message);
```

Where number parameter is the mobile number to call or send a message and
message the text message to be sent.
There are three kind of notifications which can be used as follow:
``` java
sm.sendSimpleNotification(title, text, notificationID, icon, context);
PendingIntent contentIntent =
PendingIntent.getActivity(getApplicationContext(), 0, Intent, 0);
int flags = Notification.FLAG_AUTO_CANCEL;
sm.sendComplexNotification(title, text, notificationId, icon, flags,
contentIntent, context);
Uri soundUri =
RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
sm.sendComplexNotificationCustomSound(title, text, notificationId, icon,
flags, contentIntent, soundUri, context);
```

Where _title_ parameter is the notification title, _text_ the text to be shown, _notificationId_ the notification ID, _icon_ a notification icon and context the application UI
context. For the last two notification type, _contentIntent_ is the Intent which is run
when the notifications are clicked on and flags parameters are the notifications 
flags available for user usage.

_NOTE. The minimum Android SDK Version required is 10th so some flags belonging
to newer Android SDK versions may do not work properly._

These same notifications may be scheduled. It is necessary to declare in the Android
Manifest the class ScheduledTask as a receiver.

``` java
<receiver android:name="systemManager.services.ScheduledTask"/>
```

```
sm.scheduledSimpleNotification(title, text, notificationID, icon, context,
date);
sm.scheduledComplexNotification(title, text, notificationId, icon, flags,
contentIntent, context, date);
sm.scheduledComplexNotificationCustomSound(title, text, notificationId, icon,
flags, contentIntent, soundUri, context, date);
```

All parameters play the same role than explained previously except _Date_. It is a
Calendar type which indicates when the notification is scheduled. It is also possible to
schedule an audio to be reroduced by this:
``` java
sm.scheduledAudio(path, context, date);
```
Where _path_ is the files path, _context_ is the application UI context and _date_ is a
Calendar type indicating when the audio is scheduled to be reproduced.

To turn on/off Wi-Fi or Bluetooth:
``` java
sm.setWifiEnabled(true/false, context);
sm.setBluetooth(true/false);
```
The first boolean parameter set the Wi-Fi/Bluetooth state and _context_ is the application
UI context. Finally, the screen brightness could be adjust as follows:
``` java
sm.setScreenBrightness(brightness, activity)
```
The parameter of _brightness_ is a float number between 0 and 1 (from dark to
bright). It is also possible to set it to less than 0, which means that the user default
screen brightness is used.

#### Guidelines

The manner of using any of the guidelines modules is slightly dierent. First, it is
necessary to obtain an instance of the class which will be used and once this is done,
just use its functions.

##### YouTube

To build the YouTube module has been necessary the YouTube Android Player API. To employ this API is compulsory to provide at least a _YoutubePlayerView_. An
example of how to use the YouTube Guideline module is shown next:

``` java
setContentView(R.layout.youtube_layout);
YouTubePlayerViewyoutubeView =
(YouTubePlayerView)findViewById(R.id.youtube_view);
youtube = sm.getYoutubePlayer(getApplicationContext(), youtubeView, DEVELOPER_KEY);
```

The developer key is a variable that identifies the YouTube developer submitting
an API request. In the case when only playing a YouTube video is wanted, it could be
done by:
``` java
youtube.reproduceSingleVideoMode(URL);
```
The _URL_ parameter is the YouTube video URL to be reproduced, that consists of
the last part of the video link.

It is also possible to reproduce YouTube playlist videos, selecting the video to be
reproduced using a _listView_ view. In order to do this, it is necessary to have in the
current layout a _listView_ view.
``` java
ListViewvideosListView = (ListView)findViewById(R.id.listListView);
youtube.reproducePlaylistMode(videosListView, R.layout.entry, PLAYLIST_URL);
```
The parameter _entry_ is a layout with every entry format of the _listview_.

##### Audio

The Audio module has been developed using the Media Player Android API.
To utilize this module, the first thing to do is to obtain an instance of the Audio class.

``` java
Audio audioPlayer = sm.getAudioPlayer();
```
Now, to play a stored audio file in the mobile phone, this must be done:
``` java
String path = Environment.getExternalStorageDirectory() + "/" + "song.mp3";
audioPlayer.loadFile(path);
audioPlayer.prepare();
audioPlayer.play();
```
More functions are available, such as _pause_, _resume_, _getDuration_, etc. However,
every function of the MediaPlayer API may be used.

##### Video

The Video module is based on the functionality provides by the _VideoView_ view. To reproduce a video which is stored in the mobile external card, the following
should be done:

``` java
setContentView(R.layout.video_layout);
VideoViewvideoHolder = (VideoView)findViewById(R.id.videoView);
Video video = sm.getVideoPlayer(videoHolder);
String path = Enviroment.getExternalStorageDirectory() + "/" + "video.mp4";
video.setVideoPath(path);
video.play();
```

More functions are available, such as pause or resume. Every function of the
_VideoView_ class may be used.

### Data Processing Manager

Since it is a singleton class, first it is necessary to get the unique instance.
``` java
DataProcessingManager dpm = DataProcessingManager.getInstance();
```
It is also necessary to initialize and set (for the acquisition module) the Storage
variable belonging to the RemoteStorageManager class called storage. This is done by:
``` java
dpm.createAndSetStorage(getApplicationContext());
```
Now it proceeds depending on the inference knowledge approach desired: online or
offine.

#### Offline

##### Acquisition
    
   To show how the data acquisition is done, some devices and sensors where data
are retrieved must be selected. For example, it is shown how to acquire the signals
accelerometer X, accelerometer Y, accelerometer Z and timestamp of a given wearable
device called "Shimmer Chest" and the gyroscope X, gyroscope Y, gyroscope Z,
humidity and timestamp of a portable mobile device called "Mobile Device".

    ``` java
    ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices = new
    ArrayList<Pair<ArrayList<SensorType>, String>>();
    ArrayList<SensorType> sensors1 = new ArrayList<SensorType>();
    ArrayList<SensorType> sensors2 = new ArrayList<SensorType>();
    sensors1.add(SensorType.ACCELEROMETER_X);
    sensors1.add(SensorType.ACCELEROMETER_Y);
    sensors1.add(SensorType.ACCELEROMETER_Z);
    sensors1.add(SensorType.TIMESTAMP);
    sensors2.add(SensorType.GYROSCOPE_X);
    sensors2.add(SensorType.GYROSCOPE_Y);
    sensors2.add(SensorType.GYROSCOPE_Z);
    sensors2.add(SensorType.HUMIDITY);
    sensors2.add(SensorType.TIMESTAMP);
    String nameDevice1 = "Shimmer CHEST";
    String nameDevice2 = "Mobile Device";
    Pair<ArrayList<SensorType>, String> pair1 = new Pair(sensors1, nameDevice1);
    Pair<ArrayList<SensorType>, String> pair2 = new Pair(sensors2, nameDevice2);
    sensorsAndDevices.add(pair1);
    sensorsAndDevices.add(pair2);
    ``` 
    
   Now that _sensorsAndDevices_ is ready, any Acquisition function may be used. To
acquire all the selected data in the IDs range 100-500:
``` java
dpm.retrieveInformationByID(100, 500, sensorsAndDevices);
```
To retrieve data belonging to the first existing session:
``` java
dpm.retrieveBySession(1, sensorsAndDevices);
```
To retrieve data belonging to the first, second and third session:
``` java
dpm.retrieveInformationByIntervalSessions(1, 3, sensorsAndDevices);
```
To retrieve the last 20 seconds of available data in the local database:
``` java
dpm.retrieveInformationLastSeconds(20, sensorsAndDevices);
```
Retrieving data using dates is slightly more complicated. An example of retrieving
all the data streamed from the day 24th January 2014 at 22:00:00 hour to the day 25th
January 2014 at 16:00:00 follows. It is important to keep in mind that month value
goes in the interval [0-11]
``` java
Time start = new Time();
start.hour = 22;
start.minute = 0;
start.second = 0;
start.year = 2014;
start.month = 0;
start.monthDay = 24;
Time end = new Time();
end.hour = 16;
end.minute = 0;
end.second = 0;
end.year = 2014;
end.month = 0;
end.monthDay = 25;
dpm.retrieveInformationByDates(start, end, sensorsAndDevices);
```
To retrieve all the information available:
``` java
dpm.retrieveAllInformation(sensorsAndDevices);
```

#### Pre-Processing

From this point, it is assumed that the data has been already acquired and stored
in the hash variable (which is defined in this manager). Thus, to calculate either the
_Upsampling_ or the _Downsampling_, one just needs to do the following:
``` java
dpm.downSampling(2);
dpm.upSampling(3);
```
The result will be stored into the _hashProcessed_ variable, which is defined in this
manager.

#### Segmentation

To make the segmentation, first it is necessary to get the devices' sample rate. For
this example, an array with false sample rates is created:

``` java
ArrayList<Float> rates = new ArrayList<Float>();
rates.add((float) 50.0);
rates.add((float) 50.0);
rates.add((float) 50.0);
```
Then, the windowing no overlap can be calculated as follow:
``` java
dpm.windowing_NoOverlap(DataType.Raw, (float) 2, rates);
and the windowing overlap as follow:
dpm.windowing_overlap(DataType.Raw, (float) 2.5, (float) 0.5, rates);
```
#### Features Extraction

The features must be defined in order to make the features extraction. These are
some features definitions:
``` java
dpm.addFeature("Device Shimmer 1", SensorType.ACCELEROMETER_X,
FeatureType.MAXIMUM);
dpm.addFeature("Device Shimmer 2", SensorType.ACCELEROMETER_Y,
FeatureType.MINIMUM);
dpm.addFeature("Device Mobile", SensorType.ACCELEROMETER_Z,
FeatureType.VARIANCE);
dpm.addFeature("Device Shimmer 1", SensorType.GYROSCOPE_Y,
FeatureType.STANDARD_DEVIATION);
dpm.addFeature("Device Shimmer 2", SensorType.MAGNETOMETER_X,
FeatureType.ZERO_CROSSING_RATE);
dpm.addFeature("Device Mobile", SensorType.MAGNETOMETER_Z,
FeatureType.MEAN_CROSSING_RATE);
```
Owing to the previous steps, the features extraction can be done from different data:

- From not processed data and not segmented:
``` java
dpm.feature_extraction(DataType.Raw, false, false);
```
- From processed data and not segmented:
``` java
dpm.feature_extraction(DataType.Processed, false, false);
```
- From not processed data and segmented:
``` java
dpm.feature_extraction(DataType.Raw, true, false);
```
- From processed and segmented data, using the uncompleted windows:
``` java
dpm.feature_extraction(DataType.Processed, true, true);
```

#### Classification

The way to use the Classification module does not change depending on the selected
approach (offine or online). The first thing to do is to read a Weka file (arff format).

``` java
dpm.readFile(Environment.getExternalStorageDirectory(), "example.arff");
```
Now, test or training instances may be set. Also a summary of both train instances
may be obtained.
``` java
dpm.setTrainInstances();
dpm.setTestInstances();
String TrainSummary = dpm.getTrainInstancesSummary();
```
A model may be built with the _trainClassifier_ function. The first parameter is the
class attribute and usually is the last attribute. The second parameter is the classifier
type to be used, in this case J48 (Decision Tree). The _getAttributes_ function returns a
list of the existing Attributes.
``` java
int numAttributes = classification.getAttributes().size();
dpm.trainClassifier(numAttributes - 1, classifierType.J48);
```
The model may be directly loaded as well using the _loadModel_ method:
``` java
dpm.loadModel(classifier);
```
Now that the model has been built, this can be evaluated by the method _testClassifier_. A summary about the evaluation may be obtained by _getTestSummary_ method.
The confusion matrix is obtained using the _getTestConfusionMatrix_ function.
``` java
dpm.testClassifier();
String summaryEvaluation = dpm.getTestSummary();
String confusionMatrix = dpm.getConfusionMatrix();
```
To convert features vector or features matrix coming from the Feature Extracture
stage into Instances Weka objects, the methods _featureVectorToInstances_ and _featureMatrixToInstances_ are available.
``` java
Instances instances = dpm.featureVectorToInstances(featureVector);
Instances instances = dpm.featureMatrixToInstances(featureMatrix);
```
To clasify and unlabeled instance, there are two functions available: _classifyInstanceToDouble_ and _classifyInstanceToString_. For example, to classify the first instance of an Instances object called instances:
``` java
String label = dpm.classifyInstanceToString(instances.firstInstance());
Double label = dpm.classifyInstanceToDouble(instances.firstInstance());
```

### Online

#### Acquisition and Segmentation

These modules are not used in the online inference knowledge process, due to that
the Communication Manager is the one which provides the data through windows with
a determinate size.

#### Pre-Processing, Feature Extraction, and Classification

To make the online knowledge inference, it is necessary to define the features to
be extracted. The classification model and the class attribute must be defined as well.
The previous section, Offine knowledge inference, shows how to do it.
In order to execute the knowledge inference as a sequence, all the steps are dened
under one function:
``` java
dpm.inferenceOnline(2, sensorsAndDevices, null, 0, 1);
```

The last parameter indicates which kind the class attribute is (and in consequence the
value returned): String or Double. In case the last parameter is 0, the returned value
will be a String and will be stored in the _stringClassified variable_ (which is defuned in this manager). In case the last parameter is 1 (like in the example), the returned value will be Double and will be stored in the _doubleClassified_ variable (which is also defined in this manager).

There is also another function for the online knowledge inference:
``` java
dpm.inferenceOnline(2, sensorsAndDevices, null, 0, 1, mHandler);
```


## Contribution guidelines

How can you help mHealthDroid to be a more completed tool? Here we propose you some ideas!

- Adding new biomedical devices drivers would be awesome! The possibility of using more and more biomedical devices makes mHealthDroid a tool more accesible for everybody!

- To improve the data visualization including more functionalities. Representing online visualization for different devices in the same graph would be cool!

- Extending the pre-processing module of the Data Processing Manager by adding more methods (downsampling and upsampling are already provided).

- Extending the features extraction module of the Data Processing Manager by adding new features (mean, variance, standard deviation, zero crossing rate, mean crossing rate, maximum and minimum already provided).

- To extend the funcionalities for the remote storage, making possible the data transference in both ways. ¿Cloud services for high performance computacion?

- To extend the guidelines functionalities. For example, Audio and Video modules encapsulates the basic functionalities of the Android Media Player API and the VideoView Android class. Adding extra functionalities would help to a more complete experience.

- To add more functionalities to manage the mobile aspects.

- To update notifications to the new ones provided by the last Android versions. Notifications implementation to this day, uses the functions provided for the Android SDK Version 10.

More contribution ideas will be added soon.


## Community

How can you keep track of the development and news of mHealthDroid? Coming soon in our twitter and webpage.

## Team Members

### Authors

Rafael García Fernández

* _University of Granada (UGR)_
* http://github.com/rafagf
* Email: rafagarfer@gmail.com

Alejandro Sáez Fernández

* _University of Granada (UGR)_
* http://github.com/alsafer
* Email: alejandrosaez3@gmail.com

### Original Idea and Project Supervisor

Oresti Baños Legrán

* _Research Center for Information and Communication Technology University of Granada (CITIC-UGR)_
* Email: oresti.bl@gmail.com

### Collaborators

Juan Antonio Holgado Terriza

* _University of Granada (UGR)_
* Email: jholgado@ugr.es

Jesús Luis Muros Cobos

* _Research Center for Information and Communication Technology University of Granada (CITIC-UGR)_
* Email: jesusmuros@ugr.es

## Where can I get help?

You can get any help you need getting in contact with any of the authors. We are looking forward to help you!

## Inspiration

This project is an original idea. It began as the MsC thesis for Computer Engineering of both authors in the University of Granada (UGR). 

After a research, we got the conclusion that there exist many wearable health devices, a lot biomedical applications and even a
wide variety of biomedical frameworks and mobile frameworks, but in terms of biomedical mobile frameworks there is only a few and most of them are focused in the acquisition of data, patient monitoring and data broadcasting. We consider quality trustworthy health applications should incorporate these functionalities plus many others such as data visualization, local and remote storage, guidelines, notifications or knowledge inference and should not be limited to a particular biomedical device. For all these reasons the idea of mHealthDroid was born.

## Copyright and license

Project released under the license GPL V.3.
