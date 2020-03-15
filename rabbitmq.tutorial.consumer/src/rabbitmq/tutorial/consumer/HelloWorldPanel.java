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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class HelloWorldPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtQueueName;
	private JTextArea txtResult;

	public HelloWorldPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setBounds(12, 13, 57, 15);
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblQueue);

		txtQueueName = new JTextField();
		txtQueueName.setBounds(75, 10, 400, 21);
		txtQueueName.setText("hello");
		txtQueueName.setColumns(10);
		add(txtQueueName);

		JButton btnReceive = new JButton("RECV");
		btnReceive.setBounds(22, 41, 97, 23);
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receive();
					btnReceive.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnReceive);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 74, 762, 243);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

	}

	/**
	 * RabbitMQ로 메세지 Consume
	 * 
	 * @throws Exception
	 */
	private void receive() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueueName.getText(); // hello

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, false, false, false, null);
		txtResult.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResult.append(" [x] Received '" + message + "'\n");
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}
