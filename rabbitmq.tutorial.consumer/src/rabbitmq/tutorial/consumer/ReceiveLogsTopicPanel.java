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

public class ReceiveLogsTopicPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtExchange;
	private JTextArea txtResultAllLog;
	private JTextArea txtResultKernelLog;
	private JTextArea txtResultCriticalLog;

	public ReceiveLogsTopicPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setBounds(12, 13, 57, 15);
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblExchange);

		txtExchange = new JTextField();
		txtExchange.setBounds(75, 10, 400, 21);
		txtExchange.setText("topic_logs");
		txtExchange.setColumns(10);
		add(txtExchange);

		JButton btnLogAll = new JButton("ALL 로그 #");
		btnLogAll.setBounds(12, 52, 250, 23);
		btnLogAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveAllLog();
					btnLogAll.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnLogAll);

		JScrollPane scrollPaneAll = new JScrollPane();
		scrollPaneAll.setBounds(12, 81, 250, 223);
		add(scrollPaneAll);

		txtResultAllLog = new JTextArea();
		scrollPaneAll.setViewportView(txtResultAllLog);

		JScrollPane scrollPaneKernel = new JScrollPane();
		scrollPaneKernel.setBounds(274, 81, 250, 223);
		add(scrollPaneKernel);

		txtResultKernelLog = new JTextArea();
		scrollPaneKernel.setViewportView(txtResultKernelLog);

		JButton btnLogKernel = new JButton("KERNEL 로그 kernel.*");
		btnLogKernel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveKernelLog();
					btnLogKernel.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnLogKernel.setBounds(274, 52, 250, 23);
		add(btnLogKernel);

		JScrollPane scrollPaneCritical = new JScrollPane();
		scrollPaneCritical.setBounds(536, 81, 250, 223);
		add(scrollPaneCritical);

		txtResultCriticalLog = new JTextArea();
		scrollPaneCritical.setViewportView(txtResultCriticalLog);

		JButton btnLogCritical = new JButton("CRITICAL 로그 *.critical");
		btnLogCritical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiveCriticalLog();
					btnLogCritical.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnLogCritical.setBounds(536, 52, 250, 23);
		add(btnLogCritical);

	}

	/**
	 * 모든 로그 수신
	 * 
	 * @throws Exception
	 */
	private void receiveAllLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // topic-logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "#");
		txtResultAllLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultAllLog
			.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}

	/**
	 * Kernel 로그만 수신
	 * 
	 * @throws Exception
	 */
	private void receiveKernelLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // topic-logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "kernel.*");
		
		txtResultKernelLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultKernelLog
			.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}

	/**
	 * 심각도가 error 인 로그만 수신
	 * 
	 * @throws Exception
	 */
	private void receiveCriticalLog() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // topic-logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
		queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, exchangeName, "*.critical");
		
		txtResultCriticalLog.append(" [*] Waiting for messages.\n");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			txtResultCriticalLog
			.append(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}

}
