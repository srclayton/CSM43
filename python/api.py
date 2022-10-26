import json
import cryptocodeLocal
from flask import Flask
from flask import request

import json
app = Flask(__name__)
@app.route('/test', methods=['GET'])
def test():
    return json.dumps({"cod":"200","mensagem":"test"})

@app.route('/password_Authentication', methods=['GET'])
def password_Authentication():
    email = request.args.get('email')
    password = request.args.get('password')
    response = cryptocodeLocal.checkLogin(email, password)
    if(response):
        return json.dumps({'success': 'True', 'message': 'user found'})
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
    else:

        return json.dumps({'success': 'False', 'message': 'user already exists'})

if __name__ == '__main__':
    app.run()

