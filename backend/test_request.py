import requests

resp = requests.get("http://127.0.0.1:5000/get_users/")
print(resp.json())

resp1 = requests.post("http://127.0.0.1:5000/login/", json = {"username": "fff", "password": "ggg"})
print(resp1.status_code) # should be 200

resp2 = requests.post("http://127.0.0.1:5000/login/", json = {"username": "fff", "password": "gggg"})
print(resp2.status_code) # should be 401