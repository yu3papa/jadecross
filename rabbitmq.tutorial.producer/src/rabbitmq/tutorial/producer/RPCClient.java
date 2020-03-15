package rabbitmq.tutorial.producer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RPCClient implements AutoCloseable {

	private Connection connection;
	private Channel channel;
	private String requestQueueName; // rpc_queue

	public RPCClient(ProducerMain mainFrame, String queueName) throws IOException, TimeoutException {
		this.requestQueueName = queueName;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mainFrame.txtURL.getText());// jadecross.iptime.org
		factory.setPort(Integer.parseInt(mainFrame.txtPORT.getText()));// 5672
		factory.setUsername(mainFrame.txtUSER.getText());// rabbitmqadm
		factory.setPassword(mainFrame.txtPWD.getText());// jadecross

		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	public String call(String message) throws IOException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();

		String replyQueueName = channel.queueDeclare().getQueue();
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
				.build();

		channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

		final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

		String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response.offer(new String(delivery.getBody(), "UTF-8"));
			}
		}, consumerTag -> {
		});

		String result = response.take();
		channel.basicCancel(ctag);
		return result;
	}

	public void close() throws IOException {
		connection.close();
	}
}
