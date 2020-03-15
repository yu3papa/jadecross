package rabbitmq.tutorial.consumer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class ConsumerMain extends JFrame {

	public JPanel contentPane;
	public JTextField txtURL;
	public JTextField txtUSER;
	public JTextField txtPORT;
	public JTextField txtPWD;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsumerMain frame = new ConsumerMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConsumerMain() {
		setTitle("JADECROSS RabbitMQ Tutorial ▶Consumer◀");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 849, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("RabbitMQ 주소");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 10, 200, 15);
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("URL");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(22, 38, 57, 15);
		contentPane.add(label);

		txtURL = new JTextField();
		txtURL.setText("jadecross.iptime.org");
		txtURL.setColumns(10);
		txtURL.setBounds(91, 35, 200, 21);
		contentPane.add(txtURL);

		txtUSER = new JTextField();
		txtUSER.setText("rabbitmqadm");
		txtUSER.setColumns(10);
		txtUSER.setBounds(91, 63, 200, 21);
		contentPane.add(txtUSER);

		JLabel label_1 = new JLabel("USER");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(22, 66, 57, 15);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("PORT");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(303, 38, 57, 15);
		contentPane.add(label_2);

		txtPORT = new JTextField();
		txtPORT.setText("5672");
		txtPORT.setColumns(10);
		txtPORT.setBounds(372, 35, 116, 21);
		contentPane.add(txtPORT);

		txtPWD = new JTextField();
		txtPWD.setText("jadecross");
		txtPWD.setColumns(10);
		txtPWD.setBounds(372, 63, 116, 21);
		contentPane.add(txtPWD);

		JLabel label_3 = new JLabel("PWD");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(303, 66, 57, 15);
		contentPane.add(label_3);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(22, 94, 802, 357);
		contentPane.add(tabbedPane);

		// 01. HelloWorldPanale 추가
		HelloWorldPanel panelHelloWorld = new HelloWorldPanel(this);
		tabbedPane.addTab("HelloWorld", null, panelHelloWorld, null);

		// 02. WokerPanale 추가
		WorkerPanel panelWorker = new WorkerPanel(this);
		tabbedPane.addTab("Work Queues", null, panelWorker, null);

		// 03. Publish / Subscribe 추가
		ReceiveLogsPanel panelReceiveLogs = new ReceiveLogsPanel(this);
		tabbedPane.addTab("Publish / Subscribe", null, panelReceiveLogs, null);
		
		// 04. Routing
		ReceiveLogsDirectPanel panelReceiveLogsDirect = new ReceiveLogsDirectPanel(this);
		tabbedPane.addTab("Routing", null, panelReceiveLogsDirect, null);
		
		// 05. Topic 
		ReceiveLogsTopicPanel panelReceiveLogsTopic = new ReceiveLogsTopicPanel(this);
		tabbedPane.addTab("Topics", null, panelReceiveLogsTopic, null);

		// 06. RpcServerPanel
		RpcServerPanel panelRpcServer = new RpcServerPanel(this);
		tabbedPane.addTab("RPC", null, panelRpcServer, null);
	}
}
