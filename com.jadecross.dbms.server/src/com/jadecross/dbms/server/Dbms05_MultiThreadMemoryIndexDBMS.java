package com.jadecross.dbms.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * EMPLOYEES.csv, DEPARTMENTS.csv 파일을 읽어서 Memory에 저장한후 Grid에 출력 Oracle의 SGA를 구현
 * Multi-Thread Server로 동작
 * DEPARTMETNTS 테이블의 PK Index를 추가
 * 
 * @author Administrator
 *
 */
public class Dbms05_MultiThreadMemoryIndexDBMS extends JPanel {
	private JTextField txtPORT;
	private JButton btnStartServer;
	private List lstServerStatus;

	/**
	 * Create the panel.
	 */
	public Dbms05_MultiThreadMemoryIndexDBMS() {
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

		JLabel lblNewLabel_1 = new JLabel("* Multi-Thred Server");
		lblNewLabel_1.setFont(new Font("Consolas", Font.BOLD, 16));
		lblNewLabel_1.setBounds(228, 10, 270, 15);
		add(lblNewLabel_1);

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		textArea.setText("- 동시에 여러명의 Client에게만 서비스 가능\r\n- SELECT 시 매번 DISK에 있는 파일을 읽지 않고 메모리에서 읽어옴\r\n- DEPARTMENTS 테이블에 PK Index 추가하여 속도 향상");

		textArea.setBackground(new Color(240, 240, 240));
		textArea.setBounds(259, 30, 386, 55);
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
	@SuppressWarnings("resource")
	private void startServer() throws Exception {
		ServerSocket serverSocket = null;
		Socket socket = null;

		serverSocket = new ServerSocket(Integer.parseInt(txtPORT.getText()));
		while (true) {
			socket = serverSocket.accept(); // 접속을 기다리는 중

			// Worker Thread를 이용하여 Multi-Thread 환경으로 서버 운영
			WorkerThread worker = new WorkerThread(socket);
			worker.start();
		}
	}

	class WorkerThread extends Thread {
		private Socket socket;
		private BufferedReader reader = null;
		private PrintWriter writer = null;
		private String requestEmpNo = null;
		private String clientIP = null;

		public WorkerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
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

			} catch (Exception ex) {
				addLog("Exception Occured..." + ex.getMessage());
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
			SGA2 sga = SGA2.getInstance();
			// 1. Nested Loop를 돌면서 JOIN
			for (String eachEmployee : sga.employmentSet) {
				String[] employee = eachEmployee.split(",");

				if (!empNo.equals(employee[0]) && !empNo.equals(""))
					continue;

				String[] department = sga.departmentMap.get(employee[10]).split(",");
				out.println(String.format("%s,%s,%s,%s,%s", employee[0], employee[1], employee[2], employee[4], department[1]));
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
}

class SGA2 {
	private static SGA2 sga = new SGA2();
	public HashSet<String> employmentSet;
	public HashMap<String, String> departmentMap;

	private SGA2() {
		employmentSet = new HashSet<String>();
		departmentMap = new HashMap<String, String>();

		// 1. csv 파일을 읽어서 HashSet에 담는다
		String line = "";

		BufferedReader brEmployments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/EMPLOYEES.csv")));
		BufferedReader brDepartments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/DEPARTMENTS.csv")));

		try {
			brEmployments.readLine();// Header 칼럼명은 건너뜀

			while ((line = brEmployments.readLine()) != null)
				employmentSet.add(line);

			brDepartments.readLine(); // Header 칼럼명은 건너뜀
			while ((line = brDepartments.readLine()) != null)
				departmentMap.put(line.split(",")[0], line);
		} catch (IOException e) {
		}
	}

	public static SGA2 getInstance() {
		return sga;
	}
}
