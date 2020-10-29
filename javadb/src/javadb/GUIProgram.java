package javadb;
import java.awt.Frame;
import java.awt.Button;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIProgram{
	public static void main(String[] args) {
		Frame frame = new DBFrame();
		frame.setSize(1200,800);
		frame.setVisible(true);
	}
}