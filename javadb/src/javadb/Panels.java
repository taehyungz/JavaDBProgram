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
//	int tableNumber = 0;
	JCheckBox[] checkBoxes = new JCheckBox[10];

	public OptionPanel() {
		try {
			CompanyDBController cont = new CompanyDBController("!","@","#");
			setLayout(new BorderLayout());
			
			JLabel teamName = new JLabel("직원 정보 검색 시스템");
			tableSelectPanel.add(teamName);
			
			//String[] tableNames = cont.getStringSet(cont.getTables());
			
			//JComboBox tableNameCB = new JComboBox(tableNames);
			//tableNameCB.addActionListener(new myCBListener());
			//tableSelectPanel.add(tableNameCB);
			//super.tableName에 넣기

			String[] attrNames = cont.getAttrs("EMPLOYEE");
			JLabel startAttr = new JLabel("Select Attributes");
			columnSelectPanel.add(startAttr);
			
			for(int i=0;i<attrNames.length;i++) {
				checkBoxes[i] = new JCheckBox(attrNames[i], false);
				columnSelectPanel.add(checkBoxes[i]);
				checkBoxes[i].addItemListener(new myItemListener(i));
			}

			JButton selectButton = new JButton("검색하기");
			selectButton.addActionListener(new mySelectListener());
			rightPanel.add(selectButton);

			add(tableSelectPanel,BorderLayout.WEST);
			add(columnSelectPanel,BorderLayout.CENTER);
			add(rightPanel,BorderLayout.EAST);
		} catch(Exception e) {
			
		}
	}
//	
//	class myCBListener implements ActionListener{
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			JComboBox cb = (JComboBox) e.getSource();
//			tableNumber = cb.getSelectedIndex();
//			//db안의 몇 번째 테이블인지를 반환
//			//System.out.println(tableNumber);
//		}
//		
//	}
//	
	class myItemListener implements ItemListener{
		int num = 0;
		public myItemListener(int i) {
			this.num = i;
		}
		public void itemStateChanged(ItemEvent e) {
			int select = 0;
			if(e.getStateChange() == ItemEvent.SELECTED)
				select = 1;
			else
				select = 0;
			checkValues[num] = select;
		}
	}
	
	class mySelectListener implements ActionListener{
		public mySelectListener() {
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("checkbox : "+Arrays.toString(checkValues));
			System.out.println("쿼리문을 실행합니다.");
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