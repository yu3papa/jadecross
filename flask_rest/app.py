import os
from flask import Flask
from flask import render_template
from flask_jwt import JWT
from models import db, Fcuser
from api_v1 import api as api_v1

app = Flask(__name__)
app.register_blueprint(api_v1, url_prefix='/api/v1')


@app.route('/')
def hello():
    return render_template('home.html')


@app.route('/login')
def login():
    return render_template('login.html')


@app.route('/register', methods=['GET', 'POST'])
def register():
    return render_template('register.html')


basedir = os.path.abspath(os.path.dirname(__file__))
dbfile = os.path.join(basedir, 'db.sqlite')

# app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + dbfile
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:jadecross@jadecross.iptime.org:5432/postgres'
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'jadecross'

# DBMS 설정
db.init_app(app)
db.app = app
db.create_all()


def authenticate(username, password):
    user = Fcuser.query.filter(Fcuser.userid == username).first()
    if user.password == password:
        return user


def identity(payload):
    print('payload')
    print(payload)
    id = payload['identity']
    return Fcuser.query.filter(Fcuser.id == id).first()


jwt = JWT(app, authenticate, identity)


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=True)
