import time
import psycopg2
from flask import Flask, request

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello Flask...'
    
@app.route('/app_sleep')
def app_sleep():
    sleepSec = int(request.args.get('sleepSec'))
    time.sleep(sleepSec)
    return 'Response Time : {}'.format(sleepSec)

@app.route('/db_sleep')
def db_sleep():
    sleepSec = request.args.get('sleepSec')

    conn_string = "host='220.95.250.70' port='5432' dbname ='postgres' user='postgres' password='jadecross'"
    conn = psycopg2.connect(conn_string)
    cur = conn.cursor()

    cur.execute("SELECT pg_sleep({})".format(sleepSec))
    rs = cur.fetchall()
    conn.close()

    return 'Response Time : {}'.format(sleepSec)

if __name__ == '__main__':
    # app.run(debug=True)
    app.run()
