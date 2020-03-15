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

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirectPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtExchange;
	private JTextArea txtResultInfoLog;
	private JTextArea txtResultWarningLog;
	private JTextArea txtResultErrorLog;

	public ReceiveLogsDirectPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setBounds(12, 13, 57, 15);
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblExchange);

		txtExchange = new JTextField();
		txtExchange.setBounds(75, 10, 400, 21);
		txtExchange.setText("direct_logs");
		txtExchange.setColumns(10);
		add(txtExchange);

		JButton btnLogInfo = new JButton("INFO 로그");
		btnLogInfo.setBounds(12, 50, 125, 23);
		btnLogInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveInfoLog();
					btnLogInfo.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnLogInfo);

		JScrollPane scrollPaneInfo = new JScrollPane();
		scrollPaneInfo.setBounds(12, 83, 250, 223);
		add(scrollPaneInfo);

		txtResultInfoLog = new JTextArea();
		scrollPaneInfo.setViewportView(txtResultInfoLog);

		JScrollPane scrollPaneWarning = new JScrollPane();
		scrollPaneWarning.setBounds(274, 83, 250, 223);
		add(scrollPaneWarning);

		txtResultWarningLog = new JTextArea();
		scrollPaneWarning.setViewportView(txtResultWarningLog);

		JButton btnLogWarning = new JButton("WARNING 로그");
		btnLogWarning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveWarningLog();
					btnLogWarning.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnLogWarning.setBounds(274, 50, 125, 23);
		add(btnLogWarning);

		JScrollPane scrollPaneError = new JScrollPane();
		scrollPaneError.setBounds(536, 83, 250, 223);
		add(scrollPaneError);

		txtResultErrorLog = new JTextArea();
		scrollPaneError.setViewportView(txtResultErrorLog);

		JButton btnLogError = new JButton("ERROR 로그");
		btnLogError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveErrorLog();
					btnLogError.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnLogError.setBounds(536, 50, 125, 23);
		add(btnLogError);

	}

	/**
	 * 심각도가 info 인 로그만 수신
	 * 
	 * @throws Exception
	 */
	private void receiveInfoLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // direct-logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "info");
		txtResultInfoLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultInfoLog
					.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});

	}

	/**
	 * 심각도가 warning 인 로그만 수신
	 * 
	 * @throws Exception
	 */
	private void receiveWarningLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // direct-logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "warning");
		txtResultWarningLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultWarningLog
					.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

	/**
	 * 심각도가 error 인 로그만 수신
	 * 
	 * @throws Exception
	 */
	private void receiveErrorLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "error");
		txtResultErrorLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultErrorLog
					.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

}
