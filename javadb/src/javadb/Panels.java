package javadb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
	
public class Panels extends JPanel{ // KTH
	String tableName = "";
	String[] columnsDT = {};
	String path = System.getProperty("user.dir");
	String id = "";
	String password="";
	String dbname="";
	String[] attrNames = null;

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
			
			attrNames = cont.getAttrs();
			
		} catch(Exception errDBInfo) {
			String id = "!";
			String password = "@";
			String dbname = "#";
		}
	}
}

class OptionPanel extends Panels { // KTH + PHJ
	JPanel groupingPanel = new JPanel();
	JPanel columnsDTselectPanel = new JPanel();
	JPanel selBtnPanel = new JPanel();
	int[] checkValues = {1,1,1,1,1,1,1,1};//new int[10];
	JCheckBox[] checkBoxes = new JCheckBox[10];
	String[] colsArr = null;
	Object[] contentRow;
	Object[][] contents;
	ArrayList<Object[]> contentsArrayList = new ArrayList<Object[]>();
	JScrollPane scrollPanel = null;
	DefaultTableModel defaultTableModel = new DefaultTableModel(contents, colsArr);

	//check list and person ArrayList
	ArrayList<Integer> checkList = new ArrayList<Integer>();
	ArrayList<ArrayList<String>> pidList = new ArrayList<ArrayList<String>>();

	JTable table = new JTable(defaultTableModel){
		@Override 
		public Class getColumnClass(int column){
			if(column==0){
				return Boolean.class;
			}else {
				return Object.class;
			}
		}
	};

	BottomPanel bottomPanel;

	public OptionPanel(JFrame jf) {
		bottomPanel = new BottomPanel(this);
		jf.add(bottomPanel, BorderLayout.SOUTH);
		try {
			searchQuery();
			
			DefaultTableCellRenderer dcr = new MyTableCellRenderer();
			DefaultTableCellRenderer defaultDcr = new DefaultTableCellRenderer();

			table.getColumn("CheckBox").setPreferredWidth(15);

			dcr.setHorizontalAlignment(SwingConstants.CENTER);
			defaultDcr.setHorizontalAlignment(SwingConstants.CENTER);
			
			table.getColumn("CheckBox").setCellRenderer(dcr);

			for (int i=1; i<table.getColumnCount(); i++){
				String name = table.getColumnName(i);
				table.getColumn(name).setCellRenderer(defaultDcr);
			}

			scrollPanel = new JScrollPane(table);
			
			scrollPanel.setPreferredSize(new Dimension(1180,600));//(1000,600);
			
			jf.add(scrollPanel,BorderLayout.CENTER);

			setLayout(new BorderLayout());
			
			JLabel teamName = new JLabel("직원 정보 검색 시스템");
			groupingPanel.add(teamName);

			JLabel startAttr = new JLabel("Select Attributes");
			columnsDTselectPanel.add(startAttr);
			
			for(int i=0;i<attrNames.length;i++) {
				checkBoxes[i] = new JCheckBox(attrNames[i], true);
				columnsDTselectPanel.add(checkBoxes[i]);
				checkBoxes[i].addItemListener(new myItemListener(i));
			}

			JButton selectButton = new JButton("검색하기");
			selectButton.addActionListener(new mySelectListener());
			selBtnPanel.add(selectButton);

			add(groupingPanel,BorderLayout.WEST);
			add(columnsDTselectPanel,BorderLayout.CENTER);
			add(selBtnPanel,BorderLayout.EAST);
		} catch(Exception e) {
			
		}
	}


