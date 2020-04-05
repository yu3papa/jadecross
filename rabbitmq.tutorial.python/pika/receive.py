#!/usr/bin/env python
import pika

credentials = pika.PlainCredentials('rabbitmqadm', 'jadecross')
parameters = pika.ConnectionParameters('jadecross.iptime.org', 5672, '/', credentials)
# connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
connection = pika.BlockingConnection(parameters)
channel = connection.channel()

channel.queue_declare(queue='hello')


def callback(ch, method, properties, body):
    print(" [x] Received %r" % body)

channel.basic_consume(queue='hello', on_message_callback=callback, auto_ack=True)

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()
