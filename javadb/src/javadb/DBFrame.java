package javadb;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;

public class DBFrame extends JFrame{
	public DBFrame() {
		// Frame을 생성
		this.setTitle("Java DB Program");
		JTabbedPane pane = createTabbedPane();
		this.add(pane, BorderLayout.CENTER);
	}
	public JTabbedPane createTabbedPane() {
		//탭을 만듦
		JTabbedPane pane = new JTabbedPane();
		JPanel p1 = new JPanel1();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		p1.setLayout(new FlowLayout());
		
		
		
		pane.addTab("검색",  p1);
		pane.addTab("수정",  p2);
		pane.addTab("삭제",  p3);
		
		return pane;
	}
}
