import time
import cx_Oracle
from flask import Flask, request

app = Flask(__name__)

@app.route('/app_sleep')
def app_sleep():
    sleepSec = int(request.args.get('sleepSec'))
    time.sleep(sleepSec)
    return 'Response Time : {}'.format(sleepSec)

@app.route('/db_sleep')
def db_sleep():
    sleepSec = request.args.get('sleepSec')

    conn = cx_Oracle.connect("scott/jadecross@jadecross.iptime.org:1521/orcl")
    cursor = conn.cursor()
    # cursor.execute("select sysdate, fn_sleep_n(" + sleepSec + ") as sleepTime from dual")
    cursor.execute('select sysdate, fn_sleep_n(:1) as sleepTime from dual', [int(sleepSec)])
    rs = cursor.fetchall()
    conn.close()

    return 'Response Time : {}'.format(sleepSec)

if __name__ == '__main__':
    app.run(debug=True)
