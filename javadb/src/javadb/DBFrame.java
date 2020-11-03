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
		BottomPanel bottomPanel = new BottomPanel();
		OptionPanel optionPanel = new OptionPanel(this);
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
	}
}
