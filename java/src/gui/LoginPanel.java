package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel{
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JTextField usernameField = new JTextField(20);
	JTextField passwordField = new JTextField(20);
	//JTextField confirmPasswordField = new JTextField(20);
	JButton loginButton = new JButton("Login");
	

	
	public LoginPanel() {
        setLayout(new GridLayout(20, 10)); //15 row

        JLabel userNameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
      //  JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

        add(userNameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
      //  add(confirmPasswordLabel);
      //  add(confirmPasswordField);
        add(new JLabel()); 
        add(loginButton);
        
 
    }
	

}
