package javadb;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
public class Panels extends JPanel{	
}

class NorthPanel extends Panels {
	public NorthPanel() {
		setBackground(Color.BLACK);
		setLayout(new FlowLayout());
		JLabel teamName = new JLabel("DB 8주차 4조");
		teamName.setForeground(Color.WHITE);
		add(teamName);
		
		this.setSize(100,1200);
	}
}

class OptionPanel extends Panels {
	String[] tableNames = {"Employee", "Department"};
	String[] attrNames = {"ssn", "dno", "mgr_ssn"};
	JCheckBox[] checkBoxes = new JCheckBox[10];
	public OptionPanel() {
		setLayout(new FlowLayout());
		
		JLabel teamName = new JLabel("SELECT TABLE");
		add(teamName);
		
		JComboBox tableNameCB = new JComboBox(tableNames);
		add(tableNameCB);
		
		JLabel startAttr = new JLabel("Select Attributes");
		add(startAttr);
		
		for(int i=0;i<attrNames.length;i++) {
			checkBoxes[i] = new JCheckBox(attrNames[i], false);
			add(checkBoxes[i]);
		}
		
		JButton selectButton = new JButton("검색하기");
		add(selectButton);
		
		this.setSize(400,1200);
	}
}

class ResultPanel extends Panels {
	public ResultPanel() {
		setBackground(Color.WHITE);
		add(new JLabel("result "));
	}
}
