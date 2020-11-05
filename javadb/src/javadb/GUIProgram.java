package javadb;

import java.awt.Frame;
import java.awt.Button;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIProgram{ // KTH
	public static void main(String[] args){
		Frame frame = new DBFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setSize(1400,400);
		frame.setVisible(true);
	}
}