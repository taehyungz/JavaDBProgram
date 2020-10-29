package javadb;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
public class Panels extends JPanel{	
}

class JPanel1 extends Panels {
	private String[] AttrNames = {"a","b","c","d","e","f","g","h","i","j"}; //이름 넣기, select절에서 받아오기 
	private String[] check = {"","","","","","","","","",""};
	
	
	public JPanel1(){
		setLayout(null);
		for (int i=0;i<AttrNames.length;i++) {
			JLabel label = new JLabel(AttrNames[i]);
			JCheckBox jch = new JCheckBox();
			add(label);
			add(jch);
		}
	}
}
