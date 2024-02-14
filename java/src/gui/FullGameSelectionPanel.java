package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FullGameSelectionPanel extends GameSelectionPanel {

	public FullGameSelectionPanel(UserManager userManager, ConnectionManager connectionManager,
			UpdateManager updateManager) {
		super(userManager, connectionManager, updateManager);
		this.editGameButton = new JButton("Add Game");
		this.editGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedGameName.equals("No Game Selected")) {
					ErrorPanels.createErrorDialogue("Please select a game first.");
//					JOptionPane.showMessageDialog(null, "Please select a game first.");
				} else if (addSelectedGameToUser()) {
					updateManager.redoSearch();
					updateManager.GameBrowserUpdate();
					ErrorPanels.createInfoDialogue("Successfully Added Game to account!");
//					JOptionPane.showMessageDialog(null, "Successfully Added Game to account!");
				}
			
			}
		});
		this.add(editGameButton);
	}
	
	private boolean addSelectedGameToUser() {
		Connection connection = connectionManager.getConnection();
		int returnCode = 3;
		
		try {
			CallableStatement stmt = connection.prepareCall("{? = call addGameToUser(?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, selectedGameName);
			stmt.setString(3, userManager.getUser());
			stmt.executeUpdate();
			
			returnCode = stmt.getInt(1);
			System.out.println(returnCode);
			
			if (returnCode == 5) {
				ErrorPanels.createErrorDialogue("You already own this game.");
//				JOptionPane.showMessageDialog(null, "You already own this game.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				ErrorPanels.createErrorDialogue("Game is null or nonexistant, please select a game");
//				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				ErrorPanels.createErrorDialogue("Username is null or nonexistant, please sign in.");
//				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode != 0) {
				ErrorPanels.createErrorDialogue("An error occured while adding this game. Please try again");
//				JOptionPane.showMessageDialog(null, "An error occured while adding this game. Please try again");
				return false;
			}

			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(returnCode);
			if (returnCode == 5) {
				ErrorPanels.createErrorDialogue("You already own this game.");
//				JOptionPane.showMessageDialog(null, "You already own this game.");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				ErrorPanels.createErrorDialogue("Username is null or nonexistant, please sign in.");
//				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				ErrorPanels.createErrorDialogue("Game is null or nonexistant, please select a game");
//				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode != 0) {
				ErrorPanels.createErrorDialogue("An error occured while adding this game. Please try again");
//				JOptionPane.showMessageDialog(null, "An error occured while adding this game.");
				return false;
			}
		}
		
		return false;
	}

}
