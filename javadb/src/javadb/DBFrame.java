package javadb;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class DBFrame extends JFrame{ // KTH
	public DBFrame(){
		// Frame을 생성
		this.setTitle("Java DB Program");
		
		Container contentPane = getContentPane();
		OptionPanel optionPanel = new OptionPanel(this);
		JScrollPane scrollPane = new JScrollPane(optionPanel.table);
		BottomPanel bottomPanel = new BottomPanel(optionPanel, this);
		
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
}
