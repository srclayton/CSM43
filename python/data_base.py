import pymongo
import json
from flask import Flask
from flask import request
from datetime import datetime
import sys
DB = "CSM43"
a = open("uri.txt","r")
URI = a.read()
a.close()
CLIENT = pymongo.MongoClient(URI)


#app = Flask(__name__)

#@app.route('/test', methods=['GET'])
def delete( idItem, type):
    print(idItem+ " " + type)
    result = CLIENT[DB][type].update_one({"_id":int (idItem)},{"$set":{"active":False}})
    return result.acknowledged

def restore( idItem, type):
    print(idItem+ " " + type)
    result = CLIENT[DB][type].update_one({"_id":int (idItem)},{"$set":{"active":True}})
    return result.acknowledged

def find(field, value, collection):
#def find():
    result = CLIENT[DB][collection].find({field:value})
    strResponse = []
    
    for x in result:
        strResponse.append(x)
    try:
        if(strResponse):
            strResponse = json.dumps(strResponse)
            jsonResponse = json.loads(strResponse)
            return json.dumps(jsonResponse)
    except:
        return None

#def insertOne():
def insertOne(type, data):

    match type:
        case "registerUser":
            last_id = CLIENT[DB]["User"].find({}, {"_id": 1}, sort=[('_id', -1)]).limit(sys.maxsize).next()
            last_id["_id"] += 1
            payload = {
                    "_id":last_id["_id"],
                    "su":data.pop(),
                    "name": data.pop(),
                    "email":data.pop(),
                    "password":data.pop(),
                    "team": data.pop(),
                    "registerDate": datetime.today().strftime('%Y-%m-%d %H:%M')
                }
            try:
                response = CLIENT[DB]["User"].insert_one(payload)
                return True
            except:
                return False

        case "insertFolder":
            last_id = CLIENT[DB]["Folder"].find({}, {"_id": 1}, sort=[('_id', -1)]).limit(sys.maxsize).next()
            last_id["_id"] += 1
            payload = {
                    "_id":last_id["_id"],
                    "folderName":data.pop(),
                    "idUser": int(data.pop()),
                    "idTeam":-1,
                    "registerDate": datetime.today().strftime('%Y-%m-%d %H:%M')
                }
            try:
                response = CLIENT[DB]["Folder"].insert_one(payload)
                return True
            except:
                return False
        case "insertCreditCard":
            last_id = CLIENT[DB]["CreditCard"].find({}, {"_id": 1}, sort=[('_id', -1)]).limit(sys.maxsize).next()
            last_id["_id"] += 1
            payload = {
                    "_id":last_id["_id"],
                    "idUser": int(data.pop()),
                    "idFolder": -1,
                    "surname":data.pop(),
                    "cardholderName":data.pop(),
                    "cardNumber": int(data.pop()),
                    "expirationDate":data.pop(),
                    "cvcCode":int(data.pop()),
                    "active":True,
                    "registerDate": datetime.today().strftime('%Y-%m-%d %H:%M'),
                    "type": "creditCard"
                }
            try:
                response = CLIENT[DB]["CreditCard"].insert_one(payload)
                return True
            except:
                return False
        case "insertCredential":
            last_id = CLIENT[DB]["Credential"].find({}, {"_id": 1}, sort=[('_id', -1)]).limit(sys.maxsize).next()
            last_id["_id"] += 1
            payload = {
                    "_id":last_id["_id"],
                    "idUser": int(data.pop()),
                    "idFolder": data.pop(),
                    "credentialName":data.pop(),
                    "username":data.pop(),
                    "password": data.pop(),
                    "note":data.pop(),
                    "active":True,
                    "registerDate": datetime.today().strftime('%Y-%m-%d %H:%M'),
                    "type": "credential"
                }
            try:
                response = CLIENT[DB]["Credential"].insert_one(payload)
                return True
            except:
                return False
#if __name__ == '__main__':
    #app.run()
