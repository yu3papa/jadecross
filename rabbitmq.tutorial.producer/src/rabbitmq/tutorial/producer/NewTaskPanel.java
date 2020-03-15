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
import com.rabbitmq.client.MessageProperties;
import java.awt.Font;
import java.awt.Color;

public class NewTaskPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ProducerMain mainFrame;
	private JTextField txtQueue;
	private JTextField txtMessage;
	private JTextArea txtResult;

	/**
	 * Create the panel.
	 */
	public NewTaskPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQueue.setBounds(12, 13, 57, 15);
		add(lblQueue);

		txtQueue = new JTextField();
		txtQueue.setText("task_queue");
		txtQueue.setColumns(10);
		txtQueue.setBounds(75, 10, 450, 21);
		add(txtQueue);

		txtMessage = new JTextField();
		txtMessage.setText("1'st Task...");
		txtMessage.setColumns(10);
		txtMessage.setBounds(75, 41, 450, 21);
		add(txtMessage);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMessage.setBounds(12, 44, 57, 15);
		add(lblMessage);

		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					send();
				} catch (Exception ex) {
				}
			}
		});
		btnSend.setBounds(12, 92, 97, 23);
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 125, 513, 200);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);
		
		JLabel lblNewLabel = new JLabel("메세지에 포함된 점(.) 한개당 1초 작업시간 소요됨");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("굴림", Font.ITALIC, 12));
		lblNewLabel.setBounds(75, 67, 400, 15);
		add(lblNewLabel);

	}

	/**
	 * RabbitMQ로 메세지 Publish
	 * 
	 * @throws Exception
	 */
	private void send() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName, message;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueue.getText(); // hello
		message = txtMessage.getText(); // Hello World

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
					message.getBytes(StandardCharsets.UTF_8));
			txtResult.append(" [x] Sent '" + message + "' for " + queueName + " QUEUE\n");
		}
	}

}
