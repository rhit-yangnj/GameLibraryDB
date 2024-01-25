package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class AddGamePanel extends JPanel {
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JComboBox<String> GameList; 
	JButton submitButton = new JButton("Submit");
	
	JLabel infoLabel = new JLabel("Pick a game to add");
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	public AddGamePanel(ConnectionManager connectionManager, UserManager userManager) {
		this.connectionManager = connectionManager;
		this.userManager = userManager;
		
		GameList = new JComboBox<String>(getAllStoredGames());
		GameList.setEditable(true);
			
		textPanel.add(GameList, BorderLayout.NORTH);
		buttonPanel.add(submitButton);
		
		this.add(textPanel);
		this.add(buttonPanel);
	}
	
	private String[] getAllStoredGames() {
		//TODO actually implement some request command to get all the games in the list
		String[] current = {"None", "Doom", "Baloons Tower Defence 5"};
		return current;
	}
	
	private boolean addGameToUser(String inputGame) {
		Connection connection = connectionManager.getConnection();
		int returnCode = 3;
		
		try {
			CallableStatement stmt = connection.prepareCall("{? = call addGameToUser(?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, inputGame);
			stmt.setString(3, userManager.getUser());
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
}