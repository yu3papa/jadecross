package com.jadecross.dic.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jadecross.dic.client.util.GuiUtil;

import net.miginfocom.swing.MigLayout;

public class Translator {
	private DicClient parent;
	private JPanel panel;
	private JTextField txtWord;
	private JButton btnTranslate;
	private JTextArea txareaDescription;

	public Translator(JPanel parentPanel, DicClient parent) {
		this.panel = parentPanel;
		this.parent = parent;

		// 컴포넌트 생성
		txtWord = new JTextField(60);
		txtWord.setText("apple");
		btnTranslate = new JButton("번역");
		txareaDescription = new JTextArea();

		// 화면 레이아웃 설정
		panel.setLayout(new MigLayout());
		panel.add(new JLabel("영어"));
		panel.add(txtWord, "wrap");
		panel.add(btnTranslate, "top");
		panel.add(new JScrollPane(txareaDescription), "wrap, push, grow");

		// Event Handler 등록
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					translateWord();
				} catch (Exception ex) {
					GuiUtil.debugDialog(ex);
				}
			}
		});
	}

	/**
	 * 영어단어 번역
	 */
	private void translateWord() throws Exception {
		// 서버주소객체 생성
		InetAddress serverAddress = InetAddress.getByName(parent.txtServerIP.getText());

		// 클라이언트 소켓 생성(서버 연결)
		Socket socket = new Socket(serverAddress, Integer.parseInt(parent.txtPORT.getText()));

		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		out.writeUTF(txtWord.getText());
		out.flush();

		txareaDescription.setText(in.readUTF());

		// 클라이언트 소켓 종료
		socket.close();
//		GuiUtil.debugDialog("Complete...");
	}
}