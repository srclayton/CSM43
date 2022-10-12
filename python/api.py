import cryptocodeLocal
from flask import Flask
from flask import request

import json
app = Flask(__name__)

@app.route('/password_Authentication')
def password_Authentication():
    #TODO checkLogin


@app.route('/registrationForm', methods=['GET'])
def registrationForm():
    name = request.args.get("name")
    email = request.args.get("email")
    password = request.args.get("password")
    team = request.args.get("team")
    su = request.args.get("su")
    try:
        if(cryptocodeLocal.registreUser(name,email,password,team,su)):
            return json.dumps({"cod":"200","mensagem":"Usuário registrado"})
        else:
            return json.dumps({"cod":"404","mensagem":" SRO-019: Objeto inválido"})
    except:
         return json.dumps({"cod":"404","mensagem":" SRO-019: Objeto inválido"})




if __name__ == '__main__':
    app.run()

