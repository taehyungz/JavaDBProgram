package javadb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class DBFrame extends JFrame{ // KTH
	public DBFrame(String inputId, String inputPw) throws ClassNotFoundException, SQLException{
		// Frame을 생성
		this.setTitle("Java DB Program");
		
		Container contentPane = getContentPane();
		OptionPanel optionPanel = new OptionPanel(this, inputId, inputPw);
		JScrollPane scrollPane = new JScrollPane(optionPanel.table);
		BottomPanel bottomPanel = new BottomPanel(optionPanel, this);
		
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
}
