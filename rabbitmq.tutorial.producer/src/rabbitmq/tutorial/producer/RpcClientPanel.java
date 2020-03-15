package rabbitmq.tutorial.producer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

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
import java.awt.Font;

public class RpcClientPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ProducerMain mainFrame;

	private JTextField txtQueue;
	private JTextField txtInputNo;
	private JTextArea txtResult;
	private JTextField txtOutputNo;

	public RpcClientPanel(ProducerMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);

		JLabel lblQueue = new JLabel("Queue");
		lblQueue.setBounds(12, 13, 57, 15);
		lblQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblQueue);

		txtQueue = new JTextField();
		txtQueue.setBounds(75, 10, 430, 21);
		txtQueue.setText("rpc_queue");
		txtQueue.setColumns(10);
		add(txtQueue);

		txtInputNo = new JTextField();
		txtInputNo.setHorizontalAlignment(SwingConstants.CENTER);
		txtInputNo.setBounds(196, 65, 46, 21);
		txtInputNo.setText("10");
		txtInputNo.setColumns(10);
		add(txtInputNo);

		JButton btnSend = new JButton("=");
		btnSend.setBounds(266, 64, 46, 23);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		add(btnSend);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 129, 472, 185);
		add(scrollPane);

		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);

		JLabel label = new JLabel("피보나치 수열 계산");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.BOLD, 12));
		label.setBounds(12, 49, 155, 15);
		add(label);

		JLabel lblF = new JLabel("f(");
		lblF.setHorizontalAlignment(SwingConstants.CENTER);
		lblF.setFont(new Font("굴림", Font.BOLD, 30));
		lblF.setBounds(157, 41, 46, 66);
		add(lblF);

		JLabel label_1 = new JLabel(")");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("굴림", Font.BOLD, 30));
		label_1.setBounds(231, 41, 46, 66);
		add(label_1);

		txtOutputNo = new JTextField();
		txtOutputNo.setHorizontalAlignment(SwingConstants.CENTER);
		txtOutputNo.setColumns(10);
		txtOutputNo.setBounds(324, 65, 116, 21);
		add(txtOutputNo);

		JLabel label_2 = new JLabel("로그");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(12, 104, 57, 15);
		add(label_2);

	}

	/**
	 * RabbitMQ를 이용한 RPC
	 * 
	 * @throws Exception
	 */
	private void send() {
		try (RPCClient fibonacciRpc = new RPCClient(mainFrame, txtQueue.getText())) {
			String inputNo = txtInputNo.getText();
			txtResult.append(" [x] Requesting fib(" + inputNo + ")\n");
			String response = fibonacciRpc.call(inputNo);
			txtOutputNo.setText(response);
			txtResult.append(" [.] Got '" + response + "'\n");

		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
