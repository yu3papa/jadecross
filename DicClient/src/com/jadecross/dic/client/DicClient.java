package com.jadecross.dic.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class DicClient {
	private JFrame frame;
	private JPanel topPanel; // 사전 서버 접속 컴포넌트들이 있는 패널
	private JTabbedPane tabbedPanel;
	private JPanel panelDic;
	public JTextField txtServerIP;
	public JTextField txtPORT;

	public DicClient() {
		// 생성 및 초기화
		frame = new JFrame();
		frame.setLayout(new MigLayout());

		// Top Panel 설정 START ################################
		{
			topPanel = new JPanel();
			topPanel.setLayout(new MigLayout());
			txtServerIP = new JTextField("127.0.0.1");
			txtPORT = new JTextField("5000");

			// 컴포넌트 배치
			topPanel.add(new JLabel("Server IP:"));
			topPanel.add(txtServerIP);
			topPanel.add(new JLabel("PORT:"));
			topPanel.add(txtPORT);
		}
		// ########## Top 패널 설정 END

		// ##### TabbedPane 설정
		{
			tabbedPanel = new JTabbedPane();
			panelDic = new JPanel();
			new Translator(panelDic, this);
			tabbedPanel.add("단어 해석", panelDic);
		}

		// 프래임 기본 설정
		frame.add(topPanel, "wrap");
		frame.add(tabbedPanel, "push, grow");
		frame.setSize(400, 200);
		frame.setTitle("JADECROSS DICTIONARY");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DicClient();
			}
		});
	}
}