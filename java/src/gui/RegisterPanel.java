package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class RegisterPanel extends JPanel {
	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	JPanel inputPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JTextField usernameField = new JTextField(20);
	JPasswordField passwordField = new JPasswordField(20);
	JPasswordField confirmPasswordField = new JPasswordField(20);
	JButton submitButton = new JButton("Submit");
	JLabel infoLabel = new JLabel("Enter a Username and Password");
	
	public RegisterPanel(ConnectionManager connectionManager, UserManager userManager) {
		this.connectionManager = connectionManager;
		this.userManager = userManager;
		usernameField.setText("Username");
		usernameField.setForeground(Color.GRAY);
		usernameField.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (usernameField.getText().equals("Username")) {
		        	usernameField.setText("");
		        	usernameField.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (usernameField.getText().isEmpty()) {
		        	usernameField.setForeground(Color.GRAY);
		        	usernameField.setText("Username");
		        }
		    }
		    });
		passwordField.setText("Password");
		passwordField.setForeground(Color.GRAY);
		passwordField.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (Arrays.equals(passwordField.getPassword(),"Password".toCharArray())) {
		        	passwordField.setText("");
		        	passwordField.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (passwordField.getPassword().length == 0) {
		        	passwordField.setForeground(Color.GRAY);
		        	passwordField.setText("Password");
		        }
		    }
		    });
		confirmPasswordField.setText("Confirm Password");
		confirmPasswordField.setForeground(Color.GRAY);
		confirmPasswordField.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (Arrays.equals(confirmPasswordField.getPassword(),"Confirm Password".toCharArray())) {
		        	confirmPasswordField.setText("");
		        	confirmPasswordField.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (confirmPasswordField.getPassword().length == 0) {
		        	confirmPasswordField.setForeground(Color.GRAY);
		        	confirmPasswordField.setText("Confirm Password");
		        }
		    }
		    });
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
					JOptionPane.showMessageDialog(null,
							"Passwords do not match.");
				} else if (passwordField.getPassword().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Password cannot be empty");
				} else if (usernameField.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Username cannot be empty");
				} else if (usernameField.getText().length() > 20) {
					JOptionPane.showMessageDialog(null,
							"Username must be 20 characters or less");
				} else {
					if (register(usernameField.getText(), passwordField.getPassword())) {
						JOptionPane.showMessageDialog(null,
								"Successfully registered account!");
					}
				}
			}
		}); 
			
		inputPanel.add(usernameField, BorderLayout.NORTH);
		inputPanel.add(passwordField, BorderLayout.NORTH);
		inputPanel.add(confirmPasswordField, BorderLayout.NORTH);
		buttonPanel.add(submitButton);
		
		this.add(infoLabel);
		this.add(inputPanel);
		this.add(buttonPanel);
	}
	
	private boolean register(String username, char[] password) {
		byte[] passSalt = getNewSalt();
		String passHash = hashPassword(passSalt, password);
		int returnCode = 3;
		
		Connection connection = connectionManager.getConnection();
		try {
			CallableStatement stmt = connection.prepareCall("{? = call addUser(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, username);
			stmt.setBytes(3, passSalt);
			stmt.setString(4, passHash);
			stmt.executeUpdate();
			
			returnCode = stmt.getInt(1);
			System.out.println(returnCode);
			
			if (returnCode == 1) {
				infoLabel.setText("Username cannot be empty.");
				return false;
			} else if (returnCode == 2) {
				infoLabel.setText("Username has been taken.");
				return false;
			} else if (returnCode != 0) {
				infoLabel.setText("An error occured while registering your account.");
				return false;
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(returnCode);
			if (returnCode == 1) {
				infoLabel.setText("Username cannot be empty.");
				return false;
			} else if (returnCode == 2) {
				infoLabel.setText("Username has been taken.");
				return false;
			} else if (returnCode != 0) {
				infoLabel.setText("An error occured while registering your account.");
				return false;
			}
		}
		
		return false;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, char[] password) {

		KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}
}
