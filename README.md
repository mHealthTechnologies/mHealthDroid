# **mHealthDroid**


## What is mHealthDroid? 

mHealhDroid is a framework designed to develop biomedical applications in an easy and rapid way, under the Android operating system and to be used along with portable biomedical devices or just every Android phone with built-in sensors.

This framework has been developed independently from the biomedical type device or communication protocol. Owing to that, it is necessary to create an intermediate driver for every portable biomedical device to be used which will work between the device and the framework. The drivers are included in the framework and remain unnoticed to the user. Thanks to this approach, an application can run simultaneously with different kinds of devices, providing a great flexibility.

Android target version: 4.2

Android minimum version: 2.3.3

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
- **Remote Storage Manager**: is aim is to upload the data avalaible in the local database to a remote storage.
- **Visualization Manager**: allow either online or offline visualization.
- **Data Processing Manager**: a really powerful manager, responsible for processing data and applying knowledge inference, either orline or offline.
- **System Manager**: miscellaneous manager which offers easy management of instrinsic aspect of Android devices; guidelines functionalities specially useful for health applications; and send alerts.


## Examples. How to use mHealthDroid

Coming soon.

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

* http://github.com/rafagf
* http://twitter.com/rafa_piki
* Email: rafagarfer@gmail.com

Alejandro Saez Fernandez

* http://github.com/alsafer
* Email: alejandrosaez3@gmail.com

### Contributors

Oresti Baños Legran

* Email: oresti.bl@gmail.com


## Where can I get help?

You can get any help you need getting in contact with any of the authors. We are looking forward to help you!

## Inspiration

This project is an original idea. It began as the MsC thesis for Computer Engineering of both authors in the University of Granada (UGR). 

After a research, we got the conclusion that there exist many wearable health devices, a lot biomedical applications and even a
wide variety of biomedical frameworks and mobile frameworks, but in terms of biomedical mobile frameworks there is only a few and most of them are focused in the acquisition of data, patient monitoring and data broadcasting. We consider quality trustworthy health applications should incorporate these functionalities plus many others such as data visualization, local and remote storage, guidelines, notifications or knowledge inference and should not be limited to a particular biomedical device. For all these reasons the idea of mHealthDroid was born.

## Copyright and license

Project released under the license GPL V.3.
