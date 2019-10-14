package com.jadecross.dic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.jadecross.dic.server.proc.WorkerThread;

public class DicServer {

	public static void main(String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("아규먼트로 Oracle Server IP를 지정하세요...");
			return;
		}
		System.out.println("Server가 시작되었습니다.");
		
		String oracleIP = args[0];
		new DicServer().launch(oracleIP);
	}

	public void launch(String oracleIP) throws IOException {
		ServerSocket serverSocket = new ServerSocket(5000); // ServerSocket 생성

		try {
			while (true) {
				Socket socket = serverSocket.accept(); // 접속을 기다리는 중

				// 클라이언트 연결 후 메세지 처리
				// System.out.println("연결됨 -->" + socket.getRemoteSocketAddress());
				WorkerThread worker = new WorkerThread(socket, oracleIP);
				worker.start();
			}

		} finally {
			serverSocket.close();
		}
	}
}