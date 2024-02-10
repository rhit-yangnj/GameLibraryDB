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

public class PersonalGameSelectionPanel extends GameSelectionPanel {
	
	private JButton addNoteButton;
	private JButton addReviewButton;

	public PersonalGameSelectionPanel(UserManager userManager, ConnectionManager connectionManager,
			UpdateManager updateManager) {
		super(userManager, connectionManager, updateManager);
		editGameButton = new JButton("Remove Game");
		editGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedGameName.equals("No Game Selected")) {
					JOptionPane.showMessageDialog(null, "Please select a game first.");
				} else if (removeSelectedGameFromUser()) {
					updateManager.redoSearch();
					updateManager.GameBrowserUpdate();
					JOptionPane.showMessageDialog(null, "Successfully removed game from account!");
				}
				
			}
		});
		addNoteButton = new JButton("Add Note");
		addReviewButton = new JButton("Add Review");
		addNoteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!selectedGameName.equals("No Game Selected")) {
					showAddNotePanel();
				} else {
					JOptionPane.showMessageDialog(null, "Please select a game first.");
				}
				
			}
		});
		addReviewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!selectedGameName.equals("No Game Selected")) {
					showAddReviewPanel();
				} else {
					JOptionPane.showMessageDialog(null, "Please select a game first.");
				}
			}
		});
		this.add(editGameButton);
		this.add(addNoteButton);
		this.add(addReviewButton);
	}
	
	private void showAddNotePanel() {
		final String frameTitle = "Game Library - Add Note";
		final int frameWidth = 480;
		final int frameHeight = 480;
		final int frameXLoc = 100;
		final int frameYLoc = 0;
		
		//Set up frame
		JFrame noteFrame = new JFrame();
		noteFrame.setTitle(frameTitle);
		noteFrame.setSize(frameWidth, frameHeight);
		noteFrame.setLocation(frameXLoc, frameYLoc);
		noteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		NotePanel notePanel = new NotePanel(connectionManager, userManager, selectedGameName);
		
		noteFrame.add(notePanel);
		noteFrame.setVisible(true);
	}
	
	private void showAddReviewPanel() {
		final String frameTitle = "Game Library - Add Review";
		final int frameWidth = 480;
		final int frameHeight = 480;
		final int frameXLoc = 100;
		final int frameYLoc = 0;
		
		//Set up frame
		JFrame reviewFrame = new JFrame();
		reviewFrame.setTitle(frameTitle);
		reviewFrame.setSize(frameWidth, frameHeight);
		reviewFrame.setLocation(frameXLoc, frameYLoc);
		reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ReviewPanel reviewPanel = new ReviewPanel(connectionManager, userManager, selectedGameName, updateManager);
		
		reviewFrame.add(reviewPanel);
		reviewFrame.setVisible(true);
	}
	
	private boolean removeSelectedGameFromUser() {
		Connection connection = connectionManager.getConnection();
		int returnCode = 3;
		
		try {
			CallableStatement stmt = connection.prepareCall("{? = call RemoveGameFromUser(?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, selectedGameName);
			stmt.setString(3, userManager.getUser());
			stmt.executeUpdate();
			
			returnCode = stmt.getInt(1);
			System.out.println(returnCode);
			
			if (returnCode == 1 || returnCode == 2) {
				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 5) {
				JOptionPane.showMessageDialog(null, "You do not have that game added to your library, please try again");
				return false;
			} else if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "An error occured while removing this game. Please try again");
				return false;
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(returnCode);
			if (returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode == 5) {
				JOptionPane.showMessageDialog(null, "You do not have that game added to your library, please try again");
				return false;
			} else if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "An error occured while removing this game.");
				return false;
			}
		}
		
		return false;
	}

}
