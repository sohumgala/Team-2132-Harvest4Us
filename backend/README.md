## Backend Setup

First, set up database authentication, then you'll be able to run the Flask backend, which is connected to the Harvest4Us AWS RDS MySQL Database

### Set up Database Authentication
1. Create `db_auth.json` in this directory as specified by db-info.docx in team files (be sure not to commit this file, as it contains the database password).

### Run Flask
1. CD into the 'backend' folder
2. Run `export FLASK_APP=main` from the terminal
3. Start the backend by running `flask run`