package rabbitmq.tutorial.producer;

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

public class ProducerMain extends JFrame {

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
					ProducerMain frame = new ProducerMain();
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
	public ProducerMain() {
		setTitle("JADECROSS RabbitMQ Tutorial ▶Producer◀");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
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
		tabbedPane.setBounds(22, 94, 550, 360);
		contentPane.add(tabbedPane);
		
		// 01. HelloWorldPanale 추가
		HelloWorldPanel panelHelloWorld = new HelloWorldPanel(this);
		tabbedPane.addTab("HelloWorld", null, panelHelloWorld, null);
		
		// 02. NewTaskPanel 추가
		NewTaskPanel panelNewTask = new NewTaskPanel(this);
		tabbedPane.addTab("Work Queues", null, panelNewTask, null);
		
		// 03. Publish/Subscribe 추가
		EmitLogPanel panelEmitLog = new EmitLogPanel(this);
		tabbedPane.addTab("Publish / Subscribe", null, panelEmitLog, null);
		
		// 04. Routing
		EmitLogDirectPanel panelEmitLogDirect = new EmitLogDirectPanel(this);
		tabbedPane.addTab("Routing", null, panelEmitLogDirect, null);
		
		// 05. Topic
		EmitLogTopicPanel panelEmitLogTopic = new EmitLogTopicPanel(this);
		tabbedPane.addTab("Topics", null, panelEmitLogTopic, null);
		
		// 06. RpcPanel
		RpcClientPanel panelRpc = new RpcClientPanel(this);
		tabbedPane.addTab("RPC", null, panelRpc, null);
	}
}
