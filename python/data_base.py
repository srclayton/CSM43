import requests
import json
a = open("password.txt","r")
apiKey = a.read()
a.close()
headers = {
        'Content-Type': 'application/json',
        'Access-Control-Request-Headers': '*',
        'api-key': apiKey
    }
URL = "https://data.mongodb-api.com/app/data-guzuj/endpoint/data/beta/action/"

def insertOne(name, email, password, team ,su):
    url = URL  +"insertOne" 
    payload = json.dumps({
        "dataSource": "RochaESilvaDB",
        "database": "CSM43",
        "collection": "User",
        "document": {
            "su":su,
            "name": name,
            "email":email,
            "password":password,
            "team": team
        }
    })
    response = requests.request("POST", url, headers=headers, data=payload)
    return response

def findOne(field, value):
    url = URL  +"findOne"
    payload = json.dumps({
        "collection": "User",
        "database": "CSM43",
        "dataSource": "RochaESilvaDB",
        "filter": {
            field: value
        }
    })
    response = requests.request("POST", url, headers=headers, data=payload)
    resp = response.json()
    if(resp["document"] is None):
        return None
    else:
        return resp["document"]

def updateOne(userId: int, userTrackingNumber: int):
    jsonUpdate = findOne("_id",userId)
    jsonUpdate["user_tracking_number"].append(userTrackingNumber)
    url = URL + "updateOne"

    payload = json.dumps({
    "collection": "dboUsuario",
    "database": "Distribuidora",
    "dataSource": "RochaESilvaDB",
    "filter": {"_id": userId},
    "update": {
          "$set": {
              "user_tracking_number": jsonUpdate["user_tracking_number"],
          }
      }
    })
    response = requests.request("POST", url, headers=headers, data=payload)
    resp = response.json()    
    return resp

def deleteOne(userId: int, userTrackingNumber:int):
    jsonUpdate = findOne("_id",userId)
    if(jsonUpdate is None):
        return False
    i = 0
    for x in jsonUpdate["user_tracking_number"]:
         if(x == userTrackingNumber):
            del(jsonUpdate["user_tracking_number"][i])
         i += 1
    print(jsonUpdate["user_tracking_number"])
    url = "https://data.mongodb-api.com/app/data-guzuj/endpoint/data/beta/action/updateOne"

    payload = json.dumps({
    "collection": "dboUsuario",
    "database": "Distribuidora",
    "dataSource": "RochaESilvaDB",
    "filter": {"_id": userId},
    "update": {
          "$set": {
              "user_tracking_number": jsonUpdate["user_tracking_number"],
          }
      }
    })
    response = requests.request("POST", url, headers=headers, data=payload)
    resp = response.json()   
    print(resp) 
    return resp

def insertLog(id, userName, content):
    url = "https://data.mongodb-api.com/app/data-guzuj/endpoint/data/beta/action/insertOne"
    payload = json.dumps({
        "dataSource": "RochaESilvaDB",
        "database": "Distribuidora",
        "collection": "dboLogs",
        "document": {
            "_id": id, 
            "user_name": userName,
            "log":content,
        }
    })
    response = requests.request("POST", url, headers=headers, data=payload)