package javadb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
	
public class Panels extends JPanel{
	String tableName = "";
	String[] Columns = {};

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
	int[] checkValues = new int[10];
	JCheckBox[] checkBoxes = new JCheckBox[10];

	public OptionPanel() {
		try {
			CompanyDBController cont = new CompanyDBController("root", "");
			setLayout(new BorderLayout());
			
			JLabel teamName = new JLabel("SELECT TABLE");
			tableSelectPanel.add(teamName);
			
			System.out.println("aaaaaa");
			String[] tableNames = cont.getStringSet(cont.getTables());
			
			System.out.println(Arrays.toString(tableNames));
			JComboBox tableNameCB = new JComboBox(tableNames);
			tableSelectPanel.add(tableNameCB);
			//super.tableName에 넣기
			System.out.println("ccccc");
			String[] attrNames = cont.getAttrs("EMPLOYEE");
			System.out.println(Arrays.toString(attrNames));
			JLabel startAttr = new JLabel("Select Attributes");
			columnSelectPanel.add(startAttr);
			
			for(int i=0;i<attrNames.length;i++) {
				checkBoxes[i] = new JCheckBox(attrNames[i], false);
				columnSelectPanel.add(checkBoxes[i]);
				//checkBoxes[i].addItemListener(new myItemListener(i));
			}
		} catch(Exception e) {
			
		}
	}
	
	class myItemListener implements ItemListener{
		int num = 0;
		public myItemListener(int i) {
			this.num = i;
		}
		public void itemStateChanged(ItemEvent e) {
			int select = 1;
			if(e.getStateChange() == ItemEvent.SELECTED)
				select = 1;
			else
				select = -1;
			checkValues[num] = select;
			System.out.println(Arrays.toString(checkValues));
		}
	}
}

class ResultPanel extends Panels {
	public ResultPanel() {
		setBackground(Color.WHITE);
		add(new JLabel("result "));
		//results = getResult();
		
//		JScrollPane scrollPanel = new JScrollPane();
//		JFrame upperFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,this);
//		upperFrame.add(new JLabel("SDFJL"));
	}
}