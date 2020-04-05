package com.jadecross.dbms.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * EMPLOYEES.csv, DEPARTMENTS.csv 파일을 읽어서 Grid에 출력
 * Iterative Server로 동작
 * 
 * @author Administrator
 *
 */
public class Dbms02_SingleThreadFileDBMS extends JPanel {
	private JTextField txtPORT;
	private JButton btnStartServer;
	private List lstServerStatus;

	/**
	 * Create the panel.
	 */
	public Dbms02_SingleThreadFileDBMS() {
		setLayout(null);

		JLabel lblNewLabel = new JLabel("LISTEN PORT");
		lblNewLabel.setBounds(12, 10, 93, 15);
		add(lblNewLabel);

		txtPORT = new JTextField();
		txtPORT.setText("5502");
		txtPORT.setBounds(100, 7, 116, 21);
		add(txtPORT);
		txtPORT.setColumns(10);

		btnStartServer = new JButton("DBMS SERVER 시작");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					lstServerStatus.add("JADE DBMS SERVER Started...");
					startServer();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStartServer.setBounds(12, 35, 204, 23);
		add(btnStartServer);

		JLabel lblNewLabel_1 = new JLabel("* Single-Thread Server (iterative)");
		lblNewLabel_1.setFont(new Font("Consolas", Font.BOLD, 16));
		lblNewLabel_1.setBounds(228, 10, 417, 15);
		add(lblNewLabel_1);

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		textArea.setText("- 특정 시점에 1명의 Client에게만 서비스 가능");

		textArea.setBackground(new Color(240, 240, 240));
		textArea.setBounds(259, 35, 386, 37);
		add(textArea);

		lstServerStatus = new List();
		lstServerStatus.setBounds(12, 90, 830, 320);
		add(lstServerStatus);

	}

	/**
	 * 참고주소 :
	 * https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
	 * https://m.blog.naver.com/PostView.nhn?blogId=korn123&logNo=30102017290&proxyReferer=https%3A%2F%2Fwww.google.com%2F
	 * 
	 * @throws Exception
	 */
	private void startServer() throws Exception {
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		String requestEmpNo = null;
		String clientIP = null;

		serverSocket = new ServerSocket(Integer.parseInt(txtPORT.getText()));
		while (true) {
			socket = serverSocket.accept(); // 접속을 기다리는 중
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);

			clientIP = socket.getInetAddress().getHostAddress();
			addLog(getCurrentTime() + " - " + clientIP + " Connected...");
			requestEmpNo = reader.readLine();
			addLog(".....Requested Employee ID : " + requestEmpNo);

			// Query 시작
			select(writer, requestEmpNo);

			socket.close();
			addLog(getCurrentTime() + " - " + clientIP + " Disconnected...");
		}
	}

	/**
	 * 2개의 csv 파일을 Nested Loop 방식으로 조인후 결과값을 클라이언트에게 전달
	 * 
	 * @param out
	 * @param empNo
	 * @throws Exception
	 */
	private void select(PrintWriter out, String empNo) throws Exception {
		HashSet<String> employmentSet = new HashSet<String>();
		HashSet<String> departmentSet = new HashSet<String>();

		// 1. csv 파일을 읽어서 HashSet에 담는다
		String line = "";
		BufferedReader brEmployments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/EMPLOYEES.csv")));
		BufferedReader brDepartments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/DEPARTMENTS.csv")));

		brEmployments.readLine(); // Header 칼럼명은 건너뜀
		while ((line = brEmployments.readLine()) != null)
			employmentSet.add(line);

		brDepartments.readLine(); // Header 칼럼명은 건너뜀
		while ((line = brDepartments.readLine()) != null)
			departmentSet.add(line);

		// 2. Nested Loop를 돌면서 JOIN
		for (String eachEmployee : employmentSet) {
			String[] employee = eachEmployee.split(",");

			if (!empNo.equals(employee[0]) && !empNo.equals(""))
				continue;

			for (String eachDepartment : departmentSet) {
				String[] department = eachDepartment.split(",");

				if (employee[10].equals(department[0]) == true) {
					out.println(String.format("%s,%s,%s,%s,%s", employee[0], employee[1], employee[2], employee[4], department[1]));
				}
			}
		}
	}

	/**
	 * 현재시간을 "yyyy-MM-dd HH:mm:ss" 형태로 리턴
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	/**
	 * Server 상태를 로그로 출력
	 * 
	 * @param msg
	 */
	private void addLog(String msg) {
		lstServerStatus.add(msg);
		lstServerStatus.select(lstServerStatus.getItemCount() - 1);
	}
}
