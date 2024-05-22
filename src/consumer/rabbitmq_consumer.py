import pika
import mysql.connector

# Configurações do RabbitMQ
connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
channel = connection.channel()
channel.queue_declare(queue='locacaoQueue')

# Configurações do MySQL
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="edna",
    database="rentservice"
)
cursor = db.cursor()

def callback(ch, method, properties, body):
    message = body.decode('utf-8')
    print(f"Received {message}")

    # Parse message
    parts = message.split(", ")
    name = parts[0].split(": ")[1]
    dias_restantes = int(parts[1].split(": ")[1])

    # Insert into database
    sql = "INSERT INTO locacao_restante (name, dias_restantes) VALUES (%s, %s)"
    val = (name, dias_restantes)
    cursor.execute(sql, val)
    db.commit()

channel.basic_consume(queue='locacaoQueue', on_message_callback=callback, auto_ack=True)

print('Waiting for messages. To exit press CTRL+C')
channel.start_consuming()
