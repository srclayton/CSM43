import data_base
import cryptocode


def checkLogin(username, password):
    response = data_base.findOne("email", username)
    if(response):
        if(cryptocode.decrypt(response["password"], username) == password):
            return response
        else: 
            return None
    else:
        return None

def registreUser(name,email,password,team,su):
    response = data_base.findOne("email", email)
    if(response):
        return None
    response = data_base.insertOne(name, email, cryptocode.encrypt(password,email), team ,su)
    if(response is None):
        return None
    else:
        return response

