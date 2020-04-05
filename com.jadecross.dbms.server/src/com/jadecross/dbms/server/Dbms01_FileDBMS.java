package com.jadecross.dbms.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * EMPLOYEES.csv, DEPARTMENTS.csv 파일을 읽어서 Grid에 출력
 * @author Administrator
 *
 */
public class Dbms01_FileDBMS extends JPanel {
	private JTable tableSample;
	private JTable tableResult;
	private JLabel lblResponseTime;

	/**
	 * Create the panel.
	 */
	public Dbms01_FileDBMS() {
		setLayout(null);

		JLabel lbTitle = new JLabel("EMPLOYMENTS.csv 파일과 DEPARTMENTS.csv 파일을 읽어서 아래과 같이 출력하시요.");
		lbTitle.setBounds(12, 10, 631, 15);
		add(lbTitle);

		// 샘플 결과 테이블
		tableSample = new JTable();
		tableSample.setModel(new DefaultTableModel(new String[][] { { "501", "병국", "양", "010.8984.8092", "Sales" }, { "502", "환열", "정", "010.4221.7988", "IT" }, { ".", ".", ".", ".", "." } },
				new String[] { "사번", "이름", "성", "전화번호", "부서" }));

		JScrollPane scrollPaneSample = new JScrollPane(tableSample);
		scrollPaneSample.setBounds(12, 58, 830, 96);
		add(scrollPaneSample);

		JButton btnSearch = new JButton("조회");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					select();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(12, 164, 97, 23);
		add(btnSearch);

		tableResult = new JTable();
		tableResult.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "사번", "이름", "성", "전화번호", "부서" }));
		JScrollPane scrollPaneResult = new JScrollPane(tableResult);
		scrollPaneResult.setBounds(12, 197, 830, 220);
		add(scrollPaneResult);

		lblResponseTime = new JLabel("응답시간 :");
		lblResponseTime.setBounds(121, 168, 522, 15);
		add(lblResponseTime);

		JLabel lblNewLabel = new JLabel("- EMPLOYEES 100건, DEPARTMENTS 50만건");
		lblNewLabel.setBounds(22, 35, 442, 15);
		add(lblNewLabel);
	}

	private void select() throws Exception {
		HashSet<String> employmentSet = new HashSet<String>();
		HashSet<String> departmentSet = new HashSet<String>();

		long strtTimeMillis = System.currentTimeMillis();

		// 0. 쿼리 결과 초기화
		DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
		tableModel.setNumRows(0);
		lblResponseTime.setText("응답시간 : ");

		// 1. csv 파일을 읽어서 HashSet에 담는다
		String line = "";
		BufferedReader brEmployments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/EMPLOYEES.csv")));
		while ((line = brEmployments.readLine()) != null) {
			employmentSet.add(line);
		}

		BufferedReader brDepartments = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/DEPARTMENTS.csv")));
		while ((line = brDepartments.readLine()) != null) {
			departmentSet.add(line);
		}

		// 2. Nested Loop를 돌면서 JOIN
		for (String eachEmployee : employmentSet) {
			String[] employee = eachEmployee.split(",");

			for (String eachDepartment : departmentSet) {
				String[] department = eachDepartment.split(",");

				if (employee[10].equals(department[0]) == true) {
					tableModel.addRow(new String[] { employee[0], employee[1], employee[2], employee[4], department[1] });
				}
			}
		}

		// 3. 수행시간 출력
		long endTimeMillis = System.currentTimeMillis();
		lblResponseTime.setText("응답시간 : " + (endTimeMillis - strtTimeMillis) + " ms");
	}
}
