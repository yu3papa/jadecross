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

public class EmitLogDirectPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ProducerMain mainFrame;
	private JTextField txtExchange;
	private ButtonGroup severityGroup;
	private JRadioButton rdoInfo;
	private JRadioButton rdoWarning;
	private JRadioButton rdoError;
	private JTextField txtMessage;
	private JTextArea txtResult;

	/**
	 * Create the panel.
	 */
	public EmitLogDirectPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExchange.setBounds(12, 13, 57, 15);
		add(lblExchange);

		txtExchange = new JTextField();
		txtExchange.setText("direct_logs");
		txtExchange.setColumns(10);
		txtExchange.setBounds(75, 10, 450, 21);
		add(txtExchange);

		txtMessage = new JTextField();
		txtMessage.setText("Hello World!");
		txtMessage.setColumns(10);
		txtMessage.setBounds(75, 64, 450, 21);
		add(txtMessage);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMessage.setBounds(12, 67, 57, 15);
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
		btnSend.setBounds(12, 106, 97, 23);
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 139, 513, 190);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

		JLabel lblSeverity = new JLabel("Severity");
		lblSeverity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSeverity.setBounds(12, 38, 57, 15);
		add(lblSeverity);

		rdoInfo = new JRadioButton("info");
		rdoInfo.setSelected(true);
		rdoInfo.setActionCommand("info");
		rdoInfo.setBounds(75, 37, 57, 21);
		add(rdoInfo);

		rdoWarning = new JRadioButton("warning");
		rdoWarning.setActionCommand("warning");
		rdoWarning.setBounds(139, 37, 74, 21);
		add(rdoWarning);

		rdoError = new JRadioButton("error");
		rdoError.setActionCommand("error");
		rdoError.setBounds(217, 37, 74, 21);
		add(rdoError);

		severityGroup = new ButtonGroup();
		severityGroup.add(rdoInfo);
		severityGroup.add(rdoWarning);
		severityGroup.add(rdoError);

	}

	/**
	 * RabbitMQ로 메세지 Publish
	 * 
	 * @throws Exception
	 */
	private void send() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, message, severity;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtExchange.getText(); // hello
		message = txtMessage.getText(); // info: Hello World!
		severity = severityGroup.getSelection().getActionCommand();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

			channel.basicPublish(exchangeName, severity, null, message.getBytes("UTF-8"));
			txtResult.append(" [x] Sent '" + severity + " - " + message + "'\n");
		}
	}
}
