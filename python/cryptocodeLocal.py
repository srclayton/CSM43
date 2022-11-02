import data_base
import cryptocode
import json


def checkLogin(username, password):
    response = data_base.find("email", username, "User")
    if(response):
        response = json.loads(response)
        if(cryptocode.decrypt(response[0]["password"], username) == password):
            return json.dumps(response)
        else: 
            return False
    else:
        return None

def registreUser(name,email,password,team,su):
    response = data_base.find("email", email, "User")
    if(response):
        return None
    response = data_base.insertOne(name, email, cryptocode.encrypt(password,email), team ,su)
    if(response is False):
        return None
    else:
        return response

