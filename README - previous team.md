# Harvest4Us Version 1.0

## Release Notes


### **Version 1.0 Features**

* **Web Application for Producers**:
  * *Register*
  * *Login* 
  * *Complete a profile* 
  * *Listing inventory items* 
  * *View pending transactions* 
  * *Accept or decline transactions* 
* **Consumer**:
  * *Register* 
  * *Login* 
  * *View producer profiles*
  * *View items in the marketplace* 
  * *Search for items in the marketplace*
  * *Filter items by location*
  * *List food allergies*
  * *View item attributes* 
  * *Add to cart*
  * *Delete from cart* 
  * *Change item quantity* 
  * *Reading list of resources* 
  * *Account recovery*

### **Version 1.0 Bug Fixes**

#### Mobile Application Bug Fixes
* **API Response Sync Waiting**: 
  * The app used to attempt to use a response without waiting for the API call to finish.
* **Random Duplicate File Generation**:
  * The app would sometimes generate duplicate layout files.
* **API Response Sync Waiting**: 
  * The app used to attempt to use a response without waiting for the API call to finish. 

#### API Bug Fixes
* **Cart quantity checking**: 
  * Cart-api refactored to reject negative quantities and restrict the quantity of one item to its listed maximum. 
* **Refactoring from noSQL to SQL**:
  * The entire database was refactored from AWS DynamoDB to AWS RDS. The backend now uses SQL queries instead of an attribute expressions. 
* **Updating Only One Table For Producers**:
  * A check was added so that if only the user or producer_users table is getting updated, the update SQL query for the other table was not executed in _backend/producer-api/scripts/updateProfile.updateProfile_
* **Undefined Maxmimum Distance Filter**:
  * An option undefined distance of 'X' was added to _inventory-api/scripts/filterItem.filterItem_ to handle when the consumer does not want to filter by disnace. 
* **Commas Causing Truncated Description**:
  * The regular expression processing in _producer-api/scripts/updateProfile.unwrap_ was updated to include commas to avoid description truncation for producers.
* **Empty Cart Returns 404 Error**:
  * If a consumer does not have anything in their cart, the Cart-Api will return a 200 status with an empty array instead of a 404 with an error message.

### **Version 1.0 Bugs/Defects**
* **Date Formatting**:
  * Dates passed to all APIs are checked for correct formating. These could lead to a query error if _"year-month-day"_ format not followed.
* **Valid Address Checking**:
  * Inputed addresses are not checked with Google Distance Matrix for validity. If not valid address, will result in a BAD REQUEST error from google in _backend/inventory-api/scripts/filterItem.makeRequest_
* **Valid Email Address**:
  * Inputed emails are not validated after registering. If not valid email, this will lead to an error in _backend/consumer-api/scripts/resetPassword.send_email_
* **Producer Reset Password**:
  * The reset password function was not implemented for producer accounts.
* **Valid Produce Categories Table**:
  * A table of all valid produce categories was not implemented. 
* **Apostrophe Not Accepted For Producer Description**:
  * Currently apostrophe's are not accepted as valid characters in business descriptions.

## **Installation and Project Setup Guide**

_Before you are able to launch the project, you must complete all the steps in the following guides_

### **Download Instructions**

