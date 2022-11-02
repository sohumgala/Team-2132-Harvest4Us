import requests

resp = requests.get("http://127.0.0.1:5000/get_users/")
print(resp.json())

resp1 = requests.post("http://127.0.0.1:5000/login/", json = {"username": "fff", "password": "ggg"})
print(resp1.status_code) # should be 200

resp2 = requests.post("http://127.0.0.1:5000/login/", json = {"username": "fff", "password": "gggg"})
print(resp2.status_code) # should be 401

resp3 = requests.post("http://127.0.0.1:5000/register/", json = {"username": "fff", "first_name": "dummy_first", "last_name": "dummy_last", "password": "dummy_pass"})
print(resp3.status_code) # should be 401

resp4 = requests.post("http://127.0.0.1:5000/register/", json = {"username": "ggg", "first_name": "dummy_first", "last_name": "dummy_last", "password": "dummy_pass"})
print(resp4.status_code) # should be 200 (only for the first time it's run, then 401)

resp5 = requests.get("http://127.0.0.1:5000/get_all_produce/")
print(resp5.json())
print(resp5.status_code)

resp6 = requests.get("http://127.0.0.1:5000/get_all_farms/")
print(resp6.json())
print(resp6.status_code)

resp7 = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp7.json())
print(resp7.status_code)

resp8 = requests.post("http://127.0.0.1:5000/get_produce_by_producer/", json = {"business_name": "A Business"})
print(resp8.json())
print(resp8.status_code)

# Tests for change_quantity - look at output of get_cart to see the changes
# Note this set of operations takes the database back to its original state
# delete item
resp9 = requests.post("http://127.0.0.1:5000/change_quantity/", json = {"business_name": "A Business", "product_id": 1, "username": "fff", "new_quantity": 0})
print(resp9.status_code)

resp_cart = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp_cart.json())

# re add item
resp10 = requests.post("http://127.0.0.1:5000/change_quantity/", json = {"business_name": "A Business", "product_id": 1, "username": "fff", "new_quantity": 3})
print(resp10.status_code)

resp_cart = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp_cart.json())

# increase quantity
resp11 = requests.post("http://127.0.0.1:5000/change_quantity/", json = {"business_name": "A Business", "product_id": 1, "username": "fff", "new_quantity": 4})
print(resp11.status_code)

resp_cart = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp_cart.json())

# decrease quantity
resp12 = requests.post("http://127.0.0.1:5000/change_quantity/", json = {"business_name": "A Business", "product_id": 1, "username": "fff", "new_quantity": 3})
print(resp12.status_code)

resp_cart = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp_cart.json())

# add more than available (nop)
resp13 = requests.post("http://127.0.0.1:5000/change_quantity/", json = {"business_name": "A Business", "product_id": 1, "username": "fff", "new_quantity": 50})
print(resp13.status_code)

resp_cart = requests.post("http://127.0.0.1:5000/get_cart/", json = {"username": "fff"})
print(resp_cart.json())
