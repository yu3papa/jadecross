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

public class WorkerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConsumerMain mainFrame;

	private JTextField txtQueue;
	private JTextArea txtResult1;
	private JTextArea txtResult2;
	private JTextArea txtResult3;

	public WorkerPanel(ConsumerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setBounds(12, 13, 57, 15);
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblQueue);

		txtQueue = new JTextField();
		txtQueue.setBounds(75, 10, 400, 21);
		txtQueue.setText("task_queue");
		txtQueue.setColumns(10);
		add(txtQueue);

		JButton btnWorker1 = new JButton("Worker1 작업 시작");
		btnWorker1.setBounds(12, 47, 145, 23);
		btnWorker1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					worker1Task();
					btnWorker1.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		add(btnWorker1);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 80, 250, 238);
		add(scrollPane1);

		txtResult1 = new JTextArea();
		scrollPane1.setViewportView(txtResult1);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(273, 80, 250, 238);
		add(scrollPane2);
		
		txtResult2 = new JTextArea();
		scrollPane2.setViewportView(txtResult2);
		
		JButton btnWorker2 = new JButton("Worker2 작업 시작");
		btnWorker2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					worker2Task();
					btnWorker2.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnWorker2.setBounds(273, 47, 145, 23);
		add(btnWorker2);
		
		JScrollPane scrollPane3 = new JScrollPane();
		scrollPane3.setBounds(535, 80, 250, 238);
		add(scrollPane3);
		
		txtResult3 = new JTextArea();
		scrollPane3.setViewportView(txtResult3);
		
		JButton btnWorker3 = new JButton("Worker3 작업 시작");
		btnWorker3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					worker3Task();
					btnWorker3.setEnabled(false);
				} catch (Exception ex) {
				}
			}
		});
		btnWorker3.setBounds(534, 47, 145, 23);
		add(btnWorker3);

	}
	
    private  void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

	/**
	 * RabbitMQ로 메세지 Consume
	 * 
	 * @throws Exception
	 */
	private void worker1Task() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueue.getText(); // task_queue

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, true, false, false, null);
		txtResult1.append(" [*] Waiting for messages.\n");
		
		channel.basicQos(1);
		
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            txtResult1.append(" [x] Received '" + message + "'\n");
            
            try {
                doWork(message);
            } finally {
                txtResult1.append(" [x] Done\n");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
	}
	
	private void worker2Task() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueue.getText(); // task_queue

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, true, false, false, null);
		txtResult2.append(" [*] Waiting for messages.\n");
		
		channel.basicQos(1);
		
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            txtResult2.append(" [x] Received '" + message + "'\n");
            
            try {
                doWork(message);
            } finally {
            	txtResult2.append(" [x] Done\n");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
	}
	
	private void worker3Task() throws Exception {
		String mqHost, mqPort, mqUser, mqPwd, queueName;
		mqHost = mainFrame.txtURL.getText(); // jadecross.iptime.org
		mqPort = mainFrame.txtPORT.getText(); // 5672
		mqUser = mainFrame.txtUSER.getText(); // rabbitmqadm
		mqPwd = mainFrame.txtPWD.getText(); // jadecross
		queueName = txtQueue.getText(); // task_queue

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(mqHost);
		factory.setPort(Integer.parseInt(mqPort));
		factory.setUsername(mqUser);
		factory.setPassword(mqPwd);

		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(queueName, true, false, false, null);
		txtResult3.append(" [*] Waiting for messages.\n");
		
		channel.basicQos(1);
		
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            txtResult3.append(" [x] Received '" + message + "'\n");
            
            try {
                doWork(message);
            } finally {
            	txtResult3.append(" [x] Done\n");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
	}
}
