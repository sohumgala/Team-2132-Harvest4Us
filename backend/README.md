## Backend Setup

First, set up database authentication, then you'll be able to run the Flask backend, which is connected to the Harvest4Us AWS RDS MySQL Database

### Installation Steps
1. Install Python (version 3 or later)
2. Install pip (Python package installation manager)
3. Run `pip install flask` to install Flask
4. Run `pip install mysql-connector-python` to install the library that allows the backend to connect to the database
5. Run `pip install requests` to install the library that allows for making HTTP requests (necessary to run `test_request.py`)


### Set up Database Authentication
1. Create `db_auth.json` in this directory as specified by db-info.docx in team files (be sure not to commit this file, as it contains the database password).

### Run Flask
1. CD into the 'backend' folder
2. Run `export FLASK_APP=main` from the terminal
3. Start the backend by running `flask run`