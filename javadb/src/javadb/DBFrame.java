package javadb;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class DBFrame extends JFrame{
	public DBFrame(){
		// Frame을 생성
		this.setTitle("Java DB Program");
		
		Container contentPane = getContentPane();
		JPanel optionPanel = new OptionPanel();
		JPanel resultPanel = new ResultPanel();
		contentPane.add(optionPanel, BorderLayout.NORTH);
		contentPane.add(resultPanel, BorderLayout.CENTER);
	}
}
