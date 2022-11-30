# Harvest4Us 
* Harvest4Us is an android application that connects local farmers to their community, making it easy for consumers to buy fresh produce.

# Installation Guide

To run the Harvest4Us app, you will first need to set up the backend. Once the backend is up and running, you can then build and run the app via Android Studio.

## Backend Setup
The backend is comprised of a Flask HTTP server connected to a MYSQL database. This section will show how to setup the backend and database locally.

### Local Database setup (Only needs to be done once)

You will need to install MySQL server and MySQL client. Both can be downloaded from MySQL's [website](https://www.mysql.com/downloads/). A MySQL IDE such as Workbench will also be useful for running the scripts. Workbench can also be downloaded from MySQL's website.

Once you have MySQL installed, you will need to:
* Run the ```schema``` SQL script and then the ```dummy_data``` SQL script. Both can be found in the sql-table-setup folder. This can be done using MySQL Workbench.
* In the backend folder, create a file named ```db_auth.json```. In the file, paste in the following:

``` json
{ 

    "user": "your_username", 

    "password": "your_password", 

    "host": "127.0.0.1", 

    "port": 3306, 

    "database": "harvest4us" 
} 
```
* Replace `your_username` and `your_password` with your username(most likely it is `root`) and password you created during MySQL installation.

The database will now be setup and ready to be used by the Flask backend.

## Flask Backend Installation (Only needs to be done once)
The backend is built with Flask, a backend Python framework. You will first need to ensure Python is [installed on your system](https://www.python.org/downloads/windows/) (it is installed by default on most Mac/Linux machines, but not Windows). 

The backend requires a few Python dependencies. All of these dependencies can be installed via [pip](https://pip.pypa.io/en/stable/installation/). Run the following commands to install the dependencies: 

* `pip install flask`
* `pip install mysql-connector-python`
* `pip install requests` 

## Running the Backend

Once the database is setup and all of the dependencies are installed, you can now run the backend. Navigate to the backend folder in your terminal and run the following commands:
* export FLASK_APP=main
* flask run

The backend will now be running in the terminal. Leave the terminal open to keep the backend running.

## Run the Android app
The app can be built and run using [Android Studio](https://developer.android.com/studio/). The app's source code is contained in the Harvest4usMOBILE folder. Open that folder as a project in Android Studio, and then the app can be built and run on either an Android emulator or an Android phone connected via USB. No additional configuration is needed to run the app on an emulator.

### Running on Android Phone
To run on an Android phone, you will need to change the following line in `FlaskBackendConnect.kt` from
``` Kotlin
    private val urlPrefix = emulatorUrl
```
to 
``` Kotlin
    private val urlPrefix = adbUrl
```
And, with the phone connected, run the command `adb reverse tcp:5000 tcp:5000` in your terminal.

### Running with Cardknox SDK Enabled
The `master` branch currently does not include the payment processing SDK, provided by Cardknox. This is because the Cardknox SDK has a dependency not included in the ABI used by x86 Android emulators. The Cardknox SDK can be used on ARM Android emulators or on actual Android devices.

The `cardknox_dev` branch includes the Cardknox SDK and implementation within the app. To use it, you will need a valid Cardknox API key. Add the API key to the bottom of the `local.properties` file in the Harvest4UsMOBILE folder like this:
```
cardknox_api_key=key
```
Where `key` is the API key provided by Cardknox. Clean and rebuild the project in Android Studio and the Cardknox UI will be enabled. Note that the `local.properties` file should not ever be checked into version control.

# Release Notes

## Version 0.4.0


### **Features**
* Develop the Farms page along with individual farmer's profiles to display their produce
* Updated the UI of the cart page and it's ability to display produce
* Integrated Cardknox into the checkout page

### **Bug Fixes**
* App no longer crashes upon selecting a particular farm, and shows the produce they sell
* Spacing on the marketplace page is now working as intended
* All cart buttons in the app are functional and navigate the user to the cart

### **Known Issues**
* Cardknox SDK cannot be integrated into x86 emulators, so a separate branch is necessary to hold these changes

## Version 0.3.0


### **Features**
* Marketplace shows produce details from the backend 
* Marketplace UI redesign for produce cards (now includes photos of produce) 
* Cart button allows users to view items in their cart 

### **Bug Fixes**
* Farm button doesn’t crash app and takes users to the farm page now 

### **Known Issues**
* Spacing issues on the marketplace page  
* Farms display is incorrect and the app crashes when trying to click on individual farms

## Version 0.2.0


### **Features**
* Fully functional backend now linked to the remote AWS database
* Developed authentication functionality
* Developed the "Register" capability
* Added category titles to the Resource page so articles could be grouped appropriately
* Transitioned away from the side bubble navigation system and implemented a bottom navigation bar

### **Bug Fixes**
* Checkout page doesn't crash app when loaded
* Educational section now includes categories 

### **Known Issues**
* The bottom navigation bar does not work for the 'farms' button and will cause the app to crash if selected
* There is currently no way to log out of the app other than using the back button
* There was some technical debt accured when adding the current navigation bar and Flask backend implementations
* Log out button takes users to the market place instead of log in screen 

## Version 0.1.0 


### **Features**
* Implemented new UI and color scheme
* Added an educational resources page
* Transitioned old backend from boto3 framework to Flask
* Started the AWS RDS MySQL database

### **Bug Fixes**
* The app used to not allow you to create an account because the backend database wasn't set up
* Can now get past the login in screen and navigate the app

### **Known Issues**
* The reset password function was not implemented for producer accounts
* App crashes when trying to view cart
* Categories for the educational resources section hasn't been implemented yet


