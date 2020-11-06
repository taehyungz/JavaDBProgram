package javadb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

interface totalInterface {
	JButton selectButton = new JButton("검색하기");
	JLabel updateLabel = new JLabel("   검색한 직원  : " );
	JLabel totalPersonLabel = new JLabel("  인원 수  : ");
	String selectedSsns = "";
} 

public class Panels extends JPanel{ // KTH
	String tableName = "";
	String[] columnsDT = {};
	String path = System.getProperty("user.dir");
	String[] attrNames = null;

	//PHJ
	int ComboSelect;
	String ParName;
	String ChildName;
	String ParComboName[] = {"전체","BDATE","SEX","SALARY","SUPERVISOR","DEPARTMENT"};
	ArrayList<String> ChildComboName = new ArrayList<String>();

	//콤보박스 객체
	JComboBox<String> ParCombo;
	JComboBox<String> ChildCombo;
	String inputPar="";
	String inputChild="";
	
	String Par = "";
	String child = "";

	public Panels() {
		//File file = new File(path+"\\src\\javadb\\db_connection_info.txt");
		//cont = new CompanyDBController(file);
	}
}

class OptionPanel extends Panels implements totalInterface{ // KTH + PHJ
	JFrame jFrame;
	JPanel groupingPanel = new JPanel();
	JPanel columnsDTselectPanel = new JPanel();
	JPanel selBtnPanel = new JPanel();
	int[] checkValues = {1,1,1,1,1,1,1,1};
	JCheckBox[] checkBoxes = new JCheckBox[8]; //attribute 선택 체크박스
	String[] colsArr = null; // 사용하는 속성들
	Object[][] contents; // 레코드들
	DefaultTableModel defaultTableModel = new DefaultTableModel(contents, colsArr);
	CompanyDBController cont;

	//check list and person ArrayList
	ArrayList<Integer> checkList = new ArrayList<Integer>(); // 체크한 것들의 행번호(0부터 시작)
	ArrayList<ArrayList<String>> pidList = new ArrayList<ArrayList<String>>();

	JTable table = new JTable(defaultTableModel) {
		@Override 
		public Class getColumnClass(int column){
			if(column==0){
				return Boolean.class;
			}else {
				return Object.class;
			}
		}
	};
	
