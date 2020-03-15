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

public class EmitLogPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ProducerMain mainFrame;
	private JTextField txtExchange;
	private JTextField txtMessage;
	private JTextArea txtResult;

	/**
	 * Create the panel.
	 */
	public EmitLogPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExchange.setBounds(12, 13, 57, 15);
		add(lblExchange);

		txtExchange = new JTextField();
		txtExchange.setText("logs");
		txtExchange.setColumns(10);
		txtExchange.setBounds(75, 10, 450, 21);
		add(txtExchange);

		txtMessage = new JTextField();
		txtMessage.setText("info: Hello World!");
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
		btnSend.setBounds(12, 72, 97, 23);
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 105, 513, 221);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

	}

	/**
	 * RabbitMQ로 메세지 Publish
	 * 
	 * @throws Exception
	 */
	private void send() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, message;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // hello
		message = txtMessage.getText(); // info: Hello World!

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection();
	             Channel channel = connection.createChannel()) {
	            channel.exchangeDeclare(exchangeName, "fanout");
//	            String message = argv.length < 1 ? "info: Hello World!" : String.join(" ", argv);

	            channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
	            txtResult.append(" [x] Sent '" + message + "'\n");
	        }
	}

}
