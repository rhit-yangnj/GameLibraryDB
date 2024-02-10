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

public class GameSelectionPanel extends JPanel {
	protected UserManager userManager;
	protected ConnectionManager connectionManager;
	protected UpdateManager updateManager;
	
	private JScrollPane scrollPane;
	private JTextArea gameNameTextArea;
	private JTextArea descriptionTextArea;
	private JTextArea studioTextArea;
	private JTextArea platformsTextArea;
	private JTextArea genresTextArea;
	protected JButton editGameButton;
	
	protected String selectedGameName;
	
	public GameSelectionPanel(UserManager userManager, ConnectionManager connectionManager, UpdateManager updateManager) {
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
		updateSelectedGame("No Game Selected", "No Game Selected", "No Game Selected", "No Game Selected", "No Game Selected");
		
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
	
	private void setSelectedGameName(String selectedGameName) {
		this.selectedGameName = selectedGameName;
	}
}
