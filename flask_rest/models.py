from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


class Fcuser(db.Model):
    __tablename__ = 'fcuser'
    id = db.Column(db.Integer, primary_key=True)
    userid = db.Column(db.String(20))
    username = db.Column(db.String(20))
    password = db.Column(db.String(20))

    @property
    def serialize(self):
        return {
            'id': self.id,
            'userid': self.userid,
            'username': self.username,
            'password': self.password
        }