	public OptionPanel(JFrame jf, String inputId, String inputPw) throws ClassNotFoundException, SQLException {
		cont = new CompanyDBController(inputId, inputPw);
		attrNames = cont.getAttrs();
		
		
		ParCombo = new JComboBox<String>(ParComboName);
		String[] stringArray = Arrays.copyOf(ChildComboName.toArray(), ChildComboName.toArray().length, String[].class);
		ChildCombo = new JComboBox<String>(stringArray);
		
		jFrame = jf;
		searchQuery();

		JLabel teamName = new JLabel("직원 정보 검색 시스템");
		groupingPanel.add(teamName);

		JLabel startAttr = new JLabel("Select Attributes");
		columnsDTselectPanel.add(startAttr);

		//콤보박스 추가
		columnsDTselectPanel.add(ParCombo);
		columnsDTselectPanel.add(ChildCombo);
		ParCombo.setVisible(true);			
		ChildCombo.setVisible(false);
		
		for(int i=0;i<attrNames.length;i++) {
			checkBoxes[i] = new JCheckBox(attrNames[i], true);
			columnsDTselectPanel.add(checkBoxes[i]);
			checkBoxes[i].addItemListener(new myItemListener(i));
		}

		selectButton.addActionListener(new mySelectListener());
		selBtnPanel.add(selectButton);

		// Event Listener
		ParCombo.addActionListener(new ComboEvent());

		add(groupingPanel,BorderLayout.WEST);
		add(columnsDTselectPanel,BorderLayout.CENTER);
		add(selBtnPanel,BorderLayout.EAST);
		
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
		}
	}
	
	class mySelectListener implements ActionListener{ // KTH
		public mySelectListener() {
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] nullCheckBox = {0,0,0,0,0,0,0,0};
			if(Arrays.equals(checkValues, nullCheckBox)) {
				JOptionPane.showMessageDialog(jFrame, "최소 하나 이상의 Attribute를 선택해주세요.");
				return;
			}
			if(ParCombo.getSelectedIndex() != 0 && ChildCombo.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(jFrame, "하위 옵션이 선택되지 않았습니다.");
				return;
			}
			searchQuery ();
			updateNameList();
		}
	}

	public void searchQuery (){
		try{			
			updateNameList();

			colsArr = cont.getAttrsName(cont.selectEmp(checkValues, ParCombo.getSelectedIndex(),(String)ChildCombo.getSelectedItem()));
			contents = cont.getTuples(cont.selectEmp(checkValues, ParCombo.getSelectedIndex(),(String)ChildCombo.getSelectedItem()));
			
			int rowSize = contents.length;
			totalPersonLabel.setText("검색한 직원  : "+rowSize+" 명");
			defaultTableModel.setDataVector(contents,colsArr);

			ResultSet pidResult = cont.selectSsn(ParCombo.getSelectedIndex(),(String)ChildCombo.getSelectedItem());
			
			pidList.clear();
			checkList.clear();

			while(pidResult.next()){
				ArrayList<String> pidSet = new ArrayList<>();
				for(int i=1; i<3; i++){
					pidSet.add(pidResult.getString(i));
				}
				pidList.add(pidSet);
			}

			DefaultTableCellRenderer dcr = new MyTableCellRenderer();
			table.getColumn("CheckBox").setPreferredWidth(15);
			dcr.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumn("CheckBox").setCellRenderer(dcr);
		
		} catch(Exception sqle) {		
		}
	}
	
	public void updateNameList() {
		String nameList = "선택한 직원  :  ";
		
		for(int i=0; i<checkList.size(); i++){
			if(i!=checkList.size()-1){
				nameList = nameList + pidList.get(checkList.get(i)).get(0)+"  /  ";
			}else{
				nameList = nameList + pidList.get(checkList.get(i)).get(0);
			}
		}
		updateLabel.setText(nameList);
		String[] x = totalPersonLabel.getText().split(" /");
		totalPersonLabel.setText(x[0]+" / 선택한 직원 :  "+checkList.size()+"명");
	}

	class MyTableCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent
		(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JCheckBox comp = null;
			comp = new JCheckBox();
			comp.setHorizontalAlignment(SwingConstants.CENTER);
			comp.setSelected((value!=null && ((Boolean)value).booleanValue()));
			if((value!=null && ((Boolean)value).booleanValue())){
				if(!checkList.contains((Integer)row)){
					checkList.add((Integer)row);
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
	}

	//콤보박스 이벤트
	class ComboEvent implements ActionListener { // PHJ
		
		//콤보박스 action처리
		@Override
		public void actionPerformed(ActionEvent e) {
			Par = ParCombo.getSelectedItem().toString();
			
			if(Par=="전체") {
				ChildCombo.removeAllItems();
				ChildCombo.setVisible(false);
			} else {
				int size = cont.ChildSearch(Par,ChildComboName);

				// String[] temp = ChildComboName.clone();
				ChildCombo.removeAllItems();
				int idx=0;
				for(int i= 0;i<size;i++) {
					if(ChildComboName.get(i) != null) {
						ChildCombo.insertItemAt(ChildComboName.get(i), idx);
						idx++;
					}
				}
				
				ChildCombo.setVisible(false);
				ChildCombo.setVisible(true);
			}
		}
	}
}

class BottomPanel extends Panels implements totalInterface{ // KTH + LJH
	JPanel updateNewPanel = new JPanel(); // KTH
	JPanel updatePanel = new JPanel(); // KTH
	JPanel removePanel = new JPanel(); // LJH
	JTextField newSalInp = new JTextField(10);
	double newSalary = 0;
	OptionPanel optionPanel;
	JFrame jFrame;


	public BottomPanel(OptionPanel optionPanel, JFrame jFrame) { // KTH + LJH
		this.optionPanel = optionPanel;
		setLayout(new BorderLayout());
		updatePanel.setLayout(new BorderLayout());
		updateNewPanel.setLayout(new BorderLayout());
		
		updatePanel.add(totalPersonLabel, BorderLayout.NORTH);
		add(updateLabel, BorderLayout.NORTH);
		
		JLabel newSalLabel = new JLabel("새로운 Salary : ");
		updateNewPanel.add(newSalLabel, BorderLayout.WEST);
		
		updateNewPanel.add(newSalInp, BorderLayout.CENTER);
		JButton updateBT = new JButton("수정하기");
		updateBT.addActionListener(new myUpdateListener());
		updateNewPanel.add(updateBT, BorderLayout.EAST);
		
		updatePanel.add(updateNewPanel, BorderLayout.WEST);
		add(updatePanel, BorderLayout.WEST);
		
		JLabel removeLabel = new JLabel("선택한 데이터 삭제");
		removePanel.add(removeLabel);

		JButton deleteButton = new JButton("삭제");
		deleteButton.addActionListener(new myDeleteListener());
		removePanel.add(deleteButton);

		add(removePanel, BorderLayout.EAST);
	}
	
	class myUpdateListener implements ActionListener{ // KTH
		public myUpdateListener() { }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(optionPanel.checkList.size()==0){
				JOptionPane.showMessageDialog(jFrame, "한 명 이상의 직원을 선택해주세요.");
				return;
			}
			try{
				newSalary = Double.parseDouble(newSalInp.getText());
			} catch(Exception nullInput) {
				JOptionPane.showMessageDialog(jFrame, "변경할 급여를 입력해주세요.");
				return;
			}
			for(int i=0;i<optionPanel.checkList.size();i++){
				String selectedSsn = optionPanel.pidList.get(optionPanel.checkList.get(i)).get(1);
				optionPanel.cont.updateEmp(selectedSsn, newSalary);
			}
			optionPanel.searchQuery();
			optionPanel.updateNameList();
			newSalInp.setText(null);
		}	
	}

	class myDeleteListener implements ActionListener { // LJH
		public myDeleteListener() {}

		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<optionPanel.checkList.size();i++){
				String selectedSsn = optionPanel.pidList.get(optionPanel.checkList.get(i)).get(1);
				optionPanel.cont.deleteEmp(selectedSsn);
				
			}
			optionPanel.searchQuery();
			optionPanel.updateNameList();
		}
	}
}