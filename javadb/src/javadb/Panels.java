package javadb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
	
public class Panels extends JPanel{ // KTH
	String tableName = "";
	String[] Columns = {};
	String path = System.getProperty("user.dir");
	String id = "";
	String password="";
	String dbname="";
	CompanyDBController cont = null;
	public Panels() {
		try {
			File file = new File(path+"\\src\\javadb\\db_connection_info.txt");
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String id = bufReader.readLine();
			String password = bufReader.readLine();
			String dbname = bufReader.readLine();
			cont = new CompanyDBController(id, password, dbname);
			
		} catch(Exception errDBInfo) {
			String id = "!";
			String password = "@";
			String dbname = "#";
		}
	}
}

class OptionPanel extends Panels { // KTH + PHJ
	JPanel tableSelectPanel = new JPanel();
	JPanel columnSelectPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	int[] checkValues = new int[10];
//	int tableNumber = 0;
	JCheckBox[] checkBoxes = new JCheckBox[10];

	public OptionPanel() {
		try {
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
	class myItemListener implements ItemListener{ // KTH + PHJ
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
	
	class mySelectListener implements ActionListener{ // KTH
		public mySelectListener() {
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("checkbox : "+Arrays.toString(checkValues));
			System.out.println("쿼리문을 실행합니다.");
			System.out.println(cont.getResult(cont.selectEmp(checkValues)));
		}
	}
}

class ResultPanel extends Panels { // KTH
	public ResultPanel() {
		setBackground(Color.WHITE);
		add(new JLabel("result "));
		//results = getResult();
		
//		JScrollPane scrollPanel = new JScrollPane();
//		JFrame upperFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,this);
//		upperFrame.add(new JLabel("SDFJL"));
	}
}

class BottomPanel extends Panels { // KTH + LJH
	JPanel updateNewPanel = new JPanel(); // KTH
	JPanel updatePanel = new JPanel(); // KTH
	JPanel removePanel = new JPanel(); // LJH
	int selectedPersonNum = 0;
	JTextField newSalInp = new JTextField(10);
	double newSalary = 0;
	String[] selectedNames = {"사람1", "사람2"};
	String[] selectedSSNs = {"123","456"};

	public BottomPanel() { // KTH + LJH
		setLayout(new BorderLayout());
		updatePanel.setLayout(new BorderLayout());
		
		JLabel totalPersonLabel = new JLabel("  인원 수 : "+selectedPersonNum);
		updatePanel.add(totalPersonLabel, BorderLayout.NORTH);
		JLabel updateLabel = new JLabel("  선택한 직원 : " + Arrays.toString(selectedNames));
		updatePanel.add(updateLabel, BorderLayout.CENTER);
		
		JLabel newSalLabel = new JLabel("새로운 Salary : ");
		updateNewPanel.add(newSalLabel);
		
		updateNewPanel.add(newSalInp);
		JButton updateBT = new JButton("수정하기");
		updateBT.addActionListener(new myUpdateListener());
		updateNewPanel.add(updateBT);
		
		updatePanel.add(updateNewPanel, BorderLayout.SOUTH);
		add(updatePanel, BorderLayout.WEST);

		JLabel removeLabel = new JLabel("선택한 데이터 삭제");
		removePanel.add(removeLabel);

		JButton selectButton = new JButton("삭제");
		selectButton.addActionListener(new myButtonListener());
		removePanel.add(selectButton);

		add(removePanel, BorderLayout.EAST);
	}
	
	class myUpdateListener implements ActionListener{ // KTH
		public myUpdateListener() { }

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				newSalary = Double.parseDouble(newSalInp.getText());
			} catch(Exception nullInput) {
				newSalary = 0;
			}
			
			for(int i=0;i<selectedSSNs.length;i++) {
				System.out.println(selectedSSNs[i]);
				try {
					cont.updateEmp(selectedSSNs[i], newSalary);
				} catch(Exception notUpdated) {
					System.out.println("수정되지 않았습니다.");
				}
			}
			System.out.println(newSalInp.getText());
		}
		
		
	}

	class myButtonListener implements ActionListener { // LJH
		public myButtonListener() {}

		@Override
		public void actionPerformed(ActionEvent e) {
			//cont.deleteEmp(ssn);
			System.out.println("push button!");
		}
	}
}