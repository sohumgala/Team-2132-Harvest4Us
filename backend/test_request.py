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