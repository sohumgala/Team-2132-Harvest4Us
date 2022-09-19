from flask import Flask, request, Response
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

# gets json representation of users table
@app.route("/get_users/", methods=["GET"])
def get_users():
    users = select("select * from users")
    Response(json.dumps({"users": users}), status = 200)
    return json.dumps({"users": users})

# checks if a (username, password) 
@app.route("/login/", methods=["GET"])
def login():
    data = request.get_json()
    res = select("select * from users where username = %s and password = %s", (data["username"], data["password"]))
    if len(res) == 0:
        return Response("{}", status = 401)
    else:
        return Response("{}", status = 200)

if __name__ == "main":
    with open("db_auth.json", "r") as f:
        db_auth_data = json.load(f)
    
    connection = mysql.connector.connect(
        user = db_auth_data["user"],
        password = db_auth_data["password"],
        host = db_auth_data["host"],
        port = db_auth_data["port"],
        database = db_auth_data["database"]
    )
    db = connection.cursor()
    
    app.run()