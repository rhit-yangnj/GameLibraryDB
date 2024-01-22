package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class RegisterPanel extends JPanel {
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JTextField usernameField = new JTextField(20);
	JTextField passwordField = new JTextField(20);
	JTextField confirmPasswordField = new JTextField(20);
	JButton submitButton = new JButton("Submit");
	
	public RegisterPanel() {
		usernameField.setText("Username");
		passwordField.setText("Password");
		confirmPasswordField.setText("Confirm Password");
			
		textPanel.add(usernameField, BorderLayout.NORTH);
		textPanel.add(passwordField, BorderLayout.NORTH);
		textPanel.add(confirmPasswordField, BorderLayout.NORTH);
		buttonPanel.add(submitButton);
		
		this.add(textPanel);
		this.add(buttonPanel);
	}
}
