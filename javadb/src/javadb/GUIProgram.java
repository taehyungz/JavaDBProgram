package javadb;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIProgram{ // KTH
	static LoginFrame loginFrame;
	public static void main(String[] args){
		loginFrame = new LoginFrame();
		loginFrame.setSize(400,150);
		Dimension frameSize = loginFrame.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
		loginFrame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2); // 화면 중앙
		
		loginFrame.setVisible(true);
	}
}