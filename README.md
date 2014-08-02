<img src="http://apuestasrafag.files.wordpress.com/2014/03/fotologo2.png?w=712" height=225>

## What is mHealthDroid?  

mHealhDroid is an open-source mobile framework designed to facilitate the rapid and easy development of mHealth and biomedical applications. The framework is devised to leverage the potential of mobile devices such as smartphones or tablets, wearable sensors and portable biomedical devices. These systems are used increasingly for the monitoring and delivery of personal health care and wellbeing. Likewise, mHealthDroid aims at bringing together heterogeneous multimodal sensors including both research and commercial systems. 

The framework comprises an extensive set of modules for sensor data acquisition, data management, remote storage, signal processing, machine learning, multidimensional data visualization, as well as intelligent recommendations and multimedia guidelines among others features. The core of the framework is defined to operate on the Android operating system; however, it has been implemented in a way that allows its use with any portable device. In fact, the core modules and components are defined to be completely independent of the underlying sensing and communication technology. To utilize a new device (commercial or not) it is only required to include the corresponding drivers that allow the communication between the mHealthDroid modules and the specific device (transparent to the user anyhow). The mHealthDroid initiative already includes drivers for a wide sort of portable devices; nevertheless it aims at incorporating new systems to the current portfolio of supported devices. 

This active platform is in continuous development, thereby contributions are very welcome. Suggestions and comments are also appreciated!

Contact (general info): mhealthdroid@gmail.com

Contact (R&D enquiries): oresti.bl@gmail.com [<a href="http://www.ugr.es/~oresti/">Oresti Baños</a>]

Contact (implementation enquiries): rafagarfer@gmail.com [Rafael García], alejandrosaez3@gmail.com [Alejandro Sáez] 

**Table of Contents**

