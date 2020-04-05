package com.jadecross.dbms.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 * JADE DBMS SERVER GUI Main
 * 
 * @author Administrator
 *
 */
public class ServerMain extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMain frame = new ServerMain();
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
	public ServerMain() {
		setTitle("JADECROSS DBMS ▶SERVER◀");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 883, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		// 01. File DBMS 추가
		Dbms01_FileDBMS dbms01 = new Dbms01_FileDBMS();
		tabbedPane.addTab("1. File DBMS", null, dbms01, null);

		// 02. Single-Thread File DBMS 추가
		Dbms02_SingleThreadFileDBMS dbms02 = new Dbms02_SingleThreadFileDBMS();
		tabbedPane.addTab("2. Single-Thread File DBMS", null, dbms02, null);

		// 03. Multi-Thread File DBMS 추가
		Dbms03_MultiThreadFileDBMS dbms03 = new Dbms03_MultiThreadFileDBMS();
		tabbedPane.addTab("3. Multi-Thread File DBMS", null, dbms03, null);

		// 04. Multi-Thread Memory DBMS 추가
		Dbms04_MultiThreadMemoryDBMS dbms04 = new Dbms04_MultiThreadMemoryDBMS();
		tabbedPane.addTab("4. Multi-Thread Memory DBMS", null, dbms04, null);

		// 05. Multi-Thread Memory DBMS 추가
		Dbms05_MultiThreadMemoryIndexDBMS dbms05 = new Dbms05_MultiThreadMemoryIndexDBMS();
		tabbedPane.addTab("5. Multi-Thread Memory Index DBMS", null, dbms05, null);
	}
}
