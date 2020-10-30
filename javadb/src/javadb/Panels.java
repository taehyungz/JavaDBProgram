package javadb;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
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
	}
}

class OptionPanel extends Panels {
	JPanel tableSelectPanel = new JPanel();
	JPanel columnSelectPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	String[] tableNames = {"Employee", "Department"};
	String[] attrNames = {"ssn", "dno", "mgr_ssn"};
	JCheckBox[] checkBoxes = new JCheckBox[10];
	public OptionPanel() {
		setLayout(new BorderLayout());
		
		JLabel teamName = new JLabel("SELECT TABLE");
		tableSelectPanel.add(teamName);
		
		tableNames = getTables();
		JComboBox tableNameCB = new JComboBox(tableNames);
		tableSelectPanel.add(tableNameCB);
		
		attrNames = getAttrs();
		JLabel startAttr = new JLabel("Select Attributes");
		columnSelectPanel.add(startAttr);
		
		for(int i=0;i<attrNames.length;i++) {
			checkBoxes[i] = new JCheckBox(attrNames[i], false);
			columnSelectPanel.add(checkBoxes[i]);
		}
		
		JButton selectButton = new JButton("검색하기");
		rightPanel.add(selectButton);
		
		add(tableSelectPanel,BorderLayout.WEST);
		add(columnSelectPanel,BorderLayout.CENTER);
		add(rightPanel,BorderLayout.EAST);
	}
}

class ResultPanel extends Panels {
	public ResultPanel() {
		setBackground(Color.WHITE);
		add(new JLabel("result "));
	}
}
