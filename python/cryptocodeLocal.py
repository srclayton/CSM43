import data_base
import cryptocode


def checkLogin(username, password):
    response = data_base.findOne("email", username)
    if(response):
        if(cryptocode.decrypt(response["password"], username) == password):
            return 1
        else: 
            return 0
    else:
        return 0

def registreUser(name,email,password,team,su):
    response = data_base.insertOne(name, email, cryptocode.encrypt(password,email), team ,su)
    if(response is None):
        return 0
    else:
        print(response)
        return 1
