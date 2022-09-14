from flask import Flask
import json
import mysql.connector

app = Flask(__name__)

connection = None
db = None

# use for database retrieval queries
def select(sql: str, values: tuple = ()):
    db.execute(sql, values)
    return db.fetchall()

# use for database modification queries
def modify(sql: str, values: tuple = ()):
    db.execute(sql, values)
    connection.commit()

@app.route("/get_users/", methods=["GET"])
def get_users():
    users = select("select * from users")
    return json.dumps({"users": users})

if __name__ == "main":
    with open("db_auth.json", "r") as f:
        db_auth_data = json.load(f)

    print(db_auth_data)
    
    connection = mysql.connector.connect(
        user = db_auth_data["user"],
        password = db_auth_data["password"],
        host = db_auth_data["host"],
        port = db_auth_data["port"],
        database = db_auth_data["database"]
    )
    db = connection.cursor()
    
    app.run()