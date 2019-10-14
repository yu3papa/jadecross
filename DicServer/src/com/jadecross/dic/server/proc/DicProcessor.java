package com.jadecross.dic.server.proc;

import java.io.DataOutputStream;
import java.io.IOException;

import com.jadecross.dic.server.dao.DicDAO;

public class DicProcessor {

	public void process(String requestWord, DataOutputStream out, String oracleIP) throws IOException {
		String responseMessage = null;

		DicDAO dao = new DicDAO(oracleIP);

		responseMessage = dao.getDescription(requestWord);
		
		System.out.println(requestWord + " ─▶ " + responseMessage);

		// 메소드 상세프로파일링 실습
		childMethod1();
		
		this.handleRequest(responseMessage, out);
	}

	/**
	 * 요청한 단어의 뜻을 클라이언트에게 전송
	 * 
	 * @param responseMessage
	 * @param out
	 * @throws IOException
	 */
	public void handleRequest(String responseMessage, DataOutputStream out) throws IOException {
		out.writeUTF(responseMessage);
		out.flush();
		
		
	}

	public void childMethod1() {
		childMethod2();
	}

	public void childMethod2() {
		childMethod3();
	}

	public void childMethod3() {
		childMethod4();
	}

	public void childMethod4() {
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {

		}
	}
}