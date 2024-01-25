package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
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

	JComboBox<Object> GameList; 
	JButton submitButton = new JButton("Submit");
	
	JLabel infoLabel = new JLabel("Pick a game to add");
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	public AddGamePanel(ConnectionManager connectionManager, UserManager userManager) {
		this.connectionManager = connectionManager;
		this.userManager = userManager;
		
		GameList = new JComboBox<Object>(getAllStoredGames());
		GameList.setEditable(true);
			
		textPanel.add(GameList, BorderLayout.NORTH);
		buttonPanel.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (addGameToUser(GameList.getSelectedItem().toString())) {
					infoLabel.setText("Successfully Added Game to account!");
				}
			}
			
		}); 
		
		this.add(infoLabel);
		this.add(textPanel);
		this.add(buttonPanel);
	}
	
	private Object[] getAllStoredGames() {
		Connection connection = connectionManager.getConnection();
		
		ArrayList<String> results = new ArrayList<String>();
						
		CallableStatement stmt;
		try {
			String query = "SELECT Name \n FROM [Game]";
			stmt = connection.prepareCall(query);
			ResultSet rs = stmt.executeQuery();
			
			
			while (rs.next()) {
				String CurrentGame = rs.getString("Name");
				results.add(CurrentGame);
			}
			
			return results.toArray();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			String[] current = {"Error Encountered"};
			return current;
		}
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
			
			if (returnCode == 1 || returnCode == 2) {
				infoLabel.setText("Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				infoLabel.setText("Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode != 0) {
				infoLabel.setText("An error occured while adding this game. Please try again");
				return false;
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(returnCode);
			if (returnCode == 3 || returnCode == 4) {
				infoLabel.setText("Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				infoLabel.setText("Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode != 0) {
				infoLabel.setText("An error occured while Adding this game.");
				return false;
			}
		}
		
		return false;
	}
}