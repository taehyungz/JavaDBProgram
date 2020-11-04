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
		BottomPanel bottomPanel = new BottomPanel();
		
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
}
