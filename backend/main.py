from flask import Flask, request, Response, jsonify
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
@app.route("/login/", methods=["POST"])
def login():
    data = request.get_json()
    fetch = select("select * from users where username = %s and password = %s", (data["username"], data["password"]))
    if len(fetch) == 0:
        return Response("{}", status = 401)
    else:
        return Response("{}", status = 200)

@app.route("/register/", methods=["POST"])
def register():
    data = request.get_json()
    fetch = select("select * from users where username = %s", (data["username"],))
    if len(fetch) != 0:
        return Response("{}", status = 401)
    else:
        modify("insert into users values (%s, %s, %s, %s)", (data["username"], data["first_name"], data["last_name"], data["password"]))
        return Response("{}", status = 200)

@app.route("/get_all_produce/", methods=["GET"])
def get_all_produce():
    produce = select("select product_id, business_name, produceType, produceCategory, unit, usdaGrade, active, availableQuantity, dateEdited, organic, price from producer_users inner join inventory on producer_users.username = inventory.producer where active = 1")
    return jsonify(produce), 200

@app.route("/get_all_farms/", methods=["GET"])
def get_all_farms():
    farms = select("select business_name, description, city, st from producer_users")
    return jsonify(farms), 200

@app.route("/get_cart/", methods=["POST"])
def get_cart():
    data = request.get_json()
    cart = select("select active_inventory.product_id, active_inventory.business_name, produceType, produceCategory, active_inventory.unit, active_inventory.usdaGrade, active_inventory.active, carts.quantity, active_inventory.dateEdited, active_inventory.organic, round(price * availableQuantity, 2) as 'itemPrice' from (select * from producer_users inner join inventory on producer_users.username = inventory.producer where active = 1) as active_inventory inner join carts on (carts.producer, carts.product_id) = (active_inventory.username, active_inventory.product_id) where carts.consumer = %s", (data["username"],))
    return jsonify(cart), 200

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