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

public class ReceiveLogsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtQueueName;
	private JTextArea txtResult1;
	private JTextArea txtResult2;

	public ReceiveLogsPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblExchange = new JLabel("Exchange");
		lblExchange.setBounds(12, 13, 57, 15);
		lblExchange.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblExchange);

		txtQueueName = new JTextField();
		txtQueueName.setBounds(75, 10, 400, 21);
		txtQueueName.setText("logs");
		txtQueueName.setColumns(10);
		add(txtQueueName);

		JButton btnReceive1 = new JButton("RECV1");
		btnReceive1.setBounds(12, 47, 97, 23);
		btnReceive1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receive1();
					btnReceive1.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnReceive1);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 80, 350, 237);
		add(scrollPane1);

		txtResult1 = new JTextArea();
		scrollPane1.setViewportView(txtResult1);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(432, 80, 350, 237);
		add(scrollPane2);
		
		txtResult2 = new JTextArea();
		scrollPane2.setViewportView(txtResult2);
		
		JButton btnReceive2 = new JButton("RECV2");
		btnReceive2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receive2();
					btnReceive2.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnReceive2.setBounds(432, 47, 97, 23);
		add(btnReceive2);

	}

	/**
	 * Fanout Exchange 의 Queue에세 메세지 소모하는 소비자1
	 * 
	 * @throws Exception
	 */
	private void receive1() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtQueueName.getText(); // logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "fanout");
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");

        txtResult1.append(" [*] Waiting for messages.\n");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            txtResult1.append(" [x] Received '" + message + "'\n");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}
	

	/**
	 * Fanout Exchange 의 Queue에세 메세지 소모하는 소비자2
	 * 
	 * @throws Exception
	 */
	private void receive2() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, exchangeName, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		exchangeName = txtQueueName.getText(); // logs

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "fanout");
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");

        txtResult2.append(" [*] Waiting for messages.\n");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            txtResult2.append(" [x] Received '" + message + "'\n");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}
}
