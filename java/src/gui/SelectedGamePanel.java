package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectedGamePanel extends JPanel {
	private UserManager userManager;
	private ConnectionManager connectionManager;
	private UpdateManager updateManager;
	private SearchBarPanel searchBarPanel;
	
	private JScrollPane scrollPane;
	private JTextArea gameNameTextArea;
	private JTextArea descriptionTextArea;
	private JTextArea studioTextArea;
	private JTextArea platformsTextArea;
	private JTextArea genresTextArea;
	private JButton editGameButton;
	private boolean isPersonalGames;
	
	private String selectedGameName;
	
	public SelectedGamePanel(UserManager userManager, ConnectionManager connectionManager, UpdateManager updateManager, boolean isPersonalGames, SearchBarPanel searchBarPanel) {
		this(userManager, connectionManager, updateManager, isPersonalGames);
		this.searchBarPanel = searchBarPanel;
	}
	
	public SelectedGamePanel(UserManager userManager, ConnectionManager connectionManager, UpdateManager updateManager, boolean isPersonalGames) {
		this.isPersonalGames = isPersonalGames;
		this.userManager = userManager;
		this.connectionManager = connectionManager;
		this.updateManager = updateManager;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		this.setPreferredSize(new Dimension(240,80));
		gameNameTextArea = new JTextArea(5,5);
		descriptionTextArea = new JTextArea(5,5);
		studioTextArea = new JTextArea(5,5);
		platformsTextArea = new JTextArea(5,5);
		genresTextArea = new JTextArea(5,5);
		gameNameTextArea.setEditable(false);
		descriptionTextArea.setEditable(false);
		studioTextArea.setEditable(false);
		platformsTextArea.setEditable(false);
		genresTextArea.setEditable(false);
		updateSelectedGame("No game Selected", "No game Selected", "No game Selected", "No game Selected", "No game Selected");
		if(!isPersonalGames) {
			editGameButton = new JButton("Add Game");
			editGameButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (addSelectedGameToUser()) {
						updateManager.GameUpdate();
						updateManager.GameBrowserUpdate();
						JOptionPane.showMessageDialog(null, "Successfully Added Game to account!");
					}
				
				}
			});
		} else {
			editGameButton = new JButton("Remove Game");
			editGameButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (removeSelectedGameFromUser()) {
						if (searchBarPanel != null) {	
							searchBarPanel.removeRowFromSearch(selectedGameName);
						}
						updateManager.GameUpdate();
						updateManager.GameBrowserUpdate();
						JOptionPane.showMessageDialog(null, "Successfully Removed Game from account!");
					}
					
				}
			});
		}
		
//		this.scrollPane = new JScrollPane(gameNameTextArea);
//		scrollPane.add(gameNameTextArea);
//		scrollPane.add(descriptionTextArea);
//		scrollPane.add(studioTextArea);
//		scrollPane.add(platformsTextArea);
//		scrollPane.add(genresTextArea);
//		scrollPane.add(addGameButton);
		
		gameNameTextArea.setLineWrap(true);
		descriptionTextArea.setLineWrap(true);
		studioTextArea.setLineWrap(true);
		platformsTextArea.setLineWrap(true);
		genresTextArea.setLineWrap(true);
		

		
		this.add(gameNameTextArea);
		this.add(descriptionTextArea);
		this.add(studioTextArea);
		this.add(platformsTextArea);
		this.add(genresTextArea);
		this.add(editGameButton);
//		this.add(scrollPane);
	}
	
	public void updateSelectedGame(String gameName, String description, String studio, String platforms, String genres) {
		this.gameNameTextArea.setText("Game Name: " + gameName);
		this.descriptionTextArea.setText("Description: " + description);
		this.studioTextArea.setText("Studio: " + studio);
		this.platformsTextArea.setText("Platforms: " + platforms);
		this.genresTextArea.setText("Genres" + genres);
		this.selectedGameName = gameName;
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
			
			if (returnCode == 1 || returnCode == 2) {
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
			if (returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Username is null or nonexistant, please sign in.");
				return false;
			} else if (returnCode == 1 || returnCode == 2) {
				JOptionPane.showMessageDialog(null, "Game is null or nonexistant, please select a game");
				return false;
			} else if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "An error occured while Adding this game.");
				return false;
			}
		}
		
		return false;
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
				JOptionPane.showMessageDialog(null, "An error occured while Removing this game. Please try again");
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
				JOptionPane.showMessageDialog(null, "An error occured while Removing this game.");
				return false;
			}
		}
		
		return false;
	}
	
	private void setSelectedGameName(String selectedGameName) {
		this.selectedGameName = selectedGameName;
	}
}