Access the [Version 1.0 master branch]( https://github.com/cry-queen/1132_orcaX) above  

Click the green **Code** button, then navigate click on **Download Zip**. Proceed to extract the files.  

### **Setup/Install Guide for Backend**

   1. **Database setup and configuration**:
     -  Follow the [following guide](https://www.youtube.com/watch?v=ciRbXZqBl7M) to create your database instance
       -  Enter for the name of your database: harvest4us
       -  Make sure to save the admin name and password
     - Copy your database ARN from the configure tab and replace MY_CLUSTER_ARN with this value
     - Follow the [following guide](https://rdspg.workshop.aws/lab1-create/task3.html) to create your secret arn. This will be used for each call to the database.
       - In each backend file, be sure to replace MY_SECRET_ARN with your secret arn value
     - Enter the RDS query editor and enter your database credentials
     - Navigate to _backend/sql-table-setup_
       - Load the _schema.sql_ and _dummy-data.sql_ into the editor and run the code. 
   2. **Setting up Google distance API**:
     - Use the [following guide](https://developers.google.com/maps/documentation/distance-matrix/get-api-key) to setup your Google Developer Account and get a API access key. Once you get your access key insert replace the MY_API_KEY value in _inventory-api/scripts/filterItem_. 
   3. **Creating the APIs using serverless**:
     - Use the [following guide](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) to get the AWS CLI installed on your device.
     - Use the [following guide](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-quickstart.html) to link your AWS account to your terminal. 
     - Use the [following guide](https://www.serverless.com/framework/docs/getting-started) to get serverless installed in your current directoty
     - For each API, run the command _sls deploy_ in the same directory as each _serverless.yml_ file to create each API 
       - Save the url endpoints for each


_Congrats! You now have set up the APIs for Harvest4Us. Now you need to input the endpoints you just created into the front-end. But before you can do that, you need to set the front-end up!_

### **Install Guide for Consumer Side (Android)**

**Pre-requisites**: 
* **Android Studio**:
   1. Navigate to the [Android Studio download page](https://developer.android.com/studio/) and follow the instructions to download and [install Android Studio](https://developer.android.com/studio/install.html). 
   2. Accept the default configurations for all steps and ensure that all components are selected for installation. 
   3. After the install is complete, the setup wizard downloads and installs additional components, including the Android SDK. Be patient, because this process might take some time, depending on your internet speed. 
   4. When the installation completes, Android Studio starts, and you are ready to use the application. 
* **Emulator**: (Step 4 on [this tutorial page](https://developer.android.com/codelabs/build-your-first-android-app-kotlin#2))
   _In this task, you will use the [Android Virtual Device (AVD) manager](http://developer.android.com/tools/devices/managing-avds.html) to create a virtual device (or emulator) that simulates the configuration for a particular type of Android device. The first step is to create a configuration that describes the virtual device. _ 
    - In Android Studio, select **Tools > AVD Manager**, or click the AVD Manager icon in the toolbar.  
    - Click **+Create Virtual Device**. (If you have created a virtual device before, the window shows all of your existing devices and the **+Create Virtual Device** button is at the bottom.) The **Select Hardware** window shows a list of pre-configured hardware device definitions. 
    - Choose a device definition, such as **Pixel 2**, and click **Next**. 
    - In the **System Image** dialog, from the **Recommended** tab, choose the latest release. 
    - If a **Download** link is visible next to a latest release, it is not installed yet, and you need to download it first. If necessary, click the link to start the download, and click **Next** when it's done. This may take a while depending on your connection speed. 
       - _**Note**: System images can take up a large amount of disk space, so just download what you need._
    - In the next dialog box, accept the defaults, and click **Finish**. 
       - The AVD Manager now shows the virtual device you added. 
    - If the **Your Virtual Devices AVD Manager** window is still open, go ahead and close it.  
* [Using an Android phone](https://developer.android.com/studio/run/device) (alternative for emulator)  
    - On the device, open the **Settings** app, select **Developer options**, and then enable **USB debugging** (if applicable). 
      - _**Note**: If you do not see Developer options, follow the instructions to [enable developer options]( https://developer.android.com/studio/debug/dev-options)._ 
    - Set up your system to detect your device. 
      - Chrome OS: No additional configuration required. 
      - macOS: No additional configuration required. 
      - Windows: Install a USB driver for ADB (if applicable). For an installation guide and links to OEM drivers, see the [Install OEM USB drivers]( https://developer.android.com/studio/run/oem-usb) document. 
      - Ubuntu Linux: There are two things that need to be set up correctly: each user that wants to use adb needs to be in the plugdev group, and the system needs to have udev rules installed that cover the device. 
      - plugdev group: If you see an error message that says you're not in the plugdev group, you'll need to add yourself to the plugdev group: 
         - _sudo usermod -aG plugdev $LOGNAME_
         - _**Note**: groups only get updated on login, so you'll need to log out for this change to take effect. When you log back in, you can use id to check that you're now in the plugdev group._
      - udev rules: The android-sdk-platform-tools-common package contains a community-maintained default set of udev rules for Android devices. To install: 
         - _apt-get install android-sdk-platform-tools-common_
* **Setup Credentials**:
   - Replace the value PAYPAL_CLIENT_ID in QuickStartConstants.kt with your credentials information. 
   - Replace username and password in build.gradle(project) with your credentials information. 

**Troubleshooting**: 
* A known bug is that sometimes upon running the mobile app, duplicate build files are created. Android Studio doesn’t know which copy to read in the code, so it will fail to run. To solve this, view the “Problems” tab at the bottom of the window. The error code typed in red will tell you where in the folder the duplicate files are coming from. Usually the file names will end in a “2” (ex. “BuildCartActivity” and “BuildCartActivity 2”). Navigate to that folder and delete all the duplicate files. You may have to repeat this process more than once depending on how many places the duplicates are stored. 

#### **Further Consumer Download Directions**

* Extract the files from the zip. Then open Android Studio.
* In Android Studio, navigate to File > Open 
* Navigate to the newly downloaded and extracted files, then click on **harvest4usMOBILE**, and click **OK** 
* Graphical user interface, text, application
* Description automatically generated 
  * If necessary, click Trust to continue 
* Wait for the initial build to finish – this may take a few minutes 
  * There is a progress bar at the very bottom right, and just wait until the progress bar goes away 
* **Build & Run**:
  * Click Run in the top right of Android Studio to [build and run your app on the device]( https://developer.android.com/studio/run). 

### **Install Guide for Producer Side (Web)**

* Check if you have Node.js and npm installed on your system by running the following commands in your command line: 
  *  _node –v_
  *  _npm –v_
  * If you do not have Node and/or npm installed, the package can be downloaded and installed from the Node.js website. 
* Once you have Node in working order, open Visual Studio Code.  
* Then Open > harvest4usWEB. 
* When the project is open, open the Terminal menu on the top menu bar, and select ‘New Terminal.’ 
* In the terminal, type ‘npm -install’ and hit enter. This will install all project dependencies, including the relevant libraries. 

**Libraries Used In This Project Include**: 
  -   React.js (A Javascript interaction library) 
  -   Formik (A React library used for form validation) 
  -   Yup (A validation library supplementary to Formik) 
  -   React Bootstrap (A UI library for React) 
  -   Axios (An HTTP request processing library) 
  -   Type ‘npm start’ in the terminal to open the project in your browser. 

_**Note**: that the first time the project compiles, it may take a moment to load._

**Producer Side – Replacing API Links**

_The following links will need to be replaced once tables are redeployed. CSV samples of these tables have been included in the database_tables folder. Specific parameters for these endpoints are listed in the Design Document and the corresponding backend files._

| Type        | URL                                                                                       |Draws from Which Table? | 
| ----------- | ------------------------------------------------------------------                        | ---------------------- |
| POST        | https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/new-user                       | users                  |
| GET         | https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/new-user/{username||password}  | users                  |
| GET         | https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/get-producers                  | producer_users         | 
| GET         | https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/get-producers/{username}       | producer_users         | 
| POST        | https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/update-profile                 | users, producer_users (updates one    or both depending on parameters) |
| GET         | https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-id/{producer||id}       | inventory               |
| GET         | https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-producer/{producer}     | inventory               |
| POST        |  https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/add-item                      | inventory               |
| POST        | https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/update-item                    | inventory               |
| POST        | https://naniidtff6.execute-api.us-east-1.amazonaws.com/dev/evaluate-order                 | orders                  | 
| POST        | https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/delete-item                    | inventory               | 
