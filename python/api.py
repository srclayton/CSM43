from email import message
import json
import cryptocodeLocal
import data_base
from flask import Flask
from flask import request

import json
app = Flask(__name__)
@app.route('/test', methods=['GET'])
def test():
    return json.dumps({"cod":"200","mensagem":"test"})

@app.route('/fingerprintLogin', methods=['GET'])
def fingerprintLogin():
    email = request.args.get('email')
    response = data_base.find('email', email,'User')
    if(response):
        response = json.loads(response)
        response.append({'success': 'True', 'message': 'user found'})
        return json.dumps(response)
    else:
        return json.dumps({'success': 'False', 'message': 'user not found'})

@app.route('/password_Authentication', methods=['GET'])
def password_Authentication():
    email = request.args.get('email')
    password = request.args.get('password')
    response = cryptocodeLocal.checkLogin(email, password)
    if(response):
        response = json.loads(response)
        response.append({'success': 'True', 'message': 'user found'})
        return json.dumps(response)
    elif(response is False):
        return json.dumps({'success': 'False', 'message': 'The Username or Password is Incorrect'})
    else:
        return json.dumps({'success': 'False', 'message': 'user not found'})


@app.route('/registrationForm', methods=['POST'])
def registrationForm():
    name = request.args.get("name")
    email = request.args.get("email")
    password = request.args.get("password")
    team = request.args.get("team")
    su = request.args.get("su")
    response = cryptocodeLocal.registreUser(name,email,password,team,su)
    if(response):
        return json.dumps({'success': 'True', 'message': 'user inserted'})
    elif(response is False):
        return json.dumps({'success': 'False', 'message': 'The Username or Password is Incorrect'})
    else:
        return json.dumps({'success': 'False', 'message': 'user already exists'})


@app.route('/getItem', methods=['GET'])
def getItem():
    idUser = request.args.get("idUser")
    item = request.args.get("item")
    arg = item
    arg += " not found"
    response = data_base.find("idUser", int(idUser),item)
    if(response):
        response = json.loads(response)
        response.append({'success': 'True', 'message': 'folder found'})
        return json.dumps(response)
    else:
        return json.dumps({'success': 'False', 'message': arg})


@app.route('/insertItem', methods=['POST'])
def insertItem():
    type = request.args.get("type")
    match type:
        case "folder":
            folderName = request.args.get("folderName")
            idUser = request.args.get("idUser")
            parameters = list()
            parameters.append(idUser)
            parameters.append(folderName)
            response = data_base.insertOne("insertFolder", parameters)
            if(response):
                return json.dumps({'success': 'True', 'message': 'folder inserted'})
            else:
                return json.dumps({'success': 'False', 'message': 'folder not inserted'})
        case "creditCard":
            parameters = list()
            cvcCode = request.args.get("cvcCode")
            expirationDate = request.args.get("expirationDate")
            cardnumber = request.args.get("cardnumber")
            cardholdername = request.args.get("cardholdername")
            surname = request.args.get("surname")
            idUser = request.args.get("idUser") 
            parameters.append(cvcCode)
            parameters.append(expirationDate)
            parameters.append(cardnumber)
            parameters.append(cardholdername)
            parameters.append(surname)
            parameters.append(idUser)
            
            response = data_base.insertOne("insertCreditCard", parameters)
            if(response):
                return json.dumps({'success': 'True', 'message': 'card inserted'})
            else:
                return json.dumps({'success': 'False', 'message': 'card not inserted'})
        case "credential":
            parameters = list()
            parameters.append(request.args.get("note"))
            parameters.append(request.args.get("password"))
            parameters.append(request.args.get("username"))
            parameters.append(request.args.get("credentialName"))
            parameters.append(request.args.get("idFolder"))
            parameters.append(request.args.get("idUser"))
            response = data_base.insertOne("insertCredential", parameters)
            if(response):
                return json.dumps({'success': 'True', 'message': 'credential inserted'})
            else:
                return json.dumps({'success': 'False', 'message': 'credential not inserted'})

@app.route('/removeItem', methods=['POST'])
def removeItem():
    idItem = request.args.get("idItem")
    type = request.args.get("type")
    response = data_base.delete(idItem, type)
    if(response):
        return json.dumps({'success': 'True', 'message': 'item updated'})
    else:
        return json.dumps({'success': 'False', 'message': 'item not updated'})

@app.route('/restoreItem', methods=['POST'])
def restoreItem():
    idItem = request.args.get("idItem")
    type = request.args.get("type")
    response = data_base.restore(idItem, type)
    if(response):
        return json.dumps({'success': 'True', 'message': 'item updated'})
    else:
        return json.dumps({'success': 'False', 'message': 'item not updated'})

@app.route('/checkCreditCard', methods=['GET']):
def checkCreditCard():
    numberCard = request.args.get('numberCard')
    if(numberCard != 0):
        creditCard.checkCreditCard(numberCard)

if __name__ == '__main__':
    app.run()

