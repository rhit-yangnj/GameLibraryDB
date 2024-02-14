package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FullGameSelectionPanel extends GameSelectionPanel {
	private SingleGameBrowsingPanel singleGameBrowsingPanel;

	public FullGameSelectionPanel(UserManager userManager, ConnectionManager connectionManager,
			UpdateManager updateManager) {
		super(userManager, connectionManager, updateManager);
		this.editGameButton = new JButton("Add Game");
		this.editGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedGameName.equals("No Game Selected")) {
					JOptionPane.showMessageDialog(null, "Please select a game first.");
				} else if (addSelectedGameToUser()) {
					updateManager.redoSearch();
					updateManager.GameBrowserUpdate();
					JOptionPane.showMessageDialog(null, "Successfully Added Game to account!");
				}
			
			}
		});
		this.add(editGameButton);
		
		
		this.seeReviewButton = new JButton("See Review For Selected Game");
		this.seeReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedGameName.equals("No Game Selected")) {
                    JOptionPane.showMessageDialog(null, "Please select a game first.");
                } else {
                    showSingleGameBrowsingPanel(selectedGameName);
                }
            }
        });
        this.add(seeReviewButton);
    }
	
//	
//	private void showSingleGameBrowsingPanel(String selectedGameName) {
//        if (singleGameBrowsingPanel == null) {
//            singleGameBrowsingPanel = new SingleGameBrowsingPanel(connectionManager, userManager);
//        }
//        singleGameBrowsingPanel.addReviewsToTable(selectedGameName); 
//        JOptionPane.showMessageDialog(null, singleGameBrowsingPanel); 
//    }
	
	
	private void showSingleGameBrowsingPanel(String selectedGameName) {
	    JFrame frame = new JFrame("Single Game Browsing Panel");
	    
	    if (singleGameBrowsingPanel == null) {
	        singleGameBrowsingPanel = new SingleGameBrowsingPanel(connectionManager, userManager);
	    }
	    
	    singleGameBrowsingPanel.addReviewsToTable(selectedGameName); 
	    
	    frame.add(singleGameBrowsingPanel);
	    
	    frame.pack();
	    frame.setVisible(true);
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
				JOptionPane.showMessageDialog(null, "You already own this game.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "An error occured while adding this game. Please try again");
				return false;
			}

			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(returnCode);
			if (returnCode == 5) {
				JOptionPane.showMessageDialog(null, "You already own this game.");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "An error occured while adding this game.");
				return false;
			}
		}
		
		return false;
	}

}
