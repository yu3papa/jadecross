package rabbitmq.tutorial.producer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
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

public class HelloWorldPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ProducerMain mainFrame;

	private JTextField txtQueueName;
	private JTextField txtMessage;
	private JTextArea txtResult;

	public HelloWorldPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setBounds(12, 13, 57, 15);
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblQueue);

		txtQueueName = new JTextField();
		txtQueueName.setBounds(75, 10, 450, 21);
		txtQueueName.setText("hello");
		txtQueueName.setColumns(10);
		add(txtQueueName);

		txtMessage = new JTextField();
		txtMessage.setBounds(75, 41, 450, 21);
		txtMessage.setText("Hello World");
		txtMessage.setColumns(10);
		add(txtMessage);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(12, 44, 57, 15);
		lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblMessage);

		JButton btnSend = new JButton("SEND");
		btnSend.setBounds(12, 84, 97, 23);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					send();
				} catch (Exception ex) {
				}
			}
		});
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 117, 513, 200);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

	}

	/**
	 * RabbitMQ로 메세지 Publish
	 * @throws Exception
	 */
	private void send() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName, message;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueueName.getText(); // hello
		message = txtMessage.getText(); // Hello World

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(queueName, false, false, false, null);
			channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
			txtResult.append(" [x] Sent '" + message + "' for "+ queueName +" QUEUE\n");
		}
	}
}
