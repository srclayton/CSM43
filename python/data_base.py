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
def insertOne(name,email,password,team,su):
    last_id = CLIENT[DB]["User"].find({}, {"_id": 1}, sort=[('_id', -1)]).limit(sys.maxsize).next()
    last_id["_id"] += 1
    payload = {
            "_id":last_id["_id"],
            "su":su,
            "name": name,
            "email":email,
            "password":password,
            "team": team,
            "registerDate": datetime.today().strftime('%Y-%m-%d %H:%M')
        }
    try:
        response = CLIENT[DB]["User"].insert_one(payload)
        return True
    except:
        return False

#if __name__ == '__main__':
    #app.run()
