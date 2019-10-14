package com.jadecross.dic.server.proc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WorkerThread extends Thread {
	private Socket socket;
	private String oracleIP;

	public WorkerThread(Socket socket, String oracleIP) {
		this.socket = socket;
		this.oracleIP = oracleIP;
	}

	@Override
	public void run() {
		DataInputStream in = null;
		DataOutputStream out = null;
		String requestWord = null;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			requestWord = in.readUTF(); // 클라이언트 요청 수신
			
			// 클라이언트 요청처리 및 Result 전송
			new DicProcessor().process(requestWord, out, oracleIP);
		} catch (Exception ex) {

		} finally {
			try { in.close(); } catch (IOException e) { }
			try { out.close(); } catch (IOException e) { }
			try { socket.close(); } catch (IOException e) { }
		}
	}
}
