package rabbitmq.tutorial.producer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

import javax.swing.ButtonGroup;
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
import com.rabbitmq.client.MessageProperties;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.Box;
import java.awt.Canvas;

public class EmitLogTopicPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ProducerMain mainFrame;
	private JTextField txtExchange;
	private ButtonGroup facilityGroup;
	private ButtonGroup severityGroup;
	private JTextField txtMessage;
	private JTextArea txtResult;

	/**
	 * Create the panel.
	 */
	public EmitLogTopicPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExchange.setBounds(29, 13, 57, 15);
		add(lblExchange);

		txtExchange = new JTextField();
		txtExchange.setText("topic_logs");
		txtExchange.setColumns(10);
		txtExchange.setBounds(94, 10, 430, 21);
		add(txtExchange);

		txtMessage = new JTextField();
		txtMessage.setText("Hello World!.");
		txtMessage.setColumns(10);
		txtMessage.setBounds(94, 98, 430, 21);
		add(txtMessage);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMessage.setBounds(29, 100, 57, 15);
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
		btnSend.setBounds(12, 125, 74, 23);
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(94, 125, 430, 201);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

		JLabel lblRoutingKey = new JLabel("Routing Key");
		lblRoutingKey.setFont(new Font("굴림", Font.BOLD, 12));
		lblRoutingKey.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoutingKey.setBounds(12, 55, 97, 15);
		add(lblRoutingKey);

		JLabel lblFacility = new JLabel("Facility");
		lblFacility.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFacility.setBounds(104, 49, 57, 15);
		add(lblFacility);
		
		JRadioButton rdoAuth = new JRadioButton("auth");
		rdoAuth.setSelected(true);
		rdoAuth.setActionCommand("auth");
		rdoAuth.setBounds(167, 46, 57, 21);
		add(rdoAuth);
		
		JRadioButton rdoCron = new JRadioButton("cron");
		rdoCron.setActionCommand("cron");
		rdoCron.setBounds(231, 46, 74, 21);
		add(rdoCron);
		
		JRadioButton rdoKernel = new JRadioButton("kernel");
		rdoKernel.setActionCommand("kernel");
		rdoKernel.setBounds(309, 46, 74, 21);
		add(rdoKernel);
				
		JLabel label = new JLabel("Severity");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(104, 67, 57, 15);
		add(label);
		
		JRadioButton rdoInfo = new JRadioButton("info");
		rdoInfo.setSelected(true);
		rdoInfo.setActionCommand("info");
		rdoInfo.setBounds(167, 64, 57, 21);
		add(rdoInfo);
		
		JRadioButton rdoWarning = new JRadioButton("warning");
		rdoWarning.setActionCommand("warning");
		rdoWarning.setBounds(231, 64, 74, 21);
		add(rdoWarning);
		
		JRadioButton rdoCritical = new JRadioButton("critical");
		rdoCritical.setActionCommand("critical");
		rdoCritical.setBounds(309, 64, 74, 21);
		add(rdoCritical);
		
		facilityGroup = new ButtonGroup();
		severityGroup = new ButtonGroup();
		
		facilityGroup.add(rdoAuth);
		facilityGroup.add(rdoCron);
		facilityGroup.add(rdoKernel);
		
		severityGroup.add(rdoInfo);
		severityGroup.add(rdoWarning);
		severityGroup.add(rdoCritical);		
		
		JLabel lblLineHead = new JLabel("┌────────────────────────────┐");
		lblLineHead.setBounds(88, 32, 415, 15);
		add(lblLineHead);
		
		JLabel lblLineTail = new JLabel("└────────────────────────────┘");
		lblLineTail.setBounds(88, 80, 415, 15);
		add(lblLineTail);

	}

	/**
	 * RabbitMQ로 메세지 Publish
	 * 
	 * @throws Exception
	 */
	private void send() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, message, facility, severity, routingKey;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // topic_logs
		message = txtMessage.getText(); // info: Hello World!
		facility = facilityGroup.getSelection().getActionCommand();
		severity = severityGroup.getSelection().getActionCommand();
		routingKey = facility + "." + severity;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

			channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
			txtResult.append(" [x] Sent '" + routingKey + " - " + message + "'\n");
		}
	}
}
