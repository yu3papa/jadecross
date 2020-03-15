package rabbitmq.tutorial.consumer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RpcServerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtQueue;
	private JTextArea txtResult;

	private static int fib(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

	public RpcServerPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setBounds(12, 13, 57, 15);
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblQueue);

		txtQueue = new JTextField();
		txtQueue.setBounds(75, 10, 400, 21);
		txtQueue.setText("rpc_queue");
		txtQueue.setColumns(10);
		add(txtQueue);

		JButton btnReceive = new JButton("RECV");
		btnReceive.setBounds(12, 41, 97, 23);
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receive();
//					btnReceive.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnReceive);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(121, 40, 661, 273);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

	}

	/**
	 * RPC 서버 역할 - 피보나치 수열 계산
	 * 
	 * @throws Exception
	 */
	private void receive() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, RPC_QUEUE_NAME;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		RPC_QUEUE_NAME = txtQueue.getText(); // hello

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
			channel.queuePurge(RPC_QUEUE_NAME);

			channel.basicQos(1);

			txtResult.append(" [x] Awaiting RPC requests\n");

			Object monitor = new Object();
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
						.correlationId(delivery.getProperties().getCorrelationId()).build();

				String response = "";

				try {
					String message = new String(delivery.getBody(), "UTF-8");
					int n = Integer.parseInt(message);

					txtResult.append(" [.] fib(" + message + ")\n");
					response += fib(n);
				} catch (RuntimeException e) {
					txtResult.append(" [.] " + e.toString() + "\n");
				} finally {
					channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps,
							response.getBytes("UTF-8"));
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					// RabbitMq consumer worker thread notifies the RPC server owner thread
					synchronized (monitor) {
						monitor.notify();
					}
				}
			};

			channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
			}));
			// Wait and be prepared to consume the message from RPC client.
			while (true) {
				synchronized (monitor) {
					try {
						monitor.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}