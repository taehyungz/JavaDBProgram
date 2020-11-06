package javadb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class LoginFrame extends JFrame{ // KTH
	int loginFlag = 0;
	JTextField idField;
	JPasswordField pwField;
	LoginFrame loginFrame;
	public LoginFrame(){
		// Frame을 생성
		loginFrame = this;
		setLayout(null);
		setTitle("Login");
		JLabel title = new JLabel("직원정보 검색 시스템");
		JLabel idLabel = new JLabel("아이디 :  ");
		JLabel pwLabel = new JLabel("비밀번호 : ");
		JPanel loginPanel = new JPanel();
		idField = new JTextField(20);
		pwField = new JPasswordField(20);
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginVerifyListener(this));
		
		title.setBounds(5,0,150,30);
		idLabel.setBounds(5,30,80,30);
		pwLabel.setBounds(5,70,80,30);
		idField.setBounds(85,35,200,30);
		pwField.setBounds(85,73,200,30);
		loginButton.setBounds(295,40,80,60);
		Container contentPane = getContentPane();
		contentPane.add(title);
		contentPane.add(idLabel);
		contentPane.add(idField);
		contentPane.add(pwLabel);
		contentPane.add(pwField);
		contentPane.add(loginButton);
	}
	
	class LoginVerifyListener implements ActionListener{ // KTH
		JFrame loginFrame;
		public LoginVerifyListener(JFrame loginFrame) {
			this.loginFrame = loginFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String inputId = idField.getText();
			String inputPw = String.copyValueOf(pwField.getPassword());
			CompanyDBController cont;
			try{
				cont = new CompanyDBController(inputId, inputPw);
				Frame frame = new DBFrame(inputId, inputPw);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				loginFrame.dispose();
			} catch(Exception e22) {
				JOptionPane.showMessageDialog(loginFrame, "ID, PW가 일치하지 않습니다.");
				return;
			}
		}
	}
}
