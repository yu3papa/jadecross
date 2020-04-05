package com.jadecross.dbms.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * JADE DBMS 클라이언트
 * 
 * @author Administrator
 *
 */
public class Client01 extends JPanel {
	private JTable tableResult;
	private JLabel lblResponseTime;
	private JTextField txtDBIP;
	private JTextField txtPORT;
	private JTextField txtEmpNo;

	public Client01() {
		setLayout(null);

		JLabel lblServerIP = new JLabel("JadeDBMS IP");
		lblServerIP.setBounds(12, 10, 89, 15);
		add(lblServerIP);

		JButton btnSearch = new JButton("조회");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					query();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(384, 47, 70, 50);
		add(btnSearch);

		tableResult = new JTable();
		tableResult.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "사번", "이름", "성", "전화번호", "부서" }));
		JScrollPane scrollPaneResult = new JScrollPane(tableResult);
		scrollPaneResult.setBounds(12, 113, 631, 205);
		add(scrollPaneResult);

		lblResponseTime = new JLabel("응답시간 :");
		lblResponseTime.setBounds(466, 63, 163, 15);
		add(lblResponseTime);

		txtDBIP = new JTextField();
		txtDBIP.setText("jadecross.iptime.org");
		txtDBIP.setBounds(97, 7, 155, 21);
		add(txtDBIP);
		txtDBIP.setColumns(10);

		JLabel lblPort = new JLabel("PORT");
		lblPort.setBounds(264, 10, 41, 15);
		add(lblPort);

		txtPORT = new JTextField();
		txtPORT.setText("5502");
		txtPORT.setBounds(307, 7, 76, 21);
		add(txtPORT);
		txtPORT.setColumns(10);

		JPanel panelSearchCondition = new JPanel();
		panelSearchCondition.setBorder(BorderFactory.createTitledBorder(" 조회조건 "));
		panelSearchCondition.setBounds(12, 38, 371, 65);
		add(panelSearchCondition);
		panelSearchCondition.setLayout(null);

		JLabel lblNewLabel = new JLabel("사원번호");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(57, 26, 66, 15);
		panelSearchCondition.add(lblNewLabel);

		txtEmpNo = new JTextField();
		txtEmpNo.setBounds(135, 23, 145, 21);
		panelSearchCondition.add(txtEmpNo);
		txtEmpNo.setColumns(10);
	}

	/**
	 * JADE DBMS Socket Client
	 * 참고주소 - https://www.codejava.net/java-se/networking/java-socket-client-examples-tcp-ip
	 * 
	 * @throws Exception
	 */
	private void query() throws Exception {
		InetAddress serverAddress = null;
		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		String readText = null;

		// 0. 쿼리 결과 초기화
		DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
		tableModel.setNumRows(0);
		long strtTimeMillis = System.currentTimeMillis();

		// 1. 서버주소객체 생성
		serverAddress = InetAddress.getByName(txtDBIP.getText());

		// 2. 클라이언트 소켓 생성(서버 연결)
		socket = new Socket(serverAddress, Integer.parseInt(txtPORT.getText()));

		// 3. 서버로 데이터 전송
		writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(txtEmpNo.getText().trim());

		// 4. 서버에서 데이터 읽기
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while ((readText = reader.readLine()) != null) {
			tableModel.addRow(readText.split(","));
		}

		// 5. 클라이언트 소켓 종료
		socket.close();

		// 6. 수행시간 출력
		long endTimeMillis = System.currentTimeMillis();
		lblResponseTime.setText("응답시간 : " + (endTimeMillis - strtTimeMillis) + " ms");
	}
}
