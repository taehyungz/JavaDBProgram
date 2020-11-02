package javadb;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.io.*;

public class DBFrame extends JFrame{ // KTH
	public DBFrame(){
		// Frame을 생성
		this.setTitle("Java DB Program");
		
		Container contentPane = getContentPane();
		OptionPanel optionPanel = new OptionPanel();
		ResultPanel resultPanel = new ResultPanel();
		BottomPanel bottomPanel = new BottomPanel();
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(resultPanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
}
