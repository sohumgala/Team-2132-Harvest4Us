# Harvest4Us 
* Harvest4Us is an android application that connects local farmers to their community, making it easy for consumers to buy fresh produce.
# Release Notes


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