- [What is mHealthDroid?](#what-is-mhealthdroid)
- [Features. What does mHealthDroid offer me?](#features-what-does-mhealthdroid-offer-me)
- [Installation instructions](#installation-instructions)
- [mHealthDroid structure](#mhealthdroid-structure)
- [Full documentation](#full-documentation)
- [APP examples](#app-examples)
- [How to use mHealthDroid](#examples-how-to-use-mhealthdroid)
- [Contribution guidelines](#contribution-guidelines)
- [Community](#community)
- [Team Members](#team-members)
- [Where can I get help?](#where-can-i-get-help)
- [Inspiration](#inspiration)
- [Copyright and license](#copyright-and-license)

## Features. What does mHealthDroid offer me?

* Rapid development of medical, health and wellbeing applications.

* Fast and efficient communication between portable biomedical devices and portable mobile devices to gather human-centered physiological and kinematic data. Up to now, SHIMMER biomedical devices and mobile sensors can be used as portable biomedical devices.

* Development of applications capable of working with different portable health devices simultaneously.

* Efficient data transfer across the framework's managers.

* Fast data storage (either local or remote).

* Visualization of any multimodal data streams such as patient's vital signs or kinematic data (either online or offine).

* Knowledge inference by using machine learning and pattern recognition models.

* Multimedia guidelines and recommenders, supported through audio, video and YouTube playlists, as well as notification procedures.

* System control and configuration tools to manage WiFi, 3G, Bluetooth, screen brightness, phone calls or text messages.

* User logging and data privacy mechanisms

## Installation instructions

A few steps are required to get started:

1. Download the project source files.

2. Import the project to Eclipse or other IDE.    

3. Add the following libraries:

    - GraphView 3.0.jar
    - Guava 14.0.jar
    - Shimmer.jar
    - WekaSTRIPPED.jar
    - YouTubeAndroidPlayerApi.jar

4. Select mHealthDroid as library project.

5. Create your new project and select mHealthDroid as library. 

6. Coding time!

All the libraries can be downloaded from mHealthDroid´s releases.

https://github.com/mHealthDroid/mHealthDroid/releases

## mHealthDroid structure

<img src=http://apuestasrafag.files.wordpress.com/2014/03/frameworkdiagram.png?w=800 />

The figure above depicts the structure of the framework, including all the existing managers and how they interact with each other. All these managers are of "singleton" kind, which means that only one unique instance can exist for each class and this instance is available from every component of the framework. In the following we provide a brief description of each manager. 

- **Communication Manager**: it is responsible for the connection of mobile devices (smartphone, tablet) and biomedical devices, vital data gathering, local data storage and serve the collected data to the rest of the framework components.
- **Remote Storage Manager**: it is devised to upload the data avalaible in the local database to a remote storage. Although the framework only comprises the client side of the communication, we provide along with the framework a possible implementation for the server side.
- **Visualization Manager**: it supports both online and offline visualization. It builds on an original extension of the GraphView library (http://android-graphview.org/). The source code of the new version developed as part of the _mHealthDroid_ project can be found at https://github.com/mHealthDroid/myGraphView.
- **Data Processing Manager**: it is in charge of supervising and coordinating all the processes related to the inference of knowledge. This manager provides signal processing, data mining and machine learning techniques to extract knowledge from the biomedical data.
- **System Manager**: this is a miscellaneous manager which allows for the configuration and monitoring of instrinsic aspects of the mobile and external devices. More importantly, it comprises a set of tools to support guidelines, recommendations or alerts of worth in health applications.

## Full documentation

The mHealthDroid full documentation can be found in <a href="http://apuestasrafag.files.wordpress.com/2014/02/full-documentation-thesis-rafael-garcia.pdf">Rafael's</a> and <a href="http://apuestasrafag.files.wordpress.com/2014/02/full-documentation-thesis-alejandro-saez.pdf">Alejandro's</a>  Master Thesis.

## APP examples

- mHealthAPP: exemplary APP intended to show the usefulness and potential of mHealthDroid. More information in <a href="https://github.com/mHealthDroid/mHealthAPP">mHealthAPP </a>repository

[<img src="http://apuestasrafag.files.wordpress.com/2014/03/thumbnailvideo.png?w=673" height=300>](https://www.youtube.com/watch?v=AMdxw4osjCU)


## Examples. How to use mHealthDroid

The aim of this section is to show basic examples of how to use each manager.

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

Consequently, if the user wants to use different scripts or just for server reasons
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

The manner of using any of the guidelines modules is slightly different. First, it is
necessary to obtain an instance of the class which will be used and once this is done,
just use its functions.

##### YouTube

To build the YouTube module has been necessary the YouTube Android Player API. To employ this API is compulsory to provide at least a _YoutubePlayerView_. The class where this functionality is developed must extends the _YouTubeBaseActivity_ and implements _YouTubePlayer.OnInitializedListener_.

An example of how to use the YouTube Guideline module is shown next:

At least the following fields need to be declared as class fields.

``` java
YouTubePlayerView youTubePlayerView; 
YouTubePlayer player;
ListView videosListView;
Youtube youtube;
```

Declaration and initialization:

``` java
setContentView(R.layout.youtube_layout);
player = null;
YouTubePlayerView youtubePlayerView =
(YouTubePlayerView)findViewById(R.id.youtube_view);
youtubePlayerView.initialize(KEY_DEVELOPER, this);
sm = SystemManager.getInstance();
youtube = sm.getYoutubePlayer(getApplicationContext(), youtubePlayerView);
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
ListView videosListView = (ListView) findViewById(R.id.listListView);
youtube.reproducePlaylistMode(videosListView, R.layout.entry, R.id.textViewSuperior, 
R.id.textViewInferior, R.id.imageViewImage, playlistID);
```
The parameter _entry_ is a layout with every entry format of the _listview_. _TextViewSuperior_, _TextViewInferior_ and _imageViewImage_ are fields with the title, description and thumbnail of a YouTube video. _PlaylistID_ is the ID of a list of YouTube videos. 

It is also necessary to implement two abstract methods belonging to _YouTubePlayer.OnInitializedListener_.

``` java
@Override
public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {

	Toast.makeText(this, "Oh dear, something terrible happened, sorry!", Toast.LENGTH_SHORT).show();		
}

@Override
public void onInitializationSuccess(Provider provider, YouTubePlayer playa, boolean wasRestored) {
		
	this.player = playa;
	this.player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);		
	//Now that the player is initialized, we need to set it on our Youtube class
	youtube.setPlayer(player);
}
```

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

##### Pre-Processing

From this point, it is assumed that the data has been already acquired and stored
in the hash variable (which is defined in this manager). Thus, to calculate either the
_Upsampling_ or the _Downsampling_, one just needs to do the following:
``` java
dpm.downSampling(2);
dpm.upSampling(3);
```
The result will be stored into the _hashProcessed_ variable, which is defined in this
manager.

##### Segmentation

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
##### Features Extraction

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

##### Classification

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

#### Online

##### Acquisition and Segmentation

These modules are not used in the online inference knowledge process, due to that
the Communication Manager is the one which provides the data through windows with
a determinate size.

##### Pre-Processing, Feature Extraction, and Classification

To make the online knowledge inference, it is necessary to define the features to
be extracted. The classification model and the class attribute must be defined as well.
The previous section, Offine knowledge inference, shows how to do it.
In order to execute the knowledge inference as a sequence, all the steps are defined under one function:

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

How can you keep track of the development and news of mHealthDroid? Follow us in our <a href="https://twitter.com/mhealthdroid">twitter</a>

## Team Members

### Authors

Rafael García

* http://github.com/rafagf
* Email: rafagarfer@gmail.com 

Alejandro Sáez

* http://github.com/alsafer
* Email: alejandrosaez3@gmail.com

### Original Idea and Project Coordinator

Dr. Oresti Baños

* _Research Center for Information and Communication Technology University of Granada (CITIC-UGR)_
* Email: oresti.bl@gmail.com

### Collaborators

Prof. Juan Antonio Holgado

* _University of Granada (UGR)_
* Email: jholgado@ugr.es

## Where can I get help?

You can get any help you need getting in contact with any of the authors. We are looking forward to help you!

## Inspiration and motivation

This project is an original idea. The project was initiated and developed as part of the MsC thesis (Computer Science) of the authors at University of Granada (UGR) . 

After evaluation of the state-of-the-art in mobile health we ended up with the conclusion that there exist multiple wearable health devices (most of which are becoming commercial), biomedical apps and also biomedical and mobile frameworks. However, no biomedical mobile framework exists in a broad sense to the best of our knowledge. Although there are some frameworks devised for particular clinical problems, they mainly support data acquisition, visualization and broadcasting. Powerful health applications should incorporate these features plus many others such as local and remote storage, guidelines, recommenders notifications or knowledge inference among others, and not be limited to a particular biomedical device.

## Copyright and license

Project released under the license GPL V.3.
