package com.jadecross.dic.client.util;

import javax.swing.JOptionPane;

public class GuiUtil {

	/**
	 * 디버그 팝업창
	 * 
	 * @param msg
	 */
	public static void debugDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	/**
	 * 디버그 팝업창 : Exception용
	 * 
	 * @param ex
	 */
	public static void debugDialog(Exception ex) {
		StringBuffer sb = new StringBuffer();

		sb.append(ex);
		sb.append("\n");

		for (StackTraceElement eachElement : ex.getStackTrace()) {
			sb.append("     ");
			sb.append(eachElement.toString());
			sb.append("\n");
		}

		JOptionPane.showMessageDialog(null, sb.toString());
	}

	public static void debugDialog(String msg, Exception ex) {

		StringBuffer sb = new StringBuffer();
		sb.append(msg);
		sb.append("\n\n");
		sb.append(ex);
		sb.append("\n");

		for (StackTraceElement eachElement : ex.getStackTrace()) {
			sb.append("     ");
			sb.append(eachElement.toString());
			sb.append("\n");
		}

		JOptionPane.showMessageDialog(null, sb.toString());
	}
}