	class myItemListener implements ItemListener { // KTH + PHJ
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
			System.out.println(Arrays.toString(checkValues));
		}
	}

	public void searchQuery (){
		try{
			int colCount = 0;

			contentsArrayList.clear();

			ResultSet result = cont.selectEmp(checkValues);
			for(int j=0; j<checkValues.length; j++){
				if(checkValues[j]==1){
					colCount = colCount+1;
				}
			}
			
			while(result.next()){
				contentRow = new Object[colCount+1];
				contentRow[0] = false;
				for(int k=1; k<colCount+1;k++){
					contentRow[k] = (result.getString(k));
				}
				contentsArrayList.add(contentRow);
			}

			System.out.println(contentsArrayList);

			int colSize = colCount;
			int rowSize = contentsArrayList.size();

			contents = new Object[contentsArrayList.size()][colCount];
			for (int n = 0; n<rowSize; n++){
				contents[n] = contentsArrayList.get(n);
			}
			System.out.println("debug::: in Option1");

			String colsName = "CheckBox ";

			for(int n=0;n<checkValues.length;n++) {
				if(checkValues[n]==1 && n!=0) 
					colsName+=(" "+attrNames[n]);
				else if(checkValues[n]==1 && n<colSize)
					colsName+=attrNames[n];
			}
			colsArr = colsName.split(" ");

			defaultTableModel.setDataVector(contents,colsArr);

			ResultSet pidResult = cont.selectSsn();

			pidList.clear();
			checkList.clear();

			while(pidResult.next()){
				ArrayList<String> pidSet = new ArrayList<>();
				for(int i=0; i<2; i++){
					pidSet.add(pidResult.getString(i));
				}
				pidList.add(pidSet);
			}
		
		} catch(Exception sqle) {
			
		}
	}
	
	class mySelectListener implements ActionListener{ // KTH
		public mySelectListener() {
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("checkbox : "+Arrays.toString(checkValues));
			System.out.println("쿼리문을 실행합니다.");
			searchQuery ();
			
		}
	}

	class MyTableCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			JCheckBox comp = null;
			comp = new JCheckBox();
			comp.setHorizontalAlignment(SwingConstants.CENTER);
			comp.setSelected((value!=null && ((Boolean)value).booleanValue()));
			if((value!=null && ((Boolean)value).booleanValue())){
				if(!checkList.contains((Integer)row)){
					checkList.add((Integer)row)					;
					updateNameList();
				}
			}else{
				if(checkList.contains((Integer)row)){
					checkList.remove((Integer)row);
					updateNameList();
				}
			}
			return comp;
		}

		private void updateNameList() {

			String nameList = "선택한 직원  :  ";
			for(int i=0; i<checkList.size(); i++){
				if(i!=checkList.size()-1){
					nameList = nameList + pidList.get(checkList.get(i))+"  /  ";
				}else{
					nameList = nameList + pidList.get(checkList.get(i));
				}
			}

			bottomPanel.updateLabel.setText(nameList);
		}
	}
}

class BottomPanel extends Panels { // KTH + LJH
	JPanel updateNewPanel = new JPanel(); // KTH
	JPanel updatePanel = new JPanel(); // KTH
	JPanel removePanel = new JPanel(); // LJH
	int selectedPersonNum = 0;
	JLabel updateLabel;
	JTextField newSalInp = new JTextField(10);
	double newSalary = 0;
	String[] selectedSSNs = {"123","456"};

	public BottomPanel(OptionPanel optionPanel) { // KTH + LJH
		setLayout(new BorderLayout());
		updatePanel.setLayout(new BorderLayout());
		
		JLabel totalPersonLabel = new JLabel("  인원 수 : "+selectedPersonNum);
		updatePanel.add(totalPersonLabel, BorderLayout.NORTH);
		updateLabel = new JLabel("  선택한 직원 : " );
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
			
			for(int m=0;m<selectedSSNs.length;m++) {
				System.out.println(selectedSSNs[m]);
				try {
					cont.updateEmp(selectedSSNs[m], newSalary);
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
			for(int m=0;m<selectedSSNs.length;m++) {
				System.out.println(selectedSSNs[m]);
				try {
					cont.deleteEmp(selectedSSNs[m]);
				} catch(Exception notUpdated) {
					System.out.println("삭제되지 않았습니다.");
				}
			}
		}
	}
}